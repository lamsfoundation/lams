
<h2><fmt:message key="titleHeading.instructions"/></h2>
<div id="datatablecontainer">
	<table width="50%" border="0" cellspacing="0" cellpadding="0">
<!-- 	 <tr>
	    <th scope="col">Online Instructions</th>
	    <th scope="col">Offline Instructions </th>
  	</tr>
  	
  		
		<tr>
			<td>
			<c:out value="${sessionScope.onlineInstructions}" escapeXml="false"/>
			</td>
			<td>
			<c:out value="${sessionScope.offlineInstructions}" escapeXml="false"/>
			</td>
		</tr>  -->
	<!--	<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td align="left">
				<c:out value="${sessionScope.offlineInstructions}" escapeXml="false"/>
			</td>
		</tr> -->
		
		<tr>
			<td><fmt:message key="instructions.onlineInstructions"/></td>
			<td><c:out value="${sessionScope.onlineInstructions}" escapeXml="false"/></td>
		
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><fmt:message key="instructions.offlineInstructions"/></td>
			<td><c:out value="${sessionScope.offlineInstructions}" escapeXml="false"/></td>
		</tr>
	</table>
</div>

<hr>

