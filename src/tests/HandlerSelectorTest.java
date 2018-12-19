import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import telegram.User;
import telegram.GeneralHandler;
import telegram.Handler;
import telegram.HandlerSelector;
import telegram.QuizHandler;
import telegram.UserStatus;
import telegram.WikiHandler;

public class HandlerSelectorTest {

  private HandlerSelector handlerSelector = new HandlerSelector();

  @Test
  void shouldReturnGeneralHandler() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.Default);
    Handler handler = handlerSelector.selectHandler(user.getStatus());
    assertTrue(handler instanceof GeneralHandler);
  }

  @Test
  void shouldReturnWikiHandler() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.Wiki);
    Handler handler = handlerSelector.selectHandler(user.getStatus());
    assertTrue(handler instanceof WikiHandler);
  }

  @Test
  void shouldReturnQuizHandlerFrom2() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizInGame);
    Handler handler = handlerSelector.selectHandler(user.getStatus());
    assertTrue(handler instanceof QuizHandler);
  }

  @Test
  void shouldReturnQuizHandlerFrom3() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizChoosing);
    Handler handler = handlerSelector.selectHandler(user.getStatus());
    assertTrue(handler instanceof QuizHandler);
  }

  @Test
  void shouldReturnQuizHandlerFrom4() {
    User user = new User("User1", 1);
    user.setStatus(UserStatus.QuizStart);
    Handler handler = handlerSelector.selectHandler(user.getStatus());
    assertTrue(handler instanceof QuizHandler);
  }
}
