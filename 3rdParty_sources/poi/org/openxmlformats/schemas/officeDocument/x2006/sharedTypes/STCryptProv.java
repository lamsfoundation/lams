/*
 * XML Type:  ST_CryptProv
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_CryptProv(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.
 */
public interface STCryptProv extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcryptprov36a7type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum RSA_AES = Enum.forString("rsaAES");
    Enum RSA_FULL = Enum.forString("rsaFull");
    Enum CUSTOM = Enum.forString("custom");

    int INT_RSA_AES = Enum.INT_RSA_AES;
    int INT_RSA_FULL = Enum.INT_RSA_FULL;
    int INT_CUSTOM = Enum.INT_CUSTOM;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STCryptProv.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_RSA_AES
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

        static final int INT_RSA_AES = 1;
        static final int INT_RSA_FULL = 2;
        static final int INT_CUSTOM = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("rsaAES", INT_RSA_AES),
            new Enum("rsaFull", INT_RSA_FULL),
            new Enum("custom", INT_CUSTOM),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
