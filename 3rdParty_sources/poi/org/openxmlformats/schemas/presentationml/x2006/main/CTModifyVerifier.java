/*
 * XML Type:  CT_ModifyVerifier
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_ModifyVerifier(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is a complex type.
 */
public interface CTModifyVerifier extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.presentationml.x2006.main.CTModifyVerifier> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctmodifyverifier3bb5type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "algorithmName" attribute
     */
    java.lang.String getAlgorithmName();

    /**
     * Gets (as xml) the "algorithmName" attribute
     */
    org.apache.xmlbeans.XmlString xgetAlgorithmName();

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
    void xsetAlgorithmName(org.apache.xmlbeans.XmlString algorithmName);

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
     * Gets the "spinValue" attribute
     */
    long getSpinValue();

    /**
     * Gets (as xml) the "spinValue" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetSpinValue();

    /**
     * True if has "spinValue" attribute
     */
    boolean isSetSpinValue();

    /**
     * Sets the "spinValue" attribute
     */
    void setSpinValue(long spinValue);

    /**
     * Sets (as xml) the "spinValue" attribute
     */
    void xsetSpinValue(org.apache.xmlbeans.XmlUnsignedInt spinValue);

    /**
     * Unsets the "spinValue" attribute
     */
    void unsetSpinValue();

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
    long getCryptAlgorithmSid();

    /**
     * Gets (as xml) the "cryptAlgorithmSid" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCryptAlgorithmSid();

    /**
     * True if has "cryptAlgorithmSid" attribute
     */
    boolean isSetCryptAlgorithmSid();

    /**
     * Sets the "cryptAlgorithmSid" attribute
     */
    void setCryptAlgorithmSid(long cryptAlgorithmSid);

    /**
     * Sets (as xml) the "cryptAlgorithmSid" attribute
     */
    void xsetCryptAlgorithmSid(org.apache.xmlbeans.XmlUnsignedInt cryptAlgorithmSid);

    /**
     * Unsets the "cryptAlgorithmSid" attribute
     */
    void unsetCryptAlgorithmSid();

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

    /**
     * Gets the "saltData" attribute
     */
    byte[] getSaltData();

    /**
     * Gets (as xml) the "saltData" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetSaltData();

    /**
     * True if has "saltData" attribute
     */
    boolean isSetSaltData();

    /**
     * Sets the "saltData" attribute
     */
    void setSaltData(byte[] saltData);

    /**
     * Sets (as xml) the "saltData" attribute
     */
    void xsetSaltData(org.apache.xmlbeans.XmlBase64Binary saltData);

    /**
     * Unsets the "saltData" attribute
     */
    void unsetSaltData();

    /**
     * Gets the "hashData" attribute
     */
    byte[] getHashData();

    /**
     * Gets (as xml) the "hashData" attribute
     */
    org.apache.xmlbeans.XmlBase64Binary xgetHashData();

    /**
     * True if has "hashData" attribute
     */
    boolean isSetHashData();

    /**
     * Sets the "hashData" attribute
     */
    void setHashData(byte[] hashData);

    /**
     * Sets (as xml) the "hashData" attribute
     */
    void xsetHashData(org.apache.xmlbeans.XmlBase64Binary hashData);

    /**
     * Unsets the "hashData" attribute
     */
    void unsetHashData();

    /**
     * Gets the "cryptProvider" attribute
     */
    java.lang.String getCryptProvider();

    /**
     * Gets (as xml) the "cryptProvider" attribute
     */
    org.apache.xmlbeans.XmlString xgetCryptProvider();

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
    void xsetCryptProvider(org.apache.xmlbeans.XmlString cryptProvider);

    /**
     * Unsets the "cryptProvider" attribute
     */
    void unsetCryptProvider();

    /**
     * Gets the "algIdExt" attribute
     */
    long getAlgIdExt();

    /**
     * Gets (as xml) the "algIdExt" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetAlgIdExt();

    /**
     * True if has "algIdExt" attribute
     */
    boolean isSetAlgIdExt();

    /**
     * Sets the "algIdExt" attribute
     */
    void setAlgIdExt(long algIdExt);

    /**
     * Sets (as xml) the "algIdExt" attribute
     */
    void xsetAlgIdExt(org.apache.xmlbeans.XmlUnsignedInt algIdExt);

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
    org.apache.xmlbeans.XmlString xgetAlgIdExtSource();

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
    void xsetAlgIdExtSource(org.apache.xmlbeans.XmlString algIdExtSource);

    /**
     * Unsets the "algIdExtSource" attribute
     */
    void unsetAlgIdExtSource();

    /**
     * Gets the "cryptProviderTypeExt" attribute
     */
    long getCryptProviderTypeExt();

    /**
     * Gets (as xml) the "cryptProviderTypeExt" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCryptProviderTypeExt();

    /**
     * True if has "cryptProviderTypeExt" attribute
     */
    boolean isSetCryptProviderTypeExt();

    /**
     * Sets the "cryptProviderTypeExt" attribute
     */
    void setCryptProviderTypeExt(long cryptProviderTypeExt);

    /**
     * Sets (as xml) the "cryptProviderTypeExt" attribute
     */
    void xsetCryptProviderTypeExt(org.apache.xmlbeans.XmlUnsignedInt cryptProviderTypeExt);

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
    org.apache.xmlbeans.XmlString xgetCryptProviderTypeExtSource();

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
    void xsetCryptProviderTypeExtSource(org.apache.xmlbeans.XmlString cryptProviderTypeExtSource);

    /**
     * Unsets the "cryptProviderTypeExtSource" attribute
     */
    void unsetCryptProviderTypeExtSource();
}
