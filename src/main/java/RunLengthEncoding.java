import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunLengthEncoding {

    public RunLengthEncoding() {
    }
    // метод архивации строки
    public String compress(String toCompress) {
        Pattern pattern = Pattern.compile("(\\D)\1*"); // одна или более одинаковых букв
        Matcher matcher = pattern.matcher(toCompress); // класс, который реализует поиск по шаблону
        StringBuilder encoded = new StringBuilder();
        int count = 0;
        while (matcher.find()) { // пока строка не кончилась
            if (matcher.group().length() > 2) {
                // если повторяющихся букв больше 2, то пишем их кол-во и первый символ в группе (т.е. просто саму букву)
                encoded.append(matcher.group().length());
                encoded.append(matcher.group().charAt(0));
            }
            else if (matcher.group().length() == 2) {
                // если буква повторяется два раза, кол-во повторов не пишем, оставляем как есть
                encoded.append(matcher.group().charAt(0));
                encoded.append(matcher.group().charAt(1));
                // count вводится для избежания дублирования кода и дальнейшей проверки на повторы,которые не превышают 2
                count++;
            }
            else if (matcher.group().length() == 1) {
                // если буква не повторяются, просто записываем
                encoded.append(matcher.group().charAt(0));
            }
        }
//        проверяем, есть ли в строке хоть одна цифра
//        т. е. смотрим, повторялась ли хоть одна буква,
//        а также проверяем, есть ли в строке буквы, которые повторяются 2 раза

        if (!encoded.toString().chars().anyMatch(Character::isDigit) && count == 0) {
            StringBuilder length = new StringBuilder();
            length.append("-");
            length.append(toCompress.length());
            length.append("\s");
            length.append(toCompress);
            return length.toString();
        }
        return encoded.toString();
    }
    // метод разархивации строки
    public String decompress(String toDecompress) {
        if (toDecompress.matches("\\-(\\d)+\\s(\\D)+")) {
            return toDecompress.replaceAll("\\-(\\d+)\\s", "");
        }
        Pattern pattern = Pattern.compile("(\\d*)(\\D)\1*"); // ноль или более цифр и одинаковые буквы
        Matcher matcher = pattern.matcher(toDecompress);
        StringBuilder decoded = new StringBuilder();
        int count;
        while (matcher.find()) {
            if (matcher.group(1).matches("\\d+")) { // если присутствуют цифры
                count = Integer.parseInt(matcher.group(1));
                decoded.append(matcher.group(2).repeat(count));
            }
            else { // если только буква
                decoded.append(matcher.group());
            }
        }
        return decoded.toString();
    }
}