package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Commit {
    // FIXME: (some of) these fields could have more specialized types than String
    public final String id;
    public final LocalDateTime date;
    public final String author;
    public final String description;
    public final String mergedFrom;
    public int linesAdded = 0;
    public int linesDeleted = 0;

    public Commit(String id, String author, LocalDateTime date, String description, String mergedFrom) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
    }
    
    //this function execute the command args from directory whose path is "path"
    public static BufferedReader command(Path path, String... args) {
    	ProcessBuilder builder = new ProcessBuilder();
    	builder.directory(path.toFile());
    	builder.command(args);
    	
    	Process process;
    	try {
    		process = builder.start();
    	}catch(IOException e) {
    		String arg = "";
    		for(int i = 0; i < args.length; i++) {
    			arg += args[i] + " ";
    		}
    		throw new RuntimeException("Error running \"" + arg + "\"", e);
    	}
    	InputStream is = process.getInputStream();
    	return new BufferedReader(new InputStreamReader(is));
    }
    
    //This function collects the number of line added and deleted for each commits
    public static List<Commit> getNumberLines(Path path, List<Commit> commits) {
    	for(Commit commit : commits) {
    		int[] lines = parseLine(command(path, "git", "show", "--format=oneline",commit.id,"--numstat"));
    		commit.linesAdded = lines[0];
    		commit.linesDeleted = lines[1];
    	}
    	
    	//If you want to test the function, remove the '/* */'
    	//It shows the number of line added and deleted for each commits from the most recent to the oldest
    	//You can compare the results with gitlab
    	/*for(Commit commit : commits) {
    		System.out.println(commit.linesAdded + " added , " + commit.linesDeleted + " deleted");
    	}*/
    	
    	return commits;
    	
    }
    
    //This function parses the output of the command 'git show', to collect the number of line added and deleted
    public static int[] parseLine(BufferedReader reader) {
    	try {
    		String line;
    		reader.readLine();
    		int added = 0;
    		int deleted = 0;
    		while((line = reader.readLine()) != null) {
    			if(!line.equals("")) {
	    			Scanner sc = new Scanner(line);
					String s = sc.next();
					if(s.equals("-")) added += 0;
					else added += Integer.parseInt(s);
					s = sc.next();
					if(s.equals("-")) deleted += 0;
					else deleted += Integer.parseInt(s);  		
    			}
    		}
    		int[] res = {added, deleted};
    		return res;
    	}catch(IOException e) {
    		throw new RuntimeException("Error parseLine", e);
    	}
    }
    
    public static List<Commit> parseLogFromCommand(Path gitPath) {
    	BufferedReader reader = command(gitPath, "git", "log");
        return getNumberLines(gitPath,parseLog(reader));
    }

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * Returns an empty optional if there is nothing to parse anymore.
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();
            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default:
                        System.out.println("The field '"+fieldName+"' was ignored while parsing the commit");
                }
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    @Override
    public String toString() {
        return "Commit{" +
                //these 3 fields are optional
                (id != null ? ("id='" + id + '\'') : "") +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") + //TODO: find out if this is the only optional field
                (date != null ? (", date='" + date + '\'') : "") +
                (author != null ? (", author='" + author + '\'') : "") +
                //field of description is not optional
                ", description='" + description + '\'' +
                '}';
    }
    
    public String getLinesToString() {
    	int[]t = {this.linesAdded, this.linesDeleted};
    	return t[0] + " , " + t[1];
    }
}
