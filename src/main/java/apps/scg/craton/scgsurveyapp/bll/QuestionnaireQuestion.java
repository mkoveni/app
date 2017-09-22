package apps.scg.craton.scgsurveyapp.bll;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class QuestionnaireQuestion implements Serializable{

    private int questionnaire_id;
    private int questionId;
    private String section;
    private Project project;
    private Question question;

    public int getQuestionnaire_id() {
        return questionnaire_id;
    }

    public void setQuestionnaire_id(int questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public static ArrayList<QuestionnaireQuestion> fromJson(JSONArray array)
    {
        ArrayList<QuestionnaireQuestion> questionnaireQuestions = new ArrayList<>();

        try {
            for (int x = 0; x < array.length(); x++) {
                JSONObject jsonObject = array.getJSONObject(x);
                QuestionnaireQuestion questionnaireQuestion = new QuestionnaireQuestion();
                questionnaireQuestion.setQuestionId(jsonObject.getInt("question_id"));
                questionnaireQuestion.setQuestionnaire_id(jsonObject.getInt("questionnaire_id"));
                questionnaireQuestion.setSection(jsonObject.getString("section"));

                questionnaireQuestions.add(questionnaireQuestion);
            }
        }
        catch (JSONException e){e.printStackTrace();}

        return questionnaireQuestions;
    }

}
