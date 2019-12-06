package org.lamsfoundation.lams.outcome.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.outcome.OutcomeScaleItem;
import org.lamsfoundation.lams.outcome.dao.IOutcomeDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.excel.ExcelCell;
import org.lamsfoundation.lams.util.excel.ExcelRow;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.multipart.MultipartFile;

public class OutcomeService implements IOutcomeService {
    private IOutcomeDAO outcomeDAO;
    private MessageService messageService;

    private static Logger log = Logger.getLogger(OutcomeService.class);

    @Override
    public List<Outcome> getOutcomes() {
	return outcomeDAO.getOutcomesSortedByName();
    }

    @Override
    public List<OutcomeScale> getScales() {
	return outcomeDAO.getScalesSortedByName();
    }

    @Override
    public List<Outcome> getOutcomes(String search) {
	return outcomeDAO.getOutcomesSortedByName(search);
    }

    @Override
    public List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId) {
	return outcomeDAO.getOutcomeMappings(lessonId, toolContentId, itemId);
    }

    @Override
    public long countOutcomeMappings(Long outcomeId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("outcome.outcomeId", outcomeId);
	return outcomeDAO.countByProperties(OutcomeMapping.class, properties);
    }

    @Override
    public long countScaleUse(Long scaleId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("scale.scaleId", scaleId);
	return outcomeDAO.countByProperties(Outcome.class, properties);
    }

    @Override
    public List<OutcomeResult> getOutcomeResults(Integer userId, Long lessonId, Long toolContentId, Long itemId) {
	return outcomeDAO.getOutcomeResults(userId, lessonId, toolContentId, itemId);
    }

    @Override
    public OutcomeResult getOutcomeResult(Integer userId, Long mappingId) {
	return outcomeDAO.getOutcomeResult(userId, mappingId);
    }

    @Override
    public OutcomeScale getDefaultScale() {
	return (OutcomeScale) outcomeDAO.find(OutcomeScale.class, DEFAULT_SCALE_ID);
    }

    @Override
    public boolean isDefaultScale(Long scaleId) {
	return scaleId != null && DEFAULT_SCALE_ID == scaleId;
    }

    @Override
    public void copyOutcomeMappings(Long sourceLessonId, Long sourceToolContentId, Long sourceItemId,
	    Long targetLessonId, Long targetToolContentId, Long targetItemId) {
	List<OutcomeMapping> sourceMappings = getOutcomeMappings(sourceLessonId, sourceToolContentId, sourceItemId);
	for (OutcomeMapping sourceMapping : sourceMappings) {
	    OutcomeMapping targetMapping = new OutcomeMapping();
	    targetMapping.setOutcome(sourceMapping.getOutcome());
	    targetMapping.setLessonId(targetLessonId);
	    targetMapping.setToolContentId(targetToolContentId);
	    targetMapping.setItemId(targetItemId);
	    outcomeDAO.insert(targetMapping);
	}
    }

    @Override
    public List<ExcelSheet> exportScales() {
	List<ExcelSheet> sheets = new LinkedList<ExcelSheet>();
	ExcelSheet scalesSheet = new ExcelSheet(messageService.getMessage("scale.title"));
 	sheets.add(scalesSheet);

 	ExcelRow headerRow = scalesSheet.initRow();
 	headerRow.addCell(messageService.getMessage("outcome.manage.add.name"), true);
 	headerRow.addCell(messageService.getMessage("outcome.manage.add.code"), true);
 	headerRow.addCell(messageService.getMessage("outcome.manage.add.description"), true);
 	headerRow.addCell(messageService.getMessage("scale.manage.add.value"), true);

	List<OutcomeScale> scales = getScales();
	for (OutcomeScale scale : scales) {
	    ExcelRow scaleRow = scalesSheet.initRow();
	    scaleRow.addCell(scale.getName());
	    scaleRow.addCell(scale.getCode());
	    scaleRow.addCell(scale.getDescription());
	    scaleRow.addCell(scale.getItemString());
	}

	return sheets;
    }

    @Override
    public List<ExcelSheet> exportOutcomes() {
	List<ExcelSheet> sheets = new LinkedList<ExcelSheet>();
	ExcelSheet outcomeSheet = new ExcelSheet(messageService.getMessage("index.outcome.manage"));
 	sheets.add(outcomeSheet);

	// The entire data list
	ExcelRow headerRow = outcomeSheet.initRow();
	headerRow.addCell(messageService.getMessage("outcome.manage.add.name"), true);
	headerRow.addCell(messageService.getMessage("outcome.manage.add.code"), true);
	headerRow.addCell(messageService.getMessage("outcome.manage.add.description"), true);
	headerRow.addCell(messageService.getMessage("outcome.manage.add.scale"), true);

	List<Outcome> outcomes = getOutcomes();
	for (Outcome outcome : outcomes) {
	    ExcelRow outcomeRow = outcomeSheet.initRow();
	    outcomeRow.addCell(outcome.getName());
	    outcomeRow.addCell(outcome.getCode());
	    outcomeRow.addCell(outcome.getDescription());
	    outcomeRow.addCell(outcome.getScale().getCode());
	}

	return sheets;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int importScales(MultipartFile fileItem) throws IOException {
	int counter = 0;
	POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
	try (HSSFWorkbook wb = new HSSFWorkbook(fs)) {
	    HSSFSheet sheet = wb.getSheetAt(0);
	    int startRow = sheet.getFirstRowNum();
	    int endRow = sheet.getLastRowNum();
	    User user = null;

	    // make import work with files with header ("exported on") or without (pure data)
	    HSSFRow row = sheet.getRow(startRow);
	    HSSFCell cell = row.getCell(0);
	    String header = cell.getStringCellValue();
	    startRow += "name".equalsIgnoreCase(header) ? 1 : 5;

	    for (int i = startRow; i < (endRow + 1); i++) {
		row = sheet.getRow(i);
		cell = row.getCell(0);
		String name = cell.getStringCellValue();
		cell = row.getCell(1);
		String code = cell.getStringCellValue();
		List<OutcomeScale> foundScales = outcomeDAO.findByProperty(OutcomeScale.class, "name", name);
		foundScales.addAll(outcomeDAO.findByProperty(OutcomeScale.class, "code", code));
		if (!foundScales.isEmpty()) {
		    if (log.isDebugEnabled()) {
			log.debug("Skipping an outcome scale with existing name \"" + name + "\" or code \"" + code
				+ "\"");
		    }
		    continue;
		}

		cell = row.getCell(2);
		String description = cell == null ? null : cell.getStringCellValue();
		cell = row.getCell(3);
		String itemsString = cell.getStringCellValue();

		OutcomeScale scale = new OutcomeScale();
		scale.setName(name);
		scale.setCode(code);
		scale.setDescription(description);
		if (user == null) {
		    UserDTO userDTO = OutcomeService.getUserDTO();
		    user = (User) outcomeDAO.find(User.class, userDTO.getUserID());
		}
		scale.setCreateBy(user);
		scale.setCreateDateTime(new Date());
		outcomeDAO.insert(scale);

		List<String> items = OutcomeScale.parseItems(itemsString);
		int value = 0;
		for (String itemString : items) {
		    OutcomeScaleItem item = new OutcomeScaleItem();
		    item.setName(itemString);
		    item.setValue(value++);
		    item.setScale(scale);
		    outcomeDAO.insert(item);
		}

		counter++;
	    }
	}
	return counter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int importOutcomes(MultipartFile fileItem) throws IOException {
	int counter = 0;
	POIFSFileSystem fs = new POIFSFileSystem(fileItem.getInputStream());
	try (HSSFWorkbook wb = new HSSFWorkbook(fs)) {
	    HSSFSheet sheet = wb.getSheetAt(0);
	    int startRow = sheet.getFirstRowNum();
	    int endRow = sheet.getLastRowNum();
	    User user = null;

	    // make import work with files with header ("exported on") or without (pure data)
	    HSSFRow row = sheet.getRow(startRow);
	    HSSFCell cell = row.getCell(0);
	    String header = cell.getStringCellValue();
	    startRow += "name".equalsIgnoreCase(header) ? 1 : 5;

	    for (int i = startRow; i < (endRow + 1); i++) {
		row = sheet.getRow(i);
		cell = row.getCell(0);
		String name = cell.getStringCellValue();
		cell = row.getCell(1);
		String code = cell.getStringCellValue();
		List<Outcome> foundOutcomes = outcomeDAO.findByProperty(Outcome.class, "name", name);
		foundOutcomes.addAll(outcomeDAO.findByProperty(Outcome.class, "code", code));
		if (!foundOutcomes.isEmpty()) {
		    if (log.isDebugEnabled()) {
			log.debug("Skipping an outcome with existing name \"" + name + "\" or code \"" + code + "\"");
		    }
		    continue;
		}
		cell = row.getCell(3);
		String scaleName = cell.getStringCellValue();
		List<OutcomeScale> foundScales = outcomeDAO.findByProperty(OutcomeScale.class, "name", scaleName);
		OutcomeScale scale = foundScales.isEmpty() ? null : foundScales.get(0);
		if (scale == null) {
		    if (log.isDebugEnabled()) {
			log.debug("Skipping an outcome with missing scale with name: " + scaleName);
		    }
		    continue;
		}

		cell = row.getCell(2);
		String description = cell == null ? null : cell.getStringCellValue();

		Outcome outcome = new Outcome();
		outcome.setName(name);
		outcome.setCode(code);
		outcome.setDescription(description);
		outcome.setScale(scale);
		if (user == null) {
		    UserDTO userDTO = OutcomeService.getUserDTO();
		    user = (User) outcomeDAO.find(User.class, userDTO.getUserID());
		}
		outcome.setCreateBy(user);
		outcome.setCreateDateTime(new Date());
		outcomeDAO.insert(outcome);

		counter++;
	    }
	}
	return counter;
    }

    private static UserDTO getUserDTO() {
	HttpSession ss = SessionManager.getSession();
	return (UserDTO) ss.getAttribute(AttributeNames.USER);
    }

    public void setOutcomeDAO(IOutcomeDAO outcomeDAO) {
	this.outcomeDAO = outcomeDAO;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }
}