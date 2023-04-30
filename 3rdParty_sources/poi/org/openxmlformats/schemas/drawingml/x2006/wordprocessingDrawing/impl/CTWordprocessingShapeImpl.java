/*
 * XML Type:  CT_WordprocessingShape
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_WordprocessingShape(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTWordprocessingShapeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTWordprocessingShape {
    private static final long serialVersionUID = 1L;

    public CTWordprocessingShapeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvSpPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvCnPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "spPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "style"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "extLst"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "txbx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "linkedTxbx"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "bodyPr"),
        new QName("", "normalEastAsianFlow"),
    };


    /**
     * Gets the "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cNvPr" element
     */
    @Override
    public boolean isSetCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "cNvPr" element
     */
    @Override
    public void setCNvPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps cNvPr) {
        generatedSetterHelperImpl(cNvPr, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "cNvPr" element
     */
    @Override
    public void unsetCNvPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "cNvSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps getCNvSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cNvSpPr" element
     */
    @Override
    public boolean isSetCNvSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "cNvSpPr" element
     */
    @Override
    public void setCNvSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps cNvSpPr) {
        generatedSetterHelperImpl(cNvSpPr, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvSpPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps addNewCNvSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingShapeProps)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "cNvSpPr" element
     */
    @Override
    public void unsetCNvSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "cNvCnPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties getCNvCnPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cNvCnPr" element
     */
    @Override
    public boolean isSetCNvCnPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "cNvCnPr" element
     */
    @Override
    public void setCNvCnPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties cNvCnPr) {
        generatedSetterHelperImpl(cNvCnPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvCnPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties addNewCNvCnPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualConnectorProperties)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "cNvCnPr" element
     */
    @Override
    public void unsetCNvCnPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
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
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "spPr" element
     */
    @Override
    public void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr) {
        generatedSetterHelperImpl(spPr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Gets the "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle getStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle)get_store().find_element_user(PROPERTY_QNAME[4], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "style" element
     */
    @Override
    public void setStyle(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle style) {
        generatedSetterHelperImpl(style, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "style" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle addNewStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle)get_store().add_element_user(PROPERTY_QNAME[4]);
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
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().find_element_user(PROPERTY_QNAME[5], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList)get_store().add_element_user(PROPERTY_QNAME[5]);
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
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "txbx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo getTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txbx" element
     */
    @Override
    public boolean isSetTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "txbx" element
     */
    @Override
    public void setTxbx(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo txbx) {
        generatedSetterHelperImpl(txbx, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txbx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo addNewTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTTextboxInfo)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "txbx" element
     */
    @Override
    public void unsetTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "linkedTxbx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation getLinkedTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "linkedTxbx" element
     */
    @Override
    public boolean isSetLinkedTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "linkedTxbx" element
     */
    @Override
    public void setLinkedTxbx(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation linkedTxbx) {
        generatedSetterHelperImpl(linkedTxbx, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "linkedTxbx" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation addNewLinkedTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTLinkedTextboxInformation)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "linkedTxbx" element
     */
    @Override
    public void unsetLinkedTxbx() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "bodyPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties getBodyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "bodyPr" element
     */
    @Override
    public void setBodyPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties bodyPr) {
        generatedSetterHelperImpl(bodyPr, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bodyPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties addNewBodyPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTTextBodyProperties)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Gets the "normalEastAsianFlow" attribute
     */
    @Override
    public boolean getNormalEastAsianFlow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "normalEastAsianFlow" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetNormalEastAsianFlow() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[9]);
            }
            return target;
        }
    }

    /**
     * True if has "normalEastAsianFlow" attribute
     */
    @Override
    public boolean isSetNormalEastAsianFlow() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[9]) != null;
        }
    }

    /**
     * Sets the "normalEastAsianFlow" attribute
     */
    @Override
    public void setNormalEastAsianFlow(boolean normalEastAsianFlow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.setBooleanValue(normalEastAsianFlow);
        }
    }

    /**
     * Sets (as xml) the "normalEastAsianFlow" attribute
     */
    @Override
    public void xsetNormalEastAsianFlow(org.apache.xmlbeans.XmlBoolean normalEastAsianFlow) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[9]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[9]);
            }
            target.set(normalEastAsianFlow);
        }
    }

    /**
     * Unsets the "normalEastAsianFlow" attribute
     */
    @Override
    public void unsetNormalEastAsianFlow() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[9]);
        }
    }
}
