public class Main {
    public static void main(String[] args) {
        RegisterForm myform = new RegisterForm(null); // Main part of Registration form
        User user = myform.user;
        if (user != null ) {
            System.out.println("Successfully registration of: " + user.name);
        } else {
            System.out.println("Registration canceled");
        }
    }
}