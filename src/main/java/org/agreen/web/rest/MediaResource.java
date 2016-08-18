package org.agreen.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.agreen.domain.Media;
import org.agreen.repository.MediaRepository;
import org.agreen.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Media.
 */
@RestController
@RequestMapping("/api")
public class MediaResource {

    private final Logger log = LoggerFactory.getLogger(MediaResource.class);
        
    @Inject
    private MediaRepository mediaRepository;
    
    @Inject
    private Environment env;
    

    /**
     * POST  /mediaFile : Create a new mediaFile.
     *
     * @param media the media to create
     * @return the ResponseEntity with status 201 (Created) and with body the new media, or with status 400 (Bad Request) if the media has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/media/file",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> 
    	createMediaFile(@RequestParam("mediaFile") MultipartFile mediaFile) 
    		throws URISyntaxException{

	      log.debug("REST request to save MediaFile : {}", mediaFile);	
    	
	      Media media = new Media();
	    	try{
	    		  String filename = mediaFile.getOriginalFilename();
	    	      String directory = env.getProperty("media.base");
	    	      String filepath = Paths.get(directory, filename).toString();

	    	      media.setName(filename);
	    	      media.setSize(mediaFile.getSize());
	    	      media.setType(media.getType());
	    	      
	    	      log.debug("REST request to save Media : {}", media);
	    	      
	    	      // Save the file locally
	    	      BufferedOutputStream stream =
	    	          new BufferedOutputStream(new FileOutputStream(new File(filepath)));
	    	      
	    	      stream.write(mediaFile.getBytes());
	    	      stream.close();
	    	      
	    	}catch(Exception e){
	    		log.debug(e.getMessage(), e.getCause());
	    	}
	    	
	    	Media result = mediaRepository.save(media);
	        return ResponseEntity.created(new URI("/api/media/" + result.getId()))
	            .headers(HeaderUtil.createEntityCreationAlert("media", result.getId().toString()))
	            .body(result);
    }
    
    /**
     * POST  /media : Create a new media.
     *
     * @param media the media to create
     * @return the ResponseEntity with status 201 (Created) and with body the new media, or with status 400 (Bad Request) if the media has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/media",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> createMedia(@RequestBody Media media) throws URISyntaxException {
        log.debug("REST request to save Media : {}", media);
        if (media.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("media", "idexists", "A new media cannot already have an ID")).body(null);
        }
        Media result = mediaRepository.save(media);
        return ResponseEntity.created(new URI("/api/media/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("media", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /media : Updates an existing media.
     *
     * @param media the media to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated media,
     * or with status 400 (Bad Request) if the media is not valid,
     * or with status 500 (Internal Server Error) if the media couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/media",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> updateMedia(@RequestBody Media media, @RequestParam("mediaFile") MultipartFile mediaFile) throws URISyntaxException {
        log.debug("REST request to update Media : {}", media);
        if (media.getId() == null) {
            return createMedia(media);
        }
        Media result = mediaRepository.save(media);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("media", media.getId().toString()))
            .body(result);
    }

    /**
     * GET  /media : get all the media.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of media in body
     */
    @RequestMapping(value = "/media",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Media> getAllMedia() {
        log.debug("REST request to get all Media");
        List<Media> media = mediaRepository.findAll();
        return media;
    }

    /**
     * GET  /media/:id : get the "id" media.
     *
     * @param id the id of the media to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the media, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/media/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> getMedia(@PathVariable String id) {
        log.debug("REST request to get Media : {}", id);
        Media media = mediaRepository.findOne(id);
        return Optional.ofNullable(media)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /media/:id : delete the "id" media.
     *
     * @param id the id of the media to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/media/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMedia(@PathVariable String id) {
        log.debug("REST request to delete Media : {}", id);
        mediaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("media", id.toString())).build();
    }

}
