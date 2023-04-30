/*
 * XML Type:  CT_MergeCell
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_MergeCell(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTMergeCell extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTMergeCell> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmergecelle8d9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
}
