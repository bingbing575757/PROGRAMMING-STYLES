import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        ArrayList<String> list_stop = new ArrayList<>();
        ArrayList<String> freq = new ArrayList<>();
        ArrayList<Integer> count_word = new ArrayList<>();

        try (FileReader file = new FileReader(args[0]);
             Scanner reader = new Scanner(file);
             FileReader stop_words = new FileReader("../../stop_words.txt");
             Scanner stop = new Scanner(stop_words)) {

            // Populate stop words list
            while (stop.hasNext()) {
                String[] stop_list = stop.nextLine().split(",");
                for (String stopVal : stop_list) {
                    list_stop.add(stopVal);
                }
            }

            // Process main text file
            boolean stopExists;

            while (reader.hasNext()) {
                String str = reader.nextLine() + " ";
                String word = "";

                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    int asc = (int) c;
                    if ((asc >= 48 && asc <= 57) || (asc >= 65 && asc <= 90) || (asc >= 97 && asc <= 122)) {
                        word += c;
                    } else if (c == ' ' || !word.isEmpty()) {
                        word = word.toLowerCase();

                        String finalWord = word;
                        stopExists = list_stop.stream().anyMatch(stopVal -> stopVal.equalsIgnoreCase(finalWord));

                        if (!stopExists) {
                            if (freq.isEmpty() || !freq.contains(word)) {
                                if (word.length() >= 2) {
                                    freq.add(word);
                                    count_word.add(1);
                                }
                            } else {
                                int indexOfWord = freq.indexOf(word);
                                int wordCount = count_word.get(indexOfWord);
                                count_word.set(indexOfWord, wordCount + 1);
                            }
                        }
                        word = "";
                    }
                }
            }

            // Sort
            for (int j = 0; j < freq.size(); j++) {
                int max = count_word.get(j);

                for (int k = j + 1; k < freq.size(); k++) {
                    if (count_word.get(k) > max) {
                        max = count_word.get(k);
                    }
                }

                int temp = count_word.get(j);
                String tempWord = freq.get(j);
                int max_index = count_word.indexOf(max);
                freq.set(j, freq.get(max_index));
                count_word.set(j, max);
                freq.set(max_index, tempWord);
                count_word.set(max_index, temp);
            }

            // print the sorted frequency list


            for (int i = 0; i < freq.size() && i < 25; i++) {
                System.out.println(freq.get(i) + " - " + count_word.get(i));
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nMessage:\n\nPlease enter the path to the .txt files in the command line ");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
