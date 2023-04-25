/*
 * XML Type:  CT_NumPicBullet
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_NumPicBullet(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTNumPicBulletImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTNumPicBullet {
    private static final long serialVersionUID = 1L;

    public CTNumPicBulletImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "pict"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "numPicBulletId"),
    };


    /**
     * Gets the "pict" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture getPict() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "pict" element
     */
    @Override
    public boolean isSetPict() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "pict" element
     */
    @Override
    public void setPict(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture pict) {
        generatedSetterHelperImpl(pict, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "pict" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture addNewPict() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "pict" element
     */
    @Override
    public void unsetPict() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing getDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "drawing" element
     */
    @Override
    public void setDrawing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing drawing) {
        generatedSetterHelperImpl(drawing, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing addNewDrawing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "numPicBulletId" attribute
     */
    @Override
    public java.math.BigInteger getNumPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "numPicBulletId" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetNumPicBulletId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Sets the "numPicBulletId" attribute
     */
    @Override
    public void setNumPicBulletId(java.math.BigInteger numPicBulletId) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setBigIntegerValue(numPicBulletId);
        }
    }

    /**
     * Sets (as xml) the "numPicBulletId" attribute
     */
    @Override
    public void xsetNumPicBulletId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber numPicBulletId) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(numPicBulletId);
        }
    }
}
