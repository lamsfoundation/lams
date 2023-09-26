<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	
	<lams:css/>
	
	<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#lessonForm').ajaxForm( {
				beforeSerialize: function(arr, $form, options) { 
					$("#lessonDescription").val(CKEDITOR.instances.lessonDescription.getData());                 
				},
			    success:    function() {
			    	alert('<spring:escapeBody javaScriptEscape="true"><fmt:message key="label.lesson.introduction.updated" /></spring:escapeBody>');
			    	if ( typeof parent.window.refreshMonitor !== "undefined") {
						parent.window.refreshMonitor('lesson', false);
						parent.window.closeIntroductionDialog();
			    	}
			    }
			 } );
		});
	</script>
</lams:head>

<body class="stripes">
	
	<lams:Page type="admin">		
		<form action="save.do" method="post" id="lessonForm">
			<input type="hidden" name="lessonID" value="${lesson.lessonId}"/>
	
			<H4><c:out value='${lesson.lessonName}' /></H4>
			
			<div class="field-group">
				<label for="lessonDescription"><fmt:message key="label.instructions"></fmt:message></label>
				<lams:CKEditor id="lessonDescription" value="${lesson.lessonDescription}" contentFolderID="${contentFolderID}" toolbarSet="LessonDescription" >
				</lams:CKEditor>
			</div>
		
			<div class="voffset10">
				<input type="checkbox" name="displayDesignImage" <c:if test="${displayDesignImage}">checked="checked"</c:if> class="noBorder" value="true"/>
				<fmt:message key="label.display.lesson.design"></fmt:message>
			</div>
			
			<div class="pull-right">
				<a href="#nogo" onclick="$('#lessonForm').submit();" class="btn btn-primary">
					<fmt:message key="button.save" /> 
				</a>
			</div>
		
		</form>
			
		<div id="footer"></div>
	</lams:Page>
</BODY>
	
</lams:html>
