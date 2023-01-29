package viewer;

import controller.CustomerController;
import model.CustomerDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;

    public CustomerViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public void showMenu() {
        String message = "[1] 회원 등록 [2] 회원 목록 및 검색 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {

        } else if (userChoice == 2) {
            printCustomerList();
        }
    }

    private void printCustomerList() {
        CustomerController customerController = new CustomerController(CONNECTION);
        ArrayList<CustomerDTO> list = customerController.selectAll();
        System.out.println("+---------------------------------------------------------+");
        for (int i = 0; i < list.size(); i++) {
            if (i != 0 && i % LIST_SIZE == 0) {
                String message = "[1] 회원 선택 [2] 회원 검색 [3] 이전 목록 [4] 다음 목록 [5] 뒤로 가기";
                int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);
                if (userChoice == 1) {

                } else if (userChoice == 2) {

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
            System.out.printf(" [%d] %s %s\n", list.get(i).getCustomerId(), list.get(i).getFirstName(), list.get(i).getLastName());
            System.out.println("+---------------------------------------------------------+");
        }
    }
}
