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
    public void createLogFileForIssues(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssues.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssues.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/issues?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
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
    public void createLogFileForIssues(int idProject) {
        createLogFileForIssues("1m1pdKszBNnTtCHS9KtS", idProject);
    }
    public void createLogFileForOneIssue(String privateToken,int idProject ,int id){
        Process process;
        try {
            //creating a json file
            File jsonFile = new File("../GitLog/resultsIssue"+id+".json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsIssue"+id+".json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/issues/"+id+"/discussions?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
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
    public void createLogFileForOneIssue(int idProject ,int id){
        createLogFileForOneIssue("1m1pdKszBNnTtCHS9KtS",idProject,id);
    }
    public void createLogFileForMembers(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsMembers.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsMembers.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/users?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
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
    public void createLogFileForExtensions(String privateToken,int idProject){
        Process process;
        try {

            //creating a json file
            File jsonFile = new File("../GitLog/resultsExtensions.json");
            FileWriter writeJsonFile = new FileWriter("../GitLog/resultsExtensions.json");
            //Command to terminal
            String[] command = {"curl",
                    "--header",
                    "PRIVATE-TOKEN:"+privateToken,
                    "https://gaufre.informatique.univ-paris-diderot.fr/api/v4/projects/"+idProject+"/languages?per_page=1000"};
            //Sending to GitLab API a request
            process = Runtime.getRuntime().exec(command);
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
