
public class Player
{
	public int xpos;
	public int ypos;
	public String title;
	public String desc;
	public int moveable;
	public int combative;
	public int health;
	public int armor;
	public int strength;
	public int dexterity;
	public int intelligence;
	
	public int[] items = new int[25];
	public int itemCount = 0;
	
	public boolean isAlive()
	{
		if (health > 0)
			return true;
		else if (combative == 1)
		{
			moveable = 0;
			combative = 0;
			System.out.println(title + "died.");
			return false;
		}
		else
		{
			return false;
		}
	}
	
}
