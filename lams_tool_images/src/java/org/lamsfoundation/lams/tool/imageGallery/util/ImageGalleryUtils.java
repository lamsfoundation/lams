package org.lamsfoundation.lams.tool.imageGallery.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.imageGallery.ImageGalleryConstants;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.MultipleImagesForm;
import org.lamsfoundation.lams.util.FileValidatorUtil;

public class ImageGalleryUtils {
    /**
     * Validate imageGallery item.
     *
     * @param itemForm
     * @return
     */
    public static ActionErrors validateImageGalleryItem(ImageGalleryItemForm itemForm, boolean largeFile) {
	ActionErrors errors = new ActionErrors();

	// validate file size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), largeFile, errors);
	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getFileName()))) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(ImageGalleryConstants.ERROR_MSG_FILE_BLANK));
	}

	// check for allowed format : gif, png, jpg
	if (itemForm.getFile() != null) {
	    String contentType = itemForm.getFile().getContentType();
	    if (isContentTypeForbidden(contentType)) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(ImageGalleryConstants.ERROR_MSG_NOT_ALLOWED_FORMAT));
	    }
	}

	return errors;
    }

    /**
     * Validate imageGallery item.
     *
     * @param multipleForm
     * @return
     */
    public static ActionErrors validateMultipleImages(MultipleImagesForm multipleForm, boolean largeFile) {
	ActionErrors errors = new ActionErrors();

	List<FormFile> fileList = createFileListFromMultipleForm(multipleForm);

	// validate files size
	for (FormFile file : fileList) {
	    FileValidatorUtil.validateFileSize(file, largeFile, errors);

	    // check for allowed format : gif, png, jpg
	    String contentType = file.getContentType();
	    if (isContentTypeForbidden(contentType)) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(ImageGalleryConstants.ERROR_MSG_NOT_ALLOWED_FORMAT_FOR, file.getFileName()));
	    }
	}

	return errors;
    }

    /**
     * Create file list from multiple form.
     *
     * @param multipleForm
     * @return
     */
    public static List<FormFile> createFileListFromMultipleForm(MultipleImagesForm multipleForm) {

	List<FormFile> fileList = new ArrayList<FormFile>();
	if (multipleForm.getFile1() != null && !StringUtils.isEmpty(multipleForm.getFile1().getFileName())) {
	    fileList.add(multipleForm.getFile1());
	}
	if (multipleForm.getFile2() != null && !StringUtils.isEmpty(multipleForm.getFile2().getFileName())) {
	    fileList.add(multipleForm.getFile2());
	}
	if (multipleForm.getFile3() != null && !StringUtils.isEmpty(multipleForm.getFile3().getFileName())) {
	    fileList.add(multipleForm.getFile3());
	}
	if (multipleForm.getFile4() != null && !StringUtils.isEmpty(multipleForm.getFile4().getFileName())) {
	    fileList.add(multipleForm.getFile4());
	}
	if (multipleForm.getFile5() != null && !StringUtils.isEmpty(multipleForm.getFile5().getFileName())) {
	    fileList.add(multipleForm.getFile5());
	}

	return fileList;
    }

    /**
     * Checks if the format is allowed.
     *
     * @param contentType
     * @return
     */
    private static boolean isContentTypeForbidden(String contentType) {
	boolean isContentTypeForbidden = StringUtils.isEmpty(contentType) || !(contentType.equals("image/gif")
		|| contentType.equals("image/png") || contentType.equals("image/jpg")
		|| contentType.equals("image/jpeg") || contentType.equals("image/pjpeg"));

	return isContentTypeForbidden;
    }
}
