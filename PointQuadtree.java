
import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position,
 * with children at the subdivided quadrants
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 *
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters

	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		int x = (int) point.getX();
		int y = (int) point.getY();

		int xnew = (int) p2.getX();
		int ynew = (int) p2.getY();

		if ((x < xnew) && (y < ynew)) {
			if (c4 != null) { //meaning there is a child
				c4.insert(p2);
			}
			else {
				c4 = new PointQuadtree<>(p2, x, y, x2, y2);
			}
		}
		else if ((x > xnew) && (y < ynew)){
			if (c3 != null) { //meaning there is a child
				c3.insert(p2);
			}
			else {
				c3 = new PointQuadtree<>(p2, x1, y, x, y2);
			}
		}
		else if ((x < xnew) && (y > ynew)){
			if (c1 != null) { //meaning there is a child
				c1.insert(p2);
			}
			else {
				c1 = new PointQuadtree<>(p2, x, y1, x2, y);
			}
		}
		else if ((x > xnew) && (y > ynew)){
			if (c2 != null) { //meaning there is a child
				c2.insert(p2);
			}
			else {
				c2 = new PointQuadtree<>(p2, x1, y1, x, y);
			}
		}
	}

	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		int points = 1;
		if (hasChild(1)){
			points += c1.size();
		}
		if (hasChild(2)){
			points += c2.size();
		}
		if (hasChild(3)){
			points += c3.size();
		}
		if (hasChild(4)){
			points += c4.size();
		}
		return points;
	}

	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE;
		ArrayList<E> listOfPoints = new ArrayList<>();
		addToListOfPoints(listOfPoints);
		return listOfPoints;
	}

	//	 TODO: YOUR CODE HERE for any helper methods
	private void addToListOfPoints(ArrayList<E> listOfPoints){
		listOfPoints.add(point);
		for(int i = 1; i < 5; i++){
			if (hasChild(i)) {
				getChild(i).addToListOfPoints(listOfPoints);
			}
		}

	}

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		ArrayList<E> pointsInCircle = new ArrayList<>();
		helpFindInCircle(pointsInCircle, cx, cy, cr);
		return pointsInCircle;
	}


	private void helpFindInCircle(ArrayList<E> pointsInCircle, double cx, double cy, double cr){
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)){
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {
				pointsInCircle.add(point);
			}
			for (int i = 1; i < 5; i++) {
				if (hasChild(i)) {
					getChild(i).helpFindInCircle(pointsInCircle, cx, cy, cr);
				}
			}
		}
	}
}
