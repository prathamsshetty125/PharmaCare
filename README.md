# 💊 PharmaCare – Pharmacy Billing System

**A JavaFX-based desktop application for pharmacy inventory management and customer billing.**

PharmaCare is a desktop-based Pharmacy Billing System. The application simplifies basic pharmacy operations by combining medicine inventory management, customer billing, automatic stock deduction and sales history in a single graphical interface.

---

## 📌 Features

### 1. Dashboard
- View total number of medicines.
- Monitor total available stock.
- View total bills generated.
- Track overall sales.

### 2. Medicine Management
- Add new medicine records.
- Update existing medicine details.
- Delete medicine records.
- Search medicines by ID or name.
- View medicine details, including quantity, price and expiry date.

### 3. Billing System
- Enter customer details.
- Add multiple medicines to a single bill.
- Specify medicine purchase quantities.
- Automatically calculate individual item totals and the final bill amount.
- Validate medicine availability before generating a bill.

### 4. Automatic Stock Management
- Automatically deduct purchased quantities after successful bill generation.
- Prevent purchases exceeding available stock.
- Maintain updated inventory records.

### 5. Sales History
- View previously generated bills.
- Display bill ID, customer name, phone number, date and total amount.
- View detailed information about individual bills.
- Track total sales.

### 6. Data Persistence
- Store medicine records in `medicines.txt`.
- Store billing transactions in `bills.txt`.
- Load previously saved records when the application starts.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 17 | Core programming language |
| JavaFX 17 | Graphical User Interface |
| Maven | Dependency management and project execution |
| ArrayList | Managing medicine and billing records |
| File Handling | Local data storage and retrieval |
| Object-Oriented Programming | Modular application design |

---

## 📂 Project Structure

```text
PharmaCare/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── pharmacare/
│                   ├── Main.java
│                   ├── Medicine.java
│                   ├── MedicineManager.java
│                   ├── FileStorage.java
│                   ├── BillItem.java
│                   ├── Bill.java
│                   ├── BillFileStorage.java
│                   ├── BillingManager.java
│                   ├── BillingView.java
│                   └── SalesHistoryView.java
│
├── data/
│   ├── medicines.txt
│   └── bills.txt
│
├── pom.xml
│
└── README.md
```

---

## ⚙️ Installation and Setup

### Prerequisites

Make sure the following software is installed:

- Java Development Kit (JDK 17)
- Apache Maven
- Visual Studio Code or any Java-supported IDE

### Step 1: Clone the Repository

```bash
git clone https://github.com/prathamsshetty125/NutriAI.git
```

Replace the repository URL with your PharmaCare GitHub repository URL if it is different.

### Step 2: Navigate to the Project Directory

```bash
cd PharmaCare
```

### Step 3: Run the Application

Execute the following Maven command:

```bash
mvn clean javafx:run
```

Maven will download the required dependencies and launch the JavaFX application.

---

## 💾 Data Storage

PharmaCare uses local text files instead of a database.

| File | Description |
|---|---|
| `medicines.txt` | Stores medicine inventory records |
| `bills.txt` | Stores completed customer bills and their purchased items |

This approach provides simple data persistence without requiring an external database.

---

## 🎯 Project Objective

To develop a simple desktop-based pharmacy application that combines medicine inventory management and customer billing, while automatically updating available stock and maintaining sales transaction records.

---

## 🚀 Future Enhancements

- Database integration using MySQL or PostgreSQL.
- User authentication and role-based access.
- Invoice generation and printing.
- Advanced sales reports and analytics.
- Improved inventory and expiry-date monitoring.
- Backup and restore functionality.

---

⭐ If you find this project useful, consider giving the repository a star!

