/*
 * XML Type:  CT_OMathPara
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/math
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.math;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML CT_OMathPara(@http://schemas.openxmlformats.org/officeDocument/2006/math).
 *
 * This is a complex type.
 */
public interface CTOMathPara extends org.apache.xmlbeans.XmlObject {
    DocumentFactory<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara> Factory = new DocumentFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "ctomathpara8825type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    /**
     * Gets the "oMathParaPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr getOMathParaPr();

    /**
     * True if has "oMathParaPr" element
     */
    boolean isSetOMathParaPr();

    /**
     * Sets the "oMathParaPr" element
     */
    void setOMathParaPr(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr oMathParaPr);

    /**
     * Appends and returns a new empty "oMathParaPr" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathParaPr addNewOMathParaPr();

    /**
     * Unsets the "oMathParaPr" element
     */
    void unsetOMathParaPr();

    /**
     * Gets a List of "oMath" elements
     */
    java.util.List<org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath> getOMathList();

    /**
     * Gets array of all "oMath" elements
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] getOMathArray();

    /**
     * Gets ith "oMath" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath getOMathArray(int i);

    /**
     * Returns number of "oMath" element
     */
    int sizeOfOMathArray();

    /**
     * Sets array of all "oMath" element
     */
    void setOMathArray(org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath[] oMathArray);

    /**
     * Sets ith "oMath" element
     */
    void setOMathArray(int i, org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath oMath);

    /**
     * Inserts and returns a new empty value (as xml) as the ith "oMath" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath insertNewOMath(int i);

    /**
     * Appends and returns a new empty value (as xml) as the last "oMath" element
     */
    org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath addNewOMath();

    /**
     * Removes the ith "oMath" element
     */
    void removeOMath(int i);
}
