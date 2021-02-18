import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private int size;
    private Node root;

    private class Node {
        private Point2D point2D;
        private RectHV rect;
        private Node right;
        private Node left;

        public Node(Point2D point2D, RectHV rect) {
            this.point2D = point2D;
            this.rect = rect;
        }
    }

    public KdTree() {
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }                      // is the set empty?

    public int size() {
        return size;
    }                   // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        // root = insertNode(root, p, vertical, new RectHV(0.0, 0.0, 1.0, 1.0));
        root = insert(root, p, true, 0.0, 0.0, 1.0, 1.0);
    } // add the point to the set (if it is not already in the set)

    private Node insert(Node root, Point2D point2D, boolean vertical, double xmin, double ymin,
                        double xmax, double ymax) {
        if (root == null) {
            size++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(point2D, rect);
        }
        if (vertical) {
            if (point2D.x() < root.point2D.x() || (point2D.x() == root.point2D.x()
                    && point2D.y() < root.point2D.y())) {
                root.left = insert(root.left, point2D, !vertical, root.rect.xmin(),
                                   root.rect.ymin(), root.point2D.x(),
                                   root.rect.ymax());
            }
            else if (point2D.x() > root.point2D.x() || (point2D.x() == root.point2D.x()
                    && root.point2D.y() < point2D.y())) {
                root.right = insert(root.right, point2D, !vertical, root.point2D.x(),
                                    root.rect.ymin(), root.rect.xmax(),
                                    root.rect.ymax());
            }
            else {
                root.point2D = point2D;
            }
        }
        else {
            if (point2D.y() < root.point2D.y() || (point2D.y() == root.point2D.y()
                    && point2D.x() < root.point2D.x())) {
                root.left = insert(root.left, point2D, vertical, root.rect.xmin(), root.rect.ymin(),
                                   root.rect.xmax(),
                                   root.point2D.y());
            }
            else if (point2D.y() > root.point2D.y() || (point2D.y() == root.point2D.y()
                    && point2D.x() > root.point2D.x())) {
                root.right = insert(root.right, point2D, vertical, root.rect.xmin(),
                                    root.point2D.y(), root.rect.xmax(),
                                    root.rect.ymax());
            }
            else {
                root.point2D = point2D;
            }
        }
        return root;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return contains(root, p, true);
    }          // does the set contain point p?

    private boolean contains(Node root, Point2D point2D, boolean vertical) {
        if (root == null) {
            return false;
        }
        if (root.point2D.equals(point2D)) {
            return true;
        }
        if (vertical) {
            if (point2D.x() < root.point2D.x() || (point2D.x() == root.point2D.x()
                    && point2D.y() < root.point2D.y())) {
                return contains(root.left, point2D, !vertical);
            }
            else if (point2D.x() > root.point2D.x() || (point2D.x() == root.point2D.x()
                    && root.point2D.y() < point2D.y())) {
                return contains(root.right, point2D, !vertical);
            }
            else {
                return true;
            }
        }
        else {
            if (point2D.y() < root.point2D.y() || (point2D.y() == root.point2D.y()
                    && point2D.x() < root.point2D.x())) {
                return contains(root.left, point2D, vertical);
            }
            else if (point2D.y() > root.point2D.y() || (point2D.y() == root.point2D.y()
                    && point2D.x() > root.point2D.x())) {
                return contains(root.right, point2D, vertical);
            }
            else {
                return true;
            }
        }
    }

    public void draw() {
        draw(root, true);
    }        // draw all points to standard draw

    private void draw(Node root, boolean vertical) {
        if (root == null) {
            throw new IllegalArgumentException();
        }

        StdDraw.setPenRadius();
        StdDraw.setPenColor(Color.BLACK);
        root.point2D.draw();

        if (!vertical) {
            StdDraw.setPenColor(Color.BLUE);
            // StdDraw.line()
        }
        else {
            StdDraw.setPenColor(Color.RED);
            // StdDraw.line()
        }

        if (root.right != null) {
            draw(root.right, !vertical);
        }

        if (root.left != null) {
            draw(root.left, !vertical);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> points = new Stack<Point2D>();
        range(root, points, rect);
        return points;
    }      // all points that are inside the rectangle (or on the boundary)

    private void range(Node root, Stack<Point2D> points, RectHV rectHV) {
        if (root == null || !root.rect.intersects(rectHV)) {
            return;
        }

        if (rectHV.contains(root.point2D)) {
            points.push(root.point2D);
        }

        range(root.left, points, rectHV);
        range(root.right, points, rectHV);
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }

        if (p == null) {
            throw new IllegalArgumentException();
        }
        return nearest(root, root.point2D, p, true);
    }    // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node node, Point2D nearestNeighbor, Point2D p, boolean vertical) {
        if (node == null) {
            return nearestNeighbor;
        }

        Point2D nearest = nearestNeighbor;

        if (node.point2D.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = node.point2D;
        }

        // search a node only only if it might contain a point that is closer than the best one found so far
        // if (nearest.distanceSquaredTo(p) > node.rect.distanceSquaredTo(p)) {
        if (nearest.distanceSquaredTo(p) > node.rect.distanceSquaredTo(p)) {
            // scenario to search the left subtree would be when vertical and p.x() < node.point2d.x()
            // or when horizontal and p.y() < node.point2d.y()
            if (vertical) {
                if (p.x() < node.point2D.x()) {
                    // check the left subtree recursively first, and see if there is a point closer than current nearest
                    // check right next
                    nearest = nearest(node.left, nearest, p, !vertical);
                    nearest = nearest(node.right, nearest, p, !vertical);
                }
                else {
                    nearest = nearest(node.right, nearest, p, !vertical);
                    nearest = nearest(node.left, nearest, p, !vertical);
                }
            }
            else {
                if (p.y() < node.point2D.y()) {
                    // same logic as above, except comparing y components as it is for horizontal
                    nearest = nearest(node.left, nearest, p, !vertical);
                    nearest = nearest(node.right, nearest, p, !vertical);
                }
                else {
                    nearest = nearest(node.right, nearest, p, !vertical);
                    nearest = nearest(node.left, nearest, p, !vertical);
                }
            }
        }
        return nearest;
    }

    public static void main(String[] args) { // unit testing of the methods (optional)

    }
    // unit testing of the methods (optional)
}

