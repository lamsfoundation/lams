/*
 * XML Type:  CT_WebPublishItem
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WebPublishItem(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWebPublishItem extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWebPublishItem> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwebpublishitemce57type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "id" attribute
     */
    long getId();

    /**
     * Gets (as xml) the "id" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetId();

    /**
     * Sets the "id" attribute
     */
    void setId(long id);

    /**
     * Sets (as xml) the "id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlUnsignedInt id);

    /**
     * Gets the "divId" attribute
     */
    java.lang.String getDivId();

    /**
     * Gets (as xml) the "divId" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDivId();

    /**
     * Sets the "divId" attribute
     */
    void setDivId(java.lang.String divId);

    /**
     * Sets (as xml) the "divId" attribute
     */
    void xsetDivId(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring divId);

    /**
     * Gets the "sourceType" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType.Enum getSourceType();

    /**
     * Gets (as xml) the "sourceType" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType xgetSourceType();

    /**
     * Sets the "sourceType" attribute
     */
    void setSourceType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType.Enum sourceType);

    /**
     * Sets (as xml) the "sourceType" attribute
     */
    void xsetSourceType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STWebSourceType sourceType);

    /**
     * Gets the "sourceRef" attribute
     */
    java.lang.String getSourceRef();

    /**
     * Gets (as xml) the "sourceRef" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef xgetSourceRef();

    /**
     * True if has "sourceRef" attribute
     */
    boolean isSetSourceRef();

    /**
     * Sets the "sourceRef" attribute
     */
    void setSourceRef(java.lang.String sourceRef);

    /**
     * Sets (as xml) the "sourceRef" attribute
     */
    void xsetSourceRef(org.openxmlformats.schemas.spreadsheetml.x2006.main.STRef sourceRef);

    /**
     * Unsets the "sourceRef" attribute
     */
    void unsetSourceRef();

    /**
     * Gets the "sourceObject" attribute
     */
    java.lang.String getSourceObject();

    /**
     * Gets (as xml) the "sourceObject" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetSourceObject();

    /**
     * True if has "sourceObject" attribute
     */
    boolean isSetSourceObject();

    /**
     * Sets the "sourceObject" attribute
     */
    void setSourceObject(java.lang.String sourceObject);

    /**
     * Sets (as xml) the "sourceObject" attribute
     */
    void xsetSourceObject(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring sourceObject);

    /**
     * Unsets the "sourceObject" attribute
     */
    void unsetSourceObject();

    /**
     * Gets the "destinationFile" attribute
     */
    java.lang.String getDestinationFile();

    /**
     * Gets (as xml) the "destinationFile" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDestinationFile();

    /**
     * Sets the "destinationFile" attribute
     */
    void setDestinationFile(java.lang.String destinationFile);

    /**
     * Sets (as xml) the "destinationFile" attribute
     */
    void xsetDestinationFile(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring destinationFile);

    /**
     * Gets the "title" attribute
     */
    java.lang.String getTitle();

    /**
     * Gets (as xml) the "title" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetTitle();

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
    void xsetTitle(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring title);

    /**
     * Unsets the "title" attribute
     */
    void unsetTitle();

    /**
     * Gets the "autoRepublish" attribute
     */
    boolean getAutoRepublish();

    /**
     * Gets (as xml) the "autoRepublish" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAutoRepublish();

    /**
     * True if has "autoRepublish" attribute
     */
    boolean isSetAutoRepublish();

    /**
     * Sets the "autoRepublish" attribute
     */
    void setAutoRepublish(boolean autoRepublish);

    /**
     * Sets (as xml) the "autoRepublish" attribute
     */
    void xsetAutoRepublish(org.apache.xmlbeans.XmlBoolean autoRepublish);

    /**
     * Unsets the "autoRepublish" attribute
     */
    void unsetAutoRepublish();
}
