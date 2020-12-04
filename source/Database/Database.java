package source.Database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.Identity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import source.Food.Combo;
import source.Food.Dish;
import source.Management.Cost;
import source.Payment.Menu;
import source.Payment.Order;

public class Database {
    public static enum ReadBy {
        DAY, MONTH, YEAR
    }

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
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dishes;
    }

    public ArrayList<Combo> ReadAllCombos() {
        Statement stmt = null;
        Statement stmt1 = null;
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
                    curCb.addDish(rs1.getInt(Field.Combo.DishID.GetIdx()), rs1.getInt(Field.Combo.Quantity.GetIdx()));
                }
            }

            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return combos;
    }

    public ArrayList<Order> ReadOrderBy(ReadBy readBy, LocalDateTime date) {
        ArrayList<Order> orders = new ArrayList<Order>();

        try {
            Statement stmt = conn.createStatement();
            Statement stmt1 = conn.createStatement();

            String query = "";

            switch (readBy) {
                case DAY: {
                    query = String.format("select * from payment where convert(datepayment, date) = '%s-%s-%s'",
                            date.getYear(), date.getMonthValue(), date.getDayOfMonth());

                    break;
                }
                case MONTH: {
                    query = String.format(
                            "select * from payment where month(convert(datepayment, date)) = %s and year(convert(datepayment, date)) = %s",
                            date.getMonthValue(), date.getYear());

                    break;
                }

                case YEAR: {
                    query = String.format("select * from payment where year(convert(datepayment, date)) = %s",
                            date.getYear());

                    break;
                }
            }

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Order curOr = new Order(rs.getInt(Field.Payment.ID.GetIdx()),
                        rs.getObject(Field.Payment.DateTime.GetIdx(), LocalDateTime.class),
                        rs.getString(Field.Payment.Note.GetIdx()));
                orders.add(curOr);

                ResultSet rs1 = stmt1.executeQuery(
                        String.format("select * from specificpayment where paymentid = %s", curOr.GetID()));

                while (rs1.next()) {
                    curOr.addFood(rs.getInt(Field.SpecificPayment.FoodID.GetIdx()),
                            rs.getInt(Field.SpecificPayment.Quantity.GetIdx()));
                }
            }

            stmt.close();
            stmt1.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return orders;
    }

    public ArrayList<Cost> ReadCostBy(ReadBy readBy, LocalDateTime date) {
        ArrayList<Cost> costs = new ArrayList<Cost>();

        try {
            Statement stmt = conn.createStatement();
            Statement stmt1 = conn.createStatement();

            String query = "";

            switch (readBy) {
                case DAY: {
                    query = String.format("select * from payment where convert(datepayment, date) = '%s-%s-%s'",
                            date.getYear(), date.getMonthValue(), date.getDayOfMonth());

                    break;
                }
                case MONTH: {
                    query = String.format(
                            "select * from payment where month(convert(datepayment, date)) = %s and year(convert(datepayment, date)) = %s",
                            date.getMonthValue(), date.getYear());

                    break;
                }

                case YEAR: {
                    query = String.format("select * from payment where year(convert(datepayment, date)) = %s",
                            date.getYear());

                    break;
                }
            }

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Cost curCost = new Cost(rs.getInt(Field.Cost.ID.GetIdx()),
                        rs.getString(Field.Cost.Type.GetIdx()).charAt(0),
                        rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                        rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                        rs.getInt(Field.Cost.UnitID.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx()));

                costs.add(curCost);
            }

            stmt.close();
            stmt1.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return costs;
    }

    public void WriteOrder(Order order) {
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO Payment VALUES (null, '%s', '%s', null)",
                    order.getListDateTime(), order.getNote()));
            int id = order.GetID();
            ArrayList<Integer> list_foodID = order.getListFoodID();
            ArrayList<Integer> quantity = order.getListQuantity();
            for (int i = 0; i < list_foodID.size(); i++) {
                stmt1.executeUpdate(String.format("INSERT INTO SpecificPayment VALUES (%d, '%d', '%d')", id,
                        list_foodID.get(i), quantity.get(i)));
            }
            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void WriteCost(Cost cost) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO Cost VALUES (null, '%s', '%s', '%s', %.2f, %d, %d)",
                    cost.getType(), cost.getDate(), cost.getDescription(), cost.getQuantity(), cost.getUnitId(),
                    cost.getTotalAmount()));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void WriteDish(Dish dish) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(String.format("INSERT INTO Menu VALUES (null, 'D', %d, %d, '%s', %d, %.2f)",
                    dish.getCategory(), dish.getSpecificType(), dish.getName(), dish.getPrice(), dish.getDiscount()));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void WriteCombo(Combo combo) {
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt.executeUpdate(
                    String.format("INSERT INTO Menu VALUES (null, 'C', %d, %d, '%s', %d, %.2f)", combo.getCategory(),
                            combo.getSpecificType(), combo.getName(), combo.getPrice(), combo.getDiscount()));
            int id = combo.GetID();
            ArrayList<Integer> array_dishID = combo.getArrayDishID();
            ArrayList<Integer> quantity = combo.getArrayQuantity();
            for (int i = 0; i < array_dishID.size(); i++) {
                stmt1.executeUpdate(String.format("INSERT INTO SpecificPayment VALUES (%d, '%d', '%d')", id,
                        array_dishID.get(i), quantity.get(i)));
            }
            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteCost(int id) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM Cost WHERE ID = %d", id));
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DeleteOrder(int id) {
        Statement stmt = null;
        Statement stmt1 = null;
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt.executeUpdate(String.format("DELETE FROM SpecificPayment WHERE ID = %d", id));
            stmt1.executeUpdate(String.format("DELETE FROM Payment WHERE ID = %d", id));
            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditCost(int id, String attribute_name, String value) {
        Statement stmt = null;
        try {
            if (attribute_name == "Type" || attribute_name == "Description" || attribute_name == "Quantity"
                    || attribute_name == "UnitID" || attribute_name == "TotalAmount" || attribute_name == "DateCost") {
                stmt = this.conn.createStatement();
                stmt.executeUpdate(String.format("UPDATE Cost SET %s = '%s' WHERE ID = %d", attribute_name, value, id));
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditDish(int id, String attribute_name, String value) {
        Statement stmt = null;
        try {
            if (attribute_name == "CategoryID" || attribute_name == "SpecificTypeID" || attribute_name == "Name"
                    || attribute_name == "Price" || attribute_name == "Discount") {
                stmt = this.conn.createStatement();
                stmt.executeUpdate(String.format("UPDATE Menu SET %s = '%s' WHERE ID = %d AND Type = 'D",
                        attribute_name, value, id));
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditCombo(int id, String attribute_name, String value) {
        Statement stmt = null;
        try {
            stmt = this.conn.createStatement();
            if (attribute_name == "Quantity") {
                stmt.executeUpdate(
                        String.format("UPDATE Combo SET %s = '%s' WHERE ID = %d", attribute_name, value, id));
            }
            if (attribute_name == "Name" || attribute_name == "Price" || attribute_name == "Discount") {
                stmt.executeUpdate(String.format("UPDATE Menu SET %s = '%s' WHERE ID = %d AND Type = 'C'",
                        attribute_name, value, id));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
