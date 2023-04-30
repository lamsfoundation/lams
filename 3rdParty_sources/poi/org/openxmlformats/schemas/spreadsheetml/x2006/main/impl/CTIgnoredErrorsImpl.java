/*
 * XML Type:  CT_IgnoredErrors
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_IgnoredErrors(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTIgnoredErrorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredErrors {
    private static final long serialVersionUID = 1L;

    public CTIgnoredErrorsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "ignoredError"),
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "extLst"),
    };


    /**
     * Gets a List of "ignoredError" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError> getIgnoredErrorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getIgnoredErrorArray,
                this::setIgnoredErrorArray,
                this::insertNewIgnoredError,
                this::removeIgnoredError,
                this::sizeOfIgnoredErrorArray
            );
        }
    }

    /**
     * Gets array of all "ignoredError" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError[] getIgnoredErrorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError[0]);
    }

    /**
     * Gets ith "ignoredError" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError getIgnoredErrorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ignoredError" element
     */
    @Override
    public int sizeOfIgnoredErrorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "ignoredError" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setIgnoredErrorArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError[] ignoredErrorArray) {
        check_orphaned();
        arraySetterHelper(ignoredErrorArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "ignoredError" element
     */
    @Override
    public void setIgnoredErrorArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError ignoredError) {
        generatedSetterHelperImpl(ignoredError, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ignoredError" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError insertNewIgnoredError(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ignoredError" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError addNewIgnoredError() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIgnoredError)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "ignoredError" element
     */
    @Override
    public void removeIgnoredError(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets the "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().find_element_user(PROPERTY_QNAME[1], 0);
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
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "extLst" element
     */
    @Override
    public void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst) {
        generatedSetterHelperImpl(extLst, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "extLst" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList)get_store().add_element_user(PROPERTY_QNAME[1]);
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
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }
}
