/*
 * XML Type:  CT_AuthorType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_AuthorType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTAuthorTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType {
    private static final long serialVersionUID = 1L;

    public CTAuthorTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Artist"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Author"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "BookAuthor"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Compiler"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Composer"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Conductor"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Counsel"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Director"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Editor"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Interviewee"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Interviewer"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Inventor"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Performer"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ProducerName"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Translator"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Writer"),
    };


    /**
     * Gets a List of "Artist" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getArtistList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getArtistArray,
                this::setArtistArray,
                this::insertNewArtist,
                this::removeArtist,
                this::sizeOfArtistArray
            );
        }
    }

    /**
     * Gets array of all "Artist" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getArtistArray() {
        return getXmlObjectArray(PROPERTY_QNAME[0], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Artist" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getArtistArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Artist" element
     */
    @Override
    public int sizeOfArtistArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "Artist" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setArtistArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] artistArray) {
        check_orphaned();
        arraySetterHelper(artistArray, PROPERTY_QNAME[0]);
    }

    /**
     * Sets ith "Artist" element
     */
    @Override
    public void setArtistArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType artist) {
        generatedSetterHelperImpl(artist, PROPERTY_QNAME[0], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Artist" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewArtist(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Artist" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewArtist() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "Artist" element
     */
    @Override
    public void removeArtist(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "Author" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType> getAuthorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAuthorArray,
                this::setAuthorArray,
                this::insertNewAuthor,
                this::removeAuthor,
                this::sizeOfAuthorArray
            );
        }
    }

    /**
     * Gets array of all "Author" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[] getAuthorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[1], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[0]);
    }

    /**
     * Gets ith "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType getAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Author" element
     */
    @Override
    public int sizeOfAuthorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "Author" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAuthorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[] authorArray) {
        check_orphaned();
        arraySetterHelper(authorArray, PROPERTY_QNAME[1]);
    }

    /**
     * Sets ith "Author" element
     */
    @Override
    public void setAuthorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType author) {
        generatedSetterHelperImpl(author, PROPERTY_QNAME[1], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType insertNewAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType addNewAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "Author" element
     */
    @Override
    public void removeAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "BookAuthor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getBookAuthorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getBookAuthorArray,
                this::setBookAuthorArray,
                this::insertNewBookAuthor,
                this::removeBookAuthor,
                this::sizeOfBookAuthorArray
            );
        }
    }

    /**
     * Gets array of all "BookAuthor" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getBookAuthorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "BookAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getBookAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[2], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "BookAuthor" element
     */
    @Override
    public int sizeOfBookAuthorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "BookAuthor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setBookAuthorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] bookAuthorArray) {
        check_orphaned();
        arraySetterHelper(bookAuthorArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "BookAuthor" element
     */
    @Override
    public void setBookAuthorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType bookAuthor) {
        generatedSetterHelperImpl(bookAuthor, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "BookAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewBookAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "BookAuthor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewBookAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Removes the ith "BookAuthor" element
     */
    @Override
    public void removeBookAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "Compiler" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getCompilerList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCompilerArray,
                this::setCompilerArray,
                this::insertNewCompiler,
                this::removeCompiler,
                this::sizeOfCompilerArray
            );
        }
    }

    /**
     * Gets array of all "Compiler" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getCompilerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[3], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Compiler" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getCompilerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Compiler" element
     */
    @Override
    public int sizeOfCompilerArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "Compiler" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCompilerArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] compilerArray) {
        check_orphaned();
        arraySetterHelper(compilerArray, PROPERTY_QNAME[3]);
    }

    /**
     * Sets ith "Compiler" element
     */
    @Override
    public void setCompilerArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType compiler) {
        generatedSetterHelperImpl(compiler, PROPERTY_QNAME[3], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Compiler" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewCompiler(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Compiler" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewCompiler() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "Compiler" element
     */
    @Override
    public void removeCompiler(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "Composer" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getComposerList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getComposerArray,
                this::setComposerArray,
                this::insertNewComposer,
                this::removeComposer,
                this::sizeOfComposerArray
            );
        }
    }

    /**
     * Gets array of all "Composer" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getComposerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[4], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Composer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getComposerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Composer" element
     */
    @Override
    public int sizeOfComposerArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "Composer" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setComposerArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] composerArray) {
        check_orphaned();
        arraySetterHelper(composerArray, PROPERTY_QNAME[4]);
    }

    /**
     * Sets ith "Composer" element
     */
    @Override
    public void setComposerArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType composer) {
        generatedSetterHelperImpl(composer, PROPERTY_QNAME[4], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Composer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewComposer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Composer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewComposer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "Composer" element
     */
    @Override
    public void removeComposer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "Conductor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getConductorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getConductorArray,
                this::setConductorArray,
                this::insertNewConductor,
                this::removeConductor,
                this::sizeOfConductorArray
            );
        }
    }

    /**
     * Gets array of all "Conductor" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getConductorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[5], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Conductor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getConductorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Conductor" element
     */
    @Override
    public int sizeOfConductorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "Conductor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setConductorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] conductorArray) {
        check_orphaned();
        arraySetterHelper(conductorArray, PROPERTY_QNAME[5]);
    }

    /**
     * Sets ith "Conductor" element
     */
    @Override
    public void setConductorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType conductor) {
        generatedSetterHelperImpl(conductor, PROPERTY_QNAME[5], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Conductor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewConductor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Conductor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewConductor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "Conductor" element
     */
    @Override
    public void removeConductor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "Counsel" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getCounselList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getCounselArray,
                this::setCounselArray,
                this::insertNewCounsel,
                this::removeCounsel,
                this::sizeOfCounselArray
            );
        }
    }

    /**
     * Gets array of all "Counsel" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getCounselArray() {
        return getXmlObjectArray(PROPERTY_QNAME[6], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Counsel" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getCounselArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Counsel" element
     */
    @Override
    public int sizeOfCounselArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "Counsel" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setCounselArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] counselArray) {
        check_orphaned();
        arraySetterHelper(counselArray, PROPERTY_QNAME[6]);
    }

    /**
     * Sets ith "Counsel" element
     */
    @Override
    public void setCounselArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType counsel) {
        generatedSetterHelperImpl(counsel, PROPERTY_QNAME[6], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Counsel" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewCounsel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Counsel" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewCounsel() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "Counsel" element
     */
    @Override
    public void removeCounsel(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "Director" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getDirectorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getDirectorArray,
                this::setDirectorArray,
                this::insertNewDirector,
                this::removeDirector,
                this::sizeOfDirectorArray
            );
        }
    }

    /**
     * Gets array of all "Director" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getDirectorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[7], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Director" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getDirectorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Director" element
     */
    @Override
    public int sizeOfDirectorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "Director" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setDirectorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] directorArray) {
        check_orphaned();
        arraySetterHelper(directorArray, PROPERTY_QNAME[7]);
    }

    /**
     * Sets ith "Director" element
     */
    @Override
    public void setDirectorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType director) {
        generatedSetterHelperImpl(director, PROPERTY_QNAME[7], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Director" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewDirector(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Director" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewDirector() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "Director" element
     */
    @Override
    public void removeDirector(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "Editor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getEditorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getEditorArray,
                this::setEditorArray,
                this::insertNewEditor,
                this::removeEditor,
                this::sizeOfEditorArray
            );
        }
    }

    /**
     * Gets array of all "Editor" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getEditorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[8], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Editor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getEditorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Editor" element
     */
    @Override
    public int sizeOfEditorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "Editor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setEditorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] editorArray) {
        check_orphaned();
        arraySetterHelper(editorArray, PROPERTY_QNAME[8]);
    }

    /**
     * Sets ith "Editor" element
     */
    @Override
    public void setEditorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType editor) {
        generatedSetterHelperImpl(editor, PROPERTY_QNAME[8], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Editor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewEditor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Editor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewEditor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "Editor" element
     */
    @Override
    public void removeEditor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "Interviewee" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getIntervieweeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getIntervieweeArray,
                this::setIntervieweeArray,
                this::insertNewInterviewee,
                this::removeInterviewee,
                this::sizeOfIntervieweeArray
            );
        }
    }

    /**
     * Gets array of all "Interviewee" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getIntervieweeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[9], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Interviewee" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getIntervieweeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Interviewee" element
     */
    @Override
    public int sizeOfIntervieweeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "Interviewee" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setIntervieweeArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] intervieweeArray) {
        check_orphaned();
        arraySetterHelper(intervieweeArray, PROPERTY_QNAME[9]);
    }

    /**
     * Sets ith "Interviewee" element
     */
    @Override
    public void setIntervieweeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType interviewee) {
        generatedSetterHelperImpl(interviewee, PROPERTY_QNAME[9], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Interviewee" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewInterviewee(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Interviewee" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewInterviewee() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "Interviewee" element
     */
    @Override
    public void removeInterviewee(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "Interviewer" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getInterviewerList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInterviewerArray,
                this::setInterviewerArray,
                this::insertNewInterviewer,
                this::removeInterviewer,
                this::sizeOfInterviewerArray
            );
        }
    }

    /**
     * Gets array of all "Interviewer" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getInterviewerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[10], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Interviewer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getInterviewerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Interviewer" element
     */
    @Override
    public int sizeOfInterviewerArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "Interviewer" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInterviewerArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] interviewerArray) {
        check_orphaned();
        arraySetterHelper(interviewerArray, PROPERTY_QNAME[10]);
    }

    /**
     * Sets ith "Interviewer" element
     */
    @Override
    public void setInterviewerArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType interviewer) {
        generatedSetterHelperImpl(interviewer, PROPERTY_QNAME[10], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Interviewer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewInterviewer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Interviewer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewInterviewer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "Interviewer" element
     */
    @Override
    public void removeInterviewer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "Inventor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getInventorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getInventorArray,
                this::setInventorArray,
                this::insertNewInventor,
                this::removeInventor,
                this::sizeOfInventorArray
            );
        }
    }

    /**
     * Gets array of all "Inventor" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getInventorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[11], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Inventor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getInventorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Inventor" element
     */
    @Override
    public int sizeOfInventorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "Inventor" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setInventorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] inventorArray) {
        check_orphaned();
        arraySetterHelper(inventorArray, PROPERTY_QNAME[11]);
    }

    /**
     * Sets ith "Inventor" element
     */
    @Override
    public void setInventorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType inventor) {
        generatedSetterHelperImpl(inventor, PROPERTY_QNAME[11], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Inventor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewInventor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Inventor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewInventor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "Inventor" element
     */
    @Override
    public void removeInventor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "Performer" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType> getPerformerList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getPerformerArray,
                this::setPerformerArray,
                this::insertNewPerformer,
                this::removePerformer,
                this::sizeOfPerformerArray
            );
        }
    }

    /**
     * Gets array of all "Performer" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[] getPerformerArray() {
        return getXmlObjectArray(PROPERTY_QNAME[12], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[0]);
    }

    /**
     * Gets ith "Performer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType getPerformerArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Performer" element
     */
    @Override
    public int sizeOfPerformerArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "Performer" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setPerformerArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType[] performerArray) {
        check_orphaned();
        arraySetterHelper(performerArray, PROPERTY_QNAME[12]);
    }

    /**
     * Sets ith "Performer" element
     */
    @Override
    public void setPerformerArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType performer) {
        generatedSetterHelperImpl(performer, PROPERTY_QNAME[12], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Performer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType insertNewPerformer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Performer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType addNewPerformer() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameOrCorporateType)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "Performer" element
     */
    @Override
    public void removePerformer(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "ProducerName" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getProducerNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getProducerNameArray,
                this::setProducerNameArray,
                this::insertNewProducerName,
                this::removeProducerName,
                this::sizeOfProducerNameArray
            );
        }
    }

    /**
     * Gets array of all "ProducerName" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getProducerNameArray() {
        return getXmlObjectArray(PROPERTY_QNAME[13], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "ProducerName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getProducerNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ProducerName" element
     */
    @Override
    public int sizeOfProducerNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "ProducerName" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setProducerNameArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] producerNameArray) {
        check_orphaned();
        arraySetterHelper(producerNameArray, PROPERTY_QNAME[13]);
    }

    /**
     * Sets ith "ProducerName" element
     */
    @Override
    public void setProducerNameArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType producerName) {
        generatedSetterHelperImpl(producerName, PROPERTY_QNAME[13], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ProducerName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewProducerName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ProducerName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewProducerName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "ProducerName" element
     */
    @Override
    public void removeProducerName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "Translator" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getTranslatorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getTranslatorArray,
                this::setTranslatorArray,
                this::insertNewTranslator,
                this::removeTranslator,
                this::sizeOfTranslatorArray
            );
        }
    }

    /**
     * Gets array of all "Translator" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getTranslatorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[14], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Translator" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getTranslatorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Translator" element
     */
    @Override
    public int sizeOfTranslatorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "Translator" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setTranslatorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] translatorArray) {
        check_orphaned();
        arraySetterHelper(translatorArray, PROPERTY_QNAME[14]);
    }

    /**
     * Sets ith "Translator" element
     */
    @Override
    public void setTranslatorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType translator) {
        generatedSetterHelperImpl(translator, PROPERTY_QNAME[14], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Translator" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewTranslator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Translator" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewTranslator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "Translator" element
     */
    @Override
    public void removeTranslator(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "Writer" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType> getWriterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getWriterArray,
                this::setWriterArray,
                this::insertNewWriter,
                this::removeWriter,
                this::sizeOfWriterArray
            );
        }
    }

    /**
     * Gets array of all "Writer" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] getWriterArray() {
        return getXmlObjectArray(PROPERTY_QNAME[15], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[0]);
    }

    /**
     * Gets ith "Writer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType getWriterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Writer" element
     */
    @Override
    public int sizeOfWriterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "Writer" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setWriterArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType[] writerArray) {
        check_orphaned();
        arraySetterHelper(writerArray, PROPERTY_QNAME[15]);
    }

    /**
     * Sets ith "Writer" element
     */
    @Override
    public void setWriterArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType writer) {
        generatedSetterHelperImpl(writer, PROPERTY_QNAME[15], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Writer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType insertNewWriter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Writer" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType addNewWriter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTNameType)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "Writer" element
     */
    @Override
    public void removeWriter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }
}
