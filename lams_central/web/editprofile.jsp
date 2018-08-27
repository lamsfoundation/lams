<!DOCTYPE html>

<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> 
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ page import="org.lamsfoundation.lams.usermanagement.AuthenticationMethod"
	import="org.lamsfoundation.lams.util.Configuration"
	import="org.lamsfoundation.lams.util.ConfigurationKeys"%>

<lams:html>
<lams:head>
	<lams:css/>
	<style type="text/css">
		body {
			overflow-x:hidden;
		}
	</style>

	<script type="text/javascript" src="includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="includes/javascript/profile.js"></script>
	<script type="text/javascript">
		function submitMessage() {
			var formData = new FormData(document.getElementById("newForm"));

			$.ajax({ // create an AJAX call...
				data : formData,
				processData : false, // tell jQuery not to process the data
				contentType : false, // tell jQuery not to set contentType
				type : $("#newForm").attr('method'),
				url : $("#newForm").attr('action'),
				success : function(data) {
					if ( data.indexOf('profileRestrictions') > 0)
						$("html").html(data);
					else
						window.parent.location.reload();
				}
			});
		}
		
		$(document).ready( function() {
			//update dialog's height and title
			updateMyProfileDialogSettings(
				'<fmt:message key="title.profile.edit.screen" />',
				'100%'
			);
		});
	</script>
</lams:head>

<body>
	<form:form action="saveprofile.do" modelAttribute='newForm' method="post" id='newForm'>
		<form:hidden path="userId" />
		<form:hidden path="login" />
		<form:hidden path="password" />

		 <c:set var="errorKey" value="GLOBAL" /> 
		 <c:if test="${not empty errorMap and not empty errorMap[errorKey]}"> 
		     <lams:Alert id="error" type="danger" close="false"> 
		         <c:forEach var="error" items="${errorMap[errorKey]}"> 
		             <c:out value="${error}" /><br /> 
		         </c:forEach> 
		     </lams:Alert> 
		</c:if>

		<c:set var="profileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_EDIT_ENABLE)%></c:set>
		<c:set var="partialProfileEditEnabled"><%=Configuration.get(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)%></c:set>
		<div style="clear: both;"></div>
		<div class="container">
			<div class="row vertical-center-row">
				<div
					class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
					<div class="panel">
						<div class="panel-body">
							<c:set var="authenticationMethodId">
								<lams:user property="authenticationMethodId" />
							</c:set>
							<c:set var="dbId"><%=AuthenticationMethod.DB%></c:set>
							<c:if test="${authenticationMethodId eq dbId}">

								<div class="form-group">
									<span class="lead"><label><fmt:message
												key="label.username" /></label>: ${UserForm.login}</span>
								</div>
								<div class="form-group">
									<label><fmt:message key="label.title" />:</label>
									<form:input path="title" size="32" maxlength="32"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.first_name" /> *:</label>
									<form:input path="firstName" size="50" maxlength="128"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.last_name" /> *:</label>
									<form:input path="lastName" size="50" maxlength="128"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>
								<c:if test="${!profileEditEnabled}">
									<form:hidden path="firstName" />
									<form:hidden path="lastName" />
								</c:if>
								<div class="form-group">
									<label><fmt:message key="label.email" /> *:</label>
									<form:input path="email" size="50" maxlength="128"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.address_line_1" />:</label>
									<form:input path="addressLine1" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" csseClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.address_line_2" />:</label>
									<form:input path="addressLine2" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.address_line_3" />:</label>
									<form:input path="addressLine3" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.city" />:</label>
									<form:input path="city" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.state" />:</label>
									<form:input path="state" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.postcode" />:</label>
									<form:input path="postcode" size="10" maxlength="10"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.country" />:</label>
									<form:input path="country" size="50" maxlength="64"
										disabled="${!profileEditEnabled}" cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.day_phone" />:</label>
									<form:input path="dayPhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.evening_phone" />:</label>
									<form:input path="eveningPhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										cssClass="form-control" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.mobile_phone" />:</label>
									<form:input path="mobilePhone" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.fax" />:</label>
									<form:input path="fax" size="50" maxlength="64"
										disabled="${!profileEditEnabled and !partialProfileEditEnabled}"
										cssClass="form-control" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.language" />:</label>
									<form:select path="localeId"
										disabled="${!profileEditEnabled}" styleClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<form:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.timezone.title" />:</label>
									<form:select path="timeZone" disabled="${!profileEditEnabled}" cssClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<form:option value="${timezoneDto.timeZoneId}">
												${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
											</form:option>
										</c:forEach>
									</form:select>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.tutorial.enabled" />:</label>
									<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
									<form:select path="tutorialsDisabled"
										disabled="${!profileEditEnabled}" csslass="form-control">
										<form:option value="false">
											<fmt:message key="label.yes" />
										</form:option>
										<form:option value="true">
											<fmt:message key="label.no" />
										</form:option>
									</form:select>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.theme" />:</label>
									<form:select path="userTheme" disabled="${!profileEditEnabled}" cssClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<form:option value="${theme.themeId}">${theme.name}</form:option>
										</c:forEach>
									</form:select>
								</div>
								
							</c:if>
							<br />

							<c:if test="${authenticationMethodId != dbId}">
								<form:hidden path="title" />
								<form:hidden path="firstName" />
								<form:hidden path="lastName" />
								<form:hidden path="email" />
								<form:hidden path="addressLine1" />
								<form:hidden path="addressLine2" />
								<form:hidden path="addressLine3" />
								<form:hidden path="city" />
								<form:hidden path="state" />
								<form:hidden path="postcode" />
								<form:hidden path="country" />
								<form:hidden path="dayPhone" />
								<form:hidden path="eveningPhone" />
								<form:hidden path="mobilePhone" />
								<form:hidden path="fax" />

								<div class="form-group">
									<label><fmt:message key="label.username" /></label> <input
										type="text" class="form-control"
										value="${UserForm.login}">
								</div>

								<div class="form-group">
									<label><fmt:message key="label.username" /></label> <input
										type="text" class="form-control"
										value="${UserForm.login}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.title" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.title}"/>
								</div>
								<div class="form-group">
									<label><fmt:message key="label.first_name" /> *:</label> <input
										type="text" class="form-control"
										value="${UserForm.firstName}"/>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.last_name" /> *:</label> <input
										type="text" class="form-control"
										value="${UserForm.lastName}"/>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.email" /> *:</label> <input
										type="text" class="form-control"
										value="${UserForm.email}"/>
								</div>
								<div class="form-group">
									<label><fmt:message key="label.address_line_1" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.addressLine1}"/>
								</div>
								<div class="form-group">
									<label><fmt:message key="label.address_line_2" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.addressLine2}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.address_line_3" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.addressLine3}" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.city" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.city}" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.state" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.state}" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.postcode" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.postcode}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.country" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.country}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.day_phone" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.dayPhone}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.evening_phone" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.eveningPhone}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.mobile_phone" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.mobilePhone}" />
								</div>
								<div class="form-group">
									<label><fmt:message key="label.fax" />:</label> <input
										type="text" class="form-control"
										value="${UserForm.fax}" />
								</div>

								<div class="form-group">
									<label><fmt:message key="label.theme" />:</label>
									<form:select path="userTheme"
										disabled="${!profileEditEnabled}" cssClass="form-control">
										<c:forEach items="${themes}" var="theme">
											<form:option value="${theme.themeId}">${theme.name}</form:option>
										</c:forEach>
									</form:select>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.language" />:</label>
									<form:select path="localeId"
										disabled="${!profileEditEnabled}" cssClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<form:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>

								<div class="form-group">
									<label><fmt:message key="label.timezone.title" />:</label>
									<form:select path="timeZone" disabled="${!profileEditEnabled}" cssClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<form:option value="${timezoneDto.timeZoneId}">
												${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
											</form:option>
										</c:forEach>
									</form:select>
								</div>
								<div class="form-group">
									<label><fmt:message key="label.timezone.title" />:</label>
									<c:set var="timeZone">
										<input type="text" class="form-control"
											value="${UserForm.timeZone}" />
									</c:set>
									${timeZone}
								</div>
								<div class="form-group">
									<label><fmt:message key="label.tutorial.enabled" />:${UserForm.tutorialsDisabled}</label>
									<!-- For users' comfort we write it as a positive sentece - "should the tutorials be enabled"?
			 But for simplicity of coding, we keep it as a negative value - "should the tutorials be disabled?"
			 This is the reason to mix true/false and yes/no answers.
		-->
									<form:select path="tutorialsDisabled"
										disabled="${!profileEditEnabled}" cssClass="form-control">
										<form:option value="false">
											<fmt:message key="label.yes" />
										</form:option>
										<form:option value="true">
											<fmt:message key="label.no" />
										</form:option>
									</form:select>
								</div>

							</c:if>

						</div>
					</div>
				</div>
			</div>
	</form:form>
	<div class="form-group" align="right">
		<a href="<lams:LAMSURL />profile.jsp"   class="btn btn-sm btn-default voffset5" ></a>
			<fmt:message key="button.cancel" />
		&nbsp;&nbsp;
		<c:if test="${profileEditEnabled or partialProfileEditEnabled}">
			<button class="btn btn-sm btn-primary voffset5" type="button"
				name="submit" onclick="submitMessage()"> 
				<fmt:message key="button.save" />
			</button>
		</c:if>
	</div>
	</div>
</body>
</lams:html>




