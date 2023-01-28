package util;

import java.util.Scanner;

public class ScannerUtil {
    public static int nextInt(Scanner scanner, String message) {
        String data = nextLine(scanner, message);
        while (!data.matches("\\d+")) {
            System.out.println("잘못 입력하셨습니다.");
            data = nextLine(scanner, message);
        }

        return Integer.parseInt(data);
    }

    public static int nextInt(Scanner scanner, String message, int min, int max) {
        int data = nextInt(scanner, message);
        while (data < min || data > max) {
            System.out.println("잘못 입력하셨습니다.");
            data = nextInt(scanner, message);
        }

        return data;
    }

    public static String nextLine(Scanner scanner, String message) {
        printMessage(message);
        String data = scanner.nextLine();
        if (data.isEmpty()) {
            data = scanner.nextLine();
        }

        return data;
    }

    // message 출력
    public static void printMessage(String message) {
        System.out.println(message);
        System.out.print("> ");
    }
}
