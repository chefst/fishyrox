package de.chefst.fishyrox;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.Console;
import java.util.Random;

public class Automat {

	Random random;
	Robot robot;
	Point originalMouseLoc;

	public Automat() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		random = new Random(System.currentTimeMillis());
		Console con = System.console();
		System.out.println("Place mouse _inside_ the fishing circle but close to the border");
		System.out.println("Control+C to quit, Enter to start after 5 seconds");
		con.readLine();
		countdown();
		
		originalMouseLoc = getMouseLoc();
		
		while (true) {
			// wiggle the mouse a little
			Point currentMouseLoc = getMouseLoc();
			mouseGlide(currentMouseLoc.x, currentMouseLoc.y, originalMouseLoc.x+random.nextInt(-5, 5), originalMouseLoc.y+random.nextInt(-5, 5), 100, 20);
			
			// click mouse - initiate fishing
			mouseClickLeft();
			System.out.println("start cycle");
			
			// wait until the mouse pixel turns green
			findGreenLoop();
			System.out.println("found green signal");
			
			// wait a little then click!
			sleepVar(30, 100);
			mouseClickLeft();
			System.out.println("catch!");
			
			// should have caught a fish, wait for the next cycle
			sleepVar(5000, 1000);
		}

	}

	public void findGreenLoop() {
		while (true) {
			Point ml = getMouseLoc();
			Color c = robot.getPixelColor(ml.x, ml.y);
			if (c.getGreen() > 200 && c.getRed() < 180 && c.getBlue() < 180) {
				return;
			}
			sleepVar(10, 0);
		}
	}

	public Point getMouseLoc() {
		return MouseInfo.getPointerInfo().getLocation();
	}

	public void sleepVar(int fix, int var) {
		try {
			Thread.sleep(fix + (var > 0 ? random.nextInt(var) : 0));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void countdown() {
		try {
			System.out.println("5 ...");
			Thread.sleep(1000);
			System.out.println("4 ...");
			Thread.sleep(1000);
			System.out.println("3 ...");
			Thread.sleep(1000);
			System.out.println("2 ...");
			Thread.sleep(1000);
			System.out.println("1 ...");
			Thread.sleep(1000);
			System.out.println("go!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
		try {
			Robot r = robot;
			double dx = (x2 - x1) / ((double) n);
			double dy = (y2 - y1) / ((double) n);
			double dt = t / ((double) n);
			for (int step = 1; step <= n; step++) {
				Thread.sleep((int) dt);
				r.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
			}
			;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void mouseClickLeft() {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		sleepVar(8, 9);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	
	public void printCurrentColor() {
		long t1 = System.currentTimeMillis();
		Point ml = getMouseLoc();
		Color c = robot.getPixelColor(ml.x, ml.y);
		long t2 = System.currentTimeMillis() - t1;
		System.out.println(c + "t: " + t2);
	}

	public void readColorTestLoop() {
		try {
			while (true) {
				printCurrentColor();
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
