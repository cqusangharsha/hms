package au.edu.cqu.se.hms.utils;

/**
 *
 * @author sangharshachaulagain
 */
public class Queries {

    // USERS QUERY
    public static final String CREATE_USER = """
                                             CREATE TABLE users (
                                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                                 firstName VARCHAR(255) NOT NULL,
                                                 lastName VARCHAR(255) NOT NULL,
                                                 dateOfBirth DATE,
                                                 gender VARCHAR(10),
                                                 contactNumber VARCHAR(20),
                                                 email VARCHAR(255) UNIQUE NOT NULL,
                                                 password VARCHAR(255) NOT NULL,
                                                 address VARCHAR(255),
                                                 role ENUM('ADMIN', 'DOCTOR', 'ASSISTANCE') NOT NULL
                                             )""";

    public static final String INSERT_ADMIN = """
                                               INSERT INTO users (firstName, lastName, dateOfBirth, gender, contactNumber, email, password, address, role)
                                               VALUES ('HealthLink', 'Admin', '1990-01-01', 'Male', '0458987654', 'admin@healthlink.com', 'admin', 'NSW', 'ADMIN');
                                               """;

    public static final String CREATE_SPECIALIZATION = """
                                                        CREATE TABLE specializations (
                                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                                            name VARCHAR(255) NOT NULL
                                                        )
                                                        """;

    public static final String CREATE_DOCTOR = """
                                                        CREATE TABLE doctor (
                                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                                            user_id INT,
                                                            specialization VARCHAR(255),
                                                            FOREIGN KEY (user_id) REFERENCES users(id)
                                                        )
                                                        """;

    public static final String CREATE_ASSISTANT = """
                                                        CREATE TABLE assistant (
                                                            id INT AUTO_INCREMENT PRIMARY KEY, 
                                                            user_id INT,
                                                            reports VARCHAR(255),
                                                            FOREIGN KEY (user_id) REFERENCES users(id)
                                                        )
                                                        """;
    
    
      public static final String CREATE_PATIENT = """
                                                         CREATE TABLE patient (
                                                                                                         id INT AUTO_INCREMENT PRIMARY KEY,
                                                                                                         patientName VARCHAR(255) NOT NULL,
                                                                                                         dateOfBirth DATE,
                                                                                                         gender VARCHAR(10),
                                                                                                         contactNumber VARCHAR(20),
                                                                                                         email VARCHAR(255) UNIQUE NOT NULL,
                                                                                                         address VARCHAR(255),
                                                                                                         doctor VARCHAR(255),
                                                                                                         visitReason varchar(255))
                                                        """;

}
