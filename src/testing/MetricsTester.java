package testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Testing methodology for metrics refactorings:
 * 
 * Name the initial version of ParmenidianTruth which we re-engineer as "old-version". The result of every refactoring is called the "new-version".
 * 
 * Run the old-version on some datasets and generate the metric reports.
 * Run the new-version on the same datasets and generate the metrics reports.
 * Test if the corresponding files are the same.
 * 
 * Details:
 * 
 * 		1) Under a directory "old" there is a sub-directory for each dataset that we have run. Under this sub-directory there exists the metrics report files.
 * 		2) The same is true for a "new" directory.
 * To test, we compare the metric report files under the same sub-directory name from old and new.
  */
public class MetricsTester {
	
	public MetricsTester(Path oldDirectory, Path newDirectory) throws IOException
	{
		if (!assertEqualMetricReportsMultipleDatasets(oldDirectory, newDirectory))
		{
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Tests the scenario described above.
	 * @throws IOException 
	 */
	private boolean assertEqualMetricReportsMultipleDatasets(Path oldDirectory, Path newDirectory) throws IOException
	{
		File oldDirectoryFile = oldDirectory.toFile();
		File newDirectoryFile = newDirectory.toFile();
		String[] oldDatasets = oldDirectoryFile.list();
		String[] newDatasets = newDirectoryFile.list();

		int numDatasets = oldDatasets.length;
		
		// Sort the arrays so that the same sub-directories appear in the same order
		Arrays.sort(oldDatasets);
		Arrays.sort(newDatasets);
		
		for (int i = 0; i < numDatasets; ++i)
		{
			File oldDataset = new File(oldDirectoryFile, oldDatasets[i]);
			File newDataset = new File(newDirectoryFile, newDatasets[i]);
			
			if (!assertEqualMetricReportsUnderDirectory(oldDataset.toPath(), newDataset.toPath())){ return false; }
		}
		
		return true;
	}
	
	/**
	 * Tests if the metric reports under the oldDirectory and the newDirectory are the same.
	 * 
	 * We assume that these directories contain only the metric reports and nothing else.
	 * @throws IOException 
	 */
	private boolean assertEqualMetricReportsUnderDirectory(Path oldDirectory, Path newDirectory) throws IOException
	{
		File oldDirectoryFile = oldDirectory.toFile();
		File newDirectoryFile = newDirectory.toFile();
		String[] oldMetricReports = oldDirectoryFile.list();
		String[] newMetricReports = newDirectoryFile.list();
		
		int numMetricReports = oldMetricReports.length;
		
		// Sort the arrays so that the same metric reports appear in the same order
		Arrays.sort(oldMetricReports);
		Arrays.sort(newMetricReports);
		
		for (int i = 0; i < numMetricReports; ++i)
		{
			File oldMetricReport = new File(oldDirectoryFile,oldMetricReports[i]);
			File newMetricReport = new File(newDirectoryFile, newMetricReports[i]);
			
			System.out.println("Testing: " + oldMetricReport.toString() + " with " + newMetricReport.toString());
			if (!assertEqualMetricReports(oldMetricReport.toPath(), newMetricReport.toPath())){ 
				System.err.println("Metric file problem:" + oldMetricReport.toString());
				return false; 
			}
		}
		
		return true;
	}
	
	/**
	 * Tests whether the contents of oldReport and newReport are the same. The test is made by comparing the two files line-by-line as strings.
	 */
	private boolean assertEqualMetricReports(Path oldReport, Path newReport) throws IOException
	{
		List<String> oldReportContents = Files.readAllLines(oldReport);
		List<String> newReportContents = Files.readAllLines(newReport);
		
		if (oldReportContents.size() != newReportContents.size()){ return false; }

		/**
		 * Create a map (for old and new report) that is built as follows:
		 * For each line L split it into the string up to the first "," exclusive. That string is the key and the whole line is the value.
		 * Then compare the lines that have the same keys. This is done because for the reports that have multiple lines the tables may not appear in the same order
		 */
		Map<String,String> oldReportContentsMap = createReportContentsMap(oldReportContents);
		Map<String,String> newReportContentsMap = createReportContentsMap(newReportContents);
		
		for (String key : oldReportContentsMap.keySet())
		{
			String oldValue = oldReportContentsMap.get(key);
			String newValue = newReportContentsMap.get(key);
			
			if (newValue == null || !oldValue.equals(newValue)){ 
				System.err.println("Different lines encountered:");
				System.err.println("Old: " + oldValue);
				System.err.println("New: " + newValue);
				return false; 
			}
		}
		
		return true;
	}

	private Map<String, String> createReportContentsMap(List<String> reportContents) {
		Map<String,String> reportContentsMap = new HashMap<>();
		
		for (String str : reportContents)
		{
			String key = str.substring(0,str.indexOf(","));
			reportContentsMap.put(key, str);
		}

		return reportContentsMap;
	}
}
