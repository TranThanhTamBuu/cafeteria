package source.Database;

import com.mysql.cj.result.LocalDateTimeValueFactory;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
//import javax.swing.JTable;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

//import source.Food.Combo;
//import source.Food.Dish;
//import source.Food.Food;
//import source.Management.Cost;
//import source.Payment.Menu;
//import source.Payment.Order;
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
        if (rs == 0) {
            return rs;
        }

        ScriptRunner sr = new ScriptRunner(this.conn, false, false);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        sr.runScript(new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\cafeteria\\source\\Database\\script.sql")));

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

    public boolean searchMenu(JTable tbl_menu, String keyword) {
        if (keyword.isEmpty()) {
            readMenu(tbl_menu);
            return true;
        }

        String query = "", query1 = "", query2 = "";

        if (keyword.toLowerCase().equals("combo")) {
            query = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID"
                    + " where mn.type = 'C'"
                    + " ORDER by mn.type DESC, mn.id";
        } else if (keyword.toLowerCase().equals("dish")) {
            query = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID"
                    + " where mn.type = 'D'"
                    + " ORDER by mn.type DESC, mn.id";
        } else {
            query = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID"
                    + " where"
                    + String.format(" MATCH(mn.name) AGAINST ('%s' IN NATURAL LANGUAGE MODE)", keyword)
                    + " ORDER by mn.type DESC, mn.id";

            query1 = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID"
                    + " where"
                    + String.format(" MATCH(cat.name) AGAINST ('%s' IN NATURAL LANGUAGE MODE)", keyword)
                    + " ORDER by mn.type DESC, mn.id";

            query2 = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID"
                    + " where"
                    + String.format(" MATCH(sp.name) AGAINST ('%s' IN NATURAL LANGUAGE MODE)", keyword)
                    + " ORDER by mn.type DESC, mn.id";
        }

        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_menu_model = (DefaultTableModel) tbl_menu.getModel();
            tbl_menu_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{String.valueOf(rs.getInt(Field.Menu.ID.GetIdx())),
                    rs.getString(Field.Menu.Name.GetIdx()),
                    rs.getString(Field.Menu.CategoryName.GetIdx()),
                    rs.getString(Field.Menu.Type.GetIdx()).equals("D") ? "Dish" : "Combo",
                    rs.getString(Field.Menu.SpecificName.GetIdx()),
                    String.valueOf(rs.getInt(Field.Menu.Price.GetIdx())),
                    String.valueOf(rs.getFloat(Field.Menu.Discount.GetIdx()))
                };//                

                tbl_menu_model.addRow(objs);
            }

            if (!query1.isEmpty()) {
                rs = stmt.executeQuery(query1);

                while (rs.next()) {
                    Object[] objs = new Object[]{String.valueOf(rs.getInt(Field.Menu.ID.GetIdx())),
                        rs.getString(Field.Menu.Name.GetIdx()),
                        rs.getString(Field.Menu.CategoryName.GetIdx()),
                        rs.getString(Field.Menu.Type.GetIdx()).equals("D") ? "Dish" : "Combo",
                        rs.getString(Field.Menu.SpecificName.GetIdx()),
                        String.valueOf(rs.getInt(Field.Menu.Price.GetIdx())),
                        String.valueOf(rs.getFloat(Field.Menu.Discount.GetIdx()))
                    };//                

                    tbl_menu_model.addRow(objs);
                }

                //
                rs = stmt.executeQuery(query2);

                while (rs.next()) {
                    Object[] objs = new Object[]{String.valueOf(rs.getInt(Field.Menu.ID.GetIdx())),
                        rs.getString(Field.Menu.Name.GetIdx()),
                        rs.getString(Field.Menu.CategoryName.GetIdx()),
                        rs.getString(Field.Menu.Type.GetIdx()).equals("D") ? "Dish" : "Combo",
                        rs.getString(Field.Menu.SpecificName.GetIdx()),
                        String.valueOf(rs.getInt(Field.Menu.Price.GetIdx())),
                        String.valueOf(rs.getFloat(Field.Menu.Discount.GetIdx()))
                    };//                

                    tbl_menu_model.addRow(objs);
                }
            }

            if (tbl_menu.getRowCount() > 0 && !((String) tbl_menu.getValueAt(0, 0)).isEmpty()) {
                removeDuplicates(tbl_menu);
            }

//            tbl_menu.setModel(tbl_menu_model);
            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public void removeDuplicates(JTable tbl) {
        for (int k = tbl.getRowCount(); k < 18; k++) {
            ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "", "", "", "" });
            System.out.println("After add white row");
            for (int m = 0; m < tbl.getRowCount(); m++) {
                System.out.println(tbl.getValueAt(m, 0));
            }
        }
        
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tbl.getModel());
        tbl.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        sorter.setComparator(0, new Comparator<Object>() {

            @Override
            public int compare(Object id1, Object id2) {
                if ((id1.toString()).isEmpty()) {
                    return 1;
                }
                else if ((id2.toString()).isEmpty()) return -1;
                                
                else if (Integer.parseInt(id1.toString()) < Integer.parseInt(id2.toString())) {
                    return -1;
                } else if (Integer.parseInt(id1.toString()) > Integer.parseInt(id2.toString())) {
                    return 1;
                }
                return 0;
            }
        });

        sorter.sort();
        

        int i = 0;
        int j = 0;
        int n = getValidRowCount(tbl);
        while (i < n - 1) {
            if (i == 12) {
                System.out.println("source.Database.Database.removeDuplicates()");
            }
            if (!((String) tbl.getValueAt(i, 0)).equals((String) tbl.getValueAt(i + 1, 0))) {
                copyRow(tbl, i, j);
                j++;
            }
            i++;
        }
        copyRow(tbl, n - 1, j++);

        // whiteout redundant rows
        for (; j < n; j++) {
            for (int m = 0; m < tbl.getColumnCount(); m++) {
                tbl.setValueAt("", j, m);
            }
        }
        
        
        sorter.setSortable( 0 , false );
//        tbl.setRowSorter(null);
        // add white row to full up table
        System.out.println("before add white row");
        for (int m = 0; m < tbl.getRowCount(); m++) {
            System.out.println(tbl.getValueAt(m, 0));
        }
        
    }

    public void copyRow(JTable tbl, int src, int des) {
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.setValueAt(tbl.getValueAt(src, i), des, i);
        }

    }

    public int getValidRowCount(JTable tbl) {
        int i;
        for (i = 0; i < tbl.getRowCount(); i++) {
            if (((String) tbl.getValueAt(i, 0)).isEmpty()) {
                break;
            }
        }

        return i;
    }

    public void deleteRow(JTable tbl, int removeIdx) {
        if (removeIdx != -1 && removeIdx < tbl.getRowCount()) {
            ((DefaultTableModel) tbl.getModel()).removeRow(removeIdx);
//            ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "", "", "", "" });
        }
    }

    public boolean charge(JTable tbl_order, String note, String total) {
        if (((String) tbl_order.getValueAt(0, 0)).isEmpty()) {
            return false;
        }

        Statement stmt = null;
        ResultSet rs;

        try {
            stmt = this.conn.createStatement();
//            stmt1 = this.conn.createStatement();

            // Payment table
            LocalDateTime date = LocalDateTime.now();
//            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);

            stmt.executeUpdate(String.format("insert into payment values (null, '%s', '%s', '%s')",
                    date.toString(),
                    note,
                    total
            ));

            // select latest added row
            rs = stmt.executeQuery("select * from payment order by payment.id desc limit 1");
            String paymentID = "";
            while (rs.next()) {
                paymentID = String.valueOf(rs.getInt(Field.Payment.ID.GetIdx()));
                break;
            }

            // specific payment
            String foodID = "";
            for (int i = 0; i < tbl_order.getRowCount(); i++) {
                if (((String) (tbl_order.getValueAt(i, 0))).isEmpty()) {
                    break;
                }
                rs = stmt.executeQuery(String.format("select id from menu where name = '%s'", (String) tbl_order.getValueAt(i, 0)));
                while (rs.next()) {
                    foodID = String.valueOf(rs.getInt(Field.Menu.ID.GetIdx()));
                    break;
                }

                stmt.executeUpdate(String.format("insert into specificpayment values ('%s', '%s', %s)",
                        paymentID,
                        foodID,
                        (String) tbl_order.getValueAt(i, 1)));

            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTodayRevenue() {
        
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT sum(payment.TotalAmount) from payment");
            
            int rev = 0;
            while (rs.next()) {
                rev = rs.getInt(1);
            }

//            tbl_menu.setModel(tbl_menu_model);
            stmt.close();
            return rev;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }
    
    public int getTodayOrders() {
        
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(payment.TotalAmount) from payment");
            
            int rev = 0;
            while (rs.next()) {
                rev = rs.getInt(1);
            }

//            tbl_menu.setModel(tbl_menu_model);
            stmt.close();
            return rev;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public boolean readMenu(JTable tbl) {
        String query = "select * from menu mn join category cat on mn.categoryID = cat.id join specifictype sp on sp.id = mn.specifictypeID ORDER by mn.type DESC, mn.id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{String.valueOf(rs.getInt(Field.Menu.ID.GetIdx())),
                    rs.getString(Field.Menu.Name.GetIdx()),
                    rs.getString(Field.Menu.CategoryName.GetIdx()),
                    rs.getString(Field.Menu.Type.GetIdx()).equals("D") ? "Dish" : "Combo",
                    rs.getString(Field.Menu.SpecificName.GetIdx()),
                    String.valueOf(rs.getInt(Field.Menu.Price.GetIdx())),
                    String.valueOf(rs.getFloat(Field.Menu.Discount.GetIdx()))
                };
//                for (Object obj: objs) {
//                    System.out.println(obj);
//                }

                tbl_model.addRow(objs);
                System.out.println("Row Count: " + tbl_model.getRowCount());
            }

//            tbl_menu.setModel(tbl_menu_model);
            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean ReadAllCosts(JTable tbl_cost) {
        String query = "select * from cost c join unit u on c.UnitID = u.id order by c.DateCost DESC, c.id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_cost_model = (DefaultTableModel) tbl_cost.getModel();
            tbl_cost_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                    rs.getString(Field.Cost.Type.GetIdx()).equals("G") ? "Goods" : "Operation",
                    rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                    rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                    rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                tbl_cost_model.addRow(objs);
            }
            
            for (int k = tbl_cost.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl_cost.getModel()).addRow(new Object[] { "", "", "", "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public void WriteCost(String type, String date, String description, Float quantity,
            String unit, Integer totalAmount) {
        Statement stmt = null, stmt1 = null;
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt.executeUpdate(String.format("insert ignore into unit values (null, '%s')", unit));
            ResultSet rs = stmt1.executeQuery(String.format("select id from unit where name = '%s'", unit));
            while (rs.next()) {
                unit = String.valueOf(rs.getInt(Field.Category.ID.GetIdx()));
            }

            stmt.executeUpdate(String.format("INSERT INTO Cost VALUES (null, '%s', '%s', '%s', %.2f, %s, %d)",
                    (type == "Goods") ? "G" : "O", LocalDate.parse(date), description, quantity, unit, totalAmount));
            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void EditCost(int id, String type, String date, String description, Float quantity,
            String unit, Integer totalAmount) {
        Statement stmt = null, stmt1 = null;

        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt.executeUpdate(String.format("insert ignore into unit values (null, '%s')", unit));
            ResultSet rs = stmt1.executeQuery(String.format("select id from unit where name = '%s'", unit));
            while (rs.next()) {
                unit = String.valueOf(rs.getInt(Field.Category.ID.GetIdx()));
            }

            stmt.executeUpdate(String.format("UPDATE Cost SET Type = '%s', DateCost = '%s', Description = '%s', Quantity = %.2f, UnitID = %s, TotalAmount = %d WHERE ID = %d",
                    (type == "Goods") ? "G" : "O", LocalDate.parse(date), description, quantity, unit, totalAmount, id));
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
    
    public void DeletePayment(int id) {
        Statement stmt = null, stmt1 = null;
        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();
            stmt1.executeUpdate(String.format("DELETE FROM SpecificPayment WHERE PaymentID = %d", id));
            stmt.executeUpdate(String.format("DELETE FROM Payment WHERE ID = %d", id));
            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean readAllPayment(JTable tbl) {
        String query = "select * from payment order by DatePayment DESC, id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Payment.ID.GetIdx()),
                    rs.getObject(Field.Payment.DateTime.GetIdx(), LocalDateTime.class),
                    rs.getString(Field.Payment.Note.GetIdx()), rs.getInt(Field.Payment.TotalAmount.GetIdx())};
                tbl_model.addRow(objs);
            }
            
            for (int k = tbl.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean readSpecificPaymentById(JTable tbl_payment, String idPayment) {
        String query = String.format("select * from specificpayment sp join menu mn on sp.FoodID = mn.id where sp.PaymentID = %d", Integer.parseInt(idPayment));
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_payment_model = (DefaultTableModel) tbl_payment.getModel();
            tbl_payment_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getString(Field.SpecificPayment.FoodName.GetIdx()), rs.getInt(Field.SpecificPayment.Quantity.GetIdx())};
                tbl_payment_model.addRow(objs);
            }
            
            for (int k = tbl_payment.getRowCount(); k < 3; k++) {
                ((DefaultTableModel) tbl_payment.getModel()).addRow(new Object[] { "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean readAllGoodsCosts(JTable tbl) {
        String query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'G' order by c.DateCost DESC, c.id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                    rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                    rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                    rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                tbl_model.addRow(objs);
            }
            
            for (int k = tbl.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean readAllOperationCosts(JTable tbl) {
        String query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'O' order by c.DateCost DESC, c.id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                    rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                    rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                    rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                tbl_model.addRow(objs);
            }
            
            for (int k = tbl.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean readPaymentByDate(JTable tbl, Integer month, Integer year) {
        String query = "select * from payment where ";
        if (month > 0 && month < 13) {
            query += String.format("MONTH(DatePayment)= '%d'", month);
            if (year > 0) {
                query += String.format(" and YEAR(DatePayment)= '%d'", year);
            }
        } else {
            if (year > 0) {
                query += String.format("YEAR(DatePayment)= '%d'", year);
            } else {
                return true;
            }
        }
        query += " order by DatePayment DESC, id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Payment.ID.GetIdx()),
                    rs.getObject(Field.Payment.DateTime.GetIdx(), LocalDateTime.class),
                    rs.getString(Field.Payment.Note.GetIdx()), rs.getInt(Field.Payment.TotalAmount.GetIdx())};
                tbl_model.addRow(objs);
            }
            
            for (int k = tbl.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "" });
            }
            
            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean readCostsByDate(JTable tbl, Integer month, Integer year, boolean isGoods) {
        String query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'G'";
        if (!isGoods) {
            query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'O'";
        }
        if (month > 0 && month < 13) {
            query += String.format(" and MONTH(c.DateCost)= %d", month);
            if (year > 0) {
                query += String.format(" and YEAR(c.DateCost)= %d", year);
            }
        } else {
            if (year > 0) {
                query += String.format(" and YEAR(c.DateCost)= %d", year);
            } else {
                return true;
            }
        }
        query += " order by c.DateCost DESC, c.id";
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_model = (DefaultTableModel) tbl.getModel();
            tbl_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                    rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                    rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                    rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                tbl_model.addRow(objs);
            }
            
            for (int k = tbl.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl.getModel()).addRow(new Object[] { "", "", "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean searchCost(JTable tbl_cost, String keyword) {
        if (keyword.isEmpty()) {
            ReadAllCosts(tbl_cost);
            return true;
        }

        String query = "", query1 = "";

        if (keyword.toLowerCase().equals("goods")) {
            query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'G' order by c.DateCost DESC, c.id";
        } else if (keyword.toLowerCase().equals("operation")) {
            query = "select * from cost c join unit u on c.UnitID = u.id where c.type = 'O' order by c.DateCost DESC, c.id";
        } else {
            query = "select * from cost c join unit u on c.UnitID = u.id where"
                    + String.format(" MATCH(c.description) AGAINST ('%s' IN NATURAL LANGUAGE MODE)", keyword)
                    + " ORDER by c.DateCost DESC, c.id";

            query1 = "select * from cost c join unit u on c.UnitID = u.id where"
                    + String.format(" MATCH(u.name) AGAINST ('%s' IN NATURAL LANGUAGE MODE)", keyword)
                    + " ORDER by c.DateCost DESC, c.id";
        }

        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_cost_model = (DefaultTableModel) tbl_cost.getModel();
            tbl_cost_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                    rs.getString(Field.Cost.Type.GetIdx()).equals("G") ? "Goods" : "Operation",
                    rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                    rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                    rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                tbl_cost_model.addRow(objs);
            }

            if (!query1.isEmpty()) {
                rs = stmt.executeQuery(query1);

                while (rs.next()) {
                    Object[] objs = new Object[]{rs.getInt(Field.Cost.ID.GetIdx()),
                        rs.getString(Field.Cost.Type.GetIdx()).equals("G") ? "Goods" : "Operation",
                        rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
                        rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
                        rs.getString(Field.Cost.UnitName.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx())};
                    tbl_cost_model.addRow(objs);
                }
            }
            
            for (int k = tbl_cost.getRowCount(); k < 18; k++) {
                ((DefaultTableModel) tbl_cost.getModel()).addRow(new Object[] { "", "", "", "", "", "", "" });
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public String totalAmountPayment(Integer month, Integer year) {
        String query = "select SUM(TotalAmount) as sumTA from payment";
        if (month > 0 && month < 13) {
            query += String.format(" where MONTH(DatePayment)= '%d'", month);
            if (year > 0) {
                query += String.format(" and YEAR(DatePayment)= '%d'", year);
            }
        } else {
            if (year > 0) {
                query += String.format(" where YEAR(DatePayment)= '%d'", year);
            }
        }

        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Integer sum = 0;
            while (rs.next()) {
                sum = rs.getInt(1);
            }

            stmt.close();
            return String.valueOf(sum);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
    }

    public String totalAmountGoodsCost(Integer month, Integer year) {
        String query = "select SUM(TotalAmount) as sumTA from cost where type = 'G'";
        if (month > 0 && month < 13) {
            query += String.format(" and MONTH(DateCost)= %d", month);
        }
        if (year > 0) {
            query += String.format(" and YEAR(DateCost)= %d", year);
        }

        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Integer sum = 0;
            while (rs.next()) {
                sum = rs.getInt(1);
            }

            stmt.close();
            return String.valueOf(sum);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
    }

    public String totalAmountOperationCost(Integer month, Integer year) {
        String query = "select SUM(TotalAmount) as sumTA from cost where type = 'O'";
        if (month > 0 && month < 13) {
            query += String.format(" and MONTH(DateCost)= %d", month);
        }
        if (year > 0) {
            query += String.format(" and YEAR(DateCost)= %d", year);
        }

        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Integer sum = 0;
            while (rs.next()) {
                sum = rs.getInt(1);
            }

            stmt.close();
            return String.valueOf(sum);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        }
    }

//    public boolean ReadAllCombos(JTable tbl_menu) {
//        Statement stmt = null;
//        Statement stmt1 = null;
//        
//        try {
//            stmt = this.conn.createStatement();
//            stmt1 = this.conn.createStatement();
//            ResultSet rs = stmt.executeQuery("select * from menu where type = 'C'");
//
//            while (rs.next()) {
//                
//                
//                
//                
//                
//                Combo curCb = new Combo(rs.getInt(Field.Menu.ID.GetIdx()), rs.getString(Field.Menu.Name.GetIdx()),
//                        rs.getString(Field.Menu.SpecificTypeID.GetIdx()), rs.getFloat(Field.Menu.Discount.GetIdx()),
//                        rs.getInt(Field.Menu.Price.GetIdx()));
//                combos.add(curCb);
//
//                ResultSet rs1 = stmt1
//                        .executeQuery(String.format("select * from combo c where c.comboid = %s", curCb.GetID()));
//
//                while (rs1.next()) {
//                    curCb.addDish(rs1.getInt(Field.Combo.DishID.GetIdx()), rs1.getInt(Field.Combo.Quantity.GetIdx()));
//                }
//            }
//            return true;
//
//            stmt.close();
//            stmt1.close();
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return false;
//        }
//
//    }
    public boolean readComboDetail(int comboID, JTable tbl_detail) {
        String query = String.format("select * from combo cb join menu mn on cb.dishid = mn.id where comboid = %s", comboID);
        Statement stmt;
        try {
            stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            DefaultTableModel tbl_detail_model = (DefaultTableModel) tbl_detail.getModel();
            tbl_detail_model.setRowCount(0);
            while (rs.next()) {
                Object[] objs = new Object[]{String.valueOf(rs.getInt(Field.ComboDetail.DishID.GetIdx())),
                    rs.getString(Field.ComboDetail.DishName.GetIdx()),
                    rs.getString(Field.ComboDetail.Quantity.GetIdx())};
                tbl_detail_model.addRow(objs);
            }

            int rowCount = tbl_detail_model.getRowCount();
            for (int i = 0; i < 6 - rowCount; i++) {
                tbl_detail_model.addRow(new Object[]{"", "", ""});
            }

            stmt.close();
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public void createCombo(String name, String cat, String type, String spec, String price, String dis, ArrayList<String> dishesID, ArrayList<String> quantities) {
        Statement stmt = null, stmt1 = null;

        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();

            // category
            stmt.executeUpdate(String.format("insert ignore into category values (null, '%s')", cat));
            ResultSet rs = stmt1.executeQuery(String.format("select id from category where name = '%s'", cat));
            while (rs.next()) {
                cat = String.valueOf(rs.getInt(Field.Category.ID.GetIdx()));
            }

            // specific type
            stmt.executeUpdate(String.format("insert ignore into specifictype values (null, '%s')", spec));
            rs = stmt1.executeQuery(String.format("select id from specifictype where name = '%s'", spec));
            while (rs.next()) {
                spec = String.valueOf(rs.getInt(Field.SpecificType.ID.GetIdx()));
            }

            type = type.equals("Dish") ? "D" : "C";
            stmt.executeUpdate(String.format("insert ignore into menu values (null, '%s', %s, %s, '%s', %s, %s)",
                    type, cat, spec, name, price, dis));

            // select latest added row
            rs = stmt.executeQuery("select * from menu order by menu.id desc limit 1");
            String comboID = "";
            while (rs.next()) {
                comboID = String.valueOf(rs.getInt(Field.Menu.ID.GetIdx()));
                break;
            }

            // List dishes in combo
            updateListInCombo(comboID, dishesID, quantities);

            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDish(String id) {
        Statement stmt = null;

        try {
            stmt = this.conn.createStatement();

            stmt.executeUpdate(String.format("delete from combo where dishID = %s", id));
            stmt.executeUpdate(String.format("delete from menu where id = %s", id));

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCombo(String id) {

        Statement stmt = null;

        try {
            stmt = this.conn.createStatement();
            stmt.executeUpdate(String.format("delete from combo where comboID = %s", id));
            stmt.executeUpdate(String.format("delete from menu where id = %s", id));

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createDish(String name, String cat, String type, String spec, String price, String dis) {
        Statement stmt = null, stmt1 = null;

        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();

            // category
            stmt.executeUpdate(String.format("insert ignore into category values (null, '%s')", cat));
            ResultSet rs = stmt1.executeQuery(String.format("select id from category where name = '%s'", cat));
            while (rs.next()) {
                cat = String.valueOf(rs.getInt(Field.Category.ID.GetIdx()));
            }

            // specific type
            stmt.executeUpdate(String.format("insert ignore into specifictype values (null, '%s')", spec));
            rs = stmt1.executeQuery(String.format("select id from specifictype where name = '%s'", spec));
            while (rs.next()) {
                spec = String.valueOf(rs.getInt(Field.SpecificType.ID.GetIdx()));
            }

            type = type.equals("Dish") ? "D" : "C";
            stmt.executeUpdate(String.format("insert ignore into menu values (null, '%s', %s, %s, '%s', %s, %s)",
                    type, cat, spec, name, price, dis));

            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//
//    public ArrayList<Order> ReadOrderBy(ReadBy readBy, LocalDateTime date) {
//        ArrayList<Order> orders = new ArrayList<Order>();
//
//        try {
//            Statement stmt = conn.createStatement();
//            Statement stmt1 = conn.createStatement();
//
//            String query = "";
//
//            switch (readBy) {
//                case DAY: {
//                    query = String.format("select * from payment where convert(datepayment, date) = '%s-%s-%s'",
//                            date.getYear(), date.getMonthValue(), date.getDayOfMonth());
//
//                    break;
//                }
//                case MONTH: {
//                    query = String.format(
//                            "select * from payment where month(convert(datepayment, date)) = %s and year(convert(datepayment, date)) = %s",
//                            date.getMonthValue(), date.getYear());
//
//                    break;
//                }
//
//                case YEAR: {
//                    query = String.format("select * from payment where year(convert(datepayment, date)) = %s",
//                            date.getYear());
//
//                    break;
//                }
//            }
//
//            ResultSet rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                Order curOr = new Order(rs.getInt(Field.Payment.ID.GetIdx()),
//                        rs.getObject(Field.Payment.DateTime.GetIdx(), LocalDateTime.class),
//                        rs.getString(Field.Payment.Note.GetIdx()));
//                orders.add(curOr);
//
//                ResultSet rs1 = stmt1.executeQuery(
//                        String.format("select * from specificpayment where paymentid = %s", curOr.GetID()));
//
//                while (rs1.next()) {
//                    curOr.addFood(rs.getInt(Field.SpecificPayment.FoodID.GetIdx()),
//                            rs.getInt(Field.SpecificPayment.Quantity.GetIdx()));
//                }
//            }
//
//            stmt.close();
//            stmt1.close();
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return orders;
//    }
//
//    public ArrayList<Cost> ReadCostBy(ReadBy readBy, LocalDateTime date) {
//        ArrayList<Cost> costs = new ArrayList<Cost>();
//
//        try {
//            Statement stmt = conn.createStatement();
//            Statement stmt1 = conn.createStatement();
//
//            String query = "";
//
//            switch (readBy) {
//                case DAY: {
//                    query = String.format("select * from payment where convert(datepayment, date) = '%s-%s-%s'",
//                            date.getYear(), date.getMonthValue(), date.getDayOfMonth());
//
//                    break;
//                }
//                case MONTH: {
//                    query = String.format(
//                            "select * from payment where month(convert(datepayment, date)) = %s and year(convert(datepayment, date)) = %s",
//                            date.getMonthValue(), date.getYear());
//
//                    break;
//                }
//
//                case YEAR: {
//                    query = String.format("select * from payment where year(convert(datepayment, date)) = %s",
//                            date.getYear());
//
//                    break;
//                }
//            }
//
//            ResultSet rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                Cost curCost = new Cost(rs.getInt(Field.Cost.ID.GetIdx()),
//                        rs.getString(Field.Cost.Type.GetIdx()).charAt(0),
//                        rs.getObject(Field.Cost.Date.GetIdx(), LocalDate.class),
//                        rs.getString(Field.Cost.Description.GetIdx()), rs.getFloat(Field.Cost.Quantity.GetIdx()),
//                        rs.getInt(Field.Cost.UnitID.GetIdx()), rs.getInt(Field.Cost.TotalAmount.GetIdx()));
//
//                costs.add(curCost);
//            }
//
//            stmt.close();
//            stmt1.close();
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return costs;
//    }
//
//    public void WriteOrder(Order order) {
//        Statement stmt = null;
//        Statement stmt1 = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt1 = this.conn.createStatement();
//            stmt.executeUpdate(String.format("INSERT INTO Payment VALUES (null, '%s', '%s', null)",
//                    order.getListDateTime(), order.getNote()));
//            int id = order.GetID();
//            ArrayList<Integer> list_foodID = order.getListFoodID();
//            ArrayList<Integer> quantity = order.getListQuantity();
//            for (int i = 0; i < list_foodID.size(); i++) {
//                stmt1.executeUpdate(String.format("INSERT INTO SpecificPayment VALUES (%d, '%d', '%d')", id,
//                        list_foodID.get(i), quantity.get(i)));
//            }
//            stmt.close();
//            stmt1.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void WriteCost(Cost cost) {
//        Statement stmt = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt.executeUpdate(String.format("INSERT INTO Cost VALUES (null, '%s', '%s', '%s', %.2f, %d, %d)",
//                    cost.getType(), cost.getDate(), cost.getDescription(), cost.getQuantity(), cost.getUnitId(),
//                    cost.getTotalAmount()));
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void WriteDish(Dish dish) {
//        Statement stmt = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt.executeUpdate(String.format("INSERT INTO Menu VALUES (null, 'D', %d, %d, '%s', %d, %.2f)",
//                    dish.getCategory(), dish.getSpecificType(), dish.getName(), dish.getPrice(), dish.getDiscount()));
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void WriteCombo(Combo combo) {
//        Statement stmt = null;
//        Statement stmt1 = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt1 = this.conn.createStatement();
//            stmt.executeUpdate(
//                    String.format("INSERT INTO Menu VALUES (null, 'C', %d, %d, '%s', %d, %.2f)", combo.getCategory(),
//                            combo.getSpecificType(), combo.getName(), combo.getPrice(), combo.getDiscount()));
//            int id = combo.GetID();
//            ArrayList<Integer> array_dishID = combo.getArrayDishID();
//            ArrayList<Integer> quantity = combo.getArrayQuantity();
//            for (int i = 0; i < array_dishID.size(); i++) {
//                stmt1.executeUpdate(String.format("INSERT INTO SpecificPayment VALUES (%d, '%d', '%d')", id,
//                        array_dishID.get(i), quantity.get(i)));
//            }
//            stmt.close();
//            stmt1.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    public void updateFood(String id, String name, String cat, String type, String spec, String price, String dis) {
        Statement stmt = null, stmt1 = null;

        try {
            stmt = this.conn.createStatement();
            stmt1 = this.conn.createStatement();

            // category
            stmt.executeUpdate(String.format("insert ignore into category values (null, '%s')", cat));
            ResultSet rs = stmt1.executeQuery(String.format("select id from category where name = '%s'", cat));
            while (rs.next()) {
                cat = String.valueOf(rs.getInt(Field.Category.ID.GetIdx()));
            }

            // specific type
            stmt.executeUpdate(String.format("insert ignore into specifictype values (null, '%s')", spec));
            rs = stmt1.executeQuery(String.format("select id from specifictype where name = '%s'", spec));
            while (rs.next()) {
                spec = String.valueOf(rs.getInt(Field.SpecificType.ID.GetIdx()));
            }

            type = type.equals("Dish") ? "D" : "C";
            stmt.executeUpdate(String.format("update menu set type = '%s', categoryid = %s, specifictypeid = %s, name = '%s', price = %s, discount = %s where id = %s",
                    type, cat, spec, name, price, dis, id));

            stmt.close();
            stmt1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateListInCombo(String comboID, ArrayList<String> dishesID, ArrayList<String> quantities) {
        Statement stmt = null;

        if (dishesID.size() > 0 && quantities.size() > 0) {
            try {
                stmt = this.conn.createStatement();

                stmt.executeUpdate(String.format("delete from combo where comboID = %s", comboID));
                for (int i = 0; i < dishesID.size(); i++) {
                    stmt.executeUpdate(String.format("insert ignore into combo values (%s, %s, %s)", comboID, dishesID.get(i), quantities.get(i)));
                }

                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

//
//    public void DeleteCost(int id) {
//        Statement stmt = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt.executeUpdate(String.format("DELETE FROM Cost WHERE ID = %d", id));
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void DeleteOrder(int id) {
//        Statement stmt = null;
//        Statement stmt1 = null;
//        try {
//            stmt = this.conn.createStatement();
//            stmt1 = this.conn.createStatement();
//            stmt.executeUpdate(String.format("DELETE FROM SpecificPayment WHERE ID = %d", id));
//            stmt1.executeUpdate(String.format("DELETE FROM Payment WHERE ID = %d", id));
//            stmt.close();
//            stmt1.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void EditCost(int id, String attribute_name, String value) {
//        Statement stmt = null;
//        try {
//            if (attribute_name == "Type" || attribute_name == "Description" || attribute_name == "Quantity"
//                    || attribute_name == "UnitID" || attribute_name == "TotalAmount" || attribute_name == "DateCost") {
//                stmt = this.conn.createStatement();
//                stmt.executeUpdate(String.format("UPDATE Cost SET %s = '%s' WHERE ID = %d", attribute_name, value, id));
//                stmt.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void EditDish(int id, String attribute_name, String value) {
//        Statement stmt = null;
//        try {
//            if (attribute_name == "CategoryID" || attribute_name == "SpecificTypeID" || attribute_name == "Name"
//                    || attribute_name == "Price" || attribute_name == "Discount") {
//                stmt = this.conn.createStatement();
//                stmt.executeUpdate(String.format("UPDATE Menu SET %s = '%s' WHERE ID = %d AND Type = 'D",
//                        attribute_name, value, id));
//                stmt.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void EditCombo(int id, String attribute_name, String value) {
//        Statement stmt = null;
//        try {
//            stmt = this.conn.createStatement();
//            if (attribute_name == "Quantity") {
//                stmt.executeUpdate(
//                        String.format("UPDATE Combo SET %s = '%s' WHERE ID = %d", attribute_name, value, id));
//            }
//            if (attribute_name == "Name" || attribute_name == "Price" || attribute_name == "Discount") {
//                stmt.executeUpdate(String.format("UPDATE Menu SET %s = '%s' WHERE ID = %d AND Type = 'C'",
//                        attribute_name, value, id));
//            }
//            stmt.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
