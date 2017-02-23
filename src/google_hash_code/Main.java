package google_hash_code;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        //files[0] = SMALL_FILE;
        //files[0] = MEDIUM_FILE;
        //files[2] = BIG_FILE;
        files[0] = EXAMPLE_FILE;

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

                 Results results = new Results();
//
                Algorithm a = new Algorithm();
                a.algorithm(results);
//
//                System.out.println(results.getScore());


                PrintWriter pw = new PrintWriter(new FileWriter(inputFile+".out"));
                int usedCacheServers = 0;
                for (Cache c :cacheList)
                {
                    if (!c.videoList.isEmpty()){
                        usedCacheServers++;
                    }
                }
                pw.write(usedCacheServers+"\n");

                for (Cache c : cacheList)
                {
                    if (!c.videoList.isEmpty()){
                        pw.write(c.id+" ");
                        for (Video v :
                          c.videoList)
                        {
                            pw.write(v.id + " ");
                        }
                        pw.write("\n");
                    }
                }


                pw.close();
            } catch (IOException e) {
                System.out.println("Could not find file");
            }
        }

    }

    /*Algorithm class*/
    public static class Algorithm implements HashCodeAlgorithm {





        @Override
        public void algorithm(ResultList r) {


            Map<Cache, ArrayList<EndPoint>> map = new HashMap<>();
            for (Cache c :
              cacheList)
            {
                ArrayList<EndPoint> connectedEps = new ArrayList<>();
                for (EndPoint ep :
                  epList)
                {
                    if (ep.isConnectedTo(c)){
                        connectedEps.add(ep);
                    }
                }
                map.put(c, connectedEps);
            }

            Map<Cache, int[]> requestVectorPerCache = new HashMap<>();

            for (Cache c :
              map.keySet())
            {
                int[] requestsPerVideo = new int[dc.videoList.size()];
                for (int i = 0;i<requestsPerVideo.length;i++){
                    requestsPerVideo[i] = 0;
                }
                for (Request request: rList)
                {
                    if (map.get(c).contains(request.destinationEndPoint)){
                        requestsPerVideo[request.requestedVideo.id]+=request.quantity;
                    }
                }
                requestVectorPerCache.put(c, requestsPerVideo);
            }
            for (Cache c :
              requestVectorPerCache.keySet())
            {
                requestVectorPerCache.replace(c, bubbleSort(requestVectorPerCache.get(c)));
                for (int i = 0; i < requestVectorPerCache.get(c).length; i++)
                {
                    int videoId = requestVectorPerCache.get(c)[i];
                    c.addVideo(dc.videoList.get(videoId));

                }
            }


        }

        private static int[]  bubbleSort(int[] intArray) {

            int[] orderedIndexes = new int[intArray.length];
            for (int x = 0; x<intArray.length; x++){
                orderedIndexes[x] = x;
            }
            int n = intArray.length;
            int temp = 0;
            int temp_1 = 0;

            for(int i=0; i < n; i++){
                for(int j=1; j < (n-i); j++){

                    if(intArray[j-1] > intArray[j]){
                        //swap the elements!
                        temp = intArray[j-1];
                        temp_1 = orderedIndexes[j-1];
                        intArray[j-1] = intArray[j];
                        orderedIndexes[j-1] = orderedIndexes[j];
                        intArray[j] = temp;
                        orderedIndexes[j] = temp_1;
                    }

                }
            }
            return orderedIndexes;
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

        public boolean isConnectedTo(Cache c){
            return pingList[c.id] > 0;
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

        public int              id;
        public int              size;
        public ArrayList<Video> videoList;

        public int occupiedSize;

        public Cache(int id, int size) {
            this.id = id;
            this.videoList = new ArrayList<>();
            occupiedSize = 0;
            this.size = size;
        }

        public boolean addVideo(Video v) {


            if (occupiedSize + v.mb <= size){


                this.videoList.add(v);
                occupiedSize+=v.mb;
                return true;
            }else{
                return false;
            }
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
