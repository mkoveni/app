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

import apps.scg.craton.scgsurveyapp.bll.ChoiceDependent;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/31
 */

public class ChoiceDependentDao {

    private static ArrayList<ChoiceDependent> choiceDependents;

    public static void add(ChoiceDependent choiceDependent) {
        if(!exists(choiceDependent.getChoiceId()))
            choiceDependents.add(choiceDependent);
    }

    private static boolean exists(int id)
    {
        ChoiceDependent choiceDependent = get(id);

        return choiceDependent != null;
    }

    public static ChoiceDependent get(int id)
    {
        ArrayList<ChoiceDependent> cd =  Stream.of(choiceDependents)
                .filter(v -> v.getChoiceId() == id)
                .collect(Collectors.toCollection(ArrayList::new));

        return cd.size() > 0 ? cd.get(0) : null;
    }

    public static ArrayList<ChoiceDependent> getChoiceDependents()
    {
        return choiceDependents;
    }
    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            choiceDependents = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "choice_dependent.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                choiceDependents = (ArrayList<ChoiceDependent>) obj.readObject();
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

            File file = new File(path, "choice_dependent.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(choiceDependents);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
