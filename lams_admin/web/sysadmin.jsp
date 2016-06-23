<%@ include file="/taglibs.jsp"%>

<logic:iterate name="groupedLinks" id="links">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="panel-title"><fmt:message key="${links[0]}"/></div>
		</div>
		<div class="list-group">
			<c:set var="linkBeans" value="${links[1]}"/>
 			<logic:iterate name="linkBeans" id="linkBean">
				<span class="list-group-item">
				<a href="<bean:write name="linkBean" property="link"/>">
						<fmt:message><bean:write name="linkBean" property="name"/></fmt:message>
				</a>
				</span>
			</logic:iterate>
 		</div>
	</div>		
</logic:iterate>
	
