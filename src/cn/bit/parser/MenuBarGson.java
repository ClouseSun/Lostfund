package cn.bit.parser;

import cn.bit.ui.component.MenuBar;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by KlousesSun on 2017/3/19.
 */
public class MenuBarGson {
    Gson gson;
    String jsonString;

    public MenuBarGson(String string) {
        InputStream inputStream = getClass().getResourceAsStream(string);
        gson = new Gson();
        try {
            jsonString = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MenuBar parse() {
        MenuBar menuBar = gson.fromJson(jsonString, MenuBar.class);
        return menuBar;
    }

}
