 //auto refresh all tabs every 30 seconds
let autoRefreshInterval = 30 * 1000,
	autoRefreshIntervalObject = null;

function refreshMonitor(tabName, isAuto){
	if (autoRefreshIntervalObject && !isAuto) {
		clearInterval(autoRefreshIntervalObject);
		autoRefreshIntervalObject = null;
	}

	if (!autoRefreshIntervalObject) {
		autoRefreshIntervalObject = setInterval(function(){
			// refreshMonitor(null, true);
		}, autoRefreshInterval);
	}
	
	updateLessonTab();
}

/**
 * Updates widgets in lesson tab according to response sent to refreshMonitor()
 */
function updateLessonTab(){
	$.ajax({
		dataType : 'json',
		url : LAMS_URL + 'monitoring/monitoring/getLessonDetails.do',
		cache : false,
		data : {
			'lessonID'  : lessonId
		},
		
		success : function(response) {
			updateContributeActivities(response.contributeActivities);
		}
	});
}


function updateContributeActivities(contributeActivities) {
	let requiredTasksPanel = $('#required-tasks'),
		requiredTasksContent = $('#required-tasks-content', requiredTasksPanel),
		row = null;
	$('.contribute-row', requiredTasksContent).remove();
	
	/*
	// special case - add a Live Edit option. This does not directly map to an activity
	if ( lockedForEdit && lockedForEditUserId == userId) {
		// show Live Edit task only if currently editing myself, not if someone else is editing.
		// put it at the top of the contribution list
		var cell = $('<div />').addClass('contributeActivityCell').text(LABELS.LIVE_EDIT_BUTTON);
		var row = $('<div />').addClass('contributeRow').insertAfter(row).append(cell);
		var entryContent = LABELS.LIVE_EDIT_TOOLTIP 
			+ '<span class="btn btn-xs btn-primary pull-right" onClick="javascript:openLiveEdit()" title="' + LABELS.CONTRIBUTE_TOOLTIP
			+ '">' + LABELS.CONTRIBUTE_BUTTON + '</span>';
		cell = $('<div />').addClass('contributeEntryCell').html(entryContent);
		row = row.append(cell);
	}
	*/
	
	if (contributeActivities) {
		$.each(contributeActivities, function(){
			let contributeActivity = this;
			if (contributeActivity.title) {
				$('<div />').addClass('label contribute-activity-title contribute-row')
							.text(contributeActivity.title)
							.attr('id', 'contribute' + contributeActivity.activityID)
							.appendTo(requiredTasksContent);
			}
			
			let row = $('<div />').addClass('row contribute-row' + (contributeActivity.title ? ' ml-1' : ''))
								  .appendTo(requiredTasksContent);
			
			$.each(this.contributeEntries, function(){
				var entryContent = '<div class="col-3 label">';
				switch (this.contributionType) {
					case 3  : entryContent += LABELS.CONTRIBUTE_GATE; break;
					case 6  : entryContent += LABELS.CONTRIBUTE_GROUPING; break;
					case 7  : entryContent += LABELS.CONTRIBUTE_TOOL; break;
					case 9  : entryContent += LABELS.CONTRIBUTE_BRANCHING; break;
					case 11 : entryContent += LABELS.CONTRIBUTE_CONTENT_EDITED; break; 
					case 12 : entryContent += LABELS.CONTRIBUTE_GATE_PASSWORD; break; 
				}
				entryContent += '</div><div class="col-9 text-right">';
				switch (this.contributionType) {
					case 3  : 
					case 12 : if (this.isComplete) {
						 		entryContent += '<span class="badge badge-success">' + LABELS.CONTRIBUTE_OPENED_GATE + '</span>';
							} else {
								entryContent += '<div class="btn-group" role="group"><button onClick="javascript:openGateNow('
                                    + contributeActivity.activityID + ')" type="button" class="btn" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_NOW_BUTTON 
									+ '</button><button type="button" class="btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">'
									+ '<div class="dropdown-menu"><a href="#" class="dropdown-item" onClick="javascript:openPopUp(\''
                                    + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_TOOLTIP + '">' 
									+ LABELS.CONTRIBUTE_OPEN_GATE_BUTTON + '</a></div></div>';
									
							}
							break;
					default : entryContent += '<button type="button" class="btn contribute-go-button" onClick="javascript:openPopUp(\''
						 + this.url + '\',\'ContributeActivity\', 800, 1280, true)" title="' + LABELS.CONTRIBUTE_TOOLTIP
						 + '">' + LABELS.CONTRIBUTE_BUTTON + '</button>';
				}

				row.html(entryContent + "</div>");
			});
		});
		
		requiredTasksPanel.show();
	} else {
		requiredTasksPanel.hide();
	}
}


function openGateNow(activityId) {
	var data = {
		'activityId' : activityId
	};
	data[csrfTokenName] = csrfTokenValue;
	$.ajax({
		'type' : 'post',
		'url'  : LAMS_URL + 'monitoring/gate/openGate.do',
		'data'  : data,
		'success' : function(){
			updateLessonTab();
		}
	});
}

function openPopUp(url, title, h, w, status, forceNewWindow) {

	var width = screen.width;
	var height = screen.height;

	var left = ((width / 2) - (w / 2));
	var top = ((height / 2) - (h / 2));

	if (forceNewWindow) {
		// opens a new window rather than loading content to existing one
		title += new Date().getTime();
	}

	window.open(url, title, "HEIGHT=" + h + ",WIDTH=" + w
			+ ",resizable=yes,scrollbars=yes,status=" + status
			+ ",menubar=no, toolbar=no"
			+ ",top=" + top + ",left=" + left);
}