<%@ include file="/common/taglibs.jsp"%>

<div class="panel panel-default" >
   <div class="panel-heading" id="headingReflections">
   	<span class="panel-title collapsable-icon-left">
	   	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseReflections" 
			aria-expanded="false" aria-controls="collapseReflections" >
		<fmt:message key="title.reflection"/></a>
	</span>
    </div>
     
	<div id="collapseReflections" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingReflections">

	<table class="table table-condensed table-striped">
	
		<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
			<tr>
				<td>
					<strong><c:out value="${reflectDTO.fullName}" escapeXml="true"/></strong> - <lams:Date value="${reflectDTO.date}"/>
					<br>
					<lams:out value="${reflectDTO.reflect}" escapeHtml="true" />
				</td>
			</tr>
		</c:forEach>
	
	</table>
	
	</div>
</div>
