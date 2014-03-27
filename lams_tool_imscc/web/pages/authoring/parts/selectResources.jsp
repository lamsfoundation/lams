<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
		"http://www.w3.org/TR/html4/loose.dtd">

<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tool"> <lams:WebAppURL /> </c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
	<lams:head>
		<%@ include file="/common/header.jsp"%>
		<lams:css style="tabbed"/>

		<script type="text/javascript" src="<html:rewrite page='/includes/javascript/commonCartridgeItem.js'/>"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
		<script type="text/javascript">
    		function formSubmit(){
    			$('#itemForm').ajaxSubmit({
    				target: $("#commonCartridgeListArea", self.parent.document),
    				success: function() {
    					self.parent.refreshThickbox();
    					self.parent.tb_remove();
    				}
    			});
    		}
		</script>
	</lams:head>

	<body class="stripes" onload="parent.resizeIframe();">
	<div id="content" style="width: 93%;">
		<%@ include file="/common/messages.jsp"%>

<form id="itemForm" name="itemForm" action="<c:url value='/authoring/selectResources.do'/>">
<input type="hidden" value="${sessionMapID}" name="sessionMapID"> 
<div>
	<h1 class="small-space-bottom">
		<fmt:message key="label.authoring.select.available.resources" />
	</h1>

	<table class="alternative-color" id="itemTable" cellspacing="0">
		
		<c:forEach var="resource" items="${sessionMap.uploadedCartridgeResources}" varStatus="status">
			<tr>
				<%-- resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
				<c:if test="${resource.type == 1}">
					<td width="10%">
						<input type="checkbox" name="item${status.index}" checked="checked"> 
						&nbsp;&nbsp;
						<span class="field-name"> 
							<fmt:message key="label.authoring.basic.resource.url" /> : 
						</span>
						<c:out value="${resource.title}" escapeXml="true"/>
						<br/>
						
						<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon${status.index}" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv${status.index}'), document.getElementById('treeIcon${status.index}'), '<lams:LAMSURL/>');" />
					
						<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv${status.index}'), document.getElementById('treeIcon${status.index}'),'<lams:LAMSURL/>');" >
							<fmt:message key="label.authoring.advanced.settings" />
						</a>
						
						<div class="monitoring-advanced" id="advancedDiv${status.index}" style="display:none">
							
							<div class="field-name space-top">
								<fmt:message key="label.authoring.basic.bascilti.url" />
							</div>							
							<c:choose>
								<c:when test="${(empty resource.launchUrl) && !(empty resource.secureLaunchUrl)}">
									<input type="text" name="secureLaunchUrl${status.index}" value="${resource.secureLaunchUrl}" size="55" />
								</c:when>
								<c:otherwise>
									<input type="text" name="launchUrl${status.index}" value="${resource.launchUrl}" size="55" />
								</c:otherwise>
							</c:choose>
							
							<div class="field-name space-top">
								<fmt:message key="label.authoring.basic.bascilti.key" />
							</div>
							<input type="text" name="toolKey${status.index}" value="${resource.key}" size="55" />
							
							<div class="field-name space-top">
								<fmt:message key="label.authoring.basic.bascilti.secret" />
							</div>
							<input type="text" name="toolSecret${status.index}" value="${resource.secret}" size="55" />
							
							<div class="field-name space-top">
								<fmt:message key="label.authoring.basic.bascilti.button.text" />
							</div>
							<input type="text" name="buttonText${status.index}" value="<c:out value='${resource.buttonText}' />" size="55" />
				
							<div class="space-top">
								<input type="checkbox" name="openUrlNewWindow${status.index}" id="openUrlNewWindow${status.index}" class="noBorder" <c:if test="${resource.openUrlNewWindow}">checked="checked"</c:if> />
								<label for="openUrlNewWindow">
									<fmt:message key="open.in.new.window" />
								</label>
							</div>
							
							<div class="field-name space-top">
								<fmt:message key="label.authoring.basic.bascilti.iframe.height" />
							</div>
							<input type="text" name="frameHeight${status.index}" value="${resource.frameHeight}" size="55" />
			

						</div>
						
				</c:if>
			</tr>
		</c:forEach>
	</table>
</div>
</form>


	<lams:ImgButtonWrapper >
		<a href="#" onclick="self.parent.tb_remove();" onmousedown="self.focus();" class="button space-left right-buttons">
			<fmt:message key="label.cancel" /> 
		</a>
		<a href="#" onclick="formSubmit();" onmousedown="self.focus();" class="button right-buttons">
			<fmt:message key="label.authoring.done" />
		</a>
	</lams:ImgButtonWrapper>

	</div>	
	</body>
</lams:html>
