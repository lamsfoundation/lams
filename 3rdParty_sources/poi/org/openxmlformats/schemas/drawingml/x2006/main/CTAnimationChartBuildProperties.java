/*
 * XML Type:  CT_AnimationChartBuildProperties
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/main
 * Java type: org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_AnimationChartBuildProperties(@http://schemas.openxmlformats.org/drawingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTAnimationChartBuildProperties extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.main.CTAnimationChartBuildProperties> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctanimationchartbuildproperties51d8type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "bld" attribute
     */
    java.lang.String getBld();

    /**
     * Gets (as xml) the "bld" attribute
     */
    org.openxmlformats.schemas.drawingml.x2006.main.STAnimationChartBuildType xgetBld();

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
    void xsetBld(org.openxmlformats.schemas.drawingml.x2006.main.STAnimationChartBuildType bld);

    /**
     * Unsets the "bld" attribute
     */
    void unsetBld();

    /**
     * Gets the "animBg" attribute
     */
    boolean getAnimBg();

    /**
     * Gets (as xml) the "animBg" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAnimBg();

    /**
     * True if has "animBg" attribute
     */
    boolean isSetAnimBg();

    /**
     * Sets the "animBg" attribute
     */
    void setAnimBg(boolean animBg);

    /**
     * Sets (as xml) the "animBg" attribute
     */
    void xsetAnimBg(org.apache.xmlbeans.XmlBoolean animBg);

    /**
     * Unsets the "animBg" attribute
     */
    void unsetAnimBg();
}
