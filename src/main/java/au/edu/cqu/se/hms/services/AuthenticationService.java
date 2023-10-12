package au.edu.cqu.se.hms.services;

import au.edu.cqu.se.hms.Session;
import au.edu.cqu.se.hms.daos.DoctorDao;
import au.edu.cqu.se.hms.daos.UserDao;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.utils.SessionKeys;

/**
 *
 * This is a service class to authenticate the user.
 *
 * @author sangharshachaulagain
 *
 */
public class AuthenticationService {

    private final UserDao userDao;
    private final Session session;
    private final DoctorDao doctorDao;

    public AuthenticationService() {
        userDao = UserDao.getInstance();
        session = Session.getInstance();
        doctorDao = DoctorDao.getInstance();
    }

    public User authenticate(String email, String password) {
        User user = userDao.login(email, password);

        if (user != null) {
            session.setData(SessionKeys.CURRENT_USER, user);
        }

        return user;
    }

    public User getCurrentUser() {
        if (session.hasDataFor(SessionKeys.CURRENT_USER)) {
            return (User) session.getData(SessionKeys.CURRENT_USER);
        }
        return null;
    }
}
