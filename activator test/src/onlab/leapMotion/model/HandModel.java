package onlab.leapMotion.model;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import onlab.metamodel.hand.*;
public class HandModel {
	public Hand createHand(){
		handFactory factory = handFactory.eINSTANCE;
		Hand hand =  factory.createHand();
		
		Palm palm = factory.createPalm();
		addPosition(palm,factory);
		hand.setPalm(palm);
		
		FingerWith3Bones thumb = factory.createFingerWith3Bones();
		thumb.setType(FingerType.THUMB);
		addBones(thumb, factory);
		hand.setThumb((FingerWith2Bones) thumb);
		
		FingerWith3Bones index = factory.createFingerWith3Bones();
		index.setType(FingerType.INDEX);
		addBones(index, factory);
		hand.setIndex(index);
		
		FingerWith3Bones middle = factory.createFingerWith3Bones();
		middle.setType(FingerType.MIDDLE);
		addBones(middle, factory);
		hand.setMiddle(middle);
		
		FingerWith3Bones ring = factory.createFingerWith3Bones();
		ring.setType(FingerType.RING);
		addBones(ring, factory);
		hand.setRing(ring);
		
		FingerWith3Bones pinky = factory.createFingerWith3Bones();
		pinky.setType(FingerType.PINKY);
		addBones(pinky, factory);
		hand.setPinky(pinky);
		
		return hand;
	}
	public void init() {
		   // For the initialisation of the model.
		   // Without this the following error happens:
		   //   "Package with uri 'hu.bme.mit.mdsd.erdiagram' not found."
		   handPackage.eINSTANCE.eClass();
	       Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
	        reg.getExtensionToFactoryMap().put("hand", new XMIResourceFactoryImpl());
		}
	public void addBones(FingerWith3Bones finger,handFactory factory){
		Bone distal = factory.createBone();
		addPosition(distal,factory);
		addDirection(distal,factory);
		finger.setDistal(distal);
	
		Bone proximal = factory.createBone();
		addPosition(proximal,factory);
		addDirection(proximal,factory);
		finger.setProximal(proximal);
		
		if(finger.getType()!=FingerType.THUMB){
			Bone intermediate = factory.createBone();
			addPosition(intermediate,factory);
			addDirection(intermediate,factory);
			finger.setIntermediate(intermediate);
		}
	}
	public void addPosition(Bone bone, handFactory factory){
		bone.setPosition(factory.createVector());
	}
	
	public void addDirection(Bone bone, handFactory factory){
		bone.setDirection(factory.createVector());
	}
	
	public void addPosition(Palm palm, handFactory factory){
		palm.setPosition(factory.createVector());
	}
	
	public Resource createResource(URI uri) {
	    ResourceSet resSet = new ResourceSetImpl();
	    Resource resource = resSet.createResource(uri);
	    return resource;
	}

}
