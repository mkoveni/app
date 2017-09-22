package apps.scg.craton.scgsurveyapp.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CodeThunder on 2017/03/01.
 */

public class DateService {

    SimpleDateFormat formater;

    Date date;
    public DateService()
    {
        this("yyyy-MMMM-dd");
    }

    public DateService(String format)
    {
        setFormat(format);

        date = new Date();
    }

    public void setFormat(String format)
    {
        formater = new SimpleDateFormat(format);
    }

    public Date today()
    {
        return new Date();
    }

    public Date fromString(String date) throws ParseException {
        return formater.parse(date);
    }

    public String DateToString(Date date)
    {
        return formater.format(date);
    }
}
