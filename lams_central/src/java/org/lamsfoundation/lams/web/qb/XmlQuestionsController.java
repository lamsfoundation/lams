package org.lamsfoundation.lams.web.qb;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(path = "/importQuestionsXml", method = RequestMethod.POST)
    @ResponseBody
    public void importQuestionsXml(@RequestParam("UPLOAD_FILE") MultipartFile file, HttpServletRequest request,
	    @RequestParam long collectionUid) throws ServletException {
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

	    // import learning design
	    String fullFilePath = destinationFile.getAbsolutePath();
	    // String learningDesignPath = ZipFileUtil.expandZip(new FileInputStream(designFile), filename2);
	    // FileUtil.getFullPath(learningDesignPath, ExportToolContentService.LEARNING_DESIGN_FILE_NAME);
	    List<QbQuestion> questions = (List<QbQuestion>) FileUtil.getObjectFromXML(null, fullFilePath);
	    if (questions != null) {
		Set<UUID> collectionUUIDs = null;

		for (QbQuestion qbQuestion : questions) {
		    UUID uuid = qbQuestion.getUuid();
		    // try to match the question to an existing QB question in DB
		    if (uuid != null) {
			QbQuestion existingQuestion = qbService.getQuestionByUUID(uuid);
			if (existingQuestion != null) {
			    // found an existing question with same UUID
			    // now check if it is in the collection already
			    if (collectionUUIDs == null) {
				// get UUIDs of collection questions
				collectionUUIDs = qbService.getCollectionQuestions(collectionUid).stream()
					.filter(q -> q.getUuid() != null)
					.collect(Collectors.mapping(q -> q.getUuid(), Collectors.toSet()));
			    }

			    if (collectionUUIDs.contains(uuid)) {
				if (log.isDebugEnabled()) {
				    log.debug("Skipping an existing question. Name: " + existingQuestion.getName()
					    + ", uid: " + existingQuestion.getUid());
				}
			    } else {
				qbService.addQuestionToCollection(collectionUid, existingQuestion.getQuestionId(),
					false);
				collectionUUIDs.add(uuid);

				if (log.isDebugEnabled()) {
				    log.debug("Added to collection an existing question. Name: "
					    + existingQuestion.getName() + ", uid: " + existingQuestion.getUid());
				}
			    }
			    continue;
			}
		    }

		    int questionId = qbService.generateNextQuestionId();
		    qbQuestion.setQuestionId(questionId);
		    qbQuestion.setCreateDate(new Date());
		    userManagementService.save(qbQuestion);

		    qbService.addQuestionToCollection(collectionUid, qbQuestion.getQuestionId(), false);

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
    @RequestMapping(path = "/exportQuestionsXml", method = RequestMethod.POST)
    public void exportQuestionsXml(HttpServletRequest request, HttpServletResponse response,
	    @RequestParam long collectionUid) {
	List<QbQuestion> qbQuestions = qbService.getCollectionQuestions(collectionUid);
	String errors = null;
	try {
	    ArrayList<QbQuestion> questionsToExport = new ArrayList<>();
	    for (QbQuestion qbQuestion : qbQuestions) {
		QbQuestion clonedQuestion = qbQuestion.clone();
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
