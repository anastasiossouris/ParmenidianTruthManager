package foreignkeyshistory.visualization.export;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.util.PPTX2PNG;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.visualization.TableLayout;

public class VideoSchemaHistoryVisualizationExport implements SchemaHistoryVisualizationExport{
	private File selectedPresentation;
	private String filenameOfPresentation;
    private  String outputFilename ;
    private String sourceFolder=new String();
    private  final double FRAME_RATE = 24;
    private Path previousPPTPath = null;
    
    public void setPreviousPPTPath(Path path)
    {
    	previousPPTPath = path;
    }
    
	@Override
	public void exportSchemaHistory(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output) throws IOException {
		Path pptDir = null;
		boolean pptCreated = false;
		
		if (previousPPTPath == null)
		{
			pptDir = makeTempPowerPoint(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, output);
			pptCreated = true;
		}
		else
		{
			pptDir = previousPPTPath;
		}
		
		selectedPresentation = pptDir.toFile().listFiles()[0];
		filenameOfPresentation = selectedPresentation.getAbsolutePath();		
		setOutputFilename(output);
		setSourceFolder(output);

		exportToVideo();
		
		if (pptCreated == true)
		{
			removeTempPowerPoint(pptDir);
		}
	}

	public void exportToVideo() throws IOException{
		extractPngFromPptx();
		createVideo();
		deleteGeneratedPng();		
	}

	private void extractPngFromPptx(){

		try {
			PPTX2PNG.main(new String[]{filenameOfPresentation});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void createVideo() throws IOException{
		IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
		Dimension screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
		
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,screenBounds.width/2, screenBounds.height/2);
       
        File directory = new File(sourceFolder);
        ArrayList<File> filenames = new ArrayList<File> ();
        
        File[] list= directory.listFiles(new ImageFileFilter());
		for(int i=0;i<list.length;i++){
			filenames.add(list[i]);
		}
		
		Collections.sort(filenames,new FilenameSorter());
		
		long startTime = System.nanoTime();
	     
		try{
	        for (int index = 0; index < filenames.size(); index++) {
		            BufferedImage screen = ImageIO.read(filenames.get(index));
			
		            // convert to the right image type
		            BufferedImage bgrScreen = convertToType(screen,BufferedImage.TYPE_3BYTE_BGR);
		
	            
		            //int i=0;
		            //while(i<FRAME_RATE){
		            	writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime, 
		                   TimeUnit.NANOSECONDS);
		            	//++i;
		           //}
		
		            // sleep for frame rate milliseconds
		            try {
		                Thread.sleep((long) (1000 / FRAME_RATE));
		            } 
		            catch (InterruptedException e) {
		            }
	        }
		}
		finally{
			writer.flush();
			writer.close();
		}	
	}
	
	private void deleteGeneratedPng(){
        File directory = new File(sourceFolder);
        FileFilter filter =new ImageFileFilter();
        File[] filesToBeDeleted =directory.listFiles(filter); 
		
        for(int i=0;i<filesToBeDeleted.length;++i)
        	filesToBeDeleted[i].delete();		
	}

	private BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;
		
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		}
		else {
			image = new BufferedImage(sourceImage.getWidth(), 
	                 sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}

	private void removeTempPowerPoint(Path imagesDirectory) {		
		File[] files = imagesDirectory.toFile().listFiles();
		
		for (int i = 0; i < files.length; ++i)
		{
			files[i].delete();
		}
		
		imagesDirectory.toFile().delete();
	}

	private Path makeTempPowerPoint(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout,
			int edgeType, VisualizationViewer<String, String> vv, Path output) throws IOException
	{
		File tempImgDir = new File(output.toFile(), "temp_video_export_workspace");
		tempImgDir.mkdir();
		
		PowerPointSchemaHistoryVisualizationExport imgExport = new PowerPointSchemaHistoryVisualizationExport();
		
		imgExport.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, tempImgDir.toPath());
		
		return tempImgDir.toPath();
	}
	
	private void setOutputFilename(Path output){
		//String[] leftArray = filenameOfPresentation.split("\\.",2);
		outputFilename= output.toString() +"video.mp4";	
	}
	
	private void setSourceFolder(Path output) {
		
		/*String[] leftArray = filenameOfPresentation.split("\\.",2);	
		String[] leftovers = leftArray[0].split("\\\\");

		for(int i=0;i<leftovers.length-1;++i){
			sourceFolder+=leftovers[i]+"\\";
		}*/
		
		sourceFolder = output.toString();
	}
	
	class FilenameSorter implements java.util.Comparator<File> {
		
		public int compare(File c1,File c2) {
					if(getFname(c1) > getFname(c2))
							return 1;
					else if(getFname(c1) < getFname(c2))
							return -1;
					else
							return 0;
		}
		
		public int getFname(File file){
			
			String[] leftArray = file.getAbsolutePath().split("\\.",2);	
			String[] leftovers = leftArray[0].split("\\-",2);

			return Integer.parseInt(leftovers[1]);
			
		}
	}

	public class ImageFileFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".png") || pathname.getName().endsWith(".jpg"))
				return true;
			return false;
			
		}
		
	}
}
