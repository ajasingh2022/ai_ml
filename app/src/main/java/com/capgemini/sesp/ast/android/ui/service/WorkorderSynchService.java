package com.capgemini.sesp.ast.android.ui.service;

import android.os.AsyncTask;
import android.util.Log;

import com.capgemini.sesp.ast.android.module.communication.CommunicationHelper;
import com.capgemini.sesp.ast.android.module.util.AndroidUtilsAstSep;
import com.capgemini.sesp.ast.android.module.util.ApplicationAstSep;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.capgemini.sesp.ast.android.module.util.DatabaseHandler;
import com.capgemini.sesp.ast.android.module.util.GPSThread;
import com.capgemini.sesp.ast.android.module.util.SESPPreferenceUtil;
import com.capgemini.sesp.ast.android.module.util.to.AndroidWOAttachmentBean;
import com.skvader.rsp.ast_sep.common.to.custom.WorkorderCustomWrapperTO;
import com.skvader.rsp.cft.common.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public class WorkorderSynchService extends AsyncTask<Void, Void, Boolean> {

    private transient long caseId = -1;
    private transient String fieldVisitId = "";
    protected String TAG = this.getClass().getSimpleName();
    private final int MAX_ATTEMPTS = 3;

    public WorkorderSynchService(long idCase, String fieldVisitId) {
        this.caseId = idCase;
        this.fieldVisitId = fieldVisitId;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return performSaveWorkorder(caseId);
    }

    private boolean performSaveWorkorder(final long caseId) {
        Long idSystemCoordsSystemT = SESPPreferenceUtil.getPreference(ConstantsAstSep.SharedPreferenceKeys.GPS_COORD_SYS_TYPE);
        String xCoordinate = SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.GPS_X_COORD);
        String yCoordinate = SESPPreferenceUtil.getPreferenceString(ConstantsAstSep.SharedPreferenceKeys.GPS_Y_COORD);
        // Invoke Web service call and all the heavy lifting stuff here to save the work-order
        try {

            //Save Workorder data
            final WorkorderCustomWrapperTO completedWo = DatabaseHandler.createDatabaseHandler().getFinalWOForSync(caseId, ApplicationAstSep.workOrderClass);
            if (completedWo != null) {
                Log.d(TAG, "WorkorderService - Trying to sync workorder");
                AndroidUtilsAstSep.getDelegate().saveWorkorder(completedWo);
            }
            if((idSystemCoordsSystemT  != null)&& (!xCoordinate.equals("")) &&(!yCoordinate.equals(""))) {
                new GPSThread(idSystemCoordsSystemT,xCoordinate,yCoordinate).execute();
            }
            return performAttachmentSync();
        } catch (final Exception ex) {
            writeLog(TAG + "  : performSaveWorkorder() : ", ex);
            return false;
        }
    }

    private boolean performAttachmentSync() {
        boolean allAttachmentsSynched = true;
        try {
            List<AndroidWOAttachmentBean> androidWOAttachmentBeanList = DatabaseHandler.createDatabaseHandler().getAttachment(caseId, 0L, 0L, null);
            for (AndroidWOAttachmentBean androidWOAttachmentBean : androidWOAttachmentBeanList) {
                Log.e(TAG, "Trying to attach attachment: " + androidWOAttachmentBean.getFileName());
                int numberOfAttempts = 1; //Max number of attempts to upload an attachment
                while (numberOfAttempts <= MAX_ATTEMPTS) {
                    try {
                        CommunicationHelper communicationHelper = new CommunicationHelper();
                        int serverResponseCode = communicationHelper.uploadAttachmentCallWebservice(androidWOAttachmentBean, fieldVisitId);
                        Log.e(TAG, "Uploading attachment done");
                        if (serverResponseCode >= CommunicationHelper.SUCCESSFUL_OK && serverResponseCode <= CommunicationHelper.SUCCESSFUL_OK_MAX) {
                            //Remove attachment from WO_ATTACHMENTS table after verifying upload is successful
                            Log.e(TAG, "Deleting the attachment");
                            DatabaseHandler.createDatabaseHandler().removeAttchment(
                                    androidWOAttachmentBean.getCaseId(),
                                    androidWOAttachmentBean.getCaseTypeId(),
                                    androidWOAttachmentBean.getFileName(),
                                    androidWOAttachmentBean.getAttachmentTypeId(),
                                    androidWOAttachmentBean.getReasonTypeId());
                            movePhotoFromTempFolder(androidWOAttachmentBean.getCaseId(),getPhotoName(androidWOAttachmentBean.getFileName()));
                        }
                        break;
                    } catch (Exception e) {
                        writeLog(TAG + ": performAttachmentSync() : ", e);
                        numberOfAttempts++;
                    }
                }
                if (numberOfAttempts > MAX_ATTEMPTS) allAttachmentsSynched = false;
            }
        } catch (Exception e) {
            writeLog(TAG + ": performAttachmentSync() : ", e);
        }
        return allAttachmentsSynched;
    }

    /*@Override
    protected void onPostExecute(Boolean syncStatus) {
        if(syncStatus){
            Log.i(TAG, "Work order data synced for case id : "+ caseId);
            Log.i(TAG, "Starting attachment sync for case id : "+ caseId);
            new AttachmentSyncService(caseId,fieldVisitId, new AsyncResponse<Boolean>(){
                @Override
                public void processFinish(Boolean output) {
                    Log.i(TAG, "Attachment successfully sync for case id : "+ caseId);
                    DatabaseHandler.createDatabaseHandler().deleteWOfromSync(caseId);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }else{
            Log.e(TAG, "Unable to sync Work order for case id : "+ caseId);
        }
    }*/
    @Override
    protected void onPostExecute(Boolean syncStatus) {
        try {
            if (syncStatus) {
                Log.i(TAG, "Work order data synced for case id : " + caseId);
                DatabaseHandler.createDatabaseHandler().deleteWOfromSync(caseId);
            } else {
                Log.e(TAG, "Unable to sync Work order for case id : " + caseId);
            }
        } catch (Exception e) {
            writeLog(TAG + ": onPostExecute() : ", e);
        }
    }
    /**
     * This method was created to extracy only the filename of the photo (not the full path)
     * @param fullFileName
     * @return fileName (name of the photo)
     */
    private String getPhotoName(String fullFileName){
        String fileName = null;
        if(Utils.isNotEmpty(fullFileName)) {
            String[] split = fullFileName.split("/");
            if(split.length > 0){
                fileName = split[split.length-1];
            }
        }
        return fileName;

    }

    /**
     * This method is created to move the Photo from the SESPTemp/<CaseID>/ folder to the SESPWoImage folder.
     * After copying, the photos are deleted from the Temporary folder one by one
     * Once the temporary folder is empty, even the folder is deleted.
     * @param idCase
     * @param photoName
     */
    private void movePhotoFromTempFolder(Long idCase, String photoName) {
        if(photoName != null) {

            String imageDir = AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_CASE_IMAGE_DIR_NAME, idCase);
            String archiveDirectory = AndroidUtilsAstSep.getAppImageFolder(ConstantsAstSep.OrderHandlerConstants.DEFAULT_IMAGE_DIR_NAME);
            if (Utils.isNotEmpty(imageDir) && Utils.isNotEmpty(archiveDirectory)) {
                File file = new File(imageDir,photoName);
                if (file.exists()) {
                    try {
                        File src = new File(imageDir,photoName);
                        File dst = new File(archiveDirectory,photoName);

                        copyFile(src, dst);
                        Log.i(TAG, photoName + " is copied to Archive.");
                        src.delete();
                        Log.i(TAG, photoName + " is deleted.");
                        File caseTempDirectory = new File(imageDir);
                        if(!src.exists() && caseTempDirectory.isDirectory() && !Utils.isNotEmpty(caseTempDirectory.list())){
                            caseTempDirectory.delete();
                            Log.i(TAG, "Temporary folder is deleted for Case ID: "+idCase);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
    /**
     * Copying the source File to the Destination File location
     * The files generally are photos clicked during performing WO in Android
     * @param sourceFile
     * @param destFile
     * @throws Exception
     */
    private void copyFile(File sourceFile, File destFile) throws Exception {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


}
