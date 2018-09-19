import java.util.Random;

public class questions 
{
	public static String[][] questions = {{"Pizza?"}, {"Cake?"}, {"Water?"}};
	public static String ReturnNextQuestion(){
		Random random = new Random();
		//System.out.println(random.nextInt(questions.length));
		return questions[random.nextInt(questions.length)][0];
		
	}
}
