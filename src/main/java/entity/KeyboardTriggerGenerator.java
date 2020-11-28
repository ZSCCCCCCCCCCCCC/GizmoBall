package entity;

import util.EventType;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: A `TriggerGenerator` whose trigger event is the pressing down or releasing of a keyboard key.
 * `TriggerListener`s may register themselves with a `KeyboardTriggerGenerator` to be triggered
 *   when a certain key is pressed down or released.
 *
 * Note that most other modules should not be constructing their own `KeyboardTriggerGenerator`s;
 *  which should be left to `KeyboardInput` implementations.
 * `TriggerListener`s that simply want to register themselves with a `KeyboardTriggerGenerator`
 *   should retrieve the `KeyboardTriggerGenerator` from the appropriate `KeyboardInput` instance.
 *
 * Generally, `KeyboardTriggerGenerator`s are dumb; they do not actually detect keyboard events themselves.
 *  Instead, they rely on the `KeyboardInput` instance that creates them to notify them
 *      via activate() of the keyboard events that should be causing them
 *       to trigger all the `TriggerListener`s that have registered themselves with them.
 *
 * Depending on the implementation of `KeyboardInput` that is managing this `KeyboardTriggerGenerator`,
 *  this `KeyboardTriggerGenerator` may be the `TriggerGenerator` for more than one ASCII character code.
 *
 * * For example, a `KeyboardInput` module may map both the ASCII character code for 'A' and 'a'
 * *  to the same `KeyboardTriggerGenerator`, considering both to refer to the same physical 'A' key.
 *
 * However, this `KeyboardTriggerGenerator` saves just a single ASCII character code as the canonical(规范) code
 *  that it is associated with. This code can then be used to consistently refer to this `KeyboardTriggerGenerator`,
 *
 *  * for example, when saving a `Board` and its connections to a file.
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 17:34] [周政伟][创建]
 */

public class KeyboardTriggerGenerator implements TriggerGenerator {
    private int asciiCode;
    private EventType keyDirection;

    /**
     * Constructs a new `KeyboardTriggerGenerator`.
     * @param asciiCode: the canonical ASCII code corresponding to the key this `KeyboardTriggerGenerator` triggers on.
     * @param keyDirection: whether this `KeyboardTriggerGenerator` triggers on the key down or the key up event
     * @require: 0 <= asciiCode <= 127 and keyDirection != null
     * @effects: this.asciiCode = asciiCode, this.keyDirection = keyDirection, this.triggerListeners = null
     */
    public KeyboardTriggerGenerator(int asciiCode, EventType keyDirection){
        assert (asciiCode >=0 && asciiCode <=127);
        assert (keyDirection != null);
        this.asciiCode = asciiCode;
        this.keyDirection = keyDirection;
    }

    /**
     * Same as interface 'TriggerGenerator'
     */
    @Override
    public void registerTriggerListener(TriggerListener listener) {
        triggerListeners.add(listener);
    }

    /**
     * Same as interface 'TriggerGenerator'
     */
    @Override
    public void unregisterTriggerListener(TriggerListener listener) {
        triggerListeners.remove(listener);
    }
}
