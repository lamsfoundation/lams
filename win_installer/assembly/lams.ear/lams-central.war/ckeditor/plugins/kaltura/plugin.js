/* 
 *  Plugin.js for Kaltura Contributor Wizard (KCW)
 *  ------------
 */

CKEDITOR.plugins.add('kaltura', {
	lang : [ 'en' ],
	init : function(editor) {
		var popup = null;
	    var command = editor.addCommand('Kaltura', {
	    	exec : function(editor){
		    	//open a popup window when the button is clicked
		    	if (popup==null || popup.closed || !popup.location){
		        	popup = window.open(CKEDITOR.plugins.getPath('kaltura')+'/kaltura.jsp?ckEditorName=' + editor.name,
		            	    			'Kaltura',
		            	    			'width=700,height=440,status=no,scrollbars=no,resizable=no,location=no,toolbar=no');
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
	             
	   var pluginPath = CKEDITOR.plugins.getPath('kaltura');
	             
	   editor.ui.addButton(
			   'Kaltura',{
				   label : "Kaltura",
	               command : 'Kaltura',
	               icon : pluginPath + 'kalturagif.gif',
	               title : editor.lang.kaltura.KalturaBtn
	           }
	   );
	}
});
