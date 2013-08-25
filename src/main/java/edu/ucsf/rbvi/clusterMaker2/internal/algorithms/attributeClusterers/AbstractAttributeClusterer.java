/* vim: set ts=2: */
/**
 * Copyright (c) 20118 The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *   1. Redistributions of source code must retain the above copyright
 *      notice, this list of conditions, and the following disclaimer.
 *   2. Redistributions in binary form must reproduce the above
 *      copyright notice, this list of conditions, and the following
 *      disclaimer in the documentation and/or other materials provided
 *      with the distribution.
 *   3. Redistributions must acknowledge that this software was
 *      originally developed by the UCSF Computer Graphics Laboratory
 *      under support by the NIH National Center for Research Resources,
 *      grant P41-RR01081.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package edu.ucsf.rbvi.clusterMaker2.internal.algorithms.attributeClusterers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cytoscape.group.CyGroup;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyIdentifiable;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.work.Tunable;
import org.cytoscape.work.TunableHandler;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.swing.RequestsUIHelper;
import org.cytoscape.work.swing.TunableUIHelper;


import edu.ucsf.rbvi.clusterMaker2.internal.api.ClusterManager;
import edu.ucsf.rbvi.clusterMaker2.internal.algorithms.AbstractClusterAlgorithm;
import edu.ucsf.rbvi.clusterMaker2.internal.algorithms.FuzzyNodeCluster;
import edu.ucsf.rbvi.clusterMaker2.internal.algorithms.NodeCluster;
import edu.ucsf.rbvi.clusterMaker2.internal.utils.ModelUtils;

/**
 * This abstract class is the base class for all of the attribute clusterers provided by
 * clusterMaker.  Fundamentally, an attribute clusterer is an algorithm which functions to
 * partition nodes or node attributes based on properties of the attributes.
 */
public abstract class AbstractAttributeClusterer extends AbstractClusterAlgorithm 
                                                 implements RequestsUIHelper {
	// Common instance variables
	protected DistanceMetric distanceMetric = DistanceMetric.EUCLIDEAN;

	public AbstractAttributeClusterer(ClusterManager clusterManager) {
		super(clusterManager);
	}

/*
	protected void addKTunables() {
		Tunable t = new Tunable("useSilhouette",
		                        "Estimate k using silhouette",
		                        Tunable.BOOLEAN, (Object)new Boolean(useSilhouette),
		                        (Object) null, (Object) null, 0);
		t.addTunableValueListener(this);
		clusterProperties.add(t);

		t = new Tunable("kMax",
		                "Maximum number of clusters",
		                Tunable.INTEGER, new Integer(kMax),
		                (Object)null, (Object)null, 0);
		if (!useSilhouette) t.setImmutable(true);
		clusterProperties.add(t);

		t = new Tunable("kNumber",
		                "Number of clusters (k)",
		                Tunable.INTEGER, new Integer(kNumber),
		                (Object)null, (Object)null, 0);
		if (useSilhouette) t.setImmutable(true);
		clusterProperties.add(t);

		// Whether to initialize cluster centers by choosing the most central elements
		clusterProperties.add(new Tunable("initializeNearCenter",
				                          "Initialize cluster centers from most central elements",
				                          Tunable.BOOLEAN, new Boolean(initializeNearCenter)));
	}

	protected void updateKTunables(boolean force) {
		Tunable t = clusterProperties.get("useSilhouette");
		if ((t != null) && (t.valueChanged() || force))
			useSilhouette = ((Boolean) t.getValue()).booleanValue();

		t = clusterProperties.get("kMax");
		if ((t != null) && (t.valueChanged() || force))
			kMax = ((Integer) t.getValue()).intValue();

		t = clusterProperties.get("kNumber");
		if ((t != null) && (t.valueChanged() || force))
			kNumber = ((Integer) t.getValue()).intValue();
		
		t = clusterProperties.get("initializeNearCenter");
		if ((t != null) && (t.valueChanged() || force)) {
			initializeNearCenter = ((Boolean) t.getValue()).booleanValue();
		}
	}
*/

	protected void updateKEstimates(CyNetwork network) {
/*
		// We also want to update the number our "guestimate" for k
		double nodeCount = (double)network.getNodeCount();
		if (selectedOnly) {
			int selNodes = CyTableUtil.getNodesInState(network, CyNetwork.SELECTED, true).size();
			if (selNodes > 0) nodeCount = (double)selNodes;
		}

		Tunable kTunable = clusterProperties.get("kNumber");
		double kinit = Math.sqrt(nodeCount/2);
		if (kinit > 1)
			kTunable.setValue((int)kinit);
		else
			kTunable.setValue(1);

		Tunable kMaxTunable = clusterProperties.get("kMax");
		kMaxTunable.setValue((int)kinit*2); // Double our kNumber estimate
*/
	}

/*
	public void tunableChanged(Tunable t) {
		// System.out.println("Tunable changed");
		if (t.getName().equals("useSilhouette")) {
			useSilhouette = ((Boolean) t.getValue()).booleanValue();
			if (useSilhouette) {
				clusterProperties.get("kMax").setImmutable(false);
				clusterProperties.get("kNumber").setImmutable(true);
			} else {
				clusterProperties.get("kMax").setImmutable(true);
				clusterProperties.get("kNumber").setImmutable(false);
			}
		}
	}
*/

	/**
 	 * This method resets (clears) all of the existing network attributes.
 	 */
	@SuppressWarnings("unchecked")
	protected void resetAttributes(String group_attr) {

		// Remove the attributes that are lingering
		if (ModelUtils.hasAttribute(network, network, ClusterManager.ARRAY_ORDER_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.ARRAY_ORDER_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.NODE_ORDER_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.NODE_ORDER_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.CLUSTER_ATTR_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.CLUSTER_ATTR_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.CLUSTER_NODE_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.CLUSTER_NODE_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.CLUSTER_EDGE_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.CLUSTER_EDGE_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.CLUSTER_TYPE_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.CLUSTER_TYPE_ATTRIBUTE);
		if (ModelUtils.hasAttribute(network, network, ClusterManager.CLUSTER_PARAMS_ATTRIBUTE))
			ModelUtils.deleteAttribute(network, network, ClusterManager.CLUSTER_PARAMS_ATTRIBUTE);

		// See if we have any old groups in this network
		if (ModelUtils.hasAttribute(network, network, group_attr)) {
			List<String>clList = network.getRow(network).getList(group_attr, String.class);
			/*
			for (String groupName: clList) {
				CyGroup group = CyGroupManager.findGroup(groupName);
				if (group != null)
					CyGroupManager.removeGroup(group);
			}
			*/
			ModelUtils.deleteAttribute(network, network, group_attr);
		}
	}

	/**
 	 * This method is called by all of the attribute cluster algorithms to update the
 	 * results attributes in the network.
 	 *
 	 * @param cluster_type the cluster type to indicate write into the CLUSTER_TYPE_ATTRIBUTE
 	 */
	protected void updateAttributes(String cluster_type, Integer[] rowOrder, 
	                                String weightAttributes[], List<String> attrList, 
		                              Matrix matrix) {

		ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_TYPE_ATTRIBUTE, cluster_type, String.class, null);

		if (matrix.isTransposed()) {
			ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_ATTR_ATTRIBUTE, attrList, List.class, String.class);
		} else {
			ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_NODE_ATTRIBUTE, attrList, List.class, String.class);
			if (matrix.isSymmetrical()) {
				ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_ATTR_ATTRIBUTE, attrList, List.class, String.class);
				ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_EDGE_ATTRIBUTE, weightAttributes[0], String.class, null);
			}
		}

		String[] rowArray = matrix.getRowLabels();
		ArrayList<String> orderList = new ArrayList<String>();

		String[] columnArray = matrix.getColLabels();
		ArrayList<String>columnList = new ArrayList<String>(columnArray.length);

		for (int i = 0; i < rowOrder.length; i++) {
			orderList.add(rowArray[rowOrder[i]]);
			if (matrix.isSymmetrical())
				columnList.add(rowArray[rowOrder[i]]);
		}

		if (!matrix.isSymmetrical()) {
			for (int col = 0; col < columnArray.length; col++) {
				columnList.add(columnArray[col]);
			}
		}
		// System.out.println("Order: "+orderList);

		if (matrix.isTransposed()) {
			// We did an Array cluster -- output the calculated array order
			// and the actual node order
			// netAttr.setListAttribute(netID, ClusterManager.ARRAY_ORDER_ATTRIBUTE, orderList);
			ModelUtils.createAndSet(network, network, ClusterManager.ARRAY_ORDER_ATTRIBUTE, orderList, List.class, String.class);

			// Don't override the columnlist if a node order already exists
			if (!ModelUtils.hasAttribute(network, network, ClusterManager.NODE_ORDER_ATTRIBUTE))
				ModelUtils.createAndSet(network, network, ClusterManager.NODE_ORDER_ATTRIBUTE, columnList, List.class, String.class);
		} else {
			ModelUtils.createAndSet(network, network, ClusterManager.NODE_ORDER_ATTRIBUTE, orderList, List.class, String.class);
			// Don't override the columnlist if a node order already exists
			if (!ModelUtils.hasAttribute(network, network, ClusterManager.ARRAY_ORDER_ATTRIBUTE))
				ModelUtils.createAndSet(network, network, ClusterManager.ARRAY_ORDER_ATTRIBUTE, columnList, List.class, String.class);
		}

	}

	protected void updateParams(List<String> params) {
		ModelUtils.createAndSet(network, network, ClusterManager.CLUSTER_PARAMS_ATTRIBUTE, params, List.class, String.class);
	}
}