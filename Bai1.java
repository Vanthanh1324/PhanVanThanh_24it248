package BaiTapMaHoa;

import javax.swing.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Bai1 extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private final String correctUsername = "thanh";
    
    private final String correctHashedPassword = md5("123456");

    public Bai1() {
        setTitle("Login with MD5");
        setSize(300, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(20, 20, 80, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 20, 160, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 55, 80, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 55, 160, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 90, 80, 25);
        add(loginButton);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        String hashed = md5(pass);

        if (user.equals(correctUsername) && hashed.equals(correctHashedPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            this.setVisible(false); // Ẩn màn hình đăng nhập
            new WelcomeFrame(this); // Hiện màn hình Welcome
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
        }
    }

    private static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai1().setVisible(true));
    }
}
