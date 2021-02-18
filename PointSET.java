import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return set.isEmpty();
    }                      // is the set empty?

    public int size() {
        return set.size();
    }                   // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        // for (Point2D point : set) {
        //     if (point == p) {
        //         return;
        //     }
        // }
        set.add(p);
    }            // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return set.contains(p);
    }          // does the set contain point p?

    public void draw() {
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }
    }        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> retList = new ArrayList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                retList.add(point);
            }
        }
        return retList;
    }      // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        double nearest = Integer.MAX_VALUE;
        Point2D nearestPoint = null;

        for (Point2D point : set) {
            if (p.distanceTo(point) < nearest) {
                nearest = p.distanceTo(point);
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }    // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        pointSET.insert(new Point2D(0, 1));
        pointSET.insert(new Point2D(1, 1));
        pointSET.insert(new Point2D(3, 3));
        pointSET.insert(new Point2D(6, 8));
        pointSET.insert(new Point2D(-1, 1));
        pointSET.insert(new Point2D(0, -2));
        pointSET.insert(new Point2D(0, 4));
        pointSET.insert(new Point2D(0, 4));
        System.out.println("Is the set empty? " + pointSET.isEmpty());
        System.out.println("The size is: " + pointSET.size());
        System.out.println(
                "Does the set contain the point (0,1)? " + pointSET.contains(new Point2D(0, 1)));
        System.out.println(
                "Which points are inside the rectangle? " + pointSET.range(new RectHV(0, 0, 4, 4)));
        System.out.println(
                "The nearest point to (0,1) in the set is: " + pointSET.nearest(new Point2D(0, 1)));
    }   // unit testing of the methods (optional)
}

/*
Output of the code:
Is the set empty? false
The size is: 7
Does the set contain the point (0,1)? true
Which points are inside the rectangle? [(0.0, 1.0), (1.0, 1.0), (3.0, 3.0), (0.0, 4.0)]
The nearest point to (0,1) in the set is: (-1.0, 1.0)
 */
