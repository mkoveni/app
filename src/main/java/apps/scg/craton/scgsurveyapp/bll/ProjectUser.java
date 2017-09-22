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

public class ProjectUser implements Serializable{

    private int userId;
    private int projectId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public static ArrayList<ProjectUser> fromJson(JSONArray array)
    {
        ArrayList<ProjectUser> projectUsers = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                ProjectUser projectUser = new ProjectUser();
                projectUser.setUserId(jsonObject.getInt("user_id"));
                projectUser.setProjectId(jsonObject.getInt("project_id"));

                projectUsers.add(projectUser);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return projectUsers;

    }
}
