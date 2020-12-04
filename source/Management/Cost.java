package source.Management;

import java.time.LocalDate;

public class Cost {
    private Integer id;
    private Character type;
    private LocalDate date;
    private String description;
    private Float quantity;
    private Integer unitId;
    private Integer totalAmount;

    public Cost(Integer id, Character type, LocalDate date, String description, Float quantity, Integer unitId,
            Integer totalAmount) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.description = description;
        this.quantity = quantity;
        this.unitId = unitId;
        this.totalAmount = totalAmount;
    }

    public Cost modifyType(Character type) {
        this.type = type;
        return this;
    }

    public Cost modifyDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public Cost modifyDescription(String description) {
        this.description = description;
        return this;
    }

    public Cost modifyQuantity(Float quantity) {
        this.quantity = quantity;
        return this;
    }

    public Cost modifyUnitId(Integer unitId) {
        this.unitId = unitId;
        return this;
    }

    public Cost modifyCostAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public Integer getId() {
        return this.id;
    }

    public Character getType() {
        return this.type;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getDescription() {
        return this.description;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public Integer getUnitId() {
        return this.unitId;
    }

    public Integer getTotalAmount() {
        return this.totalAmount;
    }
}
