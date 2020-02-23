<%@ include file="/common/taglibs.jsp"%>

  <!--options content goes here-->

  <c:forEach var="dto" items="${requestScope.learnerAnswersDTOList}">

    <div class="row no-gutter">
      <div class="col-xs-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <table>
              <tr>
                <td style="vertical-align: top;">${dto.displayOrder})
                </td>
                <td width="100%" style="padding-left: 5px"><c:out value="${dto.question}" escapeXml="false" />
                </td>
              </tr>
              <c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">			
                <tr>
                  <td width="100%" colspan="2">
                    [
                    <strong><fmt:message key="label.mark" /></strong>
                    <c:out value="${dto.mark}" />
                    ]                    
                  </td>
                </tr>
              </c:if>
            </table>
          </div>
          <div class="panel-body">
            <!-- Answer options begin -->
            <div class="table-responsive">
              <table class="table table-hover table-condensed">
                <tbody>
                
                  <c:forEach var="option" items="${dto.options}" varStatus="status">
                    <tr>
                    
                      <td class="text-nowrap" style="vertical-align: top;">
                      	<input type="radio" id="${dto.questionUid}-${option.uid}" name="checkedCa${dto.questionUid}" class="noBorder" 
                         		value="${dto.questionUid}-${option.uid}" 
                           		<c:if test="${option.selected}">checked="checked"</c:if>
                            		<c:if test="${!hasEditRight}">disabled="disabled"</c:if> 
                      	>
                              
	                     <c:if test="${isPrefixAnswersWithLetters}">
	                     	<c:set var="seqLetter" value="${status.index}"/>
	                     	<%=Character.toChars(97 + (Integer)pageContext.getAttribute("seqLetter"))%>)
	                     </c:if>
                       </td>
                       
	                   <td width="100%">
	                       <label for="${dto.questionUid}-${option.uid}">
	                           <c:out value="${option.mcQueOptionText}" escapeXml="false" />
	                       </label>
	                    </td>
                    </tr>
                  </c:forEach>
              </tbody>
            </table>            
          </div>
          <!-- End answer options --> 
                
			<c:if test="${sessionMap.content.enableConfidenceLevels}">
				<div class="question-type">
					<fmt:message key="label.confidence" />
				</div>
	
				<div>
					<input name="confidenceLevel${dto.questionUid}" class="bootstrap-slider" type="text" 
						data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
						data-slider-enabled="${hasEditRight}" data-slider-tooltip="hide"
						<c:if test="${dto.confidenceLevel != -1}">data-slider-value="${dto.confidenceLevel}"</c:if>
					/>
				</div>
            </c:if>           

        </div>
      </div>
    </div>
  </div>
</c:forEach>

<form:hidden path="continueOptionsCombined" value="Continue" />

<c:if test="${hasEditRight}">
  <button name="continueButton" class="btn btn-sm btn-primary pull-right" onclick="doSubmit();" type="button" id="continueButton">
    <fmt:message key="button.continue" />
  </button>
</c:if>

<!--options content ends here-->
