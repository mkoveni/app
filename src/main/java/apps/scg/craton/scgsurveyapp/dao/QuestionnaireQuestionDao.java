package apps.scg.craton.scgsurveyapp.dao;

import android.os.Environment;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import apps.scg.craton.scgsurveyapp.bll.QuestionnaireQuestion;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class QuestionnaireQuestionDao {

    private static ArrayList<QuestionnaireQuestion> questionnaireQuestions;

    public static void add(QuestionnaireQuestion questionnaireQuestion) {
        if(!exists(questionnaireQuestion.getQuestionId()))
            questionnaireQuestions.add(questionnaireQuestion);
    }

    private static boolean exists(int id)
    {
        QuestionnaireQuestion questionnaireQuestion = get(id);

        return questionnaireQuestion != null;
    }

    public static QuestionnaireQuestion get(int id)
    {
        ArrayList<QuestionnaireQuestion> questionnaireQ =  Stream.of(questionnaireQuestions)
                .filter(v -> v.getQuestionId() == id)
                .collect(Collectors.toCollection(ArrayList::new));

        return questionnaireQ.size() > 0 ? questionnaireQ.get(0) : null;
    }

    public static ArrayList<QuestionnaireQuestion> getBySection(String section){
        return Stream.of(questionnaireQuestions)
                .filter(s -> s.getSection().equals(section))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            questionnaireQuestions = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "project_question.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                questionnaireQuestions = (ArrayList<QuestionnaireQuestion>) obj.readObject();
            }
            else
            {
                file.createNewFile();
            }
        }
        catch (ClassNotFoundException ex)
        {
            throw new DataException("Could not access storage");
        }
        catch (IOException ex)
        {
            throw new DataException("failed to access storage");
        }

    }
    public static void terminate() {
        try {
            File path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            File file = new File(path, "project_question.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(questionnaireQuestions);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
