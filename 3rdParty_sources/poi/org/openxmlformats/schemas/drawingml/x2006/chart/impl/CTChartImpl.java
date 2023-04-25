/*
 * XML Type:  CT_Chart
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Chart(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public class CTChartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chart.CTChart {
    private static final long serialVersionUID = 1L;

    public CTChartImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "title"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "autoTitleDeleted"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "pivotFmts"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "view3D"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "floor"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "sideWall"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "backWall"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "plotArea"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "legend"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "plotVisOnly"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "dispBlanksAs"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "showDLblsOverMax"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chart", "extLst"),
    };


    /**
     * Gets the "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle getTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "title" element
     */
    @Override
    public boolean isSetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "title" element
     */
    @Override
    public void setTitle(org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle title) {
        generatedSetterHelperImpl(title, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "title" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "title" element
     */
    @Override
    public void unsetTitle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "autoTitleDeleted" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getAutoTitleDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoTitleDeleted" element
     */
    @Override
    public boolean isSetAutoTitleDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "autoTitleDeleted" element
     */
    @Override
    public void setAutoTitleDeleted(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean autoTitleDeleted) {
        generatedSetterHelperImpl(autoTitleDeleted, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoTitleDeleted" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewAutoTitleDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "autoTitleDeleted" element
     */
    @Override
    public void unsetAutoTitleDeleted() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "pivotFmts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts getPivotFmts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pivotFmts" element
     */
    @Override
    public boolean isSetPivotFmts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "pivotFmts" element
     */
    @Override
    public void setPivotFmts(org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts pivotFmts) {
        generatedSetterHelperImpl(pivotFmts, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pivotFmts" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts addNewPivotFmts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPivotFmts)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "pivotFmts" element
     */
    @Override
    public void unsetPivotFmts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "view3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D getView3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "view3D" element
     */
    @Override
    public boolean isSetView3D() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "view3D" element
     */
    @Override
    public void setView3D(org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D view3D) {
        generatedSetterHelperImpl(view3D, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "view3D" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D addNewView3D() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTView3D)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "view3D" element
     */
    @Override
    public void unsetView3D() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "floor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface getFloor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "floor" element
     */
    @Override
    public boolean isSetFloor() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "floor" element
     */
    @Override
    public void setFloor(org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface floor) {
        generatedSetterHelperImpl(floor, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "floor" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface addNewFloor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "floor" element
     */
    @Override
    public void unsetFloor() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "sideWall" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface getSideWall() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sideWall" element
     */
    @Override
    public boolean isSetSideWall() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "sideWall" element
     */
    @Override
    public void setSideWall(org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface sideWall) {
        generatedSetterHelperImpl(sideWall, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sideWall" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface addNewSideWall() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "sideWall" element
     */
    @Override
    public void unsetSideWall() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "backWall" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface getBackWall() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "backWall" element
     */
    @Override
    public boolean isSetBackWall() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "backWall" element
     */
    @Override
    public void setBackWall(org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface backWall) {
        generatedSetterHelperImpl(backWall, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "backWall" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface addNewBackWall() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTSurface)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "backWall" element
     */
    @Override
    public void unsetBackWall() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "plotArea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea getPlotArea() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "plotArea" element
     */
    @Override
    public void setPlotArea(org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea plotArea) {
        generatedSetterHelperImpl(plotArea, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "plotArea" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea addNewPlotArea() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Gets the "legend" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend getLegend() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "legend" element
     */
    @Override
    public boolean isSetLegend() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "legend" element
     */
    @Override
    public void setLegend(org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend legend) {
        generatedSetterHelperImpl(legend, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "legend" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend addNewLegend() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTLegend)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "legend" element
     */
    @Override
    public void unsetLegend() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "plotVisOnly" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getPlotVisOnly() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "plotVisOnly" element
     */
    @Override
    public boolean isSetPlotVisOnly() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "plotVisOnly" element
     */
    @Override
    public void setPlotVisOnly(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean plotVisOnly) {
        generatedSetterHelperImpl(plotVisOnly, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "plotVisOnly" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewPlotVisOnly() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "plotVisOnly" element
     */
    @Override
    public void unsetPlotVisOnly() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "dispBlanksAs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs getDispBlanksAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "dispBlanksAs" element
     */
    @Override
    public boolean isSetDispBlanksAs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "dispBlanksAs" element
     */
    @Override
    public void setDispBlanksAs(org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs dispBlanksAs) {
        generatedSetterHelperImpl(dispBlanksAs, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "dispBlanksAs" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs addNewDispBlanksAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTDispBlanksAs)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "dispBlanksAs" element
     */
    @Override
    public void unsetDispBlanksAs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "showDLblsOverMax" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getShowDLblsOverMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showDLblsOverMax" element
     */
    @Override
    public boolean isSetShowDLblsOverMax() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "showDLblsOverMax" element
     */
    @Override
    public void setShowDLblsOverMax(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean showDLblsOverMax) {
        generatedSetterHelperImpl(showDLblsOverMax, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showDLblsOverMax" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewShowDLblsOverMax() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "showDLblsOverMax" element
     */
    @Override
    public void unsetShowDLblsOverMax() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[12], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[12]);
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
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }
}
