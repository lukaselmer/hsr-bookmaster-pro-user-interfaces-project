package com.community.xanadu.components.dnd;

public class DnDLock {

	private static boolean lock = false;

	public static void get() {
		lock = true;
	}

	public static boolean isLocked() {
		return lock;
	}

	public static void release() {
		lock = false;
	}
}
