/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.io;

import javax.xml.namespace.QName;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * An thread safe abstract unmarshaller. This unmarshaller will:
 * <ul>
 * <li>Unmarshalling namespace decleration attributes</li>
 * <li>Unmarshalling schema instance type (xsi:type) decleration attributes</li>
 * <li>Delegating to child classes element, text, and attribute processing</li>
 * </ul>
 * 
 * <strong>NOTE:</strong> In the case of Text nodes this unmarshaller will use {@link org.w3c.dom.Text#getWholeText()}
 * to retrieve the textual content. This is probably exceptable in almost all cases, if, however, you need to deal with
 * elements that contain multiple text node children you will need to override
 * {@link #unmarshallTextContent(XMLObject, Text)} and do "the right thing" for your implementation.
 */
public abstract class AbstractXMLObjectUnmarshaller implements Unmarshaller {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractXMLObjectUnmarshaller.class);

    /** The target name and namespace for this unmarshaller. */
    private QName targetQName;

    /** Factory for XMLObjectBuilders. */
    private XMLObjectBuilderFactory xmlObjectBuilderFactory;

    /** Factory for creating unmarshallers for child elements. */
    private UnmarshallerFactory unmarshallerFactory;

    /**
     * Constructor.
     */
    protected AbstractXMLObjectUnmarshaller() {
        xmlObjectBuilderFactory = Configuration.getBuilderFactory();
        unmarshallerFactory = Configuration.getUnmarshallerFactory();
    }

    /**
     * This constructor supports checking a DOM Element to be unmarshalled, either element name or schema type, against
     * a given namespace/local name pair.
     * 
     * @deprecated no replacement
     * 
     * @param targetNamespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     * @param targetLocalName the local name of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     */
    protected AbstractXMLObjectUnmarshaller(String targetNamespaceURI, String targetLocalName) {
        targetQName = XMLHelper.constructQName(targetNamespaceURI, targetLocalName, null);

        xmlObjectBuilderFactory = Configuration.getBuilderFactory();
        unmarshallerFactory = Configuration.getUnmarshallerFactory();
    }

    /** {@inheritDoc} */
    public XMLObject unmarshall(Element domElement) throws UnmarshallingException {
        log.trace("Starting to unmarshall DOM element {}", XMLHelper.getNodeQName(domElement));

        checkElementIsTarget(domElement);

        XMLObject xmlObject = buildXMLObject(domElement);

        log.trace("Unmarshalling attributes of DOM Element {}", XMLHelper.getNodeQName(domElement));
        NamedNodeMap attributes = domElement.getAttributes();
        Node attribute;
        for (int i = 0; i < attributes.getLength(); i++) {
            attribute = attributes.item(i);

            // These should allows be attribute nodes, but just in case...
            if (attribute.getNodeType() == Node.ATTRIBUTE_NODE) {
                unmarshallAttribute(xmlObject, (Attr) attribute);
            }
        }

        log.trace("Unmarshalling other child nodes of DOM Element {}", XMLHelper.getNodeQName(domElement));
        NodeList childNodes = domElement.getChildNodes();
        Node childNode;
        for (int i = 0; i < childNodes.getLength(); i++) {
            childNode = childNodes.item(i);

            if (childNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                unmarshallAttribute(xmlObject, (Attr) childNode);
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                unmarshallChildElement(xmlObject, (Element) childNode);
            } else if (childNode.getNodeType() == Node.TEXT_NODE) {
                unmarshallTextContent(xmlObject, (Text) childNode);
            }
        }

        xmlObject.setDOM(domElement);
        return xmlObject;
    }

    /**
     * Checks that the given DOM Element's XSI type or namespace qualified element name matches the target QName of this
     * unmarshaller.
     * 
     * @param domElement the DOM element to check
     * 
     * @throws UnmarshallingException thrown if the DOM Element does not match the target of this unmarshaller
     */
    protected void checkElementIsTarget(Element domElement) throws UnmarshallingException {
        QName elementName = XMLHelper.getNodeQName(domElement);

        if (targetQName == null) {
            log.trace(
                    "Targeted QName checking is not available for this unmarshaller, DOM Element {} was not verified",
                    elementName);
            return;
        }

        log.trace("Checking that {} meets target criteria.", elementName);

        QName type = XMLHelper.getXSIType(domElement);

        if (type != null && type.equals(targetQName)) {
            log.trace("{} schema type matches target.", elementName);
            return;
        } else {
            if (elementName.equals(targetQName)) {
                log.trace("{} element name matches target.", elementName);
                return;
            } else {
                String errorMsg = "This unmarshaller only operates on " + targetQName + " elements not " + elementName;
                log.error(errorMsg);
                throw new UnmarshallingException(errorMsg);
            }
        }
    }

    /**
     * Constructs the XMLObject that the given DOM Element will be unmarshalled into. If the DOM element has an XML
     * Schema type defined this method will attempt to retrieve an XMLObjectBuilder, from the factory given at
     * construction time, using the schema type. If no schema type is present or no builder is registered with the
     * factory for the schema type, the elements QName is used. Once the builder is found the XMLObject is create by
     * invoking {@link XMLObjectBuilder#buildObject(String, String, String)}. Extending classes may wish to override
     * this logic if more than just schema type or element name (e.g. element attributes or content) need to be used to
     * determine which XMLObjectBuilder should be used to create the XMLObject.
     * 
     * @param domElement the DOM Element the created XMLObject will represent
     * 
     * @return the empty XMLObject that DOM Element can be unmarshalled into
     * 
     * @throws UnmarshallingException thrown if there is now XMLObjectBuilder registered for the given DOM Element
     */
    protected XMLObject buildXMLObject(Element domElement) throws UnmarshallingException {
        log.trace("Building XMLObject for {}", XMLHelper.getNodeQName(domElement));
        XMLObjectBuilder xmlObjectBuilder;

        xmlObjectBuilder = xmlObjectBuilderFactory.getBuilder(domElement);
        if (xmlObjectBuilder == null) {
            xmlObjectBuilder = xmlObjectBuilderFactory.getBuilder(Configuration.getDefaultProviderQName());
            if (xmlObjectBuilder == null) {
                String errorMsg = "Unable to located builder for " + XMLHelper.getNodeQName(domElement);
                log.error(errorMsg);
                throw new UnmarshallingException(errorMsg);
            } else {
                log.trace("No builder was registered for {} but the default builder {} was available, using it.",
                        XMLHelper.getNodeQName(domElement), xmlObjectBuilder.getClass().getName());
            }
        }

        return xmlObjectBuilder.buildObject(domElement);
    }

    /**
     * Unmarshalls the attributes from the given DOM Attr into the given XMLObject. If the attribute is an XML namespace
     * declaration the attribute is passed to
     * {@link AbstractXMLObjectUnmarshaller#unmarshallNamespaceAttribute(XMLObject, Attr)}. If it is an schema type
     * decleration (xsi:type) it is ignored because this attribute is handled by {@link #buildXMLObject(Element)}. All
     * other attributes are passed to the {@link #processAttribute(XMLObject, Attr)}
     * 
     * @param attribute the attribute to be unmarshalled
     * @param xmlObject the XMLObject that will recieve information from the DOM attribute
     * 
     * @throws UnmarshallingException thrown if there is a problem unmarshalling an attribute
     */
    protected void unmarshallAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException {
        QName attribName = XMLHelper.getNodeQName(attribute);
        log.trace("Pre-processing attribute {}", attribName);
        String attributeNamespace = DatatypeHelper.safeTrimOrNullString(attribute.getNamespaceURI());
        
        if (DatatypeHelper.safeEquals(attributeNamespace, XMLConstants.XMLNS_NS)) {
            unmarshallNamespaceAttribute(xmlObject, attribute);
        } else if (DatatypeHelper.safeEquals(attributeNamespace, XMLConstants.XSI_NS)) {
            unmarshallSchemaInstanceAttributes(xmlObject, attribute);
        } else {
            log.trace("Attribute {} is neither a schema type nor namespace, calling processAttribute()", XMLHelper
                    .getNodeQName(attribute));
            String attributeNSURI = attribute.getNamespaceURI();
            String attributeNSPrefix;
            if (attributeNSURI != null) {
                attributeNSPrefix = attribute.lookupPrefix(attributeNSURI);
                if (attributeNSPrefix == null && XMLConstants.XML_NS.equals(attributeNSURI)) {
                    attributeNSPrefix = XMLConstants.XML_PREFIX;
                }
                xmlObject.getNamespaceManager().registerAttributeName(attribName);
            }

            checkIDAttribute(attribute);

            processAttribute(xmlObject, attribute);
        }
    }

    /**
     * Unmarshalls a namespace declaration attribute.
     * 
     * @param xmlObject the xmlObject to recieve the namespace decleration
     * @param attribute the namespace decleration attribute
     */
    protected void unmarshallNamespaceAttribute(XMLObject xmlObject, Attr attribute) {
        log.trace("{} is a namespace declaration, adding it to the list of namespaces on the XMLObject", XMLHelper
                .getNodeQName(attribute));
        Namespace namespace;
        if(DatatypeHelper.safeEquals(attribute.getLocalName(), XMLConstants.XMLNS_PREFIX)){
            namespace = new Namespace(attribute.getValue(), null);
        }else{
            namespace = new Namespace(attribute.getValue(), attribute.getLocalName());
        }
        namespace.setAlwaysDeclare(true);
        xmlObject.getNamespaceManager().registerNamespaceDeclaration(namespace);
    }

    /**
     * Unmarshalls the XSI type, schemaLocation, and noNamespaceSchemaLocation attributes.
     * 
     * @param xmlObject the xmlObject to recieve the namespace decleration
     * @param attribute the namespace decleration attribute
     */
    protected void unmarshallSchemaInstanceAttributes(XMLObject xmlObject, Attr attribute) {
        QName attribName = XMLHelper.getNodeQName(attribute);
        if (XMLConstants.XSI_TYPE_ATTRIB_NAME.equals(attribName)) {
            log.trace("Saw XMLObject {} with an xsi:type of: {}", xmlObject.getElementQName(), attribute.getValue());
        } else if (XMLConstants.XSI_SCHEMA_LOCATION_ATTRIB_NAME.equals(attribName)) {
            log.trace("Saw XMLObject {} with an xsi:schemaLocation of: {}", xmlObject.getElementQName(), 
                    attribute.getValue());
            xmlObject.setSchemaLocation(attribute.getValue());
        } else if (XMLConstants.XSI_NO_NAMESPACE_SCHEMA_LOCATION_ATTRIB_NAME.equals(attribName)) {
            log.trace("Saw XMLObject {} with an xsi:noNamespaceSchemaLocation of: {}", xmlObject.getElementQName(), 
                    attribute.getValue());
            xmlObject.setNoNamespaceSchemaLocation(attribute.getValue());
        } else if (XMLConstants.XSI_NIL_ATTRIB_NAME.equals(attribName)) {
            log.trace("Saw XMLObject {} with an xsi:nil of: {}", xmlObject.getElementQName(), 
                    attribute.getValue());
            xmlObject.setNil(XSBooleanValue.valueOf(attribute.getValue()));
        }
    }

    /**
     * Check whether the attribute's QName is registered in the global ID attribute registry. If it is, and the
     * specified attribute's DOM Level 3 Attr.isId() is false (due to lack of schema validation, for example), then
     * declare the attribute as an ID type in the DOM on the attribute's owning element. This is to handle cases where
     * the underlying DOM needs to accurately reflect an attribute's ID-ness, for example ID reference resolution within
     * the Apache XML Security library.
     * 
     * @param attribute the DOM attribute to be checked
     */
    protected void checkIDAttribute(Attr attribute) {
        QName attribName = XMLHelper.getNodeQName(attribute);
        if (Configuration.isIDAttribute(attribName) && !attribute.isId()) {
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        }
    }

    /**
     * Unmarshalls given Element's children. For each child an unmarshaller is retrieved using
     * {@link UnmarshallerFactory#getUnmarshaller(Element)}. The unmarshaller is then used to unmarshall the child
     * element and the resultant XMLObject is passed to {@link #processChildElement(XMLObject, XMLObject)} for further
     * processing.
     * 
     * @param xmlObject the parent object of the unmarshalled children
     * @param childElement the child element to be unmarshalled
     * 
     * @throws UnmarshallingException thrown if an error occurs unmarshalling the chilren elements
     */
    protected void unmarshallChildElement(XMLObject xmlObject, Element childElement) throws UnmarshallingException {
        log.trace("Unmarshalling child elements of XMLObject {}", xmlObject.getElementQName());

        Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(childElement);

        if (unmarshaller == null) {
            unmarshaller = unmarshallerFactory.getUnmarshaller(Configuration.getDefaultProviderQName());
            if (unmarshaller == null) {
                String errorMsg = "No unmarshaller available for " + XMLHelper.getNodeQName(childElement)
                        + ", child of " + xmlObject.getElementQName();
                log.error(errorMsg);
                throw new UnmarshallingException(errorMsg);
            } else {
                log.trace("No unmarshaller was registered for {}, child of {}. Using default unmarshaller.", XMLHelper
                        .getNodeQName(childElement), xmlObject.getElementQName());
            }
        }

        log.trace("Unmarshalling child element {}with unmarshaller {}", XMLHelper.getNodeQName(childElement),
                unmarshaller.getClass().getName());
        processChildElement(xmlObject, unmarshaller.unmarshall(childElement));
    }

    /**
     * Unmarshalls the given Text node into a usable string by way of {@link Text#getWholeText()} and passes it off to
     * {@link AbstractXMLObjectUnmarshaller#processElementContent(XMLObject, String)} if the string is not null and
     * contains something other than whitespace.
     * 
     * @param xmlObject the XMLObject recieving the element content
     * @param content the textual content
     * 
     * @throws UnmarshallingException thrown if there is a problem unmarshalling the text node
     */
    protected void unmarshallTextContent(XMLObject xmlObject, Text content) throws UnmarshallingException {
        String textContent = DatatypeHelper.safeTrimOrNullString(content.getWholeText());
        if (textContent != null) {
            processElementContent(xmlObject, textContent);
        }
    }

    /**
     * Called after a child element has been unmarshalled so that it can be added to the parent XMLObject.
     * 
     * @param parentXMLObject the parent XMLObject
     * @param childXMLObject the child XMLObject
     * 
     * @throws UnmarshallingException thrown if there is a problem adding the child to the parent
     */
    protected abstract void processChildElement(XMLObject parentXMLObject, XMLObject childXMLObject)
            throws UnmarshallingException;

    /**
     * Called after an attribute has been unmarshalled so that it can be added to the XMLObject.
     * 
     * @param xmlObject the XMLObject
     * @param attribute the attribute
     * 
     * @throws UnmarshallingException thrown if there is a problem adding the attribute to the XMLObject
     */
    protected abstract void processAttribute(XMLObject xmlObject, Attr attribute) throws UnmarshallingException;

    /**
     * Called if the element being unmarshalled contained textual content so that it can be added to the XMLObject.
     * 
     * @param xmlObject XMLObject the content will be given to
     * @param elementContent the Element's content
     */
    protected abstract void processElementContent(XMLObject xmlObject, String elementContent);
}