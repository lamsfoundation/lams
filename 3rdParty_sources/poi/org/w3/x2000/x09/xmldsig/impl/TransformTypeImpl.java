/*
 * XML Type:  TransformType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.TransformType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML TransformType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class TransformTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.TransformType {
    private static final long serialVersionUID = 1L;

    public TransformTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "XPath"),
        new QName("", "Algorithm"),
    };


    /**
     * Gets a List of "XPath" elements
     */
    @Override
    public java.util.List<java.lang.String> getXPathList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getXPathArray,
                this::setXPathArray,
                this::insertXPath,
                this::removeXPath,
                this::sizeOfXPathArray
            );
        }
    }

    /**
     * Gets array of all "XPath" elements
     */
    @Override
    public java.lang.String[] getXPathArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "XPath" element
     */
    @Override
    public java.lang.String getXPathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "XPath" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetXPathList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetXPathArray,
                this::xsetXPathArray,
                this::insertNewXPath,
                this::removeXPath,
                this::sizeOfXPathArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "XPath" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetXPathArray() {
        return xgetArray(PROPERTY_QNAME[0], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "XPath" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetXPathArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "XPath" element
     */
    @Override
    public int sizeOfXPathArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "XPath" element
     */
    @Override
    public void setXPathArray(java.lang.String[] xPathArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(xPathArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "XPath" element
     */
    @Override
    public void setXPathArray(int i, java.lang.String xPath) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(xPath);
        }
    }

    /**
     * Sets (as xml) array of all "XPath" element
     */
    @Override
    public void xsetXPathArray(org.apache.xmlbeans.XmlString[]xPathArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(xPathArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "XPath" element
     */
    @Override
    public void xsetXPathArray(int i, org.apache.xmlbeans.XmlString xPath) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(xPath);
        }
    }

    /**
     * Inserts the value as the ith "XPath" element
     */
    @Override
    public void insertXPath(int i, java.lang.String xPath) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(xPath);
        }
    }

    /**
     * Appends the value as the last "XPath" element
     */
    @Override
    public void addXPath(java.lang.String xPath) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(xPath);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "XPath" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewXPath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "XPath" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewXPath() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "XPath" element
     */
    @Override
    public void removeXPath(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "Algorithm" attribute
     */
    @Override
    public java.lang.String getAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Algorithm" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlAnyURI xgetAlgorithm() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Sets the "Algorithm" attribute
     */
    @Override
    public void setAlgorithm(java.lang.String algorithm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(algorithm);
        }
    }

    /**
     * Sets (as xml) the "Algorithm" attribute
     */
    @Override
    public void xsetAlgorithm(org.apache.xmlbeans.XmlAnyURI algorithm) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlAnyURI target = null;
            target = (org.apache.xmlbeans.XmlAnyURI)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlAnyURI)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(algorithm);
        }
    }
}
