/*
 * XML Type:  CT_BookViews
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_BookViews(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public class CTBookViewsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookViews {
    private static final long serialVersionUID = 1L;

    public CTBookViewsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/spreadsheetml/2006/main", "workbookView"),
    };


    /**
     * Gets a List of "workbookView" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView> getWorkbookViewList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWorkbookViewArray,
                this::setWorkbookViewArray,
                this::insertNewWorkbookView,
                this::removeWorkbookView,
                this::sizeOfWorkbookViewArray
            );
        }
    }

    /**
     * Gets array of all "workbookView" elements
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView[] getWorkbookViewArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView[0]);
    }

    /**
     * Gets ith "workbookView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView getWorkbookViewArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "workbookView" element
     */
    @Override
    public int sizeOfWorkbookViewArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "workbookView" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWorkbookViewArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView[] workbookViewArray) {
        check_orphaned();
        arraySetterHelper(workbookViewArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "workbookView" element
     */
    @Override
    public void setWorkbookViewArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView workbookView) {
        generatedSetterHelperImpl(workbookView, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "workbookView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView insertNewWorkbookView(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "workbookView" element
     */
    @Override
    public org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView addNewWorkbookView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView target = null;
            target = (org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "workbookView" element
     */
    @Override
    public void removeWorkbookView(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
