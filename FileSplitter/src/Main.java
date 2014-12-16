import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 */
public class Main {

    static ArrayList<StringBuilder> outStrings = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        System.out.println((int)'\n');
//        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ " +
//                "abcdefghijklmnopqrstuvwxyz " +
//                "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ " +
//                "абвгдеёжзийклмнопрстуфхчцшщъыьэюя " +
//                "0123456789";
//        for (char c : s.toCharArray()) {
//            System.out.println((int) c + " " + c);
//        }
//        System.out.println("===================");
//        for (int i = 0; i < 49; i++) {
//            System.out.println((char) i + " " + i);
//        }
//        System.out.println("===================");
//        for (int i = 57; i < 66; i++) {
//            System.out.println((char) i + " " + i);
//        }
//        System.out.println("===================");
//        for (int i = 90; i < 98; i++) {
//            System.out.println((char) i + " " + i);
//        }
////        System.out.println("===================");
////        for (int i = 124; i < 1041; i++) {
////            System.out.println((char) i + " " + i);
////        }
//        System.out.println("===================");
//        for (int i = 1103; i < 1150; i++) {
//            System.out.println((char) i + " " + i);
//        }

        Scanner sc = new Scanner(
                new FileInputStream(new File("P:\\GitHome\\dummy-jetty\\FileSplitter\\res\\uliss18_ru" + ".txt")));
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.nextLine());
        }
        int length = sb.length();
        final int file_size = 5000;
        for (int i = 0; sb.length() > 0; i++) {
            int index = file_size;
            while (index < sb.length() && sb.charAt(index) != ' ') {
                index++;
            }
            if (index >= sb.length()) {
                index = sb.length();
            }
            outStrings.add(new StringBuilder(sb.substring(0, index).toLowerCase()));
            sb.delete(0, index + 1);
        }
        removeShit();
        writeFiles();
    }

    private static void writeFiles() throws IOException {
        int i = 0;
        for (StringBuilder sb : outStrings) {
            FileWriter fw = new FileWriter(new File("uliss18_" + i++));
            fw.write(sb.toString());
            fw.flush();
            fw.close();
        }
    }

    private static void removeShit() {
        for (StringBuilder s : outStrings) {
            for (int i = 0; i < s.length(); i++) {
                boolean deleted = false;
                char c = s.charAt(i);
                if (c == '[') {
                    int startNote = i;
                    int endNote = i + 1;
                    while (s.charAt(endNote) != ']') {
                        endNote++;
                    }
                    s.delete(startNote, endNote+1);
                    deleted = true;
                }
                int ch = (int) c;
                if (!checkChar(ch)) {
                    s.deleteCharAt(i);
                    deleted = true;
                }
                if (deleted) i--;
            }
        }
    }

    private static boolean checkChar(int ch) {
        if (ch == 32) { // space
            return true;
        } else if (ch > 47 && ch < 58) { // digits
            return true;
//        } else if (ch > 64 && ch < 91) { // A..Z
//            return true;
        } else if (ch > 96 && ch < 123) { // a..z
            return true;
//        } else if (ch > 1039 && ch < 1072) { // А..Я
//            return true;
        } else if (ch > 1071 && ch < 1104) { // а..я
            return true;
        } else if (ch > 191 && ch < 447 && ch != 247 && ch != 215) { //umlauts and other shit without × and ÷
            return true;
        } else if (ch > 944 && ch < 970) { //greek
            return true;
        } else if (ch == 1105) { //ё
            return true;
        } else { return false; }

    }
}
