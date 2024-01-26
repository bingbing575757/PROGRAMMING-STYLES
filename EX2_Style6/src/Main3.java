import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main3 {

    public static Scanner readFile(String args) {
        try {
            File file = new File(args);
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Please re-enter the .txt file in the command line.");
            return null;
        }
    }

    public static Scanner getStopWordsScanner() {
        try {
            File stopwords = new File("../../stop_words.txt");
            return new Scanner(stopwords);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find stop_words.txt.");
            return null;
        }
    }

    public static HashMap<String, Integer> processWord(Scanner reader) {
        HashMap<String, Integer> wordcheck = new HashMap<>();

        reader.forEachRemaining(line -> {
            String[] list_of_words = line.split("[^a-zA-Z0-9]+");
            Arrays.stream(list_of_words)
                    .filter(word -> word.length() >= 2)
                    .map(String::toLowerCase)
                    .forEach(word -> wordcheck.merge(word, 1, Integer::sum));
        });

        return wordcheck;
    }

    public static HashMap<String, Integer> removeStopWords(HashMap<String, Integer> listwords) {
        try (Scanner stop = getStopWordsScanner()) {
            stop.forEachRemaining(line -> Arrays.stream(line.split(","))
                    .forEach(stopword -> listwords.remove(stopword.trim())));
        }
        return listwords;
    }

    public static List<Map.Entry<String, Integer>> sort(HashMap<String, Integer> wordcheck) {
        return wordcheck.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public static void print(List<Map.Entry<String, Integer>> listwords) {

        int count = 0;
        for (Map.Entry<String, Integer> val : listwords) {
            if (count < 25) {
                System.out.println(val.getKey() + " - " + val.getValue());
                count++;
            }
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        print(sort(removeStopWords(processWord(readFile(args[0])))));
    }
}
