/*
 * XML Type:  CT_RevisionQueryTableField
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_RevisionQueryTableField(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTRevisionQueryTableField extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRevisionQueryTableField> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrevisionquerytablefield5a78type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sheetId" attribute
     */
    long getSheetId();

    /**
     * Gets (as xml) the "sheetId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSheetId();

    /**
     * Sets the "sheetId" attribute
     */
    void setSheetId(long sheetId);

    /**
     * Sets (as xml) the "sheetId" attribute
     */
    void xsetSheetId(org.apache.xmlbeans.XmlUnsignedInt sheetId);

    /**
     * Gets the "ref" attribute
     */
    java.lang.String getRef();

    /**
     * Gets (as xml) the "ref" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetRef();

    /**
     * Sets the "ref" attribute
     */
    void setRef(java.lang.String ref);

    /**
     * Sets (as xml) the "ref" attribute
     */
    void xsetRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef ref);

    /**
     * Gets the "fieldId" attribute
     */
    long getFieldId();

    /**
     * Gets (as xml) the "fieldId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetFieldId();

    /**
     * Sets the "fieldId" attribute
     */
    void setFieldId(long fieldId);

    /**
     * Sets (as xml) the "fieldId" attribute
     */
    void xsetFieldId(org.apache.xmlbeans.XmlUnsignedInt fieldId);
}
