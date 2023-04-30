/*
 * XML Type:  CT_LevelGroup
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_LevelGroup(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTLevelGroup extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTLevelGroup> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctlevelgroupbf24type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "groupMembers" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupMembers getGroupMembers();

    /**
     * Sets the "groupMembers" element
     */
    void setGroupMembers(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupMembers groupMembers);

    /**
     * Appends and returns a new empty "groupMembers" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupMembers addNewGroupMembers();

    /**
     * Gets the "name" attribute
     */
    java.lang.String getName();

    /**
     * Gets (as xml) the "name" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetName();

    /**
     * Sets the "name" attribute
     */
    void setName(java.lang.String name);

    /**
     * Sets (as xml) the "name" attribute
     */
    void xsetName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring name);

    /**
     * Gets the "uniqueName" attribute
     */
    java.lang.String getUniqueName();

    /**
     * Gets (as xml) the "uniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUniqueName();

    /**
     * Sets the "uniqueName" attribute
     */
    void setUniqueName(java.lang.String uniqueName);

    /**
     * Sets (as xml) the "uniqueName" attribute
     */
    void xsetUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring uniqueName);

    /**
     * Gets the "caption" attribute
     */
    java.lang.String getCaption();

    /**
     * Gets (as xml) the "caption" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCaption();

    /**
     * Sets the "caption" attribute
     */
    void setCaption(java.lang.String caption);

    /**
     * Sets (as xml) the "caption" attribute
     */
    void xsetCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring caption);

    /**
     * Gets the "uniqueParent" attribute
     */
    java.lang.String getUniqueParent();

    /**
     * Gets (as xml) the "uniqueParent" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUniqueParent();

    /**
     * True if has "uniqueParent" attribute
     */
    boolean isSetUniqueParent();

    /**
     * Sets the "uniqueParent" attribute
     */
    void setUniqueParent(java.lang.String uniqueParent);

    /**
     * Sets (as xml) the "uniqueParent" attribute
     */
    void xsetUniqueParent(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring uniqueParent);

    /**
     * Unsets the "uniqueParent" attribute
     */
    void unsetUniqueParent();

    /**
     * Gets the "id" attribute
     */
    int getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlInt xgetId();

    /**
     * True if has "id" attribute
     */
    boolean isSetId();

    /**
     * Sets the "id" attribute
     */
    void setId(int id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlInt id);

    /**
     * Unsets the "id" attribute
     */
    void unsetId();
}
