
package com.pharmacare;

import java.util.ArrayList;
import java.util.List;

public class MedicineManager {

    private List<Medicine> medicines;

    // Constructor - Load medicines from file
    public MedicineManager() {
        medicines = new ArrayList<>(
                FileStorage.loadMedicines()
        );
    }

    // Add medicine
    public boolean addMedicine(Medicine medicine) {

        if (searchById(medicine.getId()) != null) {
            return false;
        }

        medicines.add(medicine);
        saveData();

        return true;
    }

    // Update medicine
    public boolean updateMedicine(Medicine updatedMedicine) {

        for (int i = 0; i < medicines.size(); i++) {

            if (medicines.get(i).getId()
                    .equalsIgnoreCase(updatedMedicine.getId())) {

                medicines.set(i, updatedMedicine);
                saveData();

                return true;
            }
        }

        return false;
    }

    // Delete medicine
    public boolean deleteMedicine(String id) {

        Medicine medicine = searchById(id);

        if (medicine == null) {
            return false;
        }

        medicines.remove(medicine);
        saveData();

        return true;
    }

    // Search medicine by ID
    public Medicine searchById(String id) {

        for (Medicine medicine : medicines) {

            if (medicine.getId().equalsIgnoreCase(id)) {
                return medicine;
            }
        }

        return null;
    }

    // Search medicine by name
    public List<Medicine> searchByName(String name) {

        List<Medicine> results = new ArrayList<>();

        for (Medicine medicine : medicines) {

            if (medicine.getName()
                    .toLowerCase()
                    .contains(name.toLowerCase())) {

                results.add(medicine);
            }
        }

        return results;
    }

    // Get all medicines
    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicines);
    }

    // Save medicine data
    private void saveData() {
        FileStorage.saveMedicines(medicines);
    }

    // Get total number of medicines
    public int getTotalMedicines() {
        return medicines.size();
    }

    // Get total stock quantity
    public int getTotalStock() {

        int total = 0;

        for (Medicine medicine : medicines) {
            total += medicine.getQuantity();
        }

        return total;
    }
}
