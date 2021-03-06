package es.upm.etsisi.symderivation.arith;

import java.util.HashMap;

import es.upm.etsisi.symderivation.SymFunction;
import es.upm.etsisi.symderivation.constant.SymZero;

/**
 * Additive inverse of a symbolic function.
 * It is an unary operand.
 * 
 * Token notation: --.
 * 
 * @author Angel Gonzalez-Prieto
 *
 */
public class SymUnaryMinus extends SymFunction {
	SymFunction arg;
	
	public SymUnaryMinus(SymFunction arg) {
		this.arg=arg;
	}

	public Double eval(HashMap<String, Double> param) {
		Double argValue = arg.eval(param);
		
		if(argValue == null) {
			return null;
		}
		
		if(argValue.isNaN()) {
			return Double.NaN;
		}else {
			return -argValue;
		}
	}

	public SymFunction diff(String var) {
		SymFunction dif = arg.diff(var);
		if(dif instanceof SymZero) {
			return new SymZero();
		}
		
		return new SymUnaryMinus(dif);
	}

	@Override
	public String toInfix() {
		return "-(" + arg.toInfix() + ")";
	}
	
	@Override
	public String toJavaCode() {
		return "-(" + arg.toJavaCode() + ")";
	}

	@Override
	public int getDepth() {
		return arg.getDepth()+1;
	}

}
