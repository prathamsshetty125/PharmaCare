
package com.pharmacare;

import java.util.ArrayList;
import java.util.List;

public class Bill {

    private String billId;
    private String customerName;
    private String customerPhone;
    private String billDate;
    private List<BillItem> items;

    // Constructor
    public Bill(String billId, String customerName,
                String customerPhone, String billDate) {

        this.billId = billId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.billDate = billDate;
        this.items = new ArrayList<>();
    }

    // Add medicine to bill
    public void addItem(BillItem item) {
        items.add(item);
    }

    // Getters
    public String getBillId() {
        return billId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getBillDate() {
        return billDate;
    }

    public List<BillItem> getItems() {
        return items;
    }

    // Calculate total bill amount
    public double getTotalAmount() {

        double total = 0;

        for (BillItem item : items) {
            total += item.getTotalPrice();
        }

        return total;
    }
}
