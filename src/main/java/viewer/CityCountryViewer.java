package viewer;

import controller.CityCountryController;
import model.CityCountryDTO;
import util.ScannerUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class CityCountryViewer {
    private final Connection CONNECTION;
    private final Scanner SCANNER;

    public CityCountryViewer(Connection connection, Scanner scanner) {
        CONNECTION = connection;
        SCANNER = scanner;
    }

    public int searchCityByCountry() {
        String message = "검색할 나라를 입력해주세요. (부분 검색 가능)";
        String userInput = ScannerUtil.nextLine(SCANNER, message);
        CityCountryController cityCountryController = new CityCountryController(CONNECTION);
        ArrayList<CityCountryDTO> list = cityCountryController.selectByCountry(userInput);
        System.out.println("+---------------------------------------------------------+");
        System.out.println("                         검색 결과");
        System.out.println("+---------------------------------------------------------+");
        if (list.isEmpty()) {
            System.out.println(" * " + userInput + "을 포함하는 나라가 존재하지 않습니다.");
            System.out.println("+---------------------------------------------------------+");
            searchCityByCountry();
        } else {
            for (CityCountryDTO c : list) {
                System.out.printf(" [%d] %s | %s\n", c.getCityId(), c.getCity(), c.getCountry());
                System.out.println("+---------------------------------------------------------+");
            }
            return searchCityById(list);
        }
        return -1;
    }

    private int searchCityById(ArrayList<CityCountryDTO> list) {
        String message = "도시의 번호를 입력해주세요.";
        int userChoice = ScannerUtil.nextInt(SCANNER, message);
        while (!validateCityIdInList(list, userChoice)) {
            System.out.println("현재 검색 리스트에 존재하지 않는 도시의 번호입니다.");
            userChoice = ScannerUtil.nextInt(SCANNER, message);
        }
        return userChoice;
    }

    private boolean validateCityIdInList(ArrayList<CityCountryDTO> list, int cityId) {
        for (CityCountryDTO c : list) {
            if (c.getCityId() == cityId) {
                return true;
            }
        }
        return false;
    }
}
