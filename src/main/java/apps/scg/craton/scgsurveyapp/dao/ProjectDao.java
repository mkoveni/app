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

import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class ProjectDao {

    private static ArrayList<Project> projects;

    public static void add(Project project) {
        if(!exists(project.getId()))
            projects.add(project);
    }

    private static boolean exists(int id)
    {
        Project project = get(id);

        return project != null;
    }

    public static Project get(int id)
    {
        ArrayList<Project> collection = Stream.of(projects)
                .filter(p -> p.getId() == id)
                .collect(Collectors.toCollection(ArrayList::new));
        return collection.size() > 0 ? collection.get(0) : null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            projects = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "project.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                projects = (ArrayList<Project>) obj.readObject();
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

            File file = new File(path, "project.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(projects);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
