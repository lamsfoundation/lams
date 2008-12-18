package org.lamsfoundation.lams.tool.videoRecorder.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;

/**
 * 
 * @author Paul Georges
 *
 */
public class VideoRecorderRecordingComparator implements Comparator<VideoRecorderRecordingDTO> {

	public int compare(VideoRecorderRecordingDTO dto1, VideoRecorderRecordingDTO dto2) {
		/*
		VideoRecorderRecordingDTO dto1 = (VideoRecorderRecordingDTO) o1;
		VideoRecorderRecordingDTO dto2 = (VideoRecorderRecordingDTO) o2;
		*/
		Long userId1 = dto1.getCreateBy().getUid();
		Long userId2 = dto2.getCreateBy().getUid();
		
		if(userId1 > userId2) {
			return -1;
		} else if(userId1 == userId2) {
			return 0;
		} else {
			return 1;
		}
	}
}
