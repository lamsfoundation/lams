/*
 * XML Type:  ST_FtnEdn
 * Namespace: http://schemas.openxmlformats.org/wordprocessingml/2006/main
 * Java type: org.openxmlformats.schemas.wordprocessingml.x2006.main.STFtnEdn
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.wordprocessingml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_FtnEdn(@http://schemas.openxmlformats.org/wordprocessingml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.wordprocessingml.x2006.main.STFtnEdn.
 */
public interface STFtnEdn extends org.apache.xmlbeans.XmlString {
    SimpleTypeFactory<org.openxmlformats.schemas.wordprocessingml.x2006.main.STFtnEdn> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "stftnednd4c9type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum NORMAL = Enum.forString("normal");
    Enum SEPARATOR = Enum.forString("separator");
    Enum CONTINUATION_SEPARATOR = Enum.forString("continuationSeparator");
    Enum CONTINUATION_NOTICE = Enum.forString("continuationNotice");

    int INT_NORMAL = Enum.INT_NORMAL;
    int INT_SEPARATOR = Enum.INT_SEPARATOR;
    int INT_CONTINUATION_SEPARATOR = Enum.INT_CONTINUATION_SEPARATOR;
    int INT_CONTINUATION_NOTICE = Enum.INT_CONTINUATION_NOTICE;

    /**
     * Enumeration value class for org.openxmlformats.schemas.wordprocessingml.x2006.main.STFtnEdn.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_NORMAL
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

        static final int INT_NORMAL = 1;
        static final int INT_SEPARATOR = 2;
        static final int INT_CONTINUATION_SEPARATOR = 3;
        static final int INT_CONTINUATION_NOTICE = 4;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("normal", INT_NORMAL),
            new Enum("separator", INT_SEPARATOR),
            new Enum("continuationSeparator", INT_CONTINUATION_SEPARATOR),
            new Enum("continuationNotice", INT_CONTINUATION_NOTICE),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
