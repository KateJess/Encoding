import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        System.out.println("\nУкажите операцию, которую необходимо выполнить, " +
                "путь к файлу для преобразования и алгоритм " +
                "\n-c - архивация\n-d - разархивация\nrle - алгоритм rle\nlzw - алгоритм lzw" +
                "\nНапример:\n-c toCompress.txt rle - для архивации файла алгоритмом rle" +
                "\nИли:\n-d toDecompress.txt.arh rle - для разархивации файла алгоритмом rle");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        String[] list = input.split("\s+");
        assert list.length == 3;

        RunLengthEncoding rle = new RunLengthEncoding();
        LempelZivWelch lzw = new LempelZivWelch();

        File inputFile = new File(list[1]);

        try {
            String content = readFile(inputFile); // получаем содержимое файла
            if (list[0].equals("-c") && list[2].equals("rle")) {
                String compressResult = rle.compress(content); // архивируем
                createFile(inputFile, compressResult, "-c"); // создаем архивированный файл
            }
            else if (list[0].equals("-d") && list[2].equals("rle")) {
                String decompressResult = rle.decompress(content); // извлекаем
                createFile(inputFile, decompressResult, "-d"); // создаем разархивированный файл
            }
            else if (list[0].equals("-c") && list[2].equals("lzw")) {
                List<Integer> numbers = lzw.compress(content);
                StringBuilder compressResult = new StringBuilder();
                for (Integer integer : numbers) {
                    compressResult.append(integer);
                    compressResult.append("\s");
                }
                createFile(inputFile, compressResult.toString(), "-c");
            }
            else if (list[0].equals("-d") && list[2].equals("lzw")) {
                List<String> lines = Files.readAllLines(inputFile.toPath());
                String strings = String.join("\s", lines);
                List<String> array = Arrays.stream(strings.split("\\s")).toList();
                List<Integer> integerList = array.stream().map(Integer:: parseInt).collect(Collectors.toList());
                String decompressResult = lzw.decompress(integerList);
                createFile(inputFile, decompressResult, "-d");
            } else {
                System.out.println("Неверно введена операция. Попробуйте снова");
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
    private static String readFile(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        return String.join(System.lineSeparator(), lines);
    }
    private static void createFile(File originalFile, String content, String operation) {
        try {
            String filePath = originalFile.getPath();

            if (operation.equals("-c")) { // если после архивации, то просто добавляем расширение
                filePath += ".arh";
            }
            else if (operation.equals("-d")) { // если после разархивации, то вызываем метод для создания нового имени
                filePath = changeDecompressName(originalFile); // и присваиваем новое имя
            }
            File newFile = new File(filePath);
            FileWriter fileWriter = new FileWriter(newFile);
            fileWriter.write(content);
            fileWriter.close();
            System.out.println("Файл " + newFile.getName() + " создан");

        } catch (IOException e) {
            System.out.println("Ошибка при создании файла");
            e.printStackTrace();
        }
    }
    // метод создания нового имени файла после его разархивации
    private static String changeDecompressName(File file) throws IOException {
        String filePath = file.getPath().replace(".arh", ""); // убираем расширение
        String[] fileNameParts = filePath.split("\\."); // разбиваем строку на массив, в качестве разделителя используем "."
        File decryptedFile = new File(filePath); // создаем объект файл с полученным именем

        if (!decryptedFile.createNewFile()) { // проверяем, существует ли файл с таким именем
            // если да, возвращаем строку с добавленным индексом
            return fileNameParts[0] + "(1)." + fileNameParts[1];
        }
        // если не существует, возвращаем без индекса
        return filePath;
    }
}