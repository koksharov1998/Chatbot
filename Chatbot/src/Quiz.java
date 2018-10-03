public class Quiz {

  private int currentQuestionID = -1;
  private Pair[] quiz;

  public Quiz(String fileName) {
    QuizReader qr = new QuizReader(fileName);
    quiz = qr.readFromFile();
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
      user.pushQuestion(currentQuestionID);
      System.out.println("It's right!");
      return true;
    } else {
      System.out.println("It's wrong!");
      return false;
    }
  }
}
