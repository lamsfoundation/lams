/*
 * XML Type:  CT_ParaRPr
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ParaRPr(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTParaRPr extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPr> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctpararprd6fetype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getIns();

    /**
     * True if has "ins" element
     */
    boolean isSetIns();

    /**
     * Sets the "ins" element
     */
    void setIns(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange ins);

    /**
     * Appends and returns a new empty "ins" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewIns();

    /**
     * Unsets the "ins" element
     */
    void unsetIns();

    /**
     * Gets the "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getDel();

    /**
     * True if has "del" element
     */
    boolean isSetDel();

    /**
     * Sets the "del" element
     */
    void setDel(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange del);

    /**
     * Appends and returns a new empty "del" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewDel();

    /**
     * Unsets the "del" element
     */
    void unsetDel();

    /**
     * Gets the "moveFrom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getMoveFrom();

    /**
     * True if has "moveFrom" element
     */
    boolean isSetMoveFrom();

    /**
     * Sets the "moveFrom" element
     */
    void setMoveFrom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange moveFrom);

    /**
     * Appends and returns a new empty "moveFrom" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewMoveFrom();

    /**
     * Unsets the "moveFrom" element
     */
    void unsetMoveFrom();

    /**
     * Gets the "moveTo" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange getMoveTo();

    /**
     * True if has "moveTo" element
     */
    boolean isSetMoveTo();

    /**
     * Sets the "moveTo" element
     */
    void setMoveTo(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange moveTo);

    /**
     * Appends and returns a new empty "moveTo" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChange addNewMoveTo();

    /**
     * Unsets the "moveTo" element
     */
    void unsetMoveTo();

    /**
     * Gets a List of "rStyle" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString> getRStyleList();

    /**
     * Gets array of all "rStyle" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] getRStyleArray();

    /**
     * Gets ith "rStyle" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getRStyleArray(int i);

    /**
     * Returns number of "rStyle" element
     */
    int sizeOfRStyleArray();

    /**
     * Sets array of all "rStyle" element
     */
    void setRStyleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] rStyleArray);

    /**
     * Sets ith "rStyle" element
     */
    void setRStyleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString rStyle);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rStyle" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString insertNewRStyle(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rStyle" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewRStyle();

    /**
     * Removes the ith "rStyle" element
     */
    void removeRStyle(int i);

    /**
     * Gets a List of "rFonts" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts> getRFontsList();

    /**
     * Gets array of all "rFonts" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts[] getRFontsArray();

    /**
     * Gets ith "rFonts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts getRFontsArray(int i);

    /**
     * Returns number of "rFonts" element
     */
    int sizeOfRFontsArray();

    /**
     * Sets array of all "rFonts" element
     */
    void setRFontsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts[] rFontsArray);

    /**
     * Sets ith "rFonts" element
     */
    void setRFontsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts rFonts);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rFonts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts insertNewRFonts(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rFonts" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts addNewRFonts();

    /**
     * Removes the ith "rFonts" element
     */
    void removeRFonts(int i);

    /**
     * Gets a List of "b" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getBList();

    /**
     * Gets array of all "b" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getBArray();

    /**
     * Gets ith "b" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBArray(int i);

    /**
     * Returns number of "b" element
     */
    int sizeOfBArray();

    /**
     * Sets array of all "b" element
     */
    void setBArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] bArray);

    /**
     * Sets ith "b" element
     */
    void setBArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff b);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "b" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewB(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "b" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewB();

    /**
     * Removes the ith "b" element
     */
    void removeB(int i);

    /**
     * Gets a List of "bCs" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getBCsList();

    /**
     * Gets array of all "bCs" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getBCsArray();

    /**
     * Gets ith "bCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBCsArray(int i);

    /**
     * Returns number of "bCs" element
     */
    int sizeOfBCsArray();

    /**
     * Sets array of all "bCs" element
     */
    void setBCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] bCsArray);

    /**
     * Sets ith "bCs" element
     */
    void setBCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bCs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewBCs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBCs();

    /**
     * Removes the ith "bCs" element
     */
    void removeBCs(int i);

    /**
     * Gets a List of "i" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getIList();

    /**
     * Gets array of all "i" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getIArray();

    /**
     * Gets ith "i" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getIArray(int i);

    /**
     * Returns number of "i" element
     */
    int sizeOfIArray();

    /**
     * Sets array of all "i" element
     */
    void setIArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] iValueArray);

    /**
     * Sets ith "i" element
     */
    void setIArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff iValue);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "i" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewI(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "i" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewI();

    /**
     * Removes the ith "i" element
     */
    void removeI(int i);

    /**
     * Gets a List of "iCs" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getICsList();

    /**
     * Gets array of all "iCs" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getICsArray();

    /**
     * Gets ith "iCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getICsArray(int i);

    /**
     * Returns number of "iCs" element
     */
    int sizeOfICsArray();

    /**
     * Sets array of all "iCs" element
     */
    void setICsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] iCsArray);

    /**
     * Sets ith "iCs" element
     */
    void setICsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff iCs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "iCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewICs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "iCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewICs();

    /**
     * Removes the ith "iCs" element
     */
    void removeICs(int i);

    /**
     * Gets a List of "caps" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCapsList();

    /**
     * Gets array of all "caps" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCapsArray();

    /**
     * Gets ith "caps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCapsArray(int i);

    /**
     * Returns number of "caps" element
     */
    int sizeOfCapsArray();

    /**
     * Sets array of all "caps" element
     */
    void setCapsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] capsArray);

    /**
     * Sets ith "caps" element
     */
    void setCapsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff caps);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "caps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCaps(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "caps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCaps();

    /**
     * Removes the ith "caps" element
     */
    void removeCaps(int i);

    /**
     * Gets a List of "smallCaps" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSmallCapsList();

    /**
     * Gets array of all "smallCaps" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSmallCapsArray();

    /**
     * Gets ith "smallCaps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSmallCapsArray(int i);

    /**
     * Returns number of "smallCaps" element
     */
    int sizeOfSmallCapsArray();

    /**
     * Sets array of all "smallCaps" element
     */
    void setSmallCapsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] smallCapsArray);

    /**
     * Sets ith "smallCaps" element
     */
    void setSmallCapsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff smallCaps);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smallCaps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSmallCaps(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "smallCaps" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSmallCaps();

    /**
     * Removes the ith "smallCaps" element
     */
    void removeSmallCaps(int i);

    /**
     * Gets a List of "strike" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getStrikeList();

    /**
     * Gets array of all "strike" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getStrikeArray();

    /**
     * Gets ith "strike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getStrikeArray(int i);

    /**
     * Returns number of "strike" element
     */
    int sizeOfStrikeArray();

    /**
     * Sets array of all "strike" element
     */
    void setStrikeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] strikeArray);

    /**
     * Sets ith "strike" element
     */
    void setStrikeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff strike);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "strike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewStrike(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "strike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewStrike();

    /**
     * Removes the ith "strike" element
     */
    void removeStrike(int i);

    /**
     * Gets a List of "dstrike" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getDstrikeList();

    /**
     * Gets array of all "dstrike" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getDstrikeArray();

    /**
     * Gets ith "dstrike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDstrikeArray(int i);

    /**
     * Returns number of "dstrike" element
     */
    int sizeOfDstrikeArray();

    /**
     * Sets array of all "dstrike" element
     */
    void setDstrikeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] dstrikeArray);

    /**
     * Sets ith "dstrike" element
     */
    void setDstrikeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff dstrike);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "dstrike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewDstrike(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "dstrike" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDstrike();

    /**
     * Removes the ith "dstrike" element
     */
    void removeDstrike(int i);

    /**
     * Gets a List of "outline" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getOutlineList();

    /**
     * Gets array of all "outline" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getOutlineArray();

    /**
     * Gets ith "outline" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getOutlineArray(int i);

    /**
     * Returns number of "outline" element
     */
    int sizeOfOutlineArray();

    /**
     * Sets array of all "outline" element
     */
    void setOutlineArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] outlineArray);

    /**
     * Sets ith "outline" element
     */
    void setOutlineArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff outline);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "outline" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewOutline(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "outline" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewOutline();

    /**
     * Removes the ith "outline" element
     */
    void removeOutline(int i);

    /**
     * Gets a List of "shadow" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getShadowList();

    /**
     * Gets array of all "shadow" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getShadowArray();

    /**
     * Gets ith "shadow" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShadowArray(int i);

    /**
     * Returns number of "shadow" element
     */
    int sizeOfShadowArray();

    /**
     * Sets array of all "shadow" element
     */
    void setShadowArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] shadowArray);

    /**
     * Sets ith "shadow" element
     */
    void setShadowArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff shadow);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shadow" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewShadow(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "shadow" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShadow();

    /**
     * Removes the ith "shadow" element
     */
    void removeShadow(int i);

    /**
     * Gets a List of "emboss" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getEmbossList();

    /**
     * Gets array of all "emboss" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getEmbossArray();

    /**
     * Gets ith "emboss" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEmbossArray(int i);

    /**
     * Returns number of "emboss" element
     */
    int sizeOfEmbossArray();

    /**
     * Sets array of all "emboss" element
     */
    void setEmbossArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] embossArray);

    /**
     * Sets ith "emboss" element
     */
    void setEmbossArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff emboss);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "emboss" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewEmboss(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "emboss" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEmboss();

    /**
     * Removes the ith "emboss" element
     */
    void removeEmboss(int i);

    /**
     * Gets a List of "imprint" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getImprintList();

    /**
     * Gets array of all "imprint" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getImprintArray();

    /**
     * Gets ith "imprint" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getImprintArray(int i);

    /**
     * Returns number of "imprint" element
     */
    int sizeOfImprintArray();

    /**
     * Sets array of all "imprint" element
     */
    void setImprintArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] imprintArray);

    /**
     * Sets ith "imprint" element
     */
    void setImprintArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff imprint);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "imprint" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewImprint(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "imprint" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewImprint();

    /**
     * Removes the ith "imprint" element
     */
    void removeImprint(int i);

    /**
     * Gets a List of "noProof" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getNoProofList();

    /**
     * Gets array of all "noProof" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getNoProofArray();

    /**
     * Gets ith "noProof" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoProofArray(int i);

    /**
     * Returns number of "noProof" element
     */
    int sizeOfNoProofArray();

    /**
     * Sets array of all "noProof" element
     */
    void setNoProofArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] noProofArray);

    /**
     * Sets ith "noProof" element
     */
    void setNoProofArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noProof);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "noProof" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewNoProof(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "noProof" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoProof();

    /**
     * Removes the ith "noProof" element
     */
    void removeNoProof(int i);

    /**
     * Gets a List of "snapToGrid" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSnapToGridList();

    /**
     * Gets array of all "snapToGrid" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSnapToGridArray();

    /**
     * Gets ith "snapToGrid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSnapToGridArray(int i);

    /**
     * Returns number of "snapToGrid" element
     */
    int sizeOfSnapToGridArray();

    /**
     * Sets array of all "snapToGrid" element
     */
    void setSnapToGridArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] snapToGridArray);

    /**
     * Sets ith "snapToGrid" element
     */
    void setSnapToGridArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff snapToGrid);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "snapToGrid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSnapToGrid(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "snapToGrid" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSnapToGrid();

    /**
     * Removes the ith "snapToGrid" element
     */
    void removeSnapToGrid(int i);

    /**
     * Gets a List of "vanish" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getVanishList();

    /**
     * Gets array of all "vanish" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getVanishArray();

    /**
     * Gets ith "vanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getVanishArray(int i);

    /**
     * Returns number of "vanish" element
     */
    int sizeOfVanishArray();

    /**
     * Sets array of all "vanish" element
     */
    void setVanishArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] vanishArray);

    /**
     * Sets ith "vanish" element
     */
    void setVanishArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff vanish);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "vanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewVanish(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "vanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewVanish();

    /**
     * Removes the ith "vanish" element
     */
    void removeVanish(int i);

    /**
     * Gets a List of "webHidden" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getWebHiddenList();

    /**
     * Gets array of all "webHidden" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getWebHiddenArray();

    /**
     * Gets ith "webHidden" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getWebHiddenArray(int i);

    /**
     * Returns number of "webHidden" element
     */
    int sizeOfWebHiddenArray();

    /**
     * Sets array of all "webHidden" element
     */
    void setWebHiddenArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] webHiddenArray);

    /**
     * Sets ith "webHidden" element
     */
    void setWebHiddenArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff webHidden);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "webHidden" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewWebHidden(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "webHidden" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewWebHidden();

    /**
     * Removes the ith "webHidden" element
     */
    void removeWebHidden(int i);

    /**
     * Gets a List of "color" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor> getColorList();

    /**
     * Gets array of all "color" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor[] getColorArray();

    /**
     * Gets ith "color" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor getColorArray(int i);

    /**
     * Returns number of "color" element
     */
    int sizeOfColorArray();

    /**
     * Sets array of all "color" element
     */
    void setColorArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor[] colorArray);

    /**
     * Sets ith "color" element
     */
    void setColorArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor color);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "color" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor insertNewColor(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "color" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColor addNewColor();

    /**
     * Removes the ith "color" element
     */
    void removeColor(int i);

    /**
     * Gets a List of "spacing" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure> getSpacingList();

    /**
     * Gets array of all "spacing" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure[] getSpacingArray();

    /**
     * Gets ith "spacing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure getSpacingArray(int i);

    /**
     * Returns number of "spacing" element
     */
    int sizeOfSpacingArray();

    /**
     * Sets array of all "spacing" element
     */
    void setSpacingArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure[] spacingArray);

    /**
     * Sets ith "spacing" element
     */
    void setSpacingArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure spacing);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "spacing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure insertNewSpacing(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "spacing" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedTwipsMeasure addNewSpacing();

    /**
     * Removes the ith "spacing" element
     */
    void removeSpacing(int i);

    /**
     * Gets a List of "w" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale> getWList();

    /**
     * Gets array of all "w" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale[] getWArray();

    /**
     * Gets ith "w" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale getWArray(int i);

    /**
     * Returns number of "w" element
     */
    int sizeOfWArray();

    /**
     * Sets array of all "w" element
     */
    void setWArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale[] wArray);

    /**
     * Sets ith "w" element
     */
    void setWArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale w);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "w" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale insertNewW(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "w" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextScale addNewW();

    /**
     * Removes the ith "w" element
     */
    void removeW(int i);

    /**
     * Gets a List of "kern" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getKernList();

    /**
     * Gets array of all "kern" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getKernArray();

    /**
     * Gets ith "kern" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getKernArray(int i);

    /**
     * Returns number of "kern" element
     */
    int sizeOfKernArray();

    /**
     * Sets array of all "kern" element
     */
    void setKernArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] kernArray);

    /**
     * Sets ith "kern" element
     */
    void setKernArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure kern);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "kern" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewKern(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "kern" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewKern();

    /**
     * Removes the ith "kern" element
     */
    void removeKern(int i);

    /**
     * Gets a List of "position" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure> getPositionList();

    /**
     * Gets array of all "position" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure[] getPositionArray();

    /**
     * Gets ith "position" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure getPositionArray(int i);

    /**
     * Returns number of "position" element
     */
    int sizeOfPositionArray();

    /**
     * Sets array of all "position" element
     */
    void setPositionArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure[] positionArray);

    /**
     * Sets ith "position" element
     */
    void setPositionArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure position);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "position" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure insertNewPosition(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "position" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSignedHpsMeasure addNewPosition();

    /**
     * Removes the ith "position" element
     */
    void removePosition(int i);

    /**
     * Gets a List of "sz" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getSzList();

    /**
     * Gets array of all "sz" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getSzArray();

    /**
     * Gets ith "sz" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getSzArray(int i);

    /**
     * Returns number of "sz" element
     */
    int sizeOfSzArray();

    /**
     * Sets array of all "sz" element
     */
    void setSzArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] szArray);

    /**
     * Sets ith "sz" element
     */
    void setSzArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure sz);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "sz" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewSz(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "sz" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewSz();

    /**
     * Removes the ith "sz" element
     */
    void removeSz(int i);

    /**
     * Gets a List of "szCs" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure> getSzCsList();

    /**
     * Gets array of all "szCs" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] getSzCsArray();

    /**
     * Gets ith "szCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure getSzCsArray(int i);

    /**
     * Returns number of "szCs" element
     */
    int sizeOfSzCsArray();

    /**
     * Sets array of all "szCs" element
     */
    void setSzCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure[] szCsArray);

    /**
     * Sets ith "szCs" element
     */
    void setSzCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure szCs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "szCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure insertNewSzCs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "szCs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure addNewSzCs();

    /**
     * Removes the ith "szCs" element
     */
    void removeSzCs(int i);

    /**
     * Gets a List of "highlight" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight> getHighlightList();

    /**
     * Gets array of all "highlight" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight[] getHighlightArray();

    /**
     * Gets ith "highlight" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight getHighlightArray(int i);

    /**
     * Returns number of "highlight" element
     */
    int sizeOfHighlightArray();

    /**
     * Sets array of all "highlight" element
     */
    void setHighlightArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight[] highlightArray);

    /**
     * Sets ith "highlight" element
     */
    void setHighlightArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight highlight);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "highlight" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight insertNewHighlight(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "highlight" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHighlight addNewHighlight();

    /**
     * Removes the ith "highlight" element
     */
    void removeHighlight(int i);

    /**
     * Gets a List of "u" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline> getUList();

    /**
     * Gets array of all "u" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline[] getUArray();

    /**
     * Gets ith "u" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline getUArray(int i);

    /**
     * Returns number of "u" element
     */
    int sizeOfUArray();

    /**
     * Sets array of all "u" element
     */
    void setUArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline[] uArray);

    /**
     * Sets ith "u" element
     */
    void setUArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline u);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "u" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline insertNewU(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "u" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTUnderline addNewU();

    /**
     * Removes the ith "u" element
     */
    void removeU(int i);

    /**
     * Gets a List of "effect" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect> getEffectList();

    /**
     * Gets array of all "effect" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect[] getEffectArray();

    /**
     * Gets ith "effect" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect getEffectArray(int i);

    /**
     * Returns number of "effect" element
     */
    int sizeOfEffectArray();

    /**
     * Sets array of all "effect" element
     */
    void setEffectArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect[] effectArray);

    /**
     * Sets ith "effect" element
     */
    void setEffectArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect effect);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "effect" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect insertNewEffect(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "effect" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTextEffect addNewEffect();

    /**
     * Removes the ith "effect" element
     */
    void removeEffect(int i);

    /**
     * Gets a List of "bdr" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder> getBdrList();

    /**
     * Gets array of all "bdr" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder[] getBdrArray();

    /**
     * Gets ith "bdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder getBdrArray(int i);

    /**
     * Returns number of "bdr" element
     */
    int sizeOfBdrArray();

    /**
     * Sets array of all "bdr" element
     */
    void setBdrArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder[] bdrArray);

    /**
     * Sets ith "bdr" element
     */
    void setBdrArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder bdr);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "bdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder insertNewBdr(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "bdr" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder addNewBdr();

    /**
     * Removes the ith "bdr" element
     */
    void removeBdr(int i);

    /**
     * Gets a List of "shd" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd> getShdList();

    /**
     * Gets array of all "shd" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd[] getShdArray();

    /**
     * Gets ith "shd" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd getShdArray(int i);

    /**
     * Returns number of "shd" element
     */
    int sizeOfShdArray();

    /**
     * Sets array of all "shd" element
     */
    void setShdArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd[] shdArray);

    /**
     * Sets ith "shd" element
     */
    void setShdArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd shd);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "shd" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd insertNewShd(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "shd" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd addNewShd();

    /**
     * Removes the ith "shd" element
     */
    void removeShd(int i);

    /**
     * Gets a List of "fitText" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText> getFitTextList();

    /**
     * Gets array of all "fitText" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText[] getFitTextArray();

    /**
     * Gets ith "fitText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText getFitTextArray(int i);

    /**
     * Returns number of "fitText" element
     */
    int sizeOfFitTextArray();

    /**
     * Sets array of all "fitText" element
     */
    void setFitTextArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText[] fitTextArray);

    /**
     * Sets ith "fitText" element
     */
    void setFitTextArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText fitText);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "fitText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText insertNewFitText(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "fitText" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFitText addNewFitText();

    /**
     * Removes the ith "fitText" element
     */
    void removeFitText(int i);

    /**
     * Gets a List of "vertAlign" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun> getVertAlignList();

    /**
     * Gets array of all "vertAlign" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun[] getVertAlignArray();

    /**
     * Gets ith "vertAlign" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun getVertAlignArray(int i);

    /**
     * Returns number of "vertAlign" element
     */
    int sizeOfVertAlignArray();

    /**
     * Sets array of all "vertAlign" element
     */
    void setVertAlignArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun[] vertAlignArray);

    /**
     * Sets ith "vertAlign" element
     */
    void setVertAlignArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun vertAlign);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "vertAlign" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun insertNewVertAlign(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "vertAlign" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalAlignRun addNewVertAlign();

    /**
     * Removes the ith "vertAlign" element
     */
    void removeVertAlign(int i);

    /**
     * Gets a List of "rtl" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getRtlList();

    /**
     * Gets array of all "rtl" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getRtlArray();

    /**
     * Gets ith "rtl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRtlArray(int i);

    /**
     * Returns number of "rtl" element
     */
    int sizeOfRtlArray();

    /**
     * Sets array of all "rtl" element
     */
    void setRtlArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] rtlArray);

    /**
     * Sets ith "rtl" element
     */
    void setRtlArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff rtl);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "rtl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewRtl(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "rtl" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRtl();

    /**
     * Removes the ith "rtl" element
     */
    void removeRtl(int i);

    /**
     * Gets a List of "cs" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getCsList();

    /**
     * Gets array of all "cs" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getCsArray();

    /**
     * Gets ith "cs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getCsArray(int i);

    /**
     * Returns number of "cs" element
     */
    int sizeOfCsArray();

    /**
     * Sets array of all "cs" element
     */
    void setCsArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] csArray);

    /**
     * Sets ith "cs" element
     */
    void setCsArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff cs);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "cs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewCs(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "cs" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewCs();

    /**
     * Removes the ith "cs" element
     */
    void removeCs(int i);

    /**
     * Gets a List of "em" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm> getEmList();

    /**
     * Gets array of all "em" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm[] getEmArray();

    /**
     * Gets ith "em" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm getEmArray(int i);

    /**
     * Returns number of "em" element
     */
    int sizeOfEmArray();

    /**
     * Sets array of all "em" element
     */
    void setEmArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm[] emArray);

    /**
     * Sets ith "em" element
     */
    void setEmArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm em);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "em" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm insertNewEm(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "em" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEm addNewEm();

    /**
     * Removes the ith "em" element
     */
    void removeEm(int i);

    /**
     * Gets a List of "lang" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage> getLangList();

    /**
     * Gets array of all "lang" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage[] getLangArray();

    /**
     * Gets ith "lang" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage getLangArray(int i);

    /**
     * Returns number of "lang" element
     */
    int sizeOfLangArray();

    /**
     * Sets array of all "lang" element
     */
    void setLangArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage[] langArray);

    /**
     * Sets ith "lang" element
     */
    void setLangArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage lang);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "lang" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage insertNewLang(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "lang" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage addNewLang();

    /**
     * Removes the ith "lang" element
     */
    void removeLang(int i);

    /**
     * Gets a List of "eastAsianLayout" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout> getEastAsianLayoutList();

    /**
     * Gets array of all "eastAsianLayout" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout[] getEastAsianLayoutArray();

    /**
     * Gets ith "eastAsianLayout" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout getEastAsianLayoutArray(int i);

    /**
     * Returns number of "eastAsianLayout" element
     */
    int sizeOfEastAsianLayoutArray();

    /**
     * Sets array of all "eastAsianLayout" element
     */
    void setEastAsianLayoutArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout[] eastAsianLayoutArray);

    /**
     * Sets ith "eastAsianLayout" element
     */
    void setEastAsianLayoutArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout eastAsianLayout);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "eastAsianLayout" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout insertNewEastAsianLayout(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "eastAsianLayout" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEastAsianLayout addNewEastAsianLayout();

    /**
     * Removes the ith "eastAsianLayout" element
     */
    void removeEastAsianLayout(int i);

    /**
     * Gets a List of "specVanish" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getSpecVanishList();

    /**
     * Gets array of all "specVanish" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getSpecVanishArray();

    /**
     * Gets ith "specVanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSpecVanishArray(int i);

    /**
     * Returns number of "specVanish" element
     */
    int sizeOfSpecVanishArray();

    /**
     * Sets array of all "specVanish" element
     */
    void setSpecVanishArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] specVanishArray);

    /**
     * Sets ith "specVanish" element
     */
    void setSpecVanishArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff specVanish);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "specVanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewSpecVanish(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "specVanish" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSpecVanish();

    /**
     * Removes the ith "specVanish" element
     */
    void removeSpecVanish(int i);

    /**
     * Gets a List of "oMath" elements
     */
    java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff> getOMathList();

    /**
     * Gets array of all "oMath" elements
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] getOMathArray();

    /**
     * Gets ith "oMath" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getOMathArray(int i);

    /**
     * Returns number of "oMath" element
     */
    int sizeOfOMathArray();

    /**
     * Sets array of all "oMath" element
     */
    void setOMathArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff[] oMathArray);

    /**
     * Sets ith "oMath" element
     */
    void setOMathArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff oMath);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff insertNewOMath(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "oMath" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewOMath();

    /**
     * Removes the ith "oMath" element
     */
    void removeOMath(int i);

    /**
     * Gets the "rPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange getRPrChange();

    /**
     * True if has "rPrChange" element
     */
    boolean isSetRPrChange();

    /**
     * Sets the "rPrChange" element
     */
    void setRPrChange(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange rPrChange);

    /**
     * Appends and returns a new empty "rPrChange" element
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.CTParaRPrChange addNewRPrChange();

    /**
     * Unsets the "rPrChange" element
     */
    void unsetRPrChange();
}
