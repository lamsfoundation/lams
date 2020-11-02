package org.lamsfoundation.lams.questions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.HttpHeaders;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaMetadataKeys;
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

/**
 * Extract questions and answers from Microsoft Word format. They can be later used in question-based tools.
 *
 * @author Andrey Balan
 */
public class QuestionWordParser {
    private static Logger log = Logger.getLogger(QuestionWordParser.class);

    private final static String QUESTION_TAG = "question:";
    private final static String ANSWER_TAG = "answer:";
    private final static String FEEDBACK_TAG = "feedback:";
    private final static String LEARNING_OUTCOME_TAG = "lo:";
    private static final String CUSTOM_IMAGE_TAG_REGEX = "\\[IMAGE: .*?]";

    /**
     * Extracts questions from IMS QTI zip file.
     */
    public static Question[] parseWordFile(InputStream uploadedFileStream, String fileName)
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
	    Text updatedImageTag = doc.createTextNode("[IMAGE: " + src + "]");
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
	    List<Answer> answers = new ArrayList<>();
	    String feedback = null;

	    boolean optionsStarted = false;
	    boolean isMultipleResponse = false;
	    boolean answerTagFound = false;
	    for (Node questionParagraph : questionParagraphs) {
		// formatted text that includes starting and ending <p> as well as all children tags
		String formattedText = serializer.writeToString(questionParagraph).strip();
		//text without HTML tags
		String text = questionParagraph.getTextContent().strip().toLowerCase();
		boolean isTypeParagraph = "p".equals(questionParagraph.getNodeName());

		// check if answers section started
		if (isTypeParagraph && text.matches("^[a-z]\\).*")) {
		    optionsStarted = true;

		    //process a-z) answers
		    //remove <p> formatting "a-z)"
		    formattedText = formattedText.replaceFirst("^<p.*>\\s*[a-zA-Z]\\)", "").replace("</p>", "").strip();

		    Answer answer = new Answer();
		    answer.setText(formattedText);
		    answer.setDisplayOrder(answers.size() + 1);
		    answers.add(answer);
		    continue;
		}

		// check if correct answer line is found
		if (text.startsWith(ANSWER_TAG)) {
		    optionsStarted = true;
		    answerTagFound = true;

		    String correctAnswerLetters = text.substring(ANSWER_TAG.length()).replaceAll("\\s", "");
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

		if (text.startsWith(FEEDBACK_TAG)) {
		    optionsStarted = true;

		    feedback = feedback == null ? formattedText : feedback + formattedText;
		}

		// if we are still before all options and no answers section started,
		// then interpret it as question title or description
		if (!optionsStarted) {
		    if (text.startsWith(QUESTION_TAG)) {
			title = text.substring(QUESTION_TAG.length()).strip();
			continue;
		    }
		    description = description == null ? formattedText : description + formattedText;
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

	    Question question = new Question();
	    question.setResourcesFolderPath(TEMP_IMAGE_FOLDER);
	    question.setTitle(title);
	    question.setText(description);
	    question.setFeedback(feedback);
	    if (answers.isEmpty()) {
		question.setType(Question.QUESTION_TYPE_ESSAY);
	    } else {
		question.setAnswers(answers);
		if (isMultipleResponse) {
		    question.setType(Question.QUESTION_TYPE_MULTIPLE_RESPONSE);
		} else {
		    question.setType(Question.QUESTION_TYPE_MULTIPLE_CHOICE);
		}
	    }
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
	String htmlText = serializer.writeToString(node).strip();
	log.debug("Reading the next line from word document: " + htmlText);
	return htmlText;
    }

//    private static String parseUsingAutoDetect(String filename, TikaConfig tikaConfig, Metadata metadata)
//	    throws Exception {
//	System.out.println("Handling using AutoDetectParser: [" + filename + "]");
//
//	OOXMLParser parser = new OOXMLParser();
//	ToXMLContentHandler handler = new ToXMLContentHandler();
//	TikaInputStream stream = TikaInputStream.get(new File(filename), metadata);
//	parser.parse(stream, handler, metadata, new ParseContext());
//	return handler.toString();
//    }
//
//    private static String parseUsingComponents(String filename, TikaConfig tikaConfig, Metadata metadata)
//	    throws Exception {
//	MimeTypes mimeRegistry = tikaConfig.getMimeRepository();
//
//	System.out.println("Examining: [" + filename + "]");
//
//	metadata.set(Metadata.RESOURCE_NAME_KEY, filename);
//	System.out.println("The MIME type (based on filename) is: [" + mimeRegistry.detect(null, metadata) + "]");
//
//	InputStream stream = TikaInputStream.get(new File(filename));
//	System.out.println("The MIME type (based on MAGIC) is: [" + mimeRegistry.detect(stream, metadata) + "]");
//
//	stream = TikaInputStream.get(new File(filename));
//	Detector detector = tikaConfig.getDetector();
//	System.out.println(
//		"The MIME type (based on the Detector interface) is: [" + detector.detect(stream, metadata) + "]");
//
//	LanguageIdentifier lang = new LanguageIdentifier(
//		new LanguageProfile(FileUtils.readFileToString(new File(filename), UTF_8)));
//
//	System.out.println("The language of this content is: [" + lang.getLanguage() + "]");
//
//// Get a non-detecting parser that handles all the types it can
//	OOXMLParser parser = new OOXMLParser();
//// Tell it what we think the content is
//	MediaType type = detector.detect(stream, metadata);
//	metadata.set(Metadata.CONTENT_TYPE, type.toString());
//// Have the file parsed to get the content and metadata
//	ToXMLContentHandler handler = new ToXMLContentHandler();
//	parser.parse(stream, handler, metadata, new ParseContext());
//
//	return handler.toString();
//    }

    /**
     * A nested Tika parser which extracts out any images as they come along.
     */
    @SuppressWarnings("serial")
    private static class TikaImageExtractingParser implements Parser {
	private Set<MediaType> types;

	private String imgFolder = null;
	private int count = 0;

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
	    String filename = metadata.get(TikaMetadataKeys.RESOURCE_NAME_KEY);
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

	    count++;

	    // Give it a sensible name if needed
	    if (filename == null) {
		filename = "image-" + count + ".";
		filename += type.substring(type.indexOf('/') + 1);
	    }

	    // Save the image
	    File targetFile = new File(imgFolder + "/" + filename);
	    FileUtils.copyInputStreamToFile(stream, targetFile);
	}
    }

    /**
     * A content handler that re-writes image "src" and table "class" attributes,
     * passes everything else on to the real one.
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
