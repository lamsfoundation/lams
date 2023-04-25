/*
 * XML Type:  ST_MailMergeDocType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STMailMergeDocType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_MailMergeDocType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STMailMergeDocType.
 */
public interface STMailMergeDocType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STMailMergeDocType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stmailmergedoctype4be7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum CATALOG = Enum.forString("catalog");
    Enum ENVELOPES = Enum.forString("envelopes");
    Enum MAILING_LABELS = Enum.forString("mailingLabels");
    Enum FORM_LETTERS = Enum.forString("formLetters");
    Enum EMAIL = Enum.forString("email");
    Enum FAX = Enum.forString("fax");

    int INT_CATALOG = Enum.INT_CATALOG;
    int INT_ENVELOPES = Enum.INT_ENVELOPES;
    int INT_MAILING_LABELS = Enum.INT_MAILING_LABELS;
    int INT_FORM_LETTERS = Enum.INT_FORM_LETTERS;
    int INT_EMAIL = Enum.INT_EMAIL;
    int INT_FAX = Enum.INT_FAX;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STMailMergeDocType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_CATALOG
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

        static final int INT_CATALOG = 1;
        static final int INT_ENVELOPES = 2;
        static final int INT_MAILING_LABELS = 3;
        static final int INT_FORM_LETTERS = 4;
        static final int INT_EMAIL = 5;
        static final int INT_FAX = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("catalog", INT_CATALOG),
            new Enum("envelopes", INT_ENVELOPES),
            new Enum("mailingLabels", INT_MAILING_LABELS),
            new Enum("formLetters", INT_FORM_LETTERS),
            new Enum("email", INT_EMAIL),
            new Enum("fax", INT_FAX),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
