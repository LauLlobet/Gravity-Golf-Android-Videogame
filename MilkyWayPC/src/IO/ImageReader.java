package IO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// PC image reader
public class ImageReader {

	public static void readImage(String _id, int[] dim, int[][] _alpha) {
		BufferedImage img;
		try {
			img = ImageIO.read(new File(_id));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		int dx = img.getWidth();
		int dy = img.getHeight();
		dim[0]=dx;
		dim[1]=dy;
		
		if(_alpha != null){
			
			int x,y;
			for(y=0;y<dy;y++){
				for(x=0;x<dx;x++){
					int color = img.getRGB(x,y);
					int alpha = (color >> 24) & 0xff;
					_alpha[x][y] = alpha;
				}
			}
		}
			
	}

}
