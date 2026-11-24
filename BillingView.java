
package com.pharmacare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.List;

public class BillingView {

    private MedicineManager medicineManager;
    private BillingManager billingManager;

    private TextField customerNameField;
    private TextField customerPhoneField;
    private ComboBox<Medicine> medicineComboBox;
    private TextField quantityField;

    private TableView<BillItem> billTable;
    private ObservableList<BillItem> billItems;

    private Label totalLabel;

    public BillingView(MedicineManager medicineManager,
                       BillingManager billingManager) {

        this.medicineManager = medicineManager;
        this.billingManager = billingManager;

        billItems = FXCollections.observableArrayList();
    }

    public VBox getView() {

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f9;");

        Label heading = new Label("Pharmacy Billing");
        heading.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold;"
        );

        // Customer details
        Label customerHeading = new Label("Customer Details");
        customerHeading.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        customerNameField = new TextField();
        customerNameField.setPromptText("Customer Name");

        customerPhoneField = new TextField();
        customerPhoneField.setPromptText("Phone Number");

        HBox customerBox = new HBox(10,
                customerNameField, customerPhoneField
        );

        // Medicine selection
        Label medicineHeading = new Label("Add Medicines");
        medicineHeading.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        medicineComboBox = new ComboBox<>();
        medicineComboBox.setPromptText("Select Medicine");
        medicineComboBox.setPrefWidth(250);

        quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.setPrefWidth(100);

        Button addButton = new Button("Add Medicine");
        addButton.setStyle(
                "-fx-background-color: #2563eb; -fx-text-fill: white;"
        );
        addButton.setOnAction(e -> addMedicineToBill());

        HBox medicineBox = new HBox(10,
                medicineComboBox, quantityField, addButton
        );

        medicineBox.setAlignment(Pos.CENTER_LEFT);

        // Bill table
        billTable = new TableView<>();
        billTable.setItems(billItems);
        billTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<BillItem, String> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMedicineId()
                )
        );

        TableColumn<BillItem, String> nameColumn =
                new TableColumn<>("Medicine");

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getMedicineName()
                )
        );

        TableColumn<BillItem, Integer> quantityColumn =
                new TableColumn<>("Quantity");

        quantityColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getQuantity()
                ).asObject()
        );

        TableColumn<BillItem, Double> priceColumn =
                new TableColumn<>("Unit Price");

        priceColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getUnitPrice()
                ).asObject()
        );

        TableColumn<BillItem, Double> totalColumn =
                new TableColumn<>("Total");

        totalColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getTotalPrice()
                ).asObject()
        );

        billTable.getColumns().addAll(
                idColumn, nameColumn, quantityColumn,
                priceColumn, totalColumn
        );

        // Remove medicine button
        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> removeSelectedMedicine());

        // Total amount
        totalLabel = new Label("Grand Total: Rs. 0.00");
        totalLabel.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;"
        );

        // Generate bill button
        Button generateButton = new Button("Generate Bill");
        generateButton.setStyle(
                "-fx-background-color: #16a34a; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 15px; " +
                "-fx-padding: 10 25;"
        );

        generateButton.setOnAction(e -> generateBill());

        HBox bottomBox = new HBox(20,
                removeButton, totalLabel, generateButton
        );

        bottomBox.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(
                heading,
                customerHeading,
                customerBox,
                medicineHeading,
                medicineBox,
                billTable,
                bottomBox
        );

        refreshMedicineList();

        return root;
    }

    // Refresh available medicine list
    public void refreshMedicineList() {

        if (medicineComboBox == null) {
            return;
        }

        medicineComboBox.setItems(
                FXCollections.observableArrayList(
                        medicineManager.getAllMedicines().stream()
                                .filter(m -> m.getQuantity() > 0)
                                .toList()
                )
        );

        medicineComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Medicine medicine, boolean empty) {
                super.updateItem(medicine, empty);

                if (empty || medicine == null) {
                    setText(null);
                } else {
                    setText(
                            medicine.getName() +
                            " (Stock: " + medicine.getQuantity() +
                            ") - Rs. " + medicine.getPrice()
                    );
                }
            }
        });

        medicineComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Medicine medicine, boolean empty) {
                super.updateItem(medicine, empty);

                setText(
                        empty || medicine == null
                                ? "Select Medicine"
                                : medicine.getName()
                );
            }
        });
    }

    // Add medicine to bill
    private void addMedicineToBill() {

        Medicine medicine = medicineComboBox.getValue();

        if (medicine == null) {
            showAlert("Please select a medicine.");
            return;
        }

        try {

            int quantity = Integer.parseInt(
                    quantityField.getText().trim()
            );

            if (quantity <= 0) {
                showAlert("Quantity must be greater than zero.");
                return;
            }

            int alreadyAdded = 0;

            for (BillItem item : billItems) {
                if (item.getMedicineId().equals(medicine.getId())) {
                    alreadyAdded += item.getQuantity();
                }
            }

            if (alreadyAdded + quantity > medicine.getQuantity()) {
                showAlert("Insufficient stock available.");
                return;
            }

            BillItem item = new BillItem(
                    medicine.getId(),
                    medicine.getName(),
                    quantity,
                    medicine.getPrice()
            );

            billItems.add(item);

            updateTotal();

            quantityField.clear();
            medicineComboBox.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid quantity.");
        }
    }

    // Remove selected medicine
    private void removeSelectedMedicine() {

        BillItem selected = billTable.getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showAlert("Please select a medicine to remove.");
            return;
        }

        billItems.remove(selected);
        updateTotal();
    }

    // Update total amount
    private void updateTotal() {

        double total = 0;

        for (BillItem item : billItems) {
            total += item.getTotalPrice();
        }

        totalLabel.setText(
                String.format("Grand Total: Rs. %.2f", total)
        );
    }

    // Generate final bill
    private void generateBill() {

        if (customerNameField.getText().trim().isEmpty()) {
            showAlert("Please enter customer name.");
            return;
        }

        if (billItems.isEmpty()) {
            showAlert("Please add medicines to the bill.");
            return;
        }

        try {

            List<BillItem> items = new ArrayList<>(billItems);

            Bill bill = billingManager.generateBill(
                    customerNameField.getText(),
                    customerPhoneField.getText(),
                    items
            );

            showAlert(
                    "Bill generated successfully!\n" +
                    "Bill ID: " + bill.getBillId() + "\n" +
                    "Customer: " + bill.getCustomerName() + "\n" +
                    String.format("Total: Rs. %.2f", bill.getTotalAmount())
            );

            billItems.clear();
            customerNameField.clear();
            customerPhoneField.clear();
            quantityField.clear();

            updateTotal();
            refreshMedicineList();

        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
            refreshMedicineList();
        } catch (RuntimeException e) {
            showAlert("Could not complete billing: " + e.getMessage());
            refreshMedicineList();
        }
    }

    // Display alert
    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PharmaCare");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
