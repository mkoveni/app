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

import apps.scg.craton.scgsurveyapp.bll.ProjectUser;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class ProjectUserDao {

    private static ArrayList<ProjectUser> projectUsers;


    public static void add(ProjectUser projectUser) {
        if(!exists(projectUser.getProjectId(),projectUser.getUserId()))
            projectUsers.add(projectUser);
    }

    private static boolean exists(int project_id,int user_id)
    {
        ProjectUser village = get(project_id, user_id);

        return village != null;
    }

    public static ProjectUser get(int project_id, int user_id)
    {
        ArrayList<ProjectUser> collection =  Stream.of(projectUsers)
                                                .filter(v -> v.getUserId() == user_id && v.getProjectId() == project_id)
                                                .collect(Collectors.toCollection(ArrayList::new));
        return collection.size()>0? collection.get(0) : null;
    }

    public static ArrayList<ProjectUser> getByUserId(int user_id)
    {
        return Stream.of(projectUsers)
                .filter(p->p.getUserId() == user_id)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            projectUsers = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "project_user.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                projectUsers = (ArrayList<ProjectUser>) obj.readObject();
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

            File file = new File(path, "project_user.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(projectUsers);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
