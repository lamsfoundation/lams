/*
 * XML Type:  ST_Comments
 * Namespace: http://schemas.openxmlformats.org/spreadsheetml/2006/main
 * Java type: org.openxmlformats.schemas.spreadsheetml.x2006.main.STComments
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.spreadsheetml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_Comments(@http://schemas.openxmlformats.org/spreadsheetml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.spreadsheetml.x2006.main.STComments.
 */
public interface STComments extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.spreadsheetml.x2006.main.STComments> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stcommentsdbcdtype");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum COMM_NONE = Enum.forString("commNone");
    Enum COMM_INDICATOR = Enum.forString("commIndicator");
    Enum COMM_IND_AND_COMMENT = Enum.forString("commIndAndComment");

    int INT_COMM_NONE = Enum.INT_COMM_NONE;
    int INT_COMM_INDICATOR = Enum.INT_COMM_INDICATOR;
    int INT_COMM_IND_AND_COMMENT = Enum.INT_COMM_IND_AND_COMMENT;

    /**
     * Enumeration value class for org.openxmlformats.schemas.spreadsheetml.x2006.main.STComments.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_COMM_NONE
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

        static final int INT_COMM_NONE = 1;
        static final int INT_COMM_INDICATOR = 2;
        static final int INT_COMM_IND_AND_COMMENT = 3;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("commNone", INT_COMM_NONE),
            new Enum("commIndicator", INT_COMM_INDICATOR),
            new Enum("commIndAndComment", INT_COMM_IND_AND_COMMENT),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
