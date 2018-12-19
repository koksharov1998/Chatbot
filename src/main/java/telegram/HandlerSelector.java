package telegram;

public class HandlerSelector {

  private GeneralHandler generalHandler = new GeneralHandler();
  private WikiHandler wikiHandler = new WikiHandler();
  private QuizHandler quizHandler = new QuizHandler();

  public Handler selectHandler(UserStatus status) {
    switch (status) {
      case Wiki:
        return wikiHandler;
      case QuizInGame:
        return quizHandler;
      case QuizChoosing:
        return quizHandler;
      case QuizStart:
        return quizHandler;
      default:
        return generalHandler;
    }
  }
}
