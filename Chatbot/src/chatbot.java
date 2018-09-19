import java.util.Scanner;

public class chatbot
{
		public static void main(String[] args)
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Hello!");
			System.out.println(questions.ReturnNextQuestion());
			String answer = scanner.next().toLowerCase();
		}
}
