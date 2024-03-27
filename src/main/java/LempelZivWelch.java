import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LempelZivWelch {
    public LempelZivWelch(){}
    // метод архивации
    public List<Integer> compress(String toCompress) {
        // создаем словарь
        int dictionarySize = 256;
        Map<String,Integer> dictionary = new HashMap();
        for (int i = 0; i < dictionarySize; i++) dictionary.put("" + (char) i, i);

        String line = "";

        List<Integer> result = new ArrayList();
        for (char character : toCompress.toCharArray()) {
            String converted = line + character;
            if (dictionary.containsKey(converted))
                line = converted;
            else {
                result.add(dictionary.get(line));
                dictionary.put(converted, dictionarySize++);
                line = "" + character;
            }
        }
        if (!line.equals(""))
            result.add(dictionary.get(line));
        return result;
    }

    // метод разархивации
    public String decompress(List<Integer> toDecompress) {
        int dictionarySize = 256;
        Map<Integer,String> dictionary = new HashMap<>();
        for (int i = 0; i < dictionarySize; i++)
            dictionary.put(i, "" + (char)i);

        String line = "" + (char)(int)toDecompress.remove(0);
        StringBuffer result = new StringBuffer(line);
        for (int k : toDecompress) {
            String entry;
            if (dictionary.containsKey(k))
                entry = dictionary.get(k);
            else if (k == dictionarySize)
                entry = line + line.charAt(0);
            else
                throw new IllegalArgumentException();

            result.append(entry);

            dictionary.put(dictionarySize++, line + entry.charAt(0));

            line = entry;
        }
        return result.toString();
    }
}