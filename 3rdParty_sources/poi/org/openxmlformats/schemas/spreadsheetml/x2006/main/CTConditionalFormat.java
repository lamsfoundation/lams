/*
 * XML Type:  CT_ConditionalFormat
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ConditionalFormat(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTConditionalFormat extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormat> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctconditionalformat6c7ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "pivotAreas" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreas getPivotAreas();

    /**
     * Sets the "pivotAreas" element
     */
    void setPivotAreas(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreas pivotAreas);

    /**
     * Appends and returns a new empty "pivotAreas" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotAreas addNewPivotAreas();

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
     * Gets the "scope" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STScope.Enum getScope();

    /**
     * Gets (as xml) the "scope" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STScope xgetScope();

    /**
     * True if has "scope" attribute
     */
    boolean isSetScope();

    /**
     * Sets the "scope" attribute
     */
    void setScope(org.openxmlformats.schemas.spreadsheetml.x2006.main.STScope.Enum scope);

    /**
     * Sets (as xml) the "scope" attribute
     */
    void xsetScope(org.openxmlformats.schemas.spreadsheetml.x2006.main.STScope scope);

    /**
     * Unsets the "scope" attribute
     */
    void unsetScope();

    /**
     * Gets the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STType.Enum getType();

    /**
     * Gets (as xml) the "type" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STType xgetType();

    /**
     * True if has "type" attribute
     */
    boolean isSetType();

    /**
     * Sets the "type" attribute
     */
    void setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STType.Enum type);

    /**
     * Sets (as xml) the "type" attribute
     */
    void xsetType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STType type);

    /**
     * Unsets the "type" attribute
     */
    void unsetType();

    /**
     * Gets the "priority" attribute
     */
    long getPriority();

    /**
     * Gets (as xml) the "priority" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetPriority();

    /**
     * Sets the "priority" attribute
     */
    void setPriority(long priority);

    /**
     * Sets (as xml) the "priority" attribute
     */
    void xsetPriority(org.apache.xmlbeans.XmlUnsignedInt priority);
}
