/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.security.util.xml;

// $Id$

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.security.PicketBoxLogger;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.util.StringPropertyReplacer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * DOM2 utilites
 *
 * @author Thomas.Diesler@jboss.org
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public final class DOMUtils
{
    // All elements created by the same thread are created by the same builder and belong to the same doc
    private static ThreadLocal documentThreadLocal = new ThreadLocal();
    private static ThreadLocal builderThreadLocal = new ThreadLocal() {
        protected Object initialValue() {
            try
            {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setEntityResolver(new JBossEntityResolver());
                return builder;
            }
            catch (ParserConfigurationException e)
            {
                throw PicketBoxMessages.MESSAGES.failedToCreateDocumentBuilder(e);
            }
        }
    };

    // Hide the constructor
    private DOMUtils()
    {
    }

    public static DocumentBuilder getDocumentBuilder()
    {
        DocumentBuilder builder = (DocumentBuilder)builderThreadLocal.get();
        return builder;
    }

    /** Parse the given XML string and return the root Element
    * @param xmlString 
    * @return the element
    * @throws IOException 
     */
    public static Element parse(String xmlString) throws IOException
    {
        return parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
    }

    /** Parse the given XML stream and return the root Element
    * @param xmlStream 
    * @return the element
    * @throws IOException 
     */
    public static Element parse(InputStream xmlStream) throws IOException
    {
        try
        {
            Document doc = getDocumentBuilder().parse(xmlStream);
            Element root = doc.getDocumentElement();
            return root;
        }
        catch (SAXException e)
        {
            throw new IOException(e);
        }
    }

    /** Parse the given input source and return the root Element
    * @param source 
    * @return the element
    * @throws IOException 
     */
    public static Element parse(InputSource source) throws IOException
    {
        try
        {
            Document doc = getDocumentBuilder().parse(source);
            Element root = doc.getDocumentElement();
            return root;
        }
        catch (SAXException e)
        {
            throw new IOException(e);
        }
    }

    /** Create an Element for a given name
    * @param localPart 
    * @return the element
     */
    public static Element createElement(String localPart)
    {
        Document doc = getOwnerDocument();
        return doc.createElement(localPart);
    }

    /** Create an Element for a given name and prefix
    * @param localPart 
    * @param prefix 
    * @return the element
     */
    public static Element createElement(String localPart, String prefix)
    {
        Document doc = getOwnerDocument();
        return doc.createElement(prefix + ":" + localPart);
    }

    /** Create an Element for a given name, prefix and uri
    * @param localPart 
    * @param prefix 
    * @param uri 
    * @return the element
     */
    public static Element createElement(String localPart, String prefix, String uri)
    {
        Document doc = getOwnerDocument();
        if (prefix == null || prefix.length() == 0)
        {
            return doc.createElementNS(uri, localPart);
        }
        else
        {
            return doc.createElementNS(uri, prefix + ":" + localPart);
        }
    }

    /** Create an Element for a given QName
    * @param qname 
    * @return the element
     */
    public static Element createElement(QName qname)
    {
        return createElement(qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
    }

    /** Create a org.w3c.dom.Text node
    * @param value 
    * @return the text node
     */
    public static Text createTextNode(String value)
    {
        Document doc = getOwnerDocument();
        return doc.createTextNode(value);
    }

    /** @return the qname of the given node.
    * @param el 
     */
    public static QName getElementQName(Element el)
    {
        String qualifiedName = el.getNodeName();
        return resolveQName(el, qualifiedName);
    }

    /** Transform the giveen qualified name into a QName
    * @param el 
    * @param qualifiedName 
    * @return the resolved name
     */
    public static QName resolveQName(Element el, String qualifiedName)
    {
        QName qname;
        String prefix = "";
        String namespaceURI = "";
        String localPart = qualifiedName;

        int colIndex = qualifiedName.indexOf(":");
        if (colIndex > 0)
        {
            prefix = qualifiedName.substring(0, colIndex);
            localPart = qualifiedName.substring(colIndex + 1);

            if ("xmlns".equals(prefix))
            {
                namespaceURI = "URI:XML_PREDEFINED_NAMESPACE";
            }
            else
            {
                Element nsElement = el;
                while (namespaceURI.equals("") && nsElement != null)
                {
                    namespaceURI = nsElement.getAttribute("xmlns:" + prefix);
                    if (namespaceURI.equals(""))
                        nsElement = getParentElement(nsElement);
                }
            }

            if (namespaceURI.equals(""))
                throw PicketBoxMessages.MESSAGES.failedToFindNamespaceURI(qualifiedName);
        }

        qname = new QName(namespaceURI, localPart, prefix);
        return qname;
    }

    /** Get the value from the given attribute
    * @param el 
    * @param attrName 
     *
     * @return null if the attribute value is empty or the attribute is not present
     */
    public static String getAttributeValue(Element el, String attrName)
    {
        return getAttributeValue(el, new QName(attrName));
    }

    /** Get the value from the given attribute
    * @param el 
    * @param attrName 
     *
     * @return null if the attribute value is empty or the attribute is not present
     */
    public static String getAttributeValue(Element el, QName attrName)
    {
        String attr = null;
        if ("".equals(attrName.getNamespaceURI()))
            attr = el.getAttribute(attrName.getLocalPart());
        else attr = el.getAttributeNS(attrName.getNamespaceURI(), attrName.getLocalPart());

        if ("".equals(attr))
            attr = null;

        return attr;
    }

    /** @return the qname value from the given attribute
    * @param el 
    * @param attrName 
     */
    public static QName getAttributeValueAsQName(Element el, String attrName)
    {
        return getAttributeValueAsQName(el, new QName(attrName));

    }

    /** @return the qname value from the given attribute
    * @param el 
    * @param attrName 
     */
    public static QName getAttributeValueAsQName(Element el, QName attrName)
    {
        QName qname = null;

        String qualifiedName = getAttributeValue(el, attrName);
        if (qualifiedName != null)
        {
            qname = resolveQName(el, qualifiedName);
        }

        return qname;
    }

    /** @return the boolean value from the given attribute
    * @param el 
    * @param attrName 
     */
    public static boolean getAttributeValueAsBoolean(Element el, String attrName)
    {
        return getAttributeValueAsBoolean(el, new QName(attrName));
    }

    /** @return the boolean value from the given attribute
    * @param el 
    * @param attrName 
     */
    public static boolean getAttributeValueAsBoolean(Element el, QName attrName)
    {
        String attrVal = getAttributeValue(el, attrName);
        boolean ret = "true".equalsIgnoreCase(attrVal) || "1".equalsIgnoreCase(attrVal);
        return ret;
    }

    /** @return the integer value from the given attribute
    * @param el 
    * @param attrName 
     */
    public static Integer getAttributeValueAsInteger(Element el, String attrName)
    {
        return getAttributeValueAsInteger(el, new QName(attrName));
    }

    /** @param el 
    * @param attrName 
    * @return the integer value from the given attribute
     */
    public static Integer getAttributeValueAsInteger(Element el, QName attrName)
    {
        String attrVal = getAttributeValue(el, attrName);
        return (attrVal != null ? new Integer(attrVal) : null);
    }

    /** @param el 
    * @return the attributes as Map<QName, String>
     */
    public static Map getAttributes(Element el)
    {
        Map attmap = new HashMap();
        NamedNodeMap attribs = el.getAttributes();
        for (int i = 0; i < attribs.getLength(); i++)
        {
            Attr attr = (Attr)attribs.item(i);
            String name = attr.getName();
            QName qname = resolveQName(el, name);
            String value = attr.getNodeValue();
            attmap.put(qname, value);
        }
        return attmap;
    }

    /** Copy attributes between elements
    * @param destElement 
    * @param srcElement 
     */
    public static void copyAttributes(Element destElement, Element srcElement)
    {
        NamedNodeMap attribs = srcElement.getAttributes();
        for (int i = 0; i < attribs.getLength(); i++)
        {
            Attr attr = (Attr)attribs.item(i);
            String uri = attr.getNamespaceURI();
            String qname = attr.getName();
            String value = attr.getNodeValue();
            
            // Prevent DOMException: NAMESPACE_ERR: An attempt is made to create or 
            // change an object in a way which is incorrect with regard to namespaces.
            if (uri == null && qname.startsWith("xmlns"))
            {
                PicketBoxLogger.LOGGER.traceIgnoreXMLAttribute(uri, qname, value);
            }
            else
            {
               destElement.setAttributeNS(uri, qname, value);
            }
        }
    }

    /** True if the node has child elements
    * @param node 
    * @return true when has child elements
     */
    public static boolean hasChildElements(Node node)
    {
        NodeList nlist = node.getChildNodes();
        for (int i = 0; i < nlist.getLength(); i++)
        {
            Node child = nlist.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
                return true;
        }
        return false;
    }

    /** Gets child elements
    * @param node 
    * @return the iterator
     */
    public static Iterator getChildElements(Node node)
    {
        ArrayList list = new ArrayList();
        NodeList nlist = node.getChildNodes();
        for (int i = 0; i < nlist.getLength(); i++)
        {
            Node child = nlist.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
                list.add(child);
        }
        return list.iterator();
    }

    /** Get the concatenated text content, or null.
    * @param node 
     * @return getTextContent(node, false).
     */
    public static String getTextContent(Node node)
    {
       return getTextContent(node, false);
    }
    /** Get the concatenated text content, or null.
    * @param node node to search for TEXT_NODE conent
     * @param replaceProps flag indicating if ${x} property refs should be replace
    * @return the text content
     */
    public static String getTextContent(Node node, boolean replaceProps)
    {
        boolean hasTextContent = false;
        StringBuffer buffer = new StringBuffer();
        NodeList nlist = node.getChildNodes();
        for (int i = 0; i < nlist.getLength(); i++)
        {
            Node child = nlist.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
            {
                buffer.append(child.getNodeValue());
                hasTextContent = true;
            }
        }
        String text = (hasTextContent ? buffer.toString() : null);
        if ( text != null && replaceProps)
           text = StringPropertyReplacer.replaceProperties(text);

        return text;
    }

    /** @return the first child element
    * @param node 
     */
    public static Element getFirstChildElement(Node node)
    {
        return getFirstChildElementIntern(node, null);
    }

    /** @param node 
    * @param nodeName 
    * @return the first child element for a given local name without namespace
     */
    public static Element getFirstChildElement(Node node, String nodeName)
    {
        return getFirstChildElementIntern(node, new QName(nodeName));
    }

    /** @param node 
    * @param nodeName 
    * @return the first child element for a given qname
     */
    public static Element getFirstChildElement(Node node, QName nodeName)
    {
        return getFirstChildElementIntern(node, nodeName);
    }

    private static Element getFirstChildElementIntern(Node node, QName nodeName)
    {
        Element childElement = null;
        Iterator it = getChildElementsIntern(node, nodeName);
        if (it.hasNext())
        {
            childElement = (Element)it.next();
        }
        return childElement;
    }

    /** Gets the child elements for a given local name without namespace
    * @param node 
    * @param nodeName 
    * @return the iterator 
     */
    public static Iterator getChildElements(Node node, String nodeName)
    {
        return getChildElementsIntern(node, new QName(nodeName));
    }

    /** Gets the child element for a given qname
    * @param node 
    * @param nodeName 
    * @return the iterator
     */
    public static Iterator getChildElements(Node node, QName nodeName)
    {
        return getChildElementsIntern(node, nodeName);
    }

    private static Iterator getChildElementsIntern(Node node, QName nodeName)
    {
        ArrayList list = new ArrayList();
        NodeList nlist = node.getChildNodes();
        for (int i = 0; i < nlist.getLength(); i++)
        {
            Node child = nlist.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                if (nodeName == null)
                {
                    list.add(child);
                }
                else
                {
                    QName qname;
                    if (nodeName.getNamespaceURI().length() > 0)
                    {
                        qname = new QName(child.getNamespaceURI(), child.getLocalName());
                    }
                    else
                    {
                        qname = new QName(child.getLocalName());
                    }
                    if (qname.equals(nodeName))
                    {
                        list.add(child);
                    }
                }
            }
        }
        return list.iterator();
    }

    /** Gets parent element or null if there is none
    * @param node 
    * @return the element
     */
    public static Element getParentElement(Node node)
    {
        Node parent = node.getParentNode();
        return (parent instanceof Element ? (Element)parent : null);
    }

    /** @return the owner document that is associated with the current thread */
    public static Document getOwnerDocument()
    {
        Document doc = (Document)documentThreadLocal.get();
        if (doc == null)
        {
            doc = getDocumentBuilder().newDocument();
            documentThreadLocal.set(doc);
        }
        return doc;
    }
}
