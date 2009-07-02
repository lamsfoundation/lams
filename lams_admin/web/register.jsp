
<%@ include file="/taglibs.jsp"%>

<script src="<lams:LAMSURL/>/includes/javascript/jquery-latest.pack.js" type="text/javascript"></script>
<script src="<lams:LAMSURL/>/includes/javascript/jquery.validate.pack.js" type="text/javascript"></script>

<script type="text/javascript">
<!--
	
	$().ready(function() {
		// validate signup form on keyup and submit
		$("#RegisterForm").validate({
			rules: {
				siteName: "required",
				organisation: "required",
				name: "required",
				email: {
					required: true,
					email: true
				}
			},
			messages: {
				siteName: '<font color="red"><i><fmt:message key="register.error.field"/></i></font>',
				organisation: '<font color="red"><i><fmt:message key="register.error.field"/></i></font>',
				name: '<font color="red"><i><fmt:message key="register.error.field"/></i></font>',
				email: '<font color="red"><i><fmt:message key="register.error.emailRequiredValid"/></i></font>'
			}
		});
	});
//-->
</script>




<html:form action="/register" method="post" styleId="RegisterForm">
	<html:hidden property="method" value="register" />
	
	<h4 class="align-left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a>
	</h4>
	<h1><fmt:message key="sysadmin.register.server" /></h1>
	<br />
	
	<c:choose>
		<c:when test="${not empty errorKey}">
			<p class="warning">
				<fmt:message key="${errorKey}"/>
			</p>
		</c:when>
		<c:otherwise>
			<c:if test="${not empty successKey}">
				<p class="info">
					<fmt:message key="${successKey}"/>
				</p>
			</c:if>
		</c:otherwise>
	</c:choose>

	<table class="alternative-color" width=100% cellspacing="0">
			<tr>
				<th colspan="2"><fmt:message key="admin.register.heading.title"/></th>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.enableLamsCommunity"/>
				</td>
				<td>
					<html:checkbox property="enableLamsCommunityIntegration" />
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.sitename"/>
				</td>
				<td>
					<html:text property="siteName" size="40"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.orgname"/>
				</td>
				<td>
					<html:text property="organisation" size="40" />
				</td>
			<tr>
				<td>
					<fmt:message key="admin.user.name"/>
				</td>
				<td>
					<html:text property="name" size="40" />
					
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.email"/>
				</td>
				<td>
					<html:text property="email" size="40" />
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.country"/>
				</td>
				<td>
					
					<html:select property="serverCountry">
						<html:option value="AF">Afghanistan</html:option>
						<html:option value="AX">Aland Islands</html:option>
						<html:option value="AL">Albania</html:option>
						<html:option value="DZ">Algeria</html:option>
						<html:option value="AS">American Samoa</html:option>
						<html:option value="AD">Andorra</html:option>
						<html:option value="AO">Angola</html:option>
						<html:option value="AI">Anguilla</html:option>
						<html:option value="AQ">Antarctica</html:option>
						<html:option value="AG">Antigua And Barbuda</html:option>
						<html:option value="AR">Argentina</html:option>
						<html:option value="AM">Armenia</html:option>
						<html:option value="AW">Aruba</html:option>
						<html:option value="AU">Australia</html:option>
						<html:option value="AT">Austria</html:option>
						<html:option value="AZ">Azerbaijan</html:option>
						<html:option value="BS">Bahamas</html:option>
						<html:option value="BH">Bahrain</html:option>
						<html:option value="BD">Bangladesh</html:option>
						<html:option value="BB">Barbados</html:option>
						<html:option value="BY">Belarus</html:option>
						<html:option value="BE">Belgium</html:option>
						<html:option value="BZ">Belize</html:option>
						<html:option value="BJ">Benin</html:option>
						<html:option value="BM">Bermuda</html:option>
						<html:option value="BT">Bhutan</html:option>
						<html:option value="BO">Bolivia</html:option>
						<html:option value="BA">Bosnia And Herzegovina</html:option>
						<html:option value="BW">Botswana</html:option>
						<html:option value="BV">Bouvet Island</html:option>
						<html:option value="BR">Brazil</html:option>
						<html:option value="BN">Brunei Darussalam</html:option>
						<html:option value="BG">Bulgaria</html:option>
						<html:option value="BF">Burkina Faso</html:option>
						<html:option value="BI">Burundi</html:option>
						<html:option value="KH">Cambodia</html:option>
						<html:option value="CM">Cameroon</html:option>
						<html:option value="CA">Canada</html:option>
						<html:option value="CV">Cape Verde</html:option>
						<html:option value="KY">Cayman Islands</html:option>
						<html:option value="CF">Central African Republic</html:option>
						<html:option value="TD">Chad</html:option>
						<html:option value="CL">Chile</html:option>
						<html:option value="CN">China</html:option>
						<html:option value="CX">Christmas Island</html:option>
						<html:option value="CC">Cocos (Keeling) Islands</html:option>
						<html:option value="CO">Colombia</html:option>
						<html:option value="KM">Comoros</html:option>
						<html:option value="CG">Congo</html:option>
						<html:option value="CK">Cook Islands</html:option>
						<html:option value="CR">Costa Rica</html:option>
						<html:option value="CI">Cote DIvoire</html:option>
						<html:option value="HR">Croatia</html:option>
						<html:option value="CU">Cuba</html:option>
						<html:option value="CY">Cyprus</html:option>
						<html:option value="CZ">Czech Republic</html:option>
						<html:option value="DK">Denmark</html:option>
						<html:option value="DJ">Djibouti</html:option>
						<html:option value="DM">Dominica</html:option>
						<html:option value="DO">Dominican Republic</html:option>
						<html:option value="EC">Ecuador</html:option>
						<html:option value="EG">Egypt</html:option>
						<html:option value="SV">El Salvador</html:option>
						<html:option value="GQ">Equatorial Guinea</html:option>
						<html:option value="ER">Eritrea</html:option>
						<html:option value="EE">Estonia</html:option>
						<html:option value="ET">Ethiopia</html:option>
						<html:option value="FO">Faroe Islands</html:option>
						<html:option value="FJ">Fiji</html:option>
						<html:option value="FI">Finland</html:option>
						<html:option value="FR">France</html:option>
						<html:option value="GF">French Guiana</html:option>
						<html:option value="PF">French Polynesia</html:option>
						<html:option value="TF">French Southern Territories</html:option>
						<html:option value="GA">Gabon</html:option>				
						<html:option value="GM">Gambia</html:option>
						<html:option value="GE">Georgia</html:option>
						<html:option value="DE">Germany</html:option>
						<html:option value="GH">Ghana</html:option>
						<html:option value="GI">Gibraltar</html:option>
						<html:option value="GR">Greece</html:option>			
						<html:option value="GL">Greenland</html:option>
						<html:option value="GD">Grenada</html:option>
						<html:option value="GP">Guadeloupe</html:option>
						<html:option value="GU">Guam</html:option>
						<html:option value="GT">Guatemala</html:option>
						<html:option value="GN">Guinea</html:option>	
						<html:option value="GW">Guinea-Bissau</html:option>
						<html:option value="GY">Guyana</html:option>
						<html:option value="HT">Haiti</html:option>
						<html:option value="VA">Holy See (Vatican City State)</html:option>
						<html:option value="HN">Honduras</html:option>
						<html:option value="HK">Hong Kong</html:option>		
						<html:option value="HU">Hungary</html:option>
						<html:option value="IS">Iceland</html:option>
						<html:option value="IN">India</html:option>
						<html:option value="ID">Indonesia</html:option>
						<html:option value="IR">Iran</html:option>
						<html:option value="IQ">Iraq</html:option>
						<html:option value="IE">Ireland</html:option>
						<html:option value="IL">Israel</html:option>
						<html:option value="IT">Italy</html:option>
						<html:option value="JM">Jamaica</html:option>
						<html:option value="JP">Japan</html:option>
						<html:option value="JO">Jordan</html:option>
						<html:option value="KZ">Kazakhstan</html:option>
						<html:option value="KE">Kenya</html:option>
						<html:option value="KI">Kiribati</html:option>
						<html:option value="KP">North Korea</html:option>
						<html:option value="KR">South Korea</html:option>
						<html:option value="KW">Kuwait</html:option>			
						<html:option value="KG">Kyrgyzstan</html:option>
						<html:option value="LA">Lao Peoples Democratic Republic</html:option>
						<html:option value="LV">Latvia</html:option>
						<html:option value="LB">Lebanon</html:option>
						<html:option value="LS">Lesotho</html:option>
						<html:option value="LR">Liberia</html:option>				
						<html:option value="LY">Libyan Arab Jamahiriya</html:option>
						<html:option value="LI">Liechtenstein</html:option>
						<html:option value="LT">Lithuania</html:option>
						<html:option value="LU">Luxembourg</html:option>
						<html:option value="MO">Macao</html:option>
						<html:option value="MK">Macedonia, FYRO</html:option>						
						<html:option value="MG">Madagascar</html:option>
						<html:option value="MW">Malawi</html:option>
						<html:option value="MY">Malaysia</html:option>
						<html:option value="MV">Maldives</html:option>
						<html:option value="ML">Mali</html:option>
						<html:option value="MT">Malta</html:option>						
						<html:option value="MH">Marshall Islands</html:option>
						<html:option value="MQ">Martinique</html:option>
						<html:option value="MR">Mauritania</html:option>
						<html:option value="MU">Mauritius</html:option>
						<html:option value="YT">Mayotte</html:option>
						<html:option value="MX">Mexico</html:option>						
						<html:option value="FM">Micronesia</html:option>
						<html:option value="MD">Moldova</html:option>
						<html:option value="MC">Monaco</html:option>
						<html:option value="MN">Mongolia</html:option>
						<html:option value="MS">Montserrat</html:option>
						<html:option value="MA">Morocco</html:option>						
						<html:option value="MZ">Mozambique</html:option>
						<html:option value="MM">Myanmar</html:option>
						<html:option value="NA">Namibia</html:option>
						<html:option value="NR">Nauru</html:option>
						<html:option value="NP">Nepal</html:option>
						<html:option value="NL">Netherlands</html:option>						
						<html:option value="AN">Netherlands Antilles</html:option>
						<html:option value="NC">New Caledonia</html:option>
						<html:option value="NZ">New Zealand</html:option>
						<html:option value="NI">Nicaragua</html:option>
						<html:option value="NE">Niger</html:option>
						<html:option value="NG">Nigeria</html:option>						
						<html:option value="NU">Niue</html:option>
						<html:option value="NF">Norfolk Island</html:option>
						<html:option value="NO">Norway</html:option>
						<html:option value="OM">Oman</html:option>
						<html:option value="PK">Pakistan</html:option>
						<html:option value="PW">Palau</html:option>						
						<html:option value="PS">Palestinian Territory</html:option>
						<html:option value="PA">Panama</html:option>
						<html:option value="PG">Papua New Guinea</html:option>
						<html:option value="PY">Paraguay</html:option>
						<html:option value="PE">Peru</html:option>
						<html:option value="PH">Philippines</html:option>						
						<html:option value="PN">Pitcairn</html:option>
						<html:option value="PL">Poland</html:option>
						<html:option value="PT">Portugal</html:option>
						<html:option value="PR">Puerto Rico</html:option>
						<html:option value="QA">Qatar</html:option>
						<html:option value="RE">Reunion</html:option>						
						<html:option value="RO">Romania</html:option>
						<html:option value="RU">Russia</html:option>
						<html:option value="RW">Rwanda</html:option>
						<html:option value="SH">Saint Helena</html:option>
						<html:option value="KN">Saint Kitts And Nevis</html:option>
						<html:option value="LC">Saint Lucia</html:option>						
						<html:option value="PM">Saint Pierre And Miquelon</html:option>
						<html:option value="VC">Saint Vincent And The Grenadines</html:option>
						<html:option value="WS">Samoa</html:option>
						<html:option value="SM">San Marino</html:option>
						<html:option value="ST">Sao Tome And Principe</html:option>
						<html:option value="SA">Saudi Arabia</html:option>					
						<html:option value="SN">Senegal</html:option>
						<html:option value="CS">Serbia And Montenegro</html:option>
						<html:option value="SC">Seychelles</html:option>
						<html:option value="SL">Sierra Leone</html:option>
						<html:option value="SG">Singapore</html:option>
						<html:option value="SK">Slovakia</html:option>					
						<html:option value="SI">Slovenia</html:option>
						<html:option value="SB">Solomon Islands</html:option>
						<html:option value="SO">Somalia</html:option>
						<html:option value="ZA">South Africa</html:option>
						<html:option value="ES">Spain</html:option>
						<html:option value="LK">Sri Lanka</html:option>					
						<html:option value="SD">Sudan</html:option>
						<html:option value="SR">Suriname</html:option>
						<html:option value="SZ">Swaziland</html:option>
						<html:option value="SE">Sweden</html:option>
						<html:option value="CH">Switzerland</html:option>
						<html:option value="SY">Syria</html:option>						
						<html:option value="TW">Taiwan</html:option>
						<html:option value="TJ">Tajikistan</html:option>
						<html:option value="TZ">Tanzania</html:option>
						<html:option value="TH">Thailand</html:option>
						<html:option value="TL">Timor-Leste</html:option>
						<html:option value="TG">Togo</html:option>					
						<html:option value="TK">Tokelau</html:option>
						<html:option value="TO">Tonga</html:option>
						<html:option value="TT">Trinidad And Tobago</html:option>
						<html:option value="TN">Tunisia</html:option>
						<html:option value="TR">Turkey</html:option>
						<html:option value="TM">Turkmenistan</html:option>					
						<html:option value="TV">Tuvalu</html:option>
						<html:option value="UG">Uganda</html:option>
						<html:option value="UA">Ukraine</html:option>
						<html:option value="AE">United Arab Emirates</html:option>
						<html:option value="GB">United Kingdom</html:option>
						<html:option value="US">United States</html:option>					
						<html:option value="UY">Uruguay</html:option>
						<html:option value="UZ">Uzbekistan</html:option>
						<html:option value="VU">Vanuatu</html:option>
						<html:option value="VE">Venezuela</html:option>
						<html:option value="VN">Viet Nam</html:option>
						<html:option value="EH">Western Sahara</html:option>					
						<html:option value="YE">Yemen</html:option>
						<html:option value="ZM">Zambia</html:option>
						<html:option value="ZW">Zimbabwe</html:option>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.directory"/>
				</td>
				<td>
					<html:checkbox property="publicDirectory" />
				</td>
			</tr>
			<tr>
				<th colspan="2">
					<fmt:message key="admin.register.server.config.title"/>
				</th>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.url"/>
				</td>
				<td>
					${registerDTO.serverUrl}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.version"/>
				</td>
				<td>
					${registerDTO.serverVersion}				
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.build"/>
				</td>
				<td>
					${registerDTO.serverBuild}	
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.locale"/>
				</td>
				<td>
					${registerDTO.serverLocale}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.langdate"/>
				</td>
				<td>
					${registerDTO.serverLanguageDate}
				</td>
			</tr>
			<tr>
				<th colspan="2">
					<fmt:message key="admin.register.server.stats.title"/>
				</th>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.course"/>
				</td>
				<td>
					${registerDTO.groupNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.class"/>
				</td>
				<td>
					${registerDTO.subgroupNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.SYSADMIN"/>
				</td>
				<td>
					${registerDTO.sysadminNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.GROUP.ADMIN"/>
				</td>
				<td>
					${registerDTO.adminNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.GROUP.MANAGER"/>
				</td>
				<td>
					${registerDTO.managerNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.AUTHOR"/>
				</td>
				<td>
					${registerDTO.authorNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.MONITOR"/> 
				</td>
				<td>
					${registerDTO.monitorNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.LEARNER"/>
				</td>
				<td>
					${registerDTO.learnerNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.AUTHOR.ADMIN"/>
				</td>
				<td>
					${registerDTO.authorAdminNumber}
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.stats.total.users"/>
				</td>
				<td>
					${registerDTO.userNumber}
				</td>
			</tr>
	</table>
	
	<p align="center">
		<input class="button" type="submit" value="<fmt:message key="admin.register" />"/>
		<html:reset styleClass="button"><fmt:message key="admin.reset" /></html:reset>
        <input class="button" type="button" value="<fmt:message key="admin.cancel" />" onClick="javascript:window.location.href='<lams:LAMSURL/>/admin/sysadminstart.do'"/>
	</p>
</html:form>

