/*
 * XML Type:  ST_DocPartGallery
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartGallery
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_DocPartGallery(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartGallery.
 */
public interface STDocPartGallery extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartGallery> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stdocpartgallery6b71type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum PLACEHOLDER = Enum.forString("placeholder");
    Enum ANY = Enum.forString("any");
    Enum DEFAULT = Enum.forString("default");
    Enum DOC_PARTS = Enum.forString("docParts");
    Enum COVER_PG = Enum.forString("coverPg");
    Enum EQ = Enum.forString("eq");
    Enum FTRS = Enum.forString("ftrs");
    Enum HDRS = Enum.forString("hdrs");
    Enum PG_NUM = Enum.forString("pgNum");
    Enum TBLS = Enum.forString("tbls");
    Enum WATERMARKS = Enum.forString("watermarks");
    Enum AUTO_TXT = Enum.forString("autoTxt");
    Enum TXT_BOX = Enum.forString("txtBox");
    Enum PG_NUM_T = Enum.forString("pgNumT");
    Enum PG_NUM_B = Enum.forString("pgNumB");
    Enum PG_NUM_MARGINS = Enum.forString("pgNumMargins");
    Enum TBL_OF_CONTENTS = Enum.forString("tblOfContents");
    Enum BIB = Enum.forString("bib");
    Enum CUST_QUICK_PARTS = Enum.forString("custQuickParts");
    Enum CUST_COVER_PG = Enum.forString("custCoverPg");
    Enum CUST_EQ = Enum.forString("custEq");
    Enum CUST_FTRS = Enum.forString("custFtrs");
    Enum CUST_HDRS = Enum.forString("custHdrs");
    Enum CUST_PG_NUM = Enum.forString("custPgNum");
    Enum CUST_TBLS = Enum.forString("custTbls");
    Enum CUST_WATERMARKS = Enum.forString("custWatermarks");
    Enum CUST_AUTO_TXT = Enum.forString("custAutoTxt");
    Enum CUST_TXT_BOX = Enum.forString("custTxtBox");
    Enum CUST_PG_NUM_T = Enum.forString("custPgNumT");
    Enum CUST_PG_NUM_B = Enum.forString("custPgNumB");
    Enum CUST_PG_NUM_MARGINS = Enum.forString("custPgNumMargins");
    Enum CUST_TBL_OF_CONTENTS = Enum.forString("custTblOfContents");
    Enum CUST_BIB = Enum.forString("custBib");
    Enum CUSTOM_1 = Enum.forString("custom1");
    Enum CUSTOM_2 = Enum.forString("custom2");
    Enum CUSTOM_3 = Enum.forString("custom3");
    Enum CUSTOM_4 = Enum.forString("custom4");
    Enum CUSTOM_5 = Enum.forString("custom5");

    int INT_PLACEHOLDER = Enum.INT_PLACEHOLDER;
    int INT_ANY = Enum.INT_ANY;
    int INT_DEFAULT = Enum.INT_DEFAULT;
    int INT_DOC_PARTS = Enum.INT_DOC_PARTS;
    int INT_COVER_PG = Enum.INT_COVER_PG;
    int INT_EQ = Enum.INT_EQ;
    int INT_FTRS = Enum.INT_FTRS;
    int INT_HDRS = Enum.INT_HDRS;
    int INT_PG_NUM = Enum.INT_PG_NUM;
    int INT_TBLS = Enum.INT_TBLS;
    int INT_WATERMARKS = Enum.INT_WATERMARKS;
    int INT_AUTO_TXT = Enum.INT_AUTO_TXT;
    int INT_TXT_BOX = Enum.INT_TXT_BOX;
    int INT_PG_NUM_T = Enum.INT_PG_NUM_T;
    int INT_PG_NUM_B = Enum.INT_PG_NUM_B;
    int INT_PG_NUM_MARGINS = Enum.INT_PG_NUM_MARGINS;
    int INT_TBL_OF_CONTENTS = Enum.INT_TBL_OF_CONTENTS;
    int INT_BIB = Enum.INT_BIB;
    int INT_CUST_QUICK_PARTS = Enum.INT_CUST_QUICK_PARTS;
    int INT_CUST_COVER_PG = Enum.INT_CUST_COVER_PG;
    int INT_CUST_EQ = Enum.INT_CUST_EQ;
    int INT_CUST_FTRS = Enum.INT_CUST_FTRS;
    int INT_CUST_HDRS = Enum.INT_CUST_HDRS;
    int INT_CUST_PG_NUM = Enum.INT_CUST_PG_NUM;
    int INT_CUST_TBLS = Enum.INT_CUST_TBLS;
    int INT_CUST_WATERMARKS = Enum.INT_CUST_WATERMARKS;
    int INT_CUST_AUTO_TXT = Enum.INT_CUST_AUTO_TXT;
    int INT_CUST_TXT_BOX = Enum.INT_CUST_TXT_BOX;
    int INT_CUST_PG_NUM_T = Enum.INT_CUST_PG_NUM_T;
    int INT_CUST_PG_NUM_B = Enum.INT_CUST_PG_NUM_B;
    int INT_CUST_PG_NUM_MARGINS = Enum.INT_CUST_PG_NUM_MARGINS;
    int INT_CUST_TBL_OF_CONTENTS = Enum.INT_CUST_TBL_OF_CONTENTS;
    int INT_CUST_BIB = Enum.INT_CUST_BIB;
    int INT_CUSTOM_1 = Enum.INT_CUSTOM_1;
    int INT_CUSTOM_2 = Enum.INT_CUSTOM_2;
    int INT_CUSTOM_3 = Enum.INT_CUSTOM_3;
    int INT_CUSTOM_4 = Enum.INT_CUSTOM_4;
    int INT_CUSTOM_5 = Enum.INT_CUSTOM_5;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STDocPartGallery.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_PLACEHOLDER
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

        static final int INT_PLACEHOLDER = 1;
        static final int INT_ANY = 2;
        static final int INT_DEFAULT = 3;
        static final int INT_DOC_PARTS = 4;
        static final int INT_COVER_PG = 5;
        static final int INT_EQ = 6;
        static final int INT_FTRS = 7;
        static final int INT_HDRS = 8;
        static final int INT_PG_NUM = 9;
        static final int INT_TBLS = 10;
        static final int INT_WATERMARKS = 11;
        static final int INT_AUTO_TXT = 12;
        static final int INT_TXT_BOX = 13;
        static final int INT_PG_NUM_T = 14;
        static final int INT_PG_NUM_B = 15;
        static final int INT_PG_NUM_MARGINS = 16;
        static final int INT_TBL_OF_CONTENTS = 17;
        static final int INT_BIB = 18;
        static final int INT_CUST_QUICK_PARTS = 19;
        static final int INT_CUST_COVER_PG = 20;
        static final int INT_CUST_EQ = 21;
        static final int INT_CUST_FTRS = 22;
        static final int INT_CUST_HDRS = 23;
        static final int INT_CUST_PG_NUM = 24;
        static final int INT_CUST_TBLS = 25;
        static final int INT_CUST_WATERMARKS = 26;
        static final int INT_CUST_AUTO_TXT = 27;
        static final int INT_CUST_TXT_BOX = 28;
        static final int INT_CUST_PG_NUM_T = 29;
        static final int INT_CUST_PG_NUM_B = 30;
        static final int INT_CUST_PG_NUM_MARGINS = 31;
        static final int INT_CUST_TBL_OF_CONTENTS = 32;
        static final int INT_CUST_BIB = 33;
        static final int INT_CUSTOM_1 = 34;
        static final int INT_CUSTOM_2 = 35;
        static final int INT_CUSTOM_3 = 36;
        static final int INT_CUSTOM_4 = 37;
        static final int INT_CUSTOM_5 = 38;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("placeholder", INT_PLACEHOLDER),
            new Enum("any", INT_ANY),
            new Enum("default", INT_DEFAULT),
            new Enum("docParts", INT_DOC_PARTS),
            new Enum("coverPg", INT_COVER_PG),
            new Enum("eq", INT_EQ),
            new Enum("ftrs", INT_FTRS),
            new Enum("hdrs", INT_HDRS),
            new Enum("pgNum", INT_PG_NUM),
            new Enum("tbls", INT_TBLS),
            new Enum("watermarks", INT_WATERMARKS),
            new Enum("autoTxt", INT_AUTO_TXT),
            new Enum("txtBox", INT_TXT_BOX),
            new Enum("pgNumT", INT_PG_NUM_T),
            new Enum("pgNumB", INT_PG_NUM_B),
            new Enum("pgNumMargins", INT_PG_NUM_MARGINS),
            new Enum("tblOfContents", INT_TBL_OF_CONTENTS),
            new Enum("bib", INT_BIB),
            new Enum("custQuickParts", INT_CUST_QUICK_PARTS),
            new Enum("custCoverPg", INT_CUST_COVER_PG),
            new Enum("custEq", INT_CUST_EQ),
            new Enum("custFtrs", INT_CUST_FTRS),
            new Enum("custHdrs", INT_CUST_HDRS),
            new Enum("custPgNum", INT_CUST_PG_NUM),
            new Enum("custTbls", INT_CUST_TBLS),
            new Enum("custWatermarks", INT_CUST_WATERMARKS),
            new Enum("custAutoTxt", INT_CUST_AUTO_TXT),
            new Enum("custTxtBox", INT_CUST_TXT_BOX),
            new Enum("custPgNumT", INT_CUST_PG_NUM_T),
            new Enum("custPgNumB", INT_CUST_PG_NUM_B),
            new Enum("custPgNumMargins", INT_CUST_PG_NUM_MARGINS),
            new Enum("custTblOfContents", INT_CUST_TBL_OF_CONTENTS),
            new Enum("custBib", INT_CUST_BIB),
            new Enum("custom1", INT_CUSTOM_1),
            new Enum("custom2", INT_CUSTOM_2),
            new Enum("custom3", INT_CUSTOM_3),
            new Enum("custom4", INT_CUSTOM_4),
            new Enum("custom5", INT_CUSTOM_5),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
