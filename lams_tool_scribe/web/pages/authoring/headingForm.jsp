<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="<lams:LAMSURL />includes/javascript/jquery.js"></script>

<script type="text/javascript"> 
	$(function() {
		//change size of an iframe on ckeditor's autogrow 
		CKEDITOR.instances.heading.on("instanceReady", function(e) {
		    e.editor.on('resize', function(reEvent){
		    	var iframe = window.parent.document.getElementById("iframeArea");
		    	iframe.style.height = eval(iframe.contentWindow.document.body.scrollHeight) + 'px';
		    });
		});
	});

	function callHideMessage() {
		if (window.parent && window.parent.hideMessage) {
			window.parent.hideMessage();
		} else if (window.parent && window.parent.hideMessage) {
			window.top.hideMessage();
		}
	}
</script>

<table>
	<tr>
		<td>
			<html:form action="/authoring" styleId="authoringForm" method="post">

				<html:hidden property="dispatch" value="addOrUpdateHeading" />
				<html:hidden property="sessionMapID" />
				<html:hidden property="headingIndex" />

				<c:set var="formBean"
					value="<%=request.getAttribute(org.apache.struts.taglib.html.Constants.BEAN_KEY)%>" />
				<c:set var="sessionMap"
					value="${sessionScope[formBean.sessionMapID]}" />

				<h2 class="no-space-left">
					<fmt:message key="label.authoring.basic.heading.add"></fmt:message>
				</h2>

				<c:set var="headingText" value="" />
				<c:if test="${not empty formBean.headingIndex}">
					<c:set var="headingText">
				${sessionMap.headings[formBean.headingIndex].headingText}
			</c:set>
				</c:if>

				<lams:CKEditor id="heading" value="${headingText}"
					contentFolderID="${sessionMap.contentFolderID}"></lams:CKEditor>

				<div class="small-space-top">

					<html:submit styleClass="button" titleKey="basic.title">
						<fmt:message key="button.submit" />
					</html:submit>

					<html:button property="Cancel" styleClass="button"
						onclick="javascript:callHideMessage()">
						<fmt:message key="button.cancel" />
					</html:button>
			</html:form>

			</div>
		</td>
	</tr>
</table>

