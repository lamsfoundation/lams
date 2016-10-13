var dialogTemplate = $('<div class="modal fade dialogContainer" tabindex="-1" role="dialog">' +
							'<div class="modal-dialog" role="document">' +
								'<div class="modal-content">' +
									'<div class="modal-header">' +
										'<button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
											'<span aria-hidden="true">&times;</span>' +
										'</button>' + 
										'<button class="close dialogMinimise">' +
											'<span aria-hidden="true">&minus;</span>' +
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
			dialog.remove();
		} else {
			// dialog.dialog('moveToTop');
			return;
		}
	}
	
	// create a new dialog by cloning a template
	dialog = dialogTemplate.clone();
	

	$('#myModalLabel', dialog).text(LABELS.GRADEBOOK_LESSON_TITLE);
	initParams = $.extend({
		'autoOpen' : true,
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
	
	$('.modal-title', dialog).attr('id',  id + 'Label').text(initParams.title);
	dialog.attr({
		'id' : id,
		'aria-labelledby' : id + 'Label'
	});
	
	if (initParams.width) {
		$('.modal-dialog', dialog).width(initParams.width);
	}
	if (initParams.height) {
		$('.modal-content', dialog).height(initParams.height);
	}
	if (initParams.draggable) {
		$('.modal-dialog', dialog).draggable();
		$('.modal-header', dialog).css('cursor', 'move');
	}
	if (initParams.resizable) {
		$('.modal-content', dialog).resizable();
	}
	if (initParams.data) {
		dialog.data(initParams.data);
	}
	
	dialog.on('show.bs.modal', initParams.open);
	dialog.on('hide.bs.modal', initParams.beforeClose);
	dialog.on('hidden.bs.modal', initParams.close);
	
	dialog.modal({
		'keyboard' : false,
		'backdrop' : 'static',
		'show' : initParams.autoOpen
	});
	
	if (extraButtons) {
	    $('.dialogMinimise', dialog).on('click', function() {
	        dialog.siblings('.modal-backdrop').addClass('display-none');
	        dialog.toggleClass('min');
	        if (dialog.hasClass('min')) {
	            $('.minmaxCon').append(dialog);
	            $('.dialogMinimise span', dialog).text('&clone;');
	        } else {
	            $('body').append(dialog);
	            $('.dialogMinimise span', dialog).text('&minus;');
	        };
	    });
	    /*
	    $("button[data-dismiss='modal']").click(function() {
	        dialog.removeClass('min');
	        $(body).removeClass($apnData);
	        $(this).next('.modalMinimize').find("i").removeClass('fa fa-clone').addClass('fa fa-minus');
	    });
	    */
	} else {
		$('.dialogMinimise', dialog).remove();
	}
	
	return dialog;
}


/**
 * Focuses on the dialog. Called from within the contained iframe.
 */
function moveDialogToTop(id) {
	// $('#' + id).dialog('moveToTop');
}