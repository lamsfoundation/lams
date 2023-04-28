/*
 * XML Type:  CT_FileSharing
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_FileSharing(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTFileSharing extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFileSharing> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctfilesharing5c9ftype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "readOnlyRecommended" attribute
     */
    boolean getReadOnlyRecommended();

    /**
     * Gets (as xml) the "readOnlyRecommended" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetReadOnlyRecommended();

    /**
     * True if has "readOnlyRecommended" attribute
     */
    boolean isSetReadOnlyRecommended();

    /**
     * Sets the "readOnlyRecommended" attribute
     */
    void setReadOnlyRecommended(boolean readOnlyRecommended);

    /**
     * Sets (as xml) the "readOnlyRecommended" attribute
     */
    void xsetReadOnlyRecommended(org.apache.xmlbeans.XmlBoolean readOnlyRecommended);

    /**
     * Unsets the "readOnlyRecommended" attribute
     */
    void unsetReadOnlyRecommended();

    /**
     * Gets the "userName" attribute
     */
    java.lang.String getUserName();

    /**
     * Gets (as xml) the "userName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUserName();

    /**
     * True if has "userName" attribute
     */
    boolean isSetUserName();

    /**
     * Sets the "userName" attribute
     */
    void setUserName(java.lang.String userName);

    /**
     * Sets (as xml) the "userName" attribute
     */
    void xsetUserName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring userName);

    /**
     * Unsets the "userName" attribute
     */
    void unsetUserName();

    /**
     * Gets the "reservationPassword" attribute
     */
    byte[] getReservationPassword();

    /**
     * Gets (as xml) the "reservationPassword" attribute
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex xgetReservationPassword();

    /**
     * True if has "reservationPassword" attribute
     */
    boolean isSetReservationPassword();

    /**
     * Sets the "reservationPassword" attribute
     */
    void setReservationPassword(byte[] reservationPassword);

    /**
     * Sets (as xml) the "reservationPassword" attribute
     */
    void xsetReservationPassword(org.openxmlformats.schemas.spreadsheetml.x2006.main.STUnsignedShortHex reservationPassword);

    /**
     * Unsets the "reservationPassword" attribute
     */
    void unsetReservationPassword();

    /**
     * Gets the "algorithmName" attribute
     */
    java.lang.String getAlgorithmName();

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAlgorithmName();

    /**
     * True if has "algorithmName" attribute
     */
    boolean isSetAlgorithmName();

    /**
     * Sets the "algorithmName" attribute
     */
    void setAlgorithmName(java.lang.String algorithmName);

    /**
     * Sets (as xml) the "algorithmName" attribute
     */
    void xsetAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring algorithmName);

    /**
     * Unsets the "algorithmName" attribute
     */
    void unsetAlgorithmName();

    /**
     * Gets the "hashValue" attribute
     */
    byte[] getHashValue();

    /**
     * Gets (as xml) the "hashValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetHashValue();

    /**
     * True if has "hashValue" attribute
     */
    boolean isSetHashValue();

    /**
     * Sets the "hashValue" attribute
     */
    void setHashValue(byte[] hashValue);

    /**
     * Sets (as xml) the "hashValue" attribute
     */
    void xsetHashValue(org.apache.xmlbeans.XmlBase64Binary hashValue);

    /**
     * Unsets the "hashValue" attribute
     */
    void unsetHashValue();

    /**
     * Gets the "saltValue" attribute
     */
    byte[] getSaltValue();

    /**
     * Gets (as xml) the "saltValue" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSaltValue();

    /**
     * True if has "saltValue" attribute
     */
    boolean isSetSaltValue();

    /**
     * Sets the "saltValue" attribute
     */
    void setSaltValue(byte[] saltValue);

    /**
     * Sets (as xml) the "saltValue" attribute
     */
    void xsetSaltValue(org.apache.xmlbeans.XmlBase64Binary saltValue);

    /**
     * Unsets the "saltValue" attribute
     */
    void unsetSaltValue();

    /**
     * Gets the "spinCount" attribute
     */
    long getSpinCount();

    /**
     * Gets (as xml) the "spinCount" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSpinCount();

    /**
     * True if has "spinCount" attribute
     */
    boolean isSetSpinCount();

    /**
     * Sets the "spinCount" attribute
     */
    void setSpinCount(long spinCount);

    /**
     * Sets (as xml) the "spinCount" attribute
     */
    void xsetSpinCount(org.apache.xmlbeans.XmlUnsignedInt spinCount);

    /**
     * Unsets the "spinCount" attribute
     */
    void unsetSpinCount();
}
