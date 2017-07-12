package com.jersey.resources;

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
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException {

        Image image = new Image();
        System.out.println("image.getID " + image.getId());
        log.info("image.getID " + image.getId());
        System.out.println("uploadedInputStream: " + uploadedInputStream);
        System.out.println("fileDetail: " + fileDetail);
        System.out.println("fileDetail.getName: " + fileDetail.getName());
        System.out.println("fileDetail.getFileName: " + fileDetail.getFileName());
        System.out.println("fileDetail.getSize: " + fileDetail.getSize());
        System.out.println("fileDetail.getType: " + fileDetail.getType());

        String uploadedFileLocation = "src/main/resources/images/" + fileDetail.getFileName();

        System.out.println("File Path: " + uploadedFileLocation + " \n");
        log.info("File Path: " + uploadedFileLocation);

        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation + " \n";

        image.setFilename(fileDetail.getFileName());
        image.setFileLocation(image.getId() + "_" + uploadedFileLocation);
        image.setFood_id(Long.valueOf(1));

        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        image.setData(bytes);

        imageDao.save(image);
//        long imageEntiryId = imageDao.save(image).getId();
//        log.info("Image id: " + imageEntiryId);

        return Response.status(200).entity(output).build();

    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void writeToDB(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}