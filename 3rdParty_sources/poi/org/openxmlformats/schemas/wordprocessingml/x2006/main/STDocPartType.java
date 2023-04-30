/*
 * XML Type:  ST_DocPartType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DocPartType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartType.
 */
public interface STDocPartType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdocparttype191dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum NORMAL = Enum.forString("normal");
    Enum AUTO_EXP = Enum.forString("autoExp");
    Enum TOOLBAR = Enum.forString("toolbar");
    Enum SPELLER = Enum.forString("speller");
    Enum FORM_FLD = Enum.forString("formFld");
    Enum BB_PLC_HDR = Enum.forString("bbPlcHdr");

    int INT_NONE = Enum.INT_NONE;
    int INT_NORMAL = Enum.INT_NORMAL;
    int INT_AUTO_EXP = Enum.INT_AUTO_EXP;
    int INT_TOOLBAR = Enum.INT_TOOLBAR;
    int INT_SPELLER = Enum.INT_SPELLER;
    int INT_FORM_FLD = Enum.INT_FORM_FLD;
    int INT_BB_PLC_HDR = Enum.INT_BB_PLC_HDR;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_NORMAL = 2;
        static final int INT_AUTO_EXP = 3;
        static final int INT_TOOLBAR = 4;
        static final int INT_SPELLER = 5;
        static final int INT_FORM_FLD = 6;
        static final int INT_BB_PLC_HDR = 7;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("normal", INT_NORMAL),
            new Enum("autoExp", INT_AUTO_EXP),
            new Enum("toolbar", INT_TOOLBAR),
            new Enum("speller", INT_SPELLER),
            new Enum("formFld", INT_FORM_FLD),
            new Enum("bbPlcHdr", INT_BB_PLC_HDR),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
