/*
 * XML Type:  ST_AlignV
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_AlignV(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.
 */
public interface STAlignV extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stalignv5abetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TOP = Enum.forString("top");
    Enum BOTTOM = Enum.forString("bottom");
    Enum CENTER = Enum.forString("center");
    Enum INSIDE = Enum.forString("inside");
    Enum OUTSIDE = Enum.forString("outside");

    int INT_TOP = Enum.INT_TOP;
    int INT_BOTTOM = Enum.INT_BOTTOM;
    int INT_CENTER = Enum.INT_CENTER;
    int INT_INSIDE = Enum.INT_INSIDE;
    int INT_OUTSIDE = Enum.INT_OUTSIDE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STAlignV.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TOP
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

        static final int INT_TOP = 1;
        static final int INT_BOTTOM = 2;
        static final int INT_CENTER = 3;
        static final int INT_INSIDE = 4;
        static final int INT_OUTSIDE = 5;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("top", INT_TOP),
            new Enum("bottom", INT_BOTTOM),
            new Enum("center", INT_CENTER),
            new Enum("inside", INT_INSIDE),
            new Enum("outside", INT_OUTSIDE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
