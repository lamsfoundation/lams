/*
 * XML Type:  ST_HierarchyAlignment
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STHierarchyAlignment
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_HierarchyAlignment(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STHierarchyAlignment.
 */
public interface STHierarchyAlignment extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STHierarchyAlignment> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sthierarchyalignment4373type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum T_L = Enum.forString("tL");
    Enum T_R = Enum.forString("tR");
    Enum T_CTR_CH = Enum.forString("tCtrCh");
    Enum T_CTR_DES = Enum.forString("tCtrDes");
    Enum B_L = Enum.forString("bL");
    Enum B_R = Enum.forString("bR");
    Enum B_CTR_CH = Enum.forString("bCtrCh");
    Enum B_CTR_DES = Enum.forString("bCtrDes");
    Enum L_T = Enum.forString("lT");
    Enum L_B = Enum.forString("lB");
    Enum L_CTR_CH = Enum.forString("lCtrCh");
    Enum L_CTR_DES = Enum.forString("lCtrDes");
    Enum R_T = Enum.forString("rT");
    Enum R_B = Enum.forString("rB");
    Enum R_CTR_CH = Enum.forString("rCtrCh");
    Enum R_CTR_DES = Enum.forString("rCtrDes");

    int INT_T_L = Enum.INT_T_L;
    int INT_T_R = Enum.INT_T_R;
    int INT_T_CTR_CH = Enum.INT_T_CTR_CH;
    int INT_T_CTR_DES = Enum.INT_T_CTR_DES;
    int INT_B_L = Enum.INT_B_L;
    int INT_B_R = Enum.INT_B_R;
    int INT_B_CTR_CH = Enum.INT_B_CTR_CH;
    int INT_B_CTR_DES = Enum.INT_B_CTR_DES;
    int INT_L_T = Enum.INT_L_T;
    int INT_L_B = Enum.INT_L_B;
    int INT_L_CTR_CH = Enum.INT_L_CTR_CH;
    int INT_L_CTR_DES = Enum.INT_L_CTR_DES;
    int INT_R_T = Enum.INT_R_T;
    int INT_R_B = Enum.INT_R_B;
    int INT_R_CTR_CH = Enum.INT_R_CTR_CH;
    int INT_R_CTR_DES = Enum.INT_R_CTR_DES;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STHierarchyAlignment.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_T_L
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

        static final int INT_T_L = 1;
        static final int INT_T_R = 2;
        static final int INT_T_CTR_CH = 3;
        static final int INT_T_CTR_DES = 4;
        static final int INT_B_L = 5;
        static final int INT_B_R = 6;
        static final int INT_B_CTR_CH = 7;
        static final int INT_B_CTR_DES = 8;
        static final int INT_L_T = 9;
        static final int INT_L_B = 10;
        static final int INT_L_CTR_CH = 11;
        static final int INT_L_CTR_DES = 12;
        static final int INT_R_T = 13;
        static final int INT_R_B = 14;
        static final int INT_R_CTR_CH = 15;
        static final int INT_R_CTR_DES = 16;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("tL", INT_T_L),
            new Enum("tR", INT_T_R),
            new Enum("tCtrCh", INT_T_CTR_CH),
            new Enum("tCtrDes", INT_T_CTR_DES),
            new Enum("bL", INT_B_L),
            new Enum("bR", INT_B_R),
            new Enum("bCtrCh", INT_B_CTR_CH),
            new Enum("bCtrDes", INT_B_CTR_DES),
            new Enum("lT", INT_L_T),
            new Enum("lB", INT_L_B),
            new Enum("lCtrCh", INT_L_CTR_CH),
            new Enum("lCtrDes", INT_L_CTR_DES),
            new Enum("rT", INT_R_T),
            new Enum("rB", INT_R_B),
            new Enum("rCtrCh", INT_R_CTR_CH),
            new Enum("rCtrDes", INT_R_CTR_DES),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
