<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>
<c:set var="escapeHTML" value="${printDTO.allowRichEditor ? 'false' : 'true'}"/>
<lams:html>
	<lams:head>
		<lams:css/>
		
		<style>
			body {
				padding: 10px;
			}
			
			/* Print only the aread below the print button */
			@media print {
			  body * {
			    visibility: hidden;
			  }
			  #section-to-print, #section-to-print * {
			    visibility: visible;
			  }
			  #section-to-print {
			    position: absolute;
			    left: 0;
			    top: 0;
			  }
			}
		</style>
		
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
	</lams:head>

	<body>
		<button class="btn btn-primary pull-right" onClick="javascript:window.print()">
			<i class="fa fa-print"></i> <span class="hidden-xs"><fmt:message key="monitor.summary.print"/></span>
		</button>
		
		<div id="section-to-print">
			<h3><c:out value="${printDTO.title}" /></h3>
			<c:out value="${printDTO.instructions}" escapeXml="false"/>		
			<hr>
			
			<c:forEach items="${printDTO.usersBySession}" var="sessionEntry">

				<c:if test="${printDTO.groupedActivity}">
				<h3><c:out value="${sessionEntry.key}" /></h3>
				</c:if>
				
				<c:forEach items="${sessionEntry.value}" var="printUserDTO">
				<c:if test="${not empty printUserDTO.entry}">
				<h4 style="margin-bottom: 1px"><c:out value="${printUserDTO.lastName}" />&nbsp;<c:out value="${printUserDTO.firstName}" /></h4>
				<small><c:out value="${printUserDTO.email}" /></small></br>
					<span class="loffset10"><small><lams:Date value="${printUserDTO.entryModifiedDate}"/></small></span> 
					
					<div class="panel-body" style="margin-left: 1em;">
						<c:choose> 
							<c:when test="${printDTO.allowRichEditor}">
								<c:out value="${printUserDTO.entry}"  escapeXml='false' />
							</c:when>
							<c:otherwise>
								<lams:out  value="${printUserDTO.entry}" escapeHtml='false' />
							</c:otherwise>
						</c:choose>	
					</div>
					<c:if test="${not empty printUserDTO.teacherComment}">
						<div style="padding-left: 2em;">
						<h4 class="voffset20"><fmt:message key="label.comment" /></h4>
						<p class="panel-body"><lams:out value="${printUserDTO.teacherComment}"/> </p>
						</div>
					</c:if>
					<hr>
				  </c:if>	
				</c:forEach>
			   	
			</c:forEach>	
		</div>
	</body>
</lams:html>
