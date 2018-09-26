import java.util.Set;

public class User {

    private int score = 0;
    private Set<Integer> questions;

    public boolean contains(int ind) {
        return questions.contains(ind);
    }

    public void pushQuestion(int ind) {
        questions.add(ind);
    }

    public int getScore() {
        return score;
    }

    public void upScore() {
        score++;
    }
}
