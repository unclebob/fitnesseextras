package com.objectmentor.wikimail;

import fitnesse.components.CommandLine;
import fitnesse.components.SaveRecorder;
import fitnesse.http.RequestBuilder;
import fitnesse.http.Response;
import fitnesse.http.ResponseParser;
import fitnesse.wikitext.widgets.WikiWordWidget;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Postmaster {
  public PrintStream output;
  public String host;
  public int port;
  public String username;
  public String password;
  public int exitCode = 0;

  public Postmaster() {
    output = System.out;
  }

  public void args(String[] args) {
    CommandLine commandLine = new CommandLine("[-a username password] host port"
    );
    if (commandLine.parse(args)) {
      host = commandLine.getArgument("host");
      port = Integer.parseInt(commandLine.getArgument("port"));
      if (commandLine.hasOption("a")) {
        username = commandLine.getOptionArgument("a", "username");
        password = commandLine.getOptionArgument("a", "password");
      }
    } else
      usage();
  }

  private static void usage() {
    System.err.println(
      "Usage: java com.objectmentor.wikimail.Postmaster [options] host port"
    );
    System.err.println("\t-a <username> <password>");
    System.exit(1);
  }

  public void deliver(Message message) throws Exception {
    String pageName = message.getSubject();
    if (!WikiWordWidget.isWikiWord(pageName)) {
      pageName = "WikiMail.BadSubject" + makeTimeStamp();
      output.println(
        "The subject is not a wikiword.  Saving as " + pageName + "."
      );
    }

    RequestBuilder request = makeRequest(pageName, "raw");
    ResponseParser response = performRequest(request);

    int status = response.getStatus();
    if (status == 200)
      addMessageToPage(pageName, message, response.getBody());
    else if (status == 404)
      addMessageToPage(pageName, message, "");
    else {
      output.println("\tThe page " + pageName + " could not be retreived. (" +
        status + " " + Response.getReasonPhrase(status) + ")"
      );
      exitCode = status;
    }
  }

  private RequestBuilder makeRequest(String pageName, String responderType)
    throws Exception {
    RequestBuilder request = new RequestBuilder("/" + pageName);
    if (usingAuthentication())
      request.addCredentials(username, password);
    request.addInput("responder", responderType);
    return request;
  }

  private void addMessageToPage(
    String pageName, Message message, String content
  ) throws Exception {
    content += "\n----";
    String from = message.getFrom();
    content += "!meta Emailed by " + from + " ." + makeDateString() + "\n";
    content += message.getBody() + "\n";

    RequestBuilder request = makeRequest(pageName, "saveData");
    request.setMethod("POST");
    request.addInput("saveId", SaveRecorder.newIdNumber() + "");
    request.addInput("ticketId", SaveRecorder.newTicket() + "");
    request.addInput("pageContent", content);
    ResponseParser response = performRequest(request);
    int status = response.getStatus();
    if (status >= 400) {
      output.println("\tThe page could not be saved. (" + status + " " +
        Response.getReasonPhrase(status) + ")"
      );
      exitCode = status;
    } else
      output.println("\tMessage delivered successfully.");

  }

  public Message getMessage(InputStream input) throws Exception {
    StringBuffer buffer = new StringBuffer();
    int c = 0;
    while ((c = input.read()) != -1)
      buffer.append((char) c);

    EmailParser parser = new EmailParser();
    return parser.parse(buffer.toString());
  }

  public static void main(String[] args) throws Exception {
    Postmaster pm = new Postmaster();
    pm.run(args);
    System.exit(pm.exitCode);
  }

  private ResponseParser performRequest(RequestBuilder request)
    throws Exception {
    Socket s = new Socket(host, port);
    OutputStream output = s.getOutputStream();
    output.write(request.getText().getBytes());
    InputStream input = s.getInputStream();
    ResponseParser response = new ResponseParser(input);
    output.close();
    input.close();
    s.close();

    return response;
  }

  private void run(String[] args) throws Exception {
    args(args);
    output.println("Attemping delivery. (" + makeDateString() + ")");
    try {
      Message message = getMessage(System.in);
      output.println("\tFrom:    " + message.getFrom());
      output.println("\tSubject: " + message.getSubject());
      deliver(message);
    }
    catch (Throwable e) {
      output.println("\t" + e);
      output.println("\tDelivery aborted due to Exception.");
      exitCode = 1;
      e.printStackTrace();
    }
  }

  public boolean usingAuthentication() {
    return username != null;
  }

  public static String makeDateString() {
    return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss").format(new Date());
  }

  public static String makeTimeStamp() {
    return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
  }
}
