package apps.scg.craton.scgsurveyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import com.annimon.stream.Stream;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.HouseSurvey;
import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.bll.QuestionnaireQuestion;
import apps.scg.craton.scgsurveyapp.bll.Response;
import apps.scg.craton.scgsurveyapp.bll.SurveySection;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.bll.Village;

import apps.scg.craton.scgsurveyapp.components.SearchableSpinner;
import apps.scg.craton.scgsurveyapp.dao.HouseSurveyDao;
import apps.scg.craton.scgsurveyapp.dao.QuestionChoiceHelper;
import apps.scg.craton.scgsurveyapp.dao.QuestionnaireQuestionDao;
import apps.scg.craton.scgsurveyapp.dao.ResponseDao;
import apps.scg.craton.scgsurveyapp.dao.SurveySectionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import apps.scg.craton.scgsurveyapp.fragments.SectionFragment;
import apps.scg.craton.scgsurveyapp.services.AudioRecorderService;
import apps.scg.craton.scgsurveyapp.services.AudioService;
import apps.scg.craton.scgsurveyapp.services.DateService;
import apps.scg.craton.scgsurveyapp.services.UnitConverter;
import apps.scg.craton.scgsurveyapp.validation.RequiredRule;

public class SurveyActivity extends AppCompatActivity {
    int number;

    ArrayList<Question> questions;
    SurveySection surveySection;
    Map<Integer,View> controls;
    Validator validator;
    AudioRecorderService audioRecorderService;
    LinearLayout question_container;
    User user;
    Village village;
    boolean onBackButtonExit = false;
    ProgressDialog dialog;
    QuestionChoiceHelper questionChoiceHelper;
    Button btn_end_survey;

    ArrayList<Response> householdResponses;
    HouseSurvey houseSurvey;
    boolean canRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(SurveyActivity.this);
        validator.setValidationListener(new ValidationHandler());
        user = (User) getIntent().getSerializableExtra("user");
        village = (Village) getIntent().getSerializableExtra("village");


        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.question_listing);

        question_container = (LinearLayout) findViewById(R.id.question_container);

        controls = new Hashtable<>();

        canRecord= getIntent().getBooleanExtra("record",false);

        TextView txtName = (TextView)findViewById(R.id.title);
        txtName.setText(village.getName()+ "Questionnaire");

        dialog = ProgressDialog.show(SurveyActivity.this,"Loading","Loading app data, please wait...",true,false);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                generateQuestionnaireControls();
                dialog.dismiss();
                Toast.makeText(SurveyActivity.this, "I'm done with you..." + QuestionnaireQuestionDao.getBySection("Section 1").size(), Toast.LENGTH_LONG).show();
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
        try
        {

            if(canRecord)
            {
                String filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio_"+new Date()+".3gp";

                /*Intent intentService = new Intent(SurveyActivity.this, AudioService.class);
                intentService.putExtra("file", filename);
                startService(intentService);*/
                try {
                    audioRecorderService = new AudioRecorderService(filename);
                    audioRecorderService.startRecoding();
                }
                catch (DataException e){e.printStackTrace();}
            }
        }
        catch (NumberFormatException e)
        {
            Log.i("Format",e.getMessage());
        }

    }

    private void initialize() throws DataException {
        questions = new ArrayList<>();
        questionChoiceHelper = new QuestionChoiceHelper();
        householdResponses = new ArrayList<>();

        SurveySectionDao.initialize();
        QuestionnaireQuestionDao.initialize();
        surveySection = SurveySectionDao.get("Section 1");
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

            dialog = ProgressDialog.show(SurveyActivity.this,"Processing","please wait",true,false);
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    dialog.dismiss();
                   /* Intent intentService = new Intent(SurveyActivity.this, AudioService.class);
                    stopService(intentService);*/

                   if(canRecord)
                       audioRecorderService.stopRecording();
                }
            };

            Thread thread = new Thread(() -> {

                String nextActivity = "apps.scg.craton.scgsurveyapp.QUESTIONNARES";
                Intent intent = new Intent(nextActivity);
                intent.putExtra("authenticatedUser", user);
                handler.sendEmptyMessage(0);

                startActivity(intent);
            });

            thread.start();

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
                controls.put(question.getId(), spinner);

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
                validator.put(numeric, new RequiredRule(1));
                controls.put(question.getId(), numeric);
            }
        }
    }

    private void showSection(int members, String sec) {

        Stream.range(0,members).forEach(e -> {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            Fragment fragment = getSupportFragmentManager().findFragmentByTag("section_b");

            if(fragment != null)
            {
                ft.remove(fragment);
            }

            SectionFragment section = new SectionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("section", sec);
            section.setArguments(bundle);
            section.show(ft, "section_b");
            section.startQuestionLoaderTask();
        });
    }



    private void prepare() {
        DateService service = new DateService();
        service.setFormat("yyyyMMdd");
        Project project = (Project) getIntent().getSerializableExtra("project");

        String reference = village.getName().substring(0,3).toUpperCase() +"_"+ service.DateToString(new Date())+"_01";

        HouseSurvey lastRecord = HouseSurveyDao.contains(reference.substring(0,12));

        houseSurvey = new HouseSurvey();
        houseSurvey.setProjectId(project.getId());
        houseSurvey.setReferenceNo(reference);

        if(lastRecord != null)
        {
            String old_ref = lastRecord.getReferenceNo();

            int number = Integer.parseInt(old_ref.substring(old_ref.lastIndexOf("_")+1));
            number += 1;
            String last_number;

            last_number = ""+number;

            if(number < 10)
                last_number = "0" + number;

            String newReference = old_ref.substring(0,12) + "_"+last_number;

            houseSurvey.setReferenceNo(newReference);
        }

        houseSurvey.setSynced(false);

        for(Map.Entry<Integer, View> item: controls.entrySet()) {

            Question question = questionChoiceHelper.getQuestionById(item.getKey());
            String type = question.getQuestionType().getType();
            Response response = new Response();
            response.setQuestionId(question.getId());
            response.setRespondable_type("Survey\\Model\\HouseSurvey");
            response.setRespondable_id(houseSurvey.getReferenceNo());
            response.setType(type);

            if(item.getValue() instanceof SearchableSpinner) {

                SearchableSpinner spinner = (SearchableSpinner) item.getValue();
                Choice answer = (Choice) spinner.getSelectedItem();
                response.setValue(answer.getChoice());
            }

            if(item.getValue() instanceof EditText) {

                EditText answer = (EditText) item.getValue();
                response.setValue(answer.getText().toString());
            }

            householdResponses.add(response);
        }
    }

    private void save() {
        HouseSurveyDao.add(houseSurvey);

        for(Response response: householdResponses){
            response.setSynced(false);
            ResponseDao.add(response);
        }

        ResponseDao.terminate();
        HouseSurveyDao.terminate();
    }

    private class ValidationHandler implements Validator.ValidationListener {
        @Override
        public void onValidationSucceeded() {

            //TODO: do some saving operations/ call a function to do some saving operations
        }

        @Override
        public void onValidationFailed(List<ValidationError> list) {

            for(ValidationError error: list)
            {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(SurveyActivity.this);

                if(view instanceof EditText)
                {
                    ((EditText) view).setError(message);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(onBackButtonExit)
        {
            finish();
            moveTaskToBack(true);
        }

        onBackButtonExit = true;
        Toast.makeText(this, "Pressed the back button again to exit.", Toast.LENGTH_SHORT).show();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            onBackButtonExit = false;
        }, 2000);
    }
}
