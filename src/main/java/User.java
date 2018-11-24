public class User {

  private int ID;
  private String name;
  private int score = 0;

  public User(String name, int ID) {
    this.name = name;
    this.ID = ID;
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
