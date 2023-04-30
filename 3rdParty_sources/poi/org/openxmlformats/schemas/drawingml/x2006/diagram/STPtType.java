/*
 * XML Type:  ST_PtType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STPtType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PtType(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STPtType.
 */
public interface STPtType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STPtType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpttype6dc3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NODE = Enum.forString("node");
    Enum ASST = Enum.forString("asst");
    Enum DOC = Enum.forString("doc");
    Enum PRES = Enum.forString("pres");
    Enum PAR_TRANS = Enum.forString("parTrans");
    Enum SIB_TRANS = Enum.forString("sibTrans");

    int INT_NODE = Enum.INT_NODE;
    int INT_ASST = Enum.INT_ASST;
    int INT_DOC = Enum.INT_DOC;
    int INT_PRES = Enum.INT_PRES;
    int INT_PAR_TRANS = Enum.INT_PAR_TRANS;
    int INT_SIB_TRANS = Enum.INT_SIB_TRANS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STPtType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NODE
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

        static final int INT_NODE = 1;
        static final int INT_ASST = 2;
        static final int INT_DOC = 3;
        static final int INT_PRES = 4;
        static final int INT_PAR_TRANS = 5;
        static final int INT_SIB_TRANS = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("node", INT_NODE),
            new Enum("asst", INT_ASST),
            new Enum("doc", INT_DOC),
            new Enum("pres", INT_PRES),
            new Enum("parTrans", INT_PAR_TRANS),
            new Enum("sibTrans", INT_SIB_TRANS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
