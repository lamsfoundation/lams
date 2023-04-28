/*
 * XML Type:  CT_Constraints
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Constraints(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTConstraints extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraints> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconstraintsfc65type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "constr" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint> getConstrList();

    /**
     * Gets array of all "constr" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint[] getConstrArray();

    /**
     * Gets ith "constr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint getConstrArray(int i);

    /**
     * Returns number of "constr" element
     */
    int sizeOfConstrArray();

    /**
     * Sets array of all "constr" element
     */
    void setConstrArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint[] constrArray);

    /**
     * Sets ith "constr" element
     */
    void setConstrArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint constr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "constr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint insertNewConstr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "constr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTConstraint addNewConstr();

    /**
     * Removes the ith "constr" element
     */
    void removeConstr(int i);
}
