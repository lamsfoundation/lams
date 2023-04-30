/*
 * XML Type:  CT_TLTimeTargetElement
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTimeTargetElement(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTimeTargetElementImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeTargetElement {
    private static final long serialVersionUID = 1L;

    public CTTLTimeTargetElementImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sldTgt"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "sndTgt"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "spTgt"),
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "inkTgt"),
    };


    /**
     * Gets the "sldTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getSldTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sldTgt" element
     */
    @Override
    public boolean isSetSldTgt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "sldTgt" element
     */
    @Override
    public void setSldTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty sldTgt) {
        generatedSetterHelperImpl(sldTgt, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sldTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewSldTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "sldTgt" element
     */
    @Override
    public void unsetSldTgt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "sndTgt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile getSndTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "sndTgt" element
     */
    @Override
    public boolean isSetSndTgt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "sndTgt" element
     */
    @Override
    public void setSndTgt(org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile sndTgt) {
        generatedSetterHelperImpl(sndTgt, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "sndTgt" element
     */
    @Override
    public org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile addNewSndTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile target = null;
            target = (org.openxmlformats.schemas.drawingml.x2006.main.CTEmbeddedWAVAudioFile)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "sndTgt" element
     */
    @Override
    public void unsetSndTgt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "spTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement getSpTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "spTgt" element
     */
    @Override
    public boolean isSetSpTgt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "spTgt" element
     */
    @Override
    public void setSpTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement spTgt) {
        generatedSetterHelperImpl(spTgt, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "spTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement addNewSpTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLShapeTargetElement)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "spTgt" element
     */
    @Override
    public void unsetSpTgt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "inkTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId getInkTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "inkTgt" element
     */
    @Override
    public boolean isSetInkTgt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "inkTgt" element
     */
    @Override
    public void setInkTgt(org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId inkTgt) {
        generatedSetterHelperImpl(inkTgt, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "inkTgt" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId addNewInkTgt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLSubShapeId)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "inkTgt" element
     */
    @Override
    public void unsetInkTgt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }
}
