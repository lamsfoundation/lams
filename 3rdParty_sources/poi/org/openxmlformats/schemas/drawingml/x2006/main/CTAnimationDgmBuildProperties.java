/*
 * XML Type:  CT_AnimationDgmBuildProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnimationDgmBuildProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAnimationDgmBuildProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationDgmBuildProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanimationdgmbuildproperties34actype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bld" attribute
     */
    java.lang.String getBld();

    /**
     * Gets (as xml) the "bld" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAnimationDgmBuildType xgetBld();

    /**
     * True if has "bld" attribute
     */
    boolean isSetBld();

    /**
     * Sets the "bld" attribute
     */
    void setBld(java.lang.String bld);

    /**
     * Sets (as xml) the "bld" attribute
     */
    void xsetBld(org.openxmlformats.schemas.drawingml.x2006.main.STAnimationDgmBuildType bld);

    /**
     * Unsets the "bld" attribute
     */
    void unsetBld();

    /**
     * Gets the "rev" attribute
     */
    boolean getRev();

    /**
     * Gets (as xml) the "rev" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetRev();

    /**
     * True if has "rev" attribute
     */
    boolean isSetRev();

    /**
     * Sets the "rev" attribute
     */
    void setRev(boolean rev);

    /**
     * Sets (as xml) the "rev" attribute
     */
    void xsetRev(org.apache.xmlbeans.XmlBoolean rev);

    /**
     * Unsets the "rev" attribute
     */
    void unsetRev();
}
