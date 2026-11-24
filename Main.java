
package com.pharmacare;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    private MedicineManager medicineManager;
    private BillingManager billingManager;

    private BillingView billingView;
    private SalesHistoryView salesHistoryView;

    private TableView<Medicine> medicineTable;
    private ObservableList<Medicine> medicineList;

    private TextField idField;
    private TextField nameField;
    private TextField categoryField;
    private TextField manufacturerField;
    private TextField quantityField;
    private TextField priceField;
    private TextField expiryField;
    private TextField searchField;

    private Label dashboardMedicineLabel;
    private Label dashboardStockLabel;
    private Label dashboardBillsLabel;
    private Label dashboardSalesLabel;

    private VBox root;

    @Override
    public void start(Stage stage) {

        medicineManager = new MedicineManager();
        billingManager = new BillingManager(medicineManager);

        billingView = new BillingView(
                medicineManager, billingManager
        );

        salesHistoryView = new SalesHistoryView(billingManager);

        root = new VBox();

        root.getChildren().add(createHeader());

        TabPane tabPane = new TabPane();

        Tab dashboardTab = new Tab(
                "Dashboard", createDashboard()
        );

        Tab medicineTab = new Tab(
                "Medicine Management", createMedicineManagement()
        );

        Tab billingTab = new Tab(
                "Billing", billingView.getView()
        );

        Tab historyTab = new Tab(
                "Sales History", salesHistoryView.getView()
        );

        dashboardTab.setClosable(false);
        medicineTab.setClosable(false);
        billingTab.setClosable(false);
        historyTab.setClosable(false);

        tabPane.getTabs().addAll(
                dashboardTab,
                medicineTab,
                billingTab,
                historyTab
        );

        tabPane.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldTab, newTab) -> {

                    if (newTab == dashboardTab) {
                        refreshDashboard();
                    }

                    if (newTab == billingTab) {
                        billingView.refreshMedicineList();
                    }

                    if (newTab == historyTab) {
                        salesHistoryView.refreshHistory();
                    }

                    if (newTab == medicineTab) {
                        refreshMedicineTable();
                    }
                });

        root.getChildren().add(tabPane);

        Scene scene = new Scene(root, 1100, 750);

        stage.setTitle("PharmaCare - Pharmacy Billing System");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        stage.show();

        refreshDashboard();
    }

    // Header
    private HBox createHeader() {

        HBox header = new HBox();

        header.setPadding(new Insets(18, 25, 18, 25));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #17365D;");

        Label title = new Label("PharmaCare");
        title.setStyle(
                "-fx-text-fill: white; " +
                "-fx-font-size: 25px; " +
                "-fx-font-weight: bold;"
        );

        Label subtitle = new Label(
                "Pharmacy Management & Billing System"
        );

        subtitle.setStyle(
                "-fx-text-fill: #DCE6F1; -fx-font-size: 13px;"
        );

        VBox textBox = new VBox(3, title, subtitle);

        header.getChildren().add(textBox);

        return header;
    }

    // Dashboard
    private VBox createDashboard() {

        VBox dashboard = new VBox(25);

        dashboard.setPadding(new Insets(30));
        dashboard.setStyle("-fx-background-color: #f4f6f9;");

        Label heading = new Label("Dashboard");
        heading.setStyle(
                "-fx-font-size: 27px; -fx-font-weight: bold;"
        );

        HBox cards = new HBox(20);

        dashboardMedicineLabel = new Label();
        dashboardStockLabel = new Label();
        dashboardBillsLabel = new Label();
        dashboardSalesLabel = new Label();

        cards.getChildren().addAll(
                createCard("Total Medicines", dashboardMedicineLabel),
                createCard("Available Stock", dashboardStockLabel),
                createCard("Total Bills", dashboardBillsLabel),
                createCard("Total Sales", dashboardSalesLabel)
        );

        dashboard.getChildren().addAll(heading, cards);

        refreshDashboard();

        return dashboard;
    }

    // Dashboard card
    private VBox createCard(String title, Label valueLabel) {

        VBox card = new VBox(12);

        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setMinHeight(120);

        card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #dce1e8; " +
                "-fx-border-radius: 10;"
        );

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 14px; -fx-text-fill: #555;"
        );

        valueLabel.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold; " +
                "-fx-text-fill: #17365D;"
        );

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    // Refresh dashboard values
    private void refreshDashboard() {

        if (dashboardMedicineLabel == null) {
            return;
        }

        dashboardMedicineLabel.setText(
                String.valueOf(medicineManager.getTotalMedicines())
        );

        dashboardStockLabel.setText(
                String.valueOf(medicineManager.getTotalStock())
        );

        dashboardBillsLabel.setText(
                String.valueOf(billingManager.getTotalBills())
        );

        dashboardSalesLabel.setText(
                String.format(
                        "Rs. %.2f",
                        billingManager.getTotalSales()
                )
        );
    }

    // Medicine management screen
    private VBox createMedicineManagement() {

        VBox layout = new VBox(15);

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f4f6f9;");

        Label heading = new Label("Medicine Management");

        heading.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        idField = new TextField();
        nameField = new TextField();
        categoryField = new TextField();
        manufacturerField = new TextField();
        quantityField = new TextField();
        priceField = new TextField();
        expiryField = new TextField();

        idField.setPromptText("Medicine ID");
        nameField.setPromptText("Medicine Name");
        categoryField.setPromptText("Category");
        manufacturerField.setPromptText("Manufacturer");
        quantityField.setPromptText("Quantity");
        priceField.setPromptText("Price");
        expiryField.setPromptText("Expiry Date (YYYY-MM-DD)");

        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.addRow(0, new Label("ID:"), idField,
                new Label("Name:"), nameField);

        form.addRow(1, new Label("Category:"), categoryField,
                new Label("Manufacturer:"), manufacturerField);

        form.addRow(2, new Label("Quantity:"), quantityField,
                new Label("Price:"), priceField);

        form.addRow(3, new Label("Expiry Date:"), expiryField);

        Button addButton = new Button("Add Medicine");
        Button updateButton = new Button("Update Medicine");
        Button deleteButton = new Button("Delete Medicine");
        Button clearButton = new Button("Clear Fields");

        addButton.setOnAction(e -> addMedicine());
        updateButton.setOnAction(e -> updateMedicine());
        deleteButton.setOnAction(e -> deleteMedicine());
        clearButton.setOnAction(e -> clearFields());

        HBox buttons = new HBox(10,
                addButton, updateButton, deleteButton, clearButton
        );

        searchField = new TextField();
        searchField.setPromptText("Search by ID or Medicine Name");

        Button searchButton = new Button("Search");
        Button showAllButton = new Button("Show All");

        searchButton.setOnAction(e -> searchMedicine());
        showAllButton.setOnAction(e -> refreshMedicineTable());

        HBox searchBox = new HBox(10,
                searchField, searchButton, showAllButton
        );

        medicineTable = new TableView<>();

        medicineTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Medicine, String> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getId()
                )
        );

        TableColumn<Medicine, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        TableColumn<Medicine, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategory()
                )
        );

        TableColumn<Medicine, String> manufacturerColumn =
                new TableColumn<>("Manufacturer");

        manufacturerColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getManufacturer()
                )
        );

        TableColumn<Medicine, Integer> quantityColumn =
                new TableColumn<>("Stock");

        quantityColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getQuantity()
                ).asObject()
        );

        TableColumn<Medicine, Double> priceColumn =
                new TableColumn<>("Price");

        priceColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getPrice()
                ).asObject()
        );

        TableColumn<Medicine, String> expiryColumn =
                new TableColumn<>("Expiry");

        expiryColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getExpiryDate()
                )
        );

        medicineTable.getColumns().addAll(
                idColumn, nameColumn, categoryColumn,
                manufacturerColumn, quantityColumn,
                priceColumn, expiryColumn
        );

        medicineTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selected) -> {

                    if (selected != null) {
                        fillFields(selected);
                    }
                });

        medicineList = FXCollections.observableArrayList();

        medicineTable.setItems(medicineList);

        layout.getChildren().addAll(
                heading, form, buttons, searchBox, medicineTable
        );

        refreshMedicineTable();

        return layout;
    }

    // Add medicine
    private void addMedicine() {

        try {

            Medicine medicine = readMedicineFields();

            if (medicineManager.addMedicine(medicine)) {

                showAlert("Medicine added successfully!");

                clearFields();
                refreshMedicineTable();
                refreshDashboard();

            } else {

                showAlert("Medicine ID already exists.");
            }

        } catch (Exception e) {
            showAlert("Please enter valid medicine details.");
        }
    }

    // Update medicine
    private void updateMedicine() {

        try {

            Medicine medicine = readMedicineFields();

            if (medicineManager.updateMedicine(medicine)) {

                showAlert("Medicine updated successfully!");

                clearFields();
                refreshMedicineTable();
                refreshDashboard();

            } else {

                showAlert("Medicine not found.");
            }

        } catch (Exception e) {
            showAlert("Please enter valid medicine details.");
        }
    }

    // Delete medicine
    private void deleteMedicine() {

        String id = idField.getText().trim();

        if (id.isEmpty()) {
            showAlert("Enter or select a medicine ID.");
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this medicine?",
                ButtonType.YES,
                ButtonType.NO
        );

        confirmation.showAndWait().ifPresent(response -> {

            if (response == ButtonType.YES) {

                if (medicineManager.deleteMedicine(id)) {

                    showAlert("Medicine deleted successfully!");

                    clearFields();
                    refreshMedicineTable();
                    refreshDashboard();

                } else {

                    showAlert("Medicine not found.");
                }
            }
        });
    }

    // Read form fields
    private Medicine readMedicineFields() {

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String category = categoryField.getText().trim();
        String manufacturer = manufacturerField.getText().trim();

        int quantity = Integer.parseInt(
                quantityField.getText().trim()
        );

        double price = Double.parseDouble(
                priceField.getText().trim()
        );

        String expiry = expiryField.getText().trim();

        if (id.isEmpty() || name.isEmpty()
                || category.isEmpty()
                || manufacturer.isEmpty()
                || expiry.isEmpty()) {

            throw new IllegalArgumentException();
        }

        if (quantity < 0 || price < 0) {
            throw new IllegalArgumentException();
        }

        return new Medicine(
                id, name, category, manufacturer,
                quantity, price, expiry
        );
    }

    // Search medicine
    private void searchMedicine() {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            refreshMedicineTable();
            return;
        }

        Medicine medicine = medicineManager.searchById(search);

        if (medicine != null) {

            medicineList.setAll(medicine);

        } else {

            medicineList.setAll(
                    medicineManager.searchByName(search)
            );
        }
    }

    // Refresh medicine table
    private void refreshMedicineTable() {

        if (medicineTable == null) {
            return;
        }

        medicineList.setAll(
                medicineManager.getAllMedicines()
        );
    }

    // Fill fields when table row is selected
    private void fillFields(Medicine medicine) {

        idField.setText(medicine.getId());
        nameField.setText(medicine.getName());
        categoryField.setText(medicine.getCategory());
        manufacturerField.setText(medicine.getManufacturer());
        quantityField.setText(String.valueOf(medicine.getQuantity()));
        priceField.setText(String.valueOf(medicine.getPrice()));
        expiryField.setText(medicine.getExpiryDate());
    }

    // Clear form fields
    private void clearFields() {

        idField.clear();
        nameField.clear();
        categoryField.clear();
        manufacturerField.clear();
        quantityField.clear();
        priceField.clear();
        expiryField.clear();

        medicineTable.getSelectionModel().clearSelection();
    }

    // Show alert
    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("PharmaCare");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
