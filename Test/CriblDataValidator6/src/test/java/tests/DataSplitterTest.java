package tests;

import helpers.FileHelpers;
import helpers.RetryAnalyzer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSplitterTest {

    private File target1_file;
    private File target2_file;
    private static final String input_file = "C:\\criblAssesment\\InputFile\\large_1M_events.log";
    private int target1_count;
    private int target2_count;

    private static final String FILE_DIRECTORY = "C:\\criblAssesment\\";
    private static final String FILE_DIRECTORY_T1 = "C:\\criblAssesment\\vol_target_1";
    private static final String FILE_DIRECTORY_T2 = "C:\\criblAssesment\\vol_target_2";
    private static final int FILE_ENTRIES = 1000000;
    FileHelpers file = new FileHelpers();
    private static final Logger logger = LogManager.getLogger(DataSplitterTest.class);


    @BeforeSuite(alwaysRun = true)
    public void isResultFileExist() throws IOException
    {
        logger.info("Before Test Suite");
        logger.info("Verifing Result files");

        target1_file = new File(FILE_DIRECTORY_T1+"\\events.log");
        target2_file = new File(FILE_DIRECTORY_T2+"\\events.log");

        target1_count = file.getFileCount(target1_file);
        target2_count = file.getFileCount(target2_file);

        Assert.assertTrue(target1_file.getAbsolutePath().endsWith("\\events.log"),"Actual"+ target1_file.getAbsolutePath());
        Assert.assertTrue(target2_file.getAbsolutePath().endsWith("\\events.log"),"Actual"+ target2_file.getAbsolutePath());
    }

    @Test(
            enabled=true,
            singleThreaded = true,
            description = "Verify log file count for both Target apps",
            retryAnalyzer = RetryAnalyzer.class,
            groups = {"unittest"}
    )
    void testTotalFileCount() throws Exception {
        logger.info("Running test for verifying total file count");

        int total_count = target1_count + target2_count;
        Assert.assertEquals(total_count, FILE_ENTRIES,"Mistmatch in Total logfile entries. Potential Data loss.");
    }


    @Test(
            enabled=true,
            singleThreaded = true,
            description = "Verify Random Splitting Ranges",
            retryAnalyzer = RetryAnalyzer.class,
            groups = {"unittest"}
    )
    public void verifyFileSplittingRanges() throws IOException {
        logger.info("\nVerifying file splitting ranges for both targets");

        // Check if the count of records in target1 and target2 files is within expected range
        boolean isFileSplittingWithinRange = false;
        if (file.isTargetCountWithinRange(target1_count) && file.isTargetCountWithinRange(target2_count)) {
            isFileSplittingWithinRange = true;
        }
        // Assert that the count of records in both files is within expected range
        Assert.assertTrue(isFileSplittingWithinRange,"files are not splitted randomly");
    }


    @Test(
            enabled=true ,
            singleThreaded=true,
            description = "Verify File Data Input/Output Match ",
            retryAnalyzer = RetryAnalyzer.class)
    public void verifyFileDataInputOutput() throws IOException
    {
        logger.info("\nVerifying file data matches between both Targets and Agent");
        String output_file_path = FILE_DIRECTORY+"\\output_events.log";
        String comparison_file_path = FILE_DIRECTORY+"\\output_comparison_events.log";
        file.mergeFiles(target1_file.getAbsolutePath(),target2_file.getAbsolutePath(),output_file_path);

        // Compare the merged file with the original file
        boolean isFilesEqual = file.compareFiles(input_file, output_file_path, comparison_file_path);
        System.out.println("Files are equal: " + isFilesEqual);
        Assert.assertTrue(isFilesEqual,"Error while splitting. Files are not splitted properly. Potential Data loss.");
    }



}
