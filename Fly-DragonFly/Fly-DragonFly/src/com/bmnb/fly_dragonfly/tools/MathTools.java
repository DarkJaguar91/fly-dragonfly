package com.bmnb.fly_dragonfly.tools;

public class MathTools {
	public static float [] interp(float [] orig, float [] target, float start [], int percent){
		float [] out = new float[orig.length];
		for (int i = 0; i < orig.length; ++i){
			float amount = (Math.abs(target[i] - start[i]) * (float)(percent/100f));
			
			if (orig[i] < target[i]){			
				out[i] = (orig[i] + amount) > target[i] ? target[i] : (orig[i] + amount);
			}
			else {
				out[i] = (orig[i] - amount) < target[i] ? target[i] : (orig[i] - amount);
			}
		}		
		return out;
	}
}
