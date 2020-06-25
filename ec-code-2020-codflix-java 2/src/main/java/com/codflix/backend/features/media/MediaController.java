package com.codflix.backend.features.media;

import com.codflix.backend.core.Template;
import com.codflix.backend.features.genre.GenreDao;
import com.codflix.backend.models.Genre;
import com.codflix.backend.models.Media;
import spark.Request;
import spark.Response;
import java.text.ParseException;
import java.util.*;

public class MediaController {

    private final MediaDao mediaDao = new MediaDao();
    private final GenreDao genreDao = new GenreDao();

    public String list(Request request, Response response) throws ParseException {
        List<Media> medias;



        // create list genres,types , year
        List<Genre> genres = genreDao.getAllGenres();
        List<String> genresString = new ArrayList<>();
        List<String> types ;
        List<String> years ;

        String title = request.queryParams("titl");


        if (title != null && !title.isEmpty()) {
            medias = mediaDao.filterMedias(title);
        } else {
            medias = mediaDao.getAllMedias();
        }


        types = createMylist(medias,true);
        years = createMylist(medias,false);
        HashMap<String,List<Media>> mediafilterType =  classification(types,medias,true);
        HashMap<String,List<Media>> mediafilterDate = classification(years,medias,false);
        HashMap<String,List<Media>> mediafilterGenre = classificationgenre(genres,medias);


        Map<String, Object> model = new HashMap<>();

        for (int i = 0; i <genres.size() ; i++) {
            genresString.add(genres.get(i).getName().toLowerCase());
            System.out.println(genres.get(i).getName());
        }



        model.put("medias", medias);
        model.put("genres", genresString);
        model.put("types", types);
        model.put("years", years);

        for (String key : mediafilterType.keySet()) {
            model.put(key.toLowerCase(), mediafilterType.get(key));
            //System.out.println("key vaut : "+key);
            //System.out.println("list vaut : "+mediafilterGenre.get(key));
        }

        for (String key : mediafilterDate.keySet()) {
            model.put(key.toLowerCase(), mediafilterDate.get(key));
            //System.out.println("key vaut : "+key);
            //System.out.println("list vaut : "+mediafilterGenre.get(key));
        }
        for (String key : mediafilterGenre.keySet()) {
            model.put(key.toLowerCase(), mediafilterGenre.get(key));
            //System.out.println("key vaut : "+key);
            //System.out.println("list vaut : "+mediafilterGenre.get(key));
        }

        System.out.println(model);
        return Template.render("media_list.html", model);
    }


    private HashMap<String, List<Media>> classificationgenre(List<Genre> genres, List<Media> medias) {

        HashMap<String,List<Media>> filtergenre=new HashMap();
        List<Media> listmedia = new ArrayList<>();


        for (int i = 0; i <genres.size() ; i++) {
            filtergenre.put(genres.get(i).getName(),new ArrayList<>());
            if(filtergenre.get(genres.get(i).getName()).size()==0){

                for (int j = 0; j <medias.size() ; j++) {

                    if (medias.get(j).getGenreId()==genres.get(i).getId()){

                        filtergenre.get(genres.get(i).getName()).add(medias.get(j));

                    }
                }
            }else if(filtergenre.get(genres.get(i).getName()).size()!=0){
                filtergenre.get(genres.get(i).getName()).clear();
                filtergenre.put(genres.get(i).getName(),new ArrayList<>());

                for (int j = 0; j <medias.size() ; j++) {

                    if (medias.get(j).getGenreId()==genres.get(i).getId()){

                        filtergenre.get(genres.get(i).getName()).add(medias.get(j));

                    }
                }
            }



            //listmedia.clear();
        }
        return filtergenre;
    }



    public String detail(Request request, Response res) {
        int id = Integer.parseInt(request.params(":id"));
        Media media = mediaDao.getMediaById(id);

        Map<String, Object> model = new HashMap<>();
        model.put("media", media);

        return Template.render("media_detail.html", model);
    }



    public List createMylist(List<Media> medias,Boolean what) throws ParseException {

        List<String> take =new ArrayList();
        List wanted = new ArrayList();

        // check what true for type false for date
        if(what) {
            for (int i = 0; i < medias.size(); i++) {
                take.add(medias.get(i).getType().toLowerCase());
            }
        }else {
            for (int i = 0; i < medias.size(); i++) {
                take.add(dateofString(medias.get(i).getReleaseDate()));
            }
        }

        for (int i = 0; i <take.size() ; i++) {

            if(!wanted.contains(take.get(i))){
                wanted.add(take.get(i));
            }

        }


        return wanted;
    }

    public String mediaGenre(Request request, Response res){

        int id = Integer.parseInt(request.params(":id"));
        Media media = mediaDao.getMediaById(id);

        Map<String, Object> model = new HashMap<>();
        model.put("media", media);

        return Template.render("media_list.html", model);
    }

    public HashMap<String,List<Media>> classification(List<String> types,List<Media> medias,boolean value) throws ParseException {

        HashMap<String,List<Media>> filtergenre=new HashMap();
        List<Media> listmedia = new ArrayList<>();

        // condition need 2 option true false , true for media.gettype() false for media.gooddate()
        if (value){
            for (int i = 0; i <types.size() ; i++) {
                listmedia.clear();
                filtergenre.put(types.get(i),listmedia);
                for (int j = 0; j <medias.size() ; j++) {

                    if (medias.get(j).getType().equals(types.get(i))){
                        listmedia.add(medias.get(j));
                    }

                }
                filtergenre.put(types.get(i),listmedia);

            }
            return filtergenre;
        }else {
            for (int j = 0; j <types.size() ; j++) {
                listmedia.clear();
                filtergenre.put(types.get(j),listmedia);
                for (int x = 0; x <medias.size() ; x++) {

                    if (dateofString(medias.get(j).getReleaseDate()).equals(types.get(j))){
                        listmedia.add(medias.get(x));
                    }

                }
                filtergenre.put(types.get(j),listmedia);

            }
            return filtergenre;
        }
    }

    // return string date format
    public String dateofString(Date ReleaseDate){
        Date date = ReleaseDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int y = calendar.get(Calendar.YEAR);
        String text ="day : "+d +" mouth :"+m+" year : "+y;
        return  text.toLowerCase();
    }
}
