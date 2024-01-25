/**
 * Copyright (c) 2016-2018 SIL International
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */

package org.sil.lingtree.service;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.sil.lingtree.model.LingTreeNode;
import org.sil.lingtree.model.LingTreeTree;

/**
 * @author Andy Black
 *
 */
public class TreeDrawerTest extends ServiceBaseTest {
	
	private TreeDrawer drawer;

	@Test
	public void calculateMaxHeightsPerLevelTest() {
		LingTreeTree origTree = new LingTreeTree();
		LingTreeTree ltTree = TreeBuilder.parseAString("(S (NP (\\L Juan (\\G John))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		HashMap<Integer, Double> maxHeightPerLevel = drawer.getMaxHeightPerLevel();
		assertEquals(13.2890625, maxHeightPerLevel.get(1), 0.0);
		assertEquals(13.2890625, maxHeightPerLevel.get(2), 0.0);
		assertEquals(19.62890625, maxHeightPerLevel.get(3), 0.0);
		assertEquals(19.62890625, maxHeightPerLevel.get(4), 0.0);
		assertEquals(13.40625, maxHeightPerLevel.get(5), 0.0);
		assertNull(maxHeightPerLevel.get(6));
		
		ltTree = TreeBuilder.parseAString("(S (NP (N' (N (\\L Juan (\\G John))))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		maxHeightPerLevel = drawer.getMaxHeightPerLevel();
		assertEquals(13.2890625, maxHeightPerLevel.get(1), 0.0);
		assertEquals(13.2890625, maxHeightPerLevel.get(2), 0.0);
		assertEquals(13.2890625, maxHeightPerLevel.get(3), 0.0);
		assertEquals(19.62890625, maxHeightPerLevel.get(4), 0.0);
		assertEquals(19.62890625, maxHeightPerLevel.get(5), 0.0);
		assertEquals(13.40625, maxHeightPerLevel.get(6), 0.0);
		assertNull(maxHeightPerLevel.get(7));

		// abbreviations
		ltTree = TreeBuilder.parseAString("(NP (N (\\L mi libros (\\G -/a1.poss/A- book -/a pl /A))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		maxHeightPerLevel = drawer.getMaxHeightPerLevel();
		assertEquals(13.2890625, maxHeightPerLevel.get(1), 0.0);
		assertEquals(13.2890625, maxHeightPerLevel.get(2), 0.0);
		assertEquals(19.62890625, maxHeightPerLevel.get(3), 0.0);
		assertEquals(13.40625, maxHeightPerLevel.get(4), 0.0);
		assertNull(maxHeightPerLevel.get(5));
	}

	@Test
	public void calculateYCoordinateOfEveryNodeTest() {
		LingTreeTree origTree = new LingTreeTree();
		LingTreeTree ltTree = TreeBuilder.parseAString("(S (NP (\\L Juan (\\G John))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		LingTreeNode node = ltTree.getRootNode();
		assertEquals(100.0, node.getYCoordinate(), 0.0);
		assertEquals(86.306640625, node.getYUpperMid(), 0.0);
		assertEquals(105.595703125, node.getYLowerMid(), 0.0);
		LingTreeNode node1 = node.getDaughters().get(0);
		assertEquals(133.2890625, node1.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node1.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node1.getYLowerMid(), 0.0);
		LingTreeNode node2 = node.getDaughters().get(1);
		assertEquals(133.2890625, node2.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node2.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node2.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(166.578125, node1.getYCoordinate(), 0.0);
		assertEquals(149.22265625, node1.getYUpperMid(), 0.0);
		assertEquals(174.8515625, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(186.20703125, node1.getYCoordinate(), 0.0);
		assertEquals(172.34375, node1.getYUpperMid(), 0.0);
		assertEquals(191.75, node1.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(166.578125, node2.getYCoordinate(), 0.0);
		assertEquals(152.884765625, node2.getYUpperMid(), 0.0);
		assertEquals(172.173828125, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(206.20703125, node2.getYCoordinate(), 0.0);
		assertEquals(188.8515625, node2.getYUpperMid(), 0.0);
		assertEquals(214.48046875, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(225.8359375, node2.getYCoordinate(), 0.0);
		assertEquals(211.97265625, node2.getYUpperMid(), 0.0);
		assertEquals(231.37890625, node2.getYLowerMid(), 0.0);

		// Now with "show flat view" set
		ltTree = TreeBuilder.parseAString("(S (NP (\\L Juan (\\G John))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		ltTree.setShowFlatView(true);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(100.0, node.getYCoordinate(), 0.0);
		assertEquals(86.306640625, node.getYUpperMid(), 0.0);
		assertEquals(105.595703125, node.getYLowerMid(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(133.2890625, node1.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node1.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node1.getYLowerMid(), 0.0);
		node2 = node.getDaughters().get(1);
		assertEquals(133.2890625, node2.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node2.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node2.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(206.20703125, node1.getYCoordinate(), 0.0);
		assertEquals(188.8515625, node1.getYUpperMid(), 0.0);
		assertEquals(211.48046875, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(225.8359375, node1.getYCoordinate(), 0.0);
		assertEquals(214.97265625, node1.getYUpperMid(), 0.0);
		assertEquals(228.37890625, node1.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(166.578125, node2.getYCoordinate(), 0.0);
		assertEquals(152.884765625, node2.getYUpperMid(), 0.0);
		assertEquals(172.173828125, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(206.20703125, node2.getYCoordinate(), 0.0);
		assertEquals(188.8515625, node2.getYUpperMid(), 0.0);
		assertEquals(211.48046875, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(225.8359375, node2.getYCoordinate(), 0.0);
		assertEquals(214.97265625, node2.getYUpperMid(), 0.0);
		assertEquals(228.37890625, node2.getYLowerMid(), 0.0);

		// with subscript and superscript
		ltTree = TreeBuilder.parseAString("(S (NP/Ssuper (\\L Juan (\\G John))) (VP (V/ssub (\\L duerme (\\G sleeps)))))", origTree);
		ltTree.setShowFlatView(false);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(100.0, node.getYCoordinate(), 0.0);
		assertEquals(86.306640625, node.getYUpperMid(), 0.0);
		assertEquals(105.595703125, node.getYLowerMid(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(133.2890625, node1.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node1.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node1.getYLowerMid(), 0.0);
		node2 = node.getDaughters().get(1);
		assertEquals(133.2890625, node2.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node2.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node2.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(170.578121, node1.getYCoordinate(), 0.0);
		assertEquals(153.22265225, node1.getYUpperMid(), 0.0);
		assertEquals(178.8515585, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(190.20702725, node1.getYCoordinate(), 0.0);
		assertEquals(176.343746, node1.getYUpperMid(), 0.0);
		assertEquals(195.749996, node1.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(170.578121, node2.getYCoordinate(), 0.0);
		assertEquals(156.884761625, node2.getYUpperMid(), 0.0);
		assertEquals(176.173824125, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(210.20702725, node2.getYCoordinate(), 0.0);
		assertEquals(192.8515585, node2.getYUpperMid(), 0.0);
		assertEquals(218.48046475, node2.getYLowerMid(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(229.8359335, node2.getYCoordinate(), 0.0);
		assertEquals(215.97265225, node2.getYUpperMid(), 0.0);
		assertEquals(235.37890225, node2.getYLowerMid(), 0.0);

		// abbreviations
		ltTree = TreeBuilder.parseAString("(NP (N (\\L mi libros (\\G -/a1.poss/A- book -/a pl /A))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(100.0, node.getYCoordinate(), 0.0);
		assertEquals(86.306640625, node.getYUpperMid(), 0.0);
		assertEquals(105.595703125, node.getYLowerMid(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(133.2890625, node1.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node1.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(166.578125, node1.getYCoordinate(), 0.0);
		assertEquals(149.22265625, node1.getYUpperMid(), 0.0);
		assertEquals(174.8515625, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(186.20703125, node1.getYCoordinate(), 0.0);
		assertEquals(172.34375, node1.getYUpperMid(), 0.0);
		assertEquals(191.75, node1.getYLowerMid(), 0.0);

		ltTree = TreeBuilder.parseAString("(NP (N (\\L mi libros (\\G -/a1.poss/A- book -/a pl /A))))", origTree);
		drawer = new TreeDrawer(ltTree);
		ltTree.setLexGlossGapAdjustment(20.0);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(100.0, node.getYCoordinate(), 0.0);
		assertEquals(86.306640625, node.getYUpperMid(), 0.0);
		assertEquals(105.595703125, node.getYLowerMid(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(133.2890625, node1.getYCoordinate(), 0.0);
		assertEquals(119.595703125, node1.getYUpperMid(), 0.0);
		assertEquals(138.884765625, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(166.578125, node1.getYCoordinate(), 0.0);
		assertEquals(149.22265625, node1.getYUpperMid(), 0.0);
		assertEquals(174.8515625, node1.getYLowerMid(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(186.578125, node1.getYCoordinate(), 0.0);
		assertEquals(172.71484375, node1.getYUpperMid(), 0.0);
		assertEquals(192.12109375, node1.getYLowerMid(), 0.0);

	}

	@Test
	public void calculateXCoordinateOfEveryNodeTest() {
		LingTreeTree origTree = new LingTreeTree();
		LingTreeTree ltTree = TreeBuilder.parseAString("(S (NP (\\L Juan (\\G John))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		drawer.calculateXCoordinateOfEveryNode();
		LingTreeNode node = ltTree.getRootNode();
		assertEquals(141.24267578125, node.getXCoordinate(), 0.0);
		LingTreeNode node1 = node.getDaughters().get(0);
		assertEquals(105.26171875, node1.getXCoordinate(), 0.0);
		LingTreeNode node2 = node.getDaughters().get(1);
		assertEquals(168.478515625, node2.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(100.234375, node1.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(100.0, node1.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(171.8154296875, node2.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(156.021484375, node2.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(158.8046875, node2.getXCoordinate(), 0.0);

		// with subscript and superscript
		origTree = new LingTreeTree();
		ltTree = TreeBuilder.parseAString("(S (NP/Ssuper (\\L Juan (\\G John))) (VP (V/ssub (\\L duerme (\\G sleeps)))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		drawer.calculateXCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(147.03598546981812, node.getXCoordinate(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(100.0, node1.getXCoordinate(), 0.0);
		node2 = node.getDaughters().get(1);
		assertEquals(176.20292854309082, node2.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(104.09658145904541, node1.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(103.86220645904541, node1.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(173.70536994934082, node2.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(163.74589729309082, node2.getXCoordinate(), 0.0);
		node2 = node2.getDaughters().get(0);
		assertEquals(166.52910041809082, node2.getXCoordinate(), 0.0);

		// abbreviations
		origTree = new LingTreeTree();
		ltTree = TreeBuilder.parseAString("(NP (N (\\L mi libros (\\G -/a1.poss/A- book -/a pl /A))))", origTree);
		drawer = new TreeDrawer(ltTree);
		drawer.calculateMaxHeightPerLevel();
		drawer.calculateYCoordinateOfEveryNode();
		drawer.calculateXCoordinateOfEveryNode();
		node = ltTree.getRootNode();
		assertEquals(136.9375, node.getXCoordinate(), 0.0);
		node1 = node.getDaughters().get(0);
		assertEquals(140.2744140625, node1.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(121.0791015625, node1.getXCoordinate(), 0.0);
		node1 = node1.getDaughters().get(0);
		assertEquals(100, node1.getXCoordinate(), 0.0);
	}
}
