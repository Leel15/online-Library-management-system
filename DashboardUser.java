  import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class DashboardUser extends JFrame {

    private JLabel titleLabel, descriptionLabel;

    public DashboardUser() {
        this.setTitle("User Dashboard");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Set fonts
        Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font textFont = new Font("Arial", Font.PLAIN, 16);

                // Title Label
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
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(descriptionLabel);

        // Add to main content pane
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);

        // Add Notification Panel
        NotifyUser.initializeNotificationPanel(this);

        // Load Notifications Initially
        loadNotifications();

        // Menu Bar Setup
        JMenuBar mb = new JMenuBar();
        JMenu bookMenu = new JMenu("Book");
        JMenuItem exitOption = new JMenuItem("Log Out");
        JMenu contentMenu = new JMenu("=");

        JMenuItem profileMenuItem = new JMenuItem("Profile");
        JMenuItem searchMenuItem = new JMenuItem("Search Book");
        JMenuItem borrowedMenuItem = new JMenuItem("Borrowed Book");
        JMenuItem borrowMenuItem = new JMenuItem("Borrow Book");

        // Set up the menu bar
        setJMenuBar(mb);
        mb.add(contentMenu);
        mb.add(bookMenu);

        // Add menu items
        bookMenu.add(searchMenuItem);
        bookMenu.add(borrowedMenuItem);
        bookMenu.add(borrowMenuItem);

        contentMenu.add(profileMenuItem);
        contentMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        contentMenu.add(exitOption);


        

        // Action Listeners
        profileMenuItem.addActionListener(new ProfileUserListener());
        searchMenuItem.addActionListener(new SearchBookListener());
        borrowedMenuItem.addActionListener(new ViewBorrowedBooksListener());
        borrowMenuItem.addActionListener(new BorrowBookListener());
        exitOption.addActionListener(new LogoutListener());

        this.setVisible(true);
    }

    // Load Notifications from Database
private void loadNotifications() {
    NotifyUser.clearNotifications(); // Clear existing notifications

    int userID = User.getUserID(); // Get the logged-in user's ID

    try (Connection conn = getConnection()) {
        // Notifications for reminders (books due within the next 3 days)
        String reminderQuery = "SELECT b.Title, br.ReturnDate FROM Borrowings br "
                + "JOIN Books b ON br.BookID = b.BookID "
                + "WHERE br.UserID = ? AND br.ReturnDate BETWEEN DATE() AND DATE() + 3 AND br.IsReturned = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(reminderQuery)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String returnDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("Return Reminder: " + title + " is due on " + returnDate, "Reminder");
                }
            }
        }

        // Notifications for overdue books
        String overdueQuery = "SELECT b.Title, br.ReturnDate FROM Borrowings br "
                + "JOIN Books b ON br.BookID = b.BookID "
                + "WHERE br.UserID = ? AND br.ReturnDate < DATE() AND br.IsReturned = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(overdueQuery)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String overdueDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("Overdue: " + title + " was due on " + overdueDate, "Overdue");
                }
            }
        }

        // Notifications for new borrowings
        String newBorrowQuery = "SELECT b.Title, br.BorrowDate, br.ReturnDate FROM Borrowings br "
                + "JOIN Books b ON br.BookID = b.BookID "
                + "WHERE br.UserID = ? AND br.BorrowDate = DATE()";
        try (PreparedStatement stmt = conn.prepareStatement(newBorrowQuery)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String borrowDate = rs.getDate("BorrowDate").toString();
                    String returnDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("New Borrow: " + title + " (from " + borrowDate + " to " + returnDate + ")", "New Borrow");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    // Establish a database connection
    private Connection getConnection() throws SQLException {
        String url = "jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb";
        return DriverManager.getConnection(url);
    }

    // Action Listener for Profile
    public class ProfileUserListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new UserProfile();
            dispose();
        }
    }

    // Action Listener for Searching Books
    public class SearchBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BookSearch();
            dispose();
        }
    }

    // Action Listener for Viewing Borrowed Books
    public class ViewBorrowedBooksListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BorrowedTableBook();
            dispose();
        }
    }

    // Action Listener for Borrowing Books
    public class BorrowBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BooksTable();
            dispose();
        }
    }

    // Action Listener for Logout
    public class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int n = JOptionPane.showConfirmDialog(null, "Do you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
            if (n == 0) System.exit(0);
        }
    }

    // Notification System
    public static class NotifyUser {

        private static final JPanel notificationPanel = new JPanel();
        private static final Queue<String> notificationQueue = new LinkedList<>();

        public static void initializeNotificationPanel(JFrame frame) {
            notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
            notificationPanel.setBackground(Color.LIGHT_GRAY);
            notificationPanel.setPreferredSize(new Dimension(300, frame.getHeight()));
            notificationPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));

            JScrollPane scrollPane = new JScrollPane(notificationPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            frame.add(scrollPane, BorderLayout.EAST);
        }

        public static void addNotification(String message, String title) {
            notificationQueue.add("<html><b>" + title + ":</b> " + message + "</html>");
            updateNotificationPanel();
        }

        public static void updateNotificationPanel() {
            notificationPanel.removeAll();
            for (String notification : notificationQueue) {
                JLabel label = new JLabel(notification);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                notificationPanel.add(label);
            }
            notificationPanel.revalidate();
            notificationPanel.repaint();
        }

        public static void clearNotifications() {
            notificationQueue.clear();
            updateNotificationPanel();
        }
    }

   
}
