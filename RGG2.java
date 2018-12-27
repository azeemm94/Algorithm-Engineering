import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
public class RGG2 extends Canvas{
    public static String m="d"; //Change to s for square, d for disk
    public int s=600; //Size of square side in pixels
    public static int n=4000; //No of nodes
    public static double exp=32; //Expected average degree
    public double r=Math.sqrt(exp/(n*Math.PI))*s; //Adjacency in unit * pixels
    public double points[][]=new double[n][2];
    public int degrees[]=new int[5000];
    public long edges=0;
    public String adjlist="";
    public String ptslist="";
    boolean draw=true; //true for graph, false no graph
    long time = System.currentTimeMillis();
    public static void quickSort(int[][] arr, int low, int high) 
    {
		if (arr == null || arr.length == 0) return;
		if (low >= high) return;
		int middle = low + (high - low) / 2;
		int pivot = arr[middle][1];
		int i = low, j = high;
		while (i <= j) 
                {
			while (arr[i][1] < pivot) i++;
			while (arr[j][1] > pivot) j--;
			if (i <= j) 
                        {
				int temp = arr[i][0];
				arr[i][0] = arr[j][0];
				arr[j][0] = temp;
                                temp=arr[i][1];
                                arr[i][1]=arr[j][1];
                                arr[j][1]=temp;
				i++;
				j--;
			}
		}
		if (low < j) quickSort(arr, low, j);
		if (high > i) quickSort(arr, i, high);
    }
    public static int[][] getAdj() //Getting the adjacency lists from the text file
    {
        String filename="adjlistoutput_"+n+"_"+(int)exp+"_"+m+".txt"; 
        int adj[][]= new int[129000][1000];
        Scanner sc=new Scanner(System.in);
        File fileadj = new File(filename);
        int count=0;
        try
        {
            sc= new Scanner(fileadj);
            while(sc.hasNextLine())
            {
                int i=0;
                String line=sc.nextLine();
                adj[count][0]=count+1;
                for(i=0;i<line.length();i++)
                {
                    if(line.charAt(i)=='>')
                    {
                        break;
                    }
                }
                line=line.substring(i+2);
                String vals[]=new String[500];
                vals=line.split(",");
                for(i=0; i<vals.length; i++)
                {
                    adj[count][i+1]=Integer.parseInt(vals[i].trim())+1;
                }
                count++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return adj;
    }
    public static double[][] getPts() //Getting the points from the text file
    {
        double pts[][]=new double[129000][2];
        String filename="ptslistoutput_"+n+"_"+(int)exp+"_"+m+".txt"; 
        Scanner sc=new Scanner(System.in);
        File filepts = new File(filename);
        int count=0,i=0;
        try
        {
            sc= new Scanner(filepts);
            while(sc.hasNext())
            {
                String line=sc.nextLine();
                for(i=0;i<line.length();i++)
                {
                    if(line.charAt(i)=='>')
                        break;
                }
                line=line.substring(i+2);
                String vals[]=new String[2];
                vals=line.split(",");
                pts[count][0]=Double.parseDouble(vals[0].trim());                
                pts[count][1]=Double.parseDouble(vals[1].trim());
                count++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return pts;
    }
    public static int getSmallestDeg(int degreelist[][],int adj[][],int rem) //smallest last ordering function
    {
        int smallest=100000;
        int node=-1;
        int found=-1;
        for(int i=0;i<n; i++)
        {
            if(degreelist[i][1]<smallest && degreelist[i][1]>=0)
            {
                node=degreelist[i][0];
                smallest=degreelist[i][1];
                found=i;
            }
        }
        degreelist[found][1]=-1;
        
        int current=1;
        while(adj[node][current]>0)
        {
            int x=adj[node][current]-1;
            for(int i=1;i<n;i++)
            {
                if(degreelist[i][0]==(x+1))
                {
                    degreelist[i][1]--;
                    break;
                }
            }
            for(int i=1;;i++)
            {
                if(adj[x][i]==node)
                {
                    //System.out.println("Deleted"+adj[x][i]);
                    adj[x][i]=-1;
                    break;
                }
                if(adj[x][i]==0)
                {
                    break;
                }
            }
            current++;
        }
        
        return node;
    }
    public static void main(String args[]) throws IOException
    {
        int adj[][]=new int[129000][1000];        
        int adj1[][]=new int[129000][1000];
        int color[]=new int[129000];
        double pts[][]=new double[129000][2];
        int degreelist[][]=new int[129000][2];
        int order[][]=new int[129000][1000];
      //  int slast[][]=new int[129000][100];
        int sorder[]=new int[129000];
        int sordercnt=0;
        int colorfreq[]=new int[500];
        int rem=n;
        pts=getPts();
        adj=getAdj();
        
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<2;j++)
            {
               // System.out.print(pts[i][j]+" ");
            }
          //  System.out.println();
        }

        int i=0,j=0,pos=1; boolean flag=false;
        
        for(i=0;i<n;i++)
        {
            for(j=0;j<1000;j++)
            {
                if(adj[i][j]==0)
                    break;
            }
            degreelist[i][0]=i+1;
            degreelist[i][1]=j-1;
        }
        
        quickSort(degreelist, 0,n);
        for(i=0;i<n;i++)
        {
            System.out.println(degreelist[i][0]+"\t"+degreelist[i][1]);
        }
        int maxdeg=degreelist[i-1][1];
        System.out.println("Max degree= "+maxdeg);
        
//        for(i=0;i<=maxdeg;i++)
//        {
//            order[i][0]=i;
//        }
//        int deg=degreelist[1][1];
//        for(i=0;i<n;i++)
//        {
//            if(degreelist[i][1]>deg) 
//            {
//                pos=1;
//                deg=degreelist[i][1];
//            }
//            order[deg][pos]=degreelist[i][0];
//            pos++;
//        }
      
//        System.out.println("Order array");
//        for(i=0;i<=maxdeg;i++)
//        {
//            for(j=0;j<2000;j++)
//            {
//                if(order[i][j]==0) break;
//                System.out.print(order[i][j]+" ");
//            }
//            System.out.println();
//        }
        
        System.out.println("Adj");
        for(i=0;i<=n;i++)
        {
            for(j=0;j<1000;j++)
            {
                if(adj[i][j]==0)
                {
                    adj[i][j]=-2;
                    //System.out.print("Count="+ (j-1));
                    break;
                }
                //System.out.print(adj[i][j]+"\t");
            }
            //System.out.println();
        }
        
        //System.out.println("Adjacency list copied:");
        for(i=0;i<n;i++)
        {
           // System.out.print("Index"+i+"\t");
            for(j=0;j<2000;j++)
            {
                if(adj[i][j]==0) break;
                adj1[i][j]=adj[i][j];
                //System.out.print(adj1[i][j]+"\t");
            }
         //  System.out.println();
        }
        
        System.out.println("Degrees listed");
        int x=0;
        for(;rem>0;rem--)
        {
            //System.out.println(getSmallestDeg(degreelist,adj,rem));
            sorder[rem-1]=getSmallestDeg(degreelist,adj,rem);
        }
        System.out.println("Smallest last order");
        for(i=0;i<n;i++)
            System.out.println(sorder[i]);
        
        
        //Coloring graph
        String colors="";
        int current=0;
        int neighbour=0;
        boolean colored;
        System.out.println("Colored nodes");
        for(i=0;i<n-1;i++) //Iterating through smallest last order
        {
            current=sorder[i];
            colored=false;
            if(color[current-1]==0)
            {
                for(j=1;j<=1000;j++)//Iterating colors
                {
                    for(int k=1;k<1000;k++) //Iterating neighbors
                    {
                        neighbour=adj1[current-1][k];
                        if(adj1[current-1][k]==-2)
                        {
                            color[current-1]=j;
                            colorfreq[j]++;
                            System.out.println(current+"\t"+j);
                            colored=true;
                            break;
                        }
                        
                        if(color[neighbour-1]==j)
                        {
                            break;
                        }
                    }
                    if(colored)
                    {
                        break;
                    }
                }
            }
        }
        
        int topclr1=0, topclr2=0, topclr3=0,topclr4=0,temp;
        for(i=1;colorfreq[i]>0;i++)
        {
            if(colorfreq[i]>=colorfreq[topclr1])
            {
                topclr4=topclr3;
                topclr3=topclr2;
                topclr2=topclr1;
                topclr1=i;
            }
            else if(colorfreq[i]>=colorfreq[topclr2])
            {
                topclr4=topclr3;
                topclr3=topclr2;
                topclr2=i;
            }
            else if(colorfreq[i]>=colorfreq[topclr3])
            {
                topclr4=topclr3;
                topclr3=i;
            }
            else if(colorfreq[i]>=colorfreq[topclr4])
            {
                topclr4=i;
            }
            if(colorfreq[i]==0)
                break;
            System.out.println("\t"+i+"\t"+colorfreq[i]);
        }
        
        for(i=0;i<n;i++)
        {
            if(color[i]==topclr1||color[i]==topclr2||color[i]==topclr3||color[i]==topclr4)
            colors+=Integer.toString(i)+"\t"+Integer.toString(color[i])+"\n";
        }
        
        //colors+="\n"+topclr1+"\n"+topclr2+"\n"+topclr3+"\n"+topclr4;
        //Writing to txt file
        String filename="colorsoutput_"+n+"_"+(int)exp+"_"+m+".txt";  
        Files.write(Paths.get(filename), colors.getBytes());     
        
        System.out.println("TOP COLORS \n1. "+topclr1+"\n2. "+topclr2+"\n3. "+topclr3+"\n4. "+topclr4);
        
//        System.out.println("Colored");
//        for(i=0;i<n;i++)
//        {
//                System.out.println("Index "+i+" color "+color[i]+"\t");
//        }

//        for(i=0;i<n;i++) //Test print adjacency list
//        {
//            for(j=0;j<500;j++)
//            {
//                System.out.print(adj[i][j]+"\t");
//            }
//            System.out.println();
//        }
//        System.out.println("Degree at end");
//        for(i=0;i<n;i++) //Test print all degrees and nodes
//        {
//            System.out.println(degreelist[i][0]+"\t"+degreelist[i][1]);
//        }
    }
}
