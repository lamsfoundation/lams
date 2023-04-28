/*
 * XML Type:  ST_MeasurementOrPercent
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STMeasurementOrPercent
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main.impl;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.QNameSet;
import org.apache.xmlbeans.XmlObject;

/**
 * An XML ST_MeasurementOrPercent(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is a union type. Instances are of one of the following types:
 *     org.openxmlformats.schemas.wordprocessingml.x2006.main.STUnqualifiedPercentage
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STPercentage
 *     org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUniversalMeasure
 */
public class STMeasurementOrPercentImpl extends org.apache.xmlbeans.impl.values.XmlUnionImpl implements org.openxmlformats.schemas.wordprocessingml.x2006.main.STMeasurementOrPercent, org.openxmlformats.schemas.wordprocessingml.x2006.main.STDecimalNumberOrPercent, org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STUniversalMeasure {
    private static final long serialVersionUID = 1L;

    public STMeasurementOrPercentImpl(org.apache.xmlbeans.SchemaType sType) {
        super(sType, false);
    }

    protected STMeasurementOrPercentImpl(org.apache.xmlbeans.SchemaType sType, boolean b) {
        super(sType, b);
    }
}
