<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<%@ include file="/common/taglibs.jsp"%>

<html>
	<lams:head>
		<title><c:out value="${mainPageTitle}" escapeXml="false" />
		</title>
		<lams:css localLinkPath="../" />
	
		<script type="text/javascript" src="<lams:LAMSURL/>/includes/javascript/monitorToolSummaryAdvanced.js" ></script>
		
		<script type="text/javascript">
		<!--
			var viewWindow = null;
			function openViewWindow(url)
			{
				if(viewWindow && viewWindow.open && !viewWindow.closed){
					viewWindow.close();
				}
				viewWindow = window.open(url,'viewWindow','resizable,width=796,height=570,scrollbars');
				viewWindow.window.focus();
			}
	
		-->
		</script>
	
	</lams:head>

	<body class="stripes">


		<div id="content">
		
			<h1>
				${mainPageTitle}
			</h1>
			
			<br />

			<!-- The advanced options pane -->
			<h1>
				<img src="<lams:LAMSURL/>/images/tree_closed.gif" id="treeIcon" onclick="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'), '<lams:LAMSURL/>');" />
			
				<a href="javascript:toggleAdvancedOptionsVisibility(document.getElementById('advancedDiv'), document.getElementById('treeIcon'),'<lams:LAMSURL/>');" >
					<fmt:message key="monitor.summary.th.advancedSettings" />
				</a>
			</h1>
			<br />
			
			<div class="monitoring-advanced" id="advancedDiv" style="display:none">
				<table class="alternative-color">
				
					<tr>
						<td>
							<fmt:message key="advanced.lockOnFinished" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.lockOnFinish}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<tr>
						<td>
							<fmt:message key="advanced.allowLearnerCreatePages" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.allowLearnerCreatePages}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<tr>
						<td>
							<fmt:message key="advanced.allowLearnerInsertLinks" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.allowLearnerInsertLinks}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<tr>
						<td>
							<fmt:message key="advanced.allowLearnerAttachImages" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.allowLearnerAttachImages}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<tr>
						<td>
							<fmt:message key="advanced.allowLearnerAttachImages" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.allowLearnerAttachImages}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<tr>
						<td>
							<fmt:message key="monitor.summary.td.addNotebook" />
						</td>
						
						<td>
							<c:choose>
								<c:when test="${wikiDTO.reflectOnActivity == true}">
									<fmt:message key="label.on" />
								</c:when>
								<c:otherwise>
									<fmt:message key="label.off" />
								</c:otherwise>
							</c:choose>	
						</td>
					</tr>
					
					<c:choose>
						<c:when test="${wikiDTO.reflectOnActivity == true}">
							<tr>
								<td>
									<fmt:message key="monitor.summary.td.notebookInstructions" />
								</td>
								<td>
									${wikiDTO.reflectInstructions}	
								</td>
							</tr>
						</c:when>
					</c:choose>
					
					<tr>
						<td>
							<fmt:message key="advanced.editingLimits.prompt" />
						</td>
						
						<td>
							<fmt:message key="advanced.editingLimits.minimum" /> 
							<c:choose>
								<c:when test="${wikiDTO.minimumEdits == 0}">
									<fmt:message key="advanced.editingLimits.nominimum" />
								</c:when>
								<c:otherwise>
									${wikiDTO.minimumEdits}
								</c:otherwise>
							</c:choose>
							
							<br />
							
							<fmt:message key="advanced.editingLimits.maximum" /> 
							<c:choose>
								<c:when test="${wikiDTO.maximumEdits == 0}">
									<fmt:message key="advanced.editingLimits.nomaximum" />
								</c:when>
								<c:otherwise>
									${wikiDTO.maximumEdits}
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
					
				</table>
			</div>	
			
			<c:choose>
				<c:when test="${not empty wikiDTO.sessionDTOs}">
					<table class="alternative-color">
						<tr>
							<th>
								<fmt:message key="monitor.th.sessions"></fmt:message>
							</th>	
							<th>
								<fmt:message key="monitor.th.numlearners"></fmt:message>
							</th>
						</tr>
						<c:forEach var="session" items="${wikiDTO.sessionDTOs}">
						<tr>
							<td>
								<a href="javascript:openViewWindow('./${session.sessionID}.html')">${session.sessionName}</a>
							</td>	
							<td>
								${session.numberOfLearners}
							</td>	
						</tr>
						</c:forEach>
					</table>
			
				</c:when>
				<c:otherwise>
					<fmt:message key="monitor.nosessions"></fmt:message> 
				</c:otherwise>
			</c:choose>
				
			
		</div>
		<!--closes content-->

		<div id="footer">
		</div>
		<!--closes footer-->
	</body>
</html>

