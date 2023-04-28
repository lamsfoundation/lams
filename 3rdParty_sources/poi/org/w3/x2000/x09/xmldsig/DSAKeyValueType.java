/*
 * XML Type:  DSAKeyValueType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.DSAKeyValueType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML DSAKeyValueType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface DSAKeyValueType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.DSAKeyValueType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "dsakeyvaluetypee913type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "P" element
     */
    byte[] getP();

    /**
     * Gets (as xml) the "P" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetP();

    /**
     * True if has "P" element
     */
    boolean isSetP();

    /**
     * Sets the "P" element
     */
    void setP(byte[] p);

    /**
     * Sets (as xml) the "P" element
     */
    void xsetP(org.w3.x2000.x09.xmldsig.CryptoBinary p);

    /**
     * Unsets the "P" element
     */
    void unsetP();

    /**
     * Gets the "Q" element
     */
    byte[] getQ();

    /**
     * Gets (as xml) the "Q" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetQ();

    /**
     * True if has "Q" element
     */
    boolean isSetQ();

    /**
     * Sets the "Q" element
     */
    void setQ(byte[] q);

    /**
     * Sets (as xml) the "Q" element
     */
    void xsetQ(org.w3.x2000.x09.xmldsig.CryptoBinary q);

    /**
     * Unsets the "Q" element
     */
    void unsetQ();

    /**
     * Gets the "G" element
     */
    byte[] getG();

    /**
     * Gets (as xml) the "G" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetG();

    /**
     * True if has "G" element
     */
    boolean isSetG();

    /**
     * Sets the "G" element
     */
    void setG(byte[] g);

    /**
     * Sets (as xml) the "G" element
     */
    void xsetG(org.w3.x2000.x09.xmldsig.CryptoBinary g);

    /**
     * Unsets the "G" element
     */
    void unsetG();

    /**
     * Gets the "Y" element
     */
    byte[] getY();

    /**
     * Gets (as xml) the "Y" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetY();

    /**
     * Sets the "Y" element
     */
    void setY(byte[] y);

    /**
     * Sets (as xml) the "Y" element
     */
    void xsetY(org.w3.x2000.x09.xmldsig.CryptoBinary y);

    /**
     * Gets the "J" element
     */
    byte[] getJ();

    /**
     * Gets (as xml) the "J" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetJ();

    /**
     * True if has "J" element
     */
    boolean isSetJ();

    /**
     * Sets the "J" element
     */
    void setJ(byte[] j);

    /**
     * Sets (as xml) the "J" element
     */
    void xsetJ(org.w3.x2000.x09.xmldsig.CryptoBinary j);

    /**
     * Unsets the "J" element
     */
    void unsetJ();

    /**
     * Gets the "Seed" element
     */
    byte[] getSeed();

    /**
     * Gets (as xml) the "Seed" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetSeed();

    /**
     * True if has "Seed" element
     */
    boolean isSetSeed();

    /**
     * Sets the "Seed" element
     */
    void setSeed(byte[] seed);

    /**
     * Sets (as xml) the "Seed" element
     */
    void xsetSeed(org.w3.x2000.x09.xmldsig.CryptoBinary seed);

    /**
     * Unsets the "Seed" element
     */
    void unsetSeed();

    /**
     * Gets the "PgenCounter" element
     */
    byte[] getPgenCounter();

    /**
     * Gets (as xml) the "PgenCounter" element
     */
    org.w3.x2000.x09.xmldsig.CryptoBinary xgetPgenCounter();

    /**
     * True if has "PgenCounter" element
     */
    boolean isSetPgenCounter();

    /**
     * Sets the "PgenCounter" element
     */
    void setPgenCounter(byte[] pgenCounter);

    /**
     * Sets (as xml) the "PgenCounter" element
     */
    void xsetPgenCounter(org.w3.x2000.x09.xmldsig.CryptoBinary pgenCounter);

    /**
     * Unsets the "PgenCounter" element
     */
    void unsetPgenCounter();
}
