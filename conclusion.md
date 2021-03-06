# 乡村邮局选址问题
## 题目
有一条公路（x轴）经过V个村庄，每一个村庄都在整数的坐标点上。在这条公路上规划P个邮局，为了方便，这些邮局应该建在P个村庄中。要求是让不同村庄的人到邮局走的总路程最小。
## 输入
V P<br>
V1 V2 ... Vv
## 输出
最小距离和<br>
相应村庄编号
## 举例
10 5<br>
1 2 3 6 7 9 11 22 44 50<br>
9<br>
2 7 22 44 50 
## 题目分析
1. 当有N个村庄的时候，仅仅建立一个邮局，那么最方便的建立方式是在“中间”的那个村庄建立邮局，而不是其他的邮局。
2. 当邮局数目多于一个的时候，假设数目为X，由于要考虑到“最近”的问题，
   1. 第X个邮局的建立位置依赖于第X-1个的位置。
   2. 第X-1个邮局的建立位置依赖于第X-2个的位置。
   3. ...
3. 因此只有当前面的邮局建立方式是最优解，后面的建立才符合最优解，因此可以使用动态规划的方式求解。
## 状态转移式
假设F[i][j]为在前i个村庄中建立j个邮局的最短距离和<br>
使得F[i][j]可以成为最优的方法：
- 在前k个村庄中建立j-1个邮局，在k+1到i之间建立一个邮局（k >= 2 && k <=i - 1）
- 那么可以得到状态转移式：**F[i][j] = min(F[i][j], F[k][j - 1] + 在k+1到i之间建立一个邮局的距离和)**
由于我们需要知道在两个村庄之间建立一个邮局的距离和，因此设置一个矩阵matrix，用来存放两个乡村之间建立一个邮局的距离和
- 如何得到其值呢，假设road[]存储每个乡村的坐标？
- 在两个相邻乡村之间建立一个邮局的总距离一定是1
- 再加入一个邮局，只需要将上面偶数个的情况里面的邮局建在靠近新加入的邮局的地方即可
- 由此可以得到matrix的状态转移式：**matrix[i][j] = matrix[i][j - 1] + road[j] - road[(i + j)/2]**
##设计思路
```
    static final int INF = (int) ((int) 1.0 / 0.0);     // 用于表示距离无穷远
    static int[] road = new int[400];                   // 用于存储乡镇坐标
    static int[][] one = new int[400][400];             // 用于存储两个乡村之间建立一个邮局的距离和
    static int[][] table = new int[400][40];            // 在前i个村庄中建立j个邮局的最短距离和
    static ArrayList<Integer> res = new ArrayList<>();  // 邮局的位置
    static int[][] partition = new int[400][40];        // 划分矩阵，用于根据最终解回溯得到各个邮局的坐标
```
## 流程
1. 键盘输入
2. 按照升序排序乡村坐标
3. 首先初始化partition的所有元素为-1，并且根据上面提到的状态转移式填充one矩阵
4. 填充table
   1. 首先初始化最短距离和为无穷大
   2. 由于table[i][1]为在前i个乡村中建立一个邮局的距离和，与one意义相同，因此直接赋值。另外自己到自己的为0.
   3. 根据状态转移式逐次获得其余情况的最短距离和
      - 由于状态转移式是两部分的最小值，我们获取划分的依据是第一次取得最小值时候的位置k
5. 此时table[villages][offices]即为最小距离和
6. 获取邮局位置
   1. 根据partition矩阵获取最后一次的划分位置s
   2. 只要不是建立1个邮局（因为建立一个邮局的划分不在partition中涉及）
      - 向res中添加索引 (s + 当前未处理乡镇个数)/2 在road中对应的位置
      - 否则添加索引 (1 + 当前未处理乡镇个数)/2 在road中对应的位置（在这么多个乡村之间建立一个邮局不就简单了吗）