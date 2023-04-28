/*
 * XML Type:  CT_DocPartBehaviors
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_DocPartBehaviors(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTDocPartBehaviors extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehaviors> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctdocpartbehaviors0952type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "behavior" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior> getBehaviorList();

    /**
     * Gets array of all "behavior" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior[] getBehaviorArray();

    /**
     * Gets ith "behavior" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior getBehaviorArray(int i);

    /**
     * Returns number of "behavior" element
     */
    int sizeOfBehaviorArray();

    /**
     * Sets array of all "behavior" element
     */
    void setBehaviorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior[] behaviorArray);

    /**
     * Sets ith "behavior" element
     */
    void setBehaviorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior behavior);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "behavior" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior insertNewBehavior(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "behavior" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocPartBehavior addNewBehavior();

    /**
     * Removes the ith "behavior" element
     */
    void removeBehavior(int i);
}
