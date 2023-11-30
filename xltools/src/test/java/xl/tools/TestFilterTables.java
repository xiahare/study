package xl.tools;


import org.testng.annotations.Test;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class TestFilterTables {
    final private static String EMAILFILTER_PARAM_ID = "emailfilter";

    final private static String EMAILFILTER_TABLE_ID = "fml_spam";


    @Test
    public void testExist() {
        String storageId = "root";
        List<String> logtypes = new ArrayList<>();
        logtypes.add("webfilter");
        logtypes.add("traffic");
        logtypes.add("dns");
        logtypes.add("emailfilter");

        List<String> ret = filterEmailfilter(storageId, logtypes, true);

        System.out.println(ret);
    }

    @Test
    public void testMonExist() {
        String storageId = "root";
        List<String> logtypes = new ArrayList<>();
        logtypes.add("webfilter");
        logtypes.add("traffic");
        logtypes.add("dns");
        logtypes.add("emailfilter");

        List<String> ret = filterEmailfilter(storageId, logtypes, false);

        System.out.println(ret);
    }

    private List<String> filterEmailfilter(String storageId, List<String> logtypes, boolean exist){
        if(logtypes.contains(EMAILFILTER_PARAM_ID)){
            try {
                String emailfilterTableName = "__"+storageId+"_"+EMAILFILTER_TABLE_ID;
                List<String> resultTables = listTable(emailfilterTableName, exist);
                if(CollectionUtils.isEmpty(resultTables)){
                    // Remove emailfilter from logtypes param
                    logtypes.remove(EMAILFILTER_PARAM_ID);
                }
            }catch (Throwable th){
                System.err.println( th);
            }
        }

        return logtypes;
    }

    private List<String> listTable(String pattern, boolean exist){
        List<String> ret = new ArrayList<>();
        if(exist){
            ret.add("__root_fml_spam");
        }

        return ret;
    }
}
