package com.objectmentor.fitnesse.releases;

import fitnesse.FitNesseContext;
import fitnesse.http.MockRequest;
import fitnesse.http.MockResponseSender;
import fitnesse.http.Response;
import fitnesse.testutil.RegexTestCase;
import fitnesse.util.FileUtil;

public class DownloadResponderTest extends RegexTestCase {
  private DownloadResponder responder;

  public void setUp() throws Exception {
    responder = new DownloadResponder();
  }

  public void tearDown() throws Exception {
    FileUtil.deleteFileSystemDirectory("releases");
  }

  public void testParameters() throws Exception {
    MockRequest request = new MockRequest();
    request.addInput("release", "abc");
    request.setResource("xyz");

    responder.makeResponse(new FitNesseContext(), request);

    assertEquals("abc", responder.getReleaseName());
    assertEquals("xyz", responder.getFilename());
  }

  public void testDownloading() throws Exception {
    Response response = doSimpleDownload();

    MockResponseSender mockResponseSender = new MockResponseSender();
    mockResponseSender.doSending(response);
    String content = mockResponseSender.sentData();
    assertSubString("package one", content);
  }

  public void testDownloadWasRecorded() throws Exception {
    doSimpleDownload();
    Release release = new Release("xyz");
    ReleaseFile releaseFile = release.getFile("file1");
    assertEquals(1, releaseFile.downloads);
  }

  public void testMimeType() throws Exception {
    ReleaseTest.prepareReleaseWithFiles();
    FileUtil.createFile("releases/xyz/file3.jar", "jar file");
    FileUtil.createFile("releases/xyz/file4.zip", "zip file");
    MockRequest request = new MockRequest();
    request.addInput("release", "xyz");
    request.setResource("file3.jar");

    Response response = responder.makeResponse(new FitNesseContext(), request);
    assertEquals("application/x-java-archive", response.getContentType());

    request.setResource("file4.zip");
    response = responder.makeResponse(new FitNesseContext(), request);
    assertEquals("application/zip", response.getContentType());
  }

  private Response doSimpleDownload() throws Exception {
    ReleaseTest.prepareReleaseWithFiles();
    MockRequest request = new MockRequest();
    request.addInput("release", "xyz");
    request.setResource("file1");

    Response response = responder.makeResponse(new FitNesseContext(), request);
    return response;
  }

}
