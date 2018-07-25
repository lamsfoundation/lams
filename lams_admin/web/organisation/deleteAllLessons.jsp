<%@ include file="/taglibs.jsp"%>

<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var lessonCount = ${lessonCount},
		deleteButton = $('#deleteButton');
	
	if (lessonCount == 0) {
		deleteButton.prop('disabled', true);
		return;
	}
	
	deleteButton.click(function(){
		
		if (!confirm('<fmt:message key="msg.delete.all.lesson.confirm.1"><fmt:param value="${courseName}"/></fmt:message>')
				|| !confirm('<fmt:message key="msg.delete.all.lesson.confirm.2" />')) {
			return;
		}
		
		deleteButton.prop('disabled', true);
		$('#deletingBox').show();
		
		// delete lesson in batches of 5 until done
		deleteAllLessons(lessonCount, $('#lessonCount'));
	});
});

function deleteAllLessons(lessonCount, lessonCountSpan){
	if (lessonCount <= 0) {
		$('#deletingBox').hide();
		return;
	}
	$.ajax({
		'cache'   : false,
		'url'     : '<lams:WebAppURL />organisation.do',
		'data'    : {
			'method' : 'deleteAllLessons',
			'limit'  : 5,
			'orgId'  : ${param.orgId}
		},
		'success' : function(response){
			try {
				lessonCount = response;
				lessonCountSpan.text(lessonCount);
				setTimeout(function(){
					deleteAllLessons(lessonCount, lessonCountSpan);
				}, 500);
			} catch(err) {
				alert('<fmt:message key="msg.delete.all.lesson.error" />');
				lessonCountSpan.text('ERROR');
			}
		},
		'error'	  : function(){
			alert('<fmt:message key="msg.delete.all.lesson.error" />');
		}
	});
}
</script>

<p>
	<a href="orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
	:  <a href="orgmanage.do?org=${param.orgId}" class="btn btn-default"><c:out value="${courseName}" /></a>
</p>

<html:errors />

<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount">${lessonCount}</span>&nbsp;/&nbsp;<span>${lessonCount}</span>
<div id="deletingBox" style="display: none">
	<fmt:message key="label.delete.all.lesson.progress" />
</div>

<div class="pull-right">
	<button id="deleteButton" class="btn btn-primary loffset5"><fmt:message key="admin.delete"/></button>
</div>