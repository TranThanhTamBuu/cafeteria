package source.Database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import source.Food.Combo;
import source.Food.Dish;
import source.Payment.Menu;

public class Database {
    private static final Database instance = new Database();

    private String DB_NAME = "db_cftr";
    private String DB_URL = "jdbc:mysql://localhost:3306";
    private String USER_NAME = "root";
    private String PASSWORD = "";
    Connection conn;

    public static Database getInstance() {
        return instance;
    }

    private Database() {

    }

    public void InitDatabase() {
        try {
            // connnect to database 'testdb'
            this.conn = getConnection(DB_URL, USER_NAME, PASSWORD);
            // crate statement
            // Statement stmt = this.conn.createStatement();
            // // get data from table 'student'
            // ResultSet rs = stmt.executeQuery("select * from food join foodtype on
            // food.FoodTypeID = foodtype.ID");
            // // show data
            // while (rs.next()) {
            // System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getInt(3)
            // + " " + rs.getInt(4)
            // + " " + rs.getInt(5) + " " + rs.getString(6));
            // }
            // // close connection
            // this.conn.close();

            if (InitData() == 1) {
                System.out.println("Initialized successfully");
            } else {
                System.out.println("Already initialized");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error in init Database");
            e.printStackTrace();

        } catch (SQLException se) {
            System.out.println("Error in init Database");
            // Handle errors for JDBC
            se.printStackTrace();

        } catch (Exception ex) {
            System.out.println("Error in init Database");
            ex.printStackTrace();
        }
    }

    private Connection getConnection(String dbURL, String userName, String password)
            throws ClassNotFoundException, SQLException {

        Connection conn = null;

        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(dbURL, userName, password);
        System.out.println("connect successfully!");

        return conn;
    }

    private int InitData() throws SQLException, FileNotFoundException, IOException {
        Statement stmt = this.conn.createStatement();
        int rs = stmt.executeUpdate(String.format("CREATE DATABASE IF NOT EXISTS %s", DB_NAME));
        stmt.execute(String.format("USE %s", DB_NAME));

        // If already inited, return
        if (rs == 0)
            return rs;

        ScriptRunner sr = new ScriptRunner(this.conn, false, false);
        sr.runScript(new BufferedReader(new FileReader("source\\Database\\script.sql")));

        stmt.close();
        return rs;
    }

    public void CloseConnection() {
        try {
            this.conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<Dish> ReadAllDishes() {
        String query = "select * from menu where type = 'D'";
        Statement stmt;
        ArrayList<Dish> dishes = new ArrayList<>();
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                dishes.add(new Dish(rs.getInt(Field.Menu.ID.GetIdx()), rs.getString(Field.Menu.Name.GetIdx()),
                        rs.getString(Field.Menu.SpecificTypeID.GetIdx()), rs.getFloat(Field.Menu.Discount.GetIdx()),
                        rs.getInt(Field.Menu.Price.GetIdx())));
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return dishes;
        }
    }

    public ArrayList<Combo> ReadAllCombos() {
        Statement stmt;
        Statement stmt1;
        ArrayList<Combo> combos = new ArrayList<>();
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from menu where type = 'C'");

            while (rs.next()) {
                Combo curCb = new Combo(rs.getInt(Field.Menu.ID.GetIdx()), rs.getString(Field.Menu.Name.GetIdx()),
                        rs.getString(Field.Menu.SpecificTypeID.GetIdx()), rs.getFloat(Field.Menu.Discount.GetIdx()),
                        rs.getInt(Field.Menu.Price.GetIdx()));
                combos.add(curCb);

                ResultSet rs1 = stmt1
                        .executeQuery(String.format("select * from combo c where c.comboid = %s", curCb.GetID()));

                while (rs1.next()) {
                    curCb.addDish(Menu.getInstance().FindGenFood(rs1.getInt(Field.Combo.DishID.GetIdx())),
                            rs1.getInt(Field.Combo.Quantity.GetIdx()));
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return combos;
        }
    }
}
