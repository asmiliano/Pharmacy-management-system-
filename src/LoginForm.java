import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm  extends  JDialog{
    private JPanel panel1;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnEnter;
    private JButton btnCancel;
    private JPanel loginPanel;

    public LoginForm(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450 , 480));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);



        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword());

                user = getAuthenticatedUser(email,password);

                if (user != null) {
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this ,
                            "Email or Password is Invalid" ,
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);


                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });

        setVisible(true);
    }


    public User user;
    private User getAuthenticatedUser(String email , String password){
        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone = UTC";
        final String USERNAME = "root";
        final String PASSWORD = "Azatik2004";


        try {
            Connection con = DriverManager.getConnection(DB_URL , USERNAME , PASSWORD);
            //Connection to the database

            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User user = loginForm.user;

        if (user != null) {
            System.out.println("Successful authentication of:" + user.name);
            System.out.println("           Email: " + user.email);
            System.out.println("           Phone: " + user.phone);
            System.out.println("           Address: " + user.address);
        } else {
            System.out.println("Authentication canceled!");
        }
    }
}
