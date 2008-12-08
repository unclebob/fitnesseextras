package com.objectmentor.fitnesse.releases;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ReleaseFile implements Comparable {
  public String filename;
  public long size;
  public long lastModified;
  public int downloads = 0;
  private File file;

  public ReleaseFile(
    String filename, long size, long lastModified, int downloads
  ) {
    this.filename = filename;
    file = new File(filename);
    this.size = size;
    this.lastModified = lastModified;
    this.downloads = downloads;
    if (hasChanged())
      update();
  }

  public ReleaseFile(String filename) {
    this.filename = filename;
    file = new File(filename);
    update();
  }

  private boolean hasChanged() {
    return lastModified != file.lastModified();
  }

  public boolean exists() {
    return file.exists();
  }

  private void update() {
    lastModified = file.lastModified();
    size = file.length();
  }

  public String getFilename() {
    return file.getName();
  }

  public String getSize() {
    return size + " bytes";
  }

  public String getDownloads() {
    return downloads + "";
  }

  public String toString() {
    return file.getName() + "\t" + size + "\t" + lastModified + "\t" +
      downloads;
  }

  public static ReleaseFile parse(String contextPath, String text) {
    String[] tokens = text.split("\t");
    String filename = contextPath + "/" + tokens[0];
    long size = Long.parseLong(tokens[1]);
    long lastModified = Long.parseLong(tokens[2]);
    int downloads = Integer.parseInt(tokens[3]);

    return new ReleaseFile(filename, size, lastModified, downloads);
  }

  public int compareTo(Object o) {
    if (o instanceof ReleaseFile) {
      ReleaseFile that = (ReleaseFile) o;
      return this.getFilename().compareTo(that.getFilename());
    } else
      return 1;
  }

  public InputStream getDownloadStream() throws Exception {
    downloads++;
    return new FileInputStream(file);
  }

  public long getByteCount() {
    return size;
  }
}
