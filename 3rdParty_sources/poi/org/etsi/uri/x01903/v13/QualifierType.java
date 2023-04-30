/*
 * XML Type:  QualifierType
 * Namespace: http://uri.etsi.org/01903/v1.3.2#
 * Java type: org.etsi.uri.x01903.v13.QualifierType
 *
 * Automatically generated - do not modify.
 */
package org.etsi.uri.x01903.v13;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML QualifierType(@http://uri.etsi.org/01903/v1.3.2#).
 *
 * This is an atomic type that is a restriction of org.etsi.uri.x01903.v13.QualifierType.
 */
public interface QualifierType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.etsi.uri.x01903.v13.QualifierType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "qualifiertypec1c6type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum OID_AS_URI = Enum.forString("OIDAsURI");
    Enum OID_AS_URN = Enum.forString("OIDAsURN");

    int INT_OID_AS_URI = Enum.INT_OID_AS_URI;
    int INT_OID_AS_URN = Enum.INT_OID_AS_URN;

    /**
     * Enumeration value class for org.etsi.uri.x01903.v13.QualifierType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_OID_AS_URI
     * Enum.forString(s); // returns the enum value for a string
     * Enum.forInt(i); // returns the enum value for an int
     * </pre>
     * Enumeration objects are immutable singleton objects that
     * can be compared using == object equality. They have no
     * public constructor. See the constants defined within this
     * class for all the valid values.
     */
    final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase {
        /**
         * Returns the enum value for a string, or null if none.
         */
        public static Enum forString(java.lang.String s) {
            return (Enum)table.forString(s);
        }

        /**
         * Returns the enum value corresponding to an int, or null if none.
         */
        public static Enum forInt(int i) {
            return (Enum)table.forInt(i);
        }

        private Enum(java.lang.String s, int i) {
            super(s, i);
        }

        static final int INT_OID_AS_URI = 1;
        static final int INT_OID_AS_URN = 2;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("OIDAsURI", INT_OID_AS_URI),
            new Enum("OIDAsURN", INT_OID_AS_URN),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
