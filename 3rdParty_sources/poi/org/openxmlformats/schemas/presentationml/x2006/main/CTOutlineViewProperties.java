/*
 * XML Type:  CT_OutlineViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OutlineViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOutlineViewProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoutlineviewproperties3335type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cViewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonViewProperties getCViewPr();

    /**
     * Sets the "cViewPr" element
     */
    void setCViewPr(org.openxmlformats.schemas.presentationml.x2006.main.CTCommonViewProperties cViewPr);

    /**
     * Appends and returns a new empty "cViewPr" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonViewProperties addNewCViewPr();

    /**
     * Gets the "sldLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideList getSldLst();

    /**
     * True if has "sldLst" element
     */
    boolean isSetSldLst();

    /**
     * Sets the "sldLst" element
     */
    void setSldLst(org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideList sldLst);

    /**
     * Appends and returns a new empty "sldLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTOutlineViewSlideList addNewSldLst();

    /**
     * Unsets the "sldLst" element
     */
    void unsetSldLst();

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
