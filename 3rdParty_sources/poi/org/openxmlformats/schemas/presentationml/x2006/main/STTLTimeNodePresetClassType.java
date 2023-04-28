/*
 * XML Type:  ST_TLTimeNodePresetClassType
 * Namespace: http://schemas.openxmlformats.org/presentationml/2006/main
 * Java type: org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType
 *
 * Automatically generated - do not modify.
 */
package org.openxmlformats.schemas.presentationml.x2006.main;

import org.apache.xmlbeans.impl.schema.ElementFactory;
import org.apache.xmlbeans.impl.schema.AbstractDocumentFactory;
import org.apache.xmlbeans.impl.schema.DocumentFactory;
import org.apache.xmlbeans.impl.schema.SimpleTypeFactory;


/**
 * An XML ST_TLTimeNodePresetClassType(@http://schemas.openxmlformats.org/presentationml/2006/main).
 *
 * This is an atomic type that is a restriction of org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType.
 */
public interface STTLTimeNodePresetClassType extends org.apache.xmlbeans.XmlToken {
    SimpleTypeFactory<org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType> Factory = new SimpleTypeFactory<>(org.apache.poi.schemas.ooxml.system.ooxml.TypeSystemHolder.typeSystem, "sttltimenodepresetclasstypef353type");
    org.apache.xmlbeans.SchemaType type = Factory.getType();


    org.apache.xmlbeans.StringEnumAbstractBase getEnumValue();
    void setEnumValue(org.apache.xmlbeans.StringEnumAbstractBase e);

    Enum ENTR = Enum.forString("entr");
    Enum EXIT = Enum.forString("exit");
    Enum EMPH = Enum.forString("emph");
    Enum PATH = Enum.forString("path");
    Enum VERB = Enum.forString("verb");
    Enum MEDIACALL = Enum.forString("mediacall");

    int INT_ENTR = Enum.INT_ENTR;
    int INT_EXIT = Enum.INT_EXIT;
    int INT_EMPH = Enum.INT_EMPH;
    int INT_PATH = Enum.INT_PATH;
    int INT_VERB = Enum.INT_VERB;
    int INT_MEDIACALL = Enum.INT_MEDIACALL;

    /**
     * Enumeration value class for org.openxmlformats.schemas.presentationml.x2006.main.STTLTimeNodePresetClassType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_ENTR
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

        static final int INT_ENTR = 1;
        static final int INT_EXIT = 2;
        static final int INT_EMPH = 3;
        static final int INT_PATH = 4;
        static final int INT_VERB = 5;
        static final int INT_MEDIACALL = 6;

        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table(new Enum[] {
            new Enum("entr", INT_ENTR),
            new Enum("exit", INT_EXIT),
            new Enum("emph", INT_EMPH),
            new Enum("path", INT_PATH),
            new Enum("verb", INT_VERB),
            new Enum("mediacall", INT_MEDIACALL),
        });
        private static final long serialVersionUID = 1L;
        private java.lang.Object readResolve() {
            return forInt(intValue());
        }
    }
}
