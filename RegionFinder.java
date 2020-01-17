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
 */
public class RegionFinder {
    private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50;                // how many points in a region to be worth considering
    private static final int radius = 1;

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions;            // a region is a list of points
    // so the identified regions are in a list of lists of points
    private ArrayList<Point> innerRegion;
    private ArrayList<Point> toVisit;


    public RegionFinder() {
        this.image = null;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
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
        // TODO: YOUR CODE HERE
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        innerRegion = new ArrayList<>();
        toVisit = new ArrayList<Point>();
        regions = new ArrayList<ArrayList<Point>>();


        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                System.out.println("x:" + x + ", y:" + y);
                int regionSearch = 0;
                Color initialColor = new Color(image.getRGB(x, y));
                if (colorMatch(initialColor, targetColor)) {
                        toVisit.add(new Point(x, y));
                        while (toVisit.get(regionSearch) != null) {
                            int nx = (int) (toVisit.get(regionSearch).getX());
                            int ny = (int) (toVisit.get(regionSearch).getY());
                            visited.setRGB(nx, ny, 1);
                            //System.out.println("nx:" + nx + ", ny:" + ny);
                            regionSearch++;


                            for (int b = Math.max(0, ny - radius); b <= Math.min(image.getHeight(), ny + radius); b++) {
                                for (int a = Math.max(0, nx - radius); a <= Math.min(image.getWidth(), nx + radius); a++) {
                                    //System.out.println("a:" + a + ", b:" + b);
									Color checkedColor = new Color(image.getRGB(a, b));
                                    //System.out.println(checkedColor);
                                    if (visited.getRGB(a, b) == 0){
                                        if (colorMatch(checkedColor, targetColor)) {
                                            //System.out.println("match found");
                                            innerRegion.add(new Point(a, b));
                                            toVisit.add(new Point(a, b));
                                        }
                                    }
                                }
                            }
                            System.out.println("Inner Region Size: "+innerRegion.size());
						}
						//deal with removal of toVisit and check region size
						toVisit.clear();

						if (innerRegion.size() >= minRegion){
							regions.add(innerRegion);
							innerRegion.clear();
						}
						else {
							innerRegion.clear();
						}



                }
            }
        }
    }

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch (Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		int diff = (c1.getRed() - c2.getRed()) * (c1.getRed() - c2.getRed())
				+ (c1.getGreen() - c2.getGreen()) * (c1.getGreen() - c2.getGreen())
				+ (c1.getBlue() - c2.getBlue()) * (c1.getBlue() - c2.getBlue());
		if (diff <= maxColorDiff){
			return true;
		}
		return false;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
		int entireRegionSearch = 0;
		int maxRegionSize = 0;
		int maxRegionIndex = 0;

		while(regions.get(entireRegionSearch) != null){
			if (regions.get(entireRegionSearch).size() >= maxRegionSize){
				maxRegionIndex = entireRegionSearch;
			}
			else{
				System.out.println("Largest region not identified.");
			}
		}
		return regions.get(maxRegionIndex);
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// TODO: YOUR CODE HERE
		int regionIndex = 0;
		int pixelIndex = 0;
		int maxColorValue = 16777216;
		int chosenColor;


//		while (regions.get(regionIndex) != null){
//			chosenColor = (int)(Math.random()*(maxColorValue+ 1)); //generates random number in range of colors
//			Color color = new Color(chosenColor); //creates new color with chosen color value
//			while(regions.get(regionIndex).get(pixelIndex) != null){
//				int nx = (int) (regions.get(regionIndex).get(pixelIndex).getX());
//				int ny = (int) (regions.get(regionIndex).get(pixelIndex).getY());
//				recoloredImage.setRGB(nx, ny, color.getRGB());
//				pixelIndex++;
//			}
//			regionIndex++;
//		}
	}
}
