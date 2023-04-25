/*
 * XML Type:  CT_Sources
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Sources(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTSourcesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSources {
    private static final long serialVersionUID = 1L;

    public CTSourcesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Source"),
        new QName("", "SelectedStyle"),
        new QName("", "StyleName"),
        new QName("", "URI"),
    };


    /**
     * Gets a List of "Source" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType> getSourceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSourceArray,
                this::setSourceArray,
                this::insertNewSource,
                this::removeSource,
                this::sizeOfSourceArray
            );
        }
    }

    /**
     * Gets array of all "Source" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType[] getSourceArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType[0]);
    }

    /**
     * Gets ith "Source" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType getSourceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Source" element
     */
    @Override
    public int sizeOfSourceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Source" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSourceArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType[] sourceArray) {
        check_orphaned();
        arraySetterHelper(sourceArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Source" element
     */
    @Override
    public void setSourceArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType source) {
        generatedSetterHelperImpl(source, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Source" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType insertNewSource(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Source" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType addNewSource() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Source" element
     */
    @Override
    public void removeSource(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "SelectedStyle" attribute
     */
    @Override
    public java.lang.String getSelectedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "SelectedStyle" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetSelectedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * True if has "SelectedStyle" attribute
     */
    @Override
    public boolean isSetSelectedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "SelectedStyle" attribute
     */
    @Override
    public void setSelectedStyle(java.lang.String selectedStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(selectedStyle);
        }
    }

    /**
     * Sets (as xml) the "SelectedStyle" attribute
     */
    @Override
    public void xsetSelectedStyle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString selectedStyle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(selectedStyle);
        }
    }

    /**
     * Unsets the "SelectedStyle" attribute
     */
    @Override
    public void unsetSelectedStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Gets the "StyleName" attribute
     */
    @Override
    public java.lang.String getStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "StyleName" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * True if has "StyleName" attribute
     */
    @Override
    public boolean isSetStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[2]) != null;
        }
    }

    /**
     * Sets the "StyleName" attribute
     */
    @Override
    public void setStyleName(java.lang.String styleName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(styleName);
        }
    }

    /**
     * Sets (as xml) the "StyleName" attribute
     */
    @Override
    public void xsetStyleName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString styleName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[2]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[2]);
            }
            target.set(styleName);
        }
    }

    /**
     * Unsets the "StyleName" attribute
     */
    @Override
    public void unsetStyleName() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Gets the "URI" attribute
     */
    @Override
    public java.lang.String getURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "URI" attribute
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetURI() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * True if has "URI" attribute
     */
    @Override
    public boolean isSetURI() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[3]) != null;
        }
    }

    /**
     * Sets the "URI" attribute
     */
    @Override
    public void setURI(java.lang.String uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.setStringValue(uri);
        }
    }

    /**
     * Sets (as xml) the "URI" attribute
     */
    @Override
    public void xsetURI(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString uri) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_attribute_user(PROPERTY_QNAME[3]);
            if (target == null) {
                target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_attribute_user(PROPERTY_QNAME[3]);
            }
            target.set(uri);
        }
    }

    /**
     * Unsets the "URI" attribute
     */
    @Override
    public void unsetURI() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[3]);
        }
    }
}
