package util;

import entity.Board;
import exception.XMLLoadException;

import java.io.File;
import java.net.URL;

import java.util.ArrayList;
import java.util.Map;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
/**
 * 项目名称: GizmoBall
 * 创建时间: 2020/11/27
 * 描述信息: 周政伟的 mock，请自行创建
 *
 * @author <a href="mail to: 10185101124@stu.ecnu.edu.cn" rel="nofollow">周政伟</a>
 * @update [1][2020-11-27 20:13] [周政伟][创建]
 */
public class BoardBuilderXML {
	
public void buildFileFromBoard(Board newBoard, File outputFile){
	ArrayList<XML> gizmos = new ArrayList<XML>();
	ArrayList<XML> balls = new ArrayList<XML>();
	XML connectionsXML = new XML("connections");
	for(Gizmo gizmo: newBoard){
		if(gizmo.getTriggerGenerators() != null){
			for(TriggerGenerator tg: gizmo.getTriggerGenerators()){
				if (tg instanceof KeyboardTriggerGenerator){
					String keyDirection;
					if(((KeyboardTriggerGenerator) tg).getKeyDirection() == 
							KeyboardInput.EventType.KEY_DOWN){
						keyDirection = "down";
					}
					else{
						keyDirection = "up";
					}
					connectionsXML.addElement(new XML("keyConnect", false)
							.addXMLAttribute("key",Integer.toString(
									((KeyboardTriggerGenerator) tg).getAsciiCode()))
							.addXMLAttribute("keyDirection", keyDirection)
                           .addXMLAttribute("targetGizmo",gizmo.getName())
							);
				}
				if (tg instanceof Gizmo){
					connectionsXML.addElement(new XML("connect", false)
							.addXMLAttribute("sourceGizmo", ((Gizmo) tg).getName())
							.addXMLAttribute("targetGizmo", gizmo.getName())
							);
				}
				else{
                        	   System.out.println(tg.getClass());
				}
			}
		}
		if(gizmo instanceof Ball){
			balls.add(new XML("ball",false)
					.addXMLAttribute("yVelocity",Double.toString(gizmo.getVelocity().y()))
					.addXMLAttribute("xVelocity",Double.toString(gizmo.getVelocity().x()))
					.addXMLAttribute("y",Double.toString(gizmo.getPosition().y()))
					.addXMLAttribute("x",Double.toString(gizmo.getPosition().x()))
					.addXMLAttribute("name", gizmo.getName())
					);
		}
		else if(gizmo instanceof Absorber){
			Absorber g = (Absorber) gizmo;
			gizmos.add(new XML(xmlTag(gizmo),false)
					.addXMLAttribute("y",Integer.toString(new Double(gizmo.getPosition().y()).intValue()))
					.addXMLAttribute("x",Integer.toString(new Double(gizmo.getPosition().x()).intValue()))
					.addXMLAttribute("width",Integer.toString(g.getWidth()))
					.addXMLAttribute("height",Integer.toString(g.getHeight()))
					.addXMLAttribute("name", gizmo.getName())
					.addXMLAttribute("delay", Integer.toString(gizmo.getDelay()))
					);
		}
		else if(gizmo instanceof LeftFlipper || 
				gizmo instanceof RightFlipper ||
				gizmo instanceof TriangularBumper){
			gizmos.add(new XML(xmlTag(gizmo),false)
					.addXMLAttribute("y",Integer.toString(new Double(gizmo.getPosition().y()).intValue()))
					.addXMLAttribute("x",Integer.toString(new Double(gizmo.getPosition().x()).intValue()))
					.addXMLAttribute("orientation",Integer.toString(gizmo.getOrientation()))
					.addXMLAttribute("name", gizmo.getName())
					.addXMLAttribute("delay", Integer.toString(gizmo.getDelay()))
					);
		}
		else if(gizmo instanceof OuterWalls){
		}
		else{
			gizmos.add(new XML(xmlTag(gizmo),false)
					.addXMLAttribute("y",Integer.toString(new Double(gizmo.getPosition().y()).intValue()))
					.addXMLAttribute("x",Integer.toString(new Double(gizmo.getPosition().x()).intValue()))
					.addXMLAttribute("name", gizmo.getName())
					.addXMLAttribute("delay", Integer.toString(gizmo.getDelay())));
		}
	}
	XML boardXML = new XML("board")
			.addXMLAttribute("xGravity",Double.toString(newBoard.getGravity().x()))
			.addXMLAttribute("yGravity",Double.toString(newBoard.getGravity().y()))
			.addXMLAttribute("friction1",Float.toString(newBoard.getFriction().elementAt(0)))
			.addXMLAttribute("friction2",Float.toString(newBoard.getFriction().elementAt(1)));
	for(XML ball: balls){
		boardXML.addElement(ball);
	}
	XML gizmosXML = new XML("gizmos");
	for(XML g: gizmos){
		gizmosXML.addElement(g);
	}
	gizmosXML.setPrettyPrint(true);
	boardXML.addElement(gizmosXML);
	boardXML.addElement(connectionsXML);
	boardXML.setPrettyPrint(true);
}

	
public Board buildBoardFromFile(URL url, KeyboardInput keyboardInput) throws XMLLoadException {
	Document document = getDomFromXML(url);
	Node boardNode = document.getElementsByTagName("board").item(0);
	Board returnBoard = makeSkeletonBoard(boardNode);
	returnBoard.setInputMode(keyboardInput);
	Map<Gizmos,NodeList> gizmoNodeLists = new TreeMap<Gizmos,NodeList>();
	for(Gizmos g: Gizmos.values()){
		if(document.getElementsByTagName(g.xmlTag()) != null){
			gizmoNodeLists.put(g, document.getElementsByTagName(g.xmlTag()));   
		}
	}
		Map<String,Gizmo> gizmoList = makeGizmosFromDOM(gizmoNodeLists);
		returnBoard.addGizmos(gizmoList.values());
		NodeList connectList = document.getElementsByTagName("connect");
		for(int i = 0, j = connectList.getLength(); i<j;i++){
			String sourceGizmo = "", targetGizmo = "";
			Node connectNode = connectList.item(i);
			NamedNodeMap connectNodeMap = connectNode.getAttributes();
			if(connectNodeMap.getNamedItem("sourceGizmo")!=null){
				sourceGizmo = connectNodeMap.getNamedItem("sourceGizmo").getNodeValue();
			}
			if(connectNodeMap.getNamedItem("targetGizmo")!=null){
				targetGizmo = connectNodeMap.getNamedItem("targetGizmo").getNodeValue();
			}
			if (debug > 0)
				System.out.println("Going to setup a trigger listener");
			Gizmo sourceGizmoObject = gizmoList.get(sourceGizmo);
			Gizmo targetGizmoObject = gizmoList.get(targetGizmo);
			if (debug > 0)
				System.out.println(sourceGizmoObject + "=>" +
						targetGizmoObject);
			targetGizmoObject.connectToTriggerGenerator(sourceGizmoObject);
		}
		NodeList keyConnectList = document.getElementsByTagName("keyConnect");
		for(int i = 0, j = keyConnectList.getLength(); i<j;i++){
			String keyString = "", keyDirectionString = "", targetGizmo = "";
			int key = 0;
			KeyboardInput.EventType keyDirection;
			Node connectNode = keyConnectList.item(i);
			NamedNodeMap connectNodeMap = connectNode.getAttributes();
			if(connectNodeMap.getNamedItem("key")!=null){
				keyString = connectNodeMap.getNamedItem("key").getNodeValue();
			}
			key = Integer.parseInt(keyString);
		}
		if(connectNodeMap.getNamedItem("keyDirection")!=null){
			keyDirectionString = connectNodeMap.getNamedItem("keyDirection").getNodeValue();
		}
		if (keyDirectionString.equals("up")) {
			keyDirection = KeyboardInput.EventType.KEY_UP;
		} else if (keyDirectionString.equals("down")){
			keyDirection = KeyboardInput.EventType.KEY_DOWN;
		}
		if(connectNodeMap.getNamedItem("targetGizmo")!=null){
			targetGizmo = connectNodeMap.getNamedItem("targetGizmo").getNodeValue();
		}
		if(gizmoList.containsKey(targetGizmo)){
			Gizmo gizmo = gizmoList.get(targetGizmo);
			gizmo.connectToTriggerGenerator(keyboardInput.
					getKeyboardTriggerGenerator(key, keyDirection));
		}

return null;
}
}
