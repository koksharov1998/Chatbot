import java.util.Map;
import java.util.Random;

public class questions 
{
	public static String[][] questions = {{"Pizza?"}, {"Cake?"}, {"Water?"}};
	
	public static String ReturnNextQuestion(){
		Random random = new Random();
		return questions[random.nextInt(questions.length)][0];
		
	}
}
