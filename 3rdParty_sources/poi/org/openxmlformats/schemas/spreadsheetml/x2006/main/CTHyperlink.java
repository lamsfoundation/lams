/*
 * XML Type:  CT_Hyperlink
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Hyperlink(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHyperlink extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHyperlink> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthyperlink0c85type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ref" attribute
     */
    java.lang.String getRef();

    /**
     * Gets (as xml) the "ref" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetRef();

    /**
     * Sets the "ref" attribute
     */
    void setRef(java.lang.String ref);

    /**
     * Sets (as xml) the "ref" attribute
     */
    void xsetRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef ref);

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();

    /**
     * Gets the "location" attribute
     */
    java.lang.String getLocation();

    /**
     * Gets (as xml) the "location" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetLocation();

    /**
     * True if has "location" attribute
     */
    boolean isSetLocation();

    /**
     * Sets the "location" attribute
     */
    void setLocation(java.lang.String location);

    /**
     * Sets (as xml) the "location" attribute
     */
    void xsetLocation(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring location);

    /**
     * Unsets the "location" attribute
     */
    void unsetLocation();

    /**
     * Gets the "tooltip" attribute
     */
    java.lang.String getTooltip();

    /**
     * Gets (as xml) the "tooltip" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetTooltip();

    /**
     * True if has "tooltip" attribute
     */
    boolean isSetTooltip();

    /**
     * Sets the "tooltip" attribute
     */
    void setTooltip(java.lang.String tooltip);

    /**
     * Sets (as xml) the "tooltip" attribute
     */
    void xsetTooltip(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring tooltip);

    /**
     * Unsets the "tooltip" attribute
     */
    void unsetTooltip();

    /**
     * Gets the "display" attribute
     */
    java.lang.String getDisplay();

    /**
     * Gets (as xml) the "display" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDisplay();

    /**
     * True if has "display" attribute
     */
    boolean isSetDisplay();

    /**
     * Sets the "display" attribute
     */
    void setDisplay(java.lang.String display);

    /**
     * Sets (as xml) the "display" attribute
     */
    void xsetDisplay(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring display);

    /**
     * Unsets the "display" attribute
     */
    void unsetDisplay();
}
