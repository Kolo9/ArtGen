package artgen;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Artist {
	
	private static enum Type {
		SQUARES, OFF_MOD, WASHED_SQUARES;
	}
	
	private static final float BASIC_INTENSITY = .8f; // Negative for secondary
	private static final int W = 1000;
	private static final int H = 1000;
	private static final boolean USING_MAP = true;
	private static final Type TYPE = Type.SQUARES;
	private static final boolean MATCH_NON_DOMINANT = false;
	private static final int SIDE_LENGTH = 5;
	
	public static void main(String[] args) throws IOException {
		float multR=1, multG=1, multB=1;
		float r, g, b;
		for (int c = 1; c <= 10; c++) {
			BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_3BYTE_BGR);
			Graphics gfx = img.getGraphics();
			int[][] map = makeSquareMap(SIDE_LENGTH);
			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					multR = multG = multB = 1;
					int xTrip = (int) (i / (img.getWidth() / 3));
					int yTrip = (int) (j / (img.getHeight() / 3));
					int condition = (USING_MAP) ? map[i][j] : (TYPE == Type.OFF_MOD) ? ((i * img.getHeight() + j) / (img.getHeight() / 3)) % 3 : (xTrip * 5 + yTrip) % 3;
					
					switch (condition) {
						case 0:
						case 3:
							if (TYPE == Type.WASHED_SQUARES) {
								multR = 1f + ((float) Math.pow(1 + (1f/(W*H))*10, (i*H+j)) * .002f);
							} else {
								multR = (map[i][j]==0 || !USING_MAP) ? 1 + BASIC_INTENSITY : 1 - BASIC_INTENSITY;
							}
							break;
						case 1:
						case 4:
							if (TYPE == Type.WASHED_SQUARES) {
								multG = 1f + ((float) Math.pow(1 + (1f/(W*H))*10, (i*H+j)) * .002f);
							} else {
								multG = (map[i][j]==1 || !USING_MAP) ? 1 + BASIC_INTENSITY : 1 - BASIC_INTENSITY;
							}
							break;
						case 2:
						case 5:
							if (TYPE == Type.WASHED_SQUARES) {
								multB = 1f + ((float) Math.pow(1 + (1f/(W*H))*10, (i*H+j)) * .002f);
							} else {
								multB = (map[i][j]==2 || !USING_MAP) ? 1 + BASIC_INTENSITY : 1 - BASIC_INTENSITY;
							}
							break;
					}
					r = (float) Math.min(Math.random()*multR, 1);
					g = (float) Math.min(Math.random()*multG, 1);
					b = (float) Math.min(Math.random()*multB, 1);
					
					
					if (MATCH_NON_DOMINANT) {
						if (multR != 1) {
							while (g > r) g = (float) Math.min(Math.random()*multG, 1);
							b = g;
						} else if (multG != 1) {
							while (b > g) b = (float) Math.min(Math.random()*multB, 1);
							r = b;
						} else if (multB != 1) {
							while (r > b) r = (float) Math.min(Math.random()*multR, 1);
							g = r;
						}
					}
					
					gfx.setColor(new Color(r, g, b));
					gfx.drawRect(i, j, 1, 1);
				}
			}
			File outFile = new File("art/img" + c + ".png");
			ImageIO.write(img, "png", outFile);
		}
	}
	
	private static int[][] makeSquareMap(int n) {
		int[][] map = new int[W][H];
		float blockWidth = (float)W/n;
		float blockHeight = (float)H/n;
		int cur;
		for (int i = 0; i < n*n; i++) {
			cur = (int) (Math.random() * 3);
			for (int x = (int)(blockWidth*(i%n)); x < Math.min(W, blockWidth*((i%n)+1)); x++) {
				for (int y = (int)(blockHeight*(i/n)); y < Math.min(H, blockHeight*((i/n)+1)); y++) {
					map[x][y] = cur;
				}
			}
		}
		
		/*
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				System.out.print(map[j][i]);
			}
			System.out.println();
		}
		*/
		return map;
	}
}