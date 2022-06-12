package com.roadtest;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    String configure;
    String key;
    Utils(String configure){
        this.configure = configure;
    }
    public String getConfig(String key) {
        Properties p;
        p = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configure);
            p.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return p.getProperty(key);
    }    
}
