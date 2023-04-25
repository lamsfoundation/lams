/*
 * XML Type:  CT_TLTimeAnimateValueList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeAnimateValueList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeAnimateValueList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValueList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimeanimatevaluelist7152type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "tav" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue> getTavList();

    /**
     * Gets array of all "tav" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue[] getTavArray();

    /**
     * Gets ith "tav" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue getTavArray(int i);

    /**
     * Returns number of "tav" element
     */
    int sizeOfTavArray();

    /**
     * Sets array of all "tav" element
     */
    void setTavArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue[] tavArray);

    /**
     * Sets ith "tav" element
     */
    void setTavArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue tav);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "tav" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue insertNewTav(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "tav" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeAnimateValue addNewTav();

    /**
     * Removes the ith "tav" element
     */
    void removeTav(int i);
}
