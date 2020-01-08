<%@ include file="/common/taglibs.jsp"%>

				<select name="${qnon}grade" id="${qnon}grade" class="form-control">
					<option value="1.0">100 %</option>
					<option value="0.9">90 %</option>
					<option value="0.83333">83.333 %</option>
					<option value="0.8">80 %</option>
					<option value="0.75">75%</option>
					<option value="0.7">70 %</option>
					<option value="0.66666">66.666 %</option>
					<option value="0.6">60 %</option>
					<option value="0.5">50 %</option>
					<option value="0.4">40 %</option>
					<option value="0.33333">33.333 %</option>
					<option value="0.3">30 %</option>
					<option value="0.25">25 %</option>
					<option value="0.2">20 %</option>
					<option value="0.16666">16.666 %</option>
					<option value="0.142857">14.2857 %</option>
					<option value="0.125">12.5 %</option>
					<option value="0.11111">11.111 %</option>
					<option value="0.1">10 %</option>
					<option value="0.05">5 %</option>
					<option value="0.0"><fmt:message key="authoring.label.none" /></option>
					<option value="-0.05">-5 %</option>	
					<option value="-0.1">-10 %</option>
					<option value="-0.11111">-11.111 %</option>
					<option value="-0.125">-12.5 %</option>
					<option value="-0.142857">-14.2857 %</option>
					<option value="-0.16666">-16.666 %</option>
					<option value="-0.2">-20 %</option>
					<option value="-0.25">-25 %</option>
					<option value="-0.3">-30 %</option>	
					<option value="-0.33333">-33.333 %</option>
					<option value="-0.4">-40 %</option>
					<option value="-0.5">-50 %</option>
					<option value="-0.6">-60 %</option>
					<option value="-0.66666">-66.666 %</option>
					<option value="-0.7">-70 %</option>
					<option value="-0.75">-75 %</option>	
					<option value="-0.8">-80 %</option>
					<option value="-0.83333">-83.333 %</option>
					<option value="-0.9">-90 %</option>
					<option value="-1.0">-100 %</option>
				</select>
		<script>
		if ( "${optionGrade}" )
		$("#${qnon}grade").val("${optionGrade}");
		else
		$("#${qnon}grade").val("0.0");
		</script>