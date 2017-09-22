package apps.scg.craton.scgsurveyapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import java.util.ArrayList;

import apps.scg.craton.scgsurveyapp.adapters.ProjectGridAdapter;
import apps.scg.craton.scgsurveyapp.bll.Project;
import apps.scg.craton.scgsurveyapp.bll.ProjectUser;
import apps.scg.craton.scgsurveyapp.bll.Village;
import apps.scg.craton.scgsurveyapp.bll.User;
import apps.scg.craton.scgsurveyapp.dao.ProjectDao;
import apps.scg.craton.scgsurveyapp.dao.ProjectUserDao;
import apps.scg.craton.scgsurveyapp.dao.VillageDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;


/**
 * @author Rivalani Simon Hlengani
 */

public class QuestionnaireActivity extends Activity {

    private ArrayList<Project> projects;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make screen full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        projects = new ArrayList<>();

        //set view
        setContentView(R.layout.questionnaire_grid);

        final User user = (User) getIntent().getSerializableExtra("authenticatedUser");

        dialog = ProgressDialog.show(QuestionnaireActivity.this,"Loading","Loading app data, please wait...",true,false);
        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {

                dialog.dismiss();

                ProjectGridAdapter adapter = new ProjectGridAdapter(QuestionnaireActivity.this, projects);

                GridView view = (GridView) findViewById(R.id.grid);

                if (projects.size() == 1)
                {
                    view.setNumColumns(1);
                }
                else if (projects.size() == 2)
                {
                    view.setNumColumns(2);
                }
                else if (projects.size() >= 3)
                {
                    view.setNumColumns(3);
                }

                view.setAdapter(adapter);

                view.setOnItemClickListener((parent, view1, position, id) -> {

                    String activity = "apps.scg.craton.scgsurveyapp.LandActivity";

                    if(projects.get(position).getType().equals("Asset"))
                    {
                        activity = "apps.scg.craton.scgsurveyapp.AssetTypeActivity";
                    }

                    Village village = projects.get(position).getVillage();
                    Intent nextActivity;
                    nextActivity = new Intent(activity);
                    nextActivity.putExtra("user",user);
                    nextActivity.putExtra("village", village);
                    nextActivity.putExtra("project", projects.get(position));
                    startActivity(nextActivity);

                });
            }
        };

        Thread thread = new Thread(() -> {
            try {
                ProjectUserDao.initialize();
                ProjectDao.initialize();
                VillageDao.initialize();

                for(ProjectUser projectUser: ProjectUserDao.getByUserId(user.getId()))
                {
                    Project project = ProjectDao.get(projectUser.getProjectId());
                    project.setVillage(VillageDao.get(project.getVillageId()));
                    projects.add(project);
                }

                handler.sendEmptyMessage(0);

            }catch (DataException e){e.printStackTrace();}
        });

        thread.start();

    }
}
