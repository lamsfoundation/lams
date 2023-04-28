/*
 * XML Type:  CT_SourceType
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/bibliography
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.bibliography.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_SourceType(@http://schemas.openxmlformats.org/officeDocument/2006/bibliography).
 *
 * This is a complex type.
 */
public class CTSourceTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTSourceType {
    private static final long serialVersionUID = 1L;

    public CTSourceTypeImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "AbbreviatedCaseNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "AlbumTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Author"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "BookTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Broadcaster"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "BroadcastTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "CaseNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ChapterNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "City"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Comments"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ConferenceName"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "CountryRegion"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Court"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Day"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "DayAccessed"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Department"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Distributor"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Edition"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Guid"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Institution"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "InternetSiteTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Issue"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "JournalName"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "LCID"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Medium"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Month"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "MonthAccessed"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "NumberVolumes"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Pages"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "PatentNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "PeriodicalTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ProductionCompany"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "PublicationTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Publisher"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "RecordingNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "RefOrder"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Reporter"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "SourceType"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ShortTitle"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "StandardNumber"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "StateProvince"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Station"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Tag"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Theater"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "ThesisType"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Title"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Type"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "URL"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Version"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Volume"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "Year"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/bibliography", "YearAccessed"),
    };


    /**
     * Gets a List of "AbbreviatedCaseNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getAbbreviatedCaseNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getAbbreviatedCaseNumberArray,
                this::setAbbreviatedCaseNumberArray,
                this::insertAbbreviatedCaseNumber,
                this::removeAbbreviatedCaseNumber,
                this::sizeOfAbbreviatedCaseNumberArray
            );
        }
    }

    /**
     * Gets array of all "AbbreviatedCaseNumber" elements
     */
    @Override
    public java.lang.String[] getAbbreviatedCaseNumberArray() {
        return getObjectArray(PROPERTY_QNAME[0], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "AbbreviatedCaseNumber" element
     */
    @Override
    public java.lang.String getAbbreviatedCaseNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "AbbreviatedCaseNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetAbbreviatedCaseNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetAbbreviatedCaseNumberArray,
                this::xsetAbbreviatedCaseNumberArray,
                this::insertNewAbbreviatedCaseNumber,
                this::removeAbbreviatedCaseNumber,
                this::sizeOfAbbreviatedCaseNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "AbbreviatedCaseNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetAbbreviatedCaseNumberArray() {
        return xgetArray(PROPERTY_QNAME[0], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "AbbreviatedCaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAbbreviatedCaseNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AbbreviatedCaseNumber" element
     */
    @Override
    public int sizeOfAbbreviatedCaseNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets array of all "AbbreviatedCaseNumber" element
     */
    @Override
    public void setAbbreviatedCaseNumberArray(java.lang.String[] abbreviatedCaseNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(abbreviatedCaseNumberArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets ith "AbbreviatedCaseNumber" element
     */
    @Override
    public void setAbbreviatedCaseNumberArray(int i, java.lang.String abbreviatedCaseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(abbreviatedCaseNumber);
        }
    }

    /**
     * Sets (as xml) array of all "AbbreviatedCaseNumber" element
     */
    @Override
    public void xsetAbbreviatedCaseNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]abbreviatedCaseNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(abbreviatedCaseNumberArray, PROPERTY_QNAME[0]);
        }
    }

    /**
     * Sets (as xml) ith "AbbreviatedCaseNumber" element
     */
    @Override
    public void xsetAbbreviatedCaseNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString abbreviatedCaseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[0], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(abbreviatedCaseNumber);
        }
    }

    /**
     * Inserts the value as the ith "AbbreviatedCaseNumber" element
     */
    @Override
    public void insertAbbreviatedCaseNumber(int i, java.lang.String abbreviatedCaseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            target.setStringValue(abbreviatedCaseNumber);
        }
    }

    /**
     * Appends the value as the last "AbbreviatedCaseNumber" element
     */
    @Override
    public void addAbbreviatedCaseNumber(java.lang.String abbreviatedCaseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            target.setStringValue(abbreviatedCaseNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AbbreviatedCaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewAbbreviatedCaseNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[0], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AbbreviatedCaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewAbbreviatedCaseNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Removes the ith "AbbreviatedCaseNumber" element
     */
    @Override
    public void removeAbbreviatedCaseNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], i);
        }
    }

    /**
     * Gets a List of "AlbumTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getAlbumTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getAlbumTitleArray,
                this::setAlbumTitleArray,
                this::insertAlbumTitle,
                this::removeAlbumTitle,
                this::sizeOfAlbumTitleArray
            );
        }
    }

    /**
     * Gets array of all "AlbumTitle" elements
     */
    @Override
    public java.lang.String[] getAlbumTitleArray() {
        return getObjectArray(PROPERTY_QNAME[1], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "AlbumTitle" element
     */
    @Override
    public java.lang.String getAlbumTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "AlbumTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetAlbumTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetAlbumTitleArray,
                this::xsetAlbumTitleArray,
                this::insertNewAlbumTitle,
                this::removeAlbumTitle,
                this::sizeOfAlbumTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "AlbumTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetAlbumTitleArray() {
        return xgetArray(PROPERTY_QNAME[1], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "AlbumTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetAlbumTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "AlbumTitle" element
     */
    @Override
    public int sizeOfAlbumTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets array of all "AlbumTitle" element
     */
    @Override
    public void setAlbumTitleArray(java.lang.String[] albumTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(albumTitleArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets ith "AlbumTitle" element
     */
    @Override
    public void setAlbumTitleArray(int i, java.lang.String albumTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(albumTitle);
        }
    }

    /**
     * Sets (as xml) array of all "AlbumTitle" element
     */
    @Override
    public void xsetAlbumTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]albumTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(albumTitleArray, PROPERTY_QNAME[1]);
        }
    }

    /**
     * Sets (as xml) ith "AlbumTitle" element
     */
    @Override
    public void xsetAlbumTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString albumTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[1], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(albumTitle);
        }
    }

    /**
     * Inserts the value as the ith "AlbumTitle" element
     */
    @Override
    public void insertAlbumTitle(int i, java.lang.String albumTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            target.setStringValue(albumTitle);
        }
    }

    /**
     * Appends the value as the last "AlbumTitle" element
     */
    @Override
    public void addAlbumTitle(java.lang.String albumTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            target.setStringValue(albumTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "AlbumTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewAlbumTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[1], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "AlbumTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewAlbumTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Removes the ith "AlbumTitle" element
     */
    @Override
    public void removeAlbumTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], i);
        }
    }

    /**
     * Gets a List of "Author" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType> getAuthorList() {
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
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType[] getAuthorArray() {
        return getXmlObjectArray(PROPERTY_QNAME[2], new org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType[0]);
    }

    /**
     * Gets ith "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType getAuthorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType)get_store().find_element_user(PROPERTY_QNAME[2], i);
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
            return get_store().count_elements(PROPERTY_QNAME[2]);
        }
    }

    /**
     * Sets array of all "Author" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAuthorArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType[] authorArray) {
        check_orphaned();
        arraySetterHelper(authorArray, PROPERTY_QNAME[2]);
    }

    /**
     * Sets ith "Author" element
     */
    @Override
    public void setAuthorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType author) {
        generatedSetterHelperImpl(author, PROPERTY_QNAME[2], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType insertNewAuthor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType)get_store().insert_element_user(PROPERTY_QNAME[2], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Author" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType addNewAuthor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.CTAuthorType)get_store().add_element_user(PROPERTY_QNAME[2]);
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
            get_store().remove_element(PROPERTY_QNAME[2], i);
        }
    }

    /**
     * Gets a List of "BookTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getBookTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getBookTitleArray,
                this::setBookTitleArray,
                this::insertBookTitle,
                this::removeBookTitle,
                this::sizeOfBookTitleArray
            );
        }
    }

    /**
     * Gets array of all "BookTitle" elements
     */
    @Override
    public java.lang.String[] getBookTitleArray() {
        return getObjectArray(PROPERTY_QNAME[3], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "BookTitle" element
     */
    @Override
    public java.lang.String getBookTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "BookTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetBookTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetBookTitleArray,
                this::xsetBookTitleArray,
                this::insertNewBookTitle,
                this::removeBookTitle,
                this::sizeOfBookTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "BookTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetBookTitleArray() {
        return xgetArray(PROPERTY_QNAME[3], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "BookTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetBookTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "BookTitle" element
     */
    @Override
    public int sizeOfBookTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets array of all "BookTitle" element
     */
    @Override
    public void setBookTitleArray(java.lang.String[] bookTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(bookTitleArray, PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets ith "BookTitle" element
     */
    @Override
    public void setBookTitleArray(int i, java.lang.String bookTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(bookTitle);
        }
    }

    /**
     * Sets (as xml) array of all "BookTitle" element
     */
    @Override
    public void xsetBookTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]bookTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(bookTitleArray, PROPERTY_QNAME[3]);
        }
    }

    /**
     * Sets (as xml) ith "BookTitle" element
     */
    @Override
    public void xsetBookTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString bookTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[3], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(bookTitle);
        }
    }

    /**
     * Inserts the value as the ith "BookTitle" element
     */
    @Override
    public void insertBookTitle(int i, java.lang.String bookTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            target.setStringValue(bookTitle);
        }
    }

    /**
     * Appends the value as the last "BookTitle" element
     */
    @Override
    public void addBookTitle(java.lang.String bookTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            target.setStringValue(bookTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "BookTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewBookTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[3], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "BookTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewBookTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Removes the ith "BookTitle" element
     */
    @Override
    public void removeBookTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], i);
        }
    }

    /**
     * Gets a List of "Broadcaster" elements
     */
    @Override
    public java.util.List<java.lang.String> getBroadcasterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getBroadcasterArray,
                this::setBroadcasterArray,
                this::insertBroadcaster,
                this::removeBroadcaster,
                this::sizeOfBroadcasterArray
            );
        }
    }

    /**
     * Gets array of all "Broadcaster" elements
     */
    @Override
    public java.lang.String[] getBroadcasterArray() {
        return getObjectArray(PROPERTY_QNAME[4], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Broadcaster" element
     */
    @Override
    public java.lang.String getBroadcasterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Broadcaster" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetBroadcasterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetBroadcasterArray,
                this::xsetBroadcasterArray,
                this::insertNewBroadcaster,
                this::removeBroadcaster,
                this::sizeOfBroadcasterArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Broadcaster" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetBroadcasterArray() {
        return xgetArray(PROPERTY_QNAME[4], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Broadcaster" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetBroadcasterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Broadcaster" element
     */
    @Override
    public int sizeOfBroadcasterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets array of all "Broadcaster" element
     */
    @Override
    public void setBroadcasterArray(java.lang.String[] broadcasterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(broadcasterArray, PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets ith "Broadcaster" element
     */
    @Override
    public void setBroadcasterArray(int i, java.lang.String broadcaster) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(broadcaster);
        }
    }

    /**
     * Sets (as xml) array of all "Broadcaster" element
     */
    @Override
    public void xsetBroadcasterArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]broadcasterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(broadcasterArray, PROPERTY_QNAME[4]);
        }
    }

    /**
     * Sets (as xml) ith "Broadcaster" element
     */
    @Override
    public void xsetBroadcasterArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString broadcaster) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[4], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(broadcaster);
        }
    }

    /**
     * Inserts the value as the ith "Broadcaster" element
     */
    @Override
    public void insertBroadcaster(int i, java.lang.String broadcaster) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            target.setStringValue(broadcaster);
        }
    }

    /**
     * Appends the value as the last "Broadcaster" element
     */
    @Override
    public void addBroadcaster(java.lang.String broadcaster) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[4]);
            target.setStringValue(broadcaster);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Broadcaster" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewBroadcaster(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[4], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Broadcaster" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewBroadcaster() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Removes the ith "Broadcaster" element
     */
    @Override
    public void removeBroadcaster(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], i);
        }
    }

    /**
     * Gets a List of "BroadcastTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getBroadcastTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getBroadcastTitleArray,
                this::setBroadcastTitleArray,
                this::insertBroadcastTitle,
                this::removeBroadcastTitle,
                this::sizeOfBroadcastTitleArray
            );
        }
    }

    /**
     * Gets array of all "BroadcastTitle" elements
     */
    @Override
    public java.lang.String[] getBroadcastTitleArray() {
        return getObjectArray(PROPERTY_QNAME[5], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "BroadcastTitle" element
     */
    @Override
    public java.lang.String getBroadcastTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "BroadcastTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetBroadcastTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetBroadcastTitleArray,
                this::xsetBroadcastTitleArray,
                this::insertNewBroadcastTitle,
                this::removeBroadcastTitle,
                this::sizeOfBroadcastTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "BroadcastTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetBroadcastTitleArray() {
        return xgetArray(PROPERTY_QNAME[5], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "BroadcastTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetBroadcastTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "BroadcastTitle" element
     */
    @Override
    public int sizeOfBroadcastTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets array of all "BroadcastTitle" element
     */
    @Override
    public void setBroadcastTitleArray(java.lang.String[] broadcastTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(broadcastTitleArray, PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets ith "BroadcastTitle" element
     */
    @Override
    public void setBroadcastTitleArray(int i, java.lang.String broadcastTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(broadcastTitle);
        }
    }

    /**
     * Sets (as xml) array of all "BroadcastTitle" element
     */
    @Override
    public void xsetBroadcastTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]broadcastTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(broadcastTitleArray, PROPERTY_QNAME[5]);
        }
    }

    /**
     * Sets (as xml) ith "BroadcastTitle" element
     */
    @Override
    public void xsetBroadcastTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString broadcastTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[5], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(broadcastTitle);
        }
    }

    /**
     * Inserts the value as the ith "BroadcastTitle" element
     */
    @Override
    public void insertBroadcastTitle(int i, java.lang.String broadcastTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            target.setStringValue(broadcastTitle);
        }
    }

    /**
     * Appends the value as the last "BroadcastTitle" element
     */
    @Override
    public void addBroadcastTitle(java.lang.String broadcastTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[5]);
            target.setStringValue(broadcastTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "BroadcastTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewBroadcastTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[5], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "BroadcastTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewBroadcastTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Removes the ith "BroadcastTitle" element
     */
    @Override
    public void removeBroadcastTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], i);
        }
    }

    /**
     * Gets a List of "CaseNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getCaseNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getCaseNumberArray,
                this::setCaseNumberArray,
                this::insertCaseNumber,
                this::removeCaseNumber,
                this::sizeOfCaseNumberArray
            );
        }
    }

    /**
     * Gets array of all "CaseNumber" elements
     */
    @Override
    public java.lang.String[] getCaseNumberArray() {
        return getObjectArray(PROPERTY_QNAME[6], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "CaseNumber" element
     */
    @Override
    public java.lang.String getCaseNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "CaseNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetCaseNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetCaseNumberArray,
                this::xsetCaseNumberArray,
                this::insertNewCaseNumber,
                this::removeCaseNumber,
                this::sizeOfCaseNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "CaseNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetCaseNumberArray() {
        return xgetArray(PROPERTY_QNAME[6], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "CaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCaseNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CaseNumber" element
     */
    @Override
    public int sizeOfCaseNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets array of all "CaseNumber" element
     */
    @Override
    public void setCaseNumberArray(java.lang.String[] caseNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(caseNumberArray, PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets ith "CaseNumber" element
     */
    @Override
    public void setCaseNumberArray(int i, java.lang.String caseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(caseNumber);
        }
    }

    /**
     * Sets (as xml) array of all "CaseNumber" element
     */
    @Override
    public void xsetCaseNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]caseNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(caseNumberArray, PROPERTY_QNAME[6]);
        }
    }

    /**
     * Sets (as xml) ith "CaseNumber" element
     */
    @Override
    public void xsetCaseNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString caseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[6], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(caseNumber);
        }
    }

    /**
     * Inserts the value as the ith "CaseNumber" element
     */
    @Override
    public void insertCaseNumber(int i, java.lang.String caseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            target.setStringValue(caseNumber);
        }
    }

    /**
     * Appends the value as the last "CaseNumber" element
     */
    @Override
    public void addCaseNumber(java.lang.String caseNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[6]);
            target.setStringValue(caseNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewCaseNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[6], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CaseNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewCaseNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Removes the ith "CaseNumber" element
     */
    @Override
    public void removeCaseNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], i);
        }
    }

    /**
     * Gets a List of "ChapterNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getChapterNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getChapterNumberArray,
                this::setChapterNumberArray,
                this::insertChapterNumber,
                this::removeChapterNumber,
                this::sizeOfChapterNumberArray
            );
        }
    }

    /**
     * Gets array of all "ChapterNumber" elements
     */
    @Override
    public java.lang.String[] getChapterNumberArray() {
        return getObjectArray(PROPERTY_QNAME[7], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ChapterNumber" element
     */
    @Override
    public java.lang.String getChapterNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ChapterNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetChapterNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetChapterNumberArray,
                this::xsetChapterNumberArray,
                this::insertNewChapterNumber,
                this::removeChapterNumber,
                this::sizeOfChapterNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ChapterNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetChapterNumberArray() {
        return xgetArray(PROPERTY_QNAME[7], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "ChapterNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetChapterNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ChapterNumber" element
     */
    @Override
    public int sizeOfChapterNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets array of all "ChapterNumber" element
     */
    @Override
    public void setChapterNumberArray(java.lang.String[] chapterNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(chapterNumberArray, PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets ith "ChapterNumber" element
     */
    @Override
    public void setChapterNumberArray(int i, java.lang.String chapterNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(chapterNumber);
        }
    }

    /**
     * Sets (as xml) array of all "ChapterNumber" element
     */
    @Override
    public void xsetChapterNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]chapterNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(chapterNumberArray, PROPERTY_QNAME[7]);
        }
    }

    /**
     * Sets (as xml) ith "ChapterNumber" element
     */
    @Override
    public void xsetChapterNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString chapterNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[7], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(chapterNumber);
        }
    }

    /**
     * Inserts the value as the ith "ChapterNumber" element
     */
    @Override
    public void insertChapterNumber(int i, java.lang.String chapterNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            target.setStringValue(chapterNumber);
        }
    }

    /**
     * Appends the value as the last "ChapterNumber" element
     */
    @Override
    public void addChapterNumber(java.lang.String chapterNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[7]);
            target.setStringValue(chapterNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ChapterNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewChapterNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[7], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ChapterNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewChapterNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Removes the ith "ChapterNumber" element
     */
    @Override
    public void removeChapterNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], i);
        }
    }

    /**
     * Gets a List of "City" elements
     */
    @Override
    public java.util.List<java.lang.String> getCityList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getCityArray,
                this::setCityArray,
                this::insertCity,
                this::removeCity,
                this::sizeOfCityArray
            );
        }
    }

    /**
     * Gets array of all "City" elements
     */
    @Override
    public java.lang.String[] getCityArray() {
        return getObjectArray(PROPERTY_QNAME[8], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "City" element
     */
    @Override
    public java.lang.String getCityArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "City" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetCityList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetCityArray,
                this::xsetCityArray,
                this::insertNewCity,
                this::removeCity,
                this::sizeOfCityArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "City" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetCityArray() {
        return xgetArray(PROPERTY_QNAME[8], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "City" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCityArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "City" element
     */
    @Override
    public int sizeOfCityArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets array of all "City" element
     */
    @Override
    public void setCityArray(java.lang.String[] cityArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cityArray, PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets ith "City" element
     */
    @Override
    public void setCityArray(int i, java.lang.String city) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(city);
        }
    }

    /**
     * Sets (as xml) array of all "City" element
     */
    @Override
    public void xsetCityArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]cityArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(cityArray, PROPERTY_QNAME[8]);
        }
    }

    /**
     * Sets (as xml) ith "City" element
     */
    @Override
    public void xsetCityArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString city) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[8], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(city);
        }
    }

    /**
     * Inserts the value as the ith "City" element
     */
    @Override
    public void insertCity(int i, java.lang.String city) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            target.setStringValue(city);
        }
    }

    /**
     * Appends the value as the last "City" element
     */
    @Override
    public void addCity(java.lang.String city) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[8]);
            target.setStringValue(city);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "City" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewCity(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[8], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "City" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewCity() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Removes the ith "City" element
     */
    @Override
    public void removeCity(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], i);
        }
    }

    /**
     * Gets a List of "Comments" elements
     */
    @Override
    public java.util.List<java.lang.String> getCommentsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getCommentsArray,
                this::setCommentsArray,
                this::insertComments,
                this::removeComments,
                this::sizeOfCommentsArray
            );
        }
    }

    /**
     * Gets array of all "Comments" elements
     */
    @Override
    public java.lang.String[] getCommentsArray() {
        return getObjectArray(PROPERTY_QNAME[9], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Comments" element
     */
    @Override
    public java.lang.String getCommentsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Comments" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetCommentsList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetCommentsArray,
                this::xsetCommentsArray,
                this::insertNewComments,
                this::removeComments,
                this::sizeOfCommentsArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Comments" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetCommentsArray() {
        return xgetArray(PROPERTY_QNAME[9], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Comments" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCommentsArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Comments" element
     */
    @Override
    public int sizeOfCommentsArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets array of all "Comments" element
     */
    @Override
    public void setCommentsArray(java.lang.String[] commentsArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(commentsArray, PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets ith "Comments" element
     */
    @Override
    public void setCommentsArray(int i, java.lang.String comments) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(comments);
        }
    }

    /**
     * Sets (as xml) array of all "Comments" element
     */
    @Override
    public void xsetCommentsArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]commentsArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(commentsArray, PROPERTY_QNAME[9]);
        }
    }

    /**
     * Sets (as xml) ith "Comments" element
     */
    @Override
    public void xsetCommentsArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString comments) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[9], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(comments);
        }
    }

    /**
     * Inserts the value as the ith "Comments" element
     */
    @Override
    public void insertComments(int i, java.lang.String comments) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            target.setStringValue(comments);
        }
    }

    /**
     * Appends the value as the last "Comments" element
     */
    @Override
    public void addComments(java.lang.String comments) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[9]);
            target.setStringValue(comments);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Comments" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewComments(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[9], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Comments" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewComments() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Removes the ith "Comments" element
     */
    @Override
    public void removeComments(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], i);
        }
    }

    /**
     * Gets a List of "ConferenceName" elements
     */
    @Override
    public java.util.List<java.lang.String> getConferenceNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getConferenceNameArray,
                this::setConferenceNameArray,
                this::insertConferenceName,
                this::removeConferenceName,
                this::sizeOfConferenceNameArray
            );
        }
    }

    /**
     * Gets array of all "ConferenceName" elements
     */
    @Override
    public java.lang.String[] getConferenceNameArray() {
        return getObjectArray(PROPERTY_QNAME[10], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ConferenceName" element
     */
    @Override
    public java.lang.String getConferenceNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ConferenceName" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetConferenceNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetConferenceNameArray,
                this::xsetConferenceNameArray,
                this::insertNewConferenceName,
                this::removeConferenceName,
                this::sizeOfConferenceNameArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ConferenceName" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetConferenceNameArray() {
        return xgetArray(PROPERTY_QNAME[10], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "ConferenceName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetConferenceNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ConferenceName" element
     */
    @Override
    public int sizeOfConferenceNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets array of all "ConferenceName" element
     */
    @Override
    public void setConferenceNameArray(java.lang.String[] conferenceNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(conferenceNameArray, PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets ith "ConferenceName" element
     */
    @Override
    public void setConferenceNameArray(int i, java.lang.String conferenceName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(conferenceName);
        }
    }

    /**
     * Sets (as xml) array of all "ConferenceName" element
     */
    @Override
    public void xsetConferenceNameArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]conferenceNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(conferenceNameArray, PROPERTY_QNAME[10]);
        }
    }

    /**
     * Sets (as xml) ith "ConferenceName" element
     */
    @Override
    public void xsetConferenceNameArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString conferenceName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[10], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(conferenceName);
        }
    }

    /**
     * Inserts the value as the ith "ConferenceName" element
     */
    @Override
    public void insertConferenceName(int i, java.lang.String conferenceName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            target.setStringValue(conferenceName);
        }
    }

    /**
     * Appends the value as the last "ConferenceName" element
     */
    @Override
    public void addConferenceName(java.lang.String conferenceName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[10]);
            target.setStringValue(conferenceName);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ConferenceName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewConferenceName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[10], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ConferenceName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewConferenceName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Removes the ith "ConferenceName" element
     */
    @Override
    public void removeConferenceName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], i);
        }
    }

    /**
     * Gets a List of "CountryRegion" elements
     */
    @Override
    public java.util.List<java.lang.String> getCountryRegionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getCountryRegionArray,
                this::setCountryRegionArray,
                this::insertCountryRegion,
                this::removeCountryRegion,
                this::sizeOfCountryRegionArray
            );
        }
    }

    /**
     * Gets array of all "CountryRegion" elements
     */
    @Override
    public java.lang.String[] getCountryRegionArray() {
        return getObjectArray(PROPERTY_QNAME[11], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "CountryRegion" element
     */
    @Override
    public java.lang.String getCountryRegionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "CountryRegion" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetCountryRegionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetCountryRegionArray,
                this::xsetCountryRegionArray,
                this::insertNewCountryRegion,
                this::removeCountryRegion,
                this::sizeOfCountryRegionArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "CountryRegion" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetCountryRegionArray() {
        return xgetArray(PROPERTY_QNAME[11], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "CountryRegion" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCountryRegionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "CountryRegion" element
     */
    @Override
    public int sizeOfCountryRegionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets array of all "CountryRegion" element
     */
    @Override
    public void setCountryRegionArray(java.lang.String[] countryRegionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(countryRegionArray, PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets ith "CountryRegion" element
     */
    @Override
    public void setCountryRegionArray(int i, java.lang.String countryRegion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(countryRegion);
        }
    }

    /**
     * Sets (as xml) array of all "CountryRegion" element
     */
    @Override
    public void xsetCountryRegionArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]countryRegionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(countryRegionArray, PROPERTY_QNAME[11]);
        }
    }

    /**
     * Sets (as xml) ith "CountryRegion" element
     */
    @Override
    public void xsetCountryRegionArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString countryRegion) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[11], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(countryRegion);
        }
    }

    /**
     * Inserts the value as the ith "CountryRegion" element
     */
    @Override
    public void insertCountryRegion(int i, java.lang.String countryRegion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            target.setStringValue(countryRegion);
        }
    }

    /**
     * Appends the value as the last "CountryRegion" element
     */
    @Override
    public void addCountryRegion(java.lang.String countryRegion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[11]);
            target.setStringValue(countryRegion);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "CountryRegion" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewCountryRegion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[11], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "CountryRegion" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewCountryRegion() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Removes the ith "CountryRegion" element
     */
    @Override
    public void removeCountryRegion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], i);
        }
    }

    /**
     * Gets a List of "Court" elements
     */
    @Override
    public java.util.List<java.lang.String> getCourtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getCourtArray,
                this::setCourtArray,
                this::insertCourt,
                this::removeCourt,
                this::sizeOfCourtArray
            );
        }
    }

    /**
     * Gets array of all "Court" elements
     */
    @Override
    public java.lang.String[] getCourtArray() {
        return getObjectArray(PROPERTY_QNAME[12], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Court" element
     */
    @Override
    public java.lang.String getCourtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Court" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetCourtList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetCourtArray,
                this::xsetCourtArray,
                this::insertNewCourt,
                this::removeCourt,
                this::sizeOfCourtArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Court" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetCourtArray() {
        return xgetArray(PROPERTY_QNAME[12], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Court" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetCourtArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Court" element
     */
    @Override
    public int sizeOfCourtArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets array of all "Court" element
     */
    @Override
    public void setCourtArray(java.lang.String[] courtArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(courtArray, PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets ith "Court" element
     */
    @Override
    public void setCourtArray(int i, java.lang.String court) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(court);
        }
    }

    /**
     * Sets (as xml) array of all "Court" element
     */
    @Override
    public void xsetCourtArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]courtArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(courtArray, PROPERTY_QNAME[12]);
        }
    }

    /**
     * Sets (as xml) ith "Court" element
     */
    @Override
    public void xsetCourtArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString court) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[12], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(court);
        }
    }

    /**
     * Inserts the value as the ith "Court" element
     */
    @Override
    public void insertCourt(int i, java.lang.String court) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            target.setStringValue(court);
        }
    }

    /**
     * Appends the value as the last "Court" element
     */
    @Override
    public void addCourt(java.lang.String court) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[12]);
            target.setStringValue(court);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Court" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewCourt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[12], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Court" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewCourt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Removes the ith "Court" element
     */
    @Override
    public void removeCourt(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], i);
        }
    }

    /**
     * Gets a List of "Day" elements
     */
    @Override
    public java.util.List<java.lang.String> getDayList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getDayArray,
                this::setDayArray,
                this::insertDay,
                this::removeDay,
                this::sizeOfDayArray
            );
        }
    }

    /**
     * Gets array of all "Day" elements
     */
    @Override
    public java.lang.String[] getDayArray() {
        return getObjectArray(PROPERTY_QNAME[13], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Day" element
     */
    @Override
    public java.lang.String getDayArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Day" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetDayList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetDayArray,
                this::xsetDayArray,
                this::insertNewDay,
                this::removeDay,
                this::sizeOfDayArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Day" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetDayArray() {
        return xgetArray(PROPERTY_QNAME[13], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Day" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDayArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Day" element
     */
    @Override
    public int sizeOfDayArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets array of all "Day" element
     */
    @Override
    public void setDayArray(java.lang.String[] dayArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(dayArray, PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets ith "Day" element
     */
    @Override
    public void setDayArray(int i, java.lang.String day) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(day);
        }
    }

    /**
     * Sets (as xml) array of all "Day" element
     */
    @Override
    public void xsetDayArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]dayArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(dayArray, PROPERTY_QNAME[13]);
        }
    }

    /**
     * Sets (as xml) ith "Day" element
     */
    @Override
    public void xsetDayArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString day) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[13], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(day);
        }
    }

    /**
     * Inserts the value as the ith "Day" element
     */
    @Override
    public void insertDay(int i, java.lang.String day) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            target.setStringValue(day);
        }
    }

    /**
     * Appends the value as the last "Day" element
     */
    @Override
    public void addDay(java.lang.String day) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[13]);
            target.setStringValue(day);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Day" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewDay(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[13], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Day" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewDay() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Removes the ith "Day" element
     */
    @Override
    public void removeDay(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], i);
        }
    }

    /**
     * Gets a List of "DayAccessed" elements
     */
    @Override
    public java.util.List<java.lang.String> getDayAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getDayAccessedArray,
                this::setDayAccessedArray,
                this::insertDayAccessed,
                this::removeDayAccessed,
                this::sizeOfDayAccessedArray
            );
        }
    }

    /**
     * Gets array of all "DayAccessed" elements
     */
    @Override
    public java.lang.String[] getDayAccessedArray() {
        return getObjectArray(PROPERTY_QNAME[14], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "DayAccessed" element
     */
    @Override
    public java.lang.String getDayAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "DayAccessed" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetDayAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetDayAccessedArray,
                this::xsetDayAccessedArray,
                this::insertNewDayAccessed,
                this::removeDayAccessed,
                this::sizeOfDayAccessedArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "DayAccessed" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetDayAccessedArray() {
        return xgetArray(PROPERTY_QNAME[14], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "DayAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDayAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "DayAccessed" element
     */
    @Override
    public int sizeOfDayAccessedArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets array of all "DayAccessed" element
     */
    @Override
    public void setDayAccessedArray(java.lang.String[] dayAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(dayAccessedArray, PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets ith "DayAccessed" element
     */
    @Override
    public void setDayAccessedArray(int i, java.lang.String dayAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(dayAccessed);
        }
    }

    /**
     * Sets (as xml) array of all "DayAccessed" element
     */
    @Override
    public void xsetDayAccessedArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]dayAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(dayAccessedArray, PROPERTY_QNAME[14]);
        }
    }

    /**
     * Sets (as xml) ith "DayAccessed" element
     */
    @Override
    public void xsetDayAccessedArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString dayAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[14], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(dayAccessed);
        }
    }

    /**
     * Inserts the value as the ith "DayAccessed" element
     */
    @Override
    public void insertDayAccessed(int i, java.lang.String dayAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            target.setStringValue(dayAccessed);
        }
    }

    /**
     * Appends the value as the last "DayAccessed" element
     */
    @Override
    public void addDayAccessed(java.lang.String dayAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[14]);
            target.setStringValue(dayAccessed);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "DayAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewDayAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[14], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "DayAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewDayAccessed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Removes the ith "DayAccessed" element
     */
    @Override
    public void removeDayAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], i);
        }
    }

    /**
     * Gets a List of "Department" elements
     */
    @Override
    public java.util.List<java.lang.String> getDepartmentList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getDepartmentArray,
                this::setDepartmentArray,
                this::insertDepartment,
                this::removeDepartment,
                this::sizeOfDepartmentArray
            );
        }
    }

    /**
     * Gets array of all "Department" elements
     */
    @Override
    public java.lang.String[] getDepartmentArray() {
        return getObjectArray(PROPERTY_QNAME[15], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Department" element
     */
    @Override
    public java.lang.String getDepartmentArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Department" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetDepartmentList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetDepartmentArray,
                this::xsetDepartmentArray,
                this::insertNewDepartment,
                this::removeDepartment,
                this::sizeOfDepartmentArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Department" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetDepartmentArray() {
        return xgetArray(PROPERTY_QNAME[15], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Department" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDepartmentArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Department" element
     */
    @Override
    public int sizeOfDepartmentArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets array of all "Department" element
     */
    @Override
    public void setDepartmentArray(java.lang.String[] departmentArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(departmentArray, PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets ith "Department" element
     */
    @Override
    public void setDepartmentArray(int i, java.lang.String department) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(department);
        }
    }

    /**
     * Sets (as xml) array of all "Department" element
     */
    @Override
    public void xsetDepartmentArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]departmentArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(departmentArray, PROPERTY_QNAME[15]);
        }
    }

    /**
     * Sets (as xml) ith "Department" element
     */
    @Override
    public void xsetDepartmentArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString department) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[15], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(department);
        }
    }

    /**
     * Inserts the value as the ith "Department" element
     */
    @Override
    public void insertDepartment(int i, java.lang.String department) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            target.setStringValue(department);
        }
    }

    /**
     * Appends the value as the last "Department" element
     */
    @Override
    public void addDepartment(java.lang.String department) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[15]);
            target.setStringValue(department);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Department" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewDepartment(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[15], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Department" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewDepartment() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Removes the ith "Department" element
     */
    @Override
    public void removeDepartment(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], i);
        }
    }

    /**
     * Gets a List of "Distributor" elements
     */
    @Override
    public java.util.List<java.lang.String> getDistributorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getDistributorArray,
                this::setDistributorArray,
                this::insertDistributor,
                this::removeDistributor,
                this::sizeOfDistributorArray
            );
        }
    }

    /**
     * Gets array of all "Distributor" elements
     */
    @Override
    public java.lang.String[] getDistributorArray() {
        return getObjectArray(PROPERTY_QNAME[16], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Distributor" element
     */
    @Override
    public java.lang.String getDistributorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Distributor" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetDistributorList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetDistributorArray,
                this::xsetDistributorArray,
                this::insertNewDistributor,
                this::removeDistributor,
                this::sizeOfDistributorArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Distributor" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetDistributorArray() {
        return xgetArray(PROPERTY_QNAME[16], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Distributor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetDistributorArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Distributor" element
     */
    @Override
    public int sizeOfDistributorArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets array of all "Distributor" element
     */
    @Override
    public void setDistributorArray(java.lang.String[] distributorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(distributorArray, PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets ith "Distributor" element
     */
    @Override
    public void setDistributorArray(int i, java.lang.String distributor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(distributor);
        }
    }

    /**
     * Sets (as xml) array of all "Distributor" element
     */
    @Override
    public void xsetDistributorArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]distributorArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(distributorArray, PROPERTY_QNAME[16]);
        }
    }

    /**
     * Sets (as xml) ith "Distributor" element
     */
    @Override
    public void xsetDistributorArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString distributor) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[16], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(distributor);
        }
    }

    /**
     * Inserts the value as the ith "Distributor" element
     */
    @Override
    public void insertDistributor(int i, java.lang.String distributor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            target.setStringValue(distributor);
        }
    }

    /**
     * Appends the value as the last "Distributor" element
     */
    @Override
    public void addDistributor(java.lang.String distributor) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[16]);
            target.setStringValue(distributor);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Distributor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewDistributor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[16], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Distributor" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewDistributor() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Removes the ith "Distributor" element
     */
    @Override
    public void removeDistributor(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], i);
        }
    }

    /**
     * Gets a List of "Edition" elements
     */
    @Override
    public java.util.List<java.lang.String> getEditionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getEditionArray,
                this::setEditionArray,
                this::insertEdition,
                this::removeEdition,
                this::sizeOfEditionArray
            );
        }
    }

    /**
     * Gets array of all "Edition" elements
     */
    @Override
    public java.lang.String[] getEditionArray() {
        return getObjectArray(PROPERTY_QNAME[17], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Edition" element
     */
    @Override
    public java.lang.String getEditionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Edition" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetEditionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetEditionArray,
                this::xsetEditionArray,
                this::insertNewEdition,
                this::removeEdition,
                this::sizeOfEditionArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Edition" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetEditionArray() {
        return xgetArray(PROPERTY_QNAME[17], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Edition" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetEditionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Edition" element
     */
    @Override
    public int sizeOfEditionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets array of all "Edition" element
     */
    @Override
    public void setEditionArray(java.lang.String[] editionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(editionArray, PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets ith "Edition" element
     */
    @Override
    public void setEditionArray(int i, java.lang.String edition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(edition);
        }
    }

    /**
     * Sets (as xml) array of all "Edition" element
     */
    @Override
    public void xsetEditionArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]editionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(editionArray, PROPERTY_QNAME[17]);
        }
    }

    /**
     * Sets (as xml) ith "Edition" element
     */
    @Override
    public void xsetEditionArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString edition) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[17], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(edition);
        }
    }

    /**
     * Inserts the value as the ith "Edition" element
     */
    @Override
    public void insertEdition(int i, java.lang.String edition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            target.setStringValue(edition);
        }
    }

    /**
     * Appends the value as the last "Edition" element
     */
    @Override
    public void addEdition(java.lang.String edition) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[17]);
            target.setStringValue(edition);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Edition" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewEdition(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[17], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Edition" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewEdition() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Removes the ith "Edition" element
     */
    @Override
    public void removeEdition(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], i);
        }
    }

    /**
     * Gets a List of "Guid" elements
     */
    @Override
    public java.util.List<java.lang.String> getGuidList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getGuidArray,
                this::setGuidArray,
                this::insertGuid,
                this::removeGuid,
                this::sizeOfGuidArray
            );
        }
    }

    /**
     * Gets array of all "Guid" elements
     */
    @Override
    public java.lang.String[] getGuidArray() {
        return getObjectArray(PROPERTY_QNAME[18], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Guid" element
     */
    @Override
    public java.lang.String getGuidArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Guid" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetGuidList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetGuidArray,
                this::xsetGuidArray,
                this::insertNewGuid,
                this::removeGuid,
                this::sizeOfGuidArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Guid" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetGuidArray() {
        return xgetArray(PROPERTY_QNAME[18], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Guid" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetGuidArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Guid" element
     */
    @Override
    public int sizeOfGuidArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets array of all "Guid" element
     */
    @Override
    public void setGuidArray(java.lang.String[] guidArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(guidArray, PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets ith "Guid" element
     */
    @Override
    public void setGuidArray(int i, java.lang.String guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(guid);
        }
    }

    /**
     * Sets (as xml) array of all "Guid" element
     */
    @Override
    public void xsetGuidArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]guidArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(guidArray, PROPERTY_QNAME[18]);
        }
    }

    /**
     * Sets (as xml) ith "Guid" element
     */
    @Override
    public void xsetGuidArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[18], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(guid);
        }
    }

    /**
     * Inserts the value as the ith "Guid" element
     */
    @Override
    public void insertGuid(int i, java.lang.String guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            target.setStringValue(guid);
        }
    }

    /**
     * Appends the value as the last "Guid" element
     */
    @Override
    public void addGuid(java.lang.String guid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[18]);
            target.setStringValue(guid);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Guid" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewGuid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[18], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Guid" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewGuid() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Removes the ith "Guid" element
     */
    @Override
    public void removeGuid(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], i);
        }
    }

    /**
     * Gets a List of "Institution" elements
     */
    @Override
    public java.util.List<java.lang.String> getInstitutionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getInstitutionArray,
                this::setInstitutionArray,
                this::insertInstitution,
                this::removeInstitution,
                this::sizeOfInstitutionArray
            );
        }
    }

    /**
     * Gets array of all "Institution" elements
     */
    @Override
    public java.lang.String[] getInstitutionArray() {
        return getObjectArray(PROPERTY_QNAME[19], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Institution" element
     */
    @Override
    public java.lang.String getInstitutionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Institution" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetInstitutionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetInstitutionArray,
                this::xsetInstitutionArray,
                this::insertNewInstitution,
                this::removeInstitution,
                this::sizeOfInstitutionArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Institution" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetInstitutionArray() {
        return xgetArray(PROPERTY_QNAME[19], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Institution" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetInstitutionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Institution" element
     */
    @Override
    public int sizeOfInstitutionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets array of all "Institution" element
     */
    @Override
    public void setInstitutionArray(java.lang.String[] institutionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(institutionArray, PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets ith "Institution" element
     */
    @Override
    public void setInstitutionArray(int i, java.lang.String institution) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(institution);
        }
    }

    /**
     * Sets (as xml) array of all "Institution" element
     */
    @Override
    public void xsetInstitutionArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]institutionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(institutionArray, PROPERTY_QNAME[19]);
        }
    }

    /**
     * Sets (as xml) ith "Institution" element
     */
    @Override
    public void xsetInstitutionArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString institution) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[19], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(institution);
        }
    }

    /**
     * Inserts the value as the ith "Institution" element
     */
    @Override
    public void insertInstitution(int i, java.lang.String institution) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            target.setStringValue(institution);
        }
    }

    /**
     * Appends the value as the last "Institution" element
     */
    @Override
    public void addInstitution(java.lang.String institution) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[19]);
            target.setStringValue(institution);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Institution" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewInstitution(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[19], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Institution" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewInstitution() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Removes the ith "Institution" element
     */
    @Override
    public void removeInstitution(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], i);
        }
    }

    /**
     * Gets a List of "InternetSiteTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getInternetSiteTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getInternetSiteTitleArray,
                this::setInternetSiteTitleArray,
                this::insertInternetSiteTitle,
                this::removeInternetSiteTitle,
                this::sizeOfInternetSiteTitleArray
            );
        }
    }

    /**
     * Gets array of all "InternetSiteTitle" elements
     */
    @Override
    public java.lang.String[] getInternetSiteTitleArray() {
        return getObjectArray(PROPERTY_QNAME[20], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "InternetSiteTitle" element
     */
    @Override
    public java.lang.String getInternetSiteTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "InternetSiteTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetInternetSiteTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetInternetSiteTitleArray,
                this::xsetInternetSiteTitleArray,
                this::insertNewInternetSiteTitle,
                this::removeInternetSiteTitle,
                this::sizeOfInternetSiteTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "InternetSiteTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetInternetSiteTitleArray() {
        return xgetArray(PROPERTY_QNAME[20], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "InternetSiteTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetInternetSiteTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "InternetSiteTitle" element
     */
    @Override
    public int sizeOfInternetSiteTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets array of all "InternetSiteTitle" element
     */
    @Override
    public void setInternetSiteTitleArray(java.lang.String[] internetSiteTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(internetSiteTitleArray, PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets ith "InternetSiteTitle" element
     */
    @Override
    public void setInternetSiteTitleArray(int i, java.lang.String internetSiteTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(internetSiteTitle);
        }
    }

    /**
     * Sets (as xml) array of all "InternetSiteTitle" element
     */
    @Override
    public void xsetInternetSiteTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]internetSiteTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(internetSiteTitleArray, PROPERTY_QNAME[20]);
        }
    }

    /**
     * Sets (as xml) ith "InternetSiteTitle" element
     */
    @Override
    public void xsetInternetSiteTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString internetSiteTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[20], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(internetSiteTitle);
        }
    }

    /**
     * Inserts the value as the ith "InternetSiteTitle" element
     */
    @Override
    public void insertInternetSiteTitle(int i, java.lang.String internetSiteTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            target.setStringValue(internetSiteTitle);
        }
    }

    /**
     * Appends the value as the last "InternetSiteTitle" element
     */
    @Override
    public void addInternetSiteTitle(java.lang.String internetSiteTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[20]);
            target.setStringValue(internetSiteTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "InternetSiteTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewInternetSiteTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[20], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "InternetSiteTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewInternetSiteTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Removes the ith "InternetSiteTitle" element
     */
    @Override
    public void removeInternetSiteTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], i);
        }
    }

    /**
     * Gets a List of "Issue" elements
     */
    @Override
    public java.util.List<java.lang.String> getIssueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getIssueArray,
                this::setIssueArray,
                this::insertIssue,
                this::removeIssue,
                this::sizeOfIssueArray
            );
        }
    }

    /**
     * Gets array of all "Issue" elements
     */
    @Override
    public java.lang.String[] getIssueArray() {
        return getObjectArray(PROPERTY_QNAME[21], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Issue" element
     */
    @Override
    public java.lang.String getIssueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Issue" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetIssueList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetIssueArray,
                this::xsetIssueArray,
                this::insertNewIssue,
                this::removeIssue,
                this::sizeOfIssueArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Issue" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetIssueArray() {
        return xgetArray(PROPERTY_QNAME[21], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Issue" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetIssueArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Issue" element
     */
    @Override
    public int sizeOfIssueArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "Issue" element
     */
    @Override
    public void setIssueArray(java.lang.String[] issueArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(issueArray, PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets ith "Issue" element
     */
    @Override
    public void setIssueArray(int i, java.lang.String issue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(issue);
        }
    }

    /**
     * Sets (as xml) array of all "Issue" element
     */
    @Override
    public void xsetIssueArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]issueArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(issueArray, PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets (as xml) ith "Issue" element
     */
    @Override
    public void xsetIssueArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString issue) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(issue);
        }
    }

    /**
     * Inserts the value as the ith "Issue" element
     */
    @Override
    public void insertIssue(int i, java.lang.String issue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            target.setStringValue(issue);
        }
    }

    /**
     * Appends the value as the last "Issue" element
     */
    @Override
    public void addIssue(java.lang.String issue) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[21]);
            target.setStringValue(issue);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Issue" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewIssue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Issue" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewIssue() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "Issue" element
     */
    @Override
    public void removeIssue(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets a List of "JournalName" elements
     */
    @Override
    public java.util.List<java.lang.String> getJournalNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getJournalNameArray,
                this::setJournalNameArray,
                this::insertJournalName,
                this::removeJournalName,
                this::sizeOfJournalNameArray
            );
        }
    }

    /**
     * Gets array of all "JournalName" elements
     */
    @Override
    public java.lang.String[] getJournalNameArray() {
        return getObjectArray(PROPERTY_QNAME[22], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "JournalName" element
     */
    @Override
    public java.lang.String getJournalNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "JournalName" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetJournalNameList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetJournalNameArray,
                this::xsetJournalNameArray,
                this::insertNewJournalName,
                this::removeJournalName,
                this::sizeOfJournalNameArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "JournalName" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetJournalNameArray() {
        return xgetArray(PROPERTY_QNAME[22], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "JournalName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetJournalNameArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "JournalName" element
     */
    @Override
    public int sizeOfJournalNameArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets array of all "JournalName" element
     */
    @Override
    public void setJournalNameArray(java.lang.String[] journalNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(journalNameArray, PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets ith "JournalName" element
     */
    @Override
    public void setJournalNameArray(int i, java.lang.String journalName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(journalName);
        }
    }

    /**
     * Sets (as xml) array of all "JournalName" element
     */
    @Override
    public void xsetJournalNameArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]journalNameArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(journalNameArray, PROPERTY_QNAME[22]);
        }
    }

    /**
     * Sets (as xml) ith "JournalName" element
     */
    @Override
    public void xsetJournalNameArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString journalName) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[22], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(journalName);
        }
    }

    /**
     * Inserts the value as the ith "JournalName" element
     */
    @Override
    public void insertJournalName(int i, java.lang.String journalName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            target.setStringValue(journalName);
        }
    }

    /**
     * Appends the value as the last "JournalName" element
     */
    @Override
    public void addJournalName(java.lang.String journalName) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[22]);
            target.setStringValue(journalName);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "JournalName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewJournalName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[22], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "JournalName" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewJournalName() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Removes the ith "JournalName" element
     */
    @Override
    public void removeJournalName(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], i);
        }
    }

    /**
     * Gets a List of "LCID" elements
     */
    @Override
    public java.util.List<java.lang.String> getLCIDList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getLCIDArray,
                this::setLCIDArray,
                this::insertLCID,
                this::removeLCID,
                this::sizeOfLCIDArray
            );
        }
    }

    /**
     * Gets array of all "LCID" elements
     */
    @Override
    public java.lang.String[] getLCIDArray() {
        return getObjectArray(PROPERTY_QNAME[23], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "LCID" element
     */
    @Override
    public java.lang.String getLCIDArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "LCID" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang> xgetLCIDList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetLCIDArray,
                this::xsetLCIDArray,
                this::insertNewLCID,
                this::removeLCID,
                this::sizeOfLCIDArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "LCID" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang[] xgetLCIDArray() {
        return xgetArray(PROPERTY_QNAME[23], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang[]::new);
    }

    /**
     * Gets (as xml) ith "LCID" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang xgetLCIDArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "LCID" element
     */
    @Override
    public int sizeOfLCIDArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets array of all "LCID" element
     */
    @Override
    public void setLCIDArray(java.lang.String[] lcidArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(lcidArray, PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets ith "LCID" element
     */
    @Override
    public void setLCIDArray(int i, java.lang.String lcid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(lcid);
        }
    }

    /**
     * Sets (as xml) array of all "LCID" element
     */
    @Override
    public void xsetLCIDArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang[]lcidArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(lcidArray, PROPERTY_QNAME[23]);
        }
    }

    /**
     * Sets (as xml) ith "LCID" element
     */
    @Override
    public void xsetLCIDArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang lcid) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().find_element_user(PROPERTY_QNAME[23], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(lcid);
        }
    }

    /**
     * Inserts the value as the ith "LCID" element
     */
    @Override
    public void insertLCID(int i, java.lang.String lcid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            target.setStringValue(lcid);
        }
    }

    /**
     * Appends the value as the last "LCID" element
     */
    @Override
    public void addLCID(java.lang.String lcid) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[23]);
            target.setStringValue(lcid);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "LCID" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang insertNewLCID(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().insert_element_user(PROPERTY_QNAME[23], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "LCID" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang addNewLCID() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STLang)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Removes the ith "LCID" element
     */
    @Override
    public void removeLCID(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], i);
        }
    }

    /**
     * Gets a List of "Medium" elements
     */
    @Override
    public java.util.List<java.lang.String> getMediumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getMediumArray,
                this::setMediumArray,
                this::insertMedium,
                this::removeMedium,
                this::sizeOfMediumArray
            );
        }
    }

    /**
     * Gets array of all "Medium" elements
     */
    @Override
    public java.lang.String[] getMediumArray() {
        return getObjectArray(PROPERTY_QNAME[24], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Medium" element
     */
    @Override
    public java.lang.String getMediumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Medium" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetMediumList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetMediumArray,
                this::xsetMediumArray,
                this::insertNewMedium,
                this::removeMedium,
                this::sizeOfMediumArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Medium" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetMediumArray() {
        return xgetArray(PROPERTY_QNAME[24], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Medium" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetMediumArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Medium" element
     */
    @Override
    public int sizeOfMediumArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets array of all "Medium" element
     */
    @Override
    public void setMediumArray(java.lang.String[] mediumArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(mediumArray, PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets ith "Medium" element
     */
    @Override
    public void setMediumArray(int i, java.lang.String medium) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(medium);
        }
    }

    /**
     * Sets (as xml) array of all "Medium" element
     */
    @Override
    public void xsetMediumArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]mediumArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(mediumArray, PROPERTY_QNAME[24]);
        }
    }

    /**
     * Sets (as xml) ith "Medium" element
     */
    @Override
    public void xsetMediumArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString medium) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[24], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(medium);
        }
    }

    /**
     * Inserts the value as the ith "Medium" element
     */
    @Override
    public void insertMedium(int i, java.lang.String medium) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            target.setStringValue(medium);
        }
    }

    /**
     * Appends the value as the last "Medium" element
     */
    @Override
    public void addMedium(java.lang.String medium) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[24]);
            target.setStringValue(medium);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Medium" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewMedium(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[24], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Medium" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewMedium() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Removes the ith "Medium" element
     */
    @Override
    public void removeMedium(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], i);
        }
    }

    /**
     * Gets a List of "Month" elements
     */
    @Override
    public java.util.List<java.lang.String> getMonthList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getMonthArray,
                this::setMonthArray,
                this::insertMonth,
                this::removeMonth,
                this::sizeOfMonthArray
            );
        }
    }

    /**
     * Gets array of all "Month" elements
     */
    @Override
    public java.lang.String[] getMonthArray() {
        return getObjectArray(PROPERTY_QNAME[25], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Month" element
     */
    @Override
    public java.lang.String getMonthArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Month" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetMonthList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetMonthArray,
                this::xsetMonthArray,
                this::insertNewMonth,
                this::removeMonth,
                this::sizeOfMonthArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Month" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetMonthArray() {
        return xgetArray(PROPERTY_QNAME[25], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Month" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetMonthArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Month" element
     */
    @Override
    public int sizeOfMonthArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets array of all "Month" element
     */
    @Override
    public void setMonthArray(java.lang.String[] monthArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(monthArray, PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets ith "Month" element
     */
    @Override
    public void setMonthArray(int i, java.lang.String month) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(month);
        }
    }

    /**
     * Sets (as xml) array of all "Month" element
     */
    @Override
    public void xsetMonthArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]monthArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(monthArray, PROPERTY_QNAME[25]);
        }
    }

    /**
     * Sets (as xml) ith "Month" element
     */
    @Override
    public void xsetMonthArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString month) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[25], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(month);
        }
    }

    /**
     * Inserts the value as the ith "Month" element
     */
    @Override
    public void insertMonth(int i, java.lang.String month) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            target.setStringValue(month);
        }
    }

    /**
     * Appends the value as the last "Month" element
     */
    @Override
    public void addMonth(java.lang.String month) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[25]);
            target.setStringValue(month);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Month" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewMonth(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[25], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Month" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewMonth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Removes the ith "Month" element
     */
    @Override
    public void removeMonth(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], i);
        }
    }

    /**
     * Gets a List of "MonthAccessed" elements
     */
    @Override
    public java.util.List<java.lang.String> getMonthAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getMonthAccessedArray,
                this::setMonthAccessedArray,
                this::insertMonthAccessed,
                this::removeMonthAccessed,
                this::sizeOfMonthAccessedArray
            );
        }
    }

    /**
     * Gets array of all "MonthAccessed" elements
     */
    @Override
    public java.lang.String[] getMonthAccessedArray() {
        return getObjectArray(PROPERTY_QNAME[26], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "MonthAccessed" element
     */
    @Override
    public java.lang.String getMonthAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "MonthAccessed" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetMonthAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetMonthAccessedArray,
                this::xsetMonthAccessedArray,
                this::insertNewMonthAccessed,
                this::removeMonthAccessed,
                this::sizeOfMonthAccessedArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "MonthAccessed" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetMonthAccessedArray() {
        return xgetArray(PROPERTY_QNAME[26], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "MonthAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetMonthAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "MonthAccessed" element
     */
    @Override
    public int sizeOfMonthAccessedArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets array of all "MonthAccessed" element
     */
    @Override
    public void setMonthAccessedArray(java.lang.String[] monthAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(monthAccessedArray, PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets ith "MonthAccessed" element
     */
    @Override
    public void setMonthAccessedArray(int i, java.lang.String monthAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(monthAccessed);
        }
    }

    /**
     * Sets (as xml) array of all "MonthAccessed" element
     */
    @Override
    public void xsetMonthAccessedArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]monthAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(monthAccessedArray, PROPERTY_QNAME[26]);
        }
    }

    /**
     * Sets (as xml) ith "MonthAccessed" element
     */
    @Override
    public void xsetMonthAccessedArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString monthAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[26], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(monthAccessed);
        }
    }

    /**
     * Inserts the value as the ith "MonthAccessed" element
     */
    @Override
    public void insertMonthAccessed(int i, java.lang.String monthAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            target.setStringValue(monthAccessed);
        }
    }

    /**
     * Appends the value as the last "MonthAccessed" element
     */
    @Override
    public void addMonthAccessed(java.lang.String monthAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[26]);
            target.setStringValue(monthAccessed);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "MonthAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewMonthAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[26], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "MonthAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewMonthAccessed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Removes the ith "MonthAccessed" element
     */
    @Override
    public void removeMonthAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], i);
        }
    }

    /**
     * Gets a List of "NumberVolumes" elements
     */
    @Override
    public java.util.List<java.lang.String> getNumberVolumesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getNumberVolumesArray,
                this::setNumberVolumesArray,
                this::insertNumberVolumes,
                this::removeNumberVolumes,
                this::sizeOfNumberVolumesArray
            );
        }
    }

    /**
     * Gets array of all "NumberVolumes" elements
     */
    @Override
    public java.lang.String[] getNumberVolumesArray() {
        return getObjectArray(PROPERTY_QNAME[27], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "NumberVolumes" element
     */
    @Override
    public java.lang.String getNumberVolumesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "NumberVolumes" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetNumberVolumesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetNumberVolumesArray,
                this::xsetNumberVolumesArray,
                this::insertNewNumberVolumes,
                this::removeNumberVolumes,
                this::sizeOfNumberVolumesArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "NumberVolumes" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetNumberVolumesArray() {
        return xgetArray(PROPERTY_QNAME[27], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "NumberVolumes" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetNumberVolumesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "NumberVolumes" element
     */
    @Override
    public int sizeOfNumberVolumesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets array of all "NumberVolumes" element
     */
    @Override
    public void setNumberVolumesArray(java.lang.String[] numberVolumesArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(numberVolumesArray, PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets ith "NumberVolumes" element
     */
    @Override
    public void setNumberVolumesArray(int i, java.lang.String numberVolumes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(numberVolumes);
        }
    }

    /**
     * Sets (as xml) array of all "NumberVolumes" element
     */
    @Override
    public void xsetNumberVolumesArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]numberVolumesArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(numberVolumesArray, PROPERTY_QNAME[27]);
        }
    }

    /**
     * Sets (as xml) ith "NumberVolumes" element
     */
    @Override
    public void xsetNumberVolumesArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString numberVolumes) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[27], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(numberVolumes);
        }
    }

    /**
     * Inserts the value as the ith "NumberVolumes" element
     */
    @Override
    public void insertNumberVolumes(int i, java.lang.String numberVolumes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            target.setStringValue(numberVolumes);
        }
    }

    /**
     * Appends the value as the last "NumberVolumes" element
     */
    @Override
    public void addNumberVolumes(java.lang.String numberVolumes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[27]);
            target.setStringValue(numberVolumes);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "NumberVolumes" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewNumberVolumes(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[27], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "NumberVolumes" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewNumberVolumes() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Removes the ith "NumberVolumes" element
     */
    @Override
    public void removeNumberVolumes(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], i);
        }
    }

    /**
     * Gets a List of "Pages" elements
     */
    @Override
    public java.util.List<java.lang.String> getPagesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getPagesArray,
                this::setPagesArray,
                this::insertPages,
                this::removePages,
                this::sizeOfPagesArray
            );
        }
    }

    /**
     * Gets array of all "Pages" elements
     */
    @Override
    public java.lang.String[] getPagesArray() {
        return getObjectArray(PROPERTY_QNAME[28], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Pages" element
     */
    @Override
    public java.lang.String getPagesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Pages" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetPagesList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetPagesArray,
                this::xsetPagesArray,
                this::insertNewPages,
                this::removePages,
                this::sizeOfPagesArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Pages" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetPagesArray() {
        return xgetArray(PROPERTY_QNAME[28], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Pages" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPagesArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Pages" element
     */
    @Override
    public int sizeOfPagesArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets array of all "Pages" element
     */
    @Override
    public void setPagesArray(java.lang.String[] pagesArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(pagesArray, PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets ith "Pages" element
     */
    @Override
    public void setPagesArray(int i, java.lang.String pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(pages);
        }
    }

    /**
     * Sets (as xml) array of all "Pages" element
     */
    @Override
    public void xsetPagesArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]pagesArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(pagesArray, PROPERTY_QNAME[28]);
        }
    }

    /**
     * Sets (as xml) ith "Pages" element
     */
    @Override
    public void xsetPagesArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[28], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(pages);
        }
    }

    /**
     * Inserts the value as the ith "Pages" element
     */
    @Override
    public void insertPages(int i, java.lang.String pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[28], i);
            target.setStringValue(pages);
        }
    }

    /**
     * Appends the value as the last "Pages" element
     */
    @Override
    public void addPages(java.lang.String pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[28]);
            target.setStringValue(pages);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Pages" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewPages(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[28], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Pages" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Removes the ith "Pages" element
     */
    @Override
    public void removePages(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], i);
        }
    }

    /**
     * Gets a List of "PatentNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getPatentNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getPatentNumberArray,
                this::setPatentNumberArray,
                this::insertPatentNumber,
                this::removePatentNumber,
                this::sizeOfPatentNumberArray
            );
        }
    }

    /**
     * Gets array of all "PatentNumber" elements
     */
    @Override
    public java.lang.String[] getPatentNumberArray() {
        return getObjectArray(PROPERTY_QNAME[29], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "PatentNumber" element
     */
    @Override
    public java.lang.String getPatentNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "PatentNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetPatentNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetPatentNumberArray,
                this::xsetPatentNumberArray,
                this::insertNewPatentNumber,
                this::removePatentNumber,
                this::sizeOfPatentNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "PatentNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetPatentNumberArray() {
        return xgetArray(PROPERTY_QNAME[29], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "PatentNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPatentNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "PatentNumber" element
     */
    @Override
    public int sizeOfPatentNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets array of all "PatentNumber" element
     */
    @Override
    public void setPatentNumberArray(java.lang.String[] patentNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(patentNumberArray, PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets ith "PatentNumber" element
     */
    @Override
    public void setPatentNumberArray(int i, java.lang.String patentNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(patentNumber);
        }
    }

    /**
     * Sets (as xml) array of all "PatentNumber" element
     */
    @Override
    public void xsetPatentNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]patentNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(patentNumberArray, PROPERTY_QNAME[29]);
        }
    }

    /**
     * Sets (as xml) ith "PatentNumber" element
     */
    @Override
    public void xsetPatentNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString patentNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[29], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(patentNumber);
        }
    }

    /**
     * Inserts the value as the ith "PatentNumber" element
     */
    @Override
    public void insertPatentNumber(int i, java.lang.String patentNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[29], i);
            target.setStringValue(patentNumber);
        }
    }

    /**
     * Appends the value as the last "PatentNumber" element
     */
    @Override
    public void addPatentNumber(java.lang.String patentNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[29]);
            target.setStringValue(patentNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "PatentNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewPatentNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[29], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "PatentNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewPatentNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Removes the ith "PatentNumber" element
     */
    @Override
    public void removePatentNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], i);
        }
    }

    /**
     * Gets a List of "PeriodicalTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getPeriodicalTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getPeriodicalTitleArray,
                this::setPeriodicalTitleArray,
                this::insertPeriodicalTitle,
                this::removePeriodicalTitle,
                this::sizeOfPeriodicalTitleArray
            );
        }
    }

    /**
     * Gets array of all "PeriodicalTitle" elements
     */
    @Override
    public java.lang.String[] getPeriodicalTitleArray() {
        return getObjectArray(PROPERTY_QNAME[30], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "PeriodicalTitle" element
     */
    @Override
    public java.lang.String getPeriodicalTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "PeriodicalTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetPeriodicalTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetPeriodicalTitleArray,
                this::xsetPeriodicalTitleArray,
                this::insertNewPeriodicalTitle,
                this::removePeriodicalTitle,
                this::sizeOfPeriodicalTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "PeriodicalTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetPeriodicalTitleArray() {
        return xgetArray(PROPERTY_QNAME[30], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "PeriodicalTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPeriodicalTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "PeriodicalTitle" element
     */
    @Override
    public int sizeOfPeriodicalTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets array of all "PeriodicalTitle" element
     */
    @Override
    public void setPeriodicalTitleArray(java.lang.String[] periodicalTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(periodicalTitleArray, PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets ith "PeriodicalTitle" element
     */
    @Override
    public void setPeriodicalTitleArray(int i, java.lang.String periodicalTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(periodicalTitle);
        }
    }

    /**
     * Sets (as xml) array of all "PeriodicalTitle" element
     */
    @Override
    public void xsetPeriodicalTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]periodicalTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(periodicalTitleArray, PROPERTY_QNAME[30]);
        }
    }

    /**
     * Sets (as xml) ith "PeriodicalTitle" element
     */
    @Override
    public void xsetPeriodicalTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString periodicalTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[30], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(periodicalTitle);
        }
    }

    /**
     * Inserts the value as the ith "PeriodicalTitle" element
     */
    @Override
    public void insertPeriodicalTitle(int i, java.lang.String periodicalTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[30], i);
            target.setStringValue(periodicalTitle);
        }
    }

    /**
     * Appends the value as the last "PeriodicalTitle" element
     */
    @Override
    public void addPeriodicalTitle(java.lang.String periodicalTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[30]);
            target.setStringValue(periodicalTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "PeriodicalTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewPeriodicalTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[30], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "PeriodicalTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewPeriodicalTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Removes the ith "PeriodicalTitle" element
     */
    @Override
    public void removePeriodicalTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], i);
        }
    }

    /**
     * Gets a List of "ProductionCompany" elements
     */
    @Override
    public java.util.List<java.lang.String> getProductionCompanyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getProductionCompanyArray,
                this::setProductionCompanyArray,
                this::insertProductionCompany,
                this::removeProductionCompany,
                this::sizeOfProductionCompanyArray
            );
        }
    }

    /**
     * Gets array of all "ProductionCompany" elements
     */
    @Override
    public java.lang.String[] getProductionCompanyArray() {
        return getObjectArray(PROPERTY_QNAME[31], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ProductionCompany" element
     */
    @Override
    public java.lang.String getProductionCompanyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ProductionCompany" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetProductionCompanyList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetProductionCompanyArray,
                this::xsetProductionCompanyArray,
                this::insertNewProductionCompany,
                this::removeProductionCompany,
                this::sizeOfProductionCompanyArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ProductionCompany" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetProductionCompanyArray() {
        return xgetArray(PROPERTY_QNAME[31], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "ProductionCompany" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetProductionCompanyArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ProductionCompany" element
     */
    @Override
    public int sizeOfProductionCompanyArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets array of all "ProductionCompany" element
     */
    @Override
    public void setProductionCompanyArray(java.lang.String[] productionCompanyArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(productionCompanyArray, PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets ith "ProductionCompany" element
     */
    @Override
    public void setProductionCompanyArray(int i, java.lang.String productionCompany) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(productionCompany);
        }
    }

    /**
     * Sets (as xml) array of all "ProductionCompany" element
     */
    @Override
    public void xsetProductionCompanyArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]productionCompanyArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(productionCompanyArray, PROPERTY_QNAME[31]);
        }
    }

    /**
     * Sets (as xml) ith "ProductionCompany" element
     */
    @Override
    public void xsetProductionCompanyArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString productionCompany) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[31], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(productionCompany);
        }
    }

    /**
     * Inserts the value as the ith "ProductionCompany" element
     */
    @Override
    public void insertProductionCompany(int i, java.lang.String productionCompany) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[31], i);
            target.setStringValue(productionCompany);
        }
    }

    /**
     * Appends the value as the last "ProductionCompany" element
     */
    @Override
    public void addProductionCompany(java.lang.String productionCompany) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[31]);
            target.setStringValue(productionCompany);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ProductionCompany" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewProductionCompany(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[31], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ProductionCompany" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewProductionCompany() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Removes the ith "ProductionCompany" element
     */
    @Override
    public void removeProductionCompany(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], i);
        }
    }

    /**
     * Gets a List of "PublicationTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getPublicationTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getPublicationTitleArray,
                this::setPublicationTitleArray,
                this::insertPublicationTitle,
                this::removePublicationTitle,
                this::sizeOfPublicationTitleArray
            );
        }
    }

    /**
     * Gets array of all "PublicationTitle" elements
     */
    @Override
    public java.lang.String[] getPublicationTitleArray() {
        return getObjectArray(PROPERTY_QNAME[32], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "PublicationTitle" element
     */
    @Override
    public java.lang.String getPublicationTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "PublicationTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetPublicationTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetPublicationTitleArray,
                this::xsetPublicationTitleArray,
                this::insertNewPublicationTitle,
                this::removePublicationTitle,
                this::sizeOfPublicationTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "PublicationTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetPublicationTitleArray() {
        return xgetArray(PROPERTY_QNAME[32], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "PublicationTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPublicationTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "PublicationTitle" element
     */
    @Override
    public int sizeOfPublicationTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets array of all "PublicationTitle" element
     */
    @Override
    public void setPublicationTitleArray(java.lang.String[] publicationTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(publicationTitleArray, PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets ith "PublicationTitle" element
     */
    @Override
    public void setPublicationTitleArray(int i, java.lang.String publicationTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(publicationTitle);
        }
    }

    /**
     * Sets (as xml) array of all "PublicationTitle" element
     */
    @Override
    public void xsetPublicationTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]publicationTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(publicationTitleArray, PROPERTY_QNAME[32]);
        }
    }

    /**
     * Sets (as xml) ith "PublicationTitle" element
     */
    @Override
    public void xsetPublicationTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString publicationTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[32], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(publicationTitle);
        }
    }

    /**
     * Inserts the value as the ith "PublicationTitle" element
     */
    @Override
    public void insertPublicationTitle(int i, java.lang.String publicationTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            target.setStringValue(publicationTitle);
        }
    }

    /**
     * Appends the value as the last "PublicationTitle" element
     */
    @Override
    public void addPublicationTitle(java.lang.String publicationTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[32]);
            target.setStringValue(publicationTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "PublicationTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewPublicationTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[32], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "PublicationTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewPublicationTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Removes the ith "PublicationTitle" element
     */
    @Override
    public void removePublicationTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], i);
        }
    }

    /**
     * Gets a List of "Publisher" elements
     */
    @Override
    public java.util.List<java.lang.String> getPublisherList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getPublisherArray,
                this::setPublisherArray,
                this::insertPublisher,
                this::removePublisher,
                this::sizeOfPublisherArray
            );
        }
    }

    /**
     * Gets array of all "Publisher" elements
     */
    @Override
    public java.lang.String[] getPublisherArray() {
        return getObjectArray(PROPERTY_QNAME[33], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Publisher" element
     */
    @Override
    public java.lang.String getPublisherArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Publisher" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetPublisherList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetPublisherArray,
                this::xsetPublisherArray,
                this::insertNewPublisher,
                this::removePublisher,
                this::sizeOfPublisherArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Publisher" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetPublisherArray() {
        return xgetArray(PROPERTY_QNAME[33], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Publisher" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetPublisherArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Publisher" element
     */
    @Override
    public int sizeOfPublisherArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets array of all "Publisher" element
     */
    @Override
    public void setPublisherArray(java.lang.String[] publisherArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(publisherArray, PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets ith "Publisher" element
     */
    @Override
    public void setPublisherArray(int i, java.lang.String publisher) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(publisher);
        }
    }

    /**
     * Sets (as xml) array of all "Publisher" element
     */
    @Override
    public void xsetPublisherArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]publisherArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(publisherArray, PROPERTY_QNAME[33]);
        }
    }

    /**
     * Sets (as xml) ith "Publisher" element
     */
    @Override
    public void xsetPublisherArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString publisher) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[33], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(publisher);
        }
    }

    /**
     * Inserts the value as the ith "Publisher" element
     */
    @Override
    public void insertPublisher(int i, java.lang.String publisher) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            target.setStringValue(publisher);
        }
    }

    /**
     * Appends the value as the last "Publisher" element
     */
    @Override
    public void addPublisher(java.lang.String publisher) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[33]);
            target.setStringValue(publisher);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Publisher" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewPublisher(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[33], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Publisher" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewPublisher() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Removes the ith "Publisher" element
     */
    @Override
    public void removePublisher(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], i);
        }
    }

    /**
     * Gets a List of "RecordingNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getRecordingNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getRecordingNumberArray,
                this::setRecordingNumberArray,
                this::insertRecordingNumber,
                this::removeRecordingNumber,
                this::sizeOfRecordingNumberArray
            );
        }
    }

    /**
     * Gets array of all "RecordingNumber" elements
     */
    @Override
    public java.lang.String[] getRecordingNumberArray() {
        return getObjectArray(PROPERTY_QNAME[34], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "RecordingNumber" element
     */
    @Override
    public java.lang.String getRecordingNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "RecordingNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetRecordingNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetRecordingNumberArray,
                this::xsetRecordingNumberArray,
                this::insertNewRecordingNumber,
                this::removeRecordingNumber,
                this::sizeOfRecordingNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "RecordingNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetRecordingNumberArray() {
        return xgetArray(PROPERTY_QNAME[34], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "RecordingNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetRecordingNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "RecordingNumber" element
     */
    @Override
    public int sizeOfRecordingNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets array of all "RecordingNumber" element
     */
    @Override
    public void setRecordingNumberArray(java.lang.String[] recordingNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(recordingNumberArray, PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets ith "RecordingNumber" element
     */
    @Override
    public void setRecordingNumberArray(int i, java.lang.String recordingNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(recordingNumber);
        }
    }

    /**
     * Sets (as xml) array of all "RecordingNumber" element
     */
    @Override
    public void xsetRecordingNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]recordingNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(recordingNumberArray, PROPERTY_QNAME[34]);
        }
    }

    /**
     * Sets (as xml) ith "RecordingNumber" element
     */
    @Override
    public void xsetRecordingNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString recordingNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[34], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(recordingNumber);
        }
    }

    /**
     * Inserts the value as the ith "RecordingNumber" element
     */
    @Override
    public void insertRecordingNumber(int i, java.lang.String recordingNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            target.setStringValue(recordingNumber);
        }
    }

    /**
     * Appends the value as the last "RecordingNumber" element
     */
    @Override
    public void addRecordingNumber(java.lang.String recordingNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[34]);
            target.setStringValue(recordingNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RecordingNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewRecordingNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[34], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "RecordingNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewRecordingNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Removes the ith "RecordingNumber" element
     */
    @Override
    public void removeRecordingNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], i);
        }
    }

    /**
     * Gets a List of "RefOrder" elements
     */
    @Override
    public java.util.List<java.lang.String> getRefOrderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getRefOrderArray,
                this::setRefOrderArray,
                this::insertRefOrder,
                this::removeRefOrder,
                this::sizeOfRefOrderArray
            );
        }
    }

    /**
     * Gets array of all "RefOrder" elements
     */
    @Override
    public java.lang.String[] getRefOrderArray() {
        return getObjectArray(PROPERTY_QNAME[35], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "RefOrder" element
     */
    @Override
    public java.lang.String getRefOrderArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "RefOrder" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetRefOrderList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetRefOrderArray,
                this::xsetRefOrderArray,
                this::insertNewRefOrder,
                this::removeRefOrder,
                this::sizeOfRefOrderArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "RefOrder" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetRefOrderArray() {
        return xgetArray(PROPERTY_QNAME[35], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "RefOrder" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetRefOrderArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "RefOrder" element
     */
    @Override
    public int sizeOfRefOrderArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets array of all "RefOrder" element
     */
    @Override
    public void setRefOrderArray(java.lang.String[] refOrderArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(refOrderArray, PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets ith "RefOrder" element
     */
    @Override
    public void setRefOrderArray(int i, java.lang.String refOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(refOrder);
        }
    }

    /**
     * Sets (as xml) array of all "RefOrder" element
     */
    @Override
    public void xsetRefOrderArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]refOrderArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(refOrderArray, PROPERTY_QNAME[35]);
        }
    }

    /**
     * Sets (as xml) ith "RefOrder" element
     */
    @Override
    public void xsetRefOrderArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString refOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[35], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(refOrder);
        }
    }

    /**
     * Inserts the value as the ith "RefOrder" element
     */
    @Override
    public void insertRefOrder(int i, java.lang.String refOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[35], i);
            target.setStringValue(refOrder);
        }
    }

    /**
     * Appends the value as the last "RefOrder" element
     */
    @Override
    public void addRefOrder(java.lang.String refOrder) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[35]);
            target.setStringValue(refOrder);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "RefOrder" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewRefOrder(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[35], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "RefOrder" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewRefOrder() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Removes the ith "RefOrder" element
     */
    @Override
    public void removeRefOrder(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], i);
        }
    }

    /**
     * Gets a List of "Reporter" elements
     */
    @Override
    public java.util.List<java.lang.String> getReporterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getReporterArray,
                this::setReporterArray,
                this::insertReporter,
                this::removeReporter,
                this::sizeOfReporterArray
            );
        }
    }

    /**
     * Gets array of all "Reporter" elements
     */
    @Override
    public java.lang.String[] getReporterArray() {
        return getObjectArray(PROPERTY_QNAME[36], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Reporter" element
     */
    @Override
    public java.lang.String getReporterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Reporter" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetReporterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetReporterArray,
                this::xsetReporterArray,
                this::insertNewReporter,
                this::removeReporter,
                this::sizeOfReporterArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Reporter" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetReporterArray() {
        return xgetArray(PROPERTY_QNAME[36], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Reporter" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetReporterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Reporter" element
     */
    @Override
    public int sizeOfReporterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]);
        }
    }

    /**
     * Sets array of all "Reporter" element
     */
    @Override
    public void setReporterArray(java.lang.String[] reporterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(reporterArray, PROPERTY_QNAME[36]);
        }
    }

    /**
     * Sets ith "Reporter" element
     */
    @Override
    public void setReporterArray(int i, java.lang.String reporter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(reporter);
        }
    }

    /**
     * Sets (as xml) array of all "Reporter" element
     */
    @Override
    public void xsetReporterArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]reporterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(reporterArray, PROPERTY_QNAME[36]);
        }
    }

    /**
     * Sets (as xml) ith "Reporter" element
     */
    @Override
    public void xsetReporterArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString reporter) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[36], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(reporter);
        }
    }

    /**
     * Inserts the value as the ith "Reporter" element
     */
    @Override
    public void insertReporter(int i, java.lang.String reporter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[36], i);
            target.setStringValue(reporter);
        }
    }

    /**
     * Appends the value as the last "Reporter" element
     */
    @Override
    public void addReporter(java.lang.String reporter) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[36]);
            target.setStringValue(reporter);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Reporter" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewReporter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[36], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Reporter" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewReporter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Removes the ith "Reporter" element
     */
    @Override
    public void removeReporter(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], i);
        }
    }

    /**
     * Gets a List of "SourceType" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum> getSourceTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getSourceTypeArray,
                this::setSourceTypeArray,
                this::insertSourceType,
                this::removeSourceType,
                this::sizeOfSourceTypeArray
            );
        }
    }

    /**
     * Gets array of all "SourceType" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum[] getSourceTypeArray() {
        return getEnumArray(PROPERTY_QNAME[37], org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum[]::new);
    }

    /**
     * Gets ith "SourceType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum getSourceTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return (org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum)target.getEnumValue();
        }
    }

    /**
     * Gets (as xml) a List of "SourceType" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType> xgetSourceTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetSourceTypeArray,
                this::xsetSourceTypeArray,
                this::insertNewSourceType,
                this::removeSourceType,
                this::sizeOfSourceTypeArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "SourceType" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType[] xgetSourceTypeArray() {
        return xgetArray(PROPERTY_QNAME[37], org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType[]::new);
    }

    /**
     * Gets (as xml) ith "SourceType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType xgetSourceTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "SourceType" element
     */
    @Override
    public int sizeOfSourceTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]);
        }
    }

    /**
     * Sets array of all "SourceType" element
     */
    @Override
    public void setSourceTypeArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum[] sourceTypeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(sourceTypeArray, PROPERTY_QNAME[37]);
        }
    }

    /**
     * Sets ith "SourceType" element
     */
    @Override
    public void setSourceTypeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setEnumValue(sourceType);
        }
    }

    /**
     * Sets (as xml) array of all "SourceType" element
     */
    @Override
    public void xsetSourceTypeArray(org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType[]sourceTypeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(sourceTypeArray, PROPERTY_QNAME[37]);
        }
    }

    /**
     * Sets (as xml) ith "SourceType" element
     */
    @Override
    public void xsetSourceTypeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType)get_store().find_element_user(PROPERTY_QNAME[37], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(sourceType);
        }
    }

    /**
     * Inserts the value as the ith "SourceType" element
     */
    @Override
    public void insertSourceType(int i, org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[37], i);
            target.setEnumValue(sourceType);
        }
    }

    /**
     * Appends the value as the last "SourceType" element
     */
    @Override
    public void addSourceType(org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType.Enum sourceType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[37]);
            target.setEnumValue(sourceType);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "SourceType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType insertNewSourceType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType)get_store().insert_element_user(PROPERTY_QNAME[37], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "SourceType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType addNewSourceType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.bibliography.STSourceType)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Removes the ith "SourceType" element
     */
    @Override
    public void removeSourceType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], i);
        }
    }

    /**
     * Gets a List of "ShortTitle" elements
     */
    @Override
    public java.util.List<java.lang.String> getShortTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getShortTitleArray,
                this::setShortTitleArray,
                this::insertShortTitle,
                this::removeShortTitle,
                this::sizeOfShortTitleArray
            );
        }
    }

    /**
     * Gets array of all "ShortTitle" elements
     */
    @Override
    public java.lang.String[] getShortTitleArray() {
        return getObjectArray(PROPERTY_QNAME[38], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ShortTitle" element
     */
    @Override
    public java.lang.String getShortTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ShortTitle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetShortTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetShortTitleArray,
                this::xsetShortTitleArray,
                this::insertNewShortTitle,
                this::removeShortTitle,
                this::sizeOfShortTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ShortTitle" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetShortTitleArray() {
        return xgetArray(PROPERTY_QNAME[38], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "ShortTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetShortTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ShortTitle" element
     */
    @Override
    public int sizeOfShortTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[38]);
        }
    }

    /**
     * Sets array of all "ShortTitle" element
     */
    @Override
    public void setShortTitleArray(java.lang.String[] shortTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(shortTitleArray, PROPERTY_QNAME[38]);
        }
    }

    /**
     * Sets ith "ShortTitle" element
     */
    @Override
    public void setShortTitleArray(int i, java.lang.String shortTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(shortTitle);
        }
    }

    /**
     * Sets (as xml) array of all "ShortTitle" element
     */
    @Override
    public void xsetShortTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]shortTitleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(shortTitleArray, PROPERTY_QNAME[38]);
        }
    }

    /**
     * Sets (as xml) ith "ShortTitle" element
     */
    @Override
    public void xsetShortTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString shortTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[38], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(shortTitle);
        }
    }

    /**
     * Inserts the value as the ith "ShortTitle" element
     */
    @Override
    public void insertShortTitle(int i, java.lang.String shortTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[38], i);
            target.setStringValue(shortTitle);
        }
    }

    /**
     * Appends the value as the last "ShortTitle" element
     */
    @Override
    public void addShortTitle(java.lang.String shortTitle) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[38]);
            target.setStringValue(shortTitle);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ShortTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewShortTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[38], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ShortTitle" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewShortTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * Removes the ith "ShortTitle" element
     */
    @Override
    public void removeShortTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[38], i);
        }
    }

    /**
     * Gets a List of "StandardNumber" elements
     */
    @Override
    public java.util.List<java.lang.String> getStandardNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getStandardNumberArray,
                this::setStandardNumberArray,
                this::insertStandardNumber,
                this::removeStandardNumber,
                this::sizeOfStandardNumberArray
            );
        }
    }

    /**
     * Gets array of all "StandardNumber" elements
     */
    @Override
    public java.lang.String[] getStandardNumberArray() {
        return getObjectArray(PROPERTY_QNAME[39], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "StandardNumber" element
     */
    @Override
    public java.lang.String getStandardNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "StandardNumber" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetStandardNumberList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetStandardNumberArray,
                this::xsetStandardNumberArray,
                this::insertNewStandardNumber,
                this::removeStandardNumber,
                this::sizeOfStandardNumberArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "StandardNumber" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetStandardNumberArray() {
        return xgetArray(PROPERTY_QNAME[39], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "StandardNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStandardNumberArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "StandardNumber" element
     */
    @Override
    public int sizeOfStandardNumberArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[39]);
        }
    }

    /**
     * Sets array of all "StandardNumber" element
     */
    @Override
    public void setStandardNumberArray(java.lang.String[] standardNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(standardNumberArray, PROPERTY_QNAME[39]);
        }
    }

    /**
     * Sets ith "StandardNumber" element
     */
    @Override
    public void setStandardNumberArray(int i, java.lang.String standardNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(standardNumber);
        }
    }

    /**
     * Sets (as xml) array of all "StandardNumber" element
     */
    @Override
    public void xsetStandardNumberArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]standardNumberArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(standardNumberArray, PROPERTY_QNAME[39]);
        }
    }

    /**
     * Sets (as xml) ith "StandardNumber" element
     */
    @Override
    public void xsetStandardNumberArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString standardNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[39], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(standardNumber);
        }
    }

    /**
     * Inserts the value as the ith "StandardNumber" element
     */
    @Override
    public void insertStandardNumber(int i, java.lang.String standardNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[39], i);
            target.setStringValue(standardNumber);
        }
    }

    /**
     * Appends the value as the last "StandardNumber" element
     */
    @Override
    public void addStandardNumber(java.lang.String standardNumber) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[39]);
            target.setStringValue(standardNumber);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "StandardNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewStandardNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[39], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "StandardNumber" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewStandardNumber() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * Removes the ith "StandardNumber" element
     */
    @Override
    public void removeStandardNumber(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[39], i);
        }
    }

    /**
     * Gets a List of "StateProvince" elements
     */
    @Override
    public java.util.List<java.lang.String> getStateProvinceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getStateProvinceArray,
                this::setStateProvinceArray,
                this::insertStateProvince,
                this::removeStateProvince,
                this::sizeOfStateProvinceArray
            );
        }
    }

    /**
     * Gets array of all "StateProvince" elements
     */
    @Override
    public java.lang.String[] getStateProvinceArray() {
        return getObjectArray(PROPERTY_QNAME[40], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "StateProvince" element
     */
    @Override
    public java.lang.String getStateProvinceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "StateProvince" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetStateProvinceList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetStateProvinceArray,
                this::xsetStateProvinceArray,
                this::insertNewStateProvince,
                this::removeStateProvince,
                this::sizeOfStateProvinceArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "StateProvince" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetStateProvinceArray() {
        return xgetArray(PROPERTY_QNAME[40], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "StateProvince" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStateProvinceArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "StateProvince" element
     */
    @Override
    public int sizeOfStateProvinceArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[40]);
        }
    }

    /**
     * Sets array of all "StateProvince" element
     */
    @Override
    public void setStateProvinceArray(java.lang.String[] stateProvinceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stateProvinceArray, PROPERTY_QNAME[40]);
        }
    }

    /**
     * Sets ith "StateProvince" element
     */
    @Override
    public void setStateProvinceArray(int i, java.lang.String stateProvince) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(stateProvince);
        }
    }

    /**
     * Sets (as xml) array of all "StateProvince" element
     */
    @Override
    public void xsetStateProvinceArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]stateProvinceArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stateProvinceArray, PROPERTY_QNAME[40]);
        }
    }

    /**
     * Sets (as xml) ith "StateProvince" element
     */
    @Override
    public void xsetStateProvinceArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString stateProvince) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[40], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(stateProvince);
        }
    }

    /**
     * Inserts the value as the ith "StateProvince" element
     */
    @Override
    public void insertStateProvince(int i, java.lang.String stateProvince) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[40], i);
            target.setStringValue(stateProvince);
        }
    }

    /**
     * Appends the value as the last "StateProvince" element
     */
    @Override
    public void addStateProvince(java.lang.String stateProvince) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[40]);
            target.setStringValue(stateProvince);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "StateProvince" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewStateProvince(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[40], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "StateProvince" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewStateProvince() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * Removes the ith "StateProvince" element
     */
    @Override
    public void removeStateProvince(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[40], i);
        }
    }

    /**
     * Gets a List of "Station" elements
     */
    @Override
    public java.util.List<java.lang.String> getStationList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getStationArray,
                this::setStationArray,
                this::insertStation,
                this::removeStation,
                this::sizeOfStationArray
            );
        }
    }

    /**
     * Gets array of all "Station" elements
     */
    @Override
    public java.lang.String[] getStationArray() {
        return getObjectArray(PROPERTY_QNAME[41], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Station" element
     */
    @Override
    public java.lang.String getStationArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Station" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetStationList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetStationArray,
                this::xsetStationArray,
                this::insertNewStation,
                this::removeStation,
                this::sizeOfStationArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Station" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetStationArray() {
        return xgetArray(PROPERTY_QNAME[41], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Station" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetStationArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Station" element
     */
    @Override
    public int sizeOfStationArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[41]);
        }
    }

    /**
     * Sets array of all "Station" element
     */
    @Override
    public void setStationArray(java.lang.String[] stationArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stationArray, PROPERTY_QNAME[41]);
        }
    }

    /**
     * Sets ith "Station" element
     */
    @Override
    public void setStationArray(int i, java.lang.String station) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(station);
        }
    }

    /**
     * Sets (as xml) array of all "Station" element
     */
    @Override
    public void xsetStationArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]stationArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(stationArray, PROPERTY_QNAME[41]);
        }
    }

    /**
     * Sets (as xml) ith "Station" element
     */
    @Override
    public void xsetStationArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString station) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[41], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(station);
        }
    }

    /**
     * Inserts the value as the ith "Station" element
     */
    @Override
    public void insertStation(int i, java.lang.String station) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[41], i);
            target.setStringValue(station);
        }
    }

    /**
     * Appends the value as the last "Station" element
     */
    @Override
    public void addStation(java.lang.String station) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[41]);
            target.setStringValue(station);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Station" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewStation(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[41], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Station" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewStation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[41]);
            return target;
        }
    }

    /**
     * Removes the ith "Station" element
     */
    @Override
    public void removeStation(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[41], i);
        }
    }

    /**
     * Gets a List of "Tag" elements
     */
    @Override
    public java.util.List<java.lang.String> getTagList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getTagArray,
                this::setTagArray,
                this::insertTag,
                this::removeTag,
                this::sizeOfTagArray
            );
        }
    }

    /**
     * Gets array of all "Tag" elements
     */
    @Override
    public java.lang.String[] getTagArray() {
        return getObjectArray(PROPERTY_QNAME[42], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Tag" element
     */
    @Override
    public java.lang.String getTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Tag" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetTagList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetTagArray,
                this::xsetTagArray,
                this::insertNewTag,
                this::removeTag,
                this::sizeOfTagArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Tag" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetTagArray() {
        return xgetArray(PROPERTY_QNAME[42], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Tag" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetTagArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Tag" element
     */
    @Override
    public int sizeOfTagArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[42]);
        }
    }

    /**
     * Sets array of all "Tag" element
     */
    @Override
    public void setTagArray(java.lang.String[] tagArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(tagArray, PROPERTY_QNAME[42]);
        }
    }

    /**
     * Sets ith "Tag" element
     */
    @Override
    public void setTagArray(int i, java.lang.String tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(tag);
        }
    }

    /**
     * Sets (as xml) array of all "Tag" element
     */
    @Override
    public void xsetTagArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]tagArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(tagArray, PROPERTY_QNAME[42]);
        }
    }

    /**
     * Sets (as xml) ith "Tag" element
     */
    @Override
    public void xsetTagArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[42], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(tag);
        }
    }

    /**
     * Inserts the value as the ith "Tag" element
     */
    @Override
    public void insertTag(int i, java.lang.String tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[42], i);
            target.setStringValue(tag);
        }
    }

    /**
     * Appends the value as the last "Tag" element
     */
    @Override
    public void addTag(java.lang.String tag) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[42]);
            target.setStringValue(tag);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Tag" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[42], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Tag" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewTag() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[42]);
            return target;
        }
    }

    /**
     * Removes the ith "Tag" element
     */
    @Override
    public void removeTag(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[42], i);
        }
    }

    /**
     * Gets a List of "Theater" elements
     */
    @Override
    public java.util.List<java.lang.String> getTheaterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getTheaterArray,
                this::setTheaterArray,
                this::insertTheater,
                this::removeTheater,
                this::sizeOfTheaterArray
            );
        }
    }

    /**
     * Gets array of all "Theater" elements
     */
    @Override
    public java.lang.String[] getTheaterArray() {
        return getObjectArray(PROPERTY_QNAME[43], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Theater" element
     */
    @Override
    public java.lang.String getTheaterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[43], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Theater" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetTheaterList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetTheaterArray,
                this::xsetTheaterArray,
                this::insertNewTheater,
                this::removeTheater,
                this::sizeOfTheaterArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Theater" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetTheaterArray() {
        return xgetArray(PROPERTY_QNAME[43], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Theater" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetTheaterArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[43], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Theater" element
     */
    @Override
    public int sizeOfTheaterArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[43]);
        }
    }

    /**
     * Sets array of all "Theater" element
     */
    @Override
    public void setTheaterArray(java.lang.String[] theaterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(theaterArray, PROPERTY_QNAME[43]);
        }
    }

    /**
     * Sets ith "Theater" element
     */
    @Override
    public void setTheaterArray(int i, java.lang.String theater) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[43], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(theater);
        }
    }

    /**
     * Sets (as xml) array of all "Theater" element
     */
    @Override
    public void xsetTheaterArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]theaterArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(theaterArray, PROPERTY_QNAME[43]);
        }
    }

    /**
     * Sets (as xml) ith "Theater" element
     */
    @Override
    public void xsetTheaterArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString theater) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[43], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(theater);
        }
    }

    /**
     * Inserts the value as the ith "Theater" element
     */
    @Override
    public void insertTheater(int i, java.lang.String theater) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[43], i);
            target.setStringValue(theater);
        }
    }

    /**
     * Appends the value as the last "Theater" element
     */
    @Override
    public void addTheater(java.lang.String theater) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[43]);
            target.setStringValue(theater);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Theater" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewTheater(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[43], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Theater" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewTheater() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[43]);
            return target;
        }
    }

    /**
     * Removes the ith "Theater" element
     */
    @Override
    public void removeTheater(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[43], i);
        }
    }

    /**
     * Gets a List of "ThesisType" elements
     */
    @Override
    public java.util.List<java.lang.String> getThesisTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getThesisTypeArray,
                this::setThesisTypeArray,
                this::insertThesisType,
                this::removeThesisType,
                this::sizeOfThesisTypeArray
            );
        }
    }

    /**
     * Gets array of all "ThesisType" elements
     */
    @Override
    public java.lang.String[] getThesisTypeArray() {
        return getObjectArray(PROPERTY_QNAME[44], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "ThesisType" element
     */
    @Override
    public java.lang.String getThesisTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[44], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "ThesisType" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetThesisTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetThesisTypeArray,
                this::xsetThesisTypeArray,
                this::insertNewThesisType,
                this::removeThesisType,
                this::sizeOfThesisTypeArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "ThesisType" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetThesisTypeArray() {
        return xgetArray(PROPERTY_QNAME[44], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "ThesisType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetThesisTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[44], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "ThesisType" element
     */
    @Override
    public int sizeOfThesisTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[44]);
        }
    }

    /**
     * Sets array of all "ThesisType" element
     */
    @Override
    public void setThesisTypeArray(java.lang.String[] thesisTypeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(thesisTypeArray, PROPERTY_QNAME[44]);
        }
    }

    /**
     * Sets ith "ThesisType" element
     */
    @Override
    public void setThesisTypeArray(int i, java.lang.String thesisType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[44], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(thesisType);
        }
    }

    /**
     * Sets (as xml) array of all "ThesisType" element
     */
    @Override
    public void xsetThesisTypeArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]thesisTypeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(thesisTypeArray, PROPERTY_QNAME[44]);
        }
    }

    /**
     * Sets (as xml) ith "ThesisType" element
     */
    @Override
    public void xsetThesisTypeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString thesisType) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[44], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(thesisType);
        }
    }

    /**
     * Inserts the value as the ith "ThesisType" element
     */
    @Override
    public void insertThesisType(int i, java.lang.String thesisType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[44], i);
            target.setStringValue(thesisType);
        }
    }

    /**
     * Appends the value as the last "ThesisType" element
     */
    @Override
    public void addThesisType(java.lang.String thesisType) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[44]);
            target.setStringValue(thesisType);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "ThesisType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewThesisType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[44], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "ThesisType" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewThesisType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[44]);
            return target;
        }
    }

    /**
     * Removes the ith "ThesisType" element
     */
    @Override
    public void removeThesisType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[44], i);
        }
    }

    /**
     * Gets a List of "Title" elements
     */
    @Override
    public java.util.List<java.lang.String> getTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getTitleArray,
                this::setTitleArray,
                this::insertTitle,
                this::removeTitle,
                this::sizeOfTitleArray
            );
        }
    }

    /**
     * Gets array of all "Title" elements
     */
    @Override
    public java.lang.String[] getTitleArray() {
        return getObjectArray(PROPERTY_QNAME[45], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Title" element
     */
    @Override
    public java.lang.String getTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[45], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Title" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetTitleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetTitleArray,
                this::xsetTitleArray,
                this::insertNewTitle,
                this::removeTitle,
                this::sizeOfTitleArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Title" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetTitleArray() {
        return xgetArray(PROPERTY_QNAME[45], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Title" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetTitleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[45], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Title" element
     */
    @Override
    public int sizeOfTitleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[45]);
        }
    }

    /**
     * Sets array of all "Title" element
     */
    @Override
    public void setTitleArray(java.lang.String[] titleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(titleArray, PROPERTY_QNAME[45]);
        }
    }

    /**
     * Sets ith "Title" element
     */
    @Override
    public void setTitleArray(int i, java.lang.String title) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[45], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(title);
        }
    }

    /**
     * Sets (as xml) array of all "Title" element
     */
    @Override
    public void xsetTitleArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]titleArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(titleArray, PROPERTY_QNAME[45]);
        }
    }

    /**
     * Sets (as xml) ith "Title" element
     */
    @Override
    public void xsetTitleArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString title) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[45], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(title);
        }
    }

    /**
     * Inserts the value as the ith "Title" element
     */
    @Override
    public void insertTitle(int i, java.lang.String title) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[45], i);
            target.setStringValue(title);
        }
    }

    /**
     * Appends the value as the last "Title" element
     */
    @Override
    public void addTitle(java.lang.String title) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[45]);
            target.setStringValue(title);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Title" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[45], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Title" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewTitle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[45]);
            return target;
        }
    }

    /**
     * Removes the ith "Title" element
     */
    @Override
    public void removeTitle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[45], i);
        }
    }

    /**
     * Gets a List of "Type" elements
     */
    @Override
    public java.util.List<java.lang.String> getTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getTypeArray,
                this::setTypeArray,
                this::insertType,
                this::removeType,
                this::sizeOfTypeArray
            );
        }
    }

    /**
     * Gets array of all "Type" elements
     */
    @Override
    public java.lang.String[] getTypeArray() {
        return getObjectArray(PROPERTY_QNAME[46], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Type" element
     */
    @Override
    public java.lang.String getTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[46], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Type" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetTypeArray,
                this::xsetTypeArray,
                this::insertNewType,
                this::removeType,
                this::sizeOfTypeArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Type" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetTypeArray() {
        return xgetArray(PROPERTY_QNAME[46], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Type" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[46], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Type" element
     */
    @Override
    public int sizeOfTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[46]);
        }
    }

    /**
     * Sets array of all "Type" element
     */
    @Override
    public void setTypeArray(java.lang.String[] typeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(typeArray, PROPERTY_QNAME[46]);
        }
    }

    /**
     * Sets ith "Type" element
     */
    @Override
    public void setTypeArray(int i, java.lang.String type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[46], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(type);
        }
    }

    /**
     * Sets (as xml) array of all "Type" element
     */
    @Override
    public void xsetTypeArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]typeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(typeArray, PROPERTY_QNAME[46]);
        }
    }

    /**
     * Sets (as xml) ith "Type" element
     */
    @Override
    public void xsetTypeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString type) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[46], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(type);
        }
    }

    /**
     * Inserts the value as the ith "Type" element
     */
    @Override
    public void insertType(int i, java.lang.String type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[46], i);
            target.setStringValue(type);
        }
    }

    /**
     * Appends the value as the last "Type" element
     */
    @Override
    public void addType(java.lang.String type) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[46]);
            target.setStringValue(type);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Type" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[46], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Type" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[46]);
            return target;
        }
    }

    /**
     * Removes the ith "Type" element
     */
    @Override
    public void removeType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[46], i);
        }
    }

    /**
     * Gets a List of "URL" elements
     */
    @Override
    public java.util.List<java.lang.String> getURLList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getURLArray,
                this::setURLArray,
                this::insertURL,
                this::removeURL,
                this::sizeOfURLArray
            );
        }
    }

    /**
     * Gets array of all "URL" elements
     */
    @Override
    public java.lang.String[] getURLArray() {
        return getObjectArray(PROPERTY_QNAME[47], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "URL" element
     */
    @Override
    public java.lang.String getURLArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[47], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "URL" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetURLList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetURLArray,
                this::xsetURLArray,
                this::insertNewURL,
                this::removeURL,
                this::sizeOfURLArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "URL" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetURLArray() {
        return xgetArray(PROPERTY_QNAME[47], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "URL" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetURLArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[47], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "URL" element
     */
    @Override
    public int sizeOfURLArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[47]);
        }
    }

    /**
     * Sets array of all "URL" element
     */
    @Override
    public void setURLArray(java.lang.String[] urlArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(urlArray, PROPERTY_QNAME[47]);
        }
    }

    /**
     * Sets ith "URL" element
     */
    @Override
    public void setURLArray(int i, java.lang.String url) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[47], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(url);
        }
    }

    /**
     * Sets (as xml) array of all "URL" element
     */
    @Override
    public void xsetURLArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]urlArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(urlArray, PROPERTY_QNAME[47]);
        }
    }

    /**
     * Sets (as xml) ith "URL" element
     */
    @Override
    public void xsetURLArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString url) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[47], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(url);
        }
    }

    /**
     * Inserts the value as the ith "URL" element
     */
    @Override
    public void insertURL(int i, java.lang.String url) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[47], i);
            target.setStringValue(url);
        }
    }

    /**
     * Appends the value as the last "URL" element
     */
    @Override
    public void addURL(java.lang.String url) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[47]);
            target.setStringValue(url);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "URL" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewURL(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[47], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "URL" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewURL() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[47]);
            return target;
        }
    }

    /**
     * Removes the ith "URL" element
     */
    @Override
    public void removeURL(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[47], i);
        }
    }

    /**
     * Gets a List of "Version" elements
     */
    @Override
    public java.util.List<java.lang.String> getVersionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getVersionArray,
                this::setVersionArray,
                this::insertVersion,
                this::removeVersion,
                this::sizeOfVersionArray
            );
        }
    }

    /**
     * Gets array of all "Version" elements
     */
    @Override
    public java.lang.String[] getVersionArray() {
        return getObjectArray(PROPERTY_QNAME[48], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Version" element
     */
    @Override
    public java.lang.String getVersionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[48], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Version" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetVersionList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetVersionArray,
                this::xsetVersionArray,
                this::insertNewVersion,
                this::removeVersion,
                this::sizeOfVersionArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Version" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetVersionArray() {
        return xgetArray(PROPERTY_QNAME[48], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Version" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetVersionArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[48], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Version" element
     */
    @Override
    public int sizeOfVersionArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[48]);
        }
    }

    /**
     * Sets array of all "Version" element
     */
    @Override
    public void setVersionArray(java.lang.String[] versionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(versionArray, PROPERTY_QNAME[48]);
        }
    }

    /**
     * Sets ith "Version" element
     */
    @Override
    public void setVersionArray(int i, java.lang.String version) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[48], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(version);
        }
    }

    /**
     * Sets (as xml) array of all "Version" element
     */
    @Override
    public void xsetVersionArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]versionArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(versionArray, PROPERTY_QNAME[48]);
        }
    }

    /**
     * Sets (as xml) ith "Version" element
     */
    @Override
    public void xsetVersionArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString version) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[48], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(version);
        }
    }

    /**
     * Inserts the value as the ith "Version" element
     */
    @Override
    public void insertVersion(int i, java.lang.String version) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[48], i);
            target.setStringValue(version);
        }
    }

    /**
     * Appends the value as the last "Version" element
     */
    @Override
    public void addVersion(java.lang.String version) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[48]);
            target.setStringValue(version);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Version" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewVersion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[48], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Version" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[48]);
            return target;
        }
    }

    /**
     * Removes the ith "Version" element
     */
    @Override
    public void removeVersion(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[48], i);
        }
    }

    /**
     * Gets a List of "Volume" elements
     */
    @Override
    public java.util.List<java.lang.String> getVolumeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getVolumeArray,
                this::setVolumeArray,
                this::insertVolume,
                this::removeVolume,
                this::sizeOfVolumeArray
            );
        }
    }

    /**
     * Gets array of all "Volume" elements
     */
    @Override
    public java.lang.String[] getVolumeArray() {
        return getObjectArray(PROPERTY_QNAME[49], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Volume" element
     */
    @Override
    public java.lang.String getVolumeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[49], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Volume" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetVolumeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetVolumeArray,
                this::xsetVolumeArray,
                this::insertNewVolume,
                this::removeVolume,
                this::sizeOfVolumeArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Volume" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetVolumeArray() {
        return xgetArray(PROPERTY_QNAME[49], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Volume" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetVolumeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[49], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Volume" element
     */
    @Override
    public int sizeOfVolumeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[49]);
        }
    }

    /**
     * Sets array of all "Volume" element
     */
    @Override
    public void setVolumeArray(java.lang.String[] volumeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(volumeArray, PROPERTY_QNAME[49]);
        }
    }

    /**
     * Sets ith "Volume" element
     */
    @Override
    public void setVolumeArray(int i, java.lang.String volume) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[49], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(volume);
        }
    }

    /**
     * Sets (as xml) array of all "Volume" element
     */
    @Override
    public void xsetVolumeArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]volumeArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(volumeArray, PROPERTY_QNAME[49]);
        }
    }

    /**
     * Sets (as xml) ith "Volume" element
     */
    @Override
    public void xsetVolumeArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString volume) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[49], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(volume);
        }
    }

    /**
     * Inserts the value as the ith "Volume" element
     */
    @Override
    public void insertVolume(int i, java.lang.String volume) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[49], i);
            target.setStringValue(volume);
        }
    }

    /**
     * Appends the value as the last "Volume" element
     */
    @Override
    public void addVolume(java.lang.String volume) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[49]);
            target.setStringValue(volume);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Volume" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewVolume(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[49], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Volume" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewVolume() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[49]);
            return target;
        }
    }

    /**
     * Removes the ith "Volume" element
     */
    @Override
    public void removeVolume(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[49], i);
        }
    }

    /**
     * Gets a List of "Year" elements
     */
    @Override
    public java.util.List<java.lang.String> getYearList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getYearArray,
                this::setYearArray,
                this::insertYear,
                this::removeYear,
                this::sizeOfYearArray
            );
        }
    }

    /**
     * Gets array of all "Year" elements
     */
    @Override
    public java.lang.String[] getYearArray() {
        return getObjectArray(PROPERTY_QNAME[50], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "Year" element
     */
    @Override
    public java.lang.String getYearArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[50], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "Year" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetYearList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetYearArray,
                this::xsetYearArray,
                this::insertNewYear,
                this::removeYear,
                this::sizeOfYearArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "Year" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetYearArray() {
        return xgetArray(PROPERTY_QNAME[50], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "Year" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetYearArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[50], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "Year" element
     */
    @Override
    public int sizeOfYearArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[50]);
        }
    }

    /**
     * Sets array of all "Year" element
     */
    @Override
    public void setYearArray(java.lang.String[] yearArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(yearArray, PROPERTY_QNAME[50]);
        }
    }

    /**
     * Sets ith "Year" element
     */
    @Override
    public void setYearArray(int i, java.lang.String year) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[50], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(year);
        }
    }

    /**
     * Sets (as xml) array of all "Year" element
     */
    @Override
    public void xsetYearArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]yearArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(yearArray, PROPERTY_QNAME[50]);
        }
    }

    /**
     * Sets (as xml) ith "Year" element
     */
    @Override
    public void xsetYearArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString year) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[50], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(year);
        }
    }

    /**
     * Inserts the value as the ith "Year" element
     */
    @Override
    public void insertYear(int i, java.lang.String year) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[50], i);
            target.setStringValue(year);
        }
    }

    /**
     * Appends the value as the last "Year" element
     */
    @Override
    public void addYear(java.lang.String year) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[50]);
            target.setStringValue(year);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "Year" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewYear(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[50], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "Year" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewYear() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[50]);
            return target;
        }
    }

    /**
     * Removes the ith "Year" element
     */
    @Override
    public void removeYear(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[50], i);
        }
    }

    /**
     * Gets a List of "YearAccessed" elements
     */
    @Override
    public java.util.List<java.lang.String> getYearAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListObject<>(
                this::getYearAccessedArray,
                this::setYearAccessedArray,
                this::insertYearAccessed,
                this::removeYearAccessed,
                this::sizeOfYearAccessedArray
            );
        }
    }

    /**
     * Gets array of all "YearAccessed" elements
     */
    @Override
    public java.lang.String[] getYearAccessedArray() {
        return getObjectArray(PROPERTY_QNAME[51], org.apache.xmlbeans.SimpleValue::getStringValue, String[]::new);
    }

    /**
     * Gets ith "YearAccessed" element
     */
    @Override
    public java.lang.String getYearAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[51], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }

    /**
     * Gets (as xml) a List of "YearAccessed" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString> xgetYearAccessedList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::xgetYearAccessedArray,
                this::xsetYearAccessedArray,
                this::insertNewYearAccessed,
                this::removeYearAccessed,
                this::sizeOfYearAccessedArray
            );
        }
    }

    /**
     * Gets (as xml) array of all "YearAccessed" elements
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[] xgetYearAccessedArray() {
        return xgetArray(PROPERTY_QNAME[51], org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]::new);
    }

    /**
     * Gets (as xml) ith "YearAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString xgetYearAccessedArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[51], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "YearAccessed" element
     */
    @Override
    public int sizeOfYearAccessedArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[51]);
        }
    }

    /**
     * Sets array of all "YearAccessed" element
     */
    @Override
    public void setYearAccessedArray(java.lang.String[] yearAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(yearAccessedArray, PROPERTY_QNAME[51]);
        }
    }

    /**
     * Sets ith "YearAccessed" element
     */
    @Override
    public void setYearAccessedArray(int i, java.lang.String yearAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[51], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(yearAccessed);
        }
    }

    /**
     * Sets (as xml) array of all "YearAccessed" element
     */
    @Override
    public void xsetYearAccessedArray(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString[]yearAccessedArray) {
        synchronized (monitor()) {
            check_orphaned();
            arraySetterHelper(yearAccessedArray, PROPERTY_QNAME[51]);
        }
    }

    /**
     * Sets (as xml) ith "YearAccessed" element
     */
    @Override
    public void xsetYearAccessedArray(int i, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString yearAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().find_element_user(PROPERTY_QNAME[51], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set(yearAccessed);
        }
    }

    /**
     * Inserts the value as the ith "YearAccessed" element
     */
    @Override
    public void insertYearAccessed(int i, java.lang.String yearAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target =
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(PROPERTY_QNAME[51], i);
            target.setStringValue(yearAccessed);
        }
    }

    /**
     * Appends the value as the last "YearAccessed" element
     */
    @Override
    public void addYearAccessed(java.lang.String yearAccessed) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[51]);
            target.setStringValue(yearAccessed);
        }
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "YearAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString insertNewYearAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().insert_element_user(PROPERTY_QNAME[51], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "YearAccessed" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString addNewYearAccessed() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STString)get_store().add_element_user(PROPERTY_QNAME[51]);
            return target;
        }
    }

    /**
     * Removes the ith "YearAccessed" element
     */
    @Override
    public void removeYearAccessed(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[51], i);
        }
    }
}
