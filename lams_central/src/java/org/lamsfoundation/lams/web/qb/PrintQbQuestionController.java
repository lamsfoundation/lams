package org.lamsfoundation.lams.web.qb;

import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qb")
public class PrintQbQuestionController {
    @Autowired
    IQbService qbService;

    @GetMapping("/printQuestions")
    public String printQuestions(HttpServletRequest request, Model model) {
	// read Flash Attributes set by another module and put them in the model
	Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	if (inputFlashMap != null) {
	    fillModelWithQuestions(inputFlashMap, model);
	}
	return "qb/printQuestions";
    }

    @GetMapping("/printQbCollectionQuestions")
    public String printQbCollectionQuestions(@RequestParam Long collectionUid, Model model,
	    HttpServletResponse response) throws IOException {
	if (!qbService.hasUserAccessToCollection(collectionUid)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user does not have access to given collection");
	    return null;
	}

	Map<String, Object> input = new HashMap<>();
	Collection<QbQuestion> questions = qbService.getCollectionQuestions(collectionUid);
	input.put("printQuestions", questions);
	QbCollection collection = qbService.getCollectionByUid(collectionUid);
	input.put("printTitleSuffix", collection.getName());

	fillModelWithQuestions(input, model);
	return "qb/printQuestions";
    }

    private static void fillModelWithQuestions(Map<String, ?> input, Model model) {
	Map<Long, List<String>> randomisedOptions = new HashMap<>();
	model.addAttribute("printRandomisedOptions", randomisedOptions);
	for (String key : input.keySet()) {
	    if (key.startsWith("print")) {
		model.addAttribute(key, input.get(key));

		if (key.equals("printQuestions")) {
		    Collection<QbQuestion> questions = (Collection<QbQuestion>) input.get(key);
		    for (QbQuestion question : questions) {
			if (question.getType().equals(QbQuestion.TYPE_MATCHING_PAIRS) || question.getType()
				.equals(QbQuestion.TYPE_ORDERING)) {
			    // randomise the order of options when correct answers are hidden
			    List<String> optionNames = new ArrayList<>();
			    for (QbOption option : question.getQbOptions()) {
				optionNames.add(option.getName());
			    }
			    Collections.shuffle(optionNames);
			    randomisedOptions.put(question.getUid(), optionNames);
			}
		    }
		}
	    }
	}
    }
}