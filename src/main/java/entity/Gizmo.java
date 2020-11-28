package entity;

import javafx.scene.shape.Sphere;

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
 */
public abstract class Gizmo implements TriggerListener, TriggerGenerator{
    private static final int DEFAULT_SIZE = 1;
    private static final Orient DEFAULT_ORIENT = Orient.RIGHT_UP_WARD; // 默认在 基点的右上方放置。

    private double reflectionCoefficient = 0.0; // 摩擦系数
    private int size = DEFAULT_SIZE; // 组件的
    private Orient orient = DEFAULT_ORIENT; // 方向
    private String name = null; // 名称

    private Vector<Float> position = null; // 组件的 基点位置
    private Board parentBoard = null;

    /**
     * Move the Gizmo to the absolute position specified.
     * @param position: a Vector<Float>  with capacity of 2.
     * @modifies: this.position, boundingSphere
     * TODO
     */
    public void moveTo(Vector<Float> position){

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
     * @return: Whether we are close enough to otherGizmo to start doing real math.
     * TODO
     */
    public boolean proximate(Gizmo otherGizmo){
        return true;
    }

    public Vector<Float> getPosition(){
        return this.position;
    }

    public Orient getOrient(){
        return this.orient;
    }

    /**
     * Set the orientation of the Gizmo, if applicable.
     * TODO: detect
     */
    public Gizmo setOrient(Orient orient){
        this.orient = orient;
        return this;
    }

    public int getSize(){
        return this.size;
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
