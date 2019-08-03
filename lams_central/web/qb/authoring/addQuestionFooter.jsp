<%@ include file="/common/taglibs.jsp"%>
	
<footer class="footer fixed-bottom">
	<div class="panel-heading ">
       	<div class="col-xs-12x col-md-6x form-groupx rowx form-inlinex btn-group-md voffset5">
       		<span <c:if test="${empty assessmentQuestionForm.sessionMapID}">style="visibility: hidden;"</c:if>>
	       		Collection
	        		
				<select class="btn btn-md btn-default" id="collection-uid-select">
					<c:forEach var="collection" items="${assessmentQuestionForm.userCollections}">
						<option value="${collection.uid}"
							<c:if test="${collection.uid == assessmentQuestionForm.oldCollectionUid}">selected="selected"</c:if>>
							<c:out value="${collection.name}" />
						</option>
					</c:forEach>
				</select>
			</span>
				
        	<div class="pull-right col-xs-12x col-md-6x" style="margin-top: -5px;">
			    <a href="#nogo" onclick="javascript:self.parent.tb_remove();" class="btn btn-sm btn-default loffset5">
					<fmt:message key="label.cancel" />
				</a>
				<a href="#nogo" onclick="javascript:$('#assessmentQuestionForm').submit();" class="btn btn-sm btn-default button-add-item">
					<fmt:message key="button.save" />
				</a>
			</div>
		</div>
   	</div>
</footer>