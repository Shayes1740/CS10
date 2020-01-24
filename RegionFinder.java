import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author Stuart Hayes, Dartmouth CS 10, Winter 2020
 * @author Thomas Jacob Fyda, Dartmouth CS 10, Winter 2020
 *
 */
public class RegionFinder {
	private static final int maxColorDiff = 200;            // how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50;               // how many points in a region to be worth considering
	private static final int radius = 1;					// how many points to check in each direction

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();;            // a region is a list of points
	// so the identified regions are in a list of lists of points
	private ArrayList<Point> innerRegion;					// holds a list of points of similar color
	private ArrayList<Point> toVisit;						// list of points that need to be checked (visited)


	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setRegions (ArrayList<ArrayList<Point>> regions){		// Creates regions of similar color to target
		this.regions = regions;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */

	public void findRegions(Color targetColor) {
		// Creates a second image to keep track of points that have been checked, all points set to black
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		// Loops through every point on the image
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				innerRegion = new ArrayList<Point>();	// Initializes the inner region
				toVisit = new ArrayList<Point>();		// Initializes the points that have yet to be checked

				// If a point is black in the visited image (has not been checked) and is close to the target color in the webcam, add it to toVisit
				if(visited.getRGB(x,y) == 0 && colorMatch(targetColor, new Color(image.getRGB(x,y)))){
					toVisit.add(new Point(x, y));
				}
				// Run while there are points to visit
				while (toVisit.size() > 0) {
					// Sets new variables to hold coordinates of the first item in toVisit
					int nx = toVisit.get(0).x;
					int ny = toVisit.get(0).y;

					// Creates a new color that holds the color at those coordinates
					Color checkedColor = new Color(image.getRGB(nx, ny));
					// Proceeds if the point is not visited and is close to the target color
					if (visited.getRGB(nx, ny) == 0 && colorMatch(checkedColor, targetColor)){
						//  Loops over the neighbors of (nx, ny), checking points in all directions
						for (int b = Math.max(0, ny - radius); b < Math.min(image.getHeight(), ny + radius + 1); b++) {
							for (int a = Math.max(0, nx - radius); a < Math.min(image.getWidth(), nx + radius + 1); a++) {
								toVisit.add(new Point(a, b));	// Adds the neighbor points to toVisit
							}
						}
						innerRegion.add(toVisit.get(0));	// Adds the first item in toVisit to the growing inner region
					}
					visited.setRGB(nx, ny, 1);		// Makes the pixel not black and marks it as visited

					if (toVisit.size() > 0) {			// Only removes if there is something to remove
						toVisit.remove(toVisit.get(0));		// Removes the first point in toVisit so it will not be checked again
					}

				}
				if (innerRegion.size() >= minRegion) {		// Checks if the region is large enough
					regions.add(innerRegion);				// Adds the contiguous inner region to the list of regions
				}

			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */

	private static boolean colorMatch (Color c1, Color c2) {
		// Uses Euclidean distance to return the maximum color difference value
		int diff = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
				+ (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
				+ (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
		return (diff < maxColorDiff);
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */

	public ArrayList<Point> largestRegion() {
		// Identifies and returns the largest region of similar color to the target color
		ArrayList<Point> largestOutThere = new ArrayList<Point>();
		for(ArrayList<Point> region: regions){
			if (region.size() > largestOutThere.size()){
				largestOutThere = region;
			}
		}
		return largestOutThere;
	}

	/**
	 * Sets recoloredImage to be a copy of image,
	 * but with each region a uniform random color,
	 * so we can see where they are
	 */

	public void recolorImage() {
		// First copy the original
		try {
			recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
			// Now recolor the regions in it

			int maxColorValue = 16777216;	// Maximum possible color value
			int chosenColor;				// Color chosen for a region

			for (ArrayList<Point> region : regions) {
				chosenColor = (int) (Math.random() * (maxColorValue + 1)); //generates random number in range of colors
				Color color = new Color(chosenColor); //creates new color with chosen color value
				for (Point points : region) {
					recoloredImage.setRGB(points.x, points.y, color.getRGB());	// Colors regions randomly
				}
			}
		}
		catch(Exception e){		// throws an exception when running too slowly
			System.out.println("too slow");
		}
	}
}