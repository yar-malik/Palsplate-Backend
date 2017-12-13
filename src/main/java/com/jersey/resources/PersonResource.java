package com.jersey.resources;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jersey.Authorization.security.Authorities;
import com.jersey.Authorization.security.PasswordResetToken;
import com.jersey.Authorization.security.PasswordResetTokenStore;
import com.jersey.persistence.PersonDao;
import com.jersey.representations.Person;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;


@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
@Component
public class PersonResource {

    private static final Logger log = LogManager.getLogger(PersonResource.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Resource(name = "defaultAuthorizationServerTokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Resource(name="consumerTokenServices")
    private ConsumerTokenServices consumerTokenServices;

    @Value("${authentication.oauth.clientid}")
    private String localClientID;

    @Value("${backend.url}")
    private String backendURL;

    @Value("${frontend.url}")
    private String frontendURL;

    private PersonDao personDao;

    @Inject
    public PersonResource(PersonDao personDao)
    {
        this.personDao = personDao;
    }

    /**
     * Get all Users
     *
     * @return persons
     */
    @GET
    @Path("secure/persons")
//    @PreAuthorize("hasPermission('PersonResource', 'ROLE_ADMIN')")
    public List<Person> getAll(@QueryParam("page") Integer page,
                               @QueryParam("size") Integer size,
                               @QueryParam("sort") List<String> sort) {

        if(page == null && size == null){
            return this.personDao.findAll();
        }

        if(page == null){
            page = new Integer(0);
        }

        if(size == null){
            size = new Integer(3);
        }

        List<Sort.Order> orders = new ArrayList<>();

        for (String propOrder: sort) {

            String[] propOrderSplit = propOrder.split(",");
            String property = propOrderSplit[0];

            if (propOrderSplit.length == 1) {
                orders.add(new Sort.Order(property));
            } else {
                Sort.Direction direction
                        = Sort.Direction.fromStringOrNull(propOrderSplit[1]);
                orders.add(new Sort.Order(direction, property));
            }
        }

        Pageable pageable = new PageRequest(page, size, orders.isEmpty() ? null : new Sort(orders));

        List<Person> persons = this.personDao.findAll(pageable).getContent();
        List<Person> personsSafe = new ArrayList<>();

        for(Person person: persons){
            personsSafe.add(CopyPersonSafe(person));
        }

        return personsSafe;
    }

    /**
     * Get single Person
     *
     * @param id
     * @return person
     */
    @GET
    @Path("secure/persons/{id}")
//    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_USER,ROLE_ADMIN')")
    public Person getOne(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            person.getPhotoName();
            person.getPhotoPublicId();
            return CopyPersonSafe(person);
        }
    }

    @GET
    @Path("secure/persons/currentuser")
    public Person getPersonViaAccessToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return CopyPersonSafe(personDao.findByEmail(email));
    }

    @GET
    @Path("secure/persons/{id}/location_persons")
    public Person getLocationForPerson(@PathParam("id")long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }

        person.getLocationPerson().size();
        return person;
    }

    /**
     * Create new Person
     *
     * @param person
     * @return new person
     */
    @POST
    @Path("public/persons")
    public Person save(@Valid Person person) {

        person.setPassword(passwordEncoder.encode(person.getPassword()));

        return personDao.save(person);
    }

    /**
     * Create a Person Photo
     * @param uploadedInputStream
     * @param fileDetail
     * @param person_id
     * @return new Photo for a specific person
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("public/persons/{person_id}/photo")
    public Person uploadPhoto(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @PathParam("person_id")long person_id) throws IOException {

        log.info("fileDetail: " + fileDetail);
        log.info("fileDetail.getName: " + fileDetail.getName());
        log.info("fileDetail.getFileName: " + fileDetail.getFileName());

        Person person = personDao.findOne(person_id);

        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        else {

            File myfile = inputStream2file(uploadedInputStream, fileDetail.getFileName(), fileDetail.getType());
            Map uploadResult = cloudinary.uploader().upload(myfile, ObjectUtils.emptyMap());

            log.info("cloudinary secure_url: " + uploadResult.get("secure_url"));
            log.info("cloudinary public_id: " + uploadResult.get("public_id"));
            log.info("cloudinary original_filename: " + uploadResult.get("original_filename"));

            person.setPhotoName(fileDetail.getFileName());
            person.setPhotoPublicId(uploadResult.get("public_id").toString());

            return personDao.save(person);
        }
    }

    /**
     * Update existing Person
     *
     * @param id
     * @param
     * @return updated person
     */
    @PUT
    @Path("secure/persons/{id}")
//    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_USER,ROLE_ADMIN')")
    public Person update(@PathParam("id") long id, Person newPerson) {

        Person person = personDao.findOne(id);

        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            return person.updatePerson(newPerson);
        }
    }

    /**
     * Delete person
     *
     * @param id
     */
    @DELETE
    @Path("secure/persons/{id}")
//    @PreAuthorize("hasPermission(#id, 'PersonResource', 'ROLE_ADMIN')")
    public void delete(@PathParam("id") long id) {
        Person person = personDao.findOne(id);
        if (person == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            personDao.delete(person);
        }
    }

    /**
     * Creates a reset token and sends email to the user with the reset password link
     * @param payload
     * @return
     * @throws FileNotFoundException
     */
    @POST
    @Path("public/persons/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> payload) throws FileNotFoundException{

        String Email = (String)payload.get("email");
        String locale = (String)payload.get("locale");

        if(Email == null || locale == null){
            throw new InvalidParameterException("The parameters email and/or locale are malformed!");
        }

        Person person = personDao.findByEmail(Email);

        if(person == null){
            throw new UsernameNotFoundException("No registered account exists with this email address!");
        }

        if(person.getRoles().contains(Authorities.ROLE_USER_FACEBOOK.name())){
            return new ResponseEntity<Object>("The account associated with this email was signed up via facebook!",HttpStatus.BAD_REQUEST);
        }

        //create a new one time use only reset token
        OAuth2Request request = new OAuth2Request(null, localClientID, Arrays.asList(new SimpleGrantedAuthority(Authorities.CHANGE_PASSWORD_PRIVILEGE.name())), true, null, null, null, null, null);
        OAuth2Authentication oAuth2Authentication =  new OAuth2Authentication(request, new UsernamePasswordAuthenticationToken(Email, "N/A", Arrays.asList(new SimpleGrantedAuthority(Authorities.CHANGE_PASSWORD_PRIVILEGE.name()))));
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        String resetToken = oAuth2AccessToken.getValue();
        PasswordResetToken token = PasswordResetTokenStore.getInstance().getTokenFromEmail(Email);

        if( token != null){
            //update the token if already exists, this will revoke the previous token associated
            PasswordResetTokenStore.getInstance().updateResetToken(Email, resetToken);
        }
        else{
            PasswordResetTokenStore.getInstance().addToken(Email, resetToken);
        }

        String emailTemplate = null;

        if(locale.equalsIgnoreCase("en")){
            emailTemplate = "en_reset_password.html";
        }
        else if(locale.equalsIgnoreCase("de")){
            emailTemplate = "de_reset_password.html";
        }

        String redirectURL = new StringBuilder(backendURL)
                            .append("/api/public/persons/changepassword")
                            .append("?")
                            .append("password_reset_token=").append(resetToken).append("&")
                            .append("user_email=").append(person.getEmail())
                            .toString();

        String html = EmailResource.htmlIntoString(emailTemplate);
        html = html.replace("%password.resetlink%",redirectURL);

        //send the email to the user
        ClientResponse response = EmailResource.sendComplexMessage("Reset your password",
                                                                   person.getEmail(),
                                                        person.getFirstName()+ " " + person.getLastName(),
                                                              "Palsplate UG <info@mg.palsplate.com>",
                                                                   html);

        JSONObject emailResponse = new JSONObject();
        emailResponse.put("email sent date", response.getResponseDate());
        emailResponse.put("email response status", response.getStatus());

        return new ResponseEntity<>(emailResponse.toJSONString(), HttpStatus.OK);
    }

    /**
     * Checks the validity of the reset token and redirects to the new password form on validation success
     * @param email
     * @param resetToken
     * @return
     */
    @GET
    @Path("public/persons/changepassword")
    public void showChangePasswordPage(@QueryParam("user_email") String email, @QueryParam("password_reset_token") String resetToken){

        Person person = personDao.findByEmail(email);

        if(person == null){
            throw new UsernameNotFoundException("No account associated with this email exists!");
        }

        if(!PasswordResetTokenStore.getInstance().isValid(person, resetToken)){
            throw new InvalidParameterException("The reset token is either invalid or expired!");
        }

        PasswordResetToken token = PasswordResetTokenStore.getInstance().getTokenFromEmail(email);

        String redirectURL = new StringBuilder(frontendURL)
                            .append("/").append("reset_password").append("?")
                            .append("reset_token=").append(resetToken)
                            .toString();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = ((ServletRequestAttributes)requestAttributes).getResponse();

        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Updates the password for the user, can only be accessed by a user with CHANGE_PASSWORD_PRIVELIGE
     * @param payload
     * @return
     */
    @POST
    @Path("savepassword")
    public ResponseEntity<?> savePassword(@RequestBody Map<String, Object> payload){

        String newPassword = (String)payload.get("new_password");

        if(newPassword == null){
            throw new InvalidParameterException("The value of the new password is null!");
        }

        String email =  (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Person person = personDao.findByEmail(email);
        newPassword = passwordEncoder.encode(newPassword);
        person.setPassword(newPassword);

        //remove the access token from the PasswordResetTokenStore and from the current security context
        //because the token is for one time use only
        PasswordResetToken token = PasswordResetTokenStore.getInstance().getTokenFromEmail(email);
        consumerTokenServices.revokeToken(token.getResetToken());
        PasswordResetTokenStore.getInstance().removeToken(token.getResetToken());

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * Copies the person object without password and other security impacting information
     * @return Copied person object
     */
    private Person CopyPersonSafe(Person person){

        Person newPerson = new Person();

        newPerson.setId(person.getId());
        newPerson.setEmail(person.getEmail());
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());
        newPerson.setGender(person.getGender());
        newPerson.setPhoneNumber(person.getPhoneNumber());
        newPerson.setDescription(person.getDescription());
        newPerson.setIsPhotoPublic(person.getIsPhotoPublic());
        newPerson.setCook(person.getCook());
        newPerson.setCustomer(person.getCustomer());
        newPerson.setPhotoName(person.getPhotoName());
        newPerson.setPhotoPublicId(person.getPhotoPublicId());
        newPerson.setRoles(person.getRoles());
        newPerson.setLocationPerson(person.getLocationPerson());

        return  newPerson;
    }

    public File inputStream2file (InputStream in, String filename, String suffix) throws IOException {
        final File tempFile = File.createTempFile(filename, suffix);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}