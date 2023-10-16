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
 *
 * @author sangharshachaulagain
 */
public class UserDao {

    private final DBConnection dbConnection;
    private static UserDao instance;

    private UserDao() {
        dbConnection = DBConnection.getInstance();
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

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

    public List<User> getUsersByRole(Role role) {
        System.out.println("Inside first condition");
        return getUsersByRole(role, 0, 0);
       
    }

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
                System.out.println("Inside third condition");
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("has values"+resultSet.next());
            while (resultSet.next()) {
                
                 System.out.println("inside while");
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
                System.out.println("First name"+ resultSet.getString("firstName") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users; // Returns the list of users (might be empty if no users are found)
    }

    public User getUserByEmail(String email) {
        Connection connection = dbConnection.getConnection();
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    private int getOffset(int page, int pageSize) {
        return page * pageSize;
    }
}
