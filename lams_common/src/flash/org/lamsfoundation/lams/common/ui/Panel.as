import org.lamsfoundation.lams.common.util.Debugger
import org.lamsfoundation.lams.common.ui.*
import mx.core.UIComponent
import mx.styles.StyleManager
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
	private var border_mc : MovieClip;	//private var background_mc : MovieClip;	private var background_ct : Color;
	private var _borderType : String;	private var _backgroundColour : Number; 
	

	/**
	* Constructor (Empty)
	*
	*/
	function Panel ()
	{
		//trace('Panel constructor');
	}
	/**
	* Intialisation, Flash calls the init() method
	* when the class is created. This method is
	* called once when a component is instantiated
	* and never again.	* NOTE: CALLED BY UI OBJECT so don't call explicitly here
	*
	*/
	function init () : Void 
	{		
		//trace ('panel innit, _initialised:'+_initialised);
		
		super.init ();
		useHandCursor = false;		
		//some default values;
		_borderType  = "inset";
		_backgroundColour = 0xCCCCCC;
		//hide the bounding box place holder
		boundingBox_mc._visible = false;
		boundingBox_mc._width = 0;
		boundingBox_mc._height = 0;
			
	}	/**
	* The draw method is invoked after the component has
	* been invalidated, by someone (USUALLY UI OBJECT) calling invalidate().
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
	* The borderStyle (uses rectBorder class) inny our outy etc..
	* @param type The kind of border we use, possible values: 'inset', 'outset', 'solid'
	*/
	[Inspectable(enumeration="inset,outset,solid,default,dropDown,none"defaultValue="inset")]
	function set borderType (bStyle:String):Void{		//trace('set bStyle:'+bStyle);
		_borderType = bStyle;		invalidate();
	}
	
	function get borderType():String{
		return _borderType;		
	}	/**
	* General set style method
	* @param styleProp The property to affect, can be backgroundColor or borderStyle
	* @param newValue the value to assign to that styleProp. Colours can be in name or hex format, borders as in set borderType
	* 
	*/	public function setStyle(styleProp:String, newValue):Void{		Debugger.log('styleProp:'+styleProp+' newValue:'+newValue,5,'setStyle','Panel');
		//only process if we want to set the bkg
		if(styleProp == "backgroundColor"){
			//trace('Setting panel bkg to:'+newValue);
			/*
			if(isNaN(newValue)){
				//colour name
				_backgroundColour = StyleManager.getColorName(newValue);
			}else{
				_backgroundColour = newValue;
			}
			*/
			_backgroundColour = newValue;
			
		}else if(styleProp == "borderStyle"){
			//must be going for the border			_borderType = newValue;		
		}else{
			trace('Panel got an unsupported set style type.... can only be backgroundColor or borderStyle');
		}
		invalidate();
	}
	
	
	/**
	* Attaches the PanelAssets mc which contains all the actual
	* mc's that make up the component
	* NOTE: CALLED BY UI OBJECT so don't call explicitly here
	* 
	*/
	private function createChildren () : Void
	{
		//trace('create children in panel, panel:'+panel);
		panel = this.createObject ("PanelAssets", "panel", getNextHighestDepth());
		//trace('panel:'+panel);		background_ct = new Color(panel.background_mc);		//the border is a MM rect border
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
		panel.background_mc._height = __height;		background_ct.setRGB(_backgroundColour);
		
		border_mc.setSize(__width, __height);
		border_mc._x = panel.background_mc._x;
		border_mc._y = panel.background_mc._y;
		if(_borderType == "none"){
			border_mc.visible=false;
		}else{
			if(!border_mc.visible){border_mc.visible=true;};			border_mc.setStyle("borderStyle",_borderType);
		}
		
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
