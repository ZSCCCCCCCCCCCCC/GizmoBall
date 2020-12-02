package util;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: 周政伟的 mock， 请自己创建相关的类
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 19:29] [周政伟][创建]
 */
public class Display {
	private Board board;
	private File boardFile;
	private int clockTickSpeed;
	private Clock clock;
	private final GLBoardView boardView;
	private SwingKeyboardInput keyboardInput;
	private final GLCanvas boardCanvas;
	private final MenuListener pausingMenuListener =
			new MenuListener() {
		public void menuCanceled(MenuEvent e) { }
		public void menuDeselected(MenuEvent e) { }
		public void menuSelected(MenuEvent e) { stopTime(); }
	};
	private Gizmo currentGizmo = null;
	
	public Display() {
	}
}
