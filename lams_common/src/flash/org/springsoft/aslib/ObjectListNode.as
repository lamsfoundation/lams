/*

ObjectListNode is part of ASLib

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
	Object Node is an implementation of the Node class. 
	Used for stack class.
=====================================================================
*/

import org.springsoft.aslib.ListNode;
import org.springsoft.aslib.SingleLinkedListObject;

class org.springsoft.aslib.ObjectListNode implements ListNode
{
	// Reference to next node
	private var next_:ObjectListNode;

	// Node data member
	private var data_:SingleLinkedListObject;

	/**
	* ObjectListNode Constructor
	*
	* @param data Object data
	*/
	function ObjectListNode(data:SingleLinkedListObject) 
	{
		next_ = null;
		data_ = data;
	}

	/**
	* Implement get method
	*
	* @returns object data
	*/
	public function get(Void):SingleLinkedListObject
	{
		return data_;
	}

	/**
	* Implement getNext method
	*
	* @returns the next list node
	*/
	public function getNext(Void):ListNode
	{
		return next_;
	}

	/**
	* Implement setNext method
	*
	* @param node Node to be linked by the next reference
	*/
	public function setNext(node:ListNode):Void
	{
		next_ = ObjectListNode(node);
	}

	/**
	* Implement set method
	*
	* @param data Object data
	*/
	public function set(data:SingleLinkedListObject):Void
	{
		data_ = data;
	}

	/**
	* Implement toString method
	*
	* @returns the list node's string representation
	*/
	public function toString(Void):String 
	{	
		return data_.toString();
	}

	/**
	* Implement getKey method
	*
	* @returns the list node's key
	*/
	public function getKey(Void):Number
	{
		return data_.getKey();
	}
}
