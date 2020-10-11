package up.visulog.gitrawdata;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class APIresponse {
    public APIresponse(){
    }
    public void createLogFileForCommits(){
        Process process;
        try {
            //getting date and time of the API request
            String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
            //creating a json file
            File jsonFile = new File("../GitLog/resultsCommit"+out+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsCommit"+out+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/repository/commits\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createLogFileForOneCommit(String id){
        Process process;
        try {
            //creating a json file
            File jsonFile = new File("../GitLog/resultsCommit"+id+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsCommit"+id+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/repository/commits/"+id+"\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createLogFileForIssues(){
        Process process;
        try {
            //getting date and time of the API request
            String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssues"+out+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssues"+out+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/issues\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createLogFileForOneIssue(int id){
        Process process;
        try {
            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssue"+id+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssue"+id+".json");
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec("curl --header \"PRIVATE-TOKEN: 1m1pdKszBNnTtCHS9KtS\" \"https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/1618/issues/"+id+"/discussions\"");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            //Writing in JSON file
            while ((line = reader.readLine()) != null) {
                writeJsonFile.write(line);
            }
            //closing the file
            writeJsonFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
