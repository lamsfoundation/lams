package org.lamsfoundation.lams.tool.imageGallery.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItem;

/**
 *
 * @author steven
 *
 */
public class ImageGalleryItemComparator implements Comparator<ImageGalleryItem> {

    @Override
    public int compare(ImageGalleryItem o1, ImageGalleryItem o2) {
	if (o1 != null && o2 != null) {
	    return o1.getSequenceId() - o2.getSequenceId();
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}
