package source.Database;

public class Field {
    public static enum Menu {
        ID(1), Type(2), CategoryID(3), SpecificTypeID(4), 
        Name(5), Price(6), Discount(7), CategoryName(9), SpecificName(11) ;

        int idx;

        Menu(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum Category {
        ID(1), Name(2);

        int idx;

        Category(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }
    
    public static enum ComboDetail {
        DishID(2), Quantity(3), DishName(8);     

        int idx;

        ComboDetail(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum SpecificType {
        ID(1), Name(2);

        int idx;

        SpecificType(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum Combo {
        ID(1), DishID(2), Quantity(3);

        int idx;

        Combo(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum Payment {
        ID(1), DateTime(2), Note(3), TotalAmount(4);

        int idx;

        Payment(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum SpecificPayment {
        ID(1), FoodID(2), Quantity(3);

        int idx;

        SpecificPayment(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum Cost {
        ID(1), Type(2), Date(3), Description(4), Quantity(5), UnitID(6), TotalAmount(7), UnitName(9);

        int idx;

        Cost(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }

    public static enum Unit {
        ID(1), Name(2);

        int idx;

        Unit(int idx) {
            this.idx = idx;
        }

        public int GetIdx() {
            return idx;
        }
    }
}
