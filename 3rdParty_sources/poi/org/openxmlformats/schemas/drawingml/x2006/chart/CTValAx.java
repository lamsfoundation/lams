/*
 * XML Type:  CT_ValAx
 * Namespace: http://schemas.openxmlformats.org/drawingml/2006/chart
 * Java type: org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.drawingml.x2006.chart;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ValAx(@http://schemas.openxmlformats.org/drawingml/2006/chart).
 *
 * This is a complex type.
 */
public interface CTValAx extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctvalaxd06etype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "axId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getAxId();

    /**
     * Sets the "axId" element
     */
    void setAxId(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt axId);

    /**
     * Appends and returns a new empty "axId" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewAxId();

    /**
     * Gets the "scaling" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling getScaling();

    /**
     * Sets the "scaling" element
     */
    void setScaling(org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling scaling);

    /**
     * Appends and returns a new empty "scaling" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTScaling addNewScaling();

    /**
     * Gets the "delete" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean getDelete();

    /**
     * True if has "delete" element
     */
    boolean isSetDelete();

    /**
     * Sets the "delete" element
     */
    void setDelete(org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean delete);

    /**
     * Appends and returns a new empty "delete" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTBoolean addNewDelete();

    /**
     * Unsets the "delete" element
     */
    void unsetDelete();

    /**
     * Gets the "axPos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos getAxPos();

    /**
     * Sets the "axPos" element
     */
    void setAxPos(org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos axPos);

    /**
     * Appends and returns a new empty "axPos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxPos addNewAxPos();

    /**
     * Gets the "majorGridlines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getMajorGridlines();

    /**
     * True if has "majorGridlines" element
     */
    boolean isSetMajorGridlines();

    /**
     * Sets the "majorGridlines" element
     */
    void setMajorGridlines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines majorGridlines);

    /**
     * Appends and returns a new empty "majorGridlines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewMajorGridlines();

    /**
     * Unsets the "majorGridlines" element
     */
    void unsetMajorGridlines();

    /**
     * Gets the "minorGridlines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines getMinorGridlines();

    /**
     * True if has "minorGridlines" element
     */
    boolean isSetMinorGridlines();

    /**
     * Sets the "minorGridlines" element
     */
    void setMinorGridlines(org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines minorGridlines);

    /**
     * Appends and returns a new empty "minorGridlines" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTChartLines addNewMinorGridlines();

    /**
     * Unsets the "minorGridlines" element
     */
    void unsetMinorGridlines();

    /**
     * Gets the "title" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle getTitle();

    /**
     * True if has "title" element
     */
    boolean isSetTitle();

    /**
     * Sets the "title" element
     */
    void setTitle(org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle title);

    /**
     * Appends and returns a new empty "title" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle addNewTitle();

    /**
     * Unsets the "title" element
     */
    void unsetTitle();

    /**
     * Gets the "numFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt getNumFmt();

    /**
     * True if has "numFmt" element
     */
    boolean isSetNumFmt();

    /**
     * Sets the "numFmt" element
     */
    void setNumFmt(org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt numFmt);

    /**
     * Appends and returns a new empty "numFmt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTNumFmt addNewNumFmt();

    /**
     * Unsets the "numFmt" element
     */
    void unsetNumFmt();

    /**
     * Gets the "majorTickMark" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark getMajorTickMark();

    /**
     * True if has "majorTickMark" element
     */
    boolean isSetMajorTickMark();

    /**
     * Sets the "majorTickMark" element
     */
    void setMajorTickMark(org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark majorTickMark);

    /**
     * Appends and returns a new empty "majorTickMark" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark addNewMajorTickMark();

    /**
     * Unsets the "majorTickMark" element
     */
    void unsetMajorTickMark();

    /**
     * Gets the "minorTickMark" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark getMinorTickMark();

    /**
     * True if has "minorTickMark" element
     */
    boolean isSetMinorTickMark();

    /**
     * Sets the "minorTickMark" element
     */
    void setMinorTickMark(org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark minorTickMark);

    /**
     * Appends and returns a new empty "minorTickMark" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickMark addNewMinorTickMark();

    /**
     * Unsets the "minorTickMark" element
     */
    void unsetMinorTickMark();

    /**
     * Gets the "tickLblPos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos getTickLblPos();

    /**
     * True if has "tickLblPos" element
     */
    boolean isSetTickLblPos();

    /**
     * Sets the "tickLblPos" element
     */
    void setTickLblPos(org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos tickLblPos);

    /**
     * Appends and returns a new empty "tickLblPos" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTTickLblPos addNewTickLblPos();

    /**
     * Unsets the "tickLblPos" element
     */
    void unsetTickLblPos();

    /**
     * Gets the "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties getSpPr();

    /**
     * True if has "spPr" element
     */
    boolean isSetSpPr();

    /**
     * Sets the "spPr" element
     */
    void setSpPr(org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties spPr);

    /**
     * Appends and returns a new empty "spPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties addNewSpPr();

    /**
     * Unsets the "spPr" element
     */
    void unsetSpPr();

    /**
     * Gets the "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody getTxPr();

    /**
     * True if has "txPr" element
     */
    boolean isSetTxPr();

    /**
     * Sets the "txPr" element
     */
    void setTxPr(org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody txPr);

    /**
     * Appends and returns a new empty "txPr" element
     */
    org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody addNewTxPr();

    /**
     * Unsets the "txPr" element
     */
    void unsetTxPr();

    /**
     * Gets the "crossAx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt getCrossAx();

    /**
     * Sets the "crossAx" element
     */
    void setCrossAx(org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt crossAx);

    /**
     * Appends and returns a new empty "crossAx" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTUnsignedInt addNewCrossAx();

    /**
     * Gets the "crosses" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses getCrosses();

    /**
     * True if has "crosses" element
     */
    boolean isSetCrosses();

    /**
     * Sets the "crosses" element
     */
    void setCrosses(org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses crosses);

    /**
     * Appends and returns a new empty "crosses" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTCrosses addNewCrosses();

    /**
     * Unsets the "crosses" element
     */
    void unsetCrosses();

    /**
     * Gets the "crossesAt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble getCrossesAt();

    /**
     * True if has "crossesAt" element
     */
    boolean isSetCrossesAt();

    /**
     * Sets the "crossesAt" element
     */
    void setCrossesAt(org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble crossesAt);

    /**
     * Appends and returns a new empty "crossesAt" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDouble addNewCrossesAt();

    /**
     * Unsets the "crossesAt" element
     */
    void unsetCrossesAt();

    /**
     * Gets the "crossBetween" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween getCrossBetween();

    /**
     * True if has "crossBetween" element
     */
    boolean isSetCrossBetween();

    /**
     * Sets the "crossBetween" element
     */
    void setCrossBetween(org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween crossBetween);

    /**
     * Appends and returns a new empty "crossBetween" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTCrossBetween addNewCrossBetween();

    /**
     * Unsets the "crossBetween" element
     */
    void unsetCrossBetween();

    /**
     * Gets the "majorUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit getMajorUnit();

    /**
     * True if has "majorUnit" element
     */
    boolean isSetMajorUnit();

    /**
     * Sets the "majorUnit" element
     */
    void setMajorUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit majorUnit);

    /**
     * Appends and returns a new empty "majorUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit addNewMajorUnit();

    /**
     * Unsets the "majorUnit" element
     */
    void unsetMajorUnit();

    /**
     * Gets the "minorUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit getMinorUnit();

    /**
     * True if has "minorUnit" element
     */
    boolean isSetMinorUnit();

    /**
     * Sets the "minorUnit" element
     */
    void setMinorUnit(org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit minorUnit);

    /**
     * Appends and returns a new empty "minorUnit" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTAxisUnit addNewMinorUnit();

    /**
     * Unsets the "minorUnit" element
     */
    void unsetMinorUnit();

    /**
     * Gets the "dispUnits" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits getDispUnits();

    /**
     * True if has "dispUnits" element
     */
    boolean isSetDispUnits();

    /**
     * Sets the "dispUnits" element
     */
    void setDispUnits(org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits dispUnits);

    /**
     * Appends and returns a new empty "dispUnits" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTDispUnits addNewDispUnits();

    /**
     * Unsets the "dispUnits" element
     */
    void unsetDispUnits();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.drawingml.x2006.chart.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();
}
