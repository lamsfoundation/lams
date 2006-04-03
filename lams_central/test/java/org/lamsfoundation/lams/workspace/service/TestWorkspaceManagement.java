package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;

/**
 * @author Manpreet Minhas
 */
public class TestWorkspaceManagement extends BaseWorkspaceTest {
	
	private static final Integer MACQ_UNI_WORKSPACE_FOLDER = new Integer(2);
	private static final Integer MELCOE_WORKSPACE_FOLDER = new Integer(3);
	private static final Integer LAMS_WORKSPACE_FOLDER = new Integer(4);
	private static final Integer MANPREETS_WORKSPACE_FOLDER = new Integer(6);
	//private static final Integer TO_DELETE_WORKSPACE_FOLDER = new Integer(7);
	//private static final Long    LONG_DELETE_WORKSPACE_FOLDER = new Long(7);
	private static final Integer DOCUMENTS_WORKSPACE_FOLDER = new Integer(8);
	private static final Integer LONG_DOCUMENTS_WORKSPACE_FOLDER = new Integer(8);
	private static final Integer PICTURES_WORKSPACE_FOLDER = new Integer(9);
	
	private static final Integer MANPREETS_WORKSPACE = new Integer(6);
	private static final Integer USER_ID = new Integer(4);

	public TestWorkspaceManagement(String name){
		super(name);
	}
    
	public void testGetAccessibleWorkspaceFolders()throws IOException{
		Vector vector = workspaceManagementService.getAccessibleOrganisationWorkspaceFolders(USER_ID);
		assertTrue("Accessible workspace folders exists",vector!=null && vector.size()>0);
	}
	public void testGetFolderContents()throws Exception{
		WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(LAMS_WORKSPACE_FOLDER);
		Vector vector = workspaceManagementService.getFolderContents(USER_ID,folder,WorkspaceManagementService.AUTHORING);		
		assertTrue("FolderContents exist",vector!=null && vector.size()>0);
	}
	
	// TODO why does copyfolder seem to take so long
	public void testCopyFolder() throws IOException{
		String packet = workspaceManagementService.copyFolder(MACQ_UNI_WORKSPACE_FOLDER,DOCUMENTS_WORKSPACE_FOLDER,USER_ID);

		Map ids = extractIdMapFromWDDXPacket(packet);
		assertTrue("Two ids returned as expected", ids != null && ids.size()==2);
		Double folderId = (Double) ids.get("workspaceFolderID");
		Double workspaceId = (Double) ids.get("workspaceID");
		assertEquals("workspace id in packet as expected",workspaceId.longValue(),MANPREETS_WORKSPACE.longValue());

		WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(folderId.intValue()));
		assertNotNull(folder);	
		assertEquals(folder.getParentWorkspaceFolder().getWorkspaceFolderId(),DOCUMENTS_WORKSPACE_FOLDER);
		assertEquals(folder.getWorkspaceID(), MANPREETS_WORKSPACE);
	}
	public void testDeleteFolder() throws Exception{
		// create a folder so we can delete it.
		String folderName = "testDeleteFolder";
		String packet = workspaceManagementService.createFolderForFlash(MANPREETS_WORKSPACE_FOLDER,folderName,USER_ID);
		Map ids = extractIdMapFromWDDXPacket(packet);
		assertNotNull(ids);
		Double id = (Double) ids.get("folderID");
		String packetFolderName = (String) ids.get("name");
		assertTrue("Folder name starts with "+folderName+". May have an added 'C'", 
				packetFolderName.startsWith(folderName));

		// okay - got the right folder. now delete it.
		Integer folderId = new Integer(id.intValue());
		workspaceManagementService.deleteFolder(folderId, USER_ID);
		try{
			workspaceFolderDAO.getWorkspaceFolderByID(folderId);
			fail("Exception should be raised because this object has already been deleted");
		}catch(HibernateObjectRetrievalFailureException he){
			assertTrue(true);
		}		
	} 	
	public void testMoveFolder()throws Exception{
		WorkspaceFolder originalWorkspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(MANPREETS_WORKSPACE_FOLDER);
		assertNotNull(originalWorkspaceFolder);
		
		// Work out a workspace folder we can move it to. By not hardcoding the destination folder, this
		// test will be valid no matter how many times it is run, as the copied design will just
		// move from workspace to workspace folder
		int newWorkspaceFolderId = originalWorkspaceFolder.getParentWorkspaceFolder().getWorkspaceFolderId().intValue() + 1;
		if ( newWorkspaceFolderId > PICTURES_WORKSPACE_FOLDER.intValue() ) {
			// max folder that we know about - cycle back to first folder.
			newWorkspaceFolderId = MACQ_UNI_WORKSPACE_FOLDER.intValue();
		}
		
		workspaceManagementService.moveFolder(MANPREETS_WORKSPACE_FOLDER,new Integer(newWorkspaceFolderId), USER_ID);				
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(MANPREETS_WORKSPACE_FOLDER);
		assertEquals(workspaceFolder.getParentWorkspaceFolder().getWorkspaceFolderId().intValue(), newWorkspaceFolderId);
	}
	public void testCreateWorkspaceFolderContent()throws Exception{
		// name must be unique or it will throw a key error.
		String name = "testCreateWorkspaceFolderContent"+System.currentTimeMillis();
		String message = workspaceManagementService.createWorkspaceFolderContent(WorkspaceFolderContent.CONTENT_TYPE_FILE,
				name,"Manpreet's Description",
				MELCOE_WORKSPACE_FOLDER,
				"TXT",testFileString);
		System.out.println(message);		
		
		NodeKey nk = extractNodeKeyFromWDDXPacket(message);
		assertNotNull("New content is uuid is populated", nk.getUuid());
		assertTrue("New content is version 1", nk.getVersion() != null && nk.getVersion().longValue() == 1);
	}
	/**This method just creates different versions of the given file. Checks the version in both 
	 * the wddx packet and the database object. */
	public void testUpdateWorkspaceFolderContent() throws Exception{

		// create some content to play with. name must be unique or it will throw a key error.
		String name = "testUpdateWorkspaceFolderContent"+System.currentTimeMillis();
		String message = workspaceManagementService.createWorkspaceFolderContent(
				WorkspaceFolderContent.CONTENT_TYPE_FILE,
				name,"This content is to be updated.",
				DOCUMENTS_WORKSPACE_FOLDER,
				"TXT",testFileString);

		System.out.print(message);

		// return value is the usual uuid, version + folderContentId
		// the following code does the same thing twice, but its the easy way to get the uuid and version
		NodeKey nk = extractNodeKeyFromWDDXPacket(message);
		Map map = extractIdMapFromWDDXPacket(message);
		Double mapFolderContentId = (Double) map.get("folderContentID");

		assertNotNull("New content is uuid is populated", nk.getUuid());
		assertTrue("New content is version 1", nk.getVersion() != null && nk.getVersion().longValue() == 1);
		assertTrue("New content is in document folder", mapFolderContentId != null && mapFolderContentId.intValue() == DOCUMENTS_WORKSPACE_FOLDER.intValue());
		WorkspaceFolderContent content = getMatchingContent(nk, LONG_DOCUMENTS_WORKSPACE_FOLDER);
		assertNotNull("Matching content found", content);
		Long folderContentId = content.getFolderContentID();
		
		// update the content 4 times.
		workspaceManagementService.updateWorkspaceFolderContent(folderContentId,testFileString);
		workspaceManagementService.updateWorkspaceFolderContent(folderContentId,testFileString);
		workspaceManagementService.updateWorkspaceFolderContent(folderContentId,testFileString);
		message = workspaceManagementService.updateWorkspaceFolderContent(folderContentId,testFileString);		
		System.out.print(message);

		// check the updated content is now version 5.
		WorkspaceFolderContent content2 = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentId);

		nk = extractNodeKeyFromWDDXPacket(message);
		assertTrue("ids returned", nk != null);
		assertEquals("content is version 5", nk.getVersion().longValue(), 5);
		assertEquals("packet uuid matches content value", content2.getUuid(), nk.getUuid());
		assertEquals("packet version matches content value", content2.getVersionID(), nk.getVersion());

		// delete a version and make sure previous version is picked up.
		workspaceManagementService.deleteContentWithVersion(nk.getUuid(),nk.getVersion(),folderContentId);
		nk = extractNodeKeyFromWDDXPacket(message);
		assertEquals("packet has deleted version 5", nk.getVersion().longValue(), 5);

		WorkspaceFolderContent content3 = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentId);
		assertEquals(content3.getVersionID().longValue(),4);
		assertEquals("packet uuid matches content value", content3.getUuid(), nk.getUuid());
		assertEquals("content value has current max version", content3.getVersionID().longValue(), 4);

	}
	
	public void testDeleteWorkspaceFolderContent() throws Exception{
		// create one to start with, so we can delete it. name must be unique or it will throw a key error.
		String name = "testDeleteWorkspaceFolderContent"+System.currentTimeMillis();
		String message = workspaceManagementService.createWorkspaceFolderContent(
				WorkspaceFolderContent.CONTENT_TYPE_FILE,
				name,"This content is to be deleted.",
				DOCUMENTS_WORKSPACE_FOLDER,
				"TXT",testFileString);

		NodeKey nk = extractNodeKeyFromWDDXPacket(message);
		assertNotNull("New content is uuid is populated", nk.getUuid());
		assertTrue("New content is version 1", nk.getVersion() != null && nk.getVersion().longValue() == 1);

		WorkspaceFolderContent content = getMatchingContent(nk, LONG_DOCUMENTS_WORKSPACE_FOLDER);
		assertNotNull("Matching content found", content);
		assertEquals(content.getVersionID(),nk.getVersion());
		System.out.print(message);

		// okay, we found the content, so now we can delete it.
		Long folderContentId = content.getFolderContentID();
		workspaceManagementService.deleteWorkspaceFolderContent(folderContentId);
		try{
			workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentId);
			fail("Exception should be raised because this object has already been deleted");
		}catch(HibernateObjectRetrievalFailureException he) {
			assertTrue(true);
		} 
		
		assertNull("Deleted content can't be found via getContentByWorkspaceFolder()", getMatchingContent(nk, LONG_DOCUMENTS_WORKSPACE_FOLDER));
	}		

	/**
	 * @param nk
	 * @param content
	 * @return
	 */
	private WorkspaceFolderContent getMatchingContent(NodeKey nk, Integer folderId) {
		assertNotNull(nk);
		assertNotNull(folderId);
		WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(folderId);
		Set content = folder.getFolderContent();
		assertNotNull(content);
		Iterator iter = content.iterator();
		while (iter.hasNext()) {
			WorkspaceFolderContent element = (WorkspaceFolderContent) iter.next();
			if ( element.getUuid().equals(nk.getUuid())) {
				return element; 
			}
		}
		return null;
	}
/*
	public void testMoveRenameLearningdesign()throws IOException{

		LearningDesign originalDesign = learningDesignDAO.getLearningDesignById(LD_ID);
		assertNotNull(originalDesign);
		assertNotNull(originalDesign.getWorkspaceFolder());
		// expect workspace folder id to be 2!
		
		// Work out a workspace folder we can move it to. By not hardcoding the destination folder, this
		// test will be valid no matter how many times it is run, as the copied design will just
		// move from workspace to workspace folder. Also generate a new name.
		int newWorkspaceFolderId = originalDesign.getWorkspaceFolder().getWorkspaceFolderId().intValue() + 1;
		if ( newWorkspaceFolderId > PICTURES_WORKSPACE_FOLDER.intValue() ) {
			// max folder that we know about - cycle back to first folder.
			newWorkspaceFolderId = MACQ_UNI_WORKSPACE_FOLDER.intValue();
		}
		String newTitle = originalDesign.getTitle()+"X";
		
		// okay, now move to our candidate directory and check the move went okay
		String packet = workspaceManagementService.moveLearningDesign(LD_ID, new Integer(newWorkspaceFolderId),USER_ID);
		Long newFolderId = extractIdFromWDDXPacket(packet);
		
		LearningDesign movedDesign = learningDesignDAO.getLearningDesignById(LD_ID);
		assertNotNull("learning design can be retrieved", movedDesign);
		assertEquals(movedDesign.getWorkspaceFolder().getWorkspaceFolderId().intValue(), newWorkspaceFolderId);

		workspaceManagementService.renameLearningDesign(LD_ID,newTitle,USER_ID);
		LearningDesign renamedDesign = learningDesignDAO.getLearningDesignById(LD_ID);
		assertNotNull("learning design can be retrieved (2)", renamedDesign);
		assertEquals(renamedDesign.getTitle(), newTitle);		
	}
	
	public void testRenameWorkspaceFolder() throws IOException{
		// generate a new name for the folder... this will work even if the test is run 
		// multiple times (until the field runs out of space), unlike just giving it a 
		// known name, which is an invalid test after the first run.
		WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(PICTURES_WORKSPACE_FOLDER);
		String newName = folder.getName() + "test";

		String packet = workspaceManagementService.renameWorkspaceFolder(PICTURES_WORKSPACE_FOLDER,newName, USER_ID);
		folder = workspaceFolderDAO.getWorkspaceFolderByID(PICTURES_WORKSPACE_FOLDER);
		assertEquals(folder.getName(),newName);		
	}
	
	public void testGetWorkspace()throws IOException{
		String packet  = workspaceManagementService.getWorkspace(USER_ID);
		//System.out.println("User workspace details: " + packet);	
	} 
	public void testCreateFolder()throws IOException{
		// create the new folder
		String folderName = "testCreateFolder";
		String packet = workspaceManagementService.createFolderForFlash(MANPREETS_WORKSPACE_FOLDER,folderName,USER_ID);

		// check that the output packet has an id and the expected name
		Map ids = extractIdMapFromWDDXPacket(packet);
		assertNotNull(ids);
		Double id = (Double) ids.get("folderID");
		String packetFolderName = (String) ids.get("name");
		assertTrue("Folder name starts with "+folderName+". May have an added 'C'", 
				packetFolderName.startsWith(folderName));

		// check we can find the created folder.
		WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(new Integer(id.intValue()));
		assertNotNull(folder);
		assertEquals(packetFolderName, folder.getName());
		
	}
	*/
	/**
	 * Given a WDDX packet in our normal format, return the NodeKey object in the 
	 * messageValue parameter. This is needed for createWorkspaceFolderContent,
	 * updateWorkspaceFolderContent
	 * 
     * @param wddxPacket
     * @return NodeKey
     */
    public NodeKey extractNodeKeyFromWDDXPacket(String wddxPacket) {
    	
    	Map map = extractIdMapFromWDDXPacket(wddxPacket);
    	// It may be a nodekey but it comes out as a map...
    	Double uuid = (Double) map.get("uuid"); 
    	assertNotNull(uuid);
    	Double version = (Double) map.get("version"); 
    	assertNotNull(version);

    	return new NodeKey(new Long(uuid.longValue()), new Long(version.longValue()));
	}

	
}
