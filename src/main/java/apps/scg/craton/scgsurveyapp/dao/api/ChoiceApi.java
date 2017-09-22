package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.dao.ChoiceDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/07
 */

public class ChoiceApi extends Api{
    public ChoiceApi() throws DataException{
        super(new SyncHttpClient());
        ChoiceDao.initialize();
    }

    public void getChoices()
    {
        http.get(Routes.ALL_CHOICES_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    for(Choice choice: Choice.fromJson((JSONArray) response.get("data")))
                    {
                        ChoiceDao.add(choice);
                    }

                    ChoiceDao.terminate();
                }
                catch (JSONException e){e.printStackTrace();}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
