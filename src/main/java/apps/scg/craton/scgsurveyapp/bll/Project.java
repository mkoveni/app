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

public class Project implements Serializable{

    private int id;
    private int questionnaireId;
    private int villageId;
    private String name;
    private String type;

    private Village village;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getVillageId() {
        return villageId;
    }

    public void setVillageId(int villageId) {
        this.villageId = villageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public static ArrayList<Project> fromJson(JSONArray array)
    {
        ArrayList<Project> projects = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                Project project = new Project();
                project.setId(jsonObject.getInt("id"));
                project.setName(jsonObject.getString("name"));
                project.setVillageId(jsonObject.getInt("village_id"));
                project.setType(jsonObject.getString("type"));

                projects.add(project);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return projects;

    }
}
