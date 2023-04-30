/*
 * XML Type:  CT_DLbl
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbl
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_DLbl(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTDLblImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTDLbl {
    private static final long serialVersionUID = 1L;

    public CTDLblImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "idx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "delete"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "layout"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "tx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "numFmt"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "txPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dLblPos"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showLegendKey"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showVal"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showCatName"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showSerName"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showPercent"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showBubbleSize"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "separator"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "idx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "idx" element
     */
    @Override
    public void setIdx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt idx) {
        generatedSetterHelperImpl(idx, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "idx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewIdx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "delete" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getDelete() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "delete" element
     */
    @Override
    public boolean isSetDelete() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "delete" element
     */
    @Override
    public void setDelete(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean delete) {
        generatedSetterHelperImpl(delete, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "delete" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewDelete() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "delete" element
     */
    @Override
    public void unsetDelete() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "layout" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout getLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "layout" element
     */
    @Override
    public boolean isSetLayout() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "layout" element
     */
    @Override
    public void setLayout(org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout layout) {
        generatedSetterHelperImpl(layout, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "layout" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout addNewLayout() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLayout)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "layout" element
     */
    @Override
    public void unsetLayout() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "tx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTx getTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTx)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "tx" element
     */
    @Override
    public boolean isSetTx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "tx" element
     */
    @Override
    public void setTx(org.openxmlformats.schemas.drawingml.x2006.chart.CTTx tx) {
        generatedSetterHelperImpl(tx, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "tx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTx addNewTx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTx target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTx)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "tx" element
     */
    @Override
    public void unsetTx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt getNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "numFmt" element
     */
    @Override
    public boolean isSetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "numFmt" element
     */
    @Override
    public void setNumFmt(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt numFmt) {
        generatedSetterHelperImpl(numFmt, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "numFmt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt addNewNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "numFmt" element
     */
    @Override
    public void unsetNumFmt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spPr" element
     */
    @Override
    public boolean isSetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "spPr" element
     */
    @Override
    public void unsetSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "txPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txPr" element
     */
    @Override
    public boolean isSetTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "txPr" element
     */
    @Override
    public void setTxPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txPr) {
        generatedSetterHelperImpl(txPr, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "txPr" element
     */
    @Override
    public void unsetTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "dLblPos" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos getDLblPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dLblPos" element
     */
    @Override
    public boolean isSetDLblPos() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "dLblPos" element
     */
    @Override
    public void setDLblPos(org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos dLblPos) {
        generatedSetterHelperImpl(dLblPos, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dLblPos" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos addNewDLblPos() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDLblPos)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "dLblPos" element
     */
    @Override
    public void unsetDLblPos() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "showLegendKey" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowLegendKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showLegendKey" element
     */
    @Override
    public boolean isSetShowLegendKey() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "showLegendKey" element
     */
    @Override
    public void setShowLegendKey(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showLegendKey) {
        generatedSetterHelperImpl(showLegendKey, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showLegendKey" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowLegendKey() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "showLegendKey" element
     */
    @Override
    public void unsetShowLegendKey() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "showVal" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showVal" element
     */
    @Override
    public boolean isSetShowVal() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "showVal" element
     */
    @Override
    public void setShowVal(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showVal) {
        generatedSetterHelperImpl(showVal, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showVal" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowVal() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "showVal" element
     */
    @Override
    public void unsetShowVal() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "showCatName" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowCatName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showCatName" element
     */
    @Override
    public boolean isSetShowCatName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "showCatName" element
     */
    @Override
    public void setShowCatName(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showCatName) {
        generatedSetterHelperImpl(showCatName, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showCatName" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowCatName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "showCatName" element
     */
    @Override
    public void unsetShowCatName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "showSerName" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowSerName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showSerName" element
     */
    @Override
    public boolean isSetShowSerName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "showSerName" element
     */
    @Override
    public void setShowSerName(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showSerName) {
        generatedSetterHelperImpl(showSerName, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showSerName" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowSerName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "showSerName" element
     */
    @Override
    public void unsetShowSerName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "showPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showPercent" element
     */
    @Override
    public boolean isSetShowPercent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "showPercent" element
     */
    @Override
    public void setShowPercent(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showPercent) {
        generatedSetterHelperImpl(showPercent, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showPercent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowPercent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "showPercent" element
     */
    @Override
    public void unsetShowPercent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "showBubbleSize" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowBubbleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showBubbleSize" element
     */
    @Override
    public boolean isSetShowBubbleSize() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "showBubbleSize" element
     */
    @Override
    public void setShowBubbleSize(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showBubbleSize) {
        generatedSetterHelperImpl(showBubbleSize, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showBubbleSize" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowBubbleSize() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "showBubbleSize" element
     */
    @Override
    public void unsetShowBubbleSize() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "separator" element
     */
    @Override
    public java.lang.String getSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "separator" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return target;
        }
    }

    /**
     * True if has "separator" element
     */
    @Override
    public boolean isSetSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "separator" element
     */
    @Override
    public void setSeparator(java.lang.String separator) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.setStringValue(separator);
        }
    }

    /**
     * Sets (as xml) the "separator" element
     */
    @Override
    public void xsetSeparator(org.apache.xmlbeans.XmlString separator) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.set(separator);
        }
    }

    /**
     * Unsets the "separator" element
     */
    @Override
    public void unsetSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "extLst" element
     */
    @Override
    public boolean isSetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "extLst" element
     */
    @Override
    public void unsetExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }
}
