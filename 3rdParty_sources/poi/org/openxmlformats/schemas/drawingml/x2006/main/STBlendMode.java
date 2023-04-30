/*
 * XML Type:  ST_BlendMode
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_BlendMode(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.
 */
public interface STBlendMode extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stblendmode3cf5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum OVER = Enum.forString("over");
    Enum MULT = Enum.forString("mult");
    Enum SCREEN = Enum.forString("screen");
    Enum DARKEN = Enum.forString("darken");
    Enum LIGHTEN = Enum.forString("lighten");

    int INT_OVER = Enum.INT_OVER;
    int INT_MULT = Enum.INT_MULT;
    int INT_SCREEN = Enum.INT_SCREEN;
    int INT_DARKEN = Enum.INT_DARKEN;
    int INT_LIGHTEN = Enum.INT_LIGHTEN;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STBlendMode.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_OVER
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

        static final int INT_OVER = 1;
        static final int INT_MULT = 2;
        static final int INT_SCREEN = 3;
        static final int INT_DARKEN = 4;
        static final int INT_LIGHTEN = 5;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("over", INT_OVER),
            new Enum("mult", INT_MULT),
            new Enum("screen", INT_SCREEN),
            new Enum("darken", INT_DARKEN),
            new Enum("lighten", INT_LIGHTEN),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
