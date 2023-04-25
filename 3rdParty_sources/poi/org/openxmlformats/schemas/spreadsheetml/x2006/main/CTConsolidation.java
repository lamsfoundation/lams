/*
 * XML Type:  CT_Consolidation
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConsolidation
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Consolidation(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConsolidation extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConsolidation> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconsolidation894btype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pages" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages getPages();

    /**
     * True if has "pages" element
     */
    boolean isSetPages();

    /**
     * Sets the "pages" element
     */
    void setPages(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages pages);

    /**
     * Appends and returns a new empty "pages" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPages addNewPages();

    /**
     * Unsets the "pages" element
     */
    void unsetPages();

    /**
     * Gets the "rangeSets" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets getRangeSets();

    /**
     * Sets the "rangeSets" element
     */
    void setRangeSets(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets rangeSets);

    /**
     * Appends and returns a new empty "rangeSets" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRangeSets addNewRangeSets();

    /**
     * Gets the "autoPage" attribute
     */
    boolean getAutoPage();

    /**
     * Gets (as xml) the "autoPage" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoPage();

    /**
     * True if has "autoPage" attribute
     */
    boolean isSetAutoPage();

    /**
     * Sets the "autoPage" attribute
     */
    void setAutoPage(boolean autoPage);

    /**
     * Sets (as xml) the "autoPage" attribute
     */
    void xsetAutoPage(org.apache.xmlbeans.XmlBoolean autoPage);

    /**
     * Unsets the "autoPage" attribute
     */
    void unsetAutoPage();
}
