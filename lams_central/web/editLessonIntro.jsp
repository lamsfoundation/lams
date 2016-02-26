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
	<!--

		$(function() {
			$('#lessonForm').ajaxForm( {
				beforeSerialize: function(arr, $form, options) { 
					$("#lessonDescription").val(CKEDITOR.instances.lessonDescription.getData());                 
				},
			    success:    function() {
			    	self.parent.document.location.reload();
			    	self.parent.tb_remove();
			    }
			 } );
		});
	//-->
	</script>
	
	<lams:css/>
	<style media="screen,projection" type="text/css">
	</style>	
	
</lams:head>

<body class="stripes">
	
	<div id="content">
	
		<form action="editLessonIntro.do?method=save" method="post" id="lessonForm">
		
			<input type="hidden" name="lessonID" value="${lesson.lessonId}"/>
	
			<div class="field-name">
				<fmt:message key="label.lesson.name"></fmt:message>
			</div>
			<input type="text" name="lessonName" value="<c:out value='${lesson.lessonName}' />" style="width: 99%;" />
			
			<div class="field-name space-top">
				<fmt:message key="label.instructions"></fmt:message>
			</div>
			<lams:CKEditor id="lessonDescription" value="${lesson.lessonDescription}"
				contentFolderID="${contentFolderID}">
			</lams:CKEditor>
		
			<div class="space-top">
				<input type="checkbox" name="displayDesignImage" <c:if test="${displayDesignImage}">checked="checked"</c:if> class="noBorder" value="true"/>
				<fmt:message key="label.display.lesson.design"></fmt:message>
			</div>
			
			<br /><br /><br />        
			
			<lams:ImgButtonWrapper>
				<div class="float-right">
					<a href="#nogo" onclick="$('#lessonForm').submit();" onmousedown="self.focus();" class="button">
						<fmt:message key="button.save" /> 
					</a>
					<a href="#nogo" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left">
						<fmt:message key="label.cancel" /> 
					</a>
				</div>
			</lams:ImgButtonWrapper>
		
		</form>
			
	</div>
	   
	<div id="footer"></div>

</BODY>
	
</lams:html>
