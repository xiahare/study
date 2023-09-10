package xl.tools.sql;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertRangePartitionToEpoch {
    private final static String prefix_path = "src/main/resources/";


    public static void main(String[] args) throws IOException {
        String filename = "part_timestamp.log";
        String format_add_partition = "ALTER TABLE temp_xl_root_fgt_traffic ADD RANGE PARTITION %s;";

        File file = new File(prefix_path+filename);
        List<String> lines = FileUtils.readLines(file, "UTF-8");

        for (String line : lines) {
            String converted = convertPerLine(line);
            String sql_add_partition = String.format(format_add_partition, converted);
            System.out.println(sql_add_partition);
        }


    }

    private static String convertPerLine(String input){
        // Define the regular expression pattern to match timestamps
        String regex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{6}Z";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        StringBuilder outputBuilder = new StringBuilder();
        int lastEnd = 0;

        // Iterate through the input and replace timestamps with epoch time
        while (matcher.find()) {
            String timestamp = matcher.group();
            long epochSeconds = convertTimestampToEpoch(timestamp);
            outputBuilder.append(input, lastEnd, matcher.start());
            outputBuilder.append(epochSeconds);
            lastEnd = matcher.end();
        }

        // Append the remaining text after the last timestamp
        outputBuilder.append(input, lastEnd, input.length());

        //System.out.println(outputBuilder.toString());
        return  outputBuilder.toString();
    }

    private static long convertTimestampToEpoch(String timestamp) {
        // Parse the timestamp string into Instant
        Instant instant = Instant.parse(timestamp);

        // Get the epoch time in seconds
        return instant.getEpochSecond();
    }
}
