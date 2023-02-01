package viewer;

import controller.CustomerController;
import controller.InventoryController;
import controller.InventoryFilmController;
import controller.RentalController;
import model.CustomerDTO;
import model.RentalDTO;
import model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class RentalViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;
    private StaffDTO login;

    public RentalViewer(Connection connection, Scanner scanner, StaffDTO login) {
        CONNECTION = connection;
        SCANNER = scanner;
        this.login = login;
    }

    public void showMenu() {
        String message = "[1] 비디오 대여 [2] 대여 목록 및 검색 [3] 비디오 반납 [4] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);
        if (userChoice == 1) {
            insertRental();
            showMenu();
        } else if (userChoice == 2) {
            RentalController rentalController = new RentalController(CONNECTION);
            ArrayList<RentalDTO> list = rentalController.selectAll();
            printRentalList(list, false);
            showMenu();
        } else if (userChoice == 3) {
            returnRental();
            showMenu();
        }
    }

    private void returnRental() {
        RentalController rentalController = new RentalController(CONNECTION);
        printRentalList(rentalController.selectByReturnDateIsNull(), false);
    }

    private void insertRental() {
        RentalDTO newRental = new RentalDTO();
        newRental.setStaffId(login.getStaffId());

        CustomerViewer customerViewer = new CustomerViewer(CONNECTION, SCANNER, login);
        customerViewer.printCustomerList();
        String message = "대여하는 회원의 번호를 입력해주세요.";
        int customerId = ScannerUtil.nextInt(SCANNER, message);
        CustomerController customerController = new CustomerController(CONNECTION);
        while (customerController.selectById(customerId) == null) {
            System.out.println("존재하지 않는 회원의 번호입니다.");
            customerId = ScannerUtil.nextInt(SCANNER, message);
        }

        message = "대여할 비디오의 번호를 입력해주세요.";
        int inventoryId = ScannerUtil.nextInt(SCANNER, message);
        InventoryController inventoryController = new InventoryController(CONNECTION);
        while (inventoryController.selectById(inventoryId) == null || inventoryController.validateInventoryId(inventoryId)) {
            System.out.println("존재하지 않거나 대여 중인 비디오의 번호입니다.");
            inventoryId = ScannerUtil.nextInt(SCANNER, message);
        }
        newRental.setCustomerId(customerId);
        newRental.setInventoryId(inventoryId);

        RentalController rentalController = new RentalController(CONNECTION);
        rentalController.insert(newRental);

        System.out.println("정상적으로 대여 처리가 완료되었습니다.");
    }

    public void printRentalList(ArrayList<RentalDTO> list, boolean search) {
        System.out.println("+---------------------------------------------------------+");
        InventoryFilmController inventoryFilmController = new InventoryFilmController(CONNECTION);
        if (search) {
            for (RentalDTO r : list) {
                String title = inventoryFilmController.selectById(r.getInventoryId()).getTitle();
                System.out.printf(" [%d] %s | %s", r.getRentalId(), title, r.getRentalDate());
                if (r.getReturnDate() == null) {
                    System.out.println("(미반납)");
                } else {
                    System.out.println("(반납)");
                }
                System.out.println("+---------------------------------------------------------+");
            }
            String message = "[1] 대여 정보 선택 [2] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message);
            if (userChoice == 1) {
                selectRental();
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (i != 0 && i % LIST_SIZE == 0) {
                    String message = "[1] 대여 정보 선택 [2] 대여 정보 검색 [3] 이전 목록 [4] 다음 목록 [5] 뒤로 가기";
                    int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 5);

                    if (userChoice == 1) {
                        selectRental();
                        i -= (LIST_SIZE + 1);
                        continue;
                    } else if (userChoice == 2) {
                        searchRental();
                        i -= (LIST_SIZE + 1);
                        continue;
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
                String title = inventoryFilmController.selectById(list.get(i).getInventoryId()).getTitle();
                System.out.printf(" [%d] %s | %s", list.get(i).getRentalId(), title, list.get(i).getRentalDate());
                if (list.get(i).getReturnDate() == null) {
                    System.out.println("(미반납)");
                } else {
                    System.out.println("(반납)");
                }
                System.out.println("+---------------------------------------------------------+");
                if (i == list.size() - 1) {
                    System.out.println("마지막 페이지입니다.");
                    i -= (list.size() % LIST_SIZE);
                }
            }
        }
    }

    private void searchRental() {
        String message = "검색할 회원의 번호를 입력해주세요.";
        int customerId = ScannerUtil.nextInt(SCANNER, message);

        CustomerController customerController = new CustomerController(CONNECTION);
        while (customerController.selectById(customerId) == null) {
            System.out.println("존재하지 않는 회원의 번호입니다.");
            customerId = ScannerUtil.nextInt(SCANNER, message);
        }
        RentalController rentalController = new RentalController(CONNECTION);
        printRentalList(rentalController.selectByCustomerId(customerId, login.getStaffId()), true);
    }

    private void selectRental() {
        String message = "상세보기할 대여 정보의 번호를 입력해주세요. [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        RentalController rentalController = new RentalController(CONNECTION);
        while (userChoice != 0 && rentalController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 대여 정보의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printRentalInfo(userChoice);
        }
    }

    private void printRentalInfo(int rentalId) {
        RentalController rentalController = new RentalController(CONNECTION);
        RentalDTO rentalDTO = rentalController.selectById(rentalId);

        InventoryFilmController inventoryFilmController = new InventoryFilmController(CONNECTION);
        String title = inventoryFilmController.selectById(rentalDTO.getInventoryId()).getTitle();
        System.out.println("+======================================+");
        System.out.println("               대여 정보");
        System.out.println("+--------------------------------------+");
        System.out.println(" [번호] " + rentalDTO.getRentalId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [비디오명] " + title);
        System.out.println("+--------------------------------------+");
        System.out.println(" [대여일] " + rentalDTO.getRentalDate());
        System.out.println("+--------------------------------------+");
        System.out.print(" [반납일] ");
        if (rentalDTO.getReturnDate() == null) {
            System.out.println("미반납");
        } else {
            System.out.println(rentalDTO.getReturnDate());
        }
        System.out.println("+--------------------------------------+");
        CustomerController customerController = new CustomerController(CONNECTION);
        CustomerDTO customerDTO = customerController.selectById(rentalDTO.getCustomerId());
        System.out.println(" [대여 회원] " + customerDTO.getFirstName() + " " + customerDTO.getLastName());
        System.out.println("+======================================+");

        String message = "";
        if (rentalDTO.getReturnDate() == null) {
            message = "[1] 반납 [0] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 1);
            if (userChoice == 1) {
                returnRental(rentalDTO.getRentalId(), title);
            }
        } else {
            message = "[0] 뒤로 가기";
            ScannerUtil.nextInt(SCANNER, message, 0, 0);
        }
    }

    private void returnRental(int rentalId, String title) {
        RentalController rentalController = new RentalController(CONNECTION);
        RentalDTO rentalDTO = rentalController.selectById(rentalId);

        System.out.println(title + "[대여 번호: " + rentalDTO.getRentalId() + "]을(를) 반납 처리하시겠습니까?");
        String message = "[Y] 반납 [N] 취소";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            rentalController.update(rentalDTO);
            System.out.println("반납되었습니다.");
        } else {
            System.out.println("취소되었습니다.");
            printRentalInfo(rentalId);
        }
    }
}
