package apps.scg.craton.scgsurveyapp.bll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import apps.scg.craton.scgsurveyapp.bll.contracts.IArray;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class User implements Serializable{

    private int id;
    private String name;
    private String email;
    private String username;
    private String password;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static ArrayList<User> fromJson(JSONArray array)
    {
        ArrayList<User> users = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                User user = new User();
                user.setId(jsonObject.getInt("id"));
                user.setEmail(jsonObject.getString("email"));
                user.setUsername(jsonObject.getString("username"));
                user.setPassword(jsonObject.getString("password"));
                user.setName(jsonObject.getString("name"));

                users.add(user);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return users;

    }
}
