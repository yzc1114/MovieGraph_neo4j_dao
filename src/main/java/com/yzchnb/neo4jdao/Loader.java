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

        products = removeDuplicatedProducts(products);

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
                DirectorActorRelation relation = directorActorRepository.getDirectorActorRelation(actorName, director.getName());
                if(relation == null){
                    //new collaboration
                    relation = new DirectorActorRelation(name2Actor.get(actorName), director, 1);
                    daRelationsToBeAdded.add(relation);
                }else{
                    relation.setCount(relation.getCount() + 1);
                    directorActorRepository.save(relation);
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

    public static void addRelations(){
        addActorRelations();
    }

    public static void addDirectorActorRelations(){
        //这是优化关系存储节点的方法。
        //之前的存储策略，是在关系上建立属性，并根据属性查询关系。
        //但是实践表明，根据关系上的属性来查询非常缓慢。所以我们查阅资料，得知neo4j在内部会维护节点间关系数量。
        //所以每对演员间的关系可以采用复数关系的形式，来看看这样是否会更快。


        //首先针对演员和导演进行测试。如果有好转，再将演员间的冗余关系也加进来。
        int s = 0, batchSize = 100;
        HashMap<String, HashSet<String>> actor2Directors = new HashMap<>();
        while(true){
            HashSet<DirectorActorRelation> rs = directorActorRepository.getBatch(s, batchSize);
            if(rs.size() == 0)
                break;
            for (DirectorActorRelation r : rs) {
                Actor a = r.getActor();
                Director d = r.getDirector();
                if(actor2Directors.containsKey(a.getName()) && actor2Directors.get(a.getName()).contains(d.getName())){
                    continue;
                }
                actor2Directors.computeIfAbsent(a.getName(), n -> new HashSet<>());
                actor2Directors.get(a.getName()).add(d.getName());
                HashSet<DirectorActorRelation> relations = new HashSet<>();
                int count = directorActorRepository.getDirectorActorCollaborationCountOptimized(a.getName(), d.getName());
                for (int i = 0; i < r.getCount() - count; i++) {
                    relations.add(new DirectorActorRelation(a, d, r.getCount()));
                }
                directorActorRepository.save(relations, 1);
                System.out.println("save new relations count: " + relations.size() + " actor id: " + a.getId() + " director id:" + d.getId());
            }
            s += rs.size();
        }

    }

    public static void addActorRelations() {
        //接下来就进行演员间关系的优化
        int s = 0, batchSize = 100;
        while(true){
            HashSet<Actor> batch = actorRepository.getBatch(s, batchSize);
            if(batch.size() == 0)
                break;
            for (Actor actor : batch) {
                ArrayList<ActorCollaborationRelation> relationsOfActor = new ArrayList<>(actor.getActorCollaborationRelations());
                HashMap<ActorCollaborationRelation, Integer> relations2Count = new HashMap<>();
                for (int i = 0; i < relationsOfActor.size(); i++) {
                    for (int j = i + 1; j < relationsOfActor.size(); j++) {
                        ActorCollaborationRelation r1 = relationsOfActor.get(i);
                        ActorCollaborationRelation r2 = relationsOfActor.get(j);
                        if(r1.same(r2)){
                            relations2Count.computeIfAbsent(r1,  (k) -> 0);
                            relations2Count.put(r1, relations2Count.get(r1) + 1);
                        }
                    }
                }
                List<ActorCollaborationRelation> newRelations = new ArrayList<>();
                relations2Count.forEach((r, c) -> {
                    ArrayList<ActorCollaborationRelation> relations = new ArrayList<>(r.getCount() - c);
                    for (int i = 0; i < (r.getCount() - c); i++) {
                        relations.add(new ActorCollaborationRelation(r.getActor1(), r.getActor2(), r.getCount()));
                    }
                    newRelations.addAll(relations);
                });
                actorCollaborationRepository.save(newRelations, 1);
            }
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

    private static boolean hasSameMovie(String title, List<Product> products){
        if(products == null)
            return false;
        for (Movie savedMovie : movieRepository.findMoviesByTitle(title)) {
            HashSet<String> s1 = products.stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));
            HashSet<String> s2 = savedMovie.getProducts().stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));
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
        //源数据没有清洗干净，有的数据productId会有重复
        for (int i = 0; i < products.size(); i++) {
            Product origin = products.get(i);
            Product p = productRepository.findByProductIdAndFormatAndPrice(origin.getProductId(), origin.getFormat(), origin.getPrice());
            if(p != null){
                products.set(i, p);
            }
        }
    }

    private static List<Product> removeDuplicatedProducts(List<Product> products){
        ArrayList<Product> newProducts = new ArrayList<>();
        for (Product product : products) {
            boolean found = false;
            for (Product newProduct : newProducts) {
                if(newProduct.getProductId() != null && newProduct.getProductId().equals(product.getProductId())){
                    if(newProduct.getFormat() == null && newProduct.getPrice() == null){
                        newProduct.setFormat(product.getFormat()); newProduct.setPrice(product.getPrice());
                    }
                    found = true;
                    break;
                }
            }
            if(!found)
                newProducts.add(product);
        }
        HashSet<String> ids = new HashSet<>();
        for (Product newProduct : newProducts) {
            ids.add(newProduct.getProductId());
        }
        assert ids.size() == newProducts.size();
        return newProducts;
    }
}
