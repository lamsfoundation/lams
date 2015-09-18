/* 
 *   CKEditior plugin.js for JLaTeXMath.
 *  ------------
 */CKEDITOR.plugins.add('jlatexmath', {	requires : ['iframedialog'],	lang : [ 'en' ],	init : function(editor) {	             var pluginPath = CKEDITOR.plugins.getPath('jlatexmath');	             	             editor.ui.addButton(	                'Jlatexmath',	                {
	                	label   : editor.lang.jlatexmath.JlatexmathBtn,		                command : 'Jlatexmath',		                icon    : pluginPath + 'icons/mathjax.png',		                title   : editor.lang.jlatexmath.JlatexmathTooltip	                }	             );	             editor.addCommand(
	            	'Jlatexmath', 
	                 {
	                 	exec : function(editor){
	                 		editor.openDialog('Jlatexmath');
	                  	}
	                 }
	             );
	             	             CKEDITOR.dialog.addIframe(	            	'Jlatexmath',	            	editor.lang.jlatexmath.DlgJlatexmathTitle,	                pluginPath + 'jlatexmath.jsp',	                450,	                260	             );	}});