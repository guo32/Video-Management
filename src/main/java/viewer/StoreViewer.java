package viewer;

import controller.AddressController;
import controller.StaffController;
import controller.StoreController;
import model.StaffDTO;
import model.StoreDTO;
import util.ScannerUtil;

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

    public void showMenu() {
        String message = "[1] 새 대여점 추가하기 [2] 대여점 목록 [3] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 1, 3);
        if (userChoice == 1) {
            insertStore();
            showMenu();
        } else if (userChoice == 2) {
            printStoreList();
            selectStore();
            showMenu();
        }
    }

    private void insertStore() {
        StoreDTO newStore = new StoreDTO();
        String message = "새로운 대여점의 주소를 입력해주세요.";
        AddressViewer addressViewer = new AddressViewer(CONNECTION, SCANNER);
        int addressId = addressViewer.insertAddress();
        while (addressId == -1) {
            System.out.println("주소 등록에 실패하였습니다.");
            addressId = addressViewer.insertAddress();
        }
        newStore.setAddressId(addressId);

        StaffViewer staffViewer = new StaffViewer(CONNECTION);
        message = "대여점의 매니저로 임명할 직원의 번호를 입력해주세요.";
        int managerStaffId = ScannerUtil.nextInt(SCANNER, message);
        StoreController storeController = new StoreController(CONNECTION);
        StaffController staffController = new StaffController(CONNECTION);
        while (staffController.selectById(managerStaffId) == null || !storeController.validateManagerStaffId(managerStaffId)) {
            System.out.println("존재하지 않는 직원의 번호이거나 이미 타 대여점의 매니저로 등록되어 있는 직원의 번호입니다.");
            managerStaffId = ScannerUtil.nextInt(SCANNER, message);
        }
        newStore.setManagerStaffId(managerStaffId);

        storeController.insert(newStore);
        System.out.println("새로운 대여점이 정상적으로 등록되었습니다.");
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

    private void selectStore() {
        String message = "상세보기할 대여점의 번호를 입력해주세요. [0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        StoreController storeController = new StoreController(CONNECTION);
        while (userChoice != 0 && storeController.selectById(userChoice) == null) {
            System.out.println("존재하지 않는 대여점의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        if (userChoice != 0) {
            printStoreInfo(userChoice);
            printStoreList();
            selectStore();
        }
    }

    private void printStoreInfo(int storeId) {
        StoreController storeController = new StoreController(CONNECTION);
        StoreDTO storeDTO = storeController.selectById(storeId);

        System.out.println("+======================================+");
        System.out.println("               대여점 정보");
        System.out.println("+--------------------------------------+");
        System.out.println(" [번호] " + storeDTO.getStoreId());
        System.out.println("+--------------------------------------+");
        StaffController staffController = new StaffController(CONNECTION);
        String managerName = staffController.selectById(storeDTO.getManagerStaffId()).getFirstName() + " " +
                staffController.selectById(storeDTO.getManagerStaffId()).getLastName();
        System.out.printf(" [매니저] %s\n", managerName);
        System.out.println("+--------------------------------------+");
        AddressController addressController = new AddressController(CONNECTION);
        String address = addressController.selectById(storeDTO.getAddressId()).getAddress() + " " +
                addressController.selectById(storeDTO.getAddressId()).getDistrict() + "(" +
                addressController.selectById(storeDTO.getAddressId()).getPostalCode() + ")";
        System.out.printf(" [주소] %s\n", address);
        System.out.println("+======================================+");

        String message = "[0] 뒤로 가기";
        int userChoice = ScannerUtil.nextInt(SCANNER, message, 0, 0);
    }
}
