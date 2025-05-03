 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;
//import visualprogramming.projectfinal.DashboardAdmin;

// BorrowedBook Class - Displays the borrowed books in a table format.

public class BorrowedBook extends JFrame {
    
    private JButton Back,filterButton;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Borrowing Date", "Returned Date", "By user"};
    private JTable table;
    private JLabel genreLabel,authorFilterLabel,yearLabel ;
    private Object[][] data = {};
    private DefaultTableModel model;
    private JComboBox<String> genreComboBox, authorComboBox,yearComboBox ;

    public BorrowedBook() {
        super("Borrowed Books");
        
        // Frame settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Fonts
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        
        genreLabel = new JLabel("Genre:      ");  
        genreLabel.setFont(fontText);
        genreComboBox = new JComboBox<>();
        genreComboBox.setPreferredSize(new Dimension(150, 25));
        
        authorFilterLabel = new JLabel("      Author:      ");
        authorFilterLabel.setFont(fontText);
        authorComboBox = new JComboBox<>();
        authorComboBox.setPreferredSize(new Dimension(150, 25));
       
        yearLabel = new JLabel("      Publication Year:      ");
        yearLabel.setFont(fontText);
        yearComboBox = new JComboBox<>();
        yearComboBox.setPreferredSize(new Dimension(150, 25));
         // Button initialization
        filterButton = new JButton("Filter");
        filterButton.setFont(fontButton); 
       
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Table and model
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);
        JScrollPane scrollPane = new JScrollPane(table);

       
        // Layout components
        
         JPanel subPanel3 = new JPanel(); 
         subPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
         subPanel3.add(genreLabel);
         subPanel3.add(genreComboBox);
         subPanel3.add(authorFilterLabel);
         subPanel3.add(authorComboBox);
         subPanel3.add(yearLabel);
         subPanel3.add(yearComboBox);
         subPanel3.add(filterButton);
         
        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);
        
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel3, BorderLayout.NORTH);
        mainPanel.add(subPanel1, BorderLayout.CENTER);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);
       
           // Retrieve data
           
        RetrieveBook();
        loadComboBoxData();

        // Back button action
        filterButton.addActionListener(new FilterOperation() );
        Back.addActionListener(new ActionListener(){ //Back button Action
        public void actionPerformed (ActionEvent e){
            new DashboardAdmin();
            dispose(); 
        }
        });
        
        this.setVisible(true);
    }

    // Database connection method
    private static Connection DatabaseConnection() {
        try {
Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Retrieve data from database
   private void RetrieveBook() {
    Connection connection = DatabaseConnection();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    if (connection == null) return;

    String query = "SELECT " +
            "Books.Title AS Title, " +
            "Books.Author AS Author, " +
            "Books.Genre AS Genre, " +
            "Borrowings.BorrowDate AS BorrowDate, " +
            "Borrowings.ReturnDate AS ReturnDate, " +
            "Users.Username AS Username " +
            "FROM Borrowings " +
            "JOIN Books ON Borrowings.BookID = Books.BookID " +
            "JOIN Users ON Borrowings.UserID = Users.UserID " +
            "WHERE Borrowings.BorrowDate IS NOT NULL AND Borrowings.ReturnDate IS NOT NULL";

    try {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        model.setRowCount(0); // Clear table rows
        int rowNumber = 1;

        while (rs.next()) {
            String No = "" + rowNumber;
            String title = rs.getString("Title");
            String author = rs.getString("Author");
            String genre = rs.getString("Genre");
            String borrowDate = rs.getString("BorrowDate");
            String returnDate = rs.getString("ReturnDate");
            String username = rs.getString("Username");

            // Format dates
            String formattedBorrowDate = "";
            String formattedReturnDate = "";
            if (borrowDate != null && !borrowDate.isEmpty()) {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(borrowDate);
                formattedBorrowDate = dateFormat.format(date);
            }
            if (returnDate != null && !returnDate.isEmpty()) {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(returnDate);
                formattedReturnDate = dateFormat.format(date);
            }

            model.addRow(new Object[]{No, title, author, genre, formattedBorrowDate, formattedReturnDate, username});
            rowNumber++;
        }

        rs.close();
        stmt.close();
        connection.close();
        System.out.println("Successfully retrieved data.");

    } catch (SQLException | ParseException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to retrieve data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
  
    private void loadComboBoxData() {
        // Load Genre
        try (Connection conn = DatabaseConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Genre FROM Books")) {

            genreComboBox.addItem("All");
            while (rs.next()) {
                genreComboBox.addItem(rs.getString("Genre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading genres: " + e.getMessage());
        }

        // Load Authors
        try (Connection conn = DatabaseConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Author FROM Books")) {

            authorComboBox.addItem("All");
            while (rs.next()) {
                authorComboBox.addItem(rs.getString("Author"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading authors: " + e.getMessage());
        }
        
        // Load Publication Years

try (Connection conn = DatabaseConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT DISTINCT YEAR(PublicationDate) AS year FROM Books")) {

    yearComboBox.addItem("All");
    while (rs.next()) {
        yearComboBox.addItem(rs.getInt("year") + ""); // إضافة السنة كعدد صحيح
    }
} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Error loading publication years: " + e.getMessage());
}}

   
  
  private class FilterOperation implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String genre = genreComboBox.getSelectedItem().toString();
        String author = authorComboBox.getSelectedItem().toString();
        String year = yearComboBox.getSelectedItem().toString();

        String query = "SELECT " +
                "Books.Title AS Title, " +
                "Books.Author AS Author, " +
                "Books.Genre AS Genre, " +
                "Borrowings.BorrowDate AS BorrowDate, " +
                "Borrowings.ReturnDate AS ReturnDate, " +
                "Users.Username AS Username " +
                "FROM Borrowings " +
                "JOIN Books ON Borrowings.BookID = Books.BookID " +
                "JOIN Users ON Borrowings.UserID = Users.UserID " +
                "WHERE Borrowings.BorrowDate IS NOT NULL AND Borrowings.ReturnDate IS NOT NULL";

        if (!genre.equals("All")) {
            query += " AND Books.Genre = '" + genre + "'";
        }
        if (!author.equals("All")) {
            query += " AND Books.Author = '" + author + "'";
        }
        if (!year.equals("All")) {
            query += " AND YEAR(Books.PublicationDate) = '" + year + "'";
        }

        try (Connection connection = DatabaseConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            model.setRowCount(0);
            int rowNumber = 1;

            while (rs.next()) {
                String No = "" + rowNumber;
                String title = rs.getString("Title");
                String filteredAuthor = rs.getString("Author");
                String filteredGenre = rs.getString("Genre");
                String borrowDate = rs.getString("BorrowDate");
                String returnDate = rs.getString("ReturnDate");
                String username = rs.getString("Username");

                String formattedBorrowDate = "";
                String formattedReturnDate = "";
                if (borrowDate != null && !borrowDate.isEmpty()) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(borrowDate);
                    formattedBorrowDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                if (returnDate != null && !returnDate.isEmpty()) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(returnDate);
                    formattedReturnDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                }

                model.addRow(new Object[]{No, title, filteredAuthor, filteredGenre, formattedBorrowDate, formattedReturnDate, username});
                rowNumber++;
            }
        } catch (SQLException | ParseException ex) {
            JOptionPane.showMessageDialog(BorrowedBook.this, "Error filtering data: " + ex.getMessage());
        }
    }
}
    public class NoresultException extends Exception{
        public NoresultException(String msg){
            super(msg);
        }
    } 
    
}
