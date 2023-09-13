package com.fssa.savinglives.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.fssa.savinglives.dao.exception.DAOException;
import com.fssa.savinglives.model.User;
import com.fssa.savinglives.utils.ConnectionUtil;

public class UserDAO {

	public boolean createUser(User user) throws DAOException {

		String query = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(query)) {
			pst.setString(1, user.getUsername());
			pst.setString(2, user.getEmail());
			pst.setString(3, user.getPassword());

			int rows = pst.executeUpdate();

			return (rows > 0);
		} catch (SQLException e) {
			throw new DAOException("Error while registering the user", e);
		}
	}

	public boolean isEmailAlreadyRegistered(String email) throws DAOException {
		final String query = "SELECT email FROM user WHERE email = ?";

		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pst = connection.prepareStatement(query)) {

			pst.setString(1, email);

			try (ResultSet rs = pst.executeQuery()) {
				return rs.next(); // Return true if the email exists
			}
		} catch (SQLException e) {
			throw new DAOException("Error in getting the email exist", e);
		}
	}

	public boolean update(User user) throws DAOException {
		final String SELECTQUERY = "UPDATE user SET  name=?,email=? WHERE user_id=?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement stmt = connection.prepareStatement(SELECTQUERY)) {

			stmt.setString(1, user.getUsername());
			stmt.setString(3, user.getEmail());
			stmt.setInt(4, user.getUserId());

			int row = stmt.executeUpdate();

			return row > 0;

		} catch (SQLException e) {
			throw new DAOException("Error in to update User", e);
		}
	}

	public boolean deleteUser(int userId) throws DAOException {
		final String SELECTQUERY = "DELETE from user WHERE user_id=?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement stmt = connection.prepareStatement(SELECTQUERY)) {

			stmt.setInt(1, userId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new DAOException("Error in to delete user", e);
		}
		return false;

	}


	public User findUserByEmail(String email) throws DAOException {
		final String SELECTQUERY = "SELECT * FROM user WHERE email = ?";

		User user = new User();

		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement pstmt = connection.prepareStatement(SELECTQUERY)) {

			pstmt.setString(1, email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {

					user.setEmail(rs.getString("email"));
					user.setUserId(rs.getInt("userid"));
					user.setPassword(rs.getString("password"));
					user.setUsername(rs.getString("name"));

				}
			}

		} catch (SQLException e) {
			throw new DAOException("error in dao", e);
		}
		return user;

	}
	
	public List<User> allUser() throws DAOException {
		// Create an empty list to store user list
		List<User> user1 = new ArrayList<>();
		final String SELECTQUERY = "SELECT * FROM user";
		// Start a try block with a prepared statement for selecting all users
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement stmt = connection.prepareStatement(SELECTQUERY);
				ResultSet rs = stmt.executeQuery()) {
			// Iterate through the result set and extract user information
			while (rs.next()) {
				int userId = rs.getInt("userid");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String password = rs.getString("password");

				user1.add(new User(email, name, password, userId));

			}
			// Return the list of user
			return user1;

		} catch (SQLException e) {
			throw new DAOException("Error in List user", e);
		}
	}

}