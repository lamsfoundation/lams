/*
 * XML Type:  ST_ConstraintType
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STConstraintType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ConstraintType(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STConstraintType.
 */
public interface STConstraintType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STConstraintType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stconstrainttype098atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NONE = Enum.forString("none");
    Enum ALIGN_OFF = Enum.forString("alignOff");
    Enum BEG_MARG = Enum.forString("begMarg");
    Enum BEND_DIST = Enum.forString("bendDist");
    Enum BEG_PAD = Enum.forString("begPad");
    Enum B = Enum.forString("b");
    Enum B_MARG = Enum.forString("bMarg");
    Enum B_OFF = Enum.forString("bOff");
    Enum CTR_X = Enum.forString("ctrX");
    Enum CTR_X_OFF = Enum.forString("ctrXOff");
    Enum CTR_Y = Enum.forString("ctrY");
    Enum CTR_Y_OFF = Enum.forString("ctrYOff");
    Enum CONN_DIST = Enum.forString("connDist");
    Enum DIAM = Enum.forString("diam");
    Enum END_MARG = Enum.forString("endMarg");
    Enum END_PAD = Enum.forString("endPad");
    Enum H = Enum.forString("h");
    Enum H_AR_H = Enum.forString("hArH");
    Enum H_OFF = Enum.forString("hOff");
    Enum L = Enum.forString("l");
    Enum L_MARG = Enum.forString("lMarg");
    Enum L_OFF = Enum.forString("lOff");
    Enum R = Enum.forString("r");
    Enum R_MARG = Enum.forString("rMarg");
    Enum R_OFF = Enum.forString("rOff");
    Enum PRIM_FONT_SZ = Enum.forString("primFontSz");
    Enum PYRA_ACCT_RATIO = Enum.forString("pyraAcctRatio");
    Enum SEC_FONT_SZ = Enum.forString("secFontSz");
    Enum SIB_SP = Enum.forString("sibSp");
    Enum SEC_SIB_SP = Enum.forString("secSibSp");
    Enum SP = Enum.forString("sp");
    Enum STEM_THICK = Enum.forString("stemThick");
    Enum T = Enum.forString("t");
    Enum T_MARG = Enum.forString("tMarg");
    Enum T_OFF = Enum.forString("tOff");
    Enum USER_A = Enum.forString("userA");
    Enum USER_B = Enum.forString("userB");
    Enum USER_C = Enum.forString("userC");
    Enum USER_D = Enum.forString("userD");
    Enum USER_E = Enum.forString("userE");
    Enum USER_F = Enum.forString("userF");
    Enum USER_G = Enum.forString("userG");
    Enum USER_H = Enum.forString("userH");
    Enum USER_I = Enum.forString("userI");
    Enum USER_J = Enum.forString("userJ");
    Enum USER_K = Enum.forString("userK");
    Enum USER_L = Enum.forString("userL");
    Enum USER_M = Enum.forString("userM");
    Enum USER_N = Enum.forString("userN");
    Enum USER_O = Enum.forString("userO");
    Enum USER_P = Enum.forString("userP");
    Enum USER_Q = Enum.forString("userQ");
    Enum USER_R = Enum.forString("userR");
    Enum USER_S = Enum.forString("userS");
    Enum USER_T = Enum.forString("userT");
    Enum USER_U = Enum.forString("userU");
    Enum USER_V = Enum.forString("userV");
    Enum USER_W = Enum.forString("userW");
    Enum USER_X = Enum.forString("userX");
    Enum USER_Y = Enum.forString("userY");
    Enum USER_Z = Enum.forString("userZ");
    Enum W = Enum.forString("w");
    Enum W_AR_H = Enum.forString("wArH");
    Enum W_OFF = Enum.forString("wOff");

    int INT_NONE = Enum.INT_NONE;
    int INT_ALIGN_OFF = Enum.INT_ALIGN_OFF;
    int INT_BEG_MARG = Enum.INT_BEG_MARG;
    int INT_BEND_DIST = Enum.INT_BEND_DIST;
    int INT_BEG_PAD = Enum.INT_BEG_PAD;
    int INT_B = Enum.INT_B;
    int INT_B_MARG = Enum.INT_B_MARG;
    int INT_B_OFF = Enum.INT_B_OFF;
    int INT_CTR_X = Enum.INT_CTR_X;
    int INT_CTR_X_OFF = Enum.INT_CTR_X_OFF;
    int INT_CTR_Y = Enum.INT_CTR_Y;
    int INT_CTR_Y_OFF = Enum.INT_CTR_Y_OFF;
    int INT_CONN_DIST = Enum.INT_CONN_DIST;
    int INT_DIAM = Enum.INT_DIAM;
    int INT_END_MARG = Enum.INT_END_MARG;
    int INT_END_PAD = Enum.INT_END_PAD;
    int INT_H = Enum.INT_H;
    int INT_H_AR_H = Enum.INT_H_AR_H;
    int INT_H_OFF = Enum.INT_H_OFF;
    int INT_L = Enum.INT_L;
    int INT_L_MARG = Enum.INT_L_MARG;
    int INT_L_OFF = Enum.INT_L_OFF;
    int INT_R = Enum.INT_R;
    int INT_R_MARG = Enum.INT_R_MARG;
    int INT_R_OFF = Enum.INT_R_OFF;
    int INT_PRIM_FONT_SZ = Enum.INT_PRIM_FONT_SZ;
    int INT_PYRA_ACCT_RATIO = Enum.INT_PYRA_ACCT_RATIO;
    int INT_SEC_FONT_SZ = Enum.INT_SEC_FONT_SZ;
    int INT_SIB_SP = Enum.INT_SIB_SP;
    int INT_SEC_SIB_SP = Enum.INT_SEC_SIB_SP;
    int INT_SP = Enum.INT_SP;
    int INT_STEM_THICK = Enum.INT_STEM_THICK;
    int INT_T = Enum.INT_T;
    int INT_T_MARG = Enum.INT_T_MARG;
    int INT_T_OFF = Enum.INT_T_OFF;
    int INT_USER_A = Enum.INT_USER_A;
    int INT_USER_B = Enum.INT_USER_B;
    int INT_USER_C = Enum.INT_USER_C;
    int INT_USER_D = Enum.INT_USER_D;
    int INT_USER_E = Enum.INT_USER_E;
    int INT_USER_F = Enum.INT_USER_F;
    int INT_USER_G = Enum.INT_USER_G;
    int INT_USER_H = Enum.INT_USER_H;
    int INT_USER_I = Enum.INT_USER_I;
    int INT_USER_J = Enum.INT_USER_J;
    int INT_USER_K = Enum.INT_USER_K;
    int INT_USER_L = Enum.INT_USER_L;
    int INT_USER_M = Enum.INT_USER_M;
    int INT_USER_N = Enum.INT_USER_N;
    int INT_USER_O = Enum.INT_USER_O;
    int INT_USER_P = Enum.INT_USER_P;
    int INT_USER_Q = Enum.INT_USER_Q;
    int INT_USER_R = Enum.INT_USER_R;
    int INT_USER_S = Enum.INT_USER_S;
    int INT_USER_T = Enum.INT_USER_T;
    int INT_USER_U = Enum.INT_USER_U;
    int INT_USER_V = Enum.INT_USER_V;
    int INT_USER_W = Enum.INT_USER_W;
    int INT_USER_X = Enum.INT_USER_X;
    int INT_USER_Y = Enum.INT_USER_Y;
    int INT_USER_Z = Enum.INT_USER_Z;
    int INT_W = Enum.INT_W;
    int INT_W_AR_H = Enum.INT_W_AR_H;
    int INT_W_OFF = Enum.INT_W_OFF;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STConstraintType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NONE
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

        static final int INT_NONE = 1;
        static final int INT_ALIGN_OFF = 2;
        static final int INT_BEG_MARG = 3;
        static final int INT_BEND_DIST = 4;
        static final int INT_BEG_PAD = 5;
        static final int INT_B = 6;
        static final int INT_B_MARG = 7;
        static final int INT_B_OFF = 8;
        static final int INT_CTR_X = 9;
        static final int INT_CTR_X_OFF = 10;
        static final int INT_CTR_Y = 11;
        static final int INT_CTR_Y_OFF = 12;
        static final int INT_CONN_DIST = 13;
        static final int INT_DIAM = 14;
        static final int INT_END_MARG = 15;
        static final int INT_END_PAD = 16;
        static final int INT_H = 17;
        static final int INT_H_AR_H = 18;
        static final int INT_H_OFF = 19;
        static final int INT_L = 20;
        static final int INT_L_MARG = 21;
        static final int INT_L_OFF = 22;
        static final int INT_R = 23;
        static final int INT_R_MARG = 24;
        static final int INT_R_OFF = 25;
        static final int INT_PRIM_FONT_SZ = 26;
        static final int INT_PYRA_ACCT_RATIO = 27;
        static final int INT_SEC_FONT_SZ = 28;
        static final int INT_SIB_SP = 29;
        static final int INT_SEC_SIB_SP = 30;
        static final int INT_SP = 31;
        static final int INT_STEM_THICK = 32;
        static final int INT_T = 33;
        static final int INT_T_MARG = 34;
        static final int INT_T_OFF = 35;
        static final int INT_USER_A = 36;
        static final int INT_USER_B = 37;
        static final int INT_USER_C = 38;
        static final int INT_USER_D = 39;
        static final int INT_USER_E = 40;
        static final int INT_USER_F = 41;
        static final int INT_USER_G = 42;
        static final int INT_USER_H = 43;
        static final int INT_USER_I = 44;
        static final int INT_USER_J = 45;
        static final int INT_USER_K = 46;
        static final int INT_USER_L = 47;
        static final int INT_USER_M = 48;
        static final int INT_USER_N = 49;
        static final int INT_USER_O = 50;
        static final int INT_USER_P = 51;
        static final int INT_USER_Q = 52;
        static final int INT_USER_R = 53;
        static final int INT_USER_S = 54;
        static final int INT_USER_T = 55;
        static final int INT_USER_U = 56;
        static final int INT_USER_V = 57;
        static final int INT_USER_W = 58;
        static final int INT_USER_X = 59;
        static final int INT_USER_Y = 60;
        static final int INT_USER_Z = 61;
        static final int INT_W = 62;
        static final int INT_W_AR_H = 63;
        static final int INT_W_OFF = 64;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("none", INT_NONE),
            new Enum("alignOff", INT_ALIGN_OFF),
            new Enum("begMarg", INT_BEG_MARG),
            new Enum("bendDist", INT_BEND_DIST),
            new Enum("begPad", INT_BEG_PAD),
            new Enum("b", INT_B),
            new Enum("bMarg", INT_B_MARG),
            new Enum("bOff", INT_B_OFF),
            new Enum("ctrX", INT_CTR_X),
            new Enum("ctrXOff", INT_CTR_X_OFF),
            new Enum("ctrY", INT_CTR_Y),
            new Enum("ctrYOff", INT_CTR_Y_OFF),
            new Enum("connDist", INT_CONN_DIST),
            new Enum("diam", INT_DIAM),
            new Enum("endMarg", INT_END_MARG),
            new Enum("endPad", INT_END_PAD),
            new Enum("h", INT_H),
            new Enum("hArH", INT_H_AR_H),
            new Enum("hOff", INT_H_OFF),
            new Enum("l", INT_L),
            new Enum("lMarg", INT_L_MARG),
            new Enum("lOff", INT_L_OFF),
            new Enum("r", INT_R),
            new Enum("rMarg", INT_R_MARG),
            new Enum("rOff", INT_R_OFF),
            new Enum("primFontSz", INT_PRIM_FONT_SZ),
            new Enum("pyraAcctRatio", INT_PYRA_ACCT_RATIO),
            new Enum("secFontSz", INT_SEC_FONT_SZ),
            new Enum("sibSp", INT_SIB_SP),
            new Enum("secSibSp", INT_SEC_SIB_SP),
            new Enum("sp", INT_SP),
            new Enum("stemThick", INT_STEM_THICK),
            new Enum("t", INT_T),
            new Enum("tMarg", INT_T_MARG),
            new Enum("tOff", INT_T_OFF),
            new Enum("userA", INT_USER_A),
            new Enum("userB", INT_USER_B),
            new Enum("userC", INT_USER_C),
            new Enum("userD", INT_USER_D),
            new Enum("userE", INT_USER_E),
            new Enum("userF", INT_USER_F),
            new Enum("userG", INT_USER_G),
            new Enum("userH", INT_USER_H),
            new Enum("userI", INT_USER_I),
            new Enum("userJ", INT_USER_J),
            new Enum("userK", INT_USER_K),
            new Enum("userL", INT_USER_L),
            new Enum("userM", INT_USER_M),
            new Enum("userN", INT_USER_N),
            new Enum("userO", INT_USER_O),
            new Enum("userP", INT_USER_P),
            new Enum("userQ", INT_USER_Q),
            new Enum("userR", INT_USER_R),
            new Enum("userS", INT_USER_S),
            new Enum("userT", INT_USER_T),
            new Enum("userU", INT_USER_U),
            new Enum("userV", INT_USER_V),
            new Enum("userW", INT_USER_W),
            new Enum("userX", INT_USER_X),
            new Enum("userY", INT_USER_Y),
            new Enum("userZ", INT_USER_Z),
            new Enum("w", INT_W),
            new Enum("wArH", INT_W_AR_H),
            new Enum("wOff", INT_W_OFF),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
