package com.example.shizhuan.banche.util;

/**  
 * 文件名称：TTS.java
 * 类说明：  
 */
public interface TTS {
	public void init();
	public void playText(String playText);
	public void stopSpeak();
	public void destroy();
	public boolean isPlaying();
	public void setCallback(ICallBack callback);
}
