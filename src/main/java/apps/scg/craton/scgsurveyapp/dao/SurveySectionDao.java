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

import apps.scg.craton.scgsurveyapp.bll.SurveySection;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/07
 */

public class SurveySectionDao {

    private static ArrayList<SurveySection> surveySections;


    public static void add(SurveySection surveySection) {
        if(!exists(surveySection.getSection()))
            surveySections.add(surveySection);
    }

    private static boolean exists(String section)
    {
        SurveySection surveySection = get(section);

        return surveySection != null;
    }

    public static SurveySection get(String section)
    {
        ArrayList<SurveySection> ss = Stream.of(surveySections)
                .filter(s -> s.getSection().equals(section))
                .collect(Collectors.toCollection(ArrayList::new));

        return ss.size() > 0 ? ss.get(0) : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            surveySections = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "sections.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                surveySections = (ArrayList<SurveySection>) obj.readObject();
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

            File file = new File(path, "sections.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(surveySections);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
