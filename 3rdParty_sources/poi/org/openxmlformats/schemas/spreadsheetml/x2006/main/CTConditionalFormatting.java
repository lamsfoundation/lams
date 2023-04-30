/*
 * XML Type:  CT_ConditionalFormatting
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ConditionalFormatting(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConditionalFormatting extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconditionalformatting0deatype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "cfRule" elements
     */
    java.util.List<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule> getCfRuleList();

    /**
     * Gets array of all "cfRule" elements
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule[] getCfRuleArray();

    /**
     * Gets ith "cfRule" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule getCfRuleArray(int i);

    /**
     * Returns number of "cfRule" element
     */
    int sizeOfCfRuleArray();

    /**
     * Sets array of all "cfRule" element
     */
    void setCfRuleArray(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule[] cfRuleArray);

    /**
     * Sets ith "cfRule" element
     */
    void setCfRuleArray(int i, org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule cfRule);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cfRule" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule insertNewCfRule(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cfRule" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule addNewCfRule();

    /**
     * Removes the ith "cfRule" element
     */
    void removeCfRule(int i);

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "pivot" attribute
     */
    boolean getPivot();

    /**
     * Gets (as xml) the "pivot" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPivot();

    /**
     * True if has "pivot" attribute
     */
    boolean isSetPivot();

    /**
     * Sets the "pivot" attribute
     */
    void setPivot(boolean pivot);

    /**
     * Sets (as xml) the "pivot" attribute
     */
    void xsetPivot(org.apache.xmlbeans.XmlBoolean pivot);

    /**
     * Unsets the "pivot" attribute
     */
    void unsetPivot();

    /**
     * Gets the "sqref" attribute
     */
    java.util.List getSqref();

    /**
     * Gets (as xml) the "sqref" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref xgetSqref();

    /**
     * True if has "sqref" attribute
     */
    boolean isSetSqref();

    /**
     * Sets the "sqref" attribute
     */
    void setSqref(java.util.List sqref);

    /**
     * Sets (as xml) the "sqref" attribute
     */
    void xsetSqref(org.openxmlformats.schemas.spreadsheetml.x2006.main.STSqref sqref);

    /**
     * Unsets the "sqref" attribute
     */
    void unsetSqref();
}
