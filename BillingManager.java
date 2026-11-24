
package com.pharmacare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BillingManager {

    private MedicineManager medicineManager;
    private List<Bill> bills;
    private int billCounter;

    // Constructor
    public BillingManager(MedicineManager medicineManager) {

        this.medicineManager = medicineManager;
        this.bills = BillFileStorage.loadBills();

        billCounter = bills.size() + 1;
    }

    // Generate a new bill
    public Bill generateBill(String customerName,
                             String customerPhone,
                             List<BillItem> purchasedItems) {

        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter customer name.");
        }

        if (purchasedItems == null || purchasedItems.isEmpty()) {
            throw new IllegalArgumentException("Add at least one medicine.");
        }

        // Validate all medicines before changing stock
        for (BillItem item : purchasedItems) {

            Medicine medicine =
                    medicineManager.searchById(item.getMedicineId());

            if (medicine == null) {
                throw new IllegalArgumentException(
                        "Medicine not found: " + item.getMedicineName()
                );
            }

            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "Invalid quantity for " + item.getMedicineName()
                );
            }

            if (medicine.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for " + medicine.getName()
                );
            }
        }

        // Create bill
        String billId = String.format("B%04d", billCounter++);

        String billDate = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        );

        Bill bill = new Bill(
                billId,
                customerName.trim(),
                customerPhone == null ? "" : customerPhone.trim(),
                billDate
        );

        for (BillItem item : purchasedItems) {
            bill.addItem(item);
        }

        // Save bill before deducting stock
        BillFileStorage.saveBill(bill);

        // Deduct purchased quantities
        for (BillItem item : purchasedItems) {

            Medicine medicine =
                    medicineManager.searchById(item.getMedicineId());

            medicine.setQuantity(
                    medicine.getQuantity() - item.getQuantity()
            );

            medicineManager.updateMedicine(medicine);
        }

        bills.add(bill);

        return bill;
    }

    // Get all bills
    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    // Get total sales
    public double getTotalSales() {

        double total = 0;

        for (Bill bill : bills) {
            total += bill.getTotalAmount();
        }

        return total;
    }

    // Get total number of bills
    public int getTotalBills() {
        return bills.size();
    }

}
