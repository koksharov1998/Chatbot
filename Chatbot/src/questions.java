import java.util.Random;

public class questions 
{
    public static pair[] quiz = new pair[]
            {
                    new pair("How many bits in byte?", "8"),
                    new pair("How many days in a leap year?", "366"),
                    new pair("What is the color of the traffic light?", "Red"),
                    new pair("What language was I written in?", "Java")
            };

	public static String[][] questions = {{"Pizza?"}, {"Cake?"}, {"Water?"}};

    public static String ReturnNext()
    {
        Random random = new Random();
        return quiz[random.nextInt(quiz.length)].m_first;
    }

	public static String ReturnNextQuestion()
    {
		Random random = new Random();
		return questions[random.nextInt(questions.length)][0];
	}
}
