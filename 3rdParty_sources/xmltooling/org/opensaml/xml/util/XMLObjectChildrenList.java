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

package org.opensaml.xml.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

import org.opensaml.xml.XMLObject;

/**
 * Resizable list for the children of XMLObjects. This list implements all optional List operations and does not all for
 * null elements. XMLObjects added to, or removed from, this list will have their parent object appropriately set and,
 * the underlying DOM will be released during mutation opertions.
 * 
 * @param <ElementType> type of elements added to the list
 */
public class XMLObjectChildrenList<ElementType extends XMLObject> extends AbstractList<ElementType> {

    /** Parent to the elements in this list. */
    private XMLObject parent;

    /** List of elements. */
    private List<ElementType> elements;

    /**
     * Constructs an empty list with all added XMLObjects being assigned the given parent XMLObject.
     * 
     * @param newParent the parent for all the added XMLObjects
     * 
     * @throws NullPointerException thrown if the parent is null
     */
    public XMLObjectChildrenList(XMLObject newParent) throws NullPointerException {
        if (newParent == null) {
            throw new NullPointerException("Parent may not be null");
        }

        parent = newParent;
        elements = new LazyList<ElementType>();
    }

    /**
     * Constructs a list containing the elements in the specified collection, in the order they are returned by the
     * collection's iterator, with each added XMLObject assigned the given parent XMLObject.
     * 
     * @param newParent the parent for all the added XMLObjects
     * @param newElements the elements to be added
     * 
     * @throws NullPointerException thrown if the parent is null
     * @throws IllegalArgumentException thrown if any of the XMLObjects in the given collection already have a parent
     *             that is different from the given parent
     */
    public XMLObjectChildrenList(XMLObject newParent, Collection<ElementType> newElements) throws NullPointerException {
        if (newParent == null) {
            throw new NullPointerException("Parent may not be null");
        }

        parent = newParent;
        elements = new LazyList<ElementType>();

        addAll(newElements);
    }

    /** {@inheritDoc} */
    public int size() {
        return elements.size();
    }

    /**
     * Checks to see if the given element is contained in this list.
     * 
     * @param element the element to check for
     * 
     * @return true if the element is in this list, false if not
     */
    public boolean contains(ElementType element) {
        return elements.contains(element);
    }

    /** {@inheritDoc} */
    public ElementType get(int index) {
        return elements.get(index);
    }

    /**
     * Replaces the XMLObject at the specified index with the given element.
     * 
     * @param index index of the XMLObject to be replaced
     * @param element element to be stored at the given index
     * 
     * @return the replaced XMLObject
     * 
     * @throws IllegalArgumentException thrown if the given XMLObject already has a parent that is different from the
     *             XMLObject given at list construction time
     */
    public ElementType set(int index, ElementType element) throws IllegalArgumentException {
        if (element == null) {
            return null;
        }

        setParent(element);

        ElementType removedElement = elements.set(index, element);
        if (removedElement != null) {
            removedElement.setParent(null);
            parent.getIDIndex().deregisterIDMappings(removedElement.getIDIndex());
        }
        
        // Note: to avoid ordering problems, this needs to be called after
        // the deregistration, in case the added element has a same ID string 
        // value as the removed one, else you will lose it.
        parent.getIDIndex().registerIDMappings(element.getIDIndex());

        modCount++;
        return removedElement;
    }

    /**
     * Adds the given XMLObject to this list.
     * 
     * @param index index at which to add the given XMLObject
     * @param element element to be stored at the given index
     * 
     * @throws IllegalArgumentException thrown if the given XMLObject already has a parent that is different from the
     *             XMLObject given at list construction time
     */
    public void add(int index, ElementType element) throws IllegalArgumentException {
        if (element == null || elements.contains(element)) {
            return;
        }

        setParent(element);
        parent.getIDIndex().registerIDMappings(element.getIDIndex());

        modCount++;
        elements.add(index, element);
    }

    /** {@inheritDoc} */
    public ElementType remove(int index) {
        ElementType element = elements.remove(index);

        if (element != null) {
            element.releaseParentDOM(true);
            element.setParent(null);
            parent.getIDIndex().deregisterIDMappings(element.getIDIndex());
        }

        modCount++;
        return element;
    }

    /**
     * Removes the element from the list.
     * 
     * @param element the element to be removed
     * 
     * @return true if the element was in the list and removed, false if not
     */
    public boolean remove(ElementType element) {
        boolean elementRemoved = false;

        elementRemoved = elements.remove(element);
        if (elementRemoved) {
            if (element != null) {
                element.releaseParentDOM(true);
                element.setParent(null);
                parent.getIDIndex().deregisterIDMappings(element.getIDIndex());
            }
        }

        return elementRemoved;
    }

    /**
     * Assigned the parent, given at list construction, to the given element if the element does not have a parent or
     * its parent matches the one given at list construction time.
     * 
     * @param element the element to set the parent on
     * 
     * @throws IllegalArgumentException thrown if the given element already has a parent and it is different than the
     *             parent given at list construction time
     */
    protected void setParent(ElementType element) throws IllegalArgumentException {
        XMLObject elemParent = element.getParent();
        if (elemParent != null && elemParent != parent) {
            throw new IllegalArgumentException(element.getElementQName()
                    + " is already the child of another XMLObject and may not be inserted in to this list");
        }

        element.setParent(parent);
        element.releaseParentDOM(true);
    }
}