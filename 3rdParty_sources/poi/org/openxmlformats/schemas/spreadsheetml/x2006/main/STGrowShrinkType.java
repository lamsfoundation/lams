/*
 * XML Type:  ST_GrowShrinkType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STGrowShrinkType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_GrowShrinkType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STGrowShrinkType.
 */
public interface STGrowShrinkType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STGrowShrinkType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stgrowshrinktype5ceftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum INSERT_DELETE = Enum.forString("insertDelete");
    Enum INSERT_CLEAR = Enum.forString("insertClear");
    Enum OVERWRITE_CLEAR = Enum.forString("overwriteClear");

    int INT_INSERT_DELETE = Enum.INT_INSERT_DELETE;
    int INT_INSERT_CLEAR = Enum.INT_INSERT_CLEAR;
    int INT_OVERWRITE_CLEAR = Enum.INT_OVERWRITE_CLEAR;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STGrowShrinkType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_INSERT_DELETE
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

        static final int INT_INSERT_DELETE = 1;
        static final int INT_INSERT_CLEAR = 2;
        static final int INT_OVERWRITE_CLEAR = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("insertDelete", INT_INSERT_DELETE),
            new Enum("insertClear", INT_INSERT_CLEAR),
            new Enum("overwriteClear", INT_OVERWRITE_CLEAR),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
