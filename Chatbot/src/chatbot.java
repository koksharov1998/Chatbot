import java.util.Scanner;

public class chatbot
{
		public static void main(String[] args)
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Hello!");
			while (true)
			{
				System.out.println(questions.ReturnNextQuestion());
				String answer = scanner.nextLine().toLowerCase();
				if (answer.equals("quit"))
				{
					System.out.println("Bye!");
					break;
				}
			}
			
			
			scanner.close();
		}
}
