<%@ include file="/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var previewCount = ${previewCount},
		deleteButton = $('#deleteButton');
	
	if (previewCount == 0) {
		deleteButton.prop('disabled', true);
		return;
	}
	
	deleteButton.click(function(){
		if (!confirm('<fmt:message key="msg.cleanup.preview.lesson.confirm" />')) {
			return;
		}
		
		deleteButton.prop('disabled', true);
		$('#deletingBox').show();
		
		// delete lesson in batches of 5 until done
		deletePreviewLessons(previewCount, $('#previewCount'), $('#allLessonCount'));
	});
});

function deletePreviewLessons(previewCount, previewCountSpan, allLessonCountSpan){
	if (previewCount <= 0) {
		$('#deletingBox').hide();
		return;
	}
	$.ajax({
		'cache'   : false,
		'url'     : '<lams:WebAppURL />cleanupPreviewLessons.do',
		'data'    : {
			'method' : 'deletePreviewLessons',
			'limit'  : 5
		},
		'success' : function(response){
			try {
				previewCount = response[0];
				previewCountSpan.text(previewCount);
				allLessonCountSpan.text(response[1]);
				setTimeout(function(){
					deletePreviewLessons(previewCount, previewCountSpan, allLessonCountSpan);
				}, 500);
			} catch(err) {
				alert('<fmt:message key="msg.cleanup.preview.lesson.error" />');
				previewCountSpan.text('ERROR');
				allLessonCountSpan.text('ERROR');
			}
		},
		'error'	  : function(){
			alert('<fmt:message key="msg.cleanup.preview.lesson.error" />');
		}
	});
}
</script>

<p><a href="<lams:LAMSURL/>admin/sysadminstart.do" class="btn btn-default"><fmt:message key="sysadmin.maintain" /></a></p>

<html:errors />

<fmt:message key="label.cleanup.preview.lesson.count" />&nbsp;<span id="previewCount">${previewCount}</span> / <span id="allLessonCount">${allLessonCount}</span>
<div id="deletingBox" style="display: none">
	<fmt:message key="label.cleanup.preview.lesson.progress" />
</div>

<div class="pull-right">
	<button id="deleteButton" class="btn btn-primary loffset5"><fmt:message key="admin.delete"/></button>
</div>