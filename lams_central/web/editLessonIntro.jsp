<!DOCTYPE html>

<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-lams" prefix="lams" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-html" prefix="html" %>
<c:set var="lams" ><lams:LAMSURL/></c:set>

<lams:html>
<lams:head>
	<TITLE><fmt:message key="title.learner.window"/></TITLE>
	
	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery.form.js"></script>
	<script language="JavaScript" type="text/javascript">
		$(function() {
			$('#lessonForm').ajaxForm( {
				beforeSerialize: function(arr, $form, options) { 
					$("#lessonDescription").val(CKEDITOR.instances.lessonDescription.getData());                 
				},
			    success:    function() {
			    	alert('<fmt:message key="label.lesson.introduction.updated"/>');
			    	if ( typeof parent.window.refreshMonitor !== "undefined") {
						parent.window.refreshMonitor('lesson', false);
						parent.window.closeIntroductionDialog();
			    	}
			    }
			 } );
		});
	</script>
	
	<lams:css/>
	
</lams:head>

<body class="stripes">
	
	<lams:Page type="admin">		
		<form action="editLessonIntro.do?method=save" method="post" id="lessonForm">
			<input type="hidden" name="lessonID" value="${lesson.lessonId}"/>
	
			<div class="field-group">
				<label for="lessonName"><fmt:message key="label.lesson.name"></fmt:message></label>
				<input type="text" name="lessonName" value="<c:out value='${lesson.lessonName}' />" class="form-control" style="width: 99%;" />
			</div>
			
			<div class="field-group">
				<label for="lessonDescription"><fmt:message key="label.instructions"></fmt:message></label>
				<lams:CKEditor id="lessonDescription" value="${lesson.lessonDescription}"
					contentFolderID="${contentFolderID}">
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
