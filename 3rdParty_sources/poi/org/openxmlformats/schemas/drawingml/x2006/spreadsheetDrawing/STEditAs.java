/*
 * XML Type:  ST_EditAs
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_EditAs(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.
 */
public interface STEditAs extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "steditasad40type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TWO_CELL = Enum.forString("twoCell");
    Enum ONE_CELL = Enum.forString("oneCell");
    Enum ABSOLUTE = Enum.forString("absolute");

    int INT_TWO_CELL = Enum.INT_TWO_CELL;
    int INT_ONE_CELL = Enum.INT_ONE_CELL;
    int INT_ABSOLUTE = Enum.INT_ABSOLUTE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TWO_CELL
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

        static final int INT_TWO_CELL = 1;
        static final int INT_ONE_CELL = 2;
        static final int INT_ABSOLUTE = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("twoCell", INT_TWO_CELL),
            new Enum("oneCell", INT_ONE_CELL),
            new Enum("absolute", INT_ABSOLUTE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
