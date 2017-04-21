package utils;

import java.util.Scanner;

public class IOUtils {
    static final String NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private static final int longitude = 8;

    private static Scanner scan = new Scanner(System.in);

    public static String enterToContinue() {
        printLine("\nPress enter to continue...");
        return scan.nextLine();
    }

    public static String askForString(String text) {
        printQuestion(text);
        return scan.nextLine();
    }

    public static Integer askForIntInRange(String text, int min, int max) {
        printQuestion(text);
        while (true) {
            String strResponse = scan.nextLine();
            if (strResponse.matches(NUMBER_REGEX)) {
                int response = Integer.parseInt(strResponse);
                if (response >= min && response <= max) {
                    return response;
                }
                System.out.println("Input out of range, try it again.");
            } else {
                System.out.println("Invalid input, try it again.");
                printQuestion(text);
            }
        }
    }

    private static void printQuestion(String text) {
        System.out.print(text + ": ");
    }

    public static void printLine(String s) {
        System.out.println(s);
    }

    public static void printContent(Object o) {
        System.out.println(o);
    }

    public static void drawLine() {
        for (int i = 0; i < longitude; i++) {
            System.out.print('-');
        }
        System.out.println();
    }

    public static Integer askForInt(String s) {
        printQuestion(s);
        while (true) {
            String strResponse = scan.nextLine();
            if (strResponse.matches(NUMBER_REGEX)) {
                return Integer.parseInt(strResponse);
            } else {
                System.out.println("Invalid input, try it again.");
                printQuestion(s);
            }
        }
    }
}
