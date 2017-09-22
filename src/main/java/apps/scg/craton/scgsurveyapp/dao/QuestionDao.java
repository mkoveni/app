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

import apps.scg.craton.scgsurveyapp.bll.Question;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class QuestionDao {
    private static ArrayList<Question> questions;

    public static void add(Question question) {
        if(!exists(question.getId()))
            questions.add(question);
    }

    private static boolean exists(int id)
    {
        Question question = get(id);

        return question != null;
    }

    public static Question get(int id)
    {
        ArrayList<Question> questions = Stream.of(getQuestions())
                .filter(q -> q.getId() == id)
                .collect(Collectors.toCollection(ArrayList::new));
        return questions.size() > 0 ? questions.get(0) : null;
    }

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public static Question getByName(String name)
    {

        ArrayList<Question> questions = Stream.of(getQuestions())
                .filter(q -> q.getName().equals(name))
                .collect(Collectors.toCollection(ArrayList::new));
        return questions.size() > 0 ? questions.get(0) : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            questions = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "question.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                questions = (ArrayList<Question>) obj.readObject();
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

            File file = new File(path, "question.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(questions);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
