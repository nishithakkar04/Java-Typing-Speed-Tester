package typeflow;

public class TypingEngine {
    private final String target;
    private int correctCharacters;
    private int incorrectCharacters;
    private int totalTyped;

    public TypingEngine(String target) {
        this.target = target;
    }

    public void update(String typed) {
        correctCharacters = 0;
        incorrectCharacters = 0;
        totalTyped = typed.length();

        for (int i = 0; i < typed.length(); i++) {
            if (i < target.length() && typed.charAt(i) == target.charAt(i)) {
                correctCharacters++;
            } else {
                incorrectCharacters++;
            }
        }
    }

    public int getCorrectCharacters() { return correctCharacters; }
    public int getIncorrectCharacters() { return incorrectCharacters; }
    public int getTotalTyped() { return totalTyped; }

    public double getAccuracy() {
        if (totalTyped == 0) return 100.0;
        return (correctCharacters * 100.0) / totalTyped;
    }

    public double getWpm(int elapsedSeconds) {
        if (elapsedSeconds <= 0) return 0;
        return (correctCharacters / 5.0) / (elapsedSeconds / 60.0);
    }

    public String getTarget() { return target; }
}
