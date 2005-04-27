/*

BinaryTree is part of ASLib

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
	Binary Tree
=====================================================================
*/

import org.springsoft.aslib.BinaryTreeObject;
import org.springsoft.aslib.TreeNode;
import org.springsoft.aslib.ObjectTreeNode;

// BinaryTree Class
class org.springsoft.aslib.BinaryTree
{
	// Tree root
	private var treeRoot_:TreeNode;
	
	/** 
	* BinaryTree Constructor 
	*/
	function BinaryTree()
	{
		treeRoot_ = null;
	}
	
	/**
	* Iterative insert
	*
	* @param data The data to insert into the tree
	*/
	public function insertIter(data:BinaryTreeObject):Void
	{
		var runner:TreeNode = treeRoot_;
		var node:ObjectTreeNode = new ObjectTreeNode(data);
		
		if(isEmpty()) {
			treeRoot_ = node;
			return;
		}
			
		while(true) {
			if(node.getKey() == runner.getKey()) {
				return;
			}
			else if(node.getKey() < runner.getKey()) {
				
				if(runner.getLeft() == null ) {
                	runner.setLeft(node);
                	return;
             }
             else
                runner = runner.getLeft();
          }
          else if(node.getKey() > runner.getKey()){
			  if(runner.getRight() == null ) {
				  
				  runner.setRight(node);
				  return;
             }
             else
                runner = runner.getRight();
           }
       } 
    }
	
	/**
	* Recursive insert
	*
	* @param data The data to insert into the tree
	*/
	public function insert(data:BinaryTreeObject):Void
	{
		if(isEmpty()) {
			treeRoot_ = new ObjectTreeNode(data);
			return;
		}
		insertHelper(new ObjectTreeNode(data), treeRoot_);
	}

	/**
	* Search the binary tree for a key
	*
	* @param key The key to search for in the tree
	* @returns BinaryTreeObject if key was found.  Otherwise returns null.
	*/
	public function search(key:Number):BinaryTreeObject
	{
		if(isEmpty()) {
			return null;
		}
		
		return searchHelper(treeRoot_, key);
	}
	
	/**
	* Print tree PreOrder
	*
	* @param tree Any tree node starting at the root
	*/
	public function printPreOrder(Void):Void
	{
		printPreOrderHelper(treeRoot_);
	}
	
	/**
	* Print tree PostOrder
	*
	* @param tree Any tree node starting at the root
	*/
	public function printPostOrder(Void):Void
	{
		printPostOrderHelper(treeRoot_);
	}
	
	/**
	* Print tree InOrder
	*
	* @param tree Any tree node starting at the root
	*/
	public function printInOrder(Void):Void
	{
		printInOrderHelper(treeRoot_);
	}
	
	/**
	* Remove node
	* 
	* @param key The key to search for and to remove it from the tree
	*/
	public function remove(key:Number):Void
	{
		var tmpNode:TreeNode = null;
		
		// Test if we remove root node
		if(key == treeRoot_.getKey()) {
			if(isEmpty() && treeRoot_.isLeave()) {
				treeRoot_ = null;
			}
			else if(null == treeRoot_.getLeft()) {
				tmpNode = treeRoot_;
				treeRoot_ = treeRoot_.getRight();
				tmpNode = null;
			}
			else if(null == treeRoot_.getRight()) {
				tmpNode = treeRoot_;
				treeRoot_ = treeRoot_.getLeft();
				tmpNode = null;
			}
			else {
				tmpNode = treeRoot_.getLeft();
				while(tmpNode.getRight()) {
					tmpNode = tmpNode.getRight();
				}
				tmpNode.setRight(treeRoot_.getRight());
				tmpNode = treeRoot_;
				treeRoot_ = treeRoot_.getLeft();
				tmpNode.setLeft(null);
				tmpNode.setRight(null);
				tmpNode = null;
			}
		}
		else {
			removeHelper(key, treeRoot_);
		}
	}
	
	/**
	* Test if tree is empty
	*
	* @returns true if tree is empty.  Otherwise returns false.
	*/
	private function isEmpty(Void):Boolean
	{
		return (null == treeRoot_) ? true : false;
	}
	
	/**
	* Helper Print tree PreOrder
	*
	* @param tree Any tree node starting at the root
	*/
	private function printPreOrderHelper(tree:TreeNode):Void
	{
		if(null != tree) {
			trace(tree.toString());
			printPreOrderHelper(tree.getLeft());
			printPreOrderHelper(tree.getRight());
		}
	}
	
	/**
	* Helper Print tree InOrder
	*
	* @param tree Any tree node starting at the root
	*/
	private function printInOrderHelper(tree:TreeNode):Void
	{
		if(null != tree) {
			printInOrderHelper(tree.getLeft());
			trace(tree.toString());
			printInOrderHelper(tree.getRight());
		}
	}
	
	/**
	* Helper recursive insert
	*
	* @param node The data to insert into the tree
	* @param tree Any tree node starting at the root
	*/
	private function insertHelper(node:TreeNode, tree:TreeNode):Void
	{
		if(node.getKey() < tree.getKey()) {
			if(tree.getLeft() == null) {
				tree.setLeft(node);
			}
			else {
				insertHelper(node, tree.getLeft());
			}
		}
		else if(node.getKey() > tree.getKey()) {
			if(tree.getRight() == null) {
				tree.setRight(node);
			}
			else {
				insertHelper(node, tree.getRight());
			}
		}
	}
	
	/**
	* Helper search binary tree
	*
	* @param tree Any tree node starting at the root
	* @param key The key to search for in the tree
	* @returns BinaryTreeObject if key was found.  Otherwise returns null.
	*/
	private function searchHelper(tree:TreeNode, key:Number):BinaryTreeObject
	{
		if(key == tree.getKey()) {
			return tree.get();
		}
		else if(key < tree.getKey()) {
			return searchHelper(tree.getLeft(), key);
		}
		else if(key > tree.getKey()) {
			return searchHelper(tree.getRight(), key);
		}
	}
	
	/**
	* Helper Print tree PostOrder
	*
	* @param tree Any tree node starting at the root
	*/
	private function printPostOrderHelper(tree:TreeNode):Void
	{
		if(null != tree) {
			printPostOrderHelper(tree.getLeft());
			printPostOrderHelper(tree.getRight());
			trace(tree.toString());
		}
	}
	
	/**
	* Helper remove node
	*
	* @param key The key to search for and to remove in the tree
	* @param tree Any tree node starting at the root
	*/
	private function removeHelper(key:Number, tree:TreeNode):TreeNode
	{
		var tmpNode:TreeNode = null;
		var findNode:TreeNode = null;
		
		if(null == tree) {
			return null;
		}
		
		if(key == tree.getKey()) {
			tmpNode = tree.getLeft();
			if(!tree.getLeft()) {
				tmpNode = tree.getRight();
			}
			else if(tree.getRight()) {
				findNode = tree.getLeft();
				while(findNode.getRight()) {
					findNode = findNode.getRight();
				}
				findNode.setRight(tree.getRight());
			}
			return tmpNode;
		}
		else if(key < tree.getKey()) {
			tree.setLeft(removeHelper(key, tree.getLeft()));
		}
		else {
			tree.setRight(removeHelper(key, tree.getRight()));
		}
		return tree;
	}
}

