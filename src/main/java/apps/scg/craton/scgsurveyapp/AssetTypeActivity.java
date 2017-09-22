package apps.scg.craton.scgsurveyapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.components.SearchableSpinner;
import apps.scg.craton.scgsurveyapp.dao.QuestionChoiceHelper;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

public class AssetTypeActivity extends AppCompatActivity {


    QuestionChoiceHelper questionChoiceHelper;
    Question question;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_asset_type);

        dialog = ProgressDialog.show(AssetTypeActivity.this, "Loading", "please wait...", true, false);
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.question_container);
                    TextView label = (TextView) getLayoutInflater().inflate(R.layout.question_textview, linearLayout,false);
                    label.setText(question.getQuestion());

                    ArrayAdapter<Choice> choiceArrayAdapter = new ArrayAdapter<Choice>(AssetTypeActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, question.getChoices());

                    SearchableSpinner spinner = (SearchableSpinner) getLayoutInflater()
                            .inflate(R.layout.question_options_spinner, linearLayout, false);
                    spinner.setAdapter(choiceArrayAdapter);

                    Button  next = (Button) getLayoutInflater().inflate(R.layout.btn_success, linearLayout, false);
                    next.setText("NEXT");
                    next.setOnClickListener(e -> {
                        dialog = ProgressDialog.show(AssetTypeActivity.this, "Loading", "please wait...", true, false);
                        Handler nextHandler = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                dialog.dismiss();
                            }
                        };

                        Thread nextThread = new Thread(() -> {
                            Intent intent = new Intent("apps.scg.craton.scgsurveyapp.LandActivity");
                            intent.putExtra("village",getIntent().getSerializableExtra("village"));
                            intent.putExtra("user", getIntent().getSerializableExtra("user"));
                            intent.putExtra("project", getIntent().getSerializableExtra("project"));
                            intent.putExtra("choice", (Choice) spinner.getSelectedItem());
                            nextHandler.sendEmptyMessage(0);
                            startActivity(intent);
                        });

                        nextThread.start();
                    });

                    linearLayout.addView(label);
                    linearLayout.addView(spinner);
                    linearLayout.addView(next);
                    dialog.dismiss();

                }
            };

            Thread thread = new Thread(() -> {
                try
                {
                    initialise();
                    handler.sendEmptyMessage(0);
                }catch (DataException e){e.printStackTrace();}
            });
        thread.start();
    }

    private void initialise() throws DataException
    {
        questionChoiceHelper = new QuestionChoiceHelper();
        question = questionChoiceHelper.getQuestionByName("type_of_asset");
        //end
    }


}
