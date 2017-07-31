package com.jersey.resources;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;


@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class EmailResource {

    private static final Logger log = LogManager.getLogger(EmailResource.class);

    @GET
    @Path("public/email")
    public String sendEmail(@QueryParam("to") String to,
                     @QueryParam("emailType") String emailType) throws FileNotFoundException {

        ClientResponse response = null;
        EmailResource emailResource = new EmailResource();

        if(emailType.equalsIgnoreCase("signup")){

            String html = emailResource.htmlIntoString("index.html");
            response = emailResource.sendComplexMessage(html);
        }

        return response.toString();
    }

    public ClientResponse sendComplexMessage(String html) {

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", "key-d1d1af3caff7b163e3860c9cc90f47ea"));

        WebResource webResource = client.resource("https://api.mailgun.net/v3/" + "mg.palsplate.com" + "/messages");

        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("from", "Mailgun User <mailgun@" + "mg.palsplate.com" + ">");
        formData.field("to", "malikasfandyarashraf@gmail.com");
        formData.field("subject", "Yar Bas kar");
        formData.field("html", html);
        ClassLoader classLoader = getClass().getClassLoader();

        ClientResponse clientResponse = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formData);
        log.info(clientResponse.toString());

        return clientResponse;
    }

    public String htmlIntoString(String file) throws FileNotFoundException {

        String content = "";
        try {

            ClassLoader classLoader = getClass().getClassLoader();

            BufferedReader in = new BufferedReader(new FileReader(new File(classLoader.getResource("index.html").getFile())));
            String str;
            while ((str = in.readLine()) != null) {
                content += str;
            }
            in.close();
        } catch (IOException e) {
        }

        return content;
    }
}
