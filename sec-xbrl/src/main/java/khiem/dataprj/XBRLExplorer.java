package khiem.dataprj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.PathType;
import org.jruby.embed.ScriptingContainer;

public class XBRLExplorer {
  
  private static int total = 0;
  private static Object lock = new Object();
  
  private static int THREAD_NUMBER = 100;
  private static int TICKER_NUMBER;
  private static int OFFSET = 0;
  
  private static final String NASDAQ = "/home/khiem/Downloads/NASDAQ.txt";
  private static final String NYSE = "/home/khiem/Downloads/NYSE.txt";
  
  private static List<String> tickerSymbols = new ArrayList<String>();
  private static ScriptingContainer container = new ScriptingContainer(LocalContextScope.CONCURRENT);
  private static Object receiver;
  
  private static Thread[] threads = new Thread[THREAD_NUMBER];
  private static List<String> failed = new ArrayList<String>();
  
  public static void main(String[] args) throws IOException, InterruptedException {
    String basepath = System.getProperty("user.dir");
    container.runScriptlet("ENV['GEM_PATH'] = '" + basepath + "/lib/jruby'");
    receiver = container.runScriptlet(PathType.ABSOLUTE, basepath + "/xbrldemo.rb");
    
    boolean reachToEnd = false; 
    
    // read the Ticker symbols from files, push to tickerSymbols list
    BufferedReader reader = new BufferedReader(new FileReader(NYSE));
    String l;
    reader.readLine();
    while ((l = reader.readLine()) != null) {
      String ticker = l.split("\t")[0];
      tickerSymbols.add(ticker);
    }
    reader.close();
    
    TICKER_NUMBER = tickerSymbols.size();
    
    // create some threads, each thread will fetch a range of ticker symbols xbrl data
    int average = (int) (TICKER_NUMBER / THREAD_NUMBER) + 1;
    for (int i = 0; i < THREAD_NUMBER; i++) {
      int start = OFFSET + i * average;
      int end = OFFSET + (i+1) * average - 1;
      if (end > OFFSET + TICKER_NUMBER - 1) end = OFFSET + TICKER_NUMBER - 1;
      if (end >= tickerSymbols.size() - 1) end = tickerSymbols.size() - 1;
      threads[i] = new Thread(new XBRLRunner(start, end));
      try {
        threads[i].start();
      } catch (Exception e) {
        System.err.format("Exception occurs when starting sub-thread %d, from ticker %d to %d%n", i, start, end);
        e.printStackTrace();
        synchronized (failed) {
          for (int j = start; j <= end; j++) { 
            if (!failed.contains(tickerSymbols.get(j))) failed.add(tickerSymbols.get(j));
          }
        }
      }
      if (end == tickerSymbols.size() - 1) {
        reachToEnd = true;
        break;
      }
    }
    // waits for all sub-thread to finish
    for (int i = 0; i < THREAD_NUMBER; i++) {
      if (threads[i] != null) threads[i].join();
    }
    
    // for all failed ticker symbols
    System.out.format("Main thread: Fetch all from failed list%n");
    for (int i = 0; i < failed.size(); i++) {
      container.callMethod(receiver, "search", failed.get(i), "10-K");
    }
    
    if (reachToEnd) System.out.println("REACH TO END OF FILE");
    System.out.println("The total XBRL report is " + total);
  }
  
  static class XBRLRunner implements Runnable {
    int start;
    int end;
    
    public XBRLRunner(int start, int end) {
      this.start = start;
      this.end = end;
    }
    
    @Override
    public void run() {
      System.out.format("%s start to fetch data from %d to %d%n", Thread.currentThread().getName(), start, end);
      
      for (int i = start; i <= end; i++) {
        System.out.format("%s: Fetching data for %s%n", Thread.currentThread().getName(), tickerSymbols.get(i));
        try {
          synchronized(lock) {
            total += fetch(tickerSymbols.get(i));
          }         
        } catch (Exception e) {
          System.err.format("%s: Failed due to this exception %s at ticker %s%n", Thread.currentThread().getName(), e.getMessage(), tickerSymbols.get(i));
          e.printStackTrace();
          synchronized (failed) {
            if (!failed.contains(tickerSymbols.get(i))) failed.add(tickerSymbols.get(i));
          }
          continue;
        }
      }
      System.out.format("%s: FINISHED!%n", Thread.currentThread().getName());
    }
    
    private int fetch(String ticker) {
      // call to jruby redbridge
      return Integer.valueOf(container.callMethod(receiver, "count_company", ticker, "10-K").toString());
    }
    
  }
}
