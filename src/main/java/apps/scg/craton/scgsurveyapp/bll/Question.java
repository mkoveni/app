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

public class Question implements Serializable{

    private int id;
    private int questionTypeId;
    private String name;
    private String question;

    private ArrayList<Choice> choices;
    private QuestionType questionType;

    public Question()
    {
        choices = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<Choice> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<Choice> choices) {
        this.choices = choices;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public static ArrayList<Question> fromJson(JSONArray array)
    {
        ArrayList<Question> questions = new ArrayList<>();
        JSONObject jsonObject;

        try {

            for(int x=0; x < array.length(); x++)
            {
                jsonObject = array.getJSONObject(x);

                Question question = new Question();
                question.setId(jsonObject.getInt("id"));
                question.setQuestion(jsonObject.getString("question"));
                question.setName(jsonObject.getString("name"));
                question.setQuestionTypeId(jsonObject.getInt("question_type_id"));

                questions.add(question);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return questions;

    }
}
