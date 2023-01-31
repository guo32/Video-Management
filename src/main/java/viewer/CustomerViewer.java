package viewer;

import controller.CustomerController;
import model.CustomerDTO;
import model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class CustomerViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;
    private final int LIST_SIZE = 5;
    private StaffDTO login;

    public CustomerViewer(Connection connection, Scanner scanner, StaffDTO login) {
        CONNECTION = connection;
        SCANNER = scanner;
        this.login = login;
    }

    public void showMenu() {
        String message = "[1] 회원 등록 [2] 회원 목록 및 검색 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            insertCustomer();
        } else if (userChoice == 2) {
            printCustomerList();
        }
    }

    private void insertCustomer() {
        CustomerDTO newCustomer = new CustomerDTO();
        String message = "회원의 이름을 입력해주세요.";
        newCustomer.setFirstName(ScannerUtil.nextLine(SCANNER, message));

        message = "회원의 성을 입력해주세요.";
        newCustomer.setLastName(ScannerUtil.nextLine(SCANNER, message));

        message = "회원의 이메일을 입력해주세요.";
        newCustomer.setEmail(ScannerUtil.nextLine(SCANNER, message));

        // address 완성 후 수정
        AddressViewer addressViewer = new AddressViewer(CONNECTION, SCANNER);
        int addressId = addressViewer.insertAddress();
        if (addressId != -1) {
            newCustomer.setAddressId(addressId);
        } else {
            System.out.println("주소 등록에 실패하였습니다.");
            addressId = addressViewer.insertAddress();
        }


        newCustomer.setStoreId(login.getStoreId()); // 등록하는 직원의 대여점을 기준으로 삽입하였음

        CustomerController customerController = new CustomerController(CONNECTION);
        customerController.insert(newCustomer);

        System.out.println("회원이 정상적으로 등록되었습니다.\n메뉴로 이동합니다.");
        showMenu();
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
                    selectCustomer();
                    i -= (LIST_SIZE + 1);
                    continue;
                } else if (userChoice == 2) {
                    searchCustomer();
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
            System.out.printf(" [%d] %s %s\n", list.get(i).getCustomerId(), list.get(i).getFirstName(), list.get(i).getLastName());
            System.out.println("+---------------------------------------------------------+");
        }
    }

    private void selectCustomer() {
        String message = "상세보기할 회원의 번호를 입력해주세요.\n[번호] 입력 [0] 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        CustomerController customerController = new CustomerController(CONNECTION);
        while (userChoice != 0 && customerController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 회원의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printCustomerInfo(userChoice);
        }
    }

    private void selectCustomer(ArrayList<CustomerDTO> list) {
        String message = "상세보기할 회원의 번호를 입력해주세요.\n[번호] 입력 [0] 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && !validateCustomerIdInList(list, userChoice)) {
            System.out.println("현재 검색 리스트에 존재하지 않는 회원의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printCustomerInfo(userChoice);
        }
    }

    private boolean validateCustomerIdInList(ArrayList<CustomerDTO> list, int customerId) {
        for (CustomerDTO c : list) {
            if (c.getCustomerId() == customerId) {
                return true;
            }
        }
        return false;
    }

    private void printCustomerInfo(int customerId) {
        CustomerController customerController = new CustomerController(CONNECTION);
        CustomerDTO customerDTO = customerController.selectById(customerId);
        System.out.println("+======================================+");
        System.out.println("               회원 정보");
        System.out.println("+--------------------------------------+");
        System.out.println(" [번호] " + customerDTO.getCustomerId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [이름] " + customerDTO.getFirstName() + " " + customerDTO.getLastName());
        System.out.println("+--------------------------------------+");
        AddressViewer addressViewer = new AddressViewer(CONNECTION, SCANNER);
        System.out.print(" [주소] ");
        addressViewer.printSimpleAddress(customerDTO.getAddressId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [이메일] " + customerDTO.getEmail());
        System.out.println("+--------------------------------------+");
        if (customerDTO.getActive() == 1) {
            System.out.println(" [활성화] Y");
        } else {
            System.out.println(" [활성화] N");
        }
        System.out.println("+======================================+");

        String message = "[1] 수정 [2] 삭제 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            updateCustomer(customerId);
        } else if (userChoice == 2) {
            deleteCustomer(customerId);
        }
    }

    private void updateCustomer(int customerId) {
        CustomerController customerController = new CustomerController(CONNECTION);
        CustomerDTO customerDTO = customerController.selectById(customerId);
        if (customerDTO.getActive() == 1) {
            String message = "해당 회원을 휴면 상태로 전환하시겠습니까?\n[Y] 예 [N] 아니오";
            String yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                customerDTO.setActive(0);
            }
        } else {
            String message = "해당 회원을 활성화시키겠습니까?\n[Y] 예 [N] 아니오";
            String yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                customerDTO.setActive(1);
            }
        }
        customerController.update(customerDTO);
        printCustomerInfo(customerId);
    }

    private void deleteCustomer(int customerId) {
        CustomerController customerController = new CustomerController(CONNECTION);
        CustomerDTO customerDTO = customerController.selectById(customerId);
        String message = "정말로 회원[" + customerDTO.getFirstName() + " " + customerDTO.getLastName() + "]을(를) 삭제하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            customerController.delete(customerId);
        } else {
            System.out.println("취소되었습니다.");
            printCustomerInfo(customerId);
        }
    }

    private void searchCustomer() {
        String message = "찾을 회원의 이름을 입력해주세요. (부분 검색 가능)";
        String userInput = ScannerUtil.nextLine(SCANNER, message);
        CustomerController customerController = new CustomerController(CONNECTION);
        ArrayList<CustomerDTO> list = customerController.selectByFirstName(userInput);
        System.out.println("+---------------------------------------------------------+");
        System.out.println("                         검색 결과");
        System.out.println("+---------------------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * 해당하는 회원이 존재하지 않습니다.");
            System.out.println("+---------------------------------------------------------+");
        } else {
            for (CustomerDTO c : list) {
                System.out.printf(" [%d] %s %s\n", c.getCustomerId(), c.getFirstName(), c.getLastName());
                System.out.println("+---------------------------------------------------------+");
            }
        }
        message = "[1] 회원 선택 [2] 목록으로 돌아가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
        if (userChoice == 1) {
            selectCustomer(list);
        }
    }
}
