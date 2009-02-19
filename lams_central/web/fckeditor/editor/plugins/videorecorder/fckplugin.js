/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */

// the command to launch
var InsertVideoRecorderCommand=function(){};

InsertVideoRecorderCommand.prototype.Execute=function(){ }

InsertVideoRecorderCommand.GetState=function() {
        return FCK_TRISTATE_OFF;
}

// the popup
var popup = null;
InsertVideoRecorderCommand.Execute=function() 
{
  //open a popup window when the button is clicked
	if (popup==null || popup.closed || !popup.location){
   		popup= window.open( FCKConfig.PluginsPath + 'videorecorder/VideoRecorderFCKEditor.jsp', 'VideoRecorder', 'width=380,height=440,status=no,scrollbars=no,resizable=no,location=no,toolbar=no');
	}

 popup.focus();
}

// register the command with fckeditor
FCKCommands.RegisterCommand( 'VideoRecorder', InsertVideoRecorderCommand ) ;

// place the toolbar button
var oVideoRecorderItem = new FCKToolbarButton( 'VideoRecorder', FCKLang['VideoRecorderBtn'] ) ;
oVideoRecorderItem.IconPath =  FCKConfig.PluginsPath + 'videorecorder/icon.png' ;

FCKToolbarItems.RegisterItem( 'VideoRecorder', oVideoRecorderItem ) ;

// create an object to handle videorecorder
var FCKVideoRecorder= new Object() ;

FCKVideoRecorder.Add = function( innerHTML )
{
	FCK.InsertHtml(innerHTML);
}