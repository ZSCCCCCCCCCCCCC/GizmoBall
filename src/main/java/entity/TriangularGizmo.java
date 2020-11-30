package entity;

import java.util.Vector;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/28
 * 描述信息: 三角形组件，可放大和旋转。
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-28 23:26] [周政伟][创建]
 */
public abstract class TriangularGizmo extends OrientatableGizmo{
    /**
     * 对三角形组件的重叠处理，将其视为一个正方形。
     * @param otherGizmo: 与之比较的另一个组件
     */
    @Override
    public boolean isOverlapped(Gizmo otherGizmo) {
        Gizmo replaceGizmo = new SquareBumper();
        replaceGizmo.setSize(size / 2)
                .setPosition(orient.dealPosForTriangular(position, size/2));
        return replaceGizmo.isOverlapped(otherGizmo);
    }
}
