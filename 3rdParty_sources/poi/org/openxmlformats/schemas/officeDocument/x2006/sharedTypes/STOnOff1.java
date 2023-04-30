/*
 * XML Type:  ST_OnOff1
 * Namespace: http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes
 * Java type: org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.officeDocument.x2006.sharedTypes;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_OnOff1(@http://schemas.openxmlformats.org/officeDocument/2006/sharedTypes).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1.
 */
public interface STOnOff1 extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stonoff18ddbtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum ON = Enum.forString("on");
    Enum OFF = Enum.forString("off");

    int INT_ON = Enum.INT_ON;
    int INT_OFF = Enum.INT_OFF;

    /**
     * Enumeration value class for org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STOnOff1.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ON
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

        static final int INT_ON = 1;
        static final int INT_OFF = 2;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("on", INT_ON),
            new Enum("off", INT_OFF),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
