package source.Payment;

import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import source.Database.Database;
import source.Food.Food;

public class Order {

    private int id;
    private ArrayList<Integer> list_foodID;
    private ArrayList<Integer> quantity;
    private LocalDateTime date_time;
    private String note;

    public Order(int id, LocalDateTime date_time, String note) {
        this.id = id;
        this.list_foodID = new ArrayList<>();
        this.quantity = new ArrayList<>();
        this.date_time = date_time;
        this.note = note;
    }

    public int GetID() {
        return this.id;
    }

    public ArrayList<Integer> getListFoodID() {
        return this.list_foodID;
    }

    public ArrayList<Integer> getListQuantity() {
        return this.quantity;
    }

    public LocalDateTime getListDateTime() {
        return this.date_time;
    }

    public String getNote() {
        return this.note;
    }

    public Order addFood(Integer foodID, int quantity) {
        for (Integer fID : list_foodID) {
            if (fID == foodID) {
                increaseQuantity(this.list_foodID.indexOf(fID), quantity);
                return this;
            }
        }

        this.list_foodID.add(foodID);
        this.quantity.add(1);

        return this;
    }

    public Order removeFood(int index) {
        this.list_foodID.remove(index);
        this.quantity.remove(index);
        return this;
    }

    public Order increaseQuantity(int index, int quantity) {
        this.quantity.set(index, this.quantity.get(index) + quantity);
        return this;
    }

    public Order decreaseQuantity(int index) {
        int n = this.quantity.get(index);

        if (n - 1 == 0)
            removeFood(index);
        else
            this.quantity.set(index, n - 1);

        return this;
    }

    public Order modifyNote(String note) {
        this.note = note;
        return this;
    }

    // public Long calcOrder() {
    // Long sum = 0L;

    // for (Food f : list_foodID) {
    // sum += f.cost();
    // }

    // return sum;
    // }

    public void writeOrder() {
        Database db = Database.getInstance();
        db.WriteOrder(this);
    }

}
