package server;

public class Quiz {

  private int currentQuestionID = -1;
  private Pair[] quiz;

  public Quiz(QuizReader qr) {
    try {
      quiz = qr.readFromStream();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public String getCurrentQuestion() {
    return quiz[currentQuestionID].getFirst();
  }

  public boolean moveNextQuestion() {
    currentQuestionID++;
    if (currentQuestionID < quiz.length) {
      return true;
    } else {
      return false;
    }
  }

  public boolean checkAnswer(User user, String answer) {
    if (answer.equals(quiz[currentQuestionID].getSecond().toLowerCase())) {
      user.upScore();
      return true;
    } else {
      return false;
    }
  }
}
