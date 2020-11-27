package entity;

import exception.NonUniqueGizmoNameException;
import exception.XMLLoadException;
import util.BoardBuilderXML;
import util.Display;
import util.KeyboardInput;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: `Board` contains a synchronized list of all of the gizmos that exist on the board.
 * Since modifications to these gizmos are done by returning an iterator
 *  of shallow copies of the gizmo objects
 * it is important that the list not be modified by two different threads simultaneously.
 *
 * RI: Since board is fully mutable, no representation is always required.
 * Instead, the user is encouraged to use isBoardReady() to find out
 *  if the board is willing to step through time or not.
 * Such a call will check to make sure that all member variables have been initialized to non-empty values.
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 19:10] [周政伟][创建]
 */
public class Board implements Iterable<Gizmo>{
    public static final String rcsId = "202011271247";
    private Display display;
    private Vector<Integer> dimensions;
    private final Vector<Float> friction;
    private final double gravity;
    private KeyboardInput input;

    private List<Gizmo> boardGizmos;

    /**
     * Creates a board by passing in all existing member variables.
     * * If friction or dimensions do not meet their specified capacities default values of
     * *  (0.025/s,0.025/L) and (20,20) respectively are used.
     * @param dimensions: the x, y max coordinates
     * @param gravity: coefficient of gravity
     * @param friction: coefficient of friction
     * @param display: which display to be used (ascii, gl, etc)
     * @param input: KeyboardInput
     * @require: xMax, yMax, grav, friction >= 0, dimensions.capacity = 2 friction.capacity = 2.
     * @effects: Creates a new Board
     * @modifies: All member variables
     */
    public Board(Vector<Integer> dimensions, double gravity,
                 Vector<Float> friction, Display display, KeyboardInput input){
        assert (gravity > 0.0);
        assert (display != null && input != null);

        if(dimensions.capacity() == 2) {
            assert (dimensions.get(0) > 0 && dimensions.get(1) > 0);
            this.dimensions = dimensions;
        } else {
            this.dimensions = new Vector<>(2, 0);
            this.dimensions.add(20);
            this.dimensions.add(20);
        }

        this.gravity = gravity;

        if(friction.capacity() == 2) {
            assert (friction.get(0) >= 0.0 && friction.get(1) >= 0.0);
            this.friction = friction;
        } else {
            this.friction = new Vector<>(2, 0);
            this.friction.add((float) 0.025);
            this.friction.add((float) 0.025);
        }

        this.display = display;
        this.input = input;
        this.boardGizmos = new LinkedList<>();
    }

    /**
     * Creates a fairly default, for use by BoardBuilderXML.
     * * If friction is incomplete (friction1 and friction2 are not specified)
     *  the default value of (0.025/s,0.025/L) is used.
     * @param gravity: coefficient of gravity
     * @param friction: coefficient of friction
     * @require: friction.capacity = 2.
     * @effects: Creates a new Board
     * @modifies: All member variables
     */
    public Board(double gravity, Vector<Float> friction){
        assert (gravity > 0.0);

        if(friction.capacity() == 2) {
            assert (friction.get(0) >= 0.0 && friction.get(1) >= 0.0);
            this.friction = friction;
        } else {
            this.friction = new Vector<>(2, 0);
            this.friction.add((float) 0.025);
            this.friction.add((float) 0.025);
        }

        this.gravity = gravity;
    }

    /**
     * Thrown to indicate an error when loading a board XML file.
     *
     * TODO: More detailed specifications.
     * @param filename: a XML file's path.
     * @param boardInput: KeyboardInput
     * @return: a Board created by XML file and boardInput.
     * @throws XMLLoadException: can't convert the XML file to a `Board` Class.
     */
    public static Board createFromFile(String filename, KeyboardInput boardInput)
            throws XMLLoadException {
        try {
            return new BoardBuilderXML().buildBoardFromFile(new URL(filename), boardInput);
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param gizmo: gizmo to be added.
     * @require: the gizmo's x, y coordinates are inside of
     *  the current or eventual(最终的) max x, y coordinates of the Board
     * @effects: adds a new gizmo to the boardGizmos list
     * @modifies: boardGizmos
     * @throws NonUniqueGizmoNameException: if someone tries to add a gizmo that does not have a unique name.
     */
    public void addGizmo(Gizmo gizmo) throws NonUniqueGizmoNameException {
        assert (gizmo.getPosition().get(0) < dimensions.get(0));
        assert (gizmo.getPosition().get(1) < dimensions.get(1));

        if(isNameUnique(gizmo.getName()))
            boardGizmos.add(gizmo);
        else{
            throw new NonUniqueGizmoNameException();
        }
    }

    public void addGizmos(Collection<Gizmo> gizmos) throws NonUniqueGizmoNameException{
        for (Gizmo gizmo : gizmos) {
            addGizmo(gizmo);
        }
    }

    /**
     * Checks to see if name is the name of a gizmo already belonging to the board.
     * @return: whether the name is unique in the board.
     */
    public boolean isNameUnique(String name){
        for (Gizmo gizmo : boardGizmos) {
            if (gizmo.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Gizmo> iterator() {
        return boardGizmos.iterator();
    }
}
