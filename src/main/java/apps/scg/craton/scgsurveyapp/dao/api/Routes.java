package apps.scg.craton.scgsurveyapp.dao.api;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class Routes {

    private static final String BASE_URL = "http://app.standardcorp.co.za/public/api/";
    static final String ALL_USERS_ROUTE = BASE_URL +"users/all";
    static final String ALL_VILLAGES_ROUTE = BASE_URL +"villages/all";
    static final String ALL_PROJECTS_ROUTE = BASE_URL +"projects/all";
    static final String ALL_QUESTIONS_ROUTE = BASE_URL +"questions/all";
    static final String ALL_PROJECT_USERS_ROUTE = BASE_URL +"project_users/all";
    static final String ALL_SECTIONS_ROUTE = BASE_URL +"survey_sections/all";
    static final String ALL_QUESTION_TYPES_ROUTE = BASE_URL +"question_types/all";
    static final String ALL_CHOICES_ROUTE = BASE_URL +"choices/all";
    static final String ALL_CHOICES_DEPENDENTS_ROUTE = BASE_URL +"choice_dependents/all";
    static final String ALL_QUESTIONNAIRE_QUESTIONS_ROUTE = BASE_URL +"questionnaire_questions/all";

    //POSt

    static final String STORE_RESPONSE_ROUTE = BASE_URL + "responses/store";


}
