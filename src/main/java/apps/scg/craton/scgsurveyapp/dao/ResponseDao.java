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


import apps.scg.craton.scgsurveyapp.bll.Response;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/09/17
 */

public class ResponseDao {

    private static ArrayList<Response> responses;

    public static void add(Response response) {
        if(!exists(response))
            responses.add(response);
    }

    public static Response get(int id) {
        ArrayList<Response> results = Stream.of(responses)
                                        .filter(response -> response.getId() == id)
                                        .collect(Collectors.toCollection(ArrayList::new));
        return results.size() > 0 ? results.get(0) : null;
    }

    public static ArrayList<Response> getByReference(String reference) {
        return Stream.of(responses)
                .filter(response -> response.getRespondable_id().equals(reference))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Response> notSynced() {
        return Stream.of(responses)
                .filter(response -> !response.isSynced())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean exists(Response response)
    {
        return get(response.getId()) != null;
    }

    public static void initialize() throws DataException {
        File path;
        File file;

        try {
            responses = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "responses.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                responses = (ArrayList<Response>) obj.readObject();
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

            File file = new File(path, "responses.dat");

            if (file.exists()) {
                ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                obj.writeObject(responses);
                obj.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
