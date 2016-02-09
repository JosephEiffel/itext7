package com.itextpdf.kernel.geom;

import com.itextpdf.kernel.geom.Point2D;

import java.util.List;

/**
 * Represents segment from a PDF path.
 */
public interface Shape {

    /**
     * Treat base points as the points which are enough to construct a shape.
     * E.g. for a bezier curve they are control points, for a line segment - the start and the end points
     * of the segment.
     *
     * @return Ordered {@link java.util.List} consisting of shape's base points.
     */
    List<Point2D> getBasePoints();
}