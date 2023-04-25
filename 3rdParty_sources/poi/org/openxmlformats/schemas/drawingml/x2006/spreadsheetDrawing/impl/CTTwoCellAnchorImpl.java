/*
 * XML Type:  CT_TwoCellAnchor
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TwoCellAnchor(@http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing).
 *
 * This is a complex type.
 */
public class CTTwoCellAnchorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor {
    private static final long serialVersionUID = 1L;

    public CTTwoCellAnchorImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "from"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "to"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "sp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "grpSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "graphicFrame"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "cxnSp"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "pic"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "contentPart"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/spreadsheetDrawing", "clientData"),
        new QName("", "editAs"),
    };


    /**
     * Gets the "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "from" element
     */
    @Override
    public void setFrom(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker from) {
        generatedSetterHelperImpl(from, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "from" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewFrom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker getTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "to" element
     */
    @Override
    public void setTo(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker to) {
        generatedSetterHelperImpl(to, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "to" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker addNewTo() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Gets the "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape getSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)get_store().find_element_user(PROPERTY_QNAME[2], 0);
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
    public void setSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape sp) {
        generatedSetterHelperImpl(sp, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape addNewSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape)get_store().add_element_user(PROPERTY_QNAME[2]);
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
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape getGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape)get_store().find_element_user(PROPERTY_QNAME[3], 0);
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
    public void setGrpSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape grpSp) {
        generatedSetterHelperImpl(grpSp, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "grpSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape addNewGrpSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGroupShape)get_store().add_element_user(PROPERTY_QNAME[3]);
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
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame getGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
    public void setGraphicFrame(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame graphicFrame) {
        generatedSetterHelperImpl(graphicFrame, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphicFrame" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame addNewGraphicFrame() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTGraphicalObjectFrame)get_store().add_element_user(PROPERTY_QNAME[4]);
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
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector getCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
    public void setCxnSp(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector cxnSp) {
        generatedSetterHelperImpl(cxnSp, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cxnSp" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector addNewCxnSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTConnector)get_store().add_element_user(PROPERTY_QNAME[5]);
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
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture getPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture)get_store().find_element_user(PROPERTY_QNAME[6], 0);
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
    public void setPic(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture pic) {
        generatedSetterHelperImpl(pic, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture addNewPic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture)get_store().add_element_user(PROPERTY_QNAME[6]);
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

    /**
     * Gets the "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel getContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "contentPart" element
     */
    @Override
    public boolean isSetContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "contentPart" element
     */
    @Override
    public void setContentPart(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel contentPart) {
        generatedSetterHelperImpl(contentPart, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "contentPart" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel addNewContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTRel)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "contentPart" element
     */
    @Override
    public void unsetContentPart() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "clientData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData getClientData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "clientData" element
     */
    @Override
    public void setClientData(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData clientData) {
        generatedSetterHelperImpl(clientData, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clientData" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData addNewClientData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAnchorClientData)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Gets the "editAs" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.Enum getEditAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? null : (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "editAs" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs xgetEditAs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "editAs" attribute
     */
    @Override
    public boolean isSetEditAs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "editAs" attribute
     */
    @Override
    public void setEditAs(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs.Enum editAs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setEnumValue(editAs);
        }
    }

    /**
     * Sets (as xml) the "editAs" attribute
     */
    @Override
    public void xsetEditAs(org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs editAs) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(editAs);
        }
    }

    /**
     * Unsets the "editAs" attribute
     */
    @Override
    public void unsetEditAs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
