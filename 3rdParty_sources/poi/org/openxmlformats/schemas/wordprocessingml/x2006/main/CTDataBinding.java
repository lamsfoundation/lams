/*
 * XML Type:  CT_DataBinding
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDataBinding
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DataBinding(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDataBinding extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDataBinding> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdatabinding9077type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "prefixMappings" attribute
     */
    java.lang.String getPrefixMappings();

    /**
     * Gets (as xml) the "prefixMappings" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPrefixMappings();

    /**
     * True if has "prefixMappings" attribute
     */
    boolean isSetPrefixMappings();

    /**
     * Sets the "prefixMappings" attribute
     */
    void setPrefixMappings(java.lang.String prefixMappings);

    /**
     * Sets (as xml) the "prefixMappings" attribute
     */
    void xsetPrefixMappings(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString prefixMappings);

    /**
     * Unsets the "prefixMappings" attribute
     */
    void unsetPrefixMappings();

    /**
     * Gets the "xpath" attribute
     */
    java.lang.String getXpath();

    /**
     * Gets (as xml) the "xpath" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetXpath();

    /**
     * Sets the "xpath" attribute
     */
    void setXpath(java.lang.String xpath);

    /**
     * Sets (as xml) the "xpath" attribute
     */
    void xsetXpath(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xpath);

    /**
     * Gets the "storeItemID" attribute
     */
    java.lang.String getStoreItemID();

    /**
     * Gets (as xml) the "storeItemID" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStoreItemID();

    /**
     * Sets the "storeItemID" attribute
     */
    void setStoreItemID(java.lang.String storeItemID);

    /**
     * Sets (as xml) the "storeItemID" attribute
     */
    void xsetStoreItemID(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString storeItemID);
}
