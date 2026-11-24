
package com.pharmacare;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private static final String FILE_PATH = "data/medicines.txt";

    // Save all medicines to file
    public static void saveMedicines(List<Medicine> medicines) {

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            for (Medicine medicine : medicines) {

                writer.write(
                        medicine.getId() + "|" +
                        medicine.getName() + "|" +
                        medicine.getCategory() + "|" +
                        medicine.getManufacturer() + "|" +
                        medicine.getQuantity() + "|" +
                        medicine.getPrice() + "|" +
                        medicine.getExpiryDate()
                );

                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error saving medicines: " + e.getMessage(), e
            );
        }
    }

    // Load medicines from file
    public static List<Medicine> loadMedicines() {

        List<Medicine> medicines = new ArrayList<>();

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return medicines;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|", -1);

                if (parts.length == 7) {

                    Medicine medicine = new Medicine(
                            parts[0],
                            parts[1],
                            parts[2],
                            parts[3],
                            Integer.parseInt(parts[4]),
                            Double.parseDouble(parts[5]),
                            parts[6]
                    );

                    medicines.add(medicine);
                }
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(
                    "Error loading medicines: " + e.getMessage(), e
            );
        }

        return medicines;
    }
}
