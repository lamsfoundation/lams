/*
 * XML Type:  CT_NotesViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_NotesViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTNotesViewProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTNotesViewProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctnotesviewproperties48d6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cSldViewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideViewProperties getCSldViewPr();

    /**
     * Sets the "cSldViewPr" element
     */
    void setCSldViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideViewProperties cSldViewPr);

    /**
     * Appends and returns a new empty "cSldViewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideViewProperties addNewCSldViewPr();

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
}
