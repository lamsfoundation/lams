/*
 * XML Type:  ST_TextAlignType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextAlignType(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.
 */
public interface STTextAlignType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextaligntypebc93type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum L = Enum.forString("l");
    Enum CTR = Enum.forString("ctr");
    Enum R = Enum.forString("r");
    Enum JUST = Enum.forString("just");
    Enum JUST_LOW = Enum.forString("justLow");
    Enum DIST = Enum.forString("dist");
    Enum THAI_DIST = Enum.forString("thaiDist");

    int INT_L = Enum.INT_L;
    int INT_CTR = Enum.INT_CTR;
    int INT_R = Enum.INT_R;
    int INT_JUST = Enum.INT_JUST;
    int INT_JUST_LOW = Enum.INT_JUST_LOW;
    int INT_DIST = Enum.INT_DIST;
    int INT_THAI_DIST = Enum.INT_THAI_DIST;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STTextAlignType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_L
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

        static final int INT_L = 1;
        static final int INT_CTR = 2;
        static final int INT_R = 3;
        static final int INT_JUST = 4;
        static final int INT_JUST_LOW = 5;
        static final int INT_DIST = 6;
        static final int INT_THAI_DIST = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("l", INT_L),
            new Enum("ctr", INT_CTR),
            new Enum("r", INT_R),
            new Enum("just", INT_JUST),
            new Enum("justLow", INT_JUST_LOW),
            new Enum("dist", INT_DIST),
            new Enum("thaiDist", INT_THAI_DIST),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
