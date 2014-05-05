package com.vagoscorp.vccompost.rc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class Grua {

	Canvas canvas;
	float Width;
	float Height;
	
	Paint gruPaint = new Paint();
	Paint carPaint = new Paint();
	Paint gruD = new Paint();
	Paint carD = new Paint();
	Paint gruI = new Paint();
	Paint carI = new Paint();
	Paint touch = new Paint();
	
	float xTouch = 0;
	float yTouch = 0;
	float xActual = 0;
	float yActual = 0;
	float xDeseado = 0;
	float yDeseado = 0;
	float xInicial = 0;
	float yInicial = 0;
	float ex = 0;
	float ey = 0;
	float dx = 0;
	float dy = 0;
	float limX = 0;
	float limY = 0;
	
	public Grua() {
		gruPaint.setARGB(255, 6, 151, 76);
		gruI.setARGB(70, 6, 151, 76);
		gruD.setARGB(100, 6, 151, 76);
		// gru.setARGB(255, 71, 75, 86);
		// gru.setStyle(Paint.Style.STROKE);
		// gru.setStrokeWidth(5);

		carPaint.setARGB(255, 154, 137, 137);
		carI.setARGB(70, 154, 137, 137);
		carD.setARGB(100, 154, 137, 137);
		// car.setStyle(Paint.Style.STROKE);
		// car.setStrokeWidth(5);
		touch.setARGB(120, 100, 100, 250);// setColor(Color.DKGRAY);
		touch.setAntiAlias(true);
		touch.setStrokeWidth(2);
		touch.setStrokeCap(Paint.Cap.ROUND);
		touch.setStyle(Paint.Style.STROKE);
	}
	
	public void draw(SurfaceHolder gruHolder, boolean enMovimiento) {
	this.canvas = gruHolder.lockCanvas();
	this.Width = this.canvas.getWidth();
	this.Height = this.canvas.getHeight();
	this.dx = (float) this.Width / 1000;
	this.dy = (float) this.Height / 1000;
	this.canvas.drawColor(Color.BLACK);
	yInicial = Height/2;
	yActual = Height/2;
	yDeseado = Height/2;
	drawGru(this.xInicial, this.yInicial, gruI, carI);
	if (!enMovimiento)
		drawGrut(gruPaint, carPaint);
	else {
		drawGru(this.xDeseado, this.yDeseado, gruD, carD);
		drawGru(this.xActual, this.yActual, gruPaint, carPaint);
	}
//	drawtouch(canvas);
	gruHolder.unlockCanvasAndPost(this.canvas);
}
	
	public void drawGru(/* Canvas canvas */float x, float y, Paint grua,
			Paint carr) {
		float wPat = this.dx * 70;
		float hPat = this.dy * 250;
		float wCar = this.dx * 100;
		float hCar = this.dy * 150;
		float hGru = this.dy * 100;
		this.limX = wPat + wCar;
		this.limY = hPat;
		this.ex = (float)(this.Width - (float)2*this.limX) / 1000;
		this.ey = (float)(this.Height - (float)2*this.limY) / 1000;
		float mwPat = wPat / 2;
		if (x <= this.limX)
			x = this.limX;
		if (y <= hPat)
			y = hPat;
		if (x >= this.Width - this.limX)
			x = this.Width - this.limX;
		if (y >= this.Height - hPat)
			y = this.Height - hPat;
		this.canvas.drawLine(wPat, 0, wPat, this.Height, gruPaint);
		this.canvas.drawLine(mwPat, 0, mwPat, this.Height, gruPaint);
		this.canvas.drawLine(0, 0, 0, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - wPat, 0, this.Width - wPat, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - mwPat, 0, this.Width - mwPat, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - 1, 0, this.Width - 1, this.Height, gruPaint);
		this.canvas.drawRect(0, y - hPat, wPat, y + hPat, grua);
		this.canvas.drawRect(this.Width - wPat, y - hPat, this.Width, y + hPat, grua);
		this.canvas.drawRect(0, y - hGru, this.Width, y + hGru, grua);
		this.canvas.drawRect(x - wCar, y - hCar, x + wCar, y + hCar, carr);
	}
	
	public void drawGrut(/* Canvas canvas */Paint grua, Paint carr) {
		float wPat = this.dx * 70;
		float hPat = this.dy * 250;
		float wCar = this.dx * 100;
		float hCar = this.dy * 150;
		float hGru = this.dy * 100;
		this.limX = wPat + wCar;
		this.limY = hPat;
		this.ex = (float)(this.Width - (float)2*this.limX) / 1000;
		this.ey = (float)(this.Height - (float)2*this.limY) / 1000;
		float mwPat = wPat / 2;
		yTouch = Height/2;
		if (xTouch <= this.limX)
			xTouch = this.limX;
//		if (yTouch <= hPat)
//			yTouch = hPat;
		if (xTouch >= this.Width - this.limX)
			xTouch = this.Width - this.limX;
//		if (yTouch >= this.Height - hPat)
//			yTouch = this.Height - hPat;
		this.canvas.drawLine(wPat, 0, wPat, this.Height, gruPaint);
		this.canvas.drawLine(mwPat, 0, mwPat, this.Height, gruPaint);
		this.canvas.drawLine(0, 0, 0, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - wPat, 0, this.Width - wPat, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - mwPat, 0, this.Width - mwPat, this.Height, gruPaint);
		this.canvas.drawLine(this.Width - 1, 0, this.Width - 1, this.Height, gruPaint);
		this.canvas.drawRect(0, yTouch - hPat, wPat, yTouch + hPat, grua);
		this.canvas.drawRect(this.Width - wPat, yTouch - hPat, this.Width, yTouch + hPat, grua);
		this.canvas.drawRect(0, yTouch - hGru, this.Width, yTouch + hGru, grua);
		this.canvas.drawRect(xTouch - wCar, yTouch - hCar, xTouch + wCar, yTouch + hCar, carr);
	}
	
	public void drawGruv(/* Canvas canvas */float x, float y, Paint grua,
			Paint carr) {
		float wPat = this.dx * 100;
		float hPat = this.dy * 100;
		float wCar = this.dx * 60;
		float hCar = this.dy * 130;
		float wGru = this.dx * 35;
		this.limX = hPat;
		this.limY = hPat + hCar;
		this.ex = (float)(this.Width - (float)2*this.limX) / 1000;
		this.ey = (float)(this.Height - (float)2*this.limY) / 1000;
		float mhPat = hPat / 2;
		if (x <= wPat)
			x = wPat;
		if (y <= this.limY)
			y = this.limY;
		if (x >= this.Width - wPat)
			x = this.Width - wPat;
		if (y >= this.Height - this.limY)
			y = this.Height - this.limY;
		this.canvas.drawLine(0, hPat, this.Width, hPat, gruPaint);
		this.canvas.drawLine(0, mhPat, this.Width, mhPat, gruPaint);
		this.canvas.drawLine(0, 0, this.Width, 0, gruPaint);
		this.canvas.drawLine(0, this.Height - hPat, this.Width, this.Height - hPat, gruPaint);
		this.canvas.drawLine(0, this.Height - mhPat, this.Width, this.Height - mhPat, gruPaint);
		this.canvas.drawLine(0, this.Height - 1, this.Width, this.Height - 1, gruPaint);
		this.canvas.drawRect(x - wPat, 0, x + wPat, hPat, grua);
		this.canvas.drawRect(x - wPat, this.Height - hPat, x + wPat, this.Height, grua);
		this.canvas.drawRect(x - wGru, 0, x + wGru, this.Height, grua);
		this.canvas.drawRect(x - wCar, y - hCar, x + wCar, y + hCar, carr);
	}
	
	public void drawtouch(Canvas canvas) {
		if (this.xTouch <= 0)
			this.xTouch = 0;
		if (this.yTouch <= 0)
			this.yTouch = 0;
		if (this.xTouch >= this.Width)
			this.xTouch = this.Width;
		if (this.yTouch >= this.Height)
			this.yTouch = this.Height;
		canvas.drawCircle(this.xTouch, this.yTouch, 40, touch);
	}

}
