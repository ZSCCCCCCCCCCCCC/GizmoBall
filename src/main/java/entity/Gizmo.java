package entity;

import exception.OverlapException;

import java.util.Vector;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: A `Gizmo` is an interface that models the physics and game logic associated with game widgets (Gizmos).
 *  A `Gizmo` can be a Ball, as well as flippers, bouncers, absorbers, etc.
 *  A Gizmo is a mutable object.
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 18:07] [周政伟][创建]
 * @update [2][2020-11-28 12:07] [周政伟][修改物理属性(大小、方向)，删除属性(速度)]
 * @update [3][2020-11-28 14:53] [周政伟][将方向属性分离为子类，大小分离为子类]
 */
public abstract class Gizmo implements TriggerListener, TriggerGenerator{
    protected static final float DEFAULT_SIZE = 1;

    private double reflectionCoefficient = 0.0; // 摩擦系数
    protected float size = DEFAULT_SIZE; // 组件的大小

    private String name = null; // 名称

    protected Vector<Float> position = null; // 组件的 基点位置
    protected Board parentBoard = null;

    /**
     * 对没有添加到 Board 中的组件设置位置（只用于初始化）。
     */
    public Gizmo setPosition(Vector<Float> position){
        this.position = new Vector<>(position);
        return this;
    }

    /**
     * Move the Gizmo to the absolute position specified if no overlapping happens,
        otherwise, recover to the previous position.
     * @param position: a Vector<Float>  with capacity of 2.
     * @modifies: this.position
     */
    public void moveTo(Vector<Float> position) throws OverlapException {
        assert (position.capacity() == 2);
        assert (position.get(0) > 0 && position.get(1) > 0);

        parentBoard.removeGizmo(this);

        Vector<Float> prePos = position;
        this.position = position;

        try{
            parentBoard.addGizmo(this);
        } catch (OverlapException e){
            this.position = prePos;
            try {
                parentBoard.addGizmo(this);
            }catch (Exception exception){
                exception.printStackTrace();
            }
            throw e;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 对没有添加到 Board 中的组件设置大小（只用于初始化）。
     */
    public Gizmo setSize(float size){
        this.size = size;
        return this;
    }

    /**
     * 改变 组件的 大小，对于三角形可能需要覆写。
     * @param size: 组件的新大小。
     * @require: 只有可放大的组件才可以调用此方法。
     */
    public void changeSize(float size) throws OverlapException{
        assert (! (this instanceof UnResizable)); // 不可以放大的组件。

        // 缩小视图不需要覆盖检测。
        if (this.size >= size) {
            this.size = size;
        }
        parentBoard.removeGizmo(this);
        float preSize = this.size;
        this.size = size;
        try {
            parentBoard.addGizmo(this);
        } catch (OverlapException e) {
            this.size = preSize;
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

    public float getSize(){
        return this.size;
    }

    /**
     * Set the parent board of this `Gizmo`.
     * This is important so we can know what how to find our peer `Gizmo`s to check for collisions, etc.
     * @param board: father board
     */
    public Gizmo setBoard(Board board){
        this.parentBoard = board;
        return this;
    }

    /**
     * @param otherGizmo: an instantiated Gizmo
     * @return: Whether it is overlapped to otherGizmo.
     * Note: object which extends `OrientableGizmo` should override this method.
     */
    public boolean isOverlapped(Gizmo otherGizmo){
        // otherGizmo 是一个 可定向组件。
        if(otherGizmo instanceof OrientatableGizmo)
            return otherGizmo.isOverlapped(this);
        // 俩者都是 不可定向（对称）组件。
        float sizeSum = this.size + otherGizmo.size;
        // x 反向重叠 && y方向重叠
        return Math.abs(this.position.get(0) - otherGizmo.position.get(0)) < sizeSum
                    && (Math.abs(this.position.get(1) - otherGizmo.position.get(1)) < sizeSum);
    }

    public Vector<Float> getPosition(){
        return this.position;
    }

    public double getReflectionCoefficient(){
        return this.reflectionCoefficient;
    }

    public Gizmo setReflectionCoefficient(double coefficient){
        this.reflectionCoefficient = coefficient;
        return this;
    }

    public String getName(){
        return this.getName();
    }

    public Gizmo setName(String name){
        this.name = name;
        return this;
    }
}
