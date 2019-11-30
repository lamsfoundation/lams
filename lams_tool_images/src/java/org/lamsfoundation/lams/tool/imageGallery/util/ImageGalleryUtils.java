package org.lamsfoundation.lams.tool.imageGallery.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.imageGallery.web.form.ImageGalleryItemForm;
import org.lamsfoundation.lams.tool.imageGallery.web.form.MultipleImagesForm;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

public class ImageGalleryUtils {

    /**
     * Validate imageGallery item.
     */
    public static MultiValueMap<String, String> validateImageGalleryItem(ImageGalleryItemForm itemForm,
	    boolean largeFile, MessageService messageService) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// validate file size
	FileValidatorUtil.validateFileSize(itemForm.getFile(), largeFile);
	// for edit validate: file already exist
	if (!itemForm.isHasFile()
		&& ((itemForm.getFile() == null) || StringUtils.isEmpty(itemForm.getFile().getOriginalFilename()))) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.resource.item.file.blank"));
	}

	// check for allowed format : gif, png, jpg
	if (itemForm.getFile() != null) {
	    String contentType = itemForm.getFile().getContentType();
	    if (ImageGalleryUtils.isContentTypeForbidden(contentType)) {
		errorMap.add("GLOBAL", messageService.getMessage("error.resource.image.not.alowed.format"));
	    }
	}

	return errorMap;
    }

    /**
     * Validate imageGallery item.
     */
    public static MultiValueMap<String, String> validateMultipleImages(MultipleImagesForm multipleForm,
	    boolean largeFile, MessageService messageService) {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	List<MultipartFile> fileList = ImageGalleryUtils.createFileListFromMultipleForm(multipleForm);

	// validate files size
	for (MultipartFile file : fileList) {
	    FileValidatorUtil.validateFileSize(file, largeFile);

	    // check for allowed format : gif, png, jpg
	    String contentType = file.getContentType();
	    if (ImageGalleryUtils.isContentTypeForbidden(contentType)) {
		errorMap.add("GLOBAL", messageService.getMessage("error.resource.image.not.alowed.format.for"));
	    }
	}

	return errorMap;
    }

    /**
     * Create file list from multiple form.
     */
    public static List<MultipartFile> createFileListFromMultipleForm(MultipleImagesForm multipleForm) {

	List<MultipartFile> fileList = new ArrayList<>();
	if (multipleForm.getFile1() != null && !StringUtils.isEmpty(multipleForm.getFile1().getOriginalFilename())) {
	    fileList.add(multipleForm.getFile1());
	}
	if (multipleForm.getFile2() != null && !StringUtils.isEmpty(multipleForm.getFile2().getOriginalFilename())) {
	    fileList.add(multipleForm.getFile2());
	}
	if (multipleForm.getFile3() != null && !StringUtils.isEmpty(multipleForm.getFile3().getOriginalFilename())) {
	    fileList.add(multipleForm.getFile3());
	}
	if (multipleForm.getFile4() != null && !StringUtils.isEmpty(multipleForm.getFile4().getOriginalFilename())) {
	    fileList.add(multipleForm.getFile4());
	}
	if (multipleForm.getFile5() != null && !StringUtils.isEmpty(multipleForm.getFile5().getOriginalFilename())) {
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