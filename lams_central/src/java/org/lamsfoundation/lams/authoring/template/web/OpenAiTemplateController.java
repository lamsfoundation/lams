package org.lamsfoundation.lams.authoring.template.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.util.JsonUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/authoring/template/tbl/ai")
public class OpenAiTemplateController {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/completions";
    private static final String API_MODEL = "text-davinci-003";
    private static final int API_MAX_TOKENS = 4000;

    private static final String ANSWER_LETTERS = "abcdefghi";

    private static final Logger log = Logger.getLogger(OpenAiTemplateController.class);

    @GetMapping("")
    @ResponseBody
    public String generateTblContent(@RequestParam String subject) throws IOException {
	String apiResponse = OpenAiTemplateController.callApi(
		"Create Team Based Learning RAT 5 multiple choice questions with at least 4 answers each about "
			+ subject + ". For example:\\n\\n1. Urbanism is described as\\n"
			+ "A. The space between buildings\\n"
			+ "B. The connected system of public and private spaces\\n" + "C. Big cities\\n"
			+ "D. The public realm\\n" + "Answer: B. The connected system of public and private spaces",
		1.5);
	List<Question> questions = OpenAiTemplateController.extractMcqQuestions(apiResponse);
	StringBuilder responseBuilder = new StringBuilder();
	for (int questionIndex = 0; questionIndex < questions.size(); questionIndex++) {
	    Question question = questions.get(questionIndex);
	    responseBuilder.append(questionIndex + 1).append(". ").append(question.getTitle()).append("<br>");
	    for (int answerIndex = 0; answerIndex < question.getAnswers().size(); answerIndex++) {
		Answer answer = question.getAnswers().get(answerIndex);
		if (answer.getScore() > 0) {
		    responseBuilder.append("<b>");
		}
		responseBuilder.append("&nbsp;&nbsp;").append(ANSWER_LETTERS.charAt(answerIndex)).append(") ")
			.append(answer.getText());
		if (answer.getScore() > 0) {
		    responseBuilder.append("</b>");
		}
		responseBuilder.append("<br>");
	    }
	    responseBuilder.append("<br>");
	}

	return responseBuilder.toString();
    }

    private static String callApi(String prompt, Double temperature) throws IOException {
	URL url = new URL(API_URL);
	HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	connection.setRequestMethod("POST");
	connection.setDoOutput(true);
	connection.setRequestProperty("Accept-Charset", StandardCharsets.UTF_8.toString());
	connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
	connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

	ObjectNode requestJSON = JsonNodeFactory.instance.objectNode();
	requestJSON.put("model", API_MODEL);
	requestJSON.put("max_tokens", API_MAX_TOKENS);
	requestJSON.put("prompt", prompt);
	requestJSON.put("temperature", temperature == null ? 0 : temperature);

	try (OutputStream output = connection.getOutputStream()) {
	    output.write(requestJSON.toString().getBytes());

	}
	if (log.isDebugEnabled()) {
	    log.debug("Sending OpenAI request with prompt \"" + prompt + "\" and temperature " + temperature);
	}
	String response = IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8.toString());
	if (log.isDebugEnabled()) {
	    log.debug(
		    "Received response from OpenAI:" + (StringUtils.isBlank(response) ? " <BLANK>" : "\n" + response));
	}

	ObjectNode responseJSON = JsonUtil.readObject(response);
	ArrayNode choicesArray = JsonUtil.optArray(responseJSON, "choices");
	String producedChoice = choicesArray.get(0).get("text").asText();
	return producedChoice;
    }

    private static List<Question> extractMcqQuestions(String text) {
	List<Question> result = new LinkedList<>();
	if (StringUtils.isBlank(text)) {
	    return result;
	}
	text = text.strip();

	String[] questionEntities = text.split("\\n\\n");
	Question question = null;
	for (String questionEntity : questionEntities) {
	    if (StringUtils.isBlank(questionEntity)) {
		continue;
	    }
	    questionEntity = questionEntity.strip().replace("\\n", "\n");
	    String[] questionParts = questionEntity.split("\\n");
	    for (String questionPart : questionParts) {
		questionPart = questionPart.strip();
		if (questionPart.matches("^\\d+\\..+")) {
		    question = new Question();
		    questionPart = questionPart.replaceFirst("\\d+\\.\\s*", "");
		    question.setTitle(questionPart);
		    question.setAnswers(new LinkedList<>());
		    result.add(question);
		} else if (questionPart.matches("^\\p{Alpha}\\..+")) {
		    if (question == null) {
			log.warn("Encountered answer without question: " + questionPart);
			continue;
		    }
		    Answer answer = new Answer();
		    question.getAnswers().add(answer);
		    answer.setDisplayOrder(question.getAnswers().size());
		    answer.setText(questionPart);
		    answer.setScore(0f);
		} else if (questionPart.startsWith("Answer: ")) {
		    if (question == null) {
			log.warn("Encountered correct answer without question: " + questionPart);
			continue;
		    }
		    questionPart = questionPart.strip().replaceFirst("Answer:\\s*", "");
		    boolean correctAnswerFound = false;
		    for (Answer answer : question.getAnswers()) {
			if (questionPart.equalsIgnoreCase(answer.getText())) {
			    answer.setScore(1f);
			    correctAnswerFound = true;
			}
			answer.setText(answer.getText().replaceFirst("^\\p{Alpha}\\.\\s*", ""));
		    }
		    if (!correctAnswerFound) {
			log.warn("Could not find correct answer among existing answers: " + questionPart);
		    }
		    question = null;
		}
	    }
	}

	return result;
    }
}