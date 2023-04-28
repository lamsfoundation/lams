/*
 * XML Type:  ST_PlaceholderType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PlaceholderType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.
 */
public interface STPlaceholderType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stplaceholdertypeca72type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum TITLE = Enum.forString("title");
    Enum BODY = Enum.forString("body");
    Enum CTR_TITLE = Enum.forString("ctrTitle");
    Enum SUB_TITLE = Enum.forString("subTitle");
    Enum DT = Enum.forString("dt");
    Enum SLD_NUM = Enum.forString("sldNum");
    Enum FTR = Enum.forString("ftr");
    Enum HDR = Enum.forString("hdr");
    Enum OBJ = Enum.forString("obj");
    Enum CHART = Enum.forString("chart");
    Enum TBL = Enum.forString("tbl");
    Enum CLIP_ART = Enum.forString("clipArt");
    Enum DGM = Enum.forString("dgm");
    Enum MEDIA = Enum.forString("media");
    Enum SLD_IMG = Enum.forString("sldImg");
    Enum PIC = Enum.forString("pic");

    int INT_TITLE = Enum.INT_TITLE;
    int INT_BODY = Enum.INT_BODY;
    int INT_CTR_TITLE = Enum.INT_CTR_TITLE;
    int INT_SUB_TITLE = Enum.INT_SUB_TITLE;
    int INT_DT = Enum.INT_DT;
    int INT_SLD_NUM = Enum.INT_SLD_NUM;
    int INT_FTR = Enum.INT_FTR;
    int INT_HDR = Enum.INT_HDR;
    int INT_OBJ = Enum.INT_OBJ;
    int INT_CHART = Enum.INT_CHART;
    int INT_TBL = Enum.INT_TBL;
    int INT_CLIP_ART = Enum.INT_CLIP_ART;
    int INT_DGM = Enum.INT_DGM;
    int INT_MEDIA = Enum.INT_MEDIA;
    int INT_SLD_IMG = Enum.INT_SLD_IMG;
    int INT_PIC = Enum.INT_PIC;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_TITLE
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

        static final int INT_TITLE = 1;
        static final int INT_BODY = 2;
        static final int INT_CTR_TITLE = 3;
        static final int INT_SUB_TITLE = 4;
        static final int INT_DT = 5;
        static final int INT_SLD_NUM = 6;
        static final int INT_FTR = 7;
        static final int INT_HDR = 8;
        static final int INT_OBJ = 9;
        static final int INT_CHART = 10;
        static final int INT_TBL = 11;
        static final int INT_CLIP_ART = 12;
        static final int INT_DGM = 13;
        static final int INT_MEDIA = 14;
        static final int INT_SLD_IMG = 15;
        static final int INT_PIC = 16;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("title", INT_TITLE),
            new Enum("body", INT_BODY),
            new Enum("ctrTitle", INT_CTR_TITLE),
            new Enum("subTitle", INT_SUB_TITLE),
            new Enum("dt", INT_DT),
            new Enum("sldNum", INT_SLD_NUM),
            new Enum("ftr", INT_FTR),
            new Enum("hdr", INT_HDR),
            new Enum("obj", INT_OBJ),
            new Enum("chart", INT_CHART),
            new Enum("tbl", INT_TBL),
            new Enum("clipArt", INT_CLIP_ART),
            new Enum("dgm", INT_DGM),
            new Enum("media", INT_MEDIA),
            new Enum("sldImg", INT_SLD_IMG),
            new Enum("pic", INT_PIC),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
