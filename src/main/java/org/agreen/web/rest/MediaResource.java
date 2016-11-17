package org.agreen.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.agreen.domain.Media;
import org.agreen.repository.MediaRepository;
import org.agreen.service.util.Tools;
import org.agreen.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${client.root-directory}")
    private String rootDirectory;

    private Media createMedia(MultipartFile mediaFile, String albumURL) throws IOException{
    	Media media = new Media();
    	 String sep 		 = Tools.getSeparator();
     	  
	        String directory = env.getProperty("client.root-directory");  	      	  
			  
	      	String filename  =  Tools.removeSpaces(mediaFile.getOriginalFilename()).toLowerCase();  
	      	
	      	String albumPath = albumURL + sep + filename;
			  
	        String filepath  = Paths.get(directory, albumPath).toString();
	      
	        log.debug("REST request to save Media at filepath: {}", filepath);
	
	        media.setName(filename);
	        media.setSize(mediaFile.getSize());
	        media.setType(mediaFile.getContentType());
	        media.setUrl(albumPath);
	      
	        log.debug("REST request to save Media : {}", media);
	      
	        // Save the file locally
	        BufferedOutputStream stream =
	          new BufferedOutputStream(new FileOutputStream(new File(filepath)));	      
	        stream.write(mediaFile.getBytes());
	        stream.close();
    	return media;
    }

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
    	createMediaFile(@RequestParam("mediaFile") MultipartFile mediaFile, @RequestParam("gallery")String album) 
    		throws URISyntaxException{

	      log.debug("REST request to save MediaFile : {}, {}", album, mediaFile);
	      
	        String sep = Tools.getSeparator();
	        String year = Tools.getYear();
	        String month = Tools.getMonth();
	        String day = Tools.getDay();
	        String hour = Tools.getHour();
	        String name = Tools.removeSpaces(album);
	        String url = year + sep + month + sep + day + sep + name;
          
	      Media media;
		try {
			media = createMedia(mediaFile, url);
			Media result = mediaRepository.save(media);
	        return ResponseEntity.created(new URI("/api/media/" + result.getId()))
	            .headers(HeaderUtil.createEntityCreationAlert("media", result.getId()))
	            .body(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	      
	    	
    }

    /**
     * POST  /mediaFile : Create a new mediaFile.
     *
     * @param media the media to create
     * @return the ResponseEntity with status 201 (Created) and with body the new media, or with status 400 (Bad Request) if the media has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/media/files",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> 
    	createMediaFiles(@RequestParam("mediaFiles") MultipartFile[] mediaFiles, @RequestParam("gallery")String album) 
    		throws URISyntaxException{

	        String sep = Tools.getSeparator();
	        String year = Tools.getYear();
	        String month = Tools.getMonth();
	        String day = Tools.getDay();
	        String name = Tools.removeSpaces(album);
	        String url = year + sep + month + sep + day + sep + name;
	        Media result = new Media();
	    	for(MultipartFile mediaFile: mediaFiles){
	    			log.debug("REST request to save MediaFile : {}, {}", album, mediaFile);
	    			Media media;
					try {
						media = createMedia(mediaFile, url);
					} catch (IOException e) {
						e.printStackTrace();
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}	    	
					media.setDescription(album);
	    			result = mediaRepository.save(media);	         	
	    	}	   

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
     * GET  /media/find-by-name/:name : get the "name" media.
     *
     * @param name the name of the media to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the media, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/media/find-by-name/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Media> getMediaByName(@PathVariable String name) {
        log.debug("REST request to get Media : {}", name);
        Media media = mediaRepository.findByName(name);
        return Optional.ofNullable(media)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    

    /**
     * GET  /media/find-by-description/:description : get the "description" media.
     *
     * @param name the name of the media to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the media, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/media/find-by-description/{description}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Set<Media>> getMediaByDescription(@PathVariable String description) {
        log.debug("REST request to get Media : {}", description);
        Set<Media> media = mediaRepository.findByDescription(description);
        return Optional.ofNullable(media)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
