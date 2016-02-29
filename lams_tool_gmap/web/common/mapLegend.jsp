<%@ include file="/common/taglibs.jsp"%>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<!--  remove the choose once all across to bootstrap - only picking up learner at the moment as only learner has been bootstrapped! -->
<c:choose>
<c:when test="${mode == 'learner'}">
<!--  bootstrap version - keep -->
<table>
	<tr>
		<td valign="middle"><fmt:message key="label.legend.unsaved" /></td>
 		<td><img src="${tool}/images/red_Marker.png"></img></td>
 		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.saved" /></td>
		<td><img src="${tool}/images/blue_Marker.png"></img></td>
		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.edited" /></td>
 		<td><img src="${tool}/images/paleblue_Marker.png"></img></td>
 		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.userSelected" /></td>
 		<td><img src="${tool}/images/yellow_Marker.png"></img></td>
	</tr>
</table>
</c:when>
<c:otherwise>
<!--  old version - to go -->
<html>
<div   >
<table style="border:WhiteSmoke 2px solid;">
	<tr>
		<td valign="middle"><fmt:message key="label.legend.unsaved" /></td>
 		<td><img src="${tool}/images/red_Marker.png"></img></td>
 		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.saved" /></td>
		<td><img src="${tool}/images/blue_Marker.png"></img></td>
		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.edited" /></td>
 		<td><img src="${tool}/images/paleblue_Marker.png"></img></td>
 		<td width="10px"></td>
 		<td valign="middle"><fmt:message key="label.legend.userSelected" /></td>
 		<td><img src="${tool}/images/yellow_Marker.png"></img></td>
	</tr>
</table>
</div>

</html>


</c:otherwise>
</c:choose>


