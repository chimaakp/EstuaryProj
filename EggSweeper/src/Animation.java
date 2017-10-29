import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

// The View

public class Animation extends JPanel{
	
	private List<AniObject> images;
	
	// constants for placing buttons
	public int buffer = 50;
	public int gridButtonSize = 45;
	public int generalButtonSize = 200;
	public int contentPaneSize = 1000;
	
	public List<AniObject> getImages() {
		return images;
	}
	
	// constructor, takes a Game object to link GUI to controller
	Animation(){
		images = new ArrayList<AniObject>();
		this.setPreferredSize(new Dimension(contentPaneSize, contentPaneSize));
		try {
			BufferedImage beach = ImageIO.read(new File("images/beach.png"));
			images.add(new AniObject("beach", buffer, buffer, contentPaneSize - (2*buffer), contentPaneSize - (2*buffer), beach));
		} catch (IOException e) {
			System.out.println("Failed to load beach, trying bin folder");
		}
		try {
			BufferedImage beach = ImageIO.read(new File("beach.png"));
			images.add(new AniObject("beach", buffer, buffer, contentPaneSize - (2*buffer), contentPaneSize - (2*buffer), beach));
		} catch (IOException e) {
			System.out.println("Failed to load beach");
		}
		
	}
	
	public void paint(Graphics g) {
		Iterator<AniObject> imageIterator = images.iterator();
		while (imageIterator.hasNext()) {
			AniObject image = imageIterator.next();
			if (image.getVisible() == true) {
				g.drawImage(image.getImage(), image.getX(), image.getY(), image.getXSize(), image.getYSize(), this);
			}
		}
    	
	}

	public void addHole(int xIndex, int yIndex) {
		AniObject hole;
		try {
			hole = new AniObject("hole", buffer + (xIndex*gridButtonSize), buffer + (yIndex*gridButtonSize), gridButtonSize, gridButtonSize, ImageIO.read(new File("images/hole.png")));
			hole.setVisible(true);
            images.add(hole);
		} catch (IOException e1) {
			System.out.println("failed to load hole, trying bin folder");
		}
		try {
			hole = new AniObject("hole", buffer + (xIndex*gridButtonSize), buffer + (yIndex*gridButtonSize), gridButtonSize, gridButtonSize, ImageIO.read(new File("hole.png")));
			hole.setVisible(true);
            images.add(hole);
		} catch (IOException e1) {
			System.out.println("failed to load hole");
		}
		
	}
	
	public void addChest() {
		AniObject chest;
		try {
			chest = new AniObject("chest", contentPaneSize - (3*buffer), contentPaneSize - (3*buffer), 2 * buffer, 2 * buffer, ImageIO.read(new File("images/chest.png")));
			chest.setVisible(true);
            images.add(chest);
		} catch (IOException e1) {
			System.out.println("failed to load chest, trying bin folder");
		}
		try {
			chest = new AniObject("chest", contentPaneSize - (3*buffer), contentPaneSize - (3*buffer), 2 * buffer, 2 * buffer, ImageIO.read(new File("chest.png")));
			chest.setVisible(true);
            images.add(chest);
		} catch (IOException e1) {
			System.out.println("failed to load chest");
		}
		
	}

	public void migrationAnimation() {
		AniObject bird;
		AniObject US;
		try {
			US = new AniObject("US", 100, 100, 600, 800, ImageIO.read(new File("images/US.png")));
            US.setVisible(true);
            images.add(US);
			bird = new AniObject("bird", (int) Math.round(contentPaneSize*(3./5.)), (int) Math.round(contentPaneSize*(4./5.)), 100, 150, ImageIO.read(new File("images/bird.png")));
			bird.setVisible(true);
            images.add(bird);
		} catch (IOException e1) {
			System.out.println("failed to load US or Bird, trying bin folder");
		}
		try {
			US = new AniObject("US", 100, 100, 600, 800, ImageIO.read(new File("US.png")));
            US.setVisible(true);
            images.add(US);
			bird = new AniObject("bird", (int) Math.round(contentPaneSize*(3./5.)), (int) Math.round(contentPaneSize*(4./5.)), 100, 150, ImageIO.read(new File("bird.png")));
			bird.setVisible(true);
            images.add(bird);
		} catch (IOException e1) {
			System.out.println("failed to load US or Bird");
		}
		
	}
	
}