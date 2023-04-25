/*
 * XML Type:  CT_SdtPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_SdtPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTSdtPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctsdtpre24dtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr getRPr();

    /**
     * True if has "rPr" element
     */
    boolean isSetRPr();

    /**
     * Sets the "rPr" element
     */
    void setRPr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr rPr);

    /**
     * Appends and returns a new empty "rPr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr addNewRPr();

    /**
     * Unsets the "rPr" element
     */
    void unsetRPr();

    /**
     * Gets the "alias" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getAlias();

    /**
     * True if has "alias" element
     */
    boolean isSetAlias();

    /**
     * Sets the "alias" element
     */
    void setAlias(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString alias);

    /**
     * Appends and returns a new empty "alias" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewAlias();

    /**
     * Unsets the "alias" element
     */
    void unsetAlias();

    /**
     * Gets the "tag" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getTag();

    /**
     * True if has "tag" element
     */
    boolean isSetTag();

    /**
     * Sets the "tag" element
     */
    void setTag(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString tag);

    /**
     * Appends and returns a new empty "tag" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewTag();

    /**
     * Unsets the "tag" element
     */
    void unsetTag();

    /**
     * Gets the "id" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getId();

    /**
     * True if has "id" element
     */
    boolean isSetId();

    /**
     * Sets the "id" element
     */
    void setId(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber id);

    /**
     * Appends and returns a new empty "id" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewId();

    /**
     * Unsets the "id" element
     */
    void unsetId();

    /**
     * Gets the "lock" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLock getLock();

    /**
     * True if has "lock" element
     */
    boolean isSetLock();

    /**
     * Sets the "lock" element
     */
    void setLock(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLock lock);

    /**
     * Appends and returns a new empty "lock" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLock addNewLock();

    /**
     * Unsets the "lock" element
     */
    void unsetLock();

    /**
     * Gets the "placeholder" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPlaceholder getPlaceholder();

    /**
     * True if has "placeholder" element
     */
    boolean isSetPlaceholder();

    /**
     * Sets the "placeholder" element
     */
    void setPlaceholder(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPlaceholder placeholder);

    /**
     * Appends and returns a new empty "placeholder" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPlaceholder addNewPlaceholder();

    /**
     * Unsets the "placeholder" element
     */
    void unsetPlaceholder();

    /**
     * Gets the "temporary" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTemporary();

    /**
     * True if has "temporary" element
     */
    boolean isSetTemporary();

    /**
     * Sets the "temporary" element
     */
    void setTemporary(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff temporary);

    /**
     * Appends and returns a new empty "temporary" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTemporary();

    /**
     * Unsets the "temporary" element
     */
    void unsetTemporary();

    /**
     * Gets the "showingPlcHdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShowingPlcHdr();

    /**
     * True if has "showingPlcHdr" element
     */
    boolean isSetShowingPlcHdr();

    /**
     * Sets the "showingPlcHdr" element
     */
    void setShowingPlcHdr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff showingPlcHdr);

    /**
     * Appends and returns a new empty "showingPlcHdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShowingPlcHdr();

    /**
     * Unsets the "showingPlcHdr" element
     */
    void unsetShowingPlcHdr();

    /**
     * Gets the "dataBinding" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDataBinding getDataBinding();

    /**
     * True if has "dataBinding" element
     */
    boolean isSetDataBinding();

    /**
     * Sets the "dataBinding" element
     */
    void setDataBinding(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDataBinding dataBinding);

    /**
     * Appends and returns a new empty "dataBinding" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDataBinding addNewDataBinding();

    /**
     * Unsets the "dataBinding" element
     */
    void unsetDataBinding();

    /**
     * Gets the "label" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getLabel();

    /**
     * True if has "label" element
     */
    boolean isSetLabel();

    /**
     * Sets the "label" element
     */
    void setLabel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber label);

    /**
     * Appends and returns a new empty "label" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewLabel();

    /**
     * Unsets the "label" element
     */
    void unsetLabel();

    /**
     * Gets the "tabIndex" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber getTabIndex();

    /**
     * True if has "tabIndex" element
     */
    boolean isSetTabIndex();

    /**
     * Sets the "tabIndex" element
     */
    void setTabIndex(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber tabIndex);

    /**
     * Appends and returns a new empty "tabIndex" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnsignedDecimalNumber addNewTabIndex();

    /**
     * Unsets the "tabIndex" element
     */
    void unsetTabIndex();

    /**
     * Gets the "equation" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getEquation();

    /**
     * True if has "equation" element
     */
    boolean isSetEquation();

    /**
     * Sets the "equation" element
     */
    void setEquation(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty equation);

    /**
     * Appends and returns a new empty "equation" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewEquation();

    /**
     * Unsets the "equation" element
     */
    void unsetEquation();

    /**
     * Gets the "comboBox" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtComboBox getComboBox();

    /**
     * True if has "comboBox" element
     */
    boolean isSetComboBox();

    /**
     * Sets the "comboBox" element
     */
    void setComboBox(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtComboBox comboBox);

    /**
     * Appends and returns a new empty "comboBox" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtComboBox addNewComboBox();

    /**
     * Unsets the "comboBox" element
     */
    void unsetComboBox();

    /**
     * Gets the "date" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDate getDate();

    /**
     * True if has "date" element
     */
    boolean isSetDate();

    /**
     * Sets the "date" element
     */
    void setDate(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDate date);

    /**
     * Appends and returns a new empty "date" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDate addNewDate();

    /**
     * Unsets the "date" element
     */
    void unsetDate();

    /**
     * Gets the "docPartObj" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart getDocPartObj();

    /**
     * True if has "docPartObj" element
     */
    boolean isSetDocPartObj();

    /**
     * Sets the "docPartObj" element
     */
    void setDocPartObj(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart docPartObj);

    /**
     * Appends and returns a new empty "docPartObj" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart addNewDocPartObj();

    /**
     * Unsets the "docPartObj" element
     */
    void unsetDocPartObj();

    /**
     * Gets the "docPartList" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart getDocPartList();

    /**
     * True if has "docPartList" element
     */
    boolean isSetDocPartList();

    /**
     * Sets the "docPartList" element
     */
    void setDocPartList(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart docPartList);

    /**
     * Appends and returns a new empty "docPartList" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDocPart addNewDocPartList();

    /**
     * Unsets the "docPartList" element
     */
    void unsetDocPartList();

    /**
     * Gets the "dropDownList" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDropDownList getDropDownList();

    /**
     * True if has "dropDownList" element
     */
    boolean isSetDropDownList();

    /**
     * Sets the "dropDownList" element
     */
    void setDropDownList(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDropDownList dropDownList);

    /**
     * Appends and returns a new empty "dropDownList" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtDropDownList addNewDropDownList();

    /**
     * Unsets the "dropDownList" element
     */
    void unsetDropDownList();

    /**
     * Gets the "picture" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getPicture();

    /**
     * True if has "picture" element
     */
    boolean isSetPicture();

    /**
     * Sets the "picture" element
     */
    void setPicture(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty picture);

    /**
     * Appends and returns a new empty "picture" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewPicture();

    /**
     * Unsets the "picture" element
     */
    void unsetPicture();

    /**
     * Gets the "richText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getRichText();

    /**
     * True if has "richText" element
     */
    boolean isSetRichText();

    /**
     * Sets the "richText" element
     */
    void setRichText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty richText);

    /**
     * Appends and returns a new empty "richText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewRichText();

    /**
     * Unsets the "richText" element
     */
    void unsetRichText();

    /**
     * Gets the "text" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText getText();

    /**
     * True if has "text" element
     */
    boolean isSetText();

    /**
     * Sets the "text" element
     */
    void setText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText text);

    /**
     * Appends and returns a new empty "text" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtText addNewText();

    /**
     * Unsets the "text" element
     */
    void unsetText();

    /**
     * Gets the "citation" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getCitation();

    /**
     * True if has "citation" element
     */
    boolean isSetCitation();

    /**
     * Sets the "citation" element
     */
    void setCitation(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty citation);

    /**
     * Appends and returns a new empty "citation" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewCitation();

    /**
     * Unsets the "citation" element
     */
    void unsetCitation();

    /**
     * Gets the "group" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getGroup();

    /**
     * True if has "group" element
     */
    boolean isSetGroup();

    /**
     * Sets the "group" element
     */
    void setGroup(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty group);

    /**
     * Appends and returns a new empty "group" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewGroup();

    /**
     * Unsets the "group" element
     */
    void unsetGroup();

    /**
     * Gets the "bibliography" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getBibliography();

    /**
     * True if has "bibliography" element
     */
    boolean isSetBibliography();

    /**
     * Sets the "bibliography" element
     */
    void setBibliography(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty bibliography);

    /**
     * Appends and returns a new empty "bibliography" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewBibliography();

    /**
     * Unsets the "bibliography" element
     */
    void unsetBibliography();
}
