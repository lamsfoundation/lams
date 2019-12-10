<%@ include file="/common/taglibs.jsp"%>
<script>
	$(document).ready(function(){
		//insert total learners number taken from the parent tblmonitor.jsp
		$("#total-learners-number").html(TOTAL_LESSON_LEARNERS_NUMBER);
	});
</script>

<!-- Header -->
<div class="row no-gutter">
	<div class="col-xs-12 col-md-12 col-lg-8">
		<h3>
			<fmt:message key="label.ira.questions.marks"/>
		</h3>
	</div>
</div>
<!-- End header -->

<!-- Notifications -->  
<div class="row no-gutter">
	<div class="col-xs-6 col-md-4 col-lg-4 ">
		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<i class="fa fa-users" style="color:gray" ></i> 
					<fmt:message key="label.attendance"/>: ${attemptedLearnersNumber}/<span id="total-learners-number"></span> 
				</h4> 
			</div>
		</div>
	</div>
	
	<c:if test="${attemptedLearnersNumber != 0}">
		<div class="col-xs-6 col-md-4 col-md-offset-4 col-lg-4 col-lg-offset-2">
			<a href="#nogo" type="button" class="btn btn-sm btn-default buttons_column"
					onclick="javascript:loadTab('mcqStudentChoices'); return false;">
				<i class="fa fa-file"></i>
				<fmt:message key="label.show.students.choices"/>
			</a>
		</div>
	</c:if>                                   
</div>
<!-- End notifications -->

<!-- Tables -->
<div class="row no-gutter">
<div class="col-xs-12 col-md-12 col-lg-12">

<c:forEach var="question" items="${questions}" varStatus="i">
	<div class="panel panel-default">
		<div class="panel-heading">
			<h4 class="panel-title">
				<span class="float-left space-right">Q${i.index + 1})</span> 
				<c:if test="${not hideTitles}">
					<c:out value="${question.name}" escapeXml="false"/>
				</c:if>
				<br>
				<c:out value="${question.description}" escapeXml="false"/>
			</h4>
		</div>
		
		<div class="panel-body">
			<div class="table-responsive">
				<table class="table table-striped">
					<tbody>
						<c:forEach var="option" items="${question.qbQuestion.qbOptions}" varStatus="j">
							<tr>
								<td width="5px">
									${ALPHABET[j.index]}.
								</td>
								<td>
									<c:out value="${option.name}" escapeXml="false"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</c:forEach>

</div>
</div>
<!-- End tables -->

