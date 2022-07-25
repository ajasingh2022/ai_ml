package com.capgemini.sesp.ast.android.module.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.capgemini.sesp.ast.android.module.cache.WorkorderCache;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceKeys;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep.SharedPreferenceType;
import com.skvader.rsp.cft.common.authentication.EncryptionUtils;
import com.skvader.rsp.cft.webservice_client.bean.to.custom.UserLoginCustomTO;

import java.util.List;
import java.util.TimeZone;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.TAG;
import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

public final class SessionState {
    private static SessionState instance = new SessionState();

    public static SessionState getInstance() {
        return instance;
    }

    private String currentUserUsername;
    private String currentUserPassword;
    private UserLoginCustomTO currentUser;
    private Boolean loggedInInOnlineMode = true;
    private TimeZone timeZone;
    private int failedConnectionsCounter = 0;

    private SessionState() {
    }

    public Boolean getLoggedInInOnlineMode() {
        return loggedInInOnlineMode;
    }

    public void setLoggedInInOnlineMode(Boolean loggedInInOnlineMode) {
        this.loggedInInOnlineMode = loggedInInOnlineMode;
    }

    /**
     * Should only be called by LoginActivity upon successful login
     *
     * @throws Exception
     */
    public void registerCurrentUserCredentials(String username, String password) throws Exception {
        currentUserUsername = username;
        currentUserPassword = EncryptionUtils.base64Encode(password);
        this.failedConnectionsCounter = 0;
    }

    /**
     * @param givenUsername
     * @param givenPassword
     * @return If login was successful
     */
    public boolean logonInOfflineMode(String givenUsername, String givenPassword) {
        final String correctUsername = DatabaseHandler.createDatabaseHandler().getLastLoginUserName();
        final String correctPassword = DatabaseHandler.createDatabaseHandler().getLastLoginPassword();


        String givenHashedPassword = null;
        try {
            givenHashedPassword = EncryptionUtils.base64Encode(givenPassword);
        } catch (Exception e) {
            writeLog(TAG+":logonInOfflineMode()",e);
        }

        boolean successful = false;
        boolean offlineLoginPermit = false;
        if (givenUsername.equals(correctUsername) && correctPassword.equals(givenHashedPassword)) {
            loggedInInOnlineMode = false;
            UserLoginCustomTO loggedInUserTO = null;
            DatabaseHandler db = DatabaseHandler.createDatabaseHandler();
            List<UserLoginCustomTO> users = db.getAll(UserLoginCustomTO.class);

            for(UserLoginCustomTO user: users){
                if(user.getUserName().equals(givenUsername)){
                    offlineLoginPermit = true;
                    loggedInUserTO=user;

                }
            }

            if (offlineLoginPermit) {
                if (loggedInUserTO != null) {
                    currentUser = loggedInUserTO;
                    try {
                        registerCurrentUserCredentials(givenUsername, givenPassword);
                    } catch (Exception e) {
                        return false;
                    }
                    successful = true;
                }
            }
        }

        return successful;
    }

    /**
     * Should only be called by LoginThread (in LoginActivity)
     */
    public void registerCurrentUser(UserLoginCustomTO user, String password, boolean loggedInInOnlineMode) {
        currentUser = user;
        this.loggedInInOnlineMode = loggedInInOnlineMode;

        final DatabaseHandler databaseHandler = DatabaseHandler.createDatabaseHandler();
        String lastLogedInUserName = databaseHandler.getLastLoginUserName();
        //  remove download times from prefncs so that fresh
        // data will be downloaded if different user logs in
        if (!currentUser.getUserName().equals(lastLogedInUserName)) {
            SESPPreferenceUtil.removeDownloadTimes();
        }
        if (lastLogedInUserName == null
                || (lastLogedInUserName != null && lastLogedInUserName.trim().equals(""))) {
            lastLogedInUserName = currentUser.getUserName();
        }

        if (currentUser != null
                && currentUser.getUserName() != null
                && !currentUser.getUserName().equals(lastLogedInUserName)) {
            WorkorderCache.clear();
            SharedPreferences settings = AndroidUtilsAstSep.getSharedPreferences(SharedPreferenceType.USER_SETTINGS);
            Editor pe = settings.edit();
            pe.putString(SharedPreferenceKeys.SETTING_LAST_DATA_UPDATE, null);
            pe.commit();
        }
        /** Store to make offline mode available */
        try {
            if (databaseHandler != null && user.getUserName() != null) {
                databaseHandler.registerUserCreds(user.getUserName(), EncryptionUtils.base64Encode(password));
                databaseHandler.delete(user.getId(),UserLoginCustomTO.class);
                databaseHandler.create(user);
            }
        } catch (final Exception ex) {
            writeLog("SessionState()  :registerCurrentUser ", ex);

        }
    }

    public boolean isLoggedInOnline() {
        return loggedInInOnlineMode;
    }

    public boolean isLoggedIn() {
        return (currentUser != null);
    }

    /**
     * Should only be called by log out
     */
    public void clearSession() {
        instance = new SessionState();
    }

    public String getCurrentUserUsername() {
        String userName = null;
        if (currentUserUsername != null && !"".equals(currentUserUsername)) {
            userName = currentUserUsername;
        } else {
            // Load it
            userName = DatabaseHandler.createDatabaseHandler().getLastLoginUserName();
            if (!(userName != null && !userName.trim().equals(""))) {
                userName = currentUser.getUserName();
            }
        }

        return userName;
    }

    public String getCurrentUserPassword() {
        String password = null;
        if (currentUserPassword != null && !"".equals(currentUserPassword)) {
            password = currentUserPassword;
        } else {
            // Load it
            password = DatabaseHandler.createDatabaseHandler().getLastLoginPassword();
        }
        return password;
    }

    public UserLoginCustomTO getCurrentUser() {
        return currentUser;
    }

    public TimeZone getUsersTimeZone() {
        return timeZone;
    }

    public void setUsersTimeZone(TimeZone t) {
        timeZone = t;
    }

    /**
     * Increment the failed connections counter by 1 for a session.
     */
    public void incrementFailedConnectionsCounter() {
        this.failedConnectionsCounter++;
    }

    public int getFailedConnectionsCounter() {
        return this.failedConnectionsCounter;
    }

}
