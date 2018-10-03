import java.util.Random;

public class Quiz {

	private int currentQuestionID = -1;
	private Pair[] quiz;

	public Quiz() {
	}

	public Quiz(String fileName) {
		QuizReader qr = new QuizReader(fileName);
		quiz = qr.readAndPrintFromFile();
	}

	public String getCurrentQuestion() {
		return quiz[currentQuestionID].getFirst();
	}

	private boolean loadRandomQuestion() {
		Random random = new Random();
		currentQuestionID = random.nextInt(quiz.length);
		return true;
	}

	public boolean loadQuestionInOrder() {
		currentQuestionID++;
		if (currentQuestionID < quiz.length) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkAnswer(User user, String answer) {
		if (answer.equals(quiz[currentQuestionID].getSecond().toLowerCase())) {
			user.upScore();
			user.pushQuestion(currentQuestionID);
			System.out.println("It's right!");
			return true;
		} else {
			System.out.println("It's wrong!");
			return false;
		}
	}
}
