/*
 * XML Type:  CT_SaveThroughXslt
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SaveThroughXslt(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSaveThroughXslt extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsavethroughxslt7b07type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();

    /**
     * Gets the "solutionID" attribute
     */
    java.lang.String getSolutionID();

    /**
     * Gets (as xml) the "solutionID" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetSolutionID();

    /**
     * True if has "solutionID" attribute
     */
    boolean isSetSolutionID();

    /**
     * Sets the "solutionID" attribute
     */
    void setSolutionID(java.lang.String solutionID);

    /**
     * Sets (as xml) the "solutionID" attribute
     */
    void xsetSolutionID(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString solutionID);

    /**
     * Unsets the "solutionID" attribute
     */
    void unsetSolutionID();
}
