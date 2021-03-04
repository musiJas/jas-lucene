package cn.begonia.lucene.jaslucene.demo.webflux;

import cn.begonia.lucene.jaslucene.common.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {


    @RequestMapping("/toJson")
    public Mono<Result>   toJson(){
        return  Mono.just(Result.isOk());
    }

    @RequestMapping("/toFlux")
    public Flux<Result>  toFlux(){
        return  Flux.just(Result.isFail(),Result.isOk(),Result.isFail());
    }


    @RequestMapping("/toFluxs")
    public Flux<Result>  toFluxs(){
        List<Result>  list=new ArrayList<>();
        list.add(Result.isOk());
        list.add(Result.isOk());
        list.add(Result.isOk());
        list.add(Result.isOk());
        return  Flux.fromIterable(list);
    }


    @RequestMapping("/toEmpty")
    public  Mono<Void>   toEmpty(){
        return Mono.empty();
    }


}
