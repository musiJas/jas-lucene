package cn.begonia.lucene.jaslucene.common;

/**
 * DTO 数据传输使用 用于上下层之间和接口之间
 *
 * */
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

    public  static Result isOk(){
        return  new Result(ResultEnum.SUCCESS);
    }

    public  static Result isFail(){
        return  new Result(ResultEnum.FAIL);
    }
}
