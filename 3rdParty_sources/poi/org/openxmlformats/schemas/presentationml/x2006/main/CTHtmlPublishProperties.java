/*
 * XML Type:  CT_HtmlPublishProperties
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_HtmlPublishProperties(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTHtmlPublishProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTHtmlPublishProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cthtmlpublishproperties1c78type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "sldAll" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getSldAll();

    /**
     * True if has "sldAll" element
     */
    boolean isSetSldAll();

    /**
     * Sets the "sldAll" element
     */
    void setSldAll(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty sldAll);

    /**
     * Appends and returns a new empty "sldAll" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewSldAll();

    /**
     * Unsets the "sldAll" element
     */
    void unsetSldAll();

    /**
     * Gets the "sldRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange getSldRg();

    /**
     * True if has "sldRg" element
     */
    boolean isSetSldRg();

    /**
     * Sets the "sldRg" element
     */
    void setSldRg(org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange sldRg);

    /**
     * Appends and returns a new empty "sldRg" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTIndexRange addNewSldRg();

    /**
     * Unsets the "sldRg" element
     */
    void unsetSldRg();

    /**
     * Gets the "custShow" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowId getCustShow();

    /**
     * True if has "custShow" element
     */
    boolean isSetCustShow();

    /**
     * Sets the "custShow" element
     */
    void setCustShow(org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowId custShow);

    /**
     * Appends and returns a new empty "custShow" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTCustomShowId addNewCustShow();

    /**
     * Unsets the "custShow" element
     */
    void unsetCustShow();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "showSpeakerNotes" attribute
     */
    boolean getShowSpeakerNotes();

    /**
     * Gets (as xml) the "showSpeakerNotes" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetShowSpeakerNotes();

    /**
     * True if has "showSpeakerNotes" attribute
     */
    boolean isSetShowSpeakerNotes();

    /**
     * Sets the "showSpeakerNotes" attribute
     */
    void setShowSpeakerNotes(boolean showSpeakerNotes);

    /**
     * Sets (as xml) the "showSpeakerNotes" attribute
     */
    void xsetShowSpeakerNotes(org.apache.xmlbeans.XmlBoolean showSpeakerNotes);

    /**
     * Unsets the "showSpeakerNotes" attribute
     */
    void unsetShowSpeakerNotes();

    /**
     * Gets the "target" attribute
     */
    java.lang.String getTarget();

    /**
     * Gets (as xml) the "target" attribute
     */
    org.apache.xmlbeans.XmlString xgetTarget();

    /**
     * True if has "target" attribute
     */
    boolean isSetTarget();

    /**
     * Sets the "target" attribute
     */
    void setTarget(java.lang.String target);

    /**
     * Sets (as xml) the "target" attribute
     */
    void xsetTarget(org.apache.xmlbeans.XmlString target);

    /**
     * Unsets the "target" attribute
     */
    void unsetTarget();

    /**
     * Gets the "title" attribute
     */
    java.lang.String getTitle();

    /**
     * Gets (as xml) the "title" attribute
     */
    org.apache.xmlbeans.XmlString xgetTitle();

    /**
     * True if has "title" attribute
     */
    boolean isSetTitle();

    /**
     * Sets the "title" attribute
     */
    void setTitle(java.lang.String title);

    /**
     * Sets (as xml) the "title" attribute
     */
    void xsetTitle(org.apache.xmlbeans.XmlString title);

    /**
     * Unsets the "title" attribute
     */
    void unsetTitle();

    /**
     * Gets the "id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.openxmlformats.schemas.officeDocument.x2006.relationships.STRelationshipId id);
}
