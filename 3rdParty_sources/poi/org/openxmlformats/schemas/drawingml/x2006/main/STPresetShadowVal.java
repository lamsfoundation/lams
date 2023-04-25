/*
 * XML Type:  ST_PresetShadowVal
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PresetShadowVal(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal.
 */
public interface STPresetShadowVal extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpresetshadowval0e63type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum SHDW_1 = Enum.forString("shdw1");
    Enum SHDW_2 = Enum.forString("shdw2");
    Enum SHDW_3 = Enum.forString("shdw3");
    Enum SHDW_4 = Enum.forString("shdw4");
    Enum SHDW_5 = Enum.forString("shdw5");
    Enum SHDW_6 = Enum.forString("shdw6");
    Enum SHDW_7 = Enum.forString("shdw7");
    Enum SHDW_8 = Enum.forString("shdw8");
    Enum SHDW_9 = Enum.forString("shdw9");
    Enum SHDW_10 = Enum.forString("shdw10");
    Enum SHDW_11 = Enum.forString("shdw11");
    Enum SHDW_12 = Enum.forString("shdw12");
    Enum SHDW_13 = Enum.forString("shdw13");
    Enum SHDW_14 = Enum.forString("shdw14");
    Enum SHDW_15 = Enum.forString("shdw15");
    Enum SHDW_16 = Enum.forString("shdw16");
    Enum SHDW_17 = Enum.forString("shdw17");
    Enum SHDW_18 = Enum.forString("shdw18");
    Enum SHDW_19 = Enum.forString("shdw19");
    Enum SHDW_20 = Enum.forString("shdw20");

    int INT_SHDW_1 = Enum.INT_SHDW_1;
    int INT_SHDW_2 = Enum.INT_SHDW_2;
    int INT_SHDW_3 = Enum.INT_SHDW_3;
    int INT_SHDW_4 = Enum.INT_SHDW_4;
    int INT_SHDW_5 = Enum.INT_SHDW_5;
    int INT_SHDW_6 = Enum.INT_SHDW_6;
    int INT_SHDW_7 = Enum.INT_SHDW_7;
    int INT_SHDW_8 = Enum.INT_SHDW_8;
    int INT_SHDW_9 = Enum.INT_SHDW_9;
    int INT_SHDW_10 = Enum.INT_SHDW_10;
    int INT_SHDW_11 = Enum.INT_SHDW_11;
    int INT_SHDW_12 = Enum.INT_SHDW_12;
    int INT_SHDW_13 = Enum.INT_SHDW_13;
    int INT_SHDW_14 = Enum.INT_SHDW_14;
    int INT_SHDW_15 = Enum.INT_SHDW_15;
    int INT_SHDW_16 = Enum.INT_SHDW_16;
    int INT_SHDW_17 = Enum.INT_SHDW_17;
    int INT_SHDW_18 = Enum.INT_SHDW_18;
    int INT_SHDW_19 = Enum.INT_SHDW_19;
    int INT_SHDW_20 = Enum.INT_SHDW_20;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STPresetShadowVal.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_SHDW_1
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

        static final int INT_SHDW_1 = 1;
        static final int INT_SHDW_2 = 2;
        static final int INT_SHDW_3 = 3;
        static final int INT_SHDW_4 = 4;
        static final int INT_SHDW_5 = 5;
        static final int INT_SHDW_6 = 6;
        static final int INT_SHDW_7 = 7;
        static final int INT_SHDW_8 = 8;
        static final int INT_SHDW_9 = 9;
        static final int INT_SHDW_10 = 10;
        static final int INT_SHDW_11 = 11;
        static final int INT_SHDW_12 = 12;
        static final int INT_SHDW_13 = 13;
        static final int INT_SHDW_14 = 14;
        static final int INT_SHDW_15 = 15;
        static final int INT_SHDW_16 = 16;
        static final int INT_SHDW_17 = 17;
        static final int INT_SHDW_18 = 18;
        static final int INT_SHDW_19 = 19;
        static final int INT_SHDW_20 = 20;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("shdw1", INT_SHDW_1),
            new Enum("shdw2", INT_SHDW_2),
            new Enum("shdw3", INT_SHDW_3),
            new Enum("shdw4", INT_SHDW_4),
            new Enum("shdw5", INT_SHDW_5),
            new Enum("shdw6", INT_SHDW_6),
            new Enum("shdw7", INT_SHDW_7),
            new Enum("shdw8", INT_SHDW_8),
            new Enum("shdw9", INT_SHDW_9),
            new Enum("shdw10", INT_SHDW_10),
            new Enum("shdw11", INT_SHDW_11),
            new Enum("shdw12", INT_SHDW_12),
            new Enum("shdw13", INT_SHDW_13),
            new Enum("shdw14", INT_SHDW_14),
            new Enum("shdw15", INT_SHDW_15),
            new Enum("shdw16", INT_SHDW_16),
            new Enum("shdw17", INT_SHDW_17),
            new Enum("shdw18", INT_SHDW_18),
            new Enum("shdw19", INT_SHDW_19),
            new Enum("shdw20", INT_SHDW_20),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
