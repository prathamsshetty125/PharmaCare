
package com.pharmacare;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class SalesHistoryView {

    private BillingManager billingManager;
    private TableView<Bill> billTable;
    private Label totalSalesLabel;
    private Label totalBillsLabel;

    public SalesHistoryView(BillingManager billingManager) {
        this.billingManager = billingManager;
    }

    public VBox getView() {

        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f9;");

        // Heading
        Label heading = new Label("Sales History");
        heading.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold;"
        );

        // Summary section
        totalBillsLabel = new Label();
        totalSalesLabel = new Label();

        totalBillsLabel.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        totalSalesLabel.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        HBox summaryBox = new HBox(
                40, totalBillsLabel, totalSalesLabel
        );

        summaryBox.setAlignment(Pos.CENTER_LEFT);

        // Bill table
        billTable = new TableView<>();

        billTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        TableColumn<Bill, String> idColumn =
                new TableColumn<>("Bill ID");

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getBillId()
                )
        );

        TableColumn<Bill, String> customerColumn =
                new TableColumn<>("Customer");

        customerColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCustomerName()
                )
        );

        TableColumn<Bill, String> phoneColumn =
                new TableColumn<>("Phone");

        phoneColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCustomerPhone()
                )
        );

        TableColumn<Bill, String> dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getBillDate()
                )
        );

        TableColumn<Bill, String> amountColumn =
                new TableColumn<>("Total Amount");

        amountColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        String.format(
                                "Rs. %.2f",
                                data.getValue().getTotalAmount()
                        )
                )
        );

        billTable.getColumns().addAll(
                idColumn,
                customerColumn,
                phoneColumn,
                dateColumn,
                amountColumn
        );

        // View bill details button
        Button detailsButton = new Button("View Bill Details");

        detailsButton.setStyle(
                "-fx-background-color: #2563eb; " +
                "-fx-text-fill: white; " +
                "-fx-padding: 10 20;"
        );

        detailsButton.setOnAction(e -> viewBillDetails());

        // Refresh button
        Button refreshButton = new Button("Refresh");

        refreshButton.setOnAction(e -> refreshHistory());

        HBox buttonBox = new HBox(10,
                detailsButton, refreshButton
        );

        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(
                heading,
                summaryBox,
                billTable,
                buttonBox
        );

        refreshHistory();

        return root;
    }

    // Refresh sales history
    public void refreshHistory() {

        if (billTable == null) {
            return;
        }

        List<Bill> bills = billingManager.getAllBills();

        billTable.setItems(
                FXCollections.observableArrayList(bills)
        );

        totalBillsLabel.setText(
                "Total Bills: " + billingManager.getTotalBills()
        );

        totalSalesLabel.setText(
                String.format(
                        "Total Sales: Rs. %.2f",
                        billingManager.getTotalSales()
                )
        );
    }

    // Display selected bill details
    private void viewBillDetails() {

        Bill selectedBill =
                billTable.getSelectionModel().getSelectedItem();

        if (selectedBill == null) {

            showAlert("Please select a bill first.");
            return;
        }

        StringBuilder details = new StringBuilder();

        details.append("Bill ID: ")
                .append(selectedBill.getBillId())
                .append("\n");

        details.append("Customer: ")
                .append(selectedBill.getCustomerName())
                .append("\n");

        details.append("Phone: ")
                .append(selectedBill.getCustomerPhone())
                .append("\n");

        details.append("Date: ")
                .append(selectedBill.getBillDate())
                .append("\n\n");

        details.append("PURCHASED MEDICINES\n");
        details.append("--------------------------------\n");

        for (BillItem item : selectedBill.getItems()) {

            details.append(item.getMedicineName())
                    .append("  x ")
                    .append(item.getQuantity())
                    .append("  @ Rs. ")
                    .append(String.format("%.2f", item.getUnitPrice()))
                    .append("  = Rs. ")
                    .append(String.format("%.2f", item.getTotalPrice()))
                    .append("\n");
        }

        details.append("\n--------------------------------\n");

        details.append("Grand Total: Rs. ")
                .append(String.format(
                        "%.2f",
                        selectedBill.getTotalAmount()
                ));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Bill Details");
        alert.setHeaderText("PharmaCare - Customer Bill");
        alert.setContentText(details.toString());

        alert.getDialogPane().setMinWidth(400);

        alert.showAndWait();
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
