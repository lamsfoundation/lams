<!DOCTYPE html>

<%@ include file="/taglibs.jsp"%>
<%@ page import="org.lamsfoundation.lams.util.Configuration"%>
<%@ page import="org.lamsfoundation.lams.util.ConfigurationKeys"%>
<c:set var="minNumChars"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_MINIMUM_CHARACTERS)%></c:set>
<c:set var="mustHaveUppercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_UPPERCASE)%></c:set>
<c:set var="mustHaveLowercase"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_LOWERCASE)%></c:set>
<c:set var="mustHaveNumerics"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_NUMERICS)%></c:set>
<c:set var="mustHaveSymbols"><%=Configuration.get(ConfigurationKeys.PASSWORD_POLICY_SYMBOLS)%></c:set>

<lams:html>
<lams:head>
	<c:set var="title"><fmt:message key="admin.user.entry"/></c:set>
	<title>${title}</title>
	<link rel="shortcut icon" href="<lams:LAMSURL/>/favicon.ico" type="image/x-icon" />

	<lams:css/>
	<link rel="stylesheet" href="<lams:LAMSURL/>admin/css/admin.css" type="text/css" media="screen">
	<link rel="stylesheet" href="<lams:LAMSURL/>css/jquery-ui-bootstrap-theme.css" type="text/css" media="screen">
	
	<script type="text/javascript" src="/lams/includes/javascript/jquery.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery-ui.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/jquery.validate.js"></script>
	<script type="text/javascript" src="/lams/includes/javascript/portrait.js"></script>
	<script type="text/javascript">
		var mustHaveUppercase = ${mustHaveUppercase},
		mustHaveNumerics  = ${mustHaveNumerics},
		mustHaveLowercase  = ${mustHaveLowercase},
		mustHaveSymbols   = ${mustHaveSymbols};
	
		$.validator.addMethod("pwcheck", function(value) {
		 return (!mustHaveUppercase || /[A-Z]/.test(value)) && // has uppercase letters 
		(!mustHaveNumerics || /\d/.test(value)) && // has a digit
		(!mustHaveLowercase || /[a-z]/.test(value)) && // has a lower case
		(!mustHaveSymbols || /[`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]/.test(value)); //has symbols
		});
	
		$.validator.addMethod("charactersAllowed", function(value) {
			return /^[A-Za-z0-9\d`~!@#$%^&*\(\)_\-+={}\[\]\\|:\;\"\'\<\>,.?\/]*$/
					.test(value)
		});
	
		$.validator.addMethod("notEqualTo", function(value, element, param) {
			return this.optional(element) || value != param;
		}, "Please specify a different (non-default) value");

		$(function() {
			// Setup form validation 
			$("#userForm").validate({
								errorClass : 'help-block',
								//  validation rules
								rules : {
									login : {
										required: true,
										maxlength : 50
									},
									password : {
										required: true,
										minlength : <c:out value="${minNumChars}"/>,
										maxlength : 25,
										charactersAllowed : true,
										pwcheck : true
									},
									password2 : {
										equalTo : "#password"
									},
									firstName : {
										required : true
									},
									lastName : {
										required : true
									},
									email : {
										required: true,
										email: true
									},
									country : {
										required: true,
										notEqualTo: "0"
									}
								},
	
								// Specify the validation error messages
								messages : {
									login : {
										required: "<fmt:message key='error.login.required'/>"
									},
									password : {
										required : "<fmt:message key='error.password.empty'/>",
										minlength : "<fmt:message key='label.password.min.length'><fmt:param value='${minNumChars}'/></fmt:message>",
										maxlength : "<fmt:message key='label.password.max.length'/>",
										charactersAllowed : "<fmt:message key='label.password.symbols.allowed'/> ` ~ ! @ # $ % ^ & * ( ) _ - + = { } [ ] \ | : ; \" ' < > , . ? /",
										pwcheck : "<fmt:message key='label.password.restrictions'/>"
									},
									password2: {
										equalTo : "<fmt:message key='error.password.mismatch'/>"
									},
									firstName: {
										required: "<fmt:message key='error.firstname.required'/>"
									},
									lastName: {
										required: "<fmt:message key='error.lastname.required'/>"
									},
									email: {
										required: "<fmt:message key='error.email.required'/>",
										email: "<fmt:message key='error.valid.email.required'/>"
									},
									country: {
										required: "<fmt:message key='error.country.required'/>",
										notEqualTo: "<fmt:message key='error.country.required'/>"
									}
								},
	
								submitHandler : function(form) {
									form.submit();
								}
			});
	
		});
		
		<c:if test="${not empty userForm.userId}">
		$(document).ready(function(){
			var portraitId = '<c:out value="${userForm.initialPortraitId}" />';
			loadPortrait(portraitId);
		});
	
		function loadPortrait(portraitId) {
			$("#portraitPicture").removeClass();
			$("#portraitPicture").css('background-image','');
			addPortrait( $("#portraitPicture"), portraitId, 
					'<c:out value="${userForm.userId}" />', 'large', true, '/lams/' );
			<c:if test="${isSysadmin}">
			if ( portraitId.length > 0 )  {
				$("#portraitButton").css('display','block');
			} else {
				$("#portraitButton").css('display','none');
			}
			</c:if>
		}
		
		<c:if test="${isSysadmin}">
			function deletePortrait() {
				$("#portraitButton").css('display','none');
				
				$.ajax({
					url : '/lams/saveportrait/deletePortrait.do',
					data : { 	
						'userId': '<c:out value="${userForm.userId}" />' ,
					  	"<csrf:tokenname/>": "<csrf:tokenvalue/>"
					},
					type : 'POST',		
					success : function(response) {
						if ( response == 'deleted') {
							loadPortrait('');
						} else {
							alert("<fmt:message key='error.portrait.removal.failed'/>");
						}
					}
				});
			}
		</c:if>
		</c:if>
	</script>
</lams:head>
    
<body class="stripes">
	<c:set var="title">${title}: <fmt:message key="admin.user.edit"/></c:set>
	<lams:Page type="admin" title="${title}" formID="userForm">
	<form:form id="userForm" action="../usersave/saveUserDetails.do" modelAttribute="userForm" method="post">
				<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"/>
				<form:hidden path="orgId" />
				<form:hidden path="userId" />

				<c:if test="${not empty userForm.orgId}">
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
					<c:if test="${not empty pOrgId}">
						: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${pOrgId}" />" class="btn btn-default">
							<c:out value="${parentName}" />
						  </a>
						: <a href="<lams:LAMSURL/>admin/usermanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
					<c:if test="${empty pOrgId}">
						: <a href="<lams:LAMSURL/>admin/orgmanage.do?org=<c:out value="${userForm.orgId}" />" class="btn btn-default">
							<c:out value="${orgName}" />
						  </a>
					</c:if>
				</c:if>
			
				<c:if test="${empty userForm.orgId}">
					<a href="<lams:LAMSURL/>admin/orgmanage.do?org=1" class="btn btn-default">
						<fmt:message key="admin.course.manage" />
					</a>
				</c:if>
			
				<div class="panel panel-default voffset5">
					<div class="panel-heading">
						<span class="panel-title"> <c:if test="${not empty userForm.userId}">
								<fmt:message key="admin.user.edit" />
							</c:if> <c:if test="${empty userForm.userId}">
								<fmt:message key="admin.user.create" />
							</c:if>
						</span>
					</div>
			
					<div class="panel-body">
					    <div class="row">
						<div class="col-md-12">
						
							<c:if test="${empty userForm.userId}">
							<lams:Alert type="info" id="passwordConditions" close="false">
							<fmt:message key='label.password.must.contain' />:
								<ul class="list-unstyled" style="line-height: 1.2">
									<li><span class="fa fa-check"></span> <fmt:message
											key='label.password.min.length'>
											<fmt:param value='${minNumChars}' />
										</fmt:message></li>
				
									<c:if test="${mustHaveUppercase}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.ucase' /></li>
									</c:if>
									<c:if test="${mustHaveLowercase}">
												<li><span class="fa fa-check"></span> <fmt:message
														key='label.password.must.lcase' /></li>
											</c:if>
				
									<c:if test="${mustHaveNumerics}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.number' /></li>
									</c:if>
				
				
									<c:if test="${mustHaveSymbols}">
										<li><span class="fa fa-check"></span> <fmt:message
												key='label.password.must.symbol' /></li>
									</c:if>
								</ul>
							</lams:Alert>
						</c:if>
					</div>
					</div>
					
					<!--  Main panel. Do not show portrait area for new user. -->
					<c:if test="${not empty userForm.userId}">
				    <div class="row">
					<div class="col-md-3">
			    			<div class="text-center"><div id="portraitPicture" ></div></div>
						<c:if test="${isSysadmin}">
			    			<div id="portraitButton" class="text-center voffset10" style="display:none; margin-bottom: 5px;">
			    			<a href="#" onclick="javascript:deletePortrait();" class="btn btn-primary btn-sm"><fmt:message key="label.delete.portrait" /></a></div>
			    			</c:if>
			    		</div>
					<div class="col-md-9">
					</c:if>
					<c:if test="${empty userForm.userId}">
				    <div class="row">
					<div class="col-md-12">
					</c:if>
					
						<table class="table table-condensed table-no-border">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.login" />
									*:</td>
								<td><lams:errors path="login"/>
								<form:input id="login" path="login"  maxlength="50"
										cssClass="form-control"/></td>
							</tr>
							<c:if test="${empty userForm.userId}">
							<tr>
								<td class="align-right"><fmt:message key="admin.user.password" />
									*:</td>
								<td><lams:errors path="password"/>
									<form:input type="password" path="password" 
										maxlength="25" id="password" cssClass="form-control" /></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.password.confirm" /> *:</td>
								<td><form:input type="password" path="password2" 
										maxlength="25" id="password2" cssClass="form-control" /></td>
							</tr>
							</c:if>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.authentication.method" />:</td>
								<td><form:select path="authenticationMethodId"
										cssClass="form-control">
										<c:forEach items="${authenticationMethods}" var="method">
											<form:option value="${method.authenticationMethodId}">
												<c:out value="${method.authenticationMethodName}" />
											</form:option>
										</c:forEach>
									</form:select></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.title" />:</td>
								<td><form:input type="text" path="title" size="32" maxlength="32"
										id="title" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.first_name" /> *:</td>
								<td><lams:errors path="firstName"/>
									<form:input path="firstName" 
										id="firstName" maxlength="128" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.last_name" /> *:</td>
								<td><lams:errors path="lastName"/>
									<form:input path="lastName" 
										id="lastName" maxlength="128" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.email" />
									*:</td>
								<td><lams:errors path="email"/>
									<form:input path="email" maxlength="128"
										cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_1" />:</td>
								<td><form:input path="addressLine1" 
										maxlength="64" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_2" />:</td>
								<td><form:input path="addressLine2" 
										maxlength="64" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message
										key="admin.user.address_line_3" />:</td>
								<td><form:input path="addressLine3" 
										maxlength="64" cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right"><fmt:message key="admin.user.city" />:</td>
								<td><form:input path="city"  maxlength="64"
										cssClass="form-control"/></td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.postcode" />:
								</td>
								<td>
									<form:input path="postcode" size="10" maxlength="10" cssClass="form-control"/>
								</td>
							</tr>
							<tr>
								<td class="align-right">	
									<fmt:message key="admin.user.state" />:
								</td>
								<td><form:input path="state"  maxlength="64"
										cssClass="form-control" />
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.country" />:
								</td>
								<td>
									<form:select path="country" cssClass="form-control">
										<form:option value="0"><fmt:message key="label.select.country" /></form:option>
										<c:forEach items="${countryCodes}" var="countryCode">
											<form:option value="${countryCode.key}">
												${countryCode.value}
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.day_phone" />:
								</td>
								<td>
									<form:input path="dayPhone"  maxlength="64" cssClass="form-control"/>
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.evening_phone" />:
								</td>
								<td>
									<form:input path="eveningPhone" maxlength="64" cssClass="form-control"/>		
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.mobile_phone" />:
								</td>
								<td>
									<form:input path="mobilePhone" maxlength="64" cssClass="form-control"/>
								</td>
							</tr>
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.fax" />:
								</td>
								<td>
									<form:input path="fax"  maxlength="64" cssClass="form-control"/>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.organisation.locale" />:
								</td>
								<td>
									<form:select path="localeId" cssClass="form-control">
										<c:forEach items="${locales}" var="locale">
											<form:option value="${locale.localeId}">
												<c:out value="${locale.description}" />
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.time.zone" />:
								</td>
								<td>
									<form:select path="timeZone" cssClass="form-control">
										<c:forEach items="${timezoneDtos}" var="timezoneDto">
											<form:option value="${timezoneDto.timeZoneId}">
												${timezoneDto.timeZoneId} - ${timezoneDto.displayName}
											</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
			
							<tr>
								<td class="align-right">
									<fmt:message key="label.theme" />:
								</td>
								<td>
									<form:select path="userTheme" class="form-control">
										<c:forEach items="${themes}" var="theme">
											<form:option value="${theme.themeId}">${theme.name}</form:option>
										</c:forEach>
									</form:select>
								</td>
							</tr>
							
							<tr>
								<td class="align-right">
									<fmt:message key="admin.user.change.password" />:
								</td>
								<td>
									<form:checkbox path="changePassword" value="true" id="changePassword"  />
								</td>
							</tr>
							
							<c:if test="${isSysadmin}">
								<tr>
									<td class="align-right">
										<fmt:message key="label.2FA.property.enable" />:
									</td>
									<td>
										<form:checkbox path="twoFactorAuthenticationEnabled" value="true"  />
									</td>
								</tr>
							</c:if>
							
							<c:if test="${not empty userForm.createDate}">
								<tr>
									<td class="align-right">
										<fmt:message key="admin.user.create.date" />:
									</td>
									<td>
										<lams:Date value="${userForm.createDate}"/>
									</td>
								</tr>
							</c:if>
				
				
						</table>
					</div>
					</div>
					
					<div class="row">
					<div class="col-md-12">
						<c:if test="${not empty userForm.userId}">
							<div class="pull-left">
							<a href="<lams:LAMSURL/>admin/userChangePass.jsp?userId=${userForm.userId}&login=${userForm.login}" class="btn btn-primary"><fmt:message key="admin.user.changePassword" /></a>
							</div>
						</c:if>
						
						<div class="pull-right">
							<a href="<lams:LAMSURL/>admin/usersearch.do" class="btn btn-default"> <fmt:message key="admin.cancel" /> </a>
							<input type="submit" id="saveButton" class="btn btn-primary loffset5" value="<fmt:message key="admin.save" />" />
						</div>
					</div>
					</div>
					
					</div>
				</div> <!-- End of panel -->
			
			
				<c:if test="${not empty globalRoles || not empty userOrgRoles}">
			
					<div class="panel panel-default voffset5">
						<div class="panel-heading">
							<span class="panel-title"> <fmt:message
									key="admin.organisation" />
							</span>
						</div>
			
						<table class="table table-no-border">
			
							<c:if test="${not empty globalRoles}">
								<tr>
									<td>
										<table class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.global.roles" />:</th>
											</tr>
											<tr>
												<td>
													<c:forEach var="role" items="${globalRoles.roles}">
														<fmt:message>role.<lams:role role="${role}" />
														</fmt:message>&nbsp;
													</c:forEach>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</c:if>
			
							<c:if test="${not empty userOrgRoles}">
								<tr>
									<td>
										<table id="tableRoles" class="table table-striped table-condensed">
											<tr>
												<th><fmt:message key="label.member.of" />:</th>
												<th><fmt:message key="label.with.roles" />:</th>
											</tr>
											
											<c:forEach var="userOrgRole" items="${userOrgRoles}">
												<tr>
													<td><c:out value="${userOrgRole.orgName}" /></td>
													<td><c:forEach var="role" items="${userOrgRole.roles}">
															<fmt:message>role.<lams:role role="${role}" />
															</fmt:message>&nbsp;
														</c:forEach>
													</td>
												</tr>
												
												<c:if test="${not empty userOrgRole.childDTOs}">
													<c:forEach var="child" items="${userOrgRole.childDTOs}">
														<tr>
															<td><i class="fa fa-square"></i>&nbsp;<c:out
																	value="${child.orgName}" /></td>
															<td><c:forEach var="role" items="${child.roles}">
																	<fmt:message>role.<lams:role role="${role}" />
																	</fmt:message>&nbsp;
																</c:forEach>
															</td>
														</tr>
													</c:forEach>
												</c:if>
											</c:forEach>
											
										</table>
									</td>
								</tr>
							</c:if>
			
						</table>
					</div>
				</c:if>
			
			</form:form>

	</lams:Page>
</body>
</lams:html>
