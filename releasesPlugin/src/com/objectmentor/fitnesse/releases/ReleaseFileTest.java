package com.objectmentor.fitnesse.releases;

import junit.framework.TestCase;

import java.io.File;

import util.FileUtil;

public class ReleaseFileTest extends TestCase {
  private File file;
  private long actualLastModified;
  private long actualSize;

  public void setUp() throws Exception {
    FileUtil.createFile("testfile.txt", "some text");
    file = new File("testfile.txt");
    actualLastModified = file.lastModified();
    actualSize = file.length();
  }

  public void tearDown() throws Exception {
    FileUtil.deleteFile("testfile.txt");
  }

  public void testConstruction() throws Exception {
    ReleaseFile releaseFile = new ReleaseFile("testfile.txt", 123,
      actualLastModified, 10
    );
    assertEquals("testfile.txt", releaseFile.filename);
    assertEquals(123, releaseFile.size);
    assertEquals(actualLastModified, releaseFile.lastModified);
    assertEquals(10, releaseFile.downloads);
  }

  public void testSizeGetsUpdatedIfModificationTimeIsDifferent()
    throws Exception {
    ReleaseFile releaseFile = new ReleaseFile("testfile.txt", 123, 1234567890,
      10
    );
    assertEquals("testfile.txt", releaseFile.filename);
    assertEquals(actualSize, releaseFile.size);
    assertEquals(actualLastModified, releaseFile.lastModified);
    assertEquals(10, releaseFile.downloads);
  }

  public void testCreationWithJustFilename() throws Exception {
    ReleaseFile releaseFile = new ReleaseFile("testfile.txt");
    assertEquals("testfile.txt", releaseFile.filename);
    assertEquals(actualSize, releaseFile.size);
    assertEquals(actualLastModified, releaseFile.lastModified);
    assertEquals(0, releaseFile.downloads);
  }

  public void testGetters() throws Exception {
    ReleaseFile releaseFile = new ReleaseFile("testfile.txt");
    assertEquals("testfile.txt", releaseFile.getFilename());

    releaseFile.size = 12345;
    assertEquals("12345 bytes", releaseFile.getSize());

    releaseFile.downloads = 34;
    assertEquals("34", releaseFile.getDownloads());
  }

  public void testToString() throws Exception {
    ReleaseFile releaseFile = new ReleaseFile("testfile.txt");
    releaseFile.size = 123;
    releaseFile.lastModified = 876543210;
    releaseFile.downloads = 321;

    assertEquals("testfile.txt\t123\t876543210\t321", releaseFile.toString());
  }

  public void testParse() throws Exception {
    ReleaseFile releaseFile = ReleaseFile.parse(".",
      "testfile.txt\t123\t" + actualLastModified + "\t321"
    );
    assertEquals("./testfile.txt", releaseFile.filename);
    assertEquals(123, releaseFile.size);
    assertEquals(321, releaseFile.downloads);
    assertEquals(actualLastModified, releaseFile.lastModified);
  }
}
