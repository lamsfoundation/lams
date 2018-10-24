/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

( function() {
	var bootpanelDialog = function( editor ) {
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
			var plugin = CKEDITOR.plugins.get( 'bootpanel' );
			CKEDITOR.document.appendStyleSheet( CKEDITOR.getUrl( plugin.path + 'dialogs/element.css' ) );

			return {
				title: editor.lang.bootpanel['title'],
				minWidth: 460,
				minHeight: 490,
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
					
					//check if it has parent with class .lams-bootpanel
					this.panelElement = editor.elementPath( element ).contains( function( element ) {        
					    if ( element.is( 'div' ) && element.hasClass( 'lams-bootpanel' )) {
					    	return true;
					    }
					});
					if (this.panelElement == null) {
						alert("bootpanel plugin failed to parse selected element and thus exited");
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
					label: editor.lang.bootpanel.infoTab,
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
                                                [editor.lang.bootpanel.classDefault, 'panel-default'],
                                                [editor.lang.bootpanel.classPrimary, 'panel-primary'],
                                                [editor.lang.bootpanel.classSuccess, 'panel-success'],
                                                [editor.lang.bootpanel.classInfo, 'panel-info'],
                                                [editor.lang.bootpanel.classWarning, 'panel-warning'],
                                                [editor.lang.bootpanel.classDanger, 'panel-danger']
                                            ],
                                            label: editor.lang.bootpanel.bootClass,
	        									onChange: function() {
	        										updatePreview( this.getDialog() );
	        									},	        									
	        									setup: function( type, element ) {
	        										if ( type == PANEL ) {
	        											if (element.hasClass( 'panel-default' )) {
	        												this.setValue( 'panel-default' );
	        											} else if (element.hasClass( 'panel-primary' )) {
	        												this.setValue( 'panel-primary' );
	        											} else if (element.hasClass( 'panel-success' )) {
	        												this.setValue( 'panel-success' );
	        											} else if (element.hasClass( 'panel-info' )) {
	        												this.setValue( 'panel-info' );
	        											} else if (element.hasClass( 'panel-warning' )) {
	        												this.setValue( 'panel-warning' );
	        											} else if (element.hasClass( 'panel-danger' )) {
	        												this.setValue( 'panel-danger' );
	        											}
	        										}
	        									},
	        									commit: function( type, element ) {
	        										
	        										if ( type == PANEL ) {
	        											if ( this.getValue() || this.isChanged() ) {
	        												element.removeClass("panel-default").removeClass("panel-primary").removeClass("panel-success").removeClass("panel-info").removeClass("panel-warning").removeClass("panel-danger");
	        												element.addClass( this.getValue() );
	        											}
	        										} else if ( type == PREVIEW ) {
	        											element.removeClass("panel-default").removeClass("panel-primary").removeClass("panel-success").removeClass("panel-info").removeClass("panel-warning").removeClass("panel-danger");
	        											element.addClass( this.getValue() );
	        										} else if ( type == CLEANUP ) {
	        											element.removeClass("panel-default").removeClass("panel-primary").removeClass("panel-success").removeClass("panel-info").removeClass("panel-warning").removeClass("panel-danger");
	        										}

	        									}
                                        }
                                    ],
                                    onLoad:function( type, element ) {
                                    	$("input[type='radio'][name='color-class_radio']+label").addClass("btn panel-heading");
                                    	
                                    	$("input[type='radio'][name='color-class_radio']:eq(0)").parent().addClass(" panel-default");
                                    	$("input[type='radio'][name='color-class_radio']:eq(1)").parent().addClass(" panel-primary");
                                    	$("input[type='radio'][name='color-class_radio']:eq(2)").parent().addClass(" panel-success");
                                    	$("input[type='radio'][name='color-class_radio']:eq(3)").parent().addClass(" panel-info");
                                    	$("input[type='radio'][name='color-class_radio']:eq(4)").parent().addClass(" panel-warning");
                                    	$("input[type='radio'][name='color-class_radio']:eq(5)").parent().addClass(" panel-danger");
	                                }
                                },
                                {
                                    type: 'checkbox',
                                    id: 'is-clickable',
                                    label: editor.lang.bootpanel.isClickable,
                                    'default': '',
                                    onClick: function() {
                                    	updatePreview( this.getDialog() );
                                    },
                                    setup: function( type, element ) {
        								if ( type == PANEL ) {
        									var divPanelHeading = element.find( 'div.panel-heading' ).getItem(0);
        									this.setValue( divPanelHeading.hasClass('clickable') );
        								}
        							},
									commit: function( type, element ) {
										
										function changeVal(element, checkbox ) {
											var divPanelHeading = element.find( 'div.panel-heading' ).getItem(0);
											var spanPullRight = divPanelHeading.find( 'span.pull-right' ).getItem(0);
											
											if (checkbox.getValue()) {
												divPanelHeading.addClass("clickable");
												spanPullRight.setStyle( 'display', 'block' );
											} else {
												divPanelHeading.removeClass("clickable");
												spanPullRight.setStyle( 'display', 'none' );
											}
										}
										
										if ( type == PANEL ) {
											if ( this.getValue() || this.isChanged() ) {
												changeVal(element, this);
											}
										} else if ( type == PREVIEW ) {
											changeVal(element, this);
										} else if ( type == CLEANUP ) {
											element.removeClass("btn-block");
										}
									}
                                }
								
								 ]
							} ]
						},
						{
							id: 'panelHeading',
							type: 'text',
							label: editor.lang.bootpanel.panelHeading,
							accessKey: 'T',
							'default': '',
							onKeyUp: function() {
								updatePreview( this.getDialog() );
							},
							setup: function( type, element ) {
								if ( type == PANEL ) {
									var h3PanelTitle = element.find( 'h3.panel-title' ).getItem(0);
									this.setValue( h3PanelTitle.getHtml() );
								}
							},
							commit: function( type, element ) {
								
								function changeVal(element, panelHeading ) {
									var h3PanelTitle = element.find( 'h3.panel-title' ).getItem(0);
									
									h3PanelTitle.setHtml( panelHeading.getValue() );
								}
								
								if ( type == PANEL ) {
									if ( this.getValue() || this.isChanged() ) {
										changeVal(element, this);
									}
								} else if ( type == PREVIEW ) {
									changeVal(element, this);
								} else if ( type == CLEANUP ) {
									var h3PanelTitle = element.find( 'h3.panel-title' ).getItem(0);
									
									h3PanelTitle.setText( '' );
								}

							}
						},
						{
							type: 'vbox',
							height: '100px',
							children: [ {
								type: 'html',
								id: 'htmlPreview',
								style: 'width:95%;',
								html: '<div>' + CKEDITOR.tools.htmlEncode( editor.lang.common.preview ) + '<br>' +
									'<div class="ImagePreviewBox">' +
							            '<div id="' + previewPanelId + '" class="panel lams-bootpanel">' +
							            '    <div class="panel-heading">' +
							            '        <h3 class="panel-title">' +
										'		</h3>' +
							            '        <span class="pull-right" style="display:none;"><i class="fa fa-minus"></i></span>' +
							            '    </div>' +
							            '    <div class="panel-body">' +
										'		<div>' +
										'		</div>' +
										'	</div>' +
							            '</div>' +
									'</div></div>'
							} ]
						}
				]

				} ]
			};
		};

		CKEDITOR.dialog.add( 'bootpanel', function( editor ) {
			return bootpanelDialog( editor );
		} );
} )();
