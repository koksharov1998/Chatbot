import java.util.Random;

public class Questions
{
    private static int number = -1;
    public static Pair[] quiz = new Pair[]
            {
                    new Pair("How many bits in byte?", "8"),
                    new Pair("How many days in a leap year?", "366"),
                    new Pair("What is the color of the traffic light?", "Red"),
                    new Pair("What language was I written in?", "Java")
            };

    public static Pair returnNext()
    {
        Random random = new Random();
        return quiz[random.nextInt(quiz.length)];
    }

    public static Pair returnQuestionsInOrder()
    {
        number++;
        if (number < quiz.length)
            return quiz[number];
        else
            return returnNext();
            //return new Pair("I have no more questions.", null);
            //throw new IndexOutOfBoundsException();
    }

	public static String[][] questions = {{"Pizza?"}, {"Cake?"}, {"Water?"}};

	public static String returnNextQuestion()
    {
		Random random = new Random();
		return questions[random.nextInt(questions.length)][0];
	}
}
