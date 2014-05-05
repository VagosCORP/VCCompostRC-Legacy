package com.vagoscorp.vccompost.rc;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class EjeZ {

	Canvas canvas;
	float Width;
	float Height;
	
	Paint gruPaint = new Paint();
	Paint carPaint = new Paint();
	Paint gruD = new Paint();
	Paint carD = new Paint();
	Paint gruI = new Paint();
	Paint carI = new Paint();
	Paint barraZ = new Paint();
	Paint barraA = new Paint();
	Paint barraZI = new Paint();
	Paint barraAI = new Paint();
	Paint barraZD = new Paint();
	Paint barraAD = new Paint();
	Paint touch = new Paint();
	
	float xTouch = 2000000;
	float yTouch = 0;
	float xActual = 2000000;
	float yActual = 0;
	float xDeseado = 2000000;
	float yDeseado = 0;
	float xInicial = 2000000;
	float yInicial = 0;
	float ew = 0;
	float ez = 0;
	float dx = 0;
	float dy = 0;
	float cent = 0;
	float lw = 0;
	float minX = 0;
	float minY = 0;
	float limX = 0;
	float limY = 0;
	float angd = 0;
	
	public EjeZ() {
		gruPaint.setARGB(255, 6, 151, 76);
		gruPaint.setTextSize(40);
//		gruI.setARGB(70, 147, 147, 167);
//		gruD.setARGB(100, 147, 147, 167);
		// gruPaint.setARGB(255, 71, 75, 86);
		gruPaint.setStyle(Paint.Style.STROKE);
		gruPaint.setStrokeWidth(5);
		carPaint.setARGB(255, 154, 137, 137);
		carI.setARGB(70, 154, 137, 137);
		carD.setARGB(100, 154, 137, 137);
		barraZ.setARGB(255, 97, 95, 95);
		barraZI.setARGB(70, 97, 95, 95);
		barraZD.setARGB(100, 97, 95, 95);
		barraA.setARGB(255, 119, 115, 115);
		barraAI.setARGB(70, 119, 115, 115);
		barraAD.setARGB(100, 119, 115, 115);
		// car.setStyle(Paint.Style.STROKE);
		// car.setStrokeWidth(5);
		touch.setARGB(120, 100, 100, 250);// setColor(Color.DKGRAY);
		touch.setAntiAlias(true);
		touch.setStrokeWidth(2);
		touch.setStrokeCap(Paint.Cap.ROUND);
		touch.setStyle(Paint.Style.STROKE);
	}
	
	public void draw(SurfaceHolder ejezHolder, boolean enMovimiento) {
		this.canvas = ejezHolder.lockCanvas();
		this.Width = this.canvas.getWidth();
		this.Height = this.canvas.getHeight();
		this.dx = (float) this.Width / 1000;
		this.dy = (float) this.Height / 1000;
		barraA.setStrokeWidth(40 * dy);
		barraAI.setStrokeWidth(40 * dy);
		barraAD.setStrokeWidth(40 * dy);
		this.canvas.drawColor(Color.BLACK);
		drawZ(this.xInicial, this.yInicial, gruPaint, carI, barraZI, barraAI);
		if(!enMovimiento) {
			drawZt(gruPaint, carPaint, barraZ, barraA);
		}else {
			drawZ(this.xDeseado, this.yDeseado, gruD, carD, barraZD, barraAD);
			drawZ(this.xActual, this.yActual, gruPaint, carPaint, barraZ, barraA);
		}
//		drawtouch(canvas);
		ejezHolder.unlockCanvasAndPost(this.canvas);
	}
	
	public void drawZ(float x, float y, Paint grua,
			Paint carr, Paint barraz, Paint barraa) {
		cent = this.dx * 300;
		float wPat = this.dx * 300;
		float hPat = this.dy * 100;
		float wCar = this.dx * 100;
		float bCar = this.dx * 150;
		float hCar = this.dy * 200;
		float wZ = this.dx * 50;
		lw = this.dx * 600;
//		float aw = this.dy * 20;
		this.limX = dx * 100;
		this.limY = this.Height - hPat - lw;
		if (x <= cent)
			x = cent;
		if (x >= this.Width - this.limX)
			x = this.Width - this.limX;
		float adyacente = x - cent;
		float ang = (float)Math.acos(adyacente / lw);
		float opuesto = (float)Math.sin(ang)*lw;
		if (y - opuesto <= hCar) {
			y = hCar + opuesto;
		}
		if (y - opuesto >= this.limY) {
			y = this.limY + opuesto;
		}
		this.ez = (float)(limY - minY) / 1000;
		this.canvas.drawRect(0, this.Height - hPat, cent + wPat, this.Height, grua);
		this.canvas.drawRect(cent - wCar, 0, cent + wCar, this.Height, grua);
		this.canvas.drawRect(cent - bCar, 0, cent + bCar, hCar, carr);
		this.canvas.drawRect(cent - wZ, hCar, cent + wZ, y - opuesto, barraz);
		this.canvas.drawLine(cent, y - opuesto, x, y, barraa);
	}
	
	public void drawZt(Paint grua, Paint carr, Paint barraz, Paint barraa) {
//		cent = this.dx * 300;
		float wPat = this.dx * 300;
		float hPat = this.dy * 100;
		float wCar = this.dx * 100;
		float bCar = this.dx * 150;
		float hCar = this.dy * 200;
		float wZ = this.dx * 50;
//		lw = this.dx * 600;
//		float aw = this.dy * 20;
		this.limX = dx * 100;
		this.minY = hCar;
		this.limY = this.Height - hPat - lw;
		if (xTouch <= cent)
			xTouch = cent;
		if (xTouch >= this.Width - this.limX)
			xTouch = this.Width - this.limX;
		float adyacente = xTouch - cent;
		float ang = (float)Math.acos(adyacente / lw);
		angd = (float)Math.toDegrees(ang);
		float opuesto = (float)Math.sin(ang)*lw;
		if (yTouch - opuesto <= minY) {
			yTouch = minY + opuesto;
		}
		if (yTouch - opuesto >= this.limY) {
			yTouch = this.limY + opuesto;
		}
		this.ez = (float)(limY - minY) / 1000;
		this.canvas.drawRect(0, this.Height - hPat, cent + wPat, this.Height, grua);
		this.canvas.drawRect(cent - wCar, 0, cent + wCar, this.Height, grua);
		this.canvas.drawRect(cent - bCar, 0, cent + bCar, hCar, carr);
		this.canvas.drawRect(cent - wZ, hCar, cent + wZ, yTouch - opuesto, barraz);
		this.canvas.drawLine(cent, yTouch - opuesto, xTouch, yTouch, barraa);
//		this.canvas.drawText("A = "+Float.toString(angd), 10, 50, gruPaint);
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
