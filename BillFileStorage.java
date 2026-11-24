
package com.pharmacare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BillFileStorage {

    private static final String FILE_PATH = "data/bills.txt";

    // Save a bill to file
    public static void saveBill(Bill bill) {

        File file = new File(FILE_PATH);

        // Create data folder if it does not exist
        file.getParentFile().mkdirs();

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file, true))) {

            writer.write("BILL|" + bill.getBillId() + "|"
                    + bill.getCustomerName() + "|"
                    + bill.getCustomerPhone() + "|"
                    + bill.getBillDate() + "|"
                    + bill.getTotalAmount());

            writer.newLine();

            for (BillItem item : bill.getItems()) {

                writer.write("ITEM|" + item.getMedicineId() + "|"
                        + item.getMedicineName() + "|"
                        + item.getQuantity() + "|"
                        + item.getUnitPrice());

                writer.newLine();
            }

            writer.write("END");
            writer.newLine();

        } catch (IOException e) {
            throw new RuntimeException("Error saving bill: " + e.getMessage(), e);
        }
    }

    // Load all saved bills
    public static List<Bill> loadBills() {

        List<Bill> bills = new ArrayList<>();

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return bills;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;
            Bill currentBill = null;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts[0].equals("BILL") && parts.length == 6) {

                    currentBill = new Bill(
                            parts[1],
                            parts[2],
                            parts[3],
                            parts[4]
                    );

                } else if (parts[0].equals("ITEM")
                        && parts.length == 5 && currentBill != null) {

                    BillItem item = new BillItem(
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]),
                            Double.parseDouble(parts[4])
                    );

                    currentBill.addItem(item);

                } else if (parts[0].equals("END")
                        && currentBill != null) {

                    bills.add(currentBill);
                    currentBill = null;
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Error loading bills: " + e.getMessage(), e);
        }

        return bills;
    }
}
