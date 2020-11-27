package entity;

import com.sun.javafx.geom.Vec3d;
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
 */
public interface Gizmo extends TriggerListener, TriggerGenerator{
    double reflectionCoefficient = 0.0;
    Integer orient = null;
    String name = null;

    Vector<Float> position = null;
    Vector<Float> velocity = null;
    Sphere boundingSphere = null;
    Board board = null;

    /**
     * A time step has occurred. Do whatever is necessary
     * @param timeSinceLastStep:
     */
    void step(int timeSinceLastStep);

    /**
     * Move the Gizmo by offset to a new position.
     * @param offset: a Vec3d.
     * @modifies: this.position, boundingSphere
     */
    void move(Vec3d offset);

    /**
     * Move the Gizmo to the absolute position specified.
     * @param position: a Vec3d.
     * @modifies: this.position, boundingSphere
     */
    void moveTo(Vec3d position);

    /**
     * Set the parent board of this `Gizmo`.
     * This is important so we can know what how to find our peer `Gizmo`s to check for collisions, etc.
     * @param board: father board
     */
    Gizmo setBoard(Board board);

    /**
     * Set the orientation of the Gizmo, if applicable.
     */
    void setOrientation(Integer orient);

    /**
     * @param otherGizmo: an instantiated Gizmo
     * @return: Whether we are close enough to otherGizmo to start doing real math.
     */
    boolean proximate(Gizmo otherGizmo);

    /**
     *
     * @param otherGizmo: an instantiated Gizmo (or Ball)
     * @param timeStep:
     * @return: are we actually colliding with the other Gizmo (or Ball)
     */
    boolean colliding(Gizmo otherGizmo, double timeStep);

    Sphere getBoundingSphere();

    Vector<Float> getPosition();

    Vector<Float> getVelocity();

    Integer getOrient();

    double getReflectionCoefficient();

    String getName();

    Gizmo setName(String name);
}
