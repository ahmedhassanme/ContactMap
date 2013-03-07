import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

import org.tc33.jheatchart.HeatChart;


public class Main 
{
	public static void main(String[] args) 
	{
		//String readFile = "/Users/Ahmed/Desktop/1DXT.pdb";
		//String writeFile = "/Users/Ahmed/Desktop/map.jpg";
		
		String readFile = args[0];
		String writeFile = args[1];
		
		try
		{	
			ArrayList<ArrayList<String>> elements = new ArrayList<ArrayList<String>>();	
			BufferedReader reader = new BufferedReader(new FileReader(readFile));	
			HashSet<String> res = new HashSet<String>();
			
			String temp;
			while ((temp = reader.readLine()) != null)
			{
				if(temp.startsWith("ATOM"))
				{
					String[] line = temp.split(" ");
					ArrayList<String> rec = new ArrayList<String>();
					for(int x = 0; x<line.length; x=x+1)
					{
						if(line[x].length()>0)
						{
							rec.add(line[x]);
						}
					}
					if(rec.get(2).equals("CA") && rec.get(4).equals("A"))
					{
						String residue = rec.get(3) + " " + rec.get(4);
						elements.add(rec);
						res.add(residue);
					}
				}
			}
			
			reader.close();
			double[][] matrix = new double[elements.size()][elements.size()];
			int tracker = 0;

			for(int x = 0; x<matrix.length; x=x+1)
			{
				for(int y = x; y<matrix[x].length; y=y+1)
				{
					Double checker = new Double(matrix[x][y]);
					if(checker!=null && x!=y)
					{
					
						double x1 = Float.parseFloat(elements.get(x).get(6));
						double y1 = Float.parseFloat(elements.get(x).get(7));
						double z1 = Float.parseFloat(elements.get(x).get(8));
						
						double x2 = Float.parseFloat(elements.get(y).get(6));
						double y2 = Float.parseFloat(elements.get(y).get(7));
						double z2 = Float.parseFloat(elements.get(y).get(8));
						
						double x0 = Math.pow(x1-x2, 2);
						double y0 = Math.pow(y1-y2, 2);
						double z0 = Math.pow(z1-z2, 2);
						
						double number = Math.sqrt(x0 + y0 + z0);
						
						if(number < 6.0)
						{
							matrix[x][y] = 1;
							matrix[y][x] = 1;
							tracker++;
						}
						else
						{
							matrix[x][y] = 0;
							matrix[y][x] = 0;
						}
					}
				}
			}
			elements.clear();
			System.out.println("Contacts: "+tracker);
			
			HeatChart map2 = new HeatChart(matrix);
			map2.setTitle("Contact Map");
			map2.setXAxisLabel("X Axis");
			map2.setYAxisLabel("Y Axis");
			map2.setAxisThickness(1);
			map2.saveToFile(new File(writeFile));
			File file = new File(writeFile);
			Desktop.getDesktop().open(file);
		}
		catch(Exception e)
		{
			System.out.println("NOPE");  
			e.printStackTrace();
		}

	}

}
