/*
 * XML Type:  ST_HorizontalAlignment
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_HorizontalAlignment(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.
 */
public interface STHorizontalAlignment extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sthorizontalalignmentf92etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum GENERAL = Enum.forString("general");
    Enum LEFT = Enum.forString("left");
    Enum CENTER = Enum.forString("center");
    Enum RIGHT = Enum.forString("right");
    Enum FILL = Enum.forString("fill");
    Enum JUSTIFY = Enum.forString("justify");
    Enum CENTER_CONTINUOUS = Enum.forString("centerContinuous");
    Enum DISTRIBUTED = Enum.forString("distributed");

    int INT_GENERAL = Enum.INT_GENERAL;
    int INT_LEFT = Enum.INT_LEFT;
    int INT_CENTER = Enum.INT_CENTER;
    int INT_RIGHT = Enum.INT_RIGHT;
    int INT_FILL = Enum.INT_FILL;
    int INT_JUSTIFY = Enum.INT_JUSTIFY;
    int INT_CENTER_CONTINUOUS = Enum.INT_CENTER_CONTINUOUS;
    int INT_DISTRIBUTED = Enum.INT_DISTRIBUTED;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_GENERAL
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

        static final int INT_GENERAL = 1;
        static final int INT_LEFT = 2;
        static final int INT_CENTER = 3;
        static final int INT_RIGHT = 4;
        static final int INT_FILL = 5;
        static final int INT_JUSTIFY = 6;
        static final int INT_CENTER_CONTINUOUS = 7;
        static final int INT_DISTRIBUTED = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("general", INT_GENERAL),
            new Enum("left", INT_LEFT),
            new Enum("center", INT_CENTER),
            new Enum("right", INT_RIGHT),
            new Enum("fill", INT_FILL),
            new Enum("justify", INT_JUSTIFY),
            new Enum("centerContinuous", INT_CENTER_CONTINUOUS),
            new Enum("distributed", INT_DISTRIBUTED),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
