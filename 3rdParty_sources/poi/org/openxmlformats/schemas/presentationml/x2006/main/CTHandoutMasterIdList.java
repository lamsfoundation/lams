/*
 * XML Type:  CT_HandoutMasterIdList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HandoutMasterIdList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHandoutMasterIdList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthandoutmasteridlist5b95type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "handoutMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry getHandoutMasterId();

    /**
     * True if has "handoutMasterId" element
     */
    boolean isSetHandoutMasterId();

    /**
     * Sets the "handoutMasterId" element
     */
    void setHandoutMasterId(org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry handoutMasterId);

    /**
     * Appends and returns a new empty "handoutMasterId" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMasterIdListEntry addNewHandoutMasterId();

    /**
     * Unsets the "handoutMasterId" element
     */
    void unsetHandoutMasterId();
}
