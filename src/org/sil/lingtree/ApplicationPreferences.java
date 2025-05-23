// Copyright (c) 2017-2025 SIL Global
// This software is licensed under the LGPL, version 2.1 or later
// (http://www.gnu.org/licenses/lgpl-2.1.html)
package org.sil.lingtree;

import java.io.File;
import java.util.prefs.Preferences;

import org.sil.lingtree.model.ColorXmlAdaptor;
import org.sil.lingtree.model.FontInfo;
import org.sil.lingtree.model.LingTreeTree;
import org.sil.utility.*;
import org.sil.utility.service.keyboards.KeyboardInfo;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ApplicationPreferences extends ApplicationPreferencesUtilities {

	static final String LAST_OPENED_FILE_PATH = "lastOpenedFilePath";
	static final String LAST_OPENED_DIRECTORY_PATH = "lastOpenedDirectoryPath";
	static final String LAST_LOCALE_LANGUAGE = "lastLocaleLanguage";
	static final String DRAW_AS_TYPE = "drawastype";
	static final String SHOW_MATCHING_PAREN_DELAY = "showmatchingparendelay";
	static final String SHOW_MATCHING_PAREN_WITH_ARROW_KEYS = "showmatchingparenwitharrowkeys";
	static final String SHOW_FULL_FILE_PATH = "showfullfilepath";
	static final String TREE_DESCRIPTION_FONT_SIZE = "treedescriptionfontsize";
	// Not trying to be anglo-centric, but we have to start with something...
	static final String DEFAULT_LOCALE_LANGUAGE = "en";

	// Window parameters to remember
	static final String POSITION_X = "PositionX";
	static final String POSITION_Y = "PositionY";
	static final String WIDTH = "Width";
	static final String HEIGHT = "Height";
	static final String MAXIMIZED = "Maximized";
	// Window parameters for main window and various dialogs
	public static final String LAST_WINDOW = "lastWindow";
	public static final String LAST_SPLIT_PANE_POSITION = "lastSplitPanePosition";

	// Tree parameters to remember
	static final String BACKGROUND_COLOR = "backgroundColor";
	static final String ABBREVIATION_FONT_COLOR = "abbreviationFontColor";
	static final String ABBREVIATION_FONT_FAMILY = "abbreviationFontFamily";
	static final String ABBREVIATION_FONT_SIZE = "abbreviationFontSize";
	static final String ABBREVIATION_FONT_TYPE = "abbreviationFontType";
	static final String DRAW_VERTICAL_LINE_WITH_EMPTY_TEXT = "drawVerticalLineWithEmptyText";
	static final String EMPTY_ELEMENT_FONT_COLOR = "emptyElementFontColor";
	static final String EMPTY_ELEMENT_FONT_FAMILY = "emptyElementFontFamily";
	static final String EMPTY_ELEMENT_FONT_SIZE = "emptyElementFontSize";
	static final String EMPTY_ELEMENT_FONT_TYPE = "emptyElementFontType";
	static final String EMPTY_ELEMENT_KEYBOARD_LOCALE = "emptyElementKeyboardLocale";
	static final String EMPTY_ELEMENT_KEYBOARD_DESCRIPTION = "emptyElementKeyboardDescription";
	static final String EMPTY_ELEMENT_KEYBOARD_MAC_DESCRIPTION = "emptyElementKeyboardMacDescription";
	static final String EMPTY_ELEMENT_KEYBOARD_WINDOWS_LANG_ID = "emptyElementKeyboardWindowsLangId";
	static final String GLOSS_FONT_COLOR = "glossFontColor";
	static final String GLOSS_FONT_FAMILY = "glossFontFamily";
	static final String GLOSS_FONT_SIZE = "glossFontSize";
	static final String GLOSS_FONT_TYPE = "glossFontType";
	static final String GLOSS_KEYBOARD_LOCALE = "glossKeyboardLocale";
	static final String GLOSS_KEYBOARD_DESCRIPTION = "glossKeyboardDescription";
	static final String GLOSS_KEYBOARD_MAC_DESCRIPTION = "glossKeyboardMacDescription";
	static final String GLOSS_KEYBOARD_WINDOWS_LANG_ID = "glossKeyboardWindowsLangId";
	static final String HORIZONTAL_GAP = "horizontalGap";
	static final String HORIZONTAL_OFFSET = "horizontalOffset";
	static final String INITIAL_X_COORDINATE = "initialXCoordinate";
	static final String INITIAL_Y_COORDINATE = "initialYCoordinate";
	public static final String LAST_QUICK_REFERENCE_GUIDE = "lastQuickReferenceGuide";
	static final String LEX_GLOSS_GAP_ADJUSTMENT = "lexGlossGapAdjustment";
	static final String MINIMUM_X_GAP_FOR_EXTRA_VERTICAL_SPACING = "minimumXGapForExtraVerticalSpacing";
	static final String VERTICAL_ADJUSTMENT_FOR_EXTRA_VERTICAL_SPACING = "verticalAdjustmentForExtraVerticalSpacing";
	static final String LEXICAL_FONT_COLOR = "lexicalFontColor";
	static final String LEXICAL_FONT_FAMILY = "lexicalFontFamily";
	static final String LEXICAL_FONT_SIZE = "lexicalFontSize";
	static final String LEXICAL_FONT_TYPE = "lexicalFontType";
	static final String LEXICAL_KEYBOARD_LOCALE = "lexicalKeyboardLocale";
	static final String LEXICAL_KEYBOARD_DESCRIPTION = "lexicalKeyboardDescription";
	static final String LEXICAL_KEYBOARD_MAC_DESCRIPTION = "lexicalKeyboardMacDescription";
	static final String LEXICAL_KEYBOARD_WINDOWS_LANG_ID = "lexicalKeyboardWindowsLangId";
	static final String LINE_COLOR = "lineColor";
	static final String LINE_WIDTH = "lineWidth";
	static final String NON_TERMINAL_FONT_COLOR = "nonTerminalFontColor";
	static final String NON_TERMINAL_FONT_FAMILY = "nonTerminalFontFamily";
	static final String NON_TERMINAL_FONT_SIZE = "nonTerminalFontSize";
	static final String NON_TERMINAL_FONT_TYPE = "nonTerminalFontType";
	static final String NON_TERMINAL_KEYBOARD_LOCALE = "nonTerminalKeyboardLocale";
	static final String NON_TERMINAL_KEYBOARD_DESCRIPTION = "nonTerminalKeyboardDescription";
	static final String NON_TERMINAL_KEYBOARD_MAC_DESCRIPTION = "nonTerminalKeyboardMacDescription";
	static final String NON_TERMINAL_KEYBOARD_WINDOWS_LANG_ID = "nonTerminalKeyboardWindowsLangId";
	static final String SAVE_AS_PNG = "saveAsPng";
	static final String SAVE_AS_SVG = "saveAsSVG";
	static final String SHOW_FLAT_VIEW = "showFlatView";
	static final String SYNTAGMEME_KEYBOARD_LOCALE = "syntagmemeKeyboardLocale";
	static final String SYNTAGMEME_KEYBOARD_DESCRIPTION = "syntagmemeKeyboardDescription";
	static final String SYNTAGMEME_KEYBOARD_MAC_DESCRIPTION = "syntagmemeKeyboardMacDescription";
	static final String SYNTAGMEME_KEYBOARD_WINDOWS_LANG_ID = "syntagmemeKeyboardWindowsLangId";
	static final String USE_COLUMN_ORIENTED_ALGORITHM = "useColumnOrientedAlgorithm";
	static final String CENTER_NODES_OVER_DAUGHTERS = "centerNodesOverDaughters";
	static final String VERTICAL_GAP = "verticalGap";

	Preferences prefs;
	ColorXmlAdaptor adaptor;

	public ApplicationPreferences(Object app) {
		prefs = Preferences.userNodeForPackage(app.getClass());
		adaptor = new ColorXmlAdaptor();
	}

	public String getLastOpenedFilePath() {
		return prefs.get(LAST_OPENED_FILE_PATH, null);
	}

	public void setLastOpenedFilePath(String lastOpenedFile) {
		setPreferencesKey(LAST_OPENED_FILE_PATH, lastOpenedFile);
	}

	public String getLastLocaleLanguage() {
		return prefs.get(LAST_LOCALE_LANGUAGE, DEFAULT_LOCALE_LANGUAGE);
	}

	public void setLastLocaleLanguage(String lastLocaleLanguage) {
		setPreferencesKey(LAST_LOCALE_LANGUAGE, lastLocaleLanguage);
	}

	public File getLastOpenedFile() {
		String filePath = prefs.get(LAST_OPENED_FILE_PATH, null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	public void setLastOpenedFilePath(File file) {
		if (file != null) {
			setPreferencesKey(LAST_OPENED_FILE_PATH, file.getPath());

		} else {
			prefs.remove(LAST_OPENED_FILE_PATH);
		}
	}

	@Override
	public String getLastOpenedDirectoryPath() {
		return prefs.get(LAST_OPENED_DIRECTORY_PATH, "");
	}

	@Override
	public void setLastOpenedDirectoryPath(String directoryPath) {
		setPreferencesKey(LAST_OPENED_DIRECTORY_PATH, directoryPath);
	}

	public boolean getDrawAsType() {
		return prefs.getBoolean(DRAW_AS_TYPE, false);
	}

	public void setDrawAsType(boolean fDrawAsType) {
		setPreferencesKey(DRAW_AS_TYPE, fDrawAsType);
	}

	public boolean getDrawVerticalLineWithEmptyText() {
		return prefs.getBoolean(DRAW_VERTICAL_LINE_WITH_EMPTY_TEXT, false);
	}

	public void setDrawVerticalLineWithEmptyText(boolean value) {
		setPreferencesKey(DRAW_VERTICAL_LINE_WITH_EMPTY_TEXT, value);
	}

	public boolean getShowFullFilePath() {
		return prefs.getBoolean(SHOW_FULL_FILE_PATH, false);
	}

	public void setShowFullFilePath(boolean fShowFullFilePath) {
		setPreferencesKey(SHOW_FULL_FILE_PATH, fShowFullFilePath);
	}

	public double getShowMatchingParenDelay() {
		return prefs.getDouble(SHOW_MATCHING_PAREN_DELAY, 750.0);
	}

	public void setShowMatchingParenDelay(double dSize) {
		setPreferencesKey(SHOW_MATCHING_PAREN_DELAY, dSize);
	}

	public boolean getShowMatchingParenWithArrowKeys() {
		return prefs.getBoolean(SHOW_MATCHING_PAREN_WITH_ARROW_KEYS, false);
	}

	public void setShowMatchingParenWithArrowKeys(boolean fShowMatchingParenWithArrowKeys) {
		setPreferencesKey(SHOW_MATCHING_PAREN_WITH_ARROW_KEYS, fShowMatchingParenWithArrowKeys);
	}

	public double getTreeDescriptionFontSize() {
		return prefs.getDouble(TREE_DESCRIPTION_FONT_SIZE, 12.0);
	}

	public void setTreeDescriptionFontSize(double dSize) {
		setPreferencesKey(TREE_DESCRIPTION_FONT_SIZE, dSize);
	}

	public boolean getUseColumnOrientedAlgorithm() {
		return prefs.getBoolean(USE_COLUMN_ORIENTED_ALGORITHM, false);
	}

	public void setUseColumnOrientedAlgorithm(boolean value) {
		setPreferencesKey(USE_COLUMN_ORIENTED_ALGORITHM, value);
	}

	public boolean getCenterNodesOverDaughters() {
		return prefs.getBoolean(CENTER_NODES_OVER_DAUGHTERS, false);
	}

	public void setCenterNodesOverDaughters(boolean value) {
		setPreferencesKey(CENTER_NODES_OVER_DAUGHTERS, value);
	}

	public Stage getLastWindowParameters(String sWindow, Stage stage, Double defaultHeight,
			Double defaultWidth) {
		double dHeight = prefs.getDouble(sWindow + HEIGHT, defaultHeight);
		double dWidth = prefs.getDouble(sWindow + WIDTH, defaultWidth);
		double dX = prefs.getDouble(sWindow + POSITION_X, 10);
		double dY = prefs.getDouble(sWindow + POSITION_Y, 10);
		if (!isXOnAScreen(dX) || !isYOnAScreen(dY)) {
			dX = 10;
			dY = 10;
			dHeight = defaultHeight;
			dWidth = defaultWidth;
		}
		stage.setHeight(dHeight);
		stage.setWidth(dWidth);
		stage.setX(dX);
		stage.setY(dY);
		boolean fValue = prefs.getBoolean(sWindow + MAXIMIZED, false);
		stage.setMaximized(fValue);
		return stage;
	}

	public boolean isXOnAScreen(double dX) {
		for (Screen screen : Screen.getScreens()) {
			Rectangle2D bounds = screen.getVisualBounds();
			if (dX >= bounds.getMinX() && dX <= bounds.getMaxX()) {
				return true;
			}
		}
		return false;
	}

	public boolean isYOnAScreen(double dY) {
		for (Screen screen : Screen.getScreens()) {
			Rectangle2D bounds = screen.getVisualBounds();
			if (dY >= bounds.getMinY() && dY <= bounds.getMaxY()) {
				return true;
			}
		}
		return false;
	}

	public void setLastWindowParameters(String sWindow, Stage stage) {
		boolean isMaximized = stage.isMaximized();
		if (!isMaximized) {
			setPreferencesKey(sWindow + HEIGHT, stage.getHeight());
			setPreferencesKey(sWindow + WIDTH, stage.getWidth());
			setPreferencesKey(sWindow + POSITION_X, stage.getX());
			setPreferencesKey(sWindow + POSITION_Y, stage.getY());
		}
		setPreferencesKey(sWindow + MAXIMIZED, stage.isMaximized());
	}

	public double getLastSplitPaneDividerPosition() {
		return prefs.getDouble(LAST_SPLIT_PANE_POSITION, 0.5);
	}

	public void setLastSplitPaneDividerPosition(double position) {
		setPreferencesKey(LAST_SPLIT_PANE_POSITION, position);
	}

	public void getSavedTreeParameters(LingTreeTree ltTree) {
		ltTree.setHorizontalGap(prefs.getDouble(HORIZONTAL_GAP, 20));
		ltTree.setHorizontalOffset(prefs.getDouble(HORIZONTAL_OFFSET, 225));
		ltTree.setInitialXCoordinate(prefs.getDouble(INITIAL_X_COORDINATE, 10));
		ltTree.setInitialYCoordinate(prefs.getDouble(INITIAL_Y_COORDINATE, 20));
		ltTree.setLexGlossGapAdjustment(prefs.getDouble(LEX_GLOSS_GAP_ADJUSTMENT, 0));
		ltTree.setMinimumXGapForExtraVerticalSpacing(prefs.getDouble(MINIMUM_X_GAP_FOR_EXTRA_VERTICAL_SPACING, 500));
		ltTree.setVerticalAdjustmentForExtraVerticalSpacing(prefs.getDouble(VERTICAL_ADJUSTMENT_FOR_EXTRA_VERTICAL_SPACING, 75));
		ltTree.setLineWidth(prefs.getDouble(LINE_WIDTH, 1));
		ltTree.setSaveAsPng(prefs.getBoolean(SAVE_AS_PNG, false));
		ltTree.setSaveAsSVG(prefs.getBoolean(SAVE_AS_SVG, true));
		ltTree.setShowFlatView(prefs.getBoolean(SHOW_FLAT_VIEW, false));
		ltTree.setVerticalGap(prefs.getDouble(VERTICAL_GAP, 20));

		ltTree.setBackgroundColor(Color.web(prefs.get(BACKGROUND_COLOR, "#ffffff")));
		ltTree.setLineColor(Color.web(prefs.get(LINE_COLOR, "#000000")));

		final String sDefaultFamily = "Arial";
		final String sDefaultType = "Regular";
		final String sDefaultColor = "#000000";
		FontInfo fontInfo = new FontInfo(prefs.get(ABBREVIATION_FONT_FAMILY, sDefaultFamily), prefs.getDouble(
				ABBREVIATION_FONT_SIZE, 12), prefs.get(ABBREVIATION_FONT_TYPE, "Italic"));
		fontInfo.setColor(Color.web(prefs.get(ABBREVIATION_FONT_COLOR, sDefaultColor)));
		ltTree.setAbbreviationFontInfo(fontInfo);
		fontInfo = new FontInfo(prefs.get(EMPTY_ELEMENT_FONT_FAMILY, sDefaultFamily), prefs.getDouble(
				EMPTY_ELEMENT_FONT_SIZE, 12), prefs.get(EMPTY_ELEMENT_FONT_TYPE, "Italic"));
		fontInfo.setColor(Color.web(prefs.get(EMPTY_ELEMENT_FONT_COLOR, sDefaultColor)));
		ltTree.setEmptyElementFontInfo(fontInfo);

		fontInfo = new FontInfo(prefs.get(GLOSS_FONT_FAMILY, sDefaultFamily),
				prefs.getDouble(GLOSS_FONT_SIZE, 12), prefs.get(GLOSS_FONT_TYPE, sDefaultType));
		fontInfo.setColor(Color.web(prefs.get(GLOSS_FONT_COLOR, "#008000")));
		ltTree.setGlossFontInfo(fontInfo);

		fontInfo = new FontInfo(prefs.get(LEXICAL_FONT_FAMILY, sDefaultFamily), prefs.getDouble(
				LEXICAL_FONT_SIZE, 12), prefs.get(LEXICAL_FONT_TYPE, "Bold"));
		fontInfo.setColor(Color.web(prefs.get(LEXICAL_FONT_COLOR, "#0000ff")));
		ltTree.setLexicalFontInfo(fontInfo);

		fontInfo = new FontInfo(prefs.get(NON_TERMINAL_FONT_FAMILY, sDefaultFamily),
				prefs.getDouble(NON_TERMINAL_FONT_SIZE, 12), prefs.get(NON_TERMINAL_FONT_TYPE,
						sDefaultType));
		fontInfo.setColor(Color.web(prefs.get(NON_TERMINAL_FONT_COLOR, sDefaultColor)));
		ltTree.setNonTerminalFontInfo(fontInfo);

		final String sDefaultLocale = "en";
		final String sDefaultDescription = "English";
		final String sDefaultMacDescription = "US";
		final int defaultWindowsLangId = 0;
		KeyboardInfo ki = new KeyboardInfo(prefs.get(GLOSS_KEYBOARD_LOCALE, sDefaultLocale),
				prefs.get(GLOSS_KEYBOARD_DESCRIPTION, sDefaultDescription), prefs.getInt(
						GLOSS_KEYBOARD_WINDOWS_LANG_ID, defaultWindowsLangId));
		ki.setMacDescription(prefs.get(GLOSS_KEYBOARD_MAC_DESCRIPTION, sDefaultMacDescription));
		ltTree.setGlossKeyboard(ki);

		ki = new KeyboardInfo(prefs.get(EMPTY_ELEMENT_KEYBOARD_LOCALE, sDefaultLocale),
				prefs.get(EMPTY_ELEMENT_KEYBOARD_DESCRIPTION, sDefaultDescription), prefs.getInt(
						EMPTY_ELEMENT_KEYBOARD_WINDOWS_LANG_ID, defaultWindowsLangId));
		ki.setMacDescription(prefs.get(EMPTY_ELEMENT_KEYBOARD_MAC_DESCRIPTION, sDefaultMacDescription));
		ltTree.setEmptyElementKeyboard(ki);

		ki = new KeyboardInfo(prefs.get(LEXICAL_KEYBOARD_LOCALE, sDefaultLocale),
				prefs.get(LEXICAL_KEYBOARD_DESCRIPTION, sDefaultDescription), prefs.getInt(
						LEXICAL_KEYBOARD_WINDOWS_LANG_ID, defaultWindowsLangId));
		ki.setMacDescription(prefs.get(LEXICAL_KEYBOARD_MAC_DESCRIPTION, sDefaultMacDescription));
		ltTree.setLexicalKeyboard(ki);

		ki = new KeyboardInfo(prefs.get(NON_TERMINAL_KEYBOARD_LOCALE, sDefaultLocale),
				prefs.get(NON_TERMINAL_KEYBOARD_DESCRIPTION, sDefaultDescription), prefs.getInt(
						NON_TERMINAL_KEYBOARD_WINDOWS_LANG_ID, defaultWindowsLangId));
		ki.setMacDescription(prefs.get(NON_TERMINAL_KEYBOARD_MAC_DESCRIPTION, sDefaultMacDescription));
		ltTree.setNonTerminalKeyboard(ki);

		ki = new KeyboardInfo(prefs.get(SYNTAGMEME_KEYBOARD_LOCALE, sDefaultLocale),
				prefs.get(SYNTAGMEME_KEYBOARD_DESCRIPTION, sDefaultDescription), prefs.getInt(
						SYNTAGMEME_KEYBOARD_WINDOWS_LANG_ID, defaultWindowsLangId));
		ki.setMacDescription(prefs.get(SYNTAGMEME_KEYBOARD_MAC_DESCRIPTION, sDefaultMacDescription));
		ltTree.setSyntagmemeKeyboard(ki);
	}

	public void setSavedTreeParameters(LingTreeTree ltTree) throws Exception {
		setPreferencesKey(HORIZONTAL_GAP, ltTree.getHorizontalGap());
		setPreferencesKey(HORIZONTAL_OFFSET, ltTree.getHorizontalOffset());
		setPreferencesKey(INITIAL_X_COORDINATE, ltTree.getInitialXCoordinate());
		setPreferencesKey(INITIAL_Y_COORDINATE, ltTree.getInitialYCoordinate());
		setPreferencesKey(LEX_GLOSS_GAP_ADJUSTMENT, ltTree.getLexGlossGapAdjustment());
		setPreferencesKey(MINIMUM_X_GAP_FOR_EXTRA_VERTICAL_SPACING, ltTree.getMinimumXGapForExtraVerticalSpacing());
		setPreferencesKey(VERTICAL_ADJUSTMENT_FOR_EXTRA_VERTICAL_SPACING, ltTree.getVerticalAdjustmentForExtraVerticalSpacing());
		setPreferencesKey(LINE_WIDTH, ltTree.getLineWidth());
		setPreferencesKey(SAVE_AS_PNG, ltTree.isSaveAsPng());
		setPreferencesKey(SAVE_AS_SVG, ltTree.isSaveAsSVG());
		setPreferencesKey(SHOW_FLAT_VIEW, ltTree.isShowFlatView());
		setPreferencesKey(VERTICAL_GAP, ltTree.getVerticalGap());
		setPreferencesKey(DRAW_VERTICAL_LINE_WITH_EMPTY_TEXT, ltTree.isDrawVerticalLineWithEmptyText());
		setPreferencesKey(USE_COLUMN_ORIENTED_ALGORITHM, ltTree.isUseColumnOrientedAlgorithm());
		setPreferencesKey(CENTER_NODES_OVER_DAUGHTERS, ltTree.isCenterColumnOrientedOnDaughtersWidth());

		setPreferencesKey(BACKGROUND_COLOR, ltTree.getBackgroundColor());
		setPreferencesKey(LINE_COLOR, ltTree.getLineColor());
		FontInfo fontInfo = ltTree.getAbbreviationFontInfo();
		setPreferencesKey(ABBREVIATION_FONT_COLOR, fontInfo.getColor());
		setPreferencesKey(ABBREVIATION_FONT_FAMILY, fontInfo.getFontFamily());
		setPreferencesKey(ABBREVIATION_FONT_SIZE, fontInfo.getFontSize());
		setPreferencesKey(ABBREVIATION_FONT_TYPE, fontInfo.getFontType());
		fontInfo = ltTree.getEmptyElementFontInfo();
		setPreferencesKey(EMPTY_ELEMENT_FONT_COLOR, fontInfo.getColor());
		setPreferencesKey(EMPTY_ELEMENT_FONT_FAMILY, fontInfo.getFontFamily());
		setPreferencesKey(EMPTY_ELEMENT_FONT_SIZE, fontInfo.getFontSize());
		setPreferencesKey(EMPTY_ELEMENT_FONT_TYPE, fontInfo.getFontType());
		fontInfo = ltTree.getGlossFontInfo();
		setPreferencesKey(GLOSS_FONT_COLOR, fontInfo.getColor());
		setPreferencesKey(GLOSS_FONT_FAMILY, fontInfo.getFontFamily());
		setPreferencesKey(GLOSS_FONT_SIZE, fontInfo.getFontSize());
		setPreferencesKey(GLOSS_FONT_TYPE, fontInfo.getFontType());
		fontInfo = ltTree.getLexicalFontInfo();
		setPreferencesKey(LEXICAL_FONT_COLOR, fontInfo.getColor());
		setPreferencesKey(LEXICAL_FONT_FAMILY, fontInfo.getFontFamily());
		setPreferencesKey(LEXICAL_FONT_SIZE, fontInfo.getFontSize());
		setPreferencesKey(LEXICAL_FONT_TYPE, fontInfo.getFontType());
		fontInfo = ltTree.getNonTerminalFontInfo();
		setPreferencesKey(NON_TERMINAL_FONT_COLOR, fontInfo.getColor());
		setPreferencesKey(NON_TERMINAL_FONT_FAMILY, fontInfo.getFontFamily());
		setPreferencesKey(NON_TERMINAL_FONT_SIZE, fontInfo.getFontSize());
		setPreferencesKey(NON_TERMINAL_FONT_TYPE, fontInfo.getFontType());

		KeyboardInfo ki = ltTree.getGlossKeyboard();
		setPreferencesKey(GLOSS_KEYBOARD_LOCALE, ki.getSLocale());
		setPreferencesKey(GLOSS_KEYBOARD_DESCRIPTION, ki.getDescription());
		setPreferencesKey(GLOSS_KEYBOARD_MAC_DESCRIPTION, ki.getMacDescription());
		setPreferencesKey(GLOSS_KEYBOARD_WINDOWS_LANG_ID, ki.getWindowsLangID());
		ki = ltTree.getEmptyElementKeyboard();
		setPreferencesKey(EMPTY_ELEMENT_KEYBOARD_LOCALE, ki.getSLocale());
		setPreferencesKey(EMPTY_ELEMENT_KEYBOARD_DESCRIPTION, ki.getDescription());
		setPreferencesKey(EMPTY_ELEMENT_KEYBOARD_MAC_DESCRIPTION, ki.getMacDescription());
		setPreferencesKey(EMPTY_ELEMENT_KEYBOARD_WINDOWS_LANG_ID, ki.getWindowsLangID());
		ki = ltTree.getLexicalKeyboard();
		setPreferencesKey(LEXICAL_KEYBOARD_LOCALE, ki.getSLocale());
		setPreferencesKey(LEXICAL_KEYBOARD_DESCRIPTION, ki.getDescription());
		setPreferencesKey(LEXICAL_KEYBOARD_MAC_DESCRIPTION, ki.getMacDescription());
		setPreferencesKey(LEXICAL_KEYBOARD_WINDOWS_LANG_ID, ki.getWindowsLangID());
		ki = ltTree.getNonTerminalKeyboard();
		setPreferencesKey(NON_TERMINAL_KEYBOARD_LOCALE, ki.getSLocale());
		setPreferencesKey(NON_TERMINAL_KEYBOARD_DESCRIPTION, ki.getDescription());
		setPreferencesKey(NON_TERMINAL_KEYBOARD_MAC_DESCRIPTION, ki.getMacDescription());
		setPreferencesKey(NON_TERMINAL_KEYBOARD_WINDOWS_LANG_ID, ki.getWindowsLangID());
		ki = ltTree.getSyntagmemeKeyboard();
		setPreferencesKey(SYNTAGMEME_KEYBOARD_LOCALE, ki.getSLocale());
		setPreferencesKey(SYNTAGMEME_KEYBOARD_DESCRIPTION, ki.getDescription());
		setPreferencesKey(SYNTAGMEME_KEYBOARD_MAC_DESCRIPTION, ki.getMacDescription());
		setPreferencesKey(SYNTAGMEME_KEYBOARD_WINDOWS_LANG_ID, ki.getWindowsLangID());
}

	private void setPreferencesKey(String key, boolean value) {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null) {
				prefs.putBoolean(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	private void setPreferencesKey(String key, Color color) throws Exception {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null) {
				String value = adaptor.marshal(color);
				prefs.put(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	private void setPreferencesKey(String key, Double value) {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null && value != null) {
				prefs.putDouble(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	private void setPreferencesKey(String key, int value) {
		if (!StringUtilities.isNullOrEmpty(key)) {
			if (key != null) {
				prefs.putInt(key, value);

			} else {
				prefs.remove(key);
			}
		}
	}

	private void setPreferencesKey(String key, String value) {
		if (!StringUtilities.isNullOrEmpty(key) && !StringUtilities.isNullOrEmpty(value)) {
			prefs.put(key, value);

		} else {
			prefs.remove(key);
		}
	}
}
