/*
 * XML Type:  ST_DLblPos
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DLblPos(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.
 */
public interface STDLblPos extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdlblpos1cf4type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum BEST_FIT = Enum.forString("bestFit");
    Enum B = Enum.forString("b");
    Enum CTR = Enum.forString("ctr");
    Enum IN_BASE = Enum.forString("inBase");
    Enum IN_END = Enum.forString("inEnd");
    Enum L = Enum.forString("l");
    Enum OUT_END = Enum.forString("outEnd");
    Enum R = Enum.forString("r");
    Enum T = Enum.forString("t");

    int INT_BEST_FIT = Enum.INT_BEST_FIT;
    int INT_B = Enum.INT_B;
    int INT_CTR = Enum.INT_CTR;
    int INT_IN_BASE = Enum.INT_IN_BASE;
    int INT_IN_END = Enum.INT_IN_END;
    int INT_L = Enum.INT_L;
    int INT_OUT_END = Enum.INT_OUT_END;
    int INT_R = Enum.INT_R;
    int INT_T = Enum.INT_T;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.chart.STDLblPos.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_BEST_FIT
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

        static final int INT_BEST_FIT = 1;
        static final int INT_B = 2;
        static final int INT_CTR = 3;
        static final int INT_IN_BASE = 4;
        static final int INT_IN_END = 5;
        static final int INT_L = 6;
        static final int INT_OUT_END = 7;
        static final int INT_R = 8;
        static final int INT_T = 9;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("bestFit", INT_BEST_FIT),
            new Enum("b", INT_B),
            new Enum("ctr", INT_CTR),
            new Enum("inBase", INT_IN_BASE),
            new Enum("inEnd", INT_IN_END),
            new Enum("l", INT_L),
            new Enum("outEnd", INT_OUT_END),
            new Enum("r", INT_R),
            new Enum("t", INT_T),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
