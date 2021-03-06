package es.upm.etsisi.symderivation.arith;

import java.util.HashMap;

import es.upm.etsisi.symderivation.SymFunction;
import es.upm.etsisi.symderivation.constant.SymOne;
import es.upm.etsisi.symderivation.constant.SymZero;
import es.upm.etsisi.symderivation.trascendent.SymLog;

/**
 * Pow function. Any symbolic function is
 * accepted as exponent.
 * 
 * Token notation: pow.
 * 
 * @author Angel Gonzalez-Prieto
 *
 */
public class SymPow extends SymFunction {
	private SymFunction base;
	private SymFunction pow;
	
	private static double THRESHOLD = 10e-15;

		
	public SymPow(SymFunction arg, SymFunction pow) {
		this.base= arg;
		this.pow = pow;
	}

	public Double eval(HashMap<String, Double> param) {
		Double argValue;
		Double powValue;
		
		if(pow instanceof SymZero) {
			return 1.0;
		}else if(pow instanceof SymOne) {
			return base.eval(param);
		}
		
		argValue = base.eval(param);
		powValue = pow.eval(param);
		
		if(powValue == null || argValue == null) {
			return null;
		}
		
		if(powValue == 0) {
			return 1.0;
		}else if(argValue.isNaN() || powValue.isNaN() ||
				(powValue <0 && Math.abs(argValue)<THRESHOLD)) {
			return Double.NaN;
		}else {
			return Math.pow(argValue, powValue);
		}
	}

	public SymFunction diff(String var) {
		if(pow instanceof SymZero) {
			return new SymZero();
		}else if(pow instanceof SymOne) {
			return base.diff(var);
		}
		
		SymFunction difbase = base.diff(var);
		SymFunction difexp = pow.diff(var);
		
		if(difexp instanceof SymZero) {
			return new SymProd(new SymProd(pow, difbase), new SymPow(base, new SymSubs(pow, new SymOne())));
		}else if(difbase instanceof SymZero) {
			return new SymProd(new SymProd(difexp, new SymLog(base)), new SymPow(base, pow));
		}
		
		return new SymSum(new SymProd(new SymProd(difexp, new SymLog(base)), new SymPow(base, pow)),
				new SymProd(new SymProd(pow, difbase), new SymPow(base, new SymSubs(pow, new SymOne()))));
	}

	@Override
	public String toInfix() {
		return "(" + base.toInfix() + ")^(" + pow.toInfix() + ")";
	}

	@Override
	public String toJavaCode() {
		return "Math.pow(" + base.toJavaCode() + "," + pow.toJavaCode() + ")";
	}

	@Override
	public int getDepth() {
		return Math.max(base.getDepth(), pow.getDepth())+1;
	}

}
