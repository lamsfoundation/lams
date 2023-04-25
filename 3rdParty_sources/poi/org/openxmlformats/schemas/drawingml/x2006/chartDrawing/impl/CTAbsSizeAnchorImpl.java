/*
 * XML Type:  CT_AbsSizeAnchor
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chartDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chartDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AbsSizeAnchor(@http://schemas.openxmlformats.org/drawingml/2006/chartDrawing).
 *
 * This is a complex type.
 */
public class CTAbsSizeAnchorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTAbsSizeAnchor {
    private static final long serialVersionUID = 1L;

    public CTAbsSizeAnchorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "from"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "ext"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "sp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "grpSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "cxnSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/chartDrawing", "pic"),
    };


    /**
     * Gets the "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "from" element
     */
    @Override
    public void setFrom(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker from) {
        generatedSetterHelperImpl(from, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker addNewFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "ext" element
     */
    @Override
    public void setExt(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D ext) {
        generatedSetterHelperImpl(ext, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ext" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape getSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sp" element
     */
    @Override
    public boolean isSetSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "sp" element
     */
    @Override
    public void setSp(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape sp) {
        generatedSetterHelperImpl(sp, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape addNewSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTShape)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "sp" element
     */
    @Override
    public void unsetSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape getGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "grpSp" element
     */
    @Override
    public boolean isSetGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "grpSp" element
     */
    @Override
    public void setGrpSp(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape grpSp) {
        generatedSetterHelperImpl(grpSp, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape addNewGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGroupShape)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "grpSp" element
     */
    @Override
    public void unsetGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame getGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "graphicFrame" element
     */
    @Override
    public boolean isSetGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "graphicFrame" element
     */
    @Override
    public void setGraphicFrame(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame addNewGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTGraphicFrame)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "graphicFrame" element
     */
    @Override
    public void unsetGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector getCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cxnSp" element
     */
    @Override
    public boolean isSetCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "cxnSp" element
     */
    @Override
    public void setCxnSp(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector cxnSp) {
        generatedSetterHelperImpl(cxnSp, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector addNewCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTConnector)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "cxnSp" element
     */
    @Override
    public void unsetCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture getPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pic" element
     */
    @Override
    public boolean isSetPic() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "pic" element
     */
    @Override
    public void setPic(org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.chartDrawing.CTPicture)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "pic" element
     */
    @Override
    public void unsetPic() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }
}
