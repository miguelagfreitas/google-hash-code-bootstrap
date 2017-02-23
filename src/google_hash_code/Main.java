package google_hash_code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Miguel Freitas
 */
public class Main {

    //
    public static ArrayList<EndPoint> epList;
    public static ArrayList<Cache> cacheList;
    public static ArrayList<Video> vList;
    public static DataCenter dc;
    public static ArrayList<Request> rList;

    public static final String EXAMPLE_FILE = "kittens.in";
    public static final String SMALL_FILE = "me_at_the_zoo.in";
    public static final String MEDIUM_FILE = "trending_today.in";
    public static final String BIG_FILE = "videos_worth_spreading.in";

    public static BufferedReader br = null;
    public static FileReader fr = null;

    public static String[] files = null;

    public static void main(String[] args) {

        files = new String[1];

        files[0] = SMALL_FILE;
//        files[1] = MEDIUM_FILE;
//        files[2] = BIG_FILE;
//        files[3] = EXAMPLE_FILE;

        for (String inputFile : files) {

            String currentLine = "";

            try {

                fr = new FileReader(inputFile);
                br = new BufferedReader(fr);

                // currentLine = br.readLine();
                //Initialize data from first line here
                epList = new ArrayList<>();
                cacheList = new ArrayList<>();
                vList = new ArrayList<>();
                dc = new DataCenter();
                rList = new ArrayList<>();

                int videoCount;
                int endPointCount;
                int requestDescriptionCount;
                int cacheCount;
                int cacheSize;

                currentLine = br.readLine();
               // System.out.println(currentLine);
                String[] setUpData = currentLine.split(" ");
                videoCount = Integer.parseInt(setUpData[0]);
                endPointCount = Integer.parseInt(setUpData[1]);
                requestDescriptionCount = Integer.parseInt(setUpData[2]);
                cacheCount = Integer.parseInt(setUpData[3]);
                cacheSize = Integer.parseInt(setUpData[4]);

                currentLine = br.readLine();
                //System.out.println(currentLine);
                String[] videoSizes_s = currentLine.split(" ");
                int[] videoSizes = new int[videoCount];

                for (int i = 0; i < videoSizes.length; i++) {
                    videoSizes[i] = Integer.parseInt(videoSizes_s[i]);
                }

                for (int i = 0; i < cacheCount; i++) {
                    cacheList.add(new Cache(i, cacheSize));
                }

                for (int i = 0; i < videoCount; i++) {
                    Video v = new Video(i, videoSizes[i]);
                    dc.addVideo(v);
                }

                for (int i = 0; i < endPointCount; i++) {
                    
                    currentLine = br.readLine();

                    EndPoint ep = new EndPoint(i, null, Integer.parseInt(currentLine.split(" ")[0]));
                    int epCacheCount = Integer.parseInt(currentLine.split(" ")[1]);
                    if (epCacheCount> 0) {
                        int count = epCacheCount;
                        while (count > 0) {
                            count--;
                            currentLine = br.readLine();
                            ep.pingList = new int[cacheCount];
                            for (int j = 0; j < cacheCount; j++) {
                                ep.pingList[j] = 0;
                            }
                            int cacheId  = Integer.parseInt(currentLine.split(" ")[0]);
                            int ms  = Integer.parseInt(currentLine.split(" ")[0]);
                            ep.pingList[cacheId] = ms;
                        }
                        
                    }
                    epList.add(ep);
                }

                for (int i = 0; i < requestDescriptionCount; i++) {
                    int endPoint;
                    int video;
                    int requestCount;
                    
                    currentLine = br.readLine();

                    endPoint = Integer.parseInt(currentLine.split(" ")[1]);
                    video = Integer.parseInt(currentLine.split(" ")[0]);
                    requestCount = Integer.parseInt(currentLine.split(" ")[2]);

                    rList.add(new Request(requestCount, dc.videoList.get(video), epList.get(endPoint)));

                }

//                Results results = new Results();
//
//                Algorithm a = new Algorithm();
//                a.algorithm(results);
//
//                System.out.println(results.getScore());

            } catch (IOException e) {
                System.out.println("Could not find file");
            }
        }

    }

    /*Algorithm class*/
    public static class Algorithm implements HashCodeAlgorithm {

        @Override
        public void algorithm(ResultList r) {
            
            
            
            
            
        }

    }

    /*Result list class*/
    public static class Results extends ResultList {

        @Override
        public long getScore() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public ArrayList getResults() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public static class Request {

        public int quantity;
        public Video requestedVideo;
        public EndPoint destinationEndPoint;

        public Request(int quantity, Video v, EndPoint ep) {
            this.quantity = quantity;
            this.requestedVideo = v;
            this.destinationEndPoint = ep;
        }

    }

    public static class EndPoint {

        public int id;
        public int[] pingList;
        public int datacenterPing;

        public EndPoint(int id, int[] pingList, int datacenterPing) {
            this.id = id;
            this.pingList = pingList;
            this.datacenterPing = datacenterPing;
        }
    }

    public static class Video {

        int id;
        int mb;

        public Video(int id, int mb) {
            this.id = id;
            this.mb = mb;
        }
    }

    public static class Cache {

        public int id;
        public int size;
        public ArrayList<Video> videoList;

        public Cache(int id, int size) {
            this.id = id;
            this.videoList = new ArrayList<>();
        }

        public void addVideo(Video v) {
            this.videoList.add(v);
        }
    }

    public static class DataCenter {

        public ArrayList<Video> videoList;

        public DataCenter() {
            this.videoList = new ArrayList<>();
        }

        public void addVideo(Video v) {
            this.videoList.add(v);
        }
    }
}
