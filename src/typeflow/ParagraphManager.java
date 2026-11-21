package typeflow;

import java.util.*;

public class ParagraphManager {
    private static final String[] EASY = {
        "The sun rises in the east and brings light to the world. A fresh day is a new chance to learn and grow.",
        "Small steps every day can lead to big changes. Stay focused, keep learning, and enjoy the journey.",
        "A good book can take you to a new place. Reading helps us learn words and understand different ideas."
    };

    private static final String[] MEDIUM = {
        "Technology changes the way people communicate, work, and solve everyday problems. Learning new skills helps us adapt to these changes.",
        "Consistency is more important than speed when building a useful habit. Practice regularly, review your progress, and improve one step at a time.",
        "A successful team listens to different opinions, shares responsibilities, and works together toward a clear goal."
    };

    private static final String[] HARD = {
        "Reliable software requires thoughtful design, readable code, consistent testing, and careful handling of unexpected input.",
        "Modern applications combine responsive interfaces, efficient algorithms, secure data handling, and maintainable architecture.",
        "Debugging is a systematic process: reproduce the issue, inspect the evidence, isolate the cause, and verify the solution."
    };

    private final Random random = new Random();

    public String getParagraph(String difficulty) {
        String[] choices;
        switch (difficulty) {
            case "Hard": choices = HARD; break;
            case "Medium": choices = MEDIUM; break;
            default: choices = EASY;
        }
        return choices[random.nextInt(choices.length)];
    }
}
