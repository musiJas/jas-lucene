package cn.begonia.lucene.jaslucene.common;

/**
 * @author begonia_chen
 * @data 2020/8/13 15:18
 * @description
 **/
public enum  ResourceType {
    simText("simTextConvertServiceImpl"),
    file("fileConvertServiceImpl"),
    http("httpImpl"),
    html("htmlImpl");
    ResourceType(String instanceName){
        this.instanceName=instanceName;
    }
    private  String  instanceName;

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
