import java.awt.Point;

/**
 * 
 * Class that contains many useful numbers for putting together the map of catan.
 * @author Andrew Flynn
 *
 */
public class MapSpecs {
	
	/**
	 * Mapping between the numbers that are shown (rolled on the dice) and the probability of each
	 * being rolled.
	 */
	protected static final int[] PROBABILITY_MAPPING =
	  { 0,  // 0
        0,  // 1
        1,  // 2
        2,  // 3
        3,  // 4
        4,  // 5
        5,  // 6
        0,  // 7
        5,  // 8
        4,  // 9
        3,  // 10
        2,  // 11
        1}; // 12
	
	/** The x delta between the points of the hexagon. */
	protected static final int X_HEX_DELTA = 40;
	
	/** The y delta between the points of the hexagon. */
	protected static final int Y_HEX_DELTA = 24;
	
	/**
	 * STANDARD BOARD (3-4 ppl)
	 * Starting value for the TL land hexagon's x value.
	 * */
	protected static final int STARTING_X_VALUE = 350;
	/**
	 * STANDARD BOARD (3-4 ppl)
	 * Starting value for the TL land hexagon y value.
	 * */
	protected static final int STARTING_Y_VALUE = 50;
	
	/**
	 * How wide the board can be (including 0)
	 */
	protected static final int BOARD_RANGE_X_VALUE = 14;
	
	/**
	 * How tall the board can be (including 0)
	 */
	protected static final int BOARD_RANGE_Y_VALUE = 8;
	
	/** The size of the dots. */
	protected static final int PROBABILITY_DOT_SIZE = 4;
	
	/**
	 * STANDARD BOARD (3-4 ppl) How many rocks/clays there are available on to distribute.
	 */
	protected static final int STANDARD_LOW_RESOURCE_NUMBER = 3;

	/**
	 * STANDARD BOARD (3-4 ppl) How many wheats/woods/sheep there are available on to distribute.
	 */
	protected static final int STANDARD_HIGH_RESOURCE_NUMBER = 4;

	/**
	 * STANDARD BOARD (3-4 ppl) List of how many of each resource this type of board contains
	 */
	protected static final Settlers.Resource[] STANDARD_AVAILABLE_RESOURCES = {
			Settlers.Resource.SHEEP, Settlers.Resource.SHEEP,
			Settlers.Resource.SHEEP, Settlers.Resource.SHEEP,
			Settlers.Resource.WHEAT, Settlers.Resource.WHEAT,
			Settlers.Resource.WHEAT, Settlers.Resource.WHEAT,
			Settlers.Resource.WOOD, Settlers.Resource.WOOD,
			Settlers.Resource.WOOD, Settlers.Resource.WOOD,
			Settlers.Resource.ROCK, Settlers.Resource.ROCK,
			Settlers.Resource.ROCK, Settlers.Resource.CLAY,
			Settlers.Resource.CLAY, Settlers.Resource.CLAY,
			Settlers.Resource.DESERT };

	/**
	 * STANDARD BOARD (3-4 ppl) List of how many of each probability this type of board contains
	 */
	protected static final int[] STANDARD_AVAILABLE_PROBABILITIES = { 2, 3, 3,
			4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12 };

	/**
	 * STANDARD BOARD (3-4 ppl) List of how many of harbors there are (desert is 3:1)
	 */
	protected static final Settlers.Resource[] STANDARD_AVAILABLE_HARBORS = {
			Settlers.Resource.SHEEP, Settlers.Resource.WHEAT,
			Settlers.Resource.WOOD, Settlers.Resource.ROCK,
			Settlers.Resource.CLAY, Settlers.Resource.DESERT,
			Settlers.Resource.DESERT, Settlers.Resource.DESERT,
			Settlers.Resource.DESERT };

	/**
	 * STANDARD BOARD (3-4 ppl) The (x,y) pixels of the TL corner of each land hexagon for drawing purposes.
	 */
	protected static final Point[] STANDARD_LAND_GRID = { new Point(4, 2),
			new Point(6, 2), new Point(8, 2), new Point(3, 3), new Point(5, 3),
			new Point(7, 3), new Point(9, 3), new Point(2, 4), new Point(4, 4),
			new Point(6, 4), new Point(8, 4), new Point(10, 4),
			new Point(3, 5), new Point(5, 5), new Point(7, 5), new Point(9, 5),
			new Point(4, 6), new Point(6, 6), new Point(8, 6) };

	/**
	 * STANDARD BOARD (3-4 ppl) The (x,y) pixels of the TL corner of each ocean hexagon for drawing purposes.
	 */
	protected static final Point[] STANDARD_WATER_GRID = { new Point(3, 1),
			new Point(5, 1), new Point(7, 1), new Point(9, 1),
			new Point(10, 2), new Point(11, 3), new Point(12, 4),
			new Point(11, 5), new Point(10, 6), new Point(9, 7),
			new Point(7, 7), new Point(5, 7), new Point(3, 7), new Point(2, 6),
			new Point(1, 5), new Point(0, 4), new Point(1, 3), new Point(2, 2) };

	/**
	 * STANDARD BOARD (3-4 ppl) This list contains 2 or 3 numbers that are the possible corners that the lines can go to (half only touch two at 2 corners and half touch at 3 corners).  0 is the TL corner, 1 is the top corner, 2 is the TR corner, and so forth clockwise around the hexagon to 5 (BL).
	 */
	protected static final int[][] STANDARD_HARBOR_LINES = { { 3, 4 },
			{ 3, 4, 5 }, { 3, 4, 5 }, { 4, 5 }, { 4, 5, 0 }, { 4, 5, 0 },
			{ 5, 0 }, { 5, 0, 1 }, { 5, 0, 1 }, { 0, 1 }, { 0, 1, 2 },
			{ 0, 1, 2 }, { 1, 2 }, { 1, 2, 3 }, { 1, 2, 3 }, { 2, 3 },
			{ 2, 3, 4 }, { 2, 3, 4 } };

	/**
	 * STANDARD BOARD (3-4 ppl) This list of lists contains the land tiles that each land tile is neighbors with.  The land tiles are numbered 0-18 starting at the TL corner and going L -> R, T -> B.
	 */
	protected static final int[][] STANDARD_LAND_NEIGHBORS = { { 1, 3, 4 },
			{ 0, 2, 4, 5 }, { 1, 5, 6 }, { 0, 4, 7, 8 }, { 0, 1, 3, 5, 8, 9 },
			{ 1, 2, 4, 6, 9, 10 }, { 2, 5, 10, 11 }, { 3, 8, 12 },
			{ 3, 4, 7, 9, 12, 13 }, { 4, 5, 8, 10, 13, 14 },
			{ 5, 6, 9, 11, 14, 15 }, { 6, 10, 15 }, { 7, 8, 13, 16 },
			{ 8, 9, 12, 14, 16, 17 }, { 9, 10, 13, 15, 17, 18 },
			{ 10, 11, 14, 18 }, { 12, 13, 17 }, { 13, 14, 16, 18 },
			{ 14, 15, 17 } };

	/**
	 * STANDARD BOARD (3-4 ppl) This list of lists contains the land tiles that each ocean tile is neighbors with.  The ocean tiles are (similar to everywhere else) numbered starting at the TL ocean tile and going around clockwise 0-17.  The land tiles are (similar to everywhere else) numbered 0-18 starting at the TL land tile and going L -> R, T -> B.  Note that the numbers are not small to large but are numbered clockwise by whichever land tile is earliest on in the clockwise rotation (starting in the TL corner).
	 */
	protected static final int[][] STANDARD_WATER_NEIGHBORS = { { 0 },
			{ 1, 0 }, { 2, 1 }, { 2 }, { 6, 2 }, { 11, 6 }, { 11 }, { 15, 11 },
			{ 18, 15 }, { 18 }, { 17, 18 }, { 16, 17 }, { 16 }, { 12, 16 },
			{ 7, 12 }, { 7 }, { 3, 7 }, { 0, 3 } };

	/**
	 * STANDARD BOARD (3-4 ppl) "Triplets" are defined as three terrain tiles that come together at an intersection (ports do not count). These are ordered starting in the TL corner going L -> R, T -> B (going straight across such that the top three terrain tiles are the "top two" for the first triplets. These are defined so that we can make sure no single settlement placement is too amazing.
	 */
	protected static final int[][] STANDARD_LAND_INTERSECTIONS = { { 0, 1, 4 },
			{ 1, 2, 5 }, { 0, 3, 4 }, { 1, 4, 5 }, { 2, 5, 6 }, { 3, 4, 8 },
			{ 4, 5, 9 }, { 5, 6, 10 }, { 3, 7, 8 }, { 4, 8, 9 }, { 5, 9, 10 },
			{ 6, 10, 11 }, { 7, 8, 12 }, { 8, 9, 13 }, { 9, 10, 14 },
			{ 10, 11, 15 }, { 8, 12, 13 }, { 9, 13, 14 }, { 10, 14, 15 },
			{ 12, 13, 16 }, { 13, 14, 17 }, { 14, 15, 18 }, { 13, 16, 17 },
			{ 14, 17, 18 } };

	/**
	 * LARGE BOARD (5 ppl)
	 * How many rocks/clays there are available on to distribute.
	 */
	protected static final int LARGE_LOW_RESOURCE_NUMBER = 4;
	
	/**
	 * LARGE BOARD (5 ppl)
	 * How many wheats/woods/sheep there are available on to distribute.
	 */
	protected static final int LARGE_HIGH_RESOURCE_NUMBER = 5;
	
	/**
	 * LARGE BOARD (5 ppl)
	 * List of how many of each probability this type of board contains
	 */
	protected static final int[] LARGE_AVAILABLE_PROBABILITIES =
	{ 2,3,3,4,4,5,5,6,6,8,8,8,9,9,9,10,10,10,11,11,11,12,12 };
	
	/**
	 * LARGE BOARD (5 ppl)
	 * List of how many of each resource this type of board contains
	 */
	protected static final Settlers.Resource[] LARGE_AVAILABLE_RESOURCES =
	{
		Settlers.Resource.SHEEP,  // 5 Sheep
		Settlers.Resource.SHEEP,
		Settlers.Resource.SHEEP,
		Settlers.Resource.SHEEP,
		Settlers.Resource.SHEEP,
		Settlers.Resource.WHEAT,  // 5 Wheat
		Settlers.Resource.WHEAT,
		Settlers.Resource.WHEAT,
		Settlers.Resource.WHEAT,
		Settlers.Resource.WHEAT,
		Settlers.Resource.WOOD,   // 5 Wood
		Settlers.Resource.WOOD,
		Settlers.Resource.WOOD,
		Settlers.Resource.WOOD,
		Settlers.Resource.WOOD,
		Settlers.Resource.ROCK,   // 4 Rock
		Settlers.Resource.ROCK,
		Settlers.Resource.ROCK,
		Settlers.Resource.ROCK,
		Settlers.Resource.CLAY,   // 4 Clay
		Settlers.Resource.CLAY,
		Settlers.Resource.CLAY,
		Settlers.Resource.CLAY,
		Settlers.Resource.DESERT, // 1 Desert
	};
	
	/**
	 * LARGE BOARD (5 ppl)
	 * List of how many of harbors there are (desert is 3:1)
	 */
	protected static final Settlers.Resource[] LARGE_AVAILABLE_HARBORS =
	{

		Settlers.Resource.SHEEP,  // 1 Sheep 2:1
		Settlers.Resource.WHEAT,  // 1 Wheat 2:1
		Settlers.Resource.WOOD,   // 1 Wood 2:1
		Settlers.Resource.ROCK,   // 1 Rock 2:1
		Settlers.Resource.CLAY,   // 1 Clay 2:1
		Settlers.Resource.DESERT, // 5 3:1's
		Settlers.Resource.DESERT,
		Settlers.Resource.DESERT,
		Settlers.Resource.DESERT,
		Settlers.Resource.DESERT,
	};
	
	/**
	 * LARGE BOARD (5 ppl)
	 * The (x,y) pixels of the TL corner of each land hexagon for drawing purposes.
	 */
	protected static final Point[] LARGE_LAND_GRID = 
	{
		new Point(4,2),  // 0
		new Point(6,2),  // 1
		new Point(8,2),  // 2
		new Point(10,2), // 3
		new Point(3,3),  // 4
		new Point(5,3),  // 5
		new Point(7,3),  // 6
		new Point(9,3),  // 7
		new Point(11,3), // 8
		new Point(2,4),  // 9
		new Point(4,4),  // 10
		new Point(6,4),  // 11
		new Point(8,4),  // 12
		new Point(10,4), // 13
		new Point(12,4), // 14
		new Point(3,5),  // 15
		new Point(5,5),  // 16
		new Point(7,5),  // 17
		new Point(9,5),  // 18
		new Point(11,5), // 19
		new Point(4,6),  // 20
		new Point(6,6),  // 21
		new Point(8,6),  // 22
		new Point(10,6)  // 23
    };
	
	/**
	 * LARGE BOARD (5 ppl)
	 * The (x,y) pixels of the TL corner of each ocean hexagon for drawing purposes.
	 */
	protected static final Point[] LARGE_WATER_GRID =
	{
		new Point(3,1),  // 0
		new Point(5,1),  // 1
		new Point(7,1),  // 2
		new Point(9,1),  // 3
		new Point(11,1), // 4
		new Point(12,2), // 5
		new Point(13,3), // 6
		new Point(14,4), // 7
		new Point(13,5), // 8
		new Point(12,6), // 9
		new Point(11,7), // 10
		new Point(9,7),  // 11
		new Point(7,7),  // 12
		new Point(5,7),  // 13
		new Point(3,7),  // 14
		new Point(2,6),  // 15
		new Point(1,5),  // 16
		new Point(0,4),  // 17
		new Point(1,3),  // 18
		new Point(2,2),  // 19
	};

	/**
	 * LARGE BOARD (5 ppl)
	 * This list contains 2 or 3 numbers that are the possible corners that
	 * the lines can go to (half only touch two at 2 corners and half touch at 3 corners).  0 is the TL corner,
	 * 1 is the top corner, 2 is the TR corner, and so forth clockwise around the hexagon to 5 (BL).
	 */
	protected static final int[][] LARGE_HARBOR_LINES =
	{
		   {3,4},   // 0
           {3,4,5}, // 1
           {3,4,5}, // 2
           {3,4,5}, // 3
           {4,5},   // 4
           {4,5,0}, // 5
           {4,5,0}, // 6
           {5,0},   // 7
           {5,0,1}, // 8
           {5,0,1}, // 9
           {0,1},   // 10
           {0,1,2}, // 11
           {0,1,2}, // 12
           {0,1,2}, // 13
           {1,2},   // 14
           {1,2,3}, // 15
           {1,2,3}, // 16
           {2,3},   // 17
           {2,3,4}, // 18
           {2,3,4}, // 19
	};
	
	/**
	 * LARGE BOARD (5 ppl)
	 * This list of lists contains the land tiles that each land tile is neighbors with.  The land tiles are
	 * numbered 0-23 starting at the TL corner and going L -> R, T -> B.
	 */
	protected static final int[][] LARGE_LAND_NEIGHBORS =
	{
		{1,4,5},
		{0,2,5,6},
		{1,3,6,7},
		{2,7,8},
		{0,5,9,10},
		{0,1,4,6,10,11},
		{1,2,5,7,11,12},
		{2,3,6,8,12,13},
		{3,7,13,14},
		{4,10,15},
		{4,5,9,11,15,16},
		{5,6,10,12,16,17},
		{6,7,11,13,17,18},
		{7,8,12,14,18,19},
		{8,13,19},
		{9,10,16,20},
		{10,11,15,17,20,21},
		{11,12,16,18,21,22},
		{12,13,17,19,22,23},
		{13,14,18,23},
		{15,16,21},
		{16,17,20,22},
		{17,18,21,23},
		{18,19,22},
	};
	
	/**
	 * LARGE BOARD (5 ppl)
	 * This list of lists contains the land tiles that each ocean tile is neighbors with.  The ocean tiles are
	 * (similar to everywhere else) numbered starting at the TL ocean tile and going around clockwise 0-17.  The
	 * land tiles are (similar to everywhere else) numbered 0-18 starting at the TL land tile and going
	 * L -> R, T -> B.  Note that the numbers are not small to large but are numbered clockwise by whichever land
	 * tile is earliest on in the clockwise rotation (starting in the TL corner).
	 */
	protected static final int[][] LARGE_WATER_NEIGHBORS =
	{
		    {0},     // 0
	        {1,0},   // 1
	        {2,1},   // 2
	        {3,2},   // 3
	        {3},     // 4
	        {8,3},   // 5
	        {14,8},  // 6
	        {14},    // 7
	        {19,14}, // 8
	        {23,19}, // 9
	        {23},    // 10
	        {22,23}, // 11
	        {21,22}, // 12
	        {20,21}, // 13
	        {20},    // 14
	        {15,20}, // 15
	        {9,15},  // 16
	        {9},     // 17
	        {4,9},   // 18
	        {0,4},   // 19
	 };
	
	/**
	 * LARGE BOARD (5 ppl)
	 * "Triplets" are defined as three terrain tiles that come together at an intersection (ports do not count).
	 * These are ordered starting in the TL corner going L -> R, T -> B (going straight across such that the top
	 * three terrain tiles are the "top two" for the first triplets.
	 * These are defined so that we can make sure no single settlement placement is too amazing.
	 */
	protected static final int[][] LARGE_LAND_INTERSECTIONS =
		{
		  {0,1,5},
		  {1,2,6},
		  {2,3,7},
		  
		  {0,4,5},
		  {1,5,6},
		  {2,6,7},
		  {3,7,8},
		  
		  {4,5,10},
		  {5,6,11},
		  {6,7,12},
		  {7,8,13},
		  
		  {4,9,10},
		  {5,10,11},
		  {6,11,12},
		  {7,12,13},
		  {8,13,14},
		 
		  {9,10,15},
		  {10,11,16},
		  {11,12,17},
		  {12,13,18},
		  {13,14,19},
		  
		  {10,15,16},
		  {11,16,17},
		  {12,17,18},
		  {13,18,19},
		  
		  {15,16,20},
		  {16,17,21},
		  {17,18,22},
		  {18,19,23},
		  
		  {16,20,21},
		  {17,21,22},
		  {18,22,23},
		};

	/**
	 * XLARGE BOARD (6 ppl) How many rocks/clays there are available on to distribute.
	 */
	protected static final int XLARGE_LOW_RESOURCE_NUMBER = 5;

	/**
	 * XLARGE BOARD (6 ppl) How many wheats/woods/sheep there are available on to distribute.
	 */
	protected static final int XLARGE_HIGH_RESOURCE_NUMBER = 6;

	/**
	 * XLARGE BOARD (6 ppl) List of how many of each probability this type of board contains
	 */
	protected static final int[] XLARGE_AVAILABLE_PROBABILITIES =
	{ 2,2,3,3,3,4,4,4,5,5,5,6,6,6,8,8,8,9,9,9,10,10,10,11,11,11,12,12 };

	/**
	 * XLARGE BOARD (6 ppl) List of how many of each resource this type of board contains
	 */
	protected static final Settlers.Resource[] XLARGE_AVAILABLE_RESOURCES =
	{
			Settlers.Resource.SHEEP, // 6 Sheep
			Settlers.Resource.SHEEP,
			Settlers.Resource.SHEEP,
			Settlers.Resource.SHEEP,
			Settlers.Resource.SHEEP,
			Settlers.Resource.SHEEP,
			Settlers.Resource.WHEAT, // 6 Wheat
			Settlers.Resource.WHEAT,
			Settlers.Resource.WHEAT,
			Settlers.Resource.WHEAT,
			Settlers.Resource.WHEAT,
			Settlers.Resource.WHEAT,
			Settlers.Resource.WOOD,  // 6 Wood
			Settlers.Resource.WOOD,
			Settlers.Resource.WOOD,
			Settlers.Resource.WOOD,
			Settlers.Resource.WOOD,
			Settlers.Resource.WOOD,
			Settlers.Resource.ROCK,  // 5 Rock
			Settlers.Resource.ROCK,
			Settlers.Resource.ROCK,
			Settlers.Resource.ROCK,
			Settlers.Resource.ROCK,
			Settlers.Resource.CLAY,  // 5 Clay
			Settlers.Resource.CLAY,
			Settlers.Resource.CLAY,
			Settlers.Resource.CLAY,
			Settlers.Resource.CLAY,
			Settlers.Resource.DESERT, // 2 Desert
			Settlers.Resource.DESERT
	};

	/**
	 * XLARGE BOARD (6 ppl) List of how many of harbors there are (desert is 3:1)
	 */
	protected static final Settlers.Resource[] XLARGE_AVAILABLE_HARBORS = {
			Settlers.Resource.SHEEP,  // 2 Sheep 2:1's
			Settlers.Resource.SHEEP,
			Settlers.Resource.WHEAT,  // 1 Wheat 2:1
			Settlers.Resource.WOOD,   // 1 Wood 2:1
			Settlers.Resource.ROCK,   // 1 Rock 2:1
			Settlers.Resource.CLAY,   // 1 Clay 2:1
			Settlers.Resource.DESERT, // 5 3:1's
			Settlers.Resource.DESERT,
			Settlers.Resource.DESERT,
			Settlers.Resource.DESERT,
			Settlers.Resource.DESERT };

	/**
	 * XLARGE BOARD (6 ppl) The (x,y) pixels of the TL corner of each land hexagon for drawing purposes.
	 */
	protected static final Point[] XLARGE_LAND_GRID =
	{
		new Point(5,1),  // 0
		new Point(7,1),  // 1
		new Point(9,1),  // 2
		new Point(4,2),  // 3
		new Point(6,2),  // 4
		new Point(8,2),  // 5
		new Point(10,2), // 6
		new Point(3,3),  // 7
		new Point(5,3),  // 8
		new Point(7,3),  // 9
		new Point(9,3),  // 10
		new Point(11,3), // 11
		new Point(2,4),  // 12
		new Point(4,4),  // 13
		new Point(6,4),  // 14
		new Point(8,4),  // 15
		new Point(10,4), // 16
		new Point(12,4), // 17
		new Point(3,5),  // 18
		new Point(5,5),  // 19
		new Point(7,5),  // 20
		new Point(9,5),  // 21
		new Point(11,5), // 22
		new Point(4,6),  // 23
		new Point(6,6),  // 24
		new Point(8,6),  // 25
		new Point(10,6), // 26
		new Point(5,7),  // 27
		new Point(7,7),  // 28
		new Point(9,7)   // 29
	};

	/**
	 * XLARGE BOARD (6 ppl) The (x,y) pixels of the TL corner of each ocean hexagon for drawing purposes.
	 */
	protected static final Point[] XLARGE_WATER_GRID =
	{
		new Point(4,0),  // 0
		new Point(6,0),  // 1
		new Point(8,0),  // 2
		new Point(10,0), // 3
		new Point(11,1), // 4
		new Point(12,2), // 5
		new Point(13,3), // 6
		new Point(14,4), // 7
		new Point(13,5), // 8
		new Point(12,6), // 9
		new Point(11,7), // 10
		new Point(10,8), // 11
		new Point(8,8),  // 12
		new Point(6,8),  // 13
		new Point(4,8),  // 14
		new Point(3,7),  // 15
		new Point(2,6),  // 16
		new Point(1,5),  // 17
		new Point(0,4),  // 18
		new Point(1,3),  // 19
		new Point(2,2),  // 20	
		new Point(3,1),  // 21	
	};

	/**
	 * XLARGE BOARD (6 ppl) This list contains 2 or 3 numbers that are the possible corners that the lines can go to (half only touch two at 2 corners and half touch at 3 corners).  0 is the TL corner, 1 is the top corner, 2 is the TR corner, and so forth clockwise around the hexagon to 5 (BL).
	 */
	protected static final int[][] XLARGE_HARBOR_LINES =
	{
		   {3,4},   // 0
           {3,4,5}, // 1
           {3,4,5}, // 2
           {4,5},   // 3
           {4,5,0}, // 4
           {4,5,0}, // 5
           {4,5,0}, // 6
           {5,0},   // 7
           {5,0,1}, // 8
           {5,0,1}, // 9
           {5,0,1}, // 10
           {0,1},   // 11
           {0,1,2}, // 12
           {0,1,2}, // 13
           {1,2},   // 14
           {1,2,3}, // 15
           {1,2,3}, // 16
           {1,2,3}, // 17
           {2,3},   // 18
           {2,3,4}, // 19
           {2,3,4}, // 20
           {2,3,4}, // 21
	};

	/**
	 * XLARGE BOARD (6 ppl) This list of lists contains the land tiles that each land tile is neighbors with.  The land tiles are numbered 0-23 starting at the TL corner and going L -> R, T -> B.
	 */
	protected static final int[][] XLARGE_LAND_NEIGHBORS =
	{
		{1,3,4},
		{0,2,4,5},
		{1,5,6},
		
		{0,4,7,8},
		{0,1,3,5,8,9},
		{1,2,4,6,9,10},
		{2,5,10,11},
		
		{3,8,12,13},
		{3,4,7,9,13,14},
		{4,5,8,10,14,15},
		{5,6,9,11,15,16},
		{6,10,16,17},
		
		{7,13,18},
		{7,8,12,14,18,19},
		{8,9,13,15,19,20},
		{9,10,14,16,20,21},
		{10,11,15,17,21,22},
		{11,16,22},
		
		{12,13,19,23},
		{13,14,18,20,23,24},
		{14,15,19,21,24,25},
		{15,16,20,22,25,26},
		{16,17,21,26},
		
		{18,19,24,27},
		{19,20,23,25,27,28},
		{20,21,24,26,28,29},
		{21,22,25,29},
		
		{23,24,28},
		{24,25,27,29},
		{25,26,28}
	};

	/**
	 * XLARGE BOARD (6 ppl) This list of lists contains the land tiles that each ocean tile is neighbors with.  The ocean tiles are (similar to everywhere else) numbered starting at the TL ocean tile and going around clockwise 0-17.  The land tiles are (similar to everywhere else) numbered 0-18 starting at the TL land tile and going L -> R, T -> B.  Note that the numbers are not small to large but are numbered clockwise by whichever land tile is earliest on in the clockwise rotation (starting in the TL corner).
	 */
	protected static final int[][] XLARGE_WATER_NEIGHBORS =
	{
		{0},
		{1,0},
		{2,1},
		{2},
		{6,2},
		{11,6},
		{17,11},
		{17},
		{22,17},
		{26,22},
		{29,26},
		{29},
		{28,29},
		{27,28},
		{27},
		{23,27},
		{18,23},
		{12,18},
		{12},
		{7,12},
		{3,7},
		{0,3}
	};

	/**
	 * XLARGE BOARD (6 ppl) "Triplets" are defined as three terrain tiles that come together at an intersection (ports do not count). These are ordered starting in the TL corner going L -> R, T -> B (going straight across such that the top three terrain tiles are the "top two" for the first triplets. These are defined so that we can make sure no single settlement placement is too amazing.
	 * 2,3,3,4,4,5,5,4,4,3,3,2
	 */
	protected static final int[][] XLARGE_LAND_INTERSECTIONS =
	{
		{0,1,4},
		{1,2,5},
		
		{0,3,4},
		{1,4,5},
		{2,5,6},
		
		{3,4,8},
		{4,5,9},
		{5,6,10},
		
		{3,7,8},
		{4,8,9},
		{5,9,10},
		{6,10,11},
		
		{7,8,13},
		{8,9,14},
		{9,10,15},
		{10,11,16},
		
		{7,12,13},
		{8,13,14},
		{9,14,15},
		{10,15,16},
		{11,16,17},
		
		{12,13,18},
		{13,14,19},
		{14,15,20},
		{15,16,21},
		{16,17,22},
		
		{13,18,19},
		{14,19,20},
		{15,20,21},
		{16,21,22},
		
		{18,19,23},
		{19,20,24},
		{20,21,25},
		{21,22,26},
		
		{19,23,24},
		{20,24,25},
		{21,25,26},
		
		{23,24,27},
		{24,25,28},
		{25,26,29},
		
		{24,27,28},
		{25,28,29}
	};
}
