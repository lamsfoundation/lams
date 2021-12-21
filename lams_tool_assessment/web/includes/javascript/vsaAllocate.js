$(document).ready(function(){
    //init options sorting feature
	$('.sortable-on').each(function() {
		let questionUid = $(this).data('question-uid');
		updateAnswerQueueSize(questionUid);
		
	    new Sortable(this, {
	    	group: 'question' + questionUid,
		    animation: 150,
		  	filter: '.filtered', // 'filtered' class is not draggable
			onEnd: function (evt) {
				let data = {
		            	questionUid: questionUid,
		            	targetOptionUid: $(evt.to).data("option-uid"),
		            	previousOptionUid: $(evt.from).data("option-uid"),
		            	answer: $('.answer-text', evt.item).text()
				    };
				data[csrfTokenName] = csrfTokenValue;
				
		        $.ajax({
		            url: WEB_APP_URL + 'monitoring/allocateUserAnswer.do',
		            data: data,
		            method: 'post',
		          	dataType: "json",
			        success: function (data) {
			        	updateAnswerQueueSize(questionUid);
			        	
			            if (data.isAnswerDuplicated) {
  				        	alert(VS_ANSWER_ALLOCATED_ALERT);
  				        	$(evt.item).appendTo("#answer-group" + data.optionUid);
  				        	$(evt.item).addClass("filtered");
	  				    }
			        }
		       	});
			}
		});
	});

	$('.answer-alternatives button').click(function(){
		var button = $(this),
			answer = button.text(),
			container = button.closest('.answer-alternatives'),
			questionUid = container.data('question-uid'),
			optionUid = container.data('option-uid'),
			data = {
	            	questionUid: questionUid,
	            	targetOptionUid: -1,
	            	previousOptionUid: optionUid,
	            	answer: answer
				    };

		data[csrfTokenName] = csrfTokenValue;
				
        $.ajax({
		    url: WEB_APP_URL + 'monitoring/allocateUserAnswer.do',
            data: data,
            method: 'post',
          	dataType: "json",
		        success: function (data) {
		            if (data.answerFoundInResults) {
				        $('<div class="list-group-item" />')
				        	.append($('<div class="portrait-anonymous portrait-generic-sm" />'))
				        	.append($('<span class="answer-text"/>').text(answer))
				        	.appendTo('#answer-queue' + questionUid);

				        updateAnswerQueueSize(questionUid);
  				    }

  				   	button.remove();
		    }
       	});
	});
});

function updateAnswerQueueSize(questionUid) {
	 var answerQueueLength = $('#answer-queue' + questionUid + ' .list-group-item').length;
	 $('#answer-queue-size' + questionUid).text(answerQueueLength ? ' (' + answerQueueLength + ')' : '');
}