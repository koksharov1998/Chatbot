import java.util.Random;

public class Quiz {

  private int number = -1;
  private Pair[] questions;
  private Pair[] quiz; /* = new Pair[]
            {
                    new Pair("How many bits in byte?", "8"),
                    new Pair("How many days in a leap year?", "366"),
                    new Pair("What is the color of the traffic light?", "Red"),
                    new Pair("What language was I written in?", "Java")
            };*/

  public Quiz(String fileName) {
    QuizReader qr = new QuizReader(fileName);
    quiz = qr.readAndPrintFromFile();
  }


  public Pair returnNext() {
    Random random = new Random();
    return quiz[random.nextInt(quiz.length)];
  }

  public Pair returnQuestionsInOrder() {
    number++;
    if (number < quiz.length) {
      return quiz[number];
    } else {
      return returnNext();
    }
    //return new Pair("I have no more questions.", null);
    //throw new IndexOutOfBoundsException();
  }
}
