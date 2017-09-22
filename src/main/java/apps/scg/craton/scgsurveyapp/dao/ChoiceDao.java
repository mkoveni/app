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
import apps.scg.craton.scgsurveyapp.bll.Choice;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/08/30
 */

public class ChoiceDao
{

        private static ArrayList<Choice> choices;

        public static void add(Choice choice) {
            if(!exists(choice.getId()))
                choices.add(choice);
        }

        private static boolean exists(int id)
        {
            Choice choice = get(id);

            return choice != null;
        }

        public static Choice get(int id)
        {
            ArrayList<Choice> results = Stream.of(choices)
                    .filter(v -> v.getId() == id)
                    .collect(Collectors.toCollection(ArrayList::new));

            return results.size() > 0 ? results.get(0) : null;
        }

        public static ArrayList<Choice> getByQuestionId(int id)
        {
            return Stream.of(choices).filter(c -> c.getQuestionId() == id)
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        public static ArrayList<Choice> getChoices()
        {
            return choices;
        }

        public static void initialize() throws DataException {
        File path;
        File file;

        try {
            choices = new ArrayList<>();
            path = new File(Environment.getExternalStorageDirectory() + "/.scg_data/");

            if (!path.exists())
                path.mkdir();

            file = new File(path, "choice.dat");

            if (file.exists())
            {
                ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
                choices = (ArrayList<Choice>) obj.readObject();
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

                File file = new File(path, "choice.dat");

                if (file.exists()) {
                    ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(file));
                    obj.writeObject(choices);
                    obj.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
