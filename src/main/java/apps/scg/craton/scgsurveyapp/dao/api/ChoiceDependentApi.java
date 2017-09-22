package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.ChoiceDependent;
import apps.scg.craton.scgsurveyapp.dao.ChoiceDependentDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/07
 */

public class ChoiceDependentApi extends Api {
    public ChoiceDependentApi() throws DataException{
        super(new SyncHttpClient());
        ChoiceDependentDao.initialize();
    }

    public void getDepedents()
    {
        http.get(Routes.ALL_CHOICES_DEPENDENTS_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    for(ChoiceDependent choiceDependent: ChoiceDependent.fromJson((JSONArray) response.get("data")))
                    {
                        ChoiceDependentDao.add(choiceDependent);
                    }

                    ChoiceDependentDao.terminate();
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
