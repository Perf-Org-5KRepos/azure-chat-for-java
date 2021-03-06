/*
Copyright (c) Microsoft Open Technologies, Inc.  All rights reserved.
 
The MIT License (MIT)
 
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
 
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */
package com.microsoftopentechnologies.azchat.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.microsoftopentechnologies.azchat.web.common.exceptions.AzureChatSystemException;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatSQLConstants;
import com.microsoftopentechnologies.azchat.web.common.utils.AzureChatUtils;
import com.microsoftopentechnologies.azchat.web.dao.data.entities.sql.UserEntity;

/**
 * Implementation class for UserDAO. This class provides operations to manage
 * the user information in azure SQL tables.
 * 
 * @author Rupesh_Shirude
 *
 */
@Service("userDao")
public class UserDAOImpl implements UserDAO {

	private static final Logger LOGGER = LogManager
			.getLogger(UserDAOImpl.class);

	/**
	 * This method executes adds new user query on the azure SQL user table.
	 * 
	 * @return userEntity
	 */
	public UserEntity saveNewUser(UserEntity user) throws Exception {
		LOGGER.info("[UserDAOImpl][saveNewUser] start ");
		int userId = 0;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection.prepareStatement(
					AzureChatSQLConstants.SAVE_NEW_USER,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement = generatePreparedStatement(preparedStatement,
					user);
			preparedStatement.executeUpdate();
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				userId = resultSet.getInt(1);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing save user query on azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing save user query on azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		user.setUserID(userId);
		LOGGER.info("[UserDAOImpl][saveNewUser] end ");
		return user;
	}

	/**
	 * This method execute query on azure SQL user table to get the user details
	 * by input user id.
	 * 
	 * @param userId
	 * @return UserEntity
	 * @author rupesh_shirude
	 */
	@Override
	public UserEntity getUserDetailsByUserId(Integer userId) throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByUserId] start ");
		UserEntity user = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_USERID);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				user = generateUserObject(resultSet);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByUserId] end ");
		return user;
	}

	/**
	 * This method execute query on azure SQL user table to get the user details
	 * by input name id.
	 * 
	 * @param nameId
	 * @return List<UserEntity>
	 * @author rupesh_shirude
	 */
	@Override
	public List<UserEntity> getUserDetailsByNameID(String nameId)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameID] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_NAMEID);
			preparedStatement.setString(1, nameId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameID] end ");
		return userEntities;
	}

	/**
	 * This method executes query on azure SQL user table to get the user
	 * details by nameId and identityProvider.
	 * 
	 * @param nameId
	 * @param identityProvider
	 * @return List<UserEntity>
	 * @author rupesh_shirude
	 * @throws ClassNotFoundException
	 */
	@Override
	public List<UserEntity> getUserDetailsByNameIdAndIdentityProvider(
			String nameId, String identityProvider) throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameIdAndIdentityProvider] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_NAMEID_IDNTITY_PROVIDR);
			preparedStatement.setString(1, nameId);
			preparedStatement.setString(2, identityProvider);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByNameIdAndIdentityProvider] end ");
		return userEntities;
	}

	/**
	 * This method executes query on azure SQL user table to get the user
	 * details by first name.
	 * 
	 * @param firstName
	 * @return userEntities
	 */
	@Override
	public List<UserEntity> getUserDetailsByFirstName(String firstName)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstName] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_FIRST_NAME);
			preparedStatement.setString(1, firstName
					+ AzureChatConstants.CONSTANT_PERCENTAGE);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstName] end ");
		return userEntities;
	}

	/**
	 * This method executes query on azure SQL user table to get the user
	 * details by last name.
	 * 
	 * @param lastName
	 * @return userEntities
	 */
	@Override
	public List<UserEntity> getUserDetailsByLastName(String lastName)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByLastName] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_LAST_NAME);
			preparedStatement.setString(1, lastName
					+ AzureChatConstants.CONSTANT_PERCENTAGE);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (SQLException e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByLastName] end ");
		return userEntities;
	}

	/**
	 * This method executes query on azure SQL user table to get the user
	 * details by first or last name
	 * 
	 * @param name
	 * @return userEntities
	 */
	@Override
	public List<UserEntity> getUserDetailsByFirstNameOrLastName(String name)
			throws Exception {
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstNameOrLastName] start ");
		List<UserEntity> userEntities = new ArrayList<UserEntity>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_BY_FIRST_LAST_NAME);
			preparedStatement.setString(1, name
					+ AzureChatConstants.CONSTANT_PERCENTAGE);
			preparedStatement.setString(2, name
					+ AzureChatConstants.CONSTANT_PERCENTAGE);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				userEntities.add(generateUserObject(resultSet));
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing get user details query on azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user details query on azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][getUserDetailsByFirstNameOrLastName] end ");
		return userEntities;
	}

	/**
	 * This method generates the prepare statement from userEntity object.
	 * 
	 * @param preparedStatement
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement generatePreparedStatement(
			PreparedStatement preparedStatement, UserEntity user)
			throws SQLException {
		preparedStatement.setString(1, user.getNameId());
		preparedStatement.setString(2, user.getIdentityProvider());
		preparedStatement.setString(3, user.getFirstName());
		preparedStatement.setString(4, user.getLastName());
		preparedStatement.setString(5, user.getPhotoBlobUrl());
		preparedStatement.setString(6, user.getEmailAddress());
		preparedStatement.setInt(7, user.getPhoneCountryCode());
		preparedStatement.setLong(8, user.getPhoneNumber());
		preparedStatement.setDate(9, new java.sql.Date(user.getDateCreated()
				.getTime()));
		preparedStatement.setDate(10, new java.sql.Date(user.getCreatedBy()
				.getTime()));
		preparedStatement.setDate(11, new java.sql.Date(user.getDateModified()
				.getTime()));
		preparedStatement.setDate(12, new java.sql.Date(user.getModifiedBy()
				.getTime()));
		return preparedStatement;
	}

	/**
	 * This method populates userEntity from the result set object.
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public UserEntity generateUserObject(ResultSet resultSet)
			throws SQLException {
		UserEntity user = new UserEntity();
		user.setUserID(resultSet.getInt(1));
		user.setNameId(resultSet.getString(2));
		user.setIdentityProvider(resultSet.getString(3));
		user.setFirstName(resultSet.getString(4));
		user.setLastName(resultSet.getString(5));
		user.setPhotoBlobUrl(resultSet.getString(6));
		user.setEmailAddress(resultSet.getString(7));
		user.setPhoneCountryCode(resultSet.getInt(8));
		user.setPhoneNumber(resultSet.getLong(9));
		user.setDateCreated(resultSet.getDate(10));
		user.setCreatedBy(resultSet.getDate(11));
		user.setDateModified(resultSet.getDate(12));
		user.setModifiedBy(resultSet.getDate(13));
		return user;
	}

	/**
	 * This method executes the update user details query in the azure SQL
	 * database for the input user entity.
	 */
	@Override
	public UserEntity updateNewUser(UserEntity user) throws Exception {
		LOGGER.info("[UserDAOImpl][updateNewUser] start ");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.UPDATE_NEW_USER);
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getLastName());
			preparedStatement.setString(3, user.getPhotoBlobUrl());
			preparedStatement.setString(4, user.getEmailAddress());
			preparedStatement.setInt(5, user.getPhoneCountryCode());
			preparedStatement.setLong(6, user.getPhoneNumber());
			preparedStatement.setString(7, user.getNameId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing update user query on the azure SQL table. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing update user query on the azure SQL table. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		LOGGER.info("[UserDAOImpl][updateNewUser] end ");
		return user;

	}

	/**
	 * This method executes the get user photo blob URL query on the azure SQL
	 * user table for input user id.
	 * 
	 * @param userId
	 * @return profileImageURL
	 */
	@Override
	public String getUserPhotoBlobURL(Integer userId) throws Exception {
		String profileImageURL = AzureChatConstants.CONSTANT_EMPTY_STRING;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.GET_USER_PROFILE_URL_BY_USERID);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				profileImageURL = resultSet.getString(1);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing get user profile image URL on azure SQL user table. Exception MEssage : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing get user profile image URL on azure SQL user table. Exception MEssage : "
							+ e.getMessage());
		} finally {
			AzureChatUtils.closeDatabaseResources(preparedStatement, resultSet,
					connection);
		}
		return profileImageURL;
	}

	/**
	 * This method executes create user table query on the azure SQL database.
	 */
	@Override
	public void createUserTable() throws Exception {
		Connection connection = null;
		Connection connection1 = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		try {
			connection = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement = connection
					.prepareStatement(AzureChatSQLConstants.CREATE_USER_TABLE);
			preparedStatement.execute();
			connection1 = AzureChatUtils.getConnection(AzureChatUtils
					.buildConnectionString());
			preparedStatement1 = connection1
					.prepareStatement(AzureChatSQLConstants.CREATE_USER_TABLE_INDEX);
			preparedStatement1.execute();
		} catch (Exception e) {
			LOGGER.error("Exception occurred while executing create user table query on the azure SQL database. Exception Message : "
					+ e.getMessage());
			throw new AzureChatSystemException(
					"Exception occurred while executing create user table query on the azure SQL database. Exception Message : "
							+ e.getMessage());
		} finally {
			AzureChatUtils
					.closeDatabaseResources(preparedStatement, connection);
			AzureChatUtils.closeDatabaseResources(preparedStatement1,
					connection1);

		}
	}
}
