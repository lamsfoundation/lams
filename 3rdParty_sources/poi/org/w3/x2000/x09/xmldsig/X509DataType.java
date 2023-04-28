/*
 * XML Type:  X509DataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509DataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML X509DataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public interface X509DataType extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.w3.x2000.x09.xmldsig.X509DataType> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "x509datatype5775type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets a List of "X509IssuerSerial" elements
     */
    java.util.List<org.w3.x2000.x09.xmldsig.X509IssuerSerialType> getX509IssuerSerialList();

    /**
     * Gets array of all "X509IssuerSerial" elements
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType[] getX509IssuerSerialArray();

    /**
     * Gets ith "X509IssuerSerial" element
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType getX509IssuerSerialArray(int i);

    /**
     * Returns number of "X509IssuerSerial" element
     */
    int sizeOfX509IssuerSerialArray();

    /**
     * Sets array of all "X509IssuerSerial" element
     */
    void setX509IssuerSerialArray(org.w3.x2000.x09.xmldsig.X509IssuerSerialType[] x509IssuerSerialArray);

    /**
     * Sets ith "X509IssuerSerial" element
     */
    void setX509IssuerSerialArray(int i, org.w3.x2000.x09.xmldsig.X509IssuerSerialType x509IssuerSerial);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509IssuerSerial" element
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType insertNewX509IssuerSerial(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "X509IssuerSerial" element
     */
    org.w3.x2000.x09.xmldsig.X509IssuerSerialType addNewX509IssuerSerial();

    /**
     * Removes the ith "X509IssuerSerial" element
     */
    void removeX509IssuerSerial(int i);

    /**
     * Gets a List of "X509SKI" elements
     */
    java.util.List<byte[]> getX509SKIList();

    /**
     * Gets array of all "X509SKI" elements
     */
    byte[][] getX509SKIArray();

    /**
     * Gets ith "X509SKI" element
     */
    byte[] getX509SKIArray(int i);

    /**
     * Gets (as xml) a List of "X509SKI" elements
     */
    java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509SKIList();

    /**
     * Gets (as xml) array of all "X509SKI" elements
     */
    org.apache.xmlbeans.XmlBase64Binary[] xgetX509SKIArray();

    /**
     * Gets (as xml) ith "X509SKI" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetX509SKIArray(int i);

    /**
     * Returns number of "X509SKI" element
     */
    int sizeOfX509SKIArray();

    /**
     * Sets array of all "X509SKI" element
     */
    void setX509SKIArray(byte[][] x509SKIArray);

    /**
     * Sets ith "X509SKI" element
     */
    void setX509SKIArray(int i, byte[] x509SKI);

    /**
     * Sets (as xml) array of all "X509SKI" element
     */
    void xsetX509SKIArray(org.apache.xmlbeans.XmlBase64Binary[] x509SKIArray);

    /**
     * Sets (as xml) ith "X509SKI" element
     */
    void xsetX509SKIArray(int i, org.apache.xmlbeans.XmlBase64Binary x509SKI);

    /**
     * Inserts the value as the ith "X509SKI" element
     */
    void insertX509SKI(int i, byte[] x509SKI);

    /**
     * Appends the value as the last "X509SKI" element
     */
    void addX509SKI(byte[] x509SKI);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509SKI" element
     */
    org.apache.xmlbeans.XmlBase64Binary insertNewX509SKI(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "X509SKI" element
     */
    org.apache.xmlbeans.XmlBase64Binary addNewX509SKI();

    /**
     * Removes the ith "X509SKI" element
     */
    void removeX509SKI(int i);

    /**
     * Gets a List of "X509SubjectName" elements
     */
    java.util.List<java.lang.String> getX509SubjectNameList();

    /**
     * Gets array of all "X509SubjectName" elements
     */
    java.lang.String[] getX509SubjectNameArray();

    /**
     * Gets ith "X509SubjectName" element
     */
    java.lang.String getX509SubjectNameArray(int i);

    /**
     * Gets (as xml) a List of "X509SubjectName" elements
     */
    java.util.List<org.apache.xmlbeans.XmlString> xgetX509SubjectNameList();

    /**
     * Gets (as xml) array of all "X509SubjectName" elements
     */
    org.apache.xmlbeans.XmlString[] xgetX509SubjectNameArray();

    /**
     * Gets (as xml) ith "X509SubjectName" element
     */
    org.apache.xmlbeans.XmlString xgetX509SubjectNameArray(int i);

    /**
     * Returns number of "X509SubjectName" element
     */
    int sizeOfX509SubjectNameArray();

    /**
     * Sets array of all "X509SubjectName" element
     */
    void setX509SubjectNameArray(java.lang.String[] x509SubjectNameArray);

    /**
     * Sets ith "X509SubjectName" element
     */
    void setX509SubjectNameArray(int i, java.lang.String x509SubjectName);

    /**
     * Sets (as xml) array of all "X509SubjectName" element
     */
    void xsetX509SubjectNameArray(org.apache.xmlbeans.XmlString[] x509SubjectNameArray);

    /**
     * Sets (as xml) ith "X509SubjectName" element
     */
    void xsetX509SubjectNameArray(int i, org.apache.xmlbeans.XmlString x509SubjectName);

    /**
     * Inserts the value as the ith "X509SubjectName" element
     */
    void insertX509SubjectName(int i, java.lang.String x509SubjectName);

    /**
     * Appends the value as the last "X509SubjectName" element
     */
    void addX509SubjectName(java.lang.String x509SubjectName);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509SubjectName" element
     */
    org.apache.xmlbeans.XmlString insertNewX509SubjectName(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "X509SubjectName" element
     */
    org.apache.xmlbeans.XmlString addNewX509SubjectName();

    /**
     * Removes the ith "X509SubjectName" element
     */
    void removeX509SubjectName(int i);

    /**
     * Gets a List of "X509Certificate" elements
     */
    java.util.List<byte[]> getX509CertificateList();

    /**
     * Gets array of all "X509Certificate" elements
     */
    byte[][] getX509CertificateArray();

    /**
     * Gets ith "X509Certificate" element
     */
    byte[] getX509CertificateArray(int i);

    /**
     * Gets (as xml) a List of "X509Certificate" elements
     */
    java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509CertificateList();

    /**
     * Gets (as xml) array of all "X509Certificate" elements
     */
    org.apache.xmlbeans.XmlBase64Binary[] xgetX509CertificateArray();

    /**
     * Gets (as xml) ith "X509Certificate" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetX509CertificateArray(int i);

    /**
     * Returns number of "X509Certificate" element
     */
    int sizeOfX509CertificateArray();

    /**
     * Sets array of all "X509Certificate" element
     */
    void setX509CertificateArray(byte[][] x509CertificateArray);

    /**
     * Sets ith "X509Certificate" element
     */
    void setX509CertificateArray(int i, byte[] x509Certificate);

    /**
     * Sets (as xml) array of all "X509Certificate" element
     */
    void xsetX509CertificateArray(org.apache.xmlbeans.XmlBase64Binary[] x509CertificateArray);

    /**
     * Sets (as xml) ith "X509Certificate" element
     */
    void xsetX509CertificateArray(int i, org.apache.xmlbeans.XmlBase64Binary x509Certificate);

    /**
     * Inserts the value as the ith "X509Certificate" element
     */
    void insertX509Certificate(int i, byte[] x509Certificate);

    /**
     * Appends the value as the last "X509Certificate" element
     */
    void addX509Certificate(byte[] x509Certificate);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509Certificate" element
     */
    org.apache.xmlbeans.XmlBase64Binary insertNewX509Certificate(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "X509Certificate" element
     */
    org.apache.xmlbeans.XmlBase64Binary addNewX509Certificate();

    /**
     * Removes the ith "X509Certificate" element
     */
    void removeX509Certificate(int i);

    /**
     * Gets a List of "X509CRL" elements
     */
    java.util.List<byte[]> getX509CRLList();

    /**
     * Gets array of all "X509CRL" elements
     */
    byte[][] getX509CRLArray();

    /**
     * Gets ith "X509CRL" element
     */
    byte[] getX509CRLArray(int i);

    /**
     * Gets (as xml) a List of "X509CRL" elements
     */
    java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509CRLList();

    /**
     * Gets (as xml) array of all "X509CRL" elements
     */
    org.apache.xmlbeans.XmlBase64Binary[] xgetX509CRLArray();

    /**
     * Gets (as xml) ith "X509CRL" element
     */
    org.apache.xmlbeans.XmlBase64Binary xgetX509CRLArray(int i);

    /**
     * Returns number of "X509CRL" element
     */
    int sizeOfX509CRLArray();

    /**
     * Sets array of all "X509CRL" element
     */
    void setX509CRLArray(byte[][] x509CRLArray);

    /**
     * Sets ith "X509CRL" element
     */
    void setX509CRLArray(int i, byte[] x509CRL);

    /**
     * Sets (as xml) array of all "X509CRL" element
     */
    void xsetX509CRLArray(org.apache.xmlbeans.XmlBase64Binary[] x509CRLArray);

    /**
     * Sets (as xml) ith "X509CRL" element
     */
    void xsetX509CRLArray(int i, org.apache.xmlbeans.XmlBase64Binary x509CRL);

    /**
     * Inserts the value as the ith "X509CRL" element
     */
    void insertX509CRL(int i, byte[] x509CRL);

    /**
     * Appends the value as the last "X509CRL" element
     */
    void addX509CRL(byte[] x509CRL);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509CRL" element
     */
    org.apache.xmlbeans.XmlBase64Binary insertNewX509CRL(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "X509CRL" element
     */
    org.apache.xmlbeans.XmlBase64Binary addNewX509CRL();

    /**
     * Removes the ith "X509CRL" element
     */
    void removeX509CRL(int i);
}
