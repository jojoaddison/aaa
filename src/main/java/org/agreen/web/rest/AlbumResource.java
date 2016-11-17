package org.agreen.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.agreen.domain.Album;
import org.agreen.domain.Media;
import org.agreen.repository.AlbumRepository;
import org.agreen.service.util.Tools;
import org.agreen.web.rest.util.HeaderUtil;
import org.agreen.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing Album.
 */
@RestController
@RequestMapping("/api")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);
        
    @Inject
    private AlbumRepository albumRepository;
    
    @Value("${client.root-directory}")
    private String rootDirectory;

    @Inject
    private Environment env;
    

    private Media createMedia(MultipartFile mediaFile, String albumURL){
    	Media media = new Media();
			try{
				
		        String sep 		 = Tools.getSeparator();
		      	  
		        String directory = env.getProperty("client.root-directory");  	      	  
				  
		      	String filename  = albumURL + sep + Tools.removeSpaces(mediaFile.getOriginalFilename()).toLowerCase();  
				  
		        String filepath  = Paths.get(directory, filename).toString();
		      
		        log.debug("REST request to save Media at filepath: {}", filepath);
		
		        media.setName(filename);
		        media.setSize(mediaFile.getSize());
		        media.setType(mediaFile.getContentType());
		        media.setUrl(filepath);
		      
		        log.debug("REST request to save Media : {}", media);
		      
		        // Save the file locally
		        BufferedOutputStream stream =
		          new BufferedOutputStream(new FileOutputStream(new File(filepath)));
		      
		        stream.write(mediaFile.getBytes());
		        stream.close();
			      
			}catch(Exception e){
	    		log.debug(e.getMessage(), e.getCause());
	    		return null;
			}
    	return media;
    }
    
    /**
     * POST  /albums : Create a new album.
     *
     * @param album the album to create
     * @return the ResponseEntity with status 201 (Created) and with body the new album, or with status 400 (Bad Request) if the album has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> createAlbum(@RequestBody Album album, 
    										@RequestParam("mediaFiles") MultipartFile[] mediaFiles) 
    												throws URISyntaxException {
        log.debug("REST request to save Album : {}", album);
        if (album.getId() != null) {
            return updateAlbum(album, mediaFiles);
        }
        String sep = Tools.getSeparator();
        String year = Tools.getYear();
        String month = Tools.getMonth();
        String day = Tools.getDay();
        String hour = Tools.getHour();
        String name = Tools.removeSpaces(album.getName());
        String url = rootDirectory + sep + year + sep + month + sep + day + sep + hour + sep + name;
        album.setUrl(url);
        album.setCreatedDate(ZonedDateTime.now());
        album.setModifiedDate(ZonedDateTime.now());
        Set<Media> photos = new HashSet<>();
        for(MultipartFile file: mediaFiles)photos.add(createMedia(file, url));
        album.setPhotos(photos);
        Album result = albumRepository.save(album);
        return ResponseEntity.created(new URI("/api/albums/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("album", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /albums : Updates an existing album.
     *
     * @param album the album to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated album,
     * or with status 400 (Bad Request) if the album is not valid,
     * or with status 500 (Internal Server Error) if the album couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> updateAlbum(@RequestBody Album album, @RequestParam("mediaFiles") MultipartFile[] mediaFiles) throws URISyntaxException {
        log.debug("REST request to update Album : {}", album);
        if (album.getId() == null) {
            return createAlbum(album, mediaFiles);
        }
        album.setModifiedDate(ZonedDateTime.now());
        Album result = albumRepository.save(album);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("album", album.getId().toString()))
            .body(result);
    }

    /**
     * GET  /albums : get all the albums.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of albums in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/albums",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Album>> getAllAlbums(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Albums");
        Page<Album> page = albumRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/albums");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /albums/:id : get the "id" album.
     *
     * @param id the id of the album to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the album, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Album> getAlbum(@PathVariable String id) {
        log.debug("REST request to get Album : {}", id);
        Album album = albumRepository.findOne(id);
        return Optional.ofNullable(album)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /albums/:id : delete the "id" album.
     *
     * @param id the id of the album to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/albums/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) {
        log.debug("REST request to delete Album : {}", id);
        albumRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("album", id.toString())).build();
    }

}
