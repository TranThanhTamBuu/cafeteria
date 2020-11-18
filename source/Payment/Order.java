package source.Payment;

import java.time.LocalDateTime;
import java.util.ArrayList;

import source.Food.Food;

public class Order {
    private ArrayList<Food> list;
    private ArrayList<Integer> amount;
    private LocalDateTime date_time;
    private String note;

    public Order() {
        this.list = new ArrayList<>();
        this.amount = new ArrayList<>();
        this.date_time = LocalDateTime.now();
        this.note = "";
    }

    public Order addOrder(Food food) {
        for (Food f : list) {
            if (f == food) {
                increaseAmount(this.list.indexOf(f));
                return this;
            }
        }

        this.list.add(food);
        this.amount.add(1);

        return this;
    }

    public Order removeOrder(int index) {
        this.list.remove(index);
        this.amount.remove(index);
        return this;
    }

    public Order increaseAmount(int index) {
        this.amount.set(index, this.amount.get(index) + 1);
        return this;
    }

    public Order decreaseAmount(int index) {
        int n = this.amount.get(index);

        if (n - 1 == 0)
            removeOrder(index);
        else
            this.amount.set(index, n - 1);

        return this;
    }

    public Order modifyNote(String note) {
        this.note = note;
        return this;
    }

    public long calcOrder() {
        long sum = 0;

        for (Food f : list) {
            sum += (long) f.getPrice() * f.getDiscount();
        }

        return sum;
    }

    public void writeOrder() {
        // TODO: implement this function after designing database
    }
}
