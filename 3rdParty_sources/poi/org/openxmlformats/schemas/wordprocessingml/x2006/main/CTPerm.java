/*
 * XML Type:  CT_Perm
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Perm(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTPerm extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPerm> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctperm7878type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString id);

    /**
     * Gets the "displacedByCustomXml" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml.Enum getDisplacedByCustomXml();

    /**
     * Gets (as xml) the "displacedByCustomXml" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml xgetDisplacedByCustomXml();

    /**
     * True if has "displacedByCustomXml" attribute
     */
    boolean isSetDisplacedByCustomXml();

    /**
     * Sets the "displacedByCustomXml" attribute
     */
    void setDisplacedByCustomXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml.Enum displacedByCustomXml);

    /**
     * Sets (as xml) the "displacedByCustomXml" attribute
     */
    void xsetDisplacedByCustomXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDisplacedByCustomXml displacedByCustomXml);

    /**
     * Unsets the "displacedByCustomXml" attribute
     */
    void unsetDisplacedByCustomXml();
}
