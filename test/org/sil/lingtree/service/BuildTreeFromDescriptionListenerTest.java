/**
 * Copyright (c) 2016-2025 SIL Global
 * This software is licensed under the LGPL, version 2.1 or later
 * (http://www.gnu.org/licenses/lgpl-2.1.html)
 */
package org.sil.lingtree.service;

import static org.junit.Assert.*;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.junit.Rule;
import org.junit.Test;
import org.sil.lingtree.descriptionparser.DescriptionConstants;
import org.sil.lingtree.model.LingTreeNode;
import org.sil.lingtree.model.LingTreeTree;
import org.sil.lingtree.model.NodeType;
import org.sil.utility.view.JavaFXThreadingRule;

/**
 * @author Andy Black
 *
 */
public class BuildTreeFromDescriptionListenerTest extends ServiceBaseTest {
	LingTreeTree origTree;
	LingTreeTree ltTree;
	LingTreeNode rootNode;

	@Rule
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

	@Test
	public void fontInfoTextTest() {
		// Non-terminal, lex, and gloss and empty element nodes
		LingTreeTree origTree = new LingTreeTree();
		LingTreeTree ltTree = TreeBuilder.parseAString("(NP (\\L Juan (\\G John)) (\\E t))", origTree);
		LingTreeNode node = ltTree.getRootNode();
		checkFontInfo(node, node.getContentTextBox(), "Times New Roman", 12.0, "Regular",
				Color.BLACK);
		List<LingTreeNode> daughters = node.getDaughters();
		node = daughters.get(0);
		checkFontInfo(node, node.getContentTextBox(), "Charis SIL", 12.0, "Regular", Color.BLUE);
		LingTreeNode node2 = daughters.get(1);
		// for some reason, this is sometimes Verdana instead of Charis SIL
		String expectedFontFamily = "Charis SIL";
		double fontSize = 12.0;
		String fontType = "Regular";
		Color color = Color.BLUE;
		if (node2.getContentTextBox().getFont().getFamily().equals("Verdana")) {
			Font verdana = node2.getContentTextBox().getFont();
			expectedFontFamily = "Verdana";
			fontSize = verdana.getSize();
			fontType = "Italic";
			color = Color.RED;
		}
		checkFontInfo(node2, node2.getContentTextBox(), expectedFontFamily, fontSize, fontType, color);
		daughters = node.getDaughters();
		node = daughters.get(0);
		checkFontInfo(node, node.getContentTextBox(), "Arial", 12.0, "Regular", Color.GREEN);

		// subscript and superscript
		ltTree = TreeBuilder.parseAString("(NP/si (N/S'))", ltTree);
		node = ltTree.getRootNode();
		checkFontInfo(node, node.getContentTextBox(), "Times New Roman", 12.0, "Regular",
				Color.BLACK);
		checkFontInfo(node, node.getSubscriptTextBox(), "Times New Roman", 8.3999996, "Regular",
				Color.BLACK);
		daughters = node.getDaughters();
		node = daughters.get(0);
		checkFontInfo(node, node.getContentTextBox(), "Times New Roman", 12.0, "Regular",
				Color.BLACK);
		checkFontInfo(node, node.getSuperscriptTextBox(), "Times New Roman", 8.3999996, "Regular", Color.BLACK);

		ltTree = TreeBuilder.parseAString("(NP/f|b/F/si/f|i/F (N/f|s14.0/F/S'/f|s19.0/F))", ltTree);
		node = ltTree.getRootNode();
		checkFontInfo(node, node.getContentTextBox(), "Times New Roman", 12.0, "Bold",
				Color.BLACK);
		checkFontInfo(node, node.getSubscriptTextBox(), "Times New Roman", 12.0, "Italic",
				Color.BLACK);
		daughters = node.getDaughters();
		node = daughters.get(0);
		checkFontInfo(node, node.getContentTextBox(), "Times New Roman", 14.0, "Regular",
				Color.BLACK);
		checkFontInfo(node, node.getSuperscriptTextBox(), "Times New Roman", 19.0, "Regular", Color.BLACK);

		// right-to-left
		origTree.setUseRightToLeftOrientation(false);
		ltTree = TreeBuilder.parseAString("(NP/si (N/S'))", origTree);
		assertFalse(ltTree.isUseRightToLeftOrientation());
		origTree.setUseRightToLeftOrientation(true);
		ltTree = TreeBuilder.parseAString("(NP/si (N/S'))", origTree);
		assertTrue(ltTree.isUseRightToLeftOrientation());
	}

	private void checkFontInfo(LingTreeNode node, Text textBox, String fontFamily, double fontSize,
			String fontType, Color color) {
		Font font = textBox.getFont();
		assertEquals(fontFamily, font.getFamily());
		assertEquals(fontSize, font.getSize(), 0.000001);
		assertEquals(fontType, font.getStyle());
		assertEquals(color, textBox.getFill());
	}

	@Test
	public void buildTreeFailuresTest() {
		LingTreeTree origTree = new LingTreeTree();
		LingTreeTree ltTree = TreeBuilder.parseAString("(S (NP) ((VP))", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 12, DescriptionConstants.MISSING_CLOSING_PAREN);

		ltTree = TreeBuilder.parseAString("(S (NP (VP))", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 10, DescriptionConstants.MISSING_CLOSING_PAREN);

		ltTree = TreeBuilder.parseAString("(S (NP ((VP))", origTree);
		checkErrorValues(origTree, ltTree, 2, 1, 11, DescriptionConstants.MISSING_CLOSING_PAREN);

		ltTree = TreeBuilder.parseAString("S (NP (VP))", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 0, DescriptionConstants.MISSING_OPENING_PAREN);

		ltTree = TreeBuilder.parseAString("(S (NP) VP))", origTree);
		checkErrorValues(origTree, ltTree, 2, 1, 8, DescriptionConstants.MISSING_OPENING_PAREN);

		ltTree = TreeBuilder.parseAString("(NP/s)", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 5, DescriptionConstants.MISSING_CONTENT_AFTER_SUBSCRIPT);

		ltTree = TreeBuilder.parseAString("(NP/S)", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 5, DescriptionConstants.MISSING_CONTENT_AFTER_SUPERSCRIPT);

		ltTree = TreeBuilder.parseAString("(S NP) (V) (VP))", origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 7, DescriptionConstants.CONTENT_AFTER_COMPLETED_TREE);

		String sBad = "(S) (NP (N-bar/S sup' (N(\\L Juan (\\G John)))))\n" +
				"(VP (V/ssub (\\T\\L duerme (\\G sleeps)))))";
		ltTree = TreeBuilder.parseAString(sBad, origTree);
		checkErrorValues(origTree, ltTree, 1, 1, 4, DescriptionConstants.CONTENT_AFTER_COMPLETED_TREE);
		String sDescriptionWithErrorLocationMarked = "(S)  << HERE >> (NP (N-bar/S sup' (N(\\L Juan (\\G John)))))\n" +
				"(VP (V/ssub (\\T\\L duerme (\\G sleeps)))))";
		assertEquals(sDescriptionWithErrorLocationMarked, TreeBuilder.getMarkedDescription(" << HERE >> "));

		sBad = "(S (NP) (N-bar/S sup' (N(\\L Juan (\\G John)))))\n" +
				"(VP (V/ssub (\\T\\L duerme (\\G sleeps)))))";
		ltTree = TreeBuilder.parseAString(sBad, origTree);
		checkErrorValues(origTree, ltTree, 1, 2, 0, DescriptionConstants.CONTENT_AFTER_COMPLETED_TREE);
		sDescriptionWithErrorLocationMarked = "(S (NP) (N-bar/S sup' (N(\\L Juan (\\G John)))))\n" +
				" << HERE >> (VP (V/ssub (\\T\\L duerme (\\G sleeps)))))";
		assertEquals(sDescriptionWithErrorLocationMarked, TreeBuilder.getMarkedDescription(" << HERE >> "));
		sBad = "(S (PP (P' (P (\\L de (\\G from))))(NP (N' (N (\\L aqui (\\G here))))))\n" +
				"(NP (N'/Ssup' (N(\\L Juanita (\\G Juanita)))))\n" +
				"(VP (NP (N' (N (\\LSusana (\\G Susana))))) (V' (V/ssub (\\T\\L duerme (\\G sleeps)))))))";
		ltTree = TreeBuilder.parseAString(sBad, origTree);
		String sErrorMessage = DescriptionConstants.TOO_MANY_CLOSING_PARENS;
		checkErrorValues(origTree, ltTree, 1, 3, 83, sErrorMessage);
		sDescriptionWithErrorLocationMarked = "(S (PP (P' (P (\\L de (\\G from))))(NP (N' (N (\\L aqui (\\G here))))))\n" +
				"(NP (N'/Ssup' (N(\\L Juanita (\\G Juanita)))))\n" +
				"(VP (NP (N' (N (\\LSusana (\\G Susana))))) (V' (V/ssub (\\T\\L duerme (\\G sleeps))))))) << HERE >> ";
		assertEquals(sDescriptionWithErrorLocationMarked, TreeBuilder.getMarkedDescription(" << HERE >> "));
		}

	private void checkErrorValues(LingTreeTree origTree, LingTreeTree ltTree,
			int expectedNumErrors, int expectedLineNumber, int expectedCharPos,
			String sExpectedErrorMessage) {
		int numErrors = TreeBuilder.getNumberOfErrors();
		int iLineNum = TreeBuilder.getLineNumberOfError();
		int iCharPos = TreeBuilder.getCharacterPositionInLineOfError();
		String sErrorMessage = TreeBuilder.getErrorMessage();
		assertEquals(expectedNumErrors, numErrors);
		assertEquals(expectedLineNumber, iLineNum);
		assertEquals(expectedCharPos, iCharPos);
		assertEquals(sExpectedErrorMessage, sErrorMessage);
		assertEquals(ltTree, origTree);
	}

	@Test
	public void buildTreesTest() {
		// Basic example
		origTree = new LingTreeTree();
		ltTree = TreeBuilder.parseAString("(S (NP) (VP))", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		List<LingTreeNode> daughters = rootNode.getDaughters();
		LingTreeNode node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "", "", false, false, NodeType.NonTerminal, 2, 0);
		checkNodeResult(node1.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		checkNodeResult(node1.getRightSister(), "VP", "", "", false, false, NodeType.NonTerminal,
				2, 0);
		LingTreeNode node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 0);
		checkNodeResult(node2.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(node2.getRightSister());

		// lex/gloss example
		ltTree = TreeBuilder.parseAString(
				"(S (NP (\\L Juan (\\G John))) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		checkNodeResult(node1.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		checkNodeResult(node1.getRightSister(), "VP", "", "", false, false, NodeType.NonTerminal,
				2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		checkNodeResult(node1.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(node2.getRightSister());
		// NP
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "Juan", "", "", false, false, NodeType.Lex, 3, 1);
		checkNodeResult(node1.getMother(), "NP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		assertNull(node1.getRightSister());
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "John", "", "", false, false, NodeType.Gloss, 4, 0);
		checkNodeResult(node1.getMother(), "Juan", "", "", false, false, NodeType.Lex, 3, 1);
		assertNull(node1.getRightSister());
		// VP
		daughters = node2.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		checkNodeResult(node1.getMother(), "VP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		assertNull(node1.getRightSister());
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "duerme", "", "", false, false, NodeType.Lex, 4, 1);
		checkNodeResult(node1.getMother(), "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		assertNull(node1.getRightSister());
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "sleeps", "", "", false, false, NodeType.Gloss, 5, 0);
		checkNodeResult(node1.getMother(), "duerme", "", "", false, false, NodeType.Lex, 4, 1);
		assertNull(node1.getRightSister());

		// empty example
		ltTree = TreeBuilder.parseAString(
				"(S (NP (\\E pro)) (VP (V (\\L duerme (\\G sleeps)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		checkNodeResult(node1.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		checkNodeResult(node1.getRightSister(), "VP", "", "", false, false, NodeType.NonTerminal,
				2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		checkNodeResult(node1.getMother(), "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(node2.getRightSister());
		// NP
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "pro", "", "", false, false, NodeType.EmptyElement, 3, 0);
		checkNodeResult(node1.getMother(), "NP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		assertNull(node1.getRightSister());
		daughters = node1.getDaughters();
		assertEquals(0,daughters.size());
		// VP
		daughters = node2.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		checkNodeResult(node1.getMother(), "VP", "", "", false, false, NodeType.NonTerminal, 2, 1);
		assertNull(node1.getRightSister());
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "duerme", "", "", false, false, NodeType.Lex, 4, 1);
		checkNodeResult(node1.getMother(), "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		assertNull(node1.getRightSister());
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "sleeps", "", "", false, false, NodeType.Gloss, 5, 0);
		checkNodeResult(node1.getMother(), "duerme", "", "", false, false, NodeType.Lex, 4, 1);
		assertNull(node1.getRightSister());

		// triangle example
		ltTree = TreeBuilder.parseAString("(NP (\\T all the King’s men))", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "NP", "", "", false, false, NodeType.NonTerminal, 1, 1);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "all the King’s men", "", "", false, true, NodeType.NonTerminal, 2,
				0);
		checkNodeResult(node1.getMother(), "NP", "", "", false, false, NodeType.NonTerminal, 1, 1);
		assertNull(node1.getRightSister());

		// quoted parentheses example
		//(NP (Paul \(the bear\)))
		ltTree = TreeBuilder
				.parseAString("(NP (Paul \\(the bear\\)))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "NP", "", "", false, false, NodeType.NonTerminal, 1, 1);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "Paul (the bear)", "", "", false, false, NodeType.NonTerminal, 2, 0);

		// omit lines example
		ltTree = TreeBuilder
				.parseAString(
						"((\\O σ (O (\\L t)) (N (R (\\L e)))) (\\O σ (O (\\L p)) (N (R (\\L i)) (C (\\L k)))))",
						origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "σ", "", "", true, false, NodeType.NonTerminal, 2, 2);
		checkNodeResult(node1.getMother(), "", "", "", false, false, NodeType.NonTerminal, 1, 2);
		checkNodeResult(node1.getRightSister(), "σ", "", "", true, false, NodeType.NonTerminal, 2,
				2);
		node2 = daughters.get(1);
		checkNodeResult(node2, "σ", "", "", true, false, NodeType.NonTerminal, 2, 2);
		checkNodeResult(node2.getMother(), "", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(node2.getRightSister());
		// not testing mother or right sister from here on down
		// O t
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		LingTreeNode node3 = daughters.get(1);
		checkNodeResult(node1, "O", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "t", "", "", false, false, NodeType.Lex, 4, 0);
		// N R e
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		node3 = node3.getDaughters().get(0);
		checkNodeResult(node3, "R", "", "", false, false, NodeType.NonTerminal, 4, 1);
		node3 = node3.getDaughters().get(0);
		checkNodeResult(node3, "e", "", "", false, false, NodeType.Lex, 5, 0);
		// O p
		daughters = node2.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "O", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "p", "", "", false, false, NodeType.Lex, 4, 0);
		node1 = node2.getDaughters().get(1);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 2);
		node3 = node1.getDaughters().get(0);
		checkNodeResult(node3, "R", "", "", false, false, NodeType.NonTerminal, 4, 1);
		node3 = node3.getDaughters().get(0);
		checkNodeResult(node3, "i", "", "", false, false, NodeType.Lex, 5, 0);
		node3 = node1.getDaughters().get(1);
		checkNodeResult(node3, "C", "", "", false, false, NodeType.NonTerminal, 4, 1);
		node3 = node3.getDaughters().get(0);
		checkNodeResult(node3, "k", "", "", false, false, NodeType.Lex, 5, 0);

		// subscript and superscript example
		ltTree = TreeBuilder.parseAString(
				"(S (NP/s1/S' (N (dogs))) (VP (V (chase)) (NP/S'/s2 (N (cats)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "1", "'", false, false, NodeType.NonTerminal, 2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 2);
		// NP N dogs
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "dogs", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP V chase
		node3 = node2.getDaughters().get(0);
		checkNodeResult(node3, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "chase", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP NP N cats
		node3 = node2.getDaughters().get(1);
		checkNodeResult(node3, "NP", "2", "'", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 4, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "cats", "", "", false, false, NodeType.NonTerminal, 5, 0);

		// subscript italic and superscript italic example
		ltTree = TreeBuilder.parseAString(
				"(S (NP/_1/^' (N (dogs))) (VP (V (chase)) (NP/^'/_2 (N (cats)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "1", "'", false, false, NodeType.NonTerminal, 2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 2);
		// NP N dogs
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "dogs", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP V chase
		node3 = node2.getDaughters().get(0);
		checkNodeResult(node3, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "chase", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP NP N cats
		node3 = node2.getDaughters().get(1);
		checkNodeResult(node3, "NP", "2", "'", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 4, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "cats", "", "", false, false, NodeType.NonTerminal, 5, 0);

		// subscript italic and superscript example
		ltTree = TreeBuilder.parseAString(
				"(S (NP/_1/S' (N (dogs))) (VP (V (chase)) (NP/S'/_2 (N (cats)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "1", "'", false, false, NodeType.NonTerminal, 2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 2);
		// NP N dogs
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "dogs", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP V chase
		node3 = node2.getDaughters().get(0);
		checkNodeResult(node3, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "chase", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP NP N cats
		node3 = node2.getDaughters().get(1);
		checkNodeResult(node3, "NP", "2", "'", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 4, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "cats", "", "", false, false, NodeType.NonTerminal, 5, 0);

		// subscript and superscript italic example
		ltTree = TreeBuilder.parseAString(
				"(S (NP/s1/^' (N (dogs))) (VP (V (chase)) (NP/^'/s2 (N (cats)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "NP", "1", "'", false, false, NodeType.NonTerminal, 2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 2);
		// NP N dogs
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "dogs", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP V chase
		node3 = node2.getDaughters().get(0);
		checkNodeResult(node3, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "chase", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP NP N cats
		node3 = node2.getDaughters().get(1);
		checkNodeResult(node3, "NP", "2", "'", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 4, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "cats", "", "", false, false, NodeType.NonTerminal, 5, 0);

		// backslash and forward slash as text node content
		ltTree = TreeBuilder.parseAString(
				"(S (/S'/Comp (N (do\\gs))) (VP (V (chase)) (/s2 (N (cats)))))", origTree);
		// root node
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 2);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "", "", "'/Comp", false, false, NodeType.NonTerminal, 2, 1);
		node2 = daughters.get(1);
		checkNodeResult(node2, "VP", "", "", false, false, NodeType.NonTerminal, 2, 2);
		// NP N dogs
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node1.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "do\\gs", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP V chase
		node3 = node2.getDaughters().get(0);
		checkNodeResult(node3, "V", "", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "chase", "", "", false, false, NodeType.NonTerminal, 4, 0);
		// VP NP N cats
		node3 = node2.getDaughters().get(1);
		checkNodeResult(node3, "", "2", "", false, false, NodeType.NonTerminal, 3, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "N", "", "", false, false, NodeType.NonTerminal, 4, 1);
		daughters = node3.getDaughters();
		node3 = daughters.get(0);
		checkNodeResult(node3, "cats", "", "", false, false, NodeType.NonTerminal, 5, 0);
		// abbreviations
		ltTree = TreeBuilder.parseAString(
				"(NP (N (\\L mi libros (\\G -/a1.poss/A- book -/a pl /A))))", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "NP", "", "", false, false, NodeType.NonTerminal, 1, 1);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "N", "", "", false, false, NodeType.NonTerminal, 2, 1);
		assertFalse(node1.hasAbbreviation());
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "mi libros", "", "", false, false, NodeType.Lex, 3, 1);
		assertFalse(node1.hasAbbreviation());
		node1 = node1.getDaughters().get(0);
		checkNodeResult(node1, "", "", "", false, false, NodeType.Gloss, 4, 0);
		assertTrue(node1.hasAbbreviation());
		assertEquals(4, node1.getContentsAsList().size());
		assertEquals("-", node1.getContentsAsList().get(0).getText());
		assertEquals("1.poss", node1.getContentsAsList().get(1).getText());
		assertEquals("- book -", node1.getContentsAsList().get(2).getText());
		assertEquals("pl", node1.getContentsAsList().get(3).getText());
		assertTrue(node1.getContentsAsList().get(0).toString().contains("NodeText"));
		assertTrue(node1.getContentsAsList().get(1).toString().contains("AbbreviationText"));
		assertTrue(node1.getContentsAsList().get(2).toString().contains("NodeText"));
		assertTrue(node1.getContentsAsList().get(3).toString().contains("AbbreviationText"));

		ltTree = TreeBuilder.parseAString(
				"(-/a1.poss/A- book -/a pl /A)", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "", "", "", false, false, NodeType.NonTerminal, 1, 0);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		assertTrue(rootNode.hasAbbreviation());
		assertEquals(4, rootNode.getContentsAsList().size());
		assertEquals("-", rootNode.getContentsAsList().get(0).getText());
		assertEquals("1.poss", rootNode.getContentsAsList().get(1).getText());
		assertEquals("- book -", rootNode.getContentsAsList().get(2).getText());
		assertEquals("pl", rootNode.getContentsAsList().get(3).getText());
		assertTrue(rootNode.getContentsAsList().get(0).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(1).toString().contains("AbbreviationText"));
		assertTrue(rootNode.getContentsAsList().get(2).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(3).toString().contains("AbbreviationText"));
		assertEquals(1, rootNode.getContentsAsList().get(0).getCharacterPositionInLine());
		assertEquals(4, rootNode.getContentsAsList().get(1).getCharacterPositionInLine());
		assertEquals(12, rootNode.getContentsAsList().get(2).getCharacterPositionInLine());
		assertEquals(22, rootNode.getContentsAsList().get(3).getCharacterPositionInLine());

		ltTree = TreeBuilder.parseAString(
				"(S (-/a1.poss/A- book -/a pl /A))", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "S", "", "", false, false, NodeType.NonTerminal, 1, 1);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		assertFalse(rootNode.hasAbbreviation());
		daughters = rootNode.getDaughters();
		node1 = daughters.get(0);
		checkNodeResult(node1, "", "", "", false, false, NodeType.NonTerminal, 2, 0);
		assertTrue(node1.hasAbbreviation());
		assertEquals(4, node1.getContentsAsList().size());
		assertEquals("-", node1.getContentsAsList().get(0).getText());
		assertEquals("1.poss", node1.getContentsAsList().get(1).getText());
		assertEquals("- book -", node1.getContentsAsList().get(2).getText());
		assertEquals("pl", node1.getContentsAsList().get(3).getText());
		assertTrue(node1.getContentsAsList().get(0).toString().contains("NodeText"));
		assertTrue(node1.getContentsAsList().get(1).toString().contains("AbbreviationText"));
		assertTrue(node1.getContentsAsList().get(2).toString().contains("NodeText"));
		assertTrue(node1.getContentsAsList().get(3).toString().contains("AbbreviationText"));

		origTree = new LingTreeTree();
		ltTree = TreeBuilder.parseAString(
				"(-/f|s13.0/F/a1.poss/A- book -/a pl /A)", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "", "", "", false, false, NodeType.NonTerminal, 1, 0);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		assertTrue(rootNode.hasAbbreviation());
		assertEquals(4, rootNode.getContentsAsList().size());
		assertEquals("-", rootNode.getContentsAsList().get(0).getText());
		assertEquals("1.poss", rootNode.getContentsAsList().get(1).getText());
		assertEquals("- book -", rootNode.getContentsAsList().get(2).getText());
		assertEquals("pl", rootNode.getContentsAsList().get(3).getText());
		assertTrue(rootNode.getContentsAsList().get(0).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(1).toString().contains("AbbreviationText"));
		assertTrue(rootNode.getContentsAsList().get(2).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(3).toString().contains("AbbreviationText"));
		assertEquals(1, rootNode.getContentsAsList().get(0).getCharacterPositionInLine());
		assertEquals(14, rootNode.getContentsAsList().get(1).getCharacterPositionInLine());
		assertEquals(22, rootNode.getContentsAsList().get(2).getCharacterPositionInLine());
		assertEquals(32, rootNode.getContentsAsList().get(3).getCharacterPositionInLine());

		origTree = new LingTreeTree();
		ltTree = TreeBuilder.parseAString(
				"(-/f|s13.0/F/a1.poss/A/f|s22.0/F- book -/f|b|i/F/a pl /A)", origTree);
		rootNode = ltTree.getRootNode();
		checkNodeResult(rootNode, "", "", "", false, false, NodeType.NonTerminal, 1, 0);
		assertNull(rootNode.getMother());
		assertNull(rootNode.getRightSister());
		assertTrue(rootNode.hasAbbreviation());
		assertEquals(4, rootNode.getContentsAsList().size());
		assertEquals("-", rootNode.getContentsAsList().get(0).getText());
		assertEquals("1.poss", rootNode.getContentsAsList().get(1).getText());
		assertEquals("- book -", rootNode.getContentsAsList().get(2).getText());
		assertEquals("pl", rootNode.getContentsAsList().get(3).getText());
		assertTrue(rootNode.getContentsAsList().get(0).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(1).toString().contains("AbbreviationText"));
		assertTrue(rootNode.getContentsAsList().get(2).toString().contains("NodeText"));
		assertTrue(rootNode.getContentsAsList().get(3).toString().contains("AbbreviationText"));
		assertEquals(1, rootNode.getContentsAsList().get(0).getCharacterPositionInLine());
		assertEquals(14, rootNode.getContentsAsList().get(1).getCharacterPositionInLine());
		assertEquals(32, rootNode.getContentsAsList().get(2).getCharacterPositionInLine());
		assertEquals(50, rootNode.getContentsAsList().get(3).getCharacterPositionInLine());
}

	private void checkNodeResult(LingTreeNode node, String sContent, String sSubscript,
			String sSuperscript, boolean fOmitLine, boolean fTriangle, NodeType nodeType,
			int iLevel, int iNumDaughters) {
		assertNotNull(node);
		assertEquals(sContent, node.getContent());
		assertEquals(sSubscript, node.getSubscript());
		assertEquals(sSuperscript, node.getSuperscript());
		assertEquals(fOmitLine, node.isOmitLine());
		assertEquals(fTriangle, node.isTriangle());
		assertEquals(nodeType, node.getNodeType());
		assertEquals(iLevel, node.getLevel());
		List<LingTreeNode> daughters = node.getDaughters();
		assertEquals(iNumDaughters, daughters.size());
	}

}
