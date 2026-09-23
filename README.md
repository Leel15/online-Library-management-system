# Online Library Management System

A comprehensive desktop-based Java application designed to streamline library operations—including user authentication, book inventory management, borrowing/returning workflows, reporting, and invoicing—powered by a Microsoft Access database.

---

## 🛠️ Tech Stack & Architecture
* **Language:** Java
* **Database:** Microsoft Access (`.accdb`) via JDBC
* **GUI & Architecture:** Java Swing/AWT Desktop Application with modular class separation
* **Design Patterns:** Object-Oriented Programming (OOP) with Role-Based Access Control (Admin & User)

---

## 📂 Core Features & Project Structure

The application is structured into several modular Java classes and assets:

### 1. System Entry & Authentication
* **`OnlineLibaryManagementSystem.java`:** The main entry point/launcher for the application.
* **`signUp.java` & `NewMember.java`:** Handle new user registration and account creation.
* **`User.java` & `UserProfile.java`:** Manage user profiles, sessions, and account details.

### 2. Dashboards & Management
* **`DashboardAdmin.java`:** Administrative panel to oversee all library operations, inventory, and users.
* **`DashboardUser.java`:** Member interface for searching books, viewing profile info, and managing activities.

### 3. Book Inventory & Updates
* **`BookInventory.java` & `addBook.java`:** Handle adding new books to the library stock.
* **`UpdateBook.java` & `UpdateMember.java`:** Manage updating existing book details and member records.
* **`BookSearch.java`:** Provides search capabilities across the library catalog.
* **`BookTable.java` & `NonEditableTableModel.java`:** Custom table models for displaying inventory securely.

### 4. Borrowing, Returning & Invoicing
* **`BorrowBook.java` & `BorrowedBook.java` & `BorrowedTableBook.java`:** Track active and historical borrowing transactions.
* **`ReturnBook.java` & `ReturndReceipt.java`:** Handle book return processes and generate transaction receipts.
* **`BorrowInvoice.java`:** Generates billing invoices for borrowed materials.

### 5. Reporting & Utilities
* **`ReportModule.java`:** Generates system reports on library statistics, inventory, and activity logs.
* **`CustomExceptions.java`:** Handles custom error management for validation and database operations.

### 6. Database & Assets
* **`LibraryDB (1).accdb`:** The backend Microsoft Access database storing all records.
* **`library_logo (1)(1).png` & `library_logo (2).png`:** UI branding and logo assets.

---

## 🚀 Getting Started & Execution

1. **Prerequisites:**
   * Install the **Java Development Kit (JDK)**.
   * Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, NetBeans).
   * Ensure your environment supports JDBC connectivity for Microsoft Access (`.accdb`).

2. **Clone the Repository:**
   ```bash
   git clone [https://github.com/Leel15/online-Library-management-system.git](https://github.com/Leel15/online-Library-management-system.git)
