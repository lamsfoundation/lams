/*
 * XML Type:  CT_ExtensionListModify
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_ExtensionListModify(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTExtensionListModifyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify {
    private static final long serialVersionUID = 1L;

    public CTExtensionListModifyImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "ext"),
        new QName("", "mod"),
    };


    /**
     * Gets a List of "ext" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTExtension> getExtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getExtArray,
                this::setExtArray,
                this::insertNewExt,
                this::removeExt,
                this::sizeOfExtArray
            );
        }
    }

    /**
     * Gets array of all "ext" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtension[] getExtArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTExtension[0]);
    }

    /**
     * Gets ith "ext" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtension getExtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtension target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtension)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ext" element
     */
    @Override
    public int sizeOfExtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ext" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setExtArray(org.openxmlformats.schemas.presentationml.x2006.main.CTExtension[] extArray) {
        check_orphaned();
        arraySetterHelper(extArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ext" element
     */
    @Override
    public void setExtArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTExtension ext) {
        generatedSetterHelperImpl(ext, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ext" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtension insertNewExt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtension target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtension)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ext" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTExtension addNewExt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTExtension target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTExtension)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ext" element
     */
    @Override
    public void removeExt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "mod" attribute
     */
    @Override
    public boolean getMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "mod" attribute
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetMod() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_default_attribute_value(PROPERTY_QNAME[1]);
            }
            return target;
        }
    }

    /**
     * True if has "mod" attribute
     */
    @Override
    public boolean isSetMod() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().find_attribute_user(PROPERTY_QNAME[1]) != null;
        }
    }

    /**
     * Sets the "mod" attribute
     */
    @Override
    public void setMod(boolean mod) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.setBooleanValue(mod);
        }
    }

    /**
     * Sets (as xml) the "mod" attribute
     */
    @Override
    public void xsetMod(org.apache.xmlbeans.XmlBoolean mod) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_attribute_user(PROPERTY_QNAME[1]);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_attribute_user(PROPERTY_QNAME[1]);
            }
            target.set(mod);
        }
    }

    /**
     * Unsets the "mod" attribute
     */
    @Override
    public void unsetMod() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_attribute(PROPERTY_QNAME[1]);
        }
    }
}
