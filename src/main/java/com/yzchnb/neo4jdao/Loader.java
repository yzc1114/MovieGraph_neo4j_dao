package com.yzchnb.neo4jdao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
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

    public static void loadData(String jsonDataPath) throws Exception{

        Utils.createIndices();
        assert jsonDataPath != null;
        File json = new File(jsonDataPath);
        if(!json.exists()){
            System.out.println("文件不存在！");
            System.exit(-1);
        }

        //Actor a = actorRepository.findActorByName("f");

        //String jsonContent = readFile(json);
        JSONReader reader = new JSONReader(new FileReader(json));
        //JSONArray jsonObject = (JSONArray)JSON.parse(jsonContent);
//        if(jsonObject == null){
//            System.out.println("jsonObject 为null");
//            System.exit(-1);
//        }
        reader.startArray();
        while(reader.hasNext()){
            Object o = reader.readObject();
            try{
                loadOneObject((JSONObject)o);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        reader.endArray();
        reader.close();

        System.out.println("finished");
    }

    private static void loadOneObject(JSONObject jsonObject){
        List<Actor> starringActors = jsonObject.getJSONArray("starrings").toJavaList(String.class).stream().map(Actor::new).collect(Collectors.toList());
        List<Actor> supportingActors = jsonObject.getJSONArray("supportingActors").toJavaList(String.class).stream().map(Actor::new).collect(Collectors.toList());
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
        HashMap<String, Actor> name2Actor = new HashMap<>();

        //新建movie实例
        Movie movie = new Movie(title,
                releaseDate,
                releaseYear,
                releaseMonth,
                releaseDay,
                releaseWeekDay,
                ranking);


        //电影的products中发现有已经存在数据库中的，则表明遇到了重复的数据
        if(hasSameMovie(movie.getTitle(), products)){
            System.out.println("遇到重复movie，跳过");
            //System.out.println(movie);
            return;
        }

        //DONE 去重
        //我们需要对已有的数据进行去重，即找到数据库中已经存在的那些数据，把他们替换过来
        replaceActors(starringActors);
        replaceActors(supportingActors);
        replaceDirectors(directors);
        replaceGenres(genres);
        replaceProducts(products);

        starringActors.forEach((a) -> name2Actor.put(a.getName(), a));
        supportingActors.forEach((a) -> name2Actor.put(a.getName(), a));
        List<String> actorNames = starringActors.stream().map(Actor::getName).collect(Collectors.toList());
        actorNames.addAll(supportingActors.stream().map(Actor::getName).collect(Collectors.toList()));

        //我们需要获得已经有的合作关系。然后更新他们的count，以及创建新的关系。
        for (int i = 0; i < actorNames.size(); i++) {
            String actorName = actorNames.get(i);
            ArrayList<ActorCollaborationRelation> acRelationsToBeAdded = new ArrayList<>();
            for (int j = i + 1; j < actorNames.size(); j++) {
                String collaboratorName = actorNames.get(j);
                //应该从actor已经有的relations中去遍历，看是否有和该人的联系
                //若没找到则加进去新的
                Actor actor = name2Actor.get(actorName);
                Actor collaborator = name2Actor.get(collaboratorName);
                ActorCollaborationRelation relation = null;
                for (ActorCollaborationRelation actorCollaborationRelation : actor.getActorCollaborationRelations()) {
                    if(actorCollaborationRelation.getActor1().equals(actor) && actorCollaborationRelation.getActor2().equals(collaborator)
                    || actorCollaborationRelation.getActor1().equals(collaborator) && actorCollaborationRelation.getActor2().equals(actor)){
                        //找到了关系
                        relation = actorCollaborationRelation;
                        break;
                    }
                }
                if(relation == null){
                    //new collaboration
                    ActorCollaborationRelation actorCollaborationRelation = new ActorCollaborationRelation(name2Actor.get(actorName), name2Actor.get(collaboratorName), 1);
                    acRelationsToBeAdded.add(actorCollaborationRelation);
                }else{
                    relation.setCount(relation.getCount() + 1);
                }
            }

            name2Actor.get(actorName).getActorCollaborationRelations().addAll(acRelationsToBeAdded);

            ArrayList<DirectorActorRelation> daRelationsToBeAdded = new ArrayList<>();
            for (Director director : directors) {
                DirectorActorRelation relation = null;
                Actor actor = name2Actor.get(actorName);
                for (DirectorActorRelation directorActorRelation : actor.getDirectorActorRelations()) {
                    if(directorActorRelation.getDirector().equals(director)){
                        //找到了旧的关系
                        relation = directorActorRelation;
                    }
                }
                if(relation == null){
                    //new collaboration
                    DirectorActorRelation directorActorRelation = new DirectorActorRelation(name2Actor.get(actorName), director, 1);
                    daRelationsToBeAdded.add(directorActorRelation);
                }else{
                    relation.setCount(relation.getCount() + 1);
                }
            }
            name2Actor.get(actorName).getDirectorActorRelations().addAll(daRelationsToBeAdded);
        }

        movie.setStarrings(starringActors);
        movie.setSupportingActors(supportingActors);
        movie.setDirectors(directors);
        movie.setProducts(products);
        movie.setGenres(genres);
        Object o = movieRepository.save(movie, 3);
        System.out.println("save movie, id: " + movie.getId());
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

    private static boolean hasSameMovie(String title, List<Product> products){
        if(products == null)
            return false;
        for (Movie savedMovie : movieRepository.findMoviesByTitle(title)) {
            HashSet<Product> s1 = new HashSet<>(products);
            HashSet<Product> s2 = new HashSet<>(savedMovie.getProducts());
            if(s1.equals(s2))
                return true;
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

    private static void replaceProducts(List<Product> products){
        for (int i = 0; i < products.size(); i++) {
            Product origin = products.get(i);
            Product p = productRepository.findByProductIdAndFormatAndPrice(origin.getProductId(), origin.getFormat(), origin.getPrice());
            if(p != null){
                products.set(i, p);
            }
        }
    }
}
