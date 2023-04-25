/*
 * XML Type:  ST_TextDirection
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextDirection(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection.
 */
public interface STTextDirection extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextdirectionf150type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TB = Enum.forString("tb");
    Enum RL = Enum.forString("rl");
    Enum LR = Enum.forString("lr");
    Enum TB_V = Enum.forString("tbV");
    Enum RL_V = Enum.forString("rlV");
    Enum LR_V = Enum.forString("lrV");
    Enum BT_LR = Enum.forString("btLr");
    Enum LR_TB = Enum.forString("lrTb");
    Enum LR_TB_V = Enum.forString("lrTbV");
    Enum TB_LR_V = Enum.forString("tbLrV");
    Enum TB_RL = Enum.forString("tbRl");
    Enum TB_RL_V = Enum.forString("tbRlV");

    int INT_TB = Enum.INT_TB;
    int INT_RL = Enum.INT_RL;
    int INT_LR = Enum.INT_LR;
    int INT_TB_V = Enum.INT_TB_V;
    int INT_RL_V = Enum.INT_RL_V;
    int INT_LR_V = Enum.INT_LR_V;
    int INT_BT_LR = Enum.INT_BT_LR;
    int INT_LR_TB = Enum.INT_LR_TB;
    int INT_LR_TB_V = Enum.INT_LR_TB_V;
    int INT_TB_LR_V = Enum.INT_TB_LR_V;
    int INT_TB_RL = Enum.INT_TB_RL;
    int INT_TB_RL_V = Enum.INT_TB_RL_V;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STTextDirection.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TB
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

        static final int INT_TB = 1;
        static final int INT_RL = 2;
        static final int INT_LR = 3;
        static final int INT_TB_V = 4;
        static final int INT_RL_V = 5;
        static final int INT_LR_V = 6;
        static final int INT_BT_LR = 7;
        static final int INT_LR_TB = 8;
        static final int INT_LR_TB_V = 9;
        static final int INT_TB_LR_V = 10;
        static final int INT_TB_RL = 11;
        static final int INT_TB_RL_V = 12;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("tb", INT_TB),
            new Enum("rl", INT_RL),
            new Enum("lr", INT_LR),
            new Enum("tbV", INT_TB_V),
            new Enum("rlV", INT_RL_V),
            new Enum("lrV", INT_LR_V),
            new Enum("btLr", INT_BT_LR),
            new Enum("lrTb", INT_LR_TB),
            new Enum("lrTbV", INT_LR_TB_V),
            new Enum("tbLrV", INT_TB_LR_V),
            new Enum("tbRl", INT_TB_RL),
            new Enum("tbRlV", INT_TB_RL_V),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
