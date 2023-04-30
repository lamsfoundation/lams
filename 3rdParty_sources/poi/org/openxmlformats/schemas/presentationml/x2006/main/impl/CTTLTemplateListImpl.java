/*
 * XML Type:  CT_TLTemplateList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplateList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_TLTemplateList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public class CTTLTemplateListImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplateList {
    private static final long serialVersionUID = 1L;

    public CTTLTemplateListImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/presentationml/2006/main", "tmpl"),
    };


    /**
     * Gets a List of "tmpl" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate> getTmplList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTmplArray,
                this::setTmplArray,
                this::insertNewTmpl,
                this::removeTmpl,
                this::sizeOfTmplArray
            );
        }
    }

    /**
     * Gets array of all "tmpl" elements
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate[] getTmplArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate[0]);
    }

    /**
     * Gets ith "tmpl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate getTmplArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "tmpl" element
     */
    @Override
    public int sizeOfTmplArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "tmpl" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTmplArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate[] tmplArray) {
        check_orphaned();
        arraySetterHelper(tmplArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "tmpl" element
     */
    @Override
    public void setTmplArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate tmpl) {
        generatedSetterHelperImpl(tmpl, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tmpl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate insertNewTmpl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "tmpl" element
     */
    @Override
    public org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate addNewTmpl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate target = null;
            target = (org.openxmlformats.schemas.presentationml.x2006.main.CTTLTemplate)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "tmpl" element
     */
    @Override
    public void removeTmpl(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }
}
