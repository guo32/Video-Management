package viewer;

import controller.FilmController;
import controller.InventoryFilmController;
import model.FilmDTO;
import model.InventoryFilmDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class FilmViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;

    public FilmViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void insertFilm() {

    }

    public void printFilmList() {
        FilmController filmController = new FilmController(CONNECTION);
        ArrayList<FilmDTO> list = filmController.selectAll();

        System.out.println("+---------------------------------------------------------+");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && i % LIST_SIZE == 0) {
                String message = "[2] 영화 검색 [3] 이전 목록 [4] 다음 목록 [5] 재고 등록 계속하기";
                int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
                if (userChoice == 2) {
                    searchFilm();
                    i -= LIST_SIZE;
                } else if (userChoice == 3) {
                    if (i - LIST_SIZE != 0) {
                        i -= LIST_SIZE * 2;
                    } else {
                        i -= LIST_SIZE;
                    }
                } else if (userChoice == 5) {
                    break;
                }
            }
            System.out.printf(" [%d] %s(%s)\n", list.get(i).getFilmId(), list.get(i).getTitle(), list.get(i).getReleaseYear().toString());
            System.out.println("+---------------------------------------------------------+");
        }
    }

    public int choiceFilmForInventory() {
        FilmController filmController = new FilmController(CONNECTION);
        String message = "재고로 등록할 영화의 번호를 입력해주세요.";
        int filmId = ScannerUtil.nextInt(SCANNER, message);
        while (filmController.selectById(filmId) == null) {
            System.out.println("존재하지 않는 영화의 번호입니다.");
            filmId = ScannerUtil.nextInt(SCANNER, message);
        }
        return filmId;
    }

    private void searchFilm() {
        String message = "검색할 영화 제목을 입력해주세요.";
        String userInput = ScannerUtil.nextLine(SCANNER, message);
        FilmController filmController = new FilmController(CONNECTION);
        ArrayList<FilmDTO> list = filmController.selectByTitle(userInput);

        System.out.println("+---------------------------------------------------------+");
        System.out.println("                         검색 결과");
        System.out.println("+---------------------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 해당하는 영화가 존재하지 않습니다.");
            System.out.println("+---------------------------------------------------------+");
        } else {
            for (FilmDTO i : list) {
                System.out.printf(" [%d] %s(%s)\n", i.getFilmId(), i.getTitle(), i.getReleaseYear().toString());
                System.out.println("+---------------------------------------------------------+");
            }
        }
        message = "[1] 다시 검색하기 [2] 목록으로 돌아가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            searchFilm();
        }
    }
}
