package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.QuestionnaireQuestion;
import apps.scg.craton.scgsurveyapp.dao.QuestionnaireQuestionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class QuestionnaireQuestionApi extends Api{
    public QuestionnaireQuestionApi() throws DataException{
        super(new SyncHttpClient());
        QuestionnaireQuestionDao.initialize();

    }

    public void getQuestionnaireQuestions()
    {
        http.get(Routes.ALL_QUESTIONNAIRE_QUESTIONS_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    for (QuestionnaireQuestion questionnaireQuestion: QuestionnaireQuestion.fromJson((JSONArray) response.get("data")))
                    {
                        QuestionnaireQuestionDao.add(questionnaireQuestion);
                    }

                    QuestionnaireQuestionDao.terminate();
                }
                catch (JSONException e){}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
