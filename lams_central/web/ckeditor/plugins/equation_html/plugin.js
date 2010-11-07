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

				var url='http://latex.codecogs.com/editor_json.php?type=url';

				if(language!='') url+='&lang='+language;
				     
				//open a popup window when the button is clicked
				if (popupEqnwin==null || popupEqnwin.closed || !popupEqnwin.location)
				{
					popupEqnwin=window.open('','LaTexEditor','width=700,height=450,status=1,scrollbars=yes,resizable=1');
					if (!popupEqnwin.opener) popupEqnwin.opener = self;
					popupEqnwin.document.write('<html><script src="'+url+'" type="text/javascript"></script><body></body></html>');
				}
				else if (window.focus)
				{
					popupEqnwin.focus()
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
			var oImage = new CKEDITOR.dom.element( 'img', CKEDITOR.document );
			oImage.setAttributes({
				'src' : name,
				'align' : 'absmiddle',
				'alt' : name,
				'contentEditable' : false });
			editor.insertElement( oImage );
			// Then select the new placeholder
			editor.getSelection().selectElement( oImage );
		}

		// http://latex.codecogs.com/editor_json.php?type=url currently expects FCKeditor methods.
		// Add FCKeditor stand-in methods
		FCKSelection = new Object() ;
		FCKSelection.GetSelectedElement = function() {
			return editor.getSelection().getSelectedElement();
		};
	}
});