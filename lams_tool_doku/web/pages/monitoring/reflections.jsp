<%@ include file="/common/taglibs.jsp"%>

<div class="panel-group" id="accordionReflection" role="tablist" aria-multiselectable="true"> 
    <div class="panel panel-default" >
        <div class="panel-heading collapsable-icon-left" id="headingReflection">
        	<span class="panel-title">
	    		<a class="collapsed" role="button" data-toggle="collapse" href="#collapseReflection" 
	    				aria-expanded="false" aria-controls="collapseReflection" >
          			<fmt:message key="title.reflection"/>
        		</a>
      		</span>
        </div>

        <div id="collapseReflection" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingReflection">
			<table class="table table-striped table-condensed">
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
</div>
