import java.util.HashSet;
import java.util.Set;

public class Quiz {

  private int currentQuestionID = 0;
  private Pair[] quiz;
  private Set<Integer> passedQuestions = new HashSet<Integer>();

  public void pushQuestion(int ind) {
    passedQuestions.add(ind);
  }

  public boolean contains(int ind) {
    return passedQuestions.contains(ind);
  }

  public Quiz(String fileName) {
    QuizReader qr = new QuizReader(fileName);
    quiz = qr.readFromFile();
  }

  public String getCurrentQuestion() {
    return quiz[currentQuestionID].getFirst();
  }

  public boolean moveNextQuestion() {
    if (currentQuestionID < quiz.length) {
      while (contains(currentQuestionID)) {
        currentQuestionID++;
      }
      return true;
    } else {
      currentQuestionID = 0;
      return false;
    }
  }

  public boolean checkAnswer(User user, String answer) {
    if (answer.equals(quiz[currentQuestionID].getSecond().toLowerCase())) {
      user.upScore();
      pushQuestion(currentQuestionID);
      System.out.println("It's right!");
      return true;
    } else {
      System.out.println("It's wrong!");
      return false;
    }
  }
}
