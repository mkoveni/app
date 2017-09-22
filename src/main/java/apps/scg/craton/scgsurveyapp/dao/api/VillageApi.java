package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.dao.VillageDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class VillageApi extends Api{

    public VillageApi() throws DataException
    {
        super(new SyncHttpClient());
        VillageDao.initialize();
    }

    public void getVillages()
    {
        this.http.get(Routes.ALL_VILLAGES_ROUTE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    for(Village village: Village.fromJson((JSONArray) response.get("data")))
                    {
                        VillageDao.add(village);
                    }
                    VillageDao.terminate();
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
