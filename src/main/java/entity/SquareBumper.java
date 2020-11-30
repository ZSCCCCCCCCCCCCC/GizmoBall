package entity;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/30
 * 描述信息: 方形反弹器。
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-30 12:49] [周政伟][创建]
 */
public class SquareBumper extends Gizmo{
    public SquareBumper(){
        this.parentBoard = null;
        this.size = DEFAULT_SIZE;
    }

    @Override
    public void registerTriggerListener(TriggerListener listener) {

    }

    @Override
    public void unregisterTriggerListener(TriggerListener listener) {

    }

    @Override
    public void trigger(TriggerGenerator trigger) {

    }
}
