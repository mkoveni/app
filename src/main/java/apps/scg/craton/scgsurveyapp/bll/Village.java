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

public class Village implements Serializable{

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ArrayList<Village> fromJson(JSONArray array)
    {
        ArrayList<Village> villages = new ArrayList<>();
        JSONObject jsonObject;

        try{
            for (int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                Village village = new Village();
                village.setName(jsonObject.getString("name"));
                village.setId(jsonObject.getInt("id"));

                villages.add(village);
            }


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return villages;
    }
}
