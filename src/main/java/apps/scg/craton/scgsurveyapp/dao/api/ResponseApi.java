package apps.scg.craton.scgsurveyapp.dao.api;

import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.Response;
import apps.scg.craton.scgsurveyapp.dao.ResponseDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

import static apps.scg.craton.scgsurveyapp.dao.api.Routes.STORE_RESPONSE_ROUTE;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/18
 */

public class ResponseApi  extends Api{
    public ResponseApi() throws DataException{
        super(new SyncHttpClient());
        ResponseDao.initialize();
    }

    public void syncResponses() {
        RequestParams params = new RequestParams();
        params.put("data", Response.toJSON(ResponseDao.notSynced()));

        http.post(STORE_RESPONSE_ROUTE, params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray array = (JSONArray) response.get("data");

                    Log.e("SUCCESS","SAVING WAS SUCCESSFUL");
                }
                catch (JSONException e){e.printStackTrace();}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                Log.e("STATUS_CODE_MKOVENI", ""+statusCode);
                Log.e("RESPONSE_MESSAGE", responseString);
            }
        });
        for(Response res: ResponseDao.notSynced()) {

        }
        ResponseDao.terminate();
    }
}
