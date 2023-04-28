/*
 * XML Type:  CT_Query
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQuery
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Query(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTQuery extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTQuery> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctquery43e7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "tpls" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples getTpls();

    /**
     * True if has "tpls" element
     */
    boolean isSetTpls();

    /**
     * Sets the "tpls" element
     */
    void setTpls(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples tpls);

    /**
     * Appends and returns a new empty "tpls" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTuples addNewTpls();

    /**
     * Unsets the "tpls" element
     */
    void unsetTpls();

    /**
     * Gets the "mdx" attribute
     */
    java.lang.String getMdx();

    /**
     * Gets (as xml) the "mdx" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetMdx();

    /**
     * Sets the "mdx" attribute
     */
    void setMdx(java.lang.String mdx);

    /**
     * Sets (as xml) the "mdx" attribute
     */
    void xsetMdx(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring mdx);
}
