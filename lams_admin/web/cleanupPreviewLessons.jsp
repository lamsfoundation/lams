<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="appadmin.batch.preview.lesson.delete"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<link rel="stylesheet" href="<lams:LAMSURL/>css/bootstrap5.custom.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>includes/font-awesome6/css/all.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/components.css">
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap5.bundle.min.js"></script>
	
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

<body class="component pb-4 pt-2 px-2 px-sm-4">

	<%-- Build breadcrumb --%>
	<c:set var="breadcrumbItems"><lams:LAMSURL/>admin/appadminstart.do | <fmt:message key="appadmin.maintain" /></c:set>
	<c:set var="breadcrumbItems">${breadcrumbItems}, . | <fmt:message key="appadmin.batch.preview.lesson.delete"/></c:set>

	<lams:Page5 type="admin" title="${title}" breadcrumbItems="${breadcrumbItems}">	

		<fmt:message key="label.cleanup.preview.lesson.count" />&nbsp;<span id="previewCount">${previewCount}</span> / <span id="allLessonCount">${allLessonCount}</span>
		<div id="deletingBox" style="display: none">
			<fmt:message key="label.cleanup.preview.lesson.progress" />
		</div>
				
		<div class="float-end">
			<button id="deleteButton" class="btn btn-primary"><fmt:message key="admin.delete"/></button>
		</div>
	</lams:Page5>

</body>
</lams:html>

