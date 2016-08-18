package org.agreen.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.agreen.domain.Media;
import org.agreen.web.rest.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.ning.http.util.Base64;

@RestController
@RequestMapping("/api/gridfs")
public class GridFileResource {

  private final GridFsTemplate gridFsTemplate;

  @Autowired
  public GridFileResource(GridFsTemplate gridFsTemplate) {
    this.gridFsTemplate = gridFsTemplate;
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Media> createOrUpdate(@RequestParam("file") MultipartFile file) throws URISyntaxException {
    String name = file.getOriginalFilename();
    try {
      Optional<GridFSDBFile> existing = maybeLoadFile(name);
      if (existing.isPresent()) {
        gridFsTemplate.delete(getFilenameQuery(name));
      }
      DBObject metaData = new BasicDBObject();
      //More metaData entries here
      GridFSFile savedFile = gridFsTemplate.store(file.getInputStream(), name, file.getContentType(), metaData);
      Media media = new Media();
      media.setId(savedFile.getId().toString());
      media.setName(name);
      media.setSize(file.getSize());
      media.setType(file.getContentType());
      
      return ResponseEntity.created(new URI("/api/gridfs/" + name))
              .headers(HeaderUtil.createEntityCreationAlert("filemedia", name))
              .body(media);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody List<String> list() {
    return getFiles().stream()
        .map(GridFSDBFile::getFilename)
        .collect(Collectors.toList());
  }

  @RequestMapping(path = "/{name:.+}", method = RequestMethod.GET)
  public ResponseEntity<Media> get(@PathVariable("name") String name) {
      try {
        Optional<GridFSDBFile> optionalCreated = maybeLoadFile(name);
        if (optionalCreated.isPresent()) {
          GridFSDBFile created = optionalCreated.get();
          ByteArrayOutputStream os = new ByteArrayOutputStream();
          created.writeTo(os);
          Media media = new Media();
          media.setBytes(os.toByteArray());
          media.setContent(Base64.encode(os.toByteArray()));
          media.setName(created.getFilename());
          media.setId(created.getId().toString());
          HttpHeaders headers = new HttpHeaders();
          headers.add(HttpHeaders.CONTENT_TYPE, created.getContentType());
          return Optional.ofNullable(media)
                  .map(result -> new ResponseEntity<>(
                      result,
                      HttpStatus.OK))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
      } catch (IOException e) {
        return new ResponseEntity<>(HttpStatus.IM_USED);
      }
  }

  private List<GridFSDBFile> getFiles() {
    return gridFsTemplate.find(null);
  }

  private Optional<GridFSDBFile> maybeLoadFile(String name) {
    GridFSDBFile file = gridFsTemplate.findOne(getFilenameQuery(name));
    return Optional.ofNullable(file);
  }

  private static Query getFilenameQuery(String name) {
    return Query.query(GridFsCriteria.whereFilename().is(name));
  }
}