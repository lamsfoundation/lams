/*
 * An XML document type.
 * Localname: additionalCharacteristics
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/characteristics
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.characteristics.AdditionalCharacteristicsDocument
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.characteristics.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * A document containing one additionalCharacteristics(@http://schemas.openxmlformats.org/officeDocument/2006/characteristics) element.
 *
 * This is a complex type.
 */
public class AdditionalCharacteristicsDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.characteristics.AdditionalCharacteristicsDocument {
    private static final long serialVersionUID = 1L;

    public AdditionalCharacteristicsDocumentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/characteristics", "additionalCharacteristics"),
    };


    /**
     * Gets the "additionalCharacteristics" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics getAdditionalCharacteristics() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * Sets the "additionalCharacteristics" element
     */
    @Override
    public void setAdditionalCharacteristics(org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics additionalCharacteristics) {
        generatedSetterHelperImpl(additionalCharacteristics, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "additionalCharacteristics" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics addNewAdditionalCharacteristics() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTAdditionalCharacteristics)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }
}
