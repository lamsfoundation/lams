<?php

	/***************************************************************************

	phpdomxml-0.9.0 - an XML Document Object Model implementation for PHP 4.10+.
	(c) copyright 2002-2004, webtweakers.com. All rights reserved.
	By: Bas van Gaalen (bas at webtweakers dot com)
	$Id$

	****************************************************************************

	Licensed under the GNU General Public License (GPL)

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	***************************************************************************/	

	// Define element type constants
	define('XML_ELEMENT_NODE', 1);
	define('XML_TEXT_NODE', 3);
	define('XML_CDATA_SECTION_NODE', 4);
	define('XML_COMMENT_NODE', 8);
	define('XML_DOCUMENT_NODE', 9);

	// Define exceptions codes
	define('XML_INDEX_SIZE_ERR', 1);
	define('XML_NOT_FOUND_ERR', 8);

	define('XML_NO_FILE_ERR', 100);
	define('XML_FILE_NOT_FOUND_ERR', 101);
	define('XML_UNABLE_TO_OPEN', 102);
	define('XML_UNABLE_TO_WRITE', 103);

	define('XML_UNABLE_TO_CONNECT', 110);
	define('XML_UNKNOWN_RESPONSE', 111);

	define('XML_TYPE_MISMATCH', 120);



	// -------------------------------------------------------------------------
	/**
	 * XML_DOMException class
	 *
	 * A static class, used to raise DOM Exceptions.
	 *
	 * @package phpdomxml
	 */
	class XML_DOMException {

		/**
		 * Trigger a PHP error message.
		 *
		 * @param int $errCode the error code
		 * @param string $errText the error message
		 */
		function raise($errCode, $errText) {
			die('<p>DOMException: '.$errCode.': '.$errText.'</p>');
		}

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Node interface
	 *
	 * The XML_Node interface is the primary datatype for the entire Document
	 * Object Model. It represents a single node in the document tree.
	 *
	 * @package phpdomxml
	 */
	class XML_Node {

		/** the name of this node */
		var $nodeName;

		/** the value of this node */
		var $nodeValue;

		/** the type of this node */
		var $nodeType;

		var $parentNode = null;
		var $childNodes = array();
		var $firstChild = null;
		var $lastChild = null;
		var $previousSibling = null;
		var $nextSibling = null;
		var $attributes = null;
		var $ownerDocument = null;

		/**
		 * XML_Node class constructor
		 */
		function XML_Node() {
			$this->_id = rand();
		}

		/**
		 * Appends the specified child node to the XML object's child list.
		 * @param $newChild the new child to add
		 */
		function appendChild(&$newChild) {

			// set parent and owner
			$newChild->ownerDocument =& $this->ownerDocument;
			$newChild->parentNode =& $this;

			// Add child to list of childNodes
			$this->childNodes[] =& $newChild;

			// Set child's previousSibling
			$newChild->previousSibling =& $this->lastChild;

			// Set current lastChild's nextSibling to this child
			if (!is_null($this->lastChild))
				$this->lastChild->nextSibling =& $newChild;

			// Now (re)set firstChild and lastChild
			$this->firstChild =& $this->childNodes[0];
			$this->lastChild =& $newChild;

			return $newChild;

		} // appendChild

		function cloneNode($deep) {
			
		} // cloneNode

		function hasAttributes() {
			return !is_null($this->attributes);
		} // hasAttributes

		function hasChildNodes() {
			return !empty($this->childNodes);
		} // hasChildNodes

		// Inserts the node newChild before the existing child node refChild.
		// If refChild is null, insert newChild at the end of the list of
		// children.
		function insertBefore(&$newChild, $refChild = null) {

			if (is_null($refChild)) {

				// append to end
				return $this->appendChild($newChild);

			}

			// set parent and owner
			$newChild->ownerDocument =& $this->ownerDocument;
			$newChild->parentNode =& $this;

			// browse childNodes list and create new structure
			$len = count($this->childNodes);
			$newList = array();
			for ($i = 0; $i < $len; $i++) {
				$child =& $this->childNodes[$i];
				if ($child->_id == $refChild->_id) {
					if ($i == 0) {

						// newChild is first in list
						$this->firstChild =& $newChild;
						$newChild->previousSibling = null;

					} else {

						// get previous in list and set refs
						$prevChild =& $this->childNodes[$i-1];
						$prevChild->nextSibling =& $newChild;
						$newChild->previousSibling =& $prevChild;

					}

					// insert newChild before child
					$newChild->nextSibling =& $child;
					$child->previousSibling =& $newChild;

					// insert in childNodes list
					$newList[] =& $newChild;

				}

				$newList[] =& $child;

			}

			// replace old list with new one
			$this->childNodes = $newList;

			// return inserted child
			return $newChild;

		} // insertBefore

		function removeChild($oldChild) {

			// browse childNodes list and create new structure
			$found = false;
			$len = count($this->childNodes);
			$newList = array();
			for ($i = 0; $i < $len; $i++) {
				$child =& $this->childNodes[$i];
				if ($child->_id == $oldChild->_id) {
					$found = true;

					// get prev & next child
					$prevChild =& $child->previousSibling;
					$nextChild =& $child->nextSibling;

					// reset siblings
					if (is_null($prevChild) && !is_null($nextChild)) {
						// first child in list
						$this->firstChild =& $nextChild;
						$nextChild->previousSibling = null;

					} else if (!is_null($prevChild) && !is_null($nextChild)) {
						// somewhere in middle of list
						$prevChild->nextSibling =& $nextChild;
						$nextChild->previousSibling =& $prevChild;

					} else if (!is_null($prevChild) && is_null($nextChild)) {
						// last child in list
						$this->lastChild =& $prevChild;
						$prevChild->nextSibling = null;

					}

				} else {

					$newList[] =& $child;

				}

			}

			if ($found) {

				// replace old list with new one
				$this->childNodes = $newList;

				// return inserted child
				return $oldChild;

			}

			XML_DOMException::raise(XML_NOT_FOUND_ERR,
				'child could not be removed: child not found');

		} // removeChild

		function replaceChild($newChild, $oldChild) {
			
		} // replaceChild

		// dummies
		function getElementsByTagName($tagName) { }
		function getElementById($id) { }
		function toString($pretty = false, $tabs = '') {}

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Element intrface
	 *
	 * Element nodes are among the most common objects in the XML document tree.
	 * Element nodes can have attributes associated with them.
	 *
	 * @package phpdomxml
	 */
	class XML_Element extends XML_Node {

		// Constructor
		function XML_Element($tagName) {
			$this->XML_Node();
			$this->nodeName = $tagName;
			$this->nodeType = XML_ELEMENT_NODE;
		}

		// Retrieves an attribute value by name
		function getAttribute($name) {
			return ($this->hasAttribute($name))?$this->attributes[$name]:'';
		} // getAttribute

		// return elements with specified attribute 'id' set
		function getElementById($id) {

			// is this one of the elements we're looking for?
			if ($this->getAttribute('id') == $id)
				return $this;

			// browse children
			if ($this->hasChildNodes())
				foreach ($this->childNodes as $child)
					if ($child->getAttribute('id') == $id)
						return $child;

			return null;

		} // getElementById

		// Return elements with specified tag name
		function getElementsByTagName($tagName) {

			$elms = array();

			// is this one of the elements we're looking for?
			if ($this->nodeName == $tagName || $tagName == '*')
				$elms[] = $this;

			// browse
			if ($this->hasChildNodes())
				foreach ($this->childNodes as $child)
					$elms = array_merge($elms, $child->getElementsByTagName($tagName));

			return $elms;

		} // getElementByTagName

		// Returns true when an attribute with given name exists
		function hasAttribute($name) {
			return isset($this->attributes[$name]);
		} // hasAttribute

		// Removes an attribute by name
		function removeAttribute($name) {
			unset($this->attributes[$name]);
		} // removeAttribute

		// Adds a new attribute, or changes an existing one
		function setAttribute($name, $value) {
			$this->attributes[$name] = $value;
		} // setAttribute

		function toString($pretty = false, $tabs = '') {

			$s = '';
			if ($pretty) $s .= $tabs;
			$s .= '<'.$this->nodeName;

			// collect attributes, if any
			if ($this->hasAttributes()) {
				foreach ($this->attributes as $key => $val)
					$s .= ' '.$key.'="'.$val.'"';
			}

			// collect children, if any
			if ($this->hasChildNodes()) {
				$s .= '>';
				if ($pretty) $s .= "\n";
				foreach ($this->childNodes as $child)
					$s .= $child->toString($pretty, $tabs."\t");
				if ($pretty) $s .= $tabs;
				$s .= '</'.$this->nodeName.'>';
				if ($pretty) $s .= "\n";
			} else {
				$s .= '/>';
				if ($pretty) $s .= "\n";
			}

			return $s;
		} // toString

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_CharacterData interface
	 *
	 * The XML_CharacterData interface extends XML_Node with a set of attributes
	 * and methods for accessing character data in the DOM.
	 *
	 * @package phpdomxml
	 */
	class XML_CharacterData extends XML_Node {

		var $data;
		var $length;

		// XML_CharacterData Constructor
		function XML_CharacterData($data) {
			$this->XML_Node();
			$this->data = $data;
			$this->length = strlen($data);
		}

		// Appends the supplied string to the existing string data
		function appendData($data) {
			$this->data .= $data;
			$this->nodeValue = $this->data;
			$this->length = strlen($this->data);
		} // appendData

		// Deletes specified data.
		function deleteData($offset, $count) {
			if ($offset < 0 || $offset > $this->length)
				XML_DOMException::raise(INDEX_SIZE_ERR,
					'offset '.$offset.' is out of range');
			if ($count < 0)
				XML_DOMException::raise(INDEX_SIZE_ERR,
					'count '.$count.' is out of range');
			$this->data = substr($this->data, 0, $offset).
				substr($this->data, $offset+$count);
			$this->nodeValue = $this->data;
			$this->length = strlen($this->data);
		} // deleteData

		// Inserts a string at the specified offset
		function insertData($offset, $data) {
			if ($offset < 0 || $offset > $this->length)
				XML_DOMException::raise(INDEX_SIZE_ERR,
					'offset '.$offset.' is out of range');
			$this->data = substr($this->data, 0, $offset).$data.
				substr($this->data, $offset);
			$this->nodeValue = $this->data;
			$this->length = strlen($this->data);
		} // insertData

		// Replaces the specified number of characters with the supplied string
		function replaceData($offset, $count, $data) {
			$this->deleteData($offset, $count);
			$this->insertData($offset, $data);
		} // replaceData

		// Retrieves a substring of the full string from the specified range
		function substringData($offset, $count) {
			if ($offset < 0 || $offset > $this->length)
				XML_DOMException::raise(INDEX_SIZE_ERR,
					'offset '.$offset.' is out of range');
			if ($count < 0)
				XML_DOMException::raise(INDEX_SIZE_ERR,
					'count '.$count.' is out of range');
			return substr($this->data, $offset, $count);
		} // substringData

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Comment interface
	 *
	 * The content refers to all characters between the start <!-- and end -->
	 * tags.
	 *
	 * @package phpdomxml
	 */
	class XML_Comment extends XML_CharacterData {

		// Constructor
		function XML_Comment($comment) {

			// call parent's constructor
			parent::XML_CharacterData($comment);

			$this->nodeName = '#comment';
			$this->nodeValue = $comment;
			$this->nodeType = XML_COMMENT_NODE;
		}

		function toString($pretty = false, $tabs = '') {
			$s = '';
			if ($pretty) $s .= $tabs;
			$s .= '<!-- '.$this->nodeValue.' -->';
			if ($pretty) $s .= "\n";
			return $s;
		} // toString

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Text interface
	 *
	 * XML refers to this text content as character data and distinguishes it
	 * from markup, the tags that modify that character data. 
	 *
	 * @package phpdomxml
	 */
	class XML_Text extends XML_CharacterData {

		// Constructor
		function XML_Text($text) {

			// call parent's constructor
			parent::XML_CharacterData($text);
			
			$this->nodeName = '#text';
			$this->nodeValue = $text;
			$this->nodeType = XML_TEXT_NODE;
		}

		function splitText($offset) {
			
		}

		function toString($pretty = false, $tabs = '') {
			$s = '';
			if ($pretty) $s .= $tabs;
			$s .= $this->nodeValue;
			if ($pretty) $s .= "\n";
			return $s;
		} // toString

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_CDATASection interface
	 *
	 * Every CDATA-section in an XML document transforms into the Node of the
	 * type CDATASection in DOM. The XML_CDATASection interface inherits the
	 * XML_CharacterData interface through the XML_Text interface.
	 *
	 * @package phpdomxml
	 */
	class XML_CDATASection extends XML_Text {

		// Constructor
		function XML_CDATASection($data) {
			$this->nodeName = '#cdata-section';
			$this->nodeValue = $data;
			$this->nodeType = XML_CDATA_SECTION_NODE;
		}

		function toString($pretty = false, $tabs = '') {
			$s = '';
			if ($pretty) $s .= $tabs;
			$s .= '<![CDATA['.$this->nodeValue.']]>';
			if ($pretty) $s .= "\n";
			return $s;
		} // toString

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Document interface
	 *
	 * The XML_Document interface represents the entire HTML or XML document.
	 * Conceptually, it is the root of the document tree, and provides the
	 * primary access to the document's data.
	 *
	 * @package phpdomxml
	 */
	class XML_Document extends XML_Node {

		function XML_Document() {
			$this->XML_Node();
			$this->nodeName = '#document';
			$this->nodeType = XML_DOCUMENT_NODE;
		}

		function &createCDATASection($data) {
			$node =& new XML_CDATASection($data);
			$node->ownerDocument =& $this;
			return $node;
		} // createCDATASection

		function &createComment($comment) {
			$node =& new XML_Comment($comment);
			$node->ownerDocument =& $this;
			return $node;
		} // createComment

		// Creates a new XML element with specified name
		function &createElement($tagName) {
			$node =& new XML_Element($tagName);
			$node->ownerDocument =& $this;
			return $node;
		} // createElement

		// Creates a new XML text node with specified text
		function &createTextNode($text) {
			$node =& new XML_Text($text);
			$node->ownerDocument =& $this;
			return $node;
		} // createTextNode

		// Return elements with specified tag name
		function getElementsByTagName($tagName) {

			$elms = array();

			// browse
			foreach ($this->childNodes as $child)
				$elms = array_merge($elms, $child->getElementsByTagName($tagName));

			// return possible list of elements
			return $elms;

		} // getElementByTagName

		// Return element with specified id
		function getElementById($id) {

			// browse children
			foreach ($this->childNodes as $child) {
				$newChild = $child->getElementById($id);
				if ($newChild->getAttribute('id') == $id)
					return $newChild;
			}

			return null;

		} // End getElementById

		// Evalutes the specified XML object, constructs a textual
		//		representation of the XML structure including the node, children
		//		and attributes, and returns the result as a string.
		function toString($pretty = false, $tabs = '') {
			$s = '';
			foreach ($this->childNodes as $child)
				$s .= $child->toString($pretty, $tabs);
			return $s;
		} // toString

	}



	// -------------------------------------------------------------------------
	/**
	 * XML class
	 *
	 * The XML object inherits from XML_Document and serves as access point for
	 * your XML needs in projects.
	 *
	 * @package phpdomxml
	 */
	class XML extends XML_Document {

		// Constructor
		function XML($url = '') {

			// call parent's constructor
			$this->XML_Document();

			// Load the referenced XML document
			if (!empty($url)) $this->load($url);

		}

		/**
		 * Load an XML document from the specified URL.
		 *
		 * @param string $url location where the XML document recides.
		 */
		function load($url = '') {
			if (empty($url))
				XML_DOMException::raise(XML_NO_FILE_ERR,
					'No file or url specified');
			if (function_exists('file_get_contents')) {
				$doc = @file_get_contents($url);
				if (!$doc || empty($doc))
					XML_DOMException::raise(XML_FILE_NOT_FOUND_ERR,
						'File not found or document is empty');
			} else {
				$doc = @file($url);
				if (!$doc || empty($doc))
					XML_DOMException::raise(XML_NOT_FOUND_ERR,
						'File not found or document is empty');
				$doc = implode('', $doc);
			}
			$this->parseXML($doc);
		} // load

		/**
		 * Save an XML document to the specified file or URL.
		 *
		 * @param string $fileName the file name for the file to create.
		 * @param boolean $pretty true for readable layout, false for no layout.
		 */
		function save($fileName, $pretty = false) {
			if (!$fp = fopen($fileName, 'w'))
				XML_DOMException::raise(XML_UNABLE_TO_OPEN,
					'Unable to open file '.$fileName.' for writing');
			if (!fwrite($fp, $this->toString($pretty)))
				XML_DOMException::raise(XML_UNABLE_TO_WRITE,
					'Unable to open write to '.$fileName);
			fclose($fp);
			return true;
		} // save

		/**
		 * Parses the XML text specified in the data argument.
		 *
		 * @param string $data the XML document to parse.
		 */
		function parseXML($data) {

			// Strip white space
			$data = preg_replace("/>\s+</i", "><", $data);

			$parser = new XML_Parser($this);
			$parser->parse($data);

		} // parseXML

		/**
		 * Encodes the specified XML object into an XML document and sends
		 * it to the specified URL using the POST method.
		 *	$url is of the form: (http://)www.domain.com:port/path/to/file
		 *
		 * @param string $url destination where to send the XML document to.
		 */
		function send($url) {

			// Get xml document
			$strXML = $this->toString();

			// Get url parts
			if (!preg_match("/http/", $url)) $url = "http://".$url;
			$urlParts = parse_url($url);
			$host = isset($urlParts['host'])?$urlParts['host']:'localhost';
			$port = isset($urlParts['port'])?$urlParts['port']:80;
			$path = isset($urlParts['path'])?$urlParts['path']:"/";

			// Open a connection with the required host
			$fp = fsockopen($host, $port, $errno, $errstr);
			if (!$fp)
				XML_DOMException::raise(XML_UNABLE_TO_CONNECT,
					'Unable to connect to '.$host.' at port '.$port.': ('.$errno.
					') '.$errstr);

			// Send the xml document
			fputs($fp, "POST ".$path." HTTP/1.0\r\n".
				"Host: ".$host."\r\n".
				"Content-length: ".strlen($strXML)."\r\n".
				"Content-type: ".$this->contentType."\r\n".
				"Connection: close\r\n\r\n".
				$strXML."\r\n");

			return $fp;
			
		} // send

		/**
		 * Encodes the specified XML object into a XML document, sends
		 *	it to the specified URL using the POST method, downloads
		 * the server's response and then loads it into the target.
		 * $url is of the form: (http://)www.domain.com:port/path/to/file
		 *
		 * @param string $url destination where to send the XML document to.
		 * @param object $target DOM object where the response will be received.
		 */
		function sendAndLoad($url, &$target) {

			// Check target type, fail on wrong type
			if (gettype($target) != 'object')
				XML_DOMException::raise(XML_TYPE_MISMATCH,
					"Target is of type '".gettype($target).
					"', but should be 'object'");

			// Send the xml document
			if (!$fp = $this->send($url)) return false;

			// Recieve response
			$buf = '';
			while (!feof($fp)) $buf .= fread($fp, 128);
			fclose($fp);

			// Filter xml out response (dump http headers)
			if (!preg_match("/(<.*>)/msi", $buf, $matches)) // Greedy match
				XML_DOMException::raise(XML_UNKNOWN_RESPONSE,
					'Unidentified server response: no xml was sent');
			$xmlResponse = $matches[1];
			$target->parseXML($xmlResponse);

			return true;

		} // sendAndLoad

	}



	// -------------------------------------------------------------------------
	/**
	 * XML_Parser class
	 *
	 * The XML_Parser parses an XML document into a DOM object.
	 *
	 * @package phpdomxml
	 */
	class XML_Parser {

		var $dom = null;
		var $lastChild = null;
		var $parser = null;
		var $encoding = 'ISO-8859-1';
		var $inText = false;
		var $inCData = false;
		var $CData = '';
		var $xmlDecl = '';
		var $version = null;
		var $docTypeDecl = '';

		// Constructor
		function XML_Parser(&$dom) {
			$this->dom =& $dom;
			$this->lastChild =& $this->dom;
		}

		// parse raw xml document into 'this'
		function parse($data) {

			// Get xml declration from document and set in object
			if (preg_match("/<?xml\ (.*?)\?>/i", $data, $matches)) {
				$this->xmlDecl = "<?xml ".$matches[1]."?>";

				// Get version
				if (preg_match("/version=\"(.*?)\"/i", $matches[1], $ver)) {
					$this->version = $ver[1];
				}

				// Get encoding
				if (preg_match("/encoding=\"(.*?)\"/i", $matches[1], $enc)) {
					$this->encoding = $enc[1];
				}

			}

			// Get document type decleration from document and set in object
			if (preg_match("/<!doctype\ (.*?)>/i", $data, $matches)) {
				$this->docTypeDecl = "<!DOCTYPE ".$matches[1].">";
			}

			// try to create parser with found encoding
			$this->parser = @xml_parser_create($this->encoding);

			// if creation failed, use php's default encoding
			if (!is_resource($this->parser))
				$this->parser = @xml_parser_create();
			
			// set options
			xml_set_object($this->parser, &$this);
			xml_set_element_handler($this->parser, 'openHandler', 'closeHandler');
			xml_set_character_data_handler($this->parser, 'cdataHandler');
			xml_set_default_handler($this->parser, 'dataHandler');
			xml_parser_set_option($this->parser, XML_OPTION_CASE_FOLDING, 0); 	
			xml_parser_set_option($this->parser, XML_OPTION_SKIP_WHITE, 1);

			// parse the raw data
			xml_parse($this->parser, $data);

			// free used memory
			xml_parser_free($this->parser);

		} // parse

		// tag open handler
		function openHandler(&$parser, $tag, $attr) {

			// create the element			
			$node =& $this->dom->createElement($tag);

			// append node to dom structure
			$this->lastChild->appendChild($node);

			// attach attributes
			while (list($name, $value) = each($attr)) {
				$node->setAttribute($name, $value);
			}

			// next child will be added to this node
			$this->lastChild =& $node;

		} // openHandler

		// tag close handler
		function closeHandler(&$parser, $tag) {

			// end of multiline text node?
			if ($this->inText) {
				$this->inText = false;
				$node =& $this->dom->createTextNode($this->CData);
				$this->lastChild->appendChild($node);
				$this->CData = '';
			}

			// next child will be added to this node's parent
			$this->lastChild =& $this->lastChild->parentNode;

		} // closeHandler

		// cdata handler
		function cdataHandler(&$parser, $data) {
			if (!$this->inCData) $this->inText = true;
			$this->CData .= $data;
		} // cdataHandler

		// misc data handler
		function dataHandler(&$parser, $data) {

			// determine data type
			$prefix = strtolower(substr($data, 0, 3));
			switch ($prefix) {

				// xml decleration
				case '<?x':
					break;

				// comment
				case '<!-':
					$node =& $this->dom->createComment(substr($data, 4, -3));
					$this->lastChild->appendChild($node);
					break;

				// cdata section start
				case '<![':
					$this->inCData = true;
					break;

				// cdata section end
				case ']]>':
					$this->inCData = false;
					$node =& $this->dom->createCDATASection($this->CData);
					$this->lastChild->appendChild($node);
					$this->CData = '';
					break;

			}
			
		} // dataHandler

	}

?>
