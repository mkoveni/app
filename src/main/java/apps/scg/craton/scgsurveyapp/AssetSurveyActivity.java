package apps.scg.craton.scgsurveyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.bll.QuestionnaireQuestion;
import apps.scg.craton.scgsurveyapp.bll.SurveySection;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.components.SearchableSpinner;
import apps.scg.craton.scgsurveyapp.dao.QuestionChoiceHelper;
import apps.scg.craton.scgsurveyapp.dao.QuestionnaireQuestionDao;
import apps.scg.craton.scgsurveyapp.dao.SurveySectionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import apps.scg.craton.scgsurveyapp.services.AudioRecorderService;
import apps.scg.craton.scgsurveyapp.services.UnitConverter;
import apps.scg.craton.scgsurveyapp.validation.RequiredRule;

public class AssetSurveyActivity extends AppCompatActivity {

    ArrayList<Question> questions;
    SurveySection surveySection;
    Map<Integer,View> controls;
    Validator validator;
    AudioRecorderService audioRecorderService;
    int questionnaireId;
    User user;
    Village village;
    boolean onBackButtonExit = false;
    ProgressDialog dialog;
    QuestionChoiceHelper questionChoiceHelper;
    Button btn_end_survey;
    LinearLayout question_container;
    int number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.question_listing);


        question_container = (LinearLayout) findViewById(R.id.question_container);
        controls = new HashMap<>();

        boolean canRecord = getIntent().getBooleanExtra("record",false);
        validator = new Validator(AssetSurveyActivity.this);
        user = (User) getIntent().getSerializableExtra("user");
        village = (Village) getIntent().getSerializableExtra("village");

        TextView txtName = (TextView)findViewById(R.id.title);
        txtName.setText(village.getName()+ "Questionnaire");

        dialog = ProgressDialog.show(AssetSurveyActivity.this,"yoh","loading man", true,false);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                generateQuestionnaireControls();
                dialog.dismiss();
            }
        };

        Thread thread = new Thread(){

            @Override
            public void run() {

                try {
                    initialize();
                    handler.sendEmptyMessage(0);
                } catch (DataException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

    private void initialize() throws DataException
    {
        questions = new ArrayList<>();
        questionChoiceHelper = new QuestionChoiceHelper();

        SurveySectionDao.initialize();
        QuestionnaireQuestionDao.initialize();

        surveySection = SurveySectionDao.get(((Choice) getIntent().getSerializableExtra("choice")).getChoice());

        ArrayList<QuestionnaireQuestion> questionnaireQuestions = QuestionnaireQuestionDao
                .getBySection(surveySection.getSection());

        for(QuestionnaireQuestion questionnaireQuestion: questionnaireQuestions)
        {
            questions.add(questionChoiceHelper.getQuestionById(questionnaireQuestion.getQuestionId()));
        }
    }
    private void generateQuestionnaireControls() {
        number = 1;
        LinearLayout categoryThumb = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, (int) UnitConverter.convertToDp(getApplicationContext(), 10));
        categoryThumb.setLayoutParams(params);
        categoryThumb.setPadding(0, (int) UnitConverter.convertToDp(getApplicationContext(), 10), 0, (int) UnitConverter.convertToDp(getApplicationContext(), 10));
        ImageView imageView = (ImageView) getLayoutInflater().inflate(R.layout.category_image, categoryThumb, false);
        TextView views = (TextView) getLayoutInflater().inflate(R.layout.category_text, categoryThumb, false);

        imageView.setImageResource(R.drawable.house);
        views.setText(surveySection.getSection() + " - " + surveySection.getDescription());
        categoryThumb.addView(imageView);
        categoryThumb.addView(views);
        question_container.addView(categoryThumb);
        renderQuestions();

        btn_end_survey = (Button) getLayoutInflater().inflate(R.layout.btn_success,question_container,false);
        btn_end_survey.setText("SAVE");
        btn_end_survey.setOnClickListener(e->{

            String nextActivity = "apps.scg.craton.scgsurveyapp.QUESTIONNARES";
            Intent intent = new Intent(nextActivity);
            intent.putExtra("authenticatedUser", user);
            startActivity(intent);
        });

        question_container.addView(btn_end_survey);


    }

    private void renderQuestions() {
        for(Question question : questions)
        {
            String type = question.getQuestionType().getType();
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.question_textview, question_container, false);
            textView.setText(String.format("%d. %s", number,question.getQuestion()));
            number++;
            question_container.addView(textView);

            if(type.equals("DROPDOWN"))
            {
                SearchableSpinner spinner = (SearchableSpinner) getLayoutInflater().inflate(R.layout.question_options_spinner,
                        question_container, false);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                ArrayList<Choice> choices = question.getChoices();

                spinner.setLayoutParams(p);

                spinner.getBackground().setColorFilter(Color.parseColor("#B22222"), PorterDuff.Mode.SRC_ATOP);

                ArrayAdapter<Choice> adapter = new ArrayAdapter<Choice>(getApplicationContext(), android.R.layout.simple_spinner_item, choices);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                question_container.addView(spinner);

            }

            if(type.equals("TEXT"))
            {
                EditText text = (EditText) getLayoutInflater().inflate(R.layout.text_edittext, question_container, false);
                question_container.addView(text);
                validator.put(text, new RequiredRule(1));
                controls.put(question.getId(), text);
            }

            if (type.equals("NUMERIC"))
            {
                EditText numeric = (EditText) getLayoutInflater().inflate(R.layout.numeric_edittext, question_container, false);
                question_container.addView(numeric);
            }
        }
    }
}
