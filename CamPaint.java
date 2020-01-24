import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Webcam-based drawing 
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Spring 2015 (based on a different webcam app from previous terms)
 * @author Stuart Hayes, Dartmouth CS 10, Winter 2020
 * @author Thomas Jacob Fyda, Dartmouth CS 10, Winter 2020
 *
 */

public class CamPaint extends Webcam {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press), starts null
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	/**
	 * Initializes the region finder and the drawing
	 */

	public CamPaint() {
		finder = new RegionFinder();
		clearPainting();
	}

	/**
	 * Resets the painting to a blank image
	 */

	protected void clearPainting() {
		painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * DrawingGUI method, here drawing one of live webcam, recolored image, or painting,
	 * depending on display variable ('w', 'r', or 'p')
	 */

	@Override
	public void draw(Graphics g) {
		// Displays the image if has been provided
		if (image != null){
			processImage();
			if (displayMode == 'w'){	// Shows the live webcam feed
				super.draw(g);
			}
			else if (displayMode == 'r'){	// Shows the image colored based off the selected color and region
				g.drawImage(finder.getRecoloredImage(), 0, 0, null);
			}
			else if(displayMode == 'p'){	// Shows the painting on a blank screen
				g.drawImage(painting, 0, 0, null);
			}
		}
	}

	/**
	 * Overrides the DrawingGUI method to set the track color.
	 */

	@Override
	public void handleMousePress(int x, int y) {
		// Sets and prints the target color based on a mouse clock
		targetColor = new Color(image.getRGB(x, y));
		System.out.println("tracking " + targetColor);
	}

	/**
	 * Webcam method, here finding regions and updating the painting.
	 */

	@Override
	public void processImage() {
		// Only builds regions and paints if a target color has been selected
		if (targetColor != null){

			finder.setImage(image);		// Sets the output image
			finder.setRegions(new ArrayList<ArrayList<Point>>());	//  Creates regions we keep track of
			finder.findRegions(targetColor);	// Finds regions that closely match the target color

			ArrayList<Point> finalRegion = finder.largestRegion();		// Picks the largest region to be the paintbrush
			for (Point points : finalRegion) {		// Paints by following the movement of the brush
				if (painting.getRGB(points.x, points.y) != paintColor.getRGB()){	// Makes it so the brush does not repaint (if the color is blue already, no need to repaint)
					painting.setRGB(points.x, points.y, paintColor.getRGB());		// Paints in blue
				}
			}
			// Recolors the webcam output to show the brush region
			finder.recolorImage();
		}
	}


	/**
	 * DrawingGUI method, here doing various drawing commands
	 */

	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
			System.out.println("Key press is on '" +displayMode+ "'");
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			saveImage(painting, "pictures/painting.png", "png");
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CamPaint();
			}
		});
	}
}
