package home;

import database.ConnectionMaker;
import database.MySqlConnectionMaker;
import viewer.StaffViewer;

public class VideoManagement {
    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new MySqlConnectionMaker();
        StaffViewer staffViewer = new StaffViewer(connectionMaker);
        staffViewer.showIndex();
    }
}
