package com.objectmentor.fitnesse.releases;

import fitnesse.util.FileUtil;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Release {
  private static final File releaseHome = new File("releases");

  private File releaseDir;
  private HashMap releaseFiles;
  private File infoFile;

  public Release(String name) throws Exception {
    releaseDir = new File(releaseHome, name);
    infoFile = new File(releaseDir, ".releaseInfo");
    releaseFiles = new HashMap();
    if (exists())
      load();
  }

  public boolean exists() {
    return releaseDir.exists();
  }

  private synchronized void load() throws Exception {
    loadRecordedFiles();
    loadLocalFiles();
  }

  private void loadLocalFiles() {
    File[] files = releaseDir.listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      String filename = file.getName();
      if (!releaseFiles.containsKey(filename) && !filename.startsWith(".") &&
        !file.isDirectory()) {
        releaseFiles.put(filename, new ReleaseFile(file.getAbsolutePath()));
      }
    }
  }

  private void loadRecordedFiles() throws Exception {
    if (infoFile.exists()) {
      String info = FileUtil.getFileContent(infoFile);
      String[] rows = info.split("\n");
      for (int i = 0; i < rows.length; i++) {
        ReleaseFile releaseFile = ReleaseFile.parse(
          releaseDir.getAbsolutePath(), rows[i]
        );
        if (releaseFile.exists())
          releaseFiles.put(releaseFile.getFilename(), releaseFile);
      }
    }
  }

  public int fileCount() {
    return releaseFiles.size();
  }

  public void saveInfo() throws Exception {
    FileWriter writer = new FileWriter(infoFile);
    for (Iterator iterator = getFiles().iterator(); iterator.hasNext();)
      writer.write(iterator.next().toString() + "\n");
    writer.close();
  }

  public List getFiles() {
    LinkedList files = new LinkedList(releaseFiles.values());
    Collections.sort(files);
    return files;
  }

  public ReleaseFile getFile(String filename) {
    return (ReleaseFile) releaseFiles.get(filename);
  }

  public boolean isCorrupted() throws Exception {
    if (infoFile == null)
      return true;
    else if (!infoFile.exists())
      return true;
    else if (FileUtil.getFileContent(infoFile).equals(("")) ||
      FileUtil.getFileContent(infoFile).equals((FileUtil.ENDL)))
      return true;

    return false;
  }
}
