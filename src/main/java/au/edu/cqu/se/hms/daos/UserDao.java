package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserDao class provides the Data Access Object (DAO) functionalities for
 * managing user records within a database. It offers CRUD-like operations for
 * users, allowing the addition, retrieval, and querying of user data.
 */
public class UserDao {

    private final DBConnection dbConnection;
    private static UserDao instance;

    /**
     * Private constructor initializing the database connection.
     */
    private UserDao() {
        dbConnection = DBConnection.getInstance();
    }

    /**
     * Returns a singleton instance of UserDao. If no instance exists, a new one
     * is created.
     *
     * @return An instance of UserDao.
     */
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    /**
     * Registers a new user by inserting their details into the database.
     *
     * @param user The User object containing the user's information.
     * @return True if registration was successful, otherwise false.
     */
    public boolean signup(User user) {
        Connection connection = dbConnection.getConnection();
        String signupQuery = "INSERT INTO users (firstName, lastName, dateOfBirth, gender, contactNumber, email, password, address, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(signupQuery);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new java.sql.Date(user.getDateOfBirth().getTime()));
            preparedStatement.setString(4, user.getGender());
            preparedStatement.setString(5, user.getContactNumber());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setString(8, user.getAddress());
            preparedStatement.setString(9, user.getRole().getValue());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Authenticates a user based on their email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A User object if authenticated, otherwise null.
     */
    public User login(String email, String password) {
        Connection connection = dbConnection.getConnection();
        String loginQuery = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                user.setGender(resultSet.getString("gender"));
                user.setContactNumber(resultSet.getString("contactNumber"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setAddress(resultSet.getString("address"));
                user.setRole(Role.fromValue(resultSet.getString("role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves a user record based on its ID.
     *
     * @param id The user's ID.
     * @return A User object if found, otherwise null.
     */
    public User findUserByID(int id) {
        Connection connection = dbConnection.getConnection();
        String loginQuery = "SELECT * FROM users WHERE id = ?;";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                user.setGender(resultSet.getString("gender"));
                user.setContactNumber(resultSet.getString("contactNumber"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setAddress(resultSet.getString("address"));
                user.setRole(Role.fromValue(resultSet.getString("role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Fetches all users with a specific role from the database.
     *
     * @param role The role of the users to be fetched.
     * @return A list of User objects with the specified role.
     */
    public List<User> getUsersByRole(Role role) {
        System.out.println("Inside first condition");
        return getUsersByRole(role, 0, 0);

    }

    /**
     * Fetches users with a specific role from the database based on pagination
     * parameters.
     *
     * @param role The role of the users to be fetched.
     * @param page The page number (zero-based).
     * @param pageSize The number of records per page.
     * @return A list of User objects with the specified role.
     */
    public List<User> getUsersByRole(Role role, int page, int pageSize) {
        Connection connection = dbConnection.getConnection();
        String getUsersByRoleQuery = "SELECT * FROM users WHERE role = ?";
        if (pageSize != 0) {
            getUsersByRoleQuery += " LIMIT ?, ?";
        }
        List<User> users = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getUsersByRoleQuery);
            preparedStatement.setString(1, role.getValue());
            System.out.println("Inside second condition");
            if (pageSize != 0) {
                preparedStatement.setInt(2, getOffset(page, pageSize));
                preparedStatement.setInt(3, pageSize);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setDateOfBirth(resultSet.getDate("dateOfBirth"));
                user.setGender(resultSet.getString("gender"));
                user.setContactNumber(resultSet.getString("contactNumber"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setAddress(resultSet.getString("address"));
                user.setRole(Role.fromValue(resultSet.getString("role")));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users; // Returns the list of users (might be empty if no users are found)
    }

    /**
     * Retrieves a user record based on their email address.
     *
     * @param email The user's email address.
     * @return A User object if found, otherwise null.
     */
    public User getUserByEmail(String email) {
        Connection connection = dbConnection.getConnection();
        String sql = "SELECT * FROM users WHERE email = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fName = resultSet.getString("firstName");
                return new User(id, fName);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Specialization not found
    }

    /**
     * Computes the offset value for database queries based on page and
     * pageSize.
     *
     * @param page The page number (zero-based).
     * @param pageSize The number of records per page.
     * @return The computed offset value.
     */
    private int getOffset(int page, int pageSize) {
        return page * pageSize;
    }
}
