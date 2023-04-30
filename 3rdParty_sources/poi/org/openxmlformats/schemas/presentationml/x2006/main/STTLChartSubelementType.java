/*
 * XML Type:  ST_TLChartSubelementType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TLChartSubelementType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType.
 */
public interface STTLChartSubelementType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttlchartsubelementtype3883type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum GRID_LEGEND = Enum.forString("gridLegend");
    Enum SERIES = Enum.forString("series");
    Enum CATEGORY = Enum.forString("category");
    Enum PT_IN_SERIES = Enum.forString("ptInSeries");
    Enum PT_IN_CATEGORY = Enum.forString("ptInCategory");

    int INT_GRID_LEGEND = Enum.INT_GRID_LEGEND;
    int INT_SERIES = Enum.INT_SERIES;
    int INT_CATEGORY = Enum.INT_CATEGORY;
    int INT_PT_IN_SERIES = Enum.INT_PT_IN_SERIES;
    int INT_PT_IN_CATEGORY = Enum.INT_PT_IN_CATEGORY;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STTLChartSubelementType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_GRID_LEGEND
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

        static final int INT_GRID_LEGEND = 1;
        static final int INT_SERIES = 2;
        static final int INT_CATEGORY = 3;
        static final int INT_PT_IN_SERIES = 4;
        static final int INT_PT_IN_CATEGORY = 5;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("gridLegend", INT_GRID_LEGEND),
            new Enum("series", INT_SERIES),
            new Enum("category", INT_CATEGORY),
            new Enum("ptInSeries", INT_PT_IN_SERIES),
            new Enum("ptInCategory", INT_PT_IN_CATEGORY),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
