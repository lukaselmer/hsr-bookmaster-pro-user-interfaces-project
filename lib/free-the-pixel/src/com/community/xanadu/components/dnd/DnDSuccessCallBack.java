package com.community.xanadu.components.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetContext;

public interface DnDSuccessCallBack {

	void dropSuccess(DataFlavor flavor, DropTargetContext context, Transferable transferable, Point location);

}
