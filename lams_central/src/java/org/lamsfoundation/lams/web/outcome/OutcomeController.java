/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web.outcome;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.OutcomeScaleItem;
import org.lamsfoundation.lams.outcome.service.IOutcomeService;
import org.lamsfoundation.lams.outcome.service.OutcomeService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/outcome")
public class OutcomeController {
    private static Logger log = Logger.getLogger(OutcomeController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IOutcomeService outcomeService;
    @Autowired
    private ILessonService lessonService;

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @RequestMapping("/outcomeManage")
    public String outcomeManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "import outcomes", true);

	List<Outcome> outcomes = outcomeService.getOutcomes();
	request.setAttribute("outcomes", outcomes);
	return "outcome/outcomeManage";
    }

    @RequestMapping("/outcomeEdit")
    public String outcomeEdit(@ModelAttribute OutcomeForm outcomeForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "import outcomes", true);

	Long outcomeId = WebUtil.readLongParam(request, "outcomeId", true);
	Outcome outcome = outcomeId == null ? null : (Outcome) userManagementService.findById(Outcome.class, outcomeId);

	outcomeForm.setContentFolderId(IOutcomeService.OUTCOME_CONTENT_FOLDER_ID);
	if (outcome == null) {
	    outcomeForm.setScaleId(IOutcomeService.DEFAULT_SCALE_ID);
	} else {
	    outcomeForm.setOutcomeId(outcome.getOutcomeId());
	    outcomeForm.setName(outcome.getName());
	    outcomeForm.setCode(outcome.getCode());
	    outcomeForm.setDescription(outcome.getDescription());
	    outcomeForm.setScaleId(outcome.getScale().getScaleId());
	}

	List<OutcomeScale> scales = outcomeService.getScales();
	request.setAttribute("scales", scales);

	return "outcome/outcomeEdit";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/outcomeSave", method = RequestMethod.POST)
    public String outcomeSave(@ModelAttribute OutcomeForm outcomeForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = OutcomeController.getUserDTO().getUserID();
	Long outcomeId = outcomeForm.getOutcomeId();
	Outcome outcome = outcomeId == null ? null : (Outcome) userManagementService.findById(Outcome.class, outcomeId);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	validateOutcomeForm(outcomeForm, errorMap);
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	} else {
	    try {
		if (outcome == null) {
		    outcome = new Outcome();
		    User user = (User) userManagementService.findById(User.class, userId);
		    outcome.setCreateBy(user);
		    outcome.setCreateDateTime(new Date());
		}

		outcome.setName(outcomeForm.getName());
		outcome.setCode(outcomeForm.getCode());
		outcome.setDescription(outcomeForm.getDescription());
		long scaleId = outcomeForm.getScaleId() == null || outcomeForm.getScaleId() == 0
			? IOutcomeService.DEFAULT_SCALE_ID
			: outcomeForm.getScaleId();
		OutcomeScale scale = (OutcomeScale) userManagementService.findById(OutcomeScale.class, scaleId);
		outcome.setScale(scale);
		userManagementService.save(outcome);

		if (log.isDebugEnabled()) {
		    log.debug("Saved outcome " + outcome.getOutcomeId());
		}
		request.setAttribute("saved", true);
	    } catch (Exception e) {
		log.error("Exception while saving an outcome", e);
		errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error"));
		request.setAttribute("errorMap", errorMap);
	    }
	}

	List<OutcomeScale> scales = userManagementService.findAll(OutcomeScale.class);
	request.setAttribute("scales", scales);
	return "outcome/outcomeEdit";
    }

    @RequestMapping(path = "/outcomeRemove", method = RequestMethod.POST)
    public String outcomeRemove(HttpServletRequest request, HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "import outcomes", true);

	Long outcomeId = WebUtil.readLongParam(request, "outcomeId", false);
	Outcome outcome = (Outcome) userManagementService.findById(Outcome.class, outcomeId);
	if (outcome == null) {
	    throw new IllegalArgumentException("Can not find an outcome with ID " + outcomeId);
	}

	long mappingCount = outcomeService.countOutcomeMappings(outcomeId);
	if (mappingCount == 0) {
	    userManagementService.delete(outcome);
	    if (log.isDebugEnabled()) {
		log.debug("Deleted outcome " + outcomeId);
	    }
	} else {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.remove.error.in.use"));
	    request.setAttribute("errorMap", errorMap);
	}

	return outcomeManage(request, response);
    }

    @RequestMapping("/outcomeSearch")
    @ResponseBody
    public String outcomeSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
	String search = WebUtil.readStrParam(request, "term", true);
	boolean addEnabled = Configuration.getAsBoolean(ConfigurationKeys.LEARNING_OUTCOME_QUICK_ADD_ENABLE);

	List<Outcome> outcomes = outcomeService.getOutcomes(search);
	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (Outcome outcome : outcomes) {
	    ObjectNode outcomeJSON = JsonNodeFactory.instance.objectNode();
	    outcomeJSON.put("value", outcome.getOutcomeId());
	    outcomeJSON.put("label",
		    outcome.getName() + (StringUtils.isBlank(outcome.getCode()) ? "" : " (" + outcome.getCode() + ")"));
	    responseJSON.add(outcomeJSON);
	}
	responseJSON.add(search);
	responseJSON.add(String.valueOf(addEnabled));
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping(path = "/outcomeMap", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String outcomeMap(@RequestParam(required = false) Long outcomeId,
	    @RequestParam(required = false) Long lessonId, @RequestParam(required = false) Long toolContentId,
	    @RequestParam(required = false) Long itemId, @RequestParam(required = false) Integer qbQuestionId,
	    @RequestParam(required = false) String name) throws Exception {
	if (lessonId == null && toolContentId == null && qbQuestionId == null) {
	    throw new IllegalArgumentException(
		    "Either lesson ID or tool content ID or QB question ID must not be null when creating an outcome mapping");
	}
	Outcome outcome = null;
	if (outcomeId == null) {
	    boolean addEnabled = Configuration.getAsBoolean(ConfigurationKeys.LEARNING_OUTCOME_QUICK_ADD_ENABLE);
	    if (!addEnabled) {
		throw new SecurityException("Adding Learning Outcomes on the fly is disabled");
	    }
	    outcome = outcomeService.createOutcome(name, OutcomeController.getUserDTO().getUserID());

	} else {
	    outcome = (Outcome) userManagementService.findById(Outcome.class, outcomeId);
	}

	List<OutcomeMapping> outcomeMappings = outcomeService.getOutcomeMappings(lessonId, toolContentId, itemId,
		qbQuestionId);
	for (OutcomeMapping existingMapping : outcomeMappings) {
	    if (existingMapping.getOutcome().getOutcomeId().equals(outcome.getOutcomeId())) {
		throw new IllegalArgumentException(
			"Trying to map an already mapped outcome with ID " + outcome.getOutcomeId());
	    }
	}

	OutcomeMapping outcomeMapping = new OutcomeMapping();
	outcomeMapping.setOutcome(outcome);
	outcomeMapping.setLessonId(lessonId);
	outcomeMapping.setToolContentId(toolContentId);
	outcomeMapping.setItemId(itemId);
	outcomeMapping.setQbQuestionId(qbQuestionId);
	userManagementService.save(outcomeMapping);

	if (log.isDebugEnabled()) {
	    log.debug("Mapped outcome " + outcome.getOutcomeId() + " to lesson ID " + lessonId + " and tool content ID "
		    + toolContentId + " and item ID " + itemId + " and QB question ID " + qbQuestionId);
	}

	return String.valueOf(outcomeMapping.getMappingId());
    }

    @RequestMapping(path = "/outcomeGetMappings")
    @ResponseBody
    public String outcomeGetMappings(@RequestParam(required = false) Long lessonId,
	    @RequestParam(required = false) Long toolContentId, @RequestParam(required = false) Long itemId,
	    @RequestParam(required = false) Integer qbQuestionId, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	if (lessonId == null && toolContentId == null && qbQuestionId == null) {
	    throw new IllegalArgumentException(
		    "Either lesson ID or tool content ID or QB question ID must not be null when fetching outcome mappings");
	}
	Integer userId = OutcomeController.getUserDTO().getUserID();
	if (!request.isUserInRole(Role.SYSADMIN) && !request.isUserInRole(Role.AUTHOR)) {
	    String error = "User " + userId + " is not sysadmin nor an author and can not map outcome";
	    log.error(error);
	    throw new SecurityException(error);
	}

	List<OutcomeMapping> outcomeMappings = outcomeService.getOutcomeMappings(lessonId, toolContentId, itemId,
		qbQuestionId);
	OutcomeService.filterQuestionMappings(outcomeMappings);

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (OutcomeMapping outcomeMapping : outcomeMappings) {
	    Outcome outcome = outcomeMapping.getOutcome();
	    ObjectNode outcomeJSON = JsonNodeFactory.instance.objectNode();
	    outcomeJSON.put("mappingId", outcomeMapping.getMappingId());
	    outcomeJSON.put("outcomeId", outcome.getOutcomeId());
	    outcomeJSON.put("qbMapping", outcomeMapping.getQbQuestionId() != null);
	    outcomeJSON.put("label",
		    outcome.getName() + (StringUtils.isBlank(outcome.getCode()) ? "" : " (" + outcome.getCode() + ")"));
	    responseJSON.add(outcomeJSON);
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping(path = "/outcomeRemoveMapping", method = RequestMethod.POST)
    public void outcomeRemoveMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long mappingId = WebUtil.readLongParam(request, "mappingId");
	OutcomeMapping outcomeMapping = (OutcomeMapping) userManagementService.findById(OutcomeMapping.class,
		mappingId);
	userManagementService.delete(outcomeMapping);

	if (log.isDebugEnabled()) {
	    log.debug("Deleted outcome mapping " + mappingId);
	}
    }

    @RequestMapping("/outcomeSetResult")
    @ResponseBody
    public String outcomeSetResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long mappingId = WebUtil.readLongParam(request, "pk");
	Integer value = WebUtil.readIntParam(request, "value");
	Integer targetUserId = WebUtil.readIntParam(request, "name");
	OutcomeMapping outcomeMapping = (OutcomeMapping) userManagementService.findById(OutcomeMapping.class,
		mappingId);
	Long lessonId = outcomeMapping.getLessonId();
	if (lessonId == null) {
	    lessonId = lessonService.getLessonByToolContentId(outcomeMapping.getToolContentId()).getLessonId();
	}
	Integer userId = OutcomeController.getUserDTO().getUserID();
	securityService.isLessonMonitor(lessonId, userId, "set outcome result", true);

	OutcomeResult result = outcomeService.getOutcomeResult(userId, mappingId);
	if (result == null) {
	    // result does not exist; if value == -1, it means it is not meant to exist, otherwise create
	    if (value > -1) {
		result = new OutcomeResult();
		User user = (User) userManagementService.findById(User.class, userId);
		result.setCreateBy(user);
		result.setCreateDateTime(new Date());
		result.setMapping(outcomeMapping);
		User targetUser = (User) userManagementService.findById(User.class, targetUserId);
		result.setUser(targetUser);
		result.setValue(value);
		userManagementService.save(result);
		if (log.isDebugEnabled()) {
		    log.debug("Added outcome result " + result.getResultId());
		}
	    }
	    // modify only if value is different
	} else if (!result.getValue().equals(value)) {
	    // if value is -1, remove the result
	    if (value == -1) {
		Long resultId = result.getResultId();
		userManagementService.delete(result);
		if (log.isDebugEnabled()) {
		    log.debug("Deleted outcome result " + resultId);
		}
	    } else {
		// update existing result
		result.setValue(value);
		User user = (User) userManagementService.findById(User.class, userId);
		result.setCreateBy(user);
		result.setCreateBy(user);
		result.setValue(value);
		userManagementService.save(result);
		if (log.isDebugEnabled()) {
		    log.debug("Edited outcome result " + result.getResultId());
		}
	    }
	}

	// if something else than OK is sent, x-editable will print ERROR!
	response.setContentType("text/plain;charset=utf-8");
	return "OK";
    }

    @RequestMapping("/outcomeExport")
    public void outcomeExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "export outcomes", true);

	List<ExcelSheet> sheets = outcomeService.exportOutcomes();

	String fileName = "lams_outcomes.xls";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, messageService.getMessage("outcome.export.date"), true, false);
    }

    @RequestMapping("/outcomeImport")
    public String outcomeImport(@RequestParam("file") MultipartFile file, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "import outcomes", true);

	try {
	    int importCount = outcomeService.importOutcomes(file);
	    log.info("Imported " + importCount + " outcomes");
	} catch (Exception e) {
	    log.error("Error while importing outcomes", e);
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.import.error"));
	    request.setAttribute("errorMap", errorMap);
	}

	return outcomeManage(request, response);
    }

    @RequestMapping("/scaleManage")
    public String scaleManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
	List<OutcomeScale> scales = outcomeService.getScales();
	request.setAttribute("scales", scales);

	return "outcome/scaleManage";
    }

    @RequestMapping("/scaleRemove")
    public String scaleRemove(HttpServletRequest request, HttpServletResponse response) throws Exception {
	Long scaleId = WebUtil.readLongParam(request, "scaleId", false);
	OutcomeScale scale = (OutcomeScale) userManagementService.findById(OutcomeScale.class, scaleId);
	if (scale == null) {
	    throw new IllegalArgumentException("Can not find an outcome scale with ID " + scaleId);
	}

	long scaleUseCount = outcomeService.countScaleUse(scaleId);
	if (scaleUseCount == 0) {
	    userManagementService.delete(scale);
	    if (log.isDebugEnabled()) {
		log.debug("Deleted outcome scale " + scaleId);
	    }
	} else {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	    errorMap.add("GLOBAL", messageService.getMessage("scale.manage.remove.error.in.use"));
	    request.setAttribute("errorMap", errorMap);
	}

	return scaleManage(request, response);
    }

    @RequestMapping("/scaleEdit")
    public String scaleEdit(@ModelAttribute("scaleForm") OutcomeScaleForm scaleForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long scaleId = WebUtil.readLongParam(request, "scaleId", true);
	OutcomeScale scale = scaleId == null ? null
		: (OutcomeScale) userManagementService.findById(OutcomeScale.class, scaleId);

	scaleForm.setContentFolderId(IOutcomeService.OUTCOME_CONTENT_FOLDER_ID);
	if (scale != null) {
	    scaleForm.setScaleId(scale.getScaleId());
	    scaleForm.setName(scale.getName());
	    scaleForm.setCode(scale.getCode());
	    scaleForm.setDescription(scale.getDescription());
	    scaleForm.setItems(scale.getItemString());
	}

	request.setAttribute("isDefaultScale", outcomeService.isDefaultScale(scaleForm.getScaleId()));
	return "outcome/scaleEdit";
    }

    @RequestMapping("/scaleSave")
    public String scaleSave(@ModelAttribute("scaleForm") OutcomeScaleForm scaleForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = OutcomeController.getUserDTO().getUserID();
	Long scaleId = scaleForm.getScaleId();
	OutcomeScale scale = scaleId == null ? null
		: (OutcomeScale) userManagementService.findById(OutcomeScale.class, scaleId);
	if (scale != null && outcomeService.isDefaultScale(scaleId)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "The default scale can not be altered");
	    return null;
	}

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	validateScaleForm(scaleForm, errorMap);
	List<String> items = OutcomeScale.parseItems(scaleForm.getItems());
	if (items == null) {
	    errorMap.add("GLOBAL", messageService.getMessage("scale.manage.add.value.error.blank"));
	}
	if (errorMap.isEmpty()) {
	    try {
		if (scale == null) {
		    scale = new OutcomeScale();
		    User user = (User) userManagementService.findById(User.class, userId);
		    scale.setCreateBy(user);
		    scale.setCreateDateTime(new Date());
		}

		scale.setName(scaleForm.getName());
		scale.setCode(scaleForm.getCode());
		scale.setDescription(scaleForm.getDescription());
		userManagementService.save(scale);

		// find existing scales and add new ones
		Set<OutcomeScaleItem> newItems = new LinkedHashSet<>();
		int value = 0;
		for (String itemString : items) {
		    itemString = itemString.trim();
		    if (StringUtils.isBlank(itemString)) {
			errorMap.add("GLOBAL", messageService.getMessage("scale.manage.add.value.error.blank"));
			break;
		    }
		    OutcomeScaleItem item = null;
		    for (OutcomeScaleItem exisitngItem : scale.getItems()) {
			if (itemString.equals(exisitngItem.getName())) {
			    item = exisitngItem;
			    break;
			}
		    }
		    if (item == null) {
			item = new OutcomeScaleItem();
			item.setScale(scale);
			item.setName(itemString);
		    }
		    item.setValue(value++);
		    newItems.add(item);
		}
		if (errorMap.isEmpty()) {
		    scale.getItems().clear();
		    scale.getItems().addAll(newItems);
		    userManagementService.save(scale);

		    if (log.isDebugEnabled()) {
			log.debug("Saved outcome scale " + scale.getScaleId());
		    }

		    request.setAttribute("saved", true);
		}
	    } catch (Exception e) {
		log.error("Exception while saving an outcome", e);
		errorMap.add("GLOBAL", messageService.getMessage("scale.manage.add.error"));
	    }
	}
	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	}

	return "outcome/scaleEdit";
    }

    @RequestMapping("/scaleExport")
    public void scaleExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "export outcome scales", true);

	List<ExcelSheet> sheets = outcomeService.exportScales();

	String fileName = "lams_outcome_scales.xls";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	fileDownloadTokenCookie.setPath("/");
	response.addCookie(fileDownloadTokenCookie);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, messageService.getMessage("outcome.export.date"), true, false);
    }

    @RequestMapping("/scaleImport")
    public String scaleImport(@RequestParam("file") MultipartFile file, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	UserDTO user = OutcomeController.getUserDTO();
	securityService.isSysadmin(user.getUserID(), "import outcome scales", true);

	try {
	    int importCount = outcomeService.importScales(file);
	    log.info("Imported " + importCount + " outcome scales");
	} catch (Exception e) {
	    log.error("Error while importing outcome scales", e);
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>(1);
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.import.error"));
	    request.setAttribute("errorMap", errorMap);
	}

	return scaleManage(request, response);
    }

    private static UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    private void validateOutcomeForm(OutcomeForm outcomeForm, MultiValueMap<String, String> errorMap) {
	if (StringUtils.isBlank(outcomeForm.getName())) {
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error.name.blank"));
	}
	/**
	 * LDEV-4786 We allow blank code when creating an outcome straight from outcome select field
	 * if (StringUtils.isBlank(outcomeForm.getCode())) {
	 * errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error.code.blank"));
	 * }
	 * If no outcome is chosen, the default scale gets chosen
	 * if (outcomeForm.getScaleId() == null || outcomeForm.getScaleId() == 0) {
	 * errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error.scale.choose"));
	 * }
	 **/
    }

    private void validateScaleForm(OutcomeScaleForm scaleForm, MultiValueMap<String, String> errorMap) {
	if (StringUtils.isBlank(scaleForm.getName())) {
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error.name.blank"));
	}
	if (StringUtils.isBlank(scaleForm.getCode())) {
	    errorMap.add("GLOBAL", messageService.getMessage("outcome.manage.add.error.code.blank"));
	}
    }
}
