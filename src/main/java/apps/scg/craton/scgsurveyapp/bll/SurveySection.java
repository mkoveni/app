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

public class SurveySection implements Serializable{

    private String section;
    private String description;
    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<SurveySection> fromJson(JSONArray array)
    {
        ArrayList<SurveySection> surveySections = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                SurveySection surveySection = new SurveySection();
                surveySection.setSection(jsonObject.getString("section"));
                surveySection.setDescription(jsonObject.getString("description"));

                surveySections.add(surveySection);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return surveySections;

    }
}
