package apps.scg.craton.scgsurveyapp.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.scg.craton.scgsurveyapp.R;
import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.bll.QuestionnaireQuestion;
import apps.scg.craton.scgsurveyapp.bll.Response;
import apps.scg.craton.scgsurveyapp.bll.SurveySection;
import apps.scg.craton.scgsurveyapp.components.SearchableSpinner;
import apps.scg.craton.scgsurveyapp.dao.QuestionChoiceHelper;
import apps.scg.craton.scgsurveyapp.dao.QuestionnaireQuestionDao;
import apps.scg.craton.scgsurveyapp.dao.SurveySectionDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import apps.scg.craton.scgsurveyapp.services.UnitConverter;
import apps.scg.craton.scgsurveyapp.validation.RequiredRule;

/**
 * @author Rivalani Simon Hlegani
 * @since 2017/05/09
 */

public class SectionFragment extends DialogFragment {
    public Map<Integer,View> controls;
    LinearLayout question_container;
    int number;
    Validator validator;
    QuestionChoiceHelper questionChoiceHelper;
    ArrayList<Question>questions;
    SurveySection surveySection;
    EditText editText;
    ProgressDialog dialog;
    public Map<Integer, ArrayList<Response>> responses;
    public int id;
    Button btn_save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.question_listing,container,false);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("SURVEY");

        validator = new Validator(this);

        question_container = (LinearLayout) view.findViewById(R.id.question_container);
        controls = new HashMap<>();

        btn_save = (Button) inflater.inflate(R.layout.btn_success,question_container,false);
        btn_save.setText("NEXT");
        btn_save.setOnClickListener(e-> getDialog().dismiss());


        setRetainInstance(true);
        return view;
    }

    private void initialize() throws DataException {
        String section = getArguments().getString("section");
        SurveySectionDao.initialize();
        QuestionnaireQuestionDao.initialize();
        questionChoiceHelper = new QuestionChoiceHelper();
        questions = new ArrayList<>();

        surveySection = SurveySectionDao.get(section);
        ArrayList<QuestionnaireQuestion> questionnaireQuestions = QuestionnaireQuestionDao.getBySection(surveySection.getSection());

        for(QuestionnaireQuestion questionnaireQuestion: questionnaireQuestions)
        {
            questions.add(questionChoiceHelper.getQuestionById(questionnaireQuestion.getQuestionId()));
        }

    }

    private void renderSection(LayoutInflater inflater) {
        LinearLayout categoryThumb = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, (int) UnitConverter.convertToDp(getContext(), 10));
        categoryThumb.setLayoutParams(params);
        categoryThumb.setPadding(0, (int) UnitConverter.convertToDp(getContext(), 10), 0, (int) UnitConverter.convertToDp(getContext(), 10));
        ImageView imageView = (ImageView) inflater.inflate(R.layout.category_image, categoryThumb, false);
        TextView views = (TextView) inflater.inflate(R.layout.category_text, categoryThumb, false);

        imageView.setImageResource(R.drawable.house);
        views.setText(surveySection.getSection() + " - " + surveySection.getDescription());
        categoryThumb.addView(imageView);
        categoryThumb.addView(views);
        question_container.addView(categoryThumb);
    }

    private void renderQuestions(LayoutInflater inflater) {
        for(Question question: questions)
        {
            number++;
            String type = question.getQuestionType().getType();

            TextView textView = (TextView) inflater.inflate(R.layout.question_textview, question_container, false);
            textView.setText(String.format("%d. %s", number, question.getQuestion()));

            question_container.addView(textView);

            switch (type){
                case "DROPDOWN":
                    renderDropdownControl(question, inflater);
                    break;
                case "TEXT":
                    renderTextControl(question, inflater);
                    break;
                case "NUMERIC":
                    renderNumericControl(question, inflater);
                    break;
                case "DATE":
                    renderDateControl(question, inflater);
                    break;
            }
        }
    }

    private void renderTextControl(Question question, LayoutInflater inflater) {
        editText = (EditText) inflater.inflate(R.layout.text_edittext,question_container,false);
        question_container.addView(editText);
        controls.put(question.getId(),editText);

        validator.put(editText, new RequiredRule(1));
    }

    private void renderDateControl(Question question, LayoutInflater inflater) {
        editText = (EditText) inflater.inflate(R.layout.text_edittext,question_container,false);
        question_container.addView(editText);
        controls.put(question.getId(),editText);
        validator.put(editText, new RequiredRule(1));
    }

    private void renderNumericControl(Question question,LayoutInflater inflater) {
        editText = (EditText) inflater.inflate(R.layout.text_edittext,question_container,false);
        question_container.addView(editText);
        controls.put(question.getId(),editText);

        validator.put(editText, new RequiredRule(1));
    }

    private void renderDropdownControl(Question question,LayoutInflater inflater) {
        LinearLayout question_block = (LinearLayout) inflater.inflate(R.layout.linear_container, question_container,false);
        SearchableSpinner sp = (SearchableSpinner) inflater.inflate(R.layout.question_options_spinner,question_container,false);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                (int)UnitConverter.convertToDp(getContext(),40));

        sp.setLayoutParams(p);
        ArrayAdapter<Choice> adapter = new ArrayAdapter<Choice>(getContext(),android.R.layout.simple_spinner_dropdown_item,
                question.getChoices());
        sp.setAdapter(adapter);
        question_block.addView(sp);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Question dependent = question.getChoices().get(position).getDependent();
                question_container.requestFocus();
                if(dependent != null)
                {
                    String type = dependent.getQuestionType().getType();
                    TextView label = (TextView) inflater.inflate(R.layout.question_textview, question_container, false);
                    label.setText(dependent.getQuestion());
                    question_block.removeAllViews();

                    question_block.addView(sp);
                    question_block.addView(label);

                    if (type.equals("DROPDOWN"))
                    {

                        SearchableSpinner searchableSpinner = (SearchableSpinner) inflater.inflate(R.layout.question_options_spinner,
                                question_container,false);
                        ArrayAdapter<Choice> ad = new ArrayAdapter<Choice>(getContext(),android.R.layout.simple_spinner_dropdown_item,
                                dependent.getChoices());
                        searchableSpinner.setLayoutParams(p);
                        searchableSpinner.setAdapter(ad);

                        question_block.addView(searchableSpinner);

                        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                question_block.removeAllViews();
                                question_block.addView(sp);
                                question_block.addView(label);
                                question_block.addView(searchableSpinner);

                                Choice choice = dependent.getChoices().get(position);
                                Question second_tag = questionChoiceHelper.getQuestionByChoiceId(choice.getId());

                                if(second_tag != null)
                                {
                                    TextView choice_label = (TextView) inflater.inflate(R.layout.question_textview, question_container,false);
                                    choice_label.setText(second_tag.getQuestion());

                                    question_block.addView(choice_label);

                                    String second_type = second_tag.getQuestionType().getType();

                                    if(second_type.equals("DROPDOWN"))
                                    {
                                        SearchableSpinner choiceSearchableSpinner = (SearchableSpinner) inflater.inflate(R.layout.question_options_spinner,
                                                question_container,false);
                                        choiceSearchableSpinner.setLayoutParams(p);

                                        choiceSearchableSpinner.setAdapter(new ArrayAdapter<Choice>(getContext(),
                                                android.R.layout.simple_spinner_dropdown_item
                                                ,second_tag.getChoices()));

                                        question_block.addView(choiceSearchableSpinner);
                                        if(controls.get(second_tag.getId()) != null) controls.remove(second_tag.getId());
                                        controls.put(second_tag.getId(), choiceSearchableSpinner);
                                    }

                                    if(second_type.equals("TEXT")){
                                        editText = (EditText) inflater.inflate(R.layout.text_edittext,question_container,false);
                                        question_block.addView(editText);

                                        if(controls.get(second_tag.getId()) != null)  controls.remove(second_tag.getId());
                                        controls.put(second_tag.getId(), editText);
                                    }

                                    if(second_type.equals("NUMERIC")) {
                                        editText = (EditText) inflater.inflate(R.layout.numeric_edittext,question_container,false);
                                        question_block.addView(editText);

                                        if(controls.get(second_tag.getId()) != null)  controls.remove(second_tag.getId());
                                        controls.put(second_tag.getId(), editText);
                                    }


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        if(controls.get(dependent.getId()) != null) controls.remove(dependent.getId());
                        controls.put(dependent.getId(),searchableSpinner);
                    }

                    if(type.equals("TEXT"))
                    {
                        editText = (EditText) inflater.inflate(R.layout.text_edittext,question_container,false);
                        question_block.addView(editText);
                    }
                    if(type.equals("NUMERIC"))
                    {
                        editText = (EditText) inflater.inflate(R.layout.numeric_edittext,question_container,false);
                        question_block.addView(editText);
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        question_container.addView(question_block);
        controls.put(question.getId(),sp);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog d = super.onCreateDialog(savedInstanceState);
        d.getWindow().getAttributes().windowAnimations = R.style.dialog_animations;
        this.setCancelable(false);
        return d;
    }

    private class ValidationHandler implements Validator.ValidationListener {

        @Override
        public void onValidationSucceeded() {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Save Warning");
            builder.setMessage("Are you sure that the questions have been answer correctly? Once saved you cannot undo/edit");
            builder.setPositiveButton("Proceed", (dialog, which) -> {

                //TODO - please code some saving mechanism
                saveSurvey();
                Toast.makeText(getContext(), "Survey Saved", Toast.LENGTH_SHORT).show();

                dismiss();
            });

            builder.setNegativeButton("Go Back", (dialog,which) -> {
                dialog.dismiss();
            });

            builder.create().show();
            builder.setCancelable(false);
        }

        @Override
        public void onValidationFailed(List<ValidationError> list) {

            for(ValidationError error:list)
            {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(getContext());

                if(view instanceof EditText)
                {
                    ((EditText)view).setError(message);
                }
            }

        }
    }

    public void startQuestionLoaderTask()
    {
        try{
            initialize();
            renderSection(getActivity().getLayoutInflater());
            renderQuestions(getActivity().getLayoutInflater());
        }
        catch (DataException e){}
    }


    private class QuestionLoaderTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {
                initialize();
            } catch (DataException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            renderSection(getActivity().getLayoutInflater());
            renderQuestions(getActivity().getLayoutInflater());
            question_container.addView(btn_save);

        }
    }

    private void saveSurvey() {

    }
}
