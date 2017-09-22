package apps.scg.craton.scgsurveyapp.bll;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/17
 */

public class Response implements Serializable{

    private int id;
    private String respondable_id;
    private String respondable_type;
    private int questionId;
    private String type;
    private String value;
    private boolean synced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRespondable_id() {
        return respondable_id;
    }

    public void setRespondable_id(String respondable_id) {
        this.respondable_id = respondable_id;
    }

    public String getRespondable_type() {
        return respondable_type;
    }

    public void setRespondable_type(String respondable_type) {
        this.respondable_type = respondable_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public RequestParams getRequestParams() {

        RequestParams params = new RequestParams();
        params.put("respondable_id", respondable_id);
        params.put("respondable_type", respondable_type);
        params.put("value", value);
        params.put("question_id", questionId);
        params.put("type", type);

        return params;
    }

    public static JSONArray toJSON(ArrayList<Response> responses) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        try {
            for (Response response : responses) {
                jsonObject = new JSONObject();
                jsonObject.put("question_id", response.getId());
                jsonObject.put("value", response.getValue());
                jsonObject.put("respondable_type", response.getRespondable_type());
                jsonObject.put("respondable_id", response.getRespondable_id());
                jsonObject.put("type", response.getType());

                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
