/*
 * XML Type:  CT_Characteristic
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/characteristics
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.characteristics;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Characteristic(@http://schemas.openxmlformats.org/officeDocument/2006/characteristics).
 *
 * This is a complex type.
 */
public interface CTCharacteristic extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.characteristics.CTCharacteristic> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcharacteristic2b69type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.apache.xmlbeans.XmlString xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.apache.xmlbeans.XmlString name);

    /**
     * Gets the "relation" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.STRelation.Enum getRelation();

    /**
     * Gets (as xml) the "relation" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.characteristics.STRelation xgetRelation();

    /**
     * Sets the "relation" attribute
     */
    void setRelation(org.openxmlformats.schemas.officeDocument.x2006.characteristics.STRelation.Enum relation);

    /**
     * Sets (as xml) the "relation" attribute
     */
    void xsetRelation(org.openxmlformats.schemas.officeDocument.x2006.characteristics.STRelation relation);

    /**
     * Gets the "val" attribute
     */
    java.lang.String getVal();

    /**
     * Gets (as xml) the "val" attribute
     */
    org.apache.xmlbeans.XmlString xgetVal();

    /**
     * Sets the "val" attribute
     */
    void setVal(java.lang.String val);

    /**
     * Sets (as xml) the "val" attribute
     */
    void xsetVal(org.apache.xmlbeans.XmlString val);

    /**
     * Gets the "vocabulary" attribute
     */
    java.lang.String getVocabulary();

    /**
     * Gets (as xml) the "vocabulary" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetVocabulary();

    /**
     * True if has "vocabulary" attribute
     */
    boolean isSetVocabulary();

    /**
     * Sets the "vocabulary" attribute
     */
    void setVocabulary(java.lang.String vocabulary);

    /**
     * Sets (as xml) the "vocabulary" attribute
     */
    void xsetVocabulary(org.apache.xmlbeans.XmlAnyURI vocabulary);

    /**
     * Unsets the "vocabulary" attribute
     */
    void unsetVocabulary();
}
