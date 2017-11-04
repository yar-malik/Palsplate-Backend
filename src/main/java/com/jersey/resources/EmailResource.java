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
import java.io.*;


@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class EmailResource {

    private static final Logger log = LogManager.getLogger(EmailResource.class);

//    @GET
//    @Path("secure/email")
//    public String sendEmail(
//                    @QueryParam("subject") String subject,
//                    @QueryParam("recipient") String recipient,
//                    @QueryParam("emailType") String emailType,
//                    @QueryParam("name") String name) throws FileNotFoundException {
//
//        System.out.println("emailType: " + emailType);
//        System.out.println("subject: " + subject);
//        System.out.println("recipient: " + recipient);
//        System.out.println("name: " + name);
//
//        ClientResponse response = null;
//        EmailResource emailResource = new EmailResource();
//        String html = null;
//
//        if(emailType.equalsIgnoreCase("sign_up")){
//             html = emailResource.htmlIntoString("sign_up.html");
//        }
//
//        if(emailType.equalsIgnoreCase("food_uploaded")){
//            html = emailResource.htmlIntoString("food_uploaded.html");
//        }
//
//        if(emailType.equalsIgnoreCase("reservation_accepted")){
//            html = emailResource.htmlIntoString("reservation_accepted.html");
//        }
//
//        if(emailType.equalsIgnoreCase("reservation_declined")){
//            html = emailResource.htmlIntoString("reservation_declined.html");
//        }
//
//        if(emailType.equalsIgnoreCase("reservation_requested")){
//            html = emailResource.htmlIntoString("reservation_requested.html");
//        }
//
//        if(emailType.equalsIgnoreCase("sign_up_successful")){
//            html = emailResource.htmlIntoString("sign_up_successful.html");
//        }
//
//        response = emailResource.sendComplexMessage(html, subject, recipient, name);
//
//        return response.toString();
//
//    }

    @POST
    @Path("secure/emails")
    public String sendEmail(@Valid Email email) throws FileNotFoundException {

        System.out.println("emailType: " + email.type);
        System.out.println("subject: " + email.subject);
        System.out.println("recipient: " + email.recipient);
        System.out.println("name: " + email.name);

        ClientResponse response = null;
        EmailResource emailResource = new EmailResource();
        String html = null;

        if(email.type.equalsIgnoreCase("sign_up")){
            html = emailResource.htmlIntoString("sign_up.html");
        }

        if(email.type.equalsIgnoreCase("food_uploaded")){
            html = emailResource.htmlIntoString("food_uploaded.html");
        }

        if(email.type.equalsIgnoreCase("reservation_accepted")){
            html = emailResource.htmlIntoString("reservation_accepted.html");
        }

        if(email.type.equalsIgnoreCase("reservation_declined")){
            html = emailResource.htmlIntoString("reservation_declined.html");
        }

        if(email.type.equalsIgnoreCase("reservation_requested")){
            html = emailResource.htmlIntoString("reservation_requested.html");
        }

        if(email.type.equalsIgnoreCase("sign_up_successful")){
            html = emailResource.htmlIntoString("sign_up_successful.html");
        }

        response = emailResource.sendComplexMessage(html, email.subject, email.recipient, email.name);

        return response.toString();

    }


    public ClientResponse sendComplexMessage(String html, String subject, String recipient, String name) {

        JSONObject recipientVariableJson = new JSONObject();
        JSONObject nameObject = new JSONObject();
        nameObject.put("name", name);
        recipientVariableJson.put(recipient, nameObject);

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api", System.getenv().get("MAILGUN_APIKEY")));
        WebResource webResource = client.resource("https://api.mailgun.net/v3/mg.palsplate.com/messages");

        FormDataMultiPart formData = new FormDataMultiPart();
        formData.field("from", "Palsplate UG <info@" + "mg.palsplate.com" + ">");
        formData.field("to", recipient);
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
