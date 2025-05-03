import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;

public class BorrowedTableBook extends JFrame {

    private JButton Back , returnB;
    private String columns[] = {"No.", "BookID" , "Title", "Author", "Genre", "Borrowing Date", "Returned", "Overdue"};
    private JTable table;

    public BorrowedTableBook() {
        super("Borrowed Books");

        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        Back = new JButton("Back");
        Back.setFont(fontButton);
        
        returnB = new JButton("Return");
        returnB.setFont(fontButton);

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);

        int id = User.getUserID();
        loadBorrowedBooks(model , id);
       

        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);
        subPanel2.add(returnB);


        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.CENTER);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               new DashboardUser();
                dispose(); 
            }
        });   
        returnB.addActionListener(new returnButton());
        this.setVisible(true);
    }

 private void loadBorrowedBooks(DefaultTableModel model, int loggedInUserID) {
    String query = "SELECT Borrowings.*, Books.Title, Books.Author, Books.Genre " +
                   "FROM Borrowings " +
                   "INNER JOIN Books ON Borrowings.BookID = Books.BookID " +
                   "WHERE Borrowings.UserID = ? AND IsReturned = FALSE";

    try {
        // Establishing the database connection
        Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setInt(1, loggedInUserID); 

        ResultSet rs = stmt.executeQuery();

        int rowNumber = 1; 
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); 

        model.setRowCount(0);

        while (rs.next()) {
            int bookID = rs.getInt("BookID");
            String title = rs.getString("Title");
            String author = rs.getString("Author");
            String genre = rs.getString("Genre");
            java.sql.Date borrowDate = rs.getDate("BorrowDate");
            java.sql.Date returnDate = rs.getDate("ReturnDate");
            boolean isReturned = rs.getBoolean("IsReturned");

            long delayDays = 0;
            if (!isReturned && returnDate != null) {
                long diff = currentDate.getTime() - returnDate.getTime();
                delayDays = diff > 0 ? diff / (1000 * 60 * 60 * 24) : 0; 
            }

            model.addRow(new Object[]{
                rowNumber,
                bookID,
                title,
                author,
                genre,
                borrowDate != null ? borrowDate.toString() : "N/A",
                returnDate != null ? returnDate.toString() : "N/A",
                isReturned ? "No Delay" : (delayDays > 0 ? delayDays + " days" : "No Delay")
            });

            rowNumber++;
        }
    } catch (SQLException ex) {
       ex.printStackTrace();
    }
}
    public class returnButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          int selectedRow = table.getSelectedRow(); 
          
          if (selectedRow == -1) { 
              JOptionPane.showMessageDialog(null, "Please select a book to return.", "No Book Selected", JOptionPane.WARNING_MESSAGE);
              return; } 
            
          // Get the BookID and BorrowID from the selected row
          int bookID = (int) table.getValueAt(selectedRow, 1);
          int borrowID = getBorrowID(bookID);
            int n = JOptionPane.showConfirmDialog(null , "Do you Want Return Book ?","Return Book",JOptionPane.YES_NO_OPTION);
            if (n == 0) {
                int check = 0;
                ReturndReceipt Receipt = new ReturndReceipt(check);
                check = Receipt.getc();
                while (check == 5) {
                    Receipt = new ReturndReceipt(check);
                    check = Receipt.getc();
                }
                if (check == 1) {
                    if (returnBook(borrowID, bookID)) {
                        JOptionPane.showMessageDialog(null, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to return the book. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }
    }
    
   private boolean returnBook(int borrowID, int bookID) { 
    
    String updateBorrowingQuery = "UPDATE Borrowings SET IsReturned = ? WHERE BorrowID = ?";
    String updateBookQuery = "UPDATE Books SET Avaliable = ? WHERE BookID = ?"; 
    try {
        
     Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
    
     // Step 1: Update Borrowings table
     PreparedStatement updateBorrowingStmt = c.prepareStatement(updateBorrowingQuery); 
     updateBorrowingStmt.setBoolean(1, true);
     updateBorrowingStmt.setInt(2, borrowID); 
     int rowsBorrowingUpdated = updateBorrowingStmt.executeUpdate(); 

// Step 2: Update Books table

PreparedStatement updateBookStmt = c.prepareStatement(updateBookQuery); 
updateBookStmt.setBoolean(1, true);

// Mark as available
updateBookStmt.setInt(2, bookID); 
int rowsBookUpdated = updateBookStmt.executeUpdate(); 
return rowsBorrowingUpdated > 0 && rowsBookUpdated > 0;
} catch (SQLException ex) 
{ ex.printStackTrace(); 
return false;
} } 

private int getBorrowID(int bookID) {
    
    String query = "SELECT BorrowID FROM Borrowings WHERE BookID = ? AND UserID = ? AND IsReturned = FALSE";
 try {
     Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
     PreparedStatement stmt = c.prepareStatement(query);
     stmt.setInt(1, bookID);
     stmt.setInt(2, User.getUserID());
     ResultSet rs = stmt.executeQuery(); 
     
     if (rs.next()) { 
         return rs.getInt("BorrowID");
     } } catch (SQLException ex) { 
         
         ex.printStackTrace();
     } return -1; // Return -1 if BorrowID not found 
}
    }
   
   
