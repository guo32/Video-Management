package viewer;

import controller.ActorController;
import controller.FilmController;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ActorViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;

    public ActorViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void printActorList() {
        ActorController actorController = new ActorController(CONNECTION);
        System.out.println("+--------------------------------------+");
        int i = 0;
        for (Map.Entry<Integer, String> data : actorController.selectAll().entrySet()) {
            if(i != 0 && i % LIST_SIZE == 0) {
                String message = "추가 배우 목록을 확인하시겠습니까?\n[Y] 더보기 [N] 목록에서 나가기";
                String yesNo = ScannerUtil.nextLine(SCANNER, message);
                if (yesNo.equalsIgnoreCase("Y")) {

                } else {
                    break;
                }
            }
            System.out.printf("[%d] %s\n", data.getKey(), data.getValue());
            System.out.println("+--------------------------------------+");
            i++;
        }
    }

    public void showMenu(int filmId) {
        String message = "[1] 배우 추가 [2] 배우 검색 [3] 취소";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            insertActorForFilm(filmId);
        } else if (userChoice == 2) {
            searchActor();
            insertActorForFilm(filmId);
        }
    }

    private void searchActor() {
        String message = "검색할 배우의 이름(first name)을 입력해주세요.";
        String userInput = ScannerUtil.nextLine(SCANNER, message);
        ActorController actorController = new ActorController(CONNECTION);
        HashMap<Integer, String> map = actorController.selectByFirstName(userInput);
        System.out.println("+--------------------------------------+");
        System.out.println("                검색 결과");
        System.out.println("+--------------------------------------+");
        if (map.isEmpty()) {
            System.out.println(" * 해당하는 배우가 없습니다.");
            System.out.println("+--------------------------------------+");
        } else {
            for (Map.Entry<Integer, String> data : map.entrySet()) {
                System.out.printf("[%d] %s\n", data.getKey(), data.getValue());
                System.out.println("+--------------------------------------+");
            }
        }
        message = "[1] 다시 검색하기 [2] 배우 추가하기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            searchActor();
        }
    }

    private void insertActorForFilm(int filmId) {
        String message = "배우의 번호를 입력해주세요.";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        ActorController actorController = new ActorController(CONNECTION);
        while (actorController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 배우의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        FilmController filmController = new FilmController(CONNECTION);
        filmController.insertFilmActor(filmId, userChoice);

        message = "배우를 더 추가하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            printActorList();
            showMenu(filmId);
        } else {
            System.out.println("배우 추가를 종료합니다.");
        }
    }
}
