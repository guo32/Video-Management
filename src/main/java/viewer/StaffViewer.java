package viewer;

import controller.StaffController;
import database.ConnectionMaker;
import model.StaffDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class StaffViewer {
    private final Scanner SCANNER;
    private final Connection CONNECTION;
    private final int LIST_SIZE = 8;
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

        if (login.getUsername().equals("Admin")) {
            showAdminMenu();
        }
        else if (login != null) {
            showStaffMenu(login.getStoreId());
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
        String message = "[1] 재고 [2] 고객 [3] 영화 [4] 직원 [5] 상점 [6] 로그아웃";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 6);
        if (userChoice == 1) {
            // 재고 관련
        } else if (userChoice == 2) {
            // 고객 관련
        } else if (userChoice == 3) {
            // 영화 관련
        } else if (userChoice == 4) {
            showStaffManagementMenu();
        } else if (userChoice == 5) {
            // 상점 관련
        } else if (userChoice == 6) {
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
        StaffDTO newStaff = new StaffDTO();
        String message = "직원의 아이디를 입력해주세요.";
        newStaff.setUsername(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 비밀번호를 입력해주세요.";
        newStaff.setPassword(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 이름을 입력해주세요.";
        newStaff.setFirstName(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 성을 입력해주세요.";
        newStaff.setLastName(ScannerUtil.nextLine(SCANNER, message));

        message = "직원의 이메일을 입력해주세요.";
        newStaff.setEmail(ScannerUtil.nextLine(SCANNER, message));

        // 주소랑 가게 처리하기

        StaffController staffController = new StaffController(CONNECTION);
        staffController.insert(newStaff);
    }

    private void printStaffList() {
        StaffController staffController = new StaffController(CONNECTION);
        ArrayList<StaffDTO> list = staffController.selectAll();
        System.out.println("+--------------------------------------+");
        for (StaffDTO s : list) {
            System.out.printf("[%d] %s\n", s.getStaffId(), s.getUsername());
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
        StaffDTO staffDTO = staffController.selectById(staffId);
        System.out.println("+======================================+");
        System.out.println("               직원 정보");
        System.out.println("+--------------------------------------+");
        System.out.println(" [번호] " + staffDTO.getStaffId());
        System.out.println("+--------------------------------------+");
        System.out.println(" [이름] " + staffDTO.getFirstName() + " " + staffDTO.getLastName());
        System.out.println("+--------------------------------------+");
        System.out.println(" [주소] " + staffDTO.getAddressId()); // 임시 -- address 연결 후 수정할 것
        System.out.println("+--------------------------------------+");
        System.out.println(" [이메일] " + staffDTO.getEmail());
        System.out.println("+--------------------------------------+");
        System.out.println(" [아이디] " + staffDTO.getUsername());
        System.out.println("+--------------------------------------+");
        System.out.println(" [소속 상점] " + staffDTO.getStoreId()); // 임시 -- store 연결 후 수정할 것
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

    }

    private void deleteStaff(int staffId) {
        StaffController staffController = new StaffController(CONNECTION);
        StaffDTO staffDTO = staffController.selectById(staffId);
        String message = "정말로 직원[" + staffDTO.getUsername() + "]을(를) 삭제하시겠습니까?\n[Y] 예 [N] 아니오";
        String yesNo = ScannerUtil.nextLine(SCANNER, message);

        if (yesNo.equalsIgnoreCase("Y")) {
            staffController.delete(staffId);
            System.out.println("정상적으로 삭제되었습니다.");
        } else {
            System.out.println("취소되었습니다.");
            printStaffInfo(staffId);
        }
    }

    private void showStaffMenu(int storeId) {
        String message = "[1] 재고 [2] 고객 [3] 로그아웃";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);

        if (userChoice == 1) {
            // 재고(inventory) viewer 생성 후 수정
        } else if (userChoice == 2) {
            // 고객(customer) viewer 생성 후 수정
        } else if (userChoice == 3) {
            System.out.println("정상적으로 로그아웃되었습니다.");
            login = null;
        }
    }
}
