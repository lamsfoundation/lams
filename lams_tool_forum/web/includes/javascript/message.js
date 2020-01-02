	/*
	 This is Resource Item instrcution area.
	 */
    var itemAttachmentTargetDiv = "itemAttachmentArea";
// Please set these 1 variables in JSP file for using tag reason:
//removeItemAttachmentUrl
	function removeItemAttachment(){
		//var id = "instructionItem" + idx;
		//Element.remove(id);
 		var url= removeItemAttachmentUrl;
	    var reqIDVar = new Date();
	    var param = "reqID="+reqIDVar.getTime();
		var data = {
			'reqID=' : reqIDVar.getTime()
		};
		data[csrfTokenName] = csrfTokenValue;
	    removeItemAttachmentLoading();
	    
		$.ajax({
            type: 'POST',
            url: url,
            data: data,
            success: function(data) {
            	$("#"+itemAttachmentTargetDiv).html(data);
            	removeItemAttachmentComplete();
            }
        });
	}
	function removeItemAttachmentLoading(){
		showBusy(itemAttachmentTargetDiv);
	}
	function removeItemAttachmentComplete(){
		hideBusy(itemAttachmentTargetDiv);
	}

	function showBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = '';
		}
	}

	function hideBusy(targetDiv){
		var div = document.getElementById(targetDiv+"_Busy");
		if(div != null){
			document.getElementById(targetDiv+"_Busy").style.display = 'none';
		}				
	}
	
	function highlightMessage() {
		$('.highlight').filter($('table')).css('background','none');
		$('.highlight').filter($('div')).switchClass('highlight', '', 6000);
		// monitor messages have bg-info class - need to fade from highlight yellow to that, otherwise from yellow to nothing
		$('.highlight').filter($('div')).children('.bg-info').removeClass('bg-info').switchClass('highlight', 'bg-info', 6000);

	}

	function setupJRating(rateMessagePath) {
		$(".rating-stars-new").filter($(".rating-stars")).jRating({
			phpPath : rateMessagePath,
			rateMax : 5,
			decimalLength : 1,
			onSuccess : function(data, messageId){
				$("#averageRating" + messageId).html(data.averageRating);
				$("#numberOfVotes" + messageId).html(data.numberOfVotes);
				$("#numOfRatings").html(data.numOfRatings);

				//disable rating feature in case maxRate limit reached
				if (data.noMoreRatings) {
					$(".rating-stars").each(function() {
						$(this).jRating('readOnly');
					});
				}
			},
			onError : function(){
				jError('Error : please retry');
			}
		});
		$(".rating-stars-new").filter($(".rating-stars-disabled")).jRating({
			rateMax : 5,
			isDisabled : true
		});
		$(".rating-stars-new").removeClass("rating-stars-new");
	}

