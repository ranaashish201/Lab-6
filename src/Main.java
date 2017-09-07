import java.io.*;
import java.util.*;
public class Main {

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		int n = scan.nextInt();
		int i = scan.nextInt();
		int x = scan.nextInt();
		int y = scan.nextInt();
		Coordinates queen = new Coordinates(x,y);
		Main ob = new Main();
		String s = ob.start(n, i,queen).toString();
		OutputStreamWriter br = new OutputStreamWriter(new FileOutputStream("output.txt"));
		br.write(s);
		br.close();
		

	}
	public StringBuilder start(int n,int it,Coordinates queen) throws IOException
	{	
		Knight knights[] = new Knight[n];
		for (int i = 0;i<n;i++)
		{
			BufferedReader reader = new BufferedReader(new FileReader(new File((i+1)+".txt")));
			String line;
			ArrayList<String> contents = new ArrayList<String>();
			while((line = reader.readLine())!=null)
			{
				contents.add(line);
			}
			String a[] = contents.get(1).split(" ");
			knights[i] = new Knight(contents.get(0),new Coordinates(Integer.parseInt(a[0]),Integer.parseInt(a[1])));
			int len = Integer.parseInt(contents.get(2));
			for (int j = 0;j<len;j++)
			{
				String[] arr = contents.get(3+j).split(" ");
				if (arr.length == 3)
				{
					int a1 = Integer.parseInt(arr[1]);
					int a2 = Integer.parseInt(arr[2]);
					knights[i].insert(new Coordinates(a1,a2));	
				}
				else
					knights[i].insert(arr[1]);
			}
		}
		Arrays.sort(knights);
		//System.out.println(Arrays.toString(knights));
		return play(knights,it,queen);
	}
	
	public int checkPresence(Coordinates c,Knight[] arr)
	{
		int ans = -1;
		for (int i = 0;i<arr.length;i++)
		{
			if (arr[i]!=null)
			{
				if (c.equals(arr[i].getC()))
					ans = i;
			}
		}
		return ans;
	}
	public StringBuilder play(Knight[] knights,int it,Coordinates queen)
	{
		StringBuilder ans = new StringBuilder();
		int flag = 0;
		for (int i = 1;i<=it;i++)
		{
			
			if (flag == 1)
				break;
			int ks = -1;
			Object o = null;
			Knight k;
			Coordinates oc = null;
			for (int j = 0;j<knights.length;j++)
			{
				boolean exceptionFound = false;
				if (knights[j] == null)
					continue;
				ans.append(i+" "+knights[j].toString()).append("\n");  // answer
				try{
				 k = knights[j];
				Stack<Object> s = k.getS();
				if (s.isEmpty())
				{
					throw new StackEmptyException();
				}
				 o = s.pop();
				if (!(o instanceof Coordinates))
				{
						throw new NonCoordinateException();
				}
				else if (o instanceof Coordinates)
				{
					 oc = (Coordinates) o;
					if (oc.equals(queen))
					{
						throw new QueenFoundException();
					}
					 ks = checkPresence(oc,knights);
					if (ks == -1)
					{
						knights[j].setC(oc);
					}
					else
					{
						throw new OverlapException();
						// exception for same coordinates
					}
						
				}
				}
				catch( OverlapException e)
				{
					exceptionFound = true;
					ans.append("OverlapException: Knights Overlap Exception "+knights[ks].getName()).append("\n");
					knights[j].setC(oc);
					knights[ks] = null;
					e.printStackTrace();
				}
				catch( NonCoordinateException e)
				{
					exceptionFound = true;
					ans.append("NonCoordinateException: Not a coordinate Exception "+o.toString()).append("\n");
					e.printStackTrace();
				}
				catch (StackEmptyException e)
				{
					exceptionFound = true;
					ans.append("StackEmptyException: Stack Empty exception").append("\n");
					knights[j] = null;
					e.printStackTrace();
				}
				catch(QueenFoundException e)
				{
					exceptionFound = true;
					ans.append("QueenFoundException: Queen has been Found. Abort!").append("\n");
					flag = 1;
					e.printStackTrace();
					break;
				}
				finally{
					if (exceptionFound == false)
					{
						ans.append("No exception "+oc).append("\n");
					}
				}
			}
		}
		return ans;
	}

}

class Knight implements Comparable<Knight>{
	private String name;
	private Coordinates c;
	private Stack<Object> s;
	Knight(String name,Coordinates c)
	{
		this.name = name;
		this.c = c;
		s = new Stack<Object>();
	}
	@Override
	public String toString()
	{
		return this.name+" "+this.c;
	}
	public void insert(Object c)
	{
		s.push(c);
	}
	public String getName() {
		return name;
	}
	public void setC(Coordinates c)
	{
		this.c = c;
	}
	public Coordinates getC() {
		return c;
	}
	public Stack<Object> getS() {
		return s;
	}
	@Override
	public int compareTo(Knight ob) {
		return this.name.compareTo(ob.name);
	}
	
}

class Coordinates{
	int x,y;
	Coordinates(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals(Object o1)
	{
		if (o1== null)
			return false;
		Coordinates o = (Coordinates)o1;
		return (this.x == o.x)&&(this.y == o.y);
	}
	@Override
	public String toString()
	{
		return this.x+" "+this.y;
	}
}

class NonCoordinateException extends Exception{
	public NonCoordinateException()
	{
		super();
	}
	@Override
	public String getMessage()
	{
		return "Not a coordinate Exception";
	}
}

class StackEmptyException extends Exception{
	public StackEmptyException()
	{
		super();
	}
	@Override
	public String getMessage()
	{
		return "Stack Empty exception";
	}
}

class QueenFoundException extends Exception{
	public QueenFoundException()
	{
		super();
	}
	@Override
	public String getMessage()
	{
		return "Queen has been Found. Abort!";
	}
}
class OverlapException extends Exception{
	public OverlapException()
	{
		super();
	}
	public String getMessage()
	{
		return "Knights Overlap Exception";
	}

}

