/*
 * XML Type:  ST_CfvoType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_CfvoType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.
 */
public interface STCfvoType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcfvotypeeb0ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NUM = Enum.forString("num");
    Enum PERCENT = Enum.forString("percent");
    Enum MAX = Enum.forString("max");
    Enum MIN = Enum.forString("min");
    Enum FORMULA = Enum.forString("formula");
    Enum PERCENTILE = Enum.forString("percentile");

    int INT_NUM = Enum.INT_NUM;
    int INT_PERCENT = Enum.INT_PERCENT;
    int INT_MAX = Enum.INT_MAX;
    int INT_MIN = Enum.INT_MIN;
    int INT_FORMULA = Enum.INT_FORMULA;
    int INT_PERCENTILE = Enum.INT_PERCENTILE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NUM
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

        static final int INT_NUM = 1;
        static final int INT_PERCENT = 2;
        static final int INT_MAX = 3;
        static final int INT_MIN = 4;
        static final int INT_FORMULA = 5;
        static final int INT_PERCENTILE = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("num", INT_NUM),
            new Enum("percent", INT_PERCENT),
            new Enum("max", INT_MAX),
            new Enum("min", INT_MIN),
            new Enum("formula", INT_FORMULA),
            new Enum("percentile", INT_PERCENTILE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
