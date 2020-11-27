package source;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import source.Database.Database;
import source.Payment.Menu;

public class Main {
    public static void main(String[] args) {
        Database db = Database.getInstance();

        try {
            db.InitDatabase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Menu.getInstance().AddAllGenFood(db.ReadAllDishes());
        Menu.getInstance().AddAllGenFood(db.ReadAllCombos());

        Menu.getInstance().PrintMenu();
    }
}
