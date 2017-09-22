package apps.scg.craton.scgsurveyapp.bll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class QuestionType implements Serializable{

    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ArrayList<QuestionType> fromJson(JSONArray array)
    {
        ArrayList<QuestionType> questionTypes = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                QuestionType questionType = new QuestionType();
                questionType.setId(jsonObject.getInt("id"));
                questionType.setType(jsonObject.getString("type"));

                questionTypes.add(questionType);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return questionTypes;

    }
}
