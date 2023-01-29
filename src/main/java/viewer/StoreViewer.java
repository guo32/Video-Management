package viewer;

import controller.StoreController;
import model.StoreDTO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class StoreViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;

    public StoreViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void printStoreList() {
        StoreController storeController = new StoreController(CONNECTION);
        ArrayList<StoreDTO> list = storeController.selectAll();
        System.out.println("+--------------------------------------+");
        for (StoreDTO s : list) {
            System.out.printf(" [%d] %s\n", s.getStoreId(), s.getAddress());
            System.out.println("+--------------------------------------+");
        }
    }
}
