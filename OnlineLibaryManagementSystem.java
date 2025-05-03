import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;



public class OnlineLibaryManagementSystem extends JFrame {
    
    private JLabel TitleLabel , userNameLabel , passwordLabel , AccountLabel;
    private JTextField userNameTextField  ;
    private JPasswordField passwordTextField;
    private JButton LogInButton  , signButton ;
    private JCheckBox showPassword ;
    
    public OnlineLibaryManagementSystem(){
        
        
        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);
        
        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14); 
        Font fonttitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16); 

        JPanel mainP = new JPanel (new GridLayout(3,1));

        JPanel m1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        TitleLabel = new  JLabel("Welcome to  Libary Management System");
        TitleLabel.setFont(fonttitle);
        TitleLabel.setBorder(new EmptyBorder(60, 50, 0, 20));

        m1.add(TitleLabel);
        
        JPanel m2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userNameLabel = new JLabel("Username   ");
        userNameLabel.setFont(fontText);
        userNameTextField = new JTextField(15);
        m2.add(userNameLabel);
        m2.add(userNameTextField);
        
        JPanel m3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordLabel = new JLabel("Password   ");
        passwordLabel.setFont(fontText);
        passwordTextField = new JPasswordField(15);
        m3.add(passwordLabel);
        m3.add(passwordTextField);
        
        JPanel m4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        showPassword = new JCheckBox("show Password");
        m4.add(showPassword);
        
       JPanel m5 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        LogInButton = new JButton("  Login  ");
        LogInButton.setPreferredSize(new Dimension(150, 25));
        LogInButton.setFont(fontButton);
        m5.add(LogInButton);
        
         JPanel m6 = new JPanel(new GridLayout(2,1));
         m6.add(m3);
         m6.add(m4);
        
        JPanel m8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        AccountLabel = new JLabel("Don't have a account ? ");
        AccountLabel.setForeground(Color.gray);
        signButton = new JButton("Sign Up");
        signButton.setFont(fontButton);
        signButton.setPreferredSize(new Dimension(150, 25));
        AccountLabel.setBorder(new EmptyBorder(0, 250, 0, 30));

        m8.add(AccountLabel);
        m8.add(signButton);
        
       JPanel m7 = new JPanel(new GridLayout(3,1));
       m7.add(m5);
       m7.add(m8);




      
        mainP.add(m1);
        mainP.add(m2);
        mainP.add(m6);

            JPanel mainpanel = (JPanel) this.getContentPane();
            TitledBorder title;
            title = BorderFactory.createTitledBorder("Online Libary Management System");
            title.setTitleFont(fonttitle);
            mainpanel.setBorder(title);
            mainpanel.add(mainP,BorderLayout.CENTER);
            mainpanel.add(m7,BorderLayout.SOUTH);

        LogInButton.addActionListener(new LogInButtonAction());
        signButton.addActionListener(new signButton());
       showPassword.addItemListener(new ItemListener() { 
                     public void itemStateChanged(ItemEvent e) { 
                         if (e.getStateChange() == ItemEvent.SELECTED) {
                                 passwordTextField.setEchoChar((char) 0);  
                       } else {  
                                  passwordTextField.setEchoChar('*');  
            } 
    }
});
      this.setVisible(true);
      
         
    }
    public class signButton implements ActionListener{
        public void actionPerformed(ActionEvent e){
            new signUp();
            dispose();
        }}
    
     public class LogInButtonAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try{
           if(userNameTextField.getText().equals(""))
               throw new CustomExceptions.EmptyException("User Name is Empty , Please Enter User Name");
            if(passwordTextField.getText().equals(""))
                throw new CustomExceptions.EmptyException("Password is Empty , Please Enter Password");
            
            String uName = userNameTextField.getText();
            String password = new String(passwordTextField.getPassword());
            int id = getIDOfUser(uName , password);
            User.setUserID(id);
            String role = authenticate(uName ,password );
            if(role == null)
                   throw new CustomExceptions.ErrorLogin("Login unsuccessful , Check your information");
            else if(role.equals("Admin")){
                 JOptionPane.showMessageDialog(null , "Welcome Admin ! ","Login successful",JOptionPane.INFORMATION_MESSAGE);
                    new DashboardAdmin();

            }
            else if(role.equals("Member")){
                 JOptionPane.showMessageDialog(null , "Welcome Member ! ","Login successful",JOptionPane.INFORMATION_MESSAGE);
                new DashboardUser();

            }
               dispose();

            }catch(CustomExceptions.EmptyException e2){
               JOptionPane.showMessageDialog(null , e2.getMessage(),"Empty text",JOptionPane.WARNING_MESSAGE);
            }catch(CustomExceptions.ErrorLogin e2){
               JOptionPane.showMessageDialog(null , e2.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }

    
    public String authenticate(String Uname , String password){
        String role = null ;
        String query = "SELECT Role FROM Users WHERE UserName = ? AND Password = ? ";
        try{
            
            Connection c = DatabaseConnection();
          PreparedStatement stmt = c.prepareStatement(query);
          stmt.setString(1, Uname);
          stmt.setString(2,password);
          ResultSet rs = stmt.executeQuery();
          if( rs.next())
              role = rs.getString("Role");
        }catch(SQLException e){
            e.printStackTrace();
        }
        return role ;
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
    public int getIDOfUser(String username, String password) {
    String query = "SELECT UserID FROM Users WHERE Username = ? AND Password = ?";
    try{
        Connection c = DatabaseConnection();
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
   


    public static void main(String[] args) {
       OnlineLibaryManagementSystem L1 = new OnlineLibaryManagementSystem();

    }
}
