/*
 * XML Type:  CT_OptimizeForBrowser
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OptimizeForBrowser(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTOptimizeForBrowser extends org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOptimizeForBrowser> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctoptimizeforbrowser4ecctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "target" attribute
     */
    java.lang.String getTarget();

    /**
     * Gets (as xml) the "target" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetTarget();

    /**
     * True if has "target" attribute
     */
    boolean isSetTarget();

    /**
     * Sets the "target" attribute
     */
    void setTarget(java.lang.String target);

    /**
     * Sets (as xml) the "target" attribute
     */
    void xsetTarget(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target);

    /**
     * Unsets the "target" attribute
     */
    void unsetTarget();
}
