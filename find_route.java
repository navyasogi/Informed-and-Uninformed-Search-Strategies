/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;

/**
 *
 * @author navyasogi
 */
public class find_route {
	
	public static int generatedNodes;
	public static int memoryNodes = 1;
	public static int expandedNodes;
	
	public static String targetCity;
	private static int costAccumulated;
	
	public static FileReader inputFileReader;
    public static FileReader hueristicFileReader;
    
    public static BufferedReader inputBuffer;
    public static BufferedReader hueristicBuffer;

    public static final String inputEnded = "END OF INPUT";
    
    public static File inputPathFile;
    public static File heuristicFile;

    private static boolean heuristic = true;
    
    public static ArrayList<PlaceDetails> cityInfoFringe = new ArrayList<PlaceDetails>();
    public static ArrayList<String> visitedCityNames = new ArrayList<String>();
    public static ArrayList<PlaceDetails> visitedCityData = new ArrayList<PlaceDetails>();
    public static ArrayList<PlaceDetails> inputCityData = new ArrayList<PlaceDetails>();
    public static HashMap<String, Integer> heuristicData = new HashMap<String, Integer>();

    public static void main(String[] args) {
        try {
           	String inputFileName = null;
        	String startCityName = null;
            String heuristicFileName = null;
            /**
             * Get arguments from command line and exit if incorrect number of args passed
             */
            if(args.length == 3) {
                inputFileName = args[0];
                startCityName = args[1];
                targetCity = args[2];
            } else if(args.length == 4) {
                inputFileName = args[0];
                startCityName = args[1];
                targetCity = args[2];
                heuristicFileName = args[3]; 
                heuristic = false;
            }else {
            	System.out.println("Enter 3 or 4 Parameters to run. Exiting..");
                System.out.println("1st Argument: Input text file");
                System.out.println("2nd Argument: Source City");
                System.out.println("3rd Argument: Destination City");
                System.out.println("4th Argument(optional): Heuristic file");
            	System.exit(0);
            }
         

            inputPathFile = new File(inputFileName);
            inputFileReader = new FileReader(inputPathFile);
            inputBuffer = new BufferedReader(inputFileReader);
       
            savePlaceDetails(inputBuffer);

        
            cityInfoFringe.add(new PlaceDetails(null, startCityName, 0));
            costAccumulated = 0;
            
            /**
             * read and store heuristic file info
             */
            if (!heuristic) {
            	
                heuristicFile = new File(heuristicFileName);
                hueristicFileReader = new FileReader(heuristicFile);
                hueristicBuffer = new BufferedReader(hueristicFileReader);
              
                saveHeuristicPlaceDetails(hueristicBuffer);
            }
            if (startCityName.equals(targetCity)) {
                System.out.println("distance : 0 km \nroute \n" + startCityName + " to " + targetCity + ", 0 km");
            } else {
                findAdjacentNodes(cityInfoFringe.get(0));
            }
        } catch (Exception exception) {
            System.out.println("exception - " + exception.getMessage());
        }
    }

    /**
     * save the heuristic place details into a arraylist
     * @param reader
     * @throws Exception 
     */
    
    public static void saveHeuristicPlaceDetails(BufferedReader placereader) throws Exception {
        String inputText;
        while (!(inputText = placereader.readLine()).contains(inputEnded)) {
            String[] place = inputText.split(" ");
            heuristicData.put(place[0], Integer.parseInt(place[1]));
        }
    }

    /**
     * save the place details into a list
     * @param placereader
     * @throws Exception 
     */ 
     public static void savePlaceDetails(BufferedReader placereader) throws Exception {
        //String currentLineInFile;
    	String inputText;
        while (!(inputText = placereader.readLine()).contains(inputEnded)) {
            String[] place = inputText.split(" ");
            inputCityData.add(new PlaceDetails(place[0], place[1], Integer.parseInt(place[2])));
            inputCityData.add(new PlaceDetails(place[1], place[0], Integer.parseInt(place[2])));
        }
    }

    /**
     * this method get least cost node(top most node), get the adjacent nodes and add them to fringe
     * @throws Exception 
     */
     public static void findAdjacentNodes(PlaceDetails place) throws Exception {
        costAccumulated = place.totalCost;
        if (cityInfoFringe.size() <= 0) {
            System.out.println("distance: infinity");
            System.out.println("route: \nnone");
        } else if (place.current.equals(targetCity)) {
            visitedCityData.add(place);
            computeFinalPath(visitedCityData);
        } else if (cityInfoFringe.size() > 0) {
        	if(cityInfoFringe.size() > memoryNodes ) {
            	memoryNodes = cityInfoFringe.size();
            }
            cityInfoFringe.remove(place);
            expandedNodes++;
            /*if (!citiesVisited.contains(city.current)) {
                maxNodesInMemory++;
              System.out.println("cityInfoFringe.size():  "+cityInfoFringe.size());
            }*/
            
            for (PlaceDetails currentPlace : inputCityData) {
            	 if (currentPlace.parent.equals(place.current)) {
                	if (!visitedCityNames.contains(place.current)) {
                		currentPlace.totalCost = currentPlace.cost + costAccumulated;
                        cityInfoFringe.add(currentPlace);
                        generatedNodes++;

                    }
                }
            }

            if (!visitedCityNames.contains(place.current)) 
            {
                visitedCityData.add(place);
                visitedCityNames.add(place.current);

            }

         
            if (cityInfoFringe.size() > 0) {
                if (!heuristic) {
                	Collections.sort(computeTotalHeuristicCost(cityInfoFringe), PlaceDetails.totalCostComparator);
                    findAdjacentNodes(cityInfoFringe.get(0));
                } else {
                	Collections.sort(cityInfoFringe, PlaceDetails.totalCostComparator);
                    findAdjacentNodes(cityInfoFringe.get(0));
                }
            } else {
                computeFinalPath(null);
                System.out.println("distance: infinity");
                System.out.println("route: \nnone");
            }
        }
    }

    /**
     * get the total heuristic cost of node 
     * @param list
     * @return
     * @throws Exception 
     */
    
    public static ArrayList<PlaceDetails> computeTotalHeuristicCost(ArrayList<PlaceDetails> list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).heuristicCost = heuristicData.get(list.get(i).current);
            list.get(i).totalCost = list.get(i).cost + list.get(i).heuristicCost;
        }
        return list;
    }

    /**
     * Find the final path from optimal Path
     */
    public static void computeFinalPath(ArrayList<PlaceDetails> optimalPath) {
         if (optimalPath != null) {
            expandedNodes++;
        }
		if (cityInfoFringe.size() > memoryNodes) {
			memoryNodes = cityInfoFringe.size();
		} 
        System.out.println("");
        System.out.println("nodes expanded:" + (expandedNodes));
        System.out.println("nodes generated:" + (generatedNodes));
        System.out.println("max nodes in memory:" + memoryNodes);
        
        
        
        if (optimalPath != null) {
            ArrayList<PlaceDetails> finalPath = new ArrayList<PlaceDetails>();
                     
            finalPath.add(optimalPath.get(optimalPath.size() - 1));
            int totalCost = finalPath.get(0).cost;
            String parent = finalPath.get(0).parent;
            
            int i = optimalPath.size() - 2;
            while (i != 0) {
                if (optimalPath.get(i).current.equals(parent)) {
                    parent = optimalPath.get(i).parent;
                    totalCost += optimalPath.get(i).cost;
                    finalPath.add(optimalPath.get(i));
                 
                }
                i--;
            }

            System.out.println("distance: " + (float) totalCost + " km");

            System.out.println("route:");
            
         
            for (int j = finalPath.size() - 1; j >= 0; j--) {

                System.out.println(finalPath.get(j).parent + " to " + finalPath.get(j).current + ", " + (float) finalPath.get(j).cost + " km");
            }
        }
    }

    /**
     * Node structure to store PlaceDetails
     */
     static class PlaceDetails {
        String parent;
        String current;
        int cost;
        int heuristicCost;
        int totalCost = 0;

        public PlaceDetails(String parent, String name, int cost) {
            this.parent = parent;
            this.current = name;
            this.cost = cost;
        }

        // comparator to compare two objects;
        public static Comparator<PlaceDetails> totalCostComparator = new Comparator<PlaceDetails>() {
            public int compare(PlaceDetails s1, PlaceDetails s2) {
                return s1.totalCost - s2.totalCost;
            }
        };
    }
}
