/*
 * XML Type:  CT_TLOleBuildChart
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_TLOleBuildChart(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTTLOleBuildChart extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTTLOleBuildChart> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "cttlolebuildchartc2eftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


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

    /**
     * Gets the "bld" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLOleChartBuildType.Enum getBld();

    /**
     * Gets (as xml) the "bld" attribute
     */
    org.openxmlformats.schemas.presentationml.x2006.main.STTLOleChartBuildType xgetBld();

    /**
     * True if has "bld" attribute
     */
    boolean isSetBld();

    /**
     * Sets the "bld" attribute
     */
    void setBld(org.openxmlformats.schemas.presentationml.x2006.main.STTLOleChartBuildType.Enum bld);

    /**
     * Sets (as xml) the "bld" attribute
     */
    void xsetBld(org.openxmlformats.schemas.presentationml.x2006.main.STTLOleChartBuildType bld);

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
