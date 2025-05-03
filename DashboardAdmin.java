import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DashboardAdmin extends JFrame {

    private JLabel titleLabel, descriptionLabel;
    private JTextArea notificationArea;

    public DashboardAdmin() {
        this.setTitle("Library Management System - Admin Dashboard");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Fonts
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

        String welcome = "<html><div style='text-align: center;'>" +
                 "Welcome to Online Library Management System" +
                 "</div></html>";
         titleLabel = new JLabel(welcome, SwingConstants.CENTER);
         titleLabel.setFont(titleFont);
         titleLabel.setForeground(new Color(34, 139, 230));
         titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

         // Description Label
         String description = "<html><div style='text-align: center; margin: 10px;'>" +
                 "Welcome to <b>Online Library Management System</b>, where you can manage your<br>" +
                 "borrowed books, check availability, and stay notified about your borrowings.<br><br>" +
                 "Enjoy a user-friendly interface tailored to enhance your library experience." +
                 "</div></html>";
         descriptionLabel = new JLabel(description, SwingConstants.CENTER);
         descriptionLabel.setFont(textFont);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Spacing
        mainPanel.add(descriptionLabel);

        // Scrollable Notifications Area
        notificationArea = new JTextArea(20, 30);
        notificationArea.setEditable(false);
        notificationArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(notificationArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Notifications"));

        // Layout Configuration
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(scrollPane, BorderLayout.EAST);

        // Set Content Pane
        this.setContentPane(contentPanel);

        // Menu Bar
        JMenuBar mb = new JMenuBar();
        JMenu book = new JMenu("Book");
        JMenu member = new JMenu("Member");
        JMenu content = new JMenu("=");
        JMenuItem Profile = new JMenuItem("Profile");
        JMenuItem addBook = new JMenuItem("Add Book");
        JMenuItem Borrowed = new JMenuItem("Borrowed Book");
        JMenuItem Search = new JMenuItem("Search Book");
        JMenuItem Update = new JMenuItem("Update Book");
        JMenuItem AddMember = new JMenuItem("Add Member");
        JMenuItem UpdateMember = new JMenuItem("Update Member");
        JMenuItem Statistic = new JMenuItem("Statistics Report");
        JMenuItem ExitOption = new JMenuItem("Log Out");

        setJMenuBar(mb);
        mb.add(content);
        mb.add(book);
        mb.add(member);

        book.add(addBook);
        book.add(Borrowed);
        book.add(Search);
        book.add(Update);
        book.add(Statistic);

        member.add(AddMember);
        member.add(UpdateMember);

        content.add(Profile);    
        content.add(new JSeparator(JSeparator.HORIZONTAL));
        content.add(ExitOption);

     

        // Action Listeners
        Profile.addActionListener(new profileUser());
        addBook.addActionListener(new addBookButton());
        Borrowed.addActionListener(new BorrowedAction());
        Search.addActionListener(new searchBook());
        Update.addActionListener(new updateBook());
        AddMember.addActionListener(new addMember());
        UpdateMember.addActionListener(new updateMember());
        Statistic.addActionListener(new statisticReport());
        ExitOption.addActionListener(new Logout());
        // Load Notifications Initially
        loadNotifications();

        this.setVisible(true);
    }
private Connection getConnection() throws SQLException {
        String url = "jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb";
        return DriverManager.getConnection(url);
    }

    // Load Notifications from Database
    private void loadNotifications() {
        notificationArea.setText(""); // Clear existing notifications

        try (Connection conn = getConnection()) {
            // Overdue Books
            String overdueQuery = "SELECT u.UserName, b.Title, br.ReturnDate FROM Borrowings br " +
                                  "JOIN Books b ON br.BookID = b.BookID " +
                                  "JOIN Users u ON br.UserID = u.UserID " +
                                  "WHERE br.ReturnDate < DATE() AND br.IsReturned = FALSE";
            try (PreparedStatement stmt = conn.prepareStatement(overdueQuery);
                 ResultSet rs = stmt.executeQuery()) {

                notificationArea.append("Overdue Books:\n");
                while (rs.next()) {
                    notificationArea.append("User: " + rs.getString("UserName") +
                            ", Book: " + rs.getString("Title") +
                            ", Due Date: " + rs.getDate("ReturnDate") + "\n");
                }
            }

            // New Borrowings Today
            String newBorrowQuery = "SELECT u.UserName, b.Title, br.BorrowDate FROM Borrowings br " +
                                    "JOIN Books b ON br.BookID = b.BookID " +
                                    "JOIN Users u ON br.UserID = u.UserID " +
                                    "WHERE br.BorrowDate = DATE()";
            try (PreparedStatement stmt = conn.prepareStatement(newBorrowQuery);
                 ResultSet rs = stmt.executeQuery()) {

                notificationArea.append("\nNew Borrowings Today:\n");
                while (rs.next()) {
                    notificationArea.append("User: " + rs.getString("UserName") +
                            ", Book: " + rs.getString("Title") +
                            ", Borrow Date: " + rs.getDate("BorrowDate") + "\n");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Action Listener Classes
    public class profileUser implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new UserProfile();
            dispose();
        }
    }

    public class addBookButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new addBook();
            dispose();
        }
    }

    public class BorrowedAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BorrowedBook();
            dispose();
        }
    }

    public class searchBook implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BookSearch();
            dispose();
        }
    }

    public class updateBook implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BookInventory();
            dispose();
        }
    }

    public class addMember implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new NewMember();
            dispose();
        }
    }

    public class updateMember implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new UpdateMember();
            dispose();
        }
    }

    public class statisticReport implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new ReportModule();
            dispose();
        }
    
    }
    public class Logout implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int n = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    
}}
