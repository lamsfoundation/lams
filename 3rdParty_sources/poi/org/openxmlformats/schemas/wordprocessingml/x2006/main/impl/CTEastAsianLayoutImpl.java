/*
 * XML Type:  CT_EastAsianLayout
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_EastAsianLayout(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTEastAsianLayoutImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout {
    private static final long serialVersionUID = 1L;

    public CTEastAsianLayoutImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "id"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "combine"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "combineBrackets"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vert"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "vertCompress"),
    };


    /**
     * Gets the "id" attribute
     */
    @Override
    public java.math.BigInteger getId() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return (target == null) ? null : target.getBigIntegerValue();
        }
    }

    /**
     * Gets (as xml) the "id" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetId() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * True if has "id" attribute
     */
    @Override
    public boolean isSetId() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[0]) != null;
        }
    }

    /**
     * Sets the "id" attribute
     */
    @Override
    public void setId(java.math.BigInteger id) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.setBigIntegerValue(id);
        }
    }

    /**
     * Sets (as xml) the "id" attribute
     */
    @Override
    public void xsetId(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber id) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().find_attribute_user(PROPERTY_QNAME[0]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber)get_store().add_attribute_user(PROPERTY_QNAME[0]);
            }
            target.set(id);
        }
    }

    /**
     * Unsets the "id" attribute
     */
    @Override
    public void unsetId() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Gets the "combine" attribute
     */
    @Override
    public java.lang.Object getCombine() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "combine" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetCombine() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "combine" attribute
     */
    @Override
    public boolean isSetCombine() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "combine" attribute
     */
    @Override
    public void setCombine(java.lang.Object combine) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setObjectValue(combine);
        }
    }

    /**
     * Sets (as xml) the "combine" attribute
     */
    @Override
    public void xsetCombine(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff combine) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(combine);
        }
    }

    /**
     * Unsets the "combine" attribute
     */
    @Override
    public void unsetCombine() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "combineBrackets" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets.Enum getCombineBrackets() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) the "combineBrackets" attribute
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets xgetCombineBrackets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "combineBrackets" attribute
     */
    @Override
    public boolean isSetCombineBrackets() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "combineBrackets" attribute
     */
    @Override
    public void setCombineBrackets(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets.Enum combineBrackets) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setEnumValue(combineBrackets);
        }
    }

    /**
     * Sets (as xml) the "combineBrackets" attribute
     */
    @Override
    public void xsetCombineBrackets(org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets combineBrackets) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.STCombineBrackets)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(combineBrackets);
        }
    }

    /**
     * Unsets the "combineBrackets" attribute
     */
    @Override
    public void unsetCombineBrackets() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "vert" attribute
     */
    @Override
    public java.lang.Object getVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "vert" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetVert() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "vert" attribute
     */
    @Override
    public boolean isSetVert() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "vert" attribute
     */
    @Override
    public void setVert(java.lang.Object vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setObjectValue(vert);
        }
    }

    /**
     * Sets (as xml) the "vert" attribute
     */
    @Override
    public void xsetVert(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff vert) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(vert);
        }
    }

    /**
     * Unsets the "vert" attribute
     */
    @Override
    public void unsetVert() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Gets the "vertCompress" attribute
     */
    @Override
    public java.lang.Object getVertCompress() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return (target == null) ? null : target.getObjectValue();
        }
    }

    /**
     * Gets (as xml) the "vertCompress" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetVertCompress() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * True if has "vertCompress" attribute
     */
    @Override
    public boolean isSetVertCompress() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[4]) != null;
        }
    }

    /**
     * Sets the "vertCompress" attribute
     */
    @Override
    public void setVertCompress(java.lang.Object vertCompress) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.setObjectValue(vertCompress);
        }
    }

    /**
     * Sets (as xml) the "vertCompress" attribute
     */
    @Override
    public void xsetVertCompress(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff vertCompress) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().find_attribute_user(PROPERTY_QNAME[4]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff)get_store().add_attribute_user(PROPERTY_QNAME[4]);
            }
            target.set(vertCompress);
        }
    }

    /**
     * Unsets the "vertCompress" attribute
     */
    @Override
    public void unsetVertCompress() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[4]);
        }
    }
}
