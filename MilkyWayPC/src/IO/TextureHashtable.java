package IO;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class TextureHashtable extends Hashtable{
	
	public Object get(Object s){

		if(containsKey(s))
			return super.get(s);
		
		String location = (String)s;
		BufferedImage img;
		try {
			img = ImageIO.read(new File((String)s));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		put(s,img);
        return img;
       
	}

}
