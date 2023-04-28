/*
 * XML Type:  CT_TLGraphicalObjectBuild
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLGraphicalObjectBuild(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLGraphicalObjectBuild extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLGraphicalObjectBuild> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlgraphicalobjectbuild9a11type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bldAsOne" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty getBldAsOne();

    /**
     * True if has "bldAsOne" element
     */
    boolean isSetBldAsOne();

    /**
     * Sets the "bldAsOne" element
     */
    void setBldAsOne(org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty bldAsOne);

    /**
     * Appends and returns a new empty "bldAsOne" element
     */
    org.openxmlformats.schemas.presentationml.x2006.main.CTEmpty addNewBldAsOne();

    /**
     * Unsets the "bldAsOne" element
     */
    void unsetBldAsOne();

    /**
     * Gets the "bldSub" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties getBldSub();

    /**
     * True if has "bldSub" element
     */
    boolean isSetBldSub();

    /**
     * Sets the "bldSub" element
     */
    void setBldSub(org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties bldSub);

    /**
     * Appends and returns a new empty "bldSub" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationGraphicalObjectBuildProperties addNewBldSub();

    /**
     * Unsets the "bldSub" element
     */
    void unsetBldSub();

    /**
     * Gets the "spid" attribute
     */
    long getSpid();

    /**
     * Gets (as xml) the "spid" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId xgetSpid();

    /**
     * Sets the "spid" attribute
     */
    void setSpid(long spid);

    /**
     * Sets (as xml) the "spid" attribute
     */
    void xsetSpid(org.openxmlformats.schemas.drawingml.x2006.main.STDrawingElementId spid);

    /**
     * Gets the "grpId" attribute
     */
    long getGrpId();

    /**
     * Gets (as xml) the "grpId" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetGrpId();

    /**
     * Sets the "grpId" attribute
     */
    void setGrpId(long grpId);

    /**
     * Sets (as xml) the "grpId" attribute
     */
    void xsetGrpId(org.apache.xmlbeans.XmlUnsignedInt grpId);

    /**
     * Gets the "uiExpand" attribute
     */
    boolean getUiExpand();

    /**
     * Gets (as xml) the "uiExpand" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUiExpand();

    /**
     * True if has "uiExpand" attribute
     */
    boolean isSetUiExpand();

    /**
     * Sets the "uiExpand" attribute
     */
    void setUiExpand(boolean uiExpand);

    /**
     * Sets (as xml) the "uiExpand" attribute
     */
    void xsetUiExpand(org.apache.xmlbeans.XmlBoolean uiExpand);

    /**
     * Unsets the "uiExpand" attribute
     */
    void unsetUiExpand();
}
