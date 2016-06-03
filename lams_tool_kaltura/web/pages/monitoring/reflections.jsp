<%@ include file="/common/taglibs.jsp"%>

<c:if test="${kaltura.reflectOnActivity && not empty reflectList}">

	<div class="panel-group" id="reflections" role="tablist" aria-multiselectable="true"> 
	    <div class="panel panel-default" >
	        <div class="panel-heading collapsable-icon-left" id="headingReflection">
	        	<span class="panel-title">
			    	<a class="collapsed" role="button" data-toggle="collapse" href="#collapseReflection" aria-expanded="false" aria-controls="collapseReflection" >
		          		<fmt:message key="label.reflections"/>
		        	</a>
	      		</span>
	        </div>
	
	        <div id="collapseReflection" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingReflection">
				<table class="table table-striped table-condensed">
					<c:forEach var="reflectDTO" items="${reflectList}">
						<tr>
							<td>
								<strong><c:out value="${reflectDTO.fullName}" escapeXml="true"/></strong> - <lams:Date value="${reflectDTO.lastModified}"/>
								<br>
								<lams:out value="${reflectDTO.entry}" escapeHtml="true" />
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>

</c:if>