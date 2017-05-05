package onlab.hand.cepExample.LeapListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import com.leapmotion.leap.Bone;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

import onlab.hand.cepExample.model.HandModel;
import onlab.metamodel.hand.*;


public class LeapListener extends Listener{
	HandModel model;
	HashMap<Integer,Hand> hands;
	ResourceSet reSet;
	public LeapListener(ResourceSet reSet){
		super();
		this.reSet = reSet;
        this.model = new HandModel();
        model.init();
		hands = new HashMap<Integer,Hand>();
	}
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
	    Frame frame = controller.frame();
	    com.leapmotion.leap.Hand leapHand = null;
	    Set<Integer> IDs = new HashSet<Integer>(hands.keySet());
	    for(int i = 0; i< frame.hands().count();i++){
	    	leapHand = frame.hands().get(i);
	    	Integer ID = frame.hands().get(i).id();
	    	if(hands.get(ID) == null){
	    		try{
	    		this.createHands(ID, leapHand);
	    		this.updateHand(ID, leapHand);
	    		}
	    		catch(NullPointerException e){
	    			System.out.println(e.getMessage());
	    		}
	    	}
	    	else{
	    		IDs.remove(ID);
	    		this.updateHand(ID, leapHand);
	    	}
	    }
	    if(IDs.size()!=0){
	    	this.deleteHands(IDs);
	    }
    }
	private synchronized void createHands(Integer ID, com.leapmotion.leap.Hand leapHand){
		if(leapHand != null && ID != null){
			String left = leapHand.isLeft() ? "LEFT" : "RIGHT";
			System.out.println("new " + left + " hand -  ID: " + ID);
			Hand hand = model.createHand(ID,leapHand.isLeft());	   	
		    Resource res =   reSet.createResource((URI.createURI("D:/BME/Programozás/runtime-EclipseApplication/onlab.hand.leapModel/bin/onlab/hand/egy.hand")));
		    res.getContents().add(hand);  
		    hands.put(ID,hand);
		}
		else{
			throw new NullPointerException();
		}
	}
	
	private synchronized void deleteHands(Set<Integer> IDs){
		for(Integer ID : IDs){
			Hand hand = hands.get(ID);
			hands.remove(ID);
			reSet.getResources().remove(hand);
			System.out.println("hand deleted -  ID: " + ID);
		}
	}
	
	public synchronized void updateHand(int ID ,com.leapmotion.leap.Hand leapHand){
		Hand hand = hands.get(ID);
		//Palm
		hand.getPalm().getPosition().setX(leapHand.palmPosition().getX());
		hand.getPalm().getPosition().setY(leapHand.palmPosition().getY());
		hand.getPalm().getPosition().setZ(leapHand.palmPosition().getZ());
		com.leapmotion.leap.Vector v = new com.leapmotion.leap.Vector(0,1,0);
		hand.getPalm().setAngle(leapHand.palmNormal().dot(v));

		//fingers
		this.updateFinger((FingerWith3Bones)hand.getThumb(), leapHand.fingers().get(0));
		this.updateFinger(hand.getIndex(), leapHand.fingers().get(1));
		this.updateFinger(hand.getMiddle(), leapHand.fingers().get(2));
		this.updateFinger(hand.getRing(), leapHand.fingers().get(3));
		this.updateFinger(hand.getPinky(),leapHand.fingers().get(4));
	}
	
		public synchronized void updateFinger(FingerWith3Bones finger,com.leapmotion.leap.Finger lFinger){
			
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

