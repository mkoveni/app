package apps.scg.craton.scgsurveyapp.connection;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

import apps.scg.craton.scgsurveyapp.services.DateService;


public class SurveyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "survey.db";
    private static final int DATABASE_VERSION = 1;
    private String continents[] = {"Antarctica","Asia","Europe","North America","Australia","South America"};
    private String outsideSADC[] = {
              "    Algeria",
              "    Benin",
              "    Burkina Faso",
              "    Burundi",
              "    Cabo Verde",
              "    Cameroon",
              "    Central African Republic (CAR)",
              "    Chad",
              "    Comoros",
              "    Republic of the Congo",
              "    Cote d'Ivoire",
              "    Djibouti",
              "    Egypt",
              "    Equatorial Guinea",
              "    Eritrea",
              "    Ethiopia",
              "    Gabon" ,
              "    Gambia",
              "    Ghana",
              "    Guinea",
              "    Guinea-Bissau",
              "    Kenya",
              "    Liberia",
              "    Libya",
              "    Mali",
              "    Mauritania",
              "    Morocco",
              "    Niger" ,
              "    Nigeria",
              "    Rwanda",
              "    Sao Tome and Principe",
              "    Senegal",
              "    Sierra Leone",
              "    Somalia",
              "    South Sudan",
              "    Sudan",
              "    Togo",
              "    Tunisia",
              "    Uganda"
    };

    public SurveyDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE `VILLAGES` ( `ID` INTEGER PRIMARY KEY, `NAME` TEXT NOT NULL, `AREA_CODE` INTEGER NOT NULL, `POPULATION` INTEGER NOT NULL, `NUMBER_OF_HOUSES` INTEGER NOT NULL, `SIZE` REAL NOT NULL, `LONGITUDE` REAL NOT NULL, `LATITUDE` REAL NOT NULL )");
        db.execSQL("CREATE TABLE `CATEGORIES` ( `ID` INTEGER PRIMARY KEY, `CATEGORY_TYPE` TEXT NOT NULL )");
        db.execSQL("CREATE TABLE `VILLAGE_CATEGORIES` ( `VILLAGE_ID` INTEGER NOT NULL, `CATEGORY_ID` INTEGER NOT NULL, PRIMARY KEY(`VILLAGE_ID`,`CATEGORY_ID`), FOREIGN KEY(`VILLAGE_ID`) REFERENCES `VILLAGES`(`ID`), FOREIGN KEY(`CATEGORY_ID`) REFERENCES CATEGORIES(ID) )");
        db.execSQL("CREATE TABLE `QUESTIONNAIRES` ( `ID` INTEGER PRIMARY KEY, `VILLAGE_ID` INTEGER NOT NULL, `NAME` TEXT NOT NULL, `START_DATE` TEXT NOT NULL, `END_DATE` TEXT NOT NULL, `IS_COMPLETED` INTEGER DEFAULT 0, FOREIGN KEY(`VILLAGE_ID`) REFERENCES VILLAGES(ID) )");
        db.execSQL("CREATE TABLE `EMPLOYEES` ( `ID` INTEGER PRIMARY KEY, `FIRST_NAME` TEXT NOT NULL, `LAST_NAME` TEXT NOT NULL, `USERNAME` TEXT NOT NULL, `EMAIL` TEXT NOT NULL, `PASSWORD` TEXT NOT NULL, `CELL_NUMBER` TEXT NOT NULL, `IS_ADMIN` INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE `QUESTION_TYPES` ( `ID` INTEGER PRIMARY KEY, `TYPE` TEXT NOT NULL, `HAS_OPTIONS` INTEGER NOT NULL )");
        db.execSQL("CREATE TABLE `QUESTIONS` ( `ID` INTEGER PRIMARY KEY, `QUESTION_TYPE_ID` INTEGER NOT NULL, `CATEGORY_ID` INTEGER NOT NULL, PARENT_ID INTEGER NOT NULL, TAG_ID INTEGER NOT NULL,NAME TEXT NULL ,`QUESTION_TEXT` TEXT NOT NULL, `ANSWER_REQUIRED` INTEGER NOT NULL, FOREIGN KEY(`QUESTION_TYPE_ID`) REFERENCES `QUESTION_TYPES`(`ID`), FOREIGN KEY(`CATEGORY_ID`) REFERENCES CATEGORIES(ID) )");
        db.execSQL("CREATE TABLE `CHOICES` ( `ID` INTEGER PRIMARY KEY, `QUESTION_ID` INTEGER NOT NULL, `CHOICE_TEXT` TEXT NOT NULL, `CHOICE_VALUE` TEXT NOT NULL, FOREIGN KEY(`QUESTION_ID`) REFERENCES QUESTIONS(ID) )");
        db.execSQL("CREATE TABLE `QUESTIONNAIRE_EMPLOYEES` ( `QUESTIONNAIRE_ID` INTEGER NOT NULL, `EMPLOYEE_ID` INTEGER NOT NULL, PRIMARY KEY(`QUESTIONNAIRE_ID`,`EMPLOYEE_ID`), FOREIGN KEY(`QUESTIONNAIRE_ID`) REFERENCES `QUESTIONNAIRES`(`ID`), FOREIGN KEY(`EMPLOYEE_ID`) REFERENCES EMPLOYEES(ID) )");
        db.execSQL("CREATE TABLE `QUESTIONNAIRE_QUESTIONS` ( `QUESTIONNAIRE_ID` INTEGER NOT NULL, `QUESTION_ID` INTEGER NOT NULL, PRIMARY KEY(`QUESTIONNAIRE_ID`,`QUESTION_ID`), FOREIGN KEY(`QUESTIONNAIRE_ID`) REFERENCES `QUESTIONNAIRES`(`ID`), FOREIGN KEY(`QUESTION_ID`) REFERENCES QUESTIONS(ID))");
        db.execSQL("CREATE TABLE `COMPLETED_SURVEYS` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `QUESTIONNAIRE_ID` INTEGER NOT NULL, `EMPLOYEE_ID` INTEGER NOT NULL, REFERENCE_NO TEXT NULL, `COMPLETED_SURVEY_ID` INTEGER NULL, `DATE_COMPLETED` TEXT NOT NULL, FOREIGN KEY(`QUESTIONNAIRE_ID`) REFERENCES `QUESTIONNAIRES`(`ID`), FOREIGN KEY(`EMPLOYEE_ID`) REFERENCES EMPLOYEES(ID) )");
        db.execSQL("CREATE TABLE `RESPONSES` ( `ID` INTEGER PRIMARY KEY, `RESPONSE_DATE` TEXT NOT NULL, `QUESTION_ID` INTEGER NOT NULL,`QUESTIONNAIRE_ID` INTEGER NULL,`RESPONSE_TYPE` TEXT NULL,FOREIGN KEY(`QUESTION_ID`) REFERENCES QUESTIONS(ID),FOREIGN KEY(`QUESTIONNAIRE_ID`) REFERENCES QUESTIONNAIRES(`ID`))");
        db.execSQL("CREATE TABLE `TEMP_RESPONSES` ( `ID` INTEGER PRIMARY KEY AUTOINCREMENT, `RESPONSE_DATE` TEXT NOT NULL, `QUESTION_ID` INTEGER NOT NULL,`VALUE` TEXT NULL,`RESPONSE_TYPE` TEXT NULL,FOREIGN KEY(`QUESTION_ID`) REFERENCES QUESTIONS(ID))");
        db.execSQL("CREATE TABLE `VILLAGE_QUESTIONNAIRES` (`VILLAGE_ID` INTEGER NOT NULL, `QUESTIONNAIRE_ID` INTEGER NOT NULL, PRIMARY KEY(`VILLAGE_ID`,`QUESTIONNAIRE_ID`)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
