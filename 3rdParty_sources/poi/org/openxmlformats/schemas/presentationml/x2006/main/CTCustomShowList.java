/*
 * XML Type:  CT_CustomShowList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CustomShowList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCustomShowList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcustomshowlist3419type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "custShow" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow> getCustShowList();

    /**
     * Gets array of all "custShow" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow[] getCustShowArray();

    /**
     * Gets ith "custShow" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow getCustShowArray(int i);

    /**
     * Returns number of "custShow" element
     */
    int sizeOfCustShowArray();

    /**
     * Sets array of all "custShow" element
     */
    void setCustShowArray(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow[] custShowArray);

    /**
     * Sets ith "custShow" element
     */
    void setCustShowArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow custShow);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "custShow" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow insertNewCustShow(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "custShow" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShow addNewCustShow();

    /**
     * Removes the ith "custShow" element
     */
    void removeCustShow(int i);
}
