/*
 * XML Type:  CT_TLShapeTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLShapeTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLShapeTargetElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement {
    private static final long serialVersionUID = 1L;

    public CTTLShapeTargetElementImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "bg"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "subSp"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "oleChartEl"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "txEl"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "graphicEl"),
        new QName("", "spid"),
    };


    /**
     * Gets the "bg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bg" element
     */
    @Override
    public boolean isSetBg() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "bg" element
     */
    @Override
    public void setBg(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty bg) {
        generatedSetterHelperImpl(bg, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bg" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewBg() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "bg" element
     */
    @Override
    public void unsetBg() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "subSp" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId getSubSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "subSp" element
     */
    @Override
    public boolean isSetSubSp() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "subSp" element
     */
    @Override
    public void setSubSp(org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId subSp) {
        generatedSetterHelperImpl(subSp, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "subSp" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId addNewSubSp() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "subSp" element
     */
    @Override
    public void unsetSubSp() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "oleChartEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement getOleChartEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "oleChartEl" element
     */
    @Override
    public boolean isSetOleChartEl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "oleChartEl" element
     */
    @Override
    public void setOleChartEl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement oleChartEl) {
        generatedSetterHelperImpl(oleChartEl, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "oleChartEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement addNewOleChartEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleChartTargetElement)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "oleChartEl" element
     */
    @Override
    public void unsetOleChartEl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "txEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement getTxEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "txEl" element
     */
    @Override
    public boolean isSetTxEl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "txEl" element
     */
    @Override
    public void setTxEl(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement txEl) {
        generatedSetterHelperImpl(txEl, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "txEl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement addNewTxEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTextTargetElement)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "txEl" element
     */
    @Override
    public void unsetTxEl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "graphicEl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice getGraphicEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "graphicEl" element
     */
    @Override
    public boolean isSetGraphicEl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "graphicEl" element
     */
    @Override
    public void setGraphicEl(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice graphicEl) {
        generatedSetterHelperImpl(graphicEl, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "graphicEl" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice addNewGraphicEl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationElementChoice)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "graphicEl" element
     */
    @Override
    public void unsetGraphicEl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "spid" attribute
     */
    @Override
    public long getSpid() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? 0L : target.getLongValue();
        }
    }

    /**
     * Gets (as xml) the "spid" attribute
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId xgetSpid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Sets the "spid" attribute
     */
    @Override
    public void setSpid(long spid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setLongValue(spid);
        }
    }

    /**
     * Sets (as xml) the "spid" attribute
     */
    @Override
    public void xsetSpid(org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId spid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(spid);
        }
    }
}
