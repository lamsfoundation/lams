/*
 * XML Type:  CT_ConnectionSiteList
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ConnectionSiteList(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConnectionSiteList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSiteList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconnectionsitelistab9etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cxn" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite> getCxnList();

    /**
     * Gets array of all "cxn" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite[] getCxnArray();

    /**
     * Gets ith "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite getCxnArray(int i);

    /**
     * Returns number of "cxn" element
     */
    int sizeOfCxnArray();

    /**
     * Sets array of all "cxn" element
     */
    void setCxnArray(org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite[] cxnArray);

    /**
     * Sets ith "cxn" element
     */
    void setCxnArray(int i, org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite cxn);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite insertNewCxn(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cxn" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTConnectionSite addNewCxn();

    /**
     * Removes the ith "cxn" element
     */
    void removeCxn(int i);
}
