/*
 * XML Type:  ST_TextAutonumberScheme
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TextAutonumberScheme(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.
 */
public interface STTextAutonumberScheme extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttextautonumberschemed675type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum ALPHA_LC_PAREN_BOTH = Enum.forString("alphaLcParenBoth");
    Enum ALPHA_UC_PAREN_BOTH = Enum.forString("alphaUcParenBoth");
    Enum ALPHA_LC_PAREN_R = Enum.forString("alphaLcParenR");
    Enum ALPHA_UC_PAREN_R = Enum.forString("alphaUcParenR");
    Enum ALPHA_LC_PERIOD = Enum.forString("alphaLcPeriod");
    Enum ALPHA_UC_PERIOD = Enum.forString("alphaUcPeriod");
    Enum ARABIC_PAREN_BOTH = Enum.forString("arabicParenBoth");
    Enum ARABIC_PAREN_R = Enum.forString("arabicParenR");
    Enum ARABIC_PERIOD = Enum.forString("arabicPeriod");
    Enum ARABIC_PLAIN = Enum.forString("arabicPlain");
    Enum ROMAN_LC_PAREN_BOTH = Enum.forString("romanLcParenBoth");
    Enum ROMAN_UC_PAREN_BOTH = Enum.forString("romanUcParenBoth");
    Enum ROMAN_LC_PAREN_R = Enum.forString("romanLcParenR");
    Enum ROMAN_UC_PAREN_R = Enum.forString("romanUcParenR");
    Enum ROMAN_LC_PERIOD = Enum.forString("romanLcPeriod");
    Enum ROMAN_UC_PERIOD = Enum.forString("romanUcPeriod");
    Enum CIRCLE_NUM_DB_PLAIN = Enum.forString("circleNumDbPlain");
    Enum CIRCLE_NUM_WD_BLACK_PLAIN = Enum.forString("circleNumWdBlackPlain");
    Enum CIRCLE_NUM_WD_WHITE_PLAIN = Enum.forString("circleNumWdWhitePlain");
    Enum ARABIC_DB_PERIOD = Enum.forString("arabicDbPeriod");
    Enum ARABIC_DB_PLAIN = Enum.forString("arabicDbPlain");
    Enum EA_1_CHS_PERIOD = Enum.forString("ea1ChsPeriod");
    Enum EA_1_CHS_PLAIN = Enum.forString("ea1ChsPlain");
    Enum EA_1_CHT_PERIOD = Enum.forString("ea1ChtPeriod");
    Enum EA_1_CHT_PLAIN = Enum.forString("ea1ChtPlain");
    Enum EA_1_JPN_CHS_DB_PERIOD = Enum.forString("ea1JpnChsDbPeriod");
    Enum EA_1_JPN_KOR_PLAIN = Enum.forString("ea1JpnKorPlain");
    Enum EA_1_JPN_KOR_PERIOD = Enum.forString("ea1JpnKorPeriod");
    Enum ARABIC_1_MINUS = Enum.forString("arabic1Minus");
    Enum ARABIC_2_MINUS = Enum.forString("arabic2Minus");
    Enum HEBREW_2_MINUS = Enum.forString("hebrew2Minus");
    Enum THAI_ALPHA_PERIOD = Enum.forString("thaiAlphaPeriod");
    Enum THAI_ALPHA_PAREN_R = Enum.forString("thaiAlphaParenR");
    Enum THAI_ALPHA_PAREN_BOTH = Enum.forString("thaiAlphaParenBoth");
    Enum THAI_NUM_PERIOD = Enum.forString("thaiNumPeriod");
    Enum THAI_NUM_PAREN_R = Enum.forString("thaiNumParenR");
    Enum THAI_NUM_PAREN_BOTH = Enum.forString("thaiNumParenBoth");
    Enum HINDI_ALPHA_PERIOD = Enum.forString("hindiAlphaPeriod");
    Enum HINDI_NUM_PERIOD = Enum.forString("hindiNumPeriod");
    Enum HINDI_NUM_PAREN_R = Enum.forString("hindiNumParenR");
    Enum HINDI_ALPHA_1_PERIOD = Enum.forString("hindiAlpha1Period");

    int INT_ALPHA_LC_PAREN_BOTH = Enum.INT_ALPHA_LC_PAREN_BOTH;
    int INT_ALPHA_UC_PAREN_BOTH = Enum.INT_ALPHA_UC_PAREN_BOTH;
    int INT_ALPHA_LC_PAREN_R = Enum.INT_ALPHA_LC_PAREN_R;
    int INT_ALPHA_UC_PAREN_R = Enum.INT_ALPHA_UC_PAREN_R;
    int INT_ALPHA_LC_PERIOD = Enum.INT_ALPHA_LC_PERIOD;
    int INT_ALPHA_UC_PERIOD = Enum.INT_ALPHA_UC_PERIOD;
    int INT_ARABIC_PAREN_BOTH = Enum.INT_ARABIC_PAREN_BOTH;
    int INT_ARABIC_PAREN_R = Enum.INT_ARABIC_PAREN_R;
    int INT_ARABIC_PERIOD = Enum.INT_ARABIC_PERIOD;
    int INT_ARABIC_PLAIN = Enum.INT_ARABIC_PLAIN;
    int INT_ROMAN_LC_PAREN_BOTH = Enum.INT_ROMAN_LC_PAREN_BOTH;
    int INT_ROMAN_UC_PAREN_BOTH = Enum.INT_ROMAN_UC_PAREN_BOTH;
    int INT_ROMAN_LC_PAREN_R = Enum.INT_ROMAN_LC_PAREN_R;
    int INT_ROMAN_UC_PAREN_R = Enum.INT_ROMAN_UC_PAREN_R;
    int INT_ROMAN_LC_PERIOD = Enum.INT_ROMAN_LC_PERIOD;
    int INT_ROMAN_UC_PERIOD = Enum.INT_ROMAN_UC_PERIOD;
    int INT_CIRCLE_NUM_DB_PLAIN = Enum.INT_CIRCLE_NUM_DB_PLAIN;
    int INT_CIRCLE_NUM_WD_BLACK_PLAIN = Enum.INT_CIRCLE_NUM_WD_BLACK_PLAIN;
    int INT_CIRCLE_NUM_WD_WHITE_PLAIN = Enum.INT_CIRCLE_NUM_WD_WHITE_PLAIN;
    int INT_ARABIC_DB_PERIOD = Enum.INT_ARABIC_DB_PERIOD;
    int INT_ARABIC_DB_PLAIN = Enum.INT_ARABIC_DB_PLAIN;
    int INT_EA_1_CHS_PERIOD = Enum.INT_EA_1_CHS_PERIOD;
    int INT_EA_1_CHS_PLAIN = Enum.INT_EA_1_CHS_PLAIN;
    int INT_EA_1_CHT_PERIOD = Enum.INT_EA_1_CHT_PERIOD;
    int INT_EA_1_CHT_PLAIN = Enum.INT_EA_1_CHT_PLAIN;
    int INT_EA_1_JPN_CHS_DB_PERIOD = Enum.INT_EA_1_JPN_CHS_DB_PERIOD;
    int INT_EA_1_JPN_KOR_PLAIN = Enum.INT_EA_1_JPN_KOR_PLAIN;
    int INT_EA_1_JPN_KOR_PERIOD = Enum.INT_EA_1_JPN_KOR_PERIOD;
    int INT_ARABIC_1_MINUS = Enum.INT_ARABIC_1_MINUS;
    int INT_ARABIC_2_MINUS = Enum.INT_ARABIC_2_MINUS;
    int INT_HEBREW_2_MINUS = Enum.INT_HEBREW_2_MINUS;
    int INT_THAI_ALPHA_PERIOD = Enum.INT_THAI_ALPHA_PERIOD;
    int INT_THAI_ALPHA_PAREN_R = Enum.INT_THAI_ALPHA_PAREN_R;
    int INT_THAI_ALPHA_PAREN_BOTH = Enum.INT_THAI_ALPHA_PAREN_BOTH;
    int INT_THAI_NUM_PERIOD = Enum.INT_THAI_NUM_PERIOD;
    int INT_THAI_NUM_PAREN_R = Enum.INT_THAI_NUM_PAREN_R;
    int INT_THAI_NUM_PAREN_BOTH = Enum.INT_THAI_NUM_PAREN_BOTH;
    int INT_HINDI_ALPHA_PERIOD = Enum.INT_HINDI_ALPHA_PERIOD;
    int INT_HINDI_NUM_PERIOD = Enum.INT_HINDI_NUM_PERIOD;
    int INT_HINDI_NUM_PAREN_R = Enum.INT_HINDI_NUM_PAREN_R;
    int INT_HINDI_ALPHA_1_PERIOD = Enum.INT_HINDI_ALPHA_1_PERIOD;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STTextAutonumberScheme.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ALPHA_LC_PAREN_BOTH
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

        static final int INT_ALPHA_LC_PAREN_BOTH = 1;
        static final int INT_ALPHA_UC_PAREN_BOTH = 2;
        static final int INT_ALPHA_LC_PAREN_R = 3;
        static final int INT_ALPHA_UC_PAREN_R = 4;
        static final int INT_ALPHA_LC_PERIOD = 5;
        static final int INT_ALPHA_UC_PERIOD = 6;
        static final int INT_ARABIC_PAREN_BOTH = 7;
        static final int INT_ARABIC_PAREN_R = 8;
        static final int INT_ARABIC_PERIOD = 9;
        static final int INT_ARABIC_PLAIN = 10;
        static final int INT_ROMAN_LC_PAREN_BOTH = 11;
        static final int INT_ROMAN_UC_PAREN_BOTH = 12;
        static final int INT_ROMAN_LC_PAREN_R = 13;
        static final int INT_ROMAN_UC_PAREN_R = 14;
        static final int INT_ROMAN_LC_PERIOD = 15;
        static final int INT_ROMAN_UC_PERIOD = 16;
        static final int INT_CIRCLE_NUM_DB_PLAIN = 17;
        static final int INT_CIRCLE_NUM_WD_BLACK_PLAIN = 18;
        static final int INT_CIRCLE_NUM_WD_WHITE_PLAIN = 19;
        static final int INT_ARABIC_DB_PERIOD = 20;
        static final int INT_ARABIC_DB_PLAIN = 21;
        static final int INT_EA_1_CHS_PERIOD = 22;
        static final int INT_EA_1_CHS_PLAIN = 23;
        static final int INT_EA_1_CHT_PERIOD = 24;
        static final int INT_EA_1_CHT_PLAIN = 25;
        static final int INT_EA_1_JPN_CHS_DB_PERIOD = 26;
        static final int INT_EA_1_JPN_KOR_PLAIN = 27;
        static final int INT_EA_1_JPN_KOR_PERIOD = 28;
        static final int INT_ARABIC_1_MINUS = 29;
        static final int INT_ARABIC_2_MINUS = 30;
        static final int INT_HEBREW_2_MINUS = 31;
        static final int INT_THAI_ALPHA_PERIOD = 32;
        static final int INT_THAI_ALPHA_PAREN_R = 33;
        static final int INT_THAI_ALPHA_PAREN_BOTH = 34;
        static final int INT_THAI_NUM_PERIOD = 35;
        static final int INT_THAI_NUM_PAREN_R = 36;
        static final int INT_THAI_NUM_PAREN_BOTH = 37;
        static final int INT_HINDI_ALPHA_PERIOD = 38;
        static final int INT_HINDI_NUM_PERIOD = 39;
        static final int INT_HINDI_NUM_PAREN_R = 40;
        static final int INT_HINDI_ALPHA_1_PERIOD = 41;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("alphaLcParenBoth", INT_ALPHA_LC_PAREN_BOTH),
            new Enum("alphaUcParenBoth", INT_ALPHA_UC_PAREN_BOTH),
            new Enum("alphaLcParenR", INT_ALPHA_LC_PAREN_R),
            new Enum("alphaUcParenR", INT_ALPHA_UC_PAREN_R),
            new Enum("alphaLcPeriod", INT_ALPHA_LC_PERIOD),
            new Enum("alphaUcPeriod", INT_ALPHA_UC_PERIOD),
            new Enum("arabicParenBoth", INT_ARABIC_PAREN_BOTH),
            new Enum("arabicParenR", INT_ARABIC_PAREN_R),
            new Enum("arabicPeriod", INT_ARABIC_PERIOD),
            new Enum("arabicPlain", INT_ARABIC_PLAIN),
            new Enum("romanLcParenBoth", INT_ROMAN_LC_PAREN_BOTH),
            new Enum("romanUcParenBoth", INT_ROMAN_UC_PAREN_BOTH),
            new Enum("romanLcParenR", INT_ROMAN_LC_PAREN_R),
            new Enum("romanUcParenR", INT_ROMAN_UC_PAREN_R),
            new Enum("romanLcPeriod", INT_ROMAN_LC_PERIOD),
            new Enum("romanUcPeriod", INT_ROMAN_UC_PERIOD),
            new Enum("circleNumDbPlain", INT_CIRCLE_NUM_DB_PLAIN),
            new Enum("circleNumWdBlackPlain", INT_CIRCLE_NUM_WD_BLACK_PLAIN),
            new Enum("circleNumWdWhitePlain", INT_CIRCLE_NUM_WD_WHITE_PLAIN),
            new Enum("arabicDbPeriod", INT_ARABIC_DB_PERIOD),
            new Enum("arabicDbPlain", INT_ARABIC_DB_PLAIN),
            new Enum("ea1ChsPeriod", INT_EA_1_CHS_PERIOD),
            new Enum("ea1ChsPlain", INT_EA_1_CHS_PLAIN),
            new Enum("ea1ChtPeriod", INT_EA_1_CHT_PERIOD),
            new Enum("ea1ChtPlain", INT_EA_1_CHT_PLAIN),
            new Enum("ea1JpnChsDbPeriod", INT_EA_1_JPN_CHS_DB_PERIOD),
            new Enum("ea1JpnKorPlain", INT_EA_1_JPN_KOR_PLAIN),
            new Enum("ea1JpnKorPeriod", INT_EA_1_JPN_KOR_PERIOD),
            new Enum("arabic1Minus", INT_ARABIC_1_MINUS),
            new Enum("arabic2Minus", INT_ARABIC_2_MINUS),
            new Enum("hebrew2Minus", INT_HEBREW_2_MINUS),
            new Enum("thaiAlphaPeriod", INT_THAI_ALPHA_PERIOD),
            new Enum("thaiAlphaParenR", INT_THAI_ALPHA_PAREN_R),
            new Enum("thaiAlphaParenBoth", INT_THAI_ALPHA_PAREN_BOTH),
            new Enum("thaiNumPeriod", INT_THAI_NUM_PERIOD),
            new Enum("thaiNumParenR", INT_THAI_NUM_PAREN_R),
            new Enum("thaiNumParenBoth", INT_THAI_NUM_PAREN_BOTH),
            new Enum("hindiAlphaPeriod", INT_HINDI_ALPHA_PERIOD),
            new Enum("hindiNumPeriod", INT_HINDI_NUM_PERIOD),
            new Enum("hindiNumParenR", INT_HINDI_NUM_PAREN_R),
            new Enum("hindiAlpha1Period", INT_HINDI_ALPHA_1_PERIOD),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
