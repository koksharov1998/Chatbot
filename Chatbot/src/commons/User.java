package commons;

public class User {

  private String name;
  private int score = 0;

  public User(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public void upScore() {
    score++;
  }
}
