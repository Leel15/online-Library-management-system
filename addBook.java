import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import javax.swing.border.TitledBorder;

public class addBook extends JFrame {

    private JLabel bookName, author, datePublished, isbn, description, Genre;
    private JTextField bookNameText, authorText, isbnText, descriptionText, GenreText;
    private JDateChooser datePublishedText;
    private JButton addBookButton, backButton;
    

    public addBook() {
        
        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);

        JPanel mainPanel = new JPanel (new GridLayout(6, 1));
        
        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14); 
        Font fonttitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16); 

        bookName = new JLabel("Book Name");
        bookName.setBorder(new EmptyBorder(0, 20, 0, 20));
        bookName.setFont(fontText);
        bookNameText = new JTextField(15);
        JPanel m1 = new JPanel(new FlowLayout());
        m1.add(bookName);
        m1.add(bookNameText);

        author = new JLabel("Author");
        author.setBorder(new EmptyBorder(0, 20, 0, 50));
        author.setFont(fontText);
        authorText = new JTextField(15);
        JPanel m2 = new JPanel(new FlowLayout());
        m2.add(author);
        m2.add(authorText);

        Genre = new JLabel("Genre");
        Genre.setBorder(new EmptyBorder(0, 20, 0, 50));
        Genre.setFont(fontText);
        GenreText = new JTextField(15);
        JPanel m3 = new JPanel(new FlowLayout());
        m3.add(Genre);
        m3.add(GenreText);

        datePublished = new JLabel("Date Published");
        datePublished.setFont(fontText);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 20));
        datePublished.setBorder(new EmptyBorder(0, 20, 0, 5));
        JPanel m4 = new JPanel(new FlowLayout());
        m4.add(datePublished);
        m4.add(datePublishedText);

        isbn = new JLabel("ISBN");
        isbn.setBorder(new EmptyBorder(0, 20, 0, 60));
        isbn.setFont(fontText);
        isbnText = new JTextField(15);
        JPanel m5 = new JPanel(new FlowLayout());
        m5.add(isbn);
        m5.add(isbnText);

        description = new JLabel("Description");
        description.setFont(fontText);
        description.setBorder(new EmptyBorder(0, 20, 0, 25));
        descriptionText = new JTextField(15);
        JPanel m6 = new JPanel(new FlowLayout());
        m6.add(description);
        m6.add(descriptionText);

        addBookButton = new JButton("Add Book");
        addBookButton.setFont(fontButton);
        backButton = new JButton("Back");
        backButton.setFont(fontButton);
        JPanel m7 = new JPanel(new FlowLayout());
        m7.add(addBookButton);
        m7.add(backButton);

    
        mainPanel.add(m1);
        mainPanel.add(m2);
        mainPanel.add(m3);
        mainPanel.add(m4);
        mainPanel.add(m5);
        mainPanel.add(m6);

           JPanel mainpanel = (JPanel) this.getContentPane();
            TitledBorder title;
            title = BorderFactory.createTitledBorder("Add Book");
            title.setTitleFont(fonttitle);
            mainpanel.setBorder(title);
            mainpanel.add(mainPanel,BorderLayout.CENTER);
            mainpanel.add(m7,BorderLayout.SOUTH);
            
        addBookButton.addActionListener(new AddButton());
        backButton.addActionListener(new BackToDashboard());
        this.setVisible(true);
    }

    public class AddButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (bookNameText.getText().equals("") || authorText.getText().equals("") || 
                    isbnText.getText().equals("") || descriptionText.getText().equals("") || 
                    datePublishedText.getDate() == null || GenreText.getText().equals("")) {
                    throw new CustomExceptions.EmptyException("Information Not completed!\nPlease Enter all Information");
                }

                String bookName = bookNameText.getText();
                String author = authorText.getText();
                String genre = GenreText.getText();

                java.util.Date utilDate = datePublishedText.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                String isbn = isbnText.getText();
                String description = descriptionText.getText();

                if (addBookQuery(bookName, author, genre, isbn, sqlDate, description)) {
                    JOptionPane.showMessageDialog(null, "Add Book Successful", "Successful", JOptionPane.INFORMATION_MESSAGE);
                    bookNameText.setText("");
                    authorText.setText("");
                    isbnText.setText("");
                    descriptionText.setText("");
                    GenreText.setText("");
                    datePublishedText.setDate(null);
 
                } else {
                    throw new CustomExceptions.ErrorAdd("Add Book unsuccessful");
                }
            } catch (CustomExceptions.EmptyException e2) {
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Information Not Completed", JOptionPane.WARNING_MESSAGE);
            } catch (CustomExceptions.ErrorAdd e2) {
                JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new DashboardAdmin();
            dispose();
        }
    }

    public boolean addBookQuery(String Title, String Author, String Genre, String isbn, java.sql.Date PublicationDate, String description) {
        String query = "INSERT INTO Books (Title, Author, Genre, PublicationDate, ISBN, Descripation, Avaliable) VALUES (?,?,?,?,?,?,?)";
        try {
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/baato/Downloads/LibraryDB.accdb");
            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, Title);
            stmt.setString(2, Author);
            stmt.setString(3, Genre);
            stmt.setDate(4, PublicationDate);  
            stmt.setString(5, isbn);
            stmt.setString(6, description);
            stmt.setBoolean(7, true); 

            int inserted = stmt.executeUpdate();
            if (inserted > 0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
   
}
