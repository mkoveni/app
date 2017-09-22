package apps.scg.craton.scgsurveyapp.dao;

import android.content.Context;
import android.os.Environment;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author CodeThunder
 * @since 2017/08/29
 */

public class UserDao {

    private static ArrayList<User> users;


    public static ArrayList<User> getUsers()
    {
        return users;
    }

    public static User findByUsername(String username) {
       ArrayList<User> userArrayList = Stream.of(users).filter(u->u.getUsername().equals(username))
                        .collect(Collectors.toCollection(ArrayList::new));

        return userArrayList.size() > 0 ? userArrayList.get(0) : null;
    }

    public static void add(User user) {
        if(!exists(user.getId()))
            users.add(user);
    }

    private static boolean exists(int id)
    {
        return Stream.of(users).filter(u->u.getId() == id).count() > 0;
    }

    public static User get(int id)
    {
        return Stream.of(users)
                .filter(u -> u.getId() == id).single();
    }

    public static void initialize(Context context) throws DataException {

        try {
            users = new ArrayList<>();

            File path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");
            File file = new File(path, "users.dat");

            if(!path.exists()) path.mkdirs();
            if(!file.exists()) file.createNewFile();



            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
            users = (ArrayList<User>) obj.readObject();
            obj.close();
        }
        catch (EOFException e) {}
        catch (Exception ex)
        {
            throw new DataException(""+ ex);
        }

    }
    public static void terminate(Context context) {
        try {

            File file = new File(new File(Environment.getExternalStorageDirectory() + "/.scg_data/", "") + File.separator + "users.dat");

            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
            obj.writeObject(users);
            obj.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
