package cn.begonia.lucene.jaslucene.util;

import java.io.*;
import java.util.UUID;

public class FileUtil {
    private  final static  int   segmentTotal=102400;

    public static void main(String[] args) {
        String  resourcePath="D:\\data\\text\\大秦帝国.txt";
        String  targetPath="D:\\data\\text\\";
        sliceFile(resourcePath,targetPath);
    }

    public static  void  sliceFile(String  resourcePath,String targetPath){
        try {
            BufferedReader  br=new BufferedReader(new FileReader(resourcePath));
            PrintWriter  pw=null;
            StringBuffer  sb=new StringBuffer();
            String content="";
            while(true){
                try {
                    if (!((content=br.readLine())!=null)) break;
                    sb.append(content);
                    long  length= sb.toString().length();
                    if(length>FileUtil.segmentTotal){
                        String name=UUID.randomUUID().toString();
                        name=name.replace("-","").substring(0,10);
                        String fileName=targetPath+File.separator+name+".txt";
                        File file=new File(fileName);
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        pw=new PrintWriter(new FileWriter(fileName));
                        pw.write(sb.toString());
                        pw.flush();
                        sb.setLength(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(pw!=null){
                        pw.close();
                    }
                    if(br!=null){
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static  void  deleteFiles(File  directory){
        if(directory.isFile()){
             directory.delete();
        }
        for(File file:directory.listFiles()){
            deleteFiles(file);
        }
    }



    @SuppressWarnings("all")
    public static  String  getStringByFile(File file) {
        BufferedReader  bf=null;
        String temp="";
        StringBuffer sb=new StringBuffer();
        try {
            bf=new BufferedReader(new FileReader(file));
            while((temp=bf.readLine())!=null){
                sb.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
