package twilightthreadcrochet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TwilightThreadSplash extends JFrame implements ActionListener {

    JLabel backgroundLabel;
    JButton nextButton;

    public TwilightThreadSplash() {
        setTitle("Twilight Thread");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // background
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("/icons/TwilightThreadSplash.jpg"));
        Image img = imgIcon.getImage();

        // Set frame size 
        int width = (int) (imgIcon.getIconWidth() * 0.8);
        int height = (int) (imgIcon.getIconHeight() * 0.8);
        setSize(width, height);
        setLocationRelativeTo(null);

        
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        backgroundLabel = new JLabel(new ImageIcon(scaledImg));
        backgroundLabel.setBounds(0, 0, width, height);
        setContentPane(backgroundLabel);
        setLayout(null);

        
        JLabel titleLabel = new JLabel("Twilight Thread", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, height / 27, width, 80);
        backgroundLabel.add(titleLabel);

        // Next button
        nextButton = new JButton("Next");
        nextButton.setBounds(width - 200, height - 100, 150, 50);
        nextButton.setFont(new Font("Arial", Font.BOLD, 28));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(Color.BLACK);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(this);
        backgroundLabel.add(nextButton);

        // Blinking effect
        new Thread(() -> {
            while (true) {
                titleLabel.setVisible(false);
                try { Thread.sleep(500); } catch (Exception e) {}
                titleLabel.setVisible(true);
                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }).start();

        setVisible(true);
    }


    public void actionPerformed(ActionEvent e) {
        new LoginFrame(); // open login frame
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TwilightThreadSplash::new);
    }
}
