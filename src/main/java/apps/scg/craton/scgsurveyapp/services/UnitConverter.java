package apps.scg.craton.scgsurveyapp.services;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by CodeThunder on 2017/03/15.
 */

public class UnitConverter {

    public static float convertToDp(Context context,float value)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,value,context.getResources().getDisplayMetrics());
    }
}
