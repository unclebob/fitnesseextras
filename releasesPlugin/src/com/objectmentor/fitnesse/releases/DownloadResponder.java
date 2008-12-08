package com.objectmentor.fitnesse.releases;

import fitnesse.FitNesseContext;
import fitnesse.Responder;
import fitnesse.http.InputStreamResponse;
import fitnesse.http.Request;
import fitnesse.http.Response;
import fitnesse.responders.ErrorResponder;
import fitnesse.responders.files.FileResponder;

import java.io.InputStream;

public class DownloadResponder implements Responder {
  private Release release;
  private String filename;
  private String releaseName;

  public Response makeResponse(FitNesseContext fitNesseContext, Request request)
    throws Exception {
    releaseName = (String) request.getInput("release");
    release = new Release(releaseName);
    filename = request.getResource();

    if (!release.exists())
      return new ErrorResponder("There is no release named " + releaseName
      ).makeResponse(fitNesseContext, request);
    ReleaseFile releaseFile = release.getFile(filename);
    if (releaseFile == null)
      return new ErrorResponder(
        "The " + releaseName + " doesn't have a file named " + filename
      ).makeResponse(fitNesseContext, request);

    InputStreamResponse response = new InputStreamResponse();
    response.setContentType(FileResponder.getContentType(filename));
    InputStream input = releaseFile.getDownloadStream();
    response.setBody(input, (int) releaseFile.getByteCount());
    release.saveInfo();
    return response;
  }

  public String getReleaseName() {
    return releaseName;
  }

  public String getFilename() {
    return filename;
  }
}
