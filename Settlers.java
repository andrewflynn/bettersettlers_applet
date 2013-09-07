import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;


/**
 * Main class for the Settler applet that contains all subclasses, functionality, and drawing for creating
 * the map of Catan.
 * 
 * @author Andrew Flynn
 *
 */
public class Settlers extends JApplet implements ActionListener {
	
	/**
	 * Enum that describes a resource in its fullness.  Variables given to each resource are a unique identifier
	 * between 0-6 inclusive, a letter for each resource to be used in debugging, and the color that is displayed
	 * in the applet for that resource.
	 */
	protected static enum Resource {
		DESERT ("D", new Color(255,204,51)),
		WHEAT  ("G", Color.YELLOW),
		CLAY   ("C", new Color(100,0,0)),
		ROCK   ("R", Color.GRAY),
		SHEEP  ("S", Color.GREEN),
		WOOD   ("W", new Color(0,75,0)),
		WATER  ("X", Color.BLUE);
		
		private final String symbol;
		private final Color color;
		Resource(String symbol, Color color) {
			this.symbol = symbol;
			this.color = color;
		}
		private String getSymbol() {
			return symbol;
		}
		private Color getColor() {
			return color;
		}
	}
	
	protected static enum MapType {
		STANDARD (MapSpecs.STANDARD_LOW_RESOURCE_NUMBER,
				MapSpecs.STANDARD_HIGH_RESOURCE_NUMBER,
				MapSpecs.STANDARD_LAND_GRID,
				MapSpecs.STANDARD_WATER_GRID,
				MapSpecs.STANDARD_HARBOR_LINES,
				MapSpecs.STANDARD_LAND_NEIGHBORS,
				MapSpecs.STANDARD_WATER_NEIGHBORS,
				MapSpecs.STANDARD_LAND_INTERSECTIONS,
				MapSpecs.STANDARD_AVAILABLE_RESOURCES,
				MapSpecs.STANDARD_AVAILABLE_PROBABILITIES,
				MapSpecs.STANDARD_AVAILABLE_HARBORS),
		LARGE (MapSpecs.LARGE_LOW_RESOURCE_NUMBER,
				MapSpecs.LARGE_HIGH_RESOURCE_NUMBER,
				MapSpecs.LARGE_LAND_GRID,
				MapSpecs.LARGE_WATER_GRID,
				MapSpecs.LARGE_HARBOR_LINES,
				MapSpecs.LARGE_LAND_NEIGHBORS,
				MapSpecs.LARGE_WATER_NEIGHBORS,
				MapSpecs.LARGE_LAND_INTERSECTIONS,
				MapSpecs.LARGE_AVAILABLE_RESOURCES,
				MapSpecs.LARGE_AVAILABLE_PROBABILITIES,
				MapSpecs.LARGE_AVAILABLE_HARBORS),
		XLARGE (MapSpecs.XLARGE_LOW_RESOURCE_NUMBER,
				MapSpecs.XLARGE_HIGH_RESOURCE_NUMBER,
				MapSpecs.XLARGE_LAND_GRID,
				MapSpecs.XLARGE_WATER_GRID,
				MapSpecs.XLARGE_HARBOR_LINES,
				MapSpecs.XLARGE_LAND_NEIGHBORS,
				MapSpecs.XLARGE_WATER_NEIGHBORS,
				MapSpecs.XLARGE_LAND_INTERSECTIONS,
				MapSpecs.XLARGE_AVAILABLE_RESOURCES,
				MapSpecs.XLARGE_AVAILABLE_PROBABILITIES,
				MapSpecs.XLARGE_AVAILABLE_HARBORS);
		
		private final int lowResourceNumber;
		private final int highResourceNumber;
		private final Point[] landGrid;
		private final Point[] waterGrid;
		private final int[][] harborLines;
		private final int[][] landNeighbors;
		private final int[][] waterNeighbors;
		private final int[][] landIntersections;
		private final Resource[] availableResources;
		private final int[] availableProbabilities;
		private final Resource[] availableHarbors;
		MapType(int lrn, int hrn, Point[] lg, Point[] wg,
				int[][] hl, int[][] ln, int[][] wn,
				int[][] li, Resource[] ar, int[] ap, Resource[] ah) {
			lowResourceNumber = lrn;
			highResourceNumber = hrn;
			landGrid = lg;
			waterGrid = wg;
			harborLines = hl;
			landNeighbors = ln;
			waterNeighbors = wn;
			landIntersections = li;
			availableResources = ar;
			availableProbabilities = ap;
			availableHarbors = ah;
		}
		private int getLowResourceNumber() {
			return lowResourceNumber;
		}
		private int getHighResourceNumber() {
			return highResourceNumber;
		}
		private Point[] getLandGrid() {
			return landGrid;
		}
		private Point[] getWaterGrid() {
			return waterGrid;
		}
		private int[][] getHarborLines() {
			return harborLines;
		}
		private int[][] getLandNeighbors() {
			return landNeighbors;
		}
		private int[][] getWaterNeighbors() {
			return waterNeighbors;
		}
		private int[][] getLandIntersections() {
			return landIntersections;
		}
		private Resource[] getAvailableResources() {
			return availableResources;
		}
		private int[] getAvailableProbabilities() {
			return availableProbabilities;
		}
		private Resource[] getAvailableHarbors() {
			return availableHarbors;
		}
	}
	
	private static final Random RAND = new Random();
	
	private Button generateButton;
	private Button shuffleProbabilitiesButton;
	private Button shuffleHarborsButton;
	private Drawing drawingPanel;
	private JPanel mapTypePanel;
	private JPanel buttonContainer;
	private ButtonGroup mapTypeButtonGroup;
	private JRadioButton standardMapButton;
	private JRadioButton largeMapButton;
	private JRadioButton xlargeMapButton;
	
	private String errorMessage;
	
	// The current type of map that needs to be generated/displayed.  Defaults to standard
	protected static MapType currentMap = MapType.STANDARD;
	
	/**
	 * The (x,y) map laid out such that each odd row has odd numbers 1,3,5,etc followed by an even
	 * row which has all even numbers 0,2,4,6,etc which come in between the odd ones.
	 */
	protected static Point[][] THE_MAP = new Point[MapSpecs.BOARD_RANGE_X_VALUE+1][MapSpecs.BOARD_RANGE_Y_VALUE+1];
	
	// Contains a sorted list of the resources for the current map L -> R, T -> B
	protected ArrayList<Resource> resourceMap = new ArrayList<Resource>();
	
	// Contains a map of each type of resource and what numbers have been assigned to it (not sorted)
	protected HashMap<Resource, ArrayList<Integer>> resourceAvails = new HashMap<Resource, ArrayList<Integer>>();
	
	// Contains a sorted list of the numbers that overlay the resource map L -> R, T -> B
	protected ArrayList<Integer> numberMap = new ArrayList<Integer>();
	
	// Contains a sorted list of the harbors starting in the TL corner and going clockwise
	protected ArrayList<Harbor> harbors = new ArrayList<Harbor>();
	
	// Init() function called when the applet is started
	public void init() {
		setBackground(new Color(255,204,51));
		setForeground(new Color(255,204,51));
		
		createBoard();
		
		repaint();
		
		createControlPanel();
	}
	
	// Create buttons/etc at the top
	private void createControlPanel() {
		Container pane = getContentPane();
		
		drawingPanel = new Drawing();
		mapTypeButtonGroup = new ButtonGroup();
		standardMapButton = new JRadioButton("Standard (3-4 ppl)");
		standardMapButton.setSelected(true);
		largeMapButton = new JRadioButton("Large (5 ppl)");
		xlargeMapButton = new JRadioButton("X-Large (6 ppl)");
		mapTypeButtonGroup.add(standardMapButton);
		mapTypeButtonGroup.add(largeMapButton);
		mapTypeButtonGroup.add(xlargeMapButton);
		mapTypePanel = new JPanel(new GridLayout(0,1));
		mapTypePanel.add(standardMapButton);
		mapTypePanel.add(largeMapButton);
		mapTypePanel.add(xlargeMapButton);
		mapTypePanel.setBackground(new Color(255,204,51));
		
		generateButton = new Button("Generate Map");
		shuffleProbabilitiesButton = new Button("Shuffle Probabilities");
		shuffleHarborsButton = new Button("Shuffle Harbors");
		
		this.setSize(1250, 800);
		buttonContainer = new JPanel();
		buttonContainer.setLayout(new GridLayout(4,1));
		buttonContainer.setSize(400,500);
		buttonContainer.add(mapTypePanel);
		buttonContainer.add(generateButton);
		buttonContainer.add(shuffleProbabilitiesButton);
		buttonContainer.add(shuffleHarborsButton);
		
		SpringLayout layout = new SpringLayout();
		pane.setLayout(layout);
		pane.add(drawingPanel);
		pane.add(buttonContainer);
		layout.putConstraint(SpringLayout.WEST, buttonContainer,
                5,
                SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, buttonContainer,
                5,
                SpringLayout.NORTH, pane);
		layout.putConstraint(SpringLayout.WEST, drawingPanel,
                5,
                SpringLayout.WEST, pane);
		layout.putConstraint(SpringLayout.NORTH, drawingPanel,
                5,
                SpringLayout.NORTH, pane);
		
		pane.setComponentZOrder(drawingPanel, 1);
		pane.setComponentZOrder(buttonContainer, 0);
		
		standardMapButton.addActionListener(this);
		largeMapButton.addActionListener(this);
		xlargeMapButton.addActionListener(this);
		generateButton.addActionListener(this);
		shuffleProbabilitiesButton.addActionListener(this);
		shuffleHarborsButton.addActionListener(this);
		
		repaint();
	}
	
	// The high level function that performs all of the higher level calls to create the board.
	private void createBoard() {
		// Create the physical board first
		for (int i = 0; i <= MapSpecs.BOARD_RANGE_Y_VALUE; i++) {
			for (int j = (i % 2); j <= MapSpecs.BOARD_RANGE_X_VALUE; j+=2) {
				Point temp = new Point((MapSpecs.X_HEX_DELTA*j)+MapSpecs.STARTING_X_VALUE,
						      (MapSpecs.Y_HEX_DELTA*3*i)+MapSpecs.STARTING_Y_VALUE);
				THE_MAP[j][i] = temp;
			}
		}
		
		
		
		boolean findingMap = true;
		while (findingMap) {
			findingMap = false;
			
			// Prepare the resource tiles first
			resourceMap = getBalancedBoard();
		
			// Prepare an equivalent amount of resources (without mapping them to terrain tiles)
			resourceAvails = getProbabilities();
		
			// Overlay the probability numbers over the terrain tiles fairly
			try {
				numberMap = getNumberedBoard(resourceMap, resourceAvails);
			} catch (Exception e) {
				if (e.getMessage().equals("Infinite Loop")) {
					findingMap = true; // Still searching
				}
			}
		}
		
		// Arrange the harbors fairly given the current board
		harbors = getHarbors(resourceMap, numberMap);
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		drawingPanel.setBackground(new Color(255,204,51));
		drawingPanel.setForeground(new Color(255,204,51));
		drawingPanel.repaint();
	}
	
	public void actionPerformed(ActionEvent event) {
		errorMessage = "";
		if (event.getSource() == generateButton) {
			createBoard();
		} else if (event.getSource() == shuffleProbabilitiesButton) {
			// Get all new numbers; may require retries
			boolean findingMap = true;
			while (findingMap) {
				findingMap = false;
				try {
					resourceAvails = getProbabilities();
					numberMap = getNumberedBoard(resourceMap, resourceAvails);
					harbors = getHarbors(resourceMap, numberMap);
				} catch (Exception e) {
					if (e.getMessage().equals("Infinite Loop")) {
						findingMap = true;
					}
				}
			}
		} else if (event.getSource() == shuffleHarborsButton) {
			harbors = getHarbors(resourceMap, numberMap);
		} else if (event.getSource() == standardMapButton
				&& currentMap != MapType.STANDARD) {
			currentMap = MapType.STANDARD;
			createBoard();
		} else if (event.getSource() == largeMapButton
				&& currentMap != MapType.LARGE) {
			currentMap = MapType.LARGE;
			createBoard();
		} else if (event.getSource() == xlargeMapButton
				&& currentMap != MapType.XLARGE) {
			currentMap = MapType.XLARGE;
			createBoard();
		}
		repaint();
	}
	
	public class InfiniteLoopException extends Exception {
		public InfiniteLoopException(String msg) {
			super(msg);
		}
	}
	
	/**
	 * The simple class that represents a harbor.  Position is a number
	 * from 0-8 for which harbor it is (0 is TL corner and goes CW). 
	 * Note that "desert"==3:1 trading, water=no harbor and resource=2:1
	 * of that resource.  Facing is a variable referring to which tile the
	 * harbor's arms are facing, according to the numbering of the land tiles.
	 */
	public class Harbor {
		private int position;
		private Resource resource;
		private int facing;
		
		public Harbor(int position, Resource resource, int facing) {
			this.position = position;
			this.resource = resource;
			this.facing = facing;
		}

		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public Resource getResource() {
			return resource;
		}
		public void setPosition(Resource resource) {
			this.resource = resource;
		}
		public int getFacing() {
			return facing;
		}
		public void setFacing(int facing) {
			this.facing = facing;
		}
	}
	
	/**
	 * Where balanced means no two resources of the same type touch each other.
	 * Basic algorithm is grab a random terrain and see if you can place it in
	 * each position going from 0-18.  If it touches another terrain of the same
	 * type, don't place it and try again, else place it.  Keep going until all
	 * are placed properly, or we run out of options and then we start over.
	 */
	private static ArrayList<Resource> getBalancedBoard() {
		// Contains the resources already set
		ArrayList<Resource> set = new ArrayList<Resource>();
		// Contains the resources already consumed in this iteration
		ArrayList<Resource> tried = new ArrayList<Resource>();
		// Contains the resources yet to be consumed
		ArrayList<Resource> avail = initAvail();
		
		// Keep going if we have not set all resources (tried/avail is not empty)
		while (!tried.isEmpty() || !avail.isEmpty()) {
			// Game over: avail is empty and we still have un-set resources.
			// Start over.
			if (avail.isEmpty()) {
				set.clear();
				tried.clear();
				avail = initAvail();
			} else {
				// Consume
				Resource nextResource = avail.remove(RAND.nextInt(avail.size()));
				int nextIndex = set.size();
				boolean canPlaceHere = true;
				// Check neighbors for same resource.
				for (int neighbor : currentMap.getLandNeighbors()[nextIndex]) {
					if (neighbor > set.size()) {
						// Do nothing, it is not yet occupied
					} else {
						if (set.get(neighbor) == nextResource) {
							canPlaceHere = false;
							break;
						} else {
							// Do nothing, at least this neighbor isn't the same
						}
					}
				}
				if (canPlaceHere) {
					set.add(nextResource);
				} else {
					tried.add(nextResource);
				}
			}
			
		}
		return set;
	}
		
	/**
	 * Helper function for getBalancedBoard
	 */
	private static ArrayList<Resource> initAvail() {
		ArrayList<Resource> avail = new ArrayList<Resource>();
		for (Resource resource : currentMap.getAvailableResources()) {
			avail.add(resource);
		}
		return avail;
	}
	
	/**
	 * Gets fair mapping between the number of resources and what probabilities should be on those resources.
	 * Does not distribute them on a map in any way, but just sorts them fairly so no one resource is too heavy
	 * or light.  Returns a map between resource and which probabilities that resource has.
	 */
	private static HashMap<Resource, ArrayList<Integer>> getProbabilities() {
		HashMap<Resource, ArrayList<Integer>> toReturn = new HashMap<Resource, ArrayList<Integer>>();

		ArrayList<Integer> numbers = initProbabilities();
		ArrayList<Integer> sheeps = new ArrayList<Integer>();
		ArrayList<Integer> woods = new ArrayList<Integer>();
		ArrayList<Integer> rocks = new ArrayList<Integer>();
		ArrayList<Integer> clays = new ArrayList<Integer>();
		ArrayList<Integer> wheats = new ArrayList<Integer>();
		
		while (true) {
			numbers = initProbabilities();
			sheeps.clear();
			woods.clear();
			rocks.clear();
			clays.clear();
			wheats.clear();
			
			// Assign numbers completely randomly to each resource
			while (!numbers.isEmpty()) {
				int temp = RAND.nextInt(numbers.size());
				if (sheeps.size() < currentMap.getHighResourceNumber()) {
					sheeps.add(numbers.remove(temp));
					continue;
				}
				if (woods.size() < currentMap.getHighResourceNumber()) {
					woods.add(numbers.remove(temp));
					continue;
				}
				if (wheats.size() < currentMap.getHighResourceNumber()) {
					wheats.add(numbers.remove(temp));
					continue;
				}
				if (rocks.size() < currentMap.getLowResourceNumber()) {
					rocks.add(numbers.remove(temp));
					continue;
				}
				if (clays.size() < currentMap.getLowResourceNumber()) {
					clays.add(numbers.remove(temp));
					continue;
				}
			}
			
			// Try out to see if a) any resource has two of the same numbers or
			// b) the probability of a single resource is too high or low or
			// c) within each resource, no one tile has more than half the probability
			/*
			if (noDuplicates(sheeps) && noDuplicates(woods) && noDuplicates(wheats)
				&& noDuplicates(rocks) && noDuplicates(clays)
				&& sumProbability(sheeps) >= 12 && sumProbability(sheeps) <= 16
				&& sumProbability(woods) >= 12 && sumProbability(woods) <= 16
				&& sumProbability(wheats) >= 12 && sumProbability(wheats) <= 16
				&& sumProbability(rocks) >= 9 && sumProbability(rocks) <= 12
				&& sumProbability(clays) >= 9 && sumProbability(clays) <= 12
				&& isBalanced(rocks) && isBalanced(clays)) {
			*/
			if (noDuplicates(sheeps) && noDuplicates(woods) && noDuplicates(wheats)
					&& noDuplicates(rocks) && noDuplicates(clays)
					&& sumProbability(sheeps) >= 3*currentMap.getLowResourceNumber()
					&& sumProbability(sheeps) <= 4*currentMap.getHighResourceNumber()
					&& sumProbability(woods) >= 3*currentMap.getLowResourceNumber()
					&& sumProbability(woods) <= 4*currentMap.getHighResourceNumber()
					&& sumProbability(wheats) >= 3*currentMap.getLowResourceNumber()
					&& sumProbability(wheats) <= 4*currentMap.getHighResourceNumber()
					&& sumProbability(rocks) >= 3*currentMap.getLowResourceNumber()
					&& sumProbability(rocks) <= 4*currentMap.getHighResourceNumber()
					&& sumProbability(clays) >= 3*currentMap.getLowResourceNumber()
					&& sumProbability(clays) <= 4*currentMap.getHighResourceNumber()
					&& isBalanced(rocks) && isBalanced(clays)) {
				break;
			}
		}
		toReturn.put(Resource.SHEEP, sheeps);
		toReturn.put(Resource.WOOD, woods);
		toReturn.put(Resource.CLAY, clays);
		toReturn.put(Resource.ROCK, rocks);
		toReturn.put(Resource.WHEAT, wheats);
		
		return toReturn;
	}
	
	/**
	 * Add in each of the probability pieces into an array and return it.
	 * Helper function for getProbabilities()
	 */
	private static ArrayList<Integer> initProbabilities() {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i : currentMap.getAvailableProbabilities()) {
			numbers.add(i);
		}
		return numbers;
	}

	/**
	 * Make sure that the given array list contains no duplicates.  Returns
	 * true for no duplicates; false otherwise.
	 * NOTE: No duplicates for an XLARGE map means they can have one pair.
	 */
	private static boolean noDuplicates(ArrayList<Integer> numbers) {
		int numFound = 0;
		for (int i = 0; i < numbers.size(); i++) {
			int num = numbers.remove(i);
			if (numbers.contains(num)) {
				// Be sure to put back at the proper place in the array so
				// that we actually go through all elements
				numbers.add(i, num);
				numFound++;
				// 2 is okay since a pair is counted twice
				if (numFound == 3 && currentMap == MapType.XLARGE) {
					return false;
				}
				if (numFound == 1 && (currentMap == MapType.STANDARD || currentMap == MapType.LARGE)) {
					return false;
				}
			} else {
				numbers.add(i, num);
			}
		}
		return true;
	}

	/**
	 * Make sure that within the given array list, no one tile has more than
	 * half the probability (is not more than half the sum of the rest).
	 */
	private static boolean isBalanced(ArrayList<Integer> numbers) {
		for (int i = 0; i < numbers.size(); i++) {
			int currentNumber = numbers.remove(i);
			if (MapSpecs.PROBABILITY_MAPPING[currentNumber]
			                                 > sumProbability(numbers)) {
				return false;
			}
			numbers.add(i, currentNumber);
		}
		return true;
	}
	
	/**
	 * Sums up the integers in the array list and returns it the sum.
	 */
	private static int sumProbability(ArrayList<Integer> numbers) {
		int sum = 0;
		for (int number : numbers) {
			sum += MapSpecs.PROBABILITY_MAPPING[number];
		}
		return sum;
	}

	/**
	 * The primary function that maps a the given probabilities associated with each
	 * resource onto the given ordered array of resources.  Returns the ordered map
	 * of probabilities to match the given ordered map of resources. Algorithm is to
	 * randomly take and place a probability on each resource until they are placed.
	 * Then check to see if the arrangement (separate from resources) is okay.
	 */
	private static ArrayList<Integer> getNumberedBoard(ArrayList<Resource> resourceList,
			HashMap<Resource, ArrayList<Integer>> resourceNumbers) 
			throws Exception {
		ArrayList<Integer> toReturn = new ArrayList<Integer>();
		HashMap<Resource, ArrayList<Integer>> resourceNumbersClone = new HashMap<Resource, ArrayList<Integer>>();
		int counter = 0; // This is to prevent an infinite loop of an unsolvable map
		while (true) {
			toReturn.clear();
			resourceNumbersClone = deepCopy(resourceNumbers);
			for (Resource resource : resourceList) {
				if (resource == Resource.DESERT) {
					toReturn.add(0);
				} else {
					ArrayList<Integer> numAvails = resourceNumbersClone.get(resource);
					toReturn.add(numAvails.remove(RAND.nextInt(numAvails.size())));
				}
			}
			if (checkCollisionsAndProbability(toReturn)) {
				break;
			}
			if (counter++ == 100000) {
				throw new Exception("Infinite Loop");
			}
		}
		return toReturn;
	}

	/**
	 * Performs a deep copy of the given map of each resource to its list of probabilities.
	 * Returns a fresh new copy of the original map.
	 */
	private static HashMap<Resource, ArrayList<Integer>> deepCopy(
			HashMap<Resource, ArrayList<Integer>> orig) {
		HashMap<Resource, ArrayList<Integer>> copy = new HashMap<Resource, ArrayList<Integer>>();
		for (Resource resource : orig.keySet()) {
			ArrayList<Integer> copyValue = new ArrayList<Integer>();
			for (Integer tempInt : orig.get(resource)) {
				Integer tempIntCopy = new Integer(tempInt);
				copyValue.add(tempIntCopy);
			}
			copy.put(resource, copyValue);
		}
		return copy;
	}

	/**
	 * Check to make sure that no intersection of three probabilities a) has two of the same
	 * number or b) has too high or low of a probability
	 */
	private static boolean checkCollisionsAndProbability(ArrayList<Integer> toCheck) {
		for (int[] triplet : currentMap.getLandIntersections()) {
			ArrayList<Integer> tempTriplets = new ArrayList<Integer>();
			for (int trip : triplet) {
				tempTriplets.add(toCheck.get(trip));
			}
			if (!noDuplicates(tempTriplets)) {
				return false;
			} else {
				if (tempTriplets.contains(0)) {
					// Has a desert has to be 4<=x<=8
					if (sumProbability(tempTriplets) < 0 || sumProbability(tempTriplets) > 8) {
						return false;
					}
				} else {
					if (sumProbability(tempTriplets) < 0 || sumProbability(tempTriplets) > 11) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Given the current resource/probabilities map of Catan, figure out
	 * fair harbors and return a list of them in order. Basic algorithm is
	 * to dole out random harbors and see if it's fair.  If not, try again.
	 */
	private ArrayList<Harbor> getHarbors(ArrayList<Resource> resourceList,
			ArrayList<Integer> numberList) {
		ArrayList<Harbor> harbors = new ArrayList<Harbor>();
		ArrayList<Resource> toTake = new ArrayList<Resource>();
		
		while (true) {
			toTake = initHarbors();
			harbors.clear();
			
			// Quick random coin-flip to see if we start around the track with a harbor or open water
			if (RAND.nextBoolean() == true) {
				int i = 0;
				while (!toTake.isEmpty()) {
					if (currentMap.getWaterNeighbors()[i].length > 1) {
						harbors.add(new Harbor(i, toTake.remove(RAND.nextInt(toTake.size())), currentMap.getWaterNeighbors()[i][RAND.nextInt(2)]));
					} else {
						harbors.add(new Harbor(i, toTake.remove(RAND.nextInt(toTake.size())), currentMap.getWaterNeighbors()[i][0]));
					}
					i++;
					if (currentMap.getWaterNeighbors()[i].length > 1) {
						harbors.add(new Harbor(i, Resource.WATER, currentMap.getWaterNeighbors()[i][RAND.nextInt(2)]));
					} else {
						harbors.add(new Harbor(i, Resource.WATER, currentMap.getWaterNeighbors()[i][0]));
					}
					i++;
				}
			} else { // Reverse the order
				int i = 0;
				while (!toTake.isEmpty()) {
					if (currentMap.getWaterNeighbors()[i].length > 1) {
						harbors.add(new Harbor(i, Resource.WATER, currentMap.getWaterNeighbors()[i][RAND.nextInt(2)]));
					} else {
						harbors.add(new Harbor(i, Resource.WATER, currentMap.getWaterNeighbors()[i][0]));
					}
					i++;
					if (currentMap.getWaterNeighbors()[i].length > 1) {
						harbors.add(new Harbor(i, toTake.remove(RAND.nextInt(toTake.size())), currentMap.getWaterNeighbors()[i][RAND.nextInt(2)]));
					} else {
						harbors.add(new Harbor(i, toTake.remove(RAND.nextInt(toTake.size())), currentMap.getWaterNeighbors()[i][0]));
					}
					i++;
				}
			}
			
			// Check to see if things are fair. A 2:1 harbor can't face one of it's own terrain
			// tiles of probability 5, 6, 8, or 9.
			boolean goAhead = true;
			for (Harbor harbor : harbors) {
				Resource harborResource = harbor.getResource();
				if (harborResource == Resource.DESERT || harborResource == Resource.WATER) {
					continue;
				} else {
					Resource landResource = resourceList.get(harbor.getFacing());
					int landNumber = numberList.get(harbor.getFacing());
					if (harborResource == landResource && (landNumber >= 5 && landNumber <= 9)) {
						goAhead = false;
						break;
					}
				}
			}
			if (goAhead) {
				break;
			}
		}
		
		return harbors;
	}
	
	/**
	 * Add in each of the harbor pieces into an array and return it.
	 * Helper function for getHarbors()
	 */
	private static ArrayList<Resource> initHarbors() {
		ArrayList<Resource> harbors = new ArrayList<Resource>();
		for (Resource resource : currentMap.getAvailableHarbors()) {
			harbors.add(resource);
		}
		return harbors;
	}

	/**
	 * Private helper function that takes in a harbor and returns the
	 * orientation of its two arms facing inland.  In other words, 0
	 * if it is facing "left" (according to the harbor facing inward
	 * to the land tiles) or 1 for "right".  Or 0 if it has no choice.
	 * Used by the paint function.
	 */
	private static int whichWayHarborFaces(Harbor harbor) {
		int pos = harbor.getPosition();
		int len = currentMap.getWaterNeighbors()[pos].length;
		if (len == 1) {
			return 0;
		} else { // len = 2
			if (harbor.getFacing() == currentMap.getWaterNeighbors()[pos][0]) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	
	/**
	 * Given a list of resources and which one to display, displays the resource's
	 * symbol or a space if it is too big.  Helper function for displayResourceMap()
	 * TODO(flynn): I don't think passing in the array is necessary.
	 */
	private static String displayResource(ArrayList<Resource> resources, int id) {
		if (id >= resources.size()) {
			return " ";
		} else {
			return resources.get(id).getSymbol();
		}
	}
	
	/**
	 * Given a list of integers and which one to display, displays the number in
	 * hex or a space if it is too big.  Helper function for displayIntegerMap()
	 * TODO(flynn): I don't think passing in the array is necessary.
	 */
	private static String displayInteger(ArrayList<Integer> integers, int id) {
		if (id >= integers.size()) {
			return " ";
		} else {
			int temp = integers.get(id);
			if (temp == 10) {
				return "A";
			} else if (temp == 11) {
				return "B";
			} else if (temp == 12) {
				return "C";
			} else {
				return integers.get(id).toString();
			}
		}
	}
	
	/**
	 * Spits out an ASCII version of a resource map for debugging.
	 * TODO(flynn): This can probably be combined with displayIntegerMap into one
	 * function.
	 */
	private static void displayResourceMap(ArrayList<Resource> resources) {
		System.out.println("       /~\\ /~\\ /~\\ /~\\");
		System.out.println("      |~O~|~O~|~O~|~O~|");
		System.out.println("     /~\\~/ \\~/ \\~/ \\~/~\\");
		System.out.println("    |~O~| " + displayResource(resources, 0) + " | " + displayResource(resources, 1)
				+ " | " + displayResource(resources, 2) + " |~O~|");
		System.out.println("   /~\\~/ \\ / \\ / \\ / \\~/~\\");
		System.out.println("  |~O~| " + displayResource(resources, 3) + " | " + displayResource(resources, 4)
				+ " | " + displayResource(resources, 5) + " | " + displayResource(resources, 6) + " |~O~|");
		System.out.println(" /~\\~/ \\ / \\ / \\ / \\ / \\~/~\\");
		System.out.println("|~O~| " + displayResource(resources, 7) + " | " + displayResource(resources, 8)
				+ " | " + displayResource(resources, 9) + " | " + displayResource(resources, 10)
				+ " | " + displayResource(resources, 11) + " |~O~|");
		System.out.println(" \\~/~\\ / \\ / \\ / \\ / \\ /~\\~/");
		System.out.println("  |~O~| " + displayResource(resources, 12) + " | " + displayResource(resources, 13)
				+ " | " + displayResource(resources, 14) + " | " + displayResource(resources, 15) + " |~O~|");
		System.out.println("   \\~/~\\ / \\ / \\ / \\ /~\\~/");
		System.out.println("    |~O~| " + displayResource(resources, 16) + " | " + displayResource(resources, 17)
				+ " | " + displayResource(resources, 18) + " |~O~|");
		System.out.println("     \\~/~\\ /~\\ /~\\ /~\\~/");
		System.out.println("      |~O~|~O~|~O~|~O~|");
		System.out.println("       \\~/ \\~/ \\~/ \\~/");
	}

	/**
	 * Spits out an ASCII version of an integer map for debugging.
	 * TODO(flynn): This can probably be combined with displayResourceMap into one
	 * function.
	 */
	private static void displayIntegerMap(ArrayList<Integer> integers) {
		System.out.println("       /~\\ /~\\ /~\\ /~\\");
		System.out.println("      |~O~|~O~|~O~|~O~|");
		System.out.println("     /~\\~/ \\~/ \\~/ \\~/~\\");
		System.out.println("    |~O~| " + displayInteger(integers, 0) + " | " + displayInteger(integers, 1)
				+ " | " + displayInteger(integers, 2) + " |~O~|");
		System.out.println("   /~\\~/ \\ / \\ / \\ / \\~/~\\");
		System.out.println("  |~O~| " + displayInteger(integers, 3) + " | " + displayInteger(integers, 4)
				+ " | " + displayInteger(integers, 5) + " | " + displayInteger(integers, 6) + " |~O~|");
		System.out.println(" /~\\~/ \\ / \\ / \\ / \\ / \\~/~\\");
		System.out.println("|~O~| " + displayInteger(integers, 7) + " | " + displayInteger(integers, 8)
				+ " | " + displayInteger(integers, 9) + " | " + displayInteger(integers, 10)
				+ " | " + displayInteger(integers, 11) + " |~O~|");
		System.out.println(" \\~/~\\ / \\ / \\ / \\ / \\ /~\\~/");
		System.out.println("  |~O~| " + displayInteger(integers, 12) + " | " + displayInteger(integers, 13)
				+ " | " + displayInteger(integers, 14) + " | " + displayInteger(integers, 15) + " |~O~|");
		System.out.println("   \\~/~\\ / \\ / \\ / \\ /~\\~/");
		System.out.println("    |~O~| " + displayInteger(integers, 16) + " | " + displayInteger(integers, 17)
				+ " | " + displayInteger(integers, 18) + " |~O~|");
		System.out.println("     \\~/~\\ /~\\ /~\\ /~\\~/");
		System.out.println("      |~O~|~O~|~O~|~O~|");
		System.out.println("       \\~/ \\~/ \\~/ \\~/");
	}
	
	public class Drawing extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
	         super.paintComponent(g);
	 		// Window size
	 		this.setSize(1100,750);
	 		g.setFont(new Font("Courier New", Font.BOLD, 18));
			
	 		/*
			for (Point[] cells : THE_MAP) {
				for (Point cell : cells) {
					g.setColor(new Color(0, cell.x % 255, cell.y % 255));
					drawHex(g, cell.x, cell.y);
				}
			}
			*/
	 		
	 		// Draw land hexagons/numbers/probabilities
	 		for (int i = 0; i < currentMap.getLandGrid().length; i++) {
	 			Point point = currentMap.getLandGrid()[i];
	 			int x = THE_MAP[point.x][point.y].x;
	 			int y = THE_MAP[point.x][point.y].y;
	 			g.setColor(resourceMap.get(i).getColor());
	 			drawHex(g, x, y);
	 			Integer thisNum = numberMap.get(i);
	 			if (thisNum == 6 || thisNum == 8) {
	 				g.setColor(new Color(210, 0, 0));
	 			} else {
	 				g.setColor(Color.BLACK);
	 			}
	 			g.drawString(thisNum.toString(), x-(MapSpecs.X_HEX_DELTA/10), y+(MapSpecs.Y_HEX_DELTA*2));
	 			drawDots(g, thisNum, x, y);
	 			
	 		}
	 		
	 		// Draw ocean hexagons/harbors
	 		for (int i = 0; i < currentMap.getWaterGrid().length; i++) {
	 			g.setColor(Color.BLUE);
	 			Point point = currentMap.getWaterGrid()[i];
	 			int x = THE_MAP[point.x][point.y].x;
	 			int y = THE_MAP[point.x][point.y].y;
	 			drawHex(g, x, y);
	 			if (harbors.get(i).getResource() == Resource.DESERT) {
	 				g.setColor(Color.WHITE);
	 				g.fillOval(x-(MapSpecs.X_HEX_DELTA/2), y+(MapSpecs.Y_HEX_DELTA*4/3),
	 						MapSpecs.X_HEX_DELTA, MapSpecs.X_HEX_DELTA);
	 				int whichDir = whichWayHarborFaces(harbors.get(i));
	 				drawHarborLine(g, currentMap.getHarborLines()[i][whichDir],
	 						x, y+(MapSpecs.Y_HEX_DELTA*2));
	 				drawHarborLine(g, currentMap.getHarborLines()[i][whichDir+1],
	 						x, y+(MapSpecs.Y_HEX_DELTA*2));
	 				g.setColor(Color.BLACK);
	 				g.drawString("3", x-(MapSpecs.X_HEX_DELTA/10), y+(MapSpecs.Y_HEX_DELTA*2+5));
	 			} else if (harbors.get(i).getResource() == Resource.WATER) {
	 				g.setColor(Color.BLUE);
	 			} else {
	 				g.setColor(harbors.get(i).getResource().getColor());
	 				g.fillOval(x-(MapSpecs.X_HEX_DELTA/2), y+(MapSpecs.Y_HEX_DELTA*4/3),
	 						MapSpecs.X_HEX_DELTA, MapSpecs.X_HEX_DELTA);
	 				int whichDir = whichWayHarborFaces(harbors.get(i));
	 				drawHarborLine(g, currentMap.getHarborLines()[i][whichDir],
	 						x, y+(MapSpecs.Y_HEX_DELTA*2));
	 				drawHarborLine(g, currentMap.getHarborLines()[i][whichDir+1],
	 						x, y+(MapSpecs.Y_HEX_DELTA*2));
	 				g.setColor(Color.BLACK);
	 				g.drawString("2", x-(MapSpecs.X_HEX_DELTA/10), y+(MapSpecs.Y_HEX_DELTA*2+5));
	 			}
	 		} 
	 		if (errorMessage != null) {
	 			g.setColor(Color.BLACK);
	 			g.drawString(errorMessage, 350, 775);
	 		}
	    }
	    
		
		/** Draw the probability dots in a terrain tile	 */
		private void drawDots(Graphics g, int num, int x, int y) {
			switch (MapSpecs.PROBABILITY_MAPPING[num]) {
			case 5:
				g.fillOval(x-(MapSpecs.X_HEX_DELTA/2), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				g.fillOval(x+(MapSpecs.X_HEX_DELTA/2), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
			case 3:
				g.fillOval(x-(MapSpecs.X_HEX_DELTA/4), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				g.fillOval(x+(MapSpecs.X_HEX_DELTA/4), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
			case 1:
				g.fillOval(x, y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				break;
			case 4:
				g.fillOval(x-(MapSpecs.X_HEX_DELTA*3/8), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				g.fillOval(x+(MapSpecs.X_HEX_DELTA*3/8), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
			case 2:
				g.fillOval(x-(MapSpecs.X_HEX_DELTA*1/8), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				g.fillOval(x+(MapSpecs.X_HEX_DELTA*1/8), y+(MapSpecs.Y_HEX_DELTA*5/2),
						MapSpecs.PROBABILITY_DOT_SIZE, MapSpecs.PROBABILITY_DOT_SIZE);
				break;
			case 0:
			default:		
			}
		}
		
		/**
		 * Draw a line given the current g, and the (x,y) starting coordinates
		 * of the middle of the harbor's hexagon, and the direction (0-5) which
		 * points to which direction of the hexagon it should extend to, 0 being
		 * the TL corner and going around clockwise.
		 */
		private void drawHarborLine(Graphics g, int dir, int x, int y) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			switch(dir) {
			case 0:
				g2.drawLine(x, y, x-MapSpecs.X_HEX_DELTA, y-MapSpecs.Y_HEX_DELTA);
				break;
			case 1:
				g2.drawLine(x, y, x, y-(2*MapSpecs.Y_HEX_DELTA));
				break;
			case 2:
				g2.drawLine(x, y, x+MapSpecs.X_HEX_DELTA, y-MapSpecs.Y_HEX_DELTA);
				break;
			case 3:
				g2.drawLine(x, y, x+MapSpecs.X_HEX_DELTA, y+MapSpecs.Y_HEX_DELTA);
				break;
			case 4:
				g2.drawLine(x, y, x, y+(2*MapSpecs.Y_HEX_DELTA));
				break;
			case 5:
				g2.drawLine(x, y, x-MapSpecs.X_HEX_DELTA, y+MapSpecs.Y_HEX_DELTA);
				break;
			default:
				System.out.println("WARNING: Cannot draw this line: " + dir);
				break;
			}
		}
		
		/** Draw a hex using the current color in g and the two starting pixels */
		private void drawHex(Graphics g, int xStart, int yStart) {
			int[] xs = {xStart, xStart+MapSpecs.X_HEX_DELTA, xStart+MapSpecs.X_HEX_DELTA,
					xStart, xStart-MapSpecs.X_HEX_DELTA, xStart-MapSpecs.X_HEX_DELTA, xStart};
			int[] ys = {yStart, yStart+MapSpecs.Y_HEX_DELTA, yStart+(3*MapSpecs.Y_HEX_DELTA),
					yStart+(4*MapSpecs.Y_HEX_DELTA), yStart+(3*MapSpecs.Y_HEX_DELTA),
					yStart+MapSpecs.Y_HEX_DELTA, yStart};
			g.fillPolygon(xs, ys, 7);
		}
	}
}
