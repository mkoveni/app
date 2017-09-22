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

public class ChoiceDependent implements Serializable{

    private int choiceId;
    private int questionId;

    public int getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(int choiceId) {
        this.choiceId = choiceId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public static ArrayList<ChoiceDependent> fromJson(JSONArray array)
    {
        ArrayList<ChoiceDependent> choiceDependents = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                ChoiceDependent choiceDependent = new ChoiceDependent();
                choiceDependent.setQuestionId(jsonObject.getInt("question_id"));
                choiceDependent.setChoiceId(jsonObject.getInt("choice_id"));

                choiceDependents.add(choiceDependent);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return choiceDependents;

    }
}


