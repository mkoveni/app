package apps.scg.craton.scgsurveyapp.dao;

import java.util.ArrayList;

import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.bll.ChoiceDependent;
import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/08
 */

public class QuestionChoiceHelper {

    public QuestionChoiceHelper() throws DataException
    {
        QuestionDao.initialize();
        ChoiceDao.initialize();
        ChoiceDependentDao.initialize();
        QuestionTypeDao.initialize();
    }

    public Question getQuestionById(int id)
    {
        Question question = QuestionDao.get(id);

        if(question != null) {
            question.setChoices(getChoiceByQuestionId(question.getId()));
            question.setQuestionType(QuestionTypeDao.get(question.getQuestionTypeId()));
        }

        return question;
    }

    public Question getQuestionByName(String name) {

        Question question = QuestionDao.getByName(name);
        if(question != null)
        {
            question.setChoices(getChoiceByQuestionId(question.getId()));
            question.setQuestionType(QuestionTypeDao.get(question.getQuestionTypeId()));
        }

        return question;
    }

    public ArrayList<Choice> getChoiceByQuestionId(int id)
    {
        ArrayList<Choice> choices = ChoiceDao.getByQuestionId(id);

        for (Choice choice: choices)
        {
            Question question = getQuestionByChoiceId(choice.getId());

            if(question != null){
                question.setChoices(getChoiceByQuestionId(question.getId()));
                question.setQuestionType(QuestionTypeDao.get(question.getQuestionTypeId()));
            }

            choice.setDependent(question);
        }

        return choices;
    }

    public Question getQuestionByChoiceId(int choiceId)
    {
        ChoiceDependent choiceDependent =  ChoiceDependentDao.get(choiceId);

        if(choiceDependent != null)
            return QuestionDao.get(choiceDependent.getQuestionId());
        return null;
    }
}
