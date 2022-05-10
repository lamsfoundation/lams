	/*
	 This is Resource Item instruction area.
	 */
    var itemAttachmentTargetDiv = "itemAttachmentArea";
	
	function removeItemAttachment(idx){
	    removeItemAttachmentLoading();
		$("#" + itemAttachmentTargetDiv).load(
			removeItemAttachmentUrl,
			{
				reqID: new Date()
			},
			removeItemAttachmentComplete
		);	    
	}
	
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}
	
	function submitResourceItem(){
		//copy value from CKEditor to textarea before ajax submit
		$("textarea[id^='instructions']").each(function()  {
			var ckeditorData = CKEDITOR.instances[this.name].getData();
			//skip out empty values
			this.value = ((ckeditorData == null) || (ckeditorData.replace(/&nbsp;| |<br \/>|\s|<p>|<\/p>|\xa0/g, "").length == 0)) ? "" : ckeditorData;		
		});
		
		// validation will fail with spaces
		var urlField = document.getElementById("url");
		if ( urlField ) {
			urlField.value = urlField.value.trim();
		}
		
		if ( ! $("#resourceItemForm").valid() ) 
			return false;

		var formData = new FormData(document.getElementById("resourceItemForm"));
		disableButtons();
		// after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" resource list and close this window.
	    $.ajax({ // create an AJAX call...
			data: formData, 
	        processData: false, // tell jQuery not to process the data
	        contentType: false, // tell jQuery not to set contentType
           	type: $("#resourceItemForm").attr('method'),
			url: $("#resourceItemForm").attr('action'),
			success: function(data) {
               $('#resourceInputArea').html(data);
			},
			complete: function() {
				enableButtons();
			}
	    });
	    
	}