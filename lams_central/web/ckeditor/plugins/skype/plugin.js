// Skype plugin for CKEditor

CKEDITOR.plugins.add( 'skype',
{
	lang:['en'],
	init: function( editor )
	{
		editor.addCommand( 'skypeDialog', new CKEDITOR.dialogCommand( 'skypeDialog' ) );
 
		editor.ui.addButton( 'Skype',
		{
			// Toolbar button tooltip.
			label: editor.lang.skype.SkypeBtn,
			// Reference to the plugin command name.
			command: 'skypeDialog',
			// Button's icon file path.
			icon: this.path + 'images/icon.png'
		} );
 
		CKEDITOR.dialog.add( 'skypeDialog', function( editor )
		{
			return {
				title : editor.lang.skype.SkypeTitle,
				minWidth : 400,
				minHeight : 100,
				contents :
				[
					{
						id : 'general',
						label : 'Settings',
						elements :
						[
							{
								type : 'html',
								html : editor.lang.skype.SkypeTxt
							},
							{
								type : 'text',
								id : 'userId',
								label : editor.lang.skype.SkypeContactName,
								validate : CKEDITOR.dialog.validate.notEmpty( editor.lang.skype.SkypeValidationError ),
								required : true,
								commit : function( data )
								{
									data.userId = this.getValue();
								}
							},
							{
								type : 'html',
								html : '<a style="text-decoration:underline !important;"target="_blank" href="http://wiki.lamsfoundation.org/x/AIGI">'+ editor.lang.skype.SkypeHelp+'</a>'
							}
						]
					}
				],
				onOk : function()
				{
					var dialog = this,
						data = {},
						link = editor.document.createElement( 'a' );
					this.commitContent( data );
					var skypeImage = '<img src="http://mystatus.skype.com/smallclassic/'+data.userId+'" style="border: none;"  alt="My status" />';

					link.setAttribute( 'href', 'skype:'+data.userId+'?chat' );

					link.setHtml( skypeImage );

					editor.insertElement( link );
				}
			};
		} );
	}
} );
