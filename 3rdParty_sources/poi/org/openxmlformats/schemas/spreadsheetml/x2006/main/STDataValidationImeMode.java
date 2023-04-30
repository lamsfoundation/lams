/*
 * XML Type:  ST_DataValidationImeMode
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DataValidationImeMode(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode.
 */
public interface STDataValidationImeMode extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdatavalidationimemodef990type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NO_CONTROL = Enum.forString("noControl");
    Enum OFF = Enum.forString("off");
    Enum ON = Enum.forString("on");
    Enum DISABLED = Enum.forString("disabled");
    Enum HIRAGANA = Enum.forString("hiragana");
    Enum FULL_KATAKANA = Enum.forString("fullKatakana");
    Enum HALF_KATAKANA = Enum.forString("halfKatakana");
    Enum FULL_ALPHA = Enum.forString("fullAlpha");
    Enum HALF_ALPHA = Enum.forString("halfAlpha");
    Enum FULL_HANGUL = Enum.forString("fullHangul");
    Enum HALF_HANGUL = Enum.forString("halfHangul");

    int INT_NO_CONTROL = Enum.INT_NO_CONTROL;
    int INT_OFF = Enum.INT_OFF;
    int INT_ON = Enum.INT_ON;
    int INT_DISABLED = Enum.INT_DISABLED;
    int INT_HIRAGANA = Enum.INT_HIRAGANA;
    int INT_FULL_KATAKANA = Enum.INT_FULL_KATAKANA;
    int INT_HALF_KATAKANA = Enum.INT_HALF_KATAKANA;
    int INT_FULL_ALPHA = Enum.INT_FULL_ALPHA;
    int INT_HALF_ALPHA = Enum.INT_HALF_ALPHA;
    int INT_FULL_HANGUL = Enum.INT_FULL_HANGUL;
    int INT_HALF_HANGUL = Enum.INT_HALF_HANGUL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STDataValidationImeMode.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NO_CONTROL
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

        static final int INT_NO_CONTROL = 1;
        static final int INT_OFF = 2;
        static final int INT_ON = 3;
        static final int INT_DISABLED = 4;
        static final int INT_HIRAGANA = 5;
        static final int INT_FULL_KATAKANA = 6;
        static final int INT_HALF_KATAKANA = 7;
        static final int INT_FULL_ALPHA = 8;
        static final int INT_HALF_ALPHA = 9;
        static final int INT_FULL_HANGUL = 10;
        static final int INT_HALF_HANGUL = 11;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("noControl", INT_NO_CONTROL),
            new Enum("off", INT_OFF),
            new Enum("on", INT_ON),
            new Enum("disabled", INT_DISABLED),
            new Enum("hiragana", INT_HIRAGANA),
            new Enum("fullKatakana", INT_FULL_KATAKANA),
            new Enum("halfKatakana", INT_HALF_KATAKANA),
            new Enum("fullAlpha", INT_FULL_ALPHA),
            new Enum("halfAlpha", INT_HALF_ALPHA),
            new Enum("fullHangul", INT_FULL_HANGUL),
            new Enum("halfHangul", INT_HALF_HANGUL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
