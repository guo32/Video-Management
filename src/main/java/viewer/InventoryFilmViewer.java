package viewer;

import controller.InventoryFilmController;
import model.CustomerDTO;
import model.InventoryFilmDTO;
import model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryFilmViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;

    public InventoryFilmViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void printListByStoreId(int storeId) {
        InventoryFilmController inventoryFilmController = new InventoryFilmController(CONNECTION);
        ArrayList<InventoryFilmDTO> list = inventoryFilmController.selectByStore(storeId);

        System.out.println("+---------------------------------------------------------+");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && i % LIST_SIZE == 0) {
                String message = "[1] 재고 선택 [2] 재고 검색 [3] 이전 목록 [4] 다음 목록 [5] 뒤로 가기";
                int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
                if (userChoice == 1) {
                    //selectInventory();
                    i -= LIST_SIZE;
                } else if (userChoice == 2) {
                    searchInventory(storeId);
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
            System.out.printf(" [%d] %s | %.2f\n", list.get(i).getInventoryId(), list.get(i).getTitle(), list.get(i).getRentalRate());
            System.out.println("+---------------------------------------------------------+");
        }
    }

    private void searchInventory(int storeId) {
        String message = "검색할 재고의 영화 제목을 입력해주세요.";
        String userInput = ScannerUtil.nextLine(SCANNER, message);
        InventoryFilmController inventoryFilmController = new InventoryFilmController(CONNECTION);
        ArrayList<InventoryFilmDTO> list = inventoryFilmController.selectByFilmTitle(userInput, storeId);

        System.out.println("+---------------------------------------------------------+");
        System.out.println("                         검색 결과");
        System.out.println("+---------------------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 해당하는 재고가 존재하지 않습니다.");
            System.out.println("+---------------------------------------------------------+");
            message = "[1] 다시 검색하기 [2] 목록으로 돌아가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
            if (userChoice == 1) {
                searchInventory(storeId);
            }
        } else {
            for (InventoryFilmDTO i : list) {
                System.out.printf(" [%d] %s | %.2f\n", i.getInventoryId(), i.getTitle(), i.getRentalRate());
                System.out.println("+---------------------------------------------------------+");
            }
            message = "[1] 재고 선택 [2] 목록으로 돌아가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
            if (userChoice == 1) {
                message = "상세보기할 재고의 번호를 입력해주세요.";
                userChoice = ScannerUtil.nextInt(SCANNER, message);
                while (!validateInventoryIdInList(list, userChoice)) {
                    System.out.println("현재 검색 리스트에 존재하지 않는 재고의 번호입니다.");
                    userChoice = ScannerUtil.nextInt(SCANNER, message);
                }
                printInventoryFilmInfo(userChoice);
            }
        }
    }

    private boolean validateInventoryIdInList(ArrayList<InventoryFilmDTO> list, int inventoryId) {
        for (InventoryFilmDTO i : list) {
            if (i.getInventoryId() == inventoryId) {
                return true;
            }
        }
        return false;
    }

    private void printInventoryFilmInfo(int inventoryId) {
        InventoryFilmController inventoryFilmController = new InventoryFilmController(CONNECTION);
        InventoryFilmDTO inventoryFilmDTO = inventoryFilmController.selectById(inventoryId);

        System.out.println("+===================================================+");
        System.out.println("                     재고 정보");
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [번호] " + inventoryFilmDTO.getInventoryId() + " | [보유 대여점] " + inventoryFilmDTO.getStoreId() + "번 대여점");
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [제목] " + inventoryFilmDTO.getTitle() + " | [등급] " + inventoryFilmDTO.getRating());
        System.out.println("+---------------------------------------------------+");
        System.out.print(" [내용] ");
        String temp = inventoryFilmDTO.getDescription();
        int i = 0;
        for (char c : temp.toCharArray()) {
            if(i % 45 == 0) System.out.print("\n ");
            System.out.print(c);
            i++;
        }
        System.out.println();
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [개봉 연도] " + inventoryFilmDTO.getReleaseYear() + "년 | [언어] " + inventoryFilmDTO.getLanguage());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [대여 기간] " + inventoryFilmDTO.getRentalDuration() + "일 | [대여 가격] " + inventoryFilmDTO.getRentalRate());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [영화 길이] " + inventoryFilmDTO.getLength() + "분 | [교체 비용] " + inventoryFilmDTO.getReplacementCost());
        System.out.println("+---------------------------------------------------+");
        System.out.println(" [특이 사항] " + inventoryFilmDTO.getSpecialFeatures());
        System.out.println("+===================================================+");

        String message = "[1] 수정 [2] 삭제 [3] 전체 목록으로";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            // 수정
        } else if (userChoice == 2) {
            // 삭제
        }
    }
}
