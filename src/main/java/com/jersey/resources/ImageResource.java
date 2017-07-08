package com.jersey.resources;

import com.jersey.persistence.ImageDao;
import com.jersey.representations.Image;
import org.apache.commons.io.IOUtils;
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

@Path("/images")
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Transactional
public class ImageResource {
    private final ImageDao imageDao;

    @Inject
    public ImageResource(ImageDao imageDao) {
        this.imageDao = imageDao;
    }


    @GET
    public List<Image> getAll(){
        return this.imageDao.findAll();
    }

    @GET
    @Path("{id}")
    public Image getImage(@PathParam("id")long id) {
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
    @POST
    public Image save(@Valid Image image) {
        return imageDao.save(image);
    }


    /**
     * Upload an Image
     * @param
     * @return
     */
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Image uploadFile(@Valid Image image,
                             @FormDataParam("file") InputStream uploadedInputStream,
                             @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

            image.setData(IOUtils.toByteArray(uploadedInputStream));
            image.setFilename(fileDetail.getFileName());

            System.out.println("File uploaded Successfully");

            return imageDao.save(image);
    }

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
}