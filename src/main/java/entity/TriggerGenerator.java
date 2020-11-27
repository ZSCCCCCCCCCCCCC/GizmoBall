package entity;

import java.util.LinkedList;
import java.util.List;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: An object that can trigger `TriggerListener`s.
 * Each `TriggerGenerator` specifies some event that causes it to generate a trigger.
 * `TriggerListener`s may then register themselves with a TriggerGenerator to be notified when such events happen.
 * * Classes that implement this interface should specify what events cause them to generate triggers.
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27] [周政伟][变更描述]
 */
public interface TriggerGenerator {
    String rcsId = "202011271247";
    List<TriggerListener> triggerListeners = new LinkedList<>();

    /**
     * Adds a `TriggerListener` to the list of listeners to be triggered by this TriggerGenerator.
     * Whenever an event that causes this `TriggerGenerator` to generate triggers,
     *  this will call the TriggerListener.trigger(TriggerGenerator) method of listener.
     * @param listener:  the listener to be added to this.triggerListeners
     * @require: listener != null and listener not in this.triggerListeners
     * @effects: this.triggerListeners += listener
     */
    void registerTriggerListener(TriggerListener listener);

    /**
     * Removes a `TriggerListener` from the list of listeners to be triggered by this `TriggerGenerator`.
     * After this method is called, listener will no longer be triggered by this `TriggerGenerator`.
     * @param listener: the listener to be removed from this.triggerListeners
     * @require: listener != null and listener in this.triggerListeners
     * @effects: this.triggerListeners -= listener
     */
    void unregisterTriggerListener(TriggerListener listener);
}
