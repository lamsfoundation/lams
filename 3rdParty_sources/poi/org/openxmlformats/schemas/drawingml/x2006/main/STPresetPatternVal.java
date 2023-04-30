/*
 * XML Type:  ST_PresetPatternVal
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_PresetPatternVal(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.
 */
public interface STPresetPatternVal extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stpresetpatternval1d5btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum PCT_5 = Enum.forString("pct5");
    Enum PCT_10 = Enum.forString("pct10");
    Enum PCT_20 = Enum.forString("pct20");
    Enum PCT_25 = Enum.forString("pct25");
    Enum PCT_30 = Enum.forString("pct30");
    Enum PCT_40 = Enum.forString("pct40");
    Enum PCT_50 = Enum.forString("pct50");
    Enum PCT_60 = Enum.forString("pct60");
    Enum PCT_70 = Enum.forString("pct70");
    Enum PCT_75 = Enum.forString("pct75");
    Enum PCT_80 = Enum.forString("pct80");
    Enum PCT_90 = Enum.forString("pct90");
    Enum HORZ = Enum.forString("horz");
    Enum VERT = Enum.forString("vert");
    Enum LT_HORZ = Enum.forString("ltHorz");
    Enum LT_VERT = Enum.forString("ltVert");
    Enum DK_HORZ = Enum.forString("dkHorz");
    Enum DK_VERT = Enum.forString("dkVert");
    Enum NAR_HORZ = Enum.forString("narHorz");
    Enum NAR_VERT = Enum.forString("narVert");
    Enum DASH_HORZ = Enum.forString("dashHorz");
    Enum DASH_VERT = Enum.forString("dashVert");
    Enum CROSS = Enum.forString("cross");
    Enum DN_DIAG = Enum.forString("dnDiag");
    Enum UP_DIAG = Enum.forString("upDiag");
    Enum LT_DN_DIAG = Enum.forString("ltDnDiag");
    Enum LT_UP_DIAG = Enum.forString("ltUpDiag");
    Enum DK_DN_DIAG = Enum.forString("dkDnDiag");
    Enum DK_UP_DIAG = Enum.forString("dkUpDiag");
    Enum WD_DN_DIAG = Enum.forString("wdDnDiag");
    Enum WD_UP_DIAG = Enum.forString("wdUpDiag");
    Enum DASH_DN_DIAG = Enum.forString("dashDnDiag");
    Enum DASH_UP_DIAG = Enum.forString("dashUpDiag");
    Enum DIAG_CROSS = Enum.forString("diagCross");
    Enum SM_CHECK = Enum.forString("smCheck");
    Enum LG_CHECK = Enum.forString("lgCheck");
    Enum SM_GRID = Enum.forString("smGrid");
    Enum LG_GRID = Enum.forString("lgGrid");
    Enum DOT_GRID = Enum.forString("dotGrid");
    Enum SM_CONFETTI = Enum.forString("smConfetti");
    Enum LG_CONFETTI = Enum.forString("lgConfetti");
    Enum HORZ_BRICK = Enum.forString("horzBrick");
    Enum DIAG_BRICK = Enum.forString("diagBrick");
    Enum SOLID_DMND = Enum.forString("solidDmnd");
    Enum OPEN_DMND = Enum.forString("openDmnd");
    Enum DOT_DMND = Enum.forString("dotDmnd");
    Enum PLAID = Enum.forString("plaid");
    Enum SPHERE = Enum.forString("sphere");
    Enum WEAVE = Enum.forString("weave");
    Enum DIVOT = Enum.forString("divot");
    Enum SHINGLE = Enum.forString("shingle");
    Enum WAVE = Enum.forString("wave");
    Enum TRELLIS = Enum.forString("trellis");
    Enum ZIG_ZAG = Enum.forString("zigZag");

    int INT_PCT_5 = Enum.INT_PCT_5;
    int INT_PCT_10 = Enum.INT_PCT_10;
    int INT_PCT_20 = Enum.INT_PCT_20;
    int INT_PCT_25 = Enum.INT_PCT_25;
    int INT_PCT_30 = Enum.INT_PCT_30;
    int INT_PCT_40 = Enum.INT_PCT_40;
    int INT_PCT_50 = Enum.INT_PCT_50;
    int INT_PCT_60 = Enum.INT_PCT_60;
    int INT_PCT_70 = Enum.INT_PCT_70;
    int INT_PCT_75 = Enum.INT_PCT_75;
    int INT_PCT_80 = Enum.INT_PCT_80;
    int INT_PCT_90 = Enum.INT_PCT_90;
    int INT_HORZ = Enum.INT_HORZ;
    int INT_VERT = Enum.INT_VERT;
    int INT_LT_HORZ = Enum.INT_LT_HORZ;
    int INT_LT_VERT = Enum.INT_LT_VERT;
    int INT_DK_HORZ = Enum.INT_DK_HORZ;
    int INT_DK_VERT = Enum.INT_DK_VERT;
    int INT_NAR_HORZ = Enum.INT_NAR_HORZ;
    int INT_NAR_VERT = Enum.INT_NAR_VERT;
    int INT_DASH_HORZ = Enum.INT_DASH_HORZ;
    int INT_DASH_VERT = Enum.INT_DASH_VERT;
    int INT_CROSS = Enum.INT_CROSS;
    int INT_DN_DIAG = Enum.INT_DN_DIAG;
    int INT_UP_DIAG = Enum.INT_UP_DIAG;
    int INT_LT_DN_DIAG = Enum.INT_LT_DN_DIAG;
    int INT_LT_UP_DIAG = Enum.INT_LT_UP_DIAG;
    int INT_DK_DN_DIAG = Enum.INT_DK_DN_DIAG;
    int INT_DK_UP_DIAG = Enum.INT_DK_UP_DIAG;
    int INT_WD_DN_DIAG = Enum.INT_WD_DN_DIAG;
    int INT_WD_UP_DIAG = Enum.INT_WD_UP_DIAG;
    int INT_DASH_DN_DIAG = Enum.INT_DASH_DN_DIAG;
    int INT_DASH_UP_DIAG = Enum.INT_DASH_UP_DIAG;
    int INT_DIAG_CROSS = Enum.INT_DIAG_CROSS;
    int INT_SM_CHECK = Enum.INT_SM_CHECK;
    int INT_LG_CHECK = Enum.INT_LG_CHECK;
    int INT_SM_GRID = Enum.INT_SM_GRID;
    int INT_LG_GRID = Enum.INT_LG_GRID;
    int INT_DOT_GRID = Enum.INT_DOT_GRID;
    int INT_SM_CONFETTI = Enum.INT_SM_CONFETTI;
    int INT_LG_CONFETTI = Enum.INT_LG_CONFETTI;
    int INT_HORZ_BRICK = Enum.INT_HORZ_BRICK;
    int INT_DIAG_BRICK = Enum.INT_DIAG_BRICK;
    int INT_SOLID_DMND = Enum.INT_SOLID_DMND;
    int INT_OPEN_DMND = Enum.INT_OPEN_DMND;
    int INT_DOT_DMND = Enum.INT_DOT_DMND;
    int INT_PLAID = Enum.INT_PLAID;
    int INT_SPHERE = Enum.INT_SPHERE;
    int INT_WEAVE = Enum.INT_WEAVE;
    int INT_DIVOT = Enum.INT_DIVOT;
    int INT_SHINGLE = Enum.INT_SHINGLE;
    int INT_WAVE = Enum.INT_WAVE;
    int INT_TRELLIS = Enum.INT_TRELLIS;
    int INT_ZIG_ZAG = Enum.INT_ZIG_ZAG;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.main.STPresetPatternVal.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_PCT_5
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

        static final int INT_PCT_5 = 1;
        static final int INT_PCT_10 = 2;
        static final int INT_PCT_20 = 3;
        static final int INT_PCT_25 = 4;
        static final int INT_PCT_30 = 5;
        static final int INT_PCT_40 = 6;
        static final int INT_PCT_50 = 7;
        static final int INT_PCT_60 = 8;
        static final int INT_PCT_70 = 9;
        static final int INT_PCT_75 = 10;
        static final int INT_PCT_80 = 11;
        static final int INT_PCT_90 = 12;
        static final int INT_HORZ = 13;
        static final int INT_VERT = 14;
        static final int INT_LT_HORZ = 15;
        static final int INT_LT_VERT = 16;
        static final int INT_DK_HORZ = 17;
        static final int INT_DK_VERT = 18;
        static final int INT_NAR_HORZ = 19;
        static final int INT_NAR_VERT = 20;
        static final int INT_DASH_HORZ = 21;
        static final int INT_DASH_VERT = 22;
        static final int INT_CROSS = 23;
        static final int INT_DN_DIAG = 24;
        static final int INT_UP_DIAG = 25;
        static final int INT_LT_DN_DIAG = 26;
        static final int INT_LT_UP_DIAG = 27;
        static final int INT_DK_DN_DIAG = 28;
        static final int INT_DK_UP_DIAG = 29;
        static final int INT_WD_DN_DIAG = 30;
        static final int INT_WD_UP_DIAG = 31;
        static final int INT_DASH_DN_DIAG = 32;
        static final int INT_DASH_UP_DIAG = 33;
        static final int INT_DIAG_CROSS = 34;
        static final int INT_SM_CHECK = 35;
        static final int INT_LG_CHECK = 36;
        static final int INT_SM_GRID = 37;
        static final int INT_LG_GRID = 38;
        static final int INT_DOT_GRID = 39;
        static final int INT_SM_CONFETTI = 40;
        static final int INT_LG_CONFETTI = 41;
        static final int INT_HORZ_BRICK = 42;
        static final int INT_DIAG_BRICK = 43;
        static final int INT_SOLID_DMND = 44;
        static final int INT_OPEN_DMND = 45;
        static final int INT_DOT_DMND = 46;
        static final int INT_PLAID = 47;
        static final int INT_SPHERE = 48;
        static final int INT_WEAVE = 49;
        static final int INT_DIVOT = 50;
        static final int INT_SHINGLE = 51;
        static final int INT_WAVE = 52;
        static final int INT_TRELLIS = 53;
        static final int INT_ZIG_ZAG = 54;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("pct5", INT_PCT_5),
            new Enum("pct10", INT_PCT_10),
            new Enum("pct20", INT_PCT_20),
            new Enum("pct25", INT_PCT_25),
            new Enum("pct30", INT_PCT_30),
            new Enum("pct40", INT_PCT_40),
            new Enum("pct50", INT_PCT_50),
            new Enum("pct60", INT_PCT_60),
            new Enum("pct70", INT_PCT_70),
            new Enum("pct75", INT_PCT_75),
            new Enum("pct80", INT_PCT_80),
            new Enum("pct90", INT_PCT_90),
            new Enum("horz", INT_HORZ),
            new Enum("vert", INT_VERT),
            new Enum("ltHorz", INT_LT_HORZ),
            new Enum("ltVert", INT_LT_VERT),
            new Enum("dkHorz", INT_DK_HORZ),
            new Enum("dkVert", INT_DK_VERT),
            new Enum("narHorz", INT_NAR_HORZ),
            new Enum("narVert", INT_NAR_VERT),
            new Enum("dashHorz", INT_DASH_HORZ),
            new Enum("dashVert", INT_DASH_VERT),
            new Enum("cross", INT_CROSS),
            new Enum("dnDiag", INT_DN_DIAG),
            new Enum("upDiag", INT_UP_DIAG),
            new Enum("ltDnDiag", INT_LT_DN_DIAG),
            new Enum("ltUpDiag", INT_LT_UP_DIAG),
            new Enum("dkDnDiag", INT_DK_DN_DIAG),
            new Enum("dkUpDiag", INT_DK_UP_DIAG),
            new Enum("wdDnDiag", INT_WD_DN_DIAG),
            new Enum("wdUpDiag", INT_WD_UP_DIAG),
            new Enum("dashDnDiag", INT_DASH_DN_DIAG),
            new Enum("dashUpDiag", INT_DASH_UP_DIAG),
            new Enum("diagCross", INT_DIAG_CROSS),
            new Enum("smCheck", INT_SM_CHECK),
            new Enum("lgCheck", INT_LG_CHECK),
            new Enum("smGrid", INT_SM_GRID),
            new Enum("lgGrid", INT_LG_GRID),
            new Enum("dotGrid", INT_DOT_GRID),
            new Enum("smConfetti", INT_SM_CONFETTI),
            new Enum("lgConfetti", INT_LG_CONFETTI),
            new Enum("horzBrick", INT_HORZ_BRICK),
            new Enum("diagBrick", INT_DIAG_BRICK),
            new Enum("solidDmnd", INT_SOLID_DMND),
            new Enum("openDmnd", INT_OPEN_DMND),
            new Enum("dotDmnd", INT_DOT_DMND),
            new Enum("plaid", INT_PLAID),
            new Enum("sphere", INT_SPHERE),
            new Enum("weave", INT_WEAVE),
            new Enum("divot", INT_DIVOT),
            new Enum("shingle", INT_SHINGLE),
            new Enum("wave", INT_WAVE),
            new Enum("trellis", INT_TRELLIS),
            new Enum("zigZag", INT_ZIG_ZAG),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
