package com.yzchnb.neo4jdao.LamdbaDefination;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ParamsProvider {
    List<List> getParams();
}
