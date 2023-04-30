/*
 * XML Type:  CT_OutlineViewSlideEntry
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OutlineViewSlideEntry(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOutlineViewSlideEntry extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideEntry> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoutlineviewslideentry9e67type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);

    /**
     * Gets the "collapse" attribute
     */
    boolean getCollapse();

    /**
     * Gets (as xml) the "collapse" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetCollapse();

    /**
     * True if has "collapse" attribute
     */
    boolean isSetCollapse();

    /**
     * Sets the "collapse" attribute
     */
    void setCollapse(boolean collapse);

    /**
     * Sets (as xml) the "collapse" attribute
     */
    void xsetCollapse(org.apache.xmlbeans.XmlBoolean collapse);

    /**
     * Unsets the "collapse" attribute
     */
    void unsetCollapse();
}
