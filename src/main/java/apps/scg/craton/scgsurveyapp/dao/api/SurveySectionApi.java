package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.SurveySection;
import apps.scg.craton.scgsurveyapp.dao.SurveySectionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/07
 */

public class SurveySectionApi extends Api {
    public SurveySectionApi() throws DataException{
        super(new SyncHttpClient());

        SurveySectionDao.initialize();
    }

    public void getSections()
    {
        http.get(Routes.ALL_SECTIONS_ROUTE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    for(SurveySection surveySection: SurveySection.fromJson((JSONArray) response.get("data")))
                    {
                        SurveySectionDao.add(surveySection);
                    }

                    SurveySectionDao.terminate();
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
