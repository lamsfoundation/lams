/*
 * XML Type:  ST_MarkerStyle
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_MarkerStyle(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle.
 */
public interface STMarkerStyle extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stmarkerstyle177ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum CIRCLE = Enum.forString("circle");
    Enum DASH = Enum.forString("dash");
    Enum DIAMOND = Enum.forString("diamond");
    Enum DOT = Enum.forString("dot");
    Enum NONE = Enum.forString("none");
    Enum PICTURE = Enum.forString("picture");
    Enum PLUS = Enum.forString("plus");
    Enum SQUARE = Enum.forString("square");
    Enum STAR = Enum.forString("star");
    Enum TRIANGLE = Enum.forString("triangle");
    Enum X = Enum.forString("x");
    Enum AUTO = Enum.forString("auto");

    int INT_CIRCLE = Enum.INT_CIRCLE;
    int INT_DASH = Enum.INT_DASH;
    int INT_DIAMOND = Enum.INT_DIAMOND;
    int INT_DOT = Enum.INT_DOT;
    int INT_NONE = Enum.INT_NONE;
    int INT_PICTURE = Enum.INT_PICTURE;
    int INT_PLUS = Enum.INT_PLUS;
    int INT_SQUARE = Enum.INT_SQUARE;
    int INT_STAR = Enum.INT_STAR;
    int INT_TRIANGLE = Enum.INT_TRIANGLE;
    int INT_X = Enum.INT_X;
    int INT_AUTO = Enum.INT_AUTO;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.chart.STMarkerStyle.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_CIRCLE
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s) {
            return (Enum)table.forString(s);
        }

        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i) {
            return (Enum)table.forInt(i);
        }

        private Enum(java.lang.String s, int i) {
            super(s, i);
        }

        static final int INT_CIRCLE = 1;
        static final int INT_DASH = 2;
        static final int INT_DIAMOND = 3;
        static final int INT_DOT = 4;
        static final int INT_NONE = 5;
        static final int INT_PICTURE = 6;
        static final int INT_PLUS = 7;
        static final int INT_SQUARE = 8;
        static final int INT_STAR = 9;
        static final int INT_TRIANGLE = 10;
        static final int INT_X = 11;
        static final int INT_AUTO = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("circle", INT_CIRCLE),
            new Enum("dash", INT_DASH),
            new Enum("diamond", INT_DIAMOND),
            new Enum("dot", INT_DOT),
            new Enum("none", INT_NONE),
            new Enum("picture", INT_PICTURE),
            new Enum("plus", INT_PLUS),
            new Enum("square", INT_SQUARE),
            new Enum("star", INT_STAR),
            new Enum("triangle", INT_TRIANGLE),
            new Enum("x", INT_X),
            new Enum("auto", INT_AUTO),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
