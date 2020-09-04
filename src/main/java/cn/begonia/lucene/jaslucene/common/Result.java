package cn.begonia.lucene.jaslucene.common;

import lombok.Data;

/**
 * DTO 数据传输使用 用于上下层之间和接口之间
 *
 * */
@Data
public class Result {
    private  int  code;
    private  String  msg;
    private  Object  obj;

    public  Result(){

    }

    public  Result(ResultEnum  re){
        this.code=re.getCode();
        this.msg=re.getMsg();
    }

    public  Result(ResultEnum  re,Object obj){
        this.code=re.getCode();
        this.msg=re.getMsg();
        this.obj=obj;
    }

    public  static Result isOk(){
        return  new Result(ResultEnum.SUCCESS);
    }
    public  static Result isOk(Object obj){
        return  new Result(ResultEnum.SUCCESS,obj);
    }
    public  static Result isFail(){
        return  new Result(ResultEnum.FAIL);
    }
}
