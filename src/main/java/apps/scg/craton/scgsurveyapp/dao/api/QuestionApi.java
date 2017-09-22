package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.dao.QuestionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class QuestionApi extends Api{

    public QuestionApi() throws DataException
    {
        super(new SyncHttpClient());
        QuestionDao.initialize();
    }

    public void getQuestions()
    {
        this.http.get(Routes.ALL_QUESTIONS_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try
                {
                    for(Question question: Question.fromJson((JSONArray) response.get("data")))
                    {
                        QuestionDao.add(question);
                    }

                    QuestionDao.terminate();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
