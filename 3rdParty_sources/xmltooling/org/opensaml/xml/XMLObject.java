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

package org.opensaml.xml;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.IDIndex;
import org.w3c.dom.Element;

/**
 * A object that represents an XML element, usually of a specific schema type, that has been unmarshalled into this Java
 * object.
 */
public interface XMLObject {

    /**
     * Adds a namespace to the ones already scoped to this element.
     * 
     * @deprecated use appropriate methods on the XMLObject's {@link NamespaceManager}.
     * 
     * @param namespace the namespace to add
     */
    public void addNamespace(Namespace namespace);

    /**
     * Detaches the XMLObject from its parent. This will release the parent's cached DOM (if it has one) and set this
     * object's parent to null. It does not remove this object from its parent, that's the responsibility of the invoker
     * of this method, nor does it re-root the cached DOM node (if there is one) in a new document. This is handled at
     * marshalling time.
     */
    public void detach();

    /**
     * Gets the DOM representation of this XMLObject, if one exists.
     * 
     * @return the DOM representation of this XMLObject
     */
    public Element getDOM();

    /**
     * Gets the QName for this element. This QName <strong>MUST</strong> contain the namespace URI, namespace prefix,
     * and local element name. Changes made to the returned QName are not reflected by the QName held by this element,
     * that is, the returned QName is a copy of the internal QName member of this class.
     * 
     * @return the QName for this attribute
     */
    public QName getElementQName();

    /**
     * Get the IDIndex holding the ID-to-XMLObject index mapping, rooted at this XMLObject's subtree.
     * 
     * @return the IDIndex owned by this XMLObject
     */
    public IDIndex getIDIndex();
    
    /**
     * Gets the {@link NamespaceManager} instance for this object.
     * 
     * @return the namespace manager for this object
     */
    public NamespaceManager getNamespaceManager();

    /**
     * Gets the namespaces that are scoped to this element.
     * 
     * @return the namespaces that are scoped to this element
     */
    public Set<Namespace> getNamespaces();

    /**
     * Gets the value of the XML Schema noNamespaceSchemaLocation attribute for this object.
     * 
     * @return value of the XML Schema noNamespaceSchemaLocation attribute for this object
     */
    public String getNoNamespaceSchemaLocation();

    /**
     * Gets an unmodifiable list of child elements in the order that they will appear in the DOM.
     * 
     * @return ordered list of child elements
     */
    public List<XMLObject> getOrderedChildren();

    /**
     * Gets the parent of this element or null if there is no parent.
     * 
     * @return the parent of this element or null
     */
    public XMLObject getParent();

    /**
     * Gets the value of the XML Schema schemaLocation attribute for this object.
     * 
     * @return schema location defined for this object
     */
    public String getSchemaLocation();

    /**
     * Gets the XML schema type of this element. This translates to contents the xsi:type attribute for the element.
     * 
     * @return XML schema type of this element
     */
    public QName getSchemaType();

    /**
     * Checks if this XMLObject has children.
     * 
     * @return true if this XMLObject has children, false if not
     */
    public boolean hasChildren();

    /**
     * Checks to see if this object has a parent.
     * 
     * @return true if the object has a parent, false if not
     */
    public boolean hasParent();

    /**
     * Releases the DOM representation of this XMLObject's children.
     * 
     * @param propagateRelease true if all descendants of this element should release their DOM
     */
    public void releaseChildrenDOM(boolean propagateRelease);

    /**
     * Releases the DOM representation of this XMLObject, if there is one.
     */
    public void releaseDOM();

    /**
     * Releases the DOM representation of this XMLObject's parent.
     * 
     * @param propagateRelease true if all ancestors of this element should release their DOM
     */
    public void releaseParentDOM(boolean propagateRelease);

    /**
     * Removes a namespace from this element.
     * 
     * @deprecated use appropriate methods on the XMLObject's {@link NamespaceManager}.
     * 
     * @param namespace the namespace to remove
     */
    public void removeNamespace(Namespace namespace);

    /**
     * Find the XMLObject which is identified by the specified ID attribute, within the subtree of XMLObjects which has
     * this XMLObject as its root.
     * 
     * @param id the ID attribute to resolve to an XMLObject
     * @return the XMLObject identified by the specified ID attribute value
     */
    public XMLObject resolveID(String id);

    /**
     * Find the XMLObject which is identified by the specified ID attribute, from the root of the tree of XMLObjects in
     * which this XMLObject is a member.
     * 
     * @param id the ID attribute to resolve to an XMLObject
     * @return the XMLObject identified by the specified ID attribute value
     */
    public XMLObject resolveIDFromRoot(String id);

    /**
     * Sets the DOM representation of this XMLObject.
     * 
     * @param dom DOM representation of this XMLObject
     */
    public void setDOM(Element dom);

    /**
     * Sets the value of the XML Schema noNamespaceSchemaLocation attribute for this object.
     * 
     * @param location value of the XML Schema noNamespaceSchemaLocation attribute for this object
     */
    public void setNoNamespaceSchemaLocation(String location);

    /**
     * Sets the parent of this element.
     * 
     * @param parent the parent of this element
     */
    public void setParent(XMLObject parent);

    /**
     * Sets the value of the XML Schema schemaLocation attribute for this object.
     * 
     * @param location value of the XML Schema schemaLocation attribute for this object
     */
    public void setSchemaLocation(String location);
    
    /**
     * Gets whether the object declares that its element content
     * is null, which corresponds to an <code>xsi:nil</code>
     * attribute of <code>true</code>.
     * 
     * <p>
     * Note that it is up to the developer to ensure that the 
     * value of this attribute is consistent with the actual
     * element content on the object instance.
     * </p>
     * 
     * <p>
     * Per the XML Schema specification, a value of true disallows 
     * element content, but not element attributes.
     * </p>
     * 
     * @see <a href="http://www.w3.org/TR/xmlschema-0/#Nils"/>
     * 
     * @return whether the object's content model is null
     */
    public Boolean isNil();

    /**
     * 
     * Gets whether the object declares that its element content
     * is null, which corresponds to an <code>xsi:nil</code>
     * attribute of <code>true</code>.
     * 
     * <p>
     * Note that it is up to the developer to ensure that the 
     * value of this attribute is consistent with the actual
     * element content on the object instance.
     * </p>
     * 
     * <p>
     * Per the XML Schema specification, a value of true disallows 
     * element content, but not element attributes.
     * </p>
     * 
     * @see <a href="http://www.w3.org/TR/xmlschema-0/#Nils/>
     * 
     * @return whether the object's content model is null
     */
    public XSBooleanValue isNilXSBoolean();

    /**
     * Sets whether the object declares that its element content
     * is null, which corresponds to an <code>xsi:nil</code>
     * attribute of <code>true</code>.
     * 
     * <p>
     * Note that it is up to the developer to ensure that the 
     * value of this attribute is consistent with the actual
     * element content on the object instance.
     * </p>
     * 
     * <p>
     * Per the XML Schema specification, a value of true disallows 
     * element content, but not element attributes.
     * </p>
     * 
     * @see <a href="http://www.w3.org/TR/xmlschema-0/#Nils/>
     * 
     * @param newNil whether the object's content model is expressed as null
     */
    public void setNil(Boolean newNil);

    /**
     * Sets whether the object declares that its element content
     * is null, which corresponds to an <code>xsi:nil</code>
     * attribute of <code>true</code>.
     * 
     * <p>
     * Note that it is up to the developer to ensure that the 
     * value of this attribute is consistent with the actual
     * element content on the object instance.
     * </p>
     * 
     * <p>
     * Per the XML Schema specification, a value of true disallows 
     * element content, but not element attributes.
     * </p>
     * 
     * @see <a href="http://www.w3.org/TR/xmlschema-0/#Nils/>
     * 
     * @param newNil whether the object's content model is expressed as null
     */
    public void setNil(XSBooleanValue newNil);

}