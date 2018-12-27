import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
public class RGG1 extends Canvas{
    public int s=600; //Size of square side in pixels
    public int n=8000; //No of nodes
    public double r=0.01*s; //Adjacency in unit * pixels
    public String m="d"; //Change to s for square, d for disk
    public double points[][]=new double[n][2];
    public double pointsx[][]=new double[n][2];
    public double pointsy[][]=new double[n][2];
    long startTime = System.currentTimeMillis();
    public void quickSortX(double[][] arr, int low, int high) 
    {
		if (arr == null || arr.length == 0) return;
		if (low >= high) return;
		int middle = low + (high - low) / 2;
		double pivot = arr[middle][0];
		int i = low, j = high;
		while (i <= j) 
                {
			while (arr[i][0] < pivot) i++;
			while (arr[j][0] > pivot) j--;
			if (i <= j) 
                        {
				double temp = arr[i][0];
				arr[i][0] = arr[j][0];
				arr[j][0] = temp;
                                temp=arr[i][1];
                                arr[i][1]=arr[j][1];
                                arr[j][1]=temp;
				i++;
				j--;
			}
		}
		if (low < j) quickSortX(arr, low, j);
		if (high > i) quickSortX(arr, i, high);
    }
    public void quickSortY(double[][] arr, int low, int high) 
    {
		if (arr == null || arr.length == 0) return;
		if (low >= high) return;
		int middle = low + (high - low) / 2;
		double pivot = arr[middle][1];
		int i = low, j = high;
		while (i <= j) 
                {
			while (arr[i][1] < pivot) i++;
			while (arr[j][1] > pivot) j--;
			if (i <= j) 
                        {
				double temp = arr[i][0];
				arr[i][0] = arr[j][0];
				arr[j][0] = temp;
                                temp=arr[i][1];
                                arr[i][1]=arr[j][1];
                                arr[j][1]=temp;
				i++;
				j--;
			}
		}
		if (low < j) quickSortY(arr, low, j);
		if (high > i) quickSortY(arr, i, high);
    }
    public double Dist(double x1,double y1, double x2, double y2)
    {
        return ((x1-x2)*(x1-x2))+((y1-y2)*(y1-y2));
    }
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.blue);
//        g2.draw(new Line2D.Double(0,0,0,s));
//        g2.draw(new Line2D.Double(0,0,s,0));
//        g2.draw(new Line2D.Double(s,0,s,s));
//        g2.draw(new Line2D.Double(0,s,s,s));
//        g.setColor(Color.green);
        int i=0;
        if(m.equals("d"))
        {
            while(i<n)
            {
                double xtemp=Math.random()*s;
                double ytemp=Math.random()*s;
                if(Dist(xtemp,ytemp,s/2,s/2) <= s*s/4)
                {
                    points[i][0]=xtemp;  
                    points[i][1]=ytemp;    
                    pointsx[i][0]=points[i][0];
                    pointsx[i][1]=points[i][1];
                    pointsy[i][0]=points[i][0];
                    pointsy[i][1]=points[i][1];
                    i++;
                }
            }
        }
        else if(m.equals("s"))
        {
            while(i<n)
            {
                points[i][0]=Math.random()*s;  
                points[i][1]=Math.random()*s;    
                pointsx[i][0]=points[i][0];
                pointsx[i][1]=points[i][1];
                pointsy[i][0]=points[i][0];
                pointsy[i][1]=points[i][1];
                i++;
            }
        }
        
        quickSortX(pointsx,0,n-1);
        quickSortY(pointsy,0,n-1);
        
        for(i=0;i<n;i++)
        {
            g2.draw(new Line2D.Double(pointsx[i][0],pointsx[i][1],pointsx[i][0],pointsx[i][1]));
        }
        g.setColor(Color.red);
        
        double x1,x2,y1,y2;
        int x1in,x2in,y1in,y2in;
        
        for(i=0; i<n; i++)
        {
//            x1=pointsx[i][0]-r;
//            x2=pointsx[i][0]+r;
//            y1=pointsx[i][1]-r;
//            y2=pointsx[i][1]+r;
            for(int j=i;j<n;j++)
            {
                double dist=Dist(pointsx[j][0],pointsx[j][1],pointsx[i][0],pointsx[i][1]);
                if(dist<= r*r && dist > 0)
                {
                   g2.draw(new Line2D.Double(pointsx[j][0],pointsx[j][1],pointsx[i][0],pointsx[i][1]));
                }
            }
        }
        System.out.println("Done");
        startTime=(System.currentTimeMillis()-startTime)/1000;
        System.out.println(startTime);
    }
    public static void main(String args[]) throws IOException
    {
        int s=600;
        RGG1 canvas = new RGG1();
        JFrame frame = new JFrame();
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }
}