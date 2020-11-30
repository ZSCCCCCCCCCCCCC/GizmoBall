package entity;

import java.util.Vector;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/28
 * 描述信息: 枚举类：表示组件摆放的方向（上下左右）。
 * 使用 index 的位来表示其位置信息。
 *      01  |   00
 *      11  |   10
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-28 12:47] [周政伟][创建]
 */
public enum Orient {
    LEFT_UP_WARD(1), LEFT_DOWN_WARD(3), RIGHT_UP_WARD(0), RIGHT_DOWN_WARD(2);
    int index;

    Orient(int index){
        this.index = index;
    }

    /**
     * 使用 位运算 计算三角形的理论正方形的位置。
     * @param prePos: 三角形的位置。
     * @param new_size: 理论正方形的大小，即 三角形大小 & 理论正方形边长 的 1/2。
     * @return: 新的存储理论正方形位置的向量。
     */
    Vector<Float> dealPosForTriangular(Vector<Float> prePos, float new_size){
        Vector<Float> pos = new Vector<>();
        pos.set(0, prePos.get(0) + new_size * ((index & 1) == 1 ? -1 : 1));
        pos.set(1, prePos.get(1) + new_size * ((index & 2) == 2 ? -1 : 1));
        return pos;
    }

    /**
     * 计算弯轨道的理论正方形的位置。
     * @param prePos: 弯轨道的位置。
     * @return: 新的存储理论正方形位置的向量。
     */
    Vector<Float> dealPosForBentRail(Vector<Float> prePos){
        Vector<Float> pos = new Vector<>();
        pos.set(0, prePos.get(0) + (float) 1.5);
        pos.set(1, prePos.get(1) + (float) 1.5);
        return pos;
    }
}
