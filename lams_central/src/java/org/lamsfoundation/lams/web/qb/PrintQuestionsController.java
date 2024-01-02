package org.lamsfoundation.lams.web.qb;

import org.lamsfoundation.lams.util.imgscalr.Scalr;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/qb")
public class PrintQuestionsController {

    @GetMapping("/printQuestions.do")
    public String printQuestions(HttpServletRequest request, Model model) {
	// read Flash Attributes set by another module and put them in the model
	Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	if (inputFlashMap != null) {
	    for (String key : inputFlashMap.keySet()) {
		if (key.startsWith("print")) {
		    model.addAttribute(key, inputFlashMap.get(key));
		}
	    }
	}
	return "qb/printQuestions";
    }
}