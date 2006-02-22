/**
* @class it.sephiroth.XML2Object
* @author Alessandro Crugnola
* @version 1.0
* @description return an object with the content of the XML translated
* If a node has more than 1 child with the same name, an array is created with the children contents
* The object created will have this structure:<br/>
* 	- obj {<br/>
*		nodeName : {<br/>
*			attributes : an object containing the node attributes<br/>
*			data : an object containing the node contents<br/>
*	}<br/>
* @usage data = new XML2Object().parseXML( anXML);
*/
class it.sephiroth.XML2Object extends XML {
	private var oResult:Object = new Object ();
	private var oXML:XML;
	/**
	* @method get xml
	* @description return the xml passed in the parseXML method
	* @usage theXML = XML2Object.xml
	*/
	public function get xml():XML{
		return oXML
	}
	/**
	* @method public parseXML
	* @description return the parsed Object
	* @usage XML2Object.parseXML( theXMLtoParse );
	* @param sFile XML
	* @returns an Object with the contents of the passed XML
	*/
	public function parseXML (sFile:XML):Object {
		this.oResult = new Object ();
		this.oXML = sFile;
		this.oResult = this.translateXML();
		return this.oResult;
	}
	/**
	* @method private translateXML
	* @description core of the XML2Object class
	*/
	private function translateXML (from, path, name, position) {
		var nodes, node, old_path;
		if (path == undefined) {
			path = this;
			name = "oResult";
		}
		path = path[name];
		if (from == undefined) {
			from = new XML (this.xml);
			from.ignoreWhite = true;
		}
		if (from.hasChildNodes ()) {
			nodes = from.childNodes;
			if (position != undefined) {
				var old_path = path;
				path = path[position];
			}
			while (nodes.length > 0) {
				node = nodes.shift ();
				if (node.nodeName != undefined) {
					var __obj__ = new Object ();
					__obj__.attributes = node.attributes;
					__obj__.data = node.firstChild.nodeValue;
					if (position != undefined) {
						var old_path = path;
					}
					if (path[node.nodeName] != undefined) {
						if (path[node.nodeName].__proto__ == Array.prototype) {
							path[node.nodeName].push (__obj__);
							name = node.nodeName;
							position = path[node.nodeName].length - 1;
						} else {
							var copyObj = path[node.nodeName];
							path[node.nodeName] = new Array ();
							path[node.nodeName].push (copyObj);
							path[node.nodeName].push (__obj__);
							name = node.nodeName;
							position = path[node.nodeName].length - 1;
						}
					} else {
						path[node.nodeName] = __obj__;
						name = node.nodeName;
						position = undefined;
					}
				}
				if (node.hasChildNodes ()) {
					this.translateXML (node, path, name, position);
				}
			}
		}
		return this.oResult;
	}
}
