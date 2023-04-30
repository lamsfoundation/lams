/*
 * XML Type:  CT_GroupLevel
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_GroupLevel(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTGroupLevel extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevel> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctgrouplevel558etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "groups" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroups getGroups();

    /**
     * True if has "groups" element
     */
    boolean isSetGroups();

    /**
     * Sets the "groups" element
     */
    void setGroups(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroups groups);

    /**
     * Appends and returns a new empty "groups" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroups addNewGroups();

    /**
     * Unsets the "groups" element
     */
    void unsetGroups();

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
     * Gets the "user" attribute
     */
    boolean getUser();

    /**
     * Gets (as xml) the "user" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUser();

    /**
     * True if has "user" attribute
     */
    boolean isSetUser();

    /**
     * Sets the "user" attribute
     */
    void setUser(boolean user);

    /**
     * Sets (as xml) the "user" attribute
     */
    void xsetUser(org.apache.xmlbeans.XmlBoolean user);

    /**
     * Unsets the "user" attribute
     */
    void unsetUser();

    /**
     * Gets the "customRollUp" attribute
     */
    boolean getCustomRollUp();

    /**
     * Gets (as xml) the "customRollUp" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetCustomRollUp();

    /**
     * True if has "customRollUp" attribute
     */
    boolean isSetCustomRollUp();

    /**
     * Sets the "customRollUp" attribute
     */
    void setCustomRollUp(boolean customRollUp);

    /**
     * Sets (as xml) the "customRollUp" attribute
     */
    void xsetCustomRollUp(org.apache.xmlbeans.XmlBoolean customRollUp);

    /**
     * Unsets the "customRollUp" attribute
     */
    void unsetCustomRollUp();
}
