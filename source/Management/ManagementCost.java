package source.Management;

import java.util.ArrayList;

public class ManagementCost {
    private static final ManagementCost instance = new ManagementCost();

    private ManagementCost() {
    }

    public static ManagementCost getInstance() {
        return instance;
    }

    public ArrayList<Cost> readCost() {
        ArrayList<Cost> listCost = new ArrayList<Cost>();
        return listCost;
    }

    public void writeCost(Cost newCost) {
        return;
    }
}
