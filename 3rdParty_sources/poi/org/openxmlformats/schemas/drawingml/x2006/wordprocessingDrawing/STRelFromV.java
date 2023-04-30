/*
 * XML Type:  ST_RelFromV
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_RelFromV(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.
 */
public interface STRelFromV extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "strelfromv56dctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum MARGIN = Enum.forString("margin");
    Enum PAGE = Enum.forString("page");
    Enum PARAGRAPH = Enum.forString("paragraph");
    Enum LINE = Enum.forString("line");
    Enum TOP_MARGIN = Enum.forString("topMargin");
    Enum BOTTOM_MARGIN = Enum.forString("bottomMargin");
    Enum INSIDE_MARGIN = Enum.forString("insideMargin");
    Enum OUTSIDE_MARGIN = Enum.forString("outsideMargin");

    int INT_MARGIN = Enum.INT_MARGIN;
    int INT_PAGE = Enum.INT_PAGE;
    int INT_PARAGRAPH = Enum.INT_PARAGRAPH;
    int INT_LINE = Enum.INT_LINE;
    int INT_TOP_MARGIN = Enum.INT_TOP_MARGIN;
    int INT_BOTTOM_MARGIN = Enum.INT_BOTTOM_MARGIN;
    int INT_INSIDE_MARGIN = Enum.INT_INSIDE_MARGIN;
    int INT_OUTSIDE_MARGIN = Enum.INT_OUTSIDE_MARGIN;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STRelFromV.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_MARGIN
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

        static final int INT_MARGIN = 1;
        static final int INT_PAGE = 2;
        static final int INT_PARAGRAPH = 3;
        static final int INT_LINE = 4;
        static final int INT_TOP_MARGIN = 5;
        static final int INT_BOTTOM_MARGIN = 6;
        static final int INT_INSIDE_MARGIN = 7;
        static final int INT_OUTSIDE_MARGIN = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("margin", INT_MARGIN),
            new Enum("page", INT_PAGE),
            new Enum("paragraph", INT_PARAGRAPH),
            new Enum("line", INT_LINE),
            new Enum("topMargin", INT_TOP_MARGIN),
            new Enum("bottomMargin", INT_BOTTOM_MARGIN),
            new Enum("insideMargin", INT_INSIDE_MARGIN),
            new Enum("outsideMargin", INT_OUTSIDE_MARGIN),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
