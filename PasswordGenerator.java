import java.awt.*;
import java.awt.datatransfer.*;
import java.security.SecureRandom;
import javax.swing.*;

public class PasswordGenerator extends JFrame {
    private JTextField passwordField;
    private JSlider lengthSlider;
    private JLabel lengthLabel;
    private JCheckBox uppercaseCheckbox;
    private JCheckBox lowercaseCheckbox;
    private JCheckBox numbersCheckbox;
    private JCheckBox specialCharsCheckbox;
    
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER_CHARS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>/?";
    
    private SecureRandom random = new SecureRandom();
    
    public PasswordGenerator() {
        setTitle("Password Generator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Password display area
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 10));
        passwordField = new JTextField(20);
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 16));
        
        JButton copyButton = new JButton("Copy");
        copyButton.addActionListener(e -> copyToClipboard());
        
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(copyButton, BorderLayout.EAST);
        
        // Options panel
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        // Length slider
        JPanel sliderPanel = new JPanel(new BorderLayout());
        lengthLabel = new JLabel("Password Length: 12");
        lengthSlider = new JSlider(JSlider.HORIZONTAL, 4, 32, 12);
        lengthSlider.setMajorTickSpacing(4);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        
        lengthSlider.addChangeListener(e -> {
            int value = lengthSlider.getValue();
            lengthLabel.setText("Password Length: " + value);
            if (!lengthSlider.getValueIsAdjusting()) {
                generatePassword();
            }
        });
        
        sliderPanel.add(lengthLabel, BorderLayout.NORTH);
        sliderPanel.add(lengthSlider, BorderLayout.CENTER);
        
        // Character options
        JPanel charOptionsPanel = new JPanel(new GridLayout(5, 5, 50, 50));
        uppercaseCheckbox = new JCheckBox("Uppercase (A-Z)", true);
        lowercaseCheckbox = new JCheckBox("Lowercase (a-z)", true);
        numbersCheckbox = new JCheckBox("Numbers (0-9)", true);
        specialCharsCheckbox = new JCheckBox("Special Characters (!@#$...)", true);
        
        uppercaseCheckbox.addActionListener(e -> generatePassword());
        lowercaseCheckbox.addActionListener(e -> generatePassword());
        numbersCheckbox.addActionListener(e -> generatePassword());
        specialCharsCheckbox.addActionListener(e -> generatePassword());
        
        charOptionsPanel.add(uppercaseCheckbox);
        charOptionsPanel.add(lowercaseCheckbox);
        charOptionsPanel.add(numbersCheckbox);
        charOptionsPanel.add(specialCharsCheckbox);
        
        // Generate button
        JPanel buttonPanel = new JPanel();
        JButton generateButton = new JButton("Generate Password");
        generateButton.addActionListener(e -> generatePassword());
        buttonPanel.add(generateButton);
        
        // Add all panels to options panel
        optionsPanel.add(sliderPanel);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionsPanel.add(charOptionsPanel);
        optionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        optionsPanel.add(buttonPanel);
        
        // Add panels to main panel
        mainPanel.add(passwordPanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
    
        add(mainPanel);

        generatePassword();
    }
    
    private void generatePassword() {
        StringBuilder charPool = new StringBuilder();
        
        if (uppercaseCheckbox.isSelected()) charPool.append(UPPERCASE_CHARS);
        if (lowercaseCheckbox.isSelected()) charPool.append(LOWERCASE_CHARS);
        if (numbersCheckbox.isSelected()) charPool.append(NUMBER_CHARS);
        if (specialCharsCheckbox.isSelected()) charPool.append(SPECIAL_CHARS);
        
        if (charPool.length() == 0) {
            passwordField.setText("Please select at least one character type");
            return;
        }
        
        int length = lengthSlider.getValue();
        StringBuilder password = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(charPool.length());
            password.append(charPool.charAt(randomIndex));
        }
        
        passwordField.setText(password.toString());
    }
    
    private void copyToClipboard() {
        String password = passwordField.getText();
        if (password.isEmpty() || password.startsWith("Please select")) {
            return;
        }
        
        StringSelection stringSelection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        
        JOptionPane.showMessageDialog(this, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PasswordGenerator().setVisible(true);
        });
    }
}