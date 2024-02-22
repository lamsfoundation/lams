<%@ include file="/common/taglibs.jsp"%>
<c:set var="newLineChar" value="<%= \"\r\n\" %>" />

<style>
	/* show horizontal scroller for iPads */
	body {
	    -webkit-overflow-scrolling: touch;
	}

	#questions-data {
	  position: relative;
	  background: #FFF;
	}
	
	#questions-data thead {
		background: #FFF;
	}
	
	#questions-data tbody th {
	  position: -webkit-sticky; /* for Safari */
	  position: sticky;
	  left: 0;
	  background: #FFF;
	}
	
	span.user-response {
	  padding: 1px 3px;
	  color: white;
	  border-radius: 3px; }
	
	span.user-response .fa, .question-detail-modal .fa {
	  cursor: default;
	}
	
	span.successful-response {
	  background-color: #5cb85c; }
	
	span.wrong-response {
	  background-color: #d9534f; }
</style>

<script>
	// tell TBL monitor that this element is scrollable horizontally
	tlbMonitorHorizontalScrollElement = '#questions-data-container';

	<c:if test="${!isTbl}">
		function exportExcel(){
			//dynamically create a form and submit it
			var exportExcelUrl = "<lams:LAMSURL/>tool/lascrt11/tblmonitoring/exportExcel.do?toolContentID=${toolContentID}&reqID=" + (new Date()).getTime();
			var form = $('<form method="post" action="' + exportExcelUrl + '"></form>');
		    var hiddenInput = $('<input type="hidden" name="<csrf:tokenname/>" value="<csrf:tokenvalue/>"></input>');
		    form.append(hiddenInput);
		    $(document.body).append(form);
		    form.submit();
		};
	</c:if>

	function openActivityMonitoring(){
		window.open("<lams:WebAppURL />monitoring/summary.do?toolContentID=${toolContentID}&lessonID=" + lessonId + 
				"&contentFolderID=" + contentFolderId + "&reqID=" + (new Date()).getTime(), "_self");
	}

	$(document).ready(function(){
		openEventSource('<lams:WebAppURL />tblmonitoring/traStudentChoicesFlux.do?toolContentId=${toolContentID}', function(event) {
			$(tlbMonitorHorizontalScrollElement).load('<lams:WebAppURL />tblmonitoring/traStudentChoicesTable.do?toolContentID=${toolContentID}');
		});

		openEventSource('<lams:WebAppURL />tblmonitoring/getTimeLimitPanelUpdateFlux.do?toolContentId=${toolContentID}', function(event) {
			if (!event.data) {
				return;
			}

			// destroy existing absolute time limit counter before refresh
			$('.absolute-time-limit-counter, .time-limit-widget-individual-counter').countdown('destroy');
			let data = JSON.parse(event.data);
			$('#time-limit-panel-placeholder').load('<lams:LAMSURL/>monitoring/timeLimit.jsp?toolContentId=${toolContentID}&absoluteTimeLimitFinish=' + data.absoluteTimeLimitFinish
					+ '&relativeTimeLimit=' + data.relativeTimeLimit + '&absoluteTimeLimit=' + data.absoluteTimeLimit
					+ '&isTbl=${isTbl}&controllerContext=tool/lascrt11/monitoring');
		});

		<c:if test="${fn:length(sessionDtos) > 10}">
			// Add sticky column headers to student choices table.
			// Standard sticky header CSS solution does not work as it is page which is being scrolled, not the table itself

			window.onscroll = function() {
				let studentChoicesTable = $('#questions-data'),
					studentChoicesStickyHeader = $('thead', studentChoicesTable),
					studentChoicesTableTopOffset = studentChoicesTable.offset().top,
					studentChoicesTableHeight = studentChoicesTable.height();
				if (window.pageYOffset > studentChoicesTableTopOffset + 20 
						&& window.pageYOffset < studentChoicesTableTopOffset + studentChoicesTableHeight - 20) {
					studentChoicesStickyHeader
						.css('transform', 'translateY(' + (window.pageYOffset - studentChoicesTableTopOffset - 2) + 'px)');
				} else {
					studentChoicesStickyHeader.css('transform', 'none');
				}
			}
		</c:if>
	});
</script>

<div class="${isTbl ? 'container-main ms-4' : ''}">
	<c:if test="${isTbl}">
		<div class="float-end">
			<button onclick="openActivityMonitoring()" type="button" class="btn btn-secondary buttons_column">
				<i class="fa-solid fa-circle-info me-1"></i>
				<fmt:message key="label.activity.monitoring"/>
			</button>
			<button type="button" class="btn btn-secondary buttons_column"
					onclick="loadTab('trat', $('#load-trat-tab-btn'))">
				<i class="fa fa-clipboard-question me-1"></i>
				<fmt:message key="label.hide.students.choices"/>
			</button>
			<button onclick="printTable()" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-print me-1"></i>
				<fmt:message key="label.print"/>
			</button>
			<button onclick="exportExcel()" type="button" class="btn btn-secondary buttons_column">
				<i class="fa fa-file-excel me-1"></i>
				<fmt:message key="label.export.excel"/>
			</button>
			<c:if test="${vsaPresent}">
				<a class="btn btn-secondary buttons_column" target="_blank"
					   href='<lams:LAMSURL />qb/vsa/displayVsaAllocate.do?toolContentID=${scratchie.contentId}'>
					<i class="fa-solid fa-arrow-down-1-9 me-1"></i>
					<fmt:message key="label.vsa.allocate.button" />
				</a>
			</c:if>
		</div>
			
		<h3>
			<fmt:message key="label.tra.questions.marks"/>
		</h3>
	</c:if>

	<div class="lcard ${isTbl ? 'mt-5' : ''}">
		<div id="questions-data-container" class="table-responsive card-body">
		</div>
	</div>
		
	<!-- Question detail modal -->
	<c:forEach var="item" items="${items}" varStatus="i">
		<div class="modal fade" id="question${i.index}Modal">
		<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header align-items-start text-bg-warning">
				<div class="modal-title d-flex">
					<span class="me-2">
						Q${i.index+1})
					</span>
					<c:if test="${not empty item.qbQuestion.name}">
						<div><c:out value="${item.qbQuestion.name}"  escapeXml="false" /></div>
					</c:if>
				</div>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			
			<div class="modal-body">
				${item.qbQuestion.description}
					
				<div class="table-responsive voffset10">
					<table class="table table-striped table-hover">
						<tbody>
							<c:forEach var="qbOption" items="${item.qbQuestion.qbOptions}" varStatus="j">
								<c:set var="cssClass"><c:if test='${qbOption.correct}'>text-bg-success</c:if></c:set>
									<tr>
										<c:choose>
											<c:when test="${item.qbQuestion.type == 1 or item.qbQuestion.type == 8}">
												<td width="5px" class="${cssClass}">
													${ALPHABET[j.index]}.
												</td>
												<td class="${cssClass}">
													<c:out value="${qbOption.name}" escapeXml="false"/>
												</td>
											</c:when>
											<c:otherwise>
												<td width="5px" class="${cssClass}">
													<c:choose>
														<c:when test="${qbOption.correct}">
															<i class="fa fa-check"></i>
														</c:when>
														<c:otherwise>
															<i class="fa fa-close"></i>
														</c:otherwise>
													</c:choose>
												</td>
												<td class="${cssClass}">
													${fn:replace(qbOption.name, newLineChar, ', ')}
												</td>
											</c:otherwise>
										</c:choose>
									</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div> 
			<div class="modal-footer">	
				<a href="#" data-bs-dismiss="modal" class="btn btn-secondary">
					<i class="fa fa-check fa-lg me-1"></i>
					<fmt:message key="button.close"/>
				</a>
			</div>
		</div>
		</div>
		</div>
	</c:forEach>
	<!-- End question detail modal -->
	
	<c:if test="${isTbl}">
		<div id="time-limit-panel-placeholder">
		</div>
	</c:if>
</div>
