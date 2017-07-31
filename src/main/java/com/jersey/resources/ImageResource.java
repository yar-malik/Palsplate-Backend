package com.jersey.resources;

import com.jersey.persistence.ImageDao;
import com.jersey.representations.Image;
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
import java.io.InputStream;
import java.util.List;


@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class ImageResource {

    private static final Logger log = LogManager.getLogger(ImageResource.class);

    private final ImageDao imageDao;

    @Inject
    public ImageResource(ImageDao imageDao) {
        this.imageDao = imageDao;
    }

    @GET
    @Path("public/images")
    public List<Image> getAll(){
        return this.imageDao.findAll();
    }

    @GET
    @Path("public/images/{id}")
    public Image getCook(@PathParam("id")long id) {
        Image image = imageDao.findOne(id);
        if (image == null) {
            throw new WebApplicationException((Response.Status.NOT_FOUND));
        }
        return image;
    }

    /**
     * Update existing Image
     * @param id
     * @param image
     * @return updated image
     */
    @PUT
    @Path("secure/images/{id}")
    public Image update(@FormDataParam("file") InputStream uploadedInputStream,
                        @FormDataParam("file") FormDataContentDisposition fileDetail,
                        @PathParam("id")long id, @Valid Image image) {
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
    @Path("secure/images/{id}")
    public void delete(@PathParam("id")long id) {
        Image image = imageDao.findOne(id);
        if(image == null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }else {
            imageDao.delete(image);
        }
    }
}