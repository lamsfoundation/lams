/*
 * XML Type:  ST_DataConsolidateFunction
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DataConsolidateFunction(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction.
 */
public interface STDataConsolidateFunction extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdataconsolidatefunction1206type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum AVERAGE = Enum.forString("average");
    Enum COUNT = Enum.forString("count");
    Enum COUNT_NUMS = Enum.forString("countNums");
    Enum MAX = Enum.forString("max");
    Enum MIN = Enum.forString("min");
    Enum PRODUCT = Enum.forString("product");
    Enum STD_DEV = Enum.forString("stdDev");
    Enum STD_DEVP = Enum.forString("stdDevp");
    Enum SUM = Enum.forString("sum");
    Enum VAR = Enum.forString("var");
    Enum VARP = Enum.forString("varp");

    int INT_AVERAGE = Enum.INT_AVERAGE;
    int INT_COUNT = Enum.INT_COUNT;
    int INT_COUNT_NUMS = Enum.INT_COUNT_NUMS;
    int INT_MAX = Enum.INT_MAX;
    int INT_MIN = Enum.INT_MIN;
    int INT_PRODUCT = Enum.INT_PRODUCT;
    int INT_STD_DEV = Enum.INT_STD_DEV;
    int INT_STD_DEVP = Enum.INT_STD_DEVP;
    int INT_SUM = Enum.INT_SUM;
    int INT_VAR = Enum.INT_VAR;
    int INT_VARP = Enum.INT_VARP;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataConsolidateFunction.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_AVERAGE
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

        static final int INT_AVERAGE = 1;
        static final int INT_COUNT = 2;
        static final int INT_COUNT_NUMS = 3;
        static final int INT_MAX = 4;
        static final int INT_MIN = 5;
        static final int INT_PRODUCT = 6;
        static final int INT_STD_DEV = 7;
        static final int INT_STD_DEVP = 8;
        static final int INT_SUM = 9;
        static final int INT_VAR = 10;
        static final int INT_VARP = 11;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("average", INT_AVERAGE),
            new Enum("count", INT_COUNT),
            new Enum("countNums", INT_COUNT_NUMS),
            new Enum("max", INT_MAX),
            new Enum("min", INT_MIN),
            new Enum("product", INT_PRODUCT),
            new Enum("stdDev", INT_STD_DEV),
            new Enum("stdDevp", INT_STD_DEVP),
            new Enum("sum", INT_SUM),
            new Enum("var", INT_VAR),
            new Enum("varp", INT_VARP),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
