package entity;

import java.util.Vector;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/28
 * 描述信息: 小球组件
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-28 12:55] [周政伟][创建]
 */
public class Ball extends Gizmo implements UnResizable{
    /**
     * A time step has occurred. Do whatever is necessary
     * @param timeSinceLastStep:
     */
    void step(int timeSinceLastStep){

    }

    /**
     * Move the Gizmo by offset to a new position.
     * @param offset: a Vector<Float> with capacity of 2.
     * @modifies: this.position, boundingSphere
     */
    void move(Vector<Float> offset){

    }

    /**
     *
     * @param otherGizmo: an instantiated Gizmo (or Ball)
     * @param timeStep:
     * @return: are we actually colliding with the other Gizmo (or Ball)
     */
    boolean colliding(Gizmo otherGizmo, double timeStep){
        return true;
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
