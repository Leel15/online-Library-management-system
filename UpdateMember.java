import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.util.Date;

public class UpdateMember extends JFrame {

    private JLabel labelUserID, labelUserName, labelPassword, labelDOB, labelGender, labelEmail, labelRole;
    private JTextField textUserID, textUserName, textEmail;
    private JPasswordField passwordTextField;
    private JDateChooser datePublishedText;
    private JRadioButton radioMale, radioFemale, radioAdmin, radioMember;
    private JButton buttonUpd, buttonBack, buttonSearch;
    private ButtonGroup genderGroup, roleGroup;

    public UpdateMember() {
        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new GridLayout(7, 1));

        labelUserID = new JLabel("User ID");
        labelUserID.setBorder(new EmptyBorder(0, 30, 0, 30));
        
        textUserID = new JTextField(5);
        labelUserID.setFont(fontText);
        
        buttonSearch = new JButton("Search");
        buttonSearch.setFont(fontButton);
        
        JPanel panelUserID = new JPanel(new FlowLayout());
        panelUserID.add(labelUserID);
        panelUserID.add(textUserID);
        panelUserID.add(buttonSearch);

        labelUserName = new JLabel("User Name");
        labelUserName.setBorder(new EmptyBorder(0, 20, 0, 20));
        textUserName = new JTextField(15);
        labelUserName.setFont(fontText);
        JPanel panelUserName = new JPanel(new FlowLayout());
        panelUserName.add(labelUserName);
        panelUserName.add(textUserName);

        labelPassword = new JLabel("Password");
        labelPassword.setBorder(new EmptyBorder(0, 20, 0, 20));
        passwordTextField = new JPasswordField(15);
        labelPassword.setFont(fontText);
        JPanel panelPassword = new JPanel(new FlowLayout());
        panelPassword.add(labelPassword);
        panelPassword.add(passwordTextField);

        labelDOB = new JLabel("Date of Birth");
        labelDOB.setBorder(new EmptyBorder(0, 20, 0, 20));
        labelDOB.setFont(fontText);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 18));
        JPanel panelDOB = new JPanel(new FlowLayout());
        panelDOB.add(labelDOB);
        panelDOB.add(datePublishedText);

        labelGender = new JLabel("Gender");
        labelGender.setBorder(new EmptyBorder(0, 20, 0, 20));
        labelGender.setFont(fontText);
        radioMale = new JRadioButton("Male");
        radioFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        JPanel panelGender = new JPanel(new FlowLayout());
        panelGender.add(labelGender);
        panelGender.add(radioMale);
        panelGender.add(radioFemale);

        labelRole = new JLabel("Role");
        labelRole.setBorder(new EmptyBorder(0, 40, 0, 20));
        labelRole.setFont(fontText);
        radioAdmin = new JRadioButton("Admin");
        radioMember = new JRadioButton("Member");
        roleGroup = new ButtonGroup();
        roleGroup.add(radioAdmin);
        roleGroup.add(radioMember);
        JPanel panelRole = new JPanel(new FlowLayout());
        panelRole.add(labelRole);
        panelRole.add(radioAdmin);
        panelRole.add(radioMember);

        labelEmail = new JLabel("Email");
        labelEmail.setBorder(new EmptyBorder(0, 20, 0, 20));
        textEmail = new JTextField(15);
        labelEmail.setFont(fontText);
        JPanel panelEmail = new JPanel(new FlowLayout());
        panelEmail.add(labelEmail);
        panelEmail.add(textEmail);

        buttonUpd = new JButton("Update");
        buttonUpd.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonUpd);
        buttonPanel.add(buttonBack);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panelUserID);
        mainPanel.add(panelUserName);
        mainPanel.add(panelPassword);
        mainPanel.add(panelDOB);
        mainPanel.add(panelGender);
        mainPanel.add(panelRole);
        mainPanel.add(panelEmail);

        JPanel mainContainer = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("Update Member Details");
        title.setTitleFont(fontTitle);
        mainContainer.setBorder(title);

        mainContainer.add(mainPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        buttonSearch.addActionListener(new SearchButton());
        buttonUpd.addActionListener(new UpdateButton());
        buttonBack.addActionListener(new BackButton());

        this.setVisible(true);
    }

    public class SearchButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (textUserID.getText().equals("")) {
                    throw new CustomExceptions.EmptyFieldsException("User ID is required!");
                }

                int userID = Integer.parseInt(textUserID.getText());
                loadUserData(userID); 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "User ID must be a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (CustomExceptions.EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Empty Fields", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public class UpdateButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (textUserName.getText().equals("") || passwordTextField.getText().equals("") ||
                        datePublishedText.getDate() == null || textEmail.getText().equals("") ||
                        (!radioMale.isSelected() && !radioFemale.isSelected()) ||
                        (!radioAdmin.isSelected() && !radioMember.isSelected())) {
                    throw new CustomExceptions.EmptyFieldsException("All fields are required!");
                }

                String userName = textUserName.getText();
                String password = new String(passwordTextField.getPassword());
                String email = textEmail.getText();
                String gender = radioMale.isSelected() ? "Male" : "Female";
                String role = radioAdmin.isSelected() ? "Admin" : "Member";

                java.util.Date utilDate = datePublishedText.getDate();
                java.sql.Date dob = new java.sql.Date(utilDate.getTime());

                int userID = Integer.parseInt(textUserID.getText());
                if (updateUser(userID, userName, email, password, role, gender, dob)) {
                    JOptionPane.showMessageDialog(null, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new CustomExceptions.DatabaseException("Failed to update member. Please try again.");
                }
            } catch (CustomExceptions.EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Empty Fields", JOptionPane.WARNING_MESSAGE);
            } catch (CustomExceptions.DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class BackButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new DashboardAdmin(); 
            dispose();
        }
    }

    public void loadUserData(int userID) {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/baato/Downloads/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                textUserName.setText(rs.getString("UserName"));
                passwordTextField.setText(rs.getString("Password"));
                datePublishedText.setDate(rs.getDate("DateOfBitrthd"));
                textEmail.setText(rs.getString("Email"));
                if (rs.getString("Gender").equalsIgnoreCase("Male")) {
                    radioMale.setSelected(true);
                } else {
                    radioFemale.setSelected(true);
                }
                if (rs.getString("Role").equalsIgnoreCase("Admin")) {
                    radioAdmin.setSelected(true);
                } else {
                    radioMember.setSelected(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean updateUser(int userID, String userName, String email, String password, String role, String gender, java.sql.Date dob) {
        String query = "UPDATE Users SET UserName = ?, Email = ?, Password = ?, Role = ?, Gender = ?, DateOfBitrthd = ? WHERE UserID = ?";
        try (Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/baato/Downloads/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query)) {

            stmt.setString(1, userName);
            stmt.setString(2, email);            
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, gender);
            stmt.setDate(6, dob);
            stmt.setInt(7, userID);


            int rowsUpdated = stmt.executeUpdate();
             if (rowsUpdated > 0)
                return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


   }
