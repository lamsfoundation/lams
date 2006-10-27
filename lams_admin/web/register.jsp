<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ include file="/taglibs.jsp"%>

<form action="http://lamscommunity.org/registration" method="get">
	<h2 align="left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :  
		<fmt:message key="sysadmin.register.server" />
	</h2>
	<br/>
	
	<table class="alternative-color" width=100%>
			<tr>
				<th colspan="2"><fmt:message key="admin.register.heading.title"/></th>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.sitename"/>
				</td>
				<td>
					<input type="text" property="sitename" name="sitename" size="40" value=""/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.orgname"/>
				</td>
				<td>
					<input type="text" name="organisation" size="40" value=""/>
				</td>
			<tr>
				<td>
					<fmt:message key="admin.user.name"/>
				</td>
				<td>
					<input type="text" name="rname" size="40" value="<bean:write name="RegisterForm" property="rname"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.email"/>
				</td>
				<td>
					<input type="text" name="remail" size="40" value="<bean:write name="RegisterForm" property="remail"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.country"/>
				</td>
				<td>
					<select name="servercountry"><option value="AF">Afghanistan</option>
						 <option value="AX">Aland Islands</option>

						 <option value="AL">Albania</option>
						
						 <option value="DZ">Algeria</option>
						 <option value="AS">American Samoa</option>
						 <option value="AD">Andorra</option>
						 <option value="AO">Angola</option>
						 <option value="AI">Anguilla</option>

						 <option value="AQ">Antarctica</option>
						
						 <option value="AG">Antigua And Barbuda</option>
						 <option value="AR">Argentina</option>
						 <option value="AM">Armenia</option>
						 <option value="AW">Aruba</option>
						 <option value="AU">Australia</option>

						 <option value="AT">Austria</option>
						
						 <option value="AZ">Azerbaijan</option>
						 <option value="BS">Bahamas</option>
						 <option value="BH">Bahrain</option>
						 <option value="BD">Bangladesh</option>
						 <option value="BB">Barbados</option>

						 <option value="BY">Belarus</option>
						
						 <option value="BE">Belgium</option>
						 <option value="BZ">Belize</option>
						 <option value="BJ">Benin</option>
						 <option value="BM">Bermuda</option>
						 <option value="BT">Bhutan</option>

						 <option value="BO">Bolivia</option>
						
						 <option value="BA">Bosnia And Herzegovina</option>
						 <option value="BW">Botswana</option>
						 <option value="BV">Bouvet Island</option>
						 <option value="BR">Brazil</option>
						 <option value="BN">Brunei Darussalam</option>

						 <option value="BG">Bulgaria</option>
						
						 <option value="BF">Burkina Faso</option>
						 <option value="BI">Burundi</option>
						 <option value="KH">Cambodia</option>
						 <option value="CM">Cameroon</option>
						 <option value="CA">Canada</option>

						 <option value="CV">Cape Verde</option>
						
						 <option value="KY">Cayman Islands</option>
						 <option value="CF">Central African Republic</option>
						 <option value="TD">Chad</option>
						 <option value="CL">Chile</option>
						 <option value="CN">China</option>

						 <option value="CX">Christmas Island</option>
						
						 <option value="CC">Cocos (Keeling) Islands</option>
						 <option value="CO">Colombia</option>
						 <option value="KM">Comoros</option>
						 <option value="CG">Congo</option>
						 <option value="CK">Cook Islands</option>

						 <option value="CR">Costa Rica</option>
						
						 <option value="CI">Cote DIvoire</option>
						 <option value="HR">Croatia</option>
						 <option value="CU">Cuba</option>
						 <option value="CY">Cyprus</option>
						 <option value="CZ">Czech Republic</option>

						 <option value="DK">Denmark</option>
						
						 <option value="DJ">Djibouti</option>
						 <option value="DM">Dominica</option>
						 <option value="DO">Dominican Republic</option>
						 <option value="EC">Ecuador</option>
						 <option value="EG">Egypt</option>

						 <option value="SV">El Salvador</option>
						
						 <option value="GQ">Equatorial Guinea</option>
						 <option value="ER">Eritrea</option>
						 <option value="EE">Estonia</option>
						 <option value="ET">Ethiopia</option>
						 <option value="FO">Faroe Islands</option>

						 <option value="FJ">Fiji</option>
						
						 <option value="FI">Finland</option>
						 <option value="FR">France</option>
						 <option value="GF">French Guiana</option>
						 <option value="PF">French Polynesia</option>
						 <option value="TF">French Southern Territories</option>

						 <option value="GA">Gabon</option>
						
						 <option value="GM">Gambia</option>
						 <option value="GE">Georgia</option>
						 <option value="DE">Germany</option>
						 <option value="GH">Ghana</option>
						 <option value="GI">Gibraltar</option>

						 <option value="GR">Greece</option>
						
						 <option value="GL">Greenland</option>
						 <option value="GD">Grenada</option>
						 <option value="GP">Guadeloupe</option>
						 <option value="GU">Guam</option>
						 <option value="GT">Guatemala</option>

						 <option value="GN">Guinea</option>
						
						 <option value="GW">Guinea-Bissau</option>
						 <option value="GY">Guyana</option>
						 <option value="HT">Haiti</option>
						 <option value="VA">Holy See (Vatican City State)</option>
						 <option value="HN">Honduras</option>

						 <option value="HK">Hong Kong</option>
						
						 <option value="HU">Hungary</option>
						 <option value="IS">Iceland</option>
						 <option value="IN">India</option>
						 <option value="ID">Indonesia</option>
						 <option value="IR">Iran</option>

						 <option value="IQ">Iraq</option>
						
						 <option value="IE">Ireland</option>
						 <option value="IL">Israel</option>
						 <option value="IT">Italy</option>
						 <option value="JM">Jamaica</option>
						 <option value="JP">Japan</option>

						 <option value="JO">Jordan</option>
						
						 <option value="KZ">Kazakhstan</option>
						 <option value="KE">Kenya</option>
						 <option value="KI">Kiribati</option>
						 <option value="KP">North Korea</option>
						 <option value="KR">South Korea</option>

						 <option value="KW">Kuwait</option>
						
						 <option value="KG">Kyrgyzstan</option>
						 <option value="LA">Lao Peoples Democratic Republic</option>
						 <option value="LV">Latvia</option>
						 <option value="LB">Lebanon</option>
						 <option value="LS">Lesotho</option>

						 <option value="LR">Liberia</option>
						
						 <option value="LY">Libyan Arab Jamahiriya</option>
						 <option value="LI">Liechtenstein</option>
						 <option value="LT">Lithuania</option>
						 <option value="LU">Luxembourg</option>
						 <option value="MO">Macao</option>

						 <option value="MK">Macedonia, FYRO</option>
						
						 <option value="MG">Madagascar</option>
						 <option value="MW">Malawi</option>
						 <option value="MY">Malaysia</option>
						 <option value="MV">Maldives</option>
						 <option value="ML">Mali</option>

						 <option value="MT">Malta</option>
						
						 <option value="MH">Marshall Islands</option>
						 <option value="MQ">Martinique</option>
						 <option value="MR">Mauritania</option>
						 <option value="MU">Mauritius</option>
						 <option value="YT">Mayotte</option>

						 <option value="MX">Mexico</option>
						
						 <option value="FM">Micronesia</option>
						 <option value="MD">Moldova</option>
						 <option value="MC">Monaco</option>
						 <option value="MN">Mongolia</option>
						 <option value="MS">Montserrat</option>

						 <option value="MA">Morocco</option>
						
						 <option value="MZ">Mozambique</option>
						 <option value="MM">Myanmar</option>
						 <option value="NA">Namibia</option>
						 <option value="NR">Nauru</option>
						 <option value="NP">Nepal</option>

						 <option value="NL">Netherlands</option>
						
						 <option value="AN">Netherlands Antilles</option>
						 <option value="NC">New Caledonia</option>
						 <option value="NZ">New Zealand</option>
						 <option value="NI">Nicaragua</option>
						 <option value="NE">Niger</option>

						 <option value="NG">Nigeria</option>
						
						 <option value="NU">Niue</option>
						 <option value="NF">Norfolk Island</option>
						 <option value="NO">Norway</option>
						 <option value="OM">Oman</option>
						 <option value="PK">Pakistan</option>

						 <option value="PW">Palau</option>
						
						 <option value="PS">Palestinian Territory</option>
						 <option value="PA">Panama</option>
						 <option value="PG">Papua New Guinea</option>
						 <option value="PY">Paraguay</option>
						 <option value="PE">Peru</option>

						 <option value="PH">Philippines</option>
						
						 <option value="PN">Pitcairn</option>
						 <option value="PL">Poland</option>
						 <option value="PT">Portugal</option>
						 <option value="PR">Puerto Rico</option>
						 <option value="QA">Qatar</option>

						 <option value="RE">Reunion</option>
						
						 <option value="RO">Romania</option>
						 <option value="RU">Russia</option>
						 <option value="RW">Rwanda</option>
						 <option value="SH">Saint Helena</option>
						 <option value="KN">Saint Kitts And Nevis</option>

						 <option value="LC">Saint Lucia</option>
						
						 <option value="PM">Saint Pierre And Miquelon</option>
						 <option value="VC">Saint Vincent And The Grenadines</option>
						 <option value="WS">Samoa</option>
						 <option value="SM">San Marino</option>
						 <option value="ST">Sao Tome And Principe</option>

						 <option value="SA">Saudi Arabia</option>
						
						 <option value="SN">Senegal</option>
						 <option value="CS">Serbia And Montenegro</option>
						 <option value="SC">Seychelles</option>
						 <option value="SL">Sierra Leone</option>
						 <option value="SG">Singapore</option>

						 <option value="SK">Slovakia</option>
						
						 <option value="SI">Slovenia</option>
						 <option value="SB">Solomon Islands</option>
						 <option value="SO">Somalia</option>
						 <option value="ZA">South Africa</option>
						 <option value="ES">Spain</option>

						 <option value="LK">Sri Lanka</option>
						
						 <option value="SD">Sudan</option>
						 <option value="SR">Suriname</option>
						 <option value="SZ">Swaziland</option>
						 <option value="SE">Sweden</option>
						 <option value="CH">Switzerland</option>

						 <option value="SY">Syria</option>
						
						 <option value="TW">Taiwan</option>
						 <option value="TJ">Tajikistan</option>
						 <option value="TZ">Tanzania</option>
						 <option value="TH">Thailand</option>
						 <option value="TL">Timor-Leste</option>

						 <option value="TG">Togo</option>
						
						 <option value="TK">Tokelau</option>
						 <option value="TO">Tonga</option>
						 <option value="TT">Trinidad And Tobago</option>
						 <option value="TN">Tunisia</option>
						 <option value="TR">Turkey</option>

						 <option value="TM">Turkmenistan</option>
						
						 <option value="TV">Tuvalu</option>
						 <option value="UG">Uganda</option>
						 <option value="UA">Ukraine</option>
						 <option value="AE">United Arab Emirates</option>
						 <option value="GB">United Kingdom</option>

						 <option value="US">United States</option>
						
						 <option value="UY">Uruguay</option>
						 <option value="UZ">Uzbekistan</option>
						 <option value="VU">Vanuatu</option>
						 <option value="VE">Venezuela</option>
						 <option value="VN">Viet Nam</option>

						 <option value="EH">Western Sahara</option>
						
						 <option value="YE">Yemen</option>
						 <option value="ZM">Zambia</option>
						 <option value="ZW">Zimbabwe</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.directory"/>
				</td>
				<td>
					<input type="radio" name="public" value="true" checked="checked">&nbsp;<fmt:message key="admin.register.directory.public"/>&nbsp;&nbsp;
					<input type="radio" name="public" value="false">&nbsp;<fmt:message key="admin.register.directory.private"/>
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
					<bean:write name="RegisterForm" property="serverurl"/>
					<input type="hidden" name="serverurl" value="<bean:write name="RegisterForm" property="serverurl"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.version"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="serverversion"/>
					<input type="hidden" name="serverversion" value="<bean:write name="RegisterForm" property="serverversion"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.build"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="serverbuild"/>
					<input type="hidden" name="serverbuild" value="<bean:write name="RegisterForm" property="serverbuild"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.locale"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="serverlocale"/>
					<input type="hidden" name="serverlocale" value="<bean:write name="RegisterForm" property="serverlocale"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.server.config.langdate"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="langdate"/>
					<input type="hidden" name="langdate" value="<bean:write name="RegisterForm" property="langdate"/>"/>
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
					<bean:write name="RegisterForm" property="groupno"/>
					<input type="hidden" name="groupno" value="<bean:write name="RegisterForm" property="groupno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.class"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="subgroupno"/>
					<input type="hidden" name="subgroupno" value="<bean:write name="RegisterForm" property="subgroupno"/>"/>
				</td>
			<tr>
			<tr>
				<td>
					<fmt:message key="role.SYSADMIN"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="sysadminno"/>
					<input type="hidden" name="sysadminno" value="<bean:write name="RegisterForm" property="sysadminno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.GROUP.ADMIN"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="adminno"/>
					<input type="hidden" name="adminno" value="<bean:write name="RegisterForm" property="adminno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.GROUP.MANAGER"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="managerno"/>
					<input type="hidden" name="managerno" value="<bean:write name="RegisterForm" property="managerno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.AUTHOR"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="authorno"/>
					<input type="hidden" name="authorno" value="<bean:write name="RegisterForm" property="authorno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.MONITOR"/> 
				</td>
				<td>
					<bean:write name="RegisterForm" property="monitorno"/>
					<input type="hidden" name="monitorno" value="<bean:write name="RegisterForm" property="monitorno"/>"/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="role.LEARNER"/>
				</td>
				<td>
					<bean:write name="RegisterForm" property="learnerno"/>
					<input type="hidden" name="learnerno" value="<bean:write name="RegisterForm" property="learnerno"/>"/>	
				</td>
			</tr>
	</table>
	
	<p align="center">
		<input type="submit" value="<fmt:message key="admin.register" />"/>
		<input type="reset"  value="<fmt:message key="admin.reset" />"/>
		<input type="button" value="<fmt:message key="admin.cancel" />" onClick="javascript:history.back(-1)"/>
	</p>
</form>

