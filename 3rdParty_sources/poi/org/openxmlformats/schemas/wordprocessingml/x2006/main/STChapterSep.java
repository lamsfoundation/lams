/*
 * XML Type:  ST_ChapterSep
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ChapterSep(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.
 */
public interface STChapterSep extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stchaptersep27e7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum HYPHEN = Enum.forString("hyphen");
    Enum PERIOD = Enum.forString("period");
    Enum COLON = Enum.forString("colon");
    Enum EM_DASH = Enum.forString("emDash");
    Enum EN_DASH = Enum.forString("enDash");

    int INT_HYPHEN = Enum.INT_HYPHEN;
    int INT_PERIOD = Enum.INT_PERIOD;
    int INT_COLON = Enum.INT_COLON;
    int INT_EM_DASH = Enum.INT_EM_DASH;
    int INT_EN_DASH = Enum.INT_EN_DASH;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STChapterSep.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_HYPHEN
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

        static final int INT_HYPHEN = 1;
        static final int INT_PERIOD = 2;
        static final int INT_COLON = 3;
        static final int INT_EM_DASH = 4;
        static final int INT_EN_DASH = 5;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("hyphen", INT_HYPHEN),
            new Enum("period", INT_PERIOD),
            new Enum("colon", INT_COLON),
            new Enum("emDash", INT_EM_DASH),
            new Enum("enDash", INT_EN_DASH),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
