/*
 * XML Type:  CT_Compat
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Compat(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTCompatImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat {
    private static final long serialVersionUID = 1L;

    public CTCompatImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useSingleBorderforContiguousCells"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wpJustification"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noTabHangInd"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noLeading"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "spaceForUL"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noColumnBalance"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "balanceSingleByteDoubleByteWidth"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noExtraLineSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotLeaveBackslashAlone"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ulTrailSpace"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotExpandShiftReturn"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "spacingInWholePoints"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "lineWrapLikeWord6"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printBodyTextBeforeHeader"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printColBlack"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wpSpaceWidth"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "showBreaksInFrames"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "subFontBySize"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressBottomSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressTopSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressSpacingAtTopOfPage"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressTopSpacingWP"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "suppressSpBfAfterPgBrk"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "swapBordersFacingPages"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "convMailMergeEsc"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "truncateFontHeightsLikeWP6"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mwSmallCaps"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "usePrinterMetrics"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotSuppressParagraphBorders"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "wrapTrailSpaces"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnoteLayoutLikeWW8"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shapeLayoutLikeWW8"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "alignTablesRowByRow"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "forgetLastTabAlignment"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "adjustLineHeightInTable"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoSpaceLikeWord95"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noSpaceRaiseLower"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotUseHTMLParagraphAutoSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "layoutRawTableWidth"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "layoutTableRowsApart"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useWord97LineBreakRules"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotBreakWrappedTables"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotSnapToGridInCell"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "selectFldWithFirstOrLastChar"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "applyBreakingRules"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotWrapTextWithPunct"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotUseEastAsianBreakRules"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useWord2002TableStyleRules"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "growAutofit"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useFELayout"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useNormalStyleForList"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotUseIndentAsNumberingTabStop"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useAltKinsokuLineBreakRules"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "allowSpaceOfSameStyleInTable"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotSuppressIndentation"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotAutofitConstrainedTables"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autofitToFirstFixedWidthCell"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "underlineTabInNumList"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "displayHangulFixedWidth"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "splitPgBreakAndParaMark"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotVertAlignCellWithSp"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotBreakConstrainedForcedTable"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotVertAlignInTxbx"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useAnsiKerningPairs"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "cachedColBalance"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "compatSetting"),
    };


    /**
     * Gets the "useSingleBorderforContiguousCells" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseSingleBorderforContiguousCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useSingleBorderforContiguousCells" element
     */
    @Override
    public boolean isSetUseSingleBorderforContiguousCells() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "useSingleBorderforContiguousCells" element
     */
    @Override
    public void setUseSingleBorderforContiguousCells(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useSingleBorderforContiguousCells) {
        generatedSetterHelperImpl(useSingleBorderforContiguousCells, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useSingleBorderforContiguousCells" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseSingleBorderforContiguousCells() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "useSingleBorderforContiguousCells" element
     */
    @Override
    public void unsetUseSingleBorderforContiguousCells() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "wpJustification" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWpJustification() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wpJustification" element
     */
    @Override
    public boolean isSetWpJustification() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "wpJustification" element
     */
    @Override
    public void setWpJustification(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff wpJustification) {
        generatedSetterHelperImpl(wpJustification, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wpJustification" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWpJustification() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "wpJustification" element
     */
    @Override
    public void unsetWpJustification() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "noTabHangInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoTabHangInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noTabHangInd" element
     */
    @Override
    public boolean isSetNoTabHangInd() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "noTabHangInd" element
     */
    @Override
    public void setNoTabHangInd(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noTabHangInd) {
        generatedSetterHelperImpl(noTabHangInd, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noTabHangInd" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoTabHangInd() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "noTabHangInd" element
     */
    @Override
    public void unsetNoTabHangInd() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "noLeading" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoLeading() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noLeading" element
     */
    @Override
    public boolean isSetNoLeading() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "noLeading" element
     */
    @Override
    public void setNoLeading(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noLeading) {
        generatedSetterHelperImpl(noLeading, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noLeading" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoLeading() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "noLeading" element
     */
    @Override
    public void unsetNoLeading() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "spaceForUL" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSpaceForUL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spaceForUL" element
     */
    @Override
    public boolean isSetSpaceForUL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "spaceForUL" element
     */
    @Override
    public void setSpaceForUL(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff spaceForUL) {
        generatedSetterHelperImpl(spaceForUL, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spaceForUL" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSpaceForUL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "spaceForUL" element
     */
    @Override
    public void unsetSpaceForUL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "noColumnBalance" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoColumnBalance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noColumnBalance" element
     */
    @Override
    public boolean isSetNoColumnBalance() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "noColumnBalance" element
     */
    @Override
    public void setNoColumnBalance(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noColumnBalance) {
        generatedSetterHelperImpl(noColumnBalance, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noColumnBalance" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoColumnBalance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "noColumnBalance" element
     */
    @Override
    public void unsetNoColumnBalance() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "balanceSingleByteDoubleByteWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBalanceSingleByteDoubleByteWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "balanceSingleByteDoubleByteWidth" element
     */
    @Override
    public boolean isSetBalanceSingleByteDoubleByteWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "balanceSingleByteDoubleByteWidth" element
     */
    @Override
    public void setBalanceSingleByteDoubleByteWidth(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff balanceSingleByteDoubleByteWidth) {
        generatedSetterHelperImpl(balanceSingleByteDoubleByteWidth, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "balanceSingleByteDoubleByteWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBalanceSingleByteDoubleByteWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "balanceSingleByteDoubleByteWidth" element
     */
    @Override
    public void unsetBalanceSingleByteDoubleByteWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "noExtraLineSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoExtraLineSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noExtraLineSpacing" element
     */
    @Override
    public boolean isSetNoExtraLineSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "noExtraLineSpacing" element
     */
    @Override
    public void setNoExtraLineSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noExtraLineSpacing) {
        generatedSetterHelperImpl(noExtraLineSpacing, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noExtraLineSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoExtraLineSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "noExtraLineSpacing" element
     */
    @Override
    public void unsetNoExtraLineSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "doNotLeaveBackslashAlone" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotLeaveBackslashAlone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotLeaveBackslashAlone" element
     */
    @Override
    public boolean isSetDoNotLeaveBackslashAlone() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "doNotLeaveBackslashAlone" element
     */
    @Override
    public void setDoNotLeaveBackslashAlone(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotLeaveBackslashAlone) {
        generatedSetterHelperImpl(doNotLeaveBackslashAlone, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotLeaveBackslashAlone" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotLeaveBackslashAlone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "doNotLeaveBackslashAlone" element
     */
    @Override
    public void unsetDoNotLeaveBackslashAlone() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "ulTrailSpace" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUlTrailSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ulTrailSpace" element
     */
    @Override
    public boolean isSetUlTrailSpace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "ulTrailSpace" element
     */
    @Override
    public void setUlTrailSpace(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff ulTrailSpace) {
        generatedSetterHelperImpl(ulTrailSpace, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ulTrailSpace" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUlTrailSpace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "ulTrailSpace" element
     */
    @Override
    public void unsetUlTrailSpace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "doNotExpandShiftReturn" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotExpandShiftReturn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotExpandShiftReturn" element
     */
    @Override
    public boolean isSetDoNotExpandShiftReturn() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "doNotExpandShiftReturn" element
     */
    @Override
    public void setDoNotExpandShiftReturn(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotExpandShiftReturn) {
        generatedSetterHelperImpl(doNotExpandShiftReturn, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotExpandShiftReturn" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotExpandShiftReturn() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "doNotExpandShiftReturn" element
     */
    @Override
    public void unsetDoNotExpandShiftReturn() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "spacingInWholePoints" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSpacingInWholePoints() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spacingInWholePoints" element
     */
    @Override
    public boolean isSetSpacingInWholePoints() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "spacingInWholePoints" element
     */
    @Override
    public void setSpacingInWholePoints(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff spacingInWholePoints) {
        generatedSetterHelperImpl(spacingInWholePoints, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spacingInWholePoints" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSpacingInWholePoints() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "spacingInWholePoints" element
     */
    @Override
    public void unsetSpacingInWholePoints() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "lineWrapLikeWord6" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLineWrapLikeWord6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lineWrapLikeWord6" element
     */
    @Override
    public boolean isSetLineWrapLikeWord6() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "lineWrapLikeWord6" element
     */
    @Override
    public void setLineWrapLikeWord6(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff lineWrapLikeWord6) {
        generatedSetterHelperImpl(lineWrapLikeWord6, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lineWrapLikeWord6" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLineWrapLikeWord6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "lineWrapLikeWord6" element
     */
    @Override
    public void unsetLineWrapLikeWord6() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "printBodyTextBeforeHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintBodyTextBeforeHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printBodyTextBeforeHeader" element
     */
    @Override
    public boolean isSetPrintBodyTextBeforeHeader() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "printBodyTextBeforeHeader" element
     */
    @Override
    public void setPrintBodyTextBeforeHeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printBodyTextBeforeHeader) {
        generatedSetterHelperImpl(printBodyTextBeforeHeader, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printBodyTextBeforeHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintBodyTextBeforeHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "printBodyTextBeforeHeader" element
     */
    @Override
    public void unsetPrintBodyTextBeforeHeader() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "printColBlack" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintColBlack() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printColBlack" element
     */
    @Override
    public boolean isSetPrintColBlack() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "printColBlack" element
     */
    @Override
    public void setPrintColBlack(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printColBlack) {
        generatedSetterHelperImpl(printColBlack, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printColBlack" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintColBlack() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "printColBlack" element
     */
    @Override
    public void unsetPrintColBlack() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "wpSpaceWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWpSpaceWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wpSpaceWidth" element
     */
    @Override
    public boolean isSetWpSpaceWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "wpSpaceWidth" element
     */
    @Override
    public void setWpSpaceWidth(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff wpSpaceWidth) {
        generatedSetterHelperImpl(wpSpaceWidth, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wpSpaceWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWpSpaceWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "wpSpaceWidth" element
     */
    @Override
    public void unsetWpSpaceWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "showBreaksInFrames" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShowBreaksInFrames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showBreaksInFrames" element
     */
    @Override
    public boolean isSetShowBreaksInFrames() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "showBreaksInFrames" element
     */
    @Override
    public void setShowBreaksInFrames(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff showBreaksInFrames) {
        generatedSetterHelperImpl(showBreaksInFrames, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showBreaksInFrames" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShowBreaksInFrames() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "showBreaksInFrames" element
     */
    @Override
    public void unsetShowBreaksInFrames() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "subFontBySize" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSubFontBySize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "subFontBySize" element
     */
    @Override
    public boolean isSetSubFontBySize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "subFontBySize" element
     */
    @Override
    public void setSubFontBySize(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff subFontBySize) {
        generatedSetterHelperImpl(subFontBySize, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "subFontBySize" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSubFontBySize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "subFontBySize" element
     */
    @Override
    public void unsetSubFontBySize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "suppressBottomSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressBottomSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressBottomSpacing" element
     */
    @Override
    public boolean isSetSuppressBottomSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "suppressBottomSpacing" element
     */
    @Override
    public void setSuppressBottomSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressBottomSpacing) {
        generatedSetterHelperImpl(suppressBottomSpacing, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressBottomSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressBottomSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "suppressBottomSpacing" element
     */
    @Override
    public void unsetSuppressBottomSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "suppressTopSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressTopSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressTopSpacing" element
     */
    @Override
    public boolean isSetSuppressTopSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "suppressTopSpacing" element
     */
    @Override
    public void setSuppressTopSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressTopSpacing) {
        generatedSetterHelperImpl(suppressTopSpacing, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressTopSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressTopSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "suppressTopSpacing" element
     */
    @Override
    public void unsetSuppressTopSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "suppressSpacingAtTopOfPage" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressSpacingAtTopOfPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressSpacingAtTopOfPage" element
     */
    @Override
    public boolean isSetSuppressSpacingAtTopOfPage() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "suppressSpacingAtTopOfPage" element
     */
    @Override
    public void setSuppressSpacingAtTopOfPage(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressSpacingAtTopOfPage) {
        generatedSetterHelperImpl(suppressSpacingAtTopOfPage, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressSpacingAtTopOfPage" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressSpacingAtTopOfPage() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "suppressSpacingAtTopOfPage" element
     */
    @Override
    public void unsetSuppressSpacingAtTopOfPage() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "suppressTopSpacingWP" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressTopSpacingWP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressTopSpacingWP" element
     */
    @Override
    public boolean isSetSuppressTopSpacingWP() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "suppressTopSpacingWP" element
     */
    @Override
    public void setSuppressTopSpacingWP(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressTopSpacingWP) {
        generatedSetterHelperImpl(suppressTopSpacingWP, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressTopSpacingWP" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressTopSpacingWP() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "suppressTopSpacingWP" element
     */
    @Override
    public void unsetSuppressTopSpacingWP() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "suppressSpBfAfterPgBrk" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSuppressSpBfAfterPgBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "suppressSpBfAfterPgBrk" element
     */
    @Override
    public boolean isSetSuppressSpBfAfterPgBrk() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "suppressSpBfAfterPgBrk" element
     */
    @Override
    public void setSuppressSpBfAfterPgBrk(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff suppressSpBfAfterPgBrk) {
        generatedSetterHelperImpl(suppressSpBfAfterPgBrk, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "suppressSpBfAfterPgBrk" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSuppressSpBfAfterPgBrk() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "suppressSpBfAfterPgBrk" element
     */
    @Override
    public void unsetSuppressSpBfAfterPgBrk() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "swapBordersFacingPages" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSwapBordersFacingPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "swapBordersFacingPages" element
     */
    @Override
    public boolean isSetSwapBordersFacingPages() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "swapBordersFacingPages" element
     */
    @Override
    public void setSwapBordersFacingPages(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff swapBordersFacingPages) {
        generatedSetterHelperImpl(swapBordersFacingPages, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "swapBordersFacingPages" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSwapBordersFacingPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "swapBordersFacingPages" element
     */
    @Override
    public void unsetSwapBordersFacingPages() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "convMailMergeEsc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getConvMailMergeEsc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "convMailMergeEsc" element
     */
    @Override
    public boolean isSetConvMailMergeEsc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "convMailMergeEsc" element
     */
    @Override
    public void setConvMailMergeEsc(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff convMailMergeEsc) {
        generatedSetterHelperImpl(convMailMergeEsc, PROPERTY_QNAME[24], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "convMailMergeEsc" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewConvMailMergeEsc() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Unsets the "convMailMergeEsc" element
     */
    @Override
    public void unsetConvMailMergeEsc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "truncateFontHeightsLikeWP6" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTruncateFontHeightsLikeWP6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "truncateFontHeightsLikeWP6" element
     */
    @Override
    public boolean isSetTruncateFontHeightsLikeWP6() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "truncateFontHeightsLikeWP6" element
     */
    @Override
    public void setTruncateFontHeightsLikeWP6(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff truncateFontHeightsLikeWP6) {
        generatedSetterHelperImpl(truncateFontHeightsLikeWP6, PROPERTY_QNAME[25], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "truncateFontHeightsLikeWP6" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTruncateFontHeightsLikeWP6() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Unsets the "truncateFontHeightsLikeWP6" element
     */
    @Override
    public void unsetTruncateFontHeightsLikeWP6() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "mwSmallCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getMwSmallCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mwSmallCaps" element
     */
    @Override
    public boolean isSetMwSmallCaps() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "mwSmallCaps" element
     */
    @Override
    public void setMwSmallCaps(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff mwSmallCaps) {
        generatedSetterHelperImpl(mwSmallCaps, PROPERTY_QNAME[26], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mwSmallCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewMwSmallCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Unsets the "mwSmallCaps" element
     */
    @Override
    public void unsetMwSmallCaps() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }

    /**
     * Gets the "usePrinterMetrics" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUsePrinterMetrics() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "usePrinterMetrics" element
     */
    @Override
    public boolean isSetUsePrinterMetrics() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]) != 0;
        }
    }

    /**
     * Sets the "usePrinterMetrics" element
     */
    @Override
    public void setUsePrinterMetrics(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff usePrinterMetrics) {
        generatedSetterHelperImpl(usePrinterMetrics, PROPERTY_QNAME[27], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "usePrinterMetrics" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUsePrinterMetrics() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Unsets the "usePrinterMetrics" element
     */
    @Override
    public void unsetUsePrinterMetrics() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], 0);
        }
    }

    /**
     * Gets the "doNotSuppressParagraphBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotSuppressParagraphBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotSuppressParagraphBorders" element
     */
    @Override
    public boolean isSetDoNotSuppressParagraphBorders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]) != 0;
        }
    }

    /**
     * Sets the "doNotSuppressParagraphBorders" element
     */
    @Override
    public void setDoNotSuppressParagraphBorders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotSuppressParagraphBorders) {
        generatedSetterHelperImpl(doNotSuppressParagraphBorders, PROPERTY_QNAME[28], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotSuppressParagraphBorders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotSuppressParagraphBorders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Unsets the "doNotSuppressParagraphBorders" element
     */
    @Override
    public void unsetDoNotSuppressParagraphBorders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], 0);
        }
    }

    /**
     * Gets the "wrapTrailSpaces" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWrapTrailSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "wrapTrailSpaces" element
     */
    @Override
    public boolean isSetWrapTrailSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]) != 0;
        }
    }

    /**
     * Sets the "wrapTrailSpaces" element
     */
    @Override
    public void setWrapTrailSpaces(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff wrapTrailSpaces) {
        generatedSetterHelperImpl(wrapTrailSpaces, PROPERTY_QNAME[29], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "wrapTrailSpaces" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWrapTrailSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Unsets the "wrapTrailSpaces" element
     */
    @Override
    public void unsetWrapTrailSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], 0);
        }
    }

    /**
     * Gets the "footnoteLayoutLikeWW8" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFootnoteLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "footnoteLayoutLikeWW8" element
     */
    @Override
    public boolean isSetFootnoteLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]) != 0;
        }
    }

    /**
     * Sets the "footnoteLayoutLikeWW8" element
     */
    @Override
    public void setFootnoteLayoutLikeWW8(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff footnoteLayoutLikeWW8) {
        generatedSetterHelperImpl(footnoteLayoutLikeWW8, PROPERTY_QNAME[30], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "footnoteLayoutLikeWW8" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFootnoteLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Unsets the "footnoteLayoutLikeWW8" element
     */
    @Override
    public void unsetFootnoteLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], 0);
        }
    }

    /**
     * Gets the "shapeLayoutLikeWW8" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShapeLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "shapeLayoutLikeWW8" element
     */
    @Override
    public boolean isSetShapeLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]) != 0;
        }
    }

    /**
     * Sets the "shapeLayoutLikeWW8" element
     */
    @Override
    public void setShapeLayoutLikeWW8(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff shapeLayoutLikeWW8) {
        generatedSetterHelperImpl(shapeLayoutLikeWW8, PROPERTY_QNAME[31], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shapeLayoutLikeWW8" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShapeLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Unsets the "shapeLayoutLikeWW8" element
     */
    @Override
    public void unsetShapeLayoutLikeWW8() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], 0);
        }
    }

    /**
     * Gets the "alignTablesRowByRow" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAlignTablesRowByRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[32], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "alignTablesRowByRow" element
     */
    @Override
    public boolean isSetAlignTablesRowByRow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]) != 0;
        }
    }

    /**
     * Sets the "alignTablesRowByRow" element
     */
    @Override
    public void setAlignTablesRowByRow(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff alignTablesRowByRow) {
        generatedSetterHelperImpl(alignTablesRowByRow, PROPERTY_QNAME[32], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "alignTablesRowByRow" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAlignTablesRowByRow() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Unsets the "alignTablesRowByRow" element
     */
    @Override
    public void unsetAlignTablesRowByRow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], 0);
        }
    }

    /**
     * Gets the "forgetLastTabAlignment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getForgetLastTabAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "forgetLastTabAlignment" element
     */
    @Override
    public boolean isSetForgetLastTabAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]) != 0;
        }
    }

    /**
     * Sets the "forgetLastTabAlignment" element
     */
    @Override
    public void setForgetLastTabAlignment(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff forgetLastTabAlignment) {
        generatedSetterHelperImpl(forgetLastTabAlignment, PROPERTY_QNAME[33], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "forgetLastTabAlignment" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewForgetLastTabAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Unsets the "forgetLastTabAlignment" element
     */
    @Override
    public void unsetForgetLastTabAlignment() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], 0);
        }
    }

    /**
     * Gets the "adjustLineHeightInTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAdjustLineHeightInTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[34], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "adjustLineHeightInTable" element
     */
    @Override
    public boolean isSetAdjustLineHeightInTable() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]) != 0;
        }
    }

    /**
     * Sets the "adjustLineHeightInTable" element
     */
    @Override
    public void setAdjustLineHeightInTable(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff adjustLineHeightInTable) {
        generatedSetterHelperImpl(adjustLineHeightInTable, PROPERTY_QNAME[34], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "adjustLineHeightInTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAdjustLineHeightInTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Unsets the "adjustLineHeightInTable" element
     */
    @Override
    public void unsetAdjustLineHeightInTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], 0);
        }
    }

    /**
     * Gets the "autoSpaceLikeWord95" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoSpaceLikeWord95() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[35], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoSpaceLikeWord95" element
     */
    @Override
    public boolean isSetAutoSpaceLikeWord95() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]) != 0;
        }
    }

    /**
     * Sets the "autoSpaceLikeWord95" element
     */
    @Override
    public void setAutoSpaceLikeWord95(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoSpaceLikeWord95) {
        generatedSetterHelperImpl(autoSpaceLikeWord95, PROPERTY_QNAME[35], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoSpaceLikeWord95" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoSpaceLikeWord95() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Unsets the "autoSpaceLikeWord95" element
     */
    @Override
    public void unsetAutoSpaceLikeWord95() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], 0);
        }
    }

    /**
     * Gets the "noSpaceRaiseLower" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoSpaceRaiseLower() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[36], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noSpaceRaiseLower" element
     */
    @Override
    public boolean isSetNoSpaceRaiseLower() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]) != 0;
        }
    }

    /**
     * Sets the "noSpaceRaiseLower" element
     */
    @Override
    public void setNoSpaceRaiseLower(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noSpaceRaiseLower) {
        generatedSetterHelperImpl(noSpaceRaiseLower, PROPERTY_QNAME[36], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noSpaceRaiseLower" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoSpaceRaiseLower() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Unsets the "noSpaceRaiseLower" element
     */
    @Override
    public void unsetNoSpaceRaiseLower() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], 0);
        }
    }

    /**
     * Gets the "doNotUseHTMLParagraphAutoSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotUseHTMLParagraphAutoSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[37], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotUseHTMLParagraphAutoSpacing" element
     */
    @Override
    public boolean isSetDoNotUseHTMLParagraphAutoSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]) != 0;
        }
    }

    /**
     * Sets the "doNotUseHTMLParagraphAutoSpacing" element
     */
    @Override
    public void setDoNotUseHTMLParagraphAutoSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotUseHTMLParagraphAutoSpacing) {
        generatedSetterHelperImpl(doNotUseHTMLParagraphAutoSpacing, PROPERTY_QNAME[37], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotUseHTMLParagraphAutoSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotUseHTMLParagraphAutoSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Unsets the "doNotUseHTMLParagraphAutoSpacing" element
     */
    @Override
    public void unsetDoNotUseHTMLParagraphAutoSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], 0);
        }
    }

    /**
     * Gets the "layoutRawTableWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLayoutRawTableWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[38], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "layoutRawTableWidth" element
     */
    @Override
    public boolean isSetLayoutRawTableWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[38]) != 0;
        }
    }

    /**
     * Sets the "layoutRawTableWidth" element
     */
    @Override
    public void setLayoutRawTableWidth(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff layoutRawTableWidth) {
        generatedSetterHelperImpl(layoutRawTableWidth, PROPERTY_QNAME[38], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutRawTableWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLayoutRawTableWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * Unsets the "layoutRawTableWidth" element
     */
    @Override
    public void unsetLayoutRawTableWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[38], 0);
        }
    }

    /**
     * Gets the "layoutTableRowsApart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLayoutTableRowsApart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[39], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "layoutTableRowsApart" element
     */
    @Override
    public boolean isSetLayoutTableRowsApart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[39]) != 0;
        }
    }

    /**
     * Sets the "layoutTableRowsApart" element
     */
    @Override
    public void setLayoutTableRowsApart(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff layoutTableRowsApart) {
        generatedSetterHelperImpl(layoutTableRowsApart, PROPERTY_QNAME[39], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layoutTableRowsApart" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLayoutTableRowsApart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * Unsets the "layoutTableRowsApart" element
     */
    @Override
    public void unsetLayoutTableRowsApart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[39], 0);
        }
    }

    /**
     * Gets the "useWord97LineBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseWord97LineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[40], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useWord97LineBreakRules" element
     */
    @Override
    public boolean isSetUseWord97LineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[40]) != 0;
        }
    }

    /**
     * Sets the "useWord97LineBreakRules" element
     */
    @Override
    public void setUseWord97LineBreakRules(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useWord97LineBreakRules) {
        generatedSetterHelperImpl(useWord97LineBreakRules, PROPERTY_QNAME[40], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useWord97LineBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseWord97LineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * Unsets the "useWord97LineBreakRules" element
     */
    @Override
    public void unsetUseWord97LineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[40], 0);
        }
    }

    /**
     * Gets the "doNotBreakWrappedTables" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotBreakWrappedTables() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[41], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotBreakWrappedTables" element
     */
    @Override
    public boolean isSetDoNotBreakWrappedTables() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[41]) != 0;
        }
    }

    /**
     * Sets the "doNotBreakWrappedTables" element
     */
    @Override
    public void setDoNotBreakWrappedTables(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotBreakWrappedTables) {
        generatedSetterHelperImpl(doNotBreakWrappedTables, PROPERTY_QNAME[41], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotBreakWrappedTables" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotBreakWrappedTables() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[41]);
            return target;
        }
    }

    /**
     * Unsets the "doNotBreakWrappedTables" element
     */
    @Override
    public void unsetDoNotBreakWrappedTables() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[41], 0);
        }
    }

    /**
     * Gets the "doNotSnapToGridInCell" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotSnapToGridInCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[42], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotSnapToGridInCell" element
     */
    @Override
    public boolean isSetDoNotSnapToGridInCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[42]) != 0;
        }
    }

    /**
     * Sets the "doNotSnapToGridInCell" element
     */
    @Override
    public void setDoNotSnapToGridInCell(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotSnapToGridInCell) {
        generatedSetterHelperImpl(doNotSnapToGridInCell, PROPERTY_QNAME[42], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotSnapToGridInCell" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotSnapToGridInCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[42]);
            return target;
        }
    }

    /**
     * Unsets the "doNotSnapToGridInCell" element
     */
    @Override
    public void unsetDoNotSnapToGridInCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[42], 0);
        }
    }

    /**
     * Gets the "selectFldWithFirstOrLastChar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSelectFldWithFirstOrLastChar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[43], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "selectFldWithFirstOrLastChar" element
     */
    @Override
    public boolean isSetSelectFldWithFirstOrLastChar() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[43]) != 0;
        }
    }

    /**
     * Sets the "selectFldWithFirstOrLastChar" element
     */
    @Override
    public void setSelectFldWithFirstOrLastChar(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff selectFldWithFirstOrLastChar) {
        generatedSetterHelperImpl(selectFldWithFirstOrLastChar, PROPERTY_QNAME[43], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "selectFldWithFirstOrLastChar" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSelectFldWithFirstOrLastChar() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[43]);
            return target;
        }
    }

    /**
     * Unsets the "selectFldWithFirstOrLastChar" element
     */
    @Override
    public void unsetSelectFldWithFirstOrLastChar() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[43], 0);
        }
    }

    /**
     * Gets the "applyBreakingRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getApplyBreakingRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[44], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "applyBreakingRules" element
     */
    @Override
    public boolean isSetApplyBreakingRules() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[44]) != 0;
        }
    }

    /**
     * Sets the "applyBreakingRules" element
     */
    @Override
    public void setApplyBreakingRules(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff applyBreakingRules) {
        generatedSetterHelperImpl(applyBreakingRules, PROPERTY_QNAME[44], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "applyBreakingRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewApplyBreakingRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[44]);
            return target;
        }
    }

    /**
     * Unsets the "applyBreakingRules" element
     */
    @Override
    public void unsetApplyBreakingRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[44], 0);
        }
    }

    /**
     * Gets the "doNotWrapTextWithPunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotWrapTextWithPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[45], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotWrapTextWithPunct" element
     */
    @Override
    public boolean isSetDoNotWrapTextWithPunct() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[45]) != 0;
        }
    }

    /**
     * Sets the "doNotWrapTextWithPunct" element
     */
    @Override
    public void setDoNotWrapTextWithPunct(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotWrapTextWithPunct) {
        generatedSetterHelperImpl(doNotWrapTextWithPunct, PROPERTY_QNAME[45], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotWrapTextWithPunct" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotWrapTextWithPunct() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[45]);
            return target;
        }
    }

    /**
     * Unsets the "doNotWrapTextWithPunct" element
     */
    @Override
    public void unsetDoNotWrapTextWithPunct() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[45], 0);
        }
    }

    /**
     * Gets the "doNotUseEastAsianBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotUseEastAsianBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[46], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotUseEastAsianBreakRules" element
     */
    @Override
    public boolean isSetDoNotUseEastAsianBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[46]) != 0;
        }
    }

    /**
     * Sets the "doNotUseEastAsianBreakRules" element
     */
    @Override
    public void setDoNotUseEastAsianBreakRules(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotUseEastAsianBreakRules) {
        generatedSetterHelperImpl(doNotUseEastAsianBreakRules, PROPERTY_QNAME[46], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotUseEastAsianBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotUseEastAsianBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[46]);
            return target;
        }
    }

    /**
     * Unsets the "doNotUseEastAsianBreakRules" element
     */
    @Override
    public void unsetDoNotUseEastAsianBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[46], 0);
        }
    }

    /**
     * Gets the "useWord2002TableStyleRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseWord2002TableStyleRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[47], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useWord2002TableStyleRules" element
     */
    @Override
    public boolean isSetUseWord2002TableStyleRules() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[47]) != 0;
        }
    }

    /**
     * Sets the "useWord2002TableStyleRules" element
     */
    @Override
    public void setUseWord2002TableStyleRules(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useWord2002TableStyleRules) {
        generatedSetterHelperImpl(useWord2002TableStyleRules, PROPERTY_QNAME[47], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useWord2002TableStyleRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseWord2002TableStyleRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[47]);
            return target;
        }
    }

    /**
     * Unsets the "useWord2002TableStyleRules" element
     */
    @Override
    public void unsetUseWord2002TableStyleRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[47], 0);
        }
    }

    /**
     * Gets the "growAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getGrowAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[48], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "growAutofit" element
     */
    @Override
    public boolean isSetGrowAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[48]) != 0;
        }
    }

    /**
     * Sets the "growAutofit" element
     */
    @Override
    public void setGrowAutofit(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff growAutofit) {
        generatedSetterHelperImpl(growAutofit, PROPERTY_QNAME[48], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "growAutofit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewGrowAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[48]);
            return target;
        }
    }

    /**
     * Unsets the "growAutofit" element
     */
    @Override
    public void unsetGrowAutofit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[48], 0);
        }
    }

    /**
     * Gets the "useFELayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseFELayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[49], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useFELayout" element
     */
    @Override
    public boolean isSetUseFELayout() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[49]) != 0;
        }
    }

    /**
     * Sets the "useFELayout" element
     */
    @Override
    public void setUseFELayout(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useFELayout) {
        generatedSetterHelperImpl(useFELayout, PROPERTY_QNAME[49], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useFELayout" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseFELayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[49]);
            return target;
        }
    }

    /**
     * Unsets the "useFELayout" element
     */
    @Override
    public void unsetUseFELayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[49], 0);
        }
    }

    /**
     * Gets the "useNormalStyleForList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseNormalStyleForList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[50], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useNormalStyleForList" element
     */
    @Override
    public boolean isSetUseNormalStyleForList() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[50]) != 0;
        }
    }

    /**
     * Sets the "useNormalStyleForList" element
     */
    @Override
    public void setUseNormalStyleForList(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useNormalStyleForList) {
        generatedSetterHelperImpl(useNormalStyleForList, PROPERTY_QNAME[50], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useNormalStyleForList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseNormalStyleForList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[50]);
            return target;
        }
    }

    /**
     * Unsets the "useNormalStyleForList" element
     */
    @Override
    public void unsetUseNormalStyleForList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[50], 0);
        }
    }

    /**
     * Gets the "doNotUseIndentAsNumberingTabStop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotUseIndentAsNumberingTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[51], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotUseIndentAsNumberingTabStop" element
     */
    @Override
    public boolean isSetDoNotUseIndentAsNumberingTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[51]) != 0;
        }
    }

    /**
     * Sets the "doNotUseIndentAsNumberingTabStop" element
     */
    @Override
    public void setDoNotUseIndentAsNumberingTabStop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotUseIndentAsNumberingTabStop) {
        generatedSetterHelperImpl(doNotUseIndentAsNumberingTabStop, PROPERTY_QNAME[51], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotUseIndentAsNumberingTabStop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotUseIndentAsNumberingTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[51]);
            return target;
        }
    }

    /**
     * Unsets the "doNotUseIndentAsNumberingTabStop" element
     */
    @Override
    public void unsetDoNotUseIndentAsNumberingTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[51], 0);
        }
    }

    /**
     * Gets the "useAltKinsokuLineBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseAltKinsokuLineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[52], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useAltKinsokuLineBreakRules" element
     */
    @Override
    public boolean isSetUseAltKinsokuLineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[52]) != 0;
        }
    }

    /**
     * Sets the "useAltKinsokuLineBreakRules" element
     */
    @Override
    public void setUseAltKinsokuLineBreakRules(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useAltKinsokuLineBreakRules) {
        generatedSetterHelperImpl(useAltKinsokuLineBreakRules, PROPERTY_QNAME[52], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useAltKinsokuLineBreakRules" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseAltKinsokuLineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[52]);
            return target;
        }
    }

    /**
     * Unsets the "useAltKinsokuLineBreakRules" element
     */
    @Override
    public void unsetUseAltKinsokuLineBreakRules() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[52], 0);
        }
    }

    /**
     * Gets the "allowSpaceOfSameStyleInTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAllowSpaceOfSameStyleInTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[53], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "allowSpaceOfSameStyleInTable" element
     */
    @Override
    public boolean isSetAllowSpaceOfSameStyleInTable() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[53]) != 0;
        }
    }

    /**
     * Sets the "allowSpaceOfSameStyleInTable" element
     */
    @Override
    public void setAllowSpaceOfSameStyleInTable(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff allowSpaceOfSameStyleInTable) {
        generatedSetterHelperImpl(allowSpaceOfSameStyleInTable, PROPERTY_QNAME[53], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "allowSpaceOfSameStyleInTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAllowSpaceOfSameStyleInTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[53]);
            return target;
        }
    }

    /**
     * Unsets the "allowSpaceOfSameStyleInTable" element
     */
    @Override
    public void unsetAllowSpaceOfSameStyleInTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[53], 0);
        }
    }

    /**
     * Gets the "doNotSuppressIndentation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotSuppressIndentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[54], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotSuppressIndentation" element
     */
    @Override
    public boolean isSetDoNotSuppressIndentation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[54]) != 0;
        }
    }

    /**
     * Sets the "doNotSuppressIndentation" element
     */
    @Override
    public void setDoNotSuppressIndentation(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotSuppressIndentation) {
        generatedSetterHelperImpl(doNotSuppressIndentation, PROPERTY_QNAME[54], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotSuppressIndentation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotSuppressIndentation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[54]);
            return target;
        }
    }

    /**
     * Unsets the "doNotSuppressIndentation" element
     */
    @Override
    public void unsetDoNotSuppressIndentation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[54], 0);
        }
    }

    /**
     * Gets the "doNotAutofitConstrainedTables" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotAutofitConstrainedTables() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[55], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotAutofitConstrainedTables" element
     */
    @Override
    public boolean isSetDoNotAutofitConstrainedTables() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[55]) != 0;
        }
    }

    /**
     * Sets the "doNotAutofitConstrainedTables" element
     */
    @Override
    public void setDoNotAutofitConstrainedTables(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotAutofitConstrainedTables) {
        generatedSetterHelperImpl(doNotAutofitConstrainedTables, PROPERTY_QNAME[55], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotAutofitConstrainedTables" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotAutofitConstrainedTables() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[55]);
            return target;
        }
    }

    /**
     * Unsets the "doNotAutofitConstrainedTables" element
     */
    @Override
    public void unsetDoNotAutofitConstrainedTables() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[55], 0);
        }
    }

    /**
     * Gets the "autofitToFirstFixedWidthCell" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutofitToFirstFixedWidthCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[56], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autofitToFirstFixedWidthCell" element
     */
    @Override
    public boolean isSetAutofitToFirstFixedWidthCell() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[56]) != 0;
        }
    }

    /**
     * Sets the "autofitToFirstFixedWidthCell" element
     */
    @Override
    public void setAutofitToFirstFixedWidthCell(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autofitToFirstFixedWidthCell) {
        generatedSetterHelperImpl(autofitToFirstFixedWidthCell, PROPERTY_QNAME[56], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autofitToFirstFixedWidthCell" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutofitToFirstFixedWidthCell() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[56]);
            return target;
        }
    }

    /**
     * Unsets the "autofitToFirstFixedWidthCell" element
     */
    @Override
    public void unsetAutofitToFirstFixedWidthCell() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[56], 0);
        }
    }

    /**
     * Gets the "underlineTabInNumList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUnderlineTabInNumList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[57], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "underlineTabInNumList" element
     */
    @Override
    public boolean isSetUnderlineTabInNumList() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[57]) != 0;
        }
    }

    /**
     * Sets the "underlineTabInNumList" element
     */
    @Override
    public void setUnderlineTabInNumList(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff underlineTabInNumList) {
        generatedSetterHelperImpl(underlineTabInNumList, PROPERTY_QNAME[57], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "underlineTabInNumList" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUnderlineTabInNumList() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[57]);
            return target;
        }
    }

    /**
     * Unsets the "underlineTabInNumList" element
     */
    @Override
    public void unsetUnderlineTabInNumList() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[57], 0);
        }
    }

    /**
     * Gets the "displayHangulFixedWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDisplayHangulFixedWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[58], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "displayHangulFixedWidth" element
     */
    @Override
    public boolean isSetDisplayHangulFixedWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[58]) != 0;
        }
    }

    /**
     * Sets the "displayHangulFixedWidth" element
     */
    @Override
    public void setDisplayHangulFixedWidth(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff displayHangulFixedWidth) {
        generatedSetterHelperImpl(displayHangulFixedWidth, PROPERTY_QNAME[58], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "displayHangulFixedWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDisplayHangulFixedWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[58]);
            return target;
        }
    }

    /**
     * Unsets the "displayHangulFixedWidth" element
     */
    @Override
    public void unsetDisplayHangulFixedWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[58], 0);
        }
    }

    /**
     * Gets the "splitPgBreakAndParaMark" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSplitPgBreakAndParaMark() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[59], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "splitPgBreakAndParaMark" element
     */
    @Override
    public boolean isSetSplitPgBreakAndParaMark() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[59]) != 0;
        }
    }

    /**
     * Sets the "splitPgBreakAndParaMark" element
     */
    @Override
    public void setSplitPgBreakAndParaMark(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff splitPgBreakAndParaMark) {
        generatedSetterHelperImpl(splitPgBreakAndParaMark, PROPERTY_QNAME[59], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "splitPgBreakAndParaMark" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSplitPgBreakAndParaMark() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[59]);
            return target;
        }
    }

    /**
     * Unsets the "splitPgBreakAndParaMark" element
     */
    @Override
    public void unsetSplitPgBreakAndParaMark() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[59], 0);
        }
    }

    /**
     * Gets the "doNotVertAlignCellWithSp" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotVertAlignCellWithSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[60], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotVertAlignCellWithSp" element
     */
    @Override
    public boolean isSetDoNotVertAlignCellWithSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[60]) != 0;
        }
    }

    /**
     * Sets the "doNotVertAlignCellWithSp" element
     */
    @Override
    public void setDoNotVertAlignCellWithSp(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotVertAlignCellWithSp) {
        generatedSetterHelperImpl(doNotVertAlignCellWithSp, PROPERTY_QNAME[60], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotVertAlignCellWithSp" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotVertAlignCellWithSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[60]);
            return target;
        }
    }

    /**
     * Unsets the "doNotVertAlignCellWithSp" element
     */
    @Override
    public void unsetDoNotVertAlignCellWithSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[60], 0);
        }
    }

    /**
     * Gets the "doNotBreakConstrainedForcedTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotBreakConstrainedForcedTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[61], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotBreakConstrainedForcedTable" element
     */
    @Override
    public boolean isSetDoNotBreakConstrainedForcedTable() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[61]) != 0;
        }
    }

    /**
     * Sets the "doNotBreakConstrainedForcedTable" element
     */
    @Override
    public void setDoNotBreakConstrainedForcedTable(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotBreakConstrainedForcedTable) {
        generatedSetterHelperImpl(doNotBreakConstrainedForcedTable, PROPERTY_QNAME[61], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotBreakConstrainedForcedTable" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotBreakConstrainedForcedTable() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[61]);
            return target;
        }
    }

    /**
     * Unsets the "doNotBreakConstrainedForcedTable" element
     */
    @Override
    public void unsetDoNotBreakConstrainedForcedTable() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[61], 0);
        }
    }

    /**
     * Gets the "doNotVertAlignInTxbx" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotVertAlignInTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[62], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotVertAlignInTxbx" element
     */
    @Override
    public boolean isSetDoNotVertAlignInTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[62]) != 0;
        }
    }

    /**
     * Sets the "doNotVertAlignInTxbx" element
     */
    @Override
    public void setDoNotVertAlignInTxbx(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotVertAlignInTxbx) {
        generatedSetterHelperImpl(doNotVertAlignInTxbx, PROPERTY_QNAME[62], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotVertAlignInTxbx" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotVertAlignInTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[62]);
            return target;
        }
    }

    /**
     * Unsets the "doNotVertAlignInTxbx" element
     */
    @Override
    public void unsetDoNotVertAlignInTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[62], 0);
        }
    }

    /**
     * Gets the "useAnsiKerningPairs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseAnsiKerningPairs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[63], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useAnsiKerningPairs" element
     */
    @Override
    public boolean isSetUseAnsiKerningPairs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[63]) != 0;
        }
    }

    /**
     * Sets the "useAnsiKerningPairs" element
     */
    @Override
    public void setUseAnsiKerningPairs(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useAnsiKerningPairs) {
        generatedSetterHelperImpl(useAnsiKerningPairs, PROPERTY_QNAME[63], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useAnsiKerningPairs" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseAnsiKerningPairs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[63]);
            return target;
        }
    }

    /**
     * Unsets the "useAnsiKerningPairs" element
     */
    @Override
    public void unsetUseAnsiKerningPairs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[63], 0);
        }
    }

    /**
     * Gets the "cachedColBalance" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCachedColBalance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[64], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cachedColBalance" element
     */
    @Override
    public boolean isSetCachedColBalance() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[64]) != 0;
        }
    }

    /**
     * Sets the "cachedColBalance" element
     */
    @Override
    public void setCachedColBalance(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff cachedColBalance) {
        generatedSetterHelperImpl(cachedColBalance, PROPERTY_QNAME[64], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cachedColBalance" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCachedColBalance() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[64]);
            return target;
        }
    }

    /**
     * Unsets the "cachedColBalance" element
     */
    @Override
    public void unsetCachedColBalance() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[64], 0);
        }
    }

    /**
     * Gets a List of "compatSetting" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting> getCompatSettingList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCompatSettingArray,
                this::setCompatSettingArray,
                this::insertNewCompatSetting,
                this::removeCompatSetting,
                this::sizeOfCompatSettingArray
            );
        }
    }

    /**
     * Gets array of all "compatSetting" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting[] getCompatSettingArray() {
        return getXmlObjectArray(PROPERTY_QNAME[65], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting[0]);
    }

    /**
     * Gets ith "compatSetting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting getCompatSettingArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting)get_store().find_element_user(PROPERTY_QNAME[65], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "compatSetting" element
     */
    @Override
    public int sizeOfCompatSettingArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[65]);
        }
    }

    /**
     * Sets array of all "compatSetting" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCompatSettingArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting[] compatSettingArray) {
        check_orphaned();
        arraySetterHelper(compatSettingArray, PROPERTY_QNAME[65]);
    }

    /**
     * Sets ith "compatSetting" element
     */
    @Override
    public void setCompatSettingArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting compatSetting) {
        generatedSetterHelperImpl(compatSetting, PROPERTY_QNAME[65], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "compatSetting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting insertNewCompatSetting(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting)get_store().insert_element_user(PROPERTY_QNAME[65], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "compatSetting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting addNewCompatSetting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompatSetting)get_store().add_element_user(PROPERTY_QNAME[65]);
            return target;
        }
    }

    /**
     * Removes the ith "compatSetting" element
     */
    @Override
    public void removeCompatSetting(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[65], i);
        }
    }
}
