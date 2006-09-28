package org.lamsfoundation.lams.notebook.service;

import java.util.List;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;

public interface IExtendedCoreNotebookService extends ICoreNotebookService {
	List<NotebookEntry> getEntry(Integer userID);
	List<NotebookEntry> getEntry(Integer userID, Integer idType);
	List<NotebookEntry> getEntry(Integer userID, Long lessonID);
}
