<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod" %>

<script language="JavaScript" type="text/javascript" src="includes/javascript/jquery-1.2.1.min.js"></script>
<script language="javascript" type="text/javascript">
	$(function(){
		$("div.display-group").each(function(){
			$(this).load("displayGroup.do", {stateId: 3, orgId: this.id});
		});
		$("body").click(function(event) {
			if ($(event.target).is("a.group-header")){
				$(event.target).parent("h2").parent("div.left-buttons").parent("div.row").next("div.course-contents").toggle("fast");
				return false;
			}
		});
	});
</script>
	
<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="index.myprofile" />
</h2>

<div class="shading-bg">

<table>

	<tr>

		<td class="align-right"><span class="field-name"><fmt:message
			key="label.name" />:</span></td>
		<td><bean:write name="fullName" /></td>

		<td rowspan="3" class="userpage-td" valign="top">
		<ul>
			<li class="no-list-type"><a
				href="index.do?state=active&tab=editprofile"><fmt:message
				key="title.profile.edit.screen" /> </a></li>

			<c:set var="authenticationMethodId"><lams:user property="authenticationMethodId"/></c:set>
			<c:set var="dbId"><%= AuthenticationMethod.DB %></c:set>
			<c:if test="${authenticationMethodId eq dbId}">
			<li class="no-list-type"><a
				href="index.do?state=active&tab=password"><fmt:message
				key="title.password.change.screen" /></a></li>
			</c:if>

			<li class="no-list-type"><a
				href="index.do?state=active&tab=portrait"><fmt:message
				key="title.portrait.change.screen" /></a></li>
				
			<li class="no-list-type"><a
				href="index.do?state=active&tab=lessons"><fmt:message
				key="title.all.my.lessons" /></a></li>
		</ul>

		</td>
	</tr>
	<tr>
		<td class="align-right"><span class="field-name"><fmt:message
			key="label.email" />:</span></td>
		<td><bean:write name="email" /></td>
	</tr>
	<tr>
		<td class="align-right" valign="top"><span class="field-name"><fmt:message
			key="label.portrait.current" />:</span></td>
		<td valign="top"><logic:notEqual name="portraitUuid" value="0">
			<img class="img-border"
				src="/lams/download/?uuid=<bean:write name="portraitUuid" />&preferDownload=false" />
		</logic:notEqual> <logic:equal name="portraitUuid" value="0">
			<c:set var="lams">
				<lams:LAMSURL />
			</c:set>
				<em><fmt:message key="msg.portrait.none" /></em>
			<div class="small-space-top">
			<img src="${lams}/images/default_user.gif" alt="User Portrait"
				class="img-border">
				</div>
		</logic:equal></td>
	<tr>
</table>

</div>


<h2 class="space-top"><fmt:message key="title.archived.groups" />
</h2>

<c:forEach items="${courseIds}" var="courseId">
	<div id="<c:out value="${courseId}"/>" class="display-group"></div>
</c:forEach>
<c:if test="${empty courseIds}">
	<p class="align-left"><fmt:message key="msg.groups.empty" /></p>
</c:if>
