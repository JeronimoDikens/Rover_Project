/**
 * 
 * @author Hleb Kokhanovsky
 *
 */
import java.io.IOException; import java.nio.ByteBuffer; import java.nio.channels.FileChannel; import java.nio.file.Files; import java.nio.file.InvalidPathException; import java.nio.file.Path; import java.nio.file.Paths; import java.nio.file.StandardOpenOption;

public class Rover {
  private static void parseStringArray(String [][] inputStringArray, int [][] outputIntArray ) throws CannotStartMovement {
    for(int i = 0; i<outputIntArray.length; i++) {
      for(int j = 0; j<outputIntArray[0].length; j++) {
        char [] charArr = inputStringArray[0][0].toCharArray();
          if(charArr[0] == 'X') {
            throw new CannotStartMovement("Incorrect input data. Movement cannot be started from unreachable point");
			}
				  
		char [] chArr = inputStringArray[i][j].toCharArray();
				 
          for(int x = 0; x<chArr.length ; x++) {
            if(Character.isDigit(chArr[x]) || chArr[x] == '-') {
              outputIntArray[i][j] = Integer.parseInt(inputStringArray[i][j]);
              break;
            }
            if( Character.isLetter(chArr[x]) && chArr[x]=='X') {
              outputIntArray[i][j] = 999;
              break;
            } else {
              throw new CannotStartMovement("Incorrect input data. Origanal map is incorrect");
            }
          }
      }
    }
  }
  
  private static void initLinkMatrix(int [][] map, int [][] linkMatrix) {
    final int costOfStep = 1;
      int temp_var = 0;
      int unreachablePoint = 999;
      int x;
      int y;
        for (int i=0; i < linkMatrix.length; i++) {
          linkMatrix[i][i] = 0;
            for(int j = i+1; j<linkMatrix[i].length; j++) {
              x= (i/map[0].length);
              y= (i%map[0].length);
                if(map[x][y] == unreachablePoint) {
                  for(int iter = 0; iter < linkMatrix[i].length; iter++) {
                    if(iter==i) {
                      linkMatrix[i][i] = 0;
                      continue;
                    }
                    linkMatrix[i][iter] = unreachablePoint;
                    linkMatrix[iter][i] = unreachablePoint;
                    }
                    break;
                    }
                    // 0 will stay if the current node doesn't bind with checking node
                    linkMatrix[i][j] = 0;
                    linkMatrix[j][i] = 0;
                    //one step  North
                    if(((i - map[0].length) == j) && (x-1 > 0) && (map[x-1][y] != unreachablePoint)) {
                       temp_var = ( (map[x][y] - map[x-1][y]) > 0) ? 
                       (map[x][y] - map[x-1][y]) : 
                       ((map[x][y] - map[x-1][y])*( -1));
                       linkMatrix[i][j] = temp_var + costOfStep;
                       linkMatrix[j][i] = temp_var + costOfStep;
                    }
                    //one step NW
                    if(((i - (map[0].length+1)) == j) &&
                    		(i/map[0].length != j/map[0].length ) &&
                    		((x-1 > 0) && (y-1) > 0) &&
                    		(map[x-1][y-1] != unreachablePoint)) {
                      temp_var = ( (map[x][y] - map[x-1][y-1]) > 0) ? 
                                   (map[x][y] - map[x-1][y-1]) : 
                                  ((map[x][y] - map[x-1][y-1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
                    //one step NE
                    if(((i - (map[0].length-1)) == j) &&
                    		(i/map[0].length != j/map[0].length ) &&
                    		((x-1 > 0) && (y+1) < map[0].length) &&
                    		(map[x-1][y+1] != unreachablePoint)) {
                      temp_var = ( (map[x][y] - map[x-1][y+1]) > 0) ?
                    		       (map[x][y] - map[x-1][y+1]) :
                    			  ((map[x][y] - map[x-1][y+1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
                    //one step  West
                    if(((i - 1) == j ) && (y-1 > 0)){
                      temp_var = ( (map[x][y] - map[x][y-1]) > 0) ? 
                    		       (map[x][y] - map[x][y-1]) :
                    		      ((map[x][y] - map[x][y-1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
                    //one step  East
                    if(( (i + 1) == j) && (y+1 < map[0].length)) {
                      temp_var = ( (map[x][y] - map[x][y+1]) > 0) ?
                    		       (map[x][y] - map[x][y+1]) :
                    		      ((map[x][y] - map[x][y+1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
                    //one step  South 
                    if(( (i + map[0].length) == j ) && (x+1 < map.length) ){
                      temp_var = ( (map[x][y] - map[x+1][y]) > 0) ? 
                    			   (map[x][y] - map[x+1][y]) :
                    			  ((map[x][y] - map[x+1][y])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      } 
                    //one step SW
                    if(((i + (map[0].length-1)) == j) &&
                    		(i/map[0].length != j/map[0].length ) &&
                    		((x+1 < map.length) && (y-1 > 0)) &&
                    		(map[x+1][y-1] != unreachablePoint)) {
                      temp_var = ( (map[x][y] - map[x+1][y-1]) > 0) ?
                    		       (map[x][y] - map[x+1][y-1]) :
                    		      ((map[x][y] - map[x+1][y-1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
                    //one step SE
                    if(((i + (map[0].length+1)) == j) &&
                    		(i/map[0].length != j/map[0].length ) &&
                    		((x+1 < map.length) && (y+1 < map[0].length)) &&
                    		(map[x+1][y+1] != unreachablePoint)) {
                      temp_var = ( (map[x][y] - map[x+1][y+1]) > 0) ?
                    		       (map[x][y] - map[x+1][y+1]) :
                    		      ((map[x][y] - map[x+1][y+1])*( -1));
                      linkMatrix[i][j] = temp_var + costOfStep;
                      linkMatrix[j][i] = temp_var + costOfStep;
                      }
            }
        }
  }

  private static void initStepCostArray(int [] stepCostArray, int size, int unsettedweight) {
    // All steps/elements initialized on big/infinity value
    for(int i = 0; i < size; i++) {
      stepCostArray[i] = unsettedweight;
    }
  }
		
  private static void initVisitNodesArray(int [] array, int size) {
    for(int i = 0; i < size; i++) {
      array[i] = 1;
    }
  }
		
  private static void algorithmDijkstra(int [][] originalMap, int linkMatrxArray[][], int stepCostArray[], int visitedNodesArray[], int size, int unsettedweight) {
    int minimalWeight;
    int minimalIndex;
    int tempVar;
    do {
      minimalWeight = unsettedweight;
      minimalIndex = unsettedweight;
      //pass through nodes
        for(int i = 0; i < size; i++) {
          if( (visitedNodesArray[i] == 1) && (stepCostArray[i] < minimalWeight) ) {
              minimalWeight = stepCostArray[i];
              minimalIndex = i;
              }
          }
      // processing of nodes' weight
      if(minimalIndex != unsettedweight) {
        for( int i = 0; i < size; i++) {
          if(linkMatrxArray[minimalIndex][i] > 0 && linkMatrxArray[minimalIndex][i] < 999) {
            tempVar = minimalWeight + linkMatrxArray[minimalIndex][i];
              if(tempVar < stepCostArray[i]) {
                stepCostArray[i] = tempVar;
              }else if(linkMatrxArray[minimalIndex][i] == 999) {
            	stepCostArray[i] = 999;
            	    }
           }
         }
        visitedNodesArray[minimalIndex] = 0;
       }
    } while( minimalIndex < unsettedweight);
  }

  //reversive route identification, from finish to start 
  private static void routeBuilder(int linkedNodesArray[][], int routeNodesArray[], int stepCostArray[], int size,  int startPoint, int finishPoint) {
    int weight = stepCostArray[finishPoint];
    int temp_var;
    int prv_index = 1;
    //begin at the end of the map
    routeNodesArray[0] = finishPoint + 1;
    while( finishPoint != startPoint) {
      for(int i = 0; i < size; i++ ) {
        if(linkedNodesArray[i][finishPoint] != 0) {
          temp_var = weight - linkedNodesArray[i][finishPoint];
          if(temp_var == stepCostArray[i]) {
            weight = temp_var;
            finishPoint = i;
            routeNodesArray[prv_index] = i + 1;
            prv_index++;
          }
        }
      }
    }
  }
  // method close file and stream output and write message at file
  public static void startToPrint(String message) {
    Path path = Paths.get("path-plan.txt");
    if(Files.exists(path)) {
      try {
    	  Files.delete(path);
    	  } catch (IOException e) {
    		  e.printStackTrace();
    		}
    }
    // try with resources
    try( FileChannel fileChannel = (FileChannel)Files.newByteChannel(Paths.get("path-plan.txt"),
    		                                                         StandardOpenOption.WRITE,
    		                                                         StandardOpenOption.CREATE)	) {
      ByteBuffer buffer = ByteBuffer.allocate(message.length());
      char [] string = message.toCharArray();
        for(int j = 0; j < string.length; j++) {
          buffer.put((byte)(string[j]));
        }
        buffer.rewind();
        fileChannel.write(buffer);
        buffer.rewind();
    } catch (InvalidPathException e) {
    	e.printStackTrace();
    	System.exit(1);
    } catch (IOException e ) {
    	e.printStackTrace();
    	System.exit(1);
      }
  }
  // prepare information to print
  private static void printPrepare(int routeNodesArr[], int map[][]) {
    int steps=0;
    int fuel;
    int x;
    int y;
    int xPrevious;
    int yPrevious;
    int stepArray[];
    StringBuilder pathInfo = new StringBuilder(128);
    StringBuilder fuelInfo = new StringBuilder();
    StringBuilder stepsInfo = new StringBuilder();
    String finalString;
    char [] temp;
    // counting of steps
    for(int var: routeNodesArr) {
      if(var == 0) {
        break;
      }
      steps++;
    }
    // new array encapsulate only indexes of the path
	stepArray = new int[steps];
	// every step will cost one pcs. of fuel 
	fuel = steps-1;
	// copy indexes of the path into new array
	for( int i = 0; i < steps; i++) {
		stepArray[steps-1-i] = (routeNodesArr[i] - 1);
	}
	for(int i = 0; i < stepArray.length; i++) {
	  x= (stepArray[i]/map[0].length);
	  y= (stepArray[i]%map[0].length);
	  // on every circle add next indexes in StringBuilder object
	  pathInfo.append("[" + x+ "][" + y +"]");
	  if(i < stepArray.length-1) {
	      pathInfo.append("->");
	  }
	  if( i > 0) {
	      xPrevious = (stepArray[i-1]/map[0].length);
	      yPrevious = (stepArray[i-1]%map[0].length);
	      fuel += (map[x][y] - map[xPrevious][yPrevious]) > 0 ?
	    		  (map[x][y] - map[xPrevious][yPrevious]) :
	    		  (map[x][y] - map[xPrevious][yPrevious]) * (-1);
	  }
	}
	// add strings into StringBuilder objects
	stepsInfo.append("\nsteps: " + (steps-1));
	fuelInfo.append("\nfuel: " + fuel );
	temp =  new char[pathInfo.length()+fuelInfo.length()+stepsInfo.length()];
	// complete strings
	pathInfo.getChars(0, pathInfo.length(), temp, 0);
	fuelInfo.getChars(0, fuelInfo.length(), temp, pathInfo.length());
	stepsInfo.getChars(0, stepsInfo.length(), temp, pathInfo.length()+fuelInfo.length());
	finalString = new String(pathInfo.toString()+fuelInfo.toString()+stepsInfo.toString());
	startToPrint(finalString);
  }
	
  public static void calculateRoverPath(String [][] map) throws CannotStartMovement {
    int n = map.length;
    int m = map[0].length;
    final int  unsettedWeight = 1_000_000;
    int startPointIndx = 0 ;
    int finishPointIndx = ((n*m) - 1);
    int [][] convertedMapMatrix = new int[map.length][map[0].length];
    int linkMatrix[][] = new int[n*m][n*m];
    int stepCost[] = new int[n*m];
    int visitedNodes[] = new int[n*m];
    int routeNodes[] = new int[n*m];
    
    parseStringArray(map,convertedMapMatrix);
    initLinkMatrix(convertedMapMatrix, linkMatrix);
    initStepCostArray(stepCost, n*m, unsettedWeight);
    initVisitNodesArray(visitedNodes, n*m);
    stepCost[startPointIndx] = 0;
    algorithmDijkstra(convertedMapMatrix, linkMatrix, stepCost, visitedNodes, n*m, unsettedWeight);
    routeBuilder(linkMatrix, routeNodes, stepCost, n*m, startPointIndx, finishPointIndx);
    printPrepare(routeNodes, convertedMapMatrix);
  }
}
