/*

ObjectTreeNode is part of ASLib

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
	Tree Node: Object Tree Node
=====================================================================
*/

import org.springsoft.aslib.TreeNode;
import org.springsoft.aslib.BinaryTreeObject;

class org.springsoft.aslib.ObjectTreeNode implements TreeNode
{

	// Reference to left node
	private var left_:ObjectTreeNode;

	// Reference to right node
	private var right_:ObjectTreeNode;
	
	// Node data member
	private var data_:BinaryTreeObject;
	
	/**
	* ObjectTreeNode Constructor
	*
	* @param data The object's data
	*/
	function ObjectTreeNode(data:BinaryTreeObject)
	{
		left_ = null;
		right_ = null;
		data_ = data;
	}
	
	/**
	* Implementation set left node
	*
	* @param node The node referenced by the left child
	*/
	public function setLeft(node:TreeNode):Void
	{
		left_ = ObjectTreeNode(node);
	}
	
	/**
	* Implementation set right node
	*
	* @param node The node referenced by the right child
	*/
	public function setRight(node:TreeNode):Void
	{
		right_ = ObjectTreeNode(node);
	}
	
	/**
	* Implementation set method
	*
	* @param data Object data
	*/
	public function set(data:BinaryTreeObject):Void
	{
		data_ = data;
	}

	/**
	* Implementation get left node
	*
	* @returns left child node
	*/
	public function getLeft(Void):TreeNode
	{
		return left_;
	}
	
	/**
	* Implementation get right node
	*
	* @returns right child node
	*/
	public function getRight(Void):TreeNode
	{
		return right_;
	}
	
	/**
	* Implementation get method
	*
	* @returns object data
	*/
	public function get(Void):BinaryTreeObject
	{
		return data_;
	}
	
	/**
	* Implementation toString method
	*
	* @returns the tree node's string representation
	*/
	public function toString(Void):String
	{
		return data_.toString();
	}
	
	/**
	* Implementation getKey method
	*
	* @returns the tree node's key
	*/
	public function getKey(Void):Number
	{
		return data_.getKey();
	}
	
	/**
	* Implementation isLeave method
	*
	* @returns true if node is a leave.  Otherwise returns false.
	*/
	public function isLeave(Void):Boolean
	{
		if(null == left_ && null == right_) {
			return true;
		}
		else {
			return false;
		}
	}
}

