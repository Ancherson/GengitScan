package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class Extensions {
    //result of extension as HashMap<"Name of extension",percentage>
    HashMap<String,Double> extensions = new HashMap<String,Double>();

    public HashMap<String, Double> getExtensions() {
        return extensions;
    }
    public HashMap<String,Double> parseExtensionsFromLog(String privateToken, int idProject){
        //APIresponse to get the extensions from a project
        APIresponse languages = new APIresponse();
        //sending the request to the API
        languages.createLogFileForExtensions(privateToken,idProject);
        //Using GSON to parse the json file
        try (Reader reader = new FileReader("../GitLog/resultsExtensions.json")) {
            extensions = new Gson().fromJson(reader, HashMap.class);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return extensions;
    }
    @Override
    public String toString() {
        return "Extensions{" +
                "extensions=" + extensions +
                '}';
    }
}
