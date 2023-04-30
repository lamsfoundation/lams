/*
 * XML Type:  CT_Integer255
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Integer255(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTInteger255 extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTInteger255> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctinteger255c19etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "val" attribute
     */
    int getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.STInteger255 xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(int val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.openxmlformats.schemas.officeDocument.x2006.math.STInteger255 val);
}
