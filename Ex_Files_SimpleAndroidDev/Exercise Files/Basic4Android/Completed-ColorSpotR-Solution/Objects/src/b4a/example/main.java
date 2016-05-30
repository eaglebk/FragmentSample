package b4a.example;

import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
		BA.handler.postDelayed(new WaitForLayout(), 5);

	}
	private static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        anywheresoftware.b4a.keywords.Common.ToastMessageShow("This application was developed with Basic4android trial version and should not be distributed.", true);
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
		return true;
	}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}

public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _button7 = null;
public static int _count = 0;
public static int[] _states = null;
public static int[] _defaultcolors = null;
public static int[] _selectedcolors = null;
public anywheresoftware.b4a.objects.ButtonWrapper[] _buttons = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 44;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
mostCurrent._activity.LoadLayout("Layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 45;BA.debugLine="InitializeButtons";
_initializebuttons();
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 106;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 108;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 102;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 104;BA.debugLine="End Sub";
return "";
}
public static String  _button1_click() throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Button1_Click";
 //BA.debugLineNum = 128;BA.debugLine="ProcessButton(0)";
_processbutton((int) (0));
 //BA.debugLineNum = 129;BA.debugLine="End Sub";
return "";
}
public static String  _button2_click() throws Exception{
 //BA.debugLineNum = 124;BA.debugLine="Sub Button2_Click";
 //BA.debugLineNum = 125;BA.debugLine="ProcessButton(1)";
_processbutton((int) (1));
 //BA.debugLineNum = 126;BA.debugLine="End Sub";
return "";
}
public static String  _button3_click() throws Exception{
 //BA.debugLineNum = 121;BA.debugLine="Sub Button3_Click";
 //BA.debugLineNum = 122;BA.debugLine="ProcessButton(2)";
_processbutton((int) (2));
 //BA.debugLineNum = 123;BA.debugLine="End Sub";
return "";
}
public static String  _button4_click() throws Exception{
 //BA.debugLineNum = 118;BA.debugLine="Sub Button4_Click";
 //BA.debugLineNum = 119;BA.debugLine="ProcessButton(3)";
_processbutton((int) (3));
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _button5_click() throws Exception{
 //BA.debugLineNum = 115;BA.debugLine="Sub Button5_Click";
 //BA.debugLineNum = 116;BA.debugLine="ProcessButton(4)";
_processbutton((int) (4));
 //BA.debugLineNum = 117;BA.debugLine="End Sub";
return "";
}
public static String  _button6_click() throws Exception{
 //BA.debugLineNum = 112;BA.debugLine="Sub Button6_Click";
 //BA.debugLineNum = 113;BA.debugLine="ProcessButton(5)";
_processbutton((int) (5));
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return "";
}
public static String  _button7_click() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub Button7_Click";
 //BA.debugLineNum = 131;BA.debugLine="InitializeButtons";
_initializebuttons();
 //BA.debugLineNum = 132;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private Button1 As Button";
mostCurrent._button1 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private Button2 As Button";
mostCurrent._button2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private Button3 As Button";
mostCurrent._button3 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private Button4 As Button";
mostCurrent._button4 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private Button5 As Button";
mostCurrent._button5 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private Button6 As Button";
mostCurrent._button6 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 31;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private Button7 As Button";
mostCurrent._button7 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private Count As Int";
_count = 0;
 //BA.debugLineNum = 35;BA.debugLine="Private States(6) As Int";
_states = new int[(int) (6)];
;
 //BA.debugLineNum = 36;BA.debugLine="Private DefaultColors(6) As Int";
_defaultcolors = new int[(int) (6)];
;
 //BA.debugLineNum = 37;BA.debugLine="Private SelectedColors(6) As Int";
_selectedcolors = new int[(int) (6)];
;
 //BA.debugLineNum = 38;BA.debugLine="Private Buttons(6) As Button";
mostCurrent._buttons = new anywheresoftware.b4a.objects.ButtonWrapper[(int) (6)];
{
int d0 = mostCurrent._buttons.length;
for (int i0 = 0;i0 < d0;i0++) {
mostCurrent._buttons[i0] = new anywheresoftware.b4a.objects.ButtonWrapper();
}
}
;
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}
public static String  _initializebuttons() throws Exception{
int _i = 0;
 //BA.debugLineNum = 48;BA.debugLine="Sub InitializeButtons";
 //BA.debugLineNum = 49;BA.debugLine="DefaultColors(0) = Colors.ARGB(255, 255, 0, 0) 'Red";
_defaultcolors[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (0),(int) (0));
 //BA.debugLineNum = 50;BA.debugLine="DefaultColors(1) = Colors.ARGB(255, 0, 255, 0) 'Green";
_defaultcolors[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (255),(int) (0));
 //BA.debugLineNum = 51;BA.debugLine="DefaultColors(2) = Colors.ARGB(255, 0, 0, 255) 'Blue";
_defaultcolors[(int) (2)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (0),(int) (255));
 //BA.debugLineNum = 52;BA.debugLine="DefaultColors(3) = Colors.ARGB(255, 255, 255, 0) 'Yellow";
_defaultcolors[(int) (3)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (255),(int) (0));
 //BA.debugLineNum = 53;BA.debugLine="DefaultColors(4) = Colors.ARGB(255, 255, 0, 255) 'Purple";
_defaultcolors[(int) (4)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (255),(int) (0),(int) (255));
 //BA.debugLineNum = 54;BA.debugLine="DefaultColors(5) = Colors.ARGB(255, 0, 255, 255) 'Cyan";
_defaultcolors[(int) (5)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (255),(int) (255));
 //BA.debugLineNum = 56;BA.debugLine="SelectedColors(0) = Colors.ARGB(255, 128, 0, 0) 'Red";
_selectedcolors[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (128),(int) (0),(int) (0));
 //BA.debugLineNum = 57;BA.debugLine="SelectedColors(1) = Colors.ARGB(255, 0, 128, 0) 'Green";
_selectedcolors[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (128),(int) (0));
 //BA.debugLineNum = 58;BA.debugLine="SelectedColors(2) = Colors.ARGB(255, 0, 0, 128) 'Blue";
_selectedcolors[(int) (2)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (0),(int) (128));
 //BA.debugLineNum = 59;BA.debugLine="SelectedColors(3) = Colors.ARGB(255, 128, 128, 0) 'Yellow";
_selectedcolors[(int) (3)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (128),(int) (128),(int) (0));
 //BA.debugLineNum = 60;BA.debugLine="SelectedColors(4) = Colors.ARGB(255, 128, 0, 128) 'Purple";
_selectedcolors[(int) (4)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (128),(int) (0),(int) (128));
 //BA.debugLineNum = 61;BA.debugLine="SelectedColors(5) = Colors.ARGB(255, 0, 128, 128) 'Cyan";
_selectedcolors[(int) (5)] = anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (128),(int) (128));
 //BA.debugLineNum = 63;BA.debugLine="Buttons(0) = Button1";
mostCurrent._buttons[(int) (0)] = mostCurrent._button1;
 //BA.debugLineNum = 64;BA.debugLine="Buttons(1) = Button2";
mostCurrent._buttons[(int) (1)] = mostCurrent._button2;
 //BA.debugLineNum = 65;BA.debugLine="Buttons(2) = Button3";
mostCurrent._buttons[(int) (2)] = mostCurrent._button3;
 //BA.debugLineNum = 66;BA.debugLine="Buttons(3) = Button4";
mostCurrent._buttons[(int) (3)] = mostCurrent._button4;
 //BA.debugLineNum = 67;BA.debugLine="Buttons(4) = Button5";
mostCurrent._buttons[(int) (4)] = mostCurrent._button5;
 //BA.debugLineNum = 68;BA.debugLine="Buttons(5) = Button6";
mostCurrent._buttons[(int) (5)] = mostCurrent._button6;
 //BA.debugLineNum = 70;BA.debugLine="Count = 0";
_count = (int) (0);
 //BA.debugLineNum = 72;BA.debugLine="For i = 0 To 5";
{
final int step41 = 1;
final int limit41 = (int) (5);
for (_i = (int) (0); (step41 > 0 && _i <= limit41) || (step41 < 0 && _i >= limit41); _i = ((int)(0 + _i + step41))) {
 //BA.debugLineNum = 73;BA.debugLine="States(i) = 0";
_states[_i] = (int) (0);
 //BA.debugLineNum = 74;BA.debugLine="Buttons(i).Color = DefaultColors(i)";
mostCurrent._buttons[_i].setColor(_defaultcolors[_i]);
 }
};
 //BA.debugLineNum = 77;BA.debugLine="Label1.Visible = False";
mostCurrent._label1.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 78;BA.debugLine="Button7.Visible = False";
mostCurrent._button7.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _processbutton(int _index) throws Exception{
 //BA.debugLineNum = 81;BA.debugLine="Sub ProcessButton(index As Int)";
 //BA.debugLineNum = 82;BA.debugLine="If (States(index) = 0) Then";
if ((_states[_index]==0)) { 
 //BA.debugLineNum = 83;BA.debugLine="Buttons(index).Color = SelectedColors(index)";
mostCurrent._buttons[_index].setColor(_selectedcolors[_index]);
 //BA.debugLineNum = 84;BA.debugLine="Count = Count + 1";
_count = (int) (_count+1);
 //BA.debugLineNum = 85;BA.debugLine="States(index) = 1";
_states[_index] = (int) (1);
 }else {
 //BA.debugLineNum = 87;BA.debugLine="Buttons(index).Color = DefaultColors(index)";
mostCurrent._buttons[_index].setColor(_defaultcolors[_index]);
 //BA.debugLineNum = 88;BA.debugLine="Count = Count -1";
_count = (int) (_count-1);
 //BA.debugLineNum = 89;BA.debugLine="States(index) = 0";
_states[_index] = (int) (0);
 };
 //BA.debugLineNum = 92;BA.debugLine="If Count = 6 Then";
if (_count==6) { 
 //BA.debugLineNum = 93;BA.debugLine="Label1.Text = \"You Win!\"";
mostCurrent._label1.setText((Object)("You Win!"));
 //BA.debugLineNum = 94;BA.debugLine="Label1.Visible = True";
mostCurrent._label1.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 95;BA.debugLine="Button7.Visible = True";
mostCurrent._button7.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 97;BA.debugLine="End Sub";
return "";
}
}
