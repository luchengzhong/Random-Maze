package Maze;

import java.util.Random;
import java.util.Stack;

class spot {
	public int id;// 序号,从零开始

	public int left;
	public int right;
	public int up;
	public int down;// 上下左右是否可走，可走为1,不可走为0
	public int stepflag;// 表示该点的状态，未被走过-1，走过0，正在该点1
	public int best;// 该点是否在可以到终点的路线上，是1，否0

	public int father;// 集合父链
	public int count;// 集合中元素个数

	// 其余的尽情往里加，加完告诉我
	public Coordinate coor;
	public int[] flag;// 上下左右是否被走过的标记

	public spot() {
		count = 1;
		left = 0;
		right = 0;
		up = 0;
		down = 0;
		stepflag = -1;
		best = 0;
		flag = new int[4];
		for (int i = 0; i < 4; i++)
			flag[i] = 0;
	}
}// 方格类

class Coordinate {
	public int x, y;// 坐标，横向X，纵向Y，X=j，Y=i

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
}// 坐标类

class Graph {
	public spot[][] sp;
	public int N;
	Stack<Coordinate> stack = new Stack();
	Stack<Coordinate> beststack = new Stack();
	public Coordinate begin;// 该点是否为起点，是1，否0
	public Coordinate dest;// 该点是否为终点，是1，否0
	String ways[] = { "right", "down", "left", "up" };// 方向字符串

	public Graph(int n) {
		int i, j;
		N = n;
		sp = new spot[N][N];
		for (i = 0; i < N; i++)
			for (j = 0; j < N; j++) {
				sp[i][j] = new spot();
				sp[i][j].id = j + i * N;
				sp[i][j].coor = new Coordinate(j, i);
				sp[i][j].father = sp[i][j].id;
				sp[i][j].count = 1;
			}
		begin = sp[0][0].coor;
		dest = sp[N - 1][N - 1].coor;
	}

	public spot getSpot(int id) {// 根据id返回一个点
		int x, y;
		int i;
		Coordinate coor;
		for (i = 0; i < N * N - 1; i++) {
			coor = toCoor(id);
			x = coor.x;
			y = coor.y;
			if ((sp[y][x].id == id))
				return sp[y][x];
		}
		return null;
	}

	public int findFather(spot temp) { // 寻找此点的父结点
		int s = temp.id;
		int f = temp.father;
		while (f != s) {
			s = f;
			f = getSpot(f).father;
		}
		return f;
	}

	public int isSame(spot a, spot b) { // 是否是同一集合
		if (findFather(a) == findFather(b))
			return 1;
		return 0;
	}

	public Coordinate toCoor(int id) { // 将id转换成坐标
		Coordinate coor = new Coordinate(0, 0);
		coor.x = id % N;
		coor.y = id / N;
		return coor;
	}

	public void merge(spot a, spot b) { // 合并两点集
		if (isSame(a, b) == 1)
			return;
		spot a_f = getSpot(findFather(a));
		spot b_f = getSpot(findFather(b));

		a_f.father = b_f.id;
		a_f.count = a_f.count + b_f.count;
		b_f.count = a_f.count;
	}

	public Coordinate findnei(String way, Coordinate now) {
		int x = now.x;
		int y = now.y;
		if (way.equals("left")) {
			if ((now.x - 1) < 0)
				return null;
			x = now.x - 1;
		} else if (way.equals("right")) {
			if ((now.x + 1) >= N)
				return null;
			x = now.x + 1;
		} else if (way.equals("down")) {
			if ((now.y + 1) >= N)
				return null;
			y = now.y + 1;
		} else if (way.equals("up")) {
			if ((now.y - 1) < 0)
				return null;
			y = now.y - 1;
		} else
			return null;
		return sp[y][x].coor;
	}

	public void generate() { // 破除相邻两格的墙
		Random random = new Random();
		int randid;
		int randnei;
		Coordinate randcoor = null;
		Coordinate tempcoor = null;
		int i;
		int x1 = 0, y1 = 0, x2, y2;
		do {
			randid = Math.abs(random.nextInt() % (N * N));
			randnei = Math.abs(random.nextInt() % 4);
			randcoor = toCoor(randid);
			for (i = 0; i < 4; i++) {
				if ((tempcoor = findnei(ways[randnei], randcoor)) == null)
					randnei = (randnei + 1) % 4;
				else
					break;
			}
			if (tempcoor == null)
				continue;
			x1 = randcoor.x;
			y1 = randcoor.y;
			x2 = tempcoor.x;
			y2 = tempcoor.y;
			if (isSame(sp[y1][x1], sp[y2][x2]) == 0) {
				switch (randnei) {
				case 0:
					sp[y1][x1].right = 1;
					sp[y2][x2].left = 1;
					break;
				case 1:
					sp[y1][x1].down = 1;
					sp[y2][x2].up = 1;
					break;
				case 2:
					sp[y1][x1].left = 1;
					sp[y2][x2].right = 1;
					break;
				case 3:
					sp[y1][x1].up = 1;
					sp[y2][x2].down = 1;
					break;
				}
				merge(sp[y1][x1], sp[y2][x2]);
			}
		} while (sp[y1][x1].count != N * N);
	}
}
