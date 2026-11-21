package typeflow;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class TypingTestFrame extends JFrame {
    private final ParagraphManager paragraphManager = new ParagraphManager();
    private final JComboBox<String> durationBox = new JComboBox<>(new String[]{"15 seconds", "30 seconds", "60 seconds"});
    private final JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
    private final JLabel timerLabel = new JLabel("Time: 60", SwingConstants.CENTER);
    private final JLabel wpmLabel = new JLabel("WPM: 0", SwingConstants.CENTER);
    private final JLabel accuracyLabel = new JLabel("Accuracy: 100%", SwingConstants.CENTER);
    private final JLabel errorsLabel = new JLabel("Errors: 0", SwingConstants.CENTER);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final TypingPanel typingPanel = new TypingPanel();
    private final ResultPanel resultPanel = new ResultPanel(this::startNewTest);

    private javax.swing.Timer timer;
    private String targetText;
    private TypingEngine engine;
    private int totalSeconds = 60;
    private int remainingSeconds = 60;
    private boolean started = false;

    public TypingTestFrame() {
        super("TypeFlow | Typing Speed Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setMinimumSize(new Dimension(760, 540));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(30, 32, 38));
        setLayout(new BorderLayout(0, 12));

        add(buildHeader(), BorderLayout.NORTH);
        cards.add(typingPanel, "typing");
        cards.add(resultPanel, "result");
        add(cards, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        typingPanel.getInput().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleTyping(); }
            public void removeUpdate(DocumentEvent e) { handleTyping(); }
            public void changedUpdate(DocumentEvent e) { handleTyping(); }
        });

        startNewTest();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout(10, 12));
        header.setBackground(new Color(30, 32, 38));
        header.setBorder(BorderFactory.createEmptyBorder(20, 28, 0, 28));

        JLabel title = new JLabel("typeflow");
        title.setForeground(new Color(235, 190, 95));
        title.setFont(new Font("SansSerif", Font.BOLD, 30));

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        controls.setOpaque(false);
        durationBox.setSelectedItem("60 seconds");
        controls.add(new JLabel("Duration:"));
        controls.add(durationBox);
        controls.add(new JLabel("Level:"));
        controls.add(difficultyBox);
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> startNewTest());
        controls.add(restart);

        styleHeaderLabels(controls);
        header.add(title, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);
        return header;
    }

    private void styleHeaderLabels(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JLabel) {
                component.setForeground(new Color(205, 208, 215));
            }
        }
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 4, 10, 0));
        footer.setBackground(new Color(30, 32, 38));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 28, 20, 28));
        for (JLabel label : new JLabel[]{timerLabel, wpmLabel, accuracyLabel, errorsLabel}) {
            label.setForeground(new Color(235, 190, 95));
            label.setFont(new Font("SansSerif", Font.BOLD, 16));
            label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(65, 69, 78)),
                    BorderFactory.createEmptyBorder(10, 5, 10, 5)));
            footer.add(label);
        }
        return footer;
    }

    private void startNewTest() {
        if (timer != null) timer.stop();
        started = false;
        totalSeconds = Integer.parseInt(((String) durationBox.getSelectedItem()).split(" ")[0]);
        remainingSeconds = totalSeconds;
        targetText = paragraphManager.getParagraph((String) difficultyBox.getSelectedItem());
        engine = new TypingEngine(targetText);
        typingPanel.setTarget(targetText);
        timerLabel.setText("Time: " + remainingSeconds);
        wpmLabel.setText("WPM: 0");
        accuracyLabel.setText("Accuracy: 100%");
        errorsLabel.setText("Errors: 0");
        cardLayout.show(cards, "typing");
    }

    private void handleTyping() {
        if (!"typing".equals(getVisibleCardName())) return;
        String typed = typingPanel.getInput().getText();
        if (!started && !typed.isEmpty()) {
            started = true;
            startTimer();
        }
        engine.update(typed);
        typingPanel.render(targetText, typed);
        updateLiveStats();

        if (typed.length() >= targetText.length()) {
            finishTest();
        }
    }

    private String getVisibleCardName() {
        // The input is enabled only while the typing card is active.
        return typingPanel.getInput().isEnabled() ? "typing" : "result";
    }

    private void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            remainingSeconds--;
            timerLabel.setText("Time: " + remainingSeconds);
            updateLiveStats();
            if (remainingSeconds <= 0) finishTest();
        });
        timer.start();
    }

    private void updateLiveStats() {
        int elapsed = totalSeconds - remainingSeconds;
        double wpm = engine.getWpm(elapsed);
        wpmLabel.setText(String.format("WPM: %.1f", wpm));
        accuracyLabel.setText(String.format("Accuracy: %.1f%%", engine.getAccuracy()));
        errorsLabel.setText("Errors: " + engine.getIncorrectCharacters());
    }

    private void finishTest() {
        if (timer != null) timer.stop();
        typingPanel.stopInput();
        int elapsed = Math.max(1, totalSeconds - remainingSeconds);
        double finalWpm = engine.getWpm(elapsed);
        resultPanel.setResults(finalWpm, engine.getAccuracy(), engine.getIncorrectCharacters());
        cardLayout.show(cards, "result");
    }
}
