/*
 * XML Type:  CT_HandoutMaster
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HandoutMaster(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHandoutMaster extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTHandoutMaster> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthandoutmasterb6aetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "cSld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData getCSld();

    /**
     * Sets the "cSld" element
     */
    void setCSld(org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData cSld);

    /**
     * Appends and returns a new empty "cSld" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCommonSlideData addNewCSld();

    /**
     * Gets the "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping getClrMap();

    /**
     * Sets the "clrMap" element
     */
    void setClrMap(org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping clrMap);

    /**
     * Appends and returns a new empty "clrMap" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTColorMapping addNewClrMap();

    /**
     * Gets the "hf" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter getHf();

    /**
     * True if has "hf" element
     */
    boolean isSetHf();

    /**
     * Sets the "hf" element
     */
    void setHf(org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter hf);

    /**
     * Appends and returns a new empty "hf" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTHeaderFooter addNewHf();

    /**
     * Unsets the "hf" element
     */
    void unsetHf();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionListModify addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
