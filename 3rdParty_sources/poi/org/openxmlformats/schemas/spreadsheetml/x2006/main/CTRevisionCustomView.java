/*
 * XML Type:  CT_RevisionCustomView
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RevisionCustomView(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRevisionCustomView extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionCustomView> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrevisioncustomview9d3atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "guid" attribute
     */
    java.lang.String getGuid();

    /**
     * Gets (as xml) the "guid" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid xgetGuid();

    /**
     * Sets the "guid" attribute
     */
    void setGuid(java.lang.String guid);

    /**
     * Sets (as xml) the "guid" attribute
     */
    void xsetGuid(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STGuid guid);

    /**
     * Gets the "action" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRevisionAction.Enum getAction();

    /**
     * Gets (as xml) the "action" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRevisionAction xgetAction();

    /**
     * Sets the "action" attribute
     */
    void setAction(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRevisionAction.Enum action);

    /**
     * Sets (as xml) the "action" attribute
     */
    void xsetAction(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRevisionAction action);
}
