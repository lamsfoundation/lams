/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

//import org.lamsfoundation.lams.common.util.Debugger
import mx.core.UIComponent
import mx.styles.StyleManager
/**
* Panel is a base UI building block used in LAMS.  
* It is often a holder or background for other UI components
* It is re-sizable, and supports Macromedia's skinning see setStyle for possible values:
*
* @see <a href="http://livedocs.macromedia.com/flash/mx2004/main_7_2/wwhelp/wwhimpl/common/html/wwhelp.htm?context=Flash_MX_2004&file=00003104.html">More info on core functions required when developing a component</a>
*
*/
 [Event ("change")]
class org.lamsfoundation.lams.common.ui.Panel extends UIComponent 
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
	private var _borderColor : Number; 
	

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
	* 
	* 
	* 
	* 
	*/
	[Inspectable(enumeration="inset,outset,solid,default,dropDown,none"defaultValue="inset")]
	function set borderType (bStyle:String):Void{		//trace('set bStyle:'+bStyle);
		_borderType = bStyle;		invalidate();
	}
	
	function get borderType():String{
		return _borderType;		
	}	/**
	* General set style method
	* Can set:
	* backgroundColor - Color in Hex format.
	* borderStyle - the type of border of the rectBorder class used in the panel (see set borderType for details)
	* borderColor - the colour of the border (when solid only, otherwise it looks crap if applied to 3d styles as need to set other set of styles see rectBorder class for details) in Hex format
	* styleName - this is to support the use of CSSStyleDeclaration class, you can pass a StyleObject into the panel and it wil extract the style information and apply it
	*
	* @param styleProp The property to affect
	* @param newValue the value to assign to that styleProp. Colours in hex format, borders as in set borderType
	* 
	*/	public function setStyle(styleProp:String, newValue):Void{		//trace('[Panel setStyle]styleProp:'+styleProp+' newValue:'+newValue);
		//only process if we want to set the bkg
		if(styleProp == "backgroundColor"){
			_backgroundColour = newValue;
		}else if(styleProp == "borderStyle"){
			//must be going for the border			_borderType = newValue;
		}else if(styleProp == "borderColour"){
			//must be going for the border colour
			_borderColor = newValue;
		}else if(styleProp == "styleName"){
			
			_backgroundColour = newValue.getStyle('backgroundColor');
			_borderType = newValue.getStyle('borderStyle');
			_borderColor = newValue.getStyle('borderColor');
			//trace('[Panel setStyle]styleName: _backgroundColour:'+_backgroundColour);
			//trace('[Panel setStyle]styleName: borderStyle:'+_borderType);
		}else{
			trace('Panel got an unsupported set style type.... can only be backgroundColor or borderStyle or styleName');
		}
		invalidate();
	}
	
	
	/**
	* Attaches the PanelAssets mc which contains all the actual
	* mc's that make up the component
	* NOTE: CALLED BY UI OBJECT so don't call explicitly here
	* 
	*/
	private function createChildren() : Void
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
	private function layoutChildren() : Void
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
			if(!border_mc.visible){
				border_mc.visible=true;
			}
			border_mc.setStyle("borderStyle",_borderType);
		}
		
		if(_borderColor != null){
			border_mc.setStyle('borderColor',_borderColor);
		}

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
