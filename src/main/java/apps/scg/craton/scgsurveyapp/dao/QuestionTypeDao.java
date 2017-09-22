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

import apps.scg.craton.scgsurveyapp.bll.QuestionType;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class QuestionTypeDao {

    private static ArrayList<QuestionType> questionTypes;

    public static void add(QuestionType questionType) {
        if(!exists(questionType.getId()))
            questionTypes.add(questionType);
    }

    private static boolean exists(int id)
    {
        QuestionType questionType = get(id);

        return questionType != null;
    }

    public static QuestionType get(int id)
    {
        ArrayList<QuestionType> results =  Stream.of(questionTypes)
                .filter(v -> v.getId() == id)
                .collect(Collectors.toCollection(ArrayList::new));

        return results.size() > 0 ? results.get(0) : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            questionTypes = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "question_types.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                questionTypes = (ArrayList<QuestionType>) obj.readObject();
                obj.close();
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
        try
        {
            File path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            File file = new File(path, "question_types.dat");

            if (file.exists())
            {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(questionTypes);
                obj.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
