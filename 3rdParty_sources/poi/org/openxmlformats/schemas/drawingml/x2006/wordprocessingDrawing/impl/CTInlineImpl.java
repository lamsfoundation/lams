/*
 * XML Type:  CT_Inline
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing
 * Java type: org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Inline(@http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing).
 *
 * This is a complex type.
 */
public class CTInlineImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline {
    private static final long serialVersionUID = 1L;

    public CTInlineImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "extent"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "effectExtent"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "docPr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing", "cNvGraphicFramePr"),
        new QName("http://schemas.openxmlformats.org/drawingml/2006/main", "graphic"),
        new QName("", "distT"),
        new QName("", "distB"),
        new QName("", "distL"),
        new QName("", "distR"),
    };


    /**
     * Gets the "extent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D getExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "extent" element
     */
    @Override
    public void setExtent(org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D extent) {
        generatedSetterHelperImpl(extent, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D addNewExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Gets the "effectExtent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent getEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "effectExtent" element
     */
    @Override
    public boolean isSetEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "effectExtent" element
     */
    @Override
    public void setEffectExtent(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent effectExtent) {
        generatedSetterHelperImpl(effectExtent, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "effectExtent" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent addNewEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTEffectExtent)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "effectExtent" element
     */
    @Override
    public void unsetEffectExtent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "docPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps getDocPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "docPr" element
     */
    @Override
    public void setDocPr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps docPr) {
        generatedSetterHelperImpl(docPr, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docPr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps addNewDocPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Gets the "cNvGraphicFramePr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties getCNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "cNvGraphicFramePr" element
     */
    @Override
    public boolean isSetCNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "cNvGraphicFramePr" element
     */
    @Override
    public void setCNvGraphicFramePr(org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties cNvGraphicFramePr) {
        generatedSetterHelperImpl(cNvGraphicFramePr, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "cNvGraphicFramePr" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties addNewCNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualGraphicFrameProperties)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "cNvGraphicFramePr" element
     */
    @Override
    public void unsetCNvGraphicFramePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "graphic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject getGraphic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "graphic" element
     */
    @Override
    public void setGraphic(org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject graphic) {
        generatedSetterHelperImpl(graphic, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphic" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject addNewGraphic() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Gets the "distT" attribute
     */
    @Override
    public long getDistT() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distT" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "distT" attribute
     */
    @Override
    public boolean isSetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "distT" attribute
     */
    @Override
    public void setDistT(long distT) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setLongValue(distT);
        }
    }

    /**
     * Sets (as xml) the "distT" attribute
     */
    @Override
    public void xsetDistT(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distT) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(distT);
        }
    }

    /**
     * Unsets the "distT" attribute
     */
    @Override
    public void unsetDistT() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "distB" attribute
     */
    @Override
    public long getDistB() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distB" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "distB" attribute
     */
    @Override
    public boolean isSetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "distB" attribute
     */
    @Override
    public void setDistB(long distB) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setLongValue(distB);
        }
    }

    /**
     * Sets (as xml) the "distB" attribute
     */
    @Override
    public void xsetDistB(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distB) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(distB);
        }
    }

    /**
     * Unsets the "distB" attribute
     */
    @Override
    public void unsetDistB() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Gets the "distL" attribute
     */
    @Override
    public long getDistL() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distL" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * True if has "distL" attribute
     */
    @Override
    public boolean isSetDistL() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[7]) != null;
        }
    }

    /**
     * Sets the "distL" attribute
     */
    @Override
    public void setDistL(long distL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.setLongValue(distL);
        }
    }

    /**
     * Sets (as xml) the "distL" attribute
     */
    @Override
    public void xsetDistL(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distL) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[7]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[7]);
            }
            target.set(distL);
        }
    }

    /**
     * Unsets the "distL" attribute
     */
    @Override
    public void unsetDistL() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Gets the "distR" attribute
     */
    @Override
    public long getDistR() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "distR" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance xgetDistR() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * True if has "distR" attribute
     */
    @Override
    public boolean isSetDistR() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[8]) != null;
        }
    }

    /**
     * Sets the "distR" attribute
     */
    @Override
    public void setDistR(long distR) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.setLongValue(distR);
        }
    }

    /**
     * Sets (as xml) the "distR" attribute
     */
    @Override
    public void xsetDistR(org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance distR) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().find_attribute_user(PROPERTY_QNAME[8]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.STWrapDistance)get_store().add_attribute_user(PROPERTY_QNAME[8]);
            }
            target.set(distR);
        }
    }

    /**
     * Unsets the "distR" attribute
     */
    @Override
    public void unsetDistR() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[8]);
        }
    }
}
