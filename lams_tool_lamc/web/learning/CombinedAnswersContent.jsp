<%@ include file="/common/taglibs.jsp"%>

  <!--options content goes here-->

  <c:forEach var="answerDto" items="${requestScope.learnerAnswerDtos}">

    <div class="row g-0">
      <div class="col-12">
        <div class="panel panel-default">
          <div class="panel-heading">
            <table>
              <tr>
                <td style="vertical-align: top;">
                	${answerDto.displayOrder})
                </td>
                <td width="100%" style="padding-left: 5px">
                	<c:if test="${not sessionMap.hideTitles}">
	                	<c:out value="${answerDto.questionName}" escapeXml="false" />
	                	<br>
                	</c:if>
                	<c:out value="${answerDto.questionDescription}" escapeXml="false" />
                </td>
              </tr>
              <c:if test="${mcGeneralLearnerFlowDTO.showMarks == 'true'}">			
                <tr>
                  <td width="100%" colspan="2">
                    [
                    <strong><fmt:message key="label.mark" /></strong>
                    <c:out value="${answerDto.mark}" />
                    ]                    
                  </td>
                </tr>
              </c:if>
            </table>
          </div>
          <div class="panel-body">
            <!-- Answer options begin -->
            <div class="table-responsive">
              <table class="table table-hover table-sm">
                <tbody>
                
                  <c:forEach var="option" items="${answerDto.options}" varStatus="status">
                    <tr>
                    
                      <td class="text-nowrap" width="100%">
                      	<div class="form-check">
	                      	<input type="radio" id="${answerDto.questionUid}-${option.qbOption.uid}" name="checkedCa${answerDto.questionUid}" class="form-check-input" 
	                         		value="${answerDto.questionUid}-${option.qbOption.uid}" 
	                           		<c:if test="${option.selected}">checked="checked"</c:if>
	                            	<c:if test="${!hasEditRight}">disabled="disabled"</c:if> 
	                      	>

	                       	<label for="${answerDto.questionUid}-${option.qbOption.uid}" class="form-check-label">
		                     	<c:if test="${isPrefixAnswersWithLetters}">
		                     		<c:set var="seqLetter" value="${status.index}"/>
		                     		<%=Character.toChars(97 + (Integer)pageContext.getAttribute("seqLetter"))%>)
		                     	</c:if>
	                     	
	                           	<c:out value="${option.qbOption.name}" escapeXml="false" />
	                       	</label>
	                   </div>
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
					<input name="confidenceLevel${answerDto.questionUid}" class="bootstrap-slider" type="text" 
						data-slider-ticks="[0, 5, 10]" data-slider-ticks-labels='["0", "50", "100%"]' 
						data-slider-enabled="${hasEditRight}" data-slider-tooltip="hide"
						<c:if test="${answerDto.confidenceLevel != -1}">data-slider-value="${answerDto.confidenceLevel}"</c:if>
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
	<div class="activity-bottom-buttons">
	  <button name="continueButton" class="btn btn-primary" onclick="doSubmit();" type="button" id="continueButton">
	    <fmt:message key="button.continue" />
	  </button>
  </div>
</c:if>

<!--options content ends here-->
