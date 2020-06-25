package com.codflix.backend.features.contact;

import com.codflix.backend.core.Conf;
import com.codflix.backend.core.Template;
import com.codflix.backend.models.Media;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactControler {
    public String contact(Request request, Response res) {

        HashMap<String, List<Media>> media=new HashMap();
        Map<String, Object> model = new HashMap<>();
        model.put("media", media);

        return Template.render("contact.html", model);
    }
}
