package typeflow;

import javax.swing.*;
import java.awt.*;

public class ResultPanel extends JPanel {
    private final JLabel wpm = new JLabel("0");
    private final JLabel accuracy = new JLabel("100%");
    private final JLabel errors = new JLabel("0");

    public ResultPanel(Runnable restartAction) {
        setLayout(new BorderLayout(10, 20));
        setBackground(new Color(30, 32, 38));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel heading = new JLabel("Test completed!", SwingConstants.CENTER);
        heading.setForeground(new Color(235, 190, 95));
        heading.setFont(new Font("SansSerif", Font.BOLD, 30));
        add(heading, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 3, 18, 0));
        stats.setOpaque(false);
        addStat(stats, "WPM", wpm);
        addStat(stats, "Accuracy", accuracy);
        addStat(stats, "Errors", errors);
        add(stats, BorderLayout.CENTER);

        JButton restart = new JButton("Try Again");
        restart.setFont(new Font("SansSerif", Font.BOLD, 16));
        restart.setFocusPainted(false);
        restart.addActionListener(e -> restartAction.run());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(restart);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addStat(JPanel panel, String title, JLabel value) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 10));
        card.setBackground(new Color(43, 46, 54));
        card.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(new Color(165, 170, 180));
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        value.setForeground(new Color(235, 190, 95));
        value.setFont(new Font("SansSerif", Font.BOLD, 30));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(label);
        card.add(value);
        panel.add(card);
    }

    public void setResults(double finalWpm, double finalAccuracy, int finalErrors) {
        wpm.setText(String.format("%.1f", finalWpm));
        accuracy.setText(String.format("%.1f%%", finalAccuracy));
        errors.setText(String.valueOf(finalErrors));
    }
}
