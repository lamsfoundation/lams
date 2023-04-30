/*
 * XML Type:  CT_GroupMember
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupMember
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GroupMember(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGroupMember extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupMember> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgroupmember1598type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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
     * Gets the "group" attribute
     */
    boolean getGroup();

    /**
     * Gets (as xml) the "group" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetGroup();

    /**
     * True if has "group" attribute
     */
    boolean isSetGroup();

    /**
     * Sets the "group" attribute
     */
    void setGroup(boolean group);

    /**
     * Sets (as xml) the "group" attribute
     */
    void xsetGroup(org.apache.xmlbeans.XmlBoolean group);

    /**
     * Unsets the "group" attribute
     */
    void unsetGroup();
}
