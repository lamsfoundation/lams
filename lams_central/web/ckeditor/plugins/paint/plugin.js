CKEDITOR.plugins.add('paint',
	       {
	          requires : ['iframedialog'],
			  lang : [ 'en' ],
	          init : function(editor) {
	             var pluginPath = CKEDITOR.plugins.getPath('paint');
	             
	             editor.ui.addButton(
	                'Paint_Button',
	                {
	                   label : 'Paint canvas',
	                   command : 'Paint canvas',
	                   icon : pluginPath + 'icon.png',
	                   title : editor.lang.Paint.Button
	                }
	             );
	             
	             editor.addCommand('Paint canvas', {exec:showDialogPlugin});
	             editor.on('dblclick',onImageDoubleClick);
	             
	             CKEDITOR.dialog.addIframe(
	            	editor.lang.Paint.DialogName,
	            	editor.lang.Paint.DialogTitle,
	                pluginPath + 'content.jsp',
	                852,
	                610,
	                function(){
	                }
	             );
	          }
	       }
	    );

function showDialogPlugin(e){
	e.openDialog(editor.lang.Paint.DialogName);
}

function onImageDoubleClick (ev){
	if(ev.scopeObj.getAttribute('class') == "ckeditor_paint"){
		ev.scopeObj.paintStartingImage = ev.scopeObj.src;
		ev.scopeObj.execCommand('Paint canvas');
	}
}