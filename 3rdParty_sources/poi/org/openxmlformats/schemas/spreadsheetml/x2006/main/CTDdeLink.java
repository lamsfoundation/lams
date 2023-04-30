/*
 * XML Type:  CT_DdeLink
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DdeLink(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDdeLink extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeLink> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctddelink931etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ddeItems" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems getDdeItems();

    /**
     * True if has "ddeItems" element
     */
    boolean isSetDdeItems();

    /**
     * Sets the "ddeItems" element
     */
    void setDdeItems(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems ddeItems);

    /**
     * Appends and returns a new empty "ddeItems" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeItems addNewDdeItems();

    /**
     * Unsets the "ddeItems" element
     */
    void unsetDdeItems();

    /**
     * Gets the "ddeService" attribute
     */
    java.lang.String getDdeService();

    /**
     * Gets (as xml) the "ddeService" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDdeService();

    /**
     * Sets the "ddeService" attribute
     */
    void setDdeService(java.lang.String ddeService);

    /**
     * Sets (as xml) the "ddeService" attribute
     */
    void xsetDdeService(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring ddeService);

    /**
     * Gets the "ddeTopic" attribute
     */
    java.lang.String getDdeTopic();

    /**
     * Gets (as xml) the "ddeTopic" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDdeTopic();

    /**
     * Sets the "ddeTopic" attribute
     */
    void setDdeTopic(java.lang.String ddeTopic);

    /**
     * Sets (as xml) the "ddeTopic" attribute
     */
    void xsetDdeTopic(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring ddeTopic);
}
