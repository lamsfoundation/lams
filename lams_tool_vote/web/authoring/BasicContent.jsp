<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>


<%@ include file="/common/taglibs.jsp"%>

<c:set scope="request" var="lams"><lams:LAMSURL/></c:set>
<c:set scope="request" var="tool"><lams:WebAppURL/></c:set>

<script type="text/javascript">
<!-- Common Javascript functions for LAMS -->
	/**
	 * Launches the popup window for the instruction files
	 */
	function showMessage(url) {
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="670px";
			area.style.height="100%";
			area.src=url;
			area.style.display="block";
		}
		document.getElementById("saveCancelButtons").style.visibility="hidden";
	}
	function hideMessage(){
		var area=document.getElementById("messageArea");
		if(area != null){
			area.style.width="0px";
			area.style.height="0px";
			area.style.display="none";
		}
		document.getElementById("saveCancelButtons").style.visibility="visible";
	}

	function removeNomination(questionIndex)
	{
		document.VoteAuthoringForm.questionIndex.value=questionIndex;
        submitMethod('removeNomination');
	}

	function removeMonitoringNomination(questionIndex)
	{
		document.VoteMonitoringForm.questionIndex.value=questionIndex;
        submitMonitoringMethod('removeNomination');
	}

</script>

			<html:hidden property="questionIndex"/>
			
			<table cellpadding="0">

						<tr>
							<td colspan="2">
								<div class="field-name" style="text-align: left;">
									<fmt:message key="label.authoring.title.col"></fmt:message>
								</div>
								<html:text property="title" style="width: 100%;"></html:text>
							</td>
						</tr>
						

						<tr>
							<td colspan="2">
								<div class="field-name" style="text-align: left;">
									<fmt:message key="label.authoring.instructions.col"></fmt:message>
								</div>
								<lams:FCKEditor id="instructions"
									value="${voteGeneralAuthoringDTO.activityInstructions}"
									contentFolderID="${voteGeneralAuthoringDTO.contentFolderID}"></lams:FCKEditor>
							</td>
						</tr>
	
				 		<tr>
						<td colspan="2">
							<div id="resourceListArea">
						 		<c:if test="${voteGeneralAuthoringDTO.activeModule == 'authoring' || voteGeneralAuthoringDTO.activeModule == 'defineLater'}"> 		
									<%@ include file="/authoring/itemlist.jsp"%>
								</c:if> 							
						 		<c:if test="${voteGeneralAuthoringDTO.activeModule != 'authoring' && voteGeneralAuthoringDTO.activeModule != 'defineLater'}"> 		
									<%@ include file="/monitoring/itemlist.jsp"%>
								</c:if> 							
							</div>
						</td>
						</tr>
						

			 		<c:if test="${voteGeneralAuthoringDTO.activeModule == 'authoring' || voteGeneralAuthoringDTO.activeModule == 'defineLater'}"> 								
						<tr>
							<td colspan="2">
									<a href="javascript:showMessage('<html:rewrite page="/authoring.do?dispatch=newNominationBox&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&activeModule=${voteGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${voteGeneralAuthoringDTO.defaultContentIdStr}&voteChangable=${voteGeneralAuthoringDTO.voteChangable}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}&reflectionSubject=${voteGeneralAuthoringDTO.reflectionSubject}"/>');"
										style="float:right;width:150px" class="button-add-item"> <fmt:message
										key="label.add.new.nomination" /> </a>
							</td>
						</tr>
					</c:if> 												
			 		<c:if test="${voteGeneralAuthoringDTO.activeModule != 'authoring' && voteGeneralAuthoringDTO.activeModule != 'defineLater'}"> 							
						<tr>
							<td colspan="2">
									<a href="javascript:showMessage('<html:rewrite page="/monitoring.do?dispatch=newNominationBox&contentFolderID=${voteGeneralAuthoringDTO.contentFolderID}&httpSessionID=${voteGeneralAuthoringDTO.httpSessionID}&toolContentID=${voteGeneralAuthoringDTO.toolContentID}&activeModule=${voteGeneralAuthoringDTO.activeModule}&defaultContentIdStr=${voteGeneralAuthoringDTO.defaultContentIdStr}&voteChangable=${voteGeneralAuthoringDTO.voteChangable}&lockOnFinish=${voteGeneralAuthoringDTO.lockOnFinish}&allowText=${voteGeneralAuthoringDTO.allowText}&maxNominationCount=${voteGeneralAuthoringDTO.maxNominationCount}&reflect=${voteGeneralAuthoringDTO.reflect}&reflectionSubject=${voteGeneralAuthoringDTO.reflectionSubject}"/>');"
										style="float:right;width:150px" class="button-add-item"> <fmt:message
										key="label.add.new.nomination" /> </a>
							</td>
						</tr>
					</c:if> 																		 		

					<tr>
						<td colspan="2">
							<iframe
								onload="javascript:this.style.height=this.contentWindow.document.body.scrollHeight+'px'"
								id="messageArea" name="messageArea"
								style="width:0px;height:0px;border:0px;display:none"
								frameborder="no" scrolling="no">
							</iframe>
						</td>
					</tr>

			 </table>			

				
				<table cellpadding="0">
					    <tr> <td> &nbsp </td> </tr>					    
						<tr> 
							<td>							
						      	<c:if test="${voteGeneralAuthoringDTO.activeModule != 'authoring'}"> 					
									<p align="right">
									    <a href="javascript:submitMethod('submitAllContent')" class="button">
								        	<bean:message key="label.save"/></a>
									</p>
								</c:if> 					
							</td> 
					  	</tr>
				 </table>

	                                     





