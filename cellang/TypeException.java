public class TypeException extends RuntimeException
{
	// TODO(afb) :: Actually do and not copy paste
	public TypeException() {
		super("TypeException: INCOMPATIBLE METHOD");
    }

    public TypeException(String Type) {
		super("TypeException: Incompatible method for " + Type);
    }

    public TypeException(Throwable cause) {
		super("TypeException: INCOMPATIBLE METHOD", cause);
    }

}
