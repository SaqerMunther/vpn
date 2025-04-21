package com.app.dev.cmon.utilites;

public class ColorUtil {
	private static ColorUtil cUtilSingleton;
	private String defaultColor;
	private String normal;
	private String warning;
	private String error;
	private String defaultOuterGradient;
	private String normalOuterGradient;
	private String warningOuterGradient;
	private String errorOuterGradient;
	
	private ColorUtil() {
		defaultColor = "#37568F";
		normal = "#59D67B";
		warning = "#FFAC24";
		error = "#D34864";
		defaultOuterGradient = "#3280BB";
		normalOuterGradient = "#3AA456";
		warningOuterGradient = "#CE8E26";
		errorOuterGradient = "#90142D";
	}
	
	public static ColorUtil getInstance() {
		if (cUtilSingleton == null)
			cUtilSingleton = new ColorUtil();
		return cUtilSingleton;
	}

	public String getColor(String type, boolean isGradient) {
		switch (type) {
			case "normal":
				return isGradient ? getNormal() : getNormalOuterGradient();
			case "warning":
				return isGradient ? getWarning() : getWarningOuterGradient();
			case "error":
				return isGradient ? getError() : getErrorOuterGradient();
		}
		
		return isGradient ? getDefaultColor() : getDefaultOuterGradient();
	}

	private String getNormal() {
		return normal;
	}

	private String getWarning() {
		return warning;
	}

	private String getError() {
		return error;
	}

	private String getNormalOuterGradient() {
		return normalOuterGradient;
	}

	private String getWarningOuterGradient() {
		return warningOuterGradient;
	}

	private String getErrorOuterGradient() {
		return errorOuterGradient;
	}

	private String getDefaultColor() {
		return defaultColor;
	}

	private String getDefaultOuterGradient() {
		return defaultOuterGradient;
	}
}
