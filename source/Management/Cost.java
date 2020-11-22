package source.Management;

import java.util.Date;

public class Cost {
    protected String typeId;
    protected String name;
    protected Integer unitId;
    protected Integer costAmount;
    protected Date date;

    public Cost(String typeId, String name, Integer unitId, Integer costAmount, Date date) {
        this.typeId = typeId;
        this.name = name;
        this.unitId = unitId;
        this.costAmount = costAmount;
        this.date = date;
    }

    public Cost modifyTypeId(String typeId) {
        this.typeId = typeId;
        return this;
    }
    
    public Cost modifyName(String name) {
        this.name = name;
        return this;
    }

    public Cost modifyUnitId(Integer unitId) {
        this.unitId = unitId;
        return this;
    }

    public Cost modifyCostAmount(Integer costAmount) {
        this.costAmount = costAmount;
        return this;
    }

    public Cost modifyDate(Date date) {
        this.date = date;
        return this;
    }

    public String getTypeId() {
        return this.typeId;
    }

    public String getName() {
        return this.name;
    }

    public Integer getUnitId() {
        return this.unitId;
    }

    public Integer getCostAmount() {
        return this.costAmount;
    }

    public Date getDate() {
        return this.date;
    }
}
