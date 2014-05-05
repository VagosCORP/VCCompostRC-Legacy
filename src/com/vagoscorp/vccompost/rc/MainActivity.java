package com.vagoscorp.vccompost.rc;

import libraries.vagoscorp.comunication.Eventos.OnComunicationListener;
import libraries.vagoscorp.comunication.Eventos.OnConnectionListener;
import libraries.vagoscorp.comunication.android.Comunic;
import libraries.vagoscorp.comunication.android.TimeOut;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class MainActivity extends Activity implements Runnable, OnComunicationListener {

//	TextView estado;
	SurfaceView grua;
	SurfaceView ejez;
	SurfaceHolder gruHolder;
	SurfaceHolder ejezHolder;
	Thread thread;
	Comunic comunic;
	TimeOut timeout;
	int rutCont = 0;
	int[] rutY = {0   , 0   , 0   , 0   , 0   , 0   , 0   , 0   , 0   , 0   , 0   , 0   , 0};
	int[] rutX = {1000, 1000, 0   , 0   , 1000, 1000, 0   , 0   , 1000, 1000, 0   , 0   , 0};
	int[] rutZ = {0   , 0   , 0   , 333 , 333 , 666 , 666 , 1000, 1000, 1000, 1000, 1000, 0};
	int[] rutW = {0   , 60  , 60  , 60  , 60  , 70  , 70  , 80  , 80  , 90  , 90  , 0   , 0};
	Grua gru;
	EjeZ ejeZ;
	boolean run = false;
	boolean enMovimiento = false;
	
	public static final String SI = "SIP";
	public static final String SP = "SPort";
	public static final String defIP = "10.0.0.10";
	public static final int defPort = 2000;

	public String serverip;// IP to Connect
	public int serverport;// Port to Connect
	String tarea = "";
	Thread timeOut;
	boolean run_to = false;
	boolean datoRCV = false;
	boolean fCon = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		estado = (TextView) findViewById(R.id.estado);
		grua = (SurfaceView) findViewById(R.id.grua);
		ejez = (SurfaceView) findViewById(R.id.ejeZ);
		gruHolder = grua.getHolder();
		ejezHolder = ejez.getHolder();
		serverip = defIP;
		serverport = defPort;
		comunic = new Comunic();
		gru = new Grua();
		ejeZ = new EjeZ(); 
		grua.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				gru.xTouch = event.getX();
//				gru.yTouch = event.getY();
				return true;
			}
		});
		ejez.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ejeZ.xTouch = event.getX();
				ejeZ.yTouch = event.getY();
				return true;
			}
		});
	}
	
	@Override
	protected void onStart() {
		SharedPreferences shapre = getPreferences(MODE_PRIVATE);
		serverip = shapre.getString(SI, defIP);
		serverport = shapre.getInt(SP, defPort);
		super.onStart();
	}

	@Override
	public void onPause() {
		Log.i("F_Position", "onPause");
		run = false;
		try {
			if(thread != null)
				thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread = null;
		// ontouch = false;
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.i("F_Position", "onResume");
		// upd vars
		// ontouch = false;
		if(!run) {
			run = true;
			thread = new Thread(this);
			thread.start();
		}
		super.onResume();
	}
	
	@Override
	public void onBackPressed() {
//		Toast.makeText(this, "Cierre la APP desde el menú", Toast.LENGTH_SHORT).show();
//		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		Log.i("RC", "onDestroy");
		comunic.Detener_Actividad();
		super.onDestroy();
	}

	@Override
	public void run() {
		while (run) {
			if (!gruHolder.getSurface().isValid() || !ejezHolder.getSurface().isValid())
				continue;
			gru.draw(gruHolder, enMovimiento);
			ejeZ.draw(ejezHolder, enMovimiento);
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Chan_Ser() {
		Intent CS = new Intent(this, Set_Server.class);
		CS.putExtra(SI, serverip);
		CS.putExtra(SP, serverport);
		startActivityForResult(CS, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 1) {
			serverip = data.getStringExtra(Set_Server.NSI);
			serverport = data.getIntExtra(Set_Server.NSP, defPort);
			SharedPreferences shapre = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = shapre.edit();
			editor.putString(SI, serverip);
			editor.putInt(SP, serverport);
			editor.commit();
		}
	}
	
//	private void timeOut_init() {
//		run_to = true;
//		timeOut = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				while(run_to) {
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					if(!datoRCV) {
//						comunic.Detener_Actividad();
//					}else {
//						datoRCV = false;
//					}
//				}
//			}
//		});
//		timeOut.start();
//	}
//	
//	private void timeOut_end() {
//		run_to = false;
//		try {
//			timeOut.join();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		timeOut = null;
//	}
	
	public void iniciar_Rutina() {
//		if(rutCont <= 4)
		final float xval = (float)rutX[rutCont];
		final float yval = (float)rutY[rutCont];
		final float wval = (float)rutW[rutCont];
		final float zval = (float)rutZ[rutCont];
		
//		float adyacente = ejeZ.xTouch - ejeZ.cent;
//		float ang = (float)Math.acos(adyacente / ejeZ.lw);
//		float opuesto = (float)Math.sin(ang)*ejeZ.lw;
//		float angd = (float)Math.toDegrees(ang);
		enMovimiento = true;
		if (comunic == null || comunic.estado != comunic.CONNECTED) {
			if(comunic != null)
				comunic.Detener_Actividad();
			comunic = new Comunic(this, serverip, serverport);
			comunic.setConnectionListener(new OnConnectionListener() {
				@Override
				public void onConnectionstablished() {
					Log.d("Client", "Conexión Establecida");
					
					comunic.enviar("X=" + Float.toString(xval) +
								  "=Y=" + Float.toString(yval) +
								  "=Z=" + Float.toString(zval) +
								  "=A=" + Float.toString(wval) + "=/");
					fCon = true;
					datoRCV = true;
//					timeOut_init();
				}

				@Override
				public void onConnectionfinished() {
					Log.d("Client", "Conexión Finalizada");
					if(fCon) {
//						timeOut_end();
						fCon = false;
					}
					if(enMovimiento) {
						detenerMovimiento();
					}
					if(rutCont < 13) {
						iniciar_Rutina();
						rutCont++;
					}else {
						rutCont = 0;
					}
					
				}
			});
			comunic.setComunicationListener(this);
			comunic.execute();
		}
	}

	public void iniciar_Cliente() {
		final float xval = (float)((float)gru.xDeseado - gru.limX) / gru.ex;
		final float yval = (float)((float)gru.yDeseado - gru.limY) / gru.ey;
		final float wval = (float)ejeZ.angd;
		final float zval = (float)(ejeZ.yTouch - (float)Math.sin(Math.toRadians(wval))*ejeZ.lw - ejeZ.minY) / ejeZ.ez;
//		float adyacente = ejeZ.xTouch - ejeZ.cent;
//		float ang = (float)Math.acos(adyacente / ejeZ.lw);
//		float opuesto = (float)Math.sin(ang)*ejeZ.lw;
//		float angd = (float)Math.toDegrees(ang);
		if (comunic == null || comunic.estado != comunic.CONNECTED) {
			if(comunic != null)
				comunic.Detener_Actividad();
			comunic = new Comunic(this, serverip, serverport);
			comunic.setConnectionListener(new OnConnectionListener() {
				@Override
				public void onConnectionstablished() {
					Log.d("Client", "Conexión Establecida");
					
					comunic.enviar("X=" + Float.toString(xval) +
								  "=Y=" + Float.toString(yval) +
								  "=Z=" + Float.toString(zval) +
								  "=A=" + Float.toString(wval) + "=/");
					fCon = true;
					datoRCV = true;
//					timeOut_init();
				}

				@Override
				public void onConnectionfinished() {
					Log.d("Client", "Conexión Finalizada");
					if(fCon) {
//						timeOut_end();
						fCon = false;
					}
					if(enMovimiento)
						detenerMovimiento();
					
				}
			});
			comunic.setComunicationListener(this);
			comunic.execute();
		}
	}

	@Override
	public void onDataReceived(String dato) {// IL Dato Recibido
		tarea += dato;
		if (dato.endsWith("/")) {
//			timeOut_end();
			datoRCV = true;
			Log.d("MainActivity", tarea);
//			Toast.makeText(this, tarea, Toast.LENGTH_LONG).show();
			String[] vals = tarea.split("=");
			onPause();
			gru.xActual = (float)(gru.ex*Float.parseFloat(vals[1])) + gru.limX;
			gru.yActual = (float)(gru.ey*Float.parseFloat(vals[3])) + gru.limY;
			float wActual = (float)Math.toRadians(Float.parseFloat(vals[7]));
			ejeZ.yActual = (float)(ejeZ.ez*Float.parseFloat(vals[5])) + ejeZ.minY + (float)Math.sin(wActual)*ejeZ.lw;
			ejeZ.xActual = (float)Math.cos(wActual)*ejeZ.lw + ejeZ.cent;
			onResume();
			tarea = "";
//			timeOut_init();
		}
	}
	
	private void iniciarMovimiento() {
		Toast.makeText(this, "Movimiento Iniciado", Toast.LENGTH_SHORT).show();
		onPause();
		enMovimiento = true;
		gru.xActual = gru.xInicial;
		gru.yActual = gru.yInicial;
		gru.xDeseado = gru.xTouch;
		gru.yDeseado = gru.yTouch;
		gru.xActual = gru.xInicial;
		gru.yActual = gru.yInicial;
		gru.xDeseado = gru.xTouch;
		gru.yDeseado = gru.yTouch;
		
		ejeZ.xActual = ejeZ.xInicial;
		ejeZ.yActual = ejeZ.yInicial;
		ejeZ.xDeseado = ejeZ.xTouch;
		ejeZ.yDeseado = ejeZ.yTouch;
		ejeZ.xActual = ejeZ.xInicial;
		ejeZ.yActual = ejeZ.yInicial;
		ejeZ.xDeseado = ejeZ.xTouch;
		ejeZ.yDeseado = ejeZ.yTouch;
		
		iniciar_Cliente();
		onResume();
	}

	private void detenerMovimiento() {
		Toast.makeText(this, "Movimiento Detenido", Toast.LENGTH_SHORT).show();
		onPause();
		enMovimiento = false;
		comunic.Detener_Actividad();
		gru.xInicial = gru.xActual;
		gru.yInicial = gru.yActual;
		gru.xTouch = gru.xActual;
		gru.yTouch = gru.yActual;
		gru.xDeseado = gru.xActual;
		gru.yDeseado = gru.yActual;
		
		ejeZ.xInicial = ejeZ.xActual;
		ejeZ.yInicial = ejeZ.yActual;
		ejeZ.xTouch = ejeZ.xActual;
		ejeZ.yTouch = ejeZ.yActual;
		ejeZ.xDeseado = ejeZ.xActual;
		ejeZ.yDeseado = ejeZ.yActual;
		
		onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.Mover) {
			iniciarMovimiento();
			return true;
		} else if (id == R.id.Detener) {
			detenerMovimiento();
			return true;
		}else if (id == R.id.chanServer) {
			Chan_Ser();
			return true;
		}else if (id == R.id.Rutina) {
			iniciar_Rutina();
			return true;
		}else if (id == R.id.exit) {
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	public void gruDrawing() {
//	gru.canvas = gruHolder.lockCanvas();
//	gru.Width = gru.canvas.getWidth();
//	gru.Height = gru.canvas.getHeight();
//	gru.dx = (float) gru.Width / 1000;
//	gru.dy = (float) gru.Height / 1000;
//	gru.canvas.drawColor(Color.BLACK);
//	drawGru(gru.xInicial, gru.yInicial, gruI, carI);
//	if (!enMovimiento)
//		drawGru(gru.xTouch, gru.yTouch, gruPaint, carPaint);
//	else {
//		drawGru(gru.xDeseado, gru.yDeseado, gruD, carD);
//		drawGru(gru.xActual, gru.yActual, gruPaint, carPaint);
//	}
//	// drawtouch(canvas);
//	gruHolder.unlockCanvasAndPost(gru.canvas);
//}
//	
//	public void drawGru(/* Canvas canvas */float x, float y, Paint grua,
//			Paint carr) {
//		float wPat = gru.dx * 70;
//		float hPat = gru.dy * 250;
//		float wCar = gru.dx * 100;
//		float hCar = gru.dy * 150;
//		float hGru = gru.dy * 100;
//		gru.limX = wPat + wCar;
//		gru.limY = hPat;
//		gru.ex = (float)(gru.Width - (float)2*gru.limX) / 1000;
//		gru.ey = (float)(gru.Height - (float)2*gru.limY) / 1000;
//		float mwPat = wPat / 2;
//		if (x <= gru.limX)
//			x = gru.limX;
//		if (y <= hPat)
//			y = hPat;
//		if (x >= gru.Width - gru.limX)
//			x = gru.Width - gru.limX;
//		if (y >= gru.Height - hPat)
//			y = gru.Height - hPat;
//		gru.canvas.drawLine(wPat, 0, wPat, gru.Height, gruPaint);
//		gru.canvas.drawLine(mwPat, 0, mwPat, gru.Height, gruPaint);
//		gru.canvas.drawLine(0, 0, 0, gru.Height, gruPaint);
//		gru.canvas.drawLine(gru.Width - wPat, 0, gru.Width - wPat, gru.Height, gruPaint);
//		gru.canvas.drawLine(gru.Width - mwPat, 0, gru.Width - mwPat, gru.Height, gruPaint);
//		gru.canvas.drawLine(gru.Width - 1, 0, gru.Width - 1, gru.Height, gruPaint);
//		gru.canvas.drawRect(0, y - hPat, wPat, y + hPat, grua);
//		gru.canvas.drawRect(gru.Width - wPat, y - hPat, gru.Width, y + hPat, grua);
//		gru.canvas.drawRect(0, y - hGru, gru.Width, y + hGru, grua);
//		gru.canvas.drawRect(x - wCar, y - hCar, x + wCar, y + hCar, carr);
//	}
//	
//	public void drawGruv(/* Canvas canvas */float x, float y, Paint grua,
//			Paint carr) {
//		float wPat = gru.dx * 100;
//		float hPat = gru.dy * 100;
//		float wCar = gru.dx * 60;
//		float hCar = gru.dy * 130;
//		float wGru = gru.dx * 35;
//		gru.limX = hPat;
//		gru.limY = hPat + hCar;
//		gru.ex = (float)(gru.Width - (float)2*gru.limX) / 1000;
//		gru.ey = (float)(gru.Height - (float)2*gru.limY) / 1000;
//		float mhPat = hPat / 2;
//		if (x <= wPat)
//			x = wPat;
//		if (y <= gru.limY)
//			y = gru.limY;
//		if (x >= gru.Width - wPat)
//			x = gru.Width - wPat;
//		if (y >= gru.Height - gru.limY)
//			y = gru.Height - gru.limY;
//		gru.canvas.drawLine(0, hPat, gru.Width, hPat, gruPaint);
//		gru.canvas.drawLine(0, mhPat, gru.Width, mhPat, gruPaint);
//		gru.canvas.drawLine(0, 0, gru.Width, 0, gruPaint);
//		gru.canvas.drawLine(0, gru.Height - hPat, gru.Width, gru.Height - hPat, gruPaint);
//		gru.canvas.drawLine(0, gru.Height - mhPat, gru.Width, gru.Height - mhPat, gruPaint);
//		gru.canvas.drawLine(0, gru.Height - 1, gru.Width, gru.Height - 1, gruPaint);
//		gru.canvas.drawRect(x - wPat, 0, x + wPat, hPat, grua);
//		gru.canvas.drawRect(x - wPat, gru.Height - hPat, x + wPat, gru.Height, grua);
//		gru.canvas.drawRect(x - wGru, 0, x + wGru, gru.Height, grua);
//		gru.canvas.drawRect(x - wCar, y - hCar, x + wCar, y + hCar, carr);
//	}
//	
//	public void zDrawing() {
//		ejeZ.canvas = ejezHolder.lockCanvas();
//		ejeZ.Width = ejeZ.canvas.getWidth();
//		ejeZ.Height = ejeZ.canvas.getHeight();
//		ejeZ.canvas.drawColor(Color.BLACK);
//		drawZ(gru.xInicial, gru.yInicial, gruPaint, carPaint);
//		if(!enMovimiento) {
//			
//		}else {
//			
//		}
//		ejezHolder.unlockCanvasAndPost(ejeZ.canvas);
//	}
//	
//	public void drawZ(float l, float w, Paint grua,
//			Paint carr) {
//		float wPat = ejeZ.dx * 70;
//		float hPat = ejeZ.dy * 250;
//		float wCar = ejeZ.dx * 100;
//		float hCar = ejeZ.dy * 150;
//		float hGru = ejeZ.dy * 100;
//		ejeZ.canvas.drawRect(0, ejeZ.Height - wPat, ejeZ.Width, ejeZ.Height, grua);
//		ejeZ.canvas.drawRect(ejeZ.Width/2 - wPat, 0, ejeZ.Width/2 + wPat, ejeZ.Height, grua);
//		ejeZ.canvas.drawRect(ejeZ.Width/2 - wCar, 0, ejeZ.Width/2 + wCar, hPat, carr);
//		ejeZ.canvas.drawRect(ejeZ.Width/2 - ejeZ.dx*30, hPat, ejeZ.Width/2 + ejeZ.dx*30, hPat + hPat, carr);
//	}

	// onCreate:
	// if (savedInstanceState == null) {
	// getFragmentManager().beginTransaction()
	// .add(R.id.container, new PlaceholderFragment()).commit();
	// }

	// /**
	// * A placeholder fragment containing a simple view.
	// */
	// public static class PlaceholderFragment extends Fragment {
	//
	// public PlaceholderFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_main, container,
	// false);
	// return rootView;
	// }
	// }

}
