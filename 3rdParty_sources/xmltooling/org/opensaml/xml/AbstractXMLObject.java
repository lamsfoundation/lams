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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.IDIndex;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * An abstract implementation of XMLObject.
 */
public abstract class AbstractXMLObject implements XMLObject {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(AbstractXMLObject.class);

    /** Parent of this element. */
    private XMLObject parent;

    /** The name of this element with namespace and prefix information. */
    private QName elementQname;

    /** Schema locations for this XML object. */
    private String schemaLocation;

    /** No-namespace schema locations for this XML object. */
    private String noNamespaceSchemaLocation;

    /** The schema type of this element with namespace and prefix information. */
    private QName typeQname;

    /** DOM Element representation of this object. */
    private Element dom;
    
    /** The value of the <code>xsi:nil</code> attribute. */
    private  XSBooleanValue nil;
    
    /** The namespace manager for this XML object. */
    private NamespaceManager nsManager;

    /**
     * Mapping of ID attributes to XMLObjects in the subtree rooted at this object. This allows constant-time
     * dereferencing of ID-typed attributes within the subtree.
     */
    private final IDIndex idIndex;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected AbstractXMLObject(String namespaceURI, String elementLocalName, String namespacePrefix) {
        nsManager = new NamespaceManager(this);
        idIndex = new IDIndex(this);
        elementQname = XMLHelper.constructQName(namespaceURI, elementLocalName, namespacePrefix);
        if(namespaceURI != null){
            setElementNamespacePrefix(namespacePrefix);
        }
    }
    
    /** {@inheritDoc} */
    public void addNamespace(Namespace newNamespace) {
        getNamespaceManager().registerNamespace(newNamespace);
    }

    /** {@inheritDoc} */
    public void detach(){
        releaseParentDOM(true);
        parent = null;
    }

    /** {@inheritDoc} */
    public Element getDOM() {
        return dom;
    }

    /** {@inheritDoc} */
    public QName getElementQName() {
        return new QName(elementQname.getNamespaceURI(), elementQname.getLocalPart(), elementQname.getPrefix());
    }

    /** {@inheritDoc} */
    public IDIndex getIDIndex() {
        return idIndex;
    }
    
    /** {@inheritDoc} */
    public NamespaceManager getNamespaceManager() {
        return nsManager;
    }

    /** {@inheritDoc} */
    public Set<Namespace> getNamespaces() {
        return Collections.unmodifiableSet(getNamespaceManager().getNamespaces());
    }

    /** {@inheritDoc} */
    public String getNoNamespaceSchemaLocation() {
        return noNamespaceSchemaLocation;
    }

    /**
     * Gets the parent of this element.
     * 
     * @return the parent of this element
     */
    public XMLObject getParent() {
        return parent;
    }

    /** {@inheritDoc} */
    public String getSchemaLocation() {
        return schemaLocation;
    }

    /** {@inheritDoc} */
    public QName getSchemaType() {
        return typeQname;
    }

    /** {@inheritDoc} */
    public boolean hasChildren() {
        List<? extends XMLObject> children = getOrderedChildren();
        return children != null && children.size() > 0;
    }

    /** {@inheritDoc} */
    public boolean hasParent() {
        return getParent() != null;
    }
    
    /**
     * A helper function for derived classes.  This method should be called when the value of a
     * namespace-qualified attribute changes.
     * 
     * @param attributeName the attribute name
     * @param hasValue true to indicate that the attribute has a value, false to indicate it has no value
     */
    protected void manageQualifiedAttributeNamespace(QName attributeName, boolean hasValue) {
        if (hasValue) {
            getNamespaceManager().registerAttributeName(attributeName);
        } else {
            getNamespaceManager().deregisterAttributeName(attributeName);
        }
    }

    /**
     * A helper function for derived classes. This checks for semantic equality between two QNames if it they are
     * different invalidates the DOM. It returns the normalized value so subclasses just have to go. this.foo =
     * prepareForAssignment(this.foo, foo);
     * 
     * @param oldValue - the current value
     * @param newValue - the new value
     * 
     * @return the value that should be assigned
     * 
     * @deprecated replacement {@link #prepareAttributeValueForAssignment(String, QName, QName)} 
     *                or {@link #prepareElementContentForAssignment(QName, QName)} as appropriate
     */
    protected QName prepareForAssignment(QName oldValue, QName newValue) {
        if (oldValue == null) {
            if (newValue != null) {
                Namespace newNamespace = new Namespace(newValue.getNamespaceURI(), newValue.getPrefix());
                addNamespace(newNamespace);
                releaseThisandParentDOM();
                return newValue;
            } else {
                return null;
            }
        }

        if (!oldValue.equals(newValue)) {
            if (newValue != null) {
                Namespace newNamespace = new Namespace(newValue.getNamespaceURI(), newValue.getPrefix());
                addNamespace(newNamespace);
            }
            releaseThisandParentDOM();
        }

        return newValue;
    }
    
    /**
     * A helper function for derived classes. This checks for semantic equality between two QNames if it they are
     * different invalidates the DOM. It returns the normalized value so subclasses just have to go. this.foo =
     * prepareElementContentForAssignment(this.foo, foo);
     * 
     * @param oldValue - the current value
     * @param newValue - the new value
     * 
     * @return the value that should be assigned
     */
    protected QName prepareElementContentForAssignment(QName oldValue, QName newValue) {
        if (oldValue == null) {
            if (newValue != null) {
                getNamespaceManager().registerContentValue(newValue);
                releaseThisandParentDOM();
                return newValue;
            } else {
                return null;
            }
        }
        
        // Old value was not null, so go ahead and deregister it
        getNamespaceManager().deregisterContentValue();

        if (!oldValue.equals(newValue)) {
            if (newValue != null) {
                getNamespaceManager().registerContentValue(newValue);
            }
            releaseThisandParentDOM();
        }

        return newValue;
    }
    
    
    /**
     * A helper function for derived classes. This checks for semantic equality between two QNames if it they are
     * different invalidates the DOM. It returns the normalized value so subclasses just have to go. this.foo =
     * prepareAttributeValueForAssignment(this.foo, foo);
     * 
     * @param attributeID - unique identifier of the attribute in the content model within this XMLObject, used to 
     *        identify the attribute within the XMLObject's NamespaceManager
     * @param oldValue - the current value
     * @param newValue - the new value
     * 
     * @return the value that should be assigned
     */
    protected QName prepareAttributeValueForAssignment(String attributeID, QName oldValue, QName newValue) {
        if (oldValue == null) {
            if (newValue != null) {
                getNamespaceManager().registerAttributeValue(attributeID, newValue);
                releaseThisandParentDOM();
                return newValue;
            } else {
                return null;
            }
        }
        
        // Old value was not null, so go ahead and deregister it
        getNamespaceManager().deregisterAttributeValue(attributeID);

        if (!oldValue.equals(newValue)) {
            if (newValue != null) {
                getNamespaceManager().registerAttributeValue(attributeID, newValue);
            }
            releaseThisandParentDOM();
        }

        return newValue;
    }

    /**
     * A helper function for derived classes. This 'nornmalizes' newString and then if it is different from oldString
     * invalidates the DOM. It returns the normalized value so subclasses just have to go. this.foo =
     * prepareForAssignment(this.foo, foo);
     * 
     * @param oldValue - the current value
     * @param newValue - the new value
     * 
     * @return the value that should be assigned
     */
    protected String prepareForAssignment(String oldValue, String newValue) {
        String newString = DatatypeHelper.safeTrimOrNullString(newValue);

        if (!DatatypeHelper.safeEquals(oldValue, newString)) {
            releaseThisandParentDOM();
        }

        return newString;
    }

    /**
     * A helper function for derived classes that checks to see if the old and new value are equal and if so releases
     * the cached dom. Derived classes are expected to use this thus: <code>
     *   this.foo = prepareForAssignment(this.foo, foo);
     *   </code>
     * 
     * This method will do a (null) safe compare of the objects and will also invalidate the DOM if appropriate
     * 
     * @param <T> - type of object being compared and assigned
     * @param oldValue - current value
     * @param newValue - proposed new value
     * 
     * @return The value to assign to the saved Object.
     */
    protected <T extends Object> T prepareForAssignment(T oldValue, T newValue) {
        if (oldValue == null) {
            if (newValue != null) {
                releaseThisandParentDOM();
                return newValue;
            } else {
                return null;
            }
        }

        if (!oldValue.equals(newValue)) {
            releaseThisandParentDOM();
        }

        return newValue;
    }

    /**
     * A helper function for derived classes, similar to assignString, but for (singleton) SAML objects. It is
     * indifferent to whether either the old or the new version of the value is null. Derived classes are expected to
     * use this thus: <code>
     *   this.foo = prepareForAssignment(this.foo, foo);
     *   </code>
     * 
     * This method will do a (null) safe compare of the objects and will also invalidate the DOM if appropriate
     * 
     * @param <T> type of object being compared and assigned
     * @param oldValue current value
     * @param newValue proposed new value
     * 
     * @return The value to assign to the saved Object.
     */
    protected <T extends XMLObject> T prepareForAssignment(T oldValue, T newValue) {

        if (newValue != null && newValue.hasParent()) {
            throw new IllegalArgumentException(newValue.getClass().getName()
                    + " cannot be added - it is already the child of another SAML Object");
        }

        if (oldValue == null) {
            if (newValue != null) {
                releaseThisandParentDOM();
                newValue.setParent(this);
                idIndex.registerIDMappings(newValue.getIDIndex());
                return newValue;

            } else {
                return null;
            }
        }

        if (!oldValue.equals(newValue)) {
            oldValue.setParent(null);
            releaseThisandParentDOM();
            idIndex.deregisterIDMappings(oldValue.getIDIndex());
            if (newValue != null) {
                newValue.setParent(this);
                idIndex.registerIDMappings(newValue.getIDIndex());
            }
        }

        return newValue;
    }

    /**
     * A helper function for derived classes. The mutator/setter method for any ID-typed attributes should call this
     * method in order to handle getting the old value removed from the ID-to-XMLObject mapping, and the new value added
     * to the mapping.
     * 
     * @param oldID the old value of the ID-typed attribute
     * @param newID the new value of the ID-typed attribute
     */
    protected void registerOwnID(String oldID, String newID) {
        String newString = DatatypeHelper.safeTrimOrNullString(newID);

        if (!DatatypeHelper.safeEquals(oldID, newString)) {
            if (oldID != null) {
                idIndex.deregisterIDMapping(oldID);
            }

            if (newString != null) {
                idIndex.registerIDMapping(newString, this);
            }
        }
    }

    /** {@inheritDoc} */
    public void releaseChildrenDOM(boolean propagateRelease) {
        log.trace("Releasing cached DOM reprsentation for children of {} with propagation set to {}",
                getElementQName(), propagateRelease);
        if (getOrderedChildren() != null) {
            for (XMLObject child : getOrderedChildren()) {
                if (child != null) {
                    child.releaseDOM();
                    if (propagateRelease) {
                        child.releaseChildrenDOM(propagateRelease);
                    }
                }
            }
        }
    }

    /** {@inheritDoc} */
    public void releaseDOM() {
        log.trace("Releasing cached DOM reprsentation for {}", getElementQName());
        setDOM(null);
    }

    /** {@inheritDoc} */
    public void releaseParentDOM(boolean propagateRelease) {
        log.trace("Releasing cached DOM reprsentation for parent of {} with propagation set to {}", getElementQName(),
                propagateRelease);
        XMLObject parentElement = getParent();
        if (parentElement != null) {
            parent.releaseDOM();
            if (propagateRelease) {
                parent.releaseParentDOM(propagateRelease);
            }
        }
    }

    /**
     * A convience method that is equal to calling {@link #releaseDOM()} then {@link #releaseChildrenDOM(boolean)} with
     * the release being propogated.
     */
    public void releaseThisAndChildrenDOM() {
        if (getDOM() != null) {
            releaseDOM();
            releaseChildrenDOM(true);
        }
    }

    /**
     * A convience method that is equal to calling {@link #releaseDOM()} then {@link #releaseParentDOM(boolean)} with
     * the release being propogated.
     */
    public void releaseThisandParentDOM() {
        if (getDOM() != null) {
            releaseDOM();
            releaseParentDOM(true);
        }
    }

    /** {@inheritDoc} */
    public void removeNamespace(Namespace namespace) {
        getNamespaceManager().deregisterNamespace(namespace);
    }

    /** {@inheritDoc} */
    public XMLObject resolveID(String id) {
        return idIndex.lookup(id);
    }

    /** {@inheritDoc} */
    public XMLObject resolveIDFromRoot(String id) {
        XMLObject root = this;
        while (root.hasParent()) {
            root = root.getParent();
        }
        return root.resolveID(id);
    }

    /** {@inheritDoc} */
    public void setDOM(Element newDom) {
        dom = newDom;
    }

    /**
     * Sets the prefix for this element's namespace.
     * 
     * @param prefix the prefix for this element's namespace
     */
    public void setElementNamespacePrefix(String prefix) {
        if (prefix == null) {
            elementQname = new QName(elementQname.getNamespaceURI(), elementQname.getLocalPart());
        } else {
            elementQname = new QName(elementQname.getNamespaceURI(), elementQname.getLocalPart(), prefix);
        }
        getNamespaceManager().registerElementName(elementQname);
    }

    /**
     * Sets the element QName.
     * 
     * @param elementQName the element's QName
     */
    protected void setElementQName(QName elementQName) {
        this.elementQname = XMLHelper.constructQName(elementQName.getNamespaceURI(), elementQName.getLocalPart(),
                elementQName.getPrefix());
        getNamespaceManager().registerElementName(this.elementQname);
    }

    /** {@inheritDoc} */
    public void setNoNamespaceSchemaLocation(String location) {
        noNamespaceSchemaLocation = DatatypeHelper.safeTrimOrNullString(location);
        manageQualifiedAttributeNamespace(XMLConstants.XSI_NO_NAMESPACE_SCHEMA_LOCATION_ATTRIB_NAME, schemaLocation != null);
    }

    /** {@inheritDoc} */
    public void setParent(XMLObject newParent) {
        parent = newParent;
    }

    /** {@inheritDoc} */
    public void setSchemaLocation(String location) {
        schemaLocation = DatatypeHelper.safeTrimOrNullString(location);
        manageQualifiedAttributeNamespace(XMLConstants.XSI_SCHEMA_LOCATION_ATTRIB_NAME, schemaLocation != null);
    }

    /**
     * Sets a given QName as the schema type for the Element represented by this XMLObject. This will register the namespace
     * for the type as well as for the xsi:type qualified attribute name with the namespace manager for this XMLObject.
     * If null is passed, the type name and xsi:type name will be deregistered.
     * 
     * @param type the schema type
     */
    protected void setSchemaType(QName type) {
        typeQname = type;
        getNamespaceManager().registerElementType(typeQname);
        manageQualifiedAttributeNamespace(XMLConstants.XSI_TYPE_ATTRIB_NAME, typeQname != null);
    }
    
    /** {@inheritDoc} */
    public Boolean isNil() {
        if (nil != null) {
            return nil.getValue();
        }

        return Boolean.FALSE;
    }

    /** {@inheritDoc} */
    public XSBooleanValue isNilXSBoolean() {
        return nil;
    }

    /** {@inheritDoc} */
    public void setNil(Boolean newNil) {
        if (newNil != null) {
            nil = prepareForAssignment(nil, new XSBooleanValue(newNil, false));
        } else {
            nil = prepareForAssignment(nil, null);
        }
        manageQualifiedAttributeNamespace(XMLConstants.XSI_NIL_ATTRIB_NAME, nil != null);
    }

    /** {@inheritDoc} */
    public void setNil(XSBooleanValue newNil) {
        nil = prepareForAssignment(nil, newNil);
        manageQualifiedAttributeNamespace(XMLConstants.XSI_NIL_ATTRIB_NAME, nil != null);
    }

}