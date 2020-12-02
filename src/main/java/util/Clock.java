package util;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: 周政伟的 mock，请自行创建。
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 23:48] [周政伟][创建]
 */
public interface Clock {
	void start();
	void stop();
	boolean isRunning();
	void addBoardView(BoardView view);
	void removeBoardView(BoardView view);

}