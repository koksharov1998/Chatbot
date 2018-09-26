import java.util.Random;

public class Quiz {

  private int number = -1;
  private int currentQuestionID;
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
    number++;
    if (number < quiz.length) {
      currentQuestionID = number;
      currentQuestion = quiz[number].getFirst();
      currentAnswer = quiz[number].getSecond();
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
