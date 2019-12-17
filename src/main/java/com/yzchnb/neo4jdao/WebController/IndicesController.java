package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/indices")
@RequestMapping("/indices")
public class IndicesController {

    @GetMapping("/deleteIndices")
    public String deleteIndices(){
        Utils.deleteIndices();
        return "ok";
    }

    @GetMapping("/createIndices")
    public String createIndices(){
        Utils.createIndices();
        return "ok";
    }

}
