package home;

import database.ConnectionMaker;
import database.MySqlConnectionMaker;
import viewer.StaffViewer;

import java.sql.Connection;

public class VideoManagement {
    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new MySqlConnectionMaker();
        Connection connection = connectionMaker.makeConnection();
        StaffViewer staffViewer = new StaffViewer(connection);
        staffViewer.showIndex();
    }
}
