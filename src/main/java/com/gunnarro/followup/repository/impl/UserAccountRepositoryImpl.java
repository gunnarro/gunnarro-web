package com.gunnarro.followup.repository.impl;

import com.gunnarro.followup.domain.user.*;
import com.gunnarro.followup.repository.UserAccountRepository;
import com.gunnarro.followup.repository.mapper.UserAccountRowMapper;
import com.gunnarro.followup.repository.table.link.UserRoleLnkTable;
import com.gunnarro.followup.repository.table.user.ProfilesTable;
import com.gunnarro.followup.repository.table.user.RolesTable;
import com.gunnarro.followup.repository.table.user.UsersLogTable;
import com.gunnarro.followup.repository.table.user.UsersTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Database: jbossews User: admincnVhNH8 Password: suSNhqkXILV-
 *
 * @author admin
 */
@Repository
// @Transactional
public class UserAccountRepositoryImpl extends BaseJdbcRepository implements UserAccountRepository {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountRepositoryImpl.class);

    public UserAccountRepositoryImpl() {
        super(null);
    }

    public UserAccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile getProfile(Integer userId) {
        return getJdbcTemplate().queryForObject("SELECT * FROM profiles WHERE fk_user_id = ?", new Object[]{userId}, ProfilesTable.mapToProfileRM());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalUser getUser(Integer userId) {
        try {
            LocalUser user = getJdbcTemplate().queryForObject("SELECT * FROM users WHERE id = ?", new Object[]{userId}, UsersTable.mapToUserRM());
            if (user != null) {
                user.setRoles(getUserRoles(user.getId()));
            }
            return user;
        } catch (Exception erae) {
            LOG.debug("userId: {}, Error: {}", userId, erae.toString());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalUser getUser(String userName) {
        try {
            LocalUser user = getJdbcTemplate().queryForObject("SELECT * FROM users WHERE username = ?", new Object[]{userName}, UsersTable.mapToUserRM());
            if (user != null) {
                user.setRoles(getUserRoles(user.getId()));
            }
            return user;
        } catch (Exception erae) {
            LOG.debug("username: {}, Error: {}", userName, erae.toString());
            return null;
        }
    }

    private List<Role> getUserRoles(@NotNull Integer userId) {
        List<Role> roles = new ArrayList<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT r.id, r.name");
        query.append(" FROM user_role_lnk l, roles r");
        query.append(" WHERE l.fk_user_id = ?");
        query.append(" AND l.fk_role_id = r.id");
        try {
            roles = getJdbcTemplate().query(query.toString(), new Object[]{userId}, RolesTable.mapToRoleRM());
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
        }
        // have to get privileges
        for (Role r : roles) {
            r.setPrivileges(getPrivileges(r.getId()));
        }
        return roles;
    }

    private List<Privilege> getPrivileges(Integer roleId) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.id, p.name");
        query.append(" FROM role_permission_lnk l, roles r, permissions p");
        query.append(" WHERE l.fk_permission_id = p.id");
        query.append(" AND l.fk_role_id = r.id");
        query.append(" AND l.fk_role_id = ?");
        try {
            return getJdbcTemplate().query(query.toString(), new Object[]{roleId}, UserAccountRowMapper.mapToPrivilegeRM());
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
            return new ArrayList<>();
        }
    }

    /**
     * FIXME roles {@inheritDoc}
     */
    @Override
    public int updateUser(LocalUser user) {
        int rows = getJdbcTemplate().update(UsersTable.createUpdateQuery(), UsersTable.createUpdateParam(user));
        // we don't care if role are changed or not, we simple delete all
        // current roles, thereafter add all updated roles, if any.
        // deleteUserRoles(user.getUsername());
        // for (Role role : user.getRoles()) {
        // addRoleToUser(user.getUsername(), role);
        // }
        LOG.debug("updated user: {}", user);
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteUser(Integer id) {
        return getJdbcTemplate().update("DELETE FROM users WHERE id = ?", id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LocalUser> getUsers() {
        try {
            List<LocalUser> users = getJdbcTemplate().query("SELECT * FROM users", new Object[]{}, UsersTable.mapToUserRM());
            for (LocalUser user : users) {
                user.setRoles(getUserRoles(user.getId()));
            }
            return users;
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getUserRoles() {
        try {
            return getJdbcTemplate().query("SELECT DISTINCT * FROM roles", new Object[]{}, RolesTable.mapToRoleNameRM());
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int changeUserPwd(Integer userId, String password) {
        return getJdbcTemplate().update(UsersTable.createPasswordUpdateQuery(), UsersTable.createPasswordUpdateParam(userId, password));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int createUser(LocalUser user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(UsersTable.createInsertPreparedStatement(user), keyHolder);
        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        for (Role role : user.getRoles()) {
            createUserRoleLnk(id, role.getId());
        }
        LOG.debug("created user, id: {} ", id);
        return id;
    }

    private void createUserRoleLnk(Integer userId, Integer roleId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(UserRoleLnkTable.createInsertPreparedStatement(userId, roleId), keyHolder);
        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        LOG.debug("created link, id: {}, userId: {}, roleId: {}", id, userId, roleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int createUserLog(UserLog userLog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(UsersLogTable.createInsertPreparedStatement(userLog), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateUserLog(UserLog userLog) {
        return getJdbcTemplate().update(UsersLogTable.createUpdateQuery(), UsersLogTable.createUpdateParam(userLog));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserLog> getUserLogs() {
        try {
            return getJdbcTemplate().query("SELECT * FROM user_details_log ORDER BY last_logged_in_date_time DESC", new Object[]{}, UsersLogTable.mapToUserLogRM());
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserLog getUserLastLogin(Integer userId) {
        try {
            return getJdbcTemplate().queryForObject("SELECT * FROM user_details_log WHERE fk_user_id = ?", new Object[]{userId}, UsersLogTable.mapToUserLogRM());
        } catch (Exception erae) {
            LOG.debug("Error: {}", erae.toString());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void checkIfUserIsBlocked(Integer userId) throws SecurityException {
        LOG.debug("check if user is blocked! userId: {}", userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateUserLoginAttemptFailures(Integer userId, Integer numberOfAttemptFailures) {
        return getJdbcTemplate()
                .update(UsersLogTable.createUpdateQueryLoginAttemptFailures(), UsersLogTable.createUpdateParamLoginAttemptFailures(numberOfAttemptFailures, userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateUserLoginAttemptSuccess(Integer userId, Integer numberOfAttemptSuccess) {
        return getJdbcTemplate().update(UsersLogTable.createUpdateQueryLoginAttemptSuccess(), UsersLogTable.createUpdateParamLoginAttemptSuccess(numberOfAttemptSuccess, userId));
    }
}
