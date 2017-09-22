package apps.scg.craton.scgsurveyapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import apps.scg.craton.scgsurveyapp.dao.UserDao;
import apps.scg.craton.scgsurveyapp.exceptions.DataException;
import apps.scg.craton.scgsurveyapp.services.AuthenticationService;

/**
 * @author Rivalani Simon Hlengani
 */

public class LoginActivity extends Activity {

    @NotEmpty
    private EditText txtEmail;

    @NotEmpty
    private EditText txtPassword;

    private AuthenticationService loginService;

    private Validator validator;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make screen full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set view
        setContentView(R.layout.activity_login);

        validator = new Validator(this);
        validator.setValidationListener(new ValidationHandler());

        try {
            UserDao.initialize(LoginActivity.this);
            loginService = new AuthenticationService(LoginActivity.this);
        } catch (DataException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //set button
        Button fwlogin_btn = (Button)findViewById(R.id.fwlogin_btn);
        Button fwexit_btn = (Button) findViewById(R.id.fwexit_btn);

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        //open new view
        fwlogin_btn.setOnClickListener(view -> validator.validate());
    }


    private class ValidationHandler implements Validator.ValidationListener
    {

        @Override
        public void onValidationSucceeded() {
                dialog = ProgressDialog.show(LoginActivity.this,"Authentication","Verifying credentials",true,false);

                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        //startActivity(intent);
                        dialog.dismiss();

                    }
                };

            Handler errorHandler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    //startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Oops! Could not login with those credentials,"+loginService.getAuthenticatedUser(), Toast.LENGTH_SHORT).show();

                }
            };
                    Thread thread  =  new Thread(() -> {
                    try {
                        if(loginService.attempt(txtEmail.getText().toString(),txtPassword.getText().toString())) {
                            handler.sendEmptyMessage(0);
                            String nextActivity;
                            nextActivity = "apps.scg.craton.scgsurveyapp.QUESTIONNARES";
                            Intent intent = new Intent(nextActivity);
                            intent.putExtra("authenticatedUser", loginService.getAuthenticatedUser());
                            startActivity(intent);
                        }
                        else
                        {

                            errorHandler.sendEmptyMessage(0);
                        }
                    } catch (DataException e) {
                        e.printStackTrace();
                    }

                });
                thread.start();

        }

        @Override
        public void onValidationFailed(List<ValidationError> list) {

            Stream.of(list).forEach(validationError -> {

                String message = validationError.getCollatedErrorMessage(getApplicationContext());

                View view = validationError.getView();

                if(view instanceof EditText)
                {
                    ((EditText)view).setError(message);
                }
            });
        }
    }

}
