package viewer;

import controller.FilmController;
import controller.LanguageController;
import model.FilmDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.time.Year;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class FilmViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;

    public FilmViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void showMenu() {
        String message = "[1] 영화 등록 [2] 영화 목록 및 관리 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            insertFilm();
            showMenu();
        } else if (userChoice == 2) {
            printFilmList();
            showMenu();
        }
    }

    private void insertFilm() {
        FilmDTO newFilm = new FilmDTO();
        String message = "영화의 제목을 입력해주세요.";
        newFilm.setTitle(ScannerUtil.nextLine(SCANNER, message));

        message = "영화의 줄거리를 입력해주세요.";
        newFilm.setDescription(ScannerUtil.nextLine(SCANNER, message));

        message = "영화의 개봉 연도를 입력해주세요. (1800~2023)";
        newFilm.setReleaseYear(Year.of(ScannerUtil.nextInt(SCANNER, message, 1800, 2023)));

        message = "영화의 지원 언어 번호를 입력해주세요.";
        LanguageController languageController = new LanguageController(CONNECTION);
        for (Map.Entry<Integer, String> data : languageController.selectAll().entrySet()) {
            System.out.printf("[%d] %s\n", data.getKey(), data.getValue());
        }
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, languageController.selectAll().size());
        while (!languageController.validateId(userChoice)) {
            System.out.println("유효하지 않은 언어의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message, 1, languageController.selectAll().size());
        }
        newFilm.setLanguageId(userChoice);

        message = "영화의 최대 대여 기간을 입력해주세요. (1~10)";
        newFilm.setRentalDuration(ScannerUtil.nextInt(SCANNER, message, 1, 10));

        message = "영화의 대여 가격을 입력해주세요. (ex 1.99)";
        newFilm.setRentalRate(ScannerUtil.nextDouble(SCANNER, message, 4));

        message = "영화의 길이를 입력해주세요. (1~300)";
        newFilm.setLength(ScannerUtil.nextInt(SCANNER, message, 1, 300));

        message = "영화의 교체 비용을 입력해주세요. (ex 11.99)";
        newFilm.setReplacementCost(ScannerUtil.nextDouble(SCANNER, message, 5));

        message = "영화의 등급을 선택해주세요.\n";
        message += "[1] G [2] PG [3] PG-13 [4] R [5] NC-17";
        String rating = "";
        userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
        if(userChoice == 1) rating = "G";
        else if(userChoice == 2) rating = "PG";
        else if(userChoice == 3) rating = "PG-13";
        else if(userChoice == 4) rating = "R";
        else if(userChoice == 5) rating = "NC-17";
        newFilm.setRating(rating);

        String specialFeatures = "";
        message = "Trailers 여부 [1] O [2] X";
        userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            specialFeatures = "Trailers";
        }
        message = "Commentaries 여부 [1] O [2] X";
        userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            if (!specialFeatures.equals("")) {
                specialFeatures += ",";
            }
            specialFeatures += "Commentaries";
        }
        message = "Deleted Scenes 여부 [1] O [2] X";
        userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            if (!specialFeatures.equals("")) {
                specialFeatures += ",";
            }
            specialFeatures += "Deleted Scenes";
        }
        message = "Behind the Scenes 여부 [1] O [2] X";
        userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            if (!specialFeatures.equals("")) {
                specialFeatures += ",";
            }
            specialFeatures += "Behind the Scenes";
        }
        newFilm.setSpecialFeatures(specialFeatures);

        FilmController filmController = new FilmController(CONNECTION);
        int filmId = filmController.insert(newFilm);

        if (filmId != 0) {
            System.out.println("영화가 정상적으로 등록되었습니다.");
            message = "출연진을 추가하시겠습니까?\n[Y] 예 [N] 아니오";
            String yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                ActorViewer actorViewer = new ActorViewer(CONNECTION, SCANNER);
                actorViewer.printActorList();
                actorViewer.showMenu(filmId);
            }
        } else {
            System.out.println("영화 등록에 실패하였습니다.");
        }
    }

    public void printFilmList() {
        FilmController filmController = new FilmController(CONNECTION);
        ArrayList<FilmDTO> list = filmController.selectAll();

        System.out.println("+---------------------------------------------------------+");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && i % LIST_SIZE == 0) {
                String message = "[1] 영화 상세보기 [2] 영화 검색 [3] 이전 목록 [4] 다음 목록 [5] 뒤로 가기";
                int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
                if (userChoice == 1) {
                    selectFilm();
                    i -= LIST_SIZE;
                } else if (userChoice == 2) {
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

    private void selectFilm() {
        String message = "상세보기할 영화의 번호를 선택해주세요. [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);

        FilmController filmController = new FilmController(CONNECTION);
        while (userChoice != 0 && filmController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 영화의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        printFilmInfo(userChoice);
    }

    private void printFilmInfo(int filmId) {
        FilmController filmController = new FilmController(CONNECTION);
        FilmDTO filmDTO = filmController.selectById(filmId);
        System.out.println("+===================================================+");
        System.out.println("                     영화 정보");
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [번호] " + filmDTO.getFilmId());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [제목] " + filmDTO.getTitle() + " | [등급] " + filmDTO.getRating());
        System.out.println("+---------------------------------------------------+");
        System.out.print(" [내용] ");
        String temp = filmDTO.getDescription();
        int i = 0;
        for (char c : temp.toCharArray()) {
            if(i % 45 == 0) System.out.print("\n ");
            System.out.print(c);
            i++;
        }
        System.out.println();
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [개봉 연도] " + filmDTO.getReleaseYear() + "년 | [언어] " + filmDTO.getLanguageId());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [영화 길이] " + filmDTO.getLength() + "분 | [교체 비용] " + filmDTO.getReplacementCost());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [특이 사항] " + filmDTO.getSpecialFeatures());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [출연진]");
        for (String name : filmController.selectActorListById(filmId)) {
            System.out.println(" " + name);
        }
        System.out.println("+===================================================+");
        String message = "[0] 뒤로 가기";
        ScannerUtil.nextInt(SCANNER, message, 0, 0);
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
