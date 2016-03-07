<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=utf-8"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-bean" prefix="bean"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-logic" prefix="logic"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod" 
		 import="org.lamsfoundation.lams.util.Configuration" 
		 import="org.lamsfoundation.lams.util.ConfigurationKeys" %>
		 
<c:set var="showAllMyLessonLink"><%=Configuration.get(ConfigurationKeys.SHOW_ALL_MY_LESSON_LINK)%></c:set>
	
<div style="clear:both;"></div>

<h2 class="small-space-top"><fmt:message key="index.myprofile" /></h2>

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
				
			<c:if test="${showAllMyLessonLink}">				
				<li class="no-list-type"><a
					href="index.do?state=active&tab=lessons"><fmt:message
					key="title.all.my.lessons" /></a></li>
			</c:if>				
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
		<td valign="top">
			<c:choose>
				<c:when test="${empty portraitUuid}">
					<em><fmt:message key="msg.portrait.none" /></em>
					<div class="small-space-top">
						<img src="<lams:LAMSURL />images/default_user.gif" alt="User Portrait"
						class="img-border">
					</div>
				</c:when>
				<c:otherwise>
					<img class="img-border"	src="/lams/download/?uuid=<bean:write name="portraitUuid" />&preferDownload=false" />
				</c:otherwise>
			</c:choose>
		</td>
	<tr>
</table>

</div>