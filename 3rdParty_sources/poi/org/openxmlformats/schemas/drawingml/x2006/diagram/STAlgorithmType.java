/*
 * XML Type:  ST_AlgorithmType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_AlgorithmType(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType.
 */
public interface STAlgorithmType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stalgorithmtype8c82type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum COMPOSITE = Enum.forString("composite");
    Enum CONN = Enum.forString("conn");
    Enum CYCLE = Enum.forString("cycle");
    Enum HIER_CHILD = Enum.forString("hierChild");
    Enum HIER_ROOT = Enum.forString("hierRoot");
    Enum PYRA = Enum.forString("pyra");
    Enum LIN = Enum.forString("lin");
    Enum SP = Enum.forString("sp");
    Enum TX = Enum.forString("tx");
    Enum SNAKE = Enum.forString("snake");

    int INT_COMPOSITE = Enum.INT_COMPOSITE;
    int INT_CONN = Enum.INT_CONN;
    int INT_CYCLE = Enum.INT_CYCLE;
    int INT_HIER_CHILD = Enum.INT_HIER_CHILD;
    int INT_HIER_ROOT = Enum.INT_HIER_ROOT;
    int INT_PYRA = Enum.INT_PYRA;
    int INT_LIN = Enum.INT_LIN;
    int INT_SP = Enum.INT_SP;
    int INT_TX = Enum.INT_TX;
    int INT_SNAKE = Enum.INT_SNAKE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STAlgorithmType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_COMPOSITE
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

        static final int INT_COMPOSITE = 1;
        static final int INT_CONN = 2;
        static final int INT_CYCLE = 3;
        static final int INT_HIER_CHILD = 4;
        static final int INT_HIER_ROOT = 5;
        static final int INT_PYRA = 6;
        static final int INT_LIN = 7;
        static final int INT_SP = 8;
        static final int INT_TX = 9;
        static final int INT_SNAKE = 10;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("composite", INT_COMPOSITE),
            new Enum("conn", INT_CONN),
            new Enum("cycle", INT_CYCLE),
            new Enum("hierChild", INT_HIER_CHILD),
            new Enum("hierRoot", INT_HIER_ROOT),
            new Enum("pyra", INT_PYRA),
            new Enum("lin", INT_LIN),
            new Enum("sp", INT_SP),
            new Enum("tx", INT_TX),
            new Enum("snake", INT_SNAKE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
