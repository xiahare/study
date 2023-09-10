package xl.tools.sql;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Calendar Utilities
 */
public class CalendarUtil
{
    public static long SECOND_PER_DAY = 24 * 60 * 60;
    
    public static long MILLI_SECOND_PER_DAY = 24 * 60 * 60 * 1000;

	public static final String DEFAULT_TIME_FORMAT   = "yyyy-MM-dd HH:mm:ss";

    /**
     * Get a date time string from a date object
     *
     * @param date a Date object
     *
     * @return a date string[yyyy-MM-dd HH:mm:ss]
     *
     * 
     */
    public static String getDateTimeString(Date date)
    {    	
        return getDateTimeString(date, DEFAULT_TIME_FORMAT);
    }

	public static String getCurrentDateTimeString()
	{
		return getDateTimeString(new Timestamp(System.currentTimeMillis()), DEFAULT_TIME_FORMAT);
	}
    
    public static String getDateTimeString(Date date, String formatPattern)
    {
    	if(date==null) {
    		return null;
    	}
    	SimpleDateFormat sfDateTime = new SimpleDateFormat(formatPattern);
    	sfDateTime.setTimeZone(TimeZone.getTimeZone("UTC"));

        return sfDateTime.format(date);
    }

	public static String convertDateStrFormat(String dateStr, String originalFormat, String formatPattern) throws Exception
	{
		if(dateStr==null) {
			return null;
		}
		SimpleDateFormat sfDateTime = new SimpleDateFormat(originalFormat);
		sfDateTime.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = null;
		try {
			date = sfDateTime.parse(dateStr);
		} catch (ParseException e) {
			throw new Exception("Unable to parse the dateString from " + originalFormat + "to " + formatPattern);
		}

		return getDateTimeString(date, formatPattern);
	}
    
    public static Timestamp handleTimestampIssue(Timestamp rawTimestampValue) {
		
		Timestamp ts = null;
		
		if (null != rawTimestampValue) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			
			String rawTimeStr = simpleDateFormat.format(rawTimestampValue);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			try {
				ts = new Timestamp(simpleDateFormat.parse(rawTimeStr).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return ts;
	}
    
    public static Timestamp parseDateTimeString(String dateString) throws ParseException
    {
		return parseDateTimeString(dateString, DEFAULT_TIME_FORMAT);
    }

	public static Timestamp parseDateTimeString(String dateString, String formatPattern) throws ParseException
	{
		if( dateString == null ) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		return new Timestamp(sdf.parse(dateString).getTime());
	}

	public static Timestamp parseEpochDateTimeString(String dateString)
	{
		if( dateString == null ) {
			return null;
		}
		return new Timestamp(Long.parseLong(dateString));
	}

	public static Timestamp parseEpochDateTime(Long time) {
		if (time == null) {
			return null;
		}
		return new Timestamp(time);
	}
}
