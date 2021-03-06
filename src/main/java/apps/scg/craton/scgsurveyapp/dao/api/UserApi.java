package apps.scg.craton.scgsurveyapp.dao.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.dao.UserDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class UserApi extends Api{

    private Context context;

    public UserApi(Context context) throws DataException {
        super(new SyncHttpClient());
        this.context = context;

        UserDao.initialize(context);
    }

    public void getUsers()
    {
        this.http.get(Routes.ALL_USERS_ROUTE, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    for(User user: User.fromJson((JSONArray) response.get("data")))
                    {
                        UserDao.add(user);
                    }
                    UserDao.terminate(context);

                } catch (JSONException e) {
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
