<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.lesson.delete.title"/></c:set>
	<fmt:message key="msg.delete.all.lesson.confirm.1" var="DELETE_CONFIRM_VAR"><fmt:param value="${courseName}"/></fmt:message>
	
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			var lessonCount = ${lessonCount},
				deleteButton = $('#deleteButton'),
				decoderDiv = $('<div />');
			
			if (lessonCount == 0) {
				deleteButton.prop('disabled', true);
				return;
			}
			
			deleteButton.click(function(){
				
				if (!confirm(decoderDiv.html('<c:out value="${DELETE_CONFIRM_VAR}" />').text())
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
			'type': 'POST',
			'cache'   : false,
			'url'     : '<lams:LAMSURL/>admin/organisation/deleteAllLessons.do',
			'data'    : {
				'limit'  : 5,
				'orgId'  : <c:out value="${param.orgId}" />,
				"<csrf:tokenname/>":"<csrf:tokenvalue/>"
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
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}">
				<p>
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					:  <a href='<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${param.orgId}" />' class="btn btn-default"><c:out value="${courseName}" /></a>
				</p>
				
				<lams:errors/>
				
				<fmt:message key="label.delete.all.lesson.count" />&nbsp;<span id="lessonCount">${lessonCount}</span>&nbsp;/&nbsp;<span>${lessonCount}</span>
				<div id="deletingBox" style="display: none">
					<fmt:message key="label.delete.all.lesson.progress" />
				</div>
				
				<div class="pull-right">
					<button id="deleteButton" class="btn btn-primary loffset5"><fmt:message key="admin.delete"/></button>
				</div>
	</lams:Page>

</body>
</lams:html>


