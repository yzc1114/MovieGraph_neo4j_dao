package com.yzchnb.neo4jdao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Neo4jDaoApplication {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Neo4jDaoApplication.class, args);
        String path = null;
        boolean load = false;
        if(args.length == 0){
            path = "/Users/purchaser/Desktop/resAll.json";
            load = false;
            //System.out.println("需要参数 1: 是否进行数据导入(true/false) 2:(可选) 数据路径");
            //System.exit(-1);
        }
        if(args.length == 1){
            if(args[0].equals("true")){
                System.out.println("需要数据路径参数！");
                System.exit(-1);
                return;
            }
        }
        if(args.length >= 2){
            if(args[0].equals("true")){
                load = true;
                path = args[1];
            }
        }
        if(load){
            Loader.loadData(path);
        }
    }

}
