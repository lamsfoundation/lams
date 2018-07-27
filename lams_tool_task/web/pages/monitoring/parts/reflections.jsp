<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	$(document).ready(function(){
		initializePortraitPopover('<lams:LAMSURL />');
	});
</script>

<div class="panel panel-default" >
	<div class="panel-heading" id="headingReflections">
    	<span class="panel-title collapsable-icon-left">
    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseReflections" 
			aria-expanded="true" aria-controls="collapseReflections" >
		<fmt:message key="label.monitoring.summary.title.reflection"/></a>
		</span>
    </div>
    
    <div id="collapseReflections" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingReflections">

		<table class="table table-condensed table-striped">				
			<c:forEach var="reflectDTO" items="${sessionMap.reflectList}">
				<tr>
					<td valign=top class="align-left">
						<lams:Portrait userId="${reflectDTO.userId}" hover="true"><c:out value="${reflectDTO.fullName}" escapeXml="true"/></lams:Portrait>
						 - <lams:Date value="${reflectDTO.date}"/>
						<br>
						<lams:out value="${reflectDTO.reflect}" escapeHtml="true"/>
			
					</td>
				</tr>
			</c:forEach>
		</table>

	</div>
</div>