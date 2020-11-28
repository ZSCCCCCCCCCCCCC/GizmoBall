package entity;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/28
 * 描述信息: 三角形组件，可放大和旋转。
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-28 23:26] [周政伟][创建]
 */
public abstract class TriangularGizmo extends OrientatableGizmo{
    @Override
    public boolean isOverlapped(Gizmo otherGizmo) {
        return true;
    }
}
