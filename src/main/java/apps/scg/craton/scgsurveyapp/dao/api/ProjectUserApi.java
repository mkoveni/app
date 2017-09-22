package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.bll.ProjectUser;
import apps.scg.craton.scgsurveyapp.dao.ProjectDao;
import apps.scg.craton.scgsurveyapp.dao.ProjectUserDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import cz.msebera.android.httpclient.Header;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class ProjectUserApi extends Api{

    public ProjectUserApi() throws DataException{
        super(new SyncHttpClient());
        ProjectUserDao.initialize();
    }

    public void getProjectUsers()
    {
        http.get(Routes.ALL_PROJECT_USERS_ROUTE, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {

                    for(ProjectUser projectUser: ProjectUser.fromJson((JSONArray) response.get("data")))
                    {
                        ProjectUserDao.add(projectUser);
                    }
                    ProjectUserDao.terminate();

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
