/*

Queue is part of ASLib

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
	Queue class
=====================================================================
*/

import org.springsoft.aslib.SingleLinkedList;
import org.springsoft.aslib.QueueObject;

class org.springsoft.aslib.Queue
{
	// The SingleLinkedList queue representation
	private var list_:SingleLinkedList;

	/**
	* Queue Constructor
	*/
	function Queue() 
	{
		list_ = new SingleLinkedList();
	}

	/**
	* Put item into queue
	*
	* @param item Data object
	*/
	public function enqueue(item:QueueObject):Void
	{
		list_.insertTail(item);
	}

	/**
	* Remove item from queue
	*
	* @returns data object
	*/
	public function dequeue(Void):QueueObject
	{
		// Get the ObjectListNode and then the data with get()
		return QueueObject(list_.getFront(true).get());
	}

	/**
	* Test if queue is empty
	*
	* @returns true if queue is empty.  Otherwise returns false.
	*/
	public function isEmpty(Void):Boolean
	{
		return list_.isEmpty();
	}

	/**
	* Print the queue
	*/
	public function print(Void):Void
	{
		list_.print();
	}
}
