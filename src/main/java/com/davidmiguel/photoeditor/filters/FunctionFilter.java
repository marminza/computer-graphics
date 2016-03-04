package com.davidmiguel.photoeditor.filters;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Functional filter (Brightness, Constrast, Gamma, Inversion).
 */
public abstract class FunctionFilter implements Filter {

	protected static final short BITS = 256;
	protected short[] lookupTable;

	public FunctionFilter(double value) {
		lookupTable = new short[BITS];
		createLookupTable(value);
	}

	/**
	 * Compute the lookup table for the given value.
	 */
	protected abstract void createLookupTable(double value);

	@Override
	public Image apply(Image input) {
		// Reader for the original image
		PixelReader pixelReader = input.getPixelReader();
		int width = (int) input.getWidth();
		int height = (int) input.getHeight();
		// Create new image and get its writer
		WritableImage result = new WritableImage(width, height);
		PixelWriter pixelWriter = result.getPixelWriter();
		// Apply transformation with the lookup table
		for (int y = 0; y < input.getHeight(); y++) {
			for (int x = 0; x < input.getWidth(); x++) {
				// Actual color
				Color color = pixelReader.getColor(x, y);
				// from [0,1) to [0,BITS)
				int r = (int) (color.getRed() * (BITS - 1));
				int g = (int) (color.getGreen() * (BITS - 1));
				int b = (int) (color.getBlue() * (BITS - 1));
				int a = (int) color.getOpacity();
				// New color
				int newR = lookupTable[r];
				int newG = lookupTable[g];
				int newB = lookupTable[b];
				Color newColor = Color.rgb(newR, newG, newB, a);
				// Write new pixel
				pixelWriter.setColor(x, y, newColor);
			}
		}
		return result;
	}
}