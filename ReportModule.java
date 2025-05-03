 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReportModule extends JFrame {
    private JComboBox<String> reportComboBox;
    private JButton generateReportButton, backButton;
    private JTextArea resultArea;

    public ReportModule() {
        setTitle("Library Reports");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);

        // Components
        JLabel reportLabel = new JLabel("Select Report Type:");
        String[] reportOptions = {"Library Activity", "Popular Books", "Member Statistics"};
        reportComboBox = new JComboBox<>(reportOptions);
        generateReportButton = new JButton("Generate Report");
        backButton = new JButton("Back");
        resultArea = new JTextArea(20, 40);
        resultArea.setEditable(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(reportLabel);
        topPanel.add(reportComboBox);
        topPanel.add(generateReportButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

         reportComboBox.addActionListener(e -> resultArea.setText("")); // Clear the result when selection changes
        generateReportButton.addActionListener(new GenerateReportAction());
        backButton.addActionListener(new BackToDashboard());

        setVisible(true);
    }

    private class GenerateReportAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedReport = (String) reportComboBox.getSelectedItem();
            resultArea.setText(""); // Clear previous results

            if (selectedReport != null) {
                switch (selectedReport) {
                    case "Library Activity":
                        generateLibraryActivityReport();
                        break;
                    case "Popular Books":
                        generatePopularBooksReport();
                        break;
                    case "Member Statistics":
                        generateMemberStatisticsReport();
                        break;
                }
            }
        }
    }

    private void generateLibraryActivityReport() {
        String query = "SELECT u.UserName, bk.Title, b.BorrowDate, b.ReturnDate " +
                       "FROM Borrowings b " +
                       "JOIN Users u ON b.UserID = u.UserID " +
                       "JOIN Books bk ON b.BookID = bk.BookID";

        try (Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder("Library Activity Report:\n\n");
            int totalBooksBorrowed = 0;

            while (rs.next()) {
                report.append("User Name: ").append(rs.getString("UserName")).append("\n")
                      .append("Book Title: ").append(rs.getString("Title")).append("\n")
                      .append("Borrow Date: ").append(rs.getDate("BorrowDate")).append("\n")
                      .append("Return Date: ").append(rs.getDate("ReturnDate")).append("\n\n");
                totalBooksBorrowed++;
            }

            report.append("Total Books Borrowed: ").append(totalBooksBorrowed);
            resultArea.setText(report.toString());

        } catch (SQLException ex) {
            resultArea.setText("Error fetching Library Activity Report.\n" + ex.getMessage());
        }
    }

    private void generatePopularBooksReport() {
        String query = "SELECT bk.Title, COUNT(br.BookID) AS BorrowCount " +
                       "FROM Books bk " +
                       "JOIN Borrowings br ON bk.BookID = br.BookID " +
                       "GROUP BY bk.Title " +
                       "ORDER BY BorrowCount DESC";

        try (Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder("Popular Books Report:\n\n");
            int totalBorrows = 0;
            boolean firstBook = true;

            while (rs.next()) {
                if (firstBook) {
                    report.append("Most Borrowed Book:\n")
                          .append("Book Title: ").append(rs.getString("Title")).append("\n")
                          .append("Borrow Count: ").append(rs.getInt("BorrowCount")).append("\n\n");
                    firstBook = false;
                } else {
                    report.append("Book Title: ").append(rs.getString("Title")).append("\n")
                          .append("Borrow Count: ").append(rs.getInt("BorrowCount")).append("\n\n");
                }
                totalBorrows += rs.getInt("BorrowCount");
            }

            report.append("Total Borrow Count: ").append(totalBorrows);
            resultArea.setText(report.toString());

        } catch (SQLException ex) {
            resultArea.setText("Error fetching Popular Books Report.\n" + ex.getMessage());
        }
    }

    private void generateMemberStatisticsReport() {
        String query = "SELECT u.UserName, COUNT(br.UserID) AS BorrowCount " +
                       "FROM Users u " +
                       "JOIN Borrowings br ON u.UserID = br.UserID " +
                       "GROUP BY u.UserName " +
                       "ORDER BY BorrowCount DESC";

        try (Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            StringBuilder report = new StringBuilder("Member Statistics Report:\n\n");
            int totalMembers = 0;

            while (rs.next()) {
                report.append("User Name: ").append(rs.getString("UserName")).append("\n")
                      .append("Borrow Count: ").append(rs.getInt("BorrowCount")).append("\n\n");
                totalMembers++;
            }

            report.append("Total Members Borrowed: ").append(totalMembers);
            resultArea.setText(report.toString());

        } catch (SQLException ex) {
            resultArea.setText("Error fetching Member Statistics Report.\n" + ex.getMessage());
        }
    }

    private class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new DashboardAdmin(); 
            dispose();
        }
    }


}
