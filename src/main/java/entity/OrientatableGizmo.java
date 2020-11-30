package entity;

import exception.OverlapException;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/28
 * 描述信息: 可以改变方向（非对称）的组件。
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-28 14:41] [周政伟][创建]
 */
public abstract class OrientatableGizmo extends Gizmo{
    private static final Orient DEFAULT_ORIENT = Orient.RIGHT_UP_WARD; // 默认在 基点的右上方放置。

    protected Orient orient = DEFAULT_ORIENT; // 方向

    public Orient getOrient(){
        return this.orient;
    }

    /**
     * Set the orientation of the Gizmo, if applicable.
     */
    public void setOrient(Orient orient) throws OverlapException{
        this.parentBoard.removeGizmo(this);
        Orient preOrient = this.orient;
        this.orient = orient;

        try {
            parentBoard.addGizmo(this);
        } catch (OverlapException e) {
            this.orient = preOrient;
            try {
                parentBoard.addGizmo(this);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public abstract boolean isOverlapped(Gizmo otherGizmo);
}
