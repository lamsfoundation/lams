/*

HashTable is part of ASLib

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
	HashTable class
=====================================================================
*/

import org.springsoft.aslib.SingleLinkedList;
import org.springsoft.aslib.HashTableObject;

class org.springsoft.aslib.HashTable
{
	// HashTable array default size
	private var size_:Number = 50;
	
	// Array of SingleLinkedLists
	private var hashTable_:Array;

	// Keep tarck of HashTable's size
	private var numItems_:Number = 0;

	/**
	* HashTable Constructor
	*
	* @param size The HashTable array size
	*/
	function HashTable(size:Number)
	{
		size_ = size;

		// Create the array of single linked lists
		hashTable_ = new Array(size_);

		for(var i = 0; i < size; i++) {
			hashTable_[i] = new SingleLinkedList();
		}
	}

	/**
	* Put item in HashTable
	*
	* @param key Hash key
	* @param data Data object
	*/
	public function put(key:Number, data:HashTableObject):Void
	{
		var index:Number = key % size_;
		hashTable_[index].insert(data);
		numItems_++;
	}

	/**
	* Get an item from the HashTable
	*
	* @param key The hash key
	* @returns data object
	*/
	public function get(key:Number):HashTableObject
	{
		var index:Number = key % size_;
		// Access SLL and get the node via the SLL's get method.
		// Then we call get on the node to extract the data object.
		return HashTableObject(hashTable_[index].get(key).get());
	}

	/**
	* Remove an item from the HashTable
	*
	* @param key The hash key
	*/
	public function remove(key:Number):Void
	{
		var index:Number = key % size_;
		hashTable_[index].remove(key);
		numItems_--;
	}

	/**
	* Remove all items from the HashTable
	*/
	public function removeAll(Void):Void
	{
		for(var i = 0; i < size_; i++) {
			hashTable_[i].removeAll();
		}
		numItems_ = 0;
	}

	/**
	* Get the size of the HashTable
	*
	* @returns size of HashTable
	*/
	public function size(Void):Number
	{
		return numItems_;
	}

	/**
	* Check if the HashTable is empty
	*
	* @returns true if HashTable is empty.  Otherwise returns false.
	*/
	public function isEmpty(Void):Boolean
	{
		return (0 < numItems_) ? false : true;
	}

	/**
	* Print HashTable
	*/
	public function print(Void):Void
	{
		if(isEmpty()) {
			trace("HastTable is empty!");
		}

		for(var i:Number = 0; i < hashTable_.length; i++) {
			if(!hashTable_[i].isEmpty()) {
				trace("HashTable[" + i + "]:");
				hashTable_[i].print();
			}
		}
	}
}
