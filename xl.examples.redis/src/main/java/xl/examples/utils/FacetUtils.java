package xl.examples.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacetUtils {
	public static String extractHashId(String viewName) {
		Pattern pattern = Pattern.compile("^facet_(.+?)_(\\d+?)$");
		Matcher matcher = pattern.matcher(viewName);

		String hashId = null;
		while (matcher.find()) {
			hashId = matcher.group(2);
		}
		
		return hashId;
	}
	public static void main(String[] args) {
		extractHashId("facet_m1_1234567890");
	}
}
