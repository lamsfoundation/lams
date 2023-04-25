/*
 * XML Type:  CT_ControlPr
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControlPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ControlPr(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTControlPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTControlPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcontrolpr8f1etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "anchor" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor getAnchor();

    /**
     * Sets the "anchor" element
     */
    void setAnchor(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor anchor);

    /**
     * Appends and returns a new empty "anchor" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTObjectAnchor addNewAnchor();

    /**
     * Gets the "locked" attribute
     */
    boolean getLocked();

    /**
     * Gets (as xml) the "locked" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetLocked();

    /**
     * True if has "locked" attribute
     */
    boolean isSetLocked();

    /**
     * Sets the "locked" attribute
     */
    void setLocked(boolean locked);

    /**
     * Sets (as xml) the "locked" attribute
     */
    void xsetLocked(org.apache.xmlbeans.XmlBoolean locked);

    /**
     * Unsets the "locked" attribute
     */
    void unsetLocked();

    /**
     * Gets the "defaultSize" attribute
     */
    boolean getDefaultSize();

    /**
     * Gets (as xml) the "defaultSize" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDefaultSize();

    /**
     * True if has "defaultSize" attribute
     */
    boolean isSetDefaultSize();

    /**
     * Sets the "defaultSize" attribute
     */
    void setDefaultSize(boolean defaultSize);

    /**
     * Sets (as xml) the "defaultSize" attribute
     */
    void xsetDefaultSize(org.apache.xmlbeans.XmlBoolean defaultSize);

    /**
     * Unsets the "defaultSize" attribute
     */
    void unsetDefaultSize();

    /**
     * Gets the "print" attribute
     */
    boolean getPrint();

    /**
     * Gets (as xml) the "print" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetPrint();

    /**
     * True if has "print" attribute
     */
    boolean isSetPrint();

    /**
     * Sets the "print" attribute
     */
    void setPrint(boolean print);

    /**
     * Sets (as xml) the "print" attribute
     */
    void xsetPrint(org.apache.xmlbeans.XmlBoolean print);

    /**
     * Unsets the "print" attribute
     */
    void unsetPrint();

    /**
     * Gets the "disabled" attribute
     */
    boolean getDisabled();

    /**
     * Gets (as xml) the "disabled" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetDisabled();

    /**
     * True if has "disabled" attribute
     */
    boolean isSetDisabled();

    /**
     * Sets the "disabled" attribute
     */
    void setDisabled(boolean disabled);

    /**
     * Sets (as xml) the "disabled" attribute
     */
    void xsetDisabled(org.apache.xmlbeans.XmlBoolean disabled);

    /**
     * Unsets the "disabled" attribute
     */
    void unsetDisabled();

    /**
     * Gets the "recalcAlways" attribute
     */
    boolean getRecalcAlways();

    /**
     * Gets (as xml) the "recalcAlways" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetRecalcAlways();

    /**
     * True if has "recalcAlways" attribute
     */
    boolean isSetRecalcAlways();

    /**
     * Sets the "recalcAlways" attribute
     */
    void setRecalcAlways(boolean recalcAlways);

    /**
     * Sets (as xml) the "recalcAlways" attribute
     */
    void xsetRecalcAlways(org.apache.xmlbeans.XmlBoolean recalcAlways);

    /**
     * Unsets the "recalcAlways" attribute
     */
    void unsetRecalcAlways();

    /**
     * Gets the "uiObject" attribute
     */
    boolean getUiObject();

    /**
     * Gets (as xml) the "uiObject" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUiObject();

    /**
     * True if has "uiObject" attribute
     */
    boolean isSetUiObject();

    /**
     * Sets the "uiObject" attribute
     */
    void setUiObject(boolean uiObject);

    /**
     * Sets (as xml) the "uiObject" attribute
     */
    void xsetUiObject(org.apache.xmlbeans.XmlBoolean uiObject);

    /**
     * Unsets the "uiObject" attribute
     */
    void unsetUiObject();

    /**
     * Gets the "autoFill" attribute
     */
    boolean getAutoFill();

    /**
     * Gets (as xml) the "autoFill" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoFill();

    /**
     * True if has "autoFill" attribute
     */
    boolean isSetAutoFill();

    /**
     * Sets the "autoFill" attribute
     */
    void setAutoFill(boolean autoFill);

    /**
     * Sets (as xml) the "autoFill" attribute
     */
    void xsetAutoFill(org.apache.xmlbeans.XmlBoolean autoFill);

    /**
     * Unsets the "autoFill" attribute
     */
    void unsetAutoFill();

    /**
     * Gets the "autoLine" attribute
     */
    boolean getAutoLine();

    /**
     * Gets (as xml) the "autoLine" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoLine();

    /**
     * True if has "autoLine" attribute
     */
    boolean isSetAutoLine();

    /**
     * Sets the "autoLine" attribute
     */
    void setAutoLine(boolean autoLine);

    /**
     * Sets (as xml) the "autoLine" attribute
     */
    void xsetAutoLine(org.apache.xmlbeans.XmlBoolean autoLine);

    /**
     * Unsets the "autoLine" attribute
     */
    void unsetAutoLine();

    /**
     * Gets the "autoPict" attribute
     */
    boolean getAutoPict();

    /**
     * Gets (as xml) the "autoPict" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoPict();

    /**
     * True if has "autoPict" attribute
     */
    boolean isSetAutoPict();

    /**
     * Sets the "autoPict" attribute
     */
    void setAutoPict(boolean autoPict);

    /**
     * Sets (as xml) the "autoPict" attribute
     */
    void xsetAutoPict(org.apache.xmlbeans.XmlBoolean autoPict);

    /**
     * Unsets the "autoPict" attribute
     */
    void unsetAutoPict();

    /**
     * Gets the "macro" attribute
     */
    java.lang.String getMacro();

    /**
     * Gets (as xml) the "macro" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetMacro();

    /**
     * True if has "macro" attribute
     */
    boolean isSetMacro();

    /**
     * Sets the "macro" attribute
     */
    void setMacro(java.lang.String macro);

    /**
     * Sets (as xml) the "macro" attribute
     */
    void xsetMacro(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula macro);

    /**
     * Unsets the "macro" attribute
     */
    void unsetMacro();

    /**
     * Gets the "altText" attribute
     */
    java.lang.String getAltText();

    /**
     * Gets (as xml) the "altText" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAltText();

    /**
     * True if has "altText" attribute
     */
    boolean isSetAltText();

    /**
     * Sets the "altText" attribute
     */
    void setAltText(java.lang.String altText);

    /**
     * Sets (as xml) the "altText" attribute
     */
    void xsetAltText(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring altText);

    /**
     * Unsets the "altText" attribute
     */
    void unsetAltText();

    /**
     * Gets the "linkedCell" attribute
     */
    java.lang.String getLinkedCell();

    /**
     * Gets (as xml) the "linkedCell" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetLinkedCell();

    /**
     * True if has "linkedCell" attribute
     */
    boolean isSetLinkedCell();

    /**
     * Sets the "linkedCell" attribute
     */
    void setLinkedCell(java.lang.String linkedCell);

    /**
     * Sets (as xml) the "linkedCell" attribute
     */
    void xsetLinkedCell(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula linkedCell);

    /**
     * Unsets the "linkedCell" attribute
     */
    void unsetLinkedCell();

    /**
     * Gets the "listFillRange" attribute
     */
    java.lang.String getListFillRange();

    /**
     * Gets (as xml) the "listFillRange" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula xgetListFillRange();

    /**
     * True if has "listFillRange" attribute
     */
    boolean isSetListFillRange();

    /**
     * Sets the "listFillRange" attribute
     */
    void setListFillRange(java.lang.String listFillRange);

    /**
     * Sets (as xml) the "listFillRange" attribute
     */
    void xsetListFillRange(org.openxmlformats.schemas.spreadsheetml.x2006.main.STFormula listFillRange);

    /**
     * Unsets the "listFillRange" attribute
     */
    void unsetListFillRange();

    /**
     * Gets the "cf" attribute
     */
    java.lang.String getCf();

    /**
     * Gets (as xml) the "cf" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCf();

    /**
     * True if has "cf" attribute
     */
    boolean isSetCf();

    /**
     * Sets the "cf" attribute
     */
    void setCf(java.lang.String cf);

    /**
     * Sets (as xml) the "cf" attribute
     */
    void xsetCf(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring cf);

    /**
     * Unsets the "cf" attribute
     */
    void unsetCf();

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
}
