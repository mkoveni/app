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
import java.util.LinkedList;

import apps.scg.craton.scgsurveyapp.bll.HouseSurvey;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/31
 */

public class HouseSurveyDao {

    private static ArrayList<HouseSurvey> houseSurveys;

    public static void add(HouseSurvey houseSurvey) {
        if(!exists(houseSurvey.getReferenceNo()))
            houseSurveys.add(houseSurvey);
    }

    private static boolean exists(String id)
    {
        HouseSurvey houseSurvey = get(id);

        return houseSurvey != null;
    }

    public static HouseSurvey get(String id)
    {
        ArrayList<HouseSurvey> results =  Stream.of(houseSurveys)
                                            .filter(v -> v.getReferenceNo().equals(id))
                                            .collect(Collectors.toCollection(ArrayList::new));

        return results.size() > 0 ? results.get(0) : null;
    }

    public static ArrayList<HouseSurvey> notSynced() {
        return Stream.of(houseSurveys)
                .filter(houseSurvey -> !houseSurvey.isSynced())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static HouseSurvey contains(String ref) {

        LinkedList<HouseSurvey> results = Stream.of(houseSurveys)
                                            .filter(h -> h.getReferenceNo().contains(ref))
                                            .collect(Collectors.toCollection(LinkedList::new));
        return results.size() > 0 ? results.getLast() : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            houseSurveys = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "house_survey.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                houseSurveys = (ArrayList<HouseSurvey>) obj.readObject();
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

            File file = new File(path, "house_survey.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(houseSurveys);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
