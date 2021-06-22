<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" scope="request" />
<c:set var="tool"> <lams:WebAppURL /> </c:set>
<c:set var="sessionMap" value="${sessionScope[sessionMapID]}" />

<lams:html>
<lams:head>
	<%@ include file="/common/header.jsp"%>

	<lams:JSImport src="includes/javascript/commonCartridgeItem.js" relative="true" />
	<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
	<script type="text/javascript" src="${lams}includes/javascript/jquery.form.js"></script>
	<script type="text/javascript">
   		function formSubmit(){
   			$("#itemForm").submit();
   		}
	</script>
</lams:head>

<body class="stripes">
<form id="itemForm" name="itemForm" action="<c:url value='/authoring/selectResources.do'/>">
	
	<c:set var="title"><fmt:message key="label.authoring.select.available.resources" /></c:set>
	<lams:Page title="${title}" type="learner">
		<input type="hidden" value="${sessionMapID}" name="sessionMapID"> 
		
		<lams:errors/>

		<table class="table" id="itemTable">
			
			<c:forEach var="resource" items="${sessionMap.uploadedCartridgeResources}" varStatus="status">
				<tr>
					<%-- resource Type:1=URL,2=File,3=Website,4=Learning Object --%>
					<c:if test="${resource.type == 1}">
						<td>
							<div class="checkbox">
								<label for="item${status.index}">
									<input type="checkbox" name="item${status.index}" checked="checked"> 
									<fmt:message key="label.authoring.basic.resource.url" />: 
									<c:out value="${resource.title}" escapeXml="true"/>
								</label>
							</div>
						
							<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon${status.index}" 
									onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv${status.index}'), document.getElementById('treeIcon${status.index}'), '<lams:LAMSURL/>');" />
							<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv${status.index}'), document.getElementById('treeIcon${status.index}'),'<lams:LAMSURL/>');" >
								<fmt:message key="label.authoring.advanced.settings" />
							</a>
							
							<div class="monitoring-advanced" id="advancedDiv${status.index}" style="display:none">
								
								<div class="form-group">
									<label>
										<fmt:message key="label.authoring.basic.bascilti.url" />
									</label>
									<c:choose>
										<c:when test="${(empty resource.launchUrl) && !(empty resource.secureLaunchUrl)}">
											<input type="text" name="secureLaunchUrl${status.index}" value="${resource.secureLaunchUrl}" class="form-control" />
										</c:when>
										<c:otherwise>
											<input type="text" name="launchUrl${status.index}" value="${resource.launchUrl}" class="form-control" />
										</c:otherwise>
									</c:choose>
								</div>
								
								<div class="form-group">
									<label for="toolKey${status.index}">
										<fmt:message key="label.authoring.basic.bascilti.key" />
									</label>
									<input type="text" name="toolKey${status.index}" value="${resource.key}" class="form-control" />
								</div>
								
								<div class="field-name space-top">
									<label for="toolSecret${status.index}">
										<fmt:message key="label.authoring.basic.bascilti.secret" />
									</label>
									<input type="text" name="toolSecret${status.index}" value="${resource.secret}" class="form-control" />
								</div>
								
								<div class="field-name space-top">
									<label for="buttonText${status.index}">
										<fmt:message key="label.authoring.basic.bascilti.button.text" />
									</label>
									<input type="text" name="buttonText${status.index}" value="<c:out value='${resource.buttonText}' />" class="form-control" />
								</div>
								
								<div class="checkbox">
									<label for="openUrlNewWindow${status.index}">
										<input type="checkbox" name="openUrlNewWindow${status.index}" id="openUrlNewWindow${status.index}" class="noBorder" <c:if test="${resource.openUrlNewWindow}">checked="checked"</c:if> />
										<fmt:message key="open.in.new.window" />
									</label>
								</div>
								
								<div class="field-name space-top">
									<label for="frameHeight${status.index}">
										<fmt:message key="label.authoring.basic.bascilti.iframe.height" />
									</label>
									<input type="text" name="frameHeight${status.index}" value="${resource.frameHeight}" class="form-control" />
								</div>
								
							</div>
						</td>	
					</c:if>
				</tr>
			</c:forEach>
		</table>

		<div class="voffset10 pull-right">
			<a href="#nogo" onclick="javascript:formSubmit();" onmousedown="self.focus();"  class="btn btn-sm btn-default">
				<fmt:message key="label.authoring.done" /> 
			</a>
			<a href="#nogo" onclick="javascript:self.parent.tb_remove();" onmousedown="self.focus();"  class="btn btn-sm btn-default loffset5">
				<fmt:message key="label.cancel" /> 
			</a>
		</div>
 	</lams:Page>
</form>

</body>
</lams:html>
