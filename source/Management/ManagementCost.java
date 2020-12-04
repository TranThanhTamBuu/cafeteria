package source.Management;

import source.Database.Database;

public class ManagementCost {
    private static final ManagementCost instance = new ManagementCost();

    private ManagementCost() {
    }

    public static ManagementCost getInstance() {
        return instance;
    }

    public void writeCost(Cost newCost) {
        Database db = Database.getInstance();
        db.WriteCost(newCost);
    }
}
