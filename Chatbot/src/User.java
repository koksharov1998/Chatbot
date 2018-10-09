import java.util.HashSet;
import java.util.Set;

public class User {

  private int score = 0;
  //private Set<Integer> passedQuestions = new HashSet<Integer>();

  //public boolean contains(int ind) {
  //  return passedQuestions.contains(ind);
  //}

  //public void pushQuestion(int ind) {
   // passedQuestions.add(ind);
  //}

  public int getScore() {
    return score;
  }

  public void upScore() {
    score++;
  }
}
