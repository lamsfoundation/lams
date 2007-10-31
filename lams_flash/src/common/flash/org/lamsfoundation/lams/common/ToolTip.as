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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

import mx.managers.*
import mx.controls.*

class org.lamsfoundation.lams.common.ToolTip extends MovieClip {
	// The button to display the ToolTip on
	var btn:Button;
	var ttHolder:MovieClip;
	// The movieclips & textfield which form the tooltip
	var ToolTipHolder:MovieClip;
	var ToolTipBackground:MovieClip;
	var ToolTipText:TextField;
	
	var ToolTipBackgroundShadow:MovieClip;
	var ToolTipTextShadow:TextField;
	
	// The tooltip text.
	var message:String;
	
	// X and Y Offset
	var xoffset:Number = 0;
	var yoffset:Number = 5;	
	var showAbove:Boolean = false;
	var ttWidth:Number = 155;
	
	// Visible features
	var textFormat:Object;
	var tiptext:TextFormat;
	var tiptextshadow:TextFormat;
	
	var backgroundcolour:Number = 0xF5EFC0;
	var bordercolour:Number = 0x666666;

	// Behavioural features.
	var shadow:Boolean = false;
	var interval;
	
	var delay:Number = 1;
	
	// Class Constructor
	// ----------------------------------------------------------------------------------------
	// All of the tooltips properties have default values except the actual message to  display 
	// in the tip. Because of this, only the message is required to be sent to the constructor.
	
	public function ToolTip(){
		textFormat = formatText();
	}
	
	private function createTT()	{
		ttHolder.createEmptyMovieClip("ToolTipHolder", ttHolder.getNextHighestDepth());
		ttHolder.delay = delay;
	}
	
	private function showTT(){
		// Create our text field
		ttHolder.ToolTipHolder.createTextField("ToolTipText", 4, 2, 0, 1, 1);
		with(ttHolder.ToolTipHolder.ToolTipText)	{
			html = true;
			htmlText = this.message;
			setTextFormat(this.tiptext);
			selectable = false;
			_width = textWidth + 5;
			_height = textHeight + 5;
			
			// see if we should wrap the text
			if(_width > this.ttWidth){
				multiline = true;
				wordWrap = true;
				_width = this.ttWidth;
				_height = textHeight + 5;
			}
			_visible = false
		}
		
		var bgwidth = ttHolder.ToolTipHolder.ToolTipText.textWidth + 8;
		var bgheight = ttHolder.ToolTipHolder.ToolTipText.textHeight + 4;
		// Create the background on the tool tip
		ttHolder.ToolTipHolder.createEmptyMovieClip("ToolTipBackground", 2);
		with (ttHolder.ToolTipHolder.ToolTipBackground){
		  beginFill (this.backgroundcolour, 100);
		  lineStyle (1, this.bordercolour, 100);
		  moveTo(0, 0);
		  lineTo(bgwidth, 0);
		  lineTo(bgwidth, bgheight);
		  lineTo(0, bgheight);
		  lineTo(0, 0);
		  endFill();
		}
		
		// Create our shadow (if specified)
		if(shadow == true)	{
			with(ttHolder.ToolTipHolder)	{
				// Background shadow
				ToolTipBackground.duplicateMovieClip("ToolTipBackgroundShadow", 1);
				with(ToolTipBackgroundShadow){
					_x = ToolTipBackground._x + 2;
					_y = ToolTipBackground._y + 2;
					_alpha = 30;
				}
				
				// Text shadow
				createTextField("ToolTipTextShadow", 3, ToolTipText._x + 1, ToolTipText._y+1, ToolTipText._width, ToolTipText._height);
				with(ToolTipTextShadow){
					html = true;
					htmlText = ToolTipText.text;
					setTextFormat(this.tiptextshadow);
					selectable = ToolTipText.selectable;
					_width = ToolTipText._width;
					_height = ToolTipText._height;
					multiline = ToolTipText.multiline;
					wordWrap = ToolTipText.wordWrap;
					_alpha = 30;
					
				}
			}
		}
		// position tooltip based on param
		if (!showAbove){
			ttHolder.ToolTipHolder._x = xoffset;
			ttHolder.ToolTipHolder._y = yoffset;
		}else {
			ttHolder.ToolTipHolder._x = xoffset;
			ttHolder.ToolTipHolder._y = yoffset- (ttHolder.ToolTipHolder.ToolTipBackground._height+5);
		}
		
		// Conceal our tooltip for now
		ttHolder.ToolTipHolder._visible = false;
		
	}
	

	
	private function formatText():Object{
		var FObj:Object = new Object();
		var TipText:TextFormat = new TextFormat();
		TipText.color = 0x333333;
		TipText.font = "Verdana";
		TipText.size = 9;

		var TipTextShadow:TextFormat = new TextFormat();
		TipTextShadow.color = 0xECE9D8;
		TipTextShadow.font = "Verdana";
		TipTextShadow.size = 9;
		
		FObj.tiptext = TipText
		FObj.tiptextshadow = TipTextShadow
		
		return FObj
	}
	
	
	public function DisplayToolTip(p_ttHolder:MovieClip, p_message:String, p_xoffset:Number, p_yoffset:Number, p_above:Boolean, p_width:Number, p_btn:Button, p_tiptext:TextFormat, p_tiptextshadow:TextFormat, p_backgroundcolour:Number, p_bordercolour:Number, p_delay:Number, p_shadow:Boolean){
		// wait
		//Our tooltip holder (required parameter).
		ttHolder = p_ttHolder
		// Our tooltip text (required parameter).
		message  = p_message;
		
		// Our TextFormats
		if(p_tiptext != undefined && p_tiptextshadow != undefined){
			tiptext = p_tiptext;	
			tiptextshadow = p_tiptextshadow;
		}else {
			tiptext = textFormat.tiptext;	
			tiptextshadow = textFormat.tiptextshadow;
		}

		// The x & y offset of the tooltip (optional).
		if (p_xoffset != undefined && p_yoffset != undefined){
			xoffset = p_xoffset;
			yoffset = p_yoffset;
		}else {
			xoffset = _root._xmouse + xoffset;
			yoffset = _root._ymouse + yoffset;
		}
		
		if(p_above != undefined) showAbove = p_above;
		
		if(p_width != undefined) ttWidth = p_width;
		
		// The visible features (optional).
		if(p_backgroundcolour != undefined) backgroundcolour = p_backgroundcolour;
		if(p_bordercolour != undefined) bordercolour = p_bordercolour;

		// The behavioural features (optional).
		if(p_delay != undefined) delay = p_delay;
		if(p_shadow != undefined) shadow = p_shadow;
		
		// Create out actual ToolTip which consists of a movieclip holding one (or two) textfields and one(or two) background clips
		createTT();
		showTT();
		
		ttHolder.ToolTipHolder._visible = true;
		ttHolder.ToolTipHolder._alpha = 0;
		
		//ttHolder.ToolTipHolder._x = _root._xmouse + xoffset
		//ttHolder.ToolTipHolder._y = _root._ymouse + yoffset
		
		var delayed = 0;
		var delayfor = this.delay * 10;
		
		ttHolder.ToolTipHolder.onEnterFrame = function(){
			if(delayed < delayfor){
				delayed++;
			}
			else{
				if(_alpha < 100){
					_alpha += 20;
				}else{
					ToolTipText._visible = true
					delete this.onEnterFrame;
				}
			}
		}
	}
	
	public function CloseToolTip(){
		delete ttHolder.ToolTipHolder.onEnterFrame;
		//ttHolder.ToolTipHolder._visible = false;	
		ttHolder.ToolTipHolder.removeMovieClip();
	}
}