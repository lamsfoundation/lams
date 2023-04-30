/*
 * XML Type:  CT_SlideSorterViewProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SlideSorterViewProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSlideSorterViewProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTSlideSorterViewProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctslidesorterviewpropertiesea5btype");
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
     * Gets the "showFormatting" attribute
     */
    boolean getShowFormatting();

    /**
     * Gets (as xml) the "showFormatting" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowFormatting();

    /**
     * True if has "showFormatting" attribute
     */
    boolean isSetShowFormatting();

    /**
     * Sets the "showFormatting" attribute
     */
    void setShowFormatting(boolean showFormatting);

    /**
     * Sets (as xml) the "showFormatting" attribute
     */
    void xsetShowFormatting(org.apache.xmlbeans.XmlBoolean showFormatting);

    /**
     * Unsets the "showFormatting" attribute
     */
    void unsetShowFormatting();
}
