/*
 * XML Type:  CT_Properties
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/extended-properties
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Properties(@http://schemas.openxmlformats.org/officeDocument/2006/extended-properties).
 *
 * This is a complex type.
 */
public class CTPropertiesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTProperties {
    private static final long serialVersionUID = 1L;

    public CTPropertiesImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Template"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Manager"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Company"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Pages"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Words"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Characters"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "PresentationFormat"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Lines"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Paragraphs"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Slides"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Notes"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "TotalTime"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "HiddenSlides"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "MMClips"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "ScaleCrop"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "HeadingPairs"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "TitlesOfParts"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "LinksUpToDate"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "CharactersWithSpaces"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "SharedDoc"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "HyperlinkBase"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "HLinks"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "HyperlinksChanged"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "DigSig"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "Application"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "AppVersion"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/extended-properties", "DocSecurity"),
    };


    /**
     * Gets the "Template" element
     */
    @Override
    public java.lang.String getTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Template" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return target;
        }
    }

    /**
     * True if has "Template" element
     */
    @Override
    public boolean isSetTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "Template" element
     */
    @Override
    public void setTemplate(java.lang.String template) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.setStringValue(template);
        }
    }

    /**
     * Sets (as xml) the "Template" element
     */
    @Override
    public void xsetTemplate(org.apache.xmlbeans.XmlString template) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[0]);
            }
            target.set(template);
        }
    }

    /**
     * Unsets the "Template" element
     */
    @Override
    public void unsetTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "Manager" element
     */
    @Override
    public java.lang.String getManager() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Manager" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetManager() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return target;
        }
    }

    /**
     * True if has "Manager" element
     */
    @Override
    public boolean isSetManager() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "Manager" element
     */
    @Override
    public void setManager(java.lang.String manager) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.setStringValue(manager);
        }
    }

    /**
     * Sets (as xml) the "Manager" element
     */
    @Override
    public void xsetManager(org.apache.xmlbeans.XmlString manager) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[1]);
            }
            target.set(manager);
        }
    }

    /**
     * Unsets the "Manager" element
     */
    @Override
    public void unsetManager() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "Company" element
     */
    @Override
    public java.lang.String getCompany() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Company" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetCompany() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return target;
        }
    }

    /**
     * True if has "Company" element
     */
    @Override
    public boolean isSetCompany() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "Company" element
     */
    @Override
    public void setCompany(java.lang.String company) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.setStringValue(company);
        }
    }

    /**
     * Sets (as xml) the "Company" element
     */
    @Override
    public void xsetCompany(org.apache.xmlbeans.XmlString company) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[2]);
            }
            target.set(company);
        }
    }

    /**
     * Unsets the "Company" element
     */
    @Override
    public void unsetCompany() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "Pages" element
     */
    @Override
    public int getPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Pages" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetPages() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return target;
        }
    }

    /**
     * True if has "Pages" element
     */
    @Override
    public boolean isSetPages() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "Pages" element
     */
    @Override
    public void setPages(int pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.setIntValue(pages);
        }
    }

    /**
     * Sets (as xml) the "Pages" element
     */
    @Override
    public void xsetPages(org.apache.xmlbeans.XmlInt pages) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[3]);
            }
            target.set(pages);
        }
    }

    /**
     * Unsets the "Pages" element
     */
    @Override
    public void unsetPages() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "Words" element
     */
    @Override
    public int getWords() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Words" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetWords() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return target;
        }
    }

    /**
     * True if has "Words" element
     */
    @Override
    public boolean isSetWords() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "Words" element
     */
    @Override
    public void setWords(int words) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.setIntValue(words);
        }
    }

    /**
     * Sets (as xml) the "Words" element
     */
    @Override
    public void xsetWords(org.apache.xmlbeans.XmlInt words) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[4]);
            }
            target.set(words);
        }
    }

    /**
     * Unsets the "Words" element
     */
    @Override
    public void unsetWords() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "Characters" element
     */
    @Override
    public int getCharacters() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Characters" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCharacters() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return target;
        }
    }

    /**
     * True if has "Characters" element
     */
    @Override
    public boolean isSetCharacters() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "Characters" element
     */
    @Override
    public void setCharacters(int characters) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[5]);
            }
            target.setIntValue(characters);
        }
    }

    /**
     * Sets (as xml) the "Characters" element
     */
    @Override
    public void xsetCharacters(org.apache.xmlbeans.XmlInt characters) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[5]);
            }
            target.set(characters);
        }
    }

    /**
     * Unsets the "Characters" element
     */
    @Override
    public void unsetCharacters() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "PresentationFormat" element
     */
    @Override
    public java.lang.String getPresentationFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "PresentationFormat" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetPresentationFormat() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return target;
        }
    }

    /**
     * True if has "PresentationFormat" element
     */
    @Override
    public boolean isSetPresentationFormat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "PresentationFormat" element
     */
    @Override
    public void setPresentationFormat(java.lang.String presentationFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[6]);
            }
            target.setStringValue(presentationFormat);
        }
    }

    /**
     * Sets (as xml) the "PresentationFormat" element
     */
    @Override
    public void xsetPresentationFormat(org.apache.xmlbeans.XmlString presentationFormat) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[6]);
            }
            target.set(presentationFormat);
        }
    }

    /**
     * Unsets the "PresentationFormat" element
     */
    @Override
    public void unsetPresentationFormat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "Lines" element
     */
    @Override
    public int getLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Lines" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetLines() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return target;
        }
    }

    /**
     * True if has "Lines" element
     */
    @Override
    public boolean isSetLines() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "Lines" element
     */
    @Override
    public void setLines(int lines) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[7]);
            }
            target.setIntValue(lines);
        }
    }

    /**
     * Sets (as xml) the "Lines" element
     */
    @Override
    public void xsetLines(org.apache.xmlbeans.XmlInt lines) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[7]);
            }
            target.set(lines);
        }
    }

    /**
     * Unsets the "Lines" element
     */
    @Override
    public void unsetLines() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "Paragraphs" element
     */
    @Override
    public int getParagraphs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Paragraphs" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetParagraphs() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return target;
        }
    }

    /**
     * True if has "Paragraphs" element
     */
    @Override
    public boolean isSetParagraphs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "Paragraphs" element
     */
    @Override
    public void setParagraphs(int paragraphs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[8]);
            }
            target.setIntValue(paragraphs);
        }
    }

    /**
     * Sets (as xml) the "Paragraphs" element
     */
    @Override
    public void xsetParagraphs(org.apache.xmlbeans.XmlInt paragraphs) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[8]);
            }
            target.set(paragraphs);
        }
    }

    /**
     * Unsets the "Paragraphs" element
     */
    @Override
    public void unsetParagraphs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "Slides" element
     */
    @Override
    public int getSlides() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Slides" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetSlides() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return target;
        }
    }

    /**
     * True if has "Slides" element
     */
    @Override
    public boolean isSetSlides() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "Slides" element
     */
    @Override
    public void setSlides(int slides) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.setIntValue(slides);
        }
    }

    /**
     * Sets (as xml) the "Slides" element
     */
    @Override
    public void xsetSlides(org.apache.xmlbeans.XmlInt slides) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[9]);
            }
            target.set(slides);
        }
    }

    /**
     * Unsets the "Slides" element
     */
    @Override
    public void unsetSlides() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "Notes" element
     */
    @Override
    public int getNotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "Notes" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetNotes() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return target;
        }
    }

    /**
     * True if has "Notes" element
     */
    @Override
    public boolean isSetNotes() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "Notes" element
     */
    @Override
    public void setNotes(int notes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[10]);
            }
            target.setIntValue(notes);
        }
    }

    /**
     * Sets (as xml) the "Notes" element
     */
    @Override
    public void xsetNotes(org.apache.xmlbeans.XmlInt notes) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[10]);
            }
            target.set(notes);
        }
    }

    /**
     * Unsets the "Notes" element
     */
    @Override
    public void unsetNotes() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "TotalTime" element
     */
    @Override
    public int getTotalTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "TotalTime" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetTotalTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return target;
        }
    }

    /**
     * True if has "TotalTime" element
     */
    @Override
    public boolean isSetTotalTime() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "TotalTime" element
     */
    @Override
    public void setTotalTime(int totalTime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[11]);
            }
            target.setIntValue(totalTime);
        }
    }

    /**
     * Sets (as xml) the "TotalTime" element
     */
    @Override
    public void xsetTotalTime(org.apache.xmlbeans.XmlInt totalTime) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[11]);
            }
            target.set(totalTime);
        }
    }

    /**
     * Unsets the "TotalTime" element
     */
    @Override
    public void unsetTotalTime() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "HiddenSlides" element
     */
    @Override
    public int getHiddenSlides() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "HiddenSlides" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetHiddenSlides() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return target;
        }
    }

    /**
     * True if has "HiddenSlides" element
     */
    @Override
    public boolean isSetHiddenSlides() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "HiddenSlides" element
     */
    @Override
    public void setHiddenSlides(int hiddenSlides) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[12]);
            }
            target.setIntValue(hiddenSlides);
        }
    }

    /**
     * Sets (as xml) the "HiddenSlides" element
     */
    @Override
    public void xsetHiddenSlides(org.apache.xmlbeans.XmlInt hiddenSlides) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[12]);
            }
            target.set(hiddenSlides);
        }
    }

    /**
     * Unsets the "HiddenSlides" element
     */
    @Override
    public void unsetHiddenSlides() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "MMClips" element
     */
    @Override
    public int getMMClips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "MMClips" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetMMClips() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return target;
        }
    }

    /**
     * True if has "MMClips" element
     */
    @Override
    public boolean isSetMMClips() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "MMClips" element
     */
    @Override
    public void setMMClips(int mmClips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[13]);
            }
            target.setIntValue(mmClips);
        }
    }

    /**
     * Sets (as xml) the "MMClips" element
     */
    @Override
    public void xsetMMClips(org.apache.xmlbeans.XmlInt mmClips) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[13]);
            }
            target.set(mmClips);
        }
    }

    /**
     * Unsets the "MMClips" element
     */
    @Override
    public void unsetMMClips() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "ScaleCrop" element
     */
    @Override
    public boolean getScaleCrop() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "ScaleCrop" element
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetScaleCrop() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return target;
        }
    }

    /**
     * True if has "ScaleCrop" element
     */
    @Override
    public boolean isSetScaleCrop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "ScaleCrop" element
     */
    @Override
    public void setScaleCrop(boolean scaleCrop) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.setBooleanValue(scaleCrop);
        }
    }

    /**
     * Sets (as xml) the "ScaleCrop" element
     */
    @Override
    public void xsetScaleCrop(org.apache.xmlbeans.XmlBoolean scaleCrop) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(PROPERTY_QNAME[14]);
            }
            target.set(scaleCrop);
        }
    }

    /**
     * Unsets the "ScaleCrop" element
     */
    @Override
    public void unsetScaleCrop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "HeadingPairs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant getHeadingPairs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "HeadingPairs" element
     */
    @Override
    public boolean isSetHeadingPairs() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "HeadingPairs" element
     */
    @Override
    public void setHeadingPairs(org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant headingPairs) {
        generatedSetterHelperImpl(headingPairs, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "HeadingPairs" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant addNewHeadingPairs() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "HeadingPairs" element
     */
    @Override
    public void unsetHeadingPairs() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "TitlesOfParts" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr getTitlesOfParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "TitlesOfParts" element
     */
    @Override
    public boolean isSetTitlesOfParts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "TitlesOfParts" element
     */
    @Override
    public void setTitlesOfParts(org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr titlesOfParts) {
        generatedSetterHelperImpl(titlesOfParts, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "TitlesOfParts" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr addNewTitlesOfParts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorLpstr)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "TitlesOfParts" element
     */
    @Override
    public void unsetTitlesOfParts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "LinksUpToDate" element
     */
    @Override
    public boolean getLinksUpToDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "LinksUpToDate" element
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetLinksUpToDate() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return target;
        }
    }

    /**
     * True if has "LinksUpToDate" element
     */
    @Override
    public boolean isSetLinksUpToDate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "LinksUpToDate" element
     */
    @Override
    public void setLinksUpToDate(boolean linksUpToDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[17]);
            }
            target.setBooleanValue(linksUpToDate);
        }
    }

    /**
     * Sets (as xml) the "LinksUpToDate" element
     */
    @Override
    public void xsetLinksUpToDate(org.apache.xmlbeans.XmlBoolean linksUpToDate) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(PROPERTY_QNAME[17]);
            }
            target.set(linksUpToDate);
        }
    }

    /**
     * Unsets the "LinksUpToDate" element
     */
    @Override
    public void unsetLinksUpToDate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "CharactersWithSpaces" element
     */
    @Override
    public int getCharactersWithSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "CharactersWithSpaces" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetCharactersWithSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return target;
        }
    }

    /**
     * True if has "CharactersWithSpaces" element
     */
    @Override
    public boolean isSetCharactersWithSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "CharactersWithSpaces" element
     */
    @Override
    public void setCharactersWithSpaces(int charactersWithSpaces) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[18]);
            }
            target.setIntValue(charactersWithSpaces);
        }
    }

    /**
     * Sets (as xml) the "CharactersWithSpaces" element
     */
    @Override
    public void xsetCharactersWithSpaces(org.apache.xmlbeans.XmlInt charactersWithSpaces) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[18]);
            }
            target.set(charactersWithSpaces);
        }
    }

    /**
     * Unsets the "CharactersWithSpaces" element
     */
    @Override
    public void unsetCharactersWithSpaces() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "SharedDoc" element
     */
    @Override
    public boolean getSharedDoc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "SharedDoc" element
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetSharedDoc() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return target;
        }
    }

    /**
     * True if has "SharedDoc" element
     */
    @Override
    public boolean isSetSharedDoc() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "SharedDoc" element
     */
    @Override
    public void setSharedDoc(boolean sharedDoc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[19]);
            }
            target.setBooleanValue(sharedDoc);
        }
    }

    /**
     * Sets (as xml) the "SharedDoc" element
     */
    @Override
    public void xsetSharedDoc(org.apache.xmlbeans.XmlBoolean sharedDoc) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(PROPERTY_QNAME[19]);
            }
            target.set(sharedDoc);
        }
    }

    /**
     * Unsets the "SharedDoc" element
     */
    @Override
    public void unsetSharedDoc() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "HyperlinkBase" element
     */
    @Override
    public java.lang.String getHyperlinkBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "HyperlinkBase" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetHyperlinkBase() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return target;
        }
    }

    /**
     * True if has "HyperlinkBase" element
     */
    @Override
    public boolean isSetHyperlinkBase() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "HyperlinkBase" element
     */
    @Override
    public void setHyperlinkBase(java.lang.String hyperlinkBase) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[20]);
            }
            target.setStringValue(hyperlinkBase);
        }
    }

    /**
     * Sets (as xml) the "HyperlinkBase" element
     */
    @Override
    public void xsetHyperlinkBase(org.apache.xmlbeans.XmlString hyperlinkBase) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[20]);
            }
            target.set(hyperlinkBase);
        }
    }

    /**
     * Unsets the "HyperlinkBase" element
     */
    @Override
    public void unsetHyperlinkBase() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets the "HLinks" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant getHLinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant)get_store().find_element_user(PROPERTY_QNAME[21], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "HLinks" element
     */
    @Override
    public boolean isSetHLinks() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]) != 0;
        }
    }

    /**
     * Sets the "HLinks" element
     */
    @Override
    public void setHLinks(org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant hLinks) {
        generatedSetterHelperImpl(hLinks, PROPERTY_QNAME[21], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "HLinks" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant addNewHLinks() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTVectorVariant)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Unsets the "HLinks" element
     */
    @Override
    public void unsetHLinks() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], 0);
        }
    }

    /**
     * Gets the "HyperlinksChanged" element
     */
    @Override
    public boolean getHyperlinksChanged() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? false : target.getBooleanValue();
        }
    }

    /**
     * Gets (as xml) the "HyperlinksChanged" element
     */
    @Override
    public org.apache.xmlbeans.XmlBoolean xgetHyperlinksChanged() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return target;
        }
    }

    /**
     * True if has "HyperlinksChanged" element
     */
    @Override
    public boolean isSetHyperlinksChanged() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "HyperlinksChanged" element
     */
    @Override
    public void setHyperlinksChanged(boolean hyperlinksChanged) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[22]);
            }
            target.setBooleanValue(hyperlinksChanged);
        }
    }

    /**
     * Sets (as xml) the "HyperlinksChanged" element
     */
    @Override
    public void xsetHyperlinksChanged(org.apache.xmlbeans.XmlBoolean hyperlinksChanged) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(PROPERTY_QNAME[22]);
            }
            target.set(hyperlinksChanged);
        }
    }

    /**
     * Unsets the "HyperlinksChanged" element
     */
    @Override
    public void unsetHyperlinksChanged() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "DigSig" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob getDigSig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "DigSig" element
     */
    @Override
    public boolean isSetDigSig() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "DigSig" element
     */
    @Override
    public void setDigSig(org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob digSig) {
        generatedSetterHelperImpl(digSig, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "DigSig" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob addNewDigSig() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.extendedProperties.CTDigSigBlob)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "DigSig" element
     */
    @Override
    public void unsetDigSig() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "Application" element
     */
    @Override
    public java.lang.String getApplication() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "Application" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetApplication() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return target;
        }
    }

    /**
     * True if has "Application" element
     */
    @Override
    public boolean isSetApplication() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "Application" element
     */
    @Override
    public void setApplication(java.lang.String application) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[24]);
            }
            target.setStringValue(application);
        }
    }

    /**
     * Sets (as xml) the "Application" element
     */
    @Override
    public void xsetApplication(org.apache.xmlbeans.XmlString application) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[24]);
            }
            target.set(application);
        }
    }

    /**
     * Unsets the "Application" element
     */
    @Override
    public void unsetApplication() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "AppVersion" element
     */
    @Override
    public java.lang.String getAppVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? null : target.getStringValue();
        }
    }

    /**
     * Gets (as xml) the "AppVersion" element
     */
    @Override
    public org.apache.xmlbeans.XmlString xgetAppVersion() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return target;
        }
    }

    /**
     * True if has "AppVersion" element
     */
    @Override
    public boolean isSetAppVersion() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "AppVersion" element
     */
    @Override
    public void setAppVersion(java.lang.String appVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[25]);
            }
            target.setStringValue(appVersion);
        }
    }

    /**
     * Sets (as xml) the "AppVersion" element
     */
    @Override
    public void xsetAppVersion(org.apache.xmlbeans.XmlString appVersion) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PROPERTY_QNAME[25]);
            }
            target.set(appVersion);
        }
    }

    /**
     * Unsets the "AppVersion" element
     */
    @Override
    public void unsetAppVersion() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "DocSecurity" element
     */
    @Override
    public int getDocSecurity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? 0 : target.getIntValue();
        }
    }

    /**
     * Gets (as xml) the "DocSecurity" element
     */
    @Override
    public org.apache.xmlbeans.XmlInt xgetDocSecurity() {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return target;
        }
    }

    /**
     * True if has "DocSecurity" element
     */
    @Override
    public boolean isSetDocSecurity() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "DocSecurity" element
     */
    @Override
    public void setDocSecurity(int docSecurity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PROPERTY_QNAME[26]);
            }
            target.setIntValue(docSecurity);
        }
    }

    /**
     * Sets (as xml) the "DocSecurity" element
     */
    @Override
    public void xsetDocSecurity(org.apache.xmlbeans.XmlInt docSecurity) {
        synchronized (monitor()) {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            if (target == null) {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PROPERTY_QNAME[26]);
            }
            target.set(docSecurity);
        }
    }

    /**
     * Unsets the "DocSecurity" element
     */
    @Override
    public void unsetDocSecurity() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }
}
