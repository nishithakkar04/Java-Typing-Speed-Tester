package typeflow;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TypingPanel extends JPanel {
    private final JTextPane display = new JTextPane();
    private final JTextArea input = new JTextArea(3, 40);
    private final StyledDocument document;
    private final Style correctStyle;
    private final Style incorrectStyle;
    private final Style pendingStyle;

    public TypingPanel() {
        setLayout(new BorderLayout(0, 14));
        setBackground(new Color(30, 32, 38));
        setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        display.setEditable(false);
        display.setFocusable(false);
        display.setBackground(new Color(30, 32, 38));
        display.setFont(new Font("Consolas", Font.PLAIN, 23));
        display.setBorder(BorderFactory.createEmptyBorder(12, 4, 12, 4));
        document = display.getStyledDocument();

        correctStyle = display.addStyle("correct", null);
        StyleConstants.setForeground(correctStyle, new Color(145, 205, 145));
        incorrectStyle = display.addStyle("incorrect", null);
        StyleConstants.setForeground(incorrectStyle, new Color(255, 105, 105));
        StyleConstants.setUnderline(incorrectStyle, true);
        pendingStyle = display.addStyle("pending", null);
        StyleConstants.setForeground(pendingStyle, new Color(155, 160, 170));

        input.setFont(new Font("Consolas", Font.PLAIN, 18));
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setBackground(new Color(43, 46, 54));
        input.setForeground(new Color(235, 235, 235));
        input.setCaretColor(Color.WHITE);
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(75, 80, 92)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel hint = new JLabel("Start typing in the box below. Your progress is highlighted above.");
        hint.setForeground(new Color(170, 175, 185));
        hint.setFont(new Font("SansSerif", Font.PLAIN, 13));

        add(display, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        bottom.setOpaque(false);
        bottom.add(hint, BorderLayout.NORTH);
        bottom.add(new JScrollPane(input), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setTarget(String target) {
        input.setText("");
        input.setEnabled(true);
        render(target, "");
        input.requestFocusInWindow();
    }

    public void render(String target, String typed) {
        try {
            document.remove(0, document.getLength());
            document.insertString(0, target, pendingStyle);
            for (int i = 0; i < Math.min(typed.length(), target.length()); i++) {
                boolean correct = typed.charAt(i) == target.charAt(i);
                document.setCharacterAttributes(i, 1, correct ? correctStyle : incorrectStyle, true);
            }
            if (typed.length() > target.length()) {
                document.insertString(document.getLength(), "  +" + (typed.length() - target.length()) + " extra", incorrectStyle);
            }
        } catch (BadLocationException ignored) {}
    }

    public JTextArea getInput() { return input; }

    public void stopInput() { input.setEnabled(false); }
}
