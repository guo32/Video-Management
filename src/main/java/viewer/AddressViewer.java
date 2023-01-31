package viewer;

import controller.AddressController;
import model.AddressDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class AddressViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;

    public AddressViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public int insertAddress() {
        AddressDTO newAddress = new AddressDTO();

        String message = "주소를 입력해주세요.";
        String address = ScannerUtil.nextLine(SCANNER, message);
        while(address.length() >= 50) {
            System.out.println("50자 이내로 입력해주세요.");
            address = ScannerUtil.nextLine(SCANNER, message);
        }
        newAddress.setAddress(address);

        message = "지역(지구)을 입력해주세요.";
        String district = ScannerUtil.nextLine(SCANNER, message);
        while(!district.matches("\\w{1,20}")) {
            System.out.println("20자 이내로 입력해주세요.");
            district = ScannerUtil.nextLine(SCANNER, message);
        }
        newAddress.setDistrict(district);

        CityCountryViewer cityCountryViewer = new CityCountryViewer(CONNECTION, SCANNER);

        newAddress.setCityId(cityCountryViewer.searchCityByCountry());

        message = "우편번호를 입력해주세요.";
        String postalCode = ScannerUtil.nextLine(SCANNER, message);
        while(!postalCode.matches("\\w{1,10}")) {
            System.out.println("10자 이내로 입력해주세요.");
            postalCode = ScannerUtil.nextLine(SCANNER, message);
        }
        newAddress.setPostalCode(postalCode);

        message = "전화번호를 입력해주세요.";
        String phone = ScannerUtil.nextLine(SCANNER, message);
        while(!phone.matches("\\w{1,20}")) {
            System.out.println("20자 이내로 입력해주세요.");
            phone = ScannerUtil.nextLine(SCANNER, message);
        }
        newAddress.setPhone(phone);

        AddressController addressController = new AddressController(CONNECTION);
        if (addressController.insert(newAddress)) {
            System.out.println("새로운 주소가 정상적으로 등록되었습니다.");
            ArrayList<AddressDTO> list = addressController.selectByPostalCode(postalCode);
            return list.get(0).getAddressId();
        }
        return -1;
    }

    public void printSimpleAddress(int addressId) {
        AddressController addressController = new AddressController(CONNECTION);
        AddressDTO addressDTO = addressController.selectById(addressId);
        System.out.printf("%s %s %d(%s)\n", addressDTO.getAddress(), addressDTO.getDistrict(), addressDTO.getCityId(), addressDTO.getPostalCode());
    }
}
