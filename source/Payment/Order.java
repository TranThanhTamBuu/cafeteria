package source.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

import source.Food.Food;

public class Order {
    private ArrayList<Food> list;
    private ArrayList<Integer> quantity;
    private LocalDateTime date_time;
    private String note;

    public Order() {
        this.list = new ArrayList<>();
        this.quantity = new ArrayList<>();
        this.date_time = LocalDateTime.now();
        this.note = "";
    }

    public Order addOrder(Food food) {
        for (Food f : list) {
            if (f == food) {
                increaseQuantity(this.list.indexOf(f));
                return this;
            }
        }

        this.list.add(food);
        this.quantity.add(1);

        return this;
    }

    public Order removeOrder(int index) {
        this.list.remove(index);
        this.quantity.remove(index);
        return this;
    }

    public Order increaseQuantity(int index) {
        this.quantity.set(index, this.quantity.get(index) + 1);
        return this;
    }

    public Order decreaseQuantity(int index) {
        int n = this.quantity.get(index);

        if (n - 1 == 0)
            removeOrder(index);
        else
            this.quantity.set(index, n - 1);

        return this;
    }

    public Order modifyNote(String note) {
        this.note = note;
        return this;
    }

    public Long calcOrder() {
        Long sum = 0L;

        for (Food f : list) {
            sum += f.cost();
        }

        return sum;
    }

    public void writeOrder() {
        // TODO: implement this function after designing database
    }

}
