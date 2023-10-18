package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.models.Specialization;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The SpecializationDao class provides the Data Access Object (DAO)
 * functionalities for managing specializations within a hospital or medical
 * service. It offers CRUD operations for specializations, enabling the
 * addition, update, deletion, and retrieval of specializations from a database.
 *
 */
public class SpecializationDao {

    private final Connection connection;
    private static SpecializationDao instance;

    /**
     * Private constructor initializing the database connection.
     */
    private SpecializationDao() {
        connection = DBConnection.getInstance().getConnection();
    }

    /**
     * Returns a singleton instance of SpecializationDao. If no instance exists,
     * a new one is created.
     *
     * @return An instance of SpecializationDao.
     */
    public static SpecializationDao getInstance() {
        if (instance == null) {
            instance = new SpecializationDao();
        }
        return instance;
    }

    /**
     * Inserts a new specialization record into the database.
     *
     * @param specialization The Specialization object to be inserted.
     */
    public void addSpecialization(Specialization specialization) {
        String sql = "INSERT INTO specializations (name,checkupCost) VALUES (?,?)";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, specialization.getName());
            preparedStatement.setString(2, specialization.getCostCheckup());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a specialization record in the database.
     *
     * @param specialization The Specialization object containing updated data.
     */
    public void updateSpecialization(Specialization specialization) {
        String sql = "UPDATE specializations SET name = ? WHERE id = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, specialization.getName());
            preparedStatement.setInt(2, specialization.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specialization record from the database based on its ID.
     *
     * @param specializationId The ID of the specialization to be deleted.
     */
    public void deleteSpecialization(int specializationId) {
        String sql = "DELETE FROM specializations WHERE id = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, specializationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a specialization record from the database based on its ID.
     *
     * @param specializationId The ID of the specialization to be fetched.
     * @return A Specialization object if found, otherwise null.
     */
    public Specialization getSpecializationById(int specializationId) {
        String sql = "SELECT * FROM specializations WHERE id = ?";
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, specializationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String checkupCost = resultSet.getString("checkupCost");
                return new Specialization(id, name, checkupCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches all the specialization records from the database.
     *
     * @return A list of all Specialization objects.
     */
    public List<Specialization> getAllSpecializations() {
        return getAllSpecializations(0, 0);
    }

    /**
     * Fetches specialization records from the database based on pagination
     * parameters.
     *
     * @param page The page number (zero-based).
     * @param pageSize The number of records per page.
     * @return A list of Specialization objects.
     */
    public List<Specialization> getAllSpecializations(int page, int pageSize) {
        List<Specialization> specializations = new ArrayList<>();
        String sql = "SELECT * FROM specializations";
        if (pageSize != 0) {
            sql += " LIMIT ?, ?";
        }
        try ( PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (pageSize != 0) {
                preparedStatement.setInt(1, getOffset(page, pageSize));
                preparedStatement.setInt(2, pageSize);

            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String checkupCost = resultSet.getString("checkupCost");
                specializations.add(new Specialization(id, name, checkupCost));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specializations;
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
