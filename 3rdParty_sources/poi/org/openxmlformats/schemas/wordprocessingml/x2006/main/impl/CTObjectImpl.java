/*
 * XML Type:  CT_Object
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Object(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTObjectImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObject {
    private static final long serialVersionUID = 1L;

    public CTObjectImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "control"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "objectLink"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "objectEmbed"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "movie"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dxaOrig"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "dyaOrig"),
    };


    /**
     * Gets the "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing getDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawing" element
     */
    @Override
    public boolean isSetDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "drawing" element
     */
    @Override
    public void setDrawing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing drawing) {
        generatedSetterHelperImpl(drawing, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing addNewDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "drawing" element
     */
    @Override
    public void unsetDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "control" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl getControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "control" element
     */
    @Override
    public boolean isSetControl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "control" element
     */
    @Override
    public void setControl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl control) {
        generatedSetterHelperImpl(control, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "control" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl addNewControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTControl)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "control" element
     */
    @Override
    public void unsetControl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "objectLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink getObjectLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "objectLink" element
     */
    @Override
    public boolean isSetObjectLink() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "objectLink" element
     */
    @Override
    public void setObjectLink(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink objectLink) {
        generatedSetterHelperImpl(objectLink, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "objectLink" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink addNewObjectLink() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "objectLink" element
     */
    @Override
    public void unsetObjectLink() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "objectEmbed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed getObjectEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "objectEmbed" element
     */
    @Override
    public boolean isSetObjectEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "objectEmbed" element
     */
    @Override
    public void setObjectEmbed(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed objectEmbed) {
        generatedSetterHelperImpl(objectEmbed, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "objectEmbed" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed addNewObjectEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "objectEmbed" element
     */
    @Override
    public void unsetObjectEmbed() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "movie" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getMovie() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "movie" element
     */
    @Override
    public boolean isSetMovie() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "movie" element
     */
    @Override
    public void setMovie(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel movie) {
        generatedSetterHelperImpl(movie, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "movie" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewMovie() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "movie" element
     */
    @Override
    public void unsetMovie() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "dxaOrig" attribute
     */
    @Override
    public java.lang.Object getDxaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "dxaOrig" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetDxaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * True if has "dxaOrig" attribute
     */
    @Override
    public boolean isSetDxaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[5]) != null;
        }
    }

    /**
     * Sets the "dxaOrig" attribute
     */
    @Override
    public void setDxaOrig(java.lang.Object dxaOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.setObjectValue(dxaOrig);
        }
    }

    /**
     * Sets (as xml) the "dxaOrig" attribute
     */
    @Override
    public void xsetDxaOrig(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure dxaOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[5]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[5]);
            }
            target.set(dxaOrig);
        }
    }

    /**
     * Unsets the "dxaOrig" attribute
     */
    @Override
    public void unsetDxaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Gets the "dyaOrig" attribute
     */
    @Override
    public java.lang.Object getDyaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "dyaOrig" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure xgetDyaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * True if has "dyaOrig" attribute
     */
    @Override
    public boolean isSetDyaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[6]) != null;
        }
    }

    /**
     * Sets the "dyaOrig" attribute
     */
    @Override
    public void setDyaOrig(java.lang.Object dyaOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.setObjectValue(dyaOrig);
        }
    }

    /**
     * Sets (as xml) the "dyaOrig" attribute
     */
    @Override
    public void xsetDyaOrig(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure dyaOrig) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().find_attribute_user(PROPERTY_QNAME[6]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STTwipsMeasure)get_store().add_attribute_user(PROPERTY_QNAME[6]);
            }
            target.set(dyaOrig);
        }
    }

    /**
     * Unsets the "dyaOrig" attribute
     */
    @Override
    public void unsetDyaOrig() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[6]);
        }
    }
}
