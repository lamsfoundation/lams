/*
 * XML Type:  CT_WriteProtection
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_WriteProtection(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public interface CTWriteProtection extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctwriteprotection6a7atype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "recommended" attribute
     */
    java.lang.Object getRecommended();

    /**
     * Gets (as xml) the "recommended" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff xgetRecommended();

    /**
     * True if has "recommended" attribute
     */
    boolean isSetRecommended();

    /**
     * Sets the "recommended" attribute
     */
    void setRecommended(java.lang.Object recommended);

    /**
     * Sets (as xml) the "recommended" attribute
     */
    void xsetRecommended(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff recommended);

    /**
     * Unsets the "recommended" attribute
     */
    void unsetRecommended();

    /**
     * Gets the "algorithmName" attribute
     */
    java.lang.String getAlgorithmName();

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAlgorithmName();

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
    void xsetAlgorithmName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString algorithmName);

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
    java.math.BigInteger getSpinCount();

    /**
     * Gets (as xml) the "spinCount" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetSpinCount();

    /**
     * True if has "spinCount" attribute
     */
    boolean isSetSpinCount();

    /**
     * Sets the "spinCount" attribute
     */
    void setSpinCount(java.math.BigInteger spinCount);

    /**
     * Sets (as xml) the "spinCount" attribute
     */
    void xsetSpinCount(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber spinCount);

    /**
     * Unsets the "spinCount" attribute
     */
    void unsetSpinCount();

    /**
     * Gets the "cryptProviderType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.Enum getCryptProviderType();

    /**
     * Gets (as xml) the "cryptProviderType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv xgetCryptProviderType();

    /**
     * True if has "cryptProviderType" attribute
     */
    boolean isSetCryptProviderType();

    /**
     * Sets the "cryptProviderType" attribute
     */
    void setCryptProviderType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.Enum cryptProviderType);

    /**
     * Sets (as xml) the "cryptProviderType" attribute
     */
    void xsetCryptProviderType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv cryptProviderType);

    /**
     * Unsets the "cryptProviderType" attribute
     */
    void unsetCryptProviderType();

    /**
     * Gets the "cryptAlgorithmClass" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass.Enum getCryptAlgorithmClass();

    /**
     * Gets (as xml) the "cryptAlgorithmClass" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass xgetCryptAlgorithmClass();

    /**
     * True if has "cryptAlgorithmClass" attribute
     */
    boolean isSetCryptAlgorithmClass();

    /**
     * Sets the "cryptAlgorithmClass" attribute
     */
    void setCryptAlgorithmClass(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass.Enum cryptAlgorithmClass);

    /**
     * Sets (as xml) the "cryptAlgorithmClass" attribute
     */
    void xsetCryptAlgorithmClass(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgClass cryptAlgorithmClass);

    /**
     * Unsets the "cryptAlgorithmClass" attribute
     */
    void unsetCryptAlgorithmClass();

    /**
     * Gets the "cryptAlgorithmType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType.Enum getCryptAlgorithmType();

    /**
     * Gets (as xml) the "cryptAlgorithmType" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType xgetCryptAlgorithmType();

    /**
     * True if has "cryptAlgorithmType" attribute
     */
    boolean isSetCryptAlgorithmType();

    /**
     * Sets the "cryptAlgorithmType" attribute
     */
    void setCryptAlgorithmType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType.Enum cryptAlgorithmType);

    /**
     * Sets (as xml) the "cryptAlgorithmType" attribute
     */
    void xsetCryptAlgorithmType(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STAlgType cryptAlgorithmType);

    /**
     * Unsets the "cryptAlgorithmType" attribute
     */
    void unsetCryptAlgorithmType();

    /**
     * Gets the "cryptAlgorithmSid" attribute
     */
    java.math.BigInteger getCryptAlgorithmSid();

    /**
     * Gets (as xml) the "cryptAlgorithmSid" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCryptAlgorithmSid();

    /**
     * True if has "cryptAlgorithmSid" attribute
     */
    boolean isSetCryptAlgorithmSid();

    /**
     * Sets the "cryptAlgorithmSid" attribute
     */
    void setCryptAlgorithmSid(java.math.BigInteger cryptAlgorithmSid);

    /**
     * Sets (as xml) the "cryptAlgorithmSid" attribute
     */
    void xsetCryptAlgorithmSid(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber cryptAlgorithmSid);

    /**
     * Unsets the "cryptAlgorithmSid" attribute
     */
    void unsetCryptAlgorithmSid();

    /**
     * Gets the "cryptSpinCount" attribute
     */
    java.math.BigInteger getCryptSpinCount();

    /**
     * Gets (as xml) the "cryptSpinCount" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber xgetCryptSpinCount();

    /**
     * True if has "cryptSpinCount" attribute
     */
    boolean isSetCryptSpinCount();

    /**
     * Sets the "cryptSpinCount" attribute
     */
    void setCryptSpinCount(java.math.BigInteger cryptSpinCount);

    /**
     * Sets (as xml) the "cryptSpinCount" attribute
     */
    void xsetCryptSpinCount(org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumber cryptSpinCount);

    /**
     * Unsets the "cryptSpinCount" attribute
     */
    void unsetCryptSpinCount();

    /**
     * Gets the "cryptProvider" attribute
     */
    java.lang.String getCryptProvider();

    /**
     * Gets (as xml) the "cryptProvider" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCryptProvider();

    /**
     * True if has "cryptProvider" attribute
     */
    boolean isSetCryptProvider();

    /**
     * Sets the "cryptProvider" attribute
     */
    void setCryptProvider(java.lang.String cryptProvider);

    /**
     * Sets (as xml) the "cryptProvider" attribute
     */
    void xsetCryptProvider(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString cryptProvider);

    /**
     * Unsets the "cryptProvider" attribute
     */
    void unsetCryptProvider();

    /**
     * Gets the "algIdExt" attribute
     */
    byte[] getAlgIdExt();

    /**
     * Gets (as xml) the "algIdExt" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetAlgIdExt();

    /**
     * True if has "algIdExt" attribute
     */
    boolean isSetAlgIdExt();

    /**
     * Sets the "algIdExt" attribute
     */
    void setAlgIdExt(byte[] algIdExt);

    /**
     * Sets (as xml) the "algIdExt" attribute
     */
    void xsetAlgIdExt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber algIdExt);

    /**
     * Unsets the "algIdExt" attribute
     */
    void unsetAlgIdExt();

    /**
     * Gets the "algIdExtSource" attribute
     */
    java.lang.String getAlgIdExtSource();

    /**
     * Gets (as xml) the "algIdExtSource" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAlgIdExtSource();

    /**
     * True if has "algIdExtSource" attribute
     */
    boolean isSetAlgIdExtSource();

    /**
     * Sets the "algIdExtSource" attribute
     */
    void setAlgIdExtSource(java.lang.String algIdExtSource);

    /**
     * Sets (as xml) the "algIdExtSource" attribute
     */
    void xsetAlgIdExtSource(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString algIdExtSource);

    /**
     * Unsets the "algIdExtSource" attribute
     */
    void unsetAlgIdExtSource();

    /**
     * Gets the "cryptProviderTypeExt" attribute
     */
    byte[] getCryptProviderTypeExt();

    /**
     * Gets (as xml) the "cryptProviderTypeExt" attribute
     */
    org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber xgetCryptProviderTypeExt();

    /**
     * True if has "cryptProviderTypeExt" attribute
     */
    boolean isSetCryptProviderTypeExt();

    /**
     * Sets the "cryptProviderTypeExt" attribute
     */
    void setCryptProviderTypeExt(byte[] cryptProviderTypeExt);

    /**
     * Sets (as xml) the "cryptProviderTypeExt" attribute
     */
    void xsetCryptProviderTypeExt(org.openxmlformats.schemas.wordprocessingml.x2006.main.STLongHexNumber cryptProviderTypeExt);

    /**
     * Unsets the "cryptProviderTypeExt" attribute
     */
    void unsetCryptProviderTypeExt();

    /**
     * Gets the "cryptProviderTypeExtSource" attribute
     */
    java.lang.String getCryptProviderTypeExtSource();

    /**
     * Gets (as xml) the "cryptProviderTypeExtSource" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCryptProviderTypeExtSource();

    /**
     * True if has "cryptProviderTypeExtSource" attribute
     */
    boolean isSetCryptProviderTypeExtSource();

    /**
     * Sets the "cryptProviderTypeExtSource" attribute
     */
    void setCryptProviderTypeExtSource(java.lang.String cryptProviderTypeExtSource);

    /**
     * Sets (as xml) the "cryptProviderTypeExtSource" attribute
     */
    void xsetCryptProviderTypeExtSource(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString cryptProviderTypeExtSource);

    /**
     * Unsets the "cryptProviderTypeExtSource" attribute
     */
    void unsetCryptProviderTypeExtSource();

    /**
     * Gets the "hash" attribute
     */
    byte[] getHash();

    /**
     * Gets (as xml) the "hash" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetHash();

    /**
     * True if has "hash" attribute
     */
    boolean isSetHash();

    /**
     * Sets the "hash" attribute
     */
    void setHash(byte[] hash);

    /**
     * Sets (as xml) the "hash" attribute
     */
    void xsetHash(org.apache.xmlbeans.XmlBase64Binary hash);

    /**
     * Unsets the "hash" attribute
     */
    void unsetHash();

    /**
     * Gets the "salt" attribute
     */
    byte[] getSalt();

    /**
     * Gets (as xml) the "salt" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSalt();

    /**
     * True if has "salt" attribute
     */
    boolean isSetSalt();

    /**
     * Sets the "salt" attribute
     */
    void setSalt(byte[] salt);

    /**
     * Sets (as xml) the "salt" attribute
     */
    void xsetSalt(org.apache.xmlbeans.XmlBase64Binary salt);

    /**
     * Unsets the "salt" attribute
     */
    void unsetSalt();
}
