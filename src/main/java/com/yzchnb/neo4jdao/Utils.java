package com.yzchnb.neo4jdao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.LamdbaDefination.Procedure;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

@Component
public class Utils {
    public static String readFile(File file){
        StringBuilder builder = new StringBuilder();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while(true){
                String l = bufferedReader.readLine();
                if(l == null){
                    return builder.toString();
                }
                builder.append(l);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static Driver driver;

    @Resource
    private Driver d;

    @PostConstruct
    private void init(){
        driver = d;
    }

    public static void createIndices(){
        Session session = driver.session();
        session.run("CREATE CONSTRAINT ON (a:Actor)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("CREATE CONSTRAINT ON (a:Director)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("CREATE CONSTRAINT ON (a:Genre)\n" +
                "ASSERT a.type IS UNIQUE");
        session.run("CREATE INDEX on :Movie(title)");
        session.run("CREATE INDEX on :Movie(ranking)");
        session.run("CREATE INDEX on :Movie(releaseYear)");
        session.run("CREATE INDEX on :Movie(releaseMonth)");
        session.run("CREATE INDEX on :Movie(releaseDay)");
        session.run("CREATE INDEX on :Product(productId)");
        session.run("CREATE INDEX on :Product(price)");
        session.run("CREATE INDEX on :Product(format)");
        session.run("CREATE INDEX on :Review(time)");
        session.run("CREATE INDEX on :Review(score)");
        session.run("CREATE INDEX on :User(userId)");
        session.run("CREATE INDEX on :User(profileNames)");

        session.close();
    }

    public static void deleteIndices(){
        Session session = driver.session();
        session.run("DROP CONSTRAINT ON (a:Actor)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("DROP CONSTRAINT ON (a:Director)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("DROP CONSTRAINT ON (a:Genre)\n" +
                "ASSERT a.type IS UNIQUE");
        session.run("DROP INDEX on :Movie(title)");
        session.run("DROP INDEX on :Movie(ranking)");
        session.run("DROP INDEX on :Movie(releaseYear)");
        session.run("DROP INDEX on :Movie(releaseMonth)");
        session.run("DROP INDEX on :Movie(releaseDay)");
        session.run("DROP INDEX on :Product(productId)");
        session.run("DROP INDEX on :Product(price)");
        session.run("DROP INDEX on :Product(format)");
        session.run("DROP INDEX on :Review(time)");
        session.run("DROP INDEX on :Review(score)");
        session.run("DROP INDEX on :User(userId)");
        session.run("DROP INDEX on :User(profileNames)");

        session.close();
    }

    public static String wrap(Procedure f){
        long s = System.currentTimeMillis();
        Object o = f.process();
        s = System.currentTimeMillis() - s;
        JSONObject object = new JSONObject();
        object.put("data", o);
        object.put("time", s);
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String testQueries(Object webController, Map<String, ParamsProvider> method2Provider){
        ArrayList<Method> methods = new ArrayList<>(Arrays.asList(webController.getClass().getDeclaredMethods()));
        methods.removeIf((m) -> !m.getName().startsWith("get"));
        HashMap<String, Float> methods2Time = new HashMap<>();
        for (Method method : methods) {
            ParamsProvider paramsProvider = method2Provider.get(method.getName());
            long timeSum = 0;
            for (List params : paramsProvider.getParams()) {
                try{
                    timeSum += JSON.parseObject((String)method.invoke(webController, params.toArray())).getLong("time");
                }catch (Exception e){
                    e.printStackTrace();
                    params.forEach(System.out::println);
                }
            }
            methods2Time.put(method.getName(), (float)timeSum / (float)paramsProvider.getParams().size());
        }
        return JSON.toJSONString(methods2Time, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String compare(Object webController, Integer times) throws Exception{
        Method testMethod = webController.getClass().getMethod("test", Integer.class);
        deleteIndices();
        String no = (String) testMethod.invoke(webController, times);
        createIndices();
        String yes = (String) testMethod.invoke(webController, times);
        JSONObject res = new JSONObject();
        res.put("withIndices", JSON.parseObject(yes));
        res.put("withoutIndices", JSON.parseObject(no));
        return JSON.toJSONString(res);
    }
}
