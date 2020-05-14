<!DOCTYPE html>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.tool.notebook.util.NotebookConstants"%>
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
		<button class="btn btn-primary" onClick="javascript:window.print()">
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
					<h4><c:out value="${printUserDTO.lastName}" />&nbsp;<c:out value="${printUserDTO.firstName}" />&nbsp;<c:out value="${printUserDTO.email}" /></h4>
					<span><small><lams:Date value="${printUserDTO.entryModifiedDate}"/></small></span> 
					
					<p><c:out value="${printUserDTO.entry}" escapeXml="false" /></p>
					
					<c:if test="${not empty printUserDTO.teacherComment}">
						<h4 class="voffset20"><fmt:message key="label.comment" /></h4>
	 					<p><c:out value="${printUserDTO.teacherComment}" /></p>
					
					</c:if>
					<hr>
				</c:forEach>
			</c:forEach>	
		</div>
	</body>
</lams:html>
