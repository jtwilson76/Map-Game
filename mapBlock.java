
public class mapBlock
{

	public String title = "";
	public String desc = "";
	public int n = 1;
	public int s = 1;
	public int e = 1;
	public int w = 1;
	public int[] itemsHere = new int[25];
	public int itemCount = 0;
	
	public mapBlock(String t, String d, int north, int south, int east, int west)
	{

		title = t;
		desc = d;
		n = north;
		s = south;
		e = east;
		w = west;
	}
	
}
