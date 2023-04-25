/*
 * XML Type:  CT_Settings
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML CT_Settings(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a complex type.
 */
public class CTSettingsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSettings {
    private static final long serialVersionUID = 1L;

    public CTSettingsImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType);
    }

    private static final QName[] PROPERTY_QNAME = {
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "writeProtection"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "view"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "zoom"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "removePersonalInformation"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "removeDateAndTime"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotDisplayPageBoundaries"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "displayBackgroundShape"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printPostScriptOverText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printFractionalCharacterWidth"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printFormsData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedTrueTypeFonts"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "embedSystemFonts"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveSubsetFonts"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveFormsData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mirrorMargins"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "alignBordersAndEdges"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bordersDoNotSurroundHeader"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bordersDoNotSurroundFooter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "gutterAtTop"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hideSpellingErrors"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hideGrammaticalErrors"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "activeWritingStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "proofState"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "formsDesign"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "attachedTemplate"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "linkStyles"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "stylePaneFormatFilter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "stylePaneSortMethod"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "documentType"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "mailMerge"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "revisionView"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "trackRevisions"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotTrackMoves"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotTrackFormatting"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "documentProtection"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoFormatOverride"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styleLockTheme"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "styleLockQFSet"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defaultTabStop"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "autoHyphenation"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "consecutiveHyphenLimit"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hyphenationZone"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotHyphenateCaps"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "showEnvelope"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "summaryLength"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "clickAndTypeStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "defaultTableStyle"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "evenAndOddHeaders"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bookFoldRevPrinting"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bookFoldPrinting"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "bookFoldPrintingSheets"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawingGridHorizontalSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawingGridVerticalSpacing"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "displayHorizontalDrawingGridEvery"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "displayVerticalDrawingGridEvery"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotUseMarginsForDrawingGridOrigin"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawingGridHorizontalOrigin"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "drawingGridVerticalOrigin"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotShadeFormData"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noPunctuationKerning"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "characterSpacingControl"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "printTwoOnOne"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "strictFirstAndLastChars"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noLineBreaksAfter"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "noLineBreaksBefore"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "savePreviewPicture"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotValidateAgainstSchema"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveInvalidXml"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "ignoreMixedContent"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "alwaysShowPlaceholderText"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotDemarcateInvalidXml"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveXmlDataOnly"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "useXSLTWhenSaving"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "saveThroughXslt"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "showXMLTags"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "alwaysMergeEmptyNamespace"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "updateFields"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "hdrShapeDefaults"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "footnotePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "endnotePr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "compat"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "docVars"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "rsids"),
        new QName("http://schemas.openxmlformats.org/officeDocument/2006/math", "mathPr"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "attachedSchema"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "themeFontLang"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "clrSchemeMapping"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotIncludeSubdocsInStats"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotAutoCompressPictures"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "forceUpgrade"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "captions"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "readModeInkLockDown"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "smartTagType"),
        new QName("http://schemas.openxmlformats.org/schemaLibrary/2006/main", "schemaLibrary"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "shapeDefaults"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "doNotEmbedSmartTags"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "decimalSymbol"),
        new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "listSeparator"),
    };


    /**
     * Gets the "writeProtection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection getWriteProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection)get_store().find_element_user(PROPERTY_QNAME[0], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "writeProtection" element
     */
    @Override
    public boolean isSetWriteProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[0]) != 0;
        }
    }

    /**
     * Sets the "writeProtection" element
     */
    @Override
    public void setWriteProtection(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection writeProtection) {
        generatedSetterHelperImpl(writeProtection, PROPERTY_QNAME[0], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "writeProtection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection addNewWriteProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWriteProtection)get_store().add_element_user(PROPERTY_QNAME[0]);
            return target;
        }
    }

    /**
     * Unsets the "writeProtection" element
     */
    @Override
    public void unsetWriteProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[0], 0);
        }
    }

    /**
     * Gets the "view" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView getView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView)get_store().find_element_user(PROPERTY_QNAME[1], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "view" element
     */
    @Override
    public boolean isSetView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[1]) != 0;
        }
    }

    /**
     * Sets the "view" element
     */
    @Override
    public void setView(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView view) {
        generatedSetterHelperImpl(view, PROPERTY_QNAME[1], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "view" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView addNewView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTView)get_store().add_element_user(PROPERTY_QNAME[1]);
            return target;
        }
    }

    /**
     * Unsets the "view" element
     */
    @Override
    public void unsetView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[1], 0);
        }
    }

    /**
     * Gets the "zoom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom getZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom)get_store().find_element_user(PROPERTY_QNAME[2], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "zoom" element
     */
    @Override
    public boolean isSetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[2]) != 0;
        }
    }

    /**
     * Sets the "zoom" element
     */
    @Override
    public void setZoom(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom zoom) {
        generatedSetterHelperImpl(zoom, PROPERTY_QNAME[2], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "zoom" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom addNewZoom() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTZoom)get_store().add_element_user(PROPERTY_QNAME[2]);
            return target;
        }
    }

    /**
     * Unsets the "zoom" element
     */
    @Override
    public void unsetZoom() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[2], 0);
        }
    }

    /**
     * Gets the "removePersonalInformation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRemovePersonalInformation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[3], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "removePersonalInformation" element
     */
    @Override
    public boolean isSetRemovePersonalInformation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[3]) != 0;
        }
    }

    /**
     * Sets the "removePersonalInformation" element
     */
    @Override
    public void setRemovePersonalInformation(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff removePersonalInformation) {
        generatedSetterHelperImpl(removePersonalInformation, PROPERTY_QNAME[3], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "removePersonalInformation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRemovePersonalInformation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[3]);
            return target;
        }
    }

    /**
     * Unsets the "removePersonalInformation" element
     */
    @Override
    public void unsetRemovePersonalInformation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[3], 0);
        }
    }

    /**
     * Gets the "removeDateAndTime" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getRemoveDateAndTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[4], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "removeDateAndTime" element
     */
    @Override
    public boolean isSetRemoveDateAndTime() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[4]) != 0;
        }
    }

    /**
     * Sets the "removeDateAndTime" element
     */
    @Override
    public void setRemoveDateAndTime(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff removeDateAndTime) {
        generatedSetterHelperImpl(removeDateAndTime, PROPERTY_QNAME[4], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "removeDateAndTime" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewRemoveDateAndTime() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[4]);
            return target;
        }
    }

    /**
     * Unsets the "removeDateAndTime" element
     */
    @Override
    public void unsetRemoveDateAndTime() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[4], 0);
        }
    }

    /**
     * Gets the "doNotDisplayPageBoundaries" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotDisplayPageBoundaries() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[5], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotDisplayPageBoundaries" element
     */
    @Override
    public boolean isSetDoNotDisplayPageBoundaries() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[5]) != 0;
        }
    }

    /**
     * Sets the "doNotDisplayPageBoundaries" element
     */
    @Override
    public void setDoNotDisplayPageBoundaries(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotDisplayPageBoundaries) {
        generatedSetterHelperImpl(doNotDisplayPageBoundaries, PROPERTY_QNAME[5], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotDisplayPageBoundaries" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotDisplayPageBoundaries() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[5]);
            return target;
        }
    }

    /**
     * Unsets the "doNotDisplayPageBoundaries" element
     */
    @Override
    public void unsetDoNotDisplayPageBoundaries() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[5], 0);
        }
    }

    /**
     * Gets the "displayBackgroundShape" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDisplayBackgroundShape() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[6], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "displayBackgroundShape" element
     */
    @Override
    public boolean isSetDisplayBackgroundShape() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[6]) != 0;
        }
    }

    /**
     * Sets the "displayBackgroundShape" element
     */
    @Override
    public void setDisplayBackgroundShape(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff displayBackgroundShape) {
        generatedSetterHelperImpl(displayBackgroundShape, PROPERTY_QNAME[6], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "displayBackgroundShape" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDisplayBackgroundShape() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[6]);
            return target;
        }
    }

    /**
     * Unsets the "displayBackgroundShape" element
     */
    @Override
    public void unsetDisplayBackgroundShape() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[6], 0);
        }
    }

    /**
     * Gets the "printPostScriptOverText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintPostScriptOverText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[7], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printPostScriptOverText" element
     */
    @Override
    public boolean isSetPrintPostScriptOverText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[7]) != 0;
        }
    }

    /**
     * Sets the "printPostScriptOverText" element
     */
    @Override
    public void setPrintPostScriptOverText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printPostScriptOverText) {
        generatedSetterHelperImpl(printPostScriptOverText, PROPERTY_QNAME[7], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printPostScriptOverText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintPostScriptOverText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[7]);
            return target;
        }
    }

    /**
     * Unsets the "printPostScriptOverText" element
     */
    @Override
    public void unsetPrintPostScriptOverText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[7], 0);
        }
    }

    /**
     * Gets the "printFractionalCharacterWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintFractionalCharacterWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[8], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printFractionalCharacterWidth" element
     */
    @Override
    public boolean isSetPrintFractionalCharacterWidth() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[8]) != 0;
        }
    }

    /**
     * Sets the "printFractionalCharacterWidth" element
     */
    @Override
    public void setPrintFractionalCharacterWidth(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printFractionalCharacterWidth) {
        generatedSetterHelperImpl(printFractionalCharacterWidth, PROPERTY_QNAME[8], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printFractionalCharacterWidth" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintFractionalCharacterWidth() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[8]);
            return target;
        }
    }

    /**
     * Unsets the "printFractionalCharacterWidth" element
     */
    @Override
    public void unsetPrintFractionalCharacterWidth() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[8], 0);
        }
    }

    /**
     * Gets the "printFormsData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[9], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printFormsData" element
     */
    @Override
    public boolean isSetPrintFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[9]) != 0;
        }
    }

    /**
     * Sets the "printFormsData" element
     */
    @Override
    public void setPrintFormsData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printFormsData) {
        generatedSetterHelperImpl(printFormsData, PROPERTY_QNAME[9], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printFormsData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[9]);
            return target;
        }
    }

    /**
     * Unsets the "printFormsData" element
     */
    @Override
    public void unsetPrintFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[9], 0);
        }
    }

    /**
     * Gets the "embedTrueTypeFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[10], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedTrueTypeFonts" element
     */
    @Override
    public boolean isSetEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[10]) != 0;
        }
    }

    /**
     * Sets the "embedTrueTypeFonts" element
     */
    @Override
    public void setEmbedTrueTypeFonts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff embedTrueTypeFonts) {
        generatedSetterHelperImpl(embedTrueTypeFonts, PROPERTY_QNAME[10], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedTrueTypeFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[10]);
            return target;
        }
    }

    /**
     * Unsets the "embedTrueTypeFonts" element
     */
    @Override
    public void unsetEmbedTrueTypeFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[10], 0);
        }
    }

    /**
     * Gets the "embedSystemFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEmbedSystemFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[11], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "embedSystemFonts" element
     */
    @Override
    public boolean isSetEmbedSystemFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[11]) != 0;
        }
    }

    /**
     * Sets the "embedSystemFonts" element
     */
    @Override
    public void setEmbedSystemFonts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff embedSystemFonts) {
        generatedSetterHelperImpl(embedSystemFonts, PROPERTY_QNAME[11], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "embedSystemFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEmbedSystemFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[11]);
            return target;
        }
    }

    /**
     * Unsets the "embedSystemFonts" element
     */
    @Override
    public void unsetEmbedSystemFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[11], 0);
        }
    }

    /**
     * Gets the "saveSubsetFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[12], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveSubsetFonts" element
     */
    @Override
    public boolean isSetSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[12]) != 0;
        }
    }

    /**
     * Sets the "saveSubsetFonts" element
     */
    @Override
    public void setSaveSubsetFonts(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff saveSubsetFonts) {
        generatedSetterHelperImpl(saveSubsetFonts, PROPERTY_QNAME[12], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveSubsetFonts" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[12]);
            return target;
        }
    }

    /**
     * Unsets the "saveSubsetFonts" element
     */
    @Override
    public void unsetSaveSubsetFonts() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[12], 0);
        }
    }

    /**
     * Gets the "saveFormsData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSaveFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[13], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveFormsData" element
     */
    @Override
    public boolean isSetSaveFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[13]) != 0;
        }
    }

    /**
     * Sets the "saveFormsData" element
     */
    @Override
    public void setSaveFormsData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff saveFormsData) {
        generatedSetterHelperImpl(saveFormsData, PROPERTY_QNAME[13], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveFormsData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSaveFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[13]);
            return target;
        }
    }

    /**
     * Unsets the "saveFormsData" element
     */
    @Override
    public void unsetSaveFormsData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[13], 0);
        }
    }

    /**
     * Gets the "mirrorMargins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getMirrorMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[14], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mirrorMargins" element
     */
    @Override
    public boolean isSetMirrorMargins() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[14]) != 0;
        }
    }

    /**
     * Sets the "mirrorMargins" element
     */
    @Override
    public void setMirrorMargins(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff mirrorMargins) {
        generatedSetterHelperImpl(mirrorMargins, PROPERTY_QNAME[14], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mirrorMargins" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewMirrorMargins() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[14]);
            return target;
        }
    }

    /**
     * Unsets the "mirrorMargins" element
     */
    @Override
    public void unsetMirrorMargins() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[14], 0);
        }
    }

    /**
     * Gets the "alignBordersAndEdges" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAlignBordersAndEdges() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[15], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "alignBordersAndEdges" element
     */
    @Override
    public boolean isSetAlignBordersAndEdges() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[15]) != 0;
        }
    }

    /**
     * Sets the "alignBordersAndEdges" element
     */
    @Override
    public void setAlignBordersAndEdges(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff alignBordersAndEdges) {
        generatedSetterHelperImpl(alignBordersAndEdges, PROPERTY_QNAME[15], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "alignBordersAndEdges" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAlignBordersAndEdges() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[15]);
            return target;
        }
    }

    /**
     * Unsets the "alignBordersAndEdges" element
     */
    @Override
    public void unsetAlignBordersAndEdges() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[15], 0);
        }
    }

    /**
     * Gets the "bordersDoNotSurroundHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBordersDoNotSurroundHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[16], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bordersDoNotSurroundHeader" element
     */
    @Override
    public boolean isSetBordersDoNotSurroundHeader() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[16]) != 0;
        }
    }

    /**
     * Sets the "bordersDoNotSurroundHeader" element
     */
    @Override
    public void setBordersDoNotSurroundHeader(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bordersDoNotSurroundHeader) {
        generatedSetterHelperImpl(bordersDoNotSurroundHeader, PROPERTY_QNAME[16], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bordersDoNotSurroundHeader" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBordersDoNotSurroundHeader() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[16]);
            return target;
        }
    }

    /**
     * Unsets the "bordersDoNotSurroundHeader" element
     */
    @Override
    public void unsetBordersDoNotSurroundHeader() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[16], 0);
        }
    }

    /**
     * Gets the "bordersDoNotSurroundFooter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBordersDoNotSurroundFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[17], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bordersDoNotSurroundFooter" element
     */
    @Override
    public boolean isSetBordersDoNotSurroundFooter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[17]) != 0;
        }
    }

    /**
     * Sets the "bordersDoNotSurroundFooter" element
     */
    @Override
    public void setBordersDoNotSurroundFooter(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bordersDoNotSurroundFooter) {
        generatedSetterHelperImpl(bordersDoNotSurroundFooter, PROPERTY_QNAME[17], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bordersDoNotSurroundFooter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBordersDoNotSurroundFooter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[17]);
            return target;
        }
    }

    /**
     * Unsets the "bordersDoNotSurroundFooter" element
     */
    @Override
    public void unsetBordersDoNotSurroundFooter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[17], 0);
        }
    }

    /**
     * Gets the "gutterAtTop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getGutterAtTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[18], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "gutterAtTop" element
     */
    @Override
    public boolean isSetGutterAtTop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[18]) != 0;
        }
    }

    /**
     * Sets the "gutterAtTop" element
     */
    @Override
    public void setGutterAtTop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff gutterAtTop) {
        generatedSetterHelperImpl(gutterAtTop, PROPERTY_QNAME[18], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "gutterAtTop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewGutterAtTop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[18]);
            return target;
        }
    }

    /**
     * Unsets the "gutterAtTop" element
     */
    @Override
    public void unsetGutterAtTop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[18], 0);
        }
    }

    /**
     * Gets the "hideSpellingErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getHideSpellingErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[19], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hideSpellingErrors" element
     */
    @Override
    public boolean isSetHideSpellingErrors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[19]) != 0;
        }
    }

    /**
     * Sets the "hideSpellingErrors" element
     */
    @Override
    public void setHideSpellingErrors(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff hideSpellingErrors) {
        generatedSetterHelperImpl(hideSpellingErrors, PROPERTY_QNAME[19], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hideSpellingErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewHideSpellingErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[19]);
            return target;
        }
    }

    /**
     * Unsets the "hideSpellingErrors" element
     */
    @Override
    public void unsetHideSpellingErrors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[19], 0);
        }
    }

    /**
     * Gets the "hideGrammaticalErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getHideGrammaticalErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[20], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hideGrammaticalErrors" element
     */
    @Override
    public boolean isSetHideGrammaticalErrors() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[20]) != 0;
        }
    }

    /**
     * Sets the "hideGrammaticalErrors" element
     */
    @Override
    public void setHideGrammaticalErrors(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff hideGrammaticalErrors) {
        generatedSetterHelperImpl(hideGrammaticalErrors, PROPERTY_QNAME[20], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hideGrammaticalErrors" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewHideGrammaticalErrors() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[20]);
            return target;
        }
    }

    /**
     * Unsets the "hideGrammaticalErrors" element
     */
    @Override
    public void unsetHideGrammaticalErrors() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[20], 0);
        }
    }

    /**
     * Gets a List of "activeWritingStyle" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle> getActiveWritingStyleList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getActiveWritingStyleArray,
                this::setActiveWritingStyleArray,
                this::insertNewActiveWritingStyle,
                this::removeActiveWritingStyle,
                this::sizeOfActiveWritingStyleArray
            );
        }
    }

    /**
     * Gets array of all "activeWritingStyle" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle[] getActiveWritingStyleArray() {
        return getXmlObjectArray(PROPERTY_QNAME[21], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle[0]);
    }

    /**
     * Gets ith "activeWritingStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle getActiveWritingStyleArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle)get_store().find_element_user(PROPERTY_QNAME[21], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "activeWritingStyle" element
     */
    @Override
    public int sizeOfActiveWritingStyleArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[21]);
        }
    }

    /**
     * Sets array of all "activeWritingStyle" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setActiveWritingStyleArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle[] activeWritingStyleArray) {
        check_orphaned();
        arraySetterHelper(activeWritingStyleArray, PROPERTY_QNAME[21]);
    }

    /**
     * Sets ith "activeWritingStyle" element
     */
    @Override
    public void setActiveWritingStyleArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle activeWritingStyle) {
        generatedSetterHelperImpl(activeWritingStyle, PROPERTY_QNAME[21], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "activeWritingStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle insertNewActiveWritingStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle)get_store().insert_element_user(PROPERTY_QNAME[21], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "activeWritingStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle addNewActiveWritingStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTWritingStyle)get_store().add_element_user(PROPERTY_QNAME[21]);
            return target;
        }
    }

    /**
     * Removes the ith "activeWritingStyle" element
     */
    @Override
    public void removeActiveWritingStyle(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[21], i);
        }
    }

    /**
     * Gets the "proofState" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof getProofState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof)get_store().find_element_user(PROPERTY_QNAME[22], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "proofState" element
     */
    @Override
    public boolean isSetProofState() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[22]) != 0;
        }
    }

    /**
     * Sets the "proofState" element
     */
    @Override
    public void setProofState(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof proofState) {
        generatedSetterHelperImpl(proofState, PROPERTY_QNAME[22], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "proofState" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof addNewProofState() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTProof)get_store().add_element_user(PROPERTY_QNAME[22]);
            return target;
        }
    }

    /**
     * Unsets the "proofState" element
     */
    @Override
    public void unsetProofState() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[22], 0);
        }
    }

    /**
     * Gets the "formsDesign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getFormsDesign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[23], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "formsDesign" element
     */
    @Override
    public boolean isSetFormsDesign() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[23]) != 0;
        }
    }

    /**
     * Sets the "formsDesign" element
     */
    @Override
    public void setFormsDesign(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff formsDesign) {
        generatedSetterHelperImpl(formsDesign, PROPERTY_QNAME[23], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "formsDesign" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewFormsDesign() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[23]);
            return target;
        }
    }

    /**
     * Unsets the "formsDesign" element
     */
    @Override
    public void unsetFormsDesign() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[23], 0);
        }
    }

    /**
     * Gets the "attachedTemplate" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel getAttachedTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().find_element_user(PROPERTY_QNAME[24], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "attachedTemplate" element
     */
    @Override
    public boolean isSetAttachedTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[24]) != 0;
        }
    }

    /**
     * Sets the "attachedTemplate" element
     */
    @Override
    public void setAttachedTemplate(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel attachedTemplate) {
        generatedSetterHelperImpl(attachedTemplate, PROPERTY_QNAME[24], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "attachedTemplate" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel addNewAttachedTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRel)get_store().add_element_user(PROPERTY_QNAME[24]);
            return target;
        }
    }

    /**
     * Unsets the "attachedTemplate" element
     */
    @Override
    public void unsetAttachedTemplate() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[24], 0);
        }
    }

    /**
     * Gets the "linkStyles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getLinkStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[25], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "linkStyles" element
     */
    @Override
    public boolean isSetLinkStyles() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[25]) != 0;
        }
    }

    /**
     * Sets the "linkStyles" element
     */
    @Override
    public void setLinkStyles(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff linkStyles) {
        generatedSetterHelperImpl(linkStyles, PROPERTY_QNAME[25], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "linkStyles" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewLinkStyles() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[25]);
            return target;
        }
    }

    /**
     * Unsets the "linkStyles" element
     */
    @Override
    public void unsetLinkStyles() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[25], 0);
        }
    }

    /**
     * Gets the "stylePaneFormatFilter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter getStylePaneFormatFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter)get_store().find_element_user(PROPERTY_QNAME[26], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stylePaneFormatFilter" element
     */
    @Override
    public boolean isSetStylePaneFormatFilter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[26]) != 0;
        }
    }

    /**
     * Sets the "stylePaneFormatFilter" element
     */
    @Override
    public void setStylePaneFormatFilter(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter stylePaneFormatFilter) {
        generatedSetterHelperImpl(stylePaneFormatFilter, PROPERTY_QNAME[26], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stylePaneFormatFilter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter addNewStylePaneFormatFilter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStylePaneFilter)get_store().add_element_user(PROPERTY_QNAME[26]);
            return target;
        }
    }

    /**
     * Unsets the "stylePaneFormatFilter" element
     */
    @Override
    public void unsetStylePaneFormatFilter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[26], 0);
        }
    }

    /**
     * Gets the "stylePaneSortMethod" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort getStylePaneSortMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort)get_store().find_element_user(PROPERTY_QNAME[27], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "stylePaneSortMethod" element
     */
    @Override
    public boolean isSetStylePaneSortMethod() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[27]) != 0;
        }
    }

    /**
     * Sets the "stylePaneSortMethod" element
     */
    @Override
    public void setStylePaneSortMethod(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort stylePaneSortMethod) {
        generatedSetterHelperImpl(stylePaneSortMethod, PROPERTY_QNAME[27], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "stylePaneSortMethod" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort addNewStylePaneSortMethod() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyleSort)get_store().add_element_user(PROPERTY_QNAME[27]);
            return target;
        }
    }

    /**
     * Unsets the "stylePaneSortMethod" element
     */
    @Override
    public void unsetStylePaneSortMethod() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[27], 0);
        }
    }

    /**
     * Gets the "documentType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType getDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType)get_store().find_element_user(PROPERTY_QNAME[28], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "documentType" element
     */
    @Override
    public boolean isSetDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[28]) != 0;
        }
    }

    /**
     * Sets the "documentType" element
     */
    @Override
    public void setDocumentType(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType documentType) {
        generatedSetterHelperImpl(documentType, PROPERTY_QNAME[28], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "documentType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType addNewDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocType)get_store().add_element_user(PROPERTY_QNAME[28]);
            return target;
        }
    }

    /**
     * Unsets the "documentType" element
     */
    @Override
    public void unsetDocumentType() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[28], 0);
        }
    }

    /**
     * Gets the "mailMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge getMailMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge)get_store().find_element_user(PROPERTY_QNAME[29], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mailMerge" element
     */
    @Override
    public boolean isSetMailMerge() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[29]) != 0;
        }
    }

    /**
     * Sets the "mailMerge" element
     */
    @Override
    public void setMailMerge(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge mailMerge) {
        generatedSetterHelperImpl(mailMerge, PROPERTY_QNAME[29], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mailMerge" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge addNewMailMerge() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTMailMerge)get_store().add_element_user(PROPERTY_QNAME[29]);
            return target;
        }
    }

    /**
     * Unsets the "mailMerge" element
     */
    @Override
    public void unsetMailMerge() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[29], 0);
        }
    }

    /**
     * Gets the "revisionView" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView getRevisionView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView)get_store().find_element_user(PROPERTY_QNAME[30], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "revisionView" element
     */
    @Override
    public boolean isSetRevisionView() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[30]) != 0;
        }
    }

    /**
     * Sets the "revisionView" element
     */
    @Override
    public void setRevisionView(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView revisionView) {
        generatedSetterHelperImpl(revisionView, PROPERTY_QNAME[30], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "revisionView" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView addNewRevisionView() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrackChangesView)get_store().add_element_user(PROPERTY_QNAME[30]);
            return target;
        }
    }

    /**
     * Unsets the "revisionView" element
     */
    @Override
    public void unsetRevisionView() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[30], 0);
        }
    }

    /**
     * Gets the "trackRevisions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getTrackRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[31], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "trackRevisions" element
     */
    @Override
    public boolean isSetTrackRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[31]) != 0;
        }
    }

    /**
     * Sets the "trackRevisions" element
     */
    @Override
    public void setTrackRevisions(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff trackRevisions) {
        generatedSetterHelperImpl(trackRevisions, PROPERTY_QNAME[31], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "trackRevisions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewTrackRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[31]);
            return target;
        }
    }

    /**
     * Unsets the "trackRevisions" element
     */
    @Override
    public void unsetTrackRevisions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[31], 0);
        }
    }

    /**
     * Gets the "doNotTrackMoves" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotTrackMoves() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[32], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotTrackMoves" element
     */
    @Override
    public boolean isSetDoNotTrackMoves() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[32]) != 0;
        }
    }

    /**
     * Sets the "doNotTrackMoves" element
     */
    @Override
    public void setDoNotTrackMoves(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotTrackMoves) {
        generatedSetterHelperImpl(doNotTrackMoves, PROPERTY_QNAME[32], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotTrackMoves" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotTrackMoves() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[32]);
            return target;
        }
    }

    /**
     * Unsets the "doNotTrackMoves" element
     */
    @Override
    public void unsetDoNotTrackMoves() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[32], 0);
        }
    }

    /**
     * Gets the "doNotTrackFormatting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotTrackFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[33], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotTrackFormatting" element
     */
    @Override
    public boolean isSetDoNotTrackFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[33]) != 0;
        }
    }

    /**
     * Sets the "doNotTrackFormatting" element
     */
    @Override
    public void setDoNotTrackFormatting(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotTrackFormatting) {
        generatedSetterHelperImpl(doNotTrackFormatting, PROPERTY_QNAME[33], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotTrackFormatting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotTrackFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[33]);
            return target;
        }
    }

    /**
     * Unsets the "doNotTrackFormatting" element
     */
    @Override
    public void unsetDoNotTrackFormatting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[33], 0);
        }
    }

    /**
     * Gets the "documentProtection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect getDocumentProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect)get_store().find_element_user(PROPERTY_QNAME[34], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "documentProtection" element
     */
    @Override
    public boolean isSetDocumentProtection() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[34]) != 0;
        }
    }

    /**
     * Sets the "documentProtection" element
     */
    @Override
    public void setDocumentProtection(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect documentProtection) {
        generatedSetterHelperImpl(documentProtection, PROPERTY_QNAME[34], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "documentProtection" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect addNewDocumentProtection() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocProtect)get_store().add_element_user(PROPERTY_QNAME[34]);
            return target;
        }
    }

    /**
     * Unsets the "documentProtection" element
     */
    @Override
    public void unsetDocumentProtection() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[34], 0);
        }
    }

    /**
     * Gets the "autoFormatOverride" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoFormatOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[35], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoFormatOverride" element
     */
    @Override
    public boolean isSetAutoFormatOverride() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[35]) != 0;
        }
    }

    /**
     * Sets the "autoFormatOverride" element
     */
    @Override
    public void setAutoFormatOverride(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoFormatOverride) {
        generatedSetterHelperImpl(autoFormatOverride, PROPERTY_QNAME[35], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoFormatOverride" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoFormatOverride() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[35]);
            return target;
        }
    }

    /**
     * Unsets the "autoFormatOverride" element
     */
    @Override
    public void unsetAutoFormatOverride() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[35], 0);
        }
    }

    /**
     * Gets the "styleLockTheme" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getStyleLockTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[36], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "styleLockTheme" element
     */
    @Override
    public boolean isSetStyleLockTheme() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[36]) != 0;
        }
    }

    /**
     * Sets the "styleLockTheme" element
     */
    @Override
    public void setStyleLockTheme(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff styleLockTheme) {
        generatedSetterHelperImpl(styleLockTheme, PROPERTY_QNAME[36], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleLockTheme" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewStyleLockTheme() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[36]);
            return target;
        }
    }

    /**
     * Unsets the "styleLockTheme" element
     */
    @Override
    public void unsetStyleLockTheme() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[36], 0);
        }
    }

    /**
     * Gets the "styleLockQFSet" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getStyleLockQFSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[37], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "styleLockQFSet" element
     */
    @Override
    public boolean isSetStyleLockQFSet() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[37]) != 0;
        }
    }

    /**
     * Sets the "styleLockQFSet" element
     */
    @Override
    public void setStyleLockQFSet(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff styleLockQFSet) {
        generatedSetterHelperImpl(styleLockQFSet, PROPERTY_QNAME[37], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "styleLockQFSet" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewStyleLockQFSet() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[37]);
            return target;
        }
    }

    /**
     * Unsets the "styleLockQFSet" element
     */
    @Override
    public void unsetStyleLockQFSet() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[37], 0);
        }
    }

    /**
     * Gets the "defaultTabStop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getDefaultTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[38], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "defaultTabStop" element
     */
    @Override
    public boolean isSetDefaultTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[38]) != 0;
        }
    }

    /**
     * Sets the "defaultTabStop" element
     */
    @Override
    public void setDefaultTabStop(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure defaultTabStop) {
        generatedSetterHelperImpl(defaultTabStop, PROPERTY_QNAME[38], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "defaultTabStop" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewDefaultTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[38]);
            return target;
        }
    }

    /**
     * Unsets the "defaultTabStop" element
     */
    @Override
    public void unsetDefaultTabStop() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[38], 0);
        }
    }

    /**
     * Gets the "autoHyphenation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAutoHyphenation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[39], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "autoHyphenation" element
     */
    @Override
    public boolean isSetAutoHyphenation() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[39]) != 0;
        }
    }

    /**
     * Sets the "autoHyphenation" element
     */
    @Override
    public void setAutoHyphenation(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff autoHyphenation) {
        generatedSetterHelperImpl(autoHyphenation, PROPERTY_QNAME[39], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "autoHyphenation" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAutoHyphenation() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[39]);
            return target;
        }
    }

    /**
     * Unsets the "autoHyphenation" element
     */
    @Override
    public void unsetAutoHyphenation() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[39], 0);
        }
    }

    /**
     * Gets the "consecutiveHyphenLimit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getConsecutiveHyphenLimit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[40], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "consecutiveHyphenLimit" element
     */
    @Override
    public boolean isSetConsecutiveHyphenLimit() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[40]) != 0;
        }
    }

    /**
     * Sets the "consecutiveHyphenLimit" element
     */
    @Override
    public void setConsecutiveHyphenLimit(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber consecutiveHyphenLimit) {
        generatedSetterHelperImpl(consecutiveHyphenLimit, PROPERTY_QNAME[40], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "consecutiveHyphenLimit" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewConsecutiveHyphenLimit() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[40]);
            return target;
        }
    }

    /**
     * Unsets the "consecutiveHyphenLimit" element
     */
    @Override
    public void unsetConsecutiveHyphenLimit() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[40], 0);
        }
    }

    /**
     * Gets the "hyphenationZone" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getHyphenationZone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[41], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hyphenationZone" element
     */
    @Override
    public boolean isSetHyphenationZone() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[41]) != 0;
        }
    }

    /**
     * Sets the "hyphenationZone" element
     */
    @Override
    public void setHyphenationZone(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure hyphenationZone) {
        generatedSetterHelperImpl(hyphenationZone, PROPERTY_QNAME[41], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hyphenationZone" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewHyphenationZone() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[41]);
            return target;
        }
    }

    /**
     * Unsets the "hyphenationZone" element
     */
    @Override
    public void unsetHyphenationZone() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[41], 0);
        }
    }

    /**
     * Gets the "doNotHyphenateCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotHyphenateCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[42], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotHyphenateCaps" element
     */
    @Override
    public boolean isSetDoNotHyphenateCaps() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[42]) != 0;
        }
    }

    /**
     * Sets the "doNotHyphenateCaps" element
     */
    @Override
    public void setDoNotHyphenateCaps(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotHyphenateCaps) {
        generatedSetterHelperImpl(doNotHyphenateCaps, PROPERTY_QNAME[42], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotHyphenateCaps" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotHyphenateCaps() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[42]);
            return target;
        }
    }

    /**
     * Unsets the "doNotHyphenateCaps" element
     */
    @Override
    public void unsetDoNotHyphenateCaps() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[42], 0);
        }
    }

    /**
     * Gets the "showEnvelope" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShowEnvelope() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[43], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showEnvelope" element
     */
    @Override
    public boolean isSetShowEnvelope() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[43]) != 0;
        }
    }

    /**
     * Sets the "showEnvelope" element
     */
    @Override
    public void setShowEnvelope(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff showEnvelope) {
        generatedSetterHelperImpl(showEnvelope, PROPERTY_QNAME[43], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showEnvelope" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShowEnvelope() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[43]);
            return target;
        }
    }

    /**
     * Unsets the "showEnvelope" element
     */
    @Override
    public void unsetShowEnvelope() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[43], 0);
        }
    }

    /**
     * Gets the "summaryLength" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent getSummaryLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent)get_store().find_element_user(PROPERTY_QNAME[44], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "summaryLength" element
     */
    @Override
    public boolean isSetSummaryLength() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[44]) != 0;
        }
    }

    /**
     * Sets the "summaryLength" element
     */
    @Override
    public void setSummaryLength(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent summaryLength) {
        generatedSetterHelperImpl(summaryLength, PROPERTY_QNAME[44], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "summaryLength" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent addNewSummaryLength() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumberOrPrecent)get_store().add_element_user(PROPERTY_QNAME[44]);
            return target;
        }
    }

    /**
     * Unsets the "summaryLength" element
     */
    @Override
    public void unsetSummaryLength() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[44], 0);
        }
    }

    /**
     * Gets the "clickAndTypeStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getClickAndTypeStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[45], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clickAndTypeStyle" element
     */
    @Override
    public boolean isSetClickAndTypeStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[45]) != 0;
        }
    }

    /**
     * Sets the "clickAndTypeStyle" element
     */
    @Override
    public void setClickAndTypeStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString clickAndTypeStyle) {
        generatedSetterHelperImpl(clickAndTypeStyle, PROPERTY_QNAME[45], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clickAndTypeStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewClickAndTypeStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[45]);
            return target;
        }
    }

    /**
     * Unsets the "clickAndTypeStyle" element
     */
    @Override
    public void unsetClickAndTypeStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[45], 0);
        }
    }

    /**
     * Gets the "defaultTableStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDefaultTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[46], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "defaultTableStyle" element
     */
    @Override
    public boolean isSetDefaultTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[46]) != 0;
        }
    }

    /**
     * Sets the "defaultTableStyle" element
     */
    @Override
    public void setDefaultTableStyle(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString defaultTableStyle) {
        generatedSetterHelperImpl(defaultTableStyle, PROPERTY_QNAME[46], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "defaultTableStyle" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDefaultTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[46]);
            return target;
        }
    }

    /**
     * Unsets the "defaultTableStyle" element
     */
    @Override
    public void unsetDefaultTableStyle() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[46], 0);
        }
    }

    /**
     * Gets the "evenAndOddHeaders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getEvenAndOddHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[47], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "evenAndOddHeaders" element
     */
    @Override
    public boolean isSetEvenAndOddHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[47]) != 0;
        }
    }

    /**
     * Sets the "evenAndOddHeaders" element
     */
    @Override
    public void setEvenAndOddHeaders(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff evenAndOddHeaders) {
        generatedSetterHelperImpl(evenAndOddHeaders, PROPERTY_QNAME[47], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "evenAndOddHeaders" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewEvenAndOddHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[47]);
            return target;
        }
    }

    /**
     * Unsets the "evenAndOddHeaders" element
     */
    @Override
    public void unsetEvenAndOddHeaders() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[47], 0);
        }
    }

    /**
     * Gets the "bookFoldRevPrinting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBookFoldRevPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[48], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bookFoldRevPrinting" element
     */
    @Override
    public boolean isSetBookFoldRevPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[48]) != 0;
        }
    }

    /**
     * Sets the "bookFoldRevPrinting" element
     */
    @Override
    public void setBookFoldRevPrinting(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bookFoldRevPrinting) {
        generatedSetterHelperImpl(bookFoldRevPrinting, PROPERTY_QNAME[48], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bookFoldRevPrinting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBookFoldRevPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[48]);
            return target;
        }
    }

    /**
     * Unsets the "bookFoldRevPrinting" element
     */
    @Override
    public void unsetBookFoldRevPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[48], 0);
        }
    }

    /**
     * Gets the "bookFoldPrinting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getBookFoldPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[49], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bookFoldPrinting" element
     */
    @Override
    public boolean isSetBookFoldPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[49]) != 0;
        }
    }

    /**
     * Sets the "bookFoldPrinting" element
     */
    @Override
    public void setBookFoldPrinting(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff bookFoldPrinting) {
        generatedSetterHelperImpl(bookFoldPrinting, PROPERTY_QNAME[49], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bookFoldPrinting" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewBookFoldPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[49]);
            return target;
        }
    }

    /**
     * Unsets the "bookFoldPrinting" element
     */
    @Override
    public void unsetBookFoldPrinting() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[49], 0);
        }
    }

    /**
     * Gets the "bookFoldPrintingSheets" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getBookFoldPrintingSheets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[50], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "bookFoldPrintingSheets" element
     */
    @Override
    public boolean isSetBookFoldPrintingSheets() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[50]) != 0;
        }
    }

    /**
     * Sets the "bookFoldPrintingSheets" element
     */
    @Override
    public void setBookFoldPrintingSheets(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber bookFoldPrintingSheets) {
        generatedSetterHelperImpl(bookFoldPrintingSheets, PROPERTY_QNAME[50], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "bookFoldPrintingSheets" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewBookFoldPrintingSheets() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[50]);
            return target;
        }
    }

    /**
     * Unsets the "bookFoldPrintingSheets" element
     */
    @Override
    public void unsetBookFoldPrintingSheets() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[50], 0);
        }
    }

    /**
     * Gets the "drawingGridHorizontalSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getDrawingGridHorizontalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[51], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawingGridHorizontalSpacing" element
     */
    @Override
    public boolean isSetDrawingGridHorizontalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[51]) != 0;
        }
    }

    /**
     * Sets the "drawingGridHorizontalSpacing" element
     */
    @Override
    public void setDrawingGridHorizontalSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure drawingGridHorizontalSpacing) {
        generatedSetterHelperImpl(drawingGridHorizontalSpacing, PROPERTY_QNAME[51], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingGridHorizontalSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewDrawingGridHorizontalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[51]);
            return target;
        }
    }

    /**
     * Unsets the "drawingGridHorizontalSpacing" element
     */
    @Override
    public void unsetDrawingGridHorizontalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[51], 0);
        }
    }

    /**
     * Gets the "drawingGridVerticalSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getDrawingGridVerticalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[52], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawingGridVerticalSpacing" element
     */
    @Override
    public boolean isSetDrawingGridVerticalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[52]) != 0;
        }
    }

    /**
     * Sets the "drawingGridVerticalSpacing" element
     */
    @Override
    public void setDrawingGridVerticalSpacing(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure drawingGridVerticalSpacing) {
        generatedSetterHelperImpl(drawingGridVerticalSpacing, PROPERTY_QNAME[52], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingGridVerticalSpacing" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewDrawingGridVerticalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[52]);
            return target;
        }
    }

    /**
     * Unsets the "drawingGridVerticalSpacing" element
     */
    @Override
    public void unsetDrawingGridVerticalSpacing() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[52], 0);
        }
    }

    /**
     * Gets the "displayHorizontalDrawingGridEvery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getDisplayHorizontalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[53], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "displayHorizontalDrawingGridEvery" element
     */
    @Override
    public boolean isSetDisplayHorizontalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[53]) != 0;
        }
    }

    /**
     * Sets the "displayHorizontalDrawingGridEvery" element
     */
    @Override
    public void setDisplayHorizontalDrawingGridEvery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber displayHorizontalDrawingGridEvery) {
        generatedSetterHelperImpl(displayHorizontalDrawingGridEvery, PROPERTY_QNAME[53], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "displayHorizontalDrawingGridEvery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewDisplayHorizontalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[53]);
            return target;
        }
    }

    /**
     * Unsets the "displayHorizontalDrawingGridEvery" element
     */
    @Override
    public void unsetDisplayHorizontalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[53], 0);
        }
    }

    /**
     * Gets the "displayVerticalDrawingGridEvery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber getDisplayVerticalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().find_element_user(PROPERTY_QNAME[54], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "displayVerticalDrawingGridEvery" element
     */
    @Override
    public boolean isSetDisplayVerticalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[54]) != 0;
        }
    }

    /**
     * Sets the "displayVerticalDrawingGridEvery" element
     */
    @Override
    public void setDisplayVerticalDrawingGridEvery(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber displayVerticalDrawingGridEvery) {
        generatedSetterHelperImpl(displayVerticalDrawingGridEvery, PROPERTY_QNAME[54], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "displayVerticalDrawingGridEvery" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber addNewDisplayVerticalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber)get_store().add_element_user(PROPERTY_QNAME[54]);
            return target;
        }
    }

    /**
     * Unsets the "displayVerticalDrawingGridEvery" element
     */
    @Override
    public void unsetDisplayVerticalDrawingGridEvery() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[54], 0);
        }
    }

    /**
     * Gets the "doNotUseMarginsForDrawingGridOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotUseMarginsForDrawingGridOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[55], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotUseMarginsForDrawingGridOrigin" element
     */
    @Override
    public boolean isSetDoNotUseMarginsForDrawingGridOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[55]) != 0;
        }
    }

    /**
     * Sets the "doNotUseMarginsForDrawingGridOrigin" element
     */
    @Override
    public void setDoNotUseMarginsForDrawingGridOrigin(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotUseMarginsForDrawingGridOrigin) {
        generatedSetterHelperImpl(doNotUseMarginsForDrawingGridOrigin, PROPERTY_QNAME[55], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotUseMarginsForDrawingGridOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotUseMarginsForDrawingGridOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[55]);
            return target;
        }
    }

    /**
     * Unsets the "doNotUseMarginsForDrawingGridOrigin" element
     */
    @Override
    public void unsetDoNotUseMarginsForDrawingGridOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[55], 0);
        }
    }

    /**
     * Gets the "drawingGridHorizontalOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getDrawingGridHorizontalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[56], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawingGridHorizontalOrigin" element
     */
    @Override
    public boolean isSetDrawingGridHorizontalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[56]) != 0;
        }
    }

    /**
     * Sets the "drawingGridHorizontalOrigin" element
     */
    @Override
    public void setDrawingGridHorizontalOrigin(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure drawingGridHorizontalOrigin) {
        generatedSetterHelperImpl(drawingGridHorizontalOrigin, PROPERTY_QNAME[56], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingGridHorizontalOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewDrawingGridHorizontalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[56]);
            return target;
        }
    }

    /**
     * Unsets the "drawingGridHorizontalOrigin" element
     */
    @Override
    public void unsetDrawingGridHorizontalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[56], 0);
        }
    }

    /**
     * Gets the "drawingGridVerticalOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure getDrawingGridVerticalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().find_element_user(PROPERTY_QNAME[57], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "drawingGridVerticalOrigin" element
     */
    @Override
    public boolean isSetDrawingGridVerticalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[57]) != 0;
        }
    }

    /**
     * Sets the "drawingGridVerticalOrigin" element
     */
    @Override
    public void setDrawingGridVerticalOrigin(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure drawingGridVerticalOrigin) {
        generatedSetterHelperImpl(drawingGridVerticalOrigin, PROPERTY_QNAME[57], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "drawingGridVerticalOrigin" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure addNewDrawingGridVerticalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTwipsMeasure)get_store().add_element_user(PROPERTY_QNAME[57]);
            return target;
        }
    }

    /**
     * Unsets the "drawingGridVerticalOrigin" element
     */
    @Override
    public void unsetDrawingGridVerticalOrigin() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[57], 0);
        }
    }

    /**
     * Gets the "doNotShadeFormData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotShadeFormData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[58], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotShadeFormData" element
     */
    @Override
    public boolean isSetDoNotShadeFormData() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[58]) != 0;
        }
    }

    /**
     * Sets the "doNotShadeFormData" element
     */
    @Override
    public void setDoNotShadeFormData(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotShadeFormData) {
        generatedSetterHelperImpl(doNotShadeFormData, PROPERTY_QNAME[58], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotShadeFormData" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotShadeFormData() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[58]);
            return target;
        }
    }

    /**
     * Unsets the "doNotShadeFormData" element
     */
    @Override
    public void unsetDoNotShadeFormData() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[58], 0);
        }
    }

    /**
     * Gets the "noPunctuationKerning" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getNoPunctuationKerning() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[59], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noPunctuationKerning" element
     */
    @Override
    public boolean isSetNoPunctuationKerning() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[59]) != 0;
        }
    }

    /**
     * Sets the "noPunctuationKerning" element
     */
    @Override
    public void setNoPunctuationKerning(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff noPunctuationKerning) {
        generatedSetterHelperImpl(noPunctuationKerning, PROPERTY_QNAME[59], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noPunctuationKerning" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewNoPunctuationKerning() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[59]);
            return target;
        }
    }

    /**
     * Unsets the "noPunctuationKerning" element
     */
    @Override
    public void unsetNoPunctuationKerning() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[59], 0);
        }
    }

    /**
     * Gets the "characterSpacingControl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing getCharacterSpacingControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing)get_store().find_element_user(PROPERTY_QNAME[60], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "characterSpacingControl" element
     */
    @Override
    public boolean isSetCharacterSpacingControl() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[60]) != 0;
        }
    }

    /**
     * Sets the "characterSpacingControl" element
     */
    @Override
    public void setCharacterSpacingControl(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing characterSpacingControl) {
        generatedSetterHelperImpl(characterSpacingControl, PROPERTY_QNAME[60], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "characterSpacingControl" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing addNewCharacterSpacingControl() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCharacterSpacing)get_store().add_element_user(PROPERTY_QNAME[60]);
            return target;
        }
    }

    /**
     * Unsets the "characterSpacingControl" element
     */
    @Override
    public void unsetCharacterSpacingControl() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[60], 0);
        }
    }

    /**
     * Gets the "printTwoOnOne" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getPrintTwoOnOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[61], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "printTwoOnOne" element
     */
    @Override
    public boolean isSetPrintTwoOnOne() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[61]) != 0;
        }
    }

    /**
     * Sets the "printTwoOnOne" element
     */
    @Override
    public void setPrintTwoOnOne(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff printTwoOnOne) {
        generatedSetterHelperImpl(printTwoOnOne, PROPERTY_QNAME[61], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "printTwoOnOne" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewPrintTwoOnOne() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[61]);
            return target;
        }
    }

    /**
     * Unsets the "printTwoOnOne" element
     */
    @Override
    public void unsetPrintTwoOnOne() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[61], 0);
        }
    }

    /**
     * Gets the "strictFirstAndLastChars" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[62], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "strictFirstAndLastChars" element
     */
    @Override
    public boolean isSetStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[62]) != 0;
        }
    }

    /**
     * Sets the "strictFirstAndLastChars" element
     */
    @Override
    public void setStrictFirstAndLastChars(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff strictFirstAndLastChars) {
        generatedSetterHelperImpl(strictFirstAndLastChars, PROPERTY_QNAME[62], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "strictFirstAndLastChars" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[62]);
            return target;
        }
    }

    /**
     * Unsets the "strictFirstAndLastChars" element
     */
    @Override
    public void unsetStrictFirstAndLastChars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[62], 0);
        }
    }

    /**
     * Gets the "noLineBreaksAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku getNoLineBreaksAfter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku)get_store().find_element_user(PROPERTY_QNAME[63], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noLineBreaksAfter" element
     */
    @Override
    public boolean isSetNoLineBreaksAfter() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[63]) != 0;
        }
    }

    /**
     * Sets the "noLineBreaksAfter" element
     */
    @Override
    public void setNoLineBreaksAfter(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku noLineBreaksAfter) {
        generatedSetterHelperImpl(noLineBreaksAfter, PROPERTY_QNAME[63], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noLineBreaksAfter" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku addNewNoLineBreaksAfter() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku)get_store().add_element_user(PROPERTY_QNAME[63]);
            return target;
        }
    }

    /**
     * Unsets the "noLineBreaksAfter" element
     */
    @Override
    public void unsetNoLineBreaksAfter() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[63], 0);
        }
    }

    /**
     * Gets the "noLineBreaksBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku getNoLineBreaksBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku)get_store().find_element_user(PROPERTY_QNAME[64], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "noLineBreaksBefore" element
     */
    @Override
    public boolean isSetNoLineBreaksBefore() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[64]) != 0;
        }
    }

    /**
     * Sets the "noLineBreaksBefore" element
     */
    @Override
    public void setNoLineBreaksBefore(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku noLineBreaksBefore) {
        generatedSetterHelperImpl(noLineBreaksBefore, PROPERTY_QNAME[64], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "noLineBreaksBefore" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku addNewNoLineBreaksBefore() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTKinsoku)get_store().add_element_user(PROPERTY_QNAME[64]);
            return target;
        }
    }

    /**
     * Unsets the "noLineBreaksBefore" element
     */
    @Override
    public void unsetNoLineBreaksBefore() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[64], 0);
        }
    }

    /**
     * Gets the "savePreviewPicture" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSavePreviewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[65], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "savePreviewPicture" element
     */
    @Override
    public boolean isSetSavePreviewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[65]) != 0;
        }
    }

    /**
     * Sets the "savePreviewPicture" element
     */
    @Override
    public void setSavePreviewPicture(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff savePreviewPicture) {
        generatedSetterHelperImpl(savePreviewPicture, PROPERTY_QNAME[65], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "savePreviewPicture" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSavePreviewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[65]);
            return target;
        }
    }

    /**
     * Unsets the "savePreviewPicture" element
     */
    @Override
    public void unsetSavePreviewPicture() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[65], 0);
        }
    }

    /**
     * Gets the "doNotValidateAgainstSchema" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotValidateAgainstSchema() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[66], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotValidateAgainstSchema" element
     */
    @Override
    public boolean isSetDoNotValidateAgainstSchema() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[66]) != 0;
        }
    }

    /**
     * Sets the "doNotValidateAgainstSchema" element
     */
    @Override
    public void setDoNotValidateAgainstSchema(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotValidateAgainstSchema) {
        generatedSetterHelperImpl(doNotValidateAgainstSchema, PROPERTY_QNAME[66], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotValidateAgainstSchema" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotValidateAgainstSchema() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[66]);
            return target;
        }
    }

    /**
     * Unsets the "doNotValidateAgainstSchema" element
     */
    @Override
    public void unsetDoNotValidateAgainstSchema() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[66], 0);
        }
    }

    /**
     * Gets the "saveInvalidXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSaveInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[67], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveInvalidXml" element
     */
    @Override
    public boolean isSetSaveInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[67]) != 0;
        }
    }

    /**
     * Sets the "saveInvalidXml" element
     */
    @Override
    public void setSaveInvalidXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff saveInvalidXml) {
        generatedSetterHelperImpl(saveInvalidXml, PROPERTY_QNAME[67], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveInvalidXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSaveInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[67]);
            return target;
        }
    }

    /**
     * Unsets the "saveInvalidXml" element
     */
    @Override
    public void unsetSaveInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[67], 0);
        }
    }

    /**
     * Gets the "ignoreMixedContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getIgnoreMixedContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[68], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "ignoreMixedContent" element
     */
    @Override
    public boolean isSetIgnoreMixedContent() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[68]) != 0;
        }
    }

    /**
     * Sets the "ignoreMixedContent" element
     */
    @Override
    public void setIgnoreMixedContent(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff ignoreMixedContent) {
        generatedSetterHelperImpl(ignoreMixedContent, PROPERTY_QNAME[68], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "ignoreMixedContent" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewIgnoreMixedContent() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[68]);
            return target;
        }
    }

    /**
     * Unsets the "ignoreMixedContent" element
     */
    @Override
    public void unsetIgnoreMixedContent() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[68], 0);
        }
    }

    /**
     * Gets the "alwaysShowPlaceholderText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAlwaysShowPlaceholderText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[69], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "alwaysShowPlaceholderText" element
     */
    @Override
    public boolean isSetAlwaysShowPlaceholderText() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[69]) != 0;
        }
    }

    /**
     * Sets the "alwaysShowPlaceholderText" element
     */
    @Override
    public void setAlwaysShowPlaceholderText(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff alwaysShowPlaceholderText) {
        generatedSetterHelperImpl(alwaysShowPlaceholderText, PROPERTY_QNAME[69], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "alwaysShowPlaceholderText" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAlwaysShowPlaceholderText() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[69]);
            return target;
        }
    }

    /**
     * Unsets the "alwaysShowPlaceholderText" element
     */
    @Override
    public void unsetAlwaysShowPlaceholderText() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[69], 0);
        }
    }

    /**
     * Gets the "doNotDemarcateInvalidXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotDemarcateInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[70], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotDemarcateInvalidXml" element
     */
    @Override
    public boolean isSetDoNotDemarcateInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[70]) != 0;
        }
    }

    /**
     * Sets the "doNotDemarcateInvalidXml" element
     */
    @Override
    public void setDoNotDemarcateInvalidXml(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotDemarcateInvalidXml) {
        generatedSetterHelperImpl(doNotDemarcateInvalidXml, PROPERTY_QNAME[70], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotDemarcateInvalidXml" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotDemarcateInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[70]);
            return target;
        }
    }

    /**
     * Unsets the "doNotDemarcateInvalidXml" element
     */
    @Override
    public void unsetDoNotDemarcateInvalidXml() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[70], 0);
        }
    }

    /**
     * Gets the "saveXmlDataOnly" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getSaveXmlDataOnly() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[71], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveXmlDataOnly" element
     */
    @Override
    public boolean isSetSaveXmlDataOnly() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[71]) != 0;
        }
    }

    /**
     * Sets the "saveXmlDataOnly" element
     */
    @Override
    public void setSaveXmlDataOnly(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff saveXmlDataOnly) {
        generatedSetterHelperImpl(saveXmlDataOnly, PROPERTY_QNAME[71], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveXmlDataOnly" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewSaveXmlDataOnly() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[71]);
            return target;
        }
    }

    /**
     * Unsets the "saveXmlDataOnly" element
     */
    @Override
    public void unsetSaveXmlDataOnly() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[71], 0);
        }
    }

    /**
     * Gets the "useXSLTWhenSaving" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUseXSLTWhenSaving() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[72], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "useXSLTWhenSaving" element
     */
    @Override
    public boolean isSetUseXSLTWhenSaving() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[72]) != 0;
        }
    }

    /**
     * Sets the "useXSLTWhenSaving" element
     */
    @Override
    public void setUseXSLTWhenSaving(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff useXSLTWhenSaving) {
        generatedSetterHelperImpl(useXSLTWhenSaving, PROPERTY_QNAME[72], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "useXSLTWhenSaving" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUseXSLTWhenSaving() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[72]);
            return target;
        }
    }

    /**
     * Unsets the "useXSLTWhenSaving" element
     */
    @Override
    public void unsetUseXSLTWhenSaving() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[72], 0);
        }
    }

    /**
     * Gets the "saveThroughXslt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt getSaveThroughXslt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt)get_store().find_element_user(PROPERTY_QNAME[73], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "saveThroughXslt" element
     */
    @Override
    public boolean isSetSaveThroughXslt() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[73]) != 0;
        }
    }

    /**
     * Sets the "saveThroughXslt" element
     */
    @Override
    public void setSaveThroughXslt(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt saveThroughXslt) {
        generatedSetterHelperImpl(saveThroughXslt, PROPERTY_QNAME[73], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "saveThroughXslt" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt addNewSaveThroughXslt() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSaveThroughXslt)get_store().add_element_user(PROPERTY_QNAME[73]);
            return target;
        }
    }

    /**
     * Unsets the "saveThroughXslt" element
     */
    @Override
    public void unsetSaveThroughXslt() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[73], 0);
        }
    }

    /**
     * Gets the "showXMLTags" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getShowXMLTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[74], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "showXMLTags" element
     */
    @Override
    public boolean isSetShowXMLTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[74]) != 0;
        }
    }

    /**
     * Sets the "showXMLTags" element
     */
    @Override
    public void setShowXMLTags(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff showXMLTags) {
        generatedSetterHelperImpl(showXMLTags, PROPERTY_QNAME[74], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "showXMLTags" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewShowXMLTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[74]);
            return target;
        }
    }

    /**
     * Unsets the "showXMLTags" element
     */
    @Override
    public void unsetShowXMLTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[74], 0);
        }
    }

    /**
     * Gets the "alwaysMergeEmptyNamespace" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getAlwaysMergeEmptyNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[75], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "alwaysMergeEmptyNamespace" element
     */
    @Override
    public boolean isSetAlwaysMergeEmptyNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[75]) != 0;
        }
    }

    /**
     * Sets the "alwaysMergeEmptyNamespace" element
     */
    @Override
    public void setAlwaysMergeEmptyNamespace(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff alwaysMergeEmptyNamespace) {
        generatedSetterHelperImpl(alwaysMergeEmptyNamespace, PROPERTY_QNAME[75], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "alwaysMergeEmptyNamespace" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewAlwaysMergeEmptyNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[75]);
            return target;
        }
    }

    /**
     * Unsets the "alwaysMergeEmptyNamespace" element
     */
    @Override
    public void unsetAlwaysMergeEmptyNamespace() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[75], 0);
        }
    }

    /**
     * Gets the "updateFields" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getUpdateFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[76], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "updateFields" element
     */
    @Override
    public boolean isSetUpdateFields() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[76]) != 0;
        }
    }

    /**
     * Sets the "updateFields" element
     */
    @Override
    public void setUpdateFields(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff updateFields) {
        generatedSetterHelperImpl(updateFields, PROPERTY_QNAME[76], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "updateFields" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewUpdateFields() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[76]);
            return target;
        }
    }

    /**
     * Unsets the "updateFields" element
     */
    @Override
    public void unsetUpdateFields() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[76], 0);
        }
    }

    /**
     * Gets the "hdrShapeDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults getHdrShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults)get_store().find_element_user(PROPERTY_QNAME[77], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "hdrShapeDefaults" element
     */
    @Override
    public boolean isSetHdrShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[77]) != 0;
        }
    }

    /**
     * Sets the "hdrShapeDefaults" element
     */
    @Override
    public void setHdrShapeDefaults(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults hdrShapeDefaults) {
        generatedSetterHelperImpl(hdrShapeDefaults, PROPERTY_QNAME[77], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "hdrShapeDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults addNewHdrShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults)get_store().add_element_user(PROPERTY_QNAME[77]);
            return target;
        }
    }

    /**
     * Unsets the "hdrShapeDefaults" element
     */
    @Override
    public void unsetHdrShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[77], 0);
        }
    }

    /**
     * Gets the "footnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps getFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps)get_store().find_element_user(PROPERTY_QNAME[78], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "footnotePr" element
     */
    @Override
    public boolean isSetFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[78]) != 0;
        }
    }

    /**
     * Sets the "footnotePr" element
     */
    @Override
    public void setFootnotePr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps footnotePr) {
        generatedSetterHelperImpl(footnotePr, PROPERTY_QNAME[78], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "footnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps addNewFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFtnDocProps)get_store().add_element_user(PROPERTY_QNAME[78]);
            return target;
        }
    }

    /**
     * Unsets the "footnotePr" element
     */
    @Override
    public void unsetFootnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[78], 0);
        }
    }

    /**
     * Gets the "endnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps getEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps)get_store().find_element_user(PROPERTY_QNAME[79], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "endnotePr" element
     */
    @Override
    public boolean isSetEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[79]) != 0;
        }
    }

    /**
     * Sets the "endnotePr" element
     */
    @Override
    public void setEndnotePr(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps endnotePr) {
        generatedSetterHelperImpl(endnotePr, PROPERTY_QNAME[79], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "endnotePr" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps addNewEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEdnDocProps)get_store().add_element_user(PROPERTY_QNAME[79]);
            return target;
        }
    }

    /**
     * Unsets the "endnotePr" element
     */
    @Override
    public void unsetEndnotePr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[79], 0);
        }
    }

    /**
     * Gets the "compat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat getCompat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat)get_store().find_element_user(PROPERTY_QNAME[80], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "compat" element
     */
    @Override
    public boolean isSetCompat() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[80]) != 0;
        }
    }

    /**
     * Sets the "compat" element
     */
    @Override
    public void setCompat(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat compat) {
        generatedSetterHelperImpl(compat, PROPERTY_QNAME[80], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "compat" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat addNewCompat() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCompat)get_store().add_element_user(PROPERTY_QNAME[80]);
            return target;
        }
    }

    /**
     * Unsets the "compat" element
     */
    @Override
    public void unsetCompat() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[80], 0);
        }
    }

    /**
     * Gets the "docVars" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars getDocVars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars)get_store().find_element_user(PROPERTY_QNAME[81], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "docVars" element
     */
    @Override
    public boolean isSetDocVars() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[81]) != 0;
        }
    }

    /**
     * Sets the "docVars" element
     */
    @Override
    public void setDocVars(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars docVars) {
        generatedSetterHelperImpl(docVars, PROPERTY_QNAME[81], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "docVars" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars addNewDocVars() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocVars)get_store().add_element_user(PROPERTY_QNAME[81]);
            return target;
        }
    }

    /**
     * Unsets the "docVars" element
     */
    @Override
    public void unsetDocVars() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[81], 0);
        }
    }

    /**
     * Gets the "rsids" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids getRsids() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids)get_store().find_element_user(PROPERTY_QNAME[82], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "rsids" element
     */
    @Override
    public boolean isSetRsids() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[82]) != 0;
        }
    }

    /**
     * Sets the "rsids" element
     */
    @Override
    public void setRsids(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids rsids) {
        generatedSetterHelperImpl(rsids, PROPERTY_QNAME[82], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "rsids" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids addNewRsids() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDocRsids)get_store().add_element_user(PROPERTY_QNAME[82]);
            return target;
        }
    }

    /**
     * Unsets the "rsids" element
     */
    @Override
    public void unsetRsids() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[82], 0);
        }
    }

    /**
     * Gets the "mathPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr getMathPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr)get_store().find_element_user(PROPERTY_QNAME[83], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "mathPr" element
     */
    @Override
    public boolean isSetMathPr() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[83]) != 0;
        }
    }

    /**
     * Sets the "mathPr" element
     */
    @Override
    public void setMathPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr mathPr) {
        generatedSetterHelperImpl(mathPr, PROPERTY_QNAME[83], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "mathPr" element
     */
    @Override
    public org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr addNewMathPr() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr target = null;
            target = (org.openxmlformats.schemas.officeDocument.x2006.math.CTMathPr)get_store().add_element_user(PROPERTY_QNAME[83]);
            return target;
        }
    }

    /**
     * Unsets the "mathPr" element
     */
    @Override
    public void unsetMathPr() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[83], 0);
        }
    }

    /**
     * Gets a List of "attachedSchema" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString> getAttachedSchemaList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getAttachedSchemaArray,
                this::setAttachedSchemaArray,
                this::insertNewAttachedSchema,
                this::removeAttachedSchema,
                this::sizeOfAttachedSchemaArray
            );
        }
    }

    /**
     * Gets array of all "attachedSchema" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] getAttachedSchemaArray() {
        return getXmlObjectArray(PROPERTY_QNAME[84], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[0]);
    }

    /**
     * Gets ith "attachedSchema" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getAttachedSchemaArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[84], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "attachedSchema" element
     */
    @Override
    public int sizeOfAttachedSchemaArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[84]);
        }
    }

    /**
     * Sets array of all "attachedSchema" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setAttachedSchemaArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString[] attachedSchemaArray) {
        check_orphaned();
        arraySetterHelper(attachedSchemaArray, PROPERTY_QNAME[84]);
    }

    /**
     * Sets ith "attachedSchema" element
     */
    @Override
    public void setAttachedSchemaArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString attachedSchema) {
        generatedSetterHelperImpl(attachedSchema, PROPERTY_QNAME[84], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "attachedSchema" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString insertNewAttachedSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().insert_element_user(PROPERTY_QNAME[84], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "attachedSchema" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewAttachedSchema() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[84]);
            return target;
        }
    }

    /**
     * Removes the ith "attachedSchema" element
     */
    @Override
    public void removeAttachedSchema(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[84], i);
        }
    }

    /**
     * Gets the "themeFontLang" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage getThemeFontLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage)get_store().find_element_user(PROPERTY_QNAME[85], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "themeFontLang" element
     */
    @Override
    public boolean isSetThemeFontLang() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[85]) != 0;
        }
    }

    /**
     * Sets the "themeFontLang" element
     */
    @Override
    public void setThemeFontLang(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage themeFontLang) {
        generatedSetterHelperImpl(themeFontLang, PROPERTY_QNAME[85], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "themeFontLang" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage addNewThemeFontLang() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTLanguage)get_store().add_element_user(PROPERTY_QNAME[85]);
            return target;
        }
    }

    /**
     * Unsets the "themeFontLang" element
     */
    @Override
    public void unsetThemeFontLang() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[85], 0);
        }
    }

    /**
     * Gets the "clrSchemeMapping" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping getClrSchemeMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping)get_store().find_element_user(PROPERTY_QNAME[86], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "clrSchemeMapping" element
     */
    @Override
    public boolean isSetClrSchemeMapping() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[86]) != 0;
        }
    }

    /**
     * Sets the "clrSchemeMapping" element
     */
    @Override
    public void setClrSchemeMapping(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping clrSchemeMapping) {
        generatedSetterHelperImpl(clrSchemeMapping, PROPERTY_QNAME[86], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "clrSchemeMapping" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping addNewClrSchemeMapping() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTColorSchemeMapping)get_store().add_element_user(PROPERTY_QNAME[86]);
            return target;
        }
    }

    /**
     * Unsets the "clrSchemeMapping" element
     */
    @Override
    public void unsetClrSchemeMapping() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[86], 0);
        }
    }

    /**
     * Gets the "doNotIncludeSubdocsInStats" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotIncludeSubdocsInStats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[87], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotIncludeSubdocsInStats" element
     */
    @Override
    public boolean isSetDoNotIncludeSubdocsInStats() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[87]) != 0;
        }
    }

    /**
     * Sets the "doNotIncludeSubdocsInStats" element
     */
    @Override
    public void setDoNotIncludeSubdocsInStats(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotIncludeSubdocsInStats) {
        generatedSetterHelperImpl(doNotIncludeSubdocsInStats, PROPERTY_QNAME[87], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotIncludeSubdocsInStats" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotIncludeSubdocsInStats() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[87]);
            return target;
        }
    }

    /**
     * Unsets the "doNotIncludeSubdocsInStats" element
     */
    @Override
    public void unsetDoNotIncludeSubdocsInStats() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[87], 0);
        }
    }

    /**
     * Gets the "doNotAutoCompressPictures" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[88], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotAutoCompressPictures" element
     */
    @Override
    public boolean isSetDoNotAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[88]) != 0;
        }
    }

    /**
     * Sets the "doNotAutoCompressPictures" element
     */
    @Override
    public void setDoNotAutoCompressPictures(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotAutoCompressPictures) {
        generatedSetterHelperImpl(doNotAutoCompressPictures, PROPERTY_QNAME[88], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotAutoCompressPictures" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[88]);
            return target;
        }
    }

    /**
     * Unsets the "doNotAutoCompressPictures" element
     */
    @Override
    public void unsetDoNotAutoCompressPictures() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[88], 0);
        }
    }

    /**
     * Gets the "forceUpgrade" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty getForceUpgrade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().find_element_user(PROPERTY_QNAME[89], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "forceUpgrade" element
     */
    @Override
    public boolean isSetForceUpgrade() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[89]) != 0;
        }
    }

    /**
     * Sets the "forceUpgrade" element
     */
    @Override
    public void setForceUpgrade(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty forceUpgrade) {
        generatedSetterHelperImpl(forceUpgrade, PROPERTY_QNAME[89], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "forceUpgrade" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty addNewForceUpgrade() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTEmpty)get_store().add_element_user(PROPERTY_QNAME[89]);
            return target;
        }
    }

    /**
     * Unsets the "forceUpgrade" element
     */
    @Override
    public void unsetForceUpgrade() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[89], 0);
        }
    }

    /**
     * Gets the "captions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions getCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions)get_store().find_element_user(PROPERTY_QNAME[90], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "captions" element
     */
    @Override
    public boolean isSetCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[90]) != 0;
        }
    }

    /**
     * Sets the "captions" element
     */
    @Override
    public void setCaptions(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions captions) {
        generatedSetterHelperImpl(captions, PROPERTY_QNAME[90], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "captions" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions addNewCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTCaptions)get_store().add_element_user(PROPERTY_QNAME[90]);
            return target;
        }
    }

    /**
     * Unsets the "captions" element
     */
    @Override
    public void unsetCaptions() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[90], 0);
        }
    }

    /**
     * Gets the "readModeInkLockDown" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown getReadModeInkLockDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown)get_store().find_element_user(PROPERTY_QNAME[91], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "readModeInkLockDown" element
     */
    @Override
    public boolean isSetReadModeInkLockDown() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[91]) != 0;
        }
    }

    /**
     * Sets the "readModeInkLockDown" element
     */
    @Override
    public void setReadModeInkLockDown(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown readModeInkLockDown) {
        generatedSetterHelperImpl(readModeInkLockDown, PROPERTY_QNAME[91], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "readModeInkLockDown" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown addNewReadModeInkLockDown() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTReadingModeInkLockDown)get_store().add_element_user(PROPERTY_QNAME[91]);
            return target;
        }
    }

    /**
     * Unsets the "readModeInkLockDown" element
     */
    @Override
    public void unsetReadModeInkLockDown() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[91], 0);
        }
    }

    /**
     * Gets a List of "smartTagType" elements
     */
    @Override
    public java.util.List<org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType> getSmartTagTypeList() {
        synchronized (monitor()) {
            check_orphaned();
            return new org.apache.xmlbeans.impl.values.JavaListXmlObject<>(
                this::getSmartTagTypeArray,
                this::setSmartTagTypeArray,
                this::insertNewSmartTagType,
                this::removeSmartTagType,
                this::sizeOfSmartTagTypeArray
            );
        }
    }

    /**
     * Gets array of all "smartTagType" elements
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType[] getSmartTagTypeArray() {
        return getXmlObjectArray(PROPERTY_QNAME[92], new org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType[0]);
    }

    /**
     * Gets ith "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType getSmartTagTypeArray(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType)get_store().find_element_user(PROPERTY_QNAME[92], i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }

    /**
     * Returns number of "smartTagType" element
     */
    @Override
    public int sizeOfSmartTagTypeArray() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[92]);
        }
    }

    /**
     * Sets array of all "smartTagType" element  WARNING: This method is not atomicaly synchronized.
     */
    @Override
    public void setSmartTagTypeArray(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType[] smartTagTypeArray) {
        check_orphaned();
        arraySetterHelper(smartTagTypeArray, PROPERTY_QNAME[92]);
    }

    /**
     * Sets ith "smartTagType" element
     */
    @Override
    public void setSmartTagTypeArray(int i, org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType smartTagType) {
        generatedSetterHelperImpl(smartTagType, PROPERTY_QNAME[92], i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }

    /**
     * Inserts and returns a new empty value (as xml) as the ith "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType insertNewSmartTagType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType)get_store().insert_element_user(PROPERTY_QNAME[92], i);
            return target;
        }
    }

    /**
     * Appends and returns a new empty value (as xml) as the last "smartTagType" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType addNewSmartTagType() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSmartTagType)get_store().add_element_user(PROPERTY_QNAME[92]);
            return target;
        }
    }

    /**
     * Removes the ith "smartTagType" element
     */
    @Override
    public void removeSmartTagType(int i) {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[92], i);
        }
    }

    /**
     * Gets the "schemaLibrary" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary getSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary)get_store().find_element_user(PROPERTY_QNAME[93], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "schemaLibrary" element
     */
    @Override
    public boolean isSetSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[93]) != 0;
        }
    }

    /**
     * Sets the "schemaLibrary" element
     */
    @Override
    public void setSchemaLibrary(org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary schemaLibrary) {
        generatedSetterHelperImpl(schemaLibrary, PROPERTY_QNAME[93], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "schemaLibrary" element
     */
    @Override
    public org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary addNewSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary target = null;
            target = (org.openxmlformats.schemas.schemaLibrary.x2006.main.CTSchemaLibrary)get_store().add_element_user(PROPERTY_QNAME[93]);
            return target;
        }
    }

    /**
     * Unsets the "schemaLibrary" element
     */
    @Override
    public void unsetSchemaLibrary() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[93], 0);
        }
    }

    /**
     * Gets the "shapeDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults getShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults)get_store().find_element_user(PROPERTY_QNAME[94], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "shapeDefaults" element
     */
    @Override
    public boolean isSetShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[94]) != 0;
        }
    }

    /**
     * Sets the "shapeDefaults" element
     */
    @Override
    public void setShapeDefaults(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults shapeDefaults) {
        generatedSetterHelperImpl(shapeDefaults, PROPERTY_QNAME[94], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "shapeDefaults" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults addNewShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShapeDefaults)get_store().add_element_user(PROPERTY_QNAME[94]);
            return target;
        }
    }

    /**
     * Unsets the "shapeDefaults" element
     */
    @Override
    public void unsetShapeDefaults() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[94], 0);
        }
    }

    /**
     * Gets the "doNotEmbedSmartTags" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff getDoNotEmbedSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().find_element_user(PROPERTY_QNAME[95], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "doNotEmbedSmartTags" element
     */
    @Override
    public boolean isSetDoNotEmbedSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[95]) != 0;
        }
    }

    /**
     * Sets the "doNotEmbedSmartTags" element
     */
    @Override
    public void setDoNotEmbedSmartTags(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff doNotEmbedSmartTags) {
        generatedSetterHelperImpl(doNotEmbedSmartTags, PROPERTY_QNAME[95], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "doNotEmbedSmartTags" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff addNewDoNotEmbedSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff)get_store().add_element_user(PROPERTY_QNAME[95]);
            return target;
        }
    }

    /**
     * Unsets the "doNotEmbedSmartTags" element
     */
    @Override
    public void unsetDoNotEmbedSmartTags() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[95], 0);
        }
    }

    /**
     * Gets the "decimalSymbol" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getDecimalSymbol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[96], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "decimalSymbol" element
     */
    @Override
    public boolean isSetDecimalSymbol() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[96]) != 0;
        }
    }

    /**
     * Sets the "decimalSymbol" element
     */
    @Override
    public void setDecimalSymbol(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString decimalSymbol) {
        generatedSetterHelperImpl(decimalSymbol, PROPERTY_QNAME[96], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "decimalSymbol" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewDecimalSymbol() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[96]);
            return target;
        }
    }

    /**
     * Unsets the "decimalSymbol" element
     */
    @Override
    public void unsetDecimalSymbol() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[96], 0);
        }
    }

    /**
     * Gets the "listSeparator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString getListSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().find_element_user(PROPERTY_QNAME[97], 0);
            return (target == null) ? null : target;
        }
    }

    /**
     * True if has "listSeparator" element
     */
    @Override
    public boolean isSetListSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            return get_store().count_elements(PROPERTY_QNAME[97]) != 0;
        }
    }

    /**
     * Sets the "listSeparator" element
     */
    @Override
    public void setListSeparator(org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString listSeparator) {
        generatedSetterHelperImpl(listSeparator, PROPERTY_QNAME[97], 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }

    /**
     * Appends and returns a new empty "listSeparator" element
     */
    @Override
    public org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString addNewListSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString target = null;
            target = (org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString)get_store().add_element_user(PROPERTY_QNAME[97]);
            return target;
        }
    }

    /**
     * Unsets the "listSeparator" element
     */
    @Override
    public void unsetListSeparator() {
        synchronized (monitor()) {
            check_orphaned();
            get_store().remove_element(PROPERTY_QNAME[97], 0);
        }
    }
}
