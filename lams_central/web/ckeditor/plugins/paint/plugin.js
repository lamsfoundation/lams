CKEDITOR.plugins.add('paint', {
	requires : ['iframedialog'],
	lang : [ 'en' ],
	init : function(editor) {
		var pluginPath = CKEDITOR.plugins.getPath('paint');
	             
		editor.ui.addButton(
				'Paint_Button',
				{
					label   : 'Paint canvas',
					command : 'Paint canvas',
					icon    : pluginPath + 'icon.png',
					title   : editor.lang.paint.Button
				}
		);
  
		editor.addCommand(
				'Paint canvas', 
				{
					exec : function(editor){
						editor.openDialog(editor.lang.paint.DialogName);
					}
				}
		);
	             
		editor.paintStartingImage = "";
	             
		editor.on( 'doubleclick', function( evt ) {
			var element = evt.data.element;
			if ( element.is('img') && element.getAttribute('class') == "ckeditor_paint" ){
				evt.stop();
				evt.editor.paintStartingImage = element.getAttribute('src');
				evt.editor.execCommand('Paint canvas');
			}
		}, null, null, 1);
	             
		CKEDITOR.dialog.addIframe(
				editor.lang.paint.DialogName,
				editor.lang.paint.DialogTitle,
				pluginPath + 'content.jsp',
				852,
				610,
				function(){}
		);
	}
});