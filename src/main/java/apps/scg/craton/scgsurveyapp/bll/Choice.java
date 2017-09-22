package apps.scg.craton.scgsurveyapp.bll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class Choice implements Serializable {

    private int id;
    private int questionId;
    private String name;
    private String choice;
    private Question dependent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Question getDependent() {
        return dependent;
    }

    public void setDependent(Question dependent) {
        this.dependent = dependent;
    }

    @Override
    public String toString()
    {
        return choice;
    }

    public static ArrayList<Choice> fromJson(JSONArray array)
    {
        ArrayList<Choice> choices = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                Choice choice = new Choice();
                choice.setChoice(jsonObject.getString("choice"));
                choice.setId(jsonObject.getInt("id"));
                choice.setName(jsonObject.getString("name"));
                choice.setQuestionId(jsonObject.getInt("question_id"));

                choices.add(choice);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return choices;

    }

}