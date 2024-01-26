import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main2 {

    private static Path path;
    private static final Set<String> stopWords = new HashSet<>();
    private final static Map<String, Integer> frequencyMap = new HashMap<>();
    private static List<Map.Entry<String, Integer>> descendingList;

    private static void validateArguments(String[] args) {
        if (args.length != 1) {
            System.err.println("Please provide exactly ONE argument. Current: " + args.length);
            System.exit(1);
        }

        path = Path.of(args[0]);
        if (!path.toFile().exists()) {
            System.err.println(path + " does not exist.");
            System.exit(1);
        }
    }

    private static void StopWords() {
        // load stop words
        final String PATH_STOP_WORDS = "../../stop_words.txt";
        try {
            stopWords.addAll(Arrays.asList(new String(Files.readAllBytes(Path.of(PATH_STOP_WORDS))).split(",")));
        } catch (IOException e) {
            handleError("Error reading stop_words.txt");
        }
    }

    private static void Frequencies() {
        try {
            Files.lines(path)
                    .forEach(line -> Arrays.stream(line.split("[^a-zA-Z]+"))
                            .map(String::toLowerCase)
                            .filter(w -> !stopWords.contains(w) && w.length() > 1)
                            .forEach(w -> frequencyMap.merge(w, 1, Integer::sum)));
        } catch (IOException e) {
            handleError("Error reading file: " + path);
        }
    }

    private static void sort() {
        descendingList = new ArrayList<>(frequencyMap.entrySet());
        descendingList.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(entry -> entry.getValue()).reversed());

    }

    private static void print() {
        descendingList.stream()
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + "  -  " + entry.getValue()));
    }

    private static void handleError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public static void main(String[] args) {
        validateArguments(args);
        StopWords();
        Frequencies();
        sort();
        print();
    }
}
