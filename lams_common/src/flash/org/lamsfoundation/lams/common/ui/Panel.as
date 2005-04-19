import org.lamsfoundation.lams.common.ui.*
import mx.core.UIComponent

//imports here


[Event("change")]
class Panel extends UIComponent {
	
	// Components must declare these to be proper
	// components in the components framework.
	static var symbolName:String = "Panel";
	static var symbolOwner:Object = Panel;
	var className:String = "Panel";

	
	//Variables
	private var panel:MovieClip;	private var boundingBox_mc:MovieClip;
	
	
	function Panel(){
	}
	
	// Initialization code
	function init():Void {		trace('panel innit');
	      super.init();
	      useHandCursor = false;
	      //hide the bounding box place holder
	      boundingBox_mc._visible = false;
	      boundingBox_mc._width = 0;
		boundingBox_mc._height = 0;		//attach the actual component		createChildren();
	   }
	
	   private function createChildren():Void{		_global.breakpoint();
		trace('create children in panel');
	      panel = this.createObject("PanelAssets", "panel", 10);
	      layoutChildren();
	   }
	   
	   private function layoutChildren():Void{		panel._yscale = 100;
		panel._xscale = 100;
		//__width and __height is set in UIObject on resizes n stuff
		panel.background_mc._width = __width;
		panel.background_mc._height = __height;
		panel.borderTop_mc._width = __width;
		panel.borderBottom_mc._width = __width;
		panel.borderBottom_mc._y = __height;
		panel.borderRight_mc._height = __height;
		panel.borderRight_mc._x = __width;

		panel.borderLeft_mc._height = __height;

		trace('width:'+width);
		trace('__width :' + __width);
		trace('height:'+height);
		trace('__height:' + __height);
		   
	}
	
	   // The draw method is invoked after the component has
	   // been invalidated, by someone calling invalidate().
	   // This is better than redrawing from within the set function
	   // for value, because if there are other properties, it's
	   // better to batch up the changes into one redraw, rather
	   // than doing them all individually. This approach leads
	   // to more efficiency and better centralization of code.
	   function draw():Void {	      trace('panel.draw()');
	      super.draw();		layoutChildren();	   }
	
	//overrides the funciton in UIObject
	public function size(Void):Void{
		trace('Panel.size()');
				//super.setSize(__width, __height);		//super.size();		//trace('size in Panel class passed super.size();');		//var w = __width;
		//trace('w :' + w);
		//var h = __height;
		//trace('h :' + h);
		//_width = w;
		//_height = h;
		//trace('panel.background_mc:'+panel.background_mc);
		
		//boundingBox_mc._width = __width;
		//boundingBox_mc._height = __height;		
		
		//setWidth(__width);
		//setHeight(__height);
		
		invalidate();		
		

		
	}
	
	
	/*
	public function getWidth(Void):Number{
		return __width;
	}
	public function getHeight(Void):Number{
		return __height;
	}	
	
	//[Inspectable(defaultValue=550)]
	function set width(w:Number):Void{
		trace('set width w:'+w);
		setWidth(w);
	}

	public function setWidth(w:Number):Void{
		__width = w;
		invalidate();
	}
	

	//[Inspectable(defaultValue=40)]
	function set height(h:Number):Void{		trace('set height:'+h);
		setHeight(h);
	}
	
	public function setHeight(h:Number):Void{
		__height = h;
		size();
		
	}
	*/
	
}

/**
* 
*/

/*
	Object.registerClass("PanelClassSymbol",  PanelClass);
	PanelClass.prototype = new FUIComponentClass();
	
	PanelClass.prototype.init = function(){
		super.init();
		this.width = w;
		this.height = h;
	}
	
	
	PanelClass.prototype.setWidth = function(w){
		this._xscale = 100;
		this.background._width = w;
		this.borderTop._width = w;
		this.borderBottom._width = w;
		this.borderRight._x = w;
		this.width = w;
	}
	
	PanelClass.prototype.getWidth = function(){
		return this.width;
	}
	
	PanelClass.prototype.setHeight = function(h){
		this._yscale = 100;
		this.background._height = h;
		this.borderLeft._height = h;
		this.borderRight._height = h;
		this.borderBottom._y = h;
		this.height = h;
	}
	
	PanelClass.prototype.getHeight = function(){
		return this.height;
	}
	
	
	PanelClass.prototype.setSize = function(w, h){
		debug("setSize called");
		this.setWidth(w);
		this.setHeight(h);
	}
*/