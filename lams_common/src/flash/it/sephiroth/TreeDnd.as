import it.sephiroth.iTreeDnd
import mx.events.EventDispatcher
import mx.core.UIComponent


[Event("double_click")]
[Event("drag_start")]
[Event("drag_complete")]
[Event("drag_fail")]
[InspectableList("enabled","visible","dragRules","rowHeight")]


/**
 * Drag and Drop extension for Macromedia Tree component.
 * It allows you to define different rules for dragging items and folder 
 * into a tree component
 * @author	alessandro crugnola
 * @version	1.5
 */
class it.sephiroth.TreeDnd extends UIComponent implements iTreeDnd
{
	// define component specific variables
	static var symbolName:String   = "TreeDnd"
	static var symbolOwner:Object  = TreeDnd
	private var className:String   = "TreeDnd"
	
	static var componentVersion:String = "1.5";
	// define private variables
	private var label:MovieClip
	private var tree:mx.controls.Tree
	private var icon:MovieClip
	private var tree_listener:Object
	private var controller_mc:MovieClip
	private var boundingBox_mc:MovieClip
	private var tfList:Object
	private var __dropTarget:Array
	private var _iconFunction:Function
	private var __rowHeight:Number = 20
	private var __dropFunction:Function
	private var __dragFunction:Function
	
	private var TREEDEPTH:Number = 10
	private var CONTDEPTH:Number = 11
	private var ICONDEPTH:Number = 12
	
	var addEventListener:Function
	var removeEventListener:Function
	var dispatchEvent:Function
	
	/** 
	* deny drag a folder.
	* Each of treednd constant can be used associated with multiple other constants
	* @usage <code>import it.sephiroth.TreeDnd
	* myDndTree.dragRules = TreeDnd.DENYDRAGFOLDER | TreeDnd.DENYDROPITEM
	* </code>
	*/
	static var DENYDRAGFOLDER:Number      = 1
	/** deny drag an item node */
	static var DENYDRAGITEM:Number        = 2
	/** deny drop into an item */
	static var DENYDROPITEM:Number        = 4
	/** everything is allowed */
	static var DEFAULT:Number             = 0
	/** deny drop through nodes, only into is allowed */
	static var DENYDROPUPSIDEDOWN:Number  = 8
	/** deny all */
	static var DENYALL:Number             = DENYDRAGITEM | DENYDRAGFOLDER | DENYDROPITEM | DENYDROPUPSIDEDOWN
	
	private var __options:Number = DEFAULT
	
	/**
	 * constructor
	 */
	function TreeDnd()
	{
	}
	
	/**
	* internal, set enabled on off
	* @param enabled
	*/
	private function setEnabled(enabled:Boolean):Void
	{
		super.setEnabled();
		tree.enabled = enabled
	}	
	
	// ******************************************
	// Private methods
	// ******************************************

	/**
	 * override UIComponent init constructor
	 * @return	Void
	 */
	private function init():Void
	{
		super.init();
		var width  = __width
		var height = __height		
		__options = DEFAULT
		boundingBox_mc._visible = false
		boundingBox_mc._width = boundingBox_mc._height = 0;
		// create the objects to be used in the component
		createClassObject(mx.controls.Tree, "tree", TREEDEPTH, {_width : width, _height : height})
		controller_mc = createEmptyObject("controller_mc", CONTDEPTH);
		// initialize the event object
		EventDispatcher.initialize( this );
		initTreeListener();
		setEnabled(enabled)
		tree.rowHeight = __rowHeight
	}
	

	/**
	 * 
	 * @param	Void
	 * @return	Void
	 */
	function size(Void) : Void
	{
		super.size();
		tree.setSize( __width, __height, true );
	}
	

	/**
	 * 
	 * @param	Void
	 * @return	Void	
	 */
	function draw(Void):Void{
		super.draw();
	}
	
	/**
	 * Initialize Tree listeners and Tree MovieClip controller
	 * @usage	
	 * @return	Void	
	 */
	private function initTreeListener():Void
	{
		// Tree listeners
		tfList = new Object();
		tfList.selectedItem  = undefined
		tfList.selectedIndex = undefined
		tfList._parent = this
		tfList._time = getTimer();
		tfList._oldItem = undefined
		// tree itemRollOver
		tfList.itemRollOver = function( evt ){
			var item = this._parent.tree.getItemAt(evt.index)
			if( item != undefined )
			{
				var opt:Number   = this._parent.dragRules
				var opt_1:Number = opt
				if(opt >= TreeDnd.DENYDROPUPSIDEDOWN){
					opt_1 = opt ^ TreeDnd.DENYDROPUPSIDEDOWN
				}
				if( this._parent.__dragFunction != undefined){
					if(!this._parent.__dragFunction( item )){
						this.itemRollOut()
						return
					}
				} else {
					if( (opt_1 == DENYDRAGFOLDER  or opt_1 == (DENYDRAGFOLDER | DENYDROPITEM) or opt_1 == (DENYDRAGITEM | DENYDRAGFOLDER) or opt == DENYALL) and this._parent.tree.getIsBranch( item ))
					{
						this.itemRollOut()
						return
					} else if( !this._parent.tree.getIsBranch( item ) and (opt_1 == DENYDRAGITEM or opt == DENYALL or opt_1 == (DENYDRAGITEM | DENYDROPITEM) or opt_1 == (DENYDRAGITEM | DENYDRAGFOLDER)))
					{
						this.itemRollOut()
						return
					}
				}
				this.selectedItem  = item
				this.selectedIndex = evt.index
			} else {
				this.itemRollOut();
			}
		}
		
		// tree itemRollOut
		tfList.itemRollOut = function(evt:Object)
		{
			this.selectedItem  = undefined
			this.selectedIndex = undefined
		}
		
		tfList.change = function( evt:Object)
		{
			if( (getTimer() - this._time) < 300)
			{
				if( evt.target.selectedItem == this._oldItem)
				{
					this._parent.dispatchEvent({type:"double_click", target: this._parent.tree});
				}
			}
			this._oldItem = evt.target.selectedItem
			this._time = getTimer()
		}
		
		// add tree listeners
		tree.addEventListener("change",       tfList)
		tree.addEventListener("itemRollOver", tfList)
		tree.addEventListener("itemRollOut",  tfList)

		// controller functions
		controller_mc.tree  = tree
		controller_mc.item  = undefined
		controller_mc.index = undefined
		controller_mc.__canDrop   = false
		controller_mc.__canDropTarget = false
		controller_mc.__dropTargetMc = undefined
		controller_mc.__dropIndex = undefined
		controller_mc.__targetNode  = undefined
		controller_mc.__dragStart   = false
		controller_mc.onMouseDown = function(){
			this.__dragStart = false
			this.item  = this._parent.tfList.selectedItem
			this.index = this._parent.tfList.selectedIndex
			this.added = false
			this.__targetNode = undefined
			this.points = new Array( this._xmouse, this._ymouse);
			if( this.item == undefined ){
				return;
			}
			this.onEnterFrame = function(){
				/**
				 * canDrop:
				 * -1 => deny always
				 *  0  => deny if dropIndex == undefined
				 *  1  => allow
				 */
				this.clear();
				var canDrop:Number      = -1
				var dropIndex:Number    = undefined
				var opt:Number          = this._parent.dragRules
				var opt_1:Number        = opt
				this.__canDropTarget    = false
				this.__dropTargetMc     = undefined				
				if(opt >= TreeDnd.DENYDROPUPSIDEDOWN){
					opt_1 = opt ^ TreeDnd.DENYDROPUPSIDEDOWN
				}
				var targetLabel:Boolean = false
				var default_icon:String
				var point:Object = new Object()
				point.x = this._parent._xmouse
				point.y = this._parent._ymouse
				this._parent.localToGlobal( point )
				if( !this.added ){
					var x = this._xmouse
					var y = this._ymouse
					if(Math.abs( x  - this.points[0] ) > 2 or Math.abs( y - this.points[1] ) > 2 ){
						if( !this.added and this.item != undefined ){
							this.__dragStart = true
							this.added = true
							this._parent.createIcon( )
							this._parent.dispatchEvent({type:"drag_start", target:this.tree, sourceNode: this.item});
						}
					}
				} else {
					for(var a = 0; a < this.tree.rows.length; a++)
					{
						if( this.tree.rows[a].item != undefined)
						{
							//if( this.tree.rows[a].hitTest( this._parent._xmouse, this._parent._ymouse, true) )
							if( this.tree.rows[a].hitTest( point.x, point.y, true) )
							{
								var item = this.tree.rows[a]
								this.__targetNode = item
								if( item.item == this.item ){
									// if the same item, then DENY
									canDrop = 0;
									// If trying to DROP an item inside itself, DENY
								} else if ( this._parent.isSubNode(this.item , item.item ) ){
									canDrop = -1;
								} else {
									if( this._parent.__dropFunction != undefined ){
										canDrop = this._parent.__dropFunction( this.item, this.__targetNode.node )
									} else {
										canDrop = 1;
										// now core functions..
										// check if item can be dropped and where it will be dropped!
										// deny drop into item 3,4,5,6,7
										if( (opt_1 >= (DENYDRAGITEM | DENYDRAGFOLDER) and opt <= DENYALL) and !this.tree.getIsBranch( item.node )){
											if( opt == DENYALL ){
												canDrop = -1
											} else {
												canDrop = 0;
											}
										}
									}
								}
								if( item._ymouse > ((item.bG_mc._height/2) + item.bG_mc._height/4) and opt < DENYDROPUPSIDEDOWN && (this._parent.tree._ymouse + item._height < this._parent.tree._height))
								{
									this.beginFill(this._parent.getStyle("separatorColor") ? this._parent.getStyle("separatorColor") : 0x666666,100)
									this.drawRect( 0, item._y + item.bG_mc._height, this._parent.tree.width - (this._parent.tree.vSB.width ? this._parent.tree.vSB.width : 0) - 1, item._y + item.bG_mc._height + 1 )
									this.endFill();
									// try to retrieve the item index
									if( this.tree.getIsBranch( item.node ) )
									{
										if( this.tree.getIsOpen( item.node ))
										{
											// it's the first element of the branch
											dropIndex = item.rowIndex
										}
									}
									dropIndex = item.rowIndex
								}
								break;
							}
						}
					}
					// Now apply permissions to dragging icon
					if( canDrop == 1 )
					{
						targetLabel = true
						this.__canDrop = true
					} else if(canDrop == 0)
					{
						if(dropIndex == undefined)
						{
							targetLabel = false
							this.__canDrop = false
						} else {
							targetLabel = true
							this.__canDrop = true
						}
					} else {
						targetLabel = false
						this.__canDrop = false
					}
					this.__dropIndex = dropIndex
					default_icon = this._parent._iconFunction( targetLabel )
					
					// dropTarget
					if(this._parent.dropTarget != undefined){
						for(var a in this._parent.dropTarget){
							if(this._parent.dropTarget[a].hitTest(point.x, point.y, true))
							{
								targetLabel = true
								this.__canDropTarget = true
								this.__dropTargetMc = this._parent.dropTarget[a]
								break;
							}
						}
					}
					
					if( default_icon == undefined )
					{
						default_icon = targetLabel ? "icon_allow_drag" : "icon_deny_drag"
					}					
					if( this._parent.icon[default_icon]._name != default_icon or this._parent.icon[default_icon] == undefined)
					{
						this._parent.icon.attachMovie( default_icon, default_icon , 1 )
					}
				}
				
				var _mouseP = new Object();
				_mouseP.x = this._parent._xmouse
				_mouseP.y = this._parent._ymouse
				//this._parent.globalToLocal( _mouseP )
				this._parent.icon._x = _mouseP.x + 5
				this._parent.icon._y = _mouseP.y + 15
			}
			this.onEnterFrame();
		}
		
		/**
		* mouse up, drag and drop end
		*/
		controller_mc.onMouseUp = function(){
			delete this.onEnterFrame
			if( this.__dragStart != true ){
				return;
			}
			if( this.__canDrop == true )
			{
				var node = this.tree.getItemAt(this.index)
				var cloned = node.cloneNode(true)
				if( this.__dropIndex != undefined )
				{
					if( this.tree.getIsBranch( this.__targetNode.item ) and this.tree.getIsOpen( this.__targetNode.item ))
					{
						node.removeNode()
						this.__targetNode.item.addTreeNodeAt(0, cloned )
					} else {
						if(this.__targetNode.item.nextSibling == null)
						{
							node.removeNode()
							this.__targetNode.item.parentNode.addTreeNode( cloned )
						} else {
							if( node != this.__targetNode.item and node != this.__targetNode.item.nextSibling )	// fix by TAKATAMA, Hirokazu
							{
								node.removeNode()
								this.__targetNode.item.parentNode.insertBefore( cloned, this.__targetNode.item.nextSibling )
								this.tree.refresh()
							}
						}
					}
				} else {
					node.removeNode()
					this.__targetNode.item.addTreeNode( cloned )
				}
				this._parent.dispatchEvent({type:"drag_complete", target: this.tree, sourceNode: node, targetNode: this.__targetNode.item})
				this.tree.dataProvider = this.tree.getDataProvider()
			} else {
				this._parent.dispatchEvent({type:"drag_fail", target: this.tree, sourceNode: node, targetNode: this.__targetNode.item})
			}
			if(this.__canDropTarget == true){
				var node = this.tree.getItemAt(this.index)
				this._parent.dispatchEvent({type:"drag_target", target: this.tree, sourceNode: node, targetMc: this.__dropTargetMc})
			}			
			this.clear();
			this.__canDrop   = false
			this.__dropIndex = undefined
			this._parent.removeIcon()
			this.points = new Array();
			this.item   = undefined
			this.index  = undefined
			this.added  = false
		}
	}
	

	/**
	 * Internal, remove the dragging mouse icon
	 * @usage	
	 * @return	Void	
	 */
	private function removeIcon():Void
	{
		icon.removeMovieClip();
	}
	

	/**
	 * internal, create the dragging icon attaching from library
	 * @usage	
	 * @return	MovieClip	
	 */
	private function createIcon():MovieClip
	{
		return createEmptyObject( "icon", ICONDEPTH );
	}
	

	/**
	 * Verify that targetNode is a subnode of dragNode
	 * @usage	TreeDnd.isSubNode( sourceNode, targetNode )
	 * @param	dragNode	(XMLNode)
	 * @param	targetNode	(XMLNode)
	 * @return	Boolean	
	 */
	private function isSubNode( dragNode:XMLNode, targetNode:XMLNode):Boolean
	{
		var ret:Boolean = false;
		while( targetNode.parentNode != undefined)
		{
			if(targetNode == dragNode)
			{
				ret = true;
				break;
			}
			targetNode = targetNode.parentNode
		}
		return ret;
	}
	
	
	// ******************************************
	// Getter / Setter
	// ******************************************
	
	/**
	 * Set the Tree Drag and Drop rules
	 * @usage	TreeDnd.dragRules = TreeDnd.DENYDRAGITEM | TreeDnd.DENYDROPITEM
	 * @param	value	(Number)
	 * @return	Void
	 */	
	[Inspectable(defaultValue=0,type=Number)]
	public function set dragRules(value:Number)
	{
		if(value >= 0 and value <= DENYALL and value != undefined)
		{
			__options = value
		} else {
			throw new Error("IndexError: value must be an integer between " + DEFAULT + " and " + DENYALL);
		}
		/**
		0 DEFAULT -> Allow everything
		1 DENYDRAGFOLDER -> DENY drag folders
		2 DENYDRAGITEM   -> DENY drag items
		4 DENYDROPITEM   -> DENY drop into items
		5 DENYDRAGFOLDER | DENYDROPITEM -> Deny Drag folder & deny drop on items
		6 DENYDRAGITEM | DENYDROPITEM   -> Deny drag item & deny drop item
		7 DENYALL -> Deny All
		3 DENYDRAGITEM | DENYDRAGFOLDER -> Deny All
		8 DENYDROPUPSIDEDOWN -> deny lines
		*/
	}
	
	/**
	 * return the current drag rules
	 * @usage	
	 * @return	Number	
	 */
	public function get dragRules():Number
	{
		return __options
	}
	
	// set icon function for display allow/deny dragging icon
	public function set iconFunction(func:Function)
	{
		_iconFunction = func
	}
	
	public function get iconFunction():Function
	{
		return _iconFunction
	}
	
	// ******************************************
	// Tree public functions
	// ******************************************
	
	/**
	 * Return a pointer to the current used Tree component
	 * @usage	
	 * @return	mx.controls.Tree the tree component used
	 */
	public function getTree():mx.controls.Tree
	{
		return this.tree
	}
	
	public function set dropTarget(mc:Array):Void
	{
		this.__dropTarget = mc
	}
	
	public function get dropTarget():Array
	{
		return this.__dropTarget
	}

	[Inspectable(defaultValue=20,type=Number)]
	public function set rowHeight(w:Number)
	{
		__rowHeight = w
		this.tree.rowHeight = w
	}
	
	public function get rowHeight():Number
	{
		return __rowHeight
	}
	
	/**
	* set a user defined drop function,
	* this function must return a boolean
	* @usage <code>
	* myDndTree.dropFunction = function(sourceNode:XMLNode, targetNode:XMLNode){
		// in this way you can define a custom dragRule
	* 	return sourceNode.attributes.name != targetNode.attributes.value
	* }
	* </code>
	*/
	public function set dropFunction(fn:Function){
		__dropFunction = fn
	}
	
	/**
	* user defined drag function, which decide if item
	* can be dragged
	* @usage <code>
	* myDndTree.dragFunction = function(item:XMLNode){
	* 	return item.attributes.myattribue == 'some value'
	* }
	* </code>
	*/
	public function set dragFunction(fn:Function){
		__dragFunction = fn
	}
	
}