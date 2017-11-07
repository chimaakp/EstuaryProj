import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// The Controller

public class NewController{
	
	// The Model
	Player player;
	Board gameBoard;
	
	// The View
	JFrame frame;
	Animation animation;
	
	private GridBagConstraints constraintFactory() {
		GridBagConstraints constraints = new GridBagConstraints();
		
		//The constraints are defined every time you add an element
		//Grid x and grid y are the positions the component starts at
		constraints.gridx = 0;
		constraints.gridy = 0;
		//Grid width and grid height define how many spaces the component takes up
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		//Something about a hint on how the components can fit into place
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		//More settings
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		return constraints;
	}
	// displays start button
	public void startScreen() {
		//Declare a new JPanel
		JPanel startPanel = new JPanel();
		//Set its layout manager to GridBag
		startPanel.setLayout(new GridBagLayout());
		
		//The constraints describe each new component's location
		GridBagConstraints constraints = constraintFactory();
		//The component will render in the 3rd column of the first row
		constraints.gridx = 3;
		
		JLabel title = new JLabel("EGG SWEEPER");
		title.setFont(new Font("Arial", Font.PLAIN, 80));
		
		//Add components to the start panel instead of the frame's contentPane directly
		startPanel.add(title, constraints);
		
		JButton startButton = new JButton("Start Game");
		startButton.setFont(new Font("Arial", Font.PLAIN, 30));
		startButton.setVisible(true);
		
		//This component will be in the same column, just 3 rows below
		constraints.gridy = 3;
		
		startPanel.add(startButton,constraints);
		
		startButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        		
	        		frame.getContentPane().remove(startPanel);
	        		frame.validate();
	        		frame.getContentPane().repaint();
	        		// when clicked calls method to generate difficulty selection screen
	        		pickDifficulty();
	                
	        }
	    });
		//When built add the component to the frame
		frame.add(startPanel);
		frame.validate();
	}
	
	// displays easy, medium and hard button
	public void pickDifficulty() {
		JPanel difficultyPanel = new JPanel();
		difficultyPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = constraintFactory();
		constraints.gridx = 3;
		constraints.weightx = 50;
		constraints.weighty = 50;
		
		//The width of space between each button
		int width = 3;
		
		JButton easyButton = new JButton("Easy");
		easyButton.setFont(new Font("Arial", Font.PLAIN, 30));
		easyButton.setVisible(true);
		
		JButton mediumButton = new JButton("Medium");
		mediumButton.setFont(new Font("Arial", Font.PLAIN, 30));
		mediumButton.setVisible(true);
		
		JButton hardButton = new JButton("Hard");
		hardButton.setFont(new Font("Arial", Font.PLAIN, 30));
		hardButton.setVisible(true);
		
		//After a button is added add the width
		difficultyPanel.add(easyButton,constraints);
		constraints.gridy = width;
		difficultyPanel.add(mediumButton,constraints);
		constraints.gridy = 2*width;
		difficultyPanel.add(hardButton,constraints);
		
		frame.add(difficultyPanel);
		frame.validate();
		frame.repaint();
		easyButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        		
	        	frame.getContentPane().remove(difficultyPanel);
	        	frame.getContentPane().revalidate();
	        	frame.getContentPane().repaint();
        		// when clicked picks character and difficulty
        		gameBoard = new Board(Board.Difficulty.EASY);
        		player = new Player(Player.Bird.DUNLIN);
        		animation.migrationAnimation();
	        }
	    });
		
		mediumButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        		
	        	frame.getContentPane().remove(difficultyPanel);
	        	frame.getContentPane().revalidate();
	        	frame.getContentPane().repaint();
	        	gameBoard = new Board(Board.Difficulty.MEDIUM);
	        	player = new Player(Player.Bird.SANDPIPER); 
	        	animation.migrationAnimation();
	        }
	    });
		
		hardButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        		
	        	frame.getContentPane().remove(difficultyPanel);
	        	frame.getContentPane().revalidate();
	        	frame.getContentPane().repaint();
	        	gameBoard = new Board(Board.Difficulty.HARD);
	        	player = new Player(Player.Bird.REDKNOT);
	        	animation.migrationAnimation();
	        }
	    });
	}
	
	// add the buttons representing each GridSpace
	public void buildBoard() {
		JLabel clicks = new JLabel("Clicks remaining: " + Integer.toString(gameBoard.getClicks()));
		clicks.setFont(new Font("Arial", Font.PLAIN, 40));
		// bounds must be set for label to display
		clicks.setBounds(animation.contentPaneSize/10, 0, 500, animation.buffer);
		frame.getContentPane().add(clicks);
		
		JLabel score = new JLabel("Score: " + Integer.toString(player.getScore()));
		score.setFont(new Font("Arial", Font.PLAIN, 40));
		// bounds must be set for label to display
		score.setBounds(animation.contentPaneSize/2, 0, 500, animation.buffer);
		frame.getContentPane().add(score);
		
		JButton chestButton = new JButton();
		chestButton.setLocation(animation.contentPaneSize - (3*animation.buffer), animation.contentPaneSize - (3*animation.buffer));
		chestButton.setSize(animation.buffer * 2, animation.buffer * 2);
		chestButton.setContentAreaFilled(false);
		chestButton.setVisible(true);
		
		for (int i = 0; i < Board.boardSize; i++) {
			for (int j = 0; j < Board.boardSize; j++) {
				addButton(i, j, chestButton);
			}
		}
		
		animation.getImages().get(0).setVisible(true);
	}

	// adds a button at corresponding to an index not a location on the board
	public void addButton(int xIndex, int yIndex, JButton powerChestButton) {
		
		int xLocation = animation.buffer + xIndex*(animation.gridButtonSize);
		int yLocation = animation.buffer + yIndex*(animation.gridButtonSize);
		// create the button
		JButton gridButton = new JButton();
		gridButton.setLocation(xLocation, yLocation);
		gridButton.setSize(animation.gridButtonSize, animation.gridButtonSize);
		//gridButton.setOpaque(true);
		gridButton.setContentAreaFilled(false);
		gridButton.setBorderPainted(true);
		gridButton.setVisible(true);
		// adding the click listener
		gridButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        		
	        		// clicking a button will call the checkSpace method for that GridSpace
	                GridSpace.Item item = player.checkSpace(xIndex, yIndex, gameBoard);
	                animation.addHole(xIndex, yIndex);
	                
	                JLabel newClicks = new JLabel("Clicks remaining: " + Integer.toString(gameBoard.getClicks()));
	        		newClicks.setFont(new Font("Arial", Font.PLAIN, 40));
	        		// bounds must be set for label to display
	        		newClicks.setBounds(animation.contentPaneSize/10, 0, 500, animation.buffer);
	        		newClicks.setOpaque(true);
	        		frame.getContentPane().add(newClicks, 0);
	                //clickLabel.setText("Clicks remaining: " + Integer.toString(gameBoard.getClicks()));
	        		
	        		JLabel newScore = new JLabel("Score: " + Integer.toString(player.getScore()));
	        		newScore.setFont(new Font("Arial", Font.PLAIN, 40));
	        		// bounds must be set for label to display
	        		newScore.setBounds(animation.contentPaneSize/2, 0, 500, animation.buffer);
	        		newScore.setOpaque(true);
	        		frame.getContentPane().add(newScore, 0);
	        		
	        		if (item == GridSpace.Item.TRASH) {
		        		JLabel ateSome = new JLabel("Ate Some Trash :(");
		        		ateSome.setFont(new Font("Arial", Font.PLAIN, 40));
		        		// bounds must be set for label to display
		        		ateSome.setBounds(animation.contentPaneSize/10, animation.contentPaneSize - animation.buffer, 500, animation.buffer);
		        		ateSome.setOpaque(true);
		        		frame.getContentPane().add(ateSome, 0);
	        		}
	        		else if (item == GridSpace.Item.EGG) {
		        		JLabel ateSome = new JLabel("You Found and egg!!!");
		        		ateSome.setFont(new Font("Arial", Font.PLAIN, 40));
		        		// bounds must be set for label to display
		        		ateSome.setBounds(animation.contentPaneSize/10, animation.contentPaneSize - animation.buffer, 500, animation.buffer);
		        		ateSome.setOpaque(true);
		        		frame.getContentPane().add(ateSome, 0);
	        		}
	        		else if (item == GridSpace.Item.EMPTY) {
		        		JLabel ateSome = new JLabel("Nothing there...");
		        		ateSome.setFont(new Font("Arial", Font.PLAIN, 40));
		        		// bounds must be set for label to display
		        		ateSome.setBounds(animation.contentPaneSize/10, animation.contentPaneSize - animation.buffer, 500, animation.buffer);
		        		ateSome.setOpaque(true);
		        		frame.getContentPane().add(ateSome, 0);
	        		}
	        		else if (item == GridSpace.Item.ALREADYCHECKED) {
		        		JLabel ateSome = new JLabel("Already checked there.");
		        		ateSome.setFont(new Font("Arial", Font.PLAIN, 40));
		        		// bounds must be set for label to display
		        		ateSome.setBounds(animation.contentPaneSize/10, animation.contentPaneSize - animation.buffer, 500, animation.buffer);
		        		ateSome.setOpaque(true);
		        		frame.getContentPane().add(ateSome, 0);
	        		}
	        		
	                if (gameBoard.getClicks() == 0){
	                	endScreen();
	                }
	                if ((gameBoard.getClicks() % 3 == 0) && gameBoard.getClicks() != 0) {
	                	frame.getContentPane().add(powerChestButton, 1);
	                	animation.addChest();
	                	frame.getContentPane().repaint();
	                }
	                if (gameBoard.getClicks() % 3 == 2) {
	                	frame.getContentPane().remove(powerChestButton);
	                	animation.addChest();
	                	Iterator<AniObject> chestItr = animation.getImages().iterator();
	                	while (chestItr.hasNext() == true) {
	                		if (chestItr.next().toString() == "chest") {
	                			chestItr.remove();
	                		}
	                	}
	                	frame.getContentPane().repaint();
	                }
	                
	        }
	    });
		// add the button to the contentPane
		frame.getContentPane().add(gridButton, 0);
	}
	
	// displays score, and a quit button
	public void endScreen() {
		frame.getContentPane().removeAll();
		// need to repaint the contentPane to get rid of buttons
		frame.getContentPane().repaint();
		
		for(AniObject object: animation.getImages()) {
			object.setVisible(false);
		}
		
		// create a label
		JLabel eggLabel = new JLabel("You found " + Integer.toString(player.getEggs()) + " eggs,");
		eggLabel.setFont(new Font("Arial", Font.PLAIN, 60));
		// bounds must be set for label to display
		eggLabel.setBounds((animation.contentPaneSize/2) - 400, (animation.contentPaneSize/4) - 80, 800, 70);
		
		JLabel trashLabel = new JLabel("and you ate " + Integer.toString(player.getTrash()) + " pieces of trash,");
		trashLabel.setFont(new Font("Arial", Font.PLAIN, 60));
		trashLabel.setBounds((animation.contentPaneSize/2) - 400, (animation.contentPaneSize/4), 850, 70);
		
		JLabel scoreLabel = new JLabel("so your score is " + Integer.toString(player.getScore()) + "!!!");
		scoreLabel.setFont(new Font("Arial", Font.PLAIN, 60));
		scoreLabel.setBounds((animation.contentPaneSize/2) - 400, (animation.contentPaneSize/4) + 80, 800, 70);
		
		JButton quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Arial", Font.PLAIN, 30));
		quitButton.setLocation((animation.contentPaneSize/2) - (animation.generalButtonSize/2), 8*(animation.contentPaneSize/10));
		quitButton.setSize(animation.generalButtonSize, animation.generalButtonSize/2);
		quitButton.addActionListener(new ActionListener(){
	        public void actionPerformed(ActionEvent e){
	        	
	        	// when clicked, exits
		        System.exit(0);
	                
	        }
	    });
		
		frame.getContentPane().add(eggLabel);
		frame.getContentPane().add(trashLabel);
		frame.getContentPane().add(scoreLabel);
		frame.getContentPane().add(quitButton);
	}
	
	public static void tick(Animation animation, NewController controller) {
		Iterator<AniObject> itrMigration = animation.getImages().iterator();
		boolean buildBoard = false;
		while (itrMigration.hasNext()) {
			AniObject aniObject = itrMigration.next();
			if (aniObject.toString() == "bird") {
				aniObject.setY(aniObject.getY() - 10);
				if (aniObject.getY() == animation.contentPaneSize/5) {
					buildBoard = true;
					break;
				}
			}
		}
		if (buildBoard == true) {
			buildBoard = false;
			Iterator<AniObject> itrRemove = animation.getImages().iterator();
			while (itrRemove.hasNext()) {
				AniObject aniObjectRemove = itrRemove.next();
				if ((aniObjectRemove.toString() == "US") || (aniObjectRemove.toString() == "bird")) {
					itrRemove.remove();
				}
			}
			controller.buildBoard();
		}
	}
	
	// Game with GUI
	public static void main(String[] args) {
		NewController cont = new NewController();
       	cont.frame = new JFrame();
       	cont.frame.setPreferredSize(new Dimension(1000,1000));
       	cont.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  	cont.animation = new Animation();
	  	cont.animation.setVisible(true);
	  	cont.frame.getContentPane().add(cont.animation);
	  	cont.frame.pack();
	  	cont.frame.setVisible(true);
	  	cont.startScreen();
	  	while (true) {
	    		cont.frame.repaint();
	    		tick(cont.animation, cont);
	   		try {
	    			Thread.sleep(40);
	    		} catch (InterruptedException e) {
	    			e.printStackTrace();
	    		}
	  	}
		
	}
	
}