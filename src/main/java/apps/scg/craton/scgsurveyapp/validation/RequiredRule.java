package apps.scg.craton.scgsurveyapp.validation;

import android.content.Context;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.QuickRule;

import apps.scg.craton.scgsurveyapp.R;

/**
 * Created Rivalani Simon Hlengani
 * @since 2017/03/27.
 */

public class RequiredRule extends QuickRule<EditText> {


    public RequiredRule(int sequence)
    {
        super(sequence);
    }
    @Override
    public boolean isValid(EditText view) {

        return !view.getText().toString().trim().isEmpty();
    }

    @Override
    public String getMessage(Context context) {
        return context.getString(R.string.required_message);
    }

}
