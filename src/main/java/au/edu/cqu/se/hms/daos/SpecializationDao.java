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
 *
 * @author sangharshachaulagain
 */
public class SpecializationDao {

    private final Connection connection;
    private static SpecializationDao instance;

    private SpecializationDao() {
        connection = DBConnection.getInstance().getConnection();
    }

    public static SpecializationDao getInstance() {
        if (instance == null) {
            instance = new SpecializationDao();
        }
        return instance;
    }

    public void addSpecialization(Specialization specialization) {
        String sql = "INSERT INTO specializations (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, specialization.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSpecialization(Specialization specialization) {
        String sql = "UPDATE specializations SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, specialization.getName());
            preparedStatement.setInt(2, specialization.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSpecialization(int specializationId) {
        String sql = "DELETE FROM specializations WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, specializationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Specialization getSpecializationById(int specializationId) {
        String sql = "SELECT * FROM specializations WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, specializationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                return new Specialization(id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Specialization not found
    }

    public List<Specialization> getAllSpecializations() {
        return getAllSpecializations(0, 0);
    }

    public List<Specialization> getAllSpecializations(int page, int pageSize) {
        List<Specialization> specializations = new ArrayList<>();
        String sql = "SELECT * FROM specializations";
        if (pageSize != 0) {
            sql += " LIMIT ?, ?";
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (pageSize != 0) {
                preparedStatement.setInt(1, getOffset(page, pageSize));
                preparedStatement.setInt(2, pageSize);

            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                specializations.add(new Specialization(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specializations;
    }

    private int getOffset(int page, int pageSize) {
        return page * pageSize;
    }
}
