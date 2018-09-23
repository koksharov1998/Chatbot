import java.util.Scanner;

public class chatbot
{
		public static void main(String[] args)
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Hello!");
			while (true)
			{
				//System.out.println(questions.ReturnNextQuestion());
				pair pair = questions.ReturnNext();
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
