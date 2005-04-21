import org.lamsfoundation.lams.common.ui. *
import mx.core.UIComponent
/**
* Panel is a base UI building block used in LAMS.  It is a holder for other UI
* components
* It is re-sizable, and supports Macromedia's skinning
*
* @see <a href="http://livedocs.macromedia.com/flash/mx2004/main_7_2/wwhelp/wwhimpl/common/html/wwhelp.htm?context=Flash_MX_2004&file=00003104.html">More info on core functions required when developing a component</a>
*
*/
 [Event ("change")]
class Panel extends UIComponent 
{
	// Components must declare these to be proper
	// components in the components framework.
	static var symbolName : String = "Panel";
	static var symbolOwner : Object = Panel;
	var className : String = "Panel";
	//Variables
	private var panel : MovieClip;
	private var boundingBox_mc : MovieClip;
	private var border_mc : MovieClip;
	var __borderType : String = "inset";


	/**
	* Constructor (Empty)
	*
	*/
	function Panel ()
	{
	}
	/**
	* Intialisation, Flash calls the init() method
	* when the class is created. This method is
	* called once when a component is instantiated
	* and never again.
	*
	*/
	function init () : Void 
	{
		//trace ('panel innit');
		super.init ();
		useHandCursor = false;
		//hide the bounding box place holder
		boundingBox_mc._visible = false;
		boundingBox_mc._width = 0;
		boundingBox_mc._height = 0;
		//attach the actual component
		createChildren ();
	}	/**
	* The draw method is invoked after the component has
	* been invalidated, by someone calling invalidate().
	* This is better than redrawing from within the set function
	* for value, because if there are other properties, it's
	* better to batch up the changes into one redraw, rather
	* than doing them all individually. This approach leads
	* to more efficiency and better centralization of code.
	* 
	* Here we just call layout children and the super
	* 
	*/
	
	public function draw () : Void 
	{
		//trace ('panel.draw()');
		super.draw ();
		layoutChildren ();
	}
	/**
	* Overrides the function in UIObject.
	* Calls invalidate, which will call draw() one frame 
	* later and therefore will call layoutChildren 
	* - to hanlde the size...
	*/
	public function size (Void) : Void
	{
		//trace ('Panel.size()');
		invalidate ();
	}
	
	/**
	* The type indicated if inny our outy etc..
	* @param type The kind of border we use, possible values: 'inset', 'outset', 'solid'
	*/
	[Inspectable(enumeration="inset,outset,solid,default"defaultValue="inset")]
	function set borderType (bStyle:String):Void{		//trace('set bStyle:'+bStyle);
		__borderType = bStyle;		invalidate();
	}
	
	function get borderType():String{
		return __borderType;		
	}
	
	
	/**
	* Attaches the PanelAssets mc which contains all the actual
	* mc's that make up the component
	* 
	*/
	private function createChildren () : Void
	{
		_global.breakpoint ();
		//trace ('create children in panel');
		panel = this.createObject ("PanelAssets", "panel", 10);
		border_mc = createClassObject(mx.skins.RectBorder,"border_mc", getNextHighestDepth()); //make sure this is the last
		layoutChildren();
	}
	/**
	* Lays out all the attached mcs.  Mainly handles re-sizes
	* 
	*/
	private function layoutChildren () : Void
	{
		panel._yscale = 100;
		panel._xscale = 100;
		//__width and __height is set in UIObject on resizes n stuff
		panel.background_mc._width = __width;
		panel.background_mc._height = __height;
		
		border_mc.setSize(__width, __height);		border_mc.setStyle("borderStyle",__borderType);
		
		
		/*
		panel.borderTop_mc._width = __width;
		panel.borderBottom_mc._width = __width;
		panel.borderBottom_mc._y = __height;
		panel.borderRight_mc._height = __height;
		panel.borderRight_mc._x = __width;
		panel.borderLeft_mc._height = __height;
		//trace ('width:' + width);
		//trace ('__width :' + __width);
		//trace ('height:' + height);
		//trace ('__height:' + __height);
		*/
	}	
	
	/*
	* These are not used, but I have left them her so we know how 
	* to do inspectable properties, as that bit worked!
	* 
	* 
	public function getWidth(Void):Number{
	return __width;
	}
	public function getHeight(Void):Number{
	return __height;
	}
	
	
	[Inspectable(defaultValue=550)]
	function set width(w:Number):Void{
	trace('set width w:'+w);
	setWidth(w);
	}
	
	public function setWidth(w:Number):Void{
	__width = w;
	invalidate();
	}
	
	
	[Inspectable(defaultValue=40)]
	function set height(h:Number):Void{
	trace('set height:'+h);
	setHeight(h);
	}
	
	public function setHeight(h:Number):Void{
	__height = h;
	size();
	
	}
	*/
}
