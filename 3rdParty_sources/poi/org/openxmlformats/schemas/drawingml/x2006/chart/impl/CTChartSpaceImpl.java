/*
 * XML Type:  CT_ChartSpace
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ChartSpace(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTChartSpaceImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace {
    private static final long serialVersionUID = 1L;

    public CTChartSpaceImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "date1904"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "lang"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "roundedCorners"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "style"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "clrMapOvr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pivotSource"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "protection"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "chart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "txPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "externalData"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "printSettings"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "userShapes"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "date1904" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "date1904" element
     */
    @Override
    public boolean isSetDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "date1904" element
     */
    @Override
    public void setDate1904(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean date1904) {
        generatedSetterHelperImpl(date1904, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "date1904" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "date1904" element
     */
    @Override
    public void unsetDate1904() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "lang" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID getLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "lang" element
     */
    @Override
    public boolean isSetLang() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "lang" element
     */
    @Override
    public void setLang(org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID lang) {
        generatedSetterHelperImpl(lang, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "lang" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID addNewLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTextLanguageID)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "lang" element
     */
    @Override
    public void unsetLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "roundedCorners" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "roundedCorners" element
     */
    @Override
    public boolean isSetRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "roundedCorners" element
     */
    @Override
    public void setRoundedCorners(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean roundedCorners) {
        generatedSetterHelperImpl(roundedCorners, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "roundedCorners" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "roundedCorners" element
     */
    @Override
    public void unsetRoundedCorners() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "style" element
     */
    @Override
    public boolean isSetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "style" element
     */
    @Override
    public void setStyle(org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle style) {
        generatedSetterHelperImpl(style, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle addNewStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTStyle)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "style" element
     */
    @Override
    public void unsetStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "clrMapOvr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrMapOvr" element
     */
    @Override
    public boolean isSetClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "clrMapOvr" element
     */
    @Override
    public void setClrMapOvr(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMapOvr) {
        generatedSetterHelperImpl(clrMapOvr, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrMapOvr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "clrMapOvr" element
     */
    @Override
    public void unsetClrMapOvr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "pivotSource" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource getPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotSource" element
     */
    @Override
    public boolean isSetPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "pivotSource" element
     */
    @Override
    public void setPivotSource(org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource pivotSource) {
        generatedSetterHelperImpl(pivotSource, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotSource" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource addNewPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotSource)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "pivotSource" element
     */
    @Override
    public void unsetPivotSource() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "protection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection getProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "protection" element
     */
    @Override
    public boolean isSetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "protection" element
     */
    @Override
    public void setProtection(org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection protection) {
        generatedSetterHelperImpl(protection, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "protection" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection addNewProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTProtection)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "protection" element
     */
    @Override
    public void unsetProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChart getChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChart)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "chart" element
     */
    @Override
    public void setChart(org.openxmlformats.schemas.drawingml.x2006.chart.CTChart chart) {
        generatedSetterHelperImpl(chart, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "chart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTChart addNewChart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTChart target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTChart)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[8], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[8]);
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
            get_store().remove_element(PROPERTY_QNAME[8], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().find_element_user(PROPERTY_QNAME[9], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "txPr" element
     */
    @Override
    public void setTxPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txPr) {
        generatedSetterHelperImpl(txPr, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody)get_store().add_element_user(PROPERTY_QNAME[9]);
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
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "externalData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData getExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "externalData" element
     */
    @Override
    public boolean isSetExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "externalData" element
     */
    @Override
    public void setExternalData(org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData externalData) {
        generatedSetterHelperImpl(externalData, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "externalData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData addNewExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExternalData)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "externalData" element
     */
    @Override
    public void unsetExternalData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "printSettings" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings getPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printSettings" element
     */
    @Override
    public boolean isSetPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "printSettings" element
     */
    @Override
    public void setPrintSettings(org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings printSettings) {
        generatedSetterHelperImpl(printSettings, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printSettings" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings addNewPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPrintSettings)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "printSettings" element
     */
    @Override
    public void unsetPrintSettings() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "userShapes" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId getUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "userShapes" element
     */
    @Override
    public boolean isSetUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "userShapes" element
     */
    @Override
    public void setUserShapes(org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId userShapes) {
        generatedSetterHelperImpl(userShapes, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "userShapes" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId addNewUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTRelId)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "userShapes" element
     */
    @Override
    public void unsetUserShapes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[13], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[13]);
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
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }
}
