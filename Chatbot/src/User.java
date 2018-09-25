import java.util.HashSet;

public class User {

    // количество правильных ответов пользователя
    private int m_score = 0;

    // ?Списко вопросов, заданных пользователю?
    private HashSet<Integer> m_questions;

    // ?Метод, говорящий о том был ли у пользователя определённый вопрос?
    public boolean contains(int ind) {
        return m_questions.contains(ind);
    }

    public void pushQuestion(int ind) {
        m_questions.add(ind);
    }

    public int getScore() {
        return m_score;
    }

    public void upScore() {
        m_score++;
    }
}
