package org.lamsfoundation.lams.web.qb;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/qb/vsa")
public class VsaController {

    @Autowired
    private IQbService qbService;

    @Autowired
    private ILamsToolService toolService;

    @RequestMapping("/displayVsaAllocate")
    public String displayVsaAllocate(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    Model model) {
	
	// the mapping is tool question -> unallocated answer -> user ID
	Map<QbToolQuestion, Map<String, Integer>> toolQuestionToUnallocatedAnswersMap = toolService
		.getUnallocatedVSAnswers(toolContentId);
	model.addAttribute("toolQuestions", toolQuestionToUnallocatedAnswersMap);
	return "qb/vsa/vsaAllocate";
    }

    @RequestMapping(path = "/allocateUserAnswer", method = RequestMethod.POST)
    @ResponseBody
    public String allocateUserAnswer(HttpServletResponse response, @RequestParam Long toolQuestionUid,
	    @RequestParam Long targetOptionUid, @RequestParam Long previousOptionUid, @RequestParam String answer) {

	Long optionUid = null;
	boolean answerFoundInResults = false;

	if (!targetOptionUid.equals(previousOptionUid) && StringUtils.isNotBlank(answer)) {
	    /*
	     * We need to synchronise this operation.
	     * When multiple requests are made to modify the same option, for example to add a VSA answer,
	     * we have a case of dirty reads.
	     * One answer gets added, but while DB is still flushing,
	     * another answer reads the option without the first answer,
	     * because it is not there yet.
	     * The second answer gets added, but the first one gets lost.
	     *
	     * We can not synchronise the method in service
	     * as the "dirty" transaction is already started before synchronisation kicks in.
	     * We do it here, before transaction starts.
	     * It will not work for distributed environment, though.
	     * If teachers allocate answers on different LAMS servers,
	     * we can still get the same problem. We will need a more sophisticated solution then.
	     */

	    synchronized (qbService) {
		optionUid = qbService.allocateVSAnswerToOption(toolQuestionUid, targetOptionUid, previousOptionUid,
			answer);
	    }

	    // recalculate marks for all lessons in all cases except for reshuffling inside the same container
	    answerFoundInResults = toolService.recalculateMarksForVsaQuestion(toolQuestionUid, answer);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isAnswerDuplicated", optionUid != null);
	responseJSON.put("optionUid", optionUid == null ? -1 : optionUid);
	responseJSON.put("answerFoundInResults", answerFoundInResults);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

}
