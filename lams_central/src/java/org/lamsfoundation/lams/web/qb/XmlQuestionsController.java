package org.lamsfoundation.lams.web.qb;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Answer;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionExporter;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Exports and imports questions from the given collection in XML format.
 * 
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/xmlQuestions")
public class XmlQuestionsController {
    private static Logger log = Logger.getLogger(XmlQuestionsController.class);

    @Autowired
    private IQbService qbService;
    
    @Autowired
    private IUserManagementService userManagementService;

    /**
     * Initializes import questions page.
     */
    @RequestMapping("/initImportQuestionsXml")
    public String initImportQuestionsXml() throws ServletException {
	return "qb/importQuestionsXml";
    }

    /**
     * Imports questions into question bank from uploaded xml file.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/importQuestionsXml")
    @ResponseBody
    public void importQuestionsXml(@RequestParam("UPLOAD_FILE") MultipartFile file, HttpServletRequest request,
	    @RequestParam long collectionUid) throws ServletException {
	int questionId = qbService.getMaxQuestionId();
	
	List<String> toolsErrorMsgs = new ArrayList<>();
	try {
	    String uploadPath = FileUtil.createTempDirectory("_uploaded_2questions_xml");

	    // filename on the client
	    String filename = FileUtil.getFileName(file.getOriginalFilename());
	    File destinationFile = new File(uploadPath, filename);
	    file.transferTo(destinationFile);

	    String fileExtension = FileUtil.getFileExtension(filename);
	    if (!fileExtension.equalsIgnoreCase("xml")) {
		throw new RuntimeException("Wrong file extension. Xml is expected");
	    }
	    // String learningDesignPath = ZipFileUtil.expandZip(new FileInputStream(designFile), filename2);

	    // import learning design
	    String fullFilePath = destinationFile.getAbsolutePath();// FileUtil.getFullPath(learningDesignPath,
							       // ExportToolContentService.LEARNING_DESIGN_FILE_NAME);
	    List<QbQuestion> questions = (List<QbQuestion>) FileUtil.getObjectFromXML(null, fullFilePath);
	    if (questions != null) {
		for (QbQuestion qbQuestion : questions) {
		    qbQuestion.setQuestionId(++questionId);
		    qbQuestion.setCreateDate(new Date());
		    userManagementService.save(qbQuestion);

		    qbService.addQuestionToCollection(collectionUid, qbQuestion.getUid(), false);

		    if (log.isDebugEnabled()) {
			log.debug("Imported XML question. Name: " + qbQuestion.getName() + ", uid: "
				+ qbQuestion.getUid());
		    }
		}
	    }

	} catch (Exception e) {
	    log.error("Error occured during import", e);
	    toolsErrorMsgs.add(e.getClass().getName() + " " + e.getMessage());
	}

	if (toolsErrorMsgs.size() > 0) {
	    request.setAttribute("toolsErrorMessages", toolsErrorMsgs);
	}
    }

    /**
     * Exports xml format questions from question collection.
     */
    @RequestMapping("/exportQuestionsXml")
    public void exportQuestionsXml(HttpServletRequest request, HttpServletResponse response, @RequestParam long collectionUid) {
	List<QbQuestion> qbQuestions = qbService.getCollectionQuestions(collectionUid);
	String errors = null;
	try {
	    ArrayList<QbQuestion> questionsToExport = new ArrayList<>();
	    for (QbQuestion qbQuestion : qbQuestions) {
		QbQuestion clonedQuestion = (QbQuestion) qbQuestion.clone();
		clonedQuestion.clearID();
		clonedQuestion.setVersion(1);
		questionsToExport.add(clonedQuestion);
	    }
	    // exporting XML
	    XStream designXml = new XStream(new StaxDriver());
	    designXml.addPermission(AnyTypePermission.ANY);
	    String resultedXml = designXml.toXML(questionsToExport);

	    QbCollection collection = qbService.getCollectionByUid(collectionUid);
	    String fileTitle = collection.getName() + "_questions.xml";

	    response.setContentType("application/x-download");
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileTitle);
	    log.debug("Exporting assessment questions to an xml. Collection uid " + collectionUid);

	    OutputStream out = null;
	    try {
		out = response.getOutputStream();
		out.write(resultedXml.getBytes());
		int count = resultedXml.getBytes().length;
		log.debug("Wrote out " + count + " bytes");
		response.setContentLength(count);
		out.flush();
	    } catch (Exception e) {
		log.error("Exception occured writing out file:" + e.getMessage());
		throw new ExportToolContentException(e);
	    } finally {
		try {
		    if (out != null) {
			out.close();
		    }
		} catch (Exception e) {
		    log.error("Error Closing file. File already written out - no exception being thrown.", e);
		}
	    }

	} catch (Exception e) {
	    errors = "Unable to export tool content: " + e.toString();
	    log.error(errors);
	}

	if (errors != null) {
	    try {
		PrintWriter out = response.getWriter();
		out.write(errors);
		out.flush();
	    } catch (IOException e) {
	    }
	}
    }
}
