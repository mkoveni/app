package apps.scg.craton.scgsurveyapp.connection;


import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * Created by CodeThunder on 2017/02/20.
 */

public class Database {

    private static SurveyDBHelper connection;

    public static SurveyDBHelper getInstance(Context context) throws DataException {
        if (connection == null) {

           connection = new SurveyDBHelper(context);
        }

        return connection;
    }

    public static void closeConnection() throws DataException
    {


    }
}
