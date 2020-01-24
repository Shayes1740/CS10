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
    private static final int maxColorDiff = 500;            // how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50;               // how many points in a region to be worth considering
    private static final int radius = 1;

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions = new ArrayList<ArrayList<Point>>();;            // a region is a list of points
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

    public void setRegions (ArrayList<ArrayList<Point>> regions){
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
        // TODO: YOUR CODE HERE
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                innerRegion = new ArrayList<Point>();
                toVisit = new ArrayList<Point>();

                if(visited.getRGB(x,y) == 0 && colorMatch(targetColor, new Color(image.getRGB(x,y)))){
                    toVisit.add(new Point(x, y));
                }

                while (toVisit.size() > 0) {

                    int nx = toVisit.get(0).x;
                    int ny = toVisit.get(0).y;

                    Color checkedColor = new Color(image.getRGB(nx, ny));
                    if (visited.getRGB(nx, ny) == 0 && colorMatch(checkedColor, targetColor)){
                        for (int b = Math.max(0, ny - radius); b < Math.min(image.getHeight(), ny + radius + 1); b++) {
                            for (int a = Math.max(0, nx - radius); a < Math.min(image.getWidth(), nx + radius + 1); a++) {
                                toVisit.add(new Point(a, b));
                            }
                        }
                        innerRegion.add(toVisit.get(0));
                    }
                    visited.setRGB(nx, ny, 1);
                    if (toVisit.size() > 0) {
                        toVisit.remove(toVisit.get(0));
                    }

                }
                if (innerRegion.size() >= minRegion) {
                    regions.add(innerRegion);
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
        return (diff < maxColorDiff);
    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        // TODO: YOUR CODE HERE
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
            // TODO: YOUR CODE HERE
            int maxColorValue = 16777216;
            int chosenColor;

            for (ArrayList<Point> region : regions) {
                chosenColor = (int) (Math.random() * (maxColorValue + 1)); //generates random number in range of colors
                Color color = new Color(chosenColor); //creates new color with chosen color value
                for (Point points : region) {
                    recoloredImage.setRGB(points.x, points.y, color.getRGB());
                }
            }
        }
        catch(Exception e){
            System.out.println("too slow");
        }
    }
}
