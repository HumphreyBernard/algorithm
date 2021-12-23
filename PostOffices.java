package PostOffice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author Maximilian_Li
 */
public class PostOffices {
    static final int INF = (int) ((int) 1.0 / 0.0);
    static int[] road = new int[400];
    static int[][] one = new int[400][400];
    static int[][] table = new int[400][40];
    static ArrayList<Integer> res = new ArrayList<>();
    static int[][] partition = new int[400][40];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int villages = sc.nextInt();
        int offices = sc.nextInt();

        for (int i = 1; i <= villages; i++) {
            road[i] = sc.nextInt();
        }

        // 给输入按照升序排序
        for (int i = villages; i >= 1; i--) {
            for (int j = 1; j <= i; j++) {
                if (road[i] < road[j]) {
                    int temp = road[i];
                    road[i] = road[j];
                    road[j] = temp;
                }
            }
        }

        // 找到在村庄i到j之间建立一个邮局的距离和
        for (int i = 1; i <= villages; i++) {
            Arrays.fill(partition[i], -1);
            for (int j = i + 1; j <= villages; j++) {
                one[i][j] = one[i][j - 1] + road[j] - road[(i + j)/2];
            }
        }

        // 填充table
        for (int i = 1; i <= villages; i++) {
            for (int j = 1; j <= offices; j++) {
                table[i][j] = INF;
            }

            // 从村庄1开始到某一个村庄之间建立1个邮局就是one[1][i]
            table[i][1] = one[1][i];
            table[i][i] = 0;
        }

        for (int i = 1; i <= villages; i++) {
            for (int j = 2; j <= Math.min(offices, j); j++) {
                int minValue = INF;
                int l = INF;
                int r = INF;
                for (int k = j; k <= i - 1; k++) {
                    table[i][j] = Math.min(table[i][j], table[k][j - 1] + one[k + 1][i]);

//                    System.out.println("* table1[" + i + "][" + j + "] = min{table1[" + i + "][" + j +
//                            "], table1[" + k + "][" + (j - 1) + "] + one[" + (k + 1) + "][" + i + "]} = "
//                            + table1[i][j]);

                    if (table[i][j] < minValue) {
                        minValue = table[i][j];
                        l = k + 1;
                        r = i;
                        partition[i][j] = k + 1;
                    }
                }
            }
        }
        System.out.println(table[villages][offices]);

//        for (int i = 1; i <= villages; i++) {
//            for (int j = 1; j <= offices; j++) {
//                System.out.printf("%3d ", startIndex[i][j]);
//            }
//            System.out.println();
//        }

        // 邮局位置
        int a = villages;
        int b = offices;
        while (res.size() < offices) {
            int startPoint = partition[a][b];

            if (b == 1) {
                res.add(road[(1 + a) / 2]);
//                System.out.println("from " + 1 + " to " + a + ", office is at " + road[(1 + a) / 2]);
            } else {
                res.add(road[(startPoint + a) / 2]);
//                System.out.println("from " + startPoint + " to " + a + ", office is at " + road[(startPoint + a) / 2]);
            }

            a = startPoint - 1;
            b--;
        }

        // 升序输出
        Collections.sort(res);
        for (int i : res) {
            System.out.print(i + " ");
        }
        System.out.println();

/*        System.out.println("\n********* table1 *********");
        for (int i = 1; i <= villages; i++) {
            for (int j = 1; j <= offices; j++) {
                System.out.printf("%10d ", table1[i][j]);
            }
            System.out.println();
        }
*/
    }
}