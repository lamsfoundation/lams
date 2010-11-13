CKEDITOR.plugins.add('equation_html',
{
	// Latex Equation Editor
	init: function(editor)
	{
		var language='';   // specify your language if not English (en_en)

		var popupEqnwin = null;

		// Register the related command.
		editor.addCommand( 'Equation',
		{
			exec : function( editor )
			{
				
				// type 'url' is escaped and cannot be loaded into the editor again
				var url='http://latex.codecogs.com/editor_json.php?type=latex';

				if(language!='') {
					url += '&lang=' + language;
				}
				     
				//open a popup window when the button is clicked
				if (popupEqnwin==null || popupEqnwin.closed || !popupEqnwin.location) {
					popupEqnwin=window.open('','LaTexEditor','width=700,height=450,status=1,scrollbars=yes,resizable=1');
					if (!popupEqnwin.opener) {
						popupEqnwin.opener = self;
					}
 
					var selected = FCKSelection.GetSelectedElement();
					// is it Equation image?
					if (selected && selected.is('img') && selected.getAttribute('class') == 'ckeditor_equation') {
						var src = selected.getAttribute('src');
						var questionMark = src.indexOf('?');
						if (questionMark != -1) {
							// we need to escape all '\', otherwise it doesn't work
							var sub = src.substring(questionMark + 1).replace(/\\/g,'\\\\');
							var formula =  escape(sub);
							url += '&latex=' + formula;
						}	
					}
					popupEqnwin.document.write('<html><script src="'+url+'" type="text/javascript"></script><body></body></html>');
				}
				else if (window.focus) {
					popupEqnwin.focus();
				}
			},
			state: CKEDITOR.TRISTATE_OFF
		});

		// Create the toolbar button.
		editor.ui.addButton('Equation',
			{
				icon: CKEDITOR.plugins.getPath('equation_html') + 'equation.gif',
				command: 'Equation'
			});


		// The object used for all Placeholder operations.
		FCKEquation = new Object() ;

		// Add a new placeholder at the actual selection.
		FCKEquation.Add = function( name )
		{
			// get the output in 'latex' type and parse it to create image
			var parts = name.match( /(\\\[|\$)(.*?)(\\\]|\$)/ );
			var formula = parts[2];
			var src = 'http://www.codecogs.com/gif.latex?' + formula;
			
			// var src = name;
			var oImage = new CKEDITOR.dom.element( 'img', CKEDITOR.document );
			oImage.setAttributes({
				'src' : src,
				'align' : 'absmiddle',
				'alt' : src,
				'contentEditable' : false,
				'class' : 'ckeditor_equation'});
			editor.insertElement( oImage );
			// Then select the new placeholder
			editor.getSelection().selectElement( oImage );
			if (window.focus) {
				popupEqnwin.focus();
			}
		}


		// http://latex.codecogs.com/editor_json.php?type=url currently expects FCKeditor methods.
		// Add FCKeditor stand-in methods
		FCKSelection = new Object() ;
		FCKSelection.GetSelectedElement = function() {
			return editor.getSelection().getSelectedElement();
		};
		
		editor.on( 'doubleclick', function( evt ) {
			var element = evt.data.element;
			if ( element.is('img') && element.getAttribute('class') == 'ckeditor_equation'){
				// otherwise we get the image dialog
				evt.stop();
				evt.editor.execCommand('Equation');
			}
		}, null, null, 1);
		
	}
});