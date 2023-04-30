/*
 * XML Type:  CT_DdeValue
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DdeValue(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDdeValue extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDdeValue> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctddevaluedc55type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" element
     */
    java.lang.String getVal();

    /**
     * Gets (as xml) the "val" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetVal();

    /**
     * Sets the "val" element
     */
    void setVal(java.lang.String val);

    /**
     * Sets (as xml) the "val" element
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring val);

    /**
     * Gets the "t" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STDdeValueType.Enum getT();

    /**
     * Gets (as xml) the "t" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STDdeValueType xgetT();

    /**
     * True if has "t" attribute
     */
    boolean isSetT();

    /**
     * Sets the "t" attribute
     */
    void setT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDdeValueType.Enum t);

    /**
     * Sets (as xml) the "t" attribute
     */
    void xsetT(org.openxmlformats.schemas.spreadsheetml.x2006.main.STDdeValueType t);

    /**
     * Unsets the "t" attribute
     */
    void unsetT();
}
