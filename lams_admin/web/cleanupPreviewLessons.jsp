<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="sysadmin.batch.preview.lesson.delete"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
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
				'url'     : '<lams:WebAppURL />cleanupPreviewLessons/delete.do?<csrf:token/>',
				'data'    : {
					'limit'  : 5
				},
				type: 'POST',
				'cache'   : false,
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
</lams:head>

<body class="stripes">

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/sysadminstart.do | <fmt:message key="sysadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="sysadmin.batch.preview.lesson.delete"/></c:set>

	<lams:Page type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">	
		
		<fmt:message key="label.cleanup.preview.lesson.count" />&nbsp;<span id="previewCount">${previewCount}</span> / <span id="allLessonCount">${allLessonCount}</span>
		<div id="deletingBox" style="display: none">
		<fmt:message key="label.cleanup.preview.lesson.progress" />
		</div>
				
		<div class="pull-right">
			<button id="deleteButton" class="btn btn-primary loffset5"><fmt:message key="admin.delete"/></button>
		</div>
	</lams:Page>

</body>
</lams:html>

