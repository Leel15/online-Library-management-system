import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.text.SimpleDateFormat;

public class signUp extends JFrame {

    private JLabel labelUserName, labelPassword, labelDOB, labelGender, labelEmail, labelRole , emailErrorLabel, passwordErrorLabel;
    private JTextField textUserName, textEmail;
    private JPasswordField passwordTextField;
    private JRadioButton radioMale, radioFemale;
    private JButton buttonSignUP, buttonBack;
    private ButtonGroup genderGroup, roleGroup;
    private JDateChooser datePublishedText;

    public signUp() {

        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

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
       
        
        passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        JPanel passwordFieldPanel = new JPanel(new BorderLayout());
        passwordFieldPanel.add(passwordTextField, BorderLayout.CENTER);
        passwordFieldPanel.add(passwordErrorLabel, BorderLayout.SOUTH);

        JPanel panelPassword = new JPanel(new FlowLayout());
        panelPassword.add(labelPassword);
        panelPassword.add(passwordFieldPanel);
        
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

       

        labelEmail = new JLabel("Email");
        labelEmail.setBorder(new EmptyBorder(0, 20, 0, 20));
        textEmail = new JTextField(15);
        labelEmail.setFont(fontText);
        
        emailErrorLabel = new JLabel(""); 
        emailErrorLabel.setForeground(Color.RED); 
        emailErrorLabel.setFont(new Font("Arial", Font.PLAIN, 10)); 

        JPanel emailFieldPanel = new JPanel(new BorderLayout());
        emailFieldPanel.add(textEmail, BorderLayout.CENTER); 
        emailFieldPanel.add(emailErrorLabel, BorderLayout.SOUTH); 

        JPanel panelEmail = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEmail.add(labelEmail);
        panelEmail.add(emailFieldPanel);
      

        buttonSignUP = new JButton("Sign Up");
        buttonSignUP.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonSignUP);
        buttonPanel.add(buttonBack);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panelUserName);
        mainPanel.add(panelPassword);
        mainPanel.add(panelDOB);
        mainPanel.add(panelGender);
        mainPanel.add(panelEmail);

        JPanel mainContainer = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("New Member Details");
        title.setTitleFont(fontTitle);
        mainContainer.setBorder(title);

        mainContainer.add(mainPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        buttonSignUP.addActionListener(new AddButton());
        buttonBack.addActionListener(new BackToDashboard());
        textEmail.addKeyListener(new KeyAdapter(){
            public void keyTyped(KeyEvent e){
                emailErrorLabel.setText("");
                textEmail.setBorder(UIManager.getBorder("TextField.border")); 

            }
        });
        passwordTextField.addKeyListener(new KeyAdapter() {
        @Override
            public void keyTyped(KeyEvent e) {
                passwordErrorLabel.setText("");
                passwordTextField.setBorder(UIManager.getBorder("PasswordField.border")); 
            }
});
        this.setVisible(true);
    }

    public class AddButton implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        try {
            if (textUserName.getText().equals("") || passwordTextField.getText().equals("") ||
                    datePublishedText.getDate() == null || textEmail.getText().equals("") ||
                   (!radioMale.isSelected() && !radioFemale.isSelected()))
                     {
                throw new CustomExceptions.EmptyFieldsException("All fields are required!");
            }

            String UserName = textUserName.getText();
            String Password = new String(passwordTextField.getPassword());
            String Email = textEmail.getText();
            String Gender = radioMale.isSelected() ? "Male" : "Female";

           

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
          if (!Password.matches(passwordRegex)) {
                passwordErrorLabel.setText("<html>*Password must be at least <br> 8 characters long and include <br> both letters and numbers.</html>");
                passwordTextField.setBorder(BorderFactory.createLineBorder(Color.RED)); 
                return; 
        } else {
                passwordErrorLabel.setText(""); 
                passwordTextField.setBorder(UIManager.getBorder("PasswordField.border")); 
}

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
              if (!Email.matches(emailRegex)) {
                    emailErrorLabel.setText("*Invalid email format!"); 
                    textEmail.setBorder(BorderFactory.createLineBorder(Color.RED)); 
                    return; 
            } else {
                    emailErrorLabel.setText(""); 
                    textEmail.setBorder(UIManager.getBorder("TextField.border")); 
}
            java.util.Date utilDate = datePublishedText.getDate();
            java.sql.Date DateOfBitrthd = new java.sql.Date(utilDate.getTime());

            if (addNewMember(UserName, Email, Password, Gender, DateOfBitrthd)) {
                JOptionPane.showMessageDialog(null, "Hello in Online Libary Management System!", "Success", JOptionPane.INFORMATION_MESSAGE);
                  int id = getIDOfUser(UserName , Password);
                     User.setUserID(id);
                      new DashboardUser();
                      dispose();

            } else {
                throw new CustomExceptions.DatabaseException("Failed to add member. Try again.");
            }
        } catch (CustomExceptions.EmptyFieldsException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (CustomExceptions.DatabaseException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new OnlineLibaryManagementSystem(); 
            dispose(); 
        }
    }

    public boolean addNewMember(String UserName, String Email, String Password  , String Gender ,java.sql.Date DateOfBitrthd  ) {
        String query = "INSERT INTO Users (UserName, Email, Password, Role, Gender, DateOfBitrthd) VALUES (?, ?, ?, ?, ?, ?)";
        try {
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, UserName);
            stmt.setString(2, Email );
            stmt.setString(3,Password );
            stmt.setString(4, "Member" );
            stmt.setString(5, Gender);
            stmt.setDate(6,DateOfBitrthd);

            
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
       public int getIDOfUser(String username, String password) {
    String query = "SELECT UserID FROM Users WHERE Username = ? AND Password = ?";
    try{
        Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
         PreparedStatement stmt = c.prepareStatement(query);

        stmt.setString(1, username);
        stmt.setString(2, password);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("UserID"); 
        } 
    }catch(SQLException s){
        s.printStackTrace();
    }
    return 0 ;}
   

    
   
}
