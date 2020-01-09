package fgenejfx.models.enums;

public enum OpEnum {

	SUM,
	SUBTRACT;

	public static boolean isSum(OpEnum op){
		return op.equals(OpEnum.SUM);
	}

}
