import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton registerButton;

    public DashboardForm() {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500 , 429));
        setSize(1200,700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegistredUsers = connectToDatabase();
        if (hasRegistredUsers) {
            //show login form
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                lbAdmin.setText("User:" + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                dispose();
            }
        }
        else {
            //Registration Form
            RegisterForm registerForm = new RegisterForm(this);
            User user = registerForm.user;

            if (user != null) {
                lbAdmin.setText("User" + user.name);
                setLocationRelativeTo(null);
                setVisible(true);
            }
            else {
                dispose();
            }
        }

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterForm registerForm = new RegisterForm(DashboardForm.this);
                User user = registerForm.user;

                /*btnNewAccount.addActionListener(e -> {
            RegisterTable registerTable = new RegisterTable(DashboardTable.this);
            User user = registerTable.user;*/

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New User:" + user.name,
                            "Successfully Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegistredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/MyStore?serverTimezone = UTC";
        //HereYouCanSeeTheURLOfMyDatabaseServer -- -- MyStore
        final String USERNAME = "root";
        //ConnectingToThisDatabaseWithRootUsername
        final String PASSWORD = "Azatik2004";
        //AndNoPassword

        try {
            //ConnectingToTheServer
            Connection con = DriverManager.getConnection(MYSQL_SERVER_URL , DB_URL ,PASSWORD);

            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS MyStore");
            statement.close();
            con.close();

            con = DriverManager.getConnection(DB_URL , USERNAME ,PASSWORD);
            statement = con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT(10) NOT NULL PRIMARY KEY AUTO_INCREMENT ,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE, "
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200) "
                    + "password VARCHAR(200) NOT NULL" + ")";
            statement.executeUpdate(sql);

            //checking  users who are already registered
            statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) { //if number of registered people more than 1 , we
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            con.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return hasRegistredUsers;

    }

    public static void main(String[] args) {
        DashboardForm myForm = new DashboardForm();
    }
}
