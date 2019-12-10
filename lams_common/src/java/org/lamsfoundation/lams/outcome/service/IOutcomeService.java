package org.lamsfoundation.lams.outcome.service;

import java.io.IOException;
import java.util.List;

import org.lamsfoundation.lams.outcome.Outcome;
import org.lamsfoundation.lams.outcome.OutcomeMapping;
import org.lamsfoundation.lams.outcome.OutcomeResult;
import org.lamsfoundation.lams.outcome.OutcomeScale;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.springframework.web.multipart.MultipartFile;

public interface IOutcomeService {
    static final long DEFAULT_SCALE_ID = 1;

    // just a hardcoded, random number
    static final String OUTCOME_CONTENT_FOLDER_ID = "outcomeo-utco-meou-tcom-eoutcomeoutc";

    List<Outcome> getOutcomes();

    List<Outcome> getOutcomes(String search);

    List<OutcomeMapping> getOutcomeMappings(Long lessonId, Long toolContentId, Long itemId, Integer qbQuestionId);

    long countOutcomeMappings(Long outcomeId);

    long countScaleUse(Long scaleId);

    List<OutcomeScale> getScales();

    List<OutcomeResult> getOutcomeResults(Integer userId, Long lessonId, Long toolContentId, Long itemId);

    OutcomeResult getOutcomeResult(Integer userId, Long mappingId);

    OutcomeScale getDefaultScale();

    boolean isDefaultScale(Long scaleId);

    void copyOutcomeMappings(Long sourceLessonId, Long sourceToolContentId, Long sourceItemId,
	    Integer sourceQbQuestionId, Long targetLessonId, Long targetToolContentId, Long targetItemId,
	    Integer targetQbQuestionId);

    List<ExcelSheet> exportScales();

    List<ExcelSheet> exportOutcomes();

    int importScales(MultipartFile fileItem) throws IOException;

    int importOutcomes(MultipartFile fileItem) throws IOException;
}