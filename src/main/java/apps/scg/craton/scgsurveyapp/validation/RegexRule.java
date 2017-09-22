package apps.scg.craton.scgsurveyapp.validation;

import android.content.Context;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.QuickRule;

import java.util.regex.Pattern;

/**
 * @author Rivalani Simon Hlengani
 * @since 2017/03/27.
 */

public class RegexRule extends QuickRule<EditText> {


    private Pattern pattern;
    private String message;

    public RegexRule(String regex)
    {
        this(-1,regex);
    }

    public RegexRule(int sequence,String regex)
    {
        this(sequence,regex,"Could not match pattern");
    }

    public RegexRule(String regex,String message)
    {
        this(-1,regex,message);
    }

    public RegexRule(int sequence,String regex,String message)
    {
        super(sequence);
        pattern = Pattern.compile(regex);
        this.message = message;
    }

    @Override
    public boolean isValid(EditText view) {

        return pattern.matcher(view.getText().toString()).matches();

    }

    @Override
    public String getMessage(Context context) {
        return message;
    }
}
