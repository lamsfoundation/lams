<%@ include file="/common/taglibs.jsp"%>

<c:set var="dto" value="${gmapDTO}" />
<c:forEach var="session" items="${dto.sessionDTOs}">

	<table>
		<tr>
			<td>
				<h2>
					${session.sessionName}
				</h2>
			</td>
		</tr>
	</table>

	<table cellpadding="0">
		<tr>
			<td class="field-name" width="30%">
				<fmt:message key="heading.totalLearners" />
			</td>
			<td width="70%">
				${session.numberOfLearners}
			</td>
		</tr>
	</table>

	
	<table style="cellpadding:0; cellspacing:0; border:0; width:500px;">
		<tr>
			<td width="80%">
			<div id="map_canvas" style="width:400px;height:300px;" ></div></td>
			<td width="20%">
			<div id="usersidebar" style="width:100px; overflow:auto;height:320px; background:WhiteSmoke; "></div>
			</td>
		</tr>
		<tr>
			<td>
			<a href="javascript:fitMapMarkers()" class="button"/><fmt:message key="button.fitMarkers"/></a>
			</td>
		</tr>
	</table>
	
	<table>
	<tr><td>
		<input type="text" size="60" name="address" id="address" value="<fmt:message key="label.authoring.basic.sampleAddress"></fmt:message>" />
 		<a href="javascript:showAddress()" class="button"/><fmt:message key="button.go"/></a>
	</td></tr>
	</table>
	
	<script type="text/javascript">
	<!--
		users = new Array();
		addUserToList('0','<fmt:message key="label.authoring.basic.authored"></fmt:message>' );
		<c:forEach var="user" items="${session.userDTOs}">
			addUserToList('${user.uid}','${user.firstName} ${user.lastName}' );
		</c:forEach>
	//-->
	</script>
	
</c:forEach>
