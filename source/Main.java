package source;

import java.io.IOException;
import java.sql.SQLException;

import source.Database.Database;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance();

        try {
            db.InitDatabase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
