package apps.scg.craton.scgsurveyapp;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.view.WindowManager;

import apps.scg.craton.scgsurveyapp.dao.UserDao;
import apps.scg.craton.scgsurveyapp.dao.api.ChoiceApi;
import apps.scg.craton.scgsurveyapp.dao.api.ChoiceDependentApi;
import apps.scg.craton.scgsurveyapp.dao.api.ProjectApi;
import apps.scg.craton.scgsurveyapp.dao.api.QuestionnaireQuestionApi;
import apps.scg.craton.scgsurveyapp.dao.api.ProjectUserApi;
import apps.scg.craton.scgsurveyapp.dao.api.QuestionApi;
import apps.scg.craton.scgsurveyapp.dao.api.QuestionTypeApi;
import apps.scg.craton.scgsurveyapp.dao.api.SurveySectionApi;
import apps.scg.craton.scgsurveyapp.dao.api.UserApi;
import apps.scg.craton.scgsurveyapp.dao.api.VillageApi;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class FlashScreen extends Activity {

    ProgressDialog dialog;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make screen full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set view
        setContentView(R.layout.activity_flash_screen);

        verifyPermissions(FlashScreen.this);
        dialog = ProgressDialog.show(FlashScreen.this,"App loader","Loading app data...",true,false);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                dialog.dismiss();
                Intent intent = new Intent("apps.scg.craton.scgsurveyapp.FIELDWORKER");
                startActivity(intent);
            }
        };
        Thread thread = new Thread(() -> {
            FlashScreen.sync(getApplicationContext());
            handler.sendEmptyMessage(0);
        });

        thread.start();

    }


    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        activity,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
        }
    }

    private static void sync(Context context)
    {
        try
        {
            UserDao.initialize(context);

            if(UserDao.getUsers().size() <= 0)
            {
                (new UserApi(context)).getUsers();
                (new VillageApi()).getVillages();
                (new ProjectApi()).getProjects();
                (new ProjectUserApi()).getProjectUsers();
                (new QuestionApi()).getQuestions();
                (new QuestionTypeApi()).getQuestionTypes();
                (new ChoiceApi()).getChoices();
                (new ChoiceDependentApi()).getDepedents();
                (new SurveySectionApi()).getSections();
                (new QuestionnaireQuestionApi()).getQuestionnaireQuestions();
            }
        }
        catch (DataException e)
        {
            e.printStackTrace();
        }
    }
}
