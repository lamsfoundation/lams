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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;

/** Routines shared by the 2.0 import/export and 1.0.2 import. */
public class ImportExportUtil {

    protected static Logger log = Logger.getLogger(ImportExportUtil.class);

    // encryption / descryption varibles
    private final static String ENC_CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    private final static String ENCODED_KEY_BASE64 = "emEjkQfjGZs=";
    private final static String KEY_IV_BASE64 = "AToFlf/Ue40=";
    private static Key secretKey = null;
    private static AlgorithmParameterSpec algorithmParameterSpec = null;

    /**
     * If the learning design has duplicated name in same folder, then rename it with timestamp.
     * the new name format will be oldname_ddMMYYYY_idx. The idx will be auto incremental index number, start from 1.
     */
    public static String generateUniqueLDTitle(WorkspaceFolder folder, String titleFromFile,
	    ILearningDesignDAO learningDesignDAO) {

	String newTitle = titleFromFile;
	if (newTitle == null || newTitle.length() == 0) {
	    newTitle = "unknown";
	}

	if (folder != null) {
	    boolean dupName;
	    List<LearningDesign> ldList = learningDesignDAO
		    .getAllLearningDesignsInFolder(folder.getWorkspaceFolderId());
	    int idx = 1;

	    //contruct middle part of name by timestamp
	    Calendar calendar = Calendar.getInstance();
	    int mth = calendar.get(Calendar.MONTH) + 1;
	    String mthStr = new Integer(mth).toString();
	    if (mth < 10) {
		mthStr = "0" + mthStr;
	    }
	    int day = calendar.get(Calendar.DAY_OF_MONTH);
	    String dayStr = new Integer(day).toString();
	    if (day < 10) {
		dayStr = "0" + dayStr;
	    }
	    String nameMid = dayStr + mthStr + calendar.get(Calendar.YEAR);
	    while (true) {
		dupName = false;
		for (LearningDesign eld : ldList) {
		    if (StringUtils.equals(eld.getTitle(), newTitle)) {
			dupName = true;
			break;
		    }
		}
		if (!dupName) {
		    break;
		}
		newTitle = titleFromFile + "_" + nameMid + "_" + idx;
		idx++;
	    }
	}

	return newTitle;
    }

    private static synchronized void initialiseDecryption() throws GeneralSecurityException {
	if (secretKey == null) {
	    byte[] encodedKey = Base64.decodeBase64(ENCODED_KEY_BASE64.getBytes());
	    byte[] keyIv = Base64.decodeBase64(KEY_IV_BASE64.getBytes());

	    DESKeySpec desKeySpec = new DESKeySpec(encodedKey);
	    SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
	    secretKey = factory.generateSecret(desKeySpec); // class SecretKey
	    algorithmParameterSpec = new IvParameterSpec(keyIv);
	}
    }

    /**
     * Decrypt Import packet. Exports were encrypted back in LAMS 1.0beta6 and prior but
     * encryption is not longer supported. We will not be supporting encryption of exports
     * in the future - this code is here only to support the importing of old encrypted designs.
     * 
     * @param fileContent
     * @return String
     */
    public static String decryptImport(String fileContent) throws GeneralSecurityException {

	ImportExportUtil.initialiseDecryption();

	int start = fileContent.indexOf("<CipherValue>") + 13;
	int end = fileContent.indexOf("</CipherValue>");
	String cipherValue = fileContent.substring(start, end);
	log.debug("  cipherValue= " + cipherValue);

	byte[] encryptedBytes = Base64.decodeBase64(cipherValue.getBytes());

	String decrypted = ImportExportUtil.getDecrypted(encryptedBytes);
	log.debug("  cipherValue= " + decrypted);

	return decrypted;
    }

    /**
     * Method getDecripted.
     * 
     * @param cipherValue
     * @return String
     */
    private static String getDecrypted(byte[] encryptedBytes) throws SecurityException {
	try {
	    Cipher cipher = Cipher.getInstance(ENC_CIPHER_ALGORITHM);
	    cipher.init(Cipher.DECRYPT_MODE, secretKey, algorithmParameterSpec);

	    byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
	    return new String(decryptedBytes);
	}

	catch (Exception e) {
	    log.error(e);
	    throw new SecurityException(e.getMessage());
	}
    }
}
