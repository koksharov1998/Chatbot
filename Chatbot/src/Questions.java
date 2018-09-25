import java.util.Random;

public class Questions
{

	public static String[][] questions = {{"Pizza?"}, {"Cake?"}, {"Water?"}};

	public static String returnNextQuestion()
    {
		Random random = new Random();
		return questions[random.nextInt(questions.length)][0];
	}
}
