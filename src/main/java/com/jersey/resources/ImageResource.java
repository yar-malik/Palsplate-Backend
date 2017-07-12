package com.jersey.resources;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.jersey.config.SqlInitialization;
import com.jersey.persistence.ImageDao;
import com.jersey.representations.Image;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import java.util.Map;


@Path("/images")
@Component
@Transactional
public class ImageResource {

    private static final Logger log = LogManager.getLogger(SqlInitialization.class);

    private final ImageDao imageDao;

    @Inject
    public ImageResource(ImageDao imageDao) {
        this.imageDao = imageDao;
    }


    @GET
    public List<Image> getAll(){
        return this.imageDao.findAll();
    }

//    @GET
//    @Path("{id}/foods")
//    public Image getAllFoodsForCook(@PathParam("id")long id) {
//        Image image = imageDao.findOne(id);
//        if (image == null) {
//            throw new WebApplicationException((Response.Status.NOT_FOUND));
//        }
//
//        image.getData();
//        return image;
//    }

    @GET
    @Path("{id}")
    public Image getCook(@PathParam("id")long id) {
        Image image = imageDao.findOne(id);
        if (image == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return image;
    }

    /**
     * Create new Image
     * @param image
     * @return new image
     */
//    @POST
//    public Image save(@Valid Image image) {
//        return imageDao.save(image);
//    }

    /**
     * Update existing Image
     * @param id
     * @param image
     * @return updated image
     */
    @PUT
    @Path("{id}")
    public Image update(@PathParam("id")long id, @Valid Image image) {
        if(imageDao.findOne(id) == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            image.setId(id);
            return imageDao.save(image);
        }
    }

    /**
     * Delete image
     * @param id
     */
    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id")long id) {
        Image image = imageDao.findOne(id);
        if(image == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            imageDao.delete(image);
        }
    }
    
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Image uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        log.info("uploadedInputStream: " + uploadedInputStream);
        log.info("fileDetail: " + fileDetail);
        log.info("fileDetail.getName: " + fileDetail.getName());
        log.info("fileDetail.getFileName: " + fileDetail.getFileName());
        log.info("fileDetail.getSize: " + fileDetail.getSize());
        log.info("fileDetail.getType: " + fileDetail.getType());

        File myfile = inputStream2file(uploadedInputStream, fileDetail.getFileName(), fileDetail.getType());

        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "palsplate",
                "api_key", "816138784777145",
                "api_secret", "tA4kTPJ029PlpmCb7drT-_7RHUM"));

        Map uploadResult = cloudinary.uploader().upload(myfile, ObjectUtils.emptyMap());

        log.info("cloudinary result: " + uploadResult);
        log.info("cloudinary secure_url: " + uploadResult.get("secure_url"));
        log.info("cloudinary public_id: " + uploadResult.get("public_id"));
        log.info("cloudinary original_filename: " + uploadResult.get("original_filename"));

        Image image = new Image();
        image.setFilename(fileDetail.getFileName());
        image.setFood_id(Long.valueOf(1));
        image.setCloudinary_public_id(uploadResult.get("public_id").toString());

//        long imageEntiryId = imageDao.save(image).getId();
//        log.info("Image id: " + imageEntiryId);

        return imageDao.save(image);
    }

    public static File inputStream2file (InputStream in, String filename, String suffix) throws IOException {
            final File tempFile = File.createTempFile(filename, suffix);
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(in, out);
            }
            return tempFile;
    }
}