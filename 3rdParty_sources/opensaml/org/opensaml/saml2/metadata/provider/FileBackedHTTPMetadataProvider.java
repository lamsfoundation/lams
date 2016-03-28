/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.saml2.metadata.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;

import org.apache.commons.httpclient.HttpClient;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

/**
 * A URL metadata provider that caches a copy of the retrieved metadata to disk so that, in the event that the metadata
 * may not be pulled from the URL it may be pulled from disk using the last fetched data. If the backing file does not
 * already exist it will be created.
 * 
 * It is the responsibility of the caller to re-initialize, via {@link #initialize()}, if any properties of this
 * provider are changed.
 */
public class FileBackedHTTPMetadataProvider extends HTTPMetadataProvider {

    /** Class logger. */
    private final Logger log = LoggerFactory.getLogger(FileBackedHTTPMetadataProvider.class);

    /** File containing the backup of the metadata. */
    private File metadataBackupFile;

    /**
     * Constructor.
     * 
     * @param metadataURL the URL to fetch the metadata
     * @param requestTimeout the time, in milliseconds, to wait for the metadata server to respond
     * @param backupFilePath the file that will keep a backup copy of the metadata,
     * 
     * @throws MetadataProviderException thrown if the URL is not a valid URL, the metadata can not be retrieved from
     *             the URL
     */
    @Deprecated
    public FileBackedHTTPMetadataProvider(String metadataURL, int requestTimeout, String backupFilePath)
            throws MetadataProviderException {
        super(metadataURL, requestTimeout);
        setBackupFile(backupFilePath);
    }

    /**
     * Constructor.
     * 
     * @param client HTTP client used to fetch remove metadata
     * @param backgroundTaskTimer timer used to schedule background metadata refresh tasks
     * @param metadataURL the URL to fetch the metadata
     * @param backupFilePath the file that will keep a backup copy of the metadata,
     * 
     * @throws MetadataProviderException thrown if the URL is not a valid URL, the metadata can not be retrieved from
     *             the URL
     */
    public FileBackedHTTPMetadataProvider(Timer backgroundTaskTimer, HttpClient client, String metadataURL,
            String backupFilePath) throws MetadataProviderException {
        super(backgroundTaskTimer, client, metadataURL);
        setBackupFile(backupFilePath);
    }
    
    /** {@inheritDoc} */
    public synchronized void destroy() {
        metadataBackupFile = null;
        
        super.destroy();
    }

    /** {@inheritDoc} */
    protected void doInitialization() throws MetadataProviderException {
        try {
            validateBackupFile(metadataBackupFile);
        } catch (MetadataProviderException e) {
            if (isFailFastInitialization()) {
                log.error("Metadata backup file path was invalid, initialization is fatal");
                throw e;
            } else {
                log.error("Metadata backup file path was invalid, continuing without known good backup file");
            }
        }
        
        super.doInitialization();
    }

    /**
     * Sets the file used to backup metadata. The given file path is checked to see if it is a read/writable file if it
     * exists or if can be created if it does not exist.
     * 
     * @param backupFilePath path to the backup file
     * 
     * @throws MetadataProviderException thrown if the backup file is not read/writable or creatable
     */
    protected void setBackupFile(String backupFilePath) throws MetadataProviderException {
        File backingFile = new File(backupFilePath);
        metadataBackupFile = backingFile;
    }

    /**
     * Validate the basic properties of the specified metadata backup file, for example that it 
     * exists and/or can be created; that it is not a directory; and that it is readable and writable.
     *
     * @param backupFile the file to evaluate
     * @throws MetadataProviderException if file does not pass basic properties required of a metadata backup file
     */
    protected void validateBackupFile(File backupFile) throws MetadataProviderException {
        if (!backupFile.exists()) {
            try {
                backupFile.createNewFile();
            } catch (IOException e) {
                log.error("Unable to create backing file " + backupFile, e);
                throw new MetadataProviderException("Unable to create backing file " + backupFile.getAbsolutePath(), e);
            }
        }

        if (backupFile.isDirectory()) {
            throw new MetadataProviderException("Filepath " + backupFile.getAbsolutePath()
                    + " is a directory and may not be used as a backing metadata file");
        }

        if (!backupFile.canRead()) {
            throw new MetadataProviderException("Filepath " + backupFile.getAbsolutePath()
                    + " exists but can not be read by this user");
        }

        if (!backupFile.canWrite()) {
            throw new MetadataProviderException("Filepath " + backupFile.getAbsolutePath()
                    + " exists but can not be written to by this user");
        }
    }

    /** {@inheritDoc} */
    protected byte[] fetchMetadata() throws MetadataProviderException {
        try {
            return super.fetchMetadata();
        } catch (MetadataProviderException e) {
            if (metadataBackupFile.exists()) {
                log.warn("Problem reading metadata from remote source, processing existing backup file: {}", 
                        metadataBackupFile.getAbsolutePath());
                try {
                    return DatatypeHelper.fileToByteArray(metadataBackupFile);
                } catch (IOException ioe) {
                    String errMsg = "Unable to retrieve metadata from backup file "
                            + metadataBackupFile.getAbsolutePath();
                    log.error(errMsg, ioe);
                    throw new MetadataProviderException(errMsg, ioe);
                }
            } else {
                log.error("Unable to read metadata from remote server and backup does not exist");
                throw new MetadataProviderException(
                        "Unable to read metadata from remote server and backup does not exist");
            }
        }
    }

    /** {@inheritDoc} */
    protected void postProcessMetadata(byte[] metadataBytes, Document metadataDom, XMLObject metadata)
            throws MetadataProviderException {
        try {
            validateBackupFile(metadataBackupFile);
            FileOutputStream out = new FileOutputStream(metadataBackupFile);
            out.write(metadataBytes);
            out.flush();
            out.close();
        } catch (MetadataProviderException e) {
            log.error("Unable to write metadata to backup file: " + metadataBackupFile.getAbsoluteFile(), e);
        } catch (IOException e) {
            log.error("Unable to write metadata to backup file: " + metadataBackupFile.getAbsoluteFile(), e);
        } finally {
            super.postProcessMetadata(metadataBytes, metadataDom, metadata);
        }
    }
}