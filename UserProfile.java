 import com.toedter.calendar.JDateChooser;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class UserProfile extends JFrame {
    private JLabel labelUserName, labelDOB, labelGender, labelEmail, emailErrorLabel;
    private JTextField textUserName, textEmail;
    private JRadioButton radioMale, radioFemale;
    private JButton buttonSave, buttonBack;
    private ButtonGroup genderGroup;
    private JDateChooser datePublishedText;

    private int userId; // ID of the user (for fetching data)
    
    public UserProfile() {

        this.setTitle("User Profile");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontLabel = new Font("Segoe UI", Font.BOLD, 16);
        Font fontError = new Font("Arial", Font.PLAIN, 12);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labelUserName = new JLabel("Username:");
        labelUserName.setFont(fontLabel);
        textUserName = new JTextField(20);
        textUserName.setFont(fontText);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(labelUserName, gbc);
        gbc.gridx = 1;
        mainPanel.add(textUserName, gbc);

        labelDOB = new JLabel("Date of Birth:");
        labelDOB.setFont(fontLabel);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 25));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(labelDOB, gbc);
        gbc.gridx = 1;
        mainPanel.add(datePublishedText, gbc);

        labelGender = new JLabel("Gender:");
        labelGender.setFont(fontLabel);
        radioMale = new JRadioButton("Male");
        radioFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.add(radioMale);
        genderPanel.add(radioFemale);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(labelGender, gbc);
        gbc.gridx = 1;
        mainPanel.add(genderPanel, gbc);

        labelEmail = new JLabel("Email:");
        labelEmail.setFont(fontLabel);
        textEmail = new JTextField(20);
        textEmail.setFont(fontText);
        emailErrorLabel = new JLabel("");
        emailErrorLabel.setFont(fontError);
        emailErrorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(labelEmail, gbc);
        gbc.gridx = 1;
        mainPanel.add(textEmail, gbc);
        gbc.gridy = 4;
        mainPanel.add(emailErrorLabel, gbc);

        buttonSave = new JButton("Save Changes");
        buttonSave.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonBack);

        JPanel containerPanel = new JPanel(new BorderLayout());
        TitledBorder title = BorderFactory.createTitledBorder("User Profile");
        title.setTitleFont(fontLabel);
        containerPanel.setBorder(title);
        containerPanel.add(mainPanel, BorderLayout.CENTER);
        containerPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.setContentPane(containerPanel);

        loadUserData();

        buttonSave.addActionListener(new SaveButtonAction());
        buttonBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int id = User.getUserID();
                String role = "";
                String query = "SELECT Role FROM Users Where UserID = ? "; 
                
                try { 
                    
                   Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");  
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
        this.setVisible(true);
    }
    
    

       public void loadUserData() {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection c = DatabaseConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, User.getUserID());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                textUserName.setText(rs.getString("UserName"));
                datePublishedText.setDate(rs.getDate("DateOfBitrthd"));
                textEmail.setText(rs.getString("Email"));
                if (rs.getString("Gender").equalsIgnoreCase("Male")) {
                    radioMale.setSelected(true);
                } else {
                    radioFemale.setSelected(true);
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveButtonAction implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        String userName = textUserName.getText();
        String email = textEmail.getText();
        String gender = radioMale.isSelected() ? "Male" : "Female";
        java.util.Date dob = datePublishedText.getDate();

        // Validate fields
        if (userName.isEmpty() || email.isEmpty() || dob == null) {
            JOptionPane.showMessageDialog(UserProfile.this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) {
            emailErrorLabel.setText("Invalid email format!");
            return;
        } else {
            emailErrorLabel.setText("");
        }

        // Update the user's data in the database
        String query = "UPDATE Users SET UserName = ?, Email = ?, Gender = ?, DateOfBitrthd = ? WHERE UserID = ?";
        try (Connection c = DatabaseConnection();
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, userName);
            stmt.setString(2, email);
            stmt.setString(3, gender);
            stmt.setDate(4, new java.sql.Date(dob.getTime()));
            stmt.setInt(5, User.getUserID());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(UserProfile.this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(UserProfile.this, "Failed to save changes!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(UserProfile.this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
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

   
}
