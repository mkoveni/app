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
import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/29
 */

public class VillageDao {

    private static ArrayList<Village> villages;


    public static void add(Village village) {
        if(!exists(village.getId()))
            villages.add(village);
    }

    private static boolean exists(int id)
    {
        Village village = get(id);

        return village != null;
    }

    public static Village get(int id)
    {
        ArrayList<Village> vs = Stream.of(villages)
                .filter(v -> v.getId() == id)
                .collect(Collectors.toCollection(ArrayList::new));

        return vs.size() > 0 ? villages.get(0) : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            villages = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "village.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                villages = (ArrayList<Village>) obj.readObject();
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

            File file = new File(path, "village.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(villages);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
