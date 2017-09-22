package apps.scg.craton.scgsurveyapp.dao.api;

import com.loopj.android.http.SyncHttpClient;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public abstract class Api {

    protected SyncHttpClient http;

    public Api(SyncHttpClient httpClient)
    {
        http = httpClient;
    }
}
