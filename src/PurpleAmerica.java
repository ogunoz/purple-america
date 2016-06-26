import acm.graphics.*;
import acm.program.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class PurpleAmerica extends GraphicsProgram{
	// Global variables
	private String args[];
	private String parties[];
	private String path;
	private int scaleSize = 60;
	private int RECT_SIZE = 50;
	private HashMap<GPolygon, District> stateList;
	private GLabel republicanInfo, democratInfo, otherInfo, districtInfo;

	public PurpleAmerica(String args[]){
		super();
		String fileName = "", path = "";
		this.args = new String[2];
		this.parties = new String[4];
		for (int i = 0; i < args.length; i++)
			this.args[i] = args[i];
		String wholeGeometricTxt[] = args[0].split("/");
		fileName = wholeGeometricTxt[wholeGeometricTxt.length-1];
		path = args[0].substring(0, args[0].indexOf(fileName));
		this.path = path;
		if (fileName.equals("USA") || fileName.equals("USA-county"))
			scaleSize = 30;
		stateList = new HashMap<GPolygon, District>();
		districtInfo = new GLabel("");
		republicanInfo = new GLabel("");
		democratInfo = new GLabel("");
		otherInfo = new GLabel("");
		districtInfo.setFont(new Font("Arial", Font.BOLD, 20));
		republicanInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		democratInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		otherInfo.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		republicanInfo.setColor(new Color(153,0,0));
		democratInfo.setColor(new Color(0,0,102));
		otherInfo.setColor(new Color(0,102,0));
	}

	public void run() {
		this.setVisible(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(dim.width, dim.height);
		String geometricData = args[0] + ".txt";
		String electionYear = args[1];
		GLabel a = new GLabel("Purple America: " + electionYear + " Presidential Election");
		a.setFont(new Font("Arial",Font.ITALIC,44));
		add(a,this.getWidth()/2-a.getWidth()/2,this.getHeight()/8);
		add(districtInfo, 50, getHeight()-250);
		add(democratInfo, 50, getHeight()-210);
		add(republicanInfo, 50, getHeight()-190);
		add(otherInfo, 50, getHeight()-170);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					geometricData)));
			// Local Variables
			ElectionMap electionResultList = null;
			String line = "";
			int boundaryNumber, index, minIndex = 0, maxIndex = 0;
			double maxY,minY,maxX,minX;
			String subRegion = "", region = "", previousRegion = "";
			GPolygon d = null;
			float totalVote = 0;

			line = reader.readLine(); // minimum points
			String minPoints[] = line.split(" ");
			if (minPoints.length > 4)
				minIndex++;
			minX = Double.parseDouble(minPoints[minIndex]);
			minY = Double.parseDouble(minPoints[minIndex + 3]);
			line = reader.readLine(); // maximum points
			String maxPoints[] = line.split(" ");
			if (maxPoints.length > 4)
				maxIndex++;
			maxX = Double.parseDouble(maxPoints[maxIndex]);
			maxY = Double.parseDouble(maxPoints[maxIndex + 3]);
			reader.readLine(); // total boundary numbers
			reader.readLine(); // empty line
			while ((line = reader.readLine()) != null) {
				subRegion = line;
				line = reader.readLine();
				region = line;
				if (!region.equals(previousRegion)){
					electionResultList = readElectionResultsAndFillElectionResultList(path + region+electionYear+".txt");
					if (electionResultList == null){
						System.out.println("Year is incorrect!");
						reader.close();
						return;
					}

					previousRegion = region;
				}
				line = reader.readLine();
				boundaryNumber = Integer.parseInt(line);
				d = new GPolygon();

				for (int j = 0; j < boundaryNumber; j++){
					line = reader.readLine();
					String[] coordinates = line.split(" ");
					double xCoordinate, yCoordinate = 0;
					index = 0;
					if (coordinates.length > 4)
						index++;
					xCoordinate = Double.parseDouble(coordinates[index]) - minX;
					yCoordinate = maxY- Double.parseDouble(coordinates[index + 3]);
					d.addVertex(scaleSize*xCoordinate, scaleSize*yCoordinate);
				}
				d.setFilled(true);
				ElectionResult temp = electionResultList.get(subRegion);
				if (temp != null){
					d.setFillColor(new Color(temp.getRepublicans()/temp.getTotal(), temp.getOthers()/temp.getTotal(), temp.getDemocrats()/temp.getTotal()));	
					totalVote = temp.getTotal();
				}
				else{
					d.setFillColor(Color.BLACK);
					totalVote = 0;
					System.out.println("Unknown district is " + subRegion + " and region is " + region);
				}
				d.setColor(Color.BLACK);


				add(d,this.getWidth()/2- scaleSize*(maxX-minX)/2,this.getHeight()/2-scaleSize*(maxY-minY)/2);
				District distr = new District(region, subRegion, totalVote);
				stateList.put(d,distr);
				line = reader.readLine(); // empty line
			}
			reader.close();
			GRect r1 = new GRect(RECT_SIZE,RECT_SIZE);
			GRect r2 = new GRect(RECT_SIZE,RECT_SIZE);
			GRect r3 = new GRect(RECT_SIZE,RECT_SIZE);
			GRect r4 = new GRect(RECT_SIZE,RECT_SIZE);

			GLabel democrat = new GLabel(parties[2]);
			GLabel republican = new GLabel(parties[1]);
			GLabel other = new GLabel(parties[3]);
			GLabel unknown = new GLabel("Unknown");
			Font font = new Font("Arial",20,20);
			r1.setFilled(true);
			r2.setFilled(true);
			r3.setFilled(true);
			r4.setFilled(true);
			r1.setFillColor(Color.BLUE);
			r2.setFillColor(Color.RED);
			r3.setFillColor(Color.GREEN);
			r4.setFillColor(Color.BLACK);
			democrat.setFont(font);
			other.setFont(font);
			republican.setFont(font);
			unknown.setFont(font);
			int labelSize = font.getSize();
			double rectX = this.getWidth()- r1.getWidth()-150;
			double rectY = this.getHeight()- r4.getHeight()-80;
			add(r1, rectX, rectY-210);
			add(r2, rectX, rectY-140);
			add(r3, rectX, rectY-70);
			add(r4, rectX, rectY);
			add(democrat, this.getWidth()- democrat.getWidth()-220, rectY-210 + labelSize);
			add(republican, this.getWidth()- republican.getWidth()-220, rectY-140 + labelSize);
			add(other, this.getWidth()- other.getWidth()-220, rectY-70 + labelSize);
			add(unknown, this.getWidth()- unknown.getWidth()-220, rectY + labelSize);
		} catch (IOException e){
			System.out.println(geometricData+" file cannot be found in the proper directory!");
			a.setLabel(geometricData+" file cannot be found in the proper directory!");
			a.setFont(new Font("Arial", 40,40));
			a.setColor(Color.RED);
		}
		this.setVisible(true);
		addMouseListeners();
	}

	private ElectionMap readElectionResultsAndFillElectionResultList(String electionData){
		ElectionMap electionList = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					electionData)));
			electionList = new ElectionMap();
			String line = "";
			line = reader.readLine();
			parties = line.split(",");
			while ((line = reader.readLine()) != null) {
				String[] wholeLine = line.split(",");
				String key = wholeLine[0];
				ElectionResult result = new ElectionResult(wholeLine[1], wholeLine[2], wholeLine[3]);
				electionList.put(key, result);
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(electionData+" file cannot be found in the proper directory!");
		}
		return electionList;
	}

	public void mouseClicked(MouseEvent e){
		GPolygon p = null;
		try{
			p = (GPolygon)this.getElementAt(e.getX(), e.getY());
		}
		catch(ClassCastException exc){
			return;
		}
		if (p == null)
			return;
		District d = stateList.get(p);
		float totalVote = d.getTotalVote();
		Color c = p.getFillColor();
		double r = c.getRed()/255.0;
		double b = c.getBlue()/255.0;
		double g = c.getGreen()/255.0;
		int rNum = (int)(r * totalVote);
		int dNum = (int)(b * totalVote);
		int oNum = (int)(g * totalVote);
		DecimalFormat df = new DecimalFormat("0.00");
		NumberFormat nf = NumberFormat.getInstance();
		districtInfo.setLabel(d.getDistrict() + ", " + d.getState());
		republicanInfo.setLabel(String.format("%-12s%-6s",parties[1],"Rep.").toString() + String.format("%10s", nf.format(rNum))+ String.format("%-5s%5s", " ",df.format(r*100)) +"%");
		democratInfo.setLabel(String.format("%-12s%-6s",parties[2],"Dem.").toString()+ String.format("%10s", nf.format(dNum))+ String.format("%-5s%5s", " ",df.format(b*100)) +"%");
		otherInfo.setLabel(String.format("%-18s",parties[3]).toString()+ String.format("%10s", nf.format(oNum))+ String.format("%-5s%5s", " ",df.format(g*100)) +"%");
	}
}