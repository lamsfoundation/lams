/*
 * XML Type:  CT_Rules
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/diagram
 * Java type: org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.diagram;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Rules(@http://schemas.openxmlformats.org/drawingml/2006/diagram).
 *
 * This is a complex type.
 */
public interface CTRules extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.diagram.CTRules> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrulesa884type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "rule" elements
     */
    java.util.List<org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule> getRuleList();

    /**
     * Gets array of all "rule" elements
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule[] getRuleArray();

    /**
     * Gets ith "rule" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule getRuleArray(int i);

    /**
     * Returns number of "rule" element
     */
    int sizeOfRuleArray();

    /**
     * Sets array of all "rule" element
     */
    void setRuleArray(org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule[] ruleArray);

    /**
     * Sets ith "rule" element
     */
    void setRuleArray(int i, org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule rule);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rule" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule insertNewRule(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rule" element
     */
    org.openxmlformats.schemas.drawingml.x2006.diagram.CTNumericRule addNewRule();

    /**
     * Removes the ith "rule" element
     */
    void removeRule(int i);
}
