package onlab.leapMotion.Listener;

import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

import onlab.leapMotion.model.HandModel;
import onlab.metamodel.hand.*;


public class LeapListener extends Listener{
	HandModel model;
	ArrayList<Hand> hands;
	 ResourceSet reSet;
	public LeapListener(ResourceSet reSet){
		super();
		this.reSet = reSet;
        this.model = new HandModel();
        model.init();
		hands = new ArrayList<Hand>();
	}
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
    	//System.out.println(System.currentTimeMillis());
	    Frame frame = controller.frame();
	    if(hands.size() < frame.hands().count()){
	    	this.createHands(frame);
	    }
	    else if(hands.size() > frame.hands().count()){
	    	this.deleteHands(frame);
	    }
	    this.updateHands(frame);
	    System.out.println("frame");
    }
	private void createHands(Frame frame){
		while(hands.size() < frame.hands().count()){
			Hand hand = model.createHand();	    
	    	Resource res =   reSet.createResource((URI.createURI("D:/BME/Programozás/runtime-EclipseApplication/onlab.hand.leapModel/bin/onlab/hand/egy.hand")));
	    	res.getContents().add(hand);  
	    	hands.add(hand);
		}
	}
	private void deleteHands(Frame frame){
		while(hands.size()> frame.hands().count()){
			reSet.getResources().remove(hands.size() - 1);
			hands.remove(hands.get(hands.size()-1));
		}
	}
	public void updateHands(Frame frame){
		int i = 0;
		for(Hand hand : hands){
			if(frame.hands().get(i).isLeft()){
				hand.setType(HandType.LEFT);

			}
			else if(frame.hands().get(i).isRight()){
				hand.setType(HandType.RIGHT);
			}
			//Palm
			hand.getPalm().getPosition().setX(frame.hands().get(i).palmPosition().getX());
			hand.getPalm().getPosition().setY(frame.hands().get(i).palmPosition().getY());
			hand.getPalm().getPosition().setZ(frame.hands().get(i).palmPosition().getZ());
			com.leapmotion.leap.Vector v = new com.leapmotion.leap.Vector(0,1,0);
			hand.getPalm().setAngle(frame.hands().get(i).palmNormal().dot(v));

			//thumb
			this.updateFinger((FingerWith3Bones)hand.getThumb(), frame.hands().get(i).fingers().get(0));
			this.updateFinger(hand.getIndex(), frame.hands().get(i).fingers().get(1));
			this.updateFinger(hand.getMiddle(), frame.hands().get(i).fingers().get(2));
			this.updateFinger(hand.getRing(), frame.hands().get(i).fingers().get(3));
			this.updateFinger(hand.getPinky(), frame.hands().get(i).fingers().get(4));
			i++;
		}
	
	}
		public void updateFinger(FingerWith3Bones finger,com.leapmotion.leap.Finger lFinger){
			
			finger.getDistal().getPosition().setX(lFinger.bone(Bone.Type.TYPE_DISTAL).center().getX());
			finger.getDistal().getPosition().setY(lFinger.bone(Bone.Type.TYPE_DISTAL).center().getY());
			finger.getDistal().getPosition().setZ(lFinger.bone(Bone.Type.TYPE_DISTAL).center().getZ());
			
			finger.getDistal().getDirection().setX(lFinger.bone(Bone.Type.TYPE_DISTAL).direction().getX());
			finger.getDistal().getDirection().setY(lFinger.bone(Bone.Type.TYPE_DISTAL).direction().getY());
			finger.getDistal().getDirection().setZ(lFinger.bone(Bone.Type.TYPE_DISTAL).direction().getZ());
			
			
			finger.getProximal().getPosition().setX(lFinger.bone(Bone.Type.TYPE_PROXIMAL).center().getX());
			finger.getProximal().getPosition().setY(lFinger.bone(Bone.Type.TYPE_PROXIMAL).center().getY());
			finger.getProximal().getPosition().setZ(lFinger.bone(Bone.Type.TYPE_PROXIMAL).center().getZ());
			
			finger.getProximal().getDirection().setX(lFinger.bone(Bone.Type.TYPE_PROXIMAL).direction().getX());
			finger.getProximal().getDirection().setY(lFinger.bone(Bone.Type.TYPE_PROXIMAL).direction().getY());
			finger.getProximal().getDirection().setZ(lFinger.bone(Bone.Type.TYPE_PROXIMAL).direction().getZ());
			
			
			if(finger.getType()!=FingerType.THUMB){
				finger.getIntermediate().getPosition().setX(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).center().getX());
				finger.getIntermediate().getPosition().setY(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).center().getY());
				finger.getIntermediate().getPosition().setZ(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).center().getZ());
				
				finger.getIntermediate().getDirection().setX(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).direction().getX());
				finger.getIntermediate().getDirection().setY(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).direction().getY());
				finger.getIntermediate().getDirection().setZ(lFinger.bone(Bone.Type.TYPE_INTERMEDIATE).direction().getZ());
				
			}
		
		}
	
}

