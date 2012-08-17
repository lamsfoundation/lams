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

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opensaml.xml.Configuration;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.parse.XMLParserException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A thread safe, abstract implementation of the {@link org.opensaml.xml.io.Marshaller} interface. This class handles
 * most of the boilerplate code:
 * <ul>
 * <li>Ensuring elements to be marshalled are of either the correct xsi:type or element QName</li>
 * <li>Setting the appropriate namespace and prefix for the marshalled element</li>
 * <li>Setting the xsi:type for the element if the element has an explicit type</li>
 * <li>Setting namespaces attributes declared for the element</li>
 * <li>Marshalling of child elements</li>
 * </ul>
 */
public abstract class AbstractXMLObjectMarshaller implements Marshaller {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractXMLObjectMarshaller.class);

    /** The target name and namespace for this marshaller. */
    private QName targetQName;

    /** Factory for XMLObject Marshallers. */
    private MarshallerFactory marshallerFactory;

    /** Constructor. */
    protected AbstractXMLObjectMarshaller() {
        marshallerFactory = Configuration.getMarshallerFactory();
    }

    /**
     * This constructor supports checking an XMLObject to be marshalled, either element name or schema type, against a
     * given namespace/local name pair.
     * 
     * @deprecated no replacement
     * 
     * @param targetNamespaceURI the namespace URI of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     * @param targetLocalName the local name of either the schema type QName or element QName of the elements this
     *            unmarshaller operates on
     */
    protected AbstractXMLObjectMarshaller(String targetNamespaceURI, String targetLocalName) {
        targetQName = XMLHelper.constructQName(targetNamespaceURI, targetLocalName, null);

        marshallerFactory = Configuration.getMarshallerFactory();
    }

    /** {@inheritDoc} */
    public Element marshall(XMLObject xmlObject) throws MarshallingException {
        try {
            Document document = Configuration.getParserPool().newDocument();
            return marshall(xmlObject, document);
        } catch (XMLParserException e) {
            throw new MarshallingException("Unable to create Document to place marshalled elements in", e);
        }
    }

    /** {@inheritDoc} */
    public Element marshall(XMLObject xmlObject, Document document) throws MarshallingException {
        Element domElement;

        log.trace("Starting to marshall {}", xmlObject.getElementQName());

        if (document == null) {
            throw new MarshallingException("Given document may not be null");
        }

        checkXMLObjectIsTarget(xmlObject);

        log.trace("Checking if {} contains a cached DOM representation", xmlObject.getElementQName());
        domElement = xmlObject.getDOM();
        if (domElement != null) {

            prepareForAdoption(xmlObject);

            if (domElement.getOwnerDocument() != document) {
                log.trace("Adopting DOM of XMLObject into given Document");
                XMLHelper.adoptElement(domElement, document);
            }

            log.trace("Setting DOM of XMLObject as document element of given Document");
            setDocumentElement(document, domElement);

            return domElement;
        }

        log.trace("{} does not contain a cached DOM representation. Creating Element to marshall into.",
                xmlObject.getElementQName());
        domElement = XMLHelper.constructElement(document, xmlObject.getElementQName());

        log.trace("Setting created element as document root");
        // we need to do this before the rest of the marshalling so that signing and other ID dependent operations have
        // a path to the document root
        setDocumentElement(document, domElement);

        domElement = marshallInto(xmlObject, domElement);

        log.trace("Setting created element to DOM cache for XMLObject {}", xmlObject.getElementQName());
        xmlObject.setDOM(domElement);
        xmlObject.releaseParentDOM(true);

        return domElement;
    }

    /** {@inheritDoc} */
    public Element marshall(XMLObject xmlObject, Element parentElement) throws MarshallingException {
        Element domElement;

        log.trace("Starting to marshall {} as child of {}", xmlObject.getElementQName(),
                XMLHelper.getNodeQName(parentElement));

        if (parentElement == null) {
            throw new MarshallingException("Given parent element is null");
        }

        checkXMLObjectIsTarget(xmlObject);

        log.trace("Checking if {} contains a cached DOM representation", xmlObject.getElementQName());
        domElement = xmlObject.getDOM();
        if (domElement != null) {
            log.trace("{} contains a cached DOM representation", xmlObject.getElementQName());

            prepareForAdoption(xmlObject);

            log.trace("Appending DOM of XMLObject {} as child of parent element {}", xmlObject.getElementQName(),
                    XMLHelper.getNodeQName(parentElement));
            XMLHelper.appendChildElement(parentElement, domElement);

            return domElement;
        }

        log.trace("{} does not contain a cached DOM representation. Creating Element to marshall into.",
                xmlObject.getElementQName());
        Document owningDocument = parentElement.getOwnerDocument();
        domElement = XMLHelper.constructElement(owningDocument, xmlObject.getElementQName());

        log.trace("Appending newly created element to given parent element");
        // we need to do this before the rest of the marshalling so that signing and other ID dependent operations have
        // a path to the document root
        XMLHelper.appendChildElement(parentElement, domElement);
        domElement = marshallInto(xmlObject, domElement);

        log.trace("Setting created element to DOM cache for XMLObject {}", xmlObject.getElementQName());
        xmlObject.setDOM(domElement);
        xmlObject.releaseParentDOM(true);

        return domElement;

    }

    /**
     * Sets the given element as the Document Element of the given Document. If the document already has a Document
     * Element it is replaced by the given element.
     * 
     * @param document the document
     * @param element the Element that will serve as the Document Element
     */
    protected void setDocumentElement(Document document, Element element) {
        Element documentRoot = document.getDocumentElement();
        if (documentRoot != null) {
            document.replaceChild(element, documentRoot);
        } else {
            document.appendChild(element);
        }
    }

    /**
     * Marshalls the given XMLObject into the given DOM Element. The DOM Element must be within a DOM tree whose root is
     * the Document Element of the Document that owns the given DOM Element.
     * 
     * @param xmlObject the XMLObject to marshall
     * @param targetElement the Element into which the XMLObject is marshalled into
     * 
     * @return the DOM element the {@link XMLObject} is marshalled into
     * 
     * @throws MarshallingException thrown if there is a problem marshalling the object
     */
    protected Element marshallInto(XMLObject xmlObject, Element targetElement) throws MarshallingException {
        log.trace("Setting namespace prefix for {} for XMLObject {}", xmlObject.getElementQName().getPrefix(),
                xmlObject.getElementQName());

        marshallNamespacePrefix(xmlObject, targetElement);

        marshallSchemaInstanceAttributes(xmlObject, targetElement);

        marshallNamespaces(xmlObject, targetElement);

        marshallAttributes(xmlObject, targetElement);

        marshallChildElements(xmlObject, targetElement);

        marshallElementContent(xmlObject, targetElement);

        return targetElement;
    }

    /**
     * Checks to make sure the given XMLObject's schema type or element QName matches the target parameters given at
     * marshaller construction time.
     * 
     * @param xmlObject the XMLObject to marshall
     * 
     * @throws MarshallingException thrown if the given object is not or the required type
     */
    protected void checkXMLObjectIsTarget(XMLObject xmlObject) throws MarshallingException {
        if (targetQName == null) {
            log.trace("Targeted QName checking is not available for this marshaller, XMLObject {} was not verified",
                    xmlObject.getElementQName());
            return;
        }

        log.trace("Checking that {} meets target criteria", xmlObject.getElementQName());
        QName type = xmlObject.getSchemaType();
        if (type != null && type.equals(targetQName)) {
            log.trace("{} schema type matches target", xmlObject.getElementQName());
            return;
        } else {
            QName elementQName = xmlObject.getElementQName();
            if (elementQName.equals(targetQName)) {
                log.trace("{} element QName matches target", xmlObject.getElementQName());
                return;
            }
        }

        String errorMsg =
                "This marshaller only operations on " + targetQName + " elements not " + xmlObject.getElementQName();
        log.error(errorMsg);
        throw new MarshallingException(errorMsg);
    }

    /**
     * Marshalls the namespace prefix of the XMLObject into the DOM element.
     * 
     * @param xmlObject the XMLObject being marshalled
     * @param domElement the DOM element the XMLObject is being marshalled into
     */
    protected void marshallNamespacePrefix(XMLObject xmlObject, Element domElement) {
        String prefix = xmlObject.getElementQName().getPrefix();
        prefix = DatatypeHelper.safeTrimOrNullString(prefix);

        if (prefix != null) {
            domElement.setPrefix(prefix);
        }
    }

    /**
     * Marshalls the child elements of the given XMLObject.
     * 
     * @param xmlObject the XMLObject whose children will be marshalled
     * @param domElement the DOM element that will recieved the marshalled children
     * 
     * @throws MarshallingException thrown if there is a problem marshalling a child element
     */
    protected void marshallChildElements(XMLObject xmlObject, Element domElement) throws MarshallingException {
        log.trace("Marshalling child elements for XMLObject {}", xmlObject.getElementQName());

        List<XMLObject> childXMLObjects = xmlObject.getOrderedChildren();
        if (childXMLObjects != null && childXMLObjects.size() > 0) {
            for (XMLObject childXMLObject : childXMLObjects) {
                if (childXMLObject == null) {
                    continue;
                }

                log.trace("Getting marshaller for child XMLObject {}", childXMLObject.getElementQName());
                Marshaller marshaller = marshallerFactory.getMarshaller(childXMLObject);

                if (marshaller == null) {
                    marshaller = marshallerFactory.getMarshaller(Configuration.getDefaultProviderQName());

                    if (marshaller == null) {
                        String errorMsg =
                                "No marshaller available for " + childXMLObject.getElementQName() + ", child of "
                                        + xmlObject.getElementQName();
                        log.error(errorMsg);
                        throw new MarshallingException(errorMsg);
                    } else {
                        log.trace("No marshaller was registered for {}, child of {}. Using default marshaller",
                                childXMLObject.getElementQName(), xmlObject.getElementQName());
                    }
                }

                log.trace("Marshalling {} and adding it to DOM", childXMLObject.getElementQName());
                marshaller.marshall(childXMLObject, domElement);
            }
        } else {
            log.trace("No child elements to marshall for XMLObject {}", xmlObject.getElementQName());
        }
    }

    /**
     * Creates the xmlns attributes for any namespaces set on the given XMLObject.
     * 
     * @param xmlObject the XMLObject
     * @param domElement the DOM element the namespaces will be added to
     */
    protected void marshallNamespaces(XMLObject xmlObject, Element domElement) {
        log.trace("Marshalling namespace attributes for XMLObject {}", xmlObject.getElementQName());
        Set<Namespace> namespaces = xmlObject.getNamespaces();

        for (Namespace namespace : namespaces) {
            if (!namespace.alwaysDeclare()) {
                if (DatatypeHelper.safeEquals(namespace.getNamespacePrefix(), XMLConstants.XML_PREFIX)
                        || DatatypeHelper.safeEquals(namespace.getNamespaceURI(), XMLConstants.XML_NS)) {
                    // the "xml" namespace never needs to be declared
                    continue;
                }

                String declared = XMLHelper.lookupNamespaceURI(domElement, namespace.getNamespacePrefix());
                if (declared != null && namespace.getNamespaceURI().equals(declared)) {
                    log.trace("Namespace {} has already been declared on an ancestor of {} no need to add it here",
                            namespace, xmlObject.getElementQName());
                    continue;
                }
            }
            log.trace("Adding namespace declaration {} to {}", namespace, xmlObject.getElementQName());
            String nsURI = DatatypeHelper.safeTrimOrNullString(namespace.getNamespaceURI());
            String nsPrefix = DatatypeHelper.safeTrimOrNullString(namespace.getNamespacePrefix());

            XMLHelper.appendNamespaceDeclaration(domElement, nsURI, nsPrefix);
        }
    }

    /**
     * Creates the XSI type, schemaLocation, and noNamespaceSchemaLocation attributes for an XMLObject.
     * 
     * @param xmlObject the XMLObject
     * @param domElement the DOM element the namespaces will be added to
     * 
     * @throws MarshallingException thrown if the schema type information is invalid
     */
    protected void marshallSchemaInstanceAttributes(XMLObject xmlObject, Element domElement)
            throws MarshallingException {

        if (!DatatypeHelper.isEmpty(xmlObject.getSchemaLocation())) {
            log.trace("Setting xsi:schemaLocation for XMLObject {} to {}", xmlObject.getElementQName(),
                    xmlObject.getSchemaLocation());
            domElement.setAttributeNS(XMLConstants.XSI_NS, XMLConstants.XSI_PREFIX + ":schemaLocation",
                    xmlObject.getSchemaLocation());
        }

        if (!DatatypeHelper.isEmpty(xmlObject.getNoNamespaceSchemaLocation())) {
            log.trace("Setting xsi:noNamespaceSchemaLocation for XMLObject {} to {}", xmlObject.getElementQName(),
                    xmlObject.getNoNamespaceSchemaLocation());
            domElement.setAttributeNS(XMLConstants.XSI_NS, XMLConstants.XSI_PREFIX + ":noNamespaceSchemaLocation",
                    xmlObject.getNoNamespaceSchemaLocation());
        }

        if (xmlObject.isNilXSBoolean() != null && xmlObject.isNil()) {
            log.trace("Setting xsi:nil for XMLObject {} to true", xmlObject.getElementQName());
            domElement.setAttributeNS(XMLConstants.XSI_NS, XMLConstants.XSI_PREFIX + ":nil", xmlObject.isNilXSBoolean()
                    .toString());
        }

        QName type = xmlObject.getSchemaType();
        if (type == null) {
            return;
        }

        log.trace("Setting xsi:type attribute with for XMLObject {}", xmlObject.getElementQName());
        String typeLocalName = DatatypeHelper.safeTrimOrNullString(type.getLocalPart());
        String typePrefix = DatatypeHelper.safeTrimOrNullString(type.getPrefix());

        if (typeLocalName == null) {
            throw new MarshallingException("The type QName on XMLObject " + xmlObject.getElementQName()
                    + " may not have a null local name");
        }

        if (type.getNamespaceURI() == null) {
            throw new MarshallingException("The type URI QName on XMLObject " + xmlObject.getElementQName()
                    + " may not have a null namespace URI");
        }

        String attributeValue;
        if (typePrefix == null) {
            attributeValue = typeLocalName;
        } else {
            attributeValue = typePrefix + ":" + typeLocalName;
        }

        domElement.setAttributeNS(XMLConstants.XSI_NS, XMLConstants.XSI_PREFIX + ":type", attributeValue);
    }

    /**
     * Marshalls a given XMLObject into a W3C Element. The given signing context should be blindly passed to the
     * marshaller for child elements. The XMLObject passed to this method is guaranteed to be of the target name
     * specified during this unmarshaller's construction.
     * 
     * @param xmlObject the XMLObject to marshall
     * @param domElement the W3C DOM element
     * 
     * @throws MarshallingException thrown if there is a problem marshalling the element
     */
    protected abstract void marshallAttributes(XMLObject xmlObject, Element domElement) throws MarshallingException;

    /**
     * Marshalls data from the XMLObject into content of the DOM Element.
     * 
     * @param xmlObject the XMLObject
     * @param domElement the DOM element recieving the content
     * 
     * @throws MarshallingException thrown if the textual content can not be added to the DOM element
     */
    protected abstract void marshallElementContent(XMLObject xmlObject, Element domElement) throws MarshallingException;

    /**
     * Prepares the given DOM caching XMLObject for adoption into another document. If the XMLObject has a parent then
     * all visible namespaces used by the given XMLObject and its descendants are declared within that subtree and the
     * parent's DOM is invalidated.
     * 
     * @param domCachingObject the XMLObject to prepare for adoption
     * 
     * @throws MarshallingException thrown if a namespace within the XMLObject's DOM subtree can not be resolved.
     */
    private void prepareForAdoption(XMLObject domCachingObject) throws MarshallingException {
        if (domCachingObject.getParent() != null) {
            log.trace("Rooting all visible namespaces of XMLObject {} before adding it to new parent Element",
                    domCachingObject.getElementQName());
            try {
                XMLHelper.rootNamespaces(domCachingObject.getDOM());
            } catch (XMLParserException e) {
                String errorMsg =
                        "Unable to root namespaces of cached DOM element, " + domCachingObject.getElementQName();
                log.error(errorMsg, e);
                throw new MarshallingException(errorMsg, e);
            }

            log.trace("Release DOM of XMLObject parent");
            domCachingObject.releaseParentDOM(true);
        }
    }
}
