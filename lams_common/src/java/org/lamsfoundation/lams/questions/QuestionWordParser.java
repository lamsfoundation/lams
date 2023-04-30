package org.lamsfoundation.lams.questions;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;

/**
 * Extract questions and answers from Microsoft Word format. They can be later used in question-based tools.
 *
 * @author Andrey Balan
 */
public class QuestionWordParser {
    private static Logger log = Logger.getLogger(QuestionWordParser.class);

    private final static String QUESTION_TAG = "question:";
    private final static String ANSWER_TAG = "answer:";
    private final static String CORRECT_TAG = "correct:";
    private final static String INCORRECT_TAG = "incorrect:";
    private final static String MARK_TAG = "mark:";
    private final static String MARK_HEDGING_TAG = "markhedging:";
    private final static String FEEDBACK_TAG = "feedback:";
    private final static String LEARNING_OUTCOME_TAG = "lo:";
    private static final String CUSTOM_IMAGE_TAG_REGEX = "\\[IMAGE: .*?]";
    private static final String NON_BREAK_SPACE_REGEX = "(^\\h*)|(\\h*$)";

    /**
     * Extracts questions from IMS QTI zip file.
     */
    public static Question[] parseWordFile(InputStream uploadedFileStream, String fileName, Set<String> limitType)
	    throws XPathExpressionException, ZipFileUtilException, TransformerConfigurationException, IOException,
	    SAXException, TikaException, ParserConfigurationException {
	final String TEMP_IMAGE_FOLDER = ZipFileUtil.prepareTempDirectory(fileName);
	TikaInputStream input = TikaInputStream.get(uploadedFileStream);
	OOXMLParser tikaParser = new OOXMLParser();

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
	TransformerHandler handler = factory.newTransformerHandler();
	handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
	handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
	handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	handler.setResult(new StreamResult(out));
	ContentHandler contentHandler = new TikaImageRewritingContentHandler(handler);
	Metadata metadata = new Metadata();
	// parse context needs to extract images
	ParseContext parseContext = new ParseContext();
	parseContext.set(Parser.class, new TikaImageExtractingParser(TEMP_IMAGE_FOLDER));
	tikaParser.parse(input, contentHandler, metadata, parseContext);
	String xml = new String(out.toByteArray(), "UTF-8");
	//Tika has this bug of putting b and u tags in the wrong order
	xml = xml.replaceAll("</b></u>", "</u></b>");

	//parse resulted xml
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = docBuilder.parse(new InputSource(new StringReader(xml)));
	DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
	LSSerializer serializer = domImplLS.createLSSerializer();
	serializer.getDomConfig().setParameter("xml-declaration", false);
	XPath xPath = XPathFactory.newInstance().newXPath();

	//convert all <img> tags into {IMAGE} for further processing by QuestionParser.processHTMLField(..)
	NodeList images = (NodeList) xPath.evaluate("//img[@src]", doc, XPathConstants.NODESET);
	// Set the node content
	for (int i = 0; i < images.getLength(); i++) {
	    Node image = images.item(i);
	    String src = image.getAttributes().getNamedItem("src").getNodeValue();
	    StringBuilder updatedImageTagText = new StringBuilder("[IMAGE: ").append(src);
	    Node altText = image.getAttributes().getNamedItem("alt");
	    if (altText != null) {
		updatedImageTagText.append("| alt=\"").append(altText.getNodeValue().replace('"', '\'')).append("\"");
	    }
	    updatedImageTagText.append("]");
	    Text updatedImageTag = doc.createTextNode(updatedImageTagText.toString());
	    image.getParentNode().insertBefore(updatedImageTag, image);
	    image.getParentNode().removeChild(image);
	}

	//get root-level <p> and <table>
	String expression = "/html/body/p|/html/body/table";
	NodeList nodes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

	//go through all paragraphs and search for questions divided by QUESTION_BREAK
	List<Question> questions = new LinkedList<>();
	int counter = 0;
	String line = QuestionWordParser.readNextLine(serializer, nodes, counter++);
	while (line != null) {
	    String strippedLine = line == null ? "" : WebUtil.removeHTMLtags(line).toLowerCase();

	    // skip content outside question
	    if (!strippedLine.startsWith(QUESTION_TAG)) {
		line = QuestionWordParser.readNextLine(serializer, nodes, counter++);
		continue;
	    }

	    // iterate through the next paragraphs until we meet QUESTION_BREAK or the end of file
	    List<Node> questionParagraphs = new ArrayList<>();
	    do {
		Node lineNode = nodes.item(counter - 1);
		questionParagraphs.add(lineNode);
		line = QuestionWordParser.readNextLine(serializer, nodes, counter++);
		strippedLine = line == null ? "" : WebUtil.removeHTMLtags(line).toLowerCase();

	    } while (line != null && !strippedLine.startsWith(QUESTION_TAG));

	    String title = null;
	    String description = null;
	    String mark = null;
	    boolean isMarkHedging = false;
	    String feedback = null;
	    List<Answer> answers = new ArrayList<>();
	    Answer correctVsaAnswer = null;
	    Answer incorrectVsaAnswer = null;
	    List<String> learningOutcomes = new ArrayList<>();

	    boolean optionsStarted = false;
	    boolean feedbackStarted = false;
	    boolean isMultipleResponse = false;
	    boolean answerTagFound = false;
	    for (Node questionParagraph : questionParagraphs) {
		// formatted text that includes starting and ending <p> as well as all children tags
		String formattedText = serializer.writeToString(questionParagraph);
		formattedText = QuestionWordParser.strip(formattedText);
		//text without HTML tags
		String text = questionParagraph.getTextContent().toLowerCase();
		text = QuestionWordParser.strip(text);
		boolean isTypeParagraph = "p".equals(questionParagraph.getNodeName());

		if (StringUtils.isBlank(text) && !questionParagraph.hasChildNodes()) {
		    //skip empty paragraphs
		    continue;
		}

		// check if answers section started
		if (!feedbackStarted && !answerTagFound && isTypeParagraph && text.matches("^[a-z]\\).*")) {
		    optionsStarted = true;

		    //process a-z) answers
		    //remove <p> formatting "a-z)"
		    formattedText = formattedText.replaceFirst("^<p.*>\\s*[a-zA-Z]\\)", "").replace("</p>", "");
		    formattedText = QuestionWordParser.strip(formattedText);

		    Answer answer = new Answer();
		    answer.setText(formattedText);
		    answer.setDisplayOrder(answers.size() + 1);
		    answers.add(answer);
		    continue;
		}

		// check if MCQ answer line is found
		if (text.startsWith(ANSWER_TAG)) {
		    answerTagFound = true;
		    if (correctVsaAnswer != null || incorrectVsaAnswer != null) {
			// question is malformed, display an error after this loop
			break;
		    }

		    optionsStarted = true;
		    feedbackStarted = false;

		    String correctAnswerLetters = text.replaceAll("(?i)" + ANSWER_TAG, "").replaceAll("\\s", "");
		    String[] correctAnswersTable = correctAnswerLetters.split(",");

		    for (String correctAnswerLetter : correctAnswersTable) {
			char correctAnswerChar = correctAnswerLetter.charAt(0);
			int correctAnswerIndex = correctAnswerChar - 'a' + 1;

			for (Answer answer : answers) {
			    if (answer.getDisplayOrder() == correctAnswerIndex) {
				//correct answer
				answer.setScore(1f);
				isMultipleResponse |= correctAnswersTable.length > 1;
			    }
			}
		    }

		    continue;
		}

		// check if VSA answer line is found
		if (text.startsWith(CORRECT_TAG) || text.startsWith(INCORRECT_TAG)) {
		    optionsStarted = true;
		    feedbackStarted = false;

		    String vsaAnswers = WebUtil.removeHTMLtags(formattedText).replaceAll("(?i)" + CORRECT_TAG, "")
			    .replaceAll("(?i)" + INCORRECT_TAG, "");
		    vsaAnswers = QuestionWordParser.strip(vsaAnswers);

		    Answer answer = new Answer();
		    answer.setText(vsaAnswers);
		    // there are only two answers, correct one and incorrect one
		    if (text.startsWith(CORRECT_TAG)) {
			answer.setDisplayOrder(1);
			answer.setScore(1F);
			correctVsaAnswer = answer;
		    } else {
			answer.setDisplayOrder(2);
			incorrectVsaAnswer = answer;
		    }

		    continue;
		}

		if (text.startsWith(LEARNING_OUTCOME_TAG)) {
		    optionsStarted = true;
		    feedbackStarted = false;

		    String learningOutcome = WebUtil.removeHTMLtags(formattedText)
			    .replaceAll("(?i)" + LEARNING_OUTCOME_TAG + "\\s*", "");
		    learningOutcome = QuestionWordParser.strip(learningOutcome);

		    learningOutcomes.add(learningOutcome);
		    continue;
		}

		if (text.startsWith(MARK_TAG)) {
		    optionsStarted = true;
		    feedbackStarted = false;

		    mark = WebUtil.removeHTMLtags(text).replaceAll("(?i)" + MARK_TAG + "\\s*", "");
		    mark = QuestionWordParser.strip(mark);
		    continue;
		}

		if (text.startsWith(MARK_HEDGING_TAG)) {
		    optionsStarted = true;
		    feedbackStarted = false;

		    String markHedging = WebUtil.removeHTMLtags(text)
			    .replaceAll("(?i)" + MARK_HEDGING_TAG + "\\s*", "");
		    markHedging = QuestionWordParser.strip(markHedging);
		    isMarkHedging = Boolean.valueOf(markHedging);
		    continue;
		}

		if (text.startsWith(FEEDBACK_TAG)) {
		    optionsStarted = true;
		    feedbackStarted = true;
		}

		if (feedbackStarted) {
		    String strippedFormattedText = formattedText.replaceAll("(?i)" + FEEDBACK_TAG + "\\s*", "");
		    strippedFormattedText = QuestionWordParser.strip(strippedFormattedText);
		    feedback = feedback == null ? strippedFormattedText : feedback + strippedFormattedText;
		    continue;
		}

		if (!optionsStarted) {
		    // if we are still before all options and no answers section started,
		    // then interpret it as question title or description
		    if (text.startsWith(QUESTION_TAG)) {
			title = WebUtil.removeHTMLtags(formattedText).replaceAll("(?i)" + QUESTION_TAG + "\\s*", "");
			title = QuestionWordParser.strip(title);
			continue;
		    }
		    String strippedFormattedText = QuestionWordParser.strip(formattedText);
		    description = description == null ? strippedFormattedText : description + strippedFormattedText;
		}
	    }

	    if (StringUtils.isBlank(title)) {
		if (StringUtils.isBlank(description)) {
		    log.error("No question title found. Skipping question.");
		    continue;
		}
		// remove "[IMAGE: ]" tags
		title = description.replaceAll(QuestionWordParser.CUSTOM_IMAGE_TAG_REGEX, "");
		// remove all HTML tags
		title = WebUtil.removeHTMLtags(title);
		// trim to 80 characters while preserving the last full word
		title = title.replaceAll("(?<=.{80})\\b.*", "...");
	    }

	    if (answers.isEmpty() && answerTagFound) {
		log.error("ANSWER tag found, but no answers were found in question: " + title);
		continue;
	    }
	    if (!answers.isEmpty() && !answerTagFound) {
		log.error("Answers were found, but no ANSWER tag was found in question: " + title);
		continue;
	    }

	    if (answerTagFound && (correctVsaAnswer != null || incorrectVsaAnswer != null)) {
		log.error(
			"ANSWER tag found, but also CORRECT and/or INCORRECT tag found. Can not categorise the question as MCQ or VSA: "
				+ title);
		continue;
	    }

	    if (isMarkHedging && (correctVsaAnswer != null || incorrectVsaAnswer != null)) {
		log.error(
			"MarkHedging tag found, but also CORRECT and/or INCORRECT tag found. Can not categorise the question as mark hedging or VSA. "
				+ title);
		continue;
	    }

	    if (isMarkHedging && answers.isEmpty()) {
		log.error("MarkHedging tag found, but  no answers were found in question: " + title);
		continue;
	    }

	    if (isMarkHedging && isMultipleResponse) {
		log.error("MarkHedging question must only have one correct answer in question: " + title);
		continue;
	    }

	    Question question = new Question();
	    if (correctVsaAnswer != null) {
		if (!QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_FILL_IN_BLANK, limitType,
			question)) {
		    continue;
		}
		answers.add(correctVsaAnswer);
		if (incorrectVsaAnswer != null) {
		    answers.add(incorrectVsaAnswer);
		}
		question.setAnswers(answers);
	    } else if (answers.isEmpty()) {
		if (!QuestionParser.isQuestionTypeAcceptable(Question.QUESTION_TYPE_ESSAY, limitType, question)) {
		    continue;
		}
	    } else {
		String type = Question.QUESTION_TYPE_MULTIPLE_CHOICE;
		if (isMultipleResponse) {
		    type = Question.QUESTION_TYPE_MULTIPLE_RESPONSE;
		} else if (isMarkHedging) {
		    type = Question.QUESTION_TYPE_MARK_HEDGING;
		}
		if (!QuestionParser.isQuestionTypeAcceptable(type, limitType, question)) {
		    continue;
		}
		question.setAnswers(answers);
	    }

	    if (mark != null) {
		try {
		    question.setScore(Integer.valueOf(mark));
		} catch (Exception e) {
		    log.error("Malformed mark, it must be an integer number: " + mark);
		    continue;
		}
	    }

	    question.setResourcesFolderPath(TEMP_IMAGE_FOLDER);
	    question.setTitle(title);
	    question.setText(description);
	    question.setFeedback(feedback);
	    question.setLearningOutcomes(learningOutcomes);

	    questions.add(question);
	}

	return questions.toArray(Question.QUESTION_ARRAY_TYPE);
    }

    private static String readNextLine(LSSerializer serializer, NodeList nodes, int counter) {
	//check it's not the end of file
	if (counter >= nodes.getLength()) {
	    return null;
	}

	Node node = nodes.item(counter);
	String htmlText = serializer.writeToString(node);
	htmlText = QuestionWordParser.strip(htmlText);
	log.debug("Reading the next line from word document: " + htmlText);
	return htmlText;
    }

    private static String strip(String input) {
	return input == null ? null : input.strip().replaceAll(NON_BREAK_SPACE_REGEX, "");
    }

    /**
     * A nested Tika parser which extracts out any images as they come along.
     */
    @SuppressWarnings("serial")
    private static class TikaImageExtractingParser implements Parser {
	private Set<MediaType> types;

	private String imgFolder = null;

	private TikaImageExtractingParser(String imgFolder) {
	    // Our expected types
	    types = new HashSet<>();
	    types.add(MediaType.image("bmp"));
	    types.add(MediaType.image("gif"));
	    types.add(MediaType.image("jpg"));
	    types.add(MediaType.image("jpeg"));
	    types.add(MediaType.image("png"));
	    types.add(MediaType.image("tiff"));

	    this.imgFolder = imgFolder;
	}

	@Override
	public Set<MediaType> getSupportedTypes(ParseContext context) {
	    return types;
	}

	@Override
	public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context)
		throws IOException, SAXException, TikaException {
	    // Is it a supported image?
	    String filename = metadata.get(TikaCoreProperties.RESOURCE_NAME_KEY);
	    String type = metadata.get(HttpHeaders.CONTENT_TYPE);
	    boolean accept = false;

	    if (type != null) {
		for (MediaType mt : types) {
		    if (mt.toString().equals(type)) {
			accept = true;
		    }
		}
	    }
	    if (filename != null) {
		for (MediaType mt : types) {
		    String ext = "." + mt.getSubtype();
		    if (filename.endsWith(ext)) {
			accept = true;
		    }
		}
	    }

	    if (!accept) {
		return;
	    }

	    // Give it a sensible name if needed
	    if (filename == null) {
		filename = "image-" + System.currentTimeMillis() + ".";
		filename += type.substring(type.indexOf('/') + 1);
	    }

	    // Save the image
	    File targetFile = new File(imgFolder + "/" + filename);
	    FileUtils.copyInputStreamToFile(stream, targetFile);
	}
    }

    /**
     * A content handler that re-writes image "src" and table "class" attributes, passes everything else on to the real
     * one.
     */
    private static class TikaImageRewritingContentHandler extends ContentHandlerDecorator {
	private TikaImageRewritingContentHandler(ContentHandler handler) {
	    super(handler);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes origAttrs) throws SAXException {
	    // If we have an image tag, re-write the src attribute if required
	    if ("img".equals(localName)) {
		AttributesImpl attrs;
		if (origAttrs instanceof AttributesImpl) {
		    attrs = (AttributesImpl) origAttrs;
		} else {
		    attrs = new AttributesImpl(origAttrs);
		}

		for (int i = 0; i < attrs.getLength(); i++) {
		    if ("src".equals(attrs.getLocalName(i))) {
			String src = attrs.getValue(i);
			if (src.startsWith("embedded:")) {
			    String newSrc = src.substring(src.indexOf(':') + 1);
			    attrs.setValue(i, newSrc);
			}
		    }
		}
		super.startElement(uri, localName, qName, attrs);

	    } else if ("table".equals(localName)) {
		AttributesImpl attributes;
		if (origAttrs instanceof AttributesImpl) {
		    attributes = (AttributesImpl) origAttrs;
		} else {
		    attributes = new AttributesImpl(origAttrs);
		}

		boolean classAttributeMet = false;
		for (int i = 0; i < attributes.getLength(); i++) {
		    if ("class".equals(attributes.getLocalName(i))) {
			classAttributeMet = true;
			String classAttribute = attributes.getValue(i);
			classAttribute += " table-striped";
			attributes.setValue(i, classAttribute);
		    }
		}
		if (!classAttributeMet) {
		    attributes = new AttributesImpl(origAttrs);
		    attributes.addAttribute("", "class", "class", "CDATA", "table-striped");
		}
		super.startElement(uri, localName, qName, attributes);

	    } else {
		// For any other tag, pass through as-is
		super.startElement(uri, localName, qName, origAttrs);
	    }
	}
    }
}