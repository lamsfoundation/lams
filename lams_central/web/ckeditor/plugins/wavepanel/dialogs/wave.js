/**
 * LAMS Foundation 2005 Onwards
 * GNU GPL V2 
 */

( function() {
	var wavepanelDialog = function( editor ) {
			// Load image preview.
			var PANEL = 1,
				PREVIEW = 4,
				CLEANUP = 8;

			var updatePreview = function( dialog ) {
				//Don't load before onShow.
				if ( !dialog.onShowExecutedElement || !dialog.preview ) {
					return 1;
				}

				// Read attributes and update imagePreview;
				dialog.commitContent( PREVIEW, dialog.preview );
				return 0;
			};

			function commitContent() {
				var args = arguments;

				this.foreach( function( widget ) {
					if ( widget.commit ) {
						widget.commit.apply( widget, args );
					}
				} );
			}

			// Avoid recursions
			var incommit;

			// Synchronous field values to other impacted fields is required, e.g. border
			// size change should alter inline-style text as well.
			function commitInternally( targetFields ) {
				if ( incommit ) {
					return;
				}

				incommit = 1;

				var dialog = this.getDialog(),
					element = dialog.panelElement;
				if ( element ) {
					// Commit this field and broadcast to target fields.
					this.commit( PANEL, element );

					targetFields = [].concat( targetFields );
					var length = targetFields.length,
						field;
					for ( var i = 0; i < length; i++ ) {
						field = dialog.getContentElement.apply( dialog, targetFields[ i ].split( ':' ) );
						// May cause recursion.
						field && field.setup( PANEL, element );
					}
				}

				incommit = 0;
			}

			var numbering = function( id ) {
					return CKEDITOR.tools.getNextId() + '_' + id;
				},
				previewPanelId = numbering( 'previewPanel' );
				
			// Load skin at first
			var plugin = CKEDITOR.plugins.get( 'wavepanel' );
			CKEDITOR.document.appendStyleSheet( CKEDITOR.getUrl( plugin.path + 'dialogs/wave.css' ) );

			return {
				title: editor.lang.wavepanel['title'],
				minWidth: 460,
				minHeight: 190,
				style: '',
				id:'aaaaa',
				className: 'bbbbbb',
				onShow: function() {
					this.panelElement = false;

					var editor = this.getParentEditor(),
						sel = editor.getSelection(),
						element = sel && sel.getSelectedElement();

					// creating for the sake of updatePreview() method
					this.onShowExecutedElement = editor.document.createElement( 'img' );
					this.preview = CKEDITOR.document.getById( previewPanelId );
					
					//check if it has parent with class .lams-wavepanel
					this.panelElement = editor.elementPath( element ).contains( function( element ) {        
					    if ( element.is( 'div' ) && element.hasClass( 'lams-wavepanel' )) {
					    	return true;
					    }
					});
					if (this.panelElement == null) {
						alert("wavepanel plugin failed to parse selected element and thus exited");
						return;
					}

					// Use the original element as a buffer from  since we don't want
					// temporary changes to be committed, e.g. if the dialog is canceled
					this.cleanPanelElement = this.panelElement;
					this.panelElement = this.cleanPanelElement.clone( true, true );

					// Fill out all fields
					this.setupContent( PANEL, this.panelElement );
					updatePreview( this );
					
					//setup body content as well
					var divPanelBody = this.panelElement.find( 'div.panel-body' ).getItem(0);
					this.preview.find( 'div.panel-body' ).getItem(0).setHtml(divPanelBody.getHtml());

				},
				onOk: function() {

					// Restore the original element before all commits
					this.panelElement = this.cleanPanelElement;
					delete this.cleanPanelElement;

					// Set attributes
					this.commitContent( PANEL, this.panelElement );

					//editor.insertElement( this.panelElement );
				},
				onLoad: function() {
					var doc = this._.element.getDocument();

					this.commitContent = commitContent;
				},
				onHide: function() {
					if ( this.preview ) {
						this.commitContent( CLEANUP, this.preview );
					}
						
					if ( this.onShowExecutedElement ) {
						this.onShowExecutedElement.remove();
						this.onShowExecutedElement = false; // Dialog is closed.
					}

					delete this.panelElement;
				},
				contents: [ {
					id: 'info',
					label: editor.lang.wavepanel.infoTab,
					accessKey: 'I',
					elements: [ 
					{
						type: 'hbox',
						children: [ {
							id: 'basic',
							type: 'vbox',
							children: [ 
								{
                                    type: 'hbox',
                                    id: 'bootsrap-class',
                                    style: 'margin: 5px 0;',
                                    children: [
                                        {
                                            id: 'color-class',
                                            type: 'radio',
                                            items: [	
                                                [editor.lang.wavepanel.colorBlue, 'wave-blue'],
                                                [editor.lang.wavepanel.colorOrange, 'wave-orange'],
                                                [editor.lang.wavepanel.colorGreen, 'wave-green'],
                                                [editor.lang.wavepanel.colorPurple, 'wave-purple'],
                                                [editor.lang.wavepanel.colorPink, 'wave-pink'],
                                                [editor.lang.wavepanel.colorRed, 'wave-red']
                                            ],
                                            style: 'color: white',
                                            label: editor.lang.wavepanel.waveColor,
	        									onChange: function() {
	        										updatePreview( this.getDialog() );
	        									},	        									
	        									setup: function( type, element ) {
	        										if ( type == PANEL ) {
	        											if (element.hasClass( 'wave-blue' )) {
	        												this.setValue( 'wave-blue' );
	        											} else if (element.hasClass( 'wave-orange' )) {
	        												this.setValue( 'wave-orange' );
	        											} else if (element.hasClass( 'wave-purple' )) {
	        												this.setValue( 'wave-purple' );
	        											} else if (element.hasClass( 'wave-pink' )) {
	        												this.setValue( 'wave-pink' );
	        											} else if (element.hasClass( 'wave-red' )) {
	        												this.setValue( 'wave-red' );
	        											}
	        										}
	        									},
	        									commit: function( type, element ) {
	        										
	        										if ( type == PANEL ) {
	        											if ( this.getValue() || this.isChanged() ) {
	        												element.removeClass("wave-blue").removeClass("wave-orange").removeClass("wave-green").removeClass("wave-purple").removeClass("wave-pink").removeClass("wave-red");
	        												element.addClass( this.getValue() );
	        											}
                                                    }

	        									}
                                        }
                                    ],
                                    onLoad:function( type, element ) {
                                    	$("input[type='radio'][name='color-class_radio']+label").addClass("btn");
                                    	$("input[type='radio'][name='color-class_radio']:eq(0)+label").css("background-color","blue");
                                    	$("input[type='radio'][name='color-class_radio']:eq(1)+label").css("background-color","orange");
                                    	$("input[type='radio'][name='color-class_radio']:eq(2)+label").css("background-color","green");
                                    	$("input[type='radio'][name='color-class_radio']:eq(3)+label").css("background-color","purple");
                                    	$("input[type='radio'][name='color-class_radio']:eq(4)+label").css("background-color","pink");
                                    	$("input[type='radio'][name='color-class_radio']:eq(5)+label").css("background-color","red");
	                                }
                                },
								
								 ]
							} ]
						},
				]

				} ]
			};
		};

		CKEDITOR.dialog.add( 'wavepanel', function( editor ) {
			return wavepanelDialog( editor );
		} );
} )();
