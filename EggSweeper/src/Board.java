import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// The Model

public class Board{
	
	// the GameBoard is an empty 2D array of GridSpace pointers
	private GridSpace[][] board = new GridSpace[boardSize][boardSize];
	
	// the Difficulty affects distribution of EGGs and TRASH
	public static enum Difficulty {EASY, MEDIUM, HARD};
	
	// Board data
	private int timer;
	private int clicks;
	private Difficulty difficulty;
	
	// size of the board
	public static int boardSize = 20;
	
	// randConst is used in setting Items in GridSpaces
	private static final int randConst = 100;
	// ratio for determining distribution of EMPTY spaces in all Difficulties
	private double emptyRatio = randConst * (3./10.);
	// ratios for determining distribution of EGGs and TRASH based on Difficulty
	private double easyEggRatio = randConst * (9./10.);
	private double mediumEggRatio = randConst * (8.5/10.);
	private double hardEggRatio = randConst * (8./10.);
	private List<String> questionsAsked = new ArrayList<String>();
	private String answer;
	
	// sets EMPTY, TRASH and EGG spaces in the board
	Board(Difficulty newDifficulty) {
		System.out.println(" ");
		System.out.println("Difficulty selected: " + newDifficulty);
		System.out.println("Score: 0");
		difficulty = newDifficulty;
		//timer is currently unused
		timer = 100;
		// max number of clicks
		clicks = 10;
		
		Random rand = new Random();
		// cycle through each GridSpace pointer
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				// generate random integer to use for determining contents of GridSpace
				int randomInt = rand.nextInt(randConst);
				GridSpace.Item spaceItem = GridSpace.Item.EMPTY;
				switch (difficulty) {
					// on EASY Difficulty, 30% EMPTY, 60% EGGs, 10% TRASH
					case EASY:
						if (randomInt < (emptyRatio)) {
							spaceItem = GridSpace.Item.EMPTY;
						}
						else if (randomInt < (easyEggRatio)) {
							spaceItem = GridSpace.Item.EGG;
						}
						else {
							spaceItem = GridSpace.Item.TRASH;
						}
						break;
					case MEDIUM:
						if (randomInt < (emptyRatio)) {
							spaceItem = GridSpace.Item.EMPTY;
						}
						else if (randomInt < (mediumEggRatio)) {
							spaceItem = GridSpace.Item.EGG;
						}
						else {
							spaceItem = GridSpace.Item.TRASH;
						}
						break;
					case HARD:
						if (randomInt < (emptyRatio)) {
							spaceItem = GridSpace.Item.EMPTY;
						}
						else if (randomInt < (hardEggRatio)) {
							spaceItem = GridSpace.Item.EGG;
						}
						else {
							spaceItem = GridSpace.Item.TRASH;
						}
						break;
				}
				board[i][j] = new GridSpace(spaceItem);
			}
		}
	}
	
	// get GridSpace at index (x, y)
	public GridSpace getSpace(int xIndex, int yIndex) {
		return board[xIndex][yIndex];
	}
	
	// set GridSpace at index (x, y)
	public void setSpace(int xIndex, int yIndex, GridSpace space) {
		board[xIndex][yIndex] = space;
	}
	
	public int getClicks() {
		return clicks;
	}
	
	public void setClicks(int newClicks) {
		clicks = newClicks;
	}
	
	public void decClicks() {
		clicks--;
	}
	
	public int getTime() {
		return timer;
	}
	
	public void setTime(int newTime) {
		timer = newTime;
	}
	
	public String getPowerupQuestion() throws FileNotFoundException {
		StringBuilder question = new StringBuilder(); // question string builder
		StringBuilder qNum = new StringBuilder(); // string builder for the question number
		qNum.append("Q"); //marker for questions
		File questionsFile = new File("questions/powerQuestions.txt");
		int questionNum = generateQuestionNum(5);
		qNum.append(questionNum);
		qNum.append(":"); // another marker for questions
		try {
			Scanner fn = new Scanner (questionsFile);
			while(fn.hasNextLine()) {
				String line = fn.nextLine();
				if(line.contains(qNum.toString())){ // if the line contains the Q(number):, then add it to the question string builder 
					question.append(line);
					System.out.println("Selected Question:" + question.toString());
					break;
				}
			}
			fn.close();
			this.answer = this.getPowerupAnswer(questionNum);
			
		} catch (FileNotFoundException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		return question.toString();
	}
	
	private int generateQuestionNum(int range) {
		return (int)(Math.random() * ((range - 1) + 1)) + 1;
	}
	
	private String getPowerupAnswer(int questionNum) throws FileNotFoundException{
		StringBuilder answer = new StringBuilder(); 
		StringBuilder aNum = new StringBuilder(); 
		aNum.append("A"); //marker for questions
		File questionsFile = new File("questions/answers.txt");
		aNum.append(questionNum);
		aNum.append(":");
		try {
			Scanner fn = new Scanner (questionsFile);
			while(fn.hasNextLine()) {
				String line = fn.nextLine();
				if(line.contains(aNum.toString())){ 
					answer.append(line);
					break;
				}
			}
			fn.close();
		} catch (FileNotFoundException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		return answer.toString();
	}
}
	