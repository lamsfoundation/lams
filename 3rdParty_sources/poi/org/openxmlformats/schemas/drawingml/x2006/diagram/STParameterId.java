/*
 * XML Type:  ST_ParameterId
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterId
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ParameterId(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterId.
 */
public interface STParameterId extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterId> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stparameterid4fc7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum HORZ_ALIGN = Enum.forString("horzAlign");
    Enum VERT_ALIGN = Enum.forString("vertAlign");
    Enum CH_DIR = Enum.forString("chDir");
    Enum CH_ALIGN = Enum.forString("chAlign");
    Enum SEC_CH_ALIGN = Enum.forString("secChAlign");
    Enum LIN_DIR = Enum.forString("linDir");
    Enum SEC_LIN_DIR = Enum.forString("secLinDir");
    Enum ST_ELEM = Enum.forString("stElem");
    Enum BEND_PT = Enum.forString("bendPt");
    Enum CONN_ROUT = Enum.forString("connRout");
    Enum BEG_STY = Enum.forString("begSty");
    Enum END_STY = Enum.forString("endSty");
    Enum DIM = Enum.forString("dim");
    Enum ROT_PATH = Enum.forString("rotPath");
    Enum CTR_SHP_MAP = Enum.forString("ctrShpMap");
    Enum NODE_HORZ_ALIGN = Enum.forString("nodeHorzAlign");
    Enum NODE_VERT_ALIGN = Enum.forString("nodeVertAlign");
    Enum FALLBACK = Enum.forString("fallback");
    Enum TX_DIR = Enum.forString("txDir");
    Enum PYRA_ACCT_POS = Enum.forString("pyraAcctPos");
    Enum PYRA_ACCT_TX_MAR = Enum.forString("pyraAcctTxMar");
    Enum TX_BL_DIR = Enum.forString("txBlDir");
    Enum TX_ANCHOR_HORZ = Enum.forString("txAnchorHorz");
    Enum TX_ANCHOR_VERT = Enum.forString("txAnchorVert");
    Enum TX_ANCHOR_HORZ_CH = Enum.forString("txAnchorHorzCh");
    Enum TX_ANCHOR_VERT_CH = Enum.forString("txAnchorVertCh");
    Enum PAR_TX_LTR_ALIGN = Enum.forString("parTxLTRAlign");
    Enum PAR_TX_RTL_ALIGN = Enum.forString("parTxRTLAlign");
    Enum SHP_TX_LTR_ALIGN_CH = Enum.forString("shpTxLTRAlignCh");
    Enum SHP_TX_RTL_ALIGN_CH = Enum.forString("shpTxRTLAlignCh");
    Enum AUTO_TX_ROT = Enum.forString("autoTxRot");
    Enum GR_DIR = Enum.forString("grDir");
    Enum FLOW_DIR = Enum.forString("flowDir");
    Enum CONT_DIR = Enum.forString("contDir");
    Enum BKPT = Enum.forString("bkpt");
    Enum OFF = Enum.forString("off");
    Enum HIER_ALIGN = Enum.forString("hierAlign");
    Enum BK_PT_FIXED_VAL = Enum.forString("bkPtFixedVal");
    Enum ST_BULLET_LVL = Enum.forString("stBulletLvl");
    Enum ST_ANG = Enum.forString("stAng");
    Enum SPAN_ANG = Enum.forString("spanAng");
    Enum AR = Enum.forString("ar");
    Enum LN_SP_PAR = Enum.forString("lnSpPar");
    Enum LN_SP_AF_PAR_P = Enum.forString("lnSpAfParP");
    Enum LN_SP_CH = Enum.forString("lnSpCh");
    Enum LN_SP_AF_CH_P = Enum.forString("lnSpAfChP");
    Enum RT_SHORT_DIST = Enum.forString("rtShortDist");
    Enum ALIGN_TX = Enum.forString("alignTx");
    Enum PYRA_LVL_NODE = Enum.forString("pyraLvlNode");
    Enum PYRA_ACCT_BKGD_NODE = Enum.forString("pyraAcctBkgdNode");
    Enum PYRA_ACCT_TX_NODE = Enum.forString("pyraAcctTxNode");
    Enum SRC_NODE = Enum.forString("srcNode");
    Enum DST_NODE = Enum.forString("dstNode");
    Enum BEG_PTS = Enum.forString("begPts");
    Enum END_PTS = Enum.forString("endPts");

    int INT_HORZ_ALIGN = Enum.INT_HORZ_ALIGN;
    int INT_VERT_ALIGN = Enum.INT_VERT_ALIGN;
    int INT_CH_DIR = Enum.INT_CH_DIR;
    int INT_CH_ALIGN = Enum.INT_CH_ALIGN;
    int INT_SEC_CH_ALIGN = Enum.INT_SEC_CH_ALIGN;
    int INT_LIN_DIR = Enum.INT_LIN_DIR;
    int INT_SEC_LIN_DIR = Enum.INT_SEC_LIN_DIR;
    int INT_ST_ELEM = Enum.INT_ST_ELEM;
    int INT_BEND_PT = Enum.INT_BEND_PT;
    int INT_CONN_ROUT = Enum.INT_CONN_ROUT;
    int INT_BEG_STY = Enum.INT_BEG_STY;
    int INT_END_STY = Enum.INT_END_STY;
    int INT_DIM = Enum.INT_DIM;
    int INT_ROT_PATH = Enum.INT_ROT_PATH;
    int INT_CTR_SHP_MAP = Enum.INT_CTR_SHP_MAP;
    int INT_NODE_HORZ_ALIGN = Enum.INT_NODE_HORZ_ALIGN;
    int INT_NODE_VERT_ALIGN = Enum.INT_NODE_VERT_ALIGN;
    int INT_FALLBACK = Enum.INT_FALLBACK;
    int INT_TX_DIR = Enum.INT_TX_DIR;
    int INT_PYRA_ACCT_POS = Enum.INT_PYRA_ACCT_POS;
    int INT_PYRA_ACCT_TX_MAR = Enum.INT_PYRA_ACCT_TX_MAR;
    int INT_TX_BL_DIR = Enum.INT_TX_BL_DIR;
    int INT_TX_ANCHOR_HORZ = Enum.INT_TX_ANCHOR_HORZ;
    int INT_TX_ANCHOR_VERT = Enum.INT_TX_ANCHOR_VERT;
    int INT_TX_ANCHOR_HORZ_CH = Enum.INT_TX_ANCHOR_HORZ_CH;
    int INT_TX_ANCHOR_VERT_CH = Enum.INT_TX_ANCHOR_VERT_CH;
    int INT_PAR_TX_LTR_ALIGN = Enum.INT_PAR_TX_LTR_ALIGN;
    int INT_PAR_TX_RTL_ALIGN = Enum.INT_PAR_TX_RTL_ALIGN;
    int INT_SHP_TX_LTR_ALIGN_CH = Enum.INT_SHP_TX_LTR_ALIGN_CH;
    int INT_SHP_TX_RTL_ALIGN_CH = Enum.INT_SHP_TX_RTL_ALIGN_CH;
    int INT_AUTO_TX_ROT = Enum.INT_AUTO_TX_ROT;
    int INT_GR_DIR = Enum.INT_GR_DIR;
    int INT_FLOW_DIR = Enum.INT_FLOW_DIR;
    int INT_CONT_DIR = Enum.INT_CONT_DIR;
    int INT_BKPT = Enum.INT_BKPT;
    int INT_OFF = Enum.INT_OFF;
    int INT_HIER_ALIGN = Enum.INT_HIER_ALIGN;
    int INT_BK_PT_FIXED_VAL = Enum.INT_BK_PT_FIXED_VAL;
    int INT_ST_BULLET_LVL = Enum.INT_ST_BULLET_LVL;
    int INT_ST_ANG = Enum.INT_ST_ANG;
    int INT_SPAN_ANG = Enum.INT_SPAN_ANG;
    int INT_AR = Enum.INT_AR;
    int INT_LN_SP_PAR = Enum.INT_LN_SP_PAR;
    int INT_LN_SP_AF_PAR_P = Enum.INT_LN_SP_AF_PAR_P;
    int INT_LN_SP_CH = Enum.INT_LN_SP_CH;
    int INT_LN_SP_AF_CH_P = Enum.INT_LN_SP_AF_CH_P;
    int INT_RT_SHORT_DIST = Enum.INT_RT_SHORT_DIST;
    int INT_ALIGN_TX = Enum.INT_ALIGN_TX;
    int INT_PYRA_LVL_NODE = Enum.INT_PYRA_LVL_NODE;
    int INT_PYRA_ACCT_BKGD_NODE = Enum.INT_PYRA_ACCT_BKGD_NODE;
    int INT_PYRA_ACCT_TX_NODE = Enum.INT_PYRA_ACCT_TX_NODE;
    int INT_SRC_NODE = Enum.INT_SRC_NODE;
    int INT_DST_NODE = Enum.INT_DST_NODE;
    int INT_BEG_PTS = Enum.INT_BEG_PTS;
    int INT_END_PTS = Enum.INT_END_PTS;

    /**
     * Enumeration value class for org.openxmlformats.schemas.drawingml.x2006.diagram.STParameterId.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_HORZ_ALIGN
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

        static final int INT_HORZ_ALIGN = 1;
        static final int INT_VERT_ALIGN = 2;
        static final int INT_CH_DIR = 3;
        static final int INT_CH_ALIGN = 4;
        static final int INT_SEC_CH_ALIGN = 5;
        static final int INT_LIN_DIR = 6;
        static final int INT_SEC_LIN_DIR = 7;
        static final int INT_ST_ELEM = 8;
        static final int INT_BEND_PT = 9;
        static final int INT_CONN_ROUT = 10;
        static final int INT_BEG_STY = 11;
        static final int INT_END_STY = 12;
        static final int INT_DIM = 13;
        static final int INT_ROT_PATH = 14;
        static final int INT_CTR_SHP_MAP = 15;
        static final int INT_NODE_HORZ_ALIGN = 16;
        static final int INT_NODE_VERT_ALIGN = 17;
        static final int INT_FALLBACK = 18;
        static final int INT_TX_DIR = 19;
        static final int INT_PYRA_ACCT_POS = 20;
        static final int INT_PYRA_ACCT_TX_MAR = 21;
        static final int INT_TX_BL_DIR = 22;
        static final int INT_TX_ANCHOR_HORZ = 23;
        static final int INT_TX_ANCHOR_VERT = 24;
        static final int INT_TX_ANCHOR_HORZ_CH = 25;
        static final int INT_TX_ANCHOR_VERT_CH = 26;
        static final int INT_PAR_TX_LTR_ALIGN = 27;
        static final int INT_PAR_TX_RTL_ALIGN = 28;
        static final int INT_SHP_TX_LTR_ALIGN_CH = 29;
        static final int INT_SHP_TX_RTL_ALIGN_CH = 30;
        static final int INT_AUTO_TX_ROT = 31;
        static final int INT_GR_DIR = 32;
        static final int INT_FLOW_DIR = 33;
        static final int INT_CONT_DIR = 34;
        static final int INT_BKPT = 35;
        static final int INT_OFF = 36;
        static final int INT_HIER_ALIGN = 37;
        static final int INT_BK_PT_FIXED_VAL = 38;
        static final int INT_ST_BULLET_LVL = 39;
        static final int INT_ST_ANG = 40;
        static final int INT_SPAN_ANG = 41;
        static final int INT_AR = 42;
        static final int INT_LN_SP_PAR = 43;
        static final int INT_LN_SP_AF_PAR_P = 44;
        static final int INT_LN_SP_CH = 45;
        static final int INT_LN_SP_AF_CH_P = 46;
        static final int INT_RT_SHORT_DIST = 47;
        static final int INT_ALIGN_TX = 48;
        static final int INT_PYRA_LVL_NODE = 49;
        static final int INT_PYRA_ACCT_BKGD_NODE = 50;
        static final int INT_PYRA_ACCT_TX_NODE = 51;
        static final int INT_SRC_NODE = 52;
        static final int INT_DST_NODE = 53;
        static final int INT_BEG_PTS = 54;
        static final int INT_END_PTS = 55;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("horzAlign", INT_HORZ_ALIGN),
            new Enum("vertAlign", INT_VERT_ALIGN),
            new Enum("chDir", INT_CH_DIR),
            new Enum("chAlign", INT_CH_ALIGN),
            new Enum("secChAlign", INT_SEC_CH_ALIGN),
            new Enum("linDir", INT_LIN_DIR),
            new Enum("secLinDir", INT_SEC_LIN_DIR),
            new Enum("stElem", INT_ST_ELEM),
            new Enum("bendPt", INT_BEND_PT),
            new Enum("connRout", INT_CONN_ROUT),
            new Enum("begSty", INT_BEG_STY),
            new Enum("endSty", INT_END_STY),
            new Enum("dim", INT_DIM),
            new Enum("rotPath", INT_ROT_PATH),
            new Enum("ctrShpMap", INT_CTR_SHP_MAP),
            new Enum("nodeHorzAlign", INT_NODE_HORZ_ALIGN),
            new Enum("nodeVertAlign", INT_NODE_VERT_ALIGN),
            new Enum("fallback", INT_FALLBACK),
            new Enum("txDir", INT_TX_DIR),
            new Enum("pyraAcctPos", INT_PYRA_ACCT_POS),
            new Enum("pyraAcctTxMar", INT_PYRA_ACCT_TX_MAR),
            new Enum("txBlDir", INT_TX_BL_DIR),
            new Enum("txAnchorHorz", INT_TX_ANCHOR_HORZ),
            new Enum("txAnchorVert", INT_TX_ANCHOR_VERT),
            new Enum("txAnchorHorzCh", INT_TX_ANCHOR_HORZ_CH),
            new Enum("txAnchorVertCh", INT_TX_ANCHOR_VERT_CH),
            new Enum("parTxLTRAlign", INT_PAR_TX_LTR_ALIGN),
            new Enum("parTxRTLAlign", INT_PAR_TX_RTL_ALIGN),
            new Enum("shpTxLTRAlignCh", INT_SHP_TX_LTR_ALIGN_CH),
            new Enum("shpTxRTLAlignCh", INT_SHP_TX_RTL_ALIGN_CH),
            new Enum("autoTxRot", INT_AUTO_TX_ROT),
            new Enum("grDir", INT_GR_DIR),
            new Enum("flowDir", INT_FLOW_DIR),
            new Enum("contDir", INT_CONT_DIR),
            new Enum("bkpt", INT_BKPT),
            new Enum("off", INT_OFF),
            new Enum("hierAlign", INT_HIER_ALIGN),
            new Enum("bkPtFixedVal", INT_BK_PT_FIXED_VAL),
            new Enum("stBulletLvl", INT_ST_BULLET_LVL),
            new Enum("stAng", INT_ST_ANG),
            new Enum("spanAng", INT_SPAN_ANG),
            new Enum("ar", INT_AR),
            new Enum("lnSpPar", INT_LN_SP_PAR),
            new Enum("lnSpAfParP", INT_LN_SP_AF_PAR_P),
            new Enum("lnSpCh", INT_LN_SP_CH),
            new Enum("lnSpAfChP", INT_LN_SP_AF_CH_P),
            new Enum("rtShortDist", INT_RT_SHORT_DIST),
            new Enum("alignTx", INT_ALIGN_TX),
            new Enum("pyraLvlNode", INT_PYRA_LVL_NODE),
            new Enum("pyraAcctBkgdNode", INT_PYRA_ACCT_BKGD_NODE),
            new Enum("pyraAcctTxNode", INT_PYRA_ACCT_TX_NODE),
            new Enum("srcNode", INT_SRC_NODE),
            new Enum("dstNode", INT_DST_NODE),
            new Enum("begPts", INT_BEG_PTS),
            new Enum("endPts", INT_END_PTS),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
