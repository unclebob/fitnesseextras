package com.objectmentor.fitnesse.releases;


import util.RegexTestCase;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReleaseTest extends RegexTestCase {
  public void setUp() throws Exception {
  }

  public void tearDown() throws Exception {
    FileUtil.deleteFileSystemDirectory("releases");
  }

  public void testCreationNoDirectory() throws Exception {
    Release release = new Release("xyz");
    assertFalse(release.exists());
  }

  public void testCreationWithDirectory() throws Exception {
    makeReleaseDir();
    Release release = new Release("xyz");
    assertTrue(release.exists());
    release.saveInfo();
    assertTrue(new File("releases/xyz/.releaseInfo").exists());
  }

  public void testReleaseFiles() throws Exception {
    Release release = prepareReleaseWithFiles();
    assertEquals(2, release.fileCount());
    List files = release.getFiles();
    assertEquals(2, files.size());

    Object o = files.get(0);
    assertEquals(ReleaseFile.class, o.getClass());
    ReleaseFile releaseFile = (ReleaseFile) o;
    assertEquals("file1", releaseFile.getFilename());

    o = files.get(1);
    assertEquals(ReleaseFile.class, o.getClass());
    releaseFile = (ReleaseFile) o;
    assertEquals("file2", releaseFile.getFilename());
  }

  public void testReleaseInfo() throws Exception {
    Release release = prepareReleaseWithFiles();
    release.saveInfo();
    String info = FileUtil.getFileContent("releases/xyz/.releaseInfo");
    assertSubString("file1", info);
    assertSubString("file2", info);
  }

  public void testLoadingWithReleaseInfoAndExtraFile() throws Exception {
    Release release = prepareReleaseWithFiles();
    release.saveInfo();
    FileUtil.createFile("releases/xyz/file3", "package three");
    release = new Release("xyz");

    assertEquals(3, release.fileCount());
    List files = release.getFiles();
    assertEquals(3, files.size());

    Object o = files.get(2);
    assertEquals(ReleaseFile.class, o.getClass());
    ReleaseFile releaseFile = (ReleaseFile) o;
    assertEquals("file3", releaseFile.getFilename());
  }

  public void testLoadingWithChangedFile() throws Exception {
    Release release = prepareReleaseWithFiles();
    ReleaseFile originalFile1 = (ReleaseFile) release.getFiles().get(0);
    release.saveInfo();
    Thread.sleep(1000);
    FileUtil.createFile("releases/xyz/file1",
      "with even more content than before"
    );
    release = new Release("xyz");
    ReleaseFile newFile1 = (ReleaseFile) release.getFiles().get(0);

    assertTrue(newFile1.size > originalFile1.size);
  }

  public void testLoadingWithMissingFile() throws Exception {
    Release release = prepareReleaseWithFiles();
    release.saveInfo();
    FileUtil.deleteFile("releases/xyz/file1");

    release = new Release("xyz");
    assertEquals(1, release.fileCount());
    assertEquals("file2", ((ReleaseFile) release.getFiles().get(0
    )).getFilename()
    );
  }

  public void testNotCorruptedReleaseInfo() throws Exception {
    Release release = prepareReleaseWithFiles();
    release.saveInfo();
    assertFalse(release.isCorrupted());
  }

  public void testReleaseInfoFileFailure() throws Exception {
    Release release = prepareReleaseWithFiles();
    release.saveInfo();
    List l = new ArrayList();
    l.add("");
    FileUtil.writeLinesToFile("releases/xyz/.releaseInfo", l);
    assertTrue(release.isCorrupted());
  }

  public static Release prepareReleaseWithFiles() throws Exception {
    makeReleaseDir();
    FileUtil.createFile("releases/xyz/file1", "package one");
    FileUtil.createFile("releases/xyz/file2", "package two");
    Release release = new Release("xyz");
    return release;
  }

  private static void makeReleaseDir() {
    FileUtil.makeDir("releases");
    FileUtil.makeDir("releases/xyz");
  }

}
