/* 
 *   CKEditior plugin.js for JLaTeXMath.
 *  ------------
 */
CKEDITOR.plugins.add('jlatexmath', {
	requires : ['iframedialog'],
	lang : [ 'en' ],
	init : function(editor) {
	             var pluginPath = CKEDITOR.plugins.getPath('jlatexmath');
	             
	             editor.ui.addButton(
	                'Jlatexmath',
	                {
	                	label   : editor.lang.jlatexmath.JlatexmathBtn,
		                command : 'Jlatexmath',
		                icon    : pluginPath + 'icons/mathjax.png',
		                title   : editor.lang.jlatexmath.JlatexmathTooltip
	                }
	             );

	             editor.addCommand(
	            	'Jlatexmath', 
	                 {
	                 	exec : function(editor){
	                 		editor.openDialog('Jlatexmath');
	                  	}
	                 }
	             );
	             
	             CKEDITOR.dialog.addIframe(
	            	'Jlatexmath',
	            	editor.lang.jlatexmath.DlgJlatexmathTitle,
	                pluginPath + 'jlatexmath.jsp',
	                930,
	                400
	             );
	             
	 			editor.on( 
	 				'doubleclick', 
	 				function(evt) {
	 					var element = evt.data.element;
	 					if (element && element.is('img')) {
	 						
	 						var sName = element.getAttribute('src').match( /jlatexmath\?formula(.*)/ );
	 						if (sName!=null) {
	 							evt.data.dialog = 'Jlatexmath';
	 							
	 							evt.cancelBubble = true; 
	 							evt.returnValue = false; 
	 						}
	 					}
	 				}, 
	 				null, 
	 				null, 
	 				1
	 			);

	}
});