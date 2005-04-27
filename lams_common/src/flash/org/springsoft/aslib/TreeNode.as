/*

TreeNode is part of ASLib

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
	Binary Tree: Tree Node
=====================================================================
*/


import org.springsoft.aslib.BinaryTreeObject;

interface org.springsoft.aslib.TreeNode
{
	/**
	* Set left node
	*
	* @param node The node referenced by the left child
	*/
	public function setLeft(node:TreeNode):Void;
	
	/**
	* Set right node
	*
	* @param node The node referenced by the right child
	*/
	public function setRight(node:TreeNode):Void;
	
	/**
	* Set node data
	*
	* @param data Object data
	*/
	public function set(data:BinaryTreeObject):Void;

	/**
	* Get left node
	*
	* @returns left child node
	*/
	public function getLeft(Void):TreeNode;
	
	/**
	* Get right node
	*
	* @returns right child node
	*/
	public function getRight(Void):TreeNode;
	
	/**
	* Get node data
	*
	* @returns object data
	*/
	public function get(Void):BinaryTreeObject;
	
	/**
	* TreeNode string representation
	*
	* @returns the tree node's string representation
	*/
	public function toString(Void):String;
	
	/**
	* Return node key
	*
	* @returns the tree node's key
	*/
	public function getKey(Void):Number;
	
	/**
	* Test if node is a leave
	*
	* @returns true if node is a leave.  Otherwise returns false.
	*/
	public function isLeave(Void):Boolean;
}
