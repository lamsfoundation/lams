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
CKEDITOR.plugins.add('videorecorder',
       {
		  lang : [ 'en' ],
          init : function(editor) {
			 var popup = null;
             var command = editor.addCommand('VideoRecorder', {
            	 exec : function(editor){
	            	   		//open a popup window when the button is clicked
	            	 		if (popup==null || popup.closed || !popup.location){
	            	    		popup = window.open(CKEDITOR.plugins.getPath('videorecorder')+'/VideoRecorderCKEditor.jsp?ckEditorName=' + editor.name,
	            	    							'VideoRecorder',
	            	    							'width=380,height=440,status=no,scrollbars=no,resizable=no,location=no,toolbar=no'
	            	    						    );
	            	 		}
	            	 		popup.focus();	
            	 		},
            	 setState : function(newState){
            	 			// do nothing, state is always OFF
            	 		},
            	 toggleState : function(){
            	 			// do nothing, state is always OFF
            	 		},
            	 enable : function(){
            	 			// do nothing, state is always OFF
            	 		},
                disable : function(){
            	 			// do nothing, state is always OFF
            	 		}
             });
             
             command.state = CKEDITOR.TRISTATE_OFF;
             command.previousState = CKEDITOR.TRISTATE_OFF;
             
             var pluginPath = CKEDITOR.plugins.getPath('videorecorder');
             
             editor.ui.addButton(
                'VideoRecorder',
                {
                   label : "VideoRecorder",
                   command : 'VideoRecorder',
                   icon : pluginPath + 'icon.png',
                   title : editor.lang.videorecorder.VideoRecorderBtn
                }
             );
          }
       }
    );