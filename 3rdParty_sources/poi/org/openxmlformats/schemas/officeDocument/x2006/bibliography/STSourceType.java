/*
 * XML Type:  ST_SourceType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_SourceType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.
 */
public interface STSourceType extends org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stsourcetypee8a7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum ARTICLE_IN_A_PERIODICAL = Enum.forString("ArticleInAPeriodical");
    Enum BOOK = Enum.forString("Book");
    Enum BOOK_SECTION = Enum.forString("BookSection");
    Enum JOURNAL_ARTICLE = Enum.forString("JournalArticle");
    Enum CONFERENCE_PROCEEDINGS = Enum.forString("ConferenceProceedings");
    Enum REPORT = Enum.forString("Report");
    Enum SOUND_RECORDING = Enum.forString("SoundRecording");
    Enum PERFORMANCE = Enum.forString("Performance");
    Enum ART = Enum.forString("Art");
    Enum DOCUMENT_FROM_INTERNET_SITE = Enum.forString("DocumentFromInternetSite");
    Enum INTERNET_SITE = Enum.forString("InternetSite");
    Enum FILM = Enum.forString("Film");
    Enum INTERVIEW = Enum.forString("Interview");
    Enum PATENT = Enum.forString("Patent");
    Enum ELECTRONIC_SOURCE = Enum.forString("ElectronicSource");
    Enum CASE = Enum.forString("Case");
    Enum MISC = Enum.forString("Misc");

    int INT_ARTICLE_IN_A_PERIODICAL = Enum.INT_ARTICLE_IN_A_PERIODICAL;
    int INT_BOOK = Enum.INT_BOOK;
    int INT_BOOK_SECTION = Enum.INT_BOOK_SECTION;
    int INT_JOURNAL_ARTICLE = Enum.INT_JOURNAL_ARTICLE;
    int INT_CONFERENCE_PROCEEDINGS = Enum.INT_CONFERENCE_PROCEEDINGS;
    int INT_REPORT = Enum.INT_REPORT;
    int INT_SOUND_RECORDING = Enum.INT_SOUND_RECORDING;
    int INT_PERFORMANCE = Enum.INT_PERFORMANCE;
    int INT_ART = Enum.INT_ART;
    int INT_DOCUMENT_FROM_INTERNET_SITE = Enum.INT_DOCUMENT_FROM_INTERNET_SITE;
    int INT_INTERNET_SITE = Enum.INT_INTERNET_SITE;
    int INT_FILM = Enum.INT_FILM;
    int INT_INTERVIEW = Enum.INT_INTERVIEW;
    int INT_PATENT = Enum.INT_PATENT;
    int INT_ELECTRONIC_SOURCE = Enum.INT_ELECTRONIC_SOURCE;
    int INT_CASE = Enum.INT_CASE;
    int INT_MISC = Enum.INT_MISC;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ARTICLE_IN_A_PERIODICAL
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

        static final int INT_ARTICLE_IN_A_PERIODICAL = 1;
        static final int INT_BOOK = 2;
        static final int INT_BOOK_SECTION = 3;
        static final int INT_JOURNAL_ARTICLE = 4;
        static final int INT_CONFERENCE_PROCEEDINGS = 5;
        static final int INT_REPORT = 6;
        static final int INT_SOUND_RECORDING = 7;
        static final int INT_PERFORMANCE = 8;
        static final int INT_ART = 9;
        static final int INT_DOCUMENT_FROM_INTERNET_SITE = 10;
        static final int INT_INTERNET_SITE = 11;
        static final int INT_FILM = 12;
        static final int INT_INTERVIEW = 13;
        static final int INT_PATENT = 14;
        static final int INT_ELECTRONIC_SOURCE = 15;
        static final int INT_CASE = 16;
        static final int INT_MISC = 17;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("ArticleInAPeriodical", INT_ARTICLE_IN_A_PERIODICAL),
            new Enum("Book", INT_BOOK),
            new Enum("BookSection", INT_BOOK_SECTION),
            new Enum("JournalArticle", INT_JOURNAL_ARTICLE),
            new Enum("ConferenceProceedings", INT_CONFERENCE_PROCEEDINGS),
            new Enum("Report", INT_REPORT),
            new Enum("SoundRecording", INT_SOUND_RECORDING),
            new Enum("Performance", INT_PERFORMANCE),
            new Enum("Art", INT_ART),
            new Enum("DocumentFromInternetSite", INT_DOCUMENT_FROM_INTERNET_SITE),
            new Enum("InternetSite", INT_INTERNET_SITE),
            new Enum("Film", INT_FILM),
            new Enum("Interview", INT_INTERVIEW),
            new Enum("Patent", INT_PATENT),
            new Enum("ElectronicSource", INT_ELECTRONIC_SOURCE),
            new Enum("Case", INT_CASE),
            new Enum("Misc", INT_MISC),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
