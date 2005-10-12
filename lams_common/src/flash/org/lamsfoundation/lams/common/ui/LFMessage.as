import org.lamsfoundation.lams.common.ui.*
import org.lamsfoundation.lams.common.util.*
import mx.controls.Alert;


/**  
* LFMessage  
* 
* 
*/  
class LFMessage{  
  
     //Declarations  
	 private var _ref:Object;
	 private var _fn:String;
	 
     //Constructor  
  function LFMessage() {
	  //enable accesibility for the Alert 
	mx.accessibility.AlertAccImpl.enableAccessibility();
		
  }
  
  	
  public static function showMessageAlert(msg ,okHandler, icon){
	var alt:Alert;
	//TODO: increase line breaks to account for stoopid bug in MMs alert.  
	// if an icon is being used then the width of the icon is not taken into account
	//msg += "\n  \n  \n  \n  ";
	if(okHandler != undefined){
	   //alt = Alert.show(msg,"__Message__",Alert.OK,null,okHandler,"alertIcon_gen",Alert.OK);
	   alt = Alert.show(msg,"__Message__",Alert.OK,null,okHandler,null,Alert.OK);
	}else{
	   alt = Alert.show(msg,"__Message__",Alert.OK,null,null,null,Alert.OK);
	   //alt = super.show(msg,"__Message__",Alert.OK,null,null,"alertIcon_gen",Alert.OK);
	}
	
	//alt.setSize(800,250);
  }
  
  public static function showMessageConfirm(okHandler:Function, cancelHandler:Function){

  }
  
  public function get reference():Object{
   return _ref;
  }
	
  public function get fname():String{
	return _fn;
  }
  
  /**
  * this function is in mx.controls.alertClasses.AlertForm and i think it has a bug in getting a size with an icon
  * 
* @private
* get size according to contents of form 
	
	function getSize(Void):Object
	{
		trace("DAVES OVERRIDE");
		var s:Object = new Object();
		s.height = buttons[0].height + (3 * 8);
		var tf2:Object = _parent.back_mc.title_mc._getTextFormat();
		extent = tf2.getTextExtent2(_parent.title) ;
 		s.width = Math.max( Math.max(2, buttons.length) * (buttons[0].width + 8),(extent.width) + 4 + 8);
		var tf:Object = text_mc._getTextFormat();
 		extent = tf.getTextExtent2(_parent.text);
 
 		// stick the text in the measuring TextField and let it flow baby
 		textMeasure_mc._width = 2*s.width;
 		textMeasure_mc.setNewTextFormat(text_mc._getTextFormat());
 		textMeasure_mc.text = _parent.text;
 		
 		// now the TextField height should have been adjusted since its' autoFlow
 		s.height += textMeasure_mc.textHeight + 8;
 		var numlines:Number = Math.ceil(textMeasure_mc.textHeight / extent.height);
 
		if (numlines > 1)
		{
			extent.width = 2* s.width;
			text_mc.wordWrap = true;
		}
			

//width is larger of buttons or text but not more than twice as wide as buttons
//  add extra 8 to extent.width for the 8 pixel galley on the right side

		var width:Number = Math.min(extent.width + 4 + 8, 2 * s.width);
		var bWidth = s.width;
		s.width = Math.max(width, s.width) + 8 ; 
		if (icon_mc != undefined)
		{

//calculate the additional width if we add the icon

			extent.width += icon_mc.width + 8;
			width = Math.min(extent.width + 4 + 8, 2 * bWidth);
			s.width = Math.max(width, s.width) + 8 ; 

//increase size if bigger

			var i:Number = icon_mc.height - (numlines * (extent.height + 4));
			if (i > 0)
				s.height += i;
		}
		return s;
	}
	 */
}