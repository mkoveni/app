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

import apps.scg.craton.scgsurveyapp.bll.IndividualSurvey;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/31
 */

public class IndividualSurveyDao {

    private static ArrayList<IndividualSurvey> individualSurveys;

    public static void add(IndividualSurvey individualSurvey) {
        if(!exists(individualSurvey.getId()))
            individualSurveys.add(individualSurvey);
    }

    private static boolean exists(int id)
    {
        IndividualSurvey individualSurvey = get(id);

        return individualSurvey != null;
    }

    public static IndividualSurvey get(int id)
    {
        return Stream.of(individualSurveys)
                .filter(v -> v.getReferenceNo().equals(id)).single();
    }

    public static ArrayList<IndividualSurvey> notSynced() {
        return Stream.of(individualSurveys)
                .filter(individualSurvey -> !individualSurvey.isSynced())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            individualSurveys = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "individual_survey.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                individualSurveys = (ArrayList<IndividualSurvey>) obj.readObject();
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

            File file = new File(path, "individual_survey.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(individualSurveys);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
