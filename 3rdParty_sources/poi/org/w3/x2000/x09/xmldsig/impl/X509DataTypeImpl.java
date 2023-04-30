/*
 * XML Type:  X509DataType
 * Namespace: http://www.w3.org/2000/09/xmldsig#
 * Java type: org.w3.x2000.x09.xmldsig.X509DataType
 *
 * Automatically generated - do not modify.
 */
package org.w3.x2000.x09.xmldsig.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML X509DataType(@http://www.w3.org/2000/09/xmldsig#).
 *
 * This is a complex type.
 */
public class X509DataTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.w3.x2000.x09.xmldsig.X509DataType {
    private static final long serialVersionUID = 1L;

    public X509DataTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509IssuerSerial"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509SKI"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate"),
        new QName("http://www.w3.org/2000/09/xmldsig#", "X509CRL"),
    };


    /**
     * Gets a List of "X509IssuerSerial" elements
     */
    @Override
    public java.util.List<org.w3.x2000.x09.xmldsig.X509IssuerSerialType> getX509IssuerSerialList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getX509IssuerSerialArray,
                this::setX509IssuerSerialArray,
                this::insertNewX509IssuerSerial,
                this::removeX509IssuerSerial,
                this::sizeOfX509IssuerSerialArray
            );
        }
    }

    /**
     * Gets array of all "X509IssuerSerial" elements
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType[] getX509IssuerSerialArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.w3.x2000.x09.xmldsig.X509IssuerSerialType[0]);
    }

    /**
     * Gets ith "X509IssuerSerial" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType getX509IssuerSerialArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509IssuerSerialType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509IssuerSerialType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509IssuerSerial" element
     */
    @Override
    public int sizeOfX509IssuerSerialArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "X509IssuerSerial" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setX509IssuerSerialArray(org.w3.x2000.x09.xmldsig.X509IssuerSerialType[] x509IssuerSerialArray) {
        check_orphaned();
        arraySetterHelper(x509IssuerSerialArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "X509IssuerSerial" element
     */
    @Override
    public void setX509IssuerSerialArray(int i, org.w3.x2000.x09.xmldsig.X509IssuerSerialType x509IssuerSerial) {
        generatedSetterHelperImpl(x509IssuerSerial, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509IssuerSerial" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType insertNewX509IssuerSerial(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509IssuerSerialType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509IssuerSerialType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509IssuerSerial" element
     */
    @Override
    public org.w3.x2000.x09.xmldsig.X509IssuerSerialType addNewX509IssuerSerial() {
        synchronized (monitor()) {
            check_orphaned();
            org.w3.x2000.x09.xmldsig.X509IssuerSerialType target = null;
            target = (org.w3.x2000.x09.xmldsig.X509IssuerSerialType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "X509IssuerSerial" element
     */
    @Override
    public void removeX509IssuerSerial(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "X509SKI" elements
     */
    @Override
    public java.util.List<byte[]> getX509SKIList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getX509SKIArray,
                this::setX509SKIArray,
                this::insertX509SKI,
                this::removeX509SKI,
                this::sizeOfX509SKIArray
            );
        }
    }

    /**
     * Gets array of all "X509SKI" elements
     */
    @Override
    public byte[][] getX509SKIArray() {
        return getObjectArray(PROPERTY_QNAME[1], org.apache.xmlbeans.SimpleValue::getByteArrayValue, byte[][]::new);
    }

    /**
     * Gets ith "X509SKI" element
     */
    @Override
    public byte[] getX509SKIArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) a List of "X509SKI" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509SKIList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetX509SKIArray,
                this::xsetX509SKIArray,
                this::insertNewX509SKI,
                this::removeX509SKI,
                this::sizeOfX509SKIArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "X509SKI" elements
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary[] xgetX509SKIArray() {
        return xgetArray(PROPERTY_QNAME[1], org.apache.xmlbeans.XmlBase64Binary[]::new);
    }

    /**
     * Gets (as xml) ith "X509SKI" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetX509SKIArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509SKI" element
     */
    @Override
    public int sizeOfX509SKIArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "X509SKI" element
     */
    @Override
    public void setX509SKIArray(byte[][] x509SKIArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509SKIArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets ith "X509SKI" element
     */
    @Override
    public void setX509SKIArray(int i, byte[] x509SKI) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setByteArrayValue(x509SKI);
        }
    }

    /**
     * Sets (as xml) array of all "X509SKI" element
     */
    @Override
    public void xsetX509SKIArray(org.apache.xmlbeans.XmlBase64Binary[]x509SKIArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509SKIArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets (as xml) ith "X509SKI" element
     */
    @Override
    public void xsetX509SKIArray(int i, org.apache.xmlbeans.XmlBase64Binary x509SKI) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(x509SKI);
        }
    }

    /**
     * Inserts the value as the ith "X509SKI" element
     */
    @Override
    public void insertX509SKI(int i, byte[] x509SKI) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            target.setByteArrayValue(x509SKI);
        }
    }

    /**
     * Appends the value as the last "X509SKI" element
     */
    @Override
    public void addX509SKI(byte[] x509SKI) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            target.setByteArrayValue(x509SKI);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509SKI" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary insertNewX509SKI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509SKI" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary addNewX509SKI() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "X509SKI" element
     */
    @Override
    public void removeX509SKI(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "X509SubjectName" elements
     */
    @Override
    public java.util.List<java.lang.String> getX509SubjectNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getX509SubjectNameArray,
                this::setX509SubjectNameArray,
                this::insertX509SubjectName,
                this::removeX509SubjectName,
                this::sizeOfX509SubjectNameArray
            );
        }
    }

    /**
     * Gets array of all "X509SubjectName" elements
     */
    @Override
    public java.lang.String[] getX509SubjectNameArray() {
        return getObjectArray(PROPERTY_QNAME[2], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "X509SubjectName" element
     */
    @Override
    public java.lang.String getX509SubjectNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "X509SubjectName" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlString> xgetX509SubjectNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetX509SubjectNameArray,
                this::xsetX509SubjectNameArray,
                this::insertNewX509SubjectName,
                this::removeX509SubjectName,
                this::sizeOfX509SubjectNameArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "X509SubjectName" elements
     */
    @Override
    public org.apache.xmlbeans.XmlString[] xgetX509SubjectNameArray() {
        return xgetArray(PROPERTY_QNAME[2], org.apache.xmlbeans.XmlString[]::new);
    }

    /**
     * Gets (as xml) ith "X509SubjectName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetX509SubjectNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509SubjectName" element
     */
    @Override
    public int sizeOfX509SubjectNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "X509SubjectName" element
     */
    @Override
    public void setX509SubjectNameArray(java.lang.String[] x509SubjectNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509SubjectNameArray, PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets ith "X509SubjectName" element
     */
    @Override
    public void setX509SubjectNameArray(int i, java.lang.String x509SubjectName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(x509SubjectName);
        }
    }

    /**
     * Sets (as xml) array of all "X509SubjectName" element
     */
    @Override
    public void xsetX509SubjectNameArray(org.apache.xmlbeans.XmlString[]x509SubjectNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509SubjectNameArray, PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets (as xml) ith "X509SubjectName" element
     */
    @Override
    public void xsetX509SubjectNameArray(int i, org.apache.xmlbeans.XmlString x509SubjectName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(x509SubjectName);
        }
    }

    /**
     * Inserts the value as the ith "X509SubjectName" element
     */
    @Override
    public void insertX509SubjectName(int i, java.lang.String x509SubjectName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            target.setStringValue(x509SubjectName);
        }
    }

    /**
     * Appends the value as the last "X509SubjectName" element
     */
    @Override
    public void addX509SubjectName(java.lang.String x509SubjectName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            target.setStringValue(x509SubjectName);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509SubjectName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString insertNewX509SubjectName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509SubjectName" element
     */
    @Override
    public org.apache.xmlbeans.XmlString addNewX509SubjectName() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "X509SubjectName" element
     */
    @Override
    public void removeX509SubjectName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "X509Certificate" elements
     */
    @Override
    public java.util.List<byte[]> getX509CertificateList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getX509CertificateArray,
                this::setX509CertificateArray,
                this::insertX509Certificate,
                this::removeX509Certificate,
                this::sizeOfX509CertificateArray
            );
        }
    }

    /**
     * Gets array of all "X509Certificate" elements
     */
    @Override
    public byte[][] getX509CertificateArray() {
        return getObjectArray(PROPERTY_QNAME[3], org.apache.xmlbeans.SimpleValue::getByteArrayValue, byte[][]::new);
    }

    /**
     * Gets ith "X509Certificate" element
     */
    @Override
    public byte[] getX509CertificateArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) a List of "X509Certificate" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509CertificateList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetX509CertificateArray,
                this::xsetX509CertificateArray,
                this::insertNewX509Certificate,
                this::removeX509Certificate,
                this::sizeOfX509CertificateArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "X509Certificate" elements
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary[] xgetX509CertificateArray() {
        return xgetArray(PROPERTY_QNAME[3], org.apache.xmlbeans.XmlBase64Binary[]::new);
    }

    /**
     * Gets (as xml) ith "X509Certificate" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetX509CertificateArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509Certificate" element
     */
    @Override
    public int sizeOfX509CertificateArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "X509Certificate" element
     */
    @Override
    public void setX509CertificateArray(byte[][] x509CertificateArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509CertificateArray, PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets ith "X509Certificate" element
     */
    @Override
    public void setX509CertificateArray(int i, byte[] x509Certificate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setByteArrayValue(x509Certificate);
        }
    }

    /**
     * Sets (as xml) array of all "X509Certificate" element
     */
    @Override
    public void xsetX509CertificateArray(org.apache.xmlbeans.XmlBase64Binary[]x509CertificateArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509CertificateArray, PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets (as xml) ith "X509Certificate" element
     */
    @Override
    public void xsetX509CertificateArray(int i, org.apache.xmlbeans.XmlBase64Binary x509Certificate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(x509Certificate);
        }
    }

    /**
     * Inserts the value as the ith "X509Certificate" element
     */
    @Override
    public void insertX509Certificate(int i, byte[] x509Certificate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            target.setByteArrayValue(x509Certificate);
        }
    }

    /**
     * Appends the value as the last "X509Certificate" element
     */
    @Override
    public void addX509Certificate(byte[] x509Certificate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            target.setByteArrayValue(x509Certificate);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509Certificate" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary insertNewX509Certificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509Certificate" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary addNewX509Certificate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "X509Certificate" element
     */
    @Override
    public void removeX509Certificate(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "X509CRL" elements
     */
    @Override
    public java.util.List<byte[]> getX509CRLList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getX509CRLArray,
                this::setX509CRLArray,
                this::insertX509CRL,
                this::removeX509CRL,
                this::sizeOfX509CRLArray
            );
        }
    }

    /**
     * Gets array of all "X509CRL" elements
     */
    @Override
    public byte[][] getX509CRLArray() {
        return getObjectArray(PROPERTY_QNAME[4], org.apache.xmlbeans.SimpleValue::getByteArrayValue, byte[][]::new);
    }

    /**
     * Gets ith "X509CRL" element
     */
    @Override
    public byte[] getX509CRLArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getByteArrayValue();
        }
    }

    /**
     * Gets (as xml) a List of "X509CRL" elements
     */
    @Override
    public java.util.List<org.apache.xmlbeans.XmlBase64Binary> xgetX509CRLList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetX509CRLArray,
                this::xsetX509CRLArray,
                this::insertNewX509CRL,
                this::removeX509CRL,
                this::sizeOfX509CRLArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "X509CRL" elements
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary[] xgetX509CRLArray() {
        return xgetArray(PROPERTY_QNAME[4], org.apache.xmlbeans.XmlBase64Binary[]::new);
    }

    /**
     * Gets (as xml) ith "X509CRL" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary xgetX509CRLArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "X509CRL" element
     */
    @Override
    public int sizeOfX509CRLArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "X509CRL" element
     */
    @Override
    public void setX509CRLArray(byte[][] x509CRLArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509CRLArray, PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets ith "X509CRL" element
     */
    @Override
    public void setX509CRLArray(int i, byte[] x509CRL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setByteArrayValue(x509CRL);
        }
    }

    /**
     * Sets (as xml) array of all "X509CRL" element
     */
    @Override
    public void xsetX509CRLArray(org.apache.xmlbeans.XmlBase64Binary[]x509CRLArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(x509CRLArray, PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets (as xml) ith "X509CRL" element
     */
    @Override
    public void xsetX509CRLArray(int i, org.apache.xmlbeans.XmlBase64Binary x509CRL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(x509CRL);
        }
    }

    /**
     * Inserts the value as the ith "X509CRL" element
     */
    @Override
    public void insertX509CRL(int i, byte[] x509CRL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            target.setByteArrayValue(x509CRL);
        }
    }

    /**
     * Appends the value as the last "X509CRL" element
     */
    @Override
    public void addX509CRL(byte[] x509CRL) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[4]);
            target.setByteArrayValue(x509CRL);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "X509CRL" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary insertNewX509CRL(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "X509CRL" element
     */
    @Override
    public org.apache.xmlbeans.XmlBase64Binary addNewX509CRL() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBase64Binary target = null;
            target = (org.apache.xmlbeans.XmlBase64Binary)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "X509CRL" element
     */
    @Override
    public void removeX509CRL(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }
}
