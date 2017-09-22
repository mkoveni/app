package apps.scg.craton.scgsurveyapp.services;

import android.content.Context;
import android.widget.Toast;

import com.annimon.stream.Stream;

import org.mindrot.BCrypt;

import java.io.IOException;

import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.dao.UserDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/03/01
 */

public class AuthenticationService {

    private User authUser;

    public AuthenticationService(Context context) throws DataException {
        UserDao.initialize(context);
    }
    public boolean attempt(String username, String password) throws DataException
    {
        boolean isFound = false;

        authUser = UserDao.findByUsername(username);

        if(authUser != null) {
            if(BCrypt.checkpw(password, authUser.getPassword().replace("$2y$", "$2a$"))){
                isFound = true;
            }
        }


        return isFound;
    }

    public User getAuthenticatedUser()
    {
        return  authUser;
    }
}
