<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.lesson.delete.title"/></c:set>
	<title>${title}</title>

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-smoothness-theme.css" type="text/css" media="screen">
	<script language="JavaScript" type="text/JavaScript" src="<lams:LAMSURL/>/includes/javascript/changeStyle.js"></script>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />
	
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
			'url'     : '<lams:LAMSURL/>admin/organisation/deleteAllLessons.do',
			'data'    : {
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
</lams:head>
    
<body class="stripes">

	<lams:Page type="admin" title="${title}">
				<p>
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default"><fmt:message key="admin.course.manage" /></a>
					:  <a href="<lams:LAMSURL/>admin/orgmanage.do?org=${param.orgId}" class="btn btn-default"><c:out value="${courseName}" /></a>
				</p>
				
				<%-- Error Messages --%>
				 <c:set var="errorKey" value="GLOBAL" />
				        <c:if test="${not empty errorMap and not empty errorMap[errorKey]}">
				            <lams:Alert id="error" type="danger" close="false">
				                <c:forEach var="error" items="${errorMap[errorKey]}">
				                    <c:out value="${error}" />
				                </c:forEach>
				            </lams:Alert>
				        </c:if>
				
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


