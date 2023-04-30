/*
 * XML Type:  CT_OleObjectLink
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOleObjectLink
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OleObjectLink(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOleObjectLink extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOleObjectLink> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoleobjectlinkd24etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "updateAutomatic" attribute
     */
    boolean getUpdateAutomatic();

    /**
     * Gets (as xml) the "updateAutomatic" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUpdateAutomatic();

    /**
     * True if has "updateAutomatic" attribute
     */
    boolean isSetUpdateAutomatic();

    /**
     * Sets the "updateAutomatic" attribute
     */
    void setUpdateAutomatic(boolean updateAutomatic);

    /**
     * Sets (as xml) the "updateAutomatic" attribute
     */
    void xsetUpdateAutomatic(org.apache.xmlbeans.XmlBoolean updateAutomatic);

    /**
     * Unsets the "updateAutomatic" attribute
     */
    void unsetUpdateAutomatic();
}
