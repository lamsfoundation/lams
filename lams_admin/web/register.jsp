<%@ page contentType="text/html; charset=utf-8" language="java" %>
<%@ page import="org.lamsfoundation.lams.util.Configuration" %>
<%@ taglib uri="tags-html" prefix="html" %>
<%@ taglib uri="tags-logic" prefix="logic" %>
<%@ taglib uri="tags-core" prefix="c" %>
<%@ taglib uri="tags-fmt" prefix="fmt" %>
<%@ taglib uri="tags-lams" prefix="lams" %>

<html:form action="/register" method="post">
	<html:hidden property="method" value="register"/>
	<h2 align="left">
		<a href="sysadminstart.do"><fmt:message key="sysadmin.maintain" /></a> :  
		<fmt:message key="sysadmin.register.server" />
	</h2>
	<br/>
	
	<html:hidden property="serverurl"/>
	<html:hidden property="serverversion"/>
	<html:hidden property="serverbuild"/>
	<html:hidden property="serverlocale"/>
	<html:hidden property="langdate"/>
	<html:hidden property="groupno"/>
	<html:hidden property="subgroupno"/>
	<html:hidden property="sysadminno"/>
	<html:hidden property="adminno"/>
	<html:hidden property="authorno"/>
	<html:hidden property="monitorno"/>
	<html:hidden property="managerno"/>
	<html:hidden property="learnerno"/>
	
	<table class="alternative-color" width=100%>
			<tr>
				<th colspan="2"><fmt:message key="admin.register.heading.title"/></th>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.sitename"/>
				</td>
				<td>
					<html:text property="sitename" name="sitename" size="40" value=""/>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.name"/>
				</td>
				<td>
					<html:text property="rname" size="40"><bean:write name="RegisterForm" property="rname"/></html:text>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.email"/>
				</td>
				<td>
					<html:text property="remail" size="40"><bean:write name="RegisterForm" property="remail"/></html:text>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.user.country"/>
				</td>
				<td>
					<html:select property="servercountry">
						<html:option value=""></html:option>
						<html:option value="Afghanistan" >Afghanistan</html:option>
						<html:option value="Albania" >Albania</html:option>
					
						<html:option value="Algeria" >Algeria</html:option>
						<html:option value="Andorra" >Andorra</html:option>
						<html:option value="Angola" >Angola</html:option>
						<html:option value="Antigua and Barbuda" >Antigua and Barbuda</html:option>
						<html:option value="Argentina" >Argentina</html:option>
						<html:option value="Armenia" >Armenia</html:option>
					
						<html:option value="Australia" >Australia</html:option>
						<html:option value="Austria" >Austria</html:option>
						<html:option value="Azerbaijan" >Azerbaijan</html:option>
						<html:option value="Bahamas" >Bahamas</html:option>
						<html:option value="Bahrain" >Bahrain</html:option>
						<html:option value="Bangladesh" >Bangladesh</html:option>
					
						<html:option value="Barbados" >Barbados</html:option>
						<html:option value="Belarus" >Belarus</html:option>
						<html:option value="Belgium" >Belgium</html:option>
						<html:option value="Belize" >Belize</html:option>
						<html:option value="Benin" >Benin</html:option>
						<html:option value="Bhutan" >Bhutan</html:option>
					
						<html:option value="Bolivia" >Bolivia</html:option>
						<html:option value="Bosnia and Herzegovina" >Bosnia and Herzegovina</html:option>
						<html:option value="Botswana" >Botswana</html:option>
						<html:option value="Brazil" >Brazil</html:option>
						<html:option value="Brunei Darussalam" >Brunei Darussalam</html:option>
						<html:option value="Bulgaria" >Bulgaria</html:option>
					
						<html:option value="Burkina Faso" >Burkina Faso</html:option>
						<html:option value="Burundi" >Burundi</html:option>
						<html:option value="Cambodia" >Cambodia</html:option>
						<html:option value="Cameroon" >Cameroon</html:option>
						<html:option value="Canada" >Canada</html:option>
						<html:option value="Cape Verde" >Cape Verde</html:option>
					
						<html:option value="Central African Republic" >Central African Republic</html:option>
						<html:option value="Chad" >Chad</html:option>
						<html:option value="Chile" >Chile</html:option>
						<html:option value="China" >China</html:option>
						<html:option value="Colombia" >Colombia</html:option>
						<html:option value="Comoros" >Comoros</html:option>
					
						<html:option value="Congo" >Congo</html:option>
						<html:option value="Costa Rica" >Costa Rica</html:option>
						<html:option value="C'ote d'Ivoire" >C'ote d'Ivoire</html:option>
						<html:option value="Croatia" >Croatia</html:option>
						<html:option value="Cuba" >Cuba</html:option>
						<html:option value="Cyprus" >Cyprus</html:option>
					
						<html:option value="Czech Republic" >Czech Republic</html:option>
						<html:option value="Democratic People's Republic of Korea" >Democratic People's Republic of Korea</html:option>
						<html:option value="Democratic Republic of the Congo" >Democratic Republic of the Congo</html:option>
						<html:option value="Denmark" >Denmark</html:option>
						<html:option value="Djibouti" >Djibouti</html:option>
						<html:option value="Dominica" >Dominica</html:option>
					
						<html:option value="Dominican Republic" >Dominican Republic</html:option>
						<html:option value="Ecuador" >Ecuador</html:option>
						<html:option value="Egypt" >Egypt</html:option>
						<html:option value="El Salvador" >El Salvador</html:option>
						<html:option value="Equatorial Guinea" >Equatorial Guinea</html:option>
						<html:option value="Eritrea" >Eritrea</html:option>
					
						<html:option value="Estonia" >Estonia</html:option>
						<html:option value="Ethiopia" >Ethiopia</html:option>
						<html:option value="Fiji" >Fiji</html:option>
						<html:option value="Finland" >Finland</html:option>
						<html:option value="France" >France</html:option>
						<html:option value="Gabon" >Gabon</html:option>
					
						<html:option value="Gambia" >Gambia</html:option>
						<html:option value="Georgia" >Georgia</html:option>
						<html:option value="Germany" >Germany</html:option>
						<html:option value="Ghana" >Ghana</html:option>
						<html:option value="Greece" >Greece</html:option>
						<html:option value="Grenada" >Grenada</html:option>
					
						<html:option value="Guatemala" >Guatemala</html:option>
						<html:option value="Guinea" >Guinea</html:option>
						<html:option value="Guinea Bissau" >Guinea Bissau</html:option>
						<html:option value="Guyana" >Guyana</html:option>
						<html:option value="Haiti" >Haiti</html:option>
						<html:option value="Honduras" >Honduras</html:option>
					
						<html:option value="Hungary" >Hungary</html:option>
						<html:option value="Iceland" >Iceland</html:option>
						<html:option value="India" >India</html:option>
						<html:option value="Indonesia" >Indonesia</html:option>
						<html:option value="Iran" >Iran</html:option>
						<html:option value="Iraq" >Iraq</html:option>
					
						<html:option value="Ireland" >Ireland</html:option>
						<html:option value="Israel" >Israel</html:option>
						<html:option value="Italy" >Italy</html:option>
						<html:option value="Jamaica" >Jamaica</html:option>
						<html:option value="Japan" >Japan</html:option>
						<html:option value="Jordan" >Jordan</html:option>
					
						<html:option value="Kazakhstan" >Kazakhstan</html:option>
						<html:option value="Kenya" >Kenya</html:option>
						<html:option value="Kiribati" >Kiribati</html:option>
						<html:option value="Kuwait" >Kuwait</html:option>
						<html:option value="Kyrgyzstan" >Kyrgyzstan</html:option>
						<html:option value="Lao People's Democratic Republic" >Lao People's Democratic Republic</html:option>
					
						<html:option value="Latvia" >Latvia</html:option>
						<html:option value="Lebanon" >Lebanon</html:option>
						<html:option value="Lesotho" >Lesotho</html:option>
						<html:option value="Liberia" >Liberia</html:option>
						<html:option value="Libyan Arab Jamahiriya" >Libyan Arab Jamahiriya</html:option>
						<html:option value="Liechtenstein" >Liechtenstein</html:option>
					
						<html:option value="Lithuania" >Lithuania</html:option>
						<html:option value="Luxembourg" >Luxembourg</html:option>
						<html:option value="Madagascar" >Madagascar</html:option>
						<html:option value="Malawi" >Malawi</html:option>
						<html:option value="Malaysia" >Malaysia</html:option>
						<html:option value="Maldives" >Maldives</html:option>
					
						<html:option value="Mali" >Mali</html:option>
						<html:option value="Malta" >Malta</html:option>
						<html:option value="Marshall Islands" >Marshall Islands</html:option>
						<html:option value="Mauritania" >Mauritania</html:option>
						<html:option value="Mauritius" >Mauritius</html:option>
						<html:option value="Mexico" >Mexico</html:option>
					
						<html:option value="Micronesia" >Micronesia</html:option>
						<html:option value="Moldova" >Moldova</html:option>
						<html:option value="Monaco" >Monaco</html:option>
						<html:option value="Mongolia" >Mongolia</html:option>
						<html:option value="Morocco" >Morocco</html:option>
						<html:option value="Mozambique" >Mozambique</html:option>
					
						<html:option value="Myanmar" >Myanmar</html:option>
						<html:option value="Namibia" >Namibia</html:option>
						<html:option value="Nauru" >Nauru</html:option>
						<html:option value="Nepal" >Nepal</html:option>
						<html:option value="Netherlands" >Netherlands</html:option>
						<html:option value="New Zealand" >New Zealand</html:option>
					
						<html:option value="Nicaragua" >Nicaragua</html:option>
						<html:option value="Niger" >Niger</html:option>
						<html:option value="Nigeria" >Nigeria</html:option>
						<html:option value="Norway" >Norway</html:option>
						<html:option value="Oman" >Oman</html:option>
						<html:option value="Pakistan" >Pakistan</html:option>
					
						<html:option value="Palau" >Palau</html:option>
						<html:option value="Panama" >Panama</html:option>
						<html:option value="Papua New Guinea" >Papua New Guinea</html:option>
						<html:option value="Paraguay" >Paraguay</html:option>
						<html:option value="Peru" >Peru</html:option>
						<html:option value="Philippines" >Philippines</html:option>
					
						<html:option value="Poland" >Poland</html:option>
						<html:option value="Portugal" >Portugal</html:option>
						<html:option value="Qatar" >Qatar</html:option>
						<html:option value="Republic of Korea" >Republic of Korea</html:option>
						<html:option value="Romania" >Romania</html:option>
						<html:option value="Russian Federation" >Russian Federation</html:option>
					
						<html:option value="Rwanda" >Rwanda</html:option>
						<html:option value="Saint Kitts and Nevis" >Saint Kitts and Nevis</html:option>
						<html:option value="Saint Lucia" >Saint Lucia</html:option>
						<html:option value="Saint Vincent and the Grenadines" >Saint Vincent and the Grenadines</html:option>
						<html:option value="Samoa" >Samoa</html:option>
						<html:option value="San Marino" >San Marino</html:option>
					
						<html:option value="Sao Tome and Principe" >Sao Tome and Principe</html:option>
						<html:option value="Saudi Arabia" >Saudi Arabia</html:option>
						<html:option value="Senegal" >Senegal</html:option>
						<html:option value="Seychelles" >Seychelles</html:option>
						<html:option value="Sierra Leone" >Sierra Leone</html:option>
						<html:option value="Singapore" >Singapore</html:option>
					
						<html:option value="Slovakia" >Slovakia</html:option>
						<html:option value="Slovenia" >Slovenia</html:option>
						<html:option value="Solomon Islands" >Solomon Islands</html:option>
						<html:option value="Somalia" >Somalia</html:option>
						<html:option value="South Africa" >South Africa</html:option>
						<html:option value="Spain" >Spain</html:option>
					
						<html:option value="Sri Lanka" >Sri Lanka</html:option>
						<html:option value="Sudan" >Sudan</html:option>
						<html:option value="Suriname" >Suriname</html:option>
						<html:option value="Swaziland" >Swaziland</html:option>
						<html:option value="Sweden" >Sweden</html:option>
						<html:option value="Switzerland" >Switzerland</html:option>
					
						<html:option value="Syrian Arab Republic" >Syrian Arab Republic</html:option>
						<html:option value="Taiwan" >Taiwan</html:option>
						<html:option value="Tajikistan" >Tajikistan</html:option>
						<html:option value="Thailand" >Thailand</html:option>
						<html:option value="The former Yugoslav Republic of Macedonia" >The former Yugoslav Republic of Macedonia</html:option>
						<html:option value="Timor-Leste" >Timor-Leste</html:option>
					
						<html:option value="Togo" >Togo</html:option>
						<html:option value="Tonga" >Tonga</html:option>
						<html:option value="Trinidad and Tobago" >Trinidad and Tobago</html:option>
						<html:option value="Tunisia" >Tunisia</html:option>
						<html:option value="Turkey" >Turkey</html:option>
						<html:option value="Turkmenistan" >Turkmenistan</html:option>
					
						<html:option value="Tuvalu" >Tuvalu</html:option>
						<html:option value="Uganda" >Uganda</html:option>
						<html:option value="Ukraine" >Ukraine</html:option>
						<html:option value="United Arab Emirates" >United Arab Emirates</html:option>
						<html:option value="United Kingdom" >United Kingdom</html:option>
						<html:option value="United Republic of Tanzania" >United Republic of Tanzania</html:option>
					
						<html:option value="United States" >United States</html:option>
						<html:option value="Uruguay" >Uruguay</html:option>
						<html:option value="Uzbekistan" >Uzbekistan</html:option>
						<html:option value="Vanuatu" >Vanuatu</html:option>
						<html:option value="Venezuela" >Venezuela</html:option>
						<html:option value="Vietnam" >Vietnam</html:option>
					
						<html:option value="Yemen" >Yemen</html:option>
						<html:option value="Serbia and Montenegro" >Serbia and Montenegro</html:option>
						<html:option value="Zambia" >Zambia</html:option>
						<html:option value="Zimbabwe" >Zimbabwe</html:option>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<fmt:message key="admin.register.directory"/>
				</td>
				<td>
					<html:radio property="public" value="true">&nbsp;<fmt:message key="admin.register.directory.public"/></html:radio>&nbsp;&nbsp;
					<html:radio property="public" value="false">&nbsp;<fmt:message key="admin.register.directory.private"/></html:radio>
				</td>
			</tr>
	</table>
	
	<p align="center">
		<html:submit><fmt:message key="admin.register" /></html:submit>
		<html:reset><fmt:message key="admin.reset" /></html:reset>
		<html:cancel><fmt:message key="admin.cancel" /></html:cancel>
	</p>
</html:form>