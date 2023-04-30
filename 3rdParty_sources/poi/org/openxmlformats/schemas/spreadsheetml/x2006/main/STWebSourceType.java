/*
 * XML Type:  ST_WebSourceType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_WebSourceType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType.
 */
public interface STWebSourceType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stwebsourcetypecb38type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum SHEET = Enum.forString("sheet");
    Enum PRINT_AREA = Enum.forString("printArea");
    Enum AUTO_FILTER = Enum.forString("autoFilter");
    Enum RANGE = Enum.forString("range");
    Enum CHART = Enum.forString("chart");
    Enum PIVOT_TABLE = Enum.forString("pivotTable");
    Enum QUERY = Enum.forString("query");
    Enum LABEL = Enum.forString("label");

    int INT_SHEET = Enum.INT_SHEET;
    int INT_PRINT_AREA = Enum.INT_PRINT_AREA;
    int INT_AUTO_FILTER = Enum.INT_AUTO_FILTER;
    int INT_RANGE = Enum.INT_RANGE;
    int INT_CHART = Enum.INT_CHART;
    int INT_PIVOT_TABLE = Enum.INT_PIVOT_TABLE;
    int INT_QUERY = Enum.INT_QUERY;
    int INT_LABEL = Enum.INT_LABEL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_SHEET
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

        static final int INT_SHEET = 1;
        static final int INT_PRINT_AREA = 2;
        static final int INT_AUTO_FILTER = 3;
        static final int INT_RANGE = 4;
        static final int INT_CHART = 5;
        static final int INT_PIVOT_TABLE = 6;
        static final int INT_QUERY = 7;
        static final int INT_LABEL = 8;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("sheet", INT_SHEET),
            new Enum("printArea", INT_PRINT_AREA),
            new Enum("autoFilter", INT_AUTO_FILTER),
            new Enum("range", INT_RANGE),
            new Enum("chart", INT_CHART),
            new Enum("pivotTable", INT_PIVOT_TABLE),
            new Enum("query", INT_QUERY),
            new Enum("label", INT_LABEL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
