/*
 * XML Type:  CT_ObjectLink
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ObjectLink(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTObjectLink extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectEmbed {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTObjectLink> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctobjectlinkabeftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "updateMode" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode.Enum getUpdateMode();

    /**
     * Gets (as xml) the "updateMode" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode xgetUpdateMode();

    /**
     * Sets the "updateMode" attribute
     */
    void setUpdateMode(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode.Enum updateMode);

    /**
     * Sets (as xml) the "updateMode" attribute
     */
    void xsetUpdateMode(org.openxmlformats.schemas.wordprocessingml.x2006.main.STObjectUpdateMode updateMode);

    /**
     * Gets the "lockedField" attribute
     */
    java.lang.Object getLockedField();

    /**
     * Gets (as xml) the "lockedField" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetLockedField();

    /**
     * True if has "lockedField" attribute
     */
    boolean isSetLockedField();

    /**
     * Sets the "lockedField" attribute
     */
    void setLockedField(java.lang.Object lockedField);

    /**
     * Sets (as xml) the "lockedField" attribute
     */
    void xsetLockedField(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff lockedField);

    /**
     * Unsets the "lockedField" attribute
     */
    void unsetLockedField();
}
