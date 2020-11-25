package up.visulog.cli;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCLILauncher {
    /*
    TODO: one can also add integration tests here:
    - run the whole program with some valid options and look whether the output has a valid format
    - run the whole program with bad command and see whether something that looks like help is printed
     */
    @Test
    //To test
    public void testArgumentParser() {
        var config1 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{".", "--addPlugin=countCommits"});
        assertTrue(config1.isPresent());
        var config2 = CLILauncher.makeConfigFromCommandLineArgs(new String[] {
            "--nonExistingOption"
        });
        assertFalse(config2.isPresent());

        var config3 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{"--addPlugin=countMergeCommits"});
        assertTrue(config3.isPresent());

        var config4 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{".", "--loadConfigFile=./test.txt"});
        assertTrue(config4.isPresent());

        var config5 = CLILauncher.makeConfigFromCommandLineArgs(new String[]{"--addPlugin=countCommits", "--justSaveConfigFile=./test.txt"});
        assertTrue(config5.isPresent());
    }
}
