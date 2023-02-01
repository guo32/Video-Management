package viewer;

import controller.StaffController;
import controller.StoreController;
import database.ConnectionMaker;
import model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class StaffViewer {
    private final Scanner SCANNER;
    private final Connection CONNECTION;
    private StaffDTO login = null;

    public StaffViewer(ConnectionMaker connectionMaker) {
        SCANNER = new Scanner(System.in);
        CONNECTION = connectionMaker.makeConnection();
    }

    public void showIndex() {
        while (true) {
            String message = "[1] 로그인 [2] 종료";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
            if (userChoice == 1) {
                auth();
            } else if (userChoice == 2) {
                System.out.println("시스템을 종료합니다.");
                break;
            }
        }
    }

    private void auth() {
        String message = "아이디를 입력해주세요.";
        String username = ScannerUtil.nextLine(SCANNER, message);

        message = "비밀번호를 입력해주세요.";
        String password = ScannerUtil.nextLine(SCANNER, message);

        StaffController staffController = new StaffController(CONNECTION);
        login = staffController.auth(username, password);

        if (login != null) {
            if (login.getActive() == 0) {
                System.out.println("휴면 상태의 계정은 로그인할 수 없습니다. 관리자에게 문의하세요.");
                login = null;
                return;
            }
            if (login.getUsername().equals("Admin")) {
                showAdminMenu();
            } else {
                showStaffMenu(login.getStoreId());
            }
        } else {
            System.out.println("아이디 또는 비밀번호가 잘못되었습니다.");
            message = "[1] 다시 입력 [2] 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 2);
            if (userChoice == 1) {
                auth();
            }
        }
    }

    private void showAdminMenu() {
        String message = "[1] 대여 [2] 재고 [3] 회원 [4] 영화 [5] 직원 [6] 대여점 [7] 로그아웃";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 7);
        if (userChoice == 1) {
            // 대여 관련
            RentalViewer rentalViewer = new RentalViewer(CONNECTION, SCANNER, login);
            rentalViewer.showMenu();
            showStaffMenu(login.getStoreId());
        } else if (userChoice == 2) {
            // 재고 관련 --> 관리자는 모든 재고 목록을 볼 수 있도록 수정할 것
            InventoryViewer inventoryViewer = new InventoryViewer(CONNECTION, SCANNER);
            inventoryViewer.showMenu(login.getStoreId());
            showAdminMenu();
        } else if (userChoice == 3) {
            // 고객 관련
            CustomerViewer customerViewer = new CustomerViewer(CONNECTION, SCANNER, login);
            customerViewer.showMenu();
            showAdminMenu();
        } else if (userChoice == 4) {
            // 영화 관련
        } else if (userChoice == 5) {
            showStaffManagementMenu();
        } else if (userChoice == 6) {
            // 상점 관련
        } else if (userChoice == 7) {
            System.out.println("정상적으로 로그아웃되었습니다.");
            login = null;
        }
    }

    private void showStaffManagementMenu() {
        String message = "[1] 직원 추가 [2] 직원 목록 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            insertStaff();
        } else if (userChoice == 2) {
            printStaffList();
        } else if (userChoice == 3) {
            showAdminMenu();
        }
    }

    private void insertStaff() {
        StaffController staffController = new StaffController(CONNECTION);
        StaffDTO newStaff = new StaffDTO();

        String message = "직원의 아이디를 입력해주세요.";
        String username = ScannerUtil.nextLine(SCANNER, message);
        while (staffController.validateUsername(username)) {
            System.out.println("이미 사용 중인 아이디입니다.");
            username = ScannerUtil.nextLine(SCANNER, message);
        }
        newStaff.setUsername(username);

        message = "직원의 비밀번호를 입력해주세요.";
        newStaff.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 이름을 입력해주세요.";
        newStaff.setFirstName(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 성을 입력해주세요.";
        newStaff.setLastName(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 이메일을 입력해주세요.";
        newStaff.setEmail(ScannerUtil.nextLine(SCANNER, message));

        StoreViewer storeViewer = new StoreViewer(CONNECTION, SCANNER);
        StoreController storeController = new StoreController(CONNECTION);
        storeViewer.printStoreList();
        message = "근무할 대여점의 번호를 입력해주세요.";
        int storeId = ScannerUtil.nextInt(SCANNER, message);
        while (storeController.selectById(storeId) == null) {
            System.out.println("존재하지 않는 대여점의 번호입니다.");
            storeViewer.printStoreList();
            storeId = ScannerUtil.nextInt(SCANNER, message);
        }
        newStaff.setStoreId(storeId);

        AddressViewer addressViewer = new AddressViewer(CONNECTION, SCANNER);
        int addressId = addressViewer.insertAddress();
        if (addressId != -1) {
            newStaff.setAddressId(addressId);
        } else {
            System.out.println("주소 등록에 실패하였습니다.");
            addressId = addressViewer.insertAddress();
        }

        staffController.insert(newStaff);
        System.out.println("정상적으로 등록되었습니다.");
        printStaffList();
    }

    private void printStaffList() {
        StaffController staffController = new StaffController(CONNECTION);
        ArrayList<StaffDTO> list = staffController.selectAll();
        System.out.println("+--------------------------------------+");
        for (StaffDTO s : list) {
            System.out.printf(" [%d] %s\n", s.getStaffId(), s.getUsername());
            System.out.println("+--------------------------------------+");
        }
        String message = "상세보기할 직원의 번호를 입력해주세요.\n[번호] 입력 [0] 뒤로가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (userChoice != 0 && staffController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 직원의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printStaffInfo(userChoice);
        } else {
            showStaffManagementMenu();
        }
    }

    private void printStaffInfo(int staffId) {
        StaffController staffController = new StaffController(CONNECTION);
        StoreController storeController = new StoreController(CONNECTION);
        StaffDTO staffDTO = staffController.selectById(staffId);
        System.out.println("+======================================+");
        System.out.println("               직원 정보");
        System.out.println("+--------------------------------------+");
        System.out.println(" [번호] " + staffDTO.getStaffId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [이름] " + staffDTO.getFirstName() + " " + staffDTO.getLastName());
        System.out.println("+--------------------------------------+");
        AddressViewer addressViewer = new AddressViewer(CONNECTION, SCANNER);
        System.out.print(" [주소] ");
        addressViewer.printSimpleAddress(staffDTO.getAddressId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [이메일] " + staffDTO.getEmail());
        System.out.println("+--------------------------------------+");
        System.out.println(" [아이디] " + staffDTO.getUsername());
        System.out.println("+--------------------------------------+");
        System.out.println(" [소속 상점] " + staffDTO.getStoreId() + "(" + storeController.selectById(staffDTO.getStoreId()).getAddress() + ")");
        System.out.println("+--------------------------------------+");
        if (staffDTO.getActive() == 1) {
            System.out.println(" [활성화] Y");
        } else {
            System.out.println(" [활성화] N");
        }
        System.out.println("+======================================+");

        String message = "[1] 수정 [2] 삭제 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            updateStaff(staffId);
        } else if (userChoice == 2) {
            deleteStaff(staffId);
        } else if (userChoice == 3) {
            printStaffList();
        }
    }

    private void updateStaff(int staffId) {
        StaffController staffController = new StaffController(CONNECTION);
        StaffDTO staffDTO = staffController.selectById(staffId);

        // 근무지(대여점) 변경
        String message = "직원[" + staffDTO.getUsername() + "]의 근무지를 변경하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);
        if (yesNo.equalsIgnoreCase("Y")) {
            StoreViewer storeViewer = new StoreViewer(CONNECTION, SCANNER);
            StoreController storeController = new StoreController(CONNECTION);
            storeViewer.printStoreList();
            message = "근무할 대여점의 번호를 입력해주세요.";
            int storeId = ScannerUtil.nextInt(SCANNER, message);
            while (storeController.selectById(storeId) == null) {
                System.out.println("존재하지 않는 대여점의 번호입니다.");
                storeId = ScannerUtil.nextInt(SCANNER, message);
            }
            staffDTO.setStoreId(storeId);
        }

        // 활성화 상태 변경
        if (staffDTO.getActive() == 1) {
            message = "해당 계정을 휴면 상태로 전환하시겠습니까?\n[Y] 예 [N] 아니오";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                staffDTO.setActive(0);
            }
        } else {
            message = "해당 계정을 활성화시키겠습니까?\n[Y] 예 [N] 아니오";
            yesNo = ScannerUtil.nextLine(SCANNER, message);
            if (yesNo.equalsIgnoreCase("Y")) {
                staffDTO.setActive(1);
            }
        }

        // 비밀번호 변경
        message = "새로운 비밀번호를 입력해주세요.";
        staffDTO.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "관리자 비밀번호를 입력해주세요.";
        String adminPassword = ScannerUtil.nextLine(SCANNER, message);
        if (adminPassword.equals(staffController.selectById(login.getStaffId()).getPassword())) {
            staffController.update(staffDTO);
            System.out.println("변경사항이 정상적으로 저장되었습니다.");
        } else {
            System.out.println("직원 정보 변경에 실패하였습니다.");
        }
        printStaffInfo(staffId);
    }

    private void deleteStaff(int staffId) {
        StaffController staffController = new StaffController(CONNECTION);
        StaffDTO staffDTO = staffController.selectById(staffId);
        String message = "정말로 직원[" + staffDTO.getUsername() + "]을(를) 삭제하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            staffController.delete(staffId);
            System.out.println("정상적으로 삭제되었습니다.");
            printStaffList();
        } else {
            System.out.println("취소되었습니다.");
            printStaffInfo(staffId);
        }
    }

    private void showStaffMenu(int storeId) {
        String message = "[1] 대여 [2] 재고 [3] 회원 [4] 로그아웃";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 4);

        if (userChoice == 1) {
            // 대여
            RentalViewer rentalViewer = new RentalViewer(CONNECTION, SCANNER, login);
            rentalViewer.showMenu();
            showStaffMenu(storeId);
        } else if (userChoice == 2) {
            // 재고(inventory) viewer 생성 후 수정
            InventoryViewer inventoryViewer = new InventoryViewer(CONNECTION, SCANNER);
            inventoryViewer.showMenu(storeId);
            showStaffMenu(storeId);
        } else if (userChoice == 3) {
            // 고객(customer) viewer 생성 후 수정
            CustomerViewer customerViewer = new CustomerViewer(CONNECTION, SCANNER, login);
            customerViewer.showMenu();
            showStaffMenu(storeId);
        } else if (userChoice == 4) {
            System.out.println("정상적으로 로그아웃되었습니다.");
            login = null;
        }
    }
}
