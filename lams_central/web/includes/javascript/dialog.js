var dialogTemplate = $('<div class="modal fade dialogContainer" tabindex="-1" role="dialog">' +
							'<div class="modal-dialog" role="document">' +
								'<div class="modal-content">' +
									'<div class="modal-header">' +
										'<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
											'<i class="fa fa-times" aria-hidden="true"></i>' +
										'</button>' + 
										'<button type="button" class="close dialogMaximise" aria-label="Maximise">' +
											'<i class="fa fa-plus" aria-hidden="true"></i>' +
										'</button>' +
										'<button type="button" class="close dialogMinimise" aria-label="Minimise">' +
											'<i class="fa fa-minus" aria-hidden="true"></i>' +
										'</button>' +
										'<h4 class="modal-title"></h4>' +
									'</div>' +
									'<div class="modal-body">' +
										'<iframe></iframe>' +
									'</div>' +
								'</div>' +
							'</div>' +
						'</div>');

	
/**
 * Checks if the dialog is already opened.
 * If not, creates a new dialog with the given ID and init parameters.
 */
function showDialog(id, initParams, extraButtons, recreate) {
	
	var dialog = $('#' + id);
	// is it open  already?
	if (dialog.length > 0) {
		if (recreate){
			dialog.modal('hide');
		} else {
			// we do not support multiple opened bootstrap dialogs yet
			// dialog.dialog('moveToTop');
			return;
		}
	}
	
	// create a new dialog by cloning a template
	dialog = dialogTemplate.clone().appendTo('body');
	
	// use the input attributes or fall back to default ones
	initParams = $.extend({
		'autoOpen' : true,
		'modal'    : true,
		'draggable' : true,
		'resizable' : extraButtons == true,
		'beforeClose' : function(){
			$('iframe', this).attr('src', null);
		},
		'close' : function() {
			// completely delete the dialog
			$(this).remove();
		}
	}, initParams);
	
	// update title
	$('.modal-title', dialog).attr('id',  id + 'Label').text(initParams.title);
	dialog.attr({
		'id' : id,
		'aria-labelledby' : id + 'Label'
	});
	
	var modalDialog = $('.modal-dialog', dialog),
		modalContent = $('.modal-content', dialog);
	
	if (initParams.width) {
		modalDialog.width(initParams.width);
	}
	if (initParams.height) {
		modalContent.height(initParams.height);
	}
	if (initParams.resizable) {
		modalContent.resizable();
	}
	if (initParams.draggable && ! /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		// breaks the close buttons on Android and iPhone
		modalDialog.draggable();
	}
	// store extra attributes for dialog content internal use
	if (initParams.data) {
		dialog.data(initParams.data);
	}
	
	dialog.on('show.bs.modal', initParams.open);
	dialog.on('hide.bs.modal', initParams.beforeClose);
	dialog.on('hidden.bs.modal', initParams.close);
	
	dialog.modal({
		'keyboard' : false,
		'backdrop' : initParams.modal ? 'static' : false,
		'show' : initParams.autoOpen
	});
	
	if (!initParams.modal) {
		// make the dialog non-modal
		dialog.on('shown.bs.modal', function(){
			// the main modal div is maximised, we need to shrink it
			modalDialog.css({
				'margin' : 0
			});
			dialog.width(modalDialog.outerWidth(true) + 5);
			dialog.height(modalDialog.outerHeight(true) + 5);
			// remove overlay
			dialog.siblings('.modal-backdrop').remove();
			
			dialog.position({
				'of' : 'body'
			});
			
			if (initParams.draggable) {
				modalDialog.on('drag', function(event, ui){
					dialog.offset({
						'top'  : ui.offset.top + 5,
						'left' : ui.offset.left + 5
					});
					modalDialog.css({
						'position' : 'static'
					});
				});
			}
		});
	}
	
	if (extraButtons) {
		$('.dialogMaximise', dialog).click(function(){
			var icon = $('i', this),
				wasMaxed = icon.hasClass('fa-clone'),
				internalDialog = $('.modal-dialog', dialog),
				internalContent = $('.modal-content', dialog);
			icon.toggleClass('fa-plus').toggleClass('fa-clone');
			if (wasMaxed) {
				// restore dialog
				internalDialog.width(dialog.data('oldWidth'));
				internalContent.width(dialog.data('oldWidth'));
				internalContent.height(dialog.data('oldHeight'));
				
				// enable resizing
				$('.ui-resizable-handle', dialog).show();
			} else {
				// store previous size
				dialog.data({
					'oldWidth' : internalContent.width(),
					'oldHeight': internalContent.height()
				});
				internalDialog.css({
					'width' : '100%',
					'height': '100%'
				});
				internalContent.css('width', 'calc(100% - 15px)');
				internalContent.height(internalDialog.height() - 50);
				
				// disable resizing
				$('.ui-resizable-handle', dialog).hide();
			}
			// center the dialog
			internalDialog.position({
				'my' : 'top',
				'at' : 'center top',
				'of' : window
			});
			
			internalContent.trigger('resizestop');
		});
		
	    $('.dialogMinimise', dialog).click(function() {
	    	// swap icon
            $('i', this).toggleClass('fa-minus').toggleClass('fa-clone');
	        dialog.toggleClass('dialogMin');
	        if (dialog.hasClass('dialogMin')) {
	        	// remove overlay
	        	dialog.siblings('.modal-backdrop').hide();
	        	// move the dialog to the bar at the bottom
	            $('<div>').attr('id', 'dialogMinContainer').appendTo('body').append(dialog);
	            // disable maximising 
	        	$('.dialogMaximise', dialog).hide();
	        	// disable rezising
		        $('.ui-resizable-handle', dialog).hide();
	        } else {
	            $('body').append(dialog);
	            $('#dialogMinContainer').remove();
	            dialog.siblings('.modal-backdrop').show();
	            $('.dialogMaximise', dialog).show();
	            $('.ui-resizable-handle', dialog).show();
	        };
	    });
	} else {
		$('.dialogMinimise, .dialogMaximise', dialog).remove();
	}
	
	return dialog;
}


/**
 * Focuses on the dialog. Called from within the contained iframe.
 */
function moveDialogToTop(id) {
	// $('#' + id).dialog('moveToTop');
}