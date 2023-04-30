/*
 * XML Type:  CT_CacheHierarchy
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_CacheHierarchy(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is a complex type.
 */
public interface CTCacheHierarchy extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCacheHierarchy> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctcachehierarchy761ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "fieldsUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldsUsage getFieldsUsage();

    /**
     * True if has "fieldsUsage" element
     */
    boolean isSetFieldsUsage();

    /**
     * Sets the "fieldsUsage" element
     */
    void setFieldsUsage(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldsUsage fieldsUsage);

    /**
     * Appends and returns a new empty "fieldsUsage" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFieldsUsage addNewFieldsUsage();

    /**
     * Unsets the "fieldsUsage" element
     */
    void unsetFieldsUsage();

    /**
     * Gets the "groupLevels" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevels getGroupLevels();

    /**
     * True if has "groupLevels" element
     */
    boolean isSetGroupLevels();

    /**
     * Sets the "groupLevels" element
     */
    void setGroupLevels(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevels groupLevels);

    /**
     * Appends and returns a new empty "groupLevels" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGroupLevels addNewGroupLevels();

    /**
     * Unsets the "groupLevels" element
     */
    void unsetGroupLevels();

    /**
     * Gets the "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList getExtLst();

    /**
     * True if has "extLst" element
     */
    boolean isSetExtLst();

    /**
     * Sets the "extLst" element
     */
    void setExtLst(org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList extLst);

    /**
     * Appends and returns a new empty "extLst" element
     */
    org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList addNewExtLst();

    /**
     * Unsets the "extLst" element
     */
    void unsetExtLst();

    /**
     * Gets the "uniqueName" attribute
     */
    java.lang.String getUniqueName();

    /**
     * Gets (as xml) the "uniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetUniqueName();

    /**
     * Sets the "uniqueName" attribute
     */
    void setUniqueName(java.lang.String uniqueName);

    /**
     * Sets (as xml) the "uniqueName" attribute
     */
    void xsetUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring uniqueName);

    /**
     * Gets the "caption" attribute
     */
    java.lang.String getCaption();

    /**
     * Gets (as xml) the "caption" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetCaption();

    /**
     * True if has "caption" attribute
     */
    boolean isSetCaption();

    /**
     * Sets the "caption" attribute
     */
    void setCaption(java.lang.String caption);

    /**
     * Sets (as xml) the "caption" attribute
     */
    void xsetCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring caption);

    /**
     * Unsets the "caption" attribute
     */
    void unsetCaption();

    /**
     * Gets the "measure" attribute
     */
    boolean getMeasure();

    /**
     * Gets (as xml) the "measure" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMeasure();

    /**
     * True if has "measure" attribute
     */
    boolean isSetMeasure();

    /**
     * Sets the "measure" attribute
     */
    void setMeasure(boolean measure);

    /**
     * Sets (as xml) the "measure" attribute
     */
    void xsetMeasure(org.apache.xmlbeans.XmlBoolean measure);

    /**
     * Unsets the "measure" attribute
     */
    void unsetMeasure();

    /**
     * Gets the "set" attribute
     */
    boolean getSet();

    /**
     * Gets (as xml) the "set" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetSet();

    /**
     * True if has "set" attribute
     */
    boolean isSetSet();

    /**
     * Sets the "set" attribute
     */
    void setSet(boolean set);

    /**
     * Sets (as xml) the "set" attribute
     */
    void xsetSet(org.apache.xmlbeans.XmlBoolean set);

    /**
     * Unsets the "set" attribute
     */
    void unsetSet();

    /**
     * Gets the "parentSet" attribute
     */
    long getParentSet();

    /**
     * Gets (as xml) the "parentSet" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetParentSet();

    /**
     * True if has "parentSet" attribute
     */
    boolean isSetParentSet();

    /**
     * Sets the "parentSet" attribute
     */
    void setParentSet(long parentSet);

    /**
     * Sets (as xml) the "parentSet" attribute
     */
    void xsetParentSet(org.apache.xmlbeans.XmlUnsignedInt parentSet);

    /**
     * Unsets the "parentSet" attribute
     */
    void unsetParentSet();

    /**
     * Gets the "iconSet" attribute
     */
    int getIconSet();

    /**
     * Gets (as xml) the "iconSet" attribute
     */
    org.apache.xmlbeans.XmlInt xgetIconSet();

    /**
     * True if has "iconSet" attribute
     */
    boolean isSetIconSet();

    /**
     * Sets the "iconSet" attribute
     */
    void setIconSet(int iconSet);

    /**
     * Sets (as xml) the "iconSet" attribute
     */
    void xsetIconSet(org.apache.xmlbeans.XmlInt iconSet);

    /**
     * Unsets the "iconSet" attribute
     */
    void unsetIconSet();

    /**
     * Gets the "attribute" attribute
     */
    boolean getAttribute();

    /**
     * Gets (as xml) the "attribute" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetAttribute();

    /**
     * True if has "attribute" attribute
     */
    boolean isSetAttribute();

    /**
     * Sets the "attribute" attribute
     */
    void setAttribute(boolean attribute);

    /**
     * Sets (as xml) the "attribute" attribute
     */
    void xsetAttribute(org.apache.xmlbeans.XmlBoolean attribute);

    /**
     * Unsets the "attribute" attribute
     */
    void unsetAttribute();

    /**
     * Gets the "time" attribute
     */
    boolean getTime();

    /**
     * Gets (as xml) the "time" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetTime();

    /**
     * True if has "time" attribute
     */
    boolean isSetTime();

    /**
     * Sets the "time" attribute
     */
    void setTime(boolean time);

    /**
     * Sets (as xml) the "time" attribute
     */
    void xsetTime(org.apache.xmlbeans.XmlBoolean time);

    /**
     * Unsets the "time" attribute
     */
    void unsetTime();

    /**
     * Gets the "keyAttribute" attribute
     */
    boolean getKeyAttribute();

    /**
     * Gets (as xml) the "keyAttribute" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetKeyAttribute();

    /**
     * True if has "keyAttribute" attribute
     */
    boolean isSetKeyAttribute();

    /**
     * Sets the "keyAttribute" attribute
     */
    void setKeyAttribute(boolean keyAttribute);

    /**
     * Sets (as xml) the "keyAttribute" attribute
     */
    void xsetKeyAttribute(org.apache.xmlbeans.XmlBoolean keyAttribute);

    /**
     * Unsets the "keyAttribute" attribute
     */
    void unsetKeyAttribute();

    /**
     * Gets the "defaultMemberUniqueName" attribute
     */
    java.lang.String getDefaultMemberUniqueName();

    /**
     * Gets (as xml) the "defaultMemberUniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDefaultMemberUniqueName();

    /**
     * True if has "defaultMemberUniqueName" attribute
     */
    boolean isSetDefaultMemberUniqueName();

    /**
     * Sets the "defaultMemberUniqueName" attribute
     */
    void setDefaultMemberUniqueName(java.lang.String defaultMemberUniqueName);

    /**
     * Sets (as xml) the "defaultMemberUniqueName" attribute
     */
    void xsetDefaultMemberUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring defaultMemberUniqueName);

    /**
     * Unsets the "defaultMemberUniqueName" attribute
     */
    void unsetDefaultMemberUniqueName();

    /**
     * Gets the "allUniqueName" attribute
     */
    java.lang.String getAllUniqueName();

    /**
     * Gets (as xml) the "allUniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAllUniqueName();

    /**
     * True if has "allUniqueName" attribute
     */
    boolean isSetAllUniqueName();

    /**
     * Sets the "allUniqueName" attribute
     */
    void setAllUniqueName(java.lang.String allUniqueName);

    /**
     * Sets (as xml) the "allUniqueName" attribute
     */
    void xsetAllUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring allUniqueName);

    /**
     * Unsets the "allUniqueName" attribute
     */
    void unsetAllUniqueName();

    /**
     * Gets the "allCaption" attribute
     */
    java.lang.String getAllCaption();

    /**
     * Gets (as xml) the "allCaption" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetAllCaption();

    /**
     * True if has "allCaption" attribute
     */
    boolean isSetAllCaption();

    /**
     * Sets the "allCaption" attribute
     */
    void setAllCaption(java.lang.String allCaption);

    /**
     * Sets (as xml) the "allCaption" attribute
     */
    void xsetAllCaption(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring allCaption);

    /**
     * Unsets the "allCaption" attribute
     */
    void unsetAllCaption();

    /**
     * Gets the "dimensionUniqueName" attribute
     */
    java.lang.String getDimensionUniqueName();

    /**
     * Gets (as xml) the "dimensionUniqueName" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDimensionUniqueName();

    /**
     * True if has "dimensionUniqueName" attribute
     */
    boolean isSetDimensionUniqueName();

    /**
     * Sets the "dimensionUniqueName" attribute
     */
    void setDimensionUniqueName(java.lang.String dimensionUniqueName);

    /**
     * Sets (as xml) the "dimensionUniqueName" attribute
     */
    void xsetDimensionUniqueName(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring dimensionUniqueName);

    /**
     * Unsets the "dimensionUniqueName" attribute
     */
    void unsetDimensionUniqueName();

    /**
     * Gets the "displayFolder" attribute
     */
    java.lang.String getDisplayFolder();

    /**
     * Gets (as xml) the "displayFolder" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetDisplayFolder();

    /**
     * True if has "displayFolder" attribute
     */
    boolean isSetDisplayFolder();

    /**
     * Sets the "displayFolder" attribute
     */
    void setDisplayFolder(java.lang.String displayFolder);

    /**
     * Sets (as xml) the "displayFolder" attribute
     */
    void xsetDisplayFolder(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring displayFolder);

    /**
     * Unsets the "displayFolder" attribute
     */
    void unsetDisplayFolder();

    /**
     * Gets the "measureGroup" attribute
     */
    java.lang.String getMeasureGroup();

    /**
     * Gets (as xml) the "measureGroup" attribute
     */
    org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring xgetMeasureGroup();

    /**
     * True if has "measureGroup" attribute
     */
    boolean isSetMeasureGroup();

    /**
     * Sets the "measureGroup" attribute
     */
    void setMeasureGroup(java.lang.String measureGroup);

    /**
     * Sets (as xml) the "measureGroup" attribute
     */
    void xsetMeasureGroup(org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STXstring measureGroup);

    /**
     * Unsets the "measureGroup" attribute
     */
    void unsetMeasureGroup();

    /**
     * Gets the "measures" attribute
     */
    boolean getMeasures();

    /**
     * Gets (as xml) the "measures" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetMeasures();

    /**
     * True if has "measures" attribute
     */
    boolean isSetMeasures();

    /**
     * Sets the "measures" attribute
     */
    void setMeasures(boolean measures);

    /**
     * Sets (as xml) the "measures" attribute
     */
    void xsetMeasures(org.apache.xmlbeans.XmlBoolean measures);

    /**
     * Unsets the "measures" attribute
     */
    void unsetMeasures();

    /**
     * Gets the "count" attribute
     */
    long getCount();

    /**
     * Gets (as xml) the "count" attribute
     */
    org.apache.xmlbeans.XmlUnsignedInt xgetCount();

    /**
     * Sets the "count" attribute
     */
    void setCount(long count);

    /**
     * Sets (as xml) the "count" attribute
     */
    void xsetCount(org.apache.xmlbeans.XmlUnsignedInt count);

    /**
     * Gets the "oneField" attribute
     */
    boolean getOneField();

    /**
     * Gets (as xml) the "oneField" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetOneField();

    /**
     * True if has "oneField" attribute
     */
    boolean isSetOneField();

    /**
     * Sets the "oneField" attribute
     */
    void setOneField(boolean oneField);

    /**
     * Sets (as xml) the "oneField" attribute
     */
    void xsetOneField(org.apache.xmlbeans.XmlBoolean oneField);

    /**
     * Unsets the "oneField" attribute
     */
    void unsetOneField();

    /**
     * Gets the "memberValueDatatype" attribute
     */
    int getMemberValueDatatype();

    /**
     * Gets (as xml) the "memberValueDatatype" attribute
     */
    org.apache.xmlbeans.XmlUnsignedShort xgetMemberValueDatatype();

    /**
     * True if has "memberValueDatatype" attribute
     */
    boolean isSetMemberValueDatatype();

    /**
     * Sets the "memberValueDatatype" attribute
     */
    void setMemberValueDatatype(int memberValueDatatype);

    /**
     * Sets (as xml) the "memberValueDatatype" attribute
     */
    void xsetMemberValueDatatype(org.apache.xmlbeans.XmlUnsignedShort memberValueDatatype);

    /**
     * Unsets the "memberValueDatatype" attribute
     */
    void unsetMemberValueDatatype();

    /**
     * Gets the "unbalanced" attribute
     */
    boolean getUnbalanced();

    /**
     * Gets (as xml) the "unbalanced" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUnbalanced();

    /**
     * True if has "unbalanced" attribute
     */
    boolean isSetUnbalanced();

    /**
     * Sets the "unbalanced" attribute
     */
    void setUnbalanced(boolean unbalanced);

    /**
     * Sets (as xml) the "unbalanced" attribute
     */
    void xsetUnbalanced(org.apache.xmlbeans.XmlBoolean unbalanced);

    /**
     * Unsets the "unbalanced" attribute
     */
    void unsetUnbalanced();

    /**
     * Gets the "unbalancedGroup" attribute
     */
    boolean getUnbalancedGroup();

    /**
     * Gets (as xml) the "unbalancedGroup" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetUnbalancedGroup();

    /**
     * True if has "unbalancedGroup" attribute
     */
    boolean isSetUnbalancedGroup();

    /**
     * Sets the "unbalancedGroup" attribute
     */
    void setUnbalancedGroup(boolean unbalancedGroup);

    /**
     * Sets (as xml) the "unbalancedGroup" attribute
     */
    void xsetUnbalancedGroup(org.apache.xmlbeans.XmlBoolean unbalancedGroup);

    /**
     * Unsets the "unbalancedGroup" attribute
     */
    void unsetUnbalancedGroup();

    /**
     * Gets the "hidden" attribute
     */
    boolean getHidden();

    /**
     * Gets (as xml) the "hidden" attribute
     */
    org.apache.xmlbeans.XmlBoolean xgetHidden();

    /**
     * True if has "hidden" attribute
     */
    boolean isSetHidden();

    /**
     * Sets the "hidden" attribute
     */
    void setHidden(boolean hidden);

    /**
     * Sets (as xml) the "hidden" attribute
     */
    void xsetHidden(org.apache.xmlbeans.XmlBoolean hidden);

    /**
     * Unsets the "hidden" attribute
     */
    void unsetHidden();
}
