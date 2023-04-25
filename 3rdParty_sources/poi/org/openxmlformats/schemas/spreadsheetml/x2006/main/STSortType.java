/*
 * XML Type:  ST_SortType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_SortType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortType.
 */
public interface STSortType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stsorttype2ad1type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum ASCENDING = Enum.forString("ascending");
    Enum DESCENDING = Enum.forString("descending");
    Enum ASCENDING_ALPHA = Enum.forString("ascendingAlpha");
    Enum DESCENDING_ALPHA = Enum.forString("descendingAlpha");
    Enum ASCENDING_NATURAL = Enum.forString("ascendingNatural");
    Enum DESCENDING_NATURAL = Enum.forString("descendingNatural");

    int INT_NONE = Enum.INT_NONE;
    int INT_ASCENDING = Enum.INT_ASCENDING;
    int INT_DESCENDING = Enum.INT_DESCENDING;
    int INT_ASCENDING_ALPHA = Enum.INT_ASCENDING_ALPHA;
    int INT_DESCENDING_ALPHA = Enum.INT_DESCENDING_ALPHA;
    int INT_ASCENDING_NATURAL = Enum.INT_ASCENDING_NATURAL;
    int INT_DESCENDING_NATURAL = Enum.INT_DESCENDING_NATURAL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STSortType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_ASCENDING = 2;
        static final int INT_DESCENDING = 3;
        static final int INT_ASCENDING_ALPHA = 4;
        static final int INT_DESCENDING_ALPHA = 5;
        static final int INT_ASCENDING_NATURAL = 6;
        static final int INT_DESCENDING_NATURAL = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("ascending", INT_ASCENDING),
            new Enum("descending", INT_DESCENDING),
            new Enum("ascendingAlpha", INT_ASCENDING_ALPHA),
            new Enum("descendingAlpha", INT_DESCENDING_ALPHA),
            new Enum("ascendingNatural", INT_ASCENDING_NATURAL),
            new Enum("descendingNatural", INT_DESCENDING_NATURAL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
