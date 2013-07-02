package com.tirsweb.datamining;
/**
 * 文件名：Kmeans2.java
 *
 * 版本信息： version 1.0
 * 日期：May 19, 2013
 * Copyright by menuz
 *//*
package com.tirsweb.datamining;

import java.io.*;   
import java.util.ArrayList;

import com.tirsweb.dao.jdbc.DAO5;
import com.tirsweb.model.ParkingLocation;
import com.tirsweb.util.gps.GeoDistance;

*//**
 * 
 * @author menuz  disable
 *
 *//*
public class Kmeans2    
{   
    double[][] sample;//ËùÓÐÑù±¾Êý¾Ý   
    double[][] center;//Ã¿ÖÖÀàÖÐÐÄµãµÄXYÖµ   
    int clusterkind,totaln;//¾ÛÀàÖÖÀàÊý¼°Ñù±¾×ÜÊý   
       
       
    public Kmeans2(int total,int clusterkd)   
    {   
        this.totaln=total;   
        this.clusterkind=clusterkd;   
        center=new double[clusterkind][2];   
        sample=new double[totaln][3];   
           
    }   
       
    void Calssify()//¸ù¾ÝÅ·¼¸ÀïµÂ¾àÀë,½«ËùÓÐµã»®·Öµ½¸÷¸öÀàÖÐ   
    {      
        int i,j;   
        double dis,district;   
        i=0;   
        while(i<totaln)   
        {   
            district=10000;   
            for(j=0;j<clusterkind;j++)   
            {   
                //dis=Math.sqrt((sample[i][0]-center[j][0])*(sample[i][0]-center[j][0])+(sample[i][1]-center[j][1])*(sample[i][1]-center[j][1]));   
            	dis = GeoDistance.computeCompareDistance(sample[i][0], sample[i][1], center[j][0], center[j][1]);
                if(dis<district)   
                {   
                       
                    sample[i][2]=j;   
                    district=dis;   
                }   
            }      
            i++;   
        }   
    }   
       
    boolean WetherAlter(double[][] center1,double[][]center2,int clusterkind)//ÅÐ¶ÏÒÀ´Î¼ÆËãµÄ¸÷¸öÀà¾ùÖµÊÇ·ñÓÐ¸Ä±ä   
    {   
        int cout=0;   
        while(cout<clusterkind)   
        {   
            if(center1[cout][0]!=center2[cout][0] || center1[cout][1]!=center2[cout][1])   
                return false;   
            cout++;   
        }   
            return true;   
    }   
       
    boolean IsUpdataCenter()//ÅÐ¶ÏÀàÖÐÐÄÖµÊÇ·ñ¸Ä±ä£¬¸Ä±äµÄÏàÓ¦¸ü¸ÄÀàÖÐÐÄµãµÄÖµ   
    {   
        int i,j;   
        int[] kind=new int[clusterkind];   
        double[]amoutx=new double[clusterkind];   
        double[]amouty=new double[clusterkind];   
        double[][]amoutxy =new double[clusterkind][2];   
        for(i=0;i<totaln;i++)   
        {   
            amoutx[(int)sample[i][2]]+=sample[i][0];   
            amouty[(int)sample[i][2]]+=sample[i][1];   
            kind[(int)sample[i][2]]++;   
        }   
        for(j=0;j<clusterkind;j++)   
        {   
            amoutx[j]/=(double)kind[j];   
            amouty[j]/=(double)kind[j];   
            amoutxy[j][0]=amoutx[j];   
            amoutxy[j][1]=amouty[j];   
        }   
        if(!WetherAlter(amoutxy,center,clusterkind))   
        {   
            for(i=0;i<clusterkind;i++)   
            {   
                center[i][0]=amoutxy[i][0];   
                center[i][1]=amoutxy[i][1];   
            }   
            return false;   
               
        }   
        else   
            return true;   
           
    }   
       
      void PrintSave()throws java.io.IOException//Êä³ö¾ÛÀà½á¹ûµ½ÆÁÄ»ÉÏ£¬²¢±£´æµ½Result.txtÎÄ¼þÖÐ   
        {      
            PrintWriter out=new PrintWriter(new BufferedWriter(new FileWriter("Result.txt")));   
            for(int i=0;i<totaln;i++)   
            {   
                System.out.println("sample:"+(i+1)+"  "+sample[i][0]+" "+sample[i][1]+" "+"Class:"+((int)sample[i][2]+1)+"\n");   
                out.println("sample:"+(i+1)+"  "+sample[i][0]+" "+sample[i][1]+" "+"Class:"+((int)sample[i][2]+1)+"\n");   
            }   
            
            
            for(int i=0; i<clusterkind; i++) {
            	System.out.println(center[i][0] + "     a  "  + center[i][1] + " a  ");
            }
        }   
           
           
    void Cluster()throws java.io.IOException   
    {   
        int change=1;
        int i = 0;
        while(change==1)   
        {   
        	i++;
            Calssify();   
            if(IsUpdataCenter())   
            {   
               PrintSave();   
               change=0;   
            }   
            System.out.println("i = " + i);
        }   
        System.out.println("i = " + i);
    }   
       
     public static void main(String[] args)throws java.io.IOException    
     {   
         String sourcefile;   
         System.out.print("Input the sourcefile's path:");   
         BufferedReader tempread = new BufferedReader(new InputStreamReader(System.in));   
         sourcefile = tempread.readLine();   
         int clusterkind;   
         System.out.print("Input the number of kind to cluster :");   
         clusterkind=Integer.parseInt(tempread.readLine());   
         InputStream inputstr=new FileInputStream(sourcefile);   
         BufferedReader readline=new BufferedReader(new InputStreamReader(inputstr));   
         String everyline;   
         Kmeans2 kmeans=new Kmeans2(10,clusterkind);   
         
        DAO5 dao = new DAO5();
 		ArrayList<ParkingLocation> pks = new ArrayList<ParkingLocation>();
 		dao.getParkingLocationByArcId(pks, 57);
            
 		 int clusterkind = 4;
 		 Kmeans2 kmeans=new Kmeans2(pks.size(),clusterkind); 
         int cout=0;   
         while(cout<kmeans.totaln)   
         {   
             everyline=readline.readLine();   
             if(everyline==null)   
                 break;   
             String[] line=everyline.split(" ");   
             kmeans.sample[cout][0]=pks.get(cout).getLati();   
             kmeans.sample[cout][1]=pks.get(cout).getLongi();   
             kmeans.sample[cout][2]=-1;   
             if(cout<kmeans.clusterkind)   
             {   
                 kmeans.center[cout][0]=pks.get(cout).getLati();    
                 kmeans.center[cout][1]=pks.get(cout).getLongi();   
             }   
             cout++;   
         }   
         kmeans.Cluster();   
     }   
}   

*/