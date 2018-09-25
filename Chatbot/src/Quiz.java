import java.util.ArrayList;
import java.util.Random;

public class Quiz
{
    private static int m_number = -1;
    private Pair[] m_questions;
    private static Pair[] quiz = new Pair[]
            {
                    new Pair("How many bits in byte?", "8"),
                    new Pair("How many days in a leap year?", "366"),
                    new Pair("What is the color of the traffic light?", "Red"),
                    new Pair("What language was I written in?", "Java")
            };

    Quiz(String fileName)
    {
        QuizReader qr = new QuizReader(fileName);
        Pair[] lines = qr.readAndPrintFromFile();
    }


    public static Pair returnNext()
    {
        Random random = new Random();
        return quiz[random.nextInt(quiz.length)];
    }

    public static Pair returnQuestionsInOrder()
    {
        m_number++;
        if (m_number < quiz.length)
            return quiz[m_number];
        else
            return returnNext();
        //return new Pair("I have no more questions.", null);
        //throw new IndexOutOfBoundsException();
    }
}
