package entity;

import exception.NonUniqueGizmoNameException;
import exception.OverlapException;
import exception.XMLLoadException;
import util.BoardBuilderXML;
import util.Display;
import util.JavaUtilTimerClock;
import util.KeyboardInput;

import java.io.File;
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
    private Display displayMode;
    private Vector<Integer> maxDims;
    private Vector<Float> friction;
    private double gravity;
    private KeyboardInput inputMode;
    private JavaUtilTimerClock boardClock;

    private List<Gizmo> boardGizmos;

    /**
     * Creates a board by passing in all existing member variables.
     * * If friction or dimensions do not meet their specified capacities default values of
     * *  (0.025/s,0.025/L) and (20,20) respectively are used.
     * @param maxDims: the x, y max coordinates
     * @param gravity: coefficient of gravity
     * @param friction: coefficient of friction
     * @param displayMode: which display to be used (ascii, gl, etc)
     * @param inputMode: KeyboardInput
     * @require: xMax, yMax, grav, friction >= 0, dimensions.capacity = 2 friction.capacity = 2.
     * @effects: Creates a new Board
     * @modifies: All member variables
     */
    public Board(Vector<Integer> maxDims, double gravity,
                 Vector<Float> friction, Display displayMode, KeyboardInput inputMode){
        assert (gravity > 0.0);
        assert (displayMode != null && inputMode != null);

        if(maxDims.capacity() == 2) {
            assert (maxDims.get(0) > 0 && maxDims.get(1) > 0);
            this.maxDims = maxDims;
        } else {
            this.maxDims = new Vector<>(2, 0);
            this.maxDims.add(20);
            this.maxDims.add(20);
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

        this.displayMode = displayMode;
        this.inputMode = inputMode;
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
        the current or eventual(最终的) max x, y coordinates of the Board
     * @effects: adds a new gizmo to the boardGizmos list if no overlapping exists, otherwise,
        does nothing but throws a OverlapException.
     * @modifies: boardGizmos
     * @throws NonUniqueGizmoNameException: if someone tries to add a gizmo that does not have a unique name.
     */
    public boolean addGizmo(Gizmo gizmo) throws NonUniqueGizmoNameException, OverlapException {
        assert (gizmo.getPosition().get(0) < maxDims.get(0));
        assert (gizmo.getPosition().get(1) < maxDims.get(1));

        if(!isNameUnique(gizmo.getName())){
            throw new NonUniqueGizmoNameException();
        } else if(checkOverlapping(gizmo)){
            throw new OverlapException();
        } else {
            boardGizmos.add(gizmo);
            return true;
        }
    }

    public void addGizmos(Collection<Gizmo> gizmos) throws NonUniqueGizmoNameException, OverlapException{
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

    /**
     * @param gizmo: the gizmo to be checked out
     * @return: the removed gizmo, if the gizmo is not in the list returns null
     * @require: gizmo is in boardGizmos
     * @effects: removes gizmo from boardGizmos, if it exists, and returns it to the caller.
     * @modifies: boardGizmos.
     */
    public Gizmo checkoutGizmo(Gizmo gizmo){
        if(removeGizmo(gizmo))
            return gizmo;
        return null;
    }

    /**
     * Saves the Board to an xml file at pathName
     * @param pathName: a xml file in an existing folder
     * @effects: Creates a .xml file representing this that can be loaded later.
     * Note that information on the board's dimension, the input type, clock and other state specific information
     *  is not included in the xml file.
     */
    public void saveToFile(File pathName){
        new BoardBuilderXML().buildFileFromBoard(this, pathName);
    }

    /**
     *
     * @param gizmo: the gizmo to be removed
     * @return: a boolean indicating true if the gizmo existed in the board and was removed and false otherwise
     * @effects: removes the first `Gizmo` object in boardGizmos that .equals gizmo
     * @modifies: boardGizmos
     */
    public boolean removeGizmo(Gizmo gizmo){
        return boardGizmos.remove(gizmo);
    }

    /**
     * Step will only run if it is not currently being called
     * AND the board is 'ready' as determined by the method isBoardReady().
     *
     * Calls to step will be not attempt to step the components of the board
     *  if the board is not ready and the method call will return false.
     *
     * The method will still wait for a step method to finish if it is in progress even if the board isn't ready.
     *
     * If step is currently being run, step will wait for the previous call to finish and then run.
     *
     * As such, a good program will make sure that it isn't making multiple simultaneous calls to step
     * and will be sure that the board is ready before calling step in order to avoid extraneous steps.
     *
     * @param timeSinceLastStep: int
     * @return: Whether or not the board was ready and steps were called on board elements.
     * @throws InterruptedException:  if the current thread is interrupted
     * @effects: calls step on all board elements, effectively stepping the board.
     * TODO: step method.
     */
    public boolean step(int timeSinceLastStep) throws InterruptedException{
        return true;
    }

    /**
     * Checks to see if the board is fully initialized
     * @return: true if xyMaxDims are all > 0, inputMode != null, displayMode != null, boardClock != null
     */
    public boolean isBoardReady(){
        return maxDims.get(0) > 0 && maxDims.get(1) > 0 && inputMode != null && displayMode != null && boardClock != null;
    }

    /**
     * Check to see if checkedGizmo is overlapped by the `Gizmo`s in boardGizmos.
     * @param checkedGizmo: the `Gizmo` being added to boardGizmos.
     */
    public boolean checkOverlapping(Gizmo checkedGizmo){
        for (Gizmo gizmo : boardGizmos) {
            if(checkedGizmo.isOverlapped(gizmo)){
                return true;
            };
        }
        return false;
    }

    /**
     * @return: true if isBoardReady() is true and all gizmos are inside of the maximum dimensions of the board.
     * TODO: all gizmos are inside of the maximum dimensions of the board .
     */
    public boolean checkGizmos(){
        return isBoardReady();
    }

    public Display getDisplayMode(){
        return this.displayMode;
    }

    public Board setDisplayMode(Display displayMode){
        this.displayMode = displayMode;
        return this;
    }

    public KeyboardInput getInputMode(){
        return this.inputMode;
    }

    public Board setInputMode(KeyboardInput inputMode){
        this.inputMode = inputMode;
        return this;
    }

    public Vector<Integer> getMaxDims(){
        return this.maxDims;
    }

    public Board setMaxDims(Vector<Integer> maxDims){
        this.maxDims = maxDims;
        return this;
    }

    public Vector<Float> getFriction(){
        return this.friction;
    }

    public Board setFriction(Vector<Float> friction){
        this.friction = friction;
        return this;
    }

    public double getGravity(){
        return this.gravity;
    }

    public Board setGravity(double gravity){
        this.gravity = gravity;
        return this;
    }

    public Set<String> getNames(){
        Set<String> namesSet = new HashSet<>();
        for (Gizmo gizmo : boardGizmos) {
            namesSet.add(gizmo.getName());
        }
        return namesSet;
    }

    /**
     * Returns an iterator on an unmodifiable boardGizmos.
     * @return: the iterator of the boardGizmos list
     */
    @Override
    public Iterator<Gizmo> iterator() {
        return boardGizmos.iterator();
    }
}
