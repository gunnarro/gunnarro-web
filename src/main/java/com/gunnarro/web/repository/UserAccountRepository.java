package com.gunnarro.web.repository;

import com.gunnarro.web.domain.user.LocalUser;
import com.gunnarro.web.domain.user.Profile;
import com.gunnarro.web.domain.user.UserLog;

import java.util.List;

public interface UserAccountRepository {

    /**
     */
    LocalUser getUser(String userName);

    /**
     * @param userId user id
     * @return user
     */
    LocalUser getUser(Integer userId);

    /**
     *
     * @param user
     * @return
     */
    int createUser(LocalUser user);

    /**
     * @param user
     * @return
     */
    int updateUser(LocalUser user);

    /**
     * @param id
     * @return
     */
    int deleteUser(Integer id);

    /**
     * @return
     */
    List<String> getUserRoles();

    /**
     * @return
     */
    List<LocalUser> getUsers();

    /**
     * @param userId
     * @param password
     * @return
     */
    int changeUserPwd(Integer userId, String password);

    /**
     * @return
     */
    List<UserLog> getUserLogs();

    /**
     * @param userId
     * @return
     */
    UserLog getUserLastLogin(Integer userId);

    /**
     * @param userLog
     * @return
     */
    int createUserLog(UserLog userLog);

    /**
     * @param userId
     * @return
     */
    void checkIfUserIsBlocked(Integer userId) throws SecurityException;

    /**
     * @param userId
     * @param numberOfAttemptFailures
     * @return
     */
    int updateUserLoginAttemptFailures(Integer userId, Integer numberOfAttemptFailures);

    /**
     * @param userId
     * @param numberOfAttemptSuccess
     * @return
     */
    int updateUserLoginAttemptSuccess(Integer userId, Integer numberOfAttemptSuccess);

    /**
     * @param userLog
     * @return
     */
    int updateUserLog(UserLog userLog);

    /**
     * @param userId
     * @return
     */
    Profile getProfile(Integer userId);
}
