import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.geom.Line2D;
import javax.swing.*;
public class RGG3 extends Canvas{
    public static String m="s"; //Change to s for square, d for disk
    public int s=600; //Size of square side in pixels
    public static int n=64000; //No of nodes
    public static double exp=64*2; //Expected average degree
    public double r=Math.sqrt(exp/(n*Math.PI))*s; //Adjacency in unit * pixels
    public static double pts[][]=new double[n][2];
    public int degrees[]=new int[5000];
    public long edges=0;
    public String adjlist="";
    public String ptslist="";
    public static int adj[][]=new int[130000][1000];  
    public static int visited[][]= new int[130000][2];
    public static int clr1[]=new int[20000];
    public static int clr2[]=new int[20000];
    public static int clr3[]=new int[20000];
    public static int clr4[]=new int[20000];
    public static int clr1cnt=0,clr2cnt=0,clr3cnt=0,clr4cnt=0;
    public static int bestbackbone=0;
    public static int clra=0,clrb=0;
        
    public static int[][] getAdj() //Getting the adjacency lists from the text file
    {
        String filename="adjlistoutput_"+n+"_"+(int)exp+"_"+m+".txt"; 
        int adj[][]= new int[130000][1000];
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
        double pts[][]=new double[130000][2];
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
    public static int[][] getClr()
    {
        String filename="colorsoutput_"+n+"_"+(int)exp+"_"+m+".txt";
        String line;
        int i=0;
        String[] temp=new String[2];
        int[][] clr=new int[130000][2];
        Scanner sc=new Scanner(System.in);
        File fileclr=new File(filename);
        try
        {
            sc= new Scanner(fileclr);
            while(sc.hasNext())
            {
                
                line=sc.nextLine();
                //System.out.println(line);
                
                temp=line.split("\t");
                clr[i][0]=Integer.parseInt(temp[0]);
                clr[i][1]=Integer.parseInt(temp[1]);
                // break;
                i++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return clr;
    }
    public static boolean CheckEdge(int a, int b)
    {
        boolean edge=false;
        for(int i=1;i<1000;i++)
        {
            if(adj[a][i]==0)
            {
                //System.out.println("No at\t"+a+"\t"+b);
                return false;
            }
            if((adj[a][i])==b)
            {
                //System.out.println("Yes at\t"+a+"\t"+b);
                edge=true;
                break;
            }
        }
        return edge;
    }
    public static int CountEdges(int[] a, int b[], int na, int nb)
    {
        int ne=0;
        for(int i=0;i<na;i++)
        {
            for(int j=0;j<nb;j++)
            {
                if(CheckEdge(a[i],b[j]))
                    ne++;
            }
        }
        return ne;
    }
    public static int[] copyArray(int[] a,int n)
    {
        int b[]=new int[20000];
        for(int i=0;i<n;i++)
        {
            b[i]=a[i];
        }
        return b;
    }
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        super.paint(g);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int pntno;
        //Plotting color 1
        g.setColor(Color.blue);
        for(int i=0;i<clr1cnt;i++)
        {
            pntno=clr1[i];
            g2.draw(new Line2D.Double(pts[pntno][0],pts[pntno][1],pts[pntno][0],pts[pntno][1]));
        }
        //Plotting color 2
        g.setColor(Color.red);
        for(int i=0;i<clr2cnt;i++)
        {
            pntno=clr2[i];
            g2.draw(new Line2D.Double(pts[pntno][0],pts[pntno][1],pts[pntno][0],pts[pntno][1]));
        }
        //Plotting color 3
        g.setColor(Color.yellow);
        for(int i=0;i<clr3cnt;i++)
        {
            pntno=clr3[i];
            g2.draw(new Line2D.Double(pts[pntno][0],pts[pntno][1],pts[pntno][0],pts[pntno][1]));
        }
        //Plotting color 4
        g.setColor(Color.green);
        for(int i=0;i<clr4cnt;i++)
        {
            pntno=clr4[i];
            g2.draw(new Line2D.Double(pts[pntno][0],pts[pntno][1],pts[pntno][0],pts[pntno][1]));
        }
        
        //Drawing selected bipartite backbone
        g.setColor(Color.white);
        int clr1arr[]=new int[20000];
        int clr2arr[]=new int[20000];
        int clr1arrcnt=0,clr2arrcnt=0;
        if(clra==1)
        {
            clr1arr=copyArray(clr1,clr1cnt);
            clr1arrcnt=clr1cnt;
        }
        else if(clra==2)
        {
            clr1arr=copyArray(clr2,clr2cnt);
            clr1arrcnt=clr2cnt;
        }
        else if(clra==3)
        {
            clr1arr=copyArray(clr3,clr3cnt);
            clr1arrcnt=clr3cnt;
        }
        if(clrb==2)
        {
            clr2arr=copyArray(clr2,clr2cnt);
            clr2arrcnt=clr2cnt;
        }
        else if(clrb==3)
        {
            clr2arr=copyArray(clr3,clr3cnt);
            clr2arrcnt=clr3cnt;
        }
        else if(clrb==4)
        {
            clr2arr=copyArray(clr4,clr4cnt);
            clr2arrcnt=clr4cnt;
        }
        for(int i=0;i<clr1arrcnt;i++)
        {
            for(int j=0;j<clr2arrcnt;j++)
            {
                int pt1=clr1arr[i];
                int pt2=clr2arr[j];
                if(CheckEdge(pt1,pt2))
                {
                   g2.draw(new Line2D.Double(pts[pt1][0],pts[pt1][1],pts[pt2][0],pts[pt2][1]));
                }
            }
        }
//        for(int i=0;i<n;i++)
//        {
//            for(int j=0;j<100;j++)
//            {
//                System.out.print(adj[i][j]+"\t");
//            }
//            System.out.println();
//        }
//        for(int i=0;i<n;i++)
//        {
//            System.out.println("Index "+i+" "+pts[i][0]+"\t"+pts[i][1]);
//        }
    }
    public static double coverage(int a[], int b[], int cnta, int cntb)
    {
        double cov=0;
        for(int i=1;i<=n;i++)
        {
            visited[i][0]=i;
            visited[i][1]=0;
        }
        for(int i=0;i<cnta;i++)
        {
            int current=a[i];
            visited[current][1]=1;
            for(int j=1;adj[current][j]>0;j++)
            {
                visited[adj[current][j]][1]=1;
            }
        }
        for(int i=0;i<cntb;i++)
        {
            int current=b[i];
            visited[current][1]=1;
            for(int j=1;adj[current][j]>0;j++)
            {
                visited[adj[current][j]][1]=1;
            }
        }
        for(int i=0;i<=n;i++)
        {
            if(visited[i][1]==1)
                cov++;
        }
        cov=cov/n*100.0;
        return cov;
    }
    public static void main(String args[]) throws IOException
    {
        //double pts[][]=new double[130000][2];
        int clr[][]=new int[130000][2];
        int set[]=new int[4];
        int i=0;
        int cfound=0;
        boolean found=false;
        int currcolor;
        pts=getPts();
        adj=getAdj();
        clr=getClr();
        
       // System.out.println("colors");
        for(i=0;i<n;i++)
        {
            found=false;
            currcolor=clr[i][1];
            if(cfound<4)
            for(int j=0;j<=cfound;j++)
            {
                if(currcolor==set[j])
                {
                    found=false;
                    break;
                }
                if(!found && j==cfound)
                {
                    set[cfound]=clr[i][1];
                    cfound++;
                    break;
                }
            }
            if(clr[i][0]==0&&clr[i][1]==0) 
                break;
           // System.out.println(clr[i][0]+"\t"+clr[i][1]);
        }
        
        for(i=0;i<n;i++)
        {
            if(clr[i][0]==0&&clr[i][1]==0) 
                break;
            if(clr[i][1]==set[0])
            {
                clr1[clr1cnt]=clr[i][0];
                clr1cnt++;
            }
            if(clr[i][1]==set[1])
            {
                clr2[clr2cnt]=clr[i][0];
                clr2cnt++;
            }
            if(clr[i][1]==set[2])
            {
                clr3[clr3cnt]=clr[i][0];
                clr3cnt++;
            }
            if(clr[i][1]==set[3])
            {
                clr4[clr4cnt]=clr[i][0];
                clr4cnt++;
            }
        }
        System.out.println("Cfound\tColor 1: "+clr1cnt+"\tColor 2: "+clr2cnt+"\tColor 3: "+clr3cnt+"\tColor 4: "+clr4cnt);
        
        //Printing points matrix
//        for(i=0;i<n;i++)
//        {
//            System.out.println("Index: "+i+"\t"+pts[i][0]+"\t"+pts[i][1]);
//        }
   
        //Printing adjacency list
//        for(i=0;i<n;i++)
//        {
//            System.out.print("Index: "+i+"\t");
//            for(int j=0;j<500;j++)
//            {
//                if(adj[i][j]==0)
//                    break;
//                System.out.print(adj[i][j]+"\t");
//            }
//            System.out.println();
//        }
        
        
        
       // System.out.println("\nChecking backbone 1\n");
        int nedges1=CountEdges(clr1,clr2,clr1cnt,clr2cnt); 
       // System.out.println("\nChecking backbone 2\n");       
        int nedges2=CountEdges(clr1,clr3,clr1cnt,clr3cnt);
       // System.out.println("\nChecking backbone 3\n"); 
        int nedges3=CountEdges(clr1,clr4,clr1cnt,clr4cnt);
       // System.out.println("\nChecking backbone 4\n"); 
        int nedges4=CountEdges(clr2,clr3,clr2cnt,clr3cnt);
       // System.out.println("\nChecking backbone 5\n"); 
        int nedges5=CountEdges(clr4,clr4,clr2cnt,clr4cnt);
       // System.out.println("\nChecking backbone 6\n"); 
        int nedges6=CountEdges(clr3,clr4,clr3cnt,clr4cnt);
        
        if(nedges1>=bestbackbone)
        {
            bestbackbone=nedges1;
            clra=1;clrb=2;
        }
        if(nedges2>=bestbackbone)
        {
            bestbackbone=nedges2;
            clra=1;clrb=3;
        }
        if(nedges3>=bestbackbone)
        {
            bestbackbone=nedges3;
            clra=1;clrb=4;
        }
        if(nedges4>=bestbackbone)
        {
            bestbackbone=nedges4;
            clra=2;clrb=3;
        }
        if(nedges5>=bestbackbone)
        {
            bestbackbone=nedges5;
            clra=2;clrb=4;
        }
        if(nedges6>=bestbackbone)
        {
            bestbackbone=nedges6;
            clra=3;clrb=4;
        }
        
        System.out.println("Backbone 1 coverage\t"+coverage(clr1,clr2,clr1cnt,clr2cnt));
        System.out.println("Backbone 2 coverage\t"+coverage(clr1,clr3,clr1cnt,clr3cnt));
        System.out.println("Backbone 3 coverage\t"+coverage(clr1,clr4,clr1cnt,clr4cnt));
        System.out.println("Backbone 4 coverage\t"+coverage(clr2,clr3,clr2cnt,clr3cnt));
        System.out.println("Backbone 5 coverage\t"+coverage(clr2,clr4,clr2cnt,clr4cnt));
        System.out.println("Backbone 6 coverage\t"+coverage(clr3,clr4,clr3cnt,clr4cnt));
        System.out.println("Backbone 1: Color 1 and 2: "+nedges1+" / "+(clr1cnt+clr2cnt));
        System.out.println("Backbone 2: Color 1 and 3: "+nedges2+" / "+(clr1cnt+clr3cnt));
        System.out.println("Backbone 3: Color 1 and 4: "+nedges3+" / "+(clr1cnt+clr4cnt));
        System.out.println("Backbone 4: Color 2 and 3: "+nedges4+" / "+(clr2cnt+clr3cnt));
        System.out.println("Backbone 5: Color 2 and 4: "+nedges5+" / "+(clr2cnt+clr4cnt));
        System.out.println("Backbone 6: Color 3 and 4: "+nedges6+" / "+(clr3cnt+clr4cnt));
        
        RGG3 canvas = new RGG3();
        JFrame frame = new JFrame();
        frame.setSize(1000,1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }  
}
