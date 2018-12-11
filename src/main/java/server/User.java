package server;

public class User {

  private int ID;
  private String name;
  private int score = 0;
  private int status = 0;

  public User(String name, int ID) {
    this.name = name;
    this.ID = ID;
  }

  public String getName() {
    return name;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
  public void upScore() {
    score++;
  }
}
