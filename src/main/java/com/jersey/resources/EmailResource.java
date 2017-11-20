package com.jersey.resources;

import com.jersey.representations.Email;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.cloudinary.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class EmailResource {

    private static final Logger log = LogManager.getLogger(EmailResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("secure/emails")
    public org.json.simple.JSONObject sendEmail(@Valid Email email) throws FileNotFoundException {

        if (!email.token.equalsIgnoreCase("Palsplate2017")) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        else{

            ClientResponse response = null;
            EmailResource emailResource = new EmailResource();
            String html = null;

            if(email.type.equalsIgnoreCase("signup_successful") && email.locale.equalsIgnoreCase("en")){
                html = emailResource.htmlIntoString("en_signup_successful.html");
            }

            if(email.type.equalsIgnoreCase("signup_successful") && email.locale.equalsIgnoreCase("de")){
                html = emailResource.htmlIntoString("de_signup_successful.html");
            }

            if(email.type.equalsIgnoreCase("reservation_cook") && email.locale.equalsIgnoreCase("en")){
                html = emailResource.htmlIntoString("en_reservation_cook.html");
            }

            if(email.type.equalsIgnoreCase("reservation_cook") && email.locale.equalsIgnoreCase("de")){
                html = emailResource.htmlIntoString("de_signup_successful.html");
            }

            if(email.type.equalsIgnoreCase("reservation_customer") && email.locale.equalsIgnoreCase("en")){
                html = emailResource.htmlIntoString("en_reservation_customer.html");
            }

            if(email.type.equalsIgnoreCase("reservation_customer") && email.locale.equalsIgnoreCase("de")){
                html = emailResource.htmlIntoString("de_signup_successful.html");
            }

            response = emailResource.sendComplexMessage(
                    html,
                    email.subject,
                    email.recipientEmail,
                    email.recipientName,
                    email.reservation_id,
                    email.person_id,
                    email.foodName,
                    email.foodPrice,
                    email.foodOfferStart,
                    email.foodOfferStart);

            org.json.simple.JSONObject emailResponse = new org.json.simple.JSONObject();
            emailResponse.put("response date", response.getResponseDate());
            emailResponse.put("response status", response.getStatus());

            return emailResponse;
        }
    }

    public ClientResponse sendComplexMessage(String html,
                                             String subject,
                                             String recipientEmail,
                                             String recipientName,
                                             String reservation_id,
                                             String person_id,
                                             String foodName,
                                             String foodPrice,
                                             String foodOfferStart,
                                             String foodOfferStop) {

        JSONObject recipientVariableJson = new JSONObject();
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", recipientName);
        nameObject.put("reservation_id", reservation_id);
        nameObject.put("person_id", person_id);
        nameObject.put("foodName", foodName);
        nameObject.put("foodPrice", foodPrice);
        nameObject.put("foodOfferStart", foodOfferStart);
        nameObject.put("foodOfferStop", foodOfferStop);
        nameObject.put("reservationUrl", "https://www.palsplate.com/reservations/" + reservation_id);
        nameObject.put("personUrl", "https://www.palsplate.com/user/" + person_id);

        recipientVariableJson.put(recipientEmail, nameObject);
        System.out.println(recipientVariableJson.toString());

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", System.getenv().get("MAILGUN_APIKEY")));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/mg.palsplate.com/messages");

        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("from", "Palsplate UG <info@" + "mg.palsplate.com" + ">");
        formData.field("to", recipientEmail);
        formData.field("subject", subject.toString());
        formData.field("html", html);
        formData.field("recipient-variables", recipientVariableJson.toString());

        ClientResponse clientResponse = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, formData);
        log.info(clientResponse.toString());

        return clientResponse;
    }

    public String htmlIntoString(String file) throws FileNotFoundException {

        String content = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            BufferedReader in = new BufferedReader(new FileReader(new File(classLoader.getResource(file).getFile())));
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
