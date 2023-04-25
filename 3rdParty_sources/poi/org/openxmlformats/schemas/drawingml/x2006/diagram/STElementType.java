/*
 * XML Type:  ST_ElementType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STElementType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ElementType(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STElementType.
 */
public interface STElementType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STElementType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stelementtype3655type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum ALL = Enum.forString("all");
    Enum DOC = Enum.forString("doc");
    Enum NODE = Enum.forString("node");
    Enum NORM = Enum.forString("norm");
    Enum NON_NORM = Enum.forString("nonNorm");
    Enum ASST = Enum.forString("asst");
    Enum NON_ASST = Enum.forString("nonAsst");
    Enum PAR_TRANS = Enum.forString("parTrans");
    Enum PRES = Enum.forString("pres");
    Enum SIB_TRANS = Enum.forString("sibTrans");

    int INT_ALL = Enum.INT_ALL;
    int INT_DOC = Enum.INT_DOC;
    int INT_NODE = Enum.INT_NODE;
    int INT_NORM = Enum.INT_NORM;
    int INT_NON_NORM = Enum.INT_NON_NORM;
    int INT_ASST = Enum.INT_ASST;
    int INT_NON_ASST = Enum.INT_NON_ASST;
    int INT_PAR_TRANS = Enum.INT_PAR_TRANS;
    int INT_PRES = Enum.INT_PRES;
    int INT_SIB_TRANS = Enum.INT_SIB_TRANS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STElementType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ALL
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

        static final int INT_ALL = 1;
        static final int INT_DOC = 2;
        static final int INT_NODE = 3;
        static final int INT_NORM = 4;
        static final int INT_NON_NORM = 5;
        static final int INT_ASST = 6;
        static final int INT_NON_ASST = 7;
        static final int INT_PAR_TRANS = 8;
        static final int INT_PRES = 9;
        static final int INT_SIB_TRANS = 10;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("all", INT_ALL),
            new Enum("doc", INT_DOC),
            new Enum("node", INT_NODE),
            new Enum("norm", INT_NORM),
            new Enum("nonNorm", INT_NON_NORM),
            new Enum("asst", INT_ASST),
            new Enum("nonAsst", INT_NON_ASST),
            new Enum("parTrans", INT_PAR_TRANS),
            new Enum("pres", INT_PRES),
            new Enum("sibTrans", INT_SIB_TRANS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
