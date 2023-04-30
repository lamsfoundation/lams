/*
 * XML Type:  ST_FunctionOperator
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionOperator
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FunctionOperator(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionOperator.
 */
public interface STFunctionOperator extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionOperator> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stfunctionoperator7485type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum EQU = Enum.forString("equ");
    Enum NEQ = Enum.forString("neq");
    Enum GT = Enum.forString("gt");
    Enum LT = Enum.forString("lt");
    Enum GTE = Enum.forString("gte");
    Enum LTE = Enum.forString("lte");

    int INT_EQU = Enum.INT_EQU;
    int INT_NEQ = Enum.INT_NEQ;
    int INT_GT = Enum.INT_GT;
    int INT_LT = Enum.INT_LT;
    int INT_GTE = Enum.INT_GTE;
    int INT_LTE = Enum.INT_LTE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STFunctionOperator.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_EQU
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

        static final int INT_EQU = 1;
        static final int INT_NEQ = 2;
        static final int INT_GT = 3;
        static final int INT_LT = 4;
        static final int INT_GTE = 5;
        static final int INT_LTE = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("equ", INT_EQU),
            new Enum("neq", INT_NEQ),
            new Enum("gt", INT_GT),
            new Enum("lt", INT_LT),
            new Enum("gte", INT_GTE),
            new Enum("lte", INT_LTE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
