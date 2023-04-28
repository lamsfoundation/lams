/*
 * XML Type:  CT_TableFormula
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableFormula
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TableFormula(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableFormula.
 */
public interface CTTableFormula extends org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableFormula> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttableformulaf801type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "array" attribute
     */
    boolean getArray();

    /**
     * Gets (as xml) the "array" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetArray();

    /**
     * True if has "array" attribute
     */
    boolean isSetArray();

    /**
     * Sets the "array" attribute
     */
    void setArray(boolean array);

    /**
     * Sets (as xml) the "array" attribute
     */
    void xsetArray(org.apache.xmlbeans.XmlBoolean array);

    /**
     * Unsets the "array" attribute
     */
    void unsetArray();
}
