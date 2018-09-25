import java.util.Scanner;

public class Chatbot
{
		public static void main(String[] args)
		{
			Scanner scanner = new Scanner(System.in);
			Quiz quiz = new Quiz("quiz.txt");

			//QuizReader qr = new QuizReader("quiz.txt");
			System.out.println("Hello!");
			while (true)
			{
				Pair pair = Quiz.returnQuestionsInOrder();
				System.out.println(pair.m_first);
				String answer = scanner.nextLine().toLowerCase();
				if (answer.equals("quit"))
				{
					System.out.println("Bye!");
					break;
				}
				if (answer.equals(pair.m_second.toLowerCase()))
					System.out.println("It's right!");
				else
					System.out.println("It's wrong!");
			}
			scanner.close();
		}
}
