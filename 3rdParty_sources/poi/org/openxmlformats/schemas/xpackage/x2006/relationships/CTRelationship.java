/*
 * XML Type:  CT_Relationship
 * Namespace: http://schemas.openxmlformats.org/package/2006/relationships
 * Java type: org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.xpackage.x2006.relationships;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_Relationship(@http://schemas.openxmlformats.org/package/2006/relationships).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship.
 */
public interface CTRelationship extends org.apache.xmlbeans.XmlString {
    DocumentFactory<org.openxmlformats.schemas.xpackage.x2006.relationships.CTRelationship> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctrelationship8cf8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "TargetMode" attribute
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode.Enum getTargetMode();

    /**
     * Gets (as xml) the "TargetMode" attribute
     */
    org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode xgetTargetMode();

    /**
     * True if has "TargetMode" attribute
     */
    boolean isSetTargetMode();

    /**
     * Sets the "TargetMode" attribute
     */
    void setTargetMode(org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode.Enum targetMode);

    /**
     * Sets (as xml) the "TargetMode" attribute
     */
    void xsetTargetMode(org.openxmlformats.schemas.xpackage.x2006.relationships.STTargetMode targetMode);

    /**
     * Unsets the "TargetMode" attribute
     */
    void unsetTargetMode();

    /**
     * Gets the "Target" attribute
     */
    java.lang.String getTarget();

    /**
     * Gets (as xml) the "Target" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetTarget();

    /**
     * Sets the "Target" attribute
     */
    void setTarget(java.lang.String target);

    /**
     * Sets (as xml) the "Target" attribute
     */
    void xsetTarget(org.apache.xmlbeans.XmlAnyURI target);

    /**
     * Gets the "Type" attribute
     */
    java.lang.String getType();

    /**
     * Gets (as xml) the "Type" attribute
     */
    org.apache.xmlbeans.XmlAnyURI xgetType();

    /**
     * Sets the "Type" attribute
     */
    void setType(java.lang.String type);

    /**
     * Sets (as xml) the "Type" attribute
     */
    void xsetType(org.apache.xmlbeans.XmlAnyURI type);

    /**
     * Gets the "Id" attribute
     */
    java.lang.String getId();

    /**
     * Gets (as xml) the "Id" attribute
     */
    org.apache.xmlbeans.XmlID xgetId();

    /**
     * Sets the "Id" attribute
     */
    void setId(java.lang.String id);

    /**
     * Sets (as xml) the "Id" attribute
     */
    void xsetId(org.apache.xmlbeans.XmlID id);
}
