	function submitNomination(){
		for (var i in CKEDITOR.instances) {
       		CKEDITOR.instances[i].updateElement();
		}
		// after submit, it direct to itemlist.jsp, 
		// then refresh "basic tab" resource list and close this window.
	    $.ajax({ // create an AJAX call...
			data: $("#newNominationForm").serialize(), 
           	type: $("#newNominationForm").attr('method'),
			url: $("#newNominationForm").attr('action'),
			success: function(data) {
               $('#messageArea').html(data);
			}
	    });
	    
	}