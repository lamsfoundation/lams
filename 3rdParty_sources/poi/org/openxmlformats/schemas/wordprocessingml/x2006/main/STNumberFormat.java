/*
 * XML Type:  ST_NumberFormat
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_NumberFormat(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.
 */
public interface STNumberFormat extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stnumberformat0fb8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum DECIMAL = Enum.forString("decimal");
    Enum UPPER_ROMAN = Enum.forString("upperRoman");
    Enum LOWER_ROMAN = Enum.forString("lowerRoman");
    Enum UPPER_LETTER = Enum.forString("upperLetter");
    Enum LOWER_LETTER = Enum.forString("lowerLetter");
    Enum ORDINAL = Enum.forString("ordinal");
    Enum CARDINAL_TEXT = Enum.forString("cardinalText");
    Enum ORDINAL_TEXT = Enum.forString("ordinalText");
    Enum HEX = Enum.forString("hex");
    Enum CHICAGO = Enum.forString("chicago");
    Enum IDEOGRAPH_DIGITAL = Enum.forString("ideographDigital");
    Enum JAPANESE_COUNTING = Enum.forString("japaneseCounting");
    Enum AIUEO = Enum.forString("aiueo");
    Enum IROHA = Enum.forString("iroha");
    Enum DECIMAL_FULL_WIDTH = Enum.forString("decimalFullWidth");
    Enum DECIMAL_HALF_WIDTH = Enum.forString("decimalHalfWidth");
    Enum JAPANESE_LEGAL = Enum.forString("japaneseLegal");
    Enum JAPANESE_DIGITAL_TEN_THOUSAND = Enum.forString("japaneseDigitalTenThousand");
    Enum DECIMAL_ENCLOSED_CIRCLE = Enum.forString("decimalEnclosedCircle");
    Enum DECIMAL_FULL_WIDTH_2 = Enum.forString("decimalFullWidth2");
    Enum AIUEO_FULL_WIDTH = Enum.forString("aiueoFullWidth");
    Enum IROHA_FULL_WIDTH = Enum.forString("irohaFullWidth");
    Enum DECIMAL_ZERO = Enum.forString("decimalZero");
    Enum BULLET = Enum.forString("bullet");
    Enum GANADA = Enum.forString("ganada");
    Enum CHOSUNG = Enum.forString("chosung");
    Enum DECIMAL_ENCLOSED_FULLSTOP = Enum.forString("decimalEnclosedFullstop");
    Enum DECIMAL_ENCLOSED_PAREN = Enum.forString("decimalEnclosedParen");
    Enum DECIMAL_ENCLOSED_CIRCLE_CHINESE = Enum.forString("decimalEnclosedCircleChinese");
    Enum IDEOGRAPH_ENCLOSED_CIRCLE = Enum.forString("ideographEnclosedCircle");
    Enum IDEOGRAPH_TRADITIONAL = Enum.forString("ideographTraditional");
    Enum IDEOGRAPH_ZODIAC = Enum.forString("ideographZodiac");
    Enum IDEOGRAPH_ZODIAC_TRADITIONAL = Enum.forString("ideographZodiacTraditional");
    Enum TAIWANESE_COUNTING = Enum.forString("taiwaneseCounting");
    Enum IDEOGRAPH_LEGAL_TRADITIONAL = Enum.forString("ideographLegalTraditional");
    Enum TAIWANESE_COUNTING_THOUSAND = Enum.forString("taiwaneseCountingThousand");
    Enum TAIWANESE_DIGITAL = Enum.forString("taiwaneseDigital");
    Enum CHINESE_COUNTING = Enum.forString("chineseCounting");
    Enum CHINESE_LEGAL_SIMPLIFIED = Enum.forString("chineseLegalSimplified");
    Enum CHINESE_COUNTING_THOUSAND = Enum.forString("chineseCountingThousand");
    Enum KOREAN_DIGITAL = Enum.forString("koreanDigital");
    Enum KOREAN_COUNTING = Enum.forString("koreanCounting");
    Enum KOREAN_LEGAL = Enum.forString("koreanLegal");
    Enum KOREAN_DIGITAL_2 = Enum.forString("koreanDigital2");
    Enum VIETNAMESE_COUNTING = Enum.forString("vietnameseCounting");
    Enum RUSSIAN_LOWER = Enum.forString("russianLower");
    Enum RUSSIAN_UPPER = Enum.forString("russianUpper");
    Enum NONE = Enum.forString("none");
    Enum NUMBER_IN_DASH = Enum.forString("numberInDash");
    Enum HEBREW_1 = Enum.forString("hebrew1");
    Enum HEBREW_2 = Enum.forString("hebrew2");
    Enum ARABIC_ALPHA = Enum.forString("arabicAlpha");
    Enum ARABIC_ABJAD = Enum.forString("arabicAbjad");
    Enum HINDI_VOWELS = Enum.forString("hindiVowels");
    Enum HINDI_CONSONANTS = Enum.forString("hindiConsonants");
    Enum HINDI_NUMBERS = Enum.forString("hindiNumbers");
    Enum HINDI_COUNTING = Enum.forString("hindiCounting");
    Enum THAI_LETTERS = Enum.forString("thaiLetters");
    Enum THAI_NUMBERS = Enum.forString("thaiNumbers");
    Enum THAI_COUNTING = Enum.forString("thaiCounting");
    Enum BAHT_TEXT = Enum.forString("bahtText");
    Enum DOLLAR_TEXT = Enum.forString("dollarText");
    Enum CUSTOM = Enum.forString("custom");

    int INT_DECIMAL = Enum.INT_DECIMAL;
    int INT_UPPER_ROMAN = Enum.INT_UPPER_ROMAN;
    int INT_LOWER_ROMAN = Enum.INT_LOWER_ROMAN;
    int INT_UPPER_LETTER = Enum.INT_UPPER_LETTER;
    int INT_LOWER_LETTER = Enum.INT_LOWER_LETTER;
    int INT_ORDINAL = Enum.INT_ORDINAL;
    int INT_CARDINAL_TEXT = Enum.INT_CARDINAL_TEXT;
    int INT_ORDINAL_TEXT = Enum.INT_ORDINAL_TEXT;
    int INT_HEX = Enum.INT_HEX;
    int INT_CHICAGO = Enum.INT_CHICAGO;
    int INT_IDEOGRAPH_DIGITAL = Enum.INT_IDEOGRAPH_DIGITAL;
    int INT_JAPANESE_COUNTING = Enum.INT_JAPANESE_COUNTING;
    int INT_AIUEO = Enum.INT_AIUEO;
    int INT_IROHA = Enum.INT_IROHA;
    int INT_DECIMAL_FULL_WIDTH = Enum.INT_DECIMAL_FULL_WIDTH;
    int INT_DECIMAL_HALF_WIDTH = Enum.INT_DECIMAL_HALF_WIDTH;
    int INT_JAPANESE_LEGAL = Enum.INT_JAPANESE_LEGAL;
    int INT_JAPANESE_DIGITAL_TEN_THOUSAND = Enum.INT_JAPANESE_DIGITAL_TEN_THOUSAND;
    int INT_DECIMAL_ENCLOSED_CIRCLE = Enum.INT_DECIMAL_ENCLOSED_CIRCLE;
    int INT_DECIMAL_FULL_WIDTH_2 = Enum.INT_DECIMAL_FULL_WIDTH_2;
    int INT_AIUEO_FULL_WIDTH = Enum.INT_AIUEO_FULL_WIDTH;
    int INT_IROHA_FULL_WIDTH = Enum.INT_IROHA_FULL_WIDTH;
    int INT_DECIMAL_ZERO = Enum.INT_DECIMAL_ZERO;
    int INT_BULLET = Enum.INT_BULLET;
    int INT_GANADA = Enum.INT_GANADA;
    int INT_CHOSUNG = Enum.INT_CHOSUNG;
    int INT_DECIMAL_ENCLOSED_FULLSTOP = Enum.INT_DECIMAL_ENCLOSED_FULLSTOP;
    int INT_DECIMAL_ENCLOSED_PAREN = Enum.INT_DECIMAL_ENCLOSED_PAREN;
    int INT_DECIMAL_ENCLOSED_CIRCLE_CHINESE = Enum.INT_DECIMAL_ENCLOSED_CIRCLE_CHINESE;
    int INT_IDEOGRAPH_ENCLOSED_CIRCLE = Enum.INT_IDEOGRAPH_ENCLOSED_CIRCLE;
    int INT_IDEOGRAPH_TRADITIONAL = Enum.INT_IDEOGRAPH_TRADITIONAL;
    int INT_IDEOGRAPH_ZODIAC = Enum.INT_IDEOGRAPH_ZODIAC;
    int INT_IDEOGRAPH_ZODIAC_TRADITIONAL = Enum.INT_IDEOGRAPH_ZODIAC_TRADITIONAL;
    int INT_TAIWANESE_COUNTING = Enum.INT_TAIWANESE_COUNTING;
    int INT_IDEOGRAPH_LEGAL_TRADITIONAL = Enum.INT_IDEOGRAPH_LEGAL_TRADITIONAL;
    int INT_TAIWANESE_COUNTING_THOUSAND = Enum.INT_TAIWANESE_COUNTING_THOUSAND;
    int INT_TAIWANESE_DIGITAL = Enum.INT_TAIWANESE_DIGITAL;
    int INT_CHINESE_COUNTING = Enum.INT_CHINESE_COUNTING;
    int INT_CHINESE_LEGAL_SIMPLIFIED = Enum.INT_CHINESE_LEGAL_SIMPLIFIED;
    int INT_CHINESE_COUNTING_THOUSAND = Enum.INT_CHINESE_COUNTING_THOUSAND;
    int INT_KOREAN_DIGITAL = Enum.INT_KOREAN_DIGITAL;
    int INT_KOREAN_COUNTING = Enum.INT_KOREAN_COUNTING;
    int INT_KOREAN_LEGAL = Enum.INT_KOREAN_LEGAL;
    int INT_KOREAN_DIGITAL_2 = Enum.INT_KOREAN_DIGITAL_2;
    int INT_VIETNAMESE_COUNTING = Enum.INT_VIETNAMESE_COUNTING;
    int INT_RUSSIAN_LOWER = Enum.INT_RUSSIAN_LOWER;
    int INT_RUSSIAN_UPPER = Enum.INT_RUSSIAN_UPPER;
    int INT_NONE = Enum.INT_NONE;
    int INT_NUMBER_IN_DASH = Enum.INT_NUMBER_IN_DASH;
    int INT_HEBREW_1 = Enum.INT_HEBREW_1;
    int INT_HEBREW_2 = Enum.INT_HEBREW_2;
    int INT_ARABIC_ALPHA = Enum.INT_ARABIC_ALPHA;
    int INT_ARABIC_ABJAD = Enum.INT_ARABIC_ABJAD;
    int INT_HINDI_VOWELS = Enum.INT_HINDI_VOWELS;
    int INT_HINDI_CONSONANTS = Enum.INT_HINDI_CONSONANTS;
    int INT_HINDI_NUMBERS = Enum.INT_HINDI_NUMBERS;
    int INT_HINDI_COUNTING = Enum.INT_HINDI_COUNTING;
    int INT_THAI_LETTERS = Enum.INT_THAI_LETTERS;
    int INT_THAI_NUMBERS = Enum.INT_THAI_NUMBERS;
    int INT_THAI_COUNTING = Enum.INT_THAI_COUNTING;
    int INT_BAHT_TEXT = Enum.INT_BAHT_TEXT;
    int INT_DOLLAR_TEXT = Enum.INT_DOLLAR_TEXT;
    int INT_CUSTOM = Enum.INT_CUSTOM;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STNumberFormat.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_DECIMAL
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

        static final int INT_DECIMAL = 1;
        static final int INT_UPPER_ROMAN = 2;
        static final int INT_LOWER_ROMAN = 3;
        static final int INT_UPPER_LETTER = 4;
        static final int INT_LOWER_LETTER = 5;
        static final int INT_ORDINAL = 6;
        static final int INT_CARDINAL_TEXT = 7;
        static final int INT_ORDINAL_TEXT = 8;
        static final int INT_HEX = 9;
        static final int INT_CHICAGO = 10;
        static final int INT_IDEOGRAPH_DIGITAL = 11;
        static final int INT_JAPANESE_COUNTING = 12;
        static final int INT_AIUEO = 13;
        static final int INT_IROHA = 14;
        static final int INT_DECIMAL_FULL_WIDTH = 15;
        static final int INT_DECIMAL_HALF_WIDTH = 16;
        static final int INT_JAPANESE_LEGAL = 17;
        static final int INT_JAPANESE_DIGITAL_TEN_THOUSAND = 18;
        static final int INT_DECIMAL_ENCLOSED_CIRCLE = 19;
        static final int INT_DECIMAL_FULL_WIDTH_2 = 20;
        static final int INT_AIUEO_FULL_WIDTH = 21;
        static final int INT_IROHA_FULL_WIDTH = 22;
        static final int INT_DECIMAL_ZERO = 23;
        static final int INT_BULLET = 24;
        static final int INT_GANADA = 25;
        static final int INT_CHOSUNG = 26;
        static final int INT_DECIMAL_ENCLOSED_FULLSTOP = 27;
        static final int INT_DECIMAL_ENCLOSED_PAREN = 28;
        static final int INT_DECIMAL_ENCLOSED_CIRCLE_CHINESE = 29;
        static final int INT_IDEOGRAPH_ENCLOSED_CIRCLE = 30;
        static final int INT_IDEOGRAPH_TRADITIONAL = 31;
        static final int INT_IDEOGRAPH_ZODIAC = 32;
        static final int INT_IDEOGRAPH_ZODIAC_TRADITIONAL = 33;
        static final int INT_TAIWANESE_COUNTING = 34;
        static final int INT_IDEOGRAPH_LEGAL_TRADITIONAL = 35;
        static final int INT_TAIWANESE_COUNTING_THOUSAND = 36;
        static final int INT_TAIWANESE_DIGITAL = 37;
        static final int INT_CHINESE_COUNTING = 38;
        static final int INT_CHINESE_LEGAL_SIMPLIFIED = 39;
        static final int INT_CHINESE_COUNTING_THOUSAND = 40;
        static final int INT_KOREAN_DIGITAL = 41;
        static final int INT_KOREAN_COUNTING = 42;
        static final int INT_KOREAN_LEGAL = 43;
        static final int INT_KOREAN_DIGITAL_2 = 44;
        static final int INT_VIETNAMESE_COUNTING = 45;
        static final int INT_RUSSIAN_LOWER = 46;
        static final int INT_RUSSIAN_UPPER = 47;
        static final int INT_NONE = 48;
        static final int INT_NUMBER_IN_DASH = 49;
        static final int INT_HEBREW_1 = 50;
        static final int INT_HEBREW_2 = 51;
        static final int INT_ARABIC_ALPHA = 52;
        static final int INT_ARABIC_ABJAD = 53;
        static final int INT_HINDI_VOWELS = 54;
        static final int INT_HINDI_CONSONANTS = 55;
        static final int INT_HINDI_NUMBERS = 56;
        static final int INT_HINDI_COUNTING = 57;
        static final int INT_THAI_LETTERS = 58;
        static final int INT_THAI_NUMBERS = 59;
        static final int INT_THAI_COUNTING = 60;
        static final int INT_BAHT_TEXT = 61;
        static final int INT_DOLLAR_TEXT = 62;
        static final int INT_CUSTOM = 63;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("decimal", INT_DECIMAL),
            new Enum("upperRoman", INT_UPPER_ROMAN),
            new Enum("lowerRoman", INT_LOWER_ROMAN),
            new Enum("upperLetter", INT_UPPER_LETTER),
            new Enum("lowerLetter", INT_LOWER_LETTER),
            new Enum("ordinal", INT_ORDINAL),
            new Enum("cardinalText", INT_CARDINAL_TEXT),
            new Enum("ordinalText", INT_ORDINAL_TEXT),
            new Enum("hex", INT_HEX),
            new Enum("chicago", INT_CHICAGO),
            new Enum("ideographDigital", INT_IDEOGRAPH_DIGITAL),
            new Enum("japaneseCounting", INT_JAPANESE_COUNTING),
            new Enum("aiueo", INT_AIUEO),
            new Enum("iroha", INT_IROHA),
            new Enum("decimalFullWidth", INT_DECIMAL_FULL_WIDTH),
            new Enum("decimalHalfWidth", INT_DECIMAL_HALF_WIDTH),
            new Enum("japaneseLegal", INT_JAPANESE_LEGAL),
            new Enum("japaneseDigitalTenThousand", INT_JAPANESE_DIGITAL_TEN_THOUSAND),
            new Enum("decimalEnclosedCircle", INT_DECIMAL_ENCLOSED_CIRCLE),
            new Enum("decimalFullWidth2", INT_DECIMAL_FULL_WIDTH_2),
            new Enum("aiueoFullWidth", INT_AIUEO_FULL_WIDTH),
            new Enum("irohaFullWidth", INT_IROHA_FULL_WIDTH),
            new Enum("decimalZero", INT_DECIMAL_ZERO),
            new Enum("bullet", INT_BULLET),
            new Enum("ganada", INT_GANADA),
            new Enum("chosung", INT_CHOSUNG),
            new Enum("decimalEnclosedFullstop", INT_DECIMAL_ENCLOSED_FULLSTOP),
            new Enum("decimalEnclosedParen", INT_DECIMAL_ENCLOSED_PAREN),
            new Enum("decimalEnclosedCircleChinese", INT_DECIMAL_ENCLOSED_CIRCLE_CHINESE),
            new Enum("ideographEnclosedCircle", INT_IDEOGRAPH_ENCLOSED_CIRCLE),
            new Enum("ideographTraditional", INT_IDEOGRAPH_TRADITIONAL),
            new Enum("ideographZodiac", INT_IDEOGRAPH_ZODIAC),
            new Enum("ideographZodiacTraditional", INT_IDEOGRAPH_ZODIAC_TRADITIONAL),
            new Enum("taiwaneseCounting", INT_TAIWANESE_COUNTING),
            new Enum("ideographLegalTraditional", INT_IDEOGRAPH_LEGAL_TRADITIONAL),
            new Enum("taiwaneseCountingThousand", INT_TAIWANESE_COUNTING_THOUSAND),
            new Enum("taiwaneseDigital", INT_TAIWANESE_DIGITAL),
            new Enum("chineseCounting", INT_CHINESE_COUNTING),
            new Enum("chineseLegalSimplified", INT_CHINESE_LEGAL_SIMPLIFIED),
            new Enum("chineseCountingThousand", INT_CHINESE_COUNTING_THOUSAND),
            new Enum("koreanDigital", INT_KOREAN_DIGITAL),
            new Enum("koreanCounting", INT_KOREAN_COUNTING),
            new Enum("koreanLegal", INT_KOREAN_LEGAL),
            new Enum("koreanDigital2", INT_KOREAN_DIGITAL_2),
            new Enum("vietnameseCounting", INT_VIETNAMESE_COUNTING),
            new Enum("russianLower", INT_RUSSIAN_LOWER),
            new Enum("russianUpper", INT_RUSSIAN_UPPER),
            new Enum("none", INT_NONE),
            new Enum("numberInDash", INT_NUMBER_IN_DASH),
            new Enum("hebrew1", INT_HEBREW_1),
            new Enum("hebrew2", INT_HEBREW_2),
            new Enum("arabicAlpha", INT_ARABIC_ALPHA),
            new Enum("arabicAbjad", INT_ARABIC_ABJAD),
            new Enum("hindiVowels", INT_HINDI_VOWELS),
            new Enum("hindiConsonants", INT_HINDI_CONSONANTS),
            new Enum("hindiNumbers", INT_HINDI_NUMBERS),
            new Enum("hindiCounting", INT_HINDI_COUNTING),
            new Enum("thaiLetters", INT_THAI_LETTERS),
            new Enum("thaiNumbers", INT_THAI_NUMBERS),
            new Enum("thaiCounting", INT_THAI_COUNTING),
            new Enum("bahtText", INT_BAHT_TEXT),
            new Enum("dollarText", INT_DOLLAR_TEXT),
            new Enum("custom", INT_CUSTOM),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
