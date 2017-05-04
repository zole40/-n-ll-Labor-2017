package onlab.hand.cepExample.bundle;

import java.io.IOException;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.viatra.cep.core.api.engine.CEPEngine;
import org.eclipse.viatra.cep.core.metamodels.automaton.EventContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import com.leapmotion.leap.Controller;

import onlab.hand.cepExample.LeapListener.LeapListener;
import onlab.hand.query.CepFactory;
import onlab.hand.query.mapping.QueryEngine2ViatraCep;

/**
 * The activator class controls the plug-in life cycle
 */
public class Main implements BundleActivator {

	// The plug-in ID
	public static final String PLUGIN_ID = "activator_test"; //$NON-NLS-1$

	// The shared instance
	private static Main plugin;
	private CEPEngine engine;
	/**
	 * The constructor
	 */
	public Main() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context){
		ResourceSet resources = new ResourceSetImpl();
		LeapListener listener = new LeapListener(resources);
        Controller controller = new Controller();	
        controller.addListener(listener);
		engine = CEPEngine.newEngine().eventContext(EventContext.IMMEDIATE).rules(CepFactory.getInstance().allRules()).prepare();
		QueryEngine2ViatraCep.register(resources, engine.getStreamManager().newEventStream());
		plugin = this;
        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Remove the sample listener when done
        controller.removeListener(listener);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Main getDefault() {
		return plugin;
	}

}
