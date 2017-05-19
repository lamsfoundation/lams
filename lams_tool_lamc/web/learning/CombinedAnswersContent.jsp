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
                          <c:choose>
                            <c:when test="${hasEditRight}">		
                              <input type="radio" id="${dto.questionUid}-${option.uid}" name="checkedCa${dto.questionUid}" class="noBorder" value="${dto.questionUid}-${option.uid}"
                                     <c:if test="${option.selected}">checked="checked"</c:if>/>
                            
                            </c:when>
                          <c:otherwise>
                            <input type="radio" id="${dto.questionUid}-${option.uid}" name="checkedCa${dto.questionUid}" class="noBorder" value="${dto.questionUid}-${option.uid}" <c:if test="${option.selected}">checked="checked"</c:if> disabled="disabled">
                          </c:otherwise>
                    </c:choose>
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

    </div>
    </div>
  </div>
</div>
</c:forEach>

<html:hidden property="continueOptionsCombined" value="Continue" />

<c:if test="${hasEditRight}">
  <html:button property="continueButton" styleClass="btn btn-sm btn-primary pull-right" onclick="doSubmit();" styleId="continueButton">
    <fmt:message key="button.continue" />
  </html:button>
</c:if>

<!--options content ends here-->
