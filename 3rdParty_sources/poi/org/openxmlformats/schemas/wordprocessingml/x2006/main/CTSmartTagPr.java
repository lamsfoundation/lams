/*
 * XML Type:  CT_SmartTagPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SmartTagPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSmartTagPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsmarttagprf715type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "attr" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr> getAttrList();

    /**
     * Gets array of all "attr" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr[] getAttrArray();

    /**
     * Gets ith "attr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr getAttrArray(int i);

    /**
     * Returns number of "attr" element
     */
    int sizeOfAttrArray();

    /**
     * Sets array of all "attr" element
     */
    void setAttrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr[] attrArray);

    /**
     * Sets ith "attr" element
     */
    void setAttrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr attr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr insertNewAttr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "attr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAttr addNewAttr();

    /**
     * Removes the ith "attr" element
     */
    void removeAttr(int i);
}
