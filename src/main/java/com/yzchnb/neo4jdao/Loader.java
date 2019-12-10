package com.yzchnb.neo4jdao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yzchnb.neo4jdao.NodeEntity.*;
import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import com.yzchnb.neo4jdao.Repository.*;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Loader {

    private static MovieRepository movieRepository;
    private static ActorRepository actorRepository;
    private static GenreRepository genreRepository;
    private static ProductRepository productRepository;
    private static DirectorRepository directorRepository;
    private static DirectorActorRepository directorActorRepository;
    private static ActorCollaborationRepository actorCollaborationRepository;
    private static Driver driver;

    @Resource
    private Driver neo4jDriver;
    @Resource
    private MovieRepository r;
    @Resource
    private ActorRepository a;
    @Resource
    private GenreRepository g;
    @Resource
    private ProductRepository p;
    @Resource
    private DirectorRepository d;
    @Resource
    private DirectorActorRepository dar;
    @Resource
    private ActorCollaborationRepository acr;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        movieRepository = r;
        actorRepository = a;
        genreRepository = g;
        productRepository = p;
        directorRepository = d;
        driver = neo4jDriver;
        directorActorRepository = dar;
        actorCollaborationRepository = acr;
    }

    private static void createIndexes(){
        Session session = driver.session();
        session.run("CREATE CONSTRAINT ON (a:Actor)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("CREATE CONSTRAINT ON (a:Director)\n" +
                "ASSERT a.name IS UNIQUE");
        session.run("CREATE CONSTRAINT ON (a:Genre)\n" +
                "ASSERT a.type IS UNIQUE");
        session.run("CREATE INDEX ON :Product(productId)");
        session.run("CREATE INDEX on :Movie(title)");
        session.run("CREATE INDEX on :Movie(ranking)");
        session.run("CREATE INDEX on :Movie(releaseYear)");

        session.close();
    }

    public static void loadData(String jsonDataPath){

        createIndexes();
        assert jsonDataPath != null;
        File json = new File(jsonDataPath);
        if(!json.exists()){
            System.out.println("文件不存在！");
            System.exit(-1);
        }

        //Actor a = actorRepository.findActorByName("f");

        String jsonContent = readFile(json);
        JSONArray jsonObject = (JSONArray)JSON.parse(jsonContent);
        if(jsonObject == null){
            System.out.println("jsonObject 为null");
            System.exit(-1);
        }
        for (Object o : jsonObject) {
            loadOneObject((JSONObject)o);
        }
        System.out.println(jsonObject);
    }

    private static void loadOneObject(JSONObject jsonObject){
        List<Actor> actors = jsonObject.getJSONArray("actors").toJavaList(String.class).stream().map(Actor::new).collect(Collectors.toList());
        HashMap<String, Actor> name2Actor = new HashMap<>();
        actors.forEach((a) -> name2Actor.put(a.getName(), a));
        List<String> starrings = jsonObject.getJSONArray("starrings").toJavaList(String.class);
        List<Actor> starringActors = new ArrayList<>();
        for (String starring : starrings) {
            starringActors.add(name2Actor.get(starring));
        }
        List<String> supportings = jsonObject.getJSONArray("supportingActors").toJavaList(String.class);
        List<Actor> supportingActors = new ArrayList<>();
        for (String s : supportings) {
            supportingActors.add(name2Actor.get(s));
        }
        List<Genre> genres = jsonObject.getJSONArray("genres").toJavaList(String.class).stream().map(Genre::new).collect(Collectors.toList());
        List<Director> directors = jsonObject.getJSONArray("directors").toJavaList(String.class).stream().map(Director::new).collect(Collectors.toList());
        List<Product> products = jsonObject.getJSONArray("products").toJavaList(JSONObject.class).stream().map((o) -> {
            String price = o.getString("price");
            String format = o.getString("format");
            String productId = o.getString("productId");
            return new Product(productId, format, price);
        }).collect(Collectors.toList());
        Float ranking = jsonObject.getFloat("ranking");
        String title = jsonObject.getString("title");
        Short releaseYear = jsonObject.getShort("releaseYear");
        Short releaseMonth = jsonObject.getShort("releaseMonth");
        Short releaseDay = jsonObject.getShort("releaseDay");
        Short releaseWeekDay = jsonObject.getShort("releaseWeekDay");
        String releaseDate = jsonObject.getString("releaseDate");


        //新建movie实例
        Movie movie = new Movie(title,
                releaseDate,
                releaseYear,
                releaseMonth,
                releaseDay,
                releaseWeekDay,
                ranking);


        //电影的products中发现有已经存在数据库中的，则表明遇到了重复的数据
        if(hasProduct(products)){
            System.out.println("遇到重复movie，跳过");
            System.out.println(movie);
            return;
        }

        //如果没有主演也没有配角，就把所有演员当主角
        if(starringActors.isEmpty() && supportingActors.isEmpty()){
            starringActors.addAll(actors);
        }

        //DONE 去重
        //我们需要对已有的数据进行去重，即找到数据库中已经存在的那些数据，把他们替换过来
        replaceActors(actors);
        replaceActors(starringActors);
        replaceActors(supportingActors);
        replaceDirectors(directors);
        replaceGenres(genres);


        for (Actor actor : actors) {
            ArrayList<Actor> collaborators = new ArrayList<>(actors);
            collaborators.remove(actor);
            ArrayList<ActorCollaborationRelation> acRelationsToBeAdded = new ArrayList<>();
            for (Actor collaborator : collaborators) {
                ActorCollaborationRelation relation = null;

                for (ActorCollaborationRelation actorCollaborationRelation : actor.getActorCollaborationRelations()) {
                     if(actorCollaborationRelation.getActor1().getName().equals(collaborator.getName())
                            || actorCollaborationRelation.getActor2().getName().equals(collaborator.getName()))
                         relation = actorCollaborationRelation;
                }
                if(relation == null){
                    //new collaboration
                    ActorCollaborationRelation actorCollaborationRelation = new ActorCollaborationRelation(actor, collaborator, 1);
                    acRelationsToBeAdded.add(actorCollaborationRelation);
                }else{
                    relation.setCount(relation.getCount() + 1);
                }
            }
            //acRelationsToBeAdded.forEach(actorCollaborationRepository::save);
            actor.getActorCollaborationRelations().addAll(acRelationsToBeAdded);

            ArrayList<DirectorActorRelation> daRelationsToBeAdded = new ArrayList<>();
            for (Director director : directors) {
                DirectorActorRelation relation = null;
                for (DirectorActorRelation r : actor.getDirectorActorRelations()) {
                    if(r.getDirector().getName().equals(director.getName())){
                        relation = r;
                    }
                }
                if(relation == null){
                    //new collaboration
                    DirectorActorRelation directorActorRelation = new DirectorActorRelation(actor, director, 1);
                    daRelationsToBeAdded.add(directorActorRelation);
                }else{
                    relation.setCount(relation.getCount() + 1);
                }
            }
            //daRelationsToBeAdded.forEach(directorActorRepository::save);
            actor.getDirectorActorRelations().addAll(daRelationsToBeAdded);
//            actor.getActors().addAll(collaborators);
//            actor.getDirectors().addAll(directors);
        }

        //actors.forEach((a) -> actorRepository.save(a, 2));

        movie.setStarrings(starringActors);
        movie.setSupportingActors(supportingActors);
        movie.setDirectors(directors);
        movie.setProducts(products);
        movie.setGenres(genres);
        Object o = movieRepository.save(movie, 3);
        System.out.println(o);
        //actorCollaborationRelations.forEach((r) -> actorCollaborationRelationRepository.save(r));
        //directorActorRelations.forEach((r) -> directorActorRelationRepository.save(r));
        System.out.println("save movie, id: " + movie.getId());
    }

    private static String readFile(File file){
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

    private static boolean hasProduct(List<Product> products){
        for (Product product : products) {
            if(product.getProductId() == null)
                continue;
            if(productRepository.existsByProductId(product.getProductId())){
                return true;
            }
        }
        return false;
    }

    private static void replaceActors(List<Actor> actors){
        for (int i = 0; i < actors.size(); i++) {
            Actor a = actorRepository.findActorByName(actors.get(i).getName());
            if(a != null){
                actors.set(i, a);
            }
        }
    }

    private static void replaceDirectors(List<Director> directors){
        for (int i = 0; i < directors.size(); i++) {
            Director a = directorRepository.findDirectorByName(directors.get(i).getName());
            if(a != null){
                directors.set(i, a);
            }
        }
    }

    private static void replaceGenres(List<Genre> genres){
        for (int i = 0; i < genres.size(); i++) {
            Genre g = genreRepository.findGenreByType(genres.get(i).getType());
            if(g != null){
                genres.set(i, g);
            }
        }
    }
}
