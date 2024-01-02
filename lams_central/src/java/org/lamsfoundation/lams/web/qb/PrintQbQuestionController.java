package org.lamsfoundation.lams.web.qb;

import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qb")
public class PrintQbQuestionController {

    @GetMapping("/printQuestions.do")
    public String printQuestions(HttpServletRequest request, Model model) {
	// read Flash Attributes set by another module and put them in the model
	Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	if (inputFlashMap != null) {

	    Map<Long, List<String>> randomisedOptions = new HashMap<>();
	    model.addAttribute("printRandomisedOptions", randomisedOptions);
	    for (String key : inputFlashMap.keySet()) {
		if (key.startsWith("print")) {
		    model.addAttribute(key, inputFlashMap.get(key));

		    if (key.equals("printQuestions")) {
			Collection<QbQuestion> questions = (Collection<QbQuestion>) inputFlashMap.get(key);
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
	return "qb/printQuestions";
    }
}