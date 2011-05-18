
// Toggles whether to display advanced options in monitor summary for tools
function toggleAdvancedOptionsVisibility(div, img, imageUrl)
{
	var treeClosedIcon = imageUrl + "/images/tree_closed.gif";
	var treeOpenIcon = imageUrl + "/images/tree_open.gif";
	if (div.style.display == "block")
	{
		div.style.display = "none";
		img.src = treeClosedIcon;
	}
	else if (div.style.display == "none")
	{
		div.style.display = "block";
		img.src = treeOpenIcon;
	}
}

//check if jquery is loaded
if ((typeof jQuery != 'undefined') && (typeof settings != 'undefined')) {
	//support for setting up submission deadline
	$(function(){
		$("#datetime").datetimepicker();
		
		if (settings.submissionDeadline != "") {
			var date = new Date(eval(settings.submissionDeadline));
			$("#dateInfo").html( formatDate2(date) );
			
			//open up date restriction area
			toggleAdvancedOptionsVisibility(document.getElementById('restrictUsageDiv'), document.getElementById('restrictUsageTreeIcon'),settings.lams);
		}
	});	
	
	function formatDate2(date) {
		var currHour = "" + date.getHours();
		if (currHour.length == 1) {
			currHour = "0" + currHour;
		}			
		var currMin = "" + date.getMinutes();
		if (currMin.length == 1) {
			currMin = "0" + currMin;
		}
		return $.datepicker.formatDate( 'mm/dd/yy', date ) + " " + currHour + ":" + currMin;
	}

	function setSubmissionDeadline() {
		//get the timestamp in milliseconds since midnight Jan 1, 1970
		var date = $("#datetime").datetimepicker('getDate');
		if (date == null) {
			return;
		}

		var reqIDVar = new Date();
		var parameterDelimiter = (settings.setSubmissionDeadlineUrl.indexOf("?") == -1) ? "?" : "&"; 
		
		var url = settings.setSubmissionDeadlineUrl + parameterDelimiter + "toolContentID=" + settings.toolContentID + "&submissionDeadline=" +
					+ date.getTime() + "&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI(settings.messageNotification, settings.messageRestrictionSet);
				$("#datetimeDiv").hide();
				$("#dateInfo").html(formatDate2(date) );
				$("#dateInfoDiv").show();
			}
		});
	}
	function removeSubmissionDeadline() {
		var reqIDVar = new Date();
		var parameterDelimiter = (settings.setSubmissionDeadlineUrl.indexOf("?") == -1) ? "?" : "&"; 
		
		var url = settings.setSubmissionDeadlineUrl + parameterDelimiter + "toolContentID=" + settings.toolContentID + "&submissionDeadline=" +
				"&reqID=" + reqIDVar.getTime();

		$.ajax({
			url : url,
			success : function() {
				$.growlUI(settings.messageNotification, settings.messageRestrictionRemoved);
				$("#dateInfoDiv").hide();
				
				$("#datetimeDiv").show();
				$("#datetime").val("");
			}
		});
	}
}

