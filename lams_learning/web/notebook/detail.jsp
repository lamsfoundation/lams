<%--
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt
--%>
  
<%@ page language="java"%>
<%@ taglib uri="http://jakarta.apache.org/struts/struts-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/struts-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>

<div align="center">

	<html:form action="/notebook/SaveEntry" method="GET">
		<html:hidden property="notebookEntryId" />

		<span class="error">
			<%-- Struts error messages --%>
		</span>
		
		<table width="100%" border="0" cellpadding="3" cellspacing="4" class="body" summary="This table is being used for layout purposes">
			<tr bgcolor="#999999"> 
				<td colspan="2" align="left" class="bodyBold">
					<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<td class="bodyBold"><font color="#FFFFFF"><c:out value="${notebookForm.noteType}" /></font></td>
							<td align="right" class="smallText">
								<font color="#FFFFFF">
									<strong> Date created: </strong> <c:out value="${notebookForm.createdDateTime}" />
									<c:if test="${!empty notebookForm.updatedDateTime}" >
										&nbsp;&nbsp;
										<strong> Date last edited: </strong> <c:out value="${notebookEntryForm.updatedDateTime}" />
									</c:if>
								</font>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="right" class="bodyBold" style="{border-right: solid #CCCCCC 1px; border-bottom: solid #CCCCCC 1px; }">
					Title
				</td>
				<td width="85%" align="left" class="body"  style="{border-bottom: solid #CCCCCC 1px; }">
				
					<%-- If not updatedable then display title text readonly --%>
					<c:choose>
						<c:when test="${notebookForm.updateable}">
							<html:text property="title" size="45" styleClass="textField" />
						</c:when>
						<c:otherwise>
							<c:out value="${notebookForm.title}" />
						</c:otherwise>
					</c:choose>
					<%--input name='title' type='text' size='45' width='45' class='textField' value=''--%>
		
				</td>
			</tr>
			<tr> 
				<td align="right" valign="top" class="bodyBold" style="{border-right: solid #CCCCCC 1px}">
					Note
				</td>
				<td align="left" valign="top" >
				
					<%-- If not updatedable then format text (replacing \n with <br />) and display readonly --%>
					<c:choose>
						<c:when test="${notebookForm.updateable}">
							<html:textarea property='body' cols='50' rows='6' />
						</c:when>
						<c:otherwise>
							<c:out value="${notebookForm.bodyFormatted}" />
						</c:otherwise>
					</c:choose>
				</td>
			 </tr>
			<tr> 
				<td align="right" valign="top" class="bodyBold">&nbsp;</td>
				<td align="left">
	    			<%-- If not updatedable then do not display the save button --%>
					<c:if test="${notebookForm.updateable}">
						<input name="save" type="submit" class="longButton" id="save"
								onMouseOver="setClass(this,'longButtonover')"
								onMouseOut="setClass(this,'longButton')" value="Save in Notebook" />
						&nbsp;
						<input name="saveJournal" type="submit" class="longButton" id="saveJournal"
								onMouseOver="setClass(this,'longButtonover')"
								onMouseOut="setClass(this,'longButton')" value="Save in Journal" />
						&nbsp;
					</c:if>
	
					<input name="viewList" type="button" class="longButton" id="viewList"
							onClick = ""
							onMouseOver="setClass(this,'longButtonover')"
							onMouseOut="setClass(this,'longButton')" value="View all entries" />
	
				</td>
			</tr>
		</table>

	</html:form>

</div>

