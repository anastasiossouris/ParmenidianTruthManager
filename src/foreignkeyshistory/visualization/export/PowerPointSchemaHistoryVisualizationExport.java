package foreignkeyshistory.visualization.export;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.visualization.TableLayout;

public class PowerPointSchemaHistoryVisualizationExport implements SchemaHistoryVisualizationExport{

	private int width;
	private int height;
	private Path previousImagesPath = null;
	
	public void setPreviousImagesPath(Path previousImagesPath)
	{
		this.previousImagesPath = previousImagesPath;
	}
	
	@Override
	public void exportSchemaHistory(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output) throws IOException {
		
		Path imagesDirectory = null;
		boolean imagesCreated = false;
		
		if (previousImagesPath == null)
		{
			imagesDirectory = makeTempImages(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, output);
			imagesCreated = true;
		}
		else
		{
			imagesDirectory = previousImagesPath;
		}
		
		XMLSlideShow ppt = createPresentation(imagesDirectory.toFile().listFiles());
		
		FileOutputStream out = new FileOutputStream(output.toString());
        ppt.write(out);
        out.close();

        if (imagesCreated == true)
        {
        	removeTempImages(imagesDirectory);
        }
	}

	private XMLSlideShow createPresentation(File[] args) throws FileNotFoundException, IOException
	{
		XMLSlideShow ppt = new XMLSlideShow();
		
		ppt = initializePresentation(ppt,args);
		
		for(int i=0;i<args.length;++i)
		{
			ppt = appendSlideShow(args[i], ppt);	
		}
		
		return ppt;
	}
	
	private XMLSlideShow initializePresentation(XMLSlideShow ppt, File[] files) throws IOException {
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];        
        
        for(int i=0;i<files.length;++i){
        	if(files[i].toString().contains("Diachronic Graph")){
                XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE);                
                XSLFSlide slide0 = ppt.createSlide(title);             
        		
                BufferedImage bimg = ImageIO.read(files[i]);
                width = bimg.getWidth();
                height = bimg.getHeight(); 
                
                XSLFTextShape title1 = slide0.getPlaceholder(0);
                title1.setAnchor(new Rectangle(0,0,width,100));
                title1.setText("Diachronic Graph");
                
                
                byte[] data = IOUtils.toByteArray(new FileInputStream(files[i]));
                int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
                XSLFPictureShape shape = slide0.createPicture(pictureIndex);
                shape.setAnchor(new Rectangle(0,100,width,height));        

                
//                ppt.setPageSize(new java.awt.Dimension(1100,height+100));   
                ppt.setPageSize(new java.awt.Dimension(width,height));
                
        		
        	}
        }
		
        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE_ONLY);
        XSLFSlide slide = ppt.createSlide(title);
        
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,width/4-100/2,height,100));
        title1.setText(setSlideTitle("Evolution Story"));
		
		return ppt;
	}
	
	private XMLSlideShow appendSlideShow(File imgPath, XMLSlideShow ppt) throws FileNotFoundException, IOException{

        if(!imgPath.toString().contains(".jpg")|| imgPath.toString().contains("Universal Graph"))
        	return ppt;
		
        
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];

        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE);
        XSLFSlide slide = ppt.createSlide(title);
       
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,0,width,100));
        title1.setText(setSlideTitle(imgPath.toString()));
        
        
        BufferedImage bimg = ImageIO.read(imgPath); 
        
        byte[] data = IOUtils.toByteArray(new FileInputStream(imgPath));
        int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
        XSLFPictureShape shape = slide.createPicture(pictureIndex);
        shape.setAnchor(new Rectangle(0,100,width,height));        
        
//        ppt.setPageSize(new java.awt.Dimension(1100,height+100));
        ppt.setPageSize(new java.awt.Dimension(width,height+100));
        
        return ppt;			
		
	}

	private String setSlideTitle(String imgPath) {

		String[] leftArray = imgPath.split(".jpg",2);
		String[] rightArray = leftArray[0].split("\\\\");
		return rightArray[rightArray.length-1];
	}


	private void removeTempImages(Path imagesDirectory) {		
		File[] files = imagesDirectory.toFile().listFiles();
		
		for (int i = 0; i < files.length; ++i)
		{
			files[i].delete();
		}
		
		imagesDirectory.toFile().delete();
	}

	private Path makeTempImages(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout,
			int edgeType, VisualizationViewer<String, String> vv, Path output) throws IOException
	{
		File tempImgDir = new File(output.toFile(), "temp_power_point_workspace");
		tempImgDir.mkdir();
		
		ImageSchemaHistoryVisualizationExport imgExport = new ImageSchemaHistoryVisualizationExport();
		
		imgExport.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, tempImgDir.toPath());
		
		return tempImgDir.toPath();
	}
}
