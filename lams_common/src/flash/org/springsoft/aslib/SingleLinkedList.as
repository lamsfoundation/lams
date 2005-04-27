/*

SingleLinkedList is part of ASLib

Copyright (C) 2004 Thomas P. Amsler

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

Thomas Amsler
tamsler@cal.berkeley.edu

*/

/*
=====================================================================
	Requires:
	ActionScript 2.0

	Description:
	Single Linked List class
=====================================================================
*/

import org.springsoft.aslib.SingleLinkedListObject;
import org.springsoft.aslib.ListNode;
import org.springsoft.aslib.ObjectListNode;

class org.springsoft.aslib.SingleLinkedList
{
	
	// Head of list
	private var head_:ListNode;

	/**
	* SingleLinkedList Constructor
	*/
	function SingleLinkedList() 
	{
		head_ = null;
	}

	/**
 	* Test for empty list
 	*
 	* @returns true if the linked list is empty. Otherwise returns false.
 	*/
	public function isEmpty(Void):Boolean
	{
		return (null == head_) ? true : false;
	}
	
	/** 
	* Insert a SingleLinkedListObject.  By default we insert at front of list
	*
	* @param data The SingleLinkedListObject's data object
	*/
	public function insert(data:SingleLinkedListObject):Void
	{
		insertFront(new ObjectListNode(data));
	}

	/**
	* Insert a SingleLinkedListObject at tail
	*
	* @param data The SingleLinkedListObject's data object
	*/
	public function insertTail(data:SingleLinkedListObject):Void
	{
		if(isEmpty()) {
			insert(data);
		}
		else {
			var tailNode:ListNode = null;
			for(var tempNode:ListNode = head_; tempNode != null; tailNode = tempNode, tempNode = tempNode.getNext()) { }
			insertEnd(new ObjectListNode(data), tailNode);
		}
	}
	
	/**
	* Get a node reference
	*
	* @param key The numeric key for finding node
	* @returns ListNode if we find the key.  Otherwise returns null.
	*/
	public function get(key:Number):ListNode
	{
		var tempNode:ListNode = head_;

		// Loop through the list and if we have a math, stop and return.
		// If there is not a match, returning null since tempNode will point to null
		for(; tempNode != null; tempNode = tempNode.getNext()) {
			if(key == tempNode.getKey()) {
				break;
			}
		}
		
		return tempNode;
	}
	
	/**
	* Get a SingleLinkedListObject data object reference
	*
	* @param key The numeric key to find SingleLinkedListObject
	* @returns SingleLinkedListObject if we find the key.  Otherwise returns null.
	*/
	public function getData(key:Number):SingleLinkedListObject
	{
		return this.get(key).get();
	}
	
	/**
	* Get a node at index
	*
	* @param index The numeric index to locate node
	* @returns ListNode if we locate the index.  Otherwise returns null.
	*/
	public function getAt(index:Number):ListNode
	{
		var tempNode:ListNode = head_;
		var position:Number = 0;
		
		// Loop through the list and if we reach the index, stop and return.
		// If we have an invalid index, returning null since tempNode will point to null
		for(; tempNode != null; tempNode = tempNode.getNext()) {
			if(index == position) {
				break;
			}
			position++;
		}
		
		return tempNode;
	}
	
	/**
	* Get a SingleLinkedListObject data object reference
	*
	* @param index The numeric index to locate SingleLinkedListObject
	* @returns SingleLinkedListObject if we locate the index.  Otherwise returns null.
	*/
	public function getDataAt(index:Number):SingleLinkedListObject
	{
		return this.getAt(index).get();
	}

	/**
	* Remove a node
	*
	* @param key The numeric key to find and remove node
	* @returns ListNode if list is not empty.  Otherwise returns null.
	*/
	public function remove(key:Number):ListNode
	{
		var previousTempNode:ListNode = head_;

		for(var tempNode:ListNode = head_; tempNode != null; previousTempNode = tempNode, tempNode = tempNode.getNext()) {
			if(key == tempNode.getKey()) {
				if(isFront(tempNode)) {
					return removeFront();
				}
				else if(isEnd(tempNode)) {
					return removeEnd(previousTempNode);
				}
				else {
					return removeMiddle(previousTempNode);
				}
			}
		}
		
		return null;
	}
	
	
	/**
	* Remove a SingleLinkedListObject
	*
	* @param key The numeric key to find and remove SingleLinkedListObject
	* @returns SingleLinkedListObject if list is not empty.  Otherwise returns null.
	*/
	public function removeData(key:Number):SingleLinkedListObject
	{
		return this.remove(key).get();
	}
	
	/**
	* Remove a node at index
	*
	* @param index The numeric index to locate and remove node
	* @returns ListNode if list is not empty.  Otherwise returns null.
	*/
	public function removeAt(index:Number):ListNode
	{
		var previousTempNode:ListNode = head_;
		var position:Number = 0;

		for(var tempNode:ListNode = head_; tempNode != null; previousTempNode = tempNode, tempNode = tempNode.getNext()) {
			if(index == position) {
				if(isFront(tempNode)) {
					return removeFront();
				}
				else if(isEnd(tempNode)) {
					return removeEnd(previousTempNode);
				}
				else {
					return removeMiddle(previousTempNode);
				}
			}
			
			position++;
		}
		
		return null;
	}
	
	/**
	* Remove a SingleLinkedListObject at index
	*
	* @param index The numeric index to locate and remove SingleLinkedListObject
	* @returns SingleLinkedListObject if list is not empty.  Otherwise returns null.
	*/
	public function removeDataAt(index:Number):SingleLinkedListObject
	{
		return this.removeAt(index).get();
	}
	
	/**
	* Remove all nodes
	*/
	public function removeAll():Void
	{
		var previousTempNode:ListNode = head_;

		for(var tempNode:ListNode = head_; tempNode != null; previousTempNode = tempNode, tempNode = tempNode.getNext()) {
			if(null == tempNode.getNext() && null != previousTempNode.getNext()) {
				tempNode = null;
			}

			if(previousTempNode != tempNode) {
				previousTempNode.setNext(null);
				previousTempNode = null;
			}
		}

		if(null != head_) {
			head_.setNext(null);
			head_ = null;
		}
	}
	
	/**
	* Return the front node and delete front node if canDelete is set to true
	*
	* @param canDelete Flag that indicates if we can delete the front node
	* @returns ListNode if list is not empty.  Otherwise returns null.
	*/
	public function getFront(canDelete:Boolean):ListNode
	{
		var node:ListNode = head_;
		if(canDelete) {
			removeFront();
		}
		
		return node;
	}
	
	/**
	* Return the front SingleLinkedListObject and delete front SingleLinkedListObject if canDelete is set to true
	*
	* @param canDelete Flag that indicates if we can delete the front SingleLinkedListObject
	* @returns SingleLinkedListObject if list is not empty.  Otherwise returns null.
	*/
	public function getFrontData(canDelete:Boolean):SingleLinkedListObject
	{
		return this.getFront(canDelete).get();
	}
	
	/**
	* Print linked list
	*/
	public function print(Void):Void
	{
		trace("=======================")
		trace("Single Linked List HEAD");
		for(var tempNode:ListNode = head_; tempNode != null; tempNode = tempNode.getNext()) {
			trace(tempNode.toString());
		}
		trace("Single Linked List TAIL");
		trace("=======================")
	}
	
	/**
	* Gets the size of the list
	*
	* @returns the number of nodes in the list
	*/
	public function size(Void):Number
	{
		var tempNode:ListNode = head_;
		var size:Number = 0;

		// Loop through the list and count each node
		for(; tempNode != null; tempNode = tempNode.getNext()) {
			size++;
		}
		
		return size;
	}
	
	/**
	* Test for front node
	*
	* @param node Test for front node
	* @returns true if node is front node.  Otherwise returns false.
 	*/
	private function isFront(node:ListNode):Boolean
	{
		return (head_ == node) ? true : false; 
	}
	
	/**
	* Test for end node
	*
	* @param node Test for end node
	* @returns true if node is end node.  Otherwise returns false.
	*/
	private function isEnd(node:ListNode):Boolean
	{
		return(null == node.getNext()) ? true : false; 
	}
	
	/**
	* Test if list has only one node
	*
	* @returns true if list has only one node.  Otherwise returns false.
	*/
	private function hasOneNode(Void):Boolean
	{
		if((null == head_.getNext()) && (isEmpty())) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	* Insert at front
	*
	* @param node Insert node at front of list
	*/
	private function insertFront(node:ListNode):Void
	{
		if(isEmpty()) {
			head_ = node;
			node.setNext(null);
		}
		else {
			node.setNext(head_);
			head_ = node;
		}
	}

	/**
	* Remove node from front of list
	*
	* @returns ListNode if list is not empty.  Otherwise returns false.
	*/
	private function removeFront():ListNode
	{
		var listNode:ListNode = null;
		
		if(isEmpty()) {
			// Don't do anything
		}
		else if(hasOneNode()) {
			listNode = head_;
			head_ = null;
		}
		else {
			
			listNode = head_;
			head_ = head_.getNext();
		}
		
		return listNode;
	}

	/**
	* Insert at middle
	*
	* @param node Node that needs to be inserted
	* @param targetNode Insert node after targetNode
	*/
	private function insertMiddle(node:ListNode, targetNode:ListNode):Void 
	{
		node.setNext(targetNode.getNext());
		targetNode.setNext(node);
	}

	/**
	* Remove from middle
	* 
	* @param previous Node before node that needs to be removed
	* @returns ListNode
	*/
	private function removeMiddle(previous:ListNode):ListNode
	{
		var listNode:ListNode = previous.getNext();
		previous.setNext(listNode.getNext());
		return listNode;
	}

	/**
	* Insert at end
	*
	* @param node Node that needs to be inserted
	* @param targetNode Insert node after targetNode
	*/
	private function insertEnd(node:ListNode, targetNode:ListNode):Void
	{
		targetNode.setNext(node);
		node.setNext(null);
	}

	/**
	* Remove from end
	*
	* @param previous Node before node that needs to be removed
	* @returns ListNode
	*/
	private function removeEnd(previous:ListNode):ListNode
	{
		var listNode:ListNode = previous.getNext();
		previous.setNext(null);
		return listNode;
	}
}
