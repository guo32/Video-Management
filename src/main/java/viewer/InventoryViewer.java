package viewer;

import controller.InventoryController;
import model.InventoryDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.Scanner;

public class InventoryViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;

    public InventoryViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void showMenu(int storeId) {
        String message = "[1] 입고 [2] 재고 목록 및 검색 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            // 입고
            insertInventory(storeId);
        } else if (userChoice == 2) {
            InventoryFilmViewer inventoryFilmViewer = new InventoryFilmViewer(CONNECTION, SCANNER);
            inventoryFilmViewer.printListByStoreId(storeId);
        }
    }

    private void insertInventory(int storeId) {
        InventoryDTO newInventory = new InventoryDTO();
        newInventory.setStoreId(storeId);

        System.out.println("재고로 등록 가능한 영화의 목록입니다.");
        FilmViewer filmViewer = new FilmViewer(CONNECTION, SCANNER);
        filmViewer.printFilmList();
        newInventory.setFilmId(filmViewer.choiceFilmForInventory());

        InventoryController inventoryController = new InventoryController(CONNECTION);
        inventoryController.insert(newInventory);

        System.out.println("정상적으로 등록되었습니다.");
    }
}
