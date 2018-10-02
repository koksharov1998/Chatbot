import java.util.Random;

public class Quiz {

  private int currentQuestionID = -1;
  private String currentQuestion;

  public String getCurrentQuestion() {
    return currentQuestion;
  }

  private String currentAnswer;
  private Pair[] quiz;

  public Quiz() {
    quiz = new Pair[]
        {
            new Pair("How many bits in byte?", "8")//,
            //new Pair("How many days in a leap year?", "366"),
            //new Pair("What is the color of the traffic light?", "Red"),
            //new Pair("What language was I written in?", "Java")
        };
  }

  public Quiz(String fileName) {
    QuizReader qr = new QuizReader(fileName);
    quiz = qr.readAndPrintFromFile();
  }


  private boolean loadRandomQuestion() {
    Random random = new Random();
    currentQuestionID = random.nextInt(quiz.length);
    currentQuestion = quiz[random.nextInt(quiz.length)].getFirst();
    currentAnswer = quiz[random.nextInt(quiz.length)].getSecond();
    return true;
  }

  public boolean loadQuestionInOrder() {
    currentQuestionID++;
    if (currentQuestionID < quiz.length) {
      currentQuestion = quiz[currentQuestionID].getFirst();
      currentAnswer = quiz[currentQuestionID].getSecond();
      return true;
    } else {
      //loadRandomQuestion();
      return false;
    }
  }

  public void checkAnswer(User user, String answer) {
    if (answer.equals(currentAnswer.toLowerCase())) {
      user.upScore();
      user.pushQuestion(currentQuestionID);
      System.out.println("It's right!");
    } else {
      System.out.println("It's wrong!");
    }
  }
}
