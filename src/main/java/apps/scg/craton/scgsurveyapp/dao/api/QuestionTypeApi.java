package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.QuestionType;
import apps.scg.craton.scgsurveyapp.dao.QuestionTypeDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/07
 */

public class QuestionTypeApi extends Api{
    public QuestionTypeApi() throws DataException{
        super(new SyncHttpClient());
        QuestionTypeDao.initialize();
    }

    public void getQuestionTypes()
    {
        http.get(Routes.ALL_QUESTION_TYPES_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    for(QuestionType questionType: QuestionType.fromJson((JSONArray) response.get("data")))
                    {
                        QuestionTypeDao.add(questionType);
                    }

                    QuestionTypeDao.terminate();
                }
                catch (JSONException e){e.printStackTrace();}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
