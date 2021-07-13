function checkNextGateActivity(finishButtonId, toolSessionId, lessonId, submitFunction){
	// we need bootstrap tooltip, not jQuery UI one
	// if it has not been isolated yet, try to do it now
	if (typeof $.fn.bootstrapTooltip != 'function') {
		$.fn.bootstrapTooltip = $.fn.tooltip.noConflict();
	}
	
	$(document).ready(function(){
		let finishButton = $('#' + finishButtonId);
		
		if (finishButton.length == 0){
			return;
		}
		
		finishButton
			.bootstrapTooltip({
				'trigger' : 'manual'
			})
			.click(function(event){
				if (finishButton.prop('disabled') == true) {
					// if the button is already disabled, do not run a check
					return;
				}
				
				// disable the button
				finishButton.prop('disabled', true).attr('disabled', true);
				
				// check if there is a gate after this activity
				// if so, check if learner can pass
				$.ajax({
					'url' : '/lams/learning/learner/isNextGateActivityOpen.do?toolSessionId=' + toolSessionId + '&lessonId=' + lessonId,
					'cache' : false,
					'dataType' : 'json',
					'success'  : function(response) {
						if (response.status == 'open') {
							// learner can pass
							finishButton.prop('disabled', false).attr('disabled', false);
							submitFunction();
							return;
						}
						
						if (response.status == 'closed') {
							// if there are other events bound to click, do not make them run
							event.stopImmediatePropagation();
							
							let timeoutFunction = null;
							if (response.message) {
								// tooltips should say whatever we got in the response
								finishButton.attr({
									'title' : response.message,
									'data-original-title' : response.message
								}).bootstrapTooltip('show');
								
								timeoutFunction = function(){
									finishButton.bootstrapTooltip('hide').prop('disabled', false).attr('disabled', false);
								};
							} else {
								timeoutFunction = function(){
									finishButton.prop('disabled', false).attr('disabled', false);
								};
							}
	
							// show tooltip for several seconds
							setTimeout(timeoutFunction, 5000);
						}
					}
				});
			});
	});
}