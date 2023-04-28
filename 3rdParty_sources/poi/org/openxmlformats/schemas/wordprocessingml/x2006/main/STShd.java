/*
 * XML Type:  ST_Shd
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Shd(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd.
 */
public interface STShd extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stshd14d3type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NIL = Enum.forString("nil");
    Enum CLEAR = Enum.forString("clear");
    Enum SOLID = Enum.forString("solid");
    Enum HORZ_STRIPE = Enum.forString("horzStripe");
    Enum VERT_STRIPE = Enum.forString("vertStripe");
    Enum REVERSE_DIAG_STRIPE = Enum.forString("reverseDiagStripe");
    Enum DIAG_STRIPE = Enum.forString("diagStripe");
    Enum HORZ_CROSS = Enum.forString("horzCross");
    Enum DIAG_CROSS = Enum.forString("diagCross");
    Enum THIN_HORZ_STRIPE = Enum.forString("thinHorzStripe");
    Enum THIN_VERT_STRIPE = Enum.forString("thinVertStripe");
    Enum THIN_REVERSE_DIAG_STRIPE = Enum.forString("thinReverseDiagStripe");
    Enum THIN_DIAG_STRIPE = Enum.forString("thinDiagStripe");
    Enum THIN_HORZ_CROSS = Enum.forString("thinHorzCross");
    Enum THIN_DIAG_CROSS = Enum.forString("thinDiagCross");
    Enum PCT_5 = Enum.forString("pct5");
    Enum PCT_10 = Enum.forString("pct10");
    Enum PCT_12 = Enum.forString("pct12");
    Enum PCT_15 = Enum.forString("pct15");
    Enum PCT_20 = Enum.forString("pct20");
    Enum PCT_25 = Enum.forString("pct25");
    Enum PCT_30 = Enum.forString("pct30");
    Enum PCT_35 = Enum.forString("pct35");
    Enum PCT_37 = Enum.forString("pct37");
    Enum PCT_40 = Enum.forString("pct40");
    Enum PCT_45 = Enum.forString("pct45");
    Enum PCT_50 = Enum.forString("pct50");
    Enum PCT_55 = Enum.forString("pct55");
    Enum PCT_60 = Enum.forString("pct60");
    Enum PCT_62 = Enum.forString("pct62");
    Enum PCT_65 = Enum.forString("pct65");
    Enum PCT_70 = Enum.forString("pct70");
    Enum PCT_75 = Enum.forString("pct75");
    Enum PCT_80 = Enum.forString("pct80");
    Enum PCT_85 = Enum.forString("pct85");
    Enum PCT_87 = Enum.forString("pct87");
    Enum PCT_90 = Enum.forString("pct90");
    Enum PCT_95 = Enum.forString("pct95");

    int INT_NIL = Enum.INT_NIL;
    int INT_CLEAR = Enum.INT_CLEAR;
    int INT_SOLID = Enum.INT_SOLID;
    int INT_HORZ_STRIPE = Enum.INT_HORZ_STRIPE;
    int INT_VERT_STRIPE = Enum.INT_VERT_STRIPE;
    int INT_REVERSE_DIAG_STRIPE = Enum.INT_REVERSE_DIAG_STRIPE;
    int INT_DIAG_STRIPE = Enum.INT_DIAG_STRIPE;
    int INT_HORZ_CROSS = Enum.INT_HORZ_CROSS;
    int INT_DIAG_CROSS = Enum.INT_DIAG_CROSS;
    int INT_THIN_HORZ_STRIPE = Enum.INT_THIN_HORZ_STRIPE;
    int INT_THIN_VERT_STRIPE = Enum.INT_THIN_VERT_STRIPE;
    int INT_THIN_REVERSE_DIAG_STRIPE = Enum.INT_THIN_REVERSE_DIAG_STRIPE;
    int INT_THIN_DIAG_STRIPE = Enum.INT_THIN_DIAG_STRIPE;
    int INT_THIN_HORZ_CROSS = Enum.INT_THIN_HORZ_CROSS;
    int INT_THIN_DIAG_CROSS = Enum.INT_THIN_DIAG_CROSS;
    int INT_PCT_5 = Enum.INT_PCT_5;
    int INT_PCT_10 = Enum.INT_PCT_10;
    int INT_PCT_12 = Enum.INT_PCT_12;
    int INT_PCT_15 = Enum.INT_PCT_15;
    int INT_PCT_20 = Enum.INT_PCT_20;
    int INT_PCT_25 = Enum.INT_PCT_25;
    int INT_PCT_30 = Enum.INT_PCT_30;
    int INT_PCT_35 = Enum.INT_PCT_35;
    int INT_PCT_37 = Enum.INT_PCT_37;
    int INT_PCT_40 = Enum.INT_PCT_40;
    int INT_PCT_45 = Enum.INT_PCT_45;
    int INT_PCT_50 = Enum.INT_PCT_50;
    int INT_PCT_55 = Enum.INT_PCT_55;
    int INT_PCT_60 = Enum.INT_PCT_60;
    int INT_PCT_62 = Enum.INT_PCT_62;
    int INT_PCT_65 = Enum.INT_PCT_65;
    int INT_PCT_70 = Enum.INT_PCT_70;
    int INT_PCT_75 = Enum.INT_PCT_75;
    int INT_PCT_80 = Enum.INT_PCT_80;
    int INT_PCT_85 = Enum.INT_PCT_85;
    int INT_PCT_87 = Enum.INT_PCT_87;
    int INT_PCT_90 = Enum.INT_PCT_90;
    int INT_PCT_95 = Enum.INT_PCT_95;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NIL
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

        static final int INT_NIL = 1;
        static final int INT_CLEAR = 2;
        static final int INT_SOLID = 3;
        static final int INT_HORZ_STRIPE = 4;
        static final int INT_VERT_STRIPE = 5;
        static final int INT_REVERSE_DIAG_STRIPE = 6;
        static final int INT_DIAG_STRIPE = 7;
        static final int INT_HORZ_CROSS = 8;
        static final int INT_DIAG_CROSS = 9;
        static final int INT_THIN_HORZ_STRIPE = 10;
        static final int INT_THIN_VERT_STRIPE = 11;
        static final int INT_THIN_REVERSE_DIAG_STRIPE = 12;
        static final int INT_THIN_DIAG_STRIPE = 13;
        static final int INT_THIN_HORZ_CROSS = 14;
        static final int INT_THIN_DIAG_CROSS = 15;
        static final int INT_PCT_5 = 16;
        static final int INT_PCT_10 = 17;
        static final int INT_PCT_12 = 18;
        static final int INT_PCT_15 = 19;
        static final int INT_PCT_20 = 20;
        static final int INT_PCT_25 = 21;
        static final int INT_PCT_30 = 22;
        static final int INT_PCT_35 = 23;
        static final int INT_PCT_37 = 24;
        static final int INT_PCT_40 = 25;
        static final int INT_PCT_45 = 26;
        static final int INT_PCT_50 = 27;
        static final int INT_PCT_55 = 28;
        static final int INT_PCT_60 = 29;
        static final int INT_PCT_62 = 30;
        static final int INT_PCT_65 = 31;
        static final int INT_PCT_70 = 32;
        static final int INT_PCT_75 = 33;
        static final int INT_PCT_80 = 34;
        static final int INT_PCT_85 = 35;
        static final int INT_PCT_87 = 36;
        static final int INT_PCT_90 = 37;
        static final int INT_PCT_95 = 38;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("nil", INT_NIL),
            new Enum("clear", INT_CLEAR),
            new Enum("solid", INT_SOLID),
            new Enum("horzStripe", INT_HORZ_STRIPE),
            new Enum("vertStripe", INT_VERT_STRIPE),
            new Enum("reverseDiagStripe", INT_REVERSE_DIAG_STRIPE),
            new Enum("diagStripe", INT_DIAG_STRIPE),
            new Enum("horzCross", INT_HORZ_CROSS),
            new Enum("diagCross", INT_DIAG_CROSS),
            new Enum("thinHorzStripe", INT_THIN_HORZ_STRIPE),
            new Enum("thinVertStripe", INT_THIN_VERT_STRIPE),
            new Enum("thinReverseDiagStripe", INT_THIN_REVERSE_DIAG_STRIPE),
            new Enum("thinDiagStripe", INT_THIN_DIAG_STRIPE),
            new Enum("thinHorzCross", INT_THIN_HORZ_CROSS),
            new Enum("thinDiagCross", INT_THIN_DIAG_CROSS),
            new Enum("pct5", INT_PCT_5),
            new Enum("pct10", INT_PCT_10),
            new Enum("pct12", INT_PCT_12),
            new Enum("pct15", INT_PCT_15),
            new Enum("pct20", INT_PCT_20),
            new Enum("pct25", INT_PCT_25),
            new Enum("pct30", INT_PCT_30),
            new Enum("pct35", INT_PCT_35),
            new Enum("pct37", INT_PCT_37),
            new Enum("pct40", INT_PCT_40),
            new Enum("pct45", INT_PCT_45),
            new Enum("pct50", INT_PCT_50),
            new Enum("pct55", INT_PCT_55),
            new Enum("pct60", INT_PCT_60),
            new Enum("pct62", INT_PCT_62),
            new Enum("pct65", INT_PCT_65),
            new Enum("pct70", INT_PCT_70),
            new Enum("pct75", INT_PCT_75),
            new Enum("pct80", INT_PCT_80),
            new Enum("pct85", INT_PCT_85),
            new Enum("pct87", INT_PCT_87),
            new Enum("pct90", INT_PCT_90),
            new Enum("pct95", INT_PCT_95),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
