/*
 * XML Type:  ST_Jc
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Jc(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc.
 */
public interface STJc extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stjc977ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum START = Enum.forString("start");
    Enum CENTER = Enum.forString("center");
    Enum END = Enum.forString("end");
    Enum BOTH = Enum.forString("both");
    Enum MEDIUM_KASHIDA = Enum.forString("mediumKashida");
    Enum DISTRIBUTE = Enum.forString("distribute");
    Enum NUM_TAB = Enum.forString("numTab");
    Enum HIGH_KASHIDA = Enum.forString("highKashida");
    Enum LOW_KASHIDA = Enum.forString("lowKashida");
    Enum THAI_DISTRIBUTE = Enum.forString("thaiDistribute");
    Enum LEFT = Enum.forString("left");
    Enum RIGHT = Enum.forString("right");

    int INT_START = Enum.INT_START;
    int INT_CENTER = Enum.INT_CENTER;
    int INT_END = Enum.INT_END;
    int INT_BOTH = Enum.INT_BOTH;
    int INT_MEDIUM_KASHIDA = Enum.INT_MEDIUM_KASHIDA;
    int INT_DISTRIBUTE = Enum.INT_DISTRIBUTE;
    int INT_NUM_TAB = Enum.INT_NUM_TAB;
    int INT_HIGH_KASHIDA = Enum.INT_HIGH_KASHIDA;
    int INT_LOW_KASHIDA = Enum.INT_LOW_KASHIDA;
    int INT_THAI_DISTRIBUTE = Enum.INT_THAI_DISTRIBUTE;
    int INT_LEFT = Enum.INT_LEFT;
    int INT_RIGHT = Enum.INT_RIGHT;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_START
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

        static final int INT_START = 1;
        static final int INT_CENTER = 2;
        static final int INT_END = 3;
        static final int INT_BOTH = 4;
        static final int INT_MEDIUM_KASHIDA = 5;
        static final int INT_DISTRIBUTE = 6;
        static final int INT_NUM_TAB = 7;
        static final int INT_HIGH_KASHIDA = 8;
        static final int INT_LOW_KASHIDA = 9;
        static final int INT_THAI_DISTRIBUTE = 10;
        static final int INT_LEFT = 11;
        static final int INT_RIGHT = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("start", INT_START),
            new Enum("center", INT_CENTER),
            new Enum("end", INT_END),
            new Enum("both", INT_BOTH),
            new Enum("mediumKashida", INT_MEDIUM_KASHIDA),
            new Enum("distribute", INT_DISTRIBUTE),
            new Enum("numTab", INT_NUM_TAB),
            new Enum("highKashida", INT_HIGH_KASHIDA),
            new Enum("lowKashida", INT_LOW_KASHIDA),
            new Enum("thaiDistribute", INT_THAI_DISTRIBUTE),
            new Enum("left", INT_LEFT),
            new Enum("right", INT_RIGHT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
