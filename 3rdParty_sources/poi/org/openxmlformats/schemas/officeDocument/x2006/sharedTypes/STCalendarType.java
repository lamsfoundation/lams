/*
 * XML Type:  ST_CalendarType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_CalendarType(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.
 */
public interface STCalendarType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcalendartype8cd2type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum GREGORIAN = Enum.forString("gregorian");
    Enum GREGORIAN_US = Enum.forString("gregorianUs");
    Enum GREGORIAN_ME_FRENCH = Enum.forString("gregorianMeFrench");
    Enum GREGORIAN_ARABIC = Enum.forString("gregorianArabic");
    Enum HIJRI = Enum.forString("hijri");
    Enum HEBREW = Enum.forString("hebrew");
    Enum TAIWAN = Enum.forString("taiwan");
    Enum JAPAN = Enum.forString("japan");
    Enum THAI = Enum.forString("thai");
    Enum KOREA = Enum.forString("korea");
    Enum SAKA = Enum.forString("saka");
    Enum GREGORIAN_XLIT_ENGLISH = Enum.forString("gregorianXlitEnglish");
    Enum GREGORIAN_XLIT_FRENCH = Enum.forString("gregorianXlitFrench");
    Enum NONE = Enum.forString("none");

    int INT_GREGORIAN = Enum.INT_GREGORIAN;
    int INT_GREGORIAN_US = Enum.INT_GREGORIAN_US;
    int INT_GREGORIAN_ME_FRENCH = Enum.INT_GREGORIAN_ME_FRENCH;
    int INT_GREGORIAN_ARABIC = Enum.INT_GREGORIAN_ARABIC;
    int INT_HIJRI = Enum.INT_HIJRI;
    int INT_HEBREW = Enum.INT_HEBREW;
    int INT_TAIWAN = Enum.INT_TAIWAN;
    int INT_JAPAN = Enum.INT_JAPAN;
    int INT_THAI = Enum.INT_THAI;
    int INT_KOREA = Enum.INT_KOREA;
    int INT_SAKA = Enum.INT_SAKA;
    int INT_GREGORIAN_XLIT_ENGLISH = Enum.INT_GREGORIAN_XLIT_ENGLISH;
    int INT_GREGORIAN_XLIT_FRENCH = Enum.INT_GREGORIAN_XLIT_FRENCH;
    int INT_NONE = Enum.INT_NONE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCalendarType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_GREGORIAN
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

        static final int INT_GREGORIAN = 1;
        static final int INT_GREGORIAN_US = 2;
        static final int INT_GREGORIAN_ME_FRENCH = 3;
        static final int INT_GREGORIAN_ARABIC = 4;
        static final int INT_HIJRI = 5;
        static final int INT_HEBREW = 6;
        static final int INT_TAIWAN = 7;
        static final int INT_JAPAN = 8;
        static final int INT_THAI = 9;
        static final int INT_KOREA = 10;
        static final int INT_SAKA = 11;
        static final int INT_GREGORIAN_XLIT_ENGLISH = 12;
        static final int INT_GREGORIAN_XLIT_FRENCH = 13;
        static final int INT_NONE = 14;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("gregorian", INT_GREGORIAN),
            new Enum("gregorianUs", INT_GREGORIAN_US),
            new Enum("gregorianMeFrench", INT_GREGORIAN_ME_FRENCH),
            new Enum("gregorianArabic", INT_GREGORIAN_ARABIC),
            new Enum("hijri", INT_HIJRI),
            new Enum("hebrew", INT_HEBREW),
            new Enum("taiwan", INT_TAIWAN),
            new Enum("japan", INT_JAPAN),
            new Enum("thai", INT_THAI),
            new Enum("korea", INT_KOREA),
            new Enum("saka", INT_SAKA),
            new Enum("gregorianXlitEnglish", INT_GREGORIAN_XLIT_ENGLISH),
            new Enum("gregorianXlitFrench", INT_GREGORIAN_XLIT_FRENCH),
            new Enum("none", INT_NONE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
