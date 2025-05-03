import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;

public class ReturnBook extends JFrame {

    private JLabel bookName, dateBorrow, isbn, dateReturn;
    private JTextField bookNameText, isbnText;
    private JDateChooser dateBorrowText , dateReturnText ;
    private JButton BorrowButton, backButton;
    private int bookID;
    

    public ReturnBook(int bookID) {
        
            this.bookID = bookID;
            this.setTitle("");
            this.setSize(1024, 576);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocation(0,0);
            
            JPanel mainPanel = new JPanel (new GridLayout(4, 1));
            
            Font fontButton = new Font("Arial", Font.PLAIN, 16);
            Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
            Font fonttitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);
            
            bookName = new JLabel("Book Name");
            bookName.setBorder(new EmptyBorder(0, 20, 0, 20));
            bookName.setFont(fontText);
            bookNameText = new JTextField(30);
            JPanel m1 = new JPanel(new FlowLayout());
            m1.add(bookName);
            m1.add(bookNameText);
            
            
            isbn = new JLabel("ISBN");
            isbn.setBorder(new EmptyBorder(0, 20, 0, 60));
            isbn.setFont(fontText);
            isbnText = new JTextField(30);
            JPanel m2 = new JPanel(new FlowLayout());
            m2.add(isbn);
            m2.add(isbnText);
            
            Connection connection = DatabaseConnection();            
            if (connection == null) return;
   
            String query = "SELECT Title, ISBN FROM Books WHERE BookID = ?"; // Query to fetch book data
            
         try{
             PreparedStatement stmt = connection.prepareStatement(query);
             stmt.setInt(1, bookID);
             ResultSet rs = stmt.executeQuery();
             
             if (rs.next()) {
                bookNameText.setText(rs.getString("Title"));
                isbnText.setText(rs.getString("ISBN"));}
             
         }catch (SQLException e) {
             
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to retrieve data from the database", "Database Error", JOptionPane.ERROR_MESSAGE); } 
         
            dateBorrow = new JLabel("Date Borrow");
            dateBorrow.setFont(fontText);
            dateBorrowText = new JDateChooser();
            dateBorrowText.setPreferredSize(new Dimension(290, 20));
            dateBorrow.setBorder(new EmptyBorder(0, 20, 0, 5));
            JPanel m3 = new JPanel(new FlowLayout());
            m3.add(dateBorrow);
            m3.add(dateBorrowText);
            
            
            dateReturn = new JLabel("Date Return");
            dateReturn.setFont(fontText);
            dateReturnText = new JDateChooser();
            dateReturnText.setPreferredSize(new Dimension(290, 20));
            dateReturn.setBorder(new EmptyBorder(0, 20, 0, 5));
            JPanel m4 = new JPanel(new FlowLayout());
            m4.add(dateReturn);
            m4.add(dateReturnText);
            
            
            
            BorrowButton = new JButton("Borrow");
            BorrowButton.setFont(fontButton);
            backButton = new JButton("Back");
            backButton.setFont(fontButton);
            JPanel m7 = new JPanel(new FlowLayout());
            m7.add(BorrowButton);
            m7.add(backButton);
            
            
            mainPanel.add(m1);
            mainPanel.add(m2);
            mainPanel.add(m3);
            mainPanel.add(m4);
            
            
            JPanel mainpanel = (JPanel) this.getContentPane();
            TitledBorder title;
            title = BorderFactory.createTitledBorder("Borrow Book");
            title.setTitleFont(fonttitle);
            mainpanel.setBorder(title);
            mainpanel.add(mainPanel,BorderLayout.CENTER);
            mainpanel.add(m7,BorderLayout.SOUTH);
            
            backButton.addActionListener(new BackToDashboard());
            BorrowButton.addActionListener(new borrowButton() );
            this.setVisible(true);
        
    }
    
        public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BooksTable();
            dispose();
        }
    }
   public class borrowButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (dateBorrowText.getDate() == null || dateReturnText.getDate() == null) {
                    throw new CustomExceptions.EmptyException("Information Not completed!\nPlease Enter all Information");
                }



                java.util.Date utilDate = dateBorrowText.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                 java.util.Date utilDate2 = dateReturnText.getDate();
                java.sql.Date sqlDate2 = new java.sql.Date(utilDate2.getTime());
                
                int id = User.getUserID();
                
                if (BorrowBookQuery(id ,bookID , sqlDate ,sqlDate2 )) {
                    JOptionPane.showMessageDialog(null, "Borrow Book Successful", "Successful", JOptionPane.INFORMATION_MESSAGE);
                        new BooksTable();
                           dispose();
                } else {
                    throw new CustomExceptions.ErrorAdd("Borrow Book unsuccessful");
                }
            } catch (CustomExceptions.EmptyException e2) {
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Information Not Completed", JOptionPane.WARNING_MESSAGE);
            } catch (CustomExceptions.ErrorAdd e2) {
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

   public boolean BorrowBookQuery(int id, int bookID, java.sql.Date sqlDate, java.sql.Date sqlDate2) {
    String insertQuery = "INSERT INTO Borrowings (UserID, BookID, BorrowDate, ReturnDate, IsReturned) VALUES (?, ?, ?, ?, ?)";
    String updateQuery = "UPDATE Books SET Avaliable = ? WHERE BookID = ?";

    try {
        Connection connection = DatabaseConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return false;
        }

        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, id);
        insertStmt.setInt(2, bookID);
        insertStmt.setDate(3, sqlDate);
        insertStmt.setDate(4, sqlDate2);
        insertStmt.setBoolean(5, false);

        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Record inserted successfully!");

            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setBoolean(1, false); 
            updateStmt.setInt(2, bookID);

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book availability updated successfully!");
                return true;
            } else {
                System.out.println("Failed to update book availability.");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;
}
  
       private static Connection DatabaseConnection() {
        try {
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/baato/Downloads/LibraryDB.accdb");
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }


    }
}
