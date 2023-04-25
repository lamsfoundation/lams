/*
 * XML Type:  CT_TLTimeConditionList
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLTimeConditionList(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLTimeConditionList extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeConditionList> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttltimeconditionlistebbbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cond" elements
     */
    java.util.List<org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition> getCondList();

    /**
     * Gets array of all "cond" elements
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition[] getCondArray();

    /**
     * Gets ith "cond" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition getCondArray(int i);

    /**
     * Returns number of "cond" element
     */
    int sizeOfCondArray();

    /**
     * Sets array of all "cond" element
     */
    void setCondArray(org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition[] condArray);

    /**
     * Sets ith "cond" element
     */
    void setCondArray(int i, org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition cond);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cond" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition insertNewCond(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cond" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTTLTimeCondition addNewCond();

    /**
     * Removes the ith "cond" element
     */
    void removeCond(int i);
}
