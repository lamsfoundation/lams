/*
 * XML Type:  ST_FormulaExpression
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormulaExpression
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FormulaExpression(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormulaExpression.
 */
public interface STFormulaExpression extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormulaExpression> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stformulaexpressione5adtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum REF = Enum.forString("ref");
    Enum REF_ERROR = Enum.forString("refError");
    Enum AREA = Enum.forString("area");
    Enum AREA_ERROR = Enum.forString("areaError");
    Enum COMPUTED_AREA = Enum.forString("computedArea");

    int INT_REF = Enum.INT_REF;
    int INT_REF_ERROR = Enum.INT_REF_ERROR;
    int INT_AREA = Enum.INT_AREA;
    int INT_AREA_ERROR = Enum.INT_AREA_ERROR;
    int INT_COMPUTED_AREA = Enum.INT_COMPUTED_AREA;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormulaExpression.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_REF
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

        static final int INT_REF = 1;
        static final int INT_REF_ERROR = 2;
        static final int INT_AREA = 3;
        static final int INT_AREA_ERROR = 4;
        static final int INT_COMPUTED_AREA = 5;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("ref", INT_REF),
            new Enum("refError", INT_REF_ERROR),
            new Enum("area", INT_AREA),
            new Enum("areaError", INT_AREA_ERROR),
            new Enum("computedArea", INT_COMPUTED_AREA),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
