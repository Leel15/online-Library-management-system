import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class creates a "Book Search" GUI that allows users to search for books 
 * in a database based on selected criteria (e.g., Title, Author, Genre).
 */
/////
public class BookSearch extends JFrame {
    private JLabel searchBy, searchLabel,genreLabel,authorFilterLabel,yearLabel;
    private JComboBox list; 
    private JComboBox<String> genreComboBox, authorComboBox,yearComboBox ;
    private JTextField field; 
    private String columns[] = {"No.", "Title", "Author", "Genre", "Description", "Publishing Date"}; 
    private JTable table; 
    static private DefaultTableModel model; 
    private JButton searchButton, back,filterButton; 
    private String columnsr[] = {"Title", "Author", "Genre"};

    // Constructor for initializing the GUI components.

    public BookSearch() {
        super(""); // Create a window without a title.
        // Set up the window properties.
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Define fonts for different components.
        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14); 
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16); 
        
        // Initialize labels.
        searchBy = new JLabel("Search by: ");
        searchBy.setFont(fontText);
        
        searchLabel = new JLabel("Search:      "); 
        searchLabel.setFont(fontText);
        
        genreLabel = new JLabel("Genre:      ");  
        genreLabel.setFont(fontText);
       
        
        authorFilterLabel = new JLabel("      Author:      ");
        authorFilterLabel.setFont(fontText);
        
       
        yearLabel = new JLabel("      Publication Year:      ");
        yearLabel.setFont(fontText);
        
        list = new JComboBox(columnsr); 
        list.setPreferredSize(new Dimension(150, 25)); 
        
         genreComboBox = new JComboBox<>();
        genreComboBox.setPreferredSize(new Dimension(150, 25));
        
        authorComboBox = new JComboBox<>();
        authorComboBox.setPreferredSize(new Dimension(150, 25));
        
        yearComboBox = new JComboBox<>();
        yearComboBox.setPreferredSize(new Dimension(150, 25));
      
        field = new JTextField(); 
        field.setPreferredSize(new Dimension(150, 25));  
        
       
        searchButton = new JButton("Search"); 
        searchButton.setFont(fontButton);
        back = new JButton("Back"); 
        back.setFont(fontButton);
        filterButton = new JButton("Filter");
        filterButton.setFont(fontButton);
        
        model = new NonEditableTableModel(columns, 0); 
        table = new JTable(model); 
        JScrollPane scrollPane = new JScrollPane(table); 
        JTableHeader header = table.getTableHeader(); 
        header.setReorderingAllowed(false); 
        header.setFont(fontText);
        
        
        JPanel subPanel1 = new JPanel();
        subPanel1.add(searchBy, FlowLayout.LEFT); 
        subPanel1.add(list); 
            
        
        JPanel subPanel2 = new JPanel();
        subPanel2.add(searchLabel, FlowLayout.LEFT); 
        subPanel2.add(field); 
            
        
        JPanel subPanel3 = new JPanel();
        subPanel3.add(searchButton); 
        subPanel3.add(back);
        
        
         JPanel subPanel4 = new JPanel(); 
         subPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
         subPanel4.add(genreLabel);
         subPanel4.add(genreComboBox);
         subPanel4.add(authorFilterLabel);
         subPanel4.add(authorComboBox);
         subPanel4.add(yearLabel);
         subPanel4.add(yearComboBox);
         subPanel4.add(filterButton);
        
        JPanel mainPanel1 = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Search Book"); 
        title.setTitleFont(fontTitle);
        mainPanel1.setBorder(title); 
        mainPanel1.setLayout(new BoxLayout(mainPanel1, BoxLayout.Y_AXIS)); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel1); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel2); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel3); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel4); 
        mainPanel1.add(Box.createVerticalStrut(30)); 
        mainPanel1.add(scrollPane); 
        
        
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(mainPanel1, BorderLayout.NORTH);
         loadComboBoxData();

        // Action for the "Back" button
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = User.getUserID();
                String role = "";
                 Connection connection = DatabaseConnection(); // Connect to the database.
                 String query = "SELECT Role FROM Users Where UserID = ? "; 
                try { 
                    PreparedStatement pstmt = connection.prepareStatement(query);
                      pstmt.setInt(1, id);

                         ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                                role =  rs.getString("Role");
                        } 
                        if(role.equals("Admin"))
                              new DashboardAdmin(); 
                        else 
                            new DashboardUser();
                   dispose(); // Close the current window.

                } catch (SQLException ex) {
            ex.printStackTrace(); 
                }
                            }
        });
        
        // Action for the "Search" button and filterButton
        searchButton.addActionListener(new searchOperation());
       
       filterButton.addActionListener(new filterOperation() );
         this.setVisible(true); 
    }

    // Establish connection to the database.
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
    
    // Action listener class to handle the search operation.
    public class searchOperation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = DatabaseConnection(); // Connect to the database.
            String selectedColumn = list.getSelectedItem().toString(); 
            String searchValue = field.getText(); 
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format for the publication date.
            
            // Check if the search value is empty.
            if (searchValue.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter a value to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // SQL query to search for books based on the selected column and value.
            String query = "SELECT Title , Author , Genre , Descripation, PublicationDate from Books Where " + selectedColumn + " LIKE ?";
            try {
                if (connection == null) {
                    return; 
                }
                  
                PreparedStatement pstmt = connection.prepareStatement(query); 
                pstmt.setString(1, "%" + searchValue + "%"); 

                ResultSet rs = pstmt.executeQuery();
                model.setRowCount(0); // Clear existing rows in the table.
                int rowNumber = 1; 

                
                while (rs.next()) {
                    String No = "" + rowNumber; // Generate row number.
                    String title = rs.getString("Title");
                    String author = rs.getString("Author");
                    String genre = rs.getString("Genre");
                    String description = rs.getString("Descripation");
                    String rawDate = rs.getString("PublicationDate"); 
                    String formattedDate = "";
                    
                    if (rawDate != null && !rawDate.isEmpty()) { // Format the date.
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate); 
                        formattedDate = dateFormat.format(date); 
                    }
                    
                    
                    model.addRow(new Object[]{No, title, author, genre, description, formattedDate});
                    rowNumber++;
                }
                rs.close(); 
                pstmt.close(); 
                connection.close(); 
                
                System.out.println("Successful Search Operation.");
                
            } catch (SQLException ex) {
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "SQL error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                System.out.println("Error in parsing Date\n");
            }
        }
    }
    
    private class filterOperation implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String searchValue = field.getText().trim(); // نص البحث
        String selectedColumn = list.getSelectedItem().toString(); // العمود المختار
        String genre = genreComboBox.getSelectedItem().toString();
        String author = authorComboBox.getSelectedItem().toString();
        String year = yearComboBox.getSelectedItem().toString();

        // بدء بناء الاستعلام
        String query = "SELECT Title, Author, Genre, Descripation, PublicationDate FROM Books WHERE 1=1";

        // إضافة نص البحث إذا كان موجودًا
        if (!searchValue.isEmpty()) {
            query += " AND " + selectedColumn + " LIKE '%" + searchValue + "%'";
        }

        // إضافة تصفية النوع إذا تم تحديده
        if (!genre.equals("All")) {
            query += " AND Genre = '" + genre + "'";
        }

        // إضافة تصفية المؤلف إذا تم تحديده
        if (!author.equals("All")) {
            query += " AND Author = '" + author + "'";
        }

        // إضافة تصفية السنة إذا تم تحديدها
        if (!year.equals("All")) {
            query += " AND YEAR(PublicationDate) = '" + year + "'";
        }

        try (Connection connection = DatabaseConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            model.setRowCount(0); // مسح البيانات السابقة من الجدول
            int rowNumber = 1;

            if (!rs.isBeforeFirst()) { // التحقق إذا كانت النتائج فارغة
                throw new CustomExceptions.NoresultException("No results found for the selected filters and search criteria.");
            } else {
                while (rs.next()) {
                    String No = "" + rowNumber;
                    String title = rs.getString("Title");
                    author = rs.getString("Author");
                    genre = rs.getString("Genre");
                    String description = rs.getString("Descripation");
                    String rawDate = rs.getString("PublicationDate");
                    String formattedDate = "";

                    if (rawDate != null && !rawDate.isEmpty()) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate);
                        formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    }

                    model.addRow(new Object[]{No, title, author, genre, description, formattedDate});
                    rowNumber++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            System.out.println("Error in parsing Date\n");
        } catch (CustomExceptions.NoresultException e2) {
            JOptionPane.showMessageDialog(null, e2.getMessage(), "No Results", JOptionPane.WARNING_MESSAGE);
        }
    }
}}

       
    
