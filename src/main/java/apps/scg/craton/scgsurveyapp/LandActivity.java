package apps.scg.craton.scgsurveyapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.components.SearchableSpinner;
import apps.scg.craton.scgsurveyapp.dao.ChoiceDao;
import apps.scg.craton.scgsurveyapp.dao.QuestionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

public class LandActivity extends AppCompatActivity {

    Question question = null;
    ArrayList<Choice> options = null;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);

        LinearLayout question_container = (LinearLayout) findViewById(R.id.question_container);
        TextView lblWorker_welcome_message = (TextView) findViewById(R.id.worker_name);
        TextView lblVillage_name = (TextView) findViewById(R.id.village_name);

        User user = (User) getIntent().getSerializableExtra("user");
        Village village = (Village) getIntent().getSerializableExtra("village");
        Project project = (Project) getIntent().getSerializableExtra("project");
        Choice choice = (Choice) getIntent().getSerializableExtra("choice");

        lblVillage_name.setText("VILLAGE:"+ village.getName());
        lblWorker_welcome_message.setText("WELCOME :" +user.getName());


        dialog = ProgressDialog.show(LandActivity.this,"Loading","Preparing your content, pleas wait...",true,false);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();

                TextView textView = (TextView) getLayoutInflater().inflate(R.layout.question_textview, question_container, false);
                textView.setText(String.format("%s", question.getQuestion()));

                SearchableSpinner spinner = (SearchableSpinner) getLayoutInflater().inflate(R.layout.question_options_spinner
                        ,question_container,false);
                ArrayAdapter<Choice> choiceArrayAdapter = new ArrayAdapter<Choice>(getApplicationContext()
                        ,android.R.layout.simple_spinner_item, options);
                choiceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(choiceArrayAdapter);


                Button btn = (Button) getLayoutInflater().inflate(R.layout.btn_success,question_container,false);
                btn.setText(R.string.str_continue);

                btn.setOnClickListener(e->{

                    Choice participate = (Choice) spinner.getSelectedItem();

                    if(participate.getChoice().equals("Participate"))
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(LandActivity.this);
                        alert.setTitle("Recording Question!!!");
                        alert.setMessage("Do you want to be recorded?");
                        alert.setPositiveButton("Yes",(i,w)->{

                            String activity = "apps.scg.craton.scgsurveyapp.SurveyActivity";

                            if(project.getType().equals("Asset")) activity = "apps.scg.craton.scgsurveyapp.AssetSurveyActivity";

                            Intent nextActivity;
                            nextActivity = new Intent(activity);
                            nextActivity.putExtra("record",true);
                            nextActivity.putExtra("question",question);
                            nextActivity.putExtra("user",user);
                            nextActivity.putExtra("village",village);
                            nextActivity.putExtra("project", project);

                            if(choice != null) nextActivity.putExtra("choice",choice);

                            startActivity(nextActivity);
                        });

                        alert.setNegativeButton("No",(i,w)->{
                            String activity = "apps.scg.craton.scgsurveyapp.SurveyActivity";

                            if(project.getType().equals("Asset")) activity = "apps.scg.craton.scgsurveyapp.AssetSurveyActivity";

                            Intent nextActivity;
                            nextActivity = new Intent(activity);
                            nextActivity.putExtra("record",false);
                            nextActivity.putExtra("question",question);
                            nextActivity.putExtra("user",user);
                            nextActivity.putExtra("village",village);
                            nextActivity.putExtra("project", project);

                            if(choice != null) nextActivity.putExtra("choice",choice);

                            startActivity(nextActivity);
                        });

                        Dialog d = alert.create();
                        d.show();

                    }

                });

                question_container.addView(textView);
                question_container.addView(spinner);
                question_container.addView(btn);

            }
        };

        Thread thread  = new Thread(() -> {
            try
            {
                QuestionDao.initialize();
                ChoiceDao.initialize();
                question = QuestionDao.getByName("household_reaction");
                options = ChoiceDao.getByQuestionId(question.getId());
                handler.sendEmptyMessage(0);
            }
            catch (DataException e) {}

        });

        thread.start();
    }
}
