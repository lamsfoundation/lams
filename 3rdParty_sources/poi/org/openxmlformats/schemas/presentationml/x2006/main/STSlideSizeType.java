/*
 * XML Type:  ST_SlideSizeType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_SlideSizeType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType.
 */
public interface STSlideSizeType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stslidesizetypeb7f3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum SCREEN_4_X_3 = Enum.forString("screen4x3");
    Enum LETTER = Enum.forString("letter");
    Enum A_4 = Enum.forString("A4");
    Enum X_35_MM = Enum.forString("35mm");
    Enum OVERHEAD = Enum.forString("overhead");
    Enum BANNER = Enum.forString("banner");
    Enum CUSTOM = Enum.forString("custom");
    Enum LEDGER = Enum.forString("ledger");
    Enum A_3 = Enum.forString("A3");
    Enum B_4_ISO = Enum.forString("B4ISO");
    Enum B_5_ISO = Enum.forString("B5ISO");
    Enum B_4_JIS = Enum.forString("B4JIS");
    Enum B_5_JIS = Enum.forString("B5JIS");
    Enum HAGAKI_CARD = Enum.forString("hagakiCard");
    Enum SCREEN_16_X_9 = Enum.forString("screen16x9");
    Enum SCREEN_16_X_10 = Enum.forString("screen16x10");

    int INT_SCREEN_4_X_3 = Enum.INT_SCREEN_4_X_3;
    int INT_LETTER = Enum.INT_LETTER;
    int INT_A_4 = Enum.INT_A_4;
    int INT_X_35_MM = Enum.INT_X_35_MM;
    int INT_OVERHEAD = Enum.INT_OVERHEAD;
    int INT_BANNER = Enum.INT_BANNER;
    int INT_CUSTOM = Enum.INT_CUSTOM;
    int INT_LEDGER = Enum.INT_LEDGER;
    int INT_A_3 = Enum.INT_A_3;
    int INT_B_4_ISO = Enum.INT_B_4_ISO;
    int INT_B_5_ISO = Enum.INT_B_5_ISO;
    int INT_B_4_JIS = Enum.INT_B_4_JIS;
    int INT_B_5_JIS = Enum.INT_B_5_JIS;
    int INT_HAGAKI_CARD = Enum.INT_HAGAKI_CARD;
    int INT_SCREEN_16_X_9 = Enum.INT_SCREEN_16_X_9;
    int INT_SCREEN_16_X_10 = Enum.INT_SCREEN_16_X_10;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STSlideSizeType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_SCREEN_4_X_3
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

        static final int INT_SCREEN_4_X_3 = 1;
        static final int INT_LETTER = 2;
        static final int INT_A_4 = 3;
        static final int INT_X_35_MM = 4;
        static final int INT_OVERHEAD = 5;
        static final int INT_BANNER = 6;
        static final int INT_CUSTOM = 7;
        static final int INT_LEDGER = 8;
        static final int INT_A_3 = 9;
        static final int INT_B_4_ISO = 10;
        static final int INT_B_5_ISO = 11;
        static final int INT_B_4_JIS = 12;
        static final int INT_B_5_JIS = 13;
        static final int INT_HAGAKI_CARD = 14;
        static final int INT_SCREEN_16_X_9 = 15;
        static final int INT_SCREEN_16_X_10 = 16;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("screen4x3", INT_SCREEN_4_X_3),
            new Enum("letter", INT_LETTER),
            new Enum("A4", INT_A_4),
            new Enum("35mm", INT_X_35_MM),
            new Enum("overhead", INT_OVERHEAD),
            new Enum("banner", INT_BANNER),
            new Enum("custom", INT_CUSTOM),
            new Enum("ledger", INT_LEDGER),
            new Enum("A3", INT_A_3),
            new Enum("B4ISO", INT_B_4_ISO),
            new Enum("B5ISO", INT_B_5_ISO),
            new Enum("B4JIS", INT_B_4_JIS),
            new Enum("B5JIS", INT_B_5_JIS),
            new Enum("hagakiCard", INT_HAGAKI_CARD),
            new Enum("screen16x9", INT_SCREEN_16_X_9),
            new Enum("screen16x10", INT_SCREEN_16_X_10),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
