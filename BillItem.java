
package com.pharmacare;

public class BillItem {

    private String medicineId;
    private String medicineName;
    private int quantity;
    private double unitPrice;

    // Constructor
    public BillItem(String medicineId, String medicineName,
                    int quantity, double unitPrice) {

        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public String getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    // Calculate total price for this medicine
    public double getTotalPrice() {
        return quantity * unitPrice;
    }
}
