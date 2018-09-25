import java.util.Scanner;

public class Chatbot
{
		public static void main(String[] args)
		{
			User user = new User();
			Scanner scanner = new Scanner(System.in);
			System.out.println("Hello!");
			while (true)
			{
				//Pair pair = Questions.returnNext();
				Pair pair = Quiz.returnQuestionsInOrder();
				System.out.println(pair.m_first);
				String answer = scanner.nextLine().toLowerCase();
				if (answer.equals("quit"))
				{
					System.out.println("Bye!");
					break;
				}
				if (answer.equals("result"))
				{
					System.out.println(user.getMark());
				}
				if (answer.equals(pair.m_second.toLowerCase())) {
					user.upMark();
					System.out.println("It's right!");
				}
				else
					System.out.println("It's wrong!");
			}
			scanner.close();
		}
}
