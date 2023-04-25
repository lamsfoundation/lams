/*
 * XML Type:  ST_ExternalConnectionType
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_ExternalConnectionType(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType.
 */
public interface STExternalConnectionType extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stexternalconnectiontypee83ctype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum GENERAL = Enum.forString("general");
    Enum TEXT = Enum.forString("text");
    Enum MDY = Enum.forString("MDY");
    Enum DMY = Enum.forString("DMY");
    Enum YMD = Enum.forString("YMD");
    Enum MYD = Enum.forString("MYD");
    Enum DYM = Enum.forString("DYM");
    Enum YDM = Enum.forString("YDM");
    Enum SKIP = Enum.forString("skip");
    Enum EMD = Enum.forString("EMD");

    int INT_GENERAL = Enum.INT_GENERAL;
    int INT_TEXT = Enum.INT_TEXT;
    int INT_MDY = Enum.INT_MDY;
    int INT_DMY = Enum.INT_DMY;
    int INT_YMD = Enum.INT_YMD;
    int INT_MYD = Enum.INT_MYD;
    int INT_DYM = Enum.INT_DYM;
    int INT_YDM = Enum.INT_YDM;
    int INT_SKIP = Enum.INT_SKIP;
    int INT_EMD = Enum.INT_EMD;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STExternalConnectionType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_GENERAL
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

        static final int INT_GENERAL = 1;
        static final int INT_TEXT = 2;
        static final int INT_MDY = 3;
        static final int INT_DMY = 4;
        static final int INT_YMD = 5;
        static final int INT_MYD = 6;
        static final int INT_DYM = 7;
        static final int INT_YDM = 8;
        static final int INT_SKIP = 9;
        static final int INT_EMD = 10;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("general", INT_GENERAL),
            new Enum("text", INT_TEXT),
            new Enum("MDY", INT_MDY),
            new Enum("DMY", INT_DMY),
            new Enum("YMD", INT_YMD),
            new Enum("MYD", INT_MYD),
            new Enum("DYM", INT_DYM),
            new Enum("YDM", INT_YDM),
            new Enum("skip", INT_SKIP),
            new Enum("EMD", INT_EMD),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
