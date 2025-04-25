package BaiTapMaHoa;

import javax.swing.*;

public class Bai2 extends JFrame {
    private JTextField inputField;
    private JTextArea encryptedArea, decryptedArea;
    private JButton encryptButton;
    private JComboBox<String> algorithmBox;

    private Encryptable encryptor;

    public Bai2() {
        setTitle("Encryption Demo (RSA / AES)");
        setSize(400, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setBounds(20, 20, 80, 25);
        add(algoLabel);

        algorithmBox = new JComboBox<>(new String[]{"RSA", "AES"});
        algorithmBox.setBounds(100, 20, 100, 25);
        add(algorithmBox);

        JLabel inputLabel = new JLabel("Input:");
        inputLabel.setBounds(20, 60, 100, 25);
        add(inputLabel);

        inputField = new JTextField();
        inputField.setBounds(80, 60, 280, 25);
        add(inputField);

        encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(140, 100, 100, 25);
        add(encryptButton);

        JLabel encryptedLabel = new JLabel("Encrypted:");
        encryptedLabel.setBounds(20, 140, 100, 25);
        add(encryptedLabel);

        encryptedArea = new JTextArea();
        encryptedArea.setBounds(20, 170, 350, 50);
        encryptedArea.setLineWrap(true);
        encryptedArea.setWrapStyleWord(true);
        encryptedArea.setEditable(false);
        add(encryptedArea);

        JLabel decryptedLabel = new JLabel("Decrypted:");
        decryptedLabel.setBounds(20, 230, 100, 25);
        add(decryptedLabel);

        decryptedArea = new JTextArea();
        decryptedArea.setBounds(20, 260, 350, 50);
        decryptedArea.setLineWrap(true);
        decryptedArea.setWrapStyleWord(true);
        decryptedArea.setEditable(false);
        add(decryptedArea);

        encryptButton.addActionListener(e -> {
            String algo = (String) algorithmBox.getSelectedItem();
            if (algo.equals("RSA")) {
                encryptor = new RSAEncryptor();
            } else if (algo.equals("AES")) {
                encryptor = new AESEncryptor();
            }

            String input = inputField.getText();
            String encrypted = encryptor.encrypt(input);
            String decrypted = encryptor.decrypt(encrypted);

            encryptedArea.setText(encrypted);
            decryptedArea.setText(decrypted);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Bai2().setVisible(true));
    }
}
