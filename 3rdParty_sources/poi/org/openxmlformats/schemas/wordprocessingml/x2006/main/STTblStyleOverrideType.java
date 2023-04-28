/*
 * XML Type:  ST_TblStyleOverrideType
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TblStyleOverrideType(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.
 */
public interface STTblStyleOverrideType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttblstyleoverridetype869ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum WHOLE_TABLE = Enum.forString("wholeTable");
    Enum FIRST_ROW = Enum.forString("firstRow");
    Enum LAST_ROW = Enum.forString("lastRow");
    Enum FIRST_COL = Enum.forString("firstCol");
    Enum LAST_COL = Enum.forString("lastCol");
    Enum BAND_1_VERT = Enum.forString("band1Vert");
    Enum BAND_2_VERT = Enum.forString("band2Vert");
    Enum BAND_1_HORZ = Enum.forString("band1Horz");
    Enum BAND_2_HORZ = Enum.forString("band2Horz");
    Enum NE_CELL = Enum.forString("neCell");
    Enum NW_CELL = Enum.forString("nwCell");
    Enum SE_CELL = Enum.forString("seCell");
    Enum SW_CELL = Enum.forString("swCell");

    int INT_WHOLE_TABLE = Enum.INT_WHOLE_TABLE;
    int INT_FIRST_ROW = Enum.INT_FIRST_ROW;
    int INT_LAST_ROW = Enum.INT_LAST_ROW;
    int INT_FIRST_COL = Enum.INT_FIRST_COL;
    int INT_LAST_COL = Enum.INT_LAST_COL;
    int INT_BAND_1_VERT = Enum.INT_BAND_1_VERT;
    int INT_BAND_2_VERT = Enum.INT_BAND_2_VERT;
    int INT_BAND_1_HORZ = Enum.INT_BAND_1_HORZ;
    int INT_BAND_2_HORZ = Enum.INT_BAND_2_HORZ;
    int INT_NE_CELL = Enum.INT_NE_CELL;
    int INT_NW_CELL = Enum.INT_NW_CELL;
    int INT_SE_CELL = Enum.INT_SE_CELL;
    int INT_SW_CELL = Enum.INT_SW_CELL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblStyleOverrideType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_WHOLE_TABLE
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

        static final int INT_WHOLE_TABLE = 1;
        static final int INT_FIRST_ROW = 2;
        static final int INT_LAST_ROW = 3;
        static final int INT_FIRST_COL = 4;
        static final int INT_LAST_COL = 5;
        static final int INT_BAND_1_VERT = 6;
        static final int INT_BAND_2_VERT = 7;
        static final int INT_BAND_1_HORZ = 8;
        static final int INT_BAND_2_HORZ = 9;
        static final int INT_NE_CELL = 10;
        static final int INT_NW_CELL = 11;
        static final int INT_SE_CELL = 12;
        static final int INT_SW_CELL = 13;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("wholeTable", INT_WHOLE_TABLE),
            new Enum("firstRow", INT_FIRST_ROW),
            new Enum("lastRow", INT_LAST_ROW),
            new Enum("firstCol", INT_FIRST_COL),
            new Enum("lastCol", INT_LAST_COL),
            new Enum("band1Vert", INT_BAND_1_VERT),
            new Enum("band2Vert", INT_BAND_2_VERT),
            new Enum("band1Horz", INT_BAND_1_HORZ),
            new Enum("band2Horz", INT_BAND_2_HORZ),
            new Enum("neCell", INT_NE_CELL),
            new Enum("nwCell", INT_NW_CELL),
            new Enum("seCell", INT_SE_CELL),
            new Enum("swCell", INT_SW_CELL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
