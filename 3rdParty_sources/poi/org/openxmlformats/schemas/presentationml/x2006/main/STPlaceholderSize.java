/*
 * XML Type:  ST_PlaceholderSize
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PlaceholderSize(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.
 */
public interface STPlaceholderSize extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stplaceholdersize914btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum FULL = Enum.forString("full");
    Enum HALF = Enum.forString("half");
    Enum QUARTER = Enum.forString("quarter");

    int INT_FULL = Enum.INT_FULL;
    int INT_HALF = Enum.INT_HALF;
    int INT_QUARTER = Enum.INT_QUARTER;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderSize.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_FULL
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

        static final int INT_FULL = 1;
        static final int INT_HALF = 2;
        static final int INT_QUARTER = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("full", INT_FULL),
            new Enum("half", INT_HALF),
            new Enum("quarter", INT_QUARTER),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
